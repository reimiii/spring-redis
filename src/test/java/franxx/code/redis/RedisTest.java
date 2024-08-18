package franxx.code.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void redisTemplate() {
       assertThat(redisTemplate).isNotNull();
    }

    @Test
    void strings() throws InterruptedException {
       ValueOperations<String,String> opsForValue = redisTemplate.opsForValue(); 
       opsForValue.set("name", "mee", Duration.ofSeconds(2));

       assertThat("mee").isEqualTo(opsForValue.get("name"));
       Thread.sleep(Duration.ofSeconds(3));
       assertThat(opsForValue.get("name")).isNull();
    }


}
