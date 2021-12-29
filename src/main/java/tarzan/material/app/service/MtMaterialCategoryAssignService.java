package tarzan.material.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtMaterialCategoryAssignDTO;
import tarzan.material.domain.vo.MaterialCategoryAssignVO;

/**
 * 物料类别分配应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategoryAssignService {
    /**
     * selectMaterialCategoryAssiteById-根据物料获取物料分配
     *
     * @param tenantId 租户id
     * @param materialId 物料id
     * @return
     */
    Page<MaterialCategoryAssignVO> selectMaterialCategoryAssiteById(Long tenantId, String materialId, PageRequest pageRequest);
    /**
     * materialCategoryAssignSave-物料类别分配保存
     * @param tenantId 租户id
     * @param dto 物料类别分配对象
     * @param materialId 物料id
     * @return
     */
    void materialCategoryAssignSave(Long tenantId, List<MtMaterialCategoryAssignDTO> dto, String materialId);
    /**
     * materialCategoryAssignDelete-物料类别分配删除
     * @param tenantId 租户id
     * @param materialCategoryAssignIds 物料类别分配ID集合
     * @return
     */
    void materialCategoryAssignDelete(Long tenantId, List<String> materialCategoryAssignIds);
}
