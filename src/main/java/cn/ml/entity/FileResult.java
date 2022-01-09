package cn.ml.entity;

import javax.xml.crypto.Data;

public class FileResult extends Result{
    protected FileResult(ResultEnum status, String msg, Object data) {
        super(status, msg, data);
    }

    protected FileResult(String msg) {
        super(ResultEnum.FAILURE,msg, null);
    }

    protected FileResult(Object data) {
        super(ResultEnum.SUCCESSFUL, "上传成功", data);
    }

    public static FileResult success(Object data){
        return new FileResult(data);
    }

    public static FileResult failure(String msg){
        return new FileResult(msg);
    }
}
