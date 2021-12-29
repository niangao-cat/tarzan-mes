package tarzan.general.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtEventTypeDTO;
import tarzan.general.api.dto.MtEventTypeDTO2;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.vo.MtEventTypeVO;

/**
 * 事件类型定义应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventTypeService {

    MtEventType saveMtEventType(Long tenantId, MtEventTypeDTO dto);

    Page<MtEventType> eventTypeQuery(Long tenantId, PageRequest pageRequest, MtEventTypeDTO2 dto);

    List<String> propertyLimitEventTypeQueryUi(Long tenantId, MtEventTypeVO dto);
}
