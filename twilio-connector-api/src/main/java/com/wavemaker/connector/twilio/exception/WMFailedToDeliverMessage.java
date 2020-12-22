package com.wavemaker.connector.twilio.exception;

/**
 * Created by saraswathir on 14/12/20
 */
public class WMFailedToDeliverMessage extends RuntimeException {

    public WMFailedToDeliverMessage(String errorMessage) {
        super(String.format("Failed to deliver message: %s", errorMessage));
    }
}
