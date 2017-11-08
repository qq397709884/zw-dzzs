package cn.longicorn.dzzs.web;

import cn.longicorn.dzzs.entity.SysUser;
import cn.longicorn.dzzs.manager.SysUserManager;
import cn.longicorn.dzzs.shiro.ShiroUtils;
import cn.longicorn.dzzs.util.RspData;
import cn.longicorn.modules.data.ISearchParser;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.web.crud.DataTables;
import cn.longicorn.modules.web.crud.DataTablesSearchParser;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static cn.longicorn.dzzs.shiro.ShiroUtils.getUserId;

/**
 * Created by ywj on 2017/11/6.
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserManager sysUserManager;

    /**
     * 加载列表数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listData(HttpServletRequest request) {
        logger.debug("controller /user/listData called");
        ISearchParser<SysUser> searchParser = new DataTablesSearchParser<>(request, "filter_");
        Page<SysUser> page = searchParser.parse();
        page = sysUserManager.searchPage(page);
        String sEcho = request.getParameter("sEcho");
        DataTables<SysUser> dataTable = new DataTables<>();
        dataTable.setAaData(page.getResult());
        dataTable.setsEcho(StringUtils.isNotBlank(sEcho) ? Integer.parseInt(sEcho) : 0);
        dataTable.setiTotalRecords(page.getTotalCount());
        dataTable.setiTotalDisplayRecords(page.getTotalCount());
        logger.debug("this /user/listData counts" + page.getResult().size());
        return new ResponseEntity<>(dataTable, HttpStatus.OK);
    }

    @RequestMapping()
    public String toUserList() {
        return "sys/userList";
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public ResponseEntity<?> info() {
        return new ResponseEntity<Object>(sysUserManager.get(getUserId()), HttpStatus.OK);
    }

    /**
     * 保存用户
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public RspData save(@RequestBody SysUser user) {
        try {
            sysUserManager.create(user);
            return new RspData("00");
        } catch (Exception e) {
            return new RspData("01");
        }
    }

    /**
     * 修改用户
     */
    @RequestMapping("/update")
    public RspData update(@RequestBody SysUser user) {
        try {
            sysUserManager.update(user);
            return new RspData("00");
        } catch (Exception e) {
            return new RspData("01");
        }
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    public RspData delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return new RspData("01", "管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, ShiroUtils.getUserId())) {
            return new RspData("01", "当前用户不能删除");
        }

        sysUserManager.deleteBatch(userIds);

        return new RspData("00", "删除成功");
    }
}
