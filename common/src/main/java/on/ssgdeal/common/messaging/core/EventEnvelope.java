package on.ssgdeal.common.messaging.core;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.exception.EventPayloadException.EventDeserializeException;
import on.ssgdeal.common.messaging.exception.EventPayloadException.EventSerializeException;

@Slf4j
public record EventEnvelope<T extends EventPayload>(
    String topic,
    String passportId,
    T payload,
    String timestamp
) {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T extends EventPayload> EventEnvelope<T> wrap(
        String topic, String passportId, T payload, String timestamp
    ) {
        return new EventEnvelope<>(topic, passportId, payload, timestamp);
    }

    public static <T extends EventPayload> EventEnvelope<T> wrap(
        String topic, String passportId, T payload
    ) {
        return new EventEnvelope<>(topic, passportId, payload, LocalDateTime.now().toString());
    }

    public String toJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("EventEnvelope 의 직렬화를 실패했습니다. => {}", e.getMessage());
            throw new EventSerializeException();
        }
    }

    public static <T extends EventPayload> EventEnvelope<T> fromJson(String json, Class<T> tClass) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            log.info("Envelope Json 역직렬화 시작 - jsonNode: {}", jsonNode);

            String topic = jsonNode.get("topic").asText();
            String timestamp = jsonNode.get("timestamp").asText();
            String passportId = jsonNode.get("passportId").asText();
            T payload = OBJECT_MAPPER.treeToValue(jsonNode.get("payload"), tClass);

            return EventEnvelope.wrap(topic, passportId, payload, timestamp);
        } catch (Exception e) {
            log.error("Envelope JSON 역직렬화 실패", e);
            throw new EventDeserializeException();
        }
    }
}
