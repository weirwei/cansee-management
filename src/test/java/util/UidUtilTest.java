package util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.weirwei.cansee.util.UidUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author weirwei 2021/4/18 19:18
 */

public class UidUtilTest {
    @Test
    public void getUid() {
        String uid = UidUtil.getNewUserId();
        System.out.println(uid);
        String passwd = "123456";
        String encoded = DigestUtil.md5Hex(passwd);
        String encoded2 = DigestUtil.md5Hex(passwd);
        boolean flag = StringUtils.equals(encoded, encoded2);
        System.out.println(flag);

    }
}
