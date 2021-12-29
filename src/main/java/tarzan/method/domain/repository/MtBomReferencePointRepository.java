package tarzan.method.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomReferencePoint;
import tarzan.method.domain.vo.MtBomReferencePointVO;
import tarzan.method.domain.vo.MtBomReferencePointVO2;
import tarzan.method.domain.vo.MtBomReferencePointVO3;

/**
 * 装配清单行参考点关系资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomReferencePointRepository
                extends BaseRepository<MtBomReferencePoint>, AopProxy<MtBomReferencePointRepository> {

    /**
     * bomReferencePointBasicGet-获取装配清单组件行的参考点属性
     *
     * @param tenantId
     * @param bomReferencePointId
     * @return
     */
    MtBomReferencePoint bomReferencePointBasicGet(Long tenantId, String bomReferencePointId);

    /**
     * bomReferencePointBasicBatchGet-批量获取装配清单组件行的参考点属性
     *
     * @param tenantId
     * @param bomReferencePointIds
     * @return
     */
    List<MtBomReferencePoint> bomReferencePointBasicBatchGet(Long tenantId, List<String> bomReferencePointIds);

    /**
     * componentLimitBomReferencePointQuery-根据组件行获取装配清单组件行参考点属性ID
     *
     * @param tenantId
     * @param bomComponentId
     * @return
     */
    List<Map<String, String>> componentLimitBomReferencePointQuery(Long tenantId, String bomComponentId);

    /**
     * propertyLimitBomReferencePointQuery-根据装配清单参考点属性获取装配清单参考点属性行
     *
     * @param tenantId
     * @param condition
     */
    List<MtBomReferencePointVO2> propertyLimitBomReferencePointQuery(Long tenantId, MtBomReferencePointVO condition);

    /**
     * bomComponentReferencePointQuery-获取装配清单组件行参考点
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtBomReferencePoint> bomComponentReferencePointQuery(Long tenantId, MtBomReferencePointVO3 dto);

    /**
     * bomReferencePointUpdate-新增更新装配清单组件行参考
     *
     * @param tenantId
     * @param dto
     * @return 处理的参考点属性所属装配清单ID
     */
    String bomReferencePointUpdate(Long tenantId, MtBomReferencePoint dto);

    /**
     * 根据来源Bom组件ReferencePoint更新目标Bom组件ReferencePoint
     *
     * @author chuang.yang
     * @date 2019/4/27
     * @param tenantId
     * @param sourceBomComponentId
     * @param targetBomComponentId
     * @return java.util.List<java.lang.String>
     */
    List<String> sourceLimitTargetBomReferencePointUpdateGet(Long tenantId, String sourceBomComponentId,
                    String targetBomComponentId, Date now);

}
