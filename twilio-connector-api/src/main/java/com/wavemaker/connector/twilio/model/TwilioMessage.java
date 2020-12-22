package com.wavemaker.connector.twilio.model;

import java.net.URI;

import com.wavemaker.connector.twilio.constant.ChannelType;

/**
 * Created by saraswathir on 17/12/20
 */
public class TwilioMessage {

    private String messageId;
    private ChannelType channelType;
    private String fromNumber;
    private String text;
    private URI mediaUrl;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public URI getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(URI mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Override
    public String toString() {
        return "TwilioMessage{" +
                "messageId='" + messageId + '\'' +
                ", channelType=" + channelType +
                ", fromNumber='" + fromNumber + '\'' +
                ", text='" + text + '\'' +
                ", mediaUrl=" + mediaUrl +
                '}';
    }
}
