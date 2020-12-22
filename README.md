
Connector is a Java based backend extension for WaveMaker applications. Connectors are built as Java modules & exposes java based SDK to interact with the connector implementation.
Each connector is built for a specific purpose and can be integrated with one of the external services. Connectors are imported & used in the WaveMaker application. Each connector runs on its own container thereby providing the ability to have it’s own version of the third party dependencies.


1. Connector is a java based extension which can be integrated with external services and reused in many Wavemaker applications.
1. Each connector can work as an SDK for an external system.
1. Connectors can be imported once in a WaveMaker application and used many times in the applications by creating multiple instances.
1. Connectors are executed in its own container in the WaveMaker application, as a result there are no dependency version conflict issues between connectors.

## About Twilio Connector

### Introduction
Twilio is a cloud based communication platform which performs communication functions using its API's. Twilio lets you receive SMS, MMS, WhatsApp, Voice messages or even respond to the messages and many more services.

This connector exposes api to send messages to and receive messages from Twilio using WaveMaker application.

### Basic Twilio Actions
Basic actions that you can do with this connector
1. Send SMS to device
2. Send MMS message to the device
3. Send WhatsApp message
4. Receive and respond to message
5. Implementing OTP

### Prerequisite
1. Twilio Account with SID, Token, Twilio Number and API Key.
2. Java 1.8 or above
3. Maven 3.1.0 or above
4. Any java editor such as Eclipse, Intelij..etc
5. Internet connection

### Build
You can build this connector using following command
```
mvn clean install -DskipTests
```

###Deploy
You can import connector dist/twilio-connector.zip artifact in WaveMaker studio application under **File Explorer** section. On after deploying twilio
-connector in the WaveMaker studio application, make sure to update connector properties in the profile properties such as SID, token, Twilio Number and API
 Key.
 
### Using Twilio connector to perform actions
```
@Autowired
private TwilioConnector twilioConnector;

public void sendSMSToDevice(String toPhoneNumber, String messageBody){
    twilioConnector.sendSMS(toPhoneNumber,messageBody);
}
```

Apart from above api, twilio-connector provides other apis to send messages and implement OTP, visit TwilioConnector java class in api module.