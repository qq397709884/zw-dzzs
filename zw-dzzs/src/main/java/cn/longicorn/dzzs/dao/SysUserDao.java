package cn.longicorn.dzzs.dao;

import cn.longicorn.dzzs.entity.SysUser;
import cn.longicorn.modules.data.mybatis.StandardDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ywj on 2017/11/2.
 */
@Component
public interface SysUserDao extends StandardDao<SysUser, Long> {
    SysUser getUserByName(String username);

    /**
     * 查询所有权限
     *
     * @param userId
     * @return
     */
    List<String> queryAllPerms(Long userId);

    List<Long> queryAllMenuId(Long userId);

    void deleteBatch(Long[] userId);
}
