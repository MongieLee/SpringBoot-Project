package cn.ml.controller;

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
    public Result register(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        if (username == null || password == null) {
            return Result.failure("用戶名或密碼為空");
        }
        if (username.length() < 1 || username.length() > 15) {
            return Result.failure("無效用戶名");
        }
        if (password.length() < 1 || password.length() > 15) {
            return Result.failure("无效密码");
        }
        try {
            userService.save(username, password);
            return Result.success("注册成功!", false, null);
        } catch (DuplicateKeyException e) {
            return Result.failure("用戶已注冊");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByName(name);
        if (loggedInUser == null) {
            return Result.failure("用户没有登录");
        } else {
            SecurityContextHolder.clearContext();
            return Result.success("注销成功", false, null);
        }
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByName(authentication == null ? null : authentication.getName());
        if (loggedInUser == null) {
            return Result.failure("用户没有登录");
        } else {
            return Result.success("已登陆", true, loggedInUser);
        }
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result loggedInUser(@RequestBody Map<String, Object> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.failure("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return Result.success("登录成功", true, userService.getUserByName(username));
        } catch (BadCredentialsException e) {
            return Result.failure("密码不正确");
        }
    }
}
