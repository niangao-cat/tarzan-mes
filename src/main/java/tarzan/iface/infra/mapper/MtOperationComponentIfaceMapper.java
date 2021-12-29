package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtOperationComponentIface;
import tarzan.iface.domain.vo.MtOperationComponentIfaceVO;
import tarzan.iface.domain.vo.MtOperationComponentIfaceVO1;
import tarzan.iface.domain.vo.MtOperationComponentIfaceVO3;
import tarzan.method.domain.entity.MtRouterOperationComponent;

/**
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtOperationComponentIfaceMapper extends BaseMapper<MtOperationComponentIface> {

    /**
     * 获取接口表中status为E或N的数据
     */
    List<MtOperationComponentIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);

    /**
     * 查询对应的router_operation_id
     */
    List<MtOperationComponentIfaceVO> getRouterOperationIdList(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "headList") List<MtOperationComponentIfaceVO3> headList);

    /**
     * 查询对应的bom_component_id
     */
    List<MtOperationComponentIfaceVO1> getBomComponentIdList(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "headList") List<MtOperationComponentIfaceVO3> headList);

    /**
     * 获取mt_router_operation_component中的对应的router_operation_id
     */
    List<MtRouterOperationComponent> getOperationComponent(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "list") String list);

    /**
     * 获取mt_router_operation_component中的对应的Bom_component_id
     */
    List<MtRouterOperationComponent> getBomComponentId(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "list") String list);

    List<MtOperationComponentIface> getMyUnprocessedList(@Param(value = "tenantId")Long tenantId, @Param(value = "batchId")Long batchId);
}
