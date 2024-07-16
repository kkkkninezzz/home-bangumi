package top.rainine.homebangumi.core.settings.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.core.settings.SystemSettingsService;
import top.rainine.homebangumi.core.settings.data.BaseSystemSetting;
import top.rainine.homebangumi.dao.po.HbSystemSetting;
import top.rainine.homebangumi.dao.repository.HbSystemSettingRepository;
import top.rainine.homebangumi.def.enums.SystemSettingKeyEnum;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.StringTemplate.STR;

/**
 * @author rainine
 * @description 系统配置的服务类
 * @date 2024/3/15 16:35:50
 */
@Service
@Slf4j
public class SystemSettingsServiceImpl implements SystemSettingsService {

    private final HbSystemSettingRepository settingRepository;

    @Autowired
    public SystemSettingsServiceImpl(HbSystemSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public BaseSystemSetting getSetting(SystemSettingKeyEnum key) {
        Optional<HbSystemSetting> optional = settingRepository.findBySettingKey(key.getKey());
        Object settingValue = getSettingValue(optional, key);

        return BaseSystemSetting.builder()
                .key(key)
                .value(settingValue)
                .build();
    }

    private Object getSettingValue(Optional<HbSystemSetting> optional, SystemSettingKeyEnum settingKey) {
        return optional.map(c -> convertSettingValue(c.getSettingValue(), settingKey)).orElseGet(settingKey::getDefaultValue);
    }

    /**
     * 格式化在数据库中的配置值
     * */
    private String formatSettingValueToDb(Object value) {
        return Optional.ofNullable(value).map(Object::toString).orElse("");
    }

    /**
     * 转换配置值
     * */
    private Object convertSettingValue(String value, SystemSettingKeyEnum key) {
        Class<?> valueType = key.getValueType();
        if (String.class.equals(valueType)) {
            return value;
        }

        // 如果是其他类型的配置值，当settingValue是空字符串时，视为null
        if (StringUtils.isBlank(value)) {
            return null;
        }

        if (Integer.class.equals(valueType)) {
            return Integer.parseInt(value);
        }

        if (Long.class.equals(valueType)) {
            return Long.parseLong(value);
        }

        if (Double.class.equals(valueType)) {
            return Double.parseDouble(value);
        }

        if (Float.class.equals(valueType)) {
            return Float.parseFloat(value);
        }

        if (Boolean.class.equals(valueType)) {
            return Boolean.parseBoolean(value);
        }

        throw new IllegalArgumentException(STR."not supported setting value type, value type: \{valueType}");
    }

    @Override
    public List<BaseSystemSetting> loadSettings(List<SystemSettingKeyEnum> Keys) {
        if (CollectionUtils.isEmpty(Keys)) {
            return new ArrayList<>();
        }

        List<String> keys = Keys.stream().map(SystemSettingKeyEnum::getKey).toList();
        Map<String, HbSystemSetting> settingMap = settingRepository.findAllBySettingKeyIn(keys)
                .stream()
                .collect(Collectors.toMap(HbSystemSetting::getSettingKey, c -> c));


        return Keys.stream()
                .map(settingKey -> {
                    HbSystemSetting setting = settingMap.get(settingKey.getKey());
                    Object settingValue = getSettingValue(Optional.ofNullable(setting), settingKey);

                    return BaseSystemSetting.builder()
                    .key(settingKey)
                    .value(settingValue)
                    .build();

                }).collect(Collectors.toList());
    }

    @Override
    public Map<SystemSettingKeyEnum, BaseSystemSetting> loadSettingMap(List<SystemSettingKeyEnum> settingKeys) {
        if (CollectionUtils.isEmpty(settingKeys)) {
            return new HashMap<>();
        }

        List<String> keys = settingKeys.stream().map(SystemSettingKeyEnum::getKey).toList();
        Map<String, HbSystemSetting> settingMap = settingRepository.findAllBySettingKeyIn(keys)
                .stream()
                .collect(Collectors.toMap(HbSystemSetting::getSettingKey, c -> c));

        return settingKeys.stream()
                .map(settingKey -> {
                    HbSystemSetting setting = settingMap.get(settingKey.getKey());
                    Object settingValue = getSettingValue(Optional.ofNullable(setting), settingKey);

                    BaseSystemSetting baseSystemSetting = BaseSystemSetting.builder()
                            .key(settingKey)
                            .value(settingValue)
                            .build();
                    return Pair.of(settingKey, baseSystemSetting);

                }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override
    public void saveSetting(BaseSystemSetting baseSystemSetting) {
        HbSystemSetting hbSystemSetting = settingRepository.findBySettingKey(baseSystemSetting.key().getKey())
                .orElseGet(() -> {
                    HbSystemSetting setting = new HbSystemSetting();
                    setting.setSettingKey(baseSystemSetting.key().getKey());
                    return setting;
                });

        hbSystemSetting.setSettingValue(formatSettingValueToDb(baseSystemSetting.value()));
        settingRepository.save(hbSystemSetting);
    }

    @Override
    public void saveSettings(Map<SystemSettingKeyEnum, BaseSystemSetting> settingMap) {
        if (MapUtils.isEmpty(settingMap)) {
            return;
        }

        saveSettings(new ArrayList<>(settingMap.values()));
    }

    @Override
    public void saveSettings(List<BaseSystemSetting> settings) {
        if (CollectionUtils.isEmpty(settings)) {
            return;
        }

        List<String> keys = settings.stream().map(baseSystemSetting -> baseSystemSetting.key().getKey()).toList();
        Map<String, HbSystemSetting> settingMapInDb = settingRepository.findAllBySettingKeyIn(keys)
                .stream()
                .collect(Collectors.toMap(HbSystemSetting::getSettingKey, c -> c));

        List<HbSystemSetting> newOrUpdatedSettings = settings
                .stream()
                .map(baseSystemSetting -> {
                    HbSystemSetting hbSystemSetting = settingMapInDb.computeIfAbsent(baseSystemSetting.key().getKey(), k -> {
                        HbSystemSetting setting = new HbSystemSetting();
                        setting.setSettingKey(k);
                        return setting;
                    });
                    hbSystemSetting.setSettingValue(formatSettingValueToDb(baseSystemSetting.value()));
                    return hbSystemSetting;
                }).toList();

        settingRepository.saveAll(newOrUpdatedSettings);
    }
}















