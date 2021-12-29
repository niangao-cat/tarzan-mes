package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModSiteSchedule;

/**
 * 站点计划属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModSiteScheduleMapper extends BaseMapper<MtModSiteSchedule> {

    List<MtModSiteSchedule> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "siteIds") List<String> siteIds);
}
