package top.rainine.homebangumi.core.rss.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.api.resp.RssBangumiStatisticOnHomeResp;
import top.rainine.homebangumi.api.resp.UpdatedBangumiDto;
import top.rainine.homebangumi.api.resp.UpdatedBangumisResp;
import top.rainine.homebangumi.api.resp.UpdatedDate2BangumisDto;
import top.rainine.homebangumi.common.utils.HbDateUtils;
import top.rainine.homebangumi.dao.po.HbBangumi;
import top.rainine.homebangumi.dao.po.HbRssBangumi;
import top.rainine.homebangumi.dao.po.QHbRssBangumiEpisode;
import top.rainine.homebangumi.dao.repository.HbRssBangumiEpisodeRepository;
import top.rainine.homebangumi.dao.repository.HbRssBangumiRepository;
import top.rainine.homebangumi.dao.utils.JpaUtils;
import top.rainine.homebangumi.def.enums.RssBangumiEpisodeStatusEnum;
import top.rainine.homebangumi.def.enums.RssHandleMethodEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @authoer rainine
 * @date 2024/5/19 23:30
 * @desc 统计相关的逻辑
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RssBangumiStatisticComponent {

    private final HbRssBangumiEpisodeRepository episodeRepository;

    private final HbRssBangumiRepository rssBangumiRepository;

    private final JPAQueryFactory jpaQueryFactory;

    private final RssBangumiComponent rssBangumiComponent;

    private static final List<Integer> FAILED_EPISODE_STATUS_LIST = List.of(
            RssBangumiEpisodeStatusEnum.TITLE_PARSE_FAILED.getStatus(),
            RssBangumiEpisodeStatusEnum.TORRENT_DOWNLOAD_FAILED.getStatus(),
            RssBangumiEpisodeStatusEnum.TORRENT_PARSE_FAILED.getStatus(),
            RssBangumiEpisodeStatusEnum.TORRENT_STORED_FAILED.getStatus(),
            RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOAD_FAILED.getStatus(),
            RssBangumiEpisodeStatusEnum.RENAME_FAILED.getStatus(),
            RssBangumiEpisodeStatusEnum.ERROR.getStatus()
    );


    public RssBangumiStatisticOnHomeResp statisticOnHome() {
        LocalDateTime now = HbDateUtils.now();
        long nowMills = HbDateUtils.toMills(now);
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        long startOfTodayMills = HbDateUtils.toMills(startOfToday);
        LocalDateTime startOfWeek = HbDateUtils.getStartOfWeek(startOfToday);

        long downloadingEpisodesCount = episodeRepository.countByStatus(RssBangumiEpisodeStatusEnum.EPISODE_DOWNLOADING.getStatus());
        long failedEpisodesCount = episodeRepository.countByCreatedTimeGreaterThanEqualAndStatusIn(HbDateUtils.toMills(startOfWeek), FAILED_EPISODE_STATUS_LIST);

        long updatedBangumiCountInWeek = countUpdatedBangumi(HbDateUtils.toMills(startOfWeek));
        long updatedBangumiCountToday = countUpdatedBangumi(startOfTodayMills);

        LocalDateTime startOfQuarter = HbDateUtils.getStartOfQuarter(now);
        LocalDateTime startOfPreQuarter = HbDateUtils.getStartOfPreQuarter(now);
        long startOfQuarterMills = HbDateUtils.toMills(startOfQuarter);
        long subscriptionsCountInCurQuarter = countSubscriptionBangumi(startOfQuarterMills, nowMills);
        long subscriptionsCountInPreQuarter = countSubscriptionBangumi(HbDateUtils.toMills(startOfPreQuarter), startOfQuarterMills);

        long collectedBangumiCount = countCollectBangumi(null, null);
        long collectedBangumiCountToday = countCollectBangumi(startOfTodayMills, nowMills);

        RssBangumiStatisticOnHomeResp resp = new RssBangumiStatisticOnHomeResp();
        resp.setDownloadingEpisodesCount(downloadingEpisodesCount)
                .setFailedEpisodesCount(failedEpisodesCount)
                .setUpdatedBangumiCountInWeek(updatedBangumiCountInWeek)
                .setUpdatedBangumiCountToday(updatedBangumiCountToday)
                .setSubscriptionsCountInCurQuarter(subscriptionsCountInCurQuarter)
                .setSubscriptionsCountInPreQuarter(subscriptionsCountInPreQuarter)
                .setCollectedBangumiCount(collectedBangumiCount)
                .setCollectedBangumiCountToday(collectedBangumiCountToday);
        return resp;
    }

    /**
     * 统计更新了的番剧数量
     * */
    private long countUpdatedBangumi(long startTime) {
        QHbRssBangumiEpisode qHbRssBangumiEpisode = QHbRssBangumiEpisode.hbRssBangumiEpisode;

        BooleanExpression where = JpaUtils.and(qHbRssBangumiEpisode.status.eq(RssBangumiEpisodeStatusEnum.FINISHED.getStatus()),
                qHbRssBangumiEpisode.createdTime.gt(startTime));
        JPAQuery<Long> jpaQuery = jpaQueryFactory.selectDistinct(qHbRssBangumiEpisode.rssBangumiId.count())
                .from(qHbRssBangumiEpisode)
                .where(where);

        return Optional.ofNullable(jpaQuery.fetchOne())
                .orElse(0L);
    }

    /**
     * 统计订阅了的番剧
     * */
    private long countSubscriptionBangumi(Long startTime, Long endTime) {
        return rssBangumiRepository.countByHandleMethodAndCreatedTimeBetween(RssHandleMethodEnum.SUBSCRIBE.getMethod(), startTime, endTime);
    }

    /**
     * 统计订阅了的番剧
     * */
    private long countCollectBangumi(Long startTime, Long endTime) {
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            return rssBangumiRepository.countByHandleMethodAndCreatedTimeBetween(RssHandleMethodEnum.COLLECT.getMethod(), startTime, endTime);
        }

        return rssBangumiRepository.countByHandleMethod(RssHandleMethodEnum.COLLECT.getMethod());
    }

    public UpdatedBangumisResp getUpdatedBangumisForWeek() {
        // 7天之内的时间
        int daysAgo = 6;

        LocalDate startLocalDate = HbDateUtils.now().plusDays(-daysAgo).toLocalDate();
        long startTime = HbDateUtils.toMills(startLocalDate.atStartOfDay());

        QHbRssBangumiEpisode qHbRssBangumiEpisode = QHbRssBangumiEpisode.hbRssBangumiEpisode;
        NumberExpression<Integer> maxEpisodeNoPath = qHbRssBangumiEpisode.episodeNo.max().as("maxEpisodeNo");
        NumberExpression<Long> maxCreatedTimePath = qHbRssBangumiEpisode.createdTime.max().as("maxCreatedTime");
        BooleanExpression where = JpaUtils.and(qHbRssBangumiEpisode.createdTime.goe(startTime), qHbRssBangumiEpisode.status.eq(RssBangumiEpisodeStatusEnum.FINISHED.getStatus()));
        List<Tuple> tuples = jpaQueryFactory.select(qHbRssBangumiEpisode.rssBangumiId, maxEpisodeNoPath, maxCreatedTimePath)
                .from(qHbRssBangumiEpisode)
                .where(where)
                .groupBy(qHbRssBangumiEpisode.rssBangumiId)
                .orderBy(qHbRssBangumiEpisode.createdTime.desc())
                .fetch();

        LocalDateTime startOfToday = HbDateUtils.getStartOfDay();
        // 本周的开始时间
        LocalDateTime startOfWeek = HbDateUtils.getStartOfWeek(startOfToday);
        List<UpdatedBangumiDto> updatedBangumiDtos = tuples.stream()
                .map(tuple -> {
                    Long rssBangumiId = tuple.get(qHbRssBangumiEpisode.rssBangumiId);
                    Integer maxEpisodeNo = tuple.get(maxEpisodeNoPath);
                    Long maxCreatedTime = tuple.get(maxCreatedTimePath);
                    HbRssBangumi hbRssBangumi = rssBangumiComponent.getRssBangumiOrThrow(rssBangumiId);

                    HbBangumi hbBangumi = rssBangumiComponent.getBangumiOrThrow(hbRssBangumi.getBangumiId());

                    Objects.requireNonNull(maxCreatedTime);
                    Boolean delay = null;
                    Integer actualBroadcastDayOfWeek = null;
                    // 只有订阅类型的番剧，才需要关注实际更新的时间
                    if (Objects.equals(hbRssBangumi.getHandleMethod(), RssHandleMethodEnum.SUBSCRIBE.getMethod())) {
                        LocalDateTime createdDate = HbDateUtils.getStartOfDay(HbDateUtils.now(maxCreatedTime));
                        actualBroadcastDayOfWeek = createdDate.getDayOfWeek().getValue();

                        delay = Optional.ofNullable(hbBangumi.getBroadcastDayOfWeek())
                            .map(broadcastDayOfWeek -> {
                                // 本周预计播放的日期
                                LocalDateTime expectedBroadcastDate = startOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(broadcastDayOfWeek)));
                                // 如果本周预计播放的时间比今天时间晚，说明还没到播放时间，把时间修改为上周
                                if (expectedBroadcastDate.isAfter(startOfToday)) {
                                    expectedBroadcastDate = expectedBroadcastDate.plusDays(-daysAgo);
                                }

                                // 如果创建时间比预计播放时间早，说明也是延迟的
                                // 剧集延迟一天进行下载不视为延迟
                                return createdDate.isBefore(expectedBroadcastDate) || createdDate.isAfter(expectedBroadcastDate.plusDays(1));
                            }).orElse(false);
                    }


                    UpdatedBangumiDto updatedBangumiDto = new UpdatedBangumiDto();
                    updatedBangumiDto.setRssBangumiId(rssBangumiId)
                            .setLatestEpisodeNo(maxEpisodeNo)
                            .setLatestUpdatedTime(maxCreatedTime)
                            .setBangumiTitle(hbBangumi.getTitle())
                            .setBangumiPosterUrl(hbBangumi.getPosterUrl())
                            .setDelay(delay)
                            .setExpectedBroadcastDayOfWeek(hbBangumi.getBroadcastDayOfWeek())
                            .setActualBroadcastDayOfWeek(actualBroadcastDayOfWeek);
                    return updatedBangumiDto;
                }).toList();

        Map<LocalDate, List<UpdatedBangumiDto>> date2UpdatedBangumiMap = updatedBangumiDtos.stream()
                .collect(Collectors.groupingBy(updatedBangumiDto -> {
                    LocalDateTime localDateTime = HbDateUtils.now(updatedBangumiDto.getLatestUpdatedTime());
                    return localDateTime.toLocalDate();
                }));

        // 填充缺失的日期
        for (int i = 0; i <= daysAgo; i++) {
            LocalDate localDate = startLocalDate.plusDays(i);
            if (!date2UpdatedBangumiMap.containsKey(localDate)) {
                date2UpdatedBangumiMap.put(localDate, new ArrayList<>());
            }
        }

        List<UpdatedDate2BangumisDto> list = date2UpdatedBangumiMap.entrySet()
                .stream().map(entry -> {
                    UpdatedDate2BangumisDto dto = new UpdatedDate2BangumisDto();
                    dto.setDate(HbDateUtils.getStartOfDayMills(entry.getKey()))
                            .setDayOfWeek(entry.getKey().getDayOfWeek().getValue())
                            .setBangumis(entry.getValue());
                    return dto;
                }).sorted(Comparator.comparingLong(UpdatedDate2BangumisDto::getDate).reversed())
                .toList();
        UpdatedBangumisResp resp = new UpdatedBangumisResp();
        resp.setBangumis(list);
        return resp;
    }
}



























