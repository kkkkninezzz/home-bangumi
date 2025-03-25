package top.rainine.homebangumi.core.metadata.tmdb.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zhaoxin
 * @description tv详情
 * @date 2024/11/8 18:50:24
 */
@Getter
@Setter
@ToString
public class TmdbTvDetail {
    @SerializedName("id")
    private Integer id;

    @SerializedName("adult")
    private Boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("languages")
    private List<String> languages;

    @SerializedName("name")
    private String name;

    @SerializedName("number_of_seasons")
    private Integer numberOfSeasons;

    @SerializedName("number_of_episodes")
    private Integer numberOfEpisodes;

    @SerializedName("origin_country")
    private List<String> originCountry;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_name")
    private String originalName;


    @SerializedName("genre_ids")
    private List<Integer> genreIds;


    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("poster_path")
    private String posterPath;


    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Integer voteCount;
}
