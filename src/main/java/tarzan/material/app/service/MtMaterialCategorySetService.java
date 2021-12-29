package tarzan.material.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtMaterialCategorySetDTO;
import tarzan.material.domain.entity.MtMaterialCategorySet;


/**
 * 物料类别集应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategorySetService {
    Page<MtMaterialCategorySet> listUi(Long tenantId, MtMaterialCategorySetDTO dto, PageRequest pageRequest);

    String materialCategorySetUpdate(Long tenantId, MtMaterialCategorySet dto);
    
}
