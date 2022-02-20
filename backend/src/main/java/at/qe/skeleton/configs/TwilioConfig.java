package at.qe.skeleton.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Configuration
@ConfigurationProperties("twilio")
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String number;

}
