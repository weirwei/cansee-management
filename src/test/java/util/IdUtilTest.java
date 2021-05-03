package util;

import cn.hutool.crypto.digest.DigestUtil;
import com.weirwei.cansee.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author weirwei 2021/4/18 19:18
 */

public class IdUtilTest {
    @Test
    public void getUid() {
        String uid = IdUtil.getUserId();
        System.out.println(uid);
        String passwd = "123456";
        String encoded = DigestUtil.md5Hex(passwd);
        String encoded2 = DigestUtil.md5Hex(passwd);
        boolean flag = StringUtils.equals(encoded, encoded2);
        System.out.println(flag);

    }
}
