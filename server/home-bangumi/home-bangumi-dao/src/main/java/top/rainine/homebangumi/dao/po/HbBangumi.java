package top.rainine.homebangumi.dao.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rainine
 * @description hb定义的番剧信息
 * @date 2024/3/13 17:19:39
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "hb_bangumi", indexes = {
})
public class HbBangumi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 番剧名
     * */
    @Column(name = "title", length = 256, nullable = false)
    private String title;

    /**
     * 番剧的海报存储的本地路径
     * 如果获取失败，使用一个默认值
     * */
    @Column(name = "poster_stored_path", length = 512)
    private String posterStoredPath;

    /**
     * 番剧的海报地址
     * 如果获取失败，使用一个默认值
     * */
    @Column(name = "poster_url", length = 512, nullable = false)
    private String posterUrl;

    /**
     * 放送的星期几
     * 从1开始到7
     * 如果为null，那么认为暂无信息
     * */
    @Column(name = "broadcast_day_of_week")
    private Integer broadcastDayOfWeek;

    /**
     * 开始放送的日期
     * 毫秒时间戳
     * */
    @Column(name = "broadcast_date")
    private Long broadcastDate;

    /**
     * 放送的是哪一季
     * */
    @Column(name = "season")
    private Integer season;

    /**
     * 放送的是哪一季的名字
     * */
    @Column(name = "season_name")
    private Integer seasonName;
}
