package top.rainine.homebangumi.api.resp;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @authoer rainine
 * @date 2024/4/29 23:18
 * @desc 分页返回
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PagedResp<T> {
    /**
     * 当前页
     * */
    private Integer current;

    /**
     * 每页大小
     * */
    private Integer pageSize;

    /**
     * 总数
     * */
    private Long total;

    /**
     * 数据
     * */
    private List<T> list;

    public static <E> PagedResp<E> create() {
        return new PagedResp<>();
    }
}
