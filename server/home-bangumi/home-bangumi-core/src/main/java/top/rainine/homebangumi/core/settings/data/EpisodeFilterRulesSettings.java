package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;

import java.util.List;

/**
 * @param rules 规则
 *
 * @author rainine
 * @description
 * @date 2024/5/14 10:34:07
 */
@Builder
public record EpisodeFilterRulesSettings(List<String> rules) {
}
