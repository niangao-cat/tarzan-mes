package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.vo.MtModSiteVO1;

/**
 * 站点Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModSiteMapper extends BaseMapper<MtModSite> {


    List<MtModSite> selectSites(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "siteIds") List<String> siteIds);

    MtModSiteVO1 selectSiteById(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtModSiteVO1 dto);

    List<MtModSite> selectSitesLimitType(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "siteIds") List<String> siteIds, @Param(value = "siteType") String siteType);

    List<MtModSite> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "siteIds") List<String> siteIds);

    List<MtModSite> selectForCombination(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtModSite dto);
}
