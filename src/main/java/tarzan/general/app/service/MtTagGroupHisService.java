package tarzan.general.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagGroupHisDTO;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.domain.entity.MtTagGroup;

/**
 * 数据收集组历史表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupHisService {

    /**
     * UI查询数据收集组历史
     * 
     * @author benjamin
     * @date 2019/9/26 3:09 PM
     * @param tenantId 租户Id
     * @param dto MtTagGroupHisDTO2
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtTagGroupHisDTO> queryTagGroupHisForUi(Long tenantId, MtTagGroupHisDTO2 dto, PageRequest pageRequest);

    /**
     * 保存数据收集组历史
     * 
     * @author benjamin
     * @date 2019/9/17 4:25 PM
     * @param tenantId 租户Id
     * @param eventId 事件Id
     * @param mtTagGroup MtTagGroup
     */
    String saveTagGroupHis(Long tenantId, String eventId, MtTagGroup mtTagGroup);
}
