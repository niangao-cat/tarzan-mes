package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtCustomerIface;

/**
 * 客户数据接口表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtCustomerIfaceMapper extends BaseMapper<MtCustomerIface> {


    /**
     * get unprocessed List
     *
     * condition: status is 'N' or 'E'
     *
     * @author benjamin
     * @date 2019-07-10 15:33
     * @return List
     */
    List<MtCustomerIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);
}
