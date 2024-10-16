package top.rainine.homebangumi.def.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author rainine
 * @description 返回值code码
 * @date 2024/3/21 18:42:16
 */
@Getter
@AllArgsConstructor
public enum HbCodeEnum {
    UNKNOWN_ERROR(ErrorCode.UNKNOWN_ERROR, "unknown error"),

    SUCCESS(ErrorCode.SUCCESS, "success"),

    INTERNAL_SERVER_ERROR(ErrorCode.INTERNAL_SERVER_ERROR, "internal server error"),

    INVALID_PARAM(ErrorCode.INVALID_PARAM, "invalid param"),

    ACCOUNT_NOT_EMPTY(ErrorCode.ACCOUNT_NOT_EMPTY, "account not empty"),

    PASSWORD_NOT_EMPTY(ErrorCode.PASSWORD_NOT_EMPTY, "password not empty"),

    ACCOUNT_OR_PASSWORD_INVALID(ErrorCode.ACCOUNT_OR_PASSWORD_INVALID, "account or password invalid"),

    UNAUTHORIZED(ErrorCode.UNAUTHORIZED, "unauthorized"),

    USER_NOT_EXISTS(ErrorCode.USER_NOT_EXISTS, "user not exists"),

    RSS_CATEGORY_INVALID(ErrorCode.RSS_CATEGORY_INVALID, "rss category invalid"),

    RSS_LINK_INVALID(ErrorCode.RSS_LINK_INVALID, "rss link invalid"),

    RSS_HANDLE_METHOD_INVALID(ErrorCode.RSS_HANDLE_METHOD_INVALID, "rss handle method invalid"),

    RSS_LINK_EXISTS(ErrorCode.RSS_LINK_EXISTS, "rss link exists"),

    RSS_BANGUMI_ID_INVALID(ErrorCode.RSS_BANGUMI_ID_INVALID, "rss bangumi id invalid"),

    RSS_BANGUMI_NOT_EXISTS(ErrorCode.RSS_BANGUMI_NOT_EXISTS, "rss bangumi not exists"),

    BANGUMI_BROADCAST_DAY_OF_WEEK_INVALID(ErrorCode.BANGUMI_BROADCAST_DAY_OF_WEEK_INVALID, "bangumi broadcastDayOfWeek invalid, must [1,7]"),

    BANGUMI_SEASON_INVALID(ErrorCode.BANGUMI_SEASON_INVALID, "bangumi season invalid, must >= 0"),

    RSS_BANGUMI_ARCHIVED(ErrorCode.RSS_BANGUMI_ARCHIVED, "rss bangumi archived"),

    RSS_BANGUMI_EPISODE_NOT_EXISTS(ErrorCode.RSS_BANGUMI_EPISODE_NOT_EXISTS, "rss bangumi episode not exists"),

    RSS_BANGUMI_EPISODE_NOT_BELONG_TO_BANGUMI(ErrorCode.RSS_BANGUMI_EPISODE_NOT_BELONG_TO_BANGUMI, "rss bangumi episode not belong to bangumi"),

    CURRENT_PAGE_INVALID(ErrorCode.CURRENT_PAGE_INVALID, "current page invalid"),

    PAGE_SIZE_INVALID(ErrorCode.PAGE_SIZE_INVALID, "page size invalid"),

    RENAMED_EPISODE_FILE_NAME_INVALID(ErrorCode.RENAMED_EPISODE_FILE_NAME_INVALID, "renamed episode file name invalid"),

    RSS_BANGUMI_EPISODE_STATUS_NOT_ALLOW_RENAME(ErrorCode.RSS_BANGUMI_EPISODE_STATUS_NOT_ALLOW_RENAME, "rss bangumi episode status not allow rename"),

    EPISODE_NO_INVALID(ErrorCode.EPISODE_NO_INVALID, "episode no invalid, must >= 0"),

    VIDEO_FILE_EXTENSION_INVALID(ErrorCode.VIDEO_FILE_EXTENSION_INVALID, "video file extension invalid"),

    RSS_BANGUMI_EPISODE_STATUS_NOT_ALLOW_MANUAL_PARSE(ErrorCode.RSS_BANGUMI_EPISODE_STATUS_NOT_ALLOW_MANUAL_PARSE, "rss bangumi episode status not allow manual parse"),

    BANGUMI_NOT_EXISTS(ErrorCode.BANGUMI_NOT_EXISTS, "bangumi not exists"),

    NOT_GET_BANGUMI_SCRAPED_TITLE(ErrorCode.NOT_GET_BANGUMI_SCRAPED_TITLE, "not get bangumi scraped title"),

    NETWORK_PROXY_HOST_INVALID(ErrorCode.NETWORK_PROXY_HOST_INVALID, "network proxy host invalid"),

    NETWORK_PROXY_PORT_INVALID(ErrorCode.NETWORK_PROXY_PORT_INVALID, "network proxy port invalid"),

    NETWORK_PROXY_ENABLE_INVALID(ErrorCode.NETWORK_PROXY_ENABLE_INVALID, "network proxy enable invalid"),

    NETWORK_PROXY_TYPE_INVALID(ErrorCode.NETWORK_PROXY_TYPE_INVALID, "network proxy type invalid"),

    NETWORK_PROXY_SETTINGS_INVALID(ErrorCode.NETWORK_PROXY_SETTINGS_INVALID, "network proxy settings invalid"),

    RSS_BANGUMI_PARSE_FAILED(ErrorCode.RSS_BANGUMI_PARSE_FAILED, "rss banugmi parse failed"),

    DOWNLOADER_CATEGORY_INVALID(ErrorCode.DOWNLOADER_CATEGORY_INVALID, "downloader category invalid"),

    NOT_SUPPORTED_DOWNLOADER_CATEGORY(ErrorCode.NOT_SUPPORTED_DOWNLOADER_CATEGORY, "not supported downloader category"),

    QBITTORRENT_BASE_URL_INVALID(ErrorCode.QBITTORRENT_BASE_URL_INVALID, "qbittorrent base url invalid"),

    QBITTORRENT_USERNAME_INVALID(ErrorCode.QBITTORRENT_USERNAME_INVALID, "qbittorrent username invalid"),

    QBITTORRENT_PASSWORD_INVALID(ErrorCode.QBITTORRENT_PASSWORD_INVALID, "qbittorrent password invalid"),

    QBITTORRENT_DOWNLOAD_DIR_INVALID(ErrorCode.QBITTORRENT_DOWNLOAD_DIR_INVALID, "qbittorrent download dir invalid"),

    CHECK_EPISODE_DOWNLOAD_STATUS_DURATION_INVALID(ErrorCode.CHECK_EPISODE_DOWNLOAD_STATUS_DURATION_INVALID, "check episode download status duration invalid"),

    PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION_INVALID(ErrorCode.PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION_INVALID, "push parsed episodes to downloader duration invalid"),

    RENAME_EPISODES_DURATION_INVALID(ErrorCode.RENAME_EPISODES_DURATION_INVALID, "rename episodes duration invalid"),

    UPDATE_RSS_SUBSCRIPTION_DURATION_INVALID(ErrorCode.UPDATE_RSS_SUBSCRIPTION_DURATION_INVALID, "update rss subscription duration invalid"),

    SCHEDULED_TASK_ENUM_INVALID(ErrorCode.SCHEDULED_TASK_ID_ENUM_INVALID, "scheduled task id invalid"),

    NOT_SUPPORTED_SCHEDULED_TASK_ID(ErrorCode.NOT_SUPPORTED_SCHEDULED_TASK_ID, "not supported scheduled task id"),

    MESSAGE_NOT_EXISTS(ErrorCode.MESSAGE_NOT_EXISTS, "message not exists"),

    EPISODE_TITLE_RENAME_ADAPTER_NOT_FOUND(ErrorCode.EPISODE_TITLE_RENAME_ADAPTER_NOT_FOUND, "episode title rename adapter not found"),

    EPISODE_TITLE_RENAME_METHOD_ENUM_INVALID(ErrorCode.EPISODE_TITLE_RENAME_METHOD_ENUM_INVALID, "episode title rename method enum invalid"),

    CUSTOMIZE_RENAMED_EPISODE_TITLE_FORMAT_INVALID(ErrorCode.CUSTOMIZE_RENAMED_EPISODE_TITLE_FORMAT_INVALID, "customize renamed episode title format invalid"),

    RSS_BANGUMI_EPISODE_RENAME_FAILED(ErrorCode.RSS_BANGUMI_EPISODE_RENAME_FAILED, "rss bangumi episode rename failed"),

    EPISODE_RENAME_TASK_NAME_INVALID(ErrorCode.EPISODE_RENAME_TASK_NAME_INVALID, "episode rename task name invalid"),

    EPISODE_DIR_PATH_INVALID(ErrorCode.EPISODE_DIR_PATH_INVALID, "episode dir path invalid"),

    RENAMED_EPISODE_OUTPUT_DIR_PATH_INVALID(ErrorCode.RENAMED_EPISODE_OUTPUT_DIR_PATH_INVALID, "renamed episode output dir path invalid"),

    EPISODE_RENAME_TASK_STATUS_INVALID(ErrorCode.EPISODE_RENAME_TASK_STATUS_INVALID, "episode rename task status invalid"),

    PARSE_EPISODE_RENAME_TASK_ITEM_FAILED(ErrorCode.PARSE_EPISODE_RENAME_TASK_ITEM_FAILED, "parse episode rename task item failed"),

    EPISODE_RENAME_TASK_NOT_EXISTS(ErrorCode.EPISODE_RENAME_TASK_NOT_EXISTS, "episode rename task not exists"),

    EPISODE_RENAME_TASK_STATUS_NOT_ALLOW_OPERATE(ErrorCode.EPISODE_RENAME_TASK_STATUS_NOT_ALLOW_OPERATE, "episode rename task status not allow operate"),

    EPISODE_RENAME_TASK_ITEM_NOT_EXISTS(ErrorCode.EPISODE_RENAME_TASK_ITEM_NOT_EXISTS, "episode rename task item not exists"),

    EPISODE_RENAME_TASK_ITEM_STATUS_NOT_ALLOW_OPERATE(ErrorCode.EPISODE_RENAME_TASK_ITEM_STATUS_NOT_ALLOW_OPERATE, "episode rename task item status not allow operate"),

    UPDATE_CHECK_NOT_FINISHED_RENAME_TASK_DURATION_INVALID(ErrorCode.UPDATE_CHECK_NOT_FINISHED_RENAME_TASK_DURATION_INVALID, "update check not finished rename task duration invalid"),

    WECOMCHAN_SETTINGS_ENABLE_INVALID(ErrorCode.WECOMCHAN_SETTINGS_ENABLE_INVALID, "wecomchan settings enable invalid"),

    WECOMCHAN_SETTINGS_URL_INVALID(ErrorCode.WECOMCHAN_SETTINGS_URL_INVALID, "wecomchan settings url invalid"),

    WECOMCHAN_SETTINGS_SEND_KEY_INVALID(ErrorCode.WECOMCHAN_SETTINGS_SEND_KEY_INVALID, "wecomchan settings sendKey invalid"),

    EPISODE_RENAME_TASK_SOURCE_DIR_PATH_INVALID(ErrorCode.EPISODE_RENAME_TASK_SOURCE_DIR_PATH_INVALID, "episode rename task sourceDirPath invalid"),

    EPISODE_RENAME_TASK_OUT_DIR_PATH_INVALID(ErrorCode.EPISODE_RENAME_TASK_OUT_DIR_PATH_INVALID, "episode rename task outDirPath invalid"),

    PRE_VIEW_FILES_PATH_INVALID(ErrorCode.PRE_VIEW_FILES_PATH_INVALID, "preViewFiles path invalid"),

    WALK_FILE_PATH_FAILED(ErrorCode.WALK_FILE_PATH_FAILED, "walk file path failed"),

    EPISODE_DIR_PATH_MAX_DEPTH_INVALID(ErrorCode.EPISODE_DIR_PATH_MAX_DEPTH_INVALID, "episodeDirPathMaxDepth invalid"),


    ;
    private final String code;

    private final String msg;


    private static final HbCodeEnum[] ENUMS = values();

    private static final Map<String, HbCodeEnum> ENUM_MAP;

    static {
        ENUM_MAP = Arrays.stream(ENUMS).collect(Collectors.toMap(HbCodeEnum::getCode, code -> code));
    }

    /**
     * 获取code的msg
     * @param code 错误码
     * @return 如果错误码没有对应的code，那么返回未知异常
     * */
    public static String getCodeMsg(String code) {
        HbCodeEnum codeEnum = ENUM_MAP.getOrDefault(code, UNKNOWN_ERROR);
        return codeEnum.getMsg();
    }

    /**
     * 获取code
     * @param code 错误码
     * @return 如果错误码没有对应的code，那么返回未知异常
     * */
    public static HbCodeEnum getCode(String code) {
        return ENUM_MAP.getOrDefault(code, UNKNOWN_ERROR);
    }

    /**
     * 获取code
     * @param code 错误码
     * @param defaultCode 如果没有对应的错误码，返回的默认错误码
     * @return 如果错误码没有对应的code，那么返回未知异常
     * */
    public static HbCodeEnum getCode(String code, HbCodeEnum defaultCode) {
        return ENUM_MAP.getOrDefault(code, defaultCode);
    }

    /**
     * 400前缀表示参数有问题
     * 401前缀表示未登录
     * 100前缀表示业务类型错误码
     * */
    public interface ErrorCode {

        String UNKNOWN_ERROR = "HB999999";

        String SUCCESS = "HB000000";

        String INTERNAL_SERVER_ERROR = "HB500000";

        //region 400 参数无效
        String INVALID_PARAM = "HB400000";

        String ACCOUNT_NOT_EMPTY = "HB400001";

        String PASSWORD_NOT_EMPTY = "HB400002";

        /**
         * rss分类无效
         * */
        String RSS_CATEGORY_INVALID = "HB400003";

        /**
         * rss链接无效
         * */
        String RSS_LINK_INVALID = "HB400004";

        /**
         * rss链接处理方式无效
         * */
        String RSS_HANDLE_METHOD_INVALID = "HB400005";

        /**
         * rss bangumi id无效
         * */
        String RSS_BANGUMI_ID_INVALID = "HB400006";

        /**
         * 放送的星期几字段无效
         * */
        String BANGUMI_BROADCAST_DAY_OF_WEEK_INVALID = "HB400007";

        /**
         * 放送的季无效
         * */
        String BANGUMI_SEASON_INVALID = "HB400008";

        /**
         * 当前页字段无效
         * */
        String CURRENT_PAGE_INVALID = "HB400009";

        /**
         * 每页大小字段无效
         * */
        String PAGE_SIZE_INVALID = "HB400010";

        /**
         * 重命名后的剧集文件名无效
         * */
        String RENAMED_EPISODE_FILE_NAME_INVALID = "HB400011";

        /**
         * 剧集号无效
         * */
        String EPISODE_NO_INVALID = "HB400012";

        /**
         * 视频文件扩展名无效
         * */
        String VIDEO_FILE_EXTENSION_INVALID = "HB400013";

        /**
         * 网络代理的host无效
         * */
        String NETWORK_PROXY_HOST_INVALID = "HB400014";

        /**
         * 网络代理的端口无效
         * */
        String NETWORK_PROXY_PORT_INVALID = "HB400015";

        /**
         * 网络代理的enable字段无效
         * */
        String NETWORK_PROXY_ENABLE_INVALID = "HB400016";

        /**
         * 网络代理的类型字段无效
         * */
        String NETWORK_PROXY_TYPE_INVALID = "HB400017";

        /**
         * 下载器类别无效
         * */
        String DOWNLOADER_CATEGORY_INVALID = "HB400018";


        /**
         * qb的基础路径无效
         * */
        String QBITTORRENT_BASE_URL_INVALID = "HB400019";

        /**
         * qb的用户名无效
         * */
        String QBITTORRENT_USERNAME_INVALID = "HB400020";

        /**
         * qb的密码无效
         * */
        String QBITTORRENT_PASSWORD_INVALID = "HB400021";

        /**
         * qb的下载路径无效
         * */
        String QBITTORRENT_DOWNLOAD_DIR_INVALID = "HB400022";

        /**
         * 检查番剧下载状态的定时任务周期无效
         * */
        String CHECK_EPISODE_DOWNLOAD_STATUS_DURATION_INVALID = "HB400023";

        /**
         * 推送已经解析好的番剧到下载器的定时任务周期无效
         * */
        String PUSH_PARSED_EPISODES_TO_DOWNLOADER_DURATION_INVALID = "HB400024";

        /**
         * 重命名剧集的定时任务周期无效
         * */
        String RENAME_EPISODES_DURATION_INVALID = "HB400025";

        /**
         * 定时更新rss bangumi的定时任务周期无效
         * */
        String UPDATE_RSS_SUBSCRIPTION_DURATION_INVALID = "HB400026";


        /**
         * 定时任务枚举无效
         * */
        String SCHEDULED_TASK_ID_ENUM_INVALID = "HB400027";

        /**
         * 剧集标题重命名方式无效
         * */
        String EPISODE_TITLE_RENAME_METHOD_ENUM_INVALID = "HB400028";

        /**
         * 自定义重命名剧集标题格式无效
         * */
        String CUSTOMIZE_RENAMED_EPISODE_TITLE_FORMAT_INVALID = "HB400029";

        /**
         * 剧集重命名任务名无效
         * */
        String EPISODE_RENAME_TASK_NAME_INVALID = "HB400030";

        /**
         * 剧集的目录路径无效
         * */
        String EPISODE_DIR_PATH_INVALID = "HB400031";

        /**
         * 重命名后的剧集输出路径无效
         * */
        String RENAMED_EPISODE_OUTPUT_DIR_PATH_INVALID = "HB400032";

        /**
         * 剧集重命名任务状态无效
         * */
        String EPISODE_RENAME_TASK_STATUS_INVALID = "HB400033";


        /**
         * 定时检查未完成的重命名任务的定时任务周期无效
         * */
        String UPDATE_CHECK_NOT_FINISHED_RENAME_TASK_DURATION_INVALID = "HB400034";

        /**
         * 更新wecomchan的enable字段无效
         * */
        String WECOMCHAN_SETTINGS_ENABLE_INVALID = "HB400035";

        /**
         * 更新wecomchan的url字段无效
         * */
        String WECOMCHAN_SETTINGS_URL_INVALID = "HB400036";

        /**
         * 更新wecomchan的sendKey字段无效
         * */
        String WECOMCHAN_SETTINGS_SEND_KEY_INVALID = "HB400037";

        /**
         * 剧集重命名任务源目录路径字段无效
         * */
        String EPISODE_RENAME_TASK_SOURCE_DIR_PATH_INVALID = "HB400038";

        /**
         * 剧集重命名任务输出目录路径字段无效
         * */
        String EPISODE_RENAME_TASK_OUT_DIR_PATH_INVALID = "HB400039";

        /**
         * 预览文件路径字段无效
         * */
        String PRE_VIEW_FILES_PATH_INVALID = "HB400040";

        /**
         * 剧集的目录路径最大深度无效
         * */
        String EPISODE_DIR_PATH_MAX_DEPTH_INVALID = "HB400041";
        //endregion





        //region 100 业务异常
        /**
         * 未登录
         * */
        String UNAUTHORIZED = "HB401000";

        /**
         * 账号或者密码无效
         * */
        String ACCOUNT_OR_PASSWORD_INVALID = "HB100001";

        /**
         * 用户不存在
         * */
        String USER_NOT_EXISTS = "HB100002";

        /**
         * rss链接已经存在
         * */
        String RSS_LINK_EXISTS = "HB100003";

        /**
         * rss bangumi 不存在
         * */
        String RSS_BANGUMI_NOT_EXISTS = "HB100004";

        /**
         * 番剧已被归档
         * */
        String RSS_BANGUMI_ARCHIVED = "HB100005";

        /**
         * rss bangumi episode 不存在
         * */
        String RSS_BANGUMI_EPISODE_NOT_EXISTS = "HB100006";

        /**
         * rss bangumi episode不属于当前的bangumi
         * */
        String RSS_BANGUMI_EPISODE_NOT_BELONG_TO_BANGUMI = "HB100007";

        /**
         * 当前状态不允许修改刮削文件名
         * */
        String RSS_BANGUMI_EPISODE_STATUS_NOT_ALLOW_RENAME = "HB100008";

        /**
         * 当前状态不允许手动解析剧集
         * */
        String RSS_BANGUMI_EPISODE_STATUS_NOT_ALLOW_MANUAL_PARSE = "HB100009";

        /**
         * 番剧不存在
         * */
        String BANGUMI_NOT_EXISTS = "HB100010";

        /**
         * 未获取到番剧的刮削标题
         * */
        String NOT_GET_BANGUMI_SCRAPED_TITLE = "HB100011";

        /**
         * 网络代理配置无效
         * */
        String NETWORK_PROXY_SETTINGS_INVALID = "HB100012";

        /**
         * 番剧解析失败
         * */
        String RSS_BANGUMI_PARSE_FAILED = "HB100013";

        /**
         * 不支持的下载器类型
         * */
        String NOT_SUPPORTED_DOWNLOADER_CATEGORY = "HB100014";

        /**
         * 不支持的定时任务id
         * */
        String NOT_SUPPORTED_SCHEDULED_TASK_ID = "HB100015";

        /**
         * 消息不存在
         * */
        String MESSAGE_NOT_EXISTS = "HB100016";

        /**
         * 剧集标题重命名适配器不存在
         * */
        String EPISODE_TITLE_RENAME_ADAPTER_NOT_FOUND = "HB100017";

        /**
         * 剧集重命名失败
         * */
        String RSS_BANGUMI_EPISODE_RENAME_FAILED = "HB100018";

        /**
         * 解析剧集重命名任务item失败
         * */
        String PARSE_EPISODE_RENAME_TASK_ITEM_FAILED = "HB100019";

        /**
         * 任务不存在
         * */
        String EPISODE_RENAME_TASK_NOT_EXISTS = "HB100020";

        /**
         * 当前状态不允许更新
         * */
        String EPISODE_RENAME_TASK_STATUS_NOT_ALLOW_OPERATE = "HB100021";

        /**
         * 任务项不存在
         * */
        String EPISODE_RENAME_TASK_ITEM_NOT_EXISTS = "HB100022";

        /**
         * 当前状态不允许更新
         * */
        String EPISODE_RENAME_TASK_ITEM_STATUS_NOT_ALLOW_OPERATE = "HB100023";

        /**
         * 遍历预览路径失败
         * */
        String WALK_FILE_PATH_FAILED = "HB100024";
        //endregion
    }
}

