package util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Period;

/**
 * @author weirwei 2021/5/29 17:11
 */
public class LocalDateTimeTest {
    @Test
    public void test() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime minus = today.minus(Period.ofDays(-1));
        System.out.println(minus);
        System.out.println(today);

    }
}
