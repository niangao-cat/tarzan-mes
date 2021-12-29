package tarzan.method.domain.repository;

import java.util.Date;
import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.vo.*;

/**
 * 装配清单行资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomComponentRepository extends BaseRepository<MtBomComponent>, AopProxy<MtBomComponentRepository> {

    /**
     * bomComponentBasicGet-获取装配清单组件行属性
     *
     * @param tenantId
     * @param bomComponentId
     * @return
     */
    MtBomComponentVO8 bomComponentBasicGet(Long tenantId, String bomComponentId);

    /**
     * bomComponentBasicBatchGet-批量获取装配清单组件行属性
     *
     * @param tenantId
     * @param bomComponentIds
     * @return
     */
    List<MtBomComponentVO13> bomComponentBasicBatchGet(Long tenantId, List<String> bomComponentIds);

    /**
     * propertyLimitBomComponentQuery-根据装配清单组件行属性获取装配清单组件行
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtBomComponentVO16> propertyLimitBomComponentQuery(Long tenantId, MtBomComponentVO condition);

    /**
     * attritionLimitComponentQtyCalculate-根据损耗率计算装配组件数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    Double attritionLimitComponentQtyCalculate(Long tenantId, MtBomComponentVO6 dto);

    /**
     * materialLimitBomQuery-查询组件物料所属装配清单
     *
     * @param tenantId
     * @param siteId
     * @param materialId
     * @param bomComponentType
     * @return
     */
    List<String> materialLimitBomQuery(Long tenantId, String siteId, String materialId, String bomComponentType);

    /**
     * bomComponentQtyCalculate-计算装配清单组件物料用量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtBomComponentVO2> bomComponentQtyCalculate(Long tenantId, MtBomComponentVO5 dto);

    /**
     * bomComponentMaterialValidate-校验物料与装配清单组件一致性
     *
     * @param tenantId
     * @param bomId
     * @param materialId
     */
    MtBomComponentVO3 bomComponentMaterialValidate(Long tenantId, String bomId, String materialId);

    /**
     * bomComponentUpdate-新增更新装配清单组件行
     *
     * update remarks
     * <ul>
     * <li>2019-09-16 benjamin 修改返回参数 增加装配清单组件历史Id</li>
     * </ul>
     *
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return 处理的组件行所属装配清单ID bomId
     */
    MtBomComponentVO15 bomComponentUpdate(Long tenantId, MtBomComponentVO10 dto, String fullUpdate);

    /**
     * 根据来源Bom的组件更新目标Bom的组件
     *
     * @author chuang.yang
     * @date 2019/4/27
     * @param tenantId
     * @param sourceBomComponents
     * @param targetBomComponents
     * @param targetBomId
     * @param now
     * @return java.util.List<java.lang.String>
     */
    List<String> sourceLimitTargetBomComponentUpdateSqlGet(Long tenantId, List<MtBomComponent> sourceBomComponents,
                    List<MtBomComponent> targetBomComponents, String targetBomId, Date now);

    /**
     * bomComponentDetailInsert-根据传入组件以及组件属性
     * 返回组件及属性（Locator、referencePoint、SubstituteGroup）的insertSqlList
     *
     * @author chuang.yang
     * @date 2019/3/16
     * @param tenantId
     * @param newBomId
     * @param dtos
     * @Param now
     * @return java.util.List<java.lang.String>
     */
    List<String> bomComponentDetailInsert(Long tenantId, String newBomId, List<MtBomComponentVO7> dtos, Date now);

    /**
     * singleBomComponentQtyCalculate-计算单个装配清单组件行用量/sen.luo 2018-04-01
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtBomComponentVO12> singleBomComponentQtyCalculate(Long tenantId, MtBomComponentVO11 dto);

    /**
     * bomComponentAttrPropertyUpdate-装配清单组件新增&更新扩展表属性
     *
     * @author chuang.yang
     * @date 2019/11/13
     * @param tenantId
     * @param dto
     * @return void
     */
    void bomComponentAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * 根据bomId批量获取Bom组件
     */
    List<MtBomComponent> selectBomComponentByBomIds(Long tenantId, List<String> bomIds);

    /**
     * bomComponentLimitOperationBatchGet-根据装配组件批量获取工艺
     *
     * @Author Xie.yiyang
     * @Date 2020/1/7 15:17
     * @param tenantId
     * @param bomComponentIds
     * @return java.util.List<tarzan.method.domain.vo.MtBomComponentVO17>
     */
    List<MtBomComponentVO17> bomComponentLimitOperationBatchGet(Long tenantId, List<String> bomComponentIds);

    /**
     * 根据bomId批量获取Bom所有组件
     */
    List<MtBomComponent> selectBomComponentByBomIdsAll(Long tenantId, List<String> bomIds);

    /**
     * 根据bomComponentIds批量获取Bom所有组件
     */
    List<MtBomComponent> selectBomComponentByBomComponentIds(Long tenantId, List<String> bomComponentIds);

    /**
     * bomComponentQtyBatchCalculate-批量计算装配清单组件物料用量
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/04/03
     */
    List<MtBomComponentVO25> bomComponentQtyBatchCalculate(Long tenantId, MtBomComponentVO22 dto);

    /**
     * attritionLimitComponentQtyBatchCalculate-批量获取组件考虑损耗率之后的单件理论用量
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/04/07
     */
    List<MtBomComponentVO28> attritionLimitComponentQtyBatchCalculate(Long tenantId, MtBomComponentVO6 dto);
}
