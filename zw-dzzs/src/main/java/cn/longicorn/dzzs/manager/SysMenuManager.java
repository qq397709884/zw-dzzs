package cn.longicorn.dzzs.manager;

import cn.longicorn.dzzs.dao.SysMenuDao;
import cn.longicorn.dzzs.entity.SysMenu;
import cn.longicorn.dzzs.entity.SysUser;
import cn.longicorn.dzzs.util.CollectionUtil;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.data.mybatis.DynamicSearchBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ywj on 2017/11/3.
 */
@Service
@Transactional(readOnly = true)
public class SysMenuManager {

    @Autowired
    private SysMenuDao sysMenuDao;

    public List<SysMenu> getAllMenu() {
        return sysMenuDao.getAllMenu();
    }


    public Page<SysMenu> searchPage(Page<SysMenu> page) {
        DynamicSearchBuilder<SysMenu> dsb = new DynamicSearchBuilder<>(page);
        List<SysMenu> sysMenus = sysMenuDao.searchPage(dsb.build());
        if (CollectionUtil.isNotEmpty(sysMenus)) {
            for (SysMenu sysMenu : sysMenus) {
                SysMenu menu = sysMenuDao.get(sysMenu.getParentId());
                if (menu != null) {
                    sysMenu.setParentName(menu.getName());
                }
            }
        }
        page.setResult(sysMenus);
        return page;
    }

    public List<SysMenu> queryNotButton() {
        return sysMenuDao.queryNotButton();
    }

    public SysMenu getMenu(Long menuId) {
        return sysMenuDao.get(menuId);
    }

    @Transactional(readOnly = false)
    public void save(SysMenu menu) {
        sysMenuDao.save(menu);
    }
}
