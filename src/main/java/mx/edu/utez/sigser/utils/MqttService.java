package mx.edu.utez.sigser.utils;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private final MqttClient mqttClient;

    public MqttService(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }


    public void sendNotificationAdmin(String message) {
        try {
            String topic = "sigser/notifications/admin";
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttClient.publish(topic, mqttMessage);
            System.out.println("Mensaje enviado a MQTT: " + message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationTechnician(String message) {
        try {
            String topic = "sigser/notifications/technician";
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttClient.publish(topic, mqttMessage);
            System.out.println("Mensaje enviado a MQTT: " + message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationClient(String message) {
        try {
            String topic = "sigser/notifications/client";
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttClient.publish(topic, mqttMessage);
            System.out.println("Mensaje enviado a MQTT: " + message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
