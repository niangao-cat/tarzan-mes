package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtWorkOrderIface;

/**
 * 工单接口表Mapper
 *
 * @author xiao.tang02@hand-china.com 2019-08-23 14:16:17
 */
public interface MtWorkOrderIfaceMapper extends BaseMapper<MtWorkOrderIface> {
    /**
     * get unprocessed List
     * <p>
     * condition: status is 'N' or 'E'
     *
     * @return List
     * @author benjamin
     * @date 2019-07-10 15:33
     */
    List<MtWorkOrderIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);

    List<MtWorkOrderIface> getMyUnprocessedList(@Param(value = "tenantId") Long tenantId, @Param(value = "batchId") Long batchId);
}
