package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.domain.entity.MtEventRequestType;
import tarzan.general.domain.vo.MtEventRequestTypeVO;

/**
 * 事件组类型定义Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventRequestTypeMapper extends BaseMapper<MtEventRequestType> {

    List<String> propertyLimitEventGroupTypeQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEventRequestTypeVO dto);

    List<MtEventRequestType> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventRequestTypeIds") List<String> eventRequestTypeIds);
}
