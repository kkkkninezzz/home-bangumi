package top.rainine.homebangumi.core.metadata.tmdb.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zhaoxin
 * @description tv季度信息
 * @date 2024/11/8 18:50:24
 */
@Getter
@Setter
@ToString
public class TmdbTvSeasonInfo {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("episode_count")
    private String episodeCount;

    @SerializedName("air_date")
    private String airDate;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("season_number")
    private Integer seasonNumber;

}
