package cn.ml.entity;

public abstract class Result<T> {
    ResultEnum status;
    String msg;
    T data;

    public enum ResultEnum {
        SUCCESSFUL("success"),
        FAILURE("failure");

        private String status;

        ResultEnum(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    protected Result(ResultEnum status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ResultEnum getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}