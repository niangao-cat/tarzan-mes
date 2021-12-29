package io.tarzan.common.infra.repository.impl;

import java.util.List;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectColumnRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.infra.mapper.MtNumrangeObjectColumnMapper;

/**
 * 编码对象属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@Component
public class MtNumrangeObjectColumnRepositoryImpl extends BaseRepositoryImpl<MtNumrangeObjectColumn>
                implements MtNumrangeObjectColumnRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Autowired
    private MtNumrangeObjectColumnMapper mtNumrangeObjectColumnMapper;

    @Override
    public List<MtNumrangeObjectColumn> propertyLimitNumrangeObjectColumnQuery(Long tenantId,
                                                                               MtNumrangeObjectColumn dto) {
        // 1.校验参数合规性
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_GENERAL_0014", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0014", "GENERAL", "【API:propertyLimitNumrangeObjectColumnQuery】"));
        }
        dto.setTenantId(tenantId);
        return mtNumrangeObjectColumnMapper.select(dto);
    }
}
