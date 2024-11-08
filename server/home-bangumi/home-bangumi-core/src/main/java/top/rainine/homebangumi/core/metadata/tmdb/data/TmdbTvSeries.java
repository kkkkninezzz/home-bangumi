package top.rainine.homebangumi.core.metadata.tmdb.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zhaoxin
 * @description
 * @date 2024/11/8 18:50:24
 */
@Getter
@Setter
@ToString
public class TmdbTvSeries {
    @SerializedName("adult")
    private Boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @SerializedName("origin_country")
    private List<String> originCountry;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_name")
    private String originalName;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("name")
    private String name;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Integer voteCount;
}
