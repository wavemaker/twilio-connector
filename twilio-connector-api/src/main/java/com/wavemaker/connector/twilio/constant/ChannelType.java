package com.wavemaker.connector.twilio.constant;

/**
 * Created by saraswathir on 18/12/20
 */
public enum ChannelType {

    SMS("sms"),
    WHATSAPP("whatsapp");

    private final String value;

    private ChannelType(final String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
