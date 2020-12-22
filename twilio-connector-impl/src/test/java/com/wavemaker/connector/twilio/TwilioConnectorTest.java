package com.wavemaker.connector.twilio;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wavemaker.connector.twilio.constant.Channel;
import com.wavemaker.connector.twilio.model.VerificationResult;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TwilioConnectorTestConfiguration.class)
public class TwilioConnectorTest {

    @Autowired
    private TwilioConnector twilioConnector;

    @Test
    public void sendSMS() {
        twilioConnector.sendSMS("+91XXXXXXXXXX", "hello this message is from twilio connector");
    }

    @Test
    public void sendMMS() {
        List<URI> uris = new ArrayList<>();
        URI url = null;
        try {
            url = new URI("https://www.wavemakeronline.com/run-90t8nydg71/TwilioConnector_master/resources/images/logos/wm-brand-logo.png");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        uris.add(url);
        twilioConnector.sendMMS("+91XXXXXXXXXX", "hello this message is from twilio connector", uris);
    }

    @Test
    public void startVerification() {
        VerificationResult verificationResult = twilioConnector.sendOTP("xxxxxxxxx@gmail.com", Channel.EMAIL);
        System.out.println(verificationResult.isValid());
        Assert.assertNotNull("Verification sid should not be null", verificationResult.getId());
    }

    @Test
    public void checkVerification() {
        VerificationResult verificationResult = twilioConnector.verifyOTP("+91XXXXXXXXXX", "1233455");
        Assert.assertNotNull("Verification sid should not be null", verificationResult.getId());
    }

}
