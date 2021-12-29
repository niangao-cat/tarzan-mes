package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomHis;
import tarzan.method.domain.vo.MtBomHisVO5;

/**
 * 装配清单头历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomHisMapper extends BaseMapper<MtBomHis> {

    List<MtBomHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    List<MtBomHis> selectByBomId(@Param(value = "tenantId") Long tenantId, @Param(value = "bomId") String bomId);

    MtBomHisVO5 selectRecent(@Param(value = "tenantId") Long tenantId, @Param(value = "bomId") String bomId);
}
