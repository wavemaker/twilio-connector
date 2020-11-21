package com.wavemaker.connector.twilio;

import com.wavemaker.connector.twilio.constant.Channel;
import com.wavemaker.connector.twilio.model.VerificationResult;
import com.wavemaker.runtime.connector.annotation.WMConnector;

import java.net.URI;
import java.util.List;


@WMConnector(name = "twilio-connector",
        description = "Twilio connector to used to send & receive messages and send & verify OTPs")
public interface TwilioConnector {

    public void sendSMS(String phoneNumber, String messageBody);

    public void sendMMS(String phoneNumber, String messageBody, List<URI> mediaUris);

    public String respondToSMSOrMMS(String messageBody);

    public void sendWhatsAppMessage(String phoneNumber, String messageBody, List<URI> mediaUris);

    public String respondWhatsAppMessage(String messageBody);

    public VerificationResult startVerification(String phoneNumber, Channel channel);

    public VerificationResult checkVerification(String phoneNumber, String code);

}