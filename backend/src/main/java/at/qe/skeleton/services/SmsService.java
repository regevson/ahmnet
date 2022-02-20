package at.qe.skeleton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.qe.skeleton.model.SmsRequest;
import at.qe.skeleton.services.sms.TwilioSmsSender;

@Service
public class SmsService {

    @Autowired
    TwilioSmsSender sender;
    
    public void sendWAMessage(SmsRequest sms) {
	sender.sendWAMessage(sms);
    }

}
