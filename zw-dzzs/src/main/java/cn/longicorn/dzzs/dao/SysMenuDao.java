package cn.longicorn.dzzs.dao;

import cn.longicorn.dzzs.entity.SysMenu;
import cn.longicorn.modules.data.mybatis.StandardDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ywj on 2017/11/3.
 */
@Component
public interface SysMenuDao extends StandardDao<SysMenu, Long> {
    List<SysMenu> getAllMenu();

    List<SysMenu> queryByParentId(Long parentId);

    List<SysMenu> queryNotButton();
}
