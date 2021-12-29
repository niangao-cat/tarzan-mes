package tarzan.modeling.infra.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.repository.MtCustomerRepository;
import tarzan.modeling.infra.mapper.MtCustomerMapper;

/**
 * 客户 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
@Component
public class MtCustomerRepositoryImpl extends BaseRepositoryImpl<MtCustomer> implements MtCustomerRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtCustomerMapper mtCustomerMapper;

    @Override
    public List<MtCustomer> queryCustomerByCode(Long tenantId, List<String> customerCodeList) {
        if (CollectionUtils.isEmpty(customerCodeList)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("CUSTOMER_CODE", customerCodeList, 1000);
        return mtCustomerMapper.queryCustomerByCode(tenantId, whereInValuesSql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void customerAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "keyId", "【API:customerAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtCustomer mtCustomer = new MtCustomer();
        mtCustomer.setTenantId(tenantId);
        mtCustomer.setCustomerId(dto.getKeyId());
        mtCustomer = mtCustomerMapper.selectByPrimaryKey(mtCustomer);
        if (mtCustomer == null) {
            throw new MtException("MT_MODELING_0048",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0048", "MODELING",
                            "keyId:" + dto.getKeyId(), "mt_customer",
                            "【API:customerAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_customer_attr", dto.getKeyId(), dto.getEventId(),
                dto.getAttrs());
    }

    @Override
    public MtCustomer queryCustomerById(Long tenantId, String customerId) {
        MtCustomer mtCustomer = new MtCustomer();
        mtCustomer.setTenantId(tenantId);
        mtCustomer.setCustomerId(customerId);
        mtCustomer = mtCustomerMapper.selectByPrimaryKey(mtCustomer);
        return mtCustomer;
    }

    @Override
    public List<MtCustomer> queryCustomerByIds(Long tenantId, List<String> customerIds) {
        return mtCustomerMapper
                .selectByCondition(Condition.builder(MtCustomer.class)
                        .andWhere(Sqls.custom().andEqualTo(MtCustomer.FIELD_TENANT_ID, tenantId)
                                .andIn(MtCustomer.FIELD_CUSTOMER_ID, customerIds, true))
                        .build());
    }
}
