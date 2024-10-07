package top.rainine.homebangumi.core.settings.data.convertor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import top.rainine.homebangumi.api.common.HttpProxySettingsDto;
import top.rainine.homebangumi.api.common.Socks5ProxySettingsDto;
import top.rainine.homebangumi.api.req.*;
import top.rainine.homebangumi.api.resp.*;
import top.rainine.homebangumi.core.settings.data.*;

import java.time.Duration;
import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/5/10 00:07
 * @desc
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SystemSettingsConvertor {

    @Mapping(target = "networkProxyType", expression = "java(settings.proxyType().getType())")
    NetworkProxySettingsResp toNetworkProxySettingsResp(NetProxySettings settings);

    HttpProxySettingsDto toHttpProxySettingsDto(HttpProxySettings settings);

    Socks5ProxySettingsDto toSocks5ProxySettingsDto(Socks5ProxySettings settings);


    @Mapping(target = "proxyType", expression = "java(top.rainine.homebangumi.def.enums.NetProxyTypeEnum.of(req.getNetworkProxyType()))")
    NetProxySettings toNetProxySettings(UpdateNetworkProxySettingsReq req);

    HttpProxySettings toHttpProxySettings(HttpProxySettingsDto dto);

    Socks5ProxySettings toSocks5ProxySettings(Socks5ProxySettingsDto dto);

    QbittorrentDownloaderSettingsResp toQbittorrentDownloaderSettingsResp(QbittorrentDownloaderSettings settings);

    QbittorrentDownloaderSettings toQbittorrentDownloaderSettings(UpdateQbittorrentDownloaderSettingsReq req);

    EpisodeFilterRulesSettingsResp toEpisodeFilterRulesSettingsResp(EpisodeFilterRulesSettings settings);

    EpisodeFilterRulesSettings toEpisodeFilterRulesSettings(EpisodeFilterRulesSettingsReq req);

    @Mapping(target = "checkEpisodeDownloadStatusDuration", expression = "java(durationToMinutes(settings.checkEpisodeDownloadStatusDuration()))")
    @Mapping(target = "pushParsedEpisodesToDownloaderDuration", expression = "java(durationToMinutes(settings.pushParsedEpisodesToDownloaderDuration()))")
    @Mapping(target = "renameEpisodesDuration", expression = "java(durationToMinutes(settings.renameEpisodesDuration()))")
    @Mapping(target = "updateRssSubscriptionDuration", expression = "java(durationToMinutes(settings.updateRssSubscriptionDuration()))")
    @Mapping(target = "checkNotFinishedRenameTaskDuration", expression = "java(durationToMinutes(settings.checkNotFinishedRenameTaskDuration()))")
    ScheduledTaskSettingsResp toScheduledTaskSettingsResp(ScheduledTaskSettings settings);

    default Integer durationToMinutes(Duration duration) {
        if (Objects.isNull(duration)) {
            return null;
        }

        return Math.toIntExact(duration.toMinutes());
    }

    @Mapping(target = "checkEpisodeDownloadStatusDuration", expression = "java(minutesToDuration(req.getCheckEpisodeDownloadStatusDuration()))")
    @Mapping(target = "pushParsedEpisodesToDownloaderDuration", expression = "java(minutesToDuration(req.getPushParsedEpisodesToDownloaderDuration()))")
    @Mapping(target = "renameEpisodesDuration", expression = "java(minutesToDuration(req.getRenameEpisodesDuration()))")
    @Mapping(target = "updateRssSubscriptionDuration", expression = "java(minutesToDuration(req.getUpdateRssSubscriptionDuration()))")
    ScheduledTaskSettings toScheduledTaskSettings(UpdateScheduledTaskSettingsReq req);

    default Duration minutesToDuration(Integer minutes) {
        if (Objects.isNull(minutes)) {
            return null;
        }

        return Duration.ofMinutes(minutes);
    }

    WecomchanSettingsResp toWecomchanSettingsResp(WecomchanSettings settings);

    WecomchanSettings toWecomchanSettings(UpdateWecomchanSettingsReq req);

    EpisodeRenameTaskSettingsResp toEpisodeRenameTaskSettingsResp(EpisodeRenameTaskSettings settings);

    EpisodeRenameTaskSettings toEpisodeRenameTaskSettings(UpdateEpisodeRenameTaskSettingsReq req);
}
