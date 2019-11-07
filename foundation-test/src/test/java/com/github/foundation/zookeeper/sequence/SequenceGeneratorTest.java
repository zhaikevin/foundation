package com.github.foundation.zookeeper.sequence;

import com.github.foundation.zookeeper.ZookeeperTest;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description: 序列号生成器测试类
 * @Author: kevin
 * @Date: 2019/7/4 14:52
 */
public class SequenceGeneratorTest extends ZookeeperTest {

    @Autowired
    private SequenceGenerator sequenceGenerator;

    private String seqName = "foobar";

    @Test
    public void testNextInt() {
        int num = sequenceGenerator.nextInt(seqName);
        Assert.assertEquals(1, num);
        num = sequenceGenerator.nextInt(seqName);
        Assert.assertEquals(2, num);
    }

    @Test
    public void testNextCodeStringString() {
        String code = sequenceGenerator.nextCode(seqName, "foobar");
        Assert.assertEquals("foobar1", code);
        code = sequenceGenerator.nextCode(seqName, "foobar");
        Assert.assertEquals("foobar2", code);
    }

    @Test
    public void testNextCodeStringIntString() {
        String code = sequenceGenerator.nextCode(seqName, 4, "foobar");
        Assert.assertEquals("foobar0001", code);
        code = sequenceGenerator.nextCode(seqName, 4, "foobar");
        Assert.assertEquals("foobar0002", code);
    }

    @Test
    public void testNextCodeWithDateformat() {
        String dateStr = DateFormatUtils.format(new Date(), "yyyyMMdd");

        String code = sequenceGenerator.nextCodeWithDateformat(seqName, "yyyyMMdd", 4, "foobar");
        Assert.assertEquals("foobar" + dateStr + "0001", code);
        code = sequenceGenerator.nextCodeWithDateformat(seqName, "yyyyMMdd", 4, "foobar");
        Assert.assertEquals("foobar" + dateStr + "0002", code);
    }
}
