package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomSiteAssign;
import tarzan.method.domain.vo.MtBomSiteAssignVO2;

/**
 * 装配清单站点分配资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSiteAssignRepository
                extends BaseRepository<MtBomSiteAssign>, AopProxy<MtBomSiteAssignRepository> {

    /**
     * 根据装配清单获取分配的有效站点
     * 
     * @param tenantId
     * @param bomId
     * @return
     */
    List<String> bomLimitEnableSiteQuery(Long tenantId, String bomId);

    /**
     * 根据BOM Id查询BOM批量站点分配信息
     * 
     * @author benjamin
     * @date 2019-07-31 10:19
     * @param bomIdList BOM Id集合
     * @return List
     */
    List<MtBomSiteAssign> bomLimitBomSiteAssignBatchQuery(Long tenantId, List<String> bomIdList);

    /**
     * 根据站点获取装配清单
     * 
     * @param tenantId
     * @param siteId
     * @return
     */
    List<String> siteLimitBomQuery(Long tenantId, String siteId);

    /**
     * bomSiteAssign
     * 
     * @param tenantId
     * @param dto
     */
    void bomSiteAssign(Long tenantId, MtBomSiteAssign dto);

    /**
     * bomListLimitSiteQuery-根据装配清单列表获取分配的站点
     *
     * @author benjamin
     * @date 2019-07-31 10:19
     * @param tenantId 租户Id
     * @param bomIdList BOM Id集合
     * @param enableFlag 是否有效
     * @return List
     */
    List<MtBomSiteAssignVO2> bomListLimitSiteQuery(Long tenantId, List<String> bomIdList, String enableFlag);

}
