package top.rainine.homebangumi.core.runtimehints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import top.rainine.homebangumi.core.downloader.qbittorrent.QbTorrentProperties;
import top.rainine.homebangumi.core.message.pusher.WecomchanPusher;
import top.rainine.homebangumi.core.metadata.tmdb.data.TmdbObjectRuntimeHints;

/**
 * @authoer rainine
 * @date 2024/7/11 20:47
 * @desc mikan xml结构对象的提示
 */
@ImportRuntimeHints({CustomGsonObjectRuntimeHints.CustomGsonObjectRegistrar.class})
@Configuration
@Slf4j
public class CustomGsonObjectRuntimeHints {

    static class CustomGsonObjectRegistrar implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(QbTorrentProperties.class, MemberCategory.values());
            hints.reflection().registerType(WecomchanPusher.WecomchanPushResponse.class, MemberCategory.values());
            TmdbObjectRuntimeHints.registerHints(hints);
        }
    }
}
