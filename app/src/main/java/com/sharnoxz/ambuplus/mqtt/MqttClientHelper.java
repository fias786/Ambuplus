package com.sharnoxz.ambuplus.mqtt;

import android.content.Context;
import android.util.Log;

import com.sharnoxz.ambuplus.data.NotificationData;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.MqttClient;

public class MqttClientHelper extends  MessagingOptions {

    private Context context;

    public MqttClientHelper(Context context) {
        this.context = context;
    }

    private final String TAG = "MqttClientHelper";

    String serverUri = SOLACE_MQTT_HOST;
    private String clientId = MqttClient.generateClientId();
    MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(context,serverUri,clientId);

    public void setCallback(MqttCallbackExtended mqttCallbackExtended){
        mqttAndroidClient.setCallback(mqttCallbackExtended);
        connect();
    }


    public void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(SOLACE_CONNECTION_RECONNECT);
        mqttConnectOptions.setCleanSession(SOLACE_CONNECTION_CLEAN_SESSION);
        mqttConnectOptions.setUserName(SOLACE_CLIENT_USER_NAME);
        mqttConnectOptions.setPassword(SOLACE_CLIENT_PASSWORD.toCharArray());
        mqttConnectOptions.setConnectionTimeout(SOLACE_CONNECTION_TIMEOUT);
        mqttConnectOptions.setKeepAliveInterval(SOLACE_CONNECTION_KEEP_ALIVE_INTERVAL);
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG,"Failed to connect to server",exception);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribe(String topic, int qos){
        try {
            mqttAndroidClient.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w(TAG, "Subscribed to topic "+topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "Subscription to topic "+topic+" failed!");
                }
            });
        } catch (MqttException e) {
            System.err.println("Exception whilst subscribing to topic "+topic);
            e.printStackTrace();
        }
    }

    public void publish(String topic, NotificationData msg, int qos){
        MqttMessage mqttMessage = new MqttMessage();
        String str = msg.getUserProfile()+"##"+msg.getNotificationType()+"##"+msg.getNotificationText()+"##"+msg.getNotificationTime();
        mqttMessage.setPayload(str.getBytes());
        try {
            mqttAndroidClient.publish(topic, mqttMessage.getPayload(), qos, false);
            Log.d(TAG, "Message published to topic "+topic+msg);
        } catch (MqttException e) {
            Log.d(TAG, "Error Publishing to $topic: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return mqttAndroidClient.isConnected();
    }

    public void destroy() {
        mqttAndroidClient.unregisterResources();
        try {
            mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
