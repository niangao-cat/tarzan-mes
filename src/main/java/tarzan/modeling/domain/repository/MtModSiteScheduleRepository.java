package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModSiteSchedule;
import tarzan.modeling.domain.vo.MtModSiteScheduleVO;

/**
 * 站点计划属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModSiteScheduleRepository
                extends BaseRepository<MtModSiteSchedule>, AopProxy<MtModSiteScheduleRepository> {

    /**
     * siteSchedulePropertyGet获取站点计划属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param siteId
     * @return
     */
    MtModSiteSchedule siteSchedulePropertyGet(Long tenantId, String siteId);

    /**
     * siteSchedulePropertyBatchGet-批量获取站点计划属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param siteIds
     * @return
     */
    List<MtModSiteSchedule> siteSchedulePropertyBatchGet(Long tenantId, List<String> siteIds);

    /**
     * siteSchedulePropertyUpdate-新增更新站点计划属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return void
     */
    void siteSchedulePropertyUpdate(Long tenantId, MtModSiteScheduleVO dto, String fullUpdate);
}
