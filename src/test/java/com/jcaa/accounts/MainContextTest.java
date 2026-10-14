package com.jcaa.accounts;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.jcaa.accounts.application.port.out.NotificationPublisherPort;
import com.jcaa.accounts.infrastructure.adapter.messaging.KafkaNotificationPublisherAdapter;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("Contexto de Spring")
class MainContextTest {

  // Evita abrir una conexión real a TiDB durante el test
  @MockBean private DataSource dataSource;

  @Autowired private NotificationPublisherPort notificationPublisherPort;

  @Test
  @DisplayName("el puerto de notificaciones usa el adaptador de Kafka")
  void shouldWireKafkaPublisher() {
    // Arrange — contexto levantado por @SpringBootTest

    // Act & Assert
    assertInstanceOf(KafkaNotificationPublisherAdapter.class, notificationPublisherPort);
  }
}
