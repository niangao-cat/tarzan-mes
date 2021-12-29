package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModSiteManufacturing;

/**
 * 站点生产属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModSiteManufacturingMapper extends BaseMapper<MtModSiteManufacturing> {

    List<MtModSiteManufacturing> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "ids") List<String> ids);
}
