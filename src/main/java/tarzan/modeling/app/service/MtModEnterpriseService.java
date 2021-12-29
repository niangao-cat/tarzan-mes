package tarzan.modeling.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.api.dto.MtModEnterpriseDTO;
import tarzan.modeling.api.dto.MtModEnterpriseDTO2;
import tarzan.modeling.domain.entity.MtModEnterprise;

/**
 * 企业应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModEnterpriseService {

    Page<MtModEnterprise> queryForUi(Long tenantId, MtModEnterpriseDTO2 dto, PageRequest pageRequest);

    MtModEnterpriseDTO saveForUi(Long tenantId, MtModEnterpriseDTO dto);

    MtModEnterprise oneForUi(Long tenantId, String enterpriseId);
}
