package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.application.cache.RedisCache;
import bartos.lukasz.bookingservice.application.enums.RedisConstants;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class LoginAttemptService {

    //private LoadingCache<String, Integer> loginAttemptCache;

    private final RedisCache redisCache;

//    public LoginAttemptService() {
//        loginAttemptCache = CacheBuilder
//                .newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(100)
//                .build(new CacheLoader<>() {
//                    @Override
//                    public Integer load(String s) throws Exception {
//                        return 0;
//                    }
//                });
//    }

    public void eraseUserFromLoginAttemptCache(String username) {
        this.redisCache.remove(RedisConstants.USER_ATTEMPTS.name() + ":" + username);
//        loginAttemptCache.invalidate(username);
    }

    public void incrementAttemptInCache(String username) {
        if (this.redisCache.exist(RedisConstants.USER_ATTEMPTS.name() + ":" + username)) {
            this.redisCache.incrementByValue(RedisConstants.USER_ATTEMPTS.name() + ":" + username, 1L);
        } else {
            this.redisCache.set(RedisConstants.USER_ATTEMPTS.name() + ":" + username, "1");
        }

//        try {
//            loginAttemptCache.put(username,loginAttemptCache.get(username) + 1);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }

    public boolean hasExceededMaxAttempts(String username) {
//        try {
            if (this.redisCache.exist(RedisConstants.USER_ATTEMPTS.name() + ":" + username)) {
                return Integer.parseInt(this.redisCache.get(RedisConstants.USER_ATTEMPTS.name() + ":" + username)) >= 3;
            } else return false;
            //return loginAttemptCache.get(username) >= 8;
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return false;
    }
}
