package cn.ml.entity;

public class Result {
    String status;
    String msg;
    boolean isLogin;
    Object data;

    public static Result failure(String message) {
        return new Result("fail", message, false);
    }

    public static Result success(String message, boolean isLogin, Object data) {
        return new Result("ok", message, isLogin, data);
    }

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