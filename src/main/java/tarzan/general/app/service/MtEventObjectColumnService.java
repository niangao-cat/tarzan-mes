package tarzan.general.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;
import tarzan.general.api.dto.MtEventObjectColumnDTO;
import tarzan.general.domain.entity.MtEventObjectColumn;

/**
 * 对象列定义应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectColumnService extends BaseService<MtEventObjectColumn> {

    /**
     * 根据对象类型Id查询对象列
     * 
     * @author benjamin
     * @date 2019-08-12 11:43
     * @param tenantId 租户Id
     * @param eventObjectTypeId 对象类型Id
     * @param pageRequest PageRequest
     * @return list
     */
    List<MtEventObjectColumn> queryEventObjectColumnByEventObjectTypeForUi(Long tenantId, String eventObjectTypeId,
                    PageRequest pageRequest);

    /**
     * 保存对象列
     * 
     * @author benjamin
     * @date 2019-08-12 11:43
     * @param tenantId 租户Id
     * @param dto 对象列DTO
     * @return list
     */
    MtEventObjectColumnDTO saveEventObjectColumnForUi(Long tenantId, MtEventObjectColumnDTO dto);

    /**
     * 删除对象列
     * 
     * @author benjamin
     * @date 2019-08-14 16:40
     * @param tenantId 租户Id
     * @param eventObjectColumnIdList 对象列Id集合
     * @return int
     */
    Integer deleteEventObjectColumnForUi(Long tenantId, List<String> eventObjectColumnIdList);
}
