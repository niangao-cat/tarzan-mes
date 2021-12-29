package com.ruike.hme.infra.repository.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;

import com.ruike.hme.domain.entity.HmeException;
import com.ruike.hme.domain.repository.HmeExceptionRepository;
import com.ruike.hme.api.dto.HmeExceptionDTO;
import com.ruike.hme.infra.mapper.HmeExceptionMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 异常维护基础数据头表 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
@Component
public class HmeExceptionRepositoryImpl extends BaseRepositoryImpl<HmeException> implements HmeExceptionRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeExceptionMapper mapper;

    @Autowired
    private MtUserClient userClient;

    @Override
    public List<HmeException> exceptionUiQuery(Long tenantId, HmeExceptionDTO dto) {
        List<HmeException> hmeExceptionList = mapper.uiQuery(tenantId, dto);
        // 非数据字段赋值
        hmeExceptionList.forEach(hmeException -> {
            hmeException.setCreatedUserName(userClient.userInfoGet(tenantId, hmeException.getCreatedBy()).getRealName());
            hmeException.setLastUpdatedUserName(userClient.userInfoGet(tenantId, hmeException.getLastUpdatedBy()).getRealName());
        });
        return hmeExceptionList;
    }

    @Override
    public HmeException exceptionBasicPropertyUpdate(Long tenantId, HmeException dto) {
        String exceptionId = dto.getExceptionId();
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // 校验编码重复
        HmeException oldException = new HmeException();
        oldException.setTenantId(tenantId);
        oldException.setExceptionCode(dto.getExceptionCode());
        oldException = mapper.selectOne(oldException);
        if (null != oldException) {
            if (StringUtils.isEmpty(exceptionId) || !exceptionId.equals(oldException.getExceptionId())) {
                throw new MtException("HME_EXCEPTION_MAN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_MAN_001", "HME"));
            }
        }
        if (StringUtils.isEmpty(exceptionId)) {
            Date now = new Date();
            dto.setTenantId(tenantId);
            dto.setCreatedBy(userId);
            dto.setCreationDate(now);
            dto.setLastUpdateDate(now);
            dto.setLastUpdatedBy(userId);
            self().insertSelective(dto);

        } else {
            Date now = new Date();
            dto.setTenantId(tenantId);
            dto.setLastUpdateDate(now);
            dto.setLastUpdatedBy(userId);
            dto.setTenantId(tenantId);

            mapper.updateByPrimaryKeySelective(dto);
        }

        dto.setCreatedUserName(userClient.userInfoGet(tenantId, dto.getCreatedBy()).getRealName());
        dto.setLastUpdatedUserName(userClient.userInfoGet(tenantId, dto.getLastUpdatedBy()).getRealName());
        return dto;
    }
}
