package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtCostcenter;

/**
 * 成本中心Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:33
 */
public interface MtCostcenterMapper extends BaseMapper<MtCostcenter> {

    /**
     * query cost center by cost center code
     *
     * @author benjamin
     * @date 2019-07-23 19:42
     * @param costCenterCodeList 成本中心编码集合
     * @return List
     */
    List<MtCostcenter> queryCostCenterByCode(@Param("tenantId") Long tenantId,
                                             @Param("costCenterCodeList") String costCenterCodeList);
    
}
