package tarzan.method.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomSubstituteGroup;
import tarzan.method.domain.vo.MtBomSubstituteGroupVO;
import tarzan.method.domain.vo.MtBomSubstituteGroupVO2;
import tarzan.method.domain.vo.MtBomSubstituteGroupVO3;
import tarzan.method.domain.vo.MtBomSubstituteGroupVO7;

/**
 * 装配清单行替代组资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteGroupRepository
                extends BaseRepository<MtBomSubstituteGroup>, AopProxy<MtBomSubstituteGroupRepository> {

    /**
     * bomSubstituteGroupBasicGet-获取装配清单组件行的替代组属性
     *
     * @param tenantId
     * @param bomSubstituteGroupId
     * @return
     */
    MtBomSubstituteGroup bomSubstituteGroupBasicGet(Long tenantId, String bomSubstituteGroupId);

    /**
     * bomSubstituteGroupBasicBatchGet-批量获取装配清单组件行的替代组属性
     *
     * @param tenantId
     * @param bomSubstituteGroupIds
     * @return
     */
    List<MtBomSubstituteGroup> bomSubstituteGroupBasicBatchGet(Long tenantId, List<String> bomSubstituteGroupIds);

    /**
     * bomSubstituteQuery-获取装配清单组件行有效替代项
     *
     * @param tenantId
     * @param bomComponentId
     * @return
     */
    List<MtBomSubstituteGroupVO3> bomSubstituteQuery(Long tenantId, String bomComponentId);

    /**
     * componentLimitBomSubstituteGroupQuery 根据组件行获取装配清单组件行替代组属性ID
     *
     * @param tenantId
     * @param bomComponentId
     * @return
     */
    List<Map<String, String>> componentLimitBomSubstituteGroupQuery(Long tenantId, String bomComponentId);

    /**
     * propertyLimitBomSubstituteGroupQuery-根据装配清单替代组属性获取装配清单替代组属性行
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtBomSubstituteGroupVO2> propertyLimitBomSubstituteGroupQuery(Long tenantId, MtBomSubstituteGroupVO condition);

    /**
     * bomSubstituteGroupUpdate-新增更新装配清单组件行替
     *
     * @param tenantId
     * @param dto
     * @return 处理的装配清单行替代组所属装配清单ID
     */
    String bomSubstituteGroupUpdate(Long tenantId, MtBomSubstituteGroup dto);

    /**
     * 根据来源Bom组件SubstituteGroup更新目标Bom组件SubstituteGroup
     *
     * @author chuang.yang
     * @date 2019/4/27
     * @param tenantId
     * @param sourceBomComponentId
     * @param targetBomComponentId
     * @param now
     * @return java.util.List<java.lang.String>
     */
    List<String> sourceLimitTargetBomSubstituteGroupUpdateGet(Long tenantId, String sourceBomComponentId,
                    String targetBomComponentId, Date now);


    /**
     * bomSubstituteBatchQuery-批量获取装配清单组件行有效替代项
     *
     * @param tenantId
     * @param bomComponentIds
     * @return
     * @author guichuan.li
     * @date 2020/04/07
     */
    List<MtBomSubstituteGroupVO7> bomSubstituteBatchQuery(Long tenantId, List<String> bomComponentIds);

}
