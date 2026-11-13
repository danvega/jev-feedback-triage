package dev.danvega.triage;

import org.junit.jupiter.api.Test;
import org.springframework.ai.decision.DecisionModel;
import org.springframework.ai.decision.DecisionResponse;
import org.springframework.ai.decision.answer.ChoiceAnswer;
import org.springframework.ai.decision.answer.ScoreAnswer;
import org.springframework.ai.decision.answer.YesNoAnswer;

import static org.assertj.core.api.Assertions.assertThat;

class TriageControllerTest {

    // A fake Jev, so the test runs without an API key
    DecisionModel fakeJev = prompt -> DecisionResponse.builder(prompt)
            .answer("urgent", YesNoAnswer.builder().probability(0.93).build())
            .answer("team", ChoiceAnswer.builder().value("Billing").build())
            .answer("mood", ScoreAnswer.builder().value(0).build())
            .build();

    TriageController controller = new TriageController(fakeJev);

    @Test
    void triagesAnAngryBillingMessage() {
        var triage = controller.triage(new TriageController.Feedback("I was charged twice!"));

        assertThat(triage.urgent()).isTrue();
        assertThat(triage.team()).isEqualTo("Billing");
        assertThat(triage.mood()).isEqualTo("Unhappy");
    }
}
