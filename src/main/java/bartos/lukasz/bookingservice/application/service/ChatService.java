package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.application.dto.SocketChannelVariables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final LoggedAdminService loggedAdminService;
    private final SocketChannelControlService socketChannelControlService;

    public SocketChannelVariables endChat(String identifier) {
        // TU BĘDZIE PROBLEM CHYBA TEŻ. ZROBIĆ ZALEŻNOŚĆ, ŻE ZALEŻY KTO KOŃCZY TEN CZAT.
        String secondSideIdentifier = socketChannelControlService.get(identifier);

        SocketChannelVariables socketChannelVariables = null;

        if (secondSideIdentifier != null) {

            socketChannelControlService.remove(identifier);
            socketChannelControlService.remove(secondSideIdentifier);

            if (isAdminIdentifier(identifier)) {
                socketChannelVariables = SocketChannelVariables
                        .builder()
                        .adminIdentifier(identifier)
                        .userIdentifier(secondSideIdentifier)
                        .build();
            } else {
                socketChannelVariables = SocketChannelVariables
                        .builder()
                        .adminIdentifier(secondSideIdentifier)
                        .userIdentifier(identifier)
                        .build();
            }
        }
        return socketChannelVariables;
    }

    public boolean isAdminIdentifier(String identifier) {
        return loggedAdminService.isAdminInCache(identifier);
    }
}
