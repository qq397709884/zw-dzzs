package cn.longicorn.dzzs.web;

import cn.longicorn.dzzs.shiro.ShiroUtils;
import cn.longicorn.dzzs.util.RspData;
import org.apache.http.protocol.ResponseDate;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * Created by ywj on 2017/11/3.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login() {
        return "login";
    }


    @RequestMapping(value = "/sys/login",method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return new ResponseEntity<>(new RspData("01"), HttpStatus.OK);
        } catch (IncorrectCredentialsException e) {
            return new ResponseEntity<>(new RspData("01", "账号或密码不正确"), HttpStatus.OK);
        } catch (LockedAccountException e) {
            return new ResponseEntity<>(new RspData("01", "账号已被锁定,请联系管理员"), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new RspData("01", "账户验证失败"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new RspData("00", "登陆成功"), HttpStatus.OK);
    }

    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        ShiroUtils.logout();
        return "redirect:login";
    }
}
