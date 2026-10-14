package com.jcaa.accounts.infrastructure.adapter.messaging;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcaa.accounts.domain.exception.NotificationPublishException;
import com.jcaa.accounts.domain.model.EmailDestinationModel;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@DisplayName("KafkaNotificationPublisherAdapter")
@ExtendWith(MockitoExtension.class)
class KafkaNotificationPublisherAdapterTest {

  private static final String TOPIC = "notificaciones";

  @Mock private KafkaTemplate<String, String> kafkaTemplate;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private KafkaNotificationPublisherAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new KafkaNotificationPublisherAdapter(kafkaTemplate, objectMapper, TOPIC);
  }

  @Test
  @DisplayName("publish() envía al tópico el JSON con el formato acordado por el grupo")
  void shouldPublishAgreedJsonFormat() throws Exception {
    // Arrange
    final EmailDestinationModel destination =
        new EmailDestinationModel("ana@gmail.com", "Ana Pérez", "Cuenta creada", "<p>Hola</p>");
    when(kafkaTemplate.send(eq(TOPIC), anyString()))
        .thenReturn(new CompletableFuture<SendResult<String, String>>());

    // Act
    adapter.publish(destination);

    // Assert
    final ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
    verify(kafkaTemplate).send(eq(TOPIC), payloadCaptor.capture());
    final JsonNode json = objectMapper.readTree(payloadCaptor.getValue());
    assertAll(
        "formato del mensaje",
        () -> assertEquals("ana@gmail.com", json.get("email").asText()),
        () -> assertEquals("Ana Pérez", json.get("nombre").asText()),
        () -> assertEquals("Cuenta creada", json.get("asunto").asText()),
        () -> assertEquals("<p>Hola</p>", json.get("mensaje").asText()),
        () -> assertEquals(4, json.size()));
  }

  @Test
  @DisplayName("publish() lanza NotificationPublishException cuando Kafka falla")
  void shouldThrowWhenKafkaFails() {
    // Arrange
    final EmailDestinationModel destination =
        new EmailDestinationModel("ana@gmail.com", "Ana Pérez", "Cuenta creada", "<p>Hola</p>");
    when(kafkaTemplate.send(eq(TOPIC), anyString())).thenThrow(new KafkaException("sin broker"));

    // Act & Assert
    assertThrows(NotificationPublishException.class, () -> adapter.publish(destination));
  }
}
