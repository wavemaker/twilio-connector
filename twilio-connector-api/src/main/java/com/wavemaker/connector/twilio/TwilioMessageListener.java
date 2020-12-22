package com.wavemaker.connector.twilio;

import com.wavemaker.connector.twilio.model.TwilioMessage;

/**
 * Created by saraswathir on 17/12/20
 */
public interface TwilioMessageListener {

    String onMessage(TwilioMessage message);
}
