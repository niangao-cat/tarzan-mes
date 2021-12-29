package tarzan.material.app.service;


import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtPfepManufacturingDTO;
import tarzan.material.api.dto.MtPfepManufacturingDTO2;
import tarzan.material.api.dto.MtPfepManufacturingDTO3;
import tarzan.material.domain.vo.MtPfepManufacturingVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO2;

/**
 * 物料生产属性应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepManufacturingService {

    Page<MtPfepManufacturingVO> listForUi(Long tenantId, String materialId, PageRequest pageRequest);

    MtPfepManufacturingVO2 detailForUi(Long tenantId, String kid);

    MtPfepManufacturingDTO3 saveMtPfepManufacturingForUi(Long tenantId, MtPfepManufacturingDTO dto);

    MtPfepManufacturingDTO3 copyPfepManufacturingForUi(Long tenantId, MtPfepManufacturingDTO2 dto);

}
