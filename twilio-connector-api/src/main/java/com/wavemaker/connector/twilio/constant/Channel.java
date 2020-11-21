package com.wavemaker.connector.twilio.constant;

public enum Channel {
    SMS("sms"),
    CALL("call"),
    EMAIL("email");

    private final String value;

    private Channel(final String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

}
