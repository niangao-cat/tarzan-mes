package tarzan.general.infra.mapper;

import java.util.List;

import io.choerodon.core.domain.Page;
import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtEventObjectTypeRelDTO2;
import tarzan.general.domain.entity.MtEventObjectTypeRel;

/**
 * 事件类型与对象类型关系定义Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectTypeRelMapper extends BaseMapper<MtEventObjectTypeRel> {

    List<String> selectObjectTypeId(@Param(value = "tenantId") Long tenantId, @Param(value = "eventId") String eventId);

    Page<MtEventObjectTypeRelDTO2> selectEventTypeId(@Param(value = "tenantId") Long tenantId,@Param(value = "eventTypeId") String eventTypeId);
}
