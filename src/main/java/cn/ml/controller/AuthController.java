package cn.ml.controller;

import cn.ml.entity.LoginResult;
import cn.ml.entity.Result;
import cn.ml.entity.User;
import cn.ml.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Inject
    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public LoginResult register(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        if (username == null || password == null) {
            return LoginResult.failure("用户名或密码为空");
        }
        if (username.length() < 1 || username.length() > 15) {
            return LoginResult.failure("无效用户名");
        }
        if (password.length() < 1 || password.length() > 15) {
            return LoginResult.failure("无效密码");
        }
        try {
            userService.save(username, password);
            return LoginResult.success("注册成功!", false);
        } catch (DuplicateKeyException e) {
            return LoginResult.failure("用戶已注冊");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public LoginResult logout() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByName(name);
        if (loggedInUser == null) {
            return LoginResult.failure("用户没有登录");
        } else {
            SecurityContextHolder.clearContext();
            return LoginResult.success("注销成功", false);
        }
    }

    @GetMapping("/auth")
    @ResponseBody
    public LoginResult auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByName(authentication == null ? null : authentication.getName());
        if (loggedInUser == null) {
            return LoginResult.failure("用户没有登录");
        } else {
            return LoginResult.success("已登陆", loggedInUser);
        }
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public LoginResult loggedInUser(@RequestBody Map<String, Object> usernameAndPasswordJson, HttpServletRequest request) {
//        if (request.getHeader("user-agent") == null || !request.getHeader("user-agent").contains("Mozilla")) {
//            return LoginResult.failure("拒绝爬虫");
//        }
        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return LoginResult.failure("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return LoginResult.success("登录成功", userService.getUserByName(username));
        } catch (BadCredentialsException e) {
            return LoginResult.failure("密码不正确");
        }
    }
}
