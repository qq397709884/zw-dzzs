import cn.longicorn.dzzs.dao.SysUserDao;
import cn.longicorn.dzzs.entity.SysUser;
import cn.longicorn.dzzs.manager.SysUserManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ywj on 2017/11/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test.xml")
public class SysUserTest {

    private static final Logger logger = LoggerFactory.getLogger(SysUserTest.class);

    @Autowired
    private SysUserManager sysUserManager;

    @Test
    public void get() {
        Long id = 1L;
        SysUser sysUser = sysUserManager.get(id);
        if (sysUser != null) {
            System.out.println(sysUser.getUsername());
        }
    }

//    @Test
//    public void save() {
//        SysUser sysUser = new SysUser();
//        sysUser.setUsername("wangwu");
//        sysUser.setEmail("397709884@qq.com");
//        sysUser.setMobile("13164681020");
//        String salt = RandomStringUtils.randomAlphanumeric(20);
//        sysUser.setSalt(salt);
//        SimpleHash simpleHash = new SimpleHash("SHA-256", "123456", salt, 16);
//        sysUser.setPassword(simpleHash.toString());
//        sysUser.setStatus(1);
//        sysUser.setDeptId(1L);
//        sysUserDao.save(sysUser);
//        logger.info("插入用户名成功，用户ID为:{}", sysUser.getUserId());
//    }
//
//    @Test
//    public void update() {
//        SysUser sysUser = new SysUser();
//        sysUser.setEmail("1355468462@qq.com");
//        sysUser.setMobile("13554489831");
//        sysUser.setUserId(2L);
//        sysUserDao.update(sysUser);
//        logger.info("更新用户名成功");
//    }
//
//    @Test
//    public void delete(){
//        sysUserDao.delete(2L);
//    }
}
