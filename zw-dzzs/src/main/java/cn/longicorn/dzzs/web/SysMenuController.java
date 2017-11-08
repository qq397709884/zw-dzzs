package cn.longicorn.dzzs.web;

import cn.longicorn.dzzs.entity.SysMenu;
import cn.longicorn.dzzs.entity.SysUser;
import cn.longicorn.dzzs.exception.DataException;
import cn.longicorn.dzzs.manager.SysMenuManager;
import cn.longicorn.dzzs.service.SysMenuService;
import cn.longicorn.dzzs.shiro.ShiroUtils;
import cn.longicorn.dzzs.util.Constant;
import cn.longicorn.dzzs.util.RspData;
import cn.longicorn.modules.data.ISearchParser;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.web.crud.DataTables;
import cn.longicorn.modules.web.crud.DataTablesSearchParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ywj on 2017/11/6.
 */
@Controller
@RequestMapping("/sys/menu")
public class SysMenuController {

    private static final Logger logger = LoggerFactory.getLogger(SysMenuController.class);

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysMenuManager sysMenuManager;

    @RequestMapping(method = RequestMethod.GET)
    public String toMenu() {
        return "sys/menuList";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAddMenu(Model model) {
        SysMenu menu = new SysMenu();
        menu.setParentId(0L);
        model.addAttribute("sysMenu",menu);
        return "sys/menuAdd";
    }

    @RequestMapping("/nav")
    @ResponseBody
    public ResponseEntity<?> nav() {
        Long userId = ShiroUtils.getUserId();
        return new ResponseEntity<Object>(sysMenuService.getUserMenuList(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/listData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listData(HttpServletRequest request) {
        logger.debug("controller /menu/listData called");
        ISearchParser<SysMenu> searchParser = new DataTablesSearchParser<>(request, "filter_");
        Page<SysMenu> page = searchParser.parse();
        page = sysMenuManager.searchPage(page);
        String sEcho = request.getParameter("sEcho");
        DataTables<SysMenu> dataTable = new DataTables<>();
        dataTable.setAaData(page.getResult());
        dataTable.setsEcho(StringUtils.isNotBlank(sEcho) ? Integer.parseInt(sEcho) : 0);
        dataTable.setiTotalRecords(page.getTotalCount());
        dataTable.setiTotalDisplayRecords(page.getTotalCount());
        logger.debug("this /menu/listData counts" + page.getResult().size());
        return new ResponseEntity<>(dataTable, HttpStatus.OK);
    }

    @RequestMapping("/select")
    @ResponseBody
    public ResponseEntity<?> select() {
        List<SysMenu> sysMenus = sysMenuManager.queryNotButton();
        //添加顶级菜单
        SysMenu root = new SysMenu();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        sysMenus.add(root);
        return new ResponseEntity<Object>(sysMenus, HttpStatus.OK);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public ResponseEntity<?> save(@RequestBody SysMenu menu) {
        //数据校验
        verifyForm(menu);
        try {
            sysMenuManager.save(menu);
            return new ResponseEntity<Object>(new RspData<>("00", "保存成功"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new RspData<>("01", "保存失败"), HttpStatus.OK);
        }
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenu menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new DataException("菜单名称不能为空");
        }

        if (menu.getParentId() == null) {
            throw new DataException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new DataException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenu parentMenu = sysMenuManager.getMenu(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new DataException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new DataException("上级菜单只能为菜单类型");
            }
            return;
        }
    }
}
