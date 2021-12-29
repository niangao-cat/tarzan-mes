package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtRoutingOperationIface;

/**
 * 工艺路线接口表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtRoutingOperationIfaceMapper extends BaseMapper<MtRoutingOperationIface> {

    /**
     * get unprocessed List
     * <p>
     * condition: status is 'N' or 'E'
     *
     * @return List
     * @author benjamin
     * @date 2019-07-10 15:33
     */
    List<MtRoutingOperationIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);

    List<MtRoutingOperationIface> getMyUnprocessedList(@Param(value = "tenantId") Long tenantId, @Param(value = "batchId") Long batchId);
}
