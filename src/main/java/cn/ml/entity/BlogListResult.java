package cn.ml.entity;

import java.util.List;

public class BlogListResult extends Result<List<Blog>> {
    private Integer page;
    private Integer total;
    private Integer totalPage;

    protected BlogListResult(ResultEnum status, String msg, List<Blog> data, Integer page, Integer total, Integer totalPage) {
        super(status, msg, data);
        this.page = page;
        this.total = total;
        this.totalPage = totalPage;
    }

    public static BlogListResult success(List<Blog> data, Integer page, Integer total, Integer totalPage) {
        return new BlogListResult(ResultEnum.SUCCESSFUL, "获取成功", data, page, total, totalPage);
    }

    public static BlogListResult failure(String msg) {
        return new BlogListResult(ResultEnum.FAILURE, msg, null, 0, 0, 0);
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }
}
