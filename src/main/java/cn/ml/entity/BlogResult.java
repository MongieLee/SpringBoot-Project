package cn.ml.entity;

public class BlogResult extends Result<Blog> {
    protected BlogResult(ResultEnum status, String msg, Blog data) {
        super(status, msg, data);
    }

    public static BlogResult success(String msg, Blog blog) {
        return new BlogResult(ResultEnum.SUCCESSFUL, msg, blog);
    }

    public static BlogResult failure(String msg) {
        return new BlogResult(ResultEnum.FAILURE, msg, null);
    }

    public static BlogResult success(String msg) {
        return new BlogResult(ResultEnum.SUCCESSFUL, msg, null);
    }
}
