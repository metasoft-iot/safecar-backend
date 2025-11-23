package com.safecar.platform.insights.infrastructure.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Insights Configuration
 * 
 * <p>
 * This class is a configuration class that provides the insights gemini client.
 * </p>
 */
@Configuration
public class InsightsConfiguration {

    /**
     * Insights Gemini Client
     * 
     * <p>
     * This method provides the insights gemini client.
     * </p>
     * 
     * @param apiKey the API key for the insights gemini client
     * @return the insights gemini client
     */
    @Bean(name = "insightsGeminiClient", destroyMethod = "close")
    public Client insightsGeminiClient(@Value("${insights.gemini.api-key:}") String apiKey) {
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}
