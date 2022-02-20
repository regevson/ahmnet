package at.qe.skeleton.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;


@Configuration
public class TwilioInitializer {

/*
    @Autowired
    TwilioConfig twilioConfig;
    */
    
    private String accountSid = "ACacfce6a9592a7f53bff10b98bf3366e7";
    private String authToken = "57ef36616ed2e44e6896e6ee02b5b002";
    public static String number = "+14155238886";

    public TwilioInitializer() {
	Twilio.init(accountSid, authToken);
    }
}
