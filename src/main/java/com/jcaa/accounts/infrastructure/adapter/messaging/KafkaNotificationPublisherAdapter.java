package com.jcaa.accounts.infrastructure.adapter.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcaa.accounts.application.port.out.NotificationPublisherPort;
import com.jcaa.accounts.domain.exception.NotificationPublishException;
import com.jcaa.accounts.domain.model.EmailDestinationModel;
import com.jcaa.accounts.infrastructure.adapter.messaging.dto.NotificationMessage;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** Paso 7 del diagrama: deja el mensaje de "cuenta creada" en el tópico (async). */
@Slf4j
@Component
public class KafkaNotificationPublisherAdapter implements NotificationPublisherPort {

  private static final String PROP_TOPIC = "${kafka.topic}";
  private static final String LOG_PUBLISHED =
      "[KafkaNotificationPublisherAdapter] notificación publicada. topic={} partition={} offset={}";
  private static final String LOG_PUBLISH_FAILED =
      "[KafkaNotificationPublisherAdapter] no se pudo publicar. topic={} causa={}";

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;

  public KafkaNotificationPublisherAdapter(
      final KafkaTemplate<String, String> kafkaTemplate,
      final ObjectMapper objectMapper,
      @Value(PROP_TOPIC) final String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
  }

  @Override
  public void publish(final EmailDestinationModel destination) {
    final String payload = toJson(destination);
    try {
      kafkaTemplate
          .send(topic, payload)
          .whenComplete(
              (result, error) -> {
                if (Objects.nonNull(error)) {
                  log.error(LOG_PUBLISH_FAILED, topic, error.getMessage());
                  return;
                }
                log.info(
                    LOG_PUBLISHED,
                    topic,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
              });
    } catch (final KafkaException kafkaException) {
      throw NotificationPublishException.becausePublishFailed(kafkaException);
    }
  }

  private String toJson(final EmailDestinationModel destination) {
    final NotificationMessage message =
        new NotificationMessage(
            destination.getDestinationEmail(),
            destination.getDestinationName(),
            destination.getSubject(),
            destination.getBody());
    try {
      return objectMapper.writeValueAsString(message);
    } catch (final JsonProcessingException jsonException) {
      throw NotificationPublishException.becausePublishFailed(jsonException);
    }
  }
}
