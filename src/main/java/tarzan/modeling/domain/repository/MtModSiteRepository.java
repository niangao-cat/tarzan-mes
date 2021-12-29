package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.vo.MtModSiteVO;
import tarzan.modeling.domain.vo.MtModSiteVO6;

/**
 * 站点资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModSiteRepository extends BaseRepository<MtModSite>, AopProxy<MtModSiteRepository> {

    /**
     * siteBasicPropertyGet-获取站点基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param siteId
     * @return
     */
    MtModSite siteBasicPropertyGet(Long tenantId, String siteId);

    /**
     * siteBasicPropertyBatchGet-批量获取站点基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param siteIds
     * @return
     */
    List<MtModSite> siteBasicPropertyBatchGet(Long tenantId, List<String> siteIds);

    /**
     * propertyLimitSiteQuery-根据站点属性获取站点
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitSiteQuery(Long tenantId, MtModSiteVO condition);

    /**
     * siteBasicPropertyUpdate-新增更新站点及基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return
     */
    String siteBasicPropertyUpdate(Long tenantId, MtModSite dto, String fullUpdate);

    /**
     * propertyLimitSitePropertyQuery-根据属性获取站点信息
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/9/30
     */
    List<MtModSiteVO6> propertyLimitSitePropertyQuery(Long tenantId, MtModSiteVO6 dto);

    /**
     * modSiteAttrPropertyUpdate-站点新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/20
     */
    void modSiteAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
