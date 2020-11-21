package com.wavemaker.connector.twilio;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;
import com.wavemaker.connector.twilio.constant.Channel;
import com.wavemaker.connector.twilio.model.VerificationResult;
import com.wavemaker.connector.twilio.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;

@Service
@Primary
public class TwilioConnectorImpl implements TwilioConnector{

    private static final Logger logger = LoggerFactory.getLogger(TwilioConnectorImpl.class);

    @Autowired
    private PropertyService propertyService;

    @PostConstruct
    public void init(){
        Twilio.init(propertyService.getTwilioSID(), propertyService.getTwilioAuthToken());
    }

    public void sendSMS(String phoneNumber, String messageBody) {
        logger.info("Sending SMS message to phone number " + phoneNumber);
        Message message = Message
                .creator(new PhoneNumber(phoneNumber),
                        new PhoneNumber(propertyService.getPhoneNumber()),
                        messageBody)
                .create();
    }

    public void sendMMS(String phoneNumber, String messageBody, List<URI> mediaUris) {
        logger.info("Sending MMS message to phone number " + phoneNumber);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(phoneNumber),
                new com.twilio.type.PhoneNumber(propertyService.getPhoneNumber()),
                messageBody)
                .setMediaUrl(mediaUris)
                .create();

    }

    public String respondToSMSOrMMS(String messageBody) {
        logger.info("Preparing SMS or MMS xml response ", messageBody);
        Body body = new Body
                .Builder(messageBody)
                .build();
        com.twilio.twiml.messaging.Message sms = new com.twilio.twiml.messaging.Message
                .Builder()
                .body(body)
                .build();
        MessagingResponse twiml = new MessagingResponse
                .Builder()
                .message(sms)
                .build();
        return twiml.toXml();
    }

    public void sendWhatsAppMessage(String phoneNumber, String messageBody, List<URI> mediaUris) {
        logger.info("Sending whats app message to phone number ",phoneNumber);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:"+phoneNumber),
                new com.twilio.type.PhoneNumber("whatsapp:"+propertyService.getPhoneNumber()),
                messageBody).setMediaUrl(mediaUris)
                .create();
    }

    public String respondWhatsAppMessage(String messageBody){
        logger.info("Preparing whats app xml response ", messageBody);
        Body body = new Body
                .Builder(messageBody)
                .build();
        com.twilio.twiml.messaging.Message message = new com.twilio.twiml.messaging.Message
                .Builder()
                .body(body)
                .build();
        MessagingResponse twiml = new MessagingResponse
                .Builder()
                .message(message)
                .build();
        return twiml.toXml();
    }

    public VerificationResult startVerification(String phoneNumber, Channel channel) {
        logger.info("Sending verification using twilio to phone number "+ phoneNumber+" with tha channel " + channel.toString());
        try {
            Verification verification = Verification.creator(propertyService.getVerifyAPIKey(), phoneNumber, channel.toString()).create();
            return new VerificationResult(verification.getSid());
        } catch (ApiException exception) {
            logger.error(exception.getMessage(),exception);
            return new VerificationResult(new String[] {exception.getMessage()});
        }
    }

    public VerificationResult checkVerification(String emailOrPhoneNumber, String code){
        logger.info("Verifying given code "+ code +" using twilio");
        try {
            VerificationCheck verification = VerificationCheck.creator(propertyService.getVerifyAPIKey(), code).setTo(emailOrPhoneNumber).create();
            if("approved".equals(verification.getStatus())) {
                return new VerificationResult(verification.getSid());
            }
            return new VerificationResult(new String[]{"Invalid code."});
        } catch (ApiException exception) {
            logger.error(exception.getMessage(),exception);
            return new VerificationResult(new String[]{exception.getMessage()});
        }
    }
}