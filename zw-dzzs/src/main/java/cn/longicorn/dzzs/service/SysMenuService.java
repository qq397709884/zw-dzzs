package cn.longicorn.dzzs.service;

import cn.longicorn.dzzs.dao.SysMenuDao;
import cn.longicorn.dzzs.dao.SysUserDao;
import cn.longicorn.dzzs.entity.SysMenu;
import cn.longicorn.dzzs.util.Constant;
import cn.longicorn.dzzs.util.R;
import cn.longicorn.dzzs.util.RspData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywj on 2017/11/6.
 */
@Service
public class SysMenuService {

    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysUserDao sysUserDao;

    public R getUserMenuList(Long userId) {
        if (userId == Constant.SUPER_ADMIN) {
            return R.ok().put("menuList", getAllMenuByMenuIds(null));
        }
        //用户菜单列表
        List<Long> menuIdList = sysUserDao.queryAllMenuId(userId);

        return R.ok().put("menuList", getAllMenuByMenuIds(menuIdList));
    }


    public List<SysMenu> queryByParentId(Long parentId) {
        return sysMenuDao.queryByParentId(parentId);
    }

    /**
     * 获取所有菜单列表
     *
     * @param menuIds
     * @return
     */
    private List<SysMenu> getAllMenuByMenuIds(List<Long> menuIds) {
        List<SysMenu> sysMenus = queryByParentId(0L, menuIds);
        getMenuTrees(sysMenus, menuIds);
        return sysMenus;
    }

    /**
     * 获取父id的菜单列表
     *
     * @param parentId
     * @param menuIds
     * @return
     */
    public List<SysMenu> queryByParentId(Long parentId, List<Long> menuIds) {
        List<SysMenu> sysMenus = queryByParentId(parentId);
        if (menuIds == null) {
            return sysMenus;
        }
        List<SysMenu> userMenus = new ArrayList<>();
        for (SysMenu menu : sysMenus) {
            if (menuIds.contains(menu.getMenuId())) {
                userMenus.add(menu);
            }
        }
        return userMenus;
    }

    /**
     * 递归
     *
     * @param menus
     * @param menuIds
     * @return
     */
    private List<SysMenu> getMenuTrees(List<SysMenu> menus, List<Long> menuIds) {
        List<SysMenu> subMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getType() == Constant.MenuType.CATALOG.getValue()) { //目录
                menu.setList(getMenuTrees(queryByParentId(menu.getMenuId(), menuIds), menuIds));
            }
            subMenus.add(menu);
        }
        return subMenus;
    }
}
