package cn.longicorn.dzzs.service;

import cn.longicorn.dzzs.entity.SysRole;
import cn.longicorn.dzzs.manager.SysRoleDeptManager;
import cn.longicorn.dzzs.manager.SysRoleManager;
import cn.longicorn.dzzs.manager.SysRoleMenuManager;
import cn.longicorn.dzzs.util.RspData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ywj on 2017/11/8.
 */
@Service
@Transactional(readOnly = true)
public class SysRoleService {

    @Autowired
    private SysRoleManager sysRoleManager;

    @Autowired
    private SysRoleMenuManager sysRoleMenuManager;

    @Autowired
    private SysRoleDeptManager sysRoleDeptManager;

    public RspData getRoleInfo(Long roleId) {
        SysRole role = sysRoleManager.get(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuManager.queryMenuIds(roleId);
        role.setMenuIdList(menuIdList);

        //查询角色对应的部门
        List<Long> deptIdList = sysRoleDeptManager.queryDeptIds(roleId);
        role.setDeptIdList(deptIdList);

        RspData rspData = new RspData();
        rspData.setData(role);

        return rspData;
    }

}
