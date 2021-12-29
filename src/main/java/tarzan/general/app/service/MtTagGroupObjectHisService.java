package tarzan.general.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.api.dto.MtTagGroupObjectHisDTO;
import tarzan.general.domain.entity.MtTagGroupObject;

/**
 * 数据收集组关联对象历史表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupObjectHisService {

    /**
     * UI查询数据收集组关联对象历史
     * 
     * @author benjamin
     * @date 2019/9/26 3:41 PM
     * @param tenantId 租户Id
     * @param dto MtTagGroupHisDTO2
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtTagGroupObjectHisDTO> queryTagGroupObjectHisForUi(Long tenantId, MtTagGroupHisDTO2 dto,
                                                             PageRequest pageRequest);

    /**
     * 保存数据收集组关联对象历史数据
     * 
     * @author benjamin
     * @date 2019/9/17 4:05 PM
     * @param tenantId 租户Id
     * @param eventId 事件Id
     * @param mtTagGroupObject MtTagGroupObject
     */
    void saveTagGroupObjectHis(Long tenantId, String eventId, MtTagGroupObject mtTagGroupObject);
}
