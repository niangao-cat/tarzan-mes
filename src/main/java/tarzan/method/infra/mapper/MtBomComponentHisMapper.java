package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomComponentHis;

/**
 * 装配清单行历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomComponentHisMapper extends BaseMapper<MtBomComponentHis> {

    List<MtBomComponentHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    List<MtBomComponentHis> selectBybomComponentId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomComponentId") String bomComponentId);

}
