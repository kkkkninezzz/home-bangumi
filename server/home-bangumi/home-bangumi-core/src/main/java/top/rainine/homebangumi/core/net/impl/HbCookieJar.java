package top.rainine.homebangumi.core.net.impl;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @authoer rainine
 * @date 2024/4/27 23:38
 * @desc cookie管理
 */
public class HbCookieJar implements CookieJar {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> cookies = new ConcurrentHashMap<>();

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        return get(this.hostName(httpUrl));
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
        this.add(httpUrl, list);
    }


    private void add(HttpUrl httpUrl, Cookie cookie) {

        String name = this.cookieName(cookie);
        String hostKey = this.hostName(httpUrl);

        this.cookies.computeIfAbsent(hostKey, _ -> new ConcurrentHashMap<>()).put(name, cookie);
    }

    private void add(HttpUrl httpUrl, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (isCookieExpired(cookie)) {
                continue;
            }
            this.add(httpUrl, cookie);
        }
    }

    public List<Cookie> get(HttpUrl httpUrl) {
        return this.get(this.hostName(httpUrl));
    }

    /** 获取cookie集合 */
    private List<Cookie> get(String hostKey) {
        List<Cookie> result = new ArrayList<Cookie>();

        if (this.cookies.containsKey(hostKey)) {
            Collection<Cookie> cookies = this.cookies.get(hostKey).values();
            for (Cookie cookie : cookies) {
                if (isCookieExpired(cookie)) {
                    this.remove(hostKey, cookie);
                }
                else {
                    result.add(cookie);
                }
            }
        }
        return result;
    }

    public boolean remove(HttpUrl httpUrl, Cookie cookie) {
        return this.remove(this.hostName(httpUrl), cookie);
    }

    /** 从缓存中移除cookie */
    private boolean remove(String hostKey, Cookie cookie) {
        String name = this.cookieName(cookie);
        if (this.cookies.containsKey(hostKey) && this.cookies.get(hostKey).containsKey(name)) {
            // 从内存中移除httpUrl对应的cookie
            this.cookies.get(hostKey).remove(name);
            return true;
        }
        return false;
    }

    public boolean removeAll() {
        this.cookies.clear();
        return true;
    }

    /** 判断cookie是否失效 */
    private boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    private String hostName(HttpUrl httpUrl) {
        return httpUrl.host();
    }

    private String cookieName(Cookie cookie) {
        return cookie == null ? null : cookie.name() + cookie.domain();
    }
}
