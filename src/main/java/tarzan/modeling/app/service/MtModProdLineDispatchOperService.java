package tarzan.modeling.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.api.dto.MtModProdLineDispatchOperDTO;
import tarzan.modeling.domain.entity.MtModProdLineDispatchOper;

/**
 * 生产线调度指定工艺应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModProdLineDispatchOperService {

    /**
     * 查询生产线指定调度工艺
     * 
     * @author benjamin
     * @date 2019-08-19 13:24
     * @param tenantId 租户Id
     * @param prodLineId 生产线Id
     * @param pageRequest PageRequest
     * @return list
     */
    List<MtModProdLineDispatchOper> prodLineIdLimitDispatchOperationQueryForUi(Long tenantId, String prodLineId,
                    PageRequest pageRequest);

    /**
     * 保存生产线指定调度工艺
     * 
     * @author benjamin
     * @date 2019-08-19 13:49
     * @param tenantId 租户Id
     * @param dto MtModProdLineDispatchOperDTO
     * @return MtModProdLineDispatchOperDTO
     */
    MtModProdLineDispatchOperDTO saveModProdLineDispatchOperForUi(Long tenantId, MtModProdLineDispatchOperDTO dto);

    /**
     * 删除生产线指定工艺
     * 
     * @author benjamin
     * @date 2019-08-19 13:50
     * @param tenantId 租户Id
     * @param modProdLineDispatchOperIdList 生产线指定工艺Id集合
     * @return int
     */
    Integer deleteModProdLineDispatchOperForUi(Long tenantId, List<String> modProdLineDispatchOperIdList);

}
