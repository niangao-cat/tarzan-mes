package tarzan.iface.infra.repository.impl;

import java.util.Collections;
import java.util.List;

import io.tarzan.common.domain.util.StringHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tarzan.iface.domain.entity.MtCostcenter;
import tarzan.iface.domain.repository.MtCostcenterRepository;
import tarzan.iface.infra.mapper.MtCostcenterMapper;

/**
 * 成本中心 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:33
 */
@Component
public class MtCostcenterRepositoryImpl extends BaseRepositoryImpl<MtCostcenter> implements MtCostcenterRepository {

    @Autowired
    private MtCostcenterMapper mtCostcenterMapper;

    @Override
    public List<MtCostcenter> queryCustomerByCode(Long tenantId, List<String> costCenterCodeList) {
        if (CollectionUtils.isEmpty(costCenterCodeList)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("COSTCENTER_CODE", costCenterCodeList, 1000);
        return mtCostcenterMapper.queryCostCenterByCode(tenantId, whereInValuesSql);
    }

}
