package tarzan.material.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtMaterialDTO5;
import tarzan.material.api.dto.MtMaterialDTO6;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.vo.MtMaterialVO;

/**
 * 物料基础属性应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialService {
    /**
     * materialPropertyGet-获取物料基础属性
     *
     * @param tenantId
     * @param materialId
     * @return
     */
    MtMaterialVO materialPropertyGetForUi(Long tenantId, String materialId);

    /**
     * materialListUi-获取物料数据
     *
     * update remarks
     * <ul>
     * <li>2019-9-26 benjamin 修改查询参数</li>
     * </ul>
     * 
     * @param tenantId 租户id
     * @param dto MtMaterialDTO6
     * @param pageRequest 分页参数
     * @return
     */
    Page<MtMaterialVO> materialListUi(Long tenantId, MtMaterialDTO6 dto, PageRequest pageRequest);

    /**
     * saveExtendAttrForUi-获取物料数据
     *
     * @param tenantId 租户id
     * @param dto 物料对象
     * @return 物料ID
     */
    String materialSaveForUi(Long tenantId, MtMaterialDTO5 dto);

    /**
     * materialCopyForUi-物料复制
     *
     * @param tenantId 租户id
     * @param materialCode 物料代码
     * @return 物料信息
     */
    MtMaterial materialCheckForUi(Long tenantId, String materialCode);
}
