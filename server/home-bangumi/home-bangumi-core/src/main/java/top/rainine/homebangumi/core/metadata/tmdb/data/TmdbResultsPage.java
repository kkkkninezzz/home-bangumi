package top.rainine.homebangumi.core.metadata.tmdb.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zhaoxin
 * @description
 * @date 2024/11/8 18:44:55
 */
@Getter
@Setter
@ToString
public class TmdbResultsPage<T> {

    @SerializedName("page")
    private Integer page;

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("total_results")
    private Integer totalResults;

    @SerializedName("results")
    private List<T> results;
}
