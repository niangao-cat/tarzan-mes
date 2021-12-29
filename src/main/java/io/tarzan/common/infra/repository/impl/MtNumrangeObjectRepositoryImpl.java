package io.tarzan.common.infra.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO1;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO2;
import io.tarzan.common.infra.mapper.MtNumrangeObjectMapper;

/**
 * 编码对象属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@Component
public class MtNumrangeObjectRepositoryImpl extends BaseRepositoryImpl<MtNumrangeObject>
                implements MtNumrangeObjectRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Autowired
    private MtNumrangeObjectMapper mtNumrangeObjectMapper;

    @Override
    public List<String> propertyLimitNumrangeObjectQuery(Long tenantId, MtNumrangeObject dto) {
        dto.setTenantId(tenantId);
        List<MtNumrangeObject> list = mtNumrangeObjectMapper.select(dto);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MtNumrangeObject::getObjectId).collect(Collectors.toList());
    }

    @Override
    public MtNumrangeObject numrangeObjectGet(Long tenantId, String objectId) {
        // 1.校验参数合规性
        if (StringUtils.isEmpty(objectId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "objectId", "【API:numrangeObjectGet】"));
        }
        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        numrangeObject.setTenantId(tenantId);
        numrangeObject.setObjectId(objectId);
        return mtNumrangeObjectMapper.selectOne(numrangeObject);
    }

    @Override
    public List<MtNumrangeObjectVO2> propertyLimitNumrangeObjectPropertyQuery(Long tenantId, MtNumrangeObjectVO1 dto) {
        return mtNumrangeObjectMapper.selectCondition(tenantId, dto);
    }
}
