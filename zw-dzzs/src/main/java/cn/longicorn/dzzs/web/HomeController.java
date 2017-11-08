package cn.longicorn.dzzs.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ywj on 2017/11/3.
 */
@Controller
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping
    public String index(Model model) {
        return "index";
    }
    @RequestMapping(value = "main")
    public String main(){
        return "main/main";
    }
}
