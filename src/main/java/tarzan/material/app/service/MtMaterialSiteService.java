package tarzan.material.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import tarzan.material.api.dto.MtMaterialSiteDTO4;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.vo.MaterialSiteVO;

/**
 * 物料站点分配应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialSiteService {
    /**
     * selectMaterialSiteById-根据物料获取物料站位
     *
     * @param tenantId   租户id
     * @param materialId 物料id
     * @return
     */
    Page<MaterialSiteVO> selectMaterialSiteById(Long tenantId, String materialId, PageRequest pageRequest);

    /**
     * queryExtendAttrForUi-查询物料站点扩展字段
     *
     * @param tenantId       租户id
     * @param materialSiteId 物料站点id
     * @return
     */
    List<MtExtendAttrDTO> queryExtendAttrForUi(Long tenantId, String materialSiteId);

    /**
     * saveExtendAttrForUi-保存物料站点扩展字段
     *
     * @param tenantId       租户id
     * @param materialSiteId 物料站点id
     * @return
     */
    void saveExtendAttrForUi(Long tenantId, String materialSiteId, List<MtExtendAttrDTO3> materialSiteAttrs);

    /**
     * materialSiteSave-物料站点保存
     *
     * @param tenantId   租户id
     * @param dto        物料对象
     * @param materialId 物料id
     * @return 物料ID
     */
    void materialSiteSave(Long tenantId, List<MtMaterialSiteDTO4> dto, String materialId);

    /**
     * materialSiteLimitRelationGet-物料站点获取
     *
     * @param tenantId 租户id
     * @param dto      物料站点对象
     * @return 物料ID
     */
    String materialSiteLimitRelationGet(Long tenantId, MtMaterialSite dto);

    /**
     * materialSiteDelete-物料站点删除
     *
     * @param tenantId        租户id
     * @param materialSiteIds 物料站点id集合
     */
    void materialSiteDelete(Long tenantId, List<String> materialSiteIds);
}
