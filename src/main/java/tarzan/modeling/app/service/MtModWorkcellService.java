package tarzan.modeling.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.api.dto.MtModWorkcellDTO2;
import tarzan.modeling.api.dto.MtModWorkcellDTO3;
import tarzan.modeling.api.dto.MtModWorkcellDTO4;
import tarzan.modeling.domain.vo.MtModWorkcellVO3;

/**
 * 工作单元应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModWorkcellService {

    Page<MtModWorkcellVO3> queryForUi(Long tenantId, MtModWorkcellDTO2 dto, PageRequest pageRequest);

    MtModWorkcellDTO3 queryInfoForUi(Long tenantId, String workcellId);

    String saveForUi(Long tenantId, MtModWorkcellDTO4 dto);

}
