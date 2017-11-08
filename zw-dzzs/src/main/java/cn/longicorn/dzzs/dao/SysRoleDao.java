package cn.longicorn.dzzs.dao;

import cn.longicorn.dzzs.entity.SysRole;
import cn.longicorn.modules.data.mybatis.StandardDao;
import org.springframework.stereotype.Component;

/**
 * Created by ywj on 2017/11/3.
 */
@Component
public interface SysRoleDao extends StandardDao<SysRole, Long> {
}
