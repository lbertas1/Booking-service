package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.application.exception.SocketChannelControlServiceException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SocketChannelControlService {

    private Cache<String, String> chatSocketControl;

    public SocketChannelControlService() {
        this.chatSocketControl = CacheBuilder.newBuilder().build();
    }

    public boolean add(String userIdentifier, String admin) {
        if (validator(List.of(userIdentifier, admin))) {
            chatSocketControl.put(userIdentifier, admin);
            return true;
        }
        return false;
    }

    public String get(String userIdentifier) {
        if (validator(List.of(userIdentifier))) {
            return chatSocketControl.getIfPresent(userIdentifier);
        }
        return null;
    }

    public boolean remove(String userIdentifier) {
        if (validator(List.of(userIdentifier))) {
            chatSocketControl.invalidate(userIdentifier);
            return true;
        }
        return false;
    }

    private boolean validator(List<String> arguments) {
        arguments
                .forEach(argument -> {
                    if (Objects.isNull(argument))
                        throw new SocketChannelControlServiceException("Add to cache operation failed with argument = " + argument, 400, HttpStatus.BAD_REQUEST);
                });
        return true;
    }
}
