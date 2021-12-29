package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtCustomerSite;

/**
 * 客户地点Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
public interface MtCustomerSiteMapper extends BaseMapper<MtCustomerSite> {

    /**
     * 根据客户Id批量查询客户地点
     *
     * @author xiao.tang02@hand-china.com
     * @date 2019-07-23 21:52
     * @param tenantId 租户id
     * @param customerIdList 客户Id集合
     * @return List
     */
    List<MtCustomerSite> queryCustomerSiteByCustomerId(@Param("tenantId") Long tenantId,
                    @Param("customerIdList") List<String> customerIdList);


}
