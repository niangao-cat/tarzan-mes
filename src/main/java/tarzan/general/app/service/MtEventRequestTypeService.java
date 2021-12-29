package tarzan.general.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtEventRequestTypeDTO;
import tarzan.general.domain.vo.MtEventRequestTypeVO2;

/**
 * 事件组类型定义应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventRequestTypeService {

    Page<MtEventRequestTypeVO2> queryForUi(Long tenantId, MtEventRequestTypeDTO dto, PageRequest pageRequest);

    String saveForUi(Long tenantId, MtEventRequestTypeVO2 dto);

}
