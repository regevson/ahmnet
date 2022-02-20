package at.qe.skeleton.services.sms;

import at.qe.skeleton.model.SmsRequest;

public interface SmsSender {

    void sendWAMessage(SmsRequest sms);

}
