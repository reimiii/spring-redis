package franxx.code.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

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

  @Test
  void list() {
    ListOperations<String, String> ops = redisTemplate.opsForList();
    ops.rightPush("names", "Hilmi");
    ops.rightPush("names", "Akbar");
    ops.rightPush("names", "Muharrom");

    assertThat(ops.leftPop("names")).isEqualTo("Hilmi");
    assertThat(ops.leftPop("names")).isEqualTo("Akbar");
    assertThat(ops.leftPop("names")).isEqualTo("Muharrom");
  }

  @Test
  void set() {
    SetOperations<String, String> ops = redisTemplate.opsForSet();
    ops.add("names", "Hilmi");
    ops.add("names", "Hilmi");
    ops.add("names", "Akbar");
    ops.add("names", "Akbar");
    ops.add("names", "Muharrom");
    ops.add("names", "Muharrom");
    ops.add("names", "Muharrom");

    assertThat(ops.members("names")).hasSize(3);
    assertThat(ops.members("names")).containsOnly("Hilmi", "Akbar", "Muharrom");

    redisTemplate.delete("names");
  }

  @Test
  void zSet() {
    ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
    zSet.add("ranking", "Hilmi", 200);
    zSet.add("ranking", "Malik", 100);
    zSet.add("ranking", "Dicky", 50);

    Set<String> ranking = zSet.range("ranking", 0, -1);
    assertThat(ranking).hasSize(3);
    assertThat(ranking).containsExactly("Dicky", "Malik", "Hilmi");

    assertThat(zSet.popMax("ranking").getValue()).isEqualTo("Hilmi");

  }
}
