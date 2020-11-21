package com.wavemaker.connector.twilio;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.type.PhoneNumber;
import com.wavemaker.connector.twilio.constant.Channel;
import com.wavemaker.connector.twilio.model.VerificationResult;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import com.wavemaker.connector.twilio.TwilioConnector;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TwilioConnectorTestConfiguration.class)
public class TwilioConnectorTest {

    @Autowired
    private TwilioConnector twilioConnector;

    @Test
    public void sendSMS() {
        twilioConnector.sendSMS("+919876543210", "hello this message is from twilio connector");
    }

    @Test
    public void sendMMS() {
        List<URI> uris = new ArrayList<>();
        URI url = null;
        try {
            url = new URI("");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        uris.add(url);
        twilioConnector.sendMMS("+919876543210", "hello this message is from twilio connector", uris);
    }

    @Test
    public void startVerification() {
        VerificationResult verificationResult = twilioConnector.startVerification("+919876543210", Channel.SMS);
        Assert.assertNotNull("Verification sid should not be null", verificationResult.getId());
    }

    @Test
    public void checkVerification() {
        VerificationResult verificationResult = twilioConnector.checkVerification("+919876543210", "1233455");
        Assert.assertNotNull("Verification sid should not be null", verificationResult.getId());
    }

}