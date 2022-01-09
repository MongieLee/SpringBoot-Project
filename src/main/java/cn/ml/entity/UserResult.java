package cn.ml.entity;

public class UserResult extends Result<User> {
    protected UserResult(Result.ResultEnum status, String msg, User data) {
        super(status, msg, data);
    }

    protected UserResult(String msg) {
        super(Result.ResultEnum.FAILURE, msg, null);
    }

    protected UserResult(User user) {
        super(Result.ResultEnum.SUCCESSFUL, "更新成功", user);
    }

    public static UserResult success(User user) {
        return new UserResult(user);
    }

    public static UserResult failure(String msg) {
        return new UserResult(msg);
    }
}
