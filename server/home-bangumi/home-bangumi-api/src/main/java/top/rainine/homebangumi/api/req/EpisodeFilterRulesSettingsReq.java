package top.rainine.homebangumi.api.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author rainine
 * @description
 * @date 2024/5/14 11:33:09
 */
@Getter
@Setter
@ToString
public class EpisodeFilterRulesSettingsReq {

    /**
     * 过滤规则
     * */
    private List<String> rules;
}
