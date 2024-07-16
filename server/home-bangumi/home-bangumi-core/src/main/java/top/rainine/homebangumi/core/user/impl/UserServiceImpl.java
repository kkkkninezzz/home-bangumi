package top.rainine.homebangumi.core.user.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.rainine.homebangumi.api.req.LoginByPasswordReq;
import top.rainine.homebangumi.api.resp.LoginResp;
import top.rainine.homebangumi.api.resp.SelfInfoResp;
import top.rainine.homebangumi.core.user.configuration.DefaultAccountProperties;
import top.rainine.homebangumi.dao.po.HbUserAccount;
import top.rainine.homebangumi.dao.repository.HbUserAccountRepository;
import top.rainine.homebangumi.def.enums.HbCodeEnum;
import top.rainine.homebangumi.def.exception.HbBizException;
import top.rainine.homebangumi.core.user.UserService;

import java.util.Optional;

/**
 * @authoer rainine
 * @date 2024/3/22 00:07
 * @desc
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService, InitializingBean {

    private final DefaultAccountProperties defaultAccountProperties;

    private final HbUserAccountRepository accountRepository;

    @Autowired
    public UserServiceImpl(DefaultAccountProperties defaultAccountProperties, HbUserAccountRepository accountRepository) {
        this.defaultAccountProperties = defaultAccountProperties;
        this.accountRepository = accountRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initAccount();
    }

    /**
     * 初始化一个账号
     * */
    private void initAccount() {
        Optional<HbUserAccount> accountOptional = accountRepository.findByUsername(defaultAccountProperties.getUsername());
        // 说明已经初始过账号了
        if (accountOptional.isPresent()) {
            return;
        }

        String password = StringUtils.isNotBlank(defaultAccountProperties.getPassword()) ? defaultAccountProperties.getPassword() : "123456";
        log.info("[UserService]start init account, account: {}, password: {}", defaultAccountProperties.getUsername(), password);

        HbUserAccount account = new HbUserAccount();
        account.setUsername(defaultAccountProperties.getUsername());
        account.setPassword(BCrypt.hashpw(password));
        accountRepository.save(account);
        log.info("[UserService]end init account, account: {}, password: {}", defaultAccountProperties.getUsername(), password);
    }

    @Override
    public LoginResp login(LoginByPasswordReq req) {
        HbUserAccount account = accountRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new HbBizException(HbCodeEnum.ACCOUNT_OR_PASSWORD_INVALID));

        if (!BCrypt.checkpw(req.getPassword(), account.getPassword())) {
            throw new HbBizException(HbCodeEnum.ACCOUNT_OR_PASSWORD_INVALID);
        }

        StpUtil.login(account.getId());

        return LoginResp
                .builder()
                .accessToken(StpUtil.getTokenValue())
                .username(req.getUsername())
                .uid(account.getId())
                .build();
    }

    @Override
    public SelfInfoResp getSelfInfo(Long uid) {
        HbUserAccount account = accountRepository.findById(uid).orElseThrow(() -> new HbBizException(HbCodeEnum.USER_NOT_EXISTS));

        return SelfInfoResp
                .builder()
                .uid(account.getId())
                .nickname(account.getUsername())
                .avatar("")
                .build();
    }
}
