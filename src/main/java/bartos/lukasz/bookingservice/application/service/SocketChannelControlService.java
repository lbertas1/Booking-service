package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.application.cache.RedisCache;
import bartos.lukasz.bookingservice.application.enums.RedisConstants;
import bartos.lukasz.bookingservice.application.exception.SocketChannelControlServiceException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class SocketChannelControlService {

    private final RedisCache redisCache;

//    private Cache<String, String> chatSocketControl;
//
//    public SocketChannelControlService() {
//        this.chatSocketControl = CacheBuilder.newBuilder().build();
//    }

    private boolean validator(List<String> arguments) {
        arguments
                .forEach(argument -> {
                    if (Objects.isNull(argument))
                        throw new SocketChannelControlServiceException("Add to cache operation failed with argument = " + argument, 400, HttpStatus.BAD_REQUEST);
                });
        return true;
    }

    public boolean add(String userIdentifier, String admin) {
        log.info("\n\n\nADD USER: " + userIdentifier + "\n\n\n ADMIN:" + admin + "\n\n\n");

        if (validator(List.of(userIdentifier, admin))) {
            this.redisCache
                    .set(RedisConstants.USER_IDENTIFIER.name() + ":" + userIdentifier,
                            RedisConstants.ADMIN_IDENTIFIER.name() + ":" + admin);

            this.redisCache
                    .set(RedisConstants.ADMIN_IDENTIFIER.name() + ":" + admin,
                            RedisConstants.USER_IDENTIFIER.name() + ":" + userIdentifier);



            //chatSocketControl.put(userIdentifier, admin);
            return true;
        }
        return false;
    }

    public String get(String userIdentifier) {
        log.info("\n\n\nADD " + userIdentifier + "\n\n\n");
        if (validator(List.of(userIdentifier))) {
            return this.redisCache.get(userIdentifier);
            //return chatSocketControl.getIfPresent(userIdentifier);
        }
        return null;
    }

//    public String get(String userIdentifier) {
//        log.info("\n\n\nADD " + userIdentifier + "\n\n\n");
//        if (validator(List.of(userIdentifier))) {
//            return this.redisCache.get(RedisConstants.USER_IDENTIFIER.name() + ":" + userIdentifier);
//            //return chatSocketControl.getIfPresent(userIdentifier);
//        }
//        return null;
//    }

    public boolean remove(String identifier) {
        if (validator(List.of(identifier))) {
            return this.redisCache.remove(identifier) > 0;

            //chatSocketControl.invalidate(userIdentifier);
        }
        return false;
    }

}