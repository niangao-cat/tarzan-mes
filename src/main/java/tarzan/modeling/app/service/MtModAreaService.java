package tarzan.modeling.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;
import tarzan.modeling.api.dto.MtModAreaDTO;
import tarzan.modeling.api.dto.MtModAreaDTO2;
import tarzan.modeling.api.dto.MtModAreaDTO3;
import tarzan.modeling.domain.entity.MtModArea;

/**
 * 区域应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModAreaService extends BaseService<MtModArea> {

    /**
     * 查询区域信息
     * 
     * @author benjamin
     * @date 2019-08-12 20:09
     * @param tenantId 租户Id
     * @param dto MtModArea
     * @param pageRequest PageRequest
     * @return list
     */
    List<MtModArea> queryModAreaForUi(Long tenantId, MtModAreaDTO dto, PageRequest pageRequest);

    /**
     * 根据区域Id查询区域详细信息 包含区域采购属性和区域计划属性
     * 
     * @author benjamin
     * @date 2019-08-12 20:09
     * @param tenantId 租户Id
     * @param areaId 区域Id
     * @return MtModAreaDTO3
     */
    MtModAreaDTO2 queryModAreaDetailForUi(Long tenantId, String areaId);

    /**
     * 保存区域信息
     * 
     * @author benjamin
     * @date 2019-08-12 20:10
     * @param tenantId 租户Id
     * @param dto MtModAreaDTO3
     * @return MtModAreaDTO3
     */
    MtModAreaDTO3 saveModAreaForUi(Long tenantId, MtModAreaDTO3 dto);
}
