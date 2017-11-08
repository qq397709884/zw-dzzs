package cn.longicorn.dzzs.web;

import cn.longicorn.dzzs.entity.SysDept;
import cn.longicorn.dzzs.manager.SysDeptManager;
import cn.longicorn.dzzs.shiro.ShiroUtils;
import cn.longicorn.dzzs.util.Constant;
import cn.longicorn.dzzs.util.RspData;
import cn.longicorn.modules.data.ISearchParser;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.web.crud.DataTables;
import cn.longicorn.modules.web.crud.DataTablesSearchParser;
import io.renren.modules.sys.entity.SysDeptEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 部门管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {

    private static final Logger logger = LoggerFactory.getLogger(SysDeptController.class);

    @Autowired
    private SysDeptManager sysDeptManager;

    @RequestMapping(value = "/listData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listData(HttpServletRequest request) {
        logger.debug("controller /dept/listData called");
        ISearchParser<SysDept> searchParser = new DataTablesSearchParser<>(request, "filter_");
        Page<SysDept> page = searchParser.parse();
        page = sysDeptManager.searchPage(page);
        String sEcho = request.getParameter("sEcho");
        DataTables<SysDept> dataTable = new DataTables<>();
        dataTable.setAaData(page.getResult());
        dataTable.setsEcho(StringUtils.isNotBlank(sEcho) ? Integer.parseInt(sEcho) : 0);
        dataTable.setiTotalRecords(page.getTotalCount());
        dataTable.setiTotalDisplayRecords(page.getTotalCount());
        logger.debug("this /dept/listData counts" + page.getResult().size());
        return new ResponseEntity<>(dataTable, HttpStatus.OK);
    }


    /**
     * 选择部门(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:dept:select")
    public RspData select() {
        List<SysDept> deptList = sysDeptManager.queryAll();

        //添加一级部门
        if (ShiroUtils.getUserId() == Constant.SUPER_ADMIN) {
            SysDept root = new SysDept();
            root.setDeptId(0L);
            root.setName("一级部门");
            root.setParentId(-1L);
            root.setOpen(true);
            deptList.add(root);
        }

        return new RspData("00");
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
    public RspData info() {
        long deptId = 0;
        if (ShiroUtils.getUserId() != Constant.SUPER_ADMIN) {
            SysDept dept = sysDeptManager.get(ShiroUtils.getUserEntity().getDeptId());
            deptId = dept.getParentId();
        }
        Map<String, Long> dept = new HashMap<>();
        dept.put("deptId", deptId);
        return new RspData("00");
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public RspData<SysDept> info(@PathVariable("deptId") Long deptId) {
        SysDept dept = sysDeptManager.get(deptId);
        RspData rspData = new RspData<>();
        rspData.setData(dept);
        return rspData;
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public RspData save(@RequestBody SysDept dept) {
        sysDeptManager.create(dept);
        return new RspData("00");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dept:update")
    public RspData update(@RequestBody SysDept dept) {
        sysDeptManager.update(dept);

         return new RspData("00");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public RspData delete(long deptId) {
        //判断是否有子部门
        List<Long> deptList = sysDeptManager.queryDetpIdList(deptId);
        if (deptList.size() > 0) {
            return new  RspData("01","请删除子部门");
        }

        sysDeptManager.delete(deptId);

        return new RspData("00","删除成功");
    }

}
