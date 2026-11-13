# AGENTS.md

This is a beginner demo of the Spring AI Decision API with TypeSafe Jev. People read this code to learn, so keep it small and easy to follow.

## Stack

- Java 25, Spring Boot 4.2.0-RC2, Spring AI 2.1.0-RC2
- Jev comes from `spring-ai-systemone`. It has no starter, so the `DecisionModel` bean is built by hand in `FeedbackTriageApplication`.
- Build with the Maven wrapper. If the default JDK is too old, set `JAVA_HOME` to a JDK 25 first.

## Commands

- Build and test: `./mvnw verify`
- Run: `./mvnw spring-boot:run` (needs `TYPESAFE_API_KEY`, port 8080)
- Try it: the requests in `requests.http`

## Rules

1. Keep it beginner friendly. Prefer plain code over clever code. No extra layers (services, mappers, interfaces) unless the demo needs them.
2. Keep the app to two main classes: `FeedbackTriageApplication` and `TriageController`. Ask before adding more.
3. Don't add dependencies without asking.
4. Only use Decision API classes and methods that exist in 2.1.0-RC2. Check the source jar if you are not sure. Don't guess from blog posts.
5. Config goes in `application.yaml`, not `.properties`.
6. Never commit an API key. Read keys from environment variables.
7. Tests must run without an API key. Use a fake `DecisionModel`, like the lambda in `TriageControllerTest`.
8. Writing (README, comments, commit messages): short sentences, plain words, no em dashes.
9. Commits: Dan is the only author. No `Co-Authored-By` or other attribution lines. Don't push.

## Definition of done

A change is done when all of these are true:

- `./mvnw verify` passes.
- New or changed behavior has a test.
- `README.md` and `requests.http` match the code (endpoints, properties, sample output).
- If `TYPESAFE_API_KEY` is set, you ran the app and sent the `requests.http` samples to the real Jev API. If it is not set, say that the live check was skipped.
- You told Dan what you changed and anything you could not check.
