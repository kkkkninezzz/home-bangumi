package top.rainine.homebangumi.core.runtimehints;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import top.rainine.homebangumi.common.utils.PackageClassFinder;
import top.rainine.homebangumi.def.enums.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/7/12 00:45
 * @desc 自定义枚举的提示
 */
@ImportRuntimeHints({CustomEnumRuntimeHints.CustomEnumRegistrar.class})
@Configuration
@Slf4j
public class CustomEnumRuntimeHints {

    static class CustomEnumRegistrar implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
//            List<Class<?>> classes;
//            try {
//                classes = PackageClassFinder.getClasses("top.rainine.homebangumi.def.enums");
//            } catch (ClassNotFoundException | IOException e) {
//                log.error("[CustomEnumRuntimeHints] load custom enums failed", e);
//                throw new RuntimeException(e);
//            }
//            log.info("[CustomEnumRuntimeHints]load custom enum classes count: {}", classes.size());
//            if (CollectionUtils.isEmpty(classes)) {
//                return;
//            }
//
//            classes.forEach(clazz -> hints.reflection().registerType(clazz, MemberCategory.values()));
            hints.reflection().registerType(DownloaderCategoryEnum.class, MemberCategory.values());
            hints.reflection().registerType(EpisodeTitleRenameMethodEnum.class, MemberCategory.values());
            hints.reflection().registerType(HbCodeEnum.class, MemberCategory.values());
            hints.reflection().registerType(MessageCategoryEnum.class, MemberCategory.values());
            hints.reflection().registerType(MessageTypeEnum.class, MemberCategory.values());
            hints.reflection().registerType(NetProxyTypeEnum.class, MemberCategory.values());
            hints.reflection().registerType(RssBangumiEpisodeStatusEnum.class, MemberCategory.values());
            hints.reflection().registerType(RssBangumiStatusEnum.class, MemberCategory.values());
            hints.reflection().registerType(RssCategoryEnum.class, MemberCategory.values());
            hints.reflection().registerType(RssHandleMethodEnum.class, MemberCategory.values());
            hints.reflection().registerType(ScheduledTaskIdEnum.class, MemberCategory.values());
            hints.reflection().registerType(SystemSettingKeyEnum.class, MemberCategory.values());
            hints.reflection().registerType(TorrentDownloadStatusEnum.class, MemberCategory.values());
            hints.reflection().registerType(TorrentFileRenameResultEnum.class, MemberCategory.values());
            hints.reflection().registerType(VideoFileExtensionEnum.class, MemberCategory.values());

        }
    }
}
