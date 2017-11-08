package cn.longicorn.modules.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationHolderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetInstance() {
        assertNotNull(ConfigurationHolder.getInstance());
    }

    @Test
    public void testGetWithEscapeharacter() {
        boolean match = "13312345678".matches(ConfigurationHolder.getInstance().getProperty("sms.native.regexp"));
        assertEquals(true, match);
    }

    @Test
    public void testGetFireException() {
        try {
            ConfigurationHolder.getInstance().getBoolean("--NOT-EXSIT-KE--");
        } catch (Exception e) {
            org.junit.Assert.assertEquals(java.util.NoSuchElementException.class, e.getClass());
        }
    }

    @Test
    public void testGetInt() {
        assertEquals(new Integer(2), ConfigurationHolder.getInstance().getInteger("dbcp.minIdle"));
    }

    @Test
    public void testGetWithDefault() {
        assertEquals(new Integer(10), ConfigurationHolder.getInstance().getInteger("NOT-EXSIT-KEY", 10));
    }

    @Test
    public void testGetString() {
        assertEquals("root", ConfigurationHolder.getInstance().getProperty("jdbc.username"));
    }

    @Test
    public void testAtSpringProfileEnv() {
        System.setProperty("spring.profiles.active", "Production");
        ConfigurationHolder.reload();
        assertEquals("/data/logs", ConfigurationHolder.getInstance().getProperty("sys.logRoot"));
    }

    @Test
    public void testAtSpringProfileEnv2() {
        System.setProperty("spring.profiles.active", "Development");
        // 重新初始化ConfigurationHolder以支持上述变更
        ConfigurationHolder.reload();
        assertEquals("d:/data/logs", ConfigurationHolder.getInstance().getProperty("sys.logRoot"));
    }

    @Test
    public void testNestProperty() {
        assertEquals("平台微服务1", ConfigurationHolder.getInstance().getProperty("sys.name"));
    }
}
