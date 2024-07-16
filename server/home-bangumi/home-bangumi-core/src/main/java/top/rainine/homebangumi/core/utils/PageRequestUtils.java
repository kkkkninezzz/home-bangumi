package top.rainine.homebangumi.core.utils;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import top.rainine.homebangumi.api.req.PagedReq;


/**
 * @authoer rainine
 * @date 2024/4/30 01:04
 * @desc
 */
public class PageRequestUtils {
    private PageRequestUtils() {}

    public static Pageable toPageable(PagedReq req) {
        return PageRequest.of(req.getCurrent() - 1, req.getPageSize());
    }

//    /**
//     * 分页查询
//     * */
//    public static <T> PageInfo<T> pagedList(Supplier<JPAQuery<T>> baseQuery, Pageable pageable, OrderSpecifier<?> o) {
//        JPAQuery<T> countQuery = baseQuery.get();
//        countQuery.fet
//    }
}
