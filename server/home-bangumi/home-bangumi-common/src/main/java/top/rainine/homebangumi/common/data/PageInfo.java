package top.rainine.homebangumi.common.data;

/**
 * @authoer rainine
 * @date 2024/4/30 01:15
 * @desc 分页信息
 */
public class PageInfo<T> {

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
    private Integer total;


    /**
     * 数据
     * */
    private T list;
}
