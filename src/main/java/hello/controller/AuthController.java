package hello.controller;

import hello.entity.User;
import hello.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
            return new Result("fail", "用戶名或密碼為空", false);
        }
        if (username.length() < 1 || username.length() > 15) {
            return new Result("fail", "無效用戶名", false);
        }
        if (password.length() < 1 || password.length() > 15) {
            return new Result("fail", "無效密碼", false);
        }
        User user = userService.getUserByName(username);
        if (user == null) {
            userService.save(username, password);
            return new Result("ok", "success!", false);
        } else {
            return new Result("fail", "用戶已注冊", false);
        }
    }


    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByName(name);
        if (loggedInUser == null) {
            return new Result("ok", "用户没有登录", false);
        } else {
            SecurityContextHolder.clearContext();
            return new Result("ok", "注銷成功", true);
        }
    }


    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByName(name);
        if (loggedInUser == null) {
            return new Result("ok", "用户没有登录", false);
        } else {
            return new Result("ok", null, true, loggedInUser);
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
            return new Result("fail", "用户不存在", false);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            // 把用户信息保存在内存中的某一个地方
            // cookie
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("ok", "登录", true, userService.getUserByName(username));
        } catch (BadCredentialsException e) {
            return new Result("fail", "密码不正确", false);
        }
    }

    private static class Result {
        String status;
        String msg;
        boolean isLogin;
        Object data;

        public Result(String status, String msg, boolean isLogin) {
            this(status, msg, isLogin, null);
        }

        public Result(String status, String msg, boolean isLogin, Object data) {
            this.status = status;
            this.msg = msg;
            this.isLogin = isLogin;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public void setLogin(boolean login) {
            isLogin = login;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
