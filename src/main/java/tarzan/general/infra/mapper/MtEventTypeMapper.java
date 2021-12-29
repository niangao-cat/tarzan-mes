package tarzan.general.infra.mapper;

import java.util.List;

import io.choerodon.core.domain.Page;
import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtEventTypeDTO2;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.vo.MtEventTypeVO;

/**
 * 事件类型定义Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventTypeMapper extends BaseMapper<MtEventType> {

    List<String> propertyLimitEventTypeQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEventTypeVO dto);

    List<MtEventType> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventTypeIds") List<String> eventTypeIds);

    MtEventType selectByEventId(@Param(value = "tenantId") Long tenantId, @Param(value = "eventId") String eventId);

    Page<MtEventType> selectEventType(@Param(value = "tenantId") Long tenantId,@Param(value="dto" )MtEventTypeDTO2 dto);
}
