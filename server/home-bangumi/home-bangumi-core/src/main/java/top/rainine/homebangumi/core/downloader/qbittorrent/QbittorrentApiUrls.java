package top.rainine.homebangumi.core.downloader.qbittorrent;

/**
 * @authoer rainine
 * @date 2024/4/27 23:59
 * @desc qb的api url定义
 */
public interface QbittorrentApiUrls {


    /**
     * 拼接url
     * */
    static String concatUrl(String baseUrl, String api) {
        return baseUrl.concat(api);
    }

    /**
     * 登录api
     * */
    String LOGIN = "/api/v2/auth/login";

    /**
     * 获取登录api
     * */
    static String getLoginApi(String baseUrl) {
        return concatUrl(baseUrl, LOGIN);
    }

    /**
     * 获取种子信息
     * */
    String TORRENTS_INFO = "/api/v2/torrents/info";

    static String getTorrentsInfoAPi(String baseUrl) {
        return concatUrl(baseUrl, TORRENTS_INFO);
    }

    /**
     * 获取版本信息
     * */
    String VERSION_INFO = "/api/v2/app/version";

    static String getVersionInfoApi(String baseUrl) {
        return concatUrl(baseUrl, VERSION_INFO);
    }

    /**
     * 添加种子
     * */
    String ADD_TORRENT = "/api/v2/torrents/add";

    static String getAddTorrentApi(String baseUrl) {
        return concatUrl(baseUrl, ADD_TORRENT);
    }

    /**
     * 删除种子
     * */
    String DELETE_TORRENT = "/api/v2/torrents/delete";

    static String getDeleteTorrentApi(String baseUrl) {
        return concatUrl(baseUrl, DELETE_TORRENT);
    }

    /**
     * 重命名文件
     * */
    String RENAME_FILE = "/api/v2/torrents/renameFile";

    static String getRenameFileApi(String baseUrl) {
        return concatUrl(baseUrl, RENAME_FILE);
    }

    /**
     * 获取种子数据
     * */
    String TORRENT_PROPERTIES = "/api/v2/torrents/properties";

    static String getTorrentPropertiesApi(String baseUrl) {
        return concatUrl(baseUrl, TORRENT_PROPERTIES);
    }
}
