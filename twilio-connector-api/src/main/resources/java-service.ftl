/*Copyright (c) 2016-2017 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
 package ${packageName};

 import java.net.URI;
 import java.net.URISyntaxException;
 import java.util.ArrayList;
 import java.util.Enumeration;
 import java.util.List;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;

 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;

 import com.wavemaker.connector.twilio.TwilioConnector;
 import com.wavemaker.connector.twilio.TwilioMessageListener;
 import com.wavemaker.connector.twilio.constant.Channel;
 import com.wavemaker.connector.twilio.model.TwilioMessage;
 import com.wavemaker.connector.twilio.model.VerificationResult;
 import com.wavemaker.runtime.service.annotations.ExposeToClient;

 //import ${packageName}.model.*;

 /**
  * This is a singleton class with all its public methods exposed as REST APIs via generated controller class.
  * To avoid exposing an API for a particular public method, annotate it with @HideFromClient.
  *
  * Method names will play a major role in defining the Http Method for the generated APIs. For example, a method name
  * that starts with delete/remove, will make the API exposed as Http Method "DELETE".
  *
  * Method Parameters of type primitives (including java.lang.String) will be exposed as Query Parameters &
  * Complex Types/Objects will become part of the Request body in the generated API.
  *
  * NOTE: We do not recommend using method overloading on client exposed methods.
  */
 @ExposeToClient
 public class ${className} {

    private static final Logger logger = LoggerFactory.getLogger(${className}.class);

    @Autowired
    private TwilioConnector twilioConnector;

	public void sendSMSToDevice(String phoneNumber, String messageBody){
       twilioConnector.sendSMS(phoneNumber,messageBody);
    }

	public void sendMMS(String phoneNumber, String messageBody, List<String> urls){
        List<URI> mediaUris = new ArrayList<>();
        for(String url:urls){
        	try{
            	mediaUris.add(new URI(url));
        	}catch (URISyntaxException e) {
            	throw new RuntimeException(e);
        	}
        }
        twilioConnector.sendMMS(phoneNumber,messageBody,mediaUris);
    }

    public void sendWhatsAppMessage(String phoneNumber, String messageBody, String imageUrl){
         List<URI> mediaUris = new ArrayList<>();
         URI url = null;
         try {
             url = new URI(imageUrl);
         } catch (URISyntaxException e) {
             throw new RuntimeException(e);
         }
         mediaUris.add(url);
         twilioConnector.sendWhatsAppMessage(phoneNumber,messageBody,mediaUris);
    }

    public void respondToMessage(HttpServletRequest request,HttpServletResponse response, String responseMessage){
        twilioConnector.receiveAndRespondTwilioMessage(request, response, new TwilioMessageListener() {
            @Override
            public String onMessage(TwilioMessage twilioMessage) {
                String body=twilioMessage.getText();
                logger.info("TwilioRequest: "+twilioMessage.toString());
                //You can write your business logic here and send a response to the caller.
                //Returning the message that twilio should respond back to the device
                return responseMessage;
            }
        });
    }

    public VerificationResult sendOTPCode(String phoneNumber){
        return twilioConnector.sendOTP(phoneNumber,Channel.SMS);
    }

    public boolean validateOTP(String phoneNumber, String otpCode){
        VerificationResult result=twilioConnector.verifyOTP(phoneNumber,otpCode);
        return result.isValid();
    }
 }