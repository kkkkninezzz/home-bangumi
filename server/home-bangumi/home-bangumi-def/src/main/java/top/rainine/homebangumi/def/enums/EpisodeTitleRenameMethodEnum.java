package top.rainine.homebangumi.def.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rainine.homebangumi.def.utils.EnumUtils;

import java.util.Objects;

/**
 * @author rainine
 * @description 剧集重命名方式
 * @date 2024/6/25 16:56:04
 */
@Getter
@RequiredArgsConstructor
public enum EpisodeTitleRenameMethodEnum {

    /**
     * 不进行重命名
     * 将保存原始命名
     * */
    NONE(0),

    /**
     * 基于种子文件解析出来的标题进行重命名
     * */
    TORRENT_PARSED_TITLE(1),

    /**
     * 基于官方标题进行重命名
     * */
    OFFICIAL_TITLE(2),

    /**
     * 基于自定义标题进行重命名
     * 需要提供占位符{season} {episode}
     * */
    CUSTOMIZED_TITLE(3),

    /**
     * 在保留原始命名的基础上，需要解析出剧集号
     * */
    ONLY_PARSE_EPISODE_NO(4),
    ;

    private static final EpisodeTitleRenameMethodEnum[] ENUMS = values();

    private final int method;

    public static EpisodeTitleRenameMethodEnum of(Integer method) {
        return EnumUtils.ofInteger(ENUMS, EpisodeTitleRenameMethodEnum::getMethod, method, null);
    }

    public static boolean contains(Integer method) {
        return EnumUtils.containsInteger(ENUMS, EpisodeTitleRenameMethodEnum::getMethod, method);
    }

    public boolean equals(Integer method) {
        if (Objects.isNull(method)) {
            return false;
        }

        return this.method == method;
    }
}











