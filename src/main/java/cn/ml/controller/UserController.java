package cn.ml.controller;

import cn.ml.entity.BlogResult;
import cn.ml.entity.Result;
import cn.ml.entity.UserResult;
import cn.ml.service.AuthService;
import cn.ml.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Inject
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Autowired

    @PatchMapping("/user")
    public UserResult updateUser(@RequestBody Map<String, Object> body) {
        try {
            return authService.getCurrentUser().
                    map(user -> {
                        user.setUsername((String) body.get("username"));
                        user.setAvatar((String) body.get("avatar"));
                        return UserResult.success(userService.updateUser(user));
                    })
                    .orElse(UserResult.failure("更新失败"));
        } catch (Exception e) {
            e.printStackTrace();
            return UserResult.failure(e.toString());
        }
    }
}
