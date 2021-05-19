package bartos.lukasz.bookingservice.application.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@AllArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                .setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/topic", "/break");

//        registry
//                .setApplicationDestinationPrefixes("/app")
//                .enableStompBrokerRelay("/topic", "/break")
//                .setRelayHost("rabbitmq-broker")
//                //.setRelayHost("localhost")
//                .setRelayPort(62623)
//                .setClientLogin("web-socket-access-login")
//                .setClientPasscode("web-socket-access-password");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*");
    }
}
