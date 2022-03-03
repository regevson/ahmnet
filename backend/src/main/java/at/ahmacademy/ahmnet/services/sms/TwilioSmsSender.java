package at.ahmacademy.ahmnet.services.sms;

import org.springframework.stereotype.Component;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import at.ahmacademy.ahmnet.configs.TwilioInitializer;
import at.ahmacademy.ahmnet.model.SmsRequest;

@Component
public class TwilioSmsSender implements SmsSender {

  /*
  @Autowired
  private TwilioConfig twilioConfig;
  */

  @Override
  public void sendWAMessage(SmsRequest sms) {
    PhoneNumber to = new PhoneNumber("whatsapp:" + sms.getPhoneNumber());
    PhoneNumber from = new PhoneNumber("whatsapp:" + TwilioInitializer.number);
    String msg = sms.getMessage();
    MessageCreator creator = Message.creator(to, from, msg);
    creator.create();
  }

}
