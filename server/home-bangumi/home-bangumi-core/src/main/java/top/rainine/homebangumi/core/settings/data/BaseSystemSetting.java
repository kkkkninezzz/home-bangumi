package top.rainine.homebangumi.core.settings.data;

import lombok.Builder;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

/**
 * @param key 配置key
 * @param value 由字符串转换为在{@link SystemSettingKeyEnum#getValueType()}定义的类型后
 *
 * @author rainine
 * @description 基础的系统配置对象
 * @date 2024/3/15 16:10:29
 */
@Builder
public record BaseSystemSetting(SystemSettingKeyEnum key, Object value) {

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }
}
