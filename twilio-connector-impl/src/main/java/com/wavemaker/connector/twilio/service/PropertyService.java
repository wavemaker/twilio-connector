package com.wavemaker.connector.twilio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    @Value("${twilio.account.SID}")
    private String twilioSID;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String phoneNumber;

    @Value("${twilio.verify.api.key}")
    private String verifyAPIKey;

    public String getTwilioSID() {
        return twilioSID;
    }

    public void setTwilioSID(String twilioSID) {
        this.twilioSID = twilioSID;
    }

    public String getTwilioAuthToken() {
        return twilioAuthToken;
    }

    public void setTwilioAuthToken(String twilioAuthToken) {
        this.twilioAuthToken = twilioAuthToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerifyAPIKey() {
        return verifyAPIKey;
    }

    public void setVerifyAPIKey(String verifyAPIKey) {
        this.verifyAPIKey = verifyAPIKey;
    }

}
