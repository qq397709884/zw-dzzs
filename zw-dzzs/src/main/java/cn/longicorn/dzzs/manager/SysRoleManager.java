package cn.longicorn.dzzs.manager;

import cn.longicorn.dzzs.dao.SysRoleDao;
import cn.longicorn.dzzs.entity.SysMenu;
import cn.longicorn.dzzs.entity.SysRole;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.data.mybatis.DynamicSearchBuilder;
import cn.longicorn.modules.web.crud.StandardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ywj on 2017/11/8.
 */
@Service
@Transactional(readOnly = true)
public class SysRoleManager extends StandardManager<SysRole, Long> {

    @Autowired
    private SysRoleDao sysRoleDao;


    @Override
    public Page<SysRole> searchPage(Page<SysRole> page) {
        DynamicSearchBuilder<SysRole> dsb = new DynamicSearchBuilder<>(page);
        page.setResult(sysRoleDao.searchPage(dsb.build()));
        return page;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
        sysRoleDao.delete(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void create(SysRole sysRole) {
        sysRoleDao.save(sysRole);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(SysRole sysRole) {
        sysRoleDao.update(sysRole);
    }

    @Override
    public SysRole get(Long id) {
        return sysRoleDao.get(id);
    }
}
