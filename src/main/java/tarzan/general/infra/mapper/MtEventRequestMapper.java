package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.domain.entity.MtEventRequest;
import tarzan.general.domain.vo.MtEventRequestVO2;
import tarzan.general.domain.vo.MtEventRequestVO3;
import tarzan.general.domain.vo.MtEventRequestVO4;

/**
 * 事件请求记录Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventRequestMapper extends BaseMapper<MtEventRequest> {

    List<MtEventRequest> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventRequestIds") List<String> eventRequestIds);

    List<MtEventRequest> selectByConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtEventRequestVO2 condition);

    List<MtEventRequestVO4> selectCondition(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "dto") MtEventRequestVO3 dto);
}
