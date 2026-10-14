package com.jcaa.accounts.application.port.out;

import com.jcaa.accounts.domain.model.EmailDestinationModel;

public interface NotificationPublisherPort {
  void publish(EmailDestinationModel destination);
}
