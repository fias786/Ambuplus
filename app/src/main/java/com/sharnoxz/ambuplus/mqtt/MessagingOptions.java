package com.sharnoxz.ambuplus.mqtt;

public class MessagingOptions {

    final String SOLACE_CLIENT_USER_NAME = "solace-cloud-client";
    final String SOLACE_CLIENT_PASSWORD = "a1nnep178msst5750dr8edl2ru";
    final String SOLACE_MQTT_HOST = "tcp://mr-1n34cqfh5ifr.messaging.solace.cloud:1883";

    // Other options
    int SOLACE_CONNECTION_TIMEOUT = 3;
    int SOLACE_CONNECTION_KEEP_ALIVE_INTERVAL = 60;
    boolean SOLACE_CONNECTION_CLEAN_SESSION = true;
    boolean SOLACE_CONNECTION_RECONNECT = true;
}
