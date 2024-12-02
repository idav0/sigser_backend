package mx.edu.utez.sigser.config;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
    // URL del broker MQTT
    @Value("${mqtt.broker-url}")
    private String brokerUrl;

    @Bean
    public MqttClient mqttClient() throws MqttException {

        // Client ID único para tu cliente
        String clientId = "sigser-publisher";

        MqttClient mqttClient = new MqttClient(brokerUrl, clientId);

        // Opciones de conexión
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        //options.setUserName("your-username");  // Si tu broker requiere autenticación
        //options.setPassword("your-password".toCharArray());  // Si tu broker requiere autenticación

        // Conectar al broker
        mqttClient.connect(options);

        return mqttClient;
    }

    public void publishMessage(MqttClient mqttClient, String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        mqttMessage.setQos(1);  // Calidad de servicio (QoS), elige entre 0, 1, 2
        mqttMessage.setRetained(false);  // Si el mensaje debe ser retenido en el broker

        // Publicar el mensaje
        mqttClient.publish(topic, mqttMessage);
    }
}
