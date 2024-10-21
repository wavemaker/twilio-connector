package com.wavemaker.connector.twilio;

import java.net.URI;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.wavemaker.connector.twilio.constant.Channel;
import com.wavemaker.connector.twilio.model.VerificationResult;
import com.wavemaker.runtime.connector.annotation.WMConnector;


@WMConnector(name = "twilio-connector",
        description = "Twilio connector to used to send & receive messages and send & verify OTPs")
public interface TwilioConnector {

    /***
     * Send Message to the destination phone number.
     * @param toPhoneNumber: The destination phone number
     * @param messageBody: The text of the message you want to send. Can be up to 1,600
     *                     characters in length.
     */
    public void sendSMS(String toPhoneNumber, String messageBody);

    /***
     * Send Multi-Media Messages to the destination phone number. The media can be of type
     * `gif`, `png`, and `jpeg`.
     * @param toPhoneNumber: The destination phone number
     * @param messageBody: The text of the message you want to send. Can be up to 1,600
     *                     characters in length.
     * @param mediaUris: The URL of the media to send with the message
     */
    public void sendMMS(String toPhoneNumber, String messageBody, List<URI> mediaUris);

    /***
     * Send Multi-Media Messages to the destination WhatsApp number. The media can be of type
     * `gif`, `png`, and `jpeg`.
     * @param toPhoneNumber: The destination phone number
     * @param messageBody: The text of the message you want to send. Can be up to 1,600
     *                     characters in length.
     * @param mediaUris: The URL of the media to send with the message
     */
    public void sendWhatsAppMessage(String toPhoneNumber, String messageBody, List<URI> mediaUris);

    /***
     * Receive request send from the device to twilio and respond back with the response message that is returned from
     * the twilioMessageListener back to the requested device
     * @param request: Message send from device to twilio
     * @param response: Message that is sent from twilio to requested device.
     * @param twilioMessageListener
     */
    public void receiveAndRespondTwilioMessage(HttpServletRequest request, HttpServletResponse response,
                                               TwilioMessageListener twilioMessageListener);

    /***
     * Send OTP to the given phoneNumber or email based on selected channel(SMS, Voice or Email)
     * @param toPhoneNumberOrEmail: The phone number or email to verify
     * @param channel: The verification method to use
     * @return VerificationResult
     */
    public VerificationResult sendOTP(String toPhoneNumberOrEmail, Channel channel);

    /***
     * Validates the OTP sent to the given PhoneNumber or Email
     * @param toPhoneNumberOrEmail: The phone number or email to verify
     * @param otpCode: The verification string
     * @return VerificationResult
     */
    public VerificationResult verifyOTP(String toPhoneNumberOrEmail, String otpCode);
}
