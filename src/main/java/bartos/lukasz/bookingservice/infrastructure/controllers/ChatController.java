package bartos.lukasz.bookingservice.infrastructure.controllers;

import bartos.lukasz.bookingservice.application.annotations.CoveredControllerAdvice;
import bartos.lukasz.bookingservice.application.dto.SocketChannelVariables;
import bartos.lukasz.bookingservice.application.enums.ChatConstants;
import bartos.lukasz.bookingservice.application.service.ChatService;
import bartos.lukasz.bookingservice.application.service.LoggedAdminService;
import bartos.lukasz.bookingservice.application.service.SocketChannelControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@CoveredControllerAdvice
public class ChatController {

    private final SocketChannelControlService socketChannelControlService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;
    private final LoggedAdminService loggedAdminService;

    @PostMapping("/open-socket-channel")
    public ResponseEntity<SocketChannelVariables> chatStart(
            @RequestBody SocketChannelVariables socketChannelVariables
    ) {
        socketChannelControlService.add(socketChannelVariables.getUserIdentifier(), socketChannelVariables.getAdminIdentifier());
        socketChannelControlService.add(socketChannelVariables.getAdminIdentifier(), socketChannelVariables.getUserIdentifier());

        loggedAdminService.markAdminAsUnavailable(socketChannelVariables.getAdminIdentifier());

        return ResponseEntity.ok(socketChannelVariables);
    }

    @PostMapping("/close-socket-channel/{identifier}")
    public ResponseEntity<SocketChannelVariables> closeChat(
            @PathVariable String identifier
    ) {

        if (chatService.isAdminIdentifier(identifier)) {
            loggedAdminService.markAdminAsAvailable(identifier);
            simpMessageSendingOperations.convertAndSend("/topic/chat/" + socketChannelControlService.get(identifier), ChatConstants.SERVICE_MESSAGE_CHAT_HAS_BEEN_CLOSED.name());
        } else {
            String secondSide = socketChannelControlService.get(identifier);

            if (secondSide != null) {
                loggedAdminService.markAdminAsAvailable(secondSide);
                simpMessageSendingOperations.convertAndSend("/topic/chat/" + secondSide, ChatConstants.SERVICE_MESSAGE_CHAT_HAS_BEEN_CLOSED.name());
            }
        }

        return ResponseEntity.ok(this.chatService.endChat(identifier));
    }
}
