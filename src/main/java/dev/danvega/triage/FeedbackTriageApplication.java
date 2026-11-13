package dev.danvega.triage;

import org.springaicommunity.typesafe.TypeSafeClient;
import org.springframework.ai.decision.DecisionModel;
import org.springframework.ai.systemone.SystemOneDecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FeedbackTriageApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedbackTriageApplication.class, args);
    }

    // Jev has no Spring Boot starter yet, so we create the DecisionModel ourselves.
    @Bean
    DecisionModel decisionModel(@Value("${typesafe.api-key}") String apiKey) {
        TypeSafeClient client = TypeSafeClient.builder()
                .apiKey(apiKey)
                .build();
        return SystemOneDecisionModel.builder(client).build();
    }
}
