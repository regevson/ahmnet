package at.ahmacademy.ahmnet.services.sms;

import at.ahmacademy.ahmnet.model.SmsRequest;

public interface SmsSender {

  void sendWAMessage(SmsRequest sms);

}
