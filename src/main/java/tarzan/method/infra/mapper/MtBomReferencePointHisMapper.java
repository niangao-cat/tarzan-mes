package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomReferencePointHis;

/**
 * 装配清单行参考点关系历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomReferencePointHisMapper extends BaseMapper<MtBomReferencePointHis> {

    List<MtBomReferencePointHis> selectBomReferencePointHisByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    List<MtBomReferencePointHis> selectByBomReferencePointId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomReferencePointId") String bomReferencePointId);
}
