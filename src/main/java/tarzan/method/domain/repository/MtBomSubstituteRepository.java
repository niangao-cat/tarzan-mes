package tarzan.method.domain.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.vo.*;

/**
 * 装配清单行替代项资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteRepository
                extends BaseRepository<MtBomSubstitute>, AopProxy<MtBomSubstituteRepository> {

    /**
     * bomSubstituteBasicGet-获取装配清单组件行替代项属性
     *
     * @param tenantId
     * @param bomSubstituteId
     * @return
     */
    MtBomSubstitute bomSubstituteBasicGet(Long tenantId, String bomSubstituteId);

    /**
     * bomSubstituteBasicBatchGet-批量获取装配清单组件行替代项属性
     *
     * @param tenantId
     * @param bomSubstituteIds
     * @return
     */
    List<MtBomSubstitute> bomSubstituteBasicBatchGet(Long tenantId, List<String> bomSubstituteIds);

    /**
     * groupLimitBomSubstituteQuery根据替代组获取装配清单组件行替代项属性ID
     *
     * @param tenantId
     * @param bomSubstituteGroupId
     * @return
     */
    List<Map<String, String>> groupLimitBomSubstituteQuery(Long tenantId, String bomSubstituteGroupId);

    /**
     * propertyLimitBomSubstituteQuery根据装配清单替代物料获取装配清单替代项属性行
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtBomSubstituteVO2> propertyLimitBomSubstituteQuery(Long tenantId, MtBomSubstituteVO condition);

    /**
     * bomSubstituteQtyCalculate计算装配组件替代物料及数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtBomSubstituteVO3> bomSubstituteQtyCalculate(Long tenantId, MtBomSubstituteVO6 dto);

    List<MtBomSubstituteVO3> bomSubstituteQtyCalculate(Long tenantId, MtBomSubstituteVO6 dto,
                    MtBomComponent mtBomComponent, MtBomSubstituteGroupVO6 mtBomSubstituteGroupVO6);

    /**
     * bomComponentSubstituteQuery-获取装配清单行有效
     *
     * @param tenantId
     * @param bomComponentId
     * @return
     */
    List<MtBomSubstituteVO4> bomComponentSubstituteQuery(Long tenantId, String bomComponentId);

    /**
     * bomComponentSubstituteVerify-校验物料是否为装配清单组件行的替代物料
     *
     * @param tenantId
     * @param dto
     * @return
     */
    void bomComponentSubstituteVerify(Long tenantId, MtBomSubstituteVO5 dto);

    /**
     * bomSubstituteUpdate-新增更新装配清单组件行替代项
     *
     * @param tenantId
     * @param dto
     * @return 处理的装配清单行替代项所属装配清单ID
     */
    MtBomSubstituteVO10 bomSubstituteUpdate(Long tenantId, MtBomSubstitute dto, String fullUpdate);

    /**
     * 根据来源Bom组件Substitute更新目标Bom组件Substitute
     *
     * @author chuang.yang
     * @date 2019/4/27
     * @param tenantId
     * @param sourceBomSubstituteGroupId
     * @param targetBomSubstituteGroupId
     * @param now
     * @return java.util.List<java.lang.String>
     */
    List<String> sourceLimitTargetBomSubstituteUpdateGet(Long tenantId, String sourceBomSubstituteGroupId,
                    String targetBomSubstituteGroupId, Date now);

    /**
     * bomSubstituteBatchGetByBomCom-根据bom组件获取替代项
     *
     * @param tenantId
     * @param bomComponentIds
     * @author guichuan.li
     * @date 2019/1/6
     */
    List<MtBomSubstituteVO5> bomSubstituteBatchGetByBomCom(Long tenantId, List<String> bomComponentIds);

    /**
     * bomSubstituteLimitMaterialQtyBatchCalculate-计算装配组件替代物料及数量
     *
     * @Author Xie.yiyang
     * @Date 2020/1/6 17:45
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.method.domain.vo.MtBomSubstituteVO12>
     */
    List<MtBomSubstituteVO12> bomSubstituteLimitMaterialQtyBatchCalculate(Long tenantId, List<MtBomSubstituteVO11> dto);


    /**
     * bomSubstituteQtyBatchCalculate-批量计算装配组件替代物料及数量
     *
     * @param tenantId
     * @param dto
     * @return
     * @author guichuan.li
     * @date 2019/04/07
     */
    List<MtBomSubstituteVO17> bomSubstituteQtyBatchCalculate(Long tenantId, MtBomSubstituteVO15 dto);

}
