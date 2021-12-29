package io.tarzan.common.domain.repository;

import io.tarzan.common.domain.sys.MtUserInfo;

public interface MtUserRepository {

    /**
     * userPropertyGet-根据用户id获取用户基础属性
     *
     * @author benjamin
     * @date 2019-09-04 15:39
     * @param userId 用户Id
     * @return MtUserVO
     */
    MtUserInfo userPropertyGet(Long tenantId, Long userId);
}
