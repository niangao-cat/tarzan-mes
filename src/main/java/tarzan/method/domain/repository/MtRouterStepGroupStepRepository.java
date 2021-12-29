package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterStepGroupStep;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO1;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO2;

/**
 * 工艺路线步骤组行步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepGroupStepRepository
                extends BaseRepository<MtRouterStepGroupStep>, AopProxy<MtRouterStepGroupStepRepository> {

    /**
     * groupStepLimitStepQuery-获取步骤组内分配的步骤清单
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    List<MtRouterStepGroupStep> groupStepLimitStepQuery(Long tenantId, String routerStepId);

    /**
     * stepLimitStepGroupGet-获取步骤所属步骤组
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterStepGroupStepVO stepLimitStepGroupGet(Long tenantId, String routerStepId);

    /**
     * routerStepGroupStepBatchGet-批量获取步骤所属步骤组
     *
     * @param tenantId
     * @param routerStepIds
     * @return
     */
    List<MtRouterStepGroupStepVO> routerStepGroupStepBatchGet(Long tenantId, List<String> routerStepIds);


    /**
     * 根据步骤查询步骤组内所有步骤
     *
     * @Author peng.yuan
     * @Date 2019/10/9 10:52
     * @param tenantId :
     * @param routerStepId :
     * @return java.util.List<java.lang.String>
     */
    List<String> routerStepLimitGroupStepQuery(Long tenantId, String routerStepId);

    /**
     * routerStepGroupStepAttrPropertyUpdate-工艺路线步骤组步骤新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/12 10:23
     * @param tenantId, vo
     * @return void
     */
    void routerStepGroupStepAttrPropertyUpdate(Long tenantId, MtRouterStepGroupStepVO1 vo);

    /**
     * groupSteplimitStepBatchQuery-批量获取步骤组内分配的步骤清单
     * @param tenantId
     * @param routerStepIds
     * @return
     */
    List<MtRouterStepGroupStepVO2> groupSteplimitStepBatchQuery(Long tenantId, List<String> routerStepIds);
}
