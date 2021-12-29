package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtCustomer;

/**
 * 客户Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
public interface MtCustomerMapper extends BaseMapper<MtCustomer> {

    List<MtCustomer> batchSelectCustomerByIdList(@Param("tenantId") Long tenantId,
                    @Param("customerIdList") List<String> customerIdList);

    /**
     * 根据客户编码批量查询客户信息
     *
     * @author xiao.tang02@hand-china.com 2019年8月1日上午10:59:23
     * @param tenantId
     * @param customerCodeList
     * @return
     * @return List<MtCustomer>
     */
    List<MtCustomer> queryCustomerByCode(@Param("tenantId") Long tenantId,
                                         @Param("customerCodeList") String customerCodeList);
}
