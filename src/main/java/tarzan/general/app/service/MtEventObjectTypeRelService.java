package tarzan.general.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtEventObjectTypeRelDTO;
import tarzan.general.api.dto.MtEventObjectTypeRelDTO2;
import tarzan.general.domain.vo.MtEventObjectTypeRelVO1;
import tarzan.general.domain.vo.MtEventObjectTypeRelVO2;
import tarzan.general.domain.vo.MtEventObjectTypeRelVO5;
import tarzan.general.domain.vo.MtEventObjectTypeRelVO6;


/**
 * 事件类型与对象类型关系定义应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectTypeRelService {
    /**
     * 根据事件查询事件影响对象
     * 
     * @author benjamin
     * @date 2019-08-13 14:18
     * @param tenantId 租户Id
     * @param vo MtEventObjectTypeRelVO5
     * @return list
     */
    List<MtEventObjectTypeRelVO6> eventLimitEventObjectInfoQueryForUi(Long tenantId, MtEventObjectTypeRelVO5 vo);

    MtEventObjectTypeRelDTO2 saveEventObjectTypeRel(Long tenantId, MtEventObjectTypeRelDTO dto);

    /**
     * 根据事件类型ID获取对应的对象类型
     * @param tenantId
     * @param pageRequest
     * @param eventTypeId
     * @return
     */
    Page<MtEventObjectTypeRelDTO2> eventTypeIdLimitRel(Long tenantId, PageRequest pageRequest,String eventTypeId);

    /**
     * 根据事件类型获取关联的对象类型UI
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEventObjectTypeRelVO2> eventTypeLimitObjectTypeQueryUi(Long tenantId, MtEventObjectTypeRelVO1 dto);
}
