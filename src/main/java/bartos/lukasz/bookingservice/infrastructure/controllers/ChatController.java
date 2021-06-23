package bartos.lukasz.bookingservice.infrastructure.controllers;

import bartos.lukasz.bookingservice.application.annotations.CoveredControllerAdvice;
import bartos.lukasz.bookingservice.application.dto.SocketChannelVariables;
import bartos.lukasz.bookingservice.application.enums.ChatConstants;
import bartos.lukasz.bookingservice.application.enums.RedisConstants;
import bartos.lukasz.bookingservice.application.service.ChatService;
import bartos.lukasz.bookingservice.application.service.LoggedAdminService;
import bartos.lukasz.bookingservice.application.service.SocketChannelControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "/users")
public class ChatController {

    private final SocketChannelControlService socketChannelControlService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;
    private final LoggedAdminService loggedAdminService;

    @ApiOperation(value = """
            Method to open the chat. Returns the IDs of both sides.""")
    @PostMapping("/open-socket-channel")
    public ResponseEntity<SocketChannelVariables> chatStart(
            @RequestBody SocketChannelVariables socketChannelVariables
    ) {
        socketChannelControlService.add(socketChannelVariables.getUserIdentifier(), socketChannelVariables.getAdminIdentifier());
        loggedAdminService.markAdminAsUnavailable(socketChannelVariables.getAdminIdentifier());

        return ResponseEntity.ok(socketChannelVariables);
    }

    @ApiOperation(value = """
            Method to close the chat. Returns the IDs of both sides.""")
    @PostMapping("/close-socket-channel/{identifier}")
    public ResponseEntity<SocketChannelVariables> closeChat(
            @PathVariable String identifier
    ) {

        log.info("\n\n\n\n WLAZŁEM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!================================== \n\n\n\n\n");

        if (chatService.isAdminIdentifier(identifier)) {
            log.info("\n\n\n\n WLAZŁEM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!==================================111111111111111 \n\n\n\n\n");
            String userIdentifier = socketChannelControlService.get(RedisConstants.ADMIN_IDENTIFIER.name() + ":" + identifier);
            log.info("\n\n\n\n WLAZŁEM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!==================================33333333333333333 \n\n\n\n\n");
            log.info("\n\n\n 1 USER IFNTIFIER: " + userIdentifier + "\n\n\n" + "ADMIN IDENTIFIER: " + identifier + "\n\n\n\n");
            removeFromCache(identifier, userIdentifier.split(":")[1]);
            loggedAdminService.markAdminAsAvailable(identifier);
            simpMessageSendingOperations.convertAndSend("/topic/chat/" + socketChannelControlService.get(identifier).split(":")[1],
                    ChatConstants.SERVICE_MESSAGE_CHAT_HAS_BEEN_CLOSED.name());
        } else {
            log.info("\n\n\n\n WLAZŁEM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!==================================222222222222222222222 \n\n\n\n\n");
            String adminIdentifier = socketChannelControlService.get(RedisConstants.USER_IDENTIFIER.name() + ":" +identifier);
            log.info("\n\n\n\n WLAZŁEM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!==================================444444444444444444 \n\n\n\n\n");



            // REAKCJA NA WYŁĄCZENIE CZATU NAJPIERW PRZEZ OBSŁUGĘ????? I POWIADOMIENIE UŻYTKOWNIKA O TYM??????????????




            if (adminIdentifier != null) {
                log.info("\n\n\n 2 USER IFNTIFIER: " + identifier + "\n\n\n" + "ADMIN IDENTIFIER: " + adminIdentifier + "\n\n\n\n");
                removeFromCache(adminIdentifier.split(":")[1], identifier);
                simpMessageSendingOperations.convertAndSend("/topic/chat/" + adminIdentifier.split(":")[1],
                        ChatConstants.SERVICE_MESSAGE_CHAT_HAS_BEEN_CLOSED.name());
            }
        }

        return ResponseEntity.ok(this.chatService.endChat(identifier));
    }

    private void removeFromCache(String admin, String user) {
        socketChannelControlService.remove(RedisConstants.ADMIN_IDENTIFIER.name() + ":" + admin);
        socketChannelControlService.remove(RedisConstants.USER_IDENTIFIER.name() + ":" + user);
    }
}