package cn.longicorn.dzzs.web;

import cn.longicorn.dzzs.entity.SysMenu;
import cn.longicorn.dzzs.entity.SysRole;
import cn.longicorn.dzzs.manager.SysRoleManager;
import cn.longicorn.dzzs.service.SysRoleService;
import cn.longicorn.dzzs.util.RspData;
import cn.longicorn.modules.data.ISearchParser;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.web.crud.DataTables;
import cn.longicorn.modules.web.crud.DataTablesSearchParser;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysRoleEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ywj on 2017/11/7.
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleController.class);

    @Autowired
    private SysRoleManager sysRoleManager;

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/listData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listData(HttpServletRequest request) {
        logger.debug("controller /role/listData called");
        ISearchParser<SysRole> searchParser = new DataTablesSearchParser<>(request, "filter_");
        Page<SysRole> page = searchParser.parse();
        page = sysRoleManager.searchPage(page);
        String sEcho = request.getParameter("sEcho");
        DataTables<SysRole> dataTable = new DataTables<>();
        dataTable.setAaData(page.getResult());
        dataTable.setsEcho(StringUtils.isNotBlank(sEcho) ? Integer.parseInt(sEcho) : 0);
        dataTable.setiTotalRecords(page.getTotalCount());
        dataTable.setiTotalDisplayRecords(page.getTotalCount());
        logger.debug("this /role/listData counts" + page.getResult().size());
        return new ResponseEntity<>(dataTable, HttpStatus.OK);
    }

    /**
     * 角色信息
     */
    @RequestMapping("/info/{roleId}")
    @ResponseBody
    public RspData info(@PathVariable("roleId") Long roleId) {
        return sysRoleService.getRoleInfo(roleId);
    }


    /**
     * 保存角色
     */
    @RequestMapping("/save")
    @ResponseBody
    public RspData save(@RequestBody SysRole role) {
        try {
            sysRoleManager.create(role);
            return new RspData("00", "");
        } catch (Exception e) {
            return new RspData("00", "");
        }
    }

    /**
     * 修改角色
     */
    @RequestMapping("/update")
    @ResponseBody
    public RspData update(@RequestBody SysRole role) {
        try {
            sysRoleManager.update(role);
            return new RspData("00", "");
        } catch (Exception e) {
            return new RspData("00", "");
        }
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions("sys:role:delete")
    public RspData delete(@RequestBody Long[] roleIds) {
        try {
            if (roleIds != null && roleIds.length > 0) {
                for (Long roleId : roleIds) {
                    sysRoleManager.delete(roleId);
                }
            }
            return new RspData("00", "");
        } catch (Exception e) {
            return new RspData("00", "");
        }
    }
}
