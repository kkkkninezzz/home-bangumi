package top.rainine.homebangumi.core.metadata.tmdb.data;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;

/**
 * @author zhaoxin
 * @description
 * @date 2024/11/8 18:56:48
 */
public class TmdbObjectRuntimeHints {

    public static void registerHints(RuntimeHints hints) {
        hints.reflection().registerType(TmdbResultsPage.class, MemberCategory.values());
        hints.reflection().registerType(TmdbTvSeries.class, MemberCategory.values());
        hints.reflection().registerType(TmdbTvSeriesResultsPage.class, MemberCategory.values());
        hints.reflection().registerType(TmdbMovieSeries.class, MemberCategory.values());
        hints.reflection().registerType(TmdbMovieSeriesResultsPage.class, MemberCategory.values());
    }
}
