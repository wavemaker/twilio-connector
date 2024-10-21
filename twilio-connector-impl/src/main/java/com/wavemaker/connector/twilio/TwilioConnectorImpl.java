package com.wavemaker.connector.twilio;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;
import com.wavemaker.connector.twilio.constant.Channel;
import com.wavemaker.connector.twilio.constant.ChannelType;
import com.wavemaker.connector.twilio.exception.WMFailedToConstructResponse;
import com.wavemaker.connector.twilio.exception.WMFailedToDeliverMessage;
import com.wavemaker.connector.twilio.model.TwilioMessage;
import com.wavemaker.connector.twilio.model.VerificationResult;
import com.wavemaker.connector.twilio.service.ConfigService;

@Service
@Primary
public class TwilioConnectorImpl implements TwilioConnector {

    private static final Logger logger = LoggerFactory.getLogger(TwilioConnectorImpl.class);

    @Autowired
    private ConfigService configService;

    @PostConstruct
    protected void init() {
        Twilio.init(configService.getTwilioSID(), configService.getTwilioAuthToken());
    }

    public void sendSMS(String toPhoneNumber, String messageBody) {
        logger.info("Sending SMS message to phone number {}", toPhoneNumber);
        Message message = Message
                .creator(new PhoneNumber(toPhoneNumber),
                        new PhoneNumber(configService.getPhoneNumber()),
                        messageBody)
                .create();
        if (message.getStatus().equals(Message.Status.FAILED) || message.getStatus().equals(Message.Status.UNDELIVERED)) {
            throw new WMFailedToDeliverMessage("Failed to deliver message to phone number " + toPhoneNumber + " from twilio");
        }
    }

    public void sendMMS(String toPhoneNumber, String messageBody, List<URI> mediaUris) {
        logger.info("Sending MMS message to phone number {}", toPhoneNumber);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(toPhoneNumber),
                new com.twilio.type.PhoneNumber(configService.getPhoneNumber()),
                messageBody)
                .setMediaUrl(mediaUris)
                .create();
        if (message.getStatus().equals(Message.Status.FAILED) || message.getStatus().equals(Message.Status.UNDELIVERED)) {
            throw new WMFailedToDeliverMessage("Failed to deliver message to phone number " + toPhoneNumber + " from twilio");
        }
    }

    public void sendWhatsAppMessage(String toPhoneNumber, String messageBody, List<URI> mediaUris) {
        logger.info("Sending whats app message to phone number {}", toPhoneNumber);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + toPhoneNumber),
                new com.twilio.type.PhoneNumber("whatsapp:" + configService.getPhoneNumber()),
                messageBody).setMediaUrl(mediaUris)
                .create();
        if (message.getStatus().equals(Message.Status.FAILED) || message.getStatus().equals(Message.Status.UNDELIVERED)) {
            throw new WMFailedToDeliverMessage("Failed to deliver message to phone number " + toPhoneNumber + " from twilio");
        }
    }

    public void receiveAndRespondTwilioMessage(HttpServletRequest request, HttpServletResponse response,
                                               TwilioMessageListener twilioMessageListener) {
        TwilioMessage twilioMessage = new TwilioMessage();
        twilioMessage.setMessageId(request.getParameter("SmsMessageSid"));
        String fromParam = request.getParameter("From");
        if (fromParam.contains("whatsapp")) {
            twilioMessage.setChannelType(ChannelType.WHATSAPP);
        } else {
            twilioMessage.setChannelType(ChannelType.SMS);
        }
        twilioMessage.setFromNumber(fromParam);
        twilioMessage.setText(request.getParameter("Body"));
        try {
            twilioMessage.setMediaUrl(new URI(request.getParameter("MediaUrl0")));
        } catch (URISyntaxException e) {
            logger.info("Error while constructing URI: {}", e.getMessage());
        }
        String msg = twilioMessageListener.onMessage(twilioMessage);
        if (msg != null) {
            Body body = new Body
                    .Builder(msg)
                    .build();
            com.twilio.twiml.messaging.Message message = new com.twilio.twiml.messaging.Message.Builder()
                    .body(body)
                    .build();
            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(message)
                    .build();
            try {
                response.setContentType("application/xml");
                response.getWriter().write(twiml.toXml());
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                throw new WMFailedToConstructResponse(e.getMessage());
            }
        }

    }

    public VerificationResult sendOTP(String toPhoneNumberOrEmail, Channel channel) {
        logger.info("Sending verification using twilio to phone number {} with tha channel {}", toPhoneNumberOrEmail, channel.toString());
        try {
            Verification verification = Verification.creator(configService.getVerifyAPIKey(), toPhoneNumberOrEmail, channel.toString()).create();
            return new VerificationResult(verification.getSid());
        } catch (ApiException exception) {
            logger.error(exception.getMessage(), exception);
            return new VerificationResult(new String[]{exception.getMessage()});
        }
    }

    public VerificationResult verifyOTP(String toPhoneNumberOrEmail, String otpCode) {
        logger.info("Verifying given code {} using twilio", otpCode);
        try {
            VerificationCheck verification = VerificationCheck.creator(configService.getVerifyAPIKey(), otpCode).setTo(toPhoneNumberOrEmail).create();
            if ("approved".equals(verification.getStatus())) {
                return new VerificationResult(verification.getSid());
            }
            return new VerificationResult(new String[]{"Invalid code."});
        } catch (ApiException exception) {
            logger.error(exception.getMessage(), exception);
            return new VerificationResult(new String[]{exception.getMessage()});
        }
    }
}