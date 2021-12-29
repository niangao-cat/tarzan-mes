package tarzan.material.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtPfepManufacturingDTO;
import tarzan.material.api.dto.MtPfepManufacturingDTO2;
import tarzan.material.api.dto.MtPfepManufacturingDTO3;
import tarzan.material.domain.vo.MtPfepManufacturingVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO2;

/**
 * 物料类别生产属性应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepManufacturingCategoryService {

    Page<MtPfepManufacturingVO> listForUi(Long tenantId, String materialCategoryId, PageRequest pageRequest);

    MtPfepManufacturingVO2 detailForUi(Long tenantId, String kid);

    MtPfepManufacturingDTO3 savePfepManufacturingCategoryForUi(Long tenantId, MtPfepManufacturingDTO dto);

    MtPfepManufacturingDTO3 copyPfepManufacturingCategoryForUi(Long tenantId, MtPfepManufacturingDTO2 dto);
}
