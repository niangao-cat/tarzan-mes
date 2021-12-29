package tarzan.general.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagGroupAssignHisDTO;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.domain.entity.MtTagGroupAssign;

/**
 * 数据收集项分配收集组历史表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupAssignHisService {

    /**
     * UI查询数据收集组分配数据项历史
     * 
     * @author benjamin
     * @date 2019/9/26 3:51 PM
     * @param tenantId 租户Id
     * @param dto MtTagGroupHisDTO2
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtTagGroupAssignHisDTO> queryTagGroupAssignHisForUi(Long tenantId, MtTagGroupHisDTO2 dto,
                                                             PageRequest pageRequest);

    /**
     * 保存数据收集组分配数据项历史数据
     *
     * @author benjamin
     * @date 2019/9/17 4:05 PM
     * @param tenantId 租户Id
     * @param eventId 事件Id
     * @param mtTagGroupAssign MtTagGroupAssign
     */
    void saveTagGroupAssignHis(Long tenantId, String eventId, MtTagGroupAssign mtTagGroupAssign);
}
