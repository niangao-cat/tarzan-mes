package io.tarzan.common.infra.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;

@Component
public class MtUserRepositoryImpl implements MtUserRepository {

    @Autowired
    private MtUserClient userClient;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Override
    public MtUserInfo userPropertyGet(Long tenantId, Long userId) {
        if (null == userId) {
            throw new MtException("MT_PERMISSION_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_PERMISSION_0002", "PERMISSION", "userId", "【API:userPropertyGet】"));
        }
        return userClient.userInfoGet(tenantId, userId);
    }
}
