package at.qe.skeleton.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsRequest {

    private String phoneNumber;
    private String message;
    
    public SmsRequest(String num, String msg) {
	this.setPhoneNumber(num);
	this.setMessage(msg);
    }

}
