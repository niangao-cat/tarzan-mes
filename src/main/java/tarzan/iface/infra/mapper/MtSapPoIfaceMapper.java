package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtSapPoIface;

/**
 * SAP采购订单接口表Mapper
 *
 * @author peng.yuan@hand-china.com 2019-10-08 19:40:53
 */
public interface MtSapPoIfaceMapper extends BaseMapper<MtSapPoIface> {
    /**
     * get unprocessed List
     *
     * condition: status is 'N' or 'E'
     *
     * @return List
     */
    List<MtSapPoIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);

}
