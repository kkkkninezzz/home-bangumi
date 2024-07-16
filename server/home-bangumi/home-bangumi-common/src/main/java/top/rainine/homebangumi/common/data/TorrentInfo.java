package top.rainine.homebangumi.common.data;

import lombok.*;

/**
 * @authoer rainine
 * @date 2024/3/14 01:31
 * @desc 种子数据
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TorrentInfo {
    /**
     * 种子的hash值
     * */
    private String hash;

    /**
     * 资源名
     * */
    private String resourceName;
}
