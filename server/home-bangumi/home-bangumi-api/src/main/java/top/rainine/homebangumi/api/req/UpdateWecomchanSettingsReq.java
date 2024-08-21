package top.rainine.homebangumi.api.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import top.rainine.homebangumi.def.enums.HbCodeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author rainine
 * @description wecomchan的设置
 * @date 2024/8/20 17:56:24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@GroupSequenceProvider(UpdateWecomchanSettingsReq.WecomchanGroupSequenceProvider.class)
public class UpdateWecomchanSettingsReq {

    /**
     * 是否可用
     * */
    @NotNull(message = HbCodeEnum.ErrorCode.WECOMCHAN_SETTINGS_ENABLE_INVALID)
    private Boolean enable;

    /**
     * wecomchan的地址
     * */
    @NotEmpty(groups = {WecomchanEnableGroup.class}, message = HbCodeEnum.ErrorCode.WECOMCHAN_SETTINGS_URL_INVALID)
    private String url;

    /**
     * sendKey
     * */
    @NotEmpty(groups = {WecomchanEnableGroup.class}, message = HbCodeEnum.ErrorCode.WECOMCHAN_SETTINGS_SEND_KEY_INVALID)
    private String sendKey;


    /**
     * 标记wecomchan可用的组
     * */
    public interface WecomchanEnableGroup {}

    public static class WecomchanGroupSequenceProvider implements DefaultGroupSequenceProvider<UpdateWecomchanSettingsReq> {

        @Override
        public List<Class<?>> getValidationGroups(UpdateWecomchanSettingsReq req) {
            final List<Class<?>> defaultGroupSequence = new ArrayList<>();
            // every bean validate should have a default self group
            // 注意这个一定要加，不然连默认的规则都没了会失败的
            defaultGroupSequence.add(UpdateWecomchanSettingsReq.class);

            if (Objects.nonNull(req)) {
                if (Objects.equals(Boolean.TRUE, req.getEnable())) {
                    defaultGroupSequence.add(WecomchanEnableGroup.class);
                }
            }

            return defaultGroupSequence;
        }
    }
}
