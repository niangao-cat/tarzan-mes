package tarzan.material.app.service;



import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtMaterialCategoryDTO2;
import tarzan.material.domain.vo.MtMaterialCategoryVO2;
import tarzan.material.domain.vo.MtMaterialCategoryVO3;

/**
 * 物料类别应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategoryService {

    /**
     * 获取物料类别分页
     * @author xiao.tang02@hand-china.com 2019年8月13日下午10:05:54
     * @param tenantId
     * @param dto
     * @param request
     * @return
     * @return Page<MtMaterialCategoryVO3>
     */
    Page<MtMaterialCategoryVO3> listMaterialCategoryForUi(Long tenantId, MtMaterialCategoryVO2 dto, PageRequest request);

    /**
     * 新增&更新物料类别
     * @author xiao.tang02@hand-china.com 2019年8月13日下午10:06:04
     * @param tenantId
     * @param dto
     * @return
     * @return String
     */
    String saveMaterialCategoryForUi(Long tenantId, MtMaterialCategoryDTO2 dto);

    /**
     * 校验一组物料类别是否相似
     * @author xiao.tang02@hand-china.com 2019年8月21日下午1:49:13
     * @param tenantId
     * @param materialCategoryId
     * @return
     * @return boolean
     */
    boolean verifyDefaultSetForUi(Long tenantId, String materialCategoryId);
    
}
