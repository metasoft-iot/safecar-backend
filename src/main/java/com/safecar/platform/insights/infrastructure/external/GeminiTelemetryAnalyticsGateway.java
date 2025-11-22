package com.safecar.platform.insights.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.google.genai.Client;
import com.google.genai.types.Type;
import com.google.genai.types.Part;
import com.google.genai.types.Schema;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;

import com.safecar.platform.insights.domain.model.valueobjects.VehicleReference;
import com.safecar.platform.insights.domain.exceptions.TelemetryAnalysisException;
import com.safecar.platform.insights.domain.model.valueobjects.InsightRecommendation;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetryInsightResult;
import com.safecar.platform.insights.domain.model.valueobjects.TelemetrySensorPayload;
import com.safecar.platform.insights.application.internal.outboundservices.analytics.TelemetryAnalyticsGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;
import java.util.List;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.LinkedHashMap;

/**
 * Gemini Telemetry Analytics Gateway
 * 
 * <p>
 * This class implements the telemetry analytics gateway using the Gemini API.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class GeminiTelemetryAnalyticsGateway implements TelemetryAnalyticsGateway {

        private static final Logger LOGGER = LoggerFactory.getLogger(GeminiTelemetryAnalyticsGateway.class);
        private static final float DEFAULT_TEMPERATURE = 0.2f;

        @Qualifier("insightsGeminiClient")
        private final Client geminiClient;
        private final ObjectMapper objectMapper;

        @Value("${insights.gemini.model:gemini-2.5-flash}")
        private String model;

        @Override
        public TelemetryInsightResult analyze(VehicleReference vehicle, TelemetrySensorPayload payload) {
                validateApiKey();
                validateModel();
                var config = buildGenerateContentConfig();
                var chat = geminiClient.chats.create(model, config);
                var userContent = buildUserContent(vehicle, payload);

                try {
                        var response = chat.sendMessage(List.of(userContent));
                        var responseText = Optional.ofNullable(response.text())
                                        .filter(text -> !text.isBlank())
                                        .orElseThrow(() -> new IllegalStateException("Gemini response body was empty"));

                        var telemetryResponse = objectMapper.readValue(responseText, TelemetryLLMResponse.class);
                        LOGGER.debug("Gemini telemetry analytics completed for vehicle {} with risk {}",
                                        vehicle.vehicleId(), telemetryResponse.riskLevel());
                        return mapToResult(telemetryResponse, response.responseId().orElse(null));
                } catch (Exception ex) {
                        LOGGER.error("Unable to analyze telemetry with Gemini", ex);
                        throw new TelemetryAnalysisException("Unable to analyze telemetry with Gemini", ex);
                }
        }

        private void validateApiKey() {
                if (geminiClient.apiKey() == null || geminiClient.apiKey().isBlank()) {
                        throw new TelemetryAnalysisException(
                                        "Missing Gemini API key. Configure insights.gemini.api-key property.");
                }
        }

        private void validateModel() {
                if (model == null || model.isBlank()) {
                        throw new TelemetryAnalysisException(
                                        "Missing Gemini model configuration. Configure insights.gemini.model property.");
                }
        }

        private GenerateContentConfig buildGenerateContentConfig() {
                return GenerateContentConfig.builder()
                                .systemInstruction(buildSystemInstruction())
                                .temperature(DEFAULT_TEMPERATURE)
                                .responseMimeType("application/json")
                                .responseSchema(buildResponseSchema())
                                .build();
        }

        private Content buildSystemInstruction() {
                var prompt = "Eres el analista de conducción y mantenimiento de Safecar. " +
                                "Analiza los datos de sensores y responde en JSON válido respetando el esquema solicitado.";
                return Content.builder()
                                .role("system")
                                .parts(List.of(Part.fromText(prompt)))
                                .build();
        }

        private Content buildUserContent(VehicleReference vehicle, TelemetrySensorPayload payload) {
                return Content.builder()
                                .role("user")
                                .parts(List.of(
                                                Part.fromText(buildUserPrompt(vehicle)),
                                                Part.fromText(buildPayload(payload))))
                                .build();
        }

        private String buildUserPrompt(VehicleReference vehicle) {
                return """
                                Genera analíticas y recomendaciones para:
                                - Conductor: %s
                                - Vehículo (placa %s)
                                Devuelve mantenimiento predictivo y hábitos de conducción.
                                """.formatted(vehicle.driverFullName(), vehicle.plateNumber());
        }

        private String buildPayload(TelemetrySensorPayload sensor) {
                var payload = new LinkedHashMap<String, Object>();
                payload.put("gas_level", sensor.gasLevel());
                payload.put("engine_temp", sensor.engineTemperature());
                payload.put("ambient_temp", sensor.ambientTemperature());
                payload.put("humidity", sensor.humidity());
                payload.put("current_amps", sensor.currentAmps());
                payload.put("location", Map.of(
                                "lat", sensor.latitude(),
                                "lng", sensor.longitude()));

                try {
                        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
                } catch (JsonProcessingException e) {
                        throw new IllegalStateException("Unable to serialize telemetry payload", e);
                }
        }

        private Schema buildResponseSchema() {
                var recommendationSchema = Schema.builder()
                                .type(Type.Known.OBJECT)
                                .properties(Map.of(
                                                "title", Schema.builder().type(Type.Known.STRING).build(),
                                                "detail", Schema.builder().type(Type.Known.STRING).build()))
                                .required(List.of("title", "detail"))
                                .build();

                return Schema.builder()
                                .type(Type.Known.OBJECT)
                                .properties(Map.of(
                                                "risk_level", Schema.builder().type(Type.Known.STRING).build(),
                                                "maintenance_summary", Schema.builder().type(Type.Known.STRING).build(),
                                                "maintenance_window", Schema.builder().type(Type.Known.STRING).build(),
                                                "driving_habit_score",
                                                Schema.builder().type(Type.Known.INTEGER).build(),
                                                "driving_habit_summary",
                                                Schema.builder().type(Type.Known.STRING).build(),
                                                "driving_alerts", Schema.builder().type(Type.Known.STRING).build(),
                                                "recommendations", Schema.builder()
                                                                .type(Type.Known.ARRAY)
                                                                .items(recommendationSchema)
                                                                .build()))
                                .required(List.of(
                                                "risk_level",
                                                "maintenance_summary",
                                                "maintenance_window",
                                                "driving_habit_score",
                                                "driving_habit_summary",
                                                "driving_alerts",
                                                "recommendations"))
                                .build();
        }

        private TelemetryInsightResult mapToResult(TelemetryLLMResponse response, String responseId) {
                var recommendations = response.recommendations() == null ? List.<InsightRecommendation>of()
                                : response.recommendations().stream()
                                                .filter(Objects::nonNull)
                                                .map(r -> new InsightRecommendation(r.title(), r.detail()))
                                                .toList();

                return new TelemetryInsightResult(
                                response.riskLevel(),
                                response.maintenanceSummary(),
                                response.maintenanceWindow(),
                                response.drivingHabitScore(),
                                response.drivingHabitSummary(),
                                response.drivingAlerts(),
                                recommendations,
                                responseId,
                                Instant.now());
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private record TelemetryLLMResponse(
                        @JsonProperty("risk_level") String riskLevel,
                        @JsonProperty("maintenance_summary") String maintenanceSummary,
                        @JsonProperty("maintenance_window") String maintenanceWindow,
                        @JsonProperty("driving_habit_score") Integer drivingHabitScore,
                        @JsonProperty("driving_habit_summary") String drivingHabitSummary,
                        @JsonProperty("driving_alerts") String drivingAlerts,
                        List<RecommendationPayload> recommendations) {
        }

        private record RecommendationPayload(
                        String title,
                        String detail) {
        }
}
