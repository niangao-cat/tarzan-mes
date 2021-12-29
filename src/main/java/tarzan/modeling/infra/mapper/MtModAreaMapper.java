package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.vo.MtModAreaVO2;

/**
 * 区域Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModAreaMapper extends BaseMapper<MtModArea> {

    List<MtModArea> selectAreas(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "areaIds") List<String> areaIds);

    MtModAreaVO2 selectAreaById(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtModAreaVO2 dto);

    List<MtModArea> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "areaIds") List<String> areaIds);

    /**
     * Oracle 空字符查询
     */
    List<MtModArea> selectForEmptyString(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtModArea dto);

}
