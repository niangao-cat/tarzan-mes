package tarzan.general.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtEventDTO;
import tarzan.general.api.dto.MtEventDTO2;
import tarzan.general.domain.vo.MtEventVO5;

/**
 * 事件记录应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventService {

    /**
     * 查询事件
     * 
     * 特殊情况：如果有事件请求Id，需要进行分组，查询事件请求信息
     * 
     * @author benjamin
     * @date 2019-08-13 14:06
     * @param tenantId 租户Id
     * @param dto MtEventDTO
     * @param pageRequest PageRequest
     * @return list
     */
    Page<MtEventDTO2> eventUnionRequestGroupQueryForUi(Long tenantId, MtEventDTO dto, PageRequest pageRequest);

    /**
     * 根据父事件查询事件集合
     * 
     * @author benjamin
     * @date 2019-08-13 14:08
     * @param tenantId 租户Id
     * @param parentEventId 父事件Id
     * @return list
     */
    List<MtEventVO5> parentEventQueryForUi(Long tenantId, String parentEventId);
}
