# Jev Feedback Triage

A customer message comes in, and the app decides what to do with it. Is it urgent? Which team should handle it? How does the customer feel?

It uses the Decision API that is new in Spring AI 2.1. You don't ask a chat model for text and then parse it. You list the possible answers, and the model picks one and tells you how sure it is. The model here is [TypeSafe Jev](https://docs.typesafe.ai/api).

## Tested with

Java 25, Spring Boot 4.2.0-RC2, Spring AI 2.1.0-RC2.

## What you need

- JDK 25 or later
- A TypeSafe API key

```bash
export TYPESAFE_API_KEY=your-key-here
```

## Run it

```bash
./mvnw spring-boot:run
```

The app starts on port 8080.

## Try it

Open [requests.http](requests.http) in IntelliJ and run the requests, or use curl:

```bash
curl -X POST http://localhost:8080/triage \
  -H "Content-Type: application/json" \
  -d '{"message": "I was charged twice this month and nobody has answered my emails. Fix this today or I am cancelling."}'
```

You get back something like this:

```json
{"urgent":true,"urgentProbability":0.94,"team":"Billing","mood":"Unhappy"}
```

The numbers change a little from run to run.

## The three kinds of questions

All three are asked in one call to Jev.

| Question | Type | What you get back |
| --- | --- | --- |
| Does this message need a reply today? | `YesNoQuestion` | The probability of yes |
| Which team should handle this message? | `ChoiceQuestion` | One of Billing, Technical or Sales |
| How does the customer feel? | `ScoreQuestion` | A level: Unhappy, Neutral or Happy |

A message only counts as urgent when Jev is at least 80% sure (`urgent.isYes(0.8)`). Try the "20 more seats" request in `requests.http`. Jev gives it about 0.75, so it is not marked urgent.

## Where to look

- [TriageController.java](src/main/java/dev/danvega/triage/TriageController.java) holds the three questions and the `/triage` endpoint.
- [FeedbackTriageApplication.java](src/main/java/dev/danvega/triage/FeedbackTriageApplication.java) creates the `DecisionModel` bean for Jev.
- [TriageControllerTest.java](src/test/java/dev/danvega/triage/TriageControllerTest.java) uses a fake Jev, so the test runs without an API key.

## Good to know

- Spring AI 2.1.0-RC2 is a release candidate, not GA yet.
- Jev comes from the `spring-ai-systemone` module. It has no Spring Boot starter, so the app builds the `DecisionModel` bean itself from a `TypeSafeClient`.
- Decision models are machine learning models, not rule engines. The same message can get slightly different probabilities each time.
- If Jev refuses to answer a question, `getYesNo`, `getChoice` and `getScore` throw an exception. `response.isRefused("urgent")` lets you check first.
