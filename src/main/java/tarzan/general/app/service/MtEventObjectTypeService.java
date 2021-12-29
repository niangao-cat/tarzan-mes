package tarzan.general.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtEventObjectTypeColumnDTO;
import tarzan.general.api.dto.MtEventObjectTypeDTO;
import tarzan.general.domain.entity.MtEventObjectType;

/**
 * 对象类型定义应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectTypeService {

    /**
     * 根据条件查询对象类型
     * 
     * @author benjamin
     * @date 2019-08-12 11:44
     * @param tenantId 租户Id
     * @param dto 对象类型DTO
     * @param pageRequest PageRequest
     * @return list
     */
    Page<MtEventObjectType> queryEventObjectTypeForUi(Long tenantId, MtEventObjectTypeDTO dto, PageRequest pageRequest);

    /**
     * 保存对象类型
     * 
     * @author benjamin
     * @date 2019-08-12 11:45
     * @param tenantId 租户Id
     * @param dto 对象类型DTO
     * @return list
     */
    MtEventObjectType saveEventObjectTypeForUi(Long tenantId, MtEventObjectTypeDTO dto);

    /**
     * 查询对象类型展示信息
     * 
     * @author benjamin
     * @date 2019-08-12 14:33
     * @param tenantId 租户Id
     * @param objectTypeId 对象类型Id
     * @return MtEventObjectTypeColumnDTO
     */
    MtEventObjectTypeColumnDTO queryEventObjectTypeQuerySqlForUi(Long tenantId, String objectTypeId);

    /**
     * 删除对象类型
     * 
     * @author benjamin
     * @date 2019-08-14 16:04
     * @param tenantId 租户Id
     * @param objectTypeIdList 对象类型Id集合
     * @return int
     */
    Integer deleteEventObjectTypeForUi(Long tenantId, List<String> objectTypeIdList);
}
