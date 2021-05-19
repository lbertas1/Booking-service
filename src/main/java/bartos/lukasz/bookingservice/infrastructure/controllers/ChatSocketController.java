package bartos.lukasz.bookingservice.infrastructure.controllers;

import bartos.lukasz.bookingservice.application.service.SocketChannelControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SocketChannelControlService socketChannelControlService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/chat/{sender}")
    public void chat(
            @DestinationVariable("sender") String sender,
            String message
    ) {

        String recipient = socketChannelControlService.get(sender);
        simpMessageSendingOperations.convertAndSend("/topic/chat/" + recipient, message);
    }
}
