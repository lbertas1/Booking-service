package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.application.cache.RedisCache;
import bartos.lukasz.bookingservice.application.enums.RedisConstants;
import bartos.lukasz.bookingservice.domain.user.User;
import bartos.lukasz.bookingservice.domain.user.UserRepository;
import bartos.lukasz.bookingservice.domain.user.enums.AdminAvailability;
import bartos.lukasz.bookingservice.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class LoggedAdminService {

    private final UserRepository userRepository;

    private final RedisCache redisCache;

    @PostConstruct
    private void loadAdmins() {
        this.userRepository
                .findAllByRole(Role.ROLE_ADMIN)
                .stream()
                .map(User::getUsername)
                .forEach(adminUsername ->
                        this.redisCache
                                .setnx(RedisConstants.ADMIN_USERNAME_CHAT.name() + ":" + adminUsername,
                                        AdminAvailability.UNAVAILABLE.name()));
    }

    //    private LoadingCache<String, AdminAvailability> loginAttemptCache;

//    public LoggedAdminService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//
//
//
//
////        Map<String, AdminAvailability> adminAvailabilityMap = this.userRepository
////                .findAllByRole(Role.ROLE_ADMIN)
////                .stream()
////                .map(User::getUsername)
////                .collect(Collectors.toMap(
////                        username -> username,
////                        username -> AdminAvailability.UNAVAILABLE
////                ));
////
////        loginAttemptCache = CacheBuilder
////                .newBuilder()
////                .build(new CacheLoader<String, AdminAvailability>() {
////                    @Override
////                    public AdminAvailability load(String s) throws Exception {
////                        return null;
////                    }
////                });
////
////        loginAttemptCache.putAll(adminAvailabilityMap);
//    }

    public boolean addUsernameToCache(String username) {
        return this.redisCache.setnx(RedisConstants.ADMIN_USERNAME_CHAT.name() + ":" + username, AdminAvailability.UNAVAILABLE.name()) > 0;
    }

//    public boolean addUsernameToCache(String username) {
//        if (!whetherUsernameIsIncluded(username)) {
//            loginAttemptCache.put(username, AdminAvailability.UNAVAILABLE);
//            return true;
//        } else return false;
//    }

//    private boolean whetherUsernameIsIncluded(String username) {
//        return loginAttemptCache.getIfPresent(username) != null;
//    }

    public void markAdminAsUnavailable(String username) {
        if (this.redisCache.exist(RedisConstants.ADMIN_USERNAME_CHAT.name() + ":" + username)) {
            this.redisCache.set(RedisConstants.ADMIN_USERNAME_CHAT.name() + ":" + username, AdminAvailability.UNAVAILABLE.name());
        }
    }

    public String markAdminAsAvailable(String username) {
        return this.redisCache
                .set(RedisConstants.ADMIN_USERNAME_CHAT.name() + ":" + username, AdminAvailability.AVAILABLE.name());
    }

    public List<String> getAllFreeAdmins() {
        List<String> collect1 = this.redisCache
                .getKeys(RedisConstants.ADMIN_USERNAME_CHAT.name() + ":" + "*")
                .stream()
                .collect(Collectors.toList());

        log.info("\n\n\n\n" + collect1.toString() + "\n\n\n\n");

        List<String> collect = collect1
                .stream()
                .filter(admin -> this.redisCache.get(admin).equals(AdminAvailability.AVAILABLE.name()))
                .map(admin -> admin.split(":")[1].replace("[", "").replace("]", ""))
                .collect(Collectors.toList());

        log.info("\n\n\n\n" + collect.toString() + "\n\n\n\n");

        return collect;

//        return loginAttemptCache
//                .asMap()
//                .entrySet()
//                .stream()
//                .filter(stringBooleanEntry -> stringBooleanEntry.getValue().equals(AdminAvailability.AVAILABLE))
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
    }

    public boolean isAdminInCache(String identifier) {
        return this.redisCache.exist(RedisConstants.ADMIN_USERNAME_CHAT.name() + ":" + identifier);

        //return loginAttemptCache.getIfPresent(identifier) != null;
    }
}