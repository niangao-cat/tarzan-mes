package tarzan.material.app.service;



import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtMaterialCategorySiteDTO;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO2;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO3;

/**
 * 物料类别站点分配应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategorySiteService {

    /**
     * 获取物料类别分配站点列表
     * @author xiao.tang02@hand-china.com 2019年8月21日下午7:59:29
     * @param tenantId
     * @param materialCategoryId
     * @param pageRequest
     * @return
     * @return Page<MtMaterialCategorySiteVO>
     */
    Page<MtMaterialCategorySiteVO> listMaterialCategorySiteForUi(Long tenantId,
                                                                 String materialCategoryId, PageRequest pageRequest);

    /**
     * 新增或更新物料类别站点分配
     * @author xiao.tang02@hand-china.com 2019年8月21日下午7:59:46
     * @param tenantId
     * @param dto
     * @return
     * @return String
     */
    String saveMaterialCategorySiteForUi(Long tenantId, MtMaterialCategorySiteDTO dto);

    /**
     * 删除物料类别站点分配
     * @author xiao.tang02@hand-china.com 2019年8月21日下午7:59:46
     * @param tenantId
     * @param materialCategorySiteId
     * @return
     * @return String
     */
    void removeMaterialCategorySiteForUi(Long tenantId, String materialCategorySiteId);

    /**
     * 获取当前物料类别下未分配的站点
     * @author xiao.tang02@hand-china.com 2019年8月21日下午8:00:36
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     * @return Page<MtMaterialCategorySiteVO2>
     */
    Page<MtMaterialCategorySiteVO2> materialCategorySiteNotExistForUi(Long tenantId,
                                                                      MtMaterialCategorySiteVO3 condition, PageRequest pageRequest);
    
}
