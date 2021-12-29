package tarzan.general.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagHisDTO;
import tarzan.general.api.dto.MtTagHisDTO1;
import tarzan.general.domain.entity.MtTag;

/**
 * 数据收集项历史表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagHisService {
    /**
     * 查询TAG历史
     *
     * @author benjamin
     * @date 2019-07-04 15:13
     * @return List
     */
    Page<MtTagHisDTO> queryTagHistory(Long tenantId, MtTagHisDTO1 dto, PageRequest pageRequest);

    /**
     * 保存TAG历史
     *
     * @author benjamin
     * @date 2019-07-03 17:21
     * @param tenantId Long
     * @param mtTag MtTag
     * @param eventId 事件Id
     */
    void saveTagHistory(Long tenantId, MtTag mtTag, String eventId);
}
