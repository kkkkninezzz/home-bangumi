package top.rainine.homebangumi.common.utils;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;
import lombok.SneakyThrows;
import top.rainine.homebangumi.common.data.TorrentInfo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

/**
 * @authoer rainine
 * @date 2024/3/14 01:09
 * @desc bencode包装的工具类
 */
public enum BencodeUtils {
    INSTANCE;

    private final ThreadLocal<Bencode> threadLocal = ThreadLocal.withInitial(() -> new Bencode(StandardCharsets.UTF_8));

    /**
     * 使用iso进行编码的解析
     * */
    private final ThreadLocal<Bencode> calHashThreadLocal = ThreadLocal.withInitial(() -> new Bencode(StandardCharsets.ISO_8859_1));

    /**
     * 计算种子的hash值
     * @param torrentData 种子文件的二进制数据
     * @return 返回种子的hash值
     * @throws RuntimeException 如果计算失败，将抛出异常
     * */
    public String calTorrentHash(byte[] torrentData) {
        Bencode bencodeInfoHash = calHashThreadLocal.get();
        Map<String, Object> dictInfoHash = bencodeInfoHash.decode(torrentData, Type.DICTIONARY);

        byte[] infoData = bencodeInfoHash.encode((Map<?, ?>) dictInfoHash.get("info"));
        return sha1(infoData);
    }

    /**
     * 解析种子
     * @param torrentData 种子文件的二进制数据
     * @return 返回解析出来的种子信息
     *  @throws RuntimeException 如果计算失败，将抛出异常
     * */
    @SuppressWarnings("unchecked")
    public TorrentInfo parseTorrent(byte[] torrentData) {
        Bencode bencodeInfoHash = threadLocal.get();
        Map<String, Object> dictInfoHash = bencodeInfoHash.decode(torrentData, Type.DICTIONARY);

        String hash;
        if (dictInfoHash.containsKey("hash")) {
            hash = dictInfoHash.get("hash").toString();
        } else {
            hash = calTorrentHash(torrentData);
        }

        String resourceName = "unknown";
        Map<String, Object> info = (Map<String, Object>) dictInfoHash.get("info");
        if (info.containsKey("name")) {
            resourceName = info.get("name").toString();
        }

        return TorrentInfo
                .builder()
                .hash(hash)
                .resourceName(resourceName)
                .build();
    }

    @SneakyThrows
    private String sha1(byte[] bytes) {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
