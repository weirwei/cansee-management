package util;

import com.alibaba.fastjson.JSON;
import com.weirwei.cansee.mapper.dao.Log;
import com.weirwei.cansee.util.IdUtil;
import org.junit.Test;

/**
 * @author weirwei 2021/5/23 17:06
 */
public class produceMaker {
    @Test
    public void make() {

        Log log = new Log(IdUtil.getLogId(), "", Log.DEBUG, "华伟nb!",  this.getClass().toString(), "cansee");
        String s = JSON.toJSONString(log);
        System.out.println(s);
    }
}
