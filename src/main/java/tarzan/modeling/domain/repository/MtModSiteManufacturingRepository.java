package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModSiteManufacturing;
import tarzan.modeling.domain.vo.MtModSiteManufacturingVO;

/**
 * 站点生产属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModSiteManufacturingRepository
                extends BaseRepository<MtModSiteManufacturing>, AopProxy<MtModSiteManufacturingRepository> {

    /**
     * siteManufacturingPropertyGet-获取站点生产属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param siteId
     * @return
     */
    MtModSiteManufacturing siteManufacturingPropertyGet(Long tenantId, String siteId);

    /**
     * siteManufacturingPropertyBatchGet-批量查询站点生产属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param siteId
     * @return
     */
    List<MtModSiteManufacturing> siteManufacturingPropertyBatchGet(Long tenantId, List<String> siteId);

    /**
     * siteManufacturingPropertyUpdate-新增更新站点生产属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return void
     */
    void siteManufacturingPropertyUpdate(Long tenantId, MtModSiteManufacturingVO dto, String fullUpdate);

}
