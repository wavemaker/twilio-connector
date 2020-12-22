package com.wavemaker.connector.twilio.exception;

/**
 * Created by saraswathir on 18/12/20
 */
public class WMFailedToConstructResponse extends RuntimeException {

    public WMFailedToConstructResponse(String errorMessage) {
        super(String.format("Failed to construct response: %s", errorMessage));
    }
}
