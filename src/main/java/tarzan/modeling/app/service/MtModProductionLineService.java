package tarzan.modeling.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.api.dto.MtModProductionLineDTO2;
import tarzan.modeling.api.dto.MtModProductionLineDTO3;
import tarzan.modeling.api.dto.MtModProductionLineDTO4;
import tarzan.modeling.domain.vo.MtModProductionLineVO3;

/**
 * 生产线应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModProductionLineService {

    Page<MtModProductionLineVO3> queryForUi(Long tenantId, MtModProductionLineDTO2 dto, PageRequest pageRequest);

    MtModProductionLineDTO3 queryInfoForUi(Long tenantId, String prodLineId);

    String saveForUi(Long tenantId, MtModProductionLineDTO4 dto);

}
