package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtCostcenter;

/**
 * 成本中心资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:33
 */
public interface MtCostcenterRepository extends BaseRepository<MtCostcenter>, AopProxy<MtCostcenterRepository> {

    /**
     * 根据成本中心编码批量查询成本中心信息
     *
     * @author benjamin
     * @date 2019-07-24 09:31
     * @param costCenterCodeList 成本中心编码集合
     * @return List
     */
    List<MtCostcenter> queryCustomerByCode(Long tenantId, List<String> costCenterCodeList);
    
}
