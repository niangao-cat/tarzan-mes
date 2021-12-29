package tarzan.dispatch.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.dispatch.domain.entity.MtDispatchStrategyOrgRel;
import tarzan.dispatch.domain.vo.*;
import java.util.List;

/**
 * 调度策略与组织关系表资源库
 *
 * @author yiyang.xie 2020-02-03 19:42:38
 */
public interface MtDispatchStrategyOrgRelRepository
                extends BaseRepository<MtDispatchStrategyOrgRel>, AopProxy<MtDispatchStrategyOrgRelRepository> {

    /**
     * dispatchStrategyOrganizationRelationUpdate-新增更新调度策略与组织关系
     *
     * @param tenantId
     * @param vo
     * @return
     */
    String dispatchStrategyOrganizationRelationUpdate(Long tenantId, MtDispatchStrategyOrgRelVO1 vo);

    /**
     * 获取特定组织结构调度策略
     * 
     * @Author peng.yuan
     * @Date 2020/2/4 9:42
     * @param tenantId :
     * @param vo :
     * @return java.util.List<tarzan.dispatch.domain.vo.MtDispatchStrategyOrgRelVO3>
     */
    List<MtDispatchStrategyOrgRelVO3> organizationLimitDispatchStrategyQuery(Long tenantId,
                                                                             MtDispatchStrategyOrgRelVO2 vo);

    /**
     * 根据属性获取调度策略组织关系基础属性
     * @Author peng.yuan
     * @Date 2020/2/4 10:20
     * @param tenantId :
     * @param vo :
     * @return java.util.List<tarzan.dispatch.domain.vo.MtDispatchStrategyOrgRelVO5>
     */
    List<MtDispatchStrategyOrgRelVO5> propertyLimitDispatchStrategyOrganizationRelationQuery(Long tenantId, MtDispatchStrategyOrgRelVO4 vo);
}
