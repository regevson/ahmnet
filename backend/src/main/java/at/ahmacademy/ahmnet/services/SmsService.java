package at.ahmacademy.ahmnet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.SmsRequest;
import at.ahmacademy.ahmnet.services.sms.TwilioSmsSender;

@Service
public class SmsService {

    @Autowired
    TwilioSmsSender sender;
    
    public void sendWAMessage(SmsRequest sms) {
	sender.sendWAMessage(sms);
    }

}
