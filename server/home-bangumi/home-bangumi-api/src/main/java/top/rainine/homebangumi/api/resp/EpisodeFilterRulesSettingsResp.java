package top.rainine.homebangumi.api.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author rainine
 * @description
 * @date 2024/5/14 11:05:52
 */
@Getter
@Setter
@ToString
public class EpisodeFilterRulesSettingsResp {
    /**
     * 过滤规则
     * */
    private List<String> rules;
}
