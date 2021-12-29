package tarzan.modeling.app.service;


import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO2;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO3;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO4;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO2;

/**
 * 库位组应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModLocatorGroupService {

    Page<MtModLocatorGroupVO2> queryForUi(Long tenantId, MtModLocatorGroupDTO2 dto, PageRequest pageRequest);

    String saveForUi(Long tenantId, MtModLocatorGroupDTO dto);

    MtModLocatorGroupDTO4 oneForUi(Long tenantId, String locatorGroupId);

    String saveWithAttrForUi(Long tenantId, MtModLocatorGroupDTO3 dto);
}
