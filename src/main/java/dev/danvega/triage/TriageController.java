package dev.danvega.triage;

import java.util.Map;

import org.springframework.ai.decision.DecisionModel;
import org.springframework.ai.decision.DecisionResponse;
import org.springframework.ai.decision.answer.ChoiceAnswer;
import org.springframework.ai.decision.answer.ScoreAnswer;
import org.springframework.ai.decision.answer.YesNoAnswer;
import org.springframework.ai.decision.question.ChoiceQuestion;
import org.springframework.ai.decision.question.ScoreQuestion;
import org.springframework.ai.decision.question.YesNoQuestion;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TriageController {

    // 1. Yes or no
    private static final YesNoQuestion URGENT =
            YesNoQuestion.of("Does this message need a reply today?");

    // 2. Pick one option
    private static final ChoiceQuestion TEAM =
            ChoiceQuestion.of("Which team should handle this message?", "Billing", "Technical", "Sales");

    // 3. A level on a scale, from lowest to highest
    private static final ScoreQuestion MOOD =
            ScoreQuestion.of("How does the customer feel?", "Unhappy", "Neutral", "Happy");

    private final DecisionModel decisionModel;

    public TriageController(DecisionModel decisionModel) {
        this.decisionModel = decisionModel;
    }

    @PostMapping("/triage")
    public Triage triage(@RequestBody Feedback feedback) {
        // One call to Jev answers all three questions
        DecisionResponse response = decisionModel.call(feedback.message(),
                Map.of("urgent", URGENT, "team", TEAM, "mood", MOOD));

        YesNoAnswer urgent = response.getYesNo("urgent");
        ChoiceAnswer team = response.getChoice("team");
        ScoreAnswer mood = response.getScore("mood");

        return new Triage(
                urgent.isYes(0.8),      // only "urgent" when Jev is at least 80% sure
                urgent.getProbability(),
                team.getValue(),
                MOOD.getLevelLabels().get(mood.getNearestLevel()));
    }

    public record Feedback(String message) {
    }

    public record Triage(boolean urgent, double urgentProbability, String team, String mood) {
    }
}
