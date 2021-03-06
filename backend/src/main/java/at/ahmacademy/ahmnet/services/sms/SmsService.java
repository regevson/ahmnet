package at.ahmacademy.ahmnet.services.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.SmsRequest;

@Service
public class SmsService {

  @Autowired
  TwilioSmsSender sender;
  
  public void sendWAMessage(SmsRequest sms) {
    sender.sendWAMessage(sms);
  }

}
