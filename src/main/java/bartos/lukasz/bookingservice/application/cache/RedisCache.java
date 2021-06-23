package bartos.lukasz.bookingservice.application.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

@Service
public class RedisCache {

    @Value("${spring.profiles.active}")
    private String applicationProfile;

    private Jedis jedis;

    private final Environment environment;

    public RedisCache(Environment environment) {
        this.environment = environment;

        if (environment.getProperty("spring.profiles.active").equals("dev")) {
            this.jedis = new Jedis();
        } else {
            this.jedis = new Jedis("172.17.0.2", 6379);
        }
    }

    // user:1000:followers
    // object-type:id
    // comment:1234:reply.to
    // comment:1234:reply-to
    public String set(String key, String value) {
        return this.jedis.set(key, value);
    }

    public Long setnx(String key, String value) {
        return this.jedis.setnx(key, value);
    }

    public Boolean exist(String key) {
        return this.jedis.exists(key);
    }

    public Set<String> getKeys(String pattern) {
        return this.jedis.keys(pattern);
    }

    public String get(String key) {
        return this.jedis.get(key);
    }

    public Long remove(String key) {
        return this.jedis.del(key);
    }

    public void incrementByValue(String key, Long value) {
        this.jedis.incrBy(key, value);
    }
}