package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeBomComponentTrxVO;
import com.ruike.hme.domain.vo.HmeRouterOperationVO;
import com.ruike.hme.domain.vo.HmeWorkOrderVO58;
import com.ruike.hme.domain.vo.HmeWorkOrderVO61;
import org.apache.ibatis.annotations.Param;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.vo.MtRouterOperationVO2;
import tarzan.method.domain.vo.MtRouterStepVO4;

import java.util.List;


/**
 * 工单管理Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
 */
public interface HmeWorkOrderManagementMapper {


    /**
     * 查询工单信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeWorkOrderVO58> woListQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeWorkOrderVO58 dto);

    /**
     * 获取当前用户对应站点id
     *
     * @param userId
     * @return
     */
    String getSiteIdByUserId(@Param("userId") Long userId);

    /**
     * 获取分配产线LOV数据
     *
     * @param tenantId
     * @param dto
     * @param siteId
     * @return
     */
    List<HmeWorkOrderVO61> prodLineLovQuery(@Param("tenantId") Long tenantId,
                                            @Param("dto") HmeWorkOrderVO61 dto, @Param("siteId") String siteId);

    /**
     * 装配清单行参考点关系
     *
     * @param tenantId
     * @param bomCompIds
     * @return java.util.List<tarzan.method.domain.entity.MtBomReferencePoint>
     * @author jiangling.zheng@hand-china.com 2020/10/15 10:55
     */

    List<MtBomReferencePoint> selectBomRefPoint(@Param("tenantId") Long tenantId,
                                                @Param("bomCompIds") List<String> bomCompIds);

    /**
     * 装配清单行替代组
     *
     * @param tenantId
     * @param bomCompIds
     * @return java.util.List<tarzan.method.domain.entity.MtBomSubstituteGroup>
     * @author jiangling.zheng@hand-china.com 2020/10/15 10:55
     */
    List<MtBomSubstituteGroup> selectBomSubstituteGroup(@Param("tenantId") Long tenantId,
                                                        @Param("bomCompIds") List<String> bomCompIds);

    /**
     * 装配清单行替代项
     *
     * @param tenantId
     * @param groupIds
     * @return java.util.List<tarzan.method.domain.entity.MtBomSubstitute>
     * @author jiangling.zheng@hand-china.com 2020/10/15 10:32
     */
    List<MtBomSubstitute> selectBomSubstitute(@Param("tenantId") Long tenantId,
                                              @Param("groupIds") List<String> groupIds);

    /**
     * 工艺路线步骤组
     *
     * @param tenantId
     * @param routerStepIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterStepGroup>
     * @author jiangling.zheng@hand-china.com 2020/10/15 11:13
     */
    List<MtRouterStepGroup> selectRouterStepGroup(@Param("tenantId") Long tenantId,
                                                  @Param("routerStepIds") List<String> routerStepIds);


    /**
     * 工艺路线步骤对应工序
     *
     * @param tenantId
     * @param routerStepIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterOperation>
     * @author jiangling.zheng@hand-china.com 2020/10/15 11:20
     */
    List<MtRouterOperation> selectRouterOperation(@Param("tenantId") Long tenantId,
                                                  @Param("routerStepIds") List<String> routerStepIds);

    /**
     * 工艺路线步骤对应工序组件
     *
     * @param tenantId
     * @param routerOperationIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterOperationComponent>
     * @author jiangling.zheng@hand-china.com 2020/10/15 12:42
     */
    List<MtRouterOperationComponent> selectRouterOperaComp(@Param("tenantId") Long tenantId,
                                                           @Param("routerOperationIds") List<String> routerOperationIds);

    /**
     * 工艺路线步骤对应工序组件语言表
     *
     * @param tenantId
     * @param routerOperationIds
     * @return java.util.List<tarzan.method.domain.vo.MtRouterOperationVO2>
     * @author jiangling.zheng@hand-china.com 2020/10/15 15:03
     */
    List<MtRouterOperationVO2> selectRouterOperationTL(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "routerOperationIds") List<String> routerOperationIds);

    /**
     * 嵌套工艺路线步骤
     *
     * @param tenantId
     * @param routerStepIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterLink>
     * @author jiangling.zheng@hand-china.com 2020/10/15 14:01
     */
    List<MtRouterLink> selectRouterLink(@Param("tenantId") Long tenantId,
                                        @Param("routerStepIds") List<String> routerStepIds);

    /**
     * 返回步骤
     *
     * @param tenantId
     * @param routerStepIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterReturnStep>
     * @author jiangling.zheng@hand-china.com 2020/10/15 14:20
     */
    List<MtRouterReturnStep> selectRouterReturnStep(@Param("tenantId") Long tenantId,
                                                    @Param("routerStepIds") List<String> routerStepIds);

    /**
     * 完成步骤
     *
     * @param tenantId
     * @param routerStepIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterDoneStep>
     * @author jiangling.zheng@hand-china.com 2020/10/15 14:31
     */
    List<MtRouterDoneStep> selectRouterDoneStep(@Param("tenantId") Long tenantId,
                                                @Param("routerStepIds") List<String> routerStepIds);

    /**
     * 工艺路线子步骤
     *
     * @param tenantId
     * @param routerStepIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterSubstep>
     * @author jiangling.zheng@hand-china.com 2020/10/15 14:38
     */
    List<MtRouterSubstep> selectRouterSubStep(@Param("tenantId") Long tenantId,
                                              @Param("routerStepIds") List<String> routerStepIds);

    /**
     * 子步骤组件
     *
     * @param tenantId
     * @param routerSubstepIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterSubstepComponent>
     * @author jiangling.zheng@hand-china.com 2020/10/15 14:47
     */
    List<MtRouterSubstepComponent> selectRouterSubstepComp(@Param("tenantId") Long tenantId,
                                                           @Param("routerSubstepIds") List<String> routerSubstepIds);

    /**
     * 语言表
     *
     * @param tenantId
     * @param routerStepIds
     * @return java.util.List<tarzan.method.domain.vo.MtRouterStepVO4>
     * @author jiangling.zheng@hand-china.com 2020/10/15 15:31
     */
    List<MtRouterStepVO4> selectRouterStepTL(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "routerStepIds") List<String> routerStepIds);

    /**
     * 下一步骤
     *
     * @param tenantId
     * @param routerStepIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterNextStep>
     * @author jiangling.zheng@hand-china.com 2020/10/15 15:41
     */

    List<MtRouterNextStep> selectRouterNextStep(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "routerStepIds") List<String> routerStepIds);

    /**
     * 工艺路线步骤组行步骤
     *
     * @param tenantId
     * @param routerStepGroupIds
     * @return java.util.List<tarzan.method.domain.entity.MtRouterNextStep>
     * @author jiangling.zheng@hand-china.com 2020/10/15 16:01
     */
    List<MtRouterStepGroupStep> selectRouterStepGroupStep(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "routerStepGroupIds") List<String> routerStepGroupIds);

    /**
     * 装配清单站点分配
     *
     * @param tenantId
     * @param bomIds
     * @return java.util.List<tarzan.method.domain.entity.MtBomSiteAssign>
     * @author jiangling.zheng@hand-china.com 2020/10/19 9:57
     */
    List<MtBomSiteAssign> selectBomSiteIds(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "bomIds") List<String> bomIds);

    /**
     * 查询bom物料下物料的列表
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @param materialId  物料
     * @return java.util.List<tarzan.method.domain.entity.MtBomComponent>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 11:29:58
     */
    List<MtBomComponent> selectBomMaterialList(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "workOrderId") String workOrderId,
                                               @Param(value = "materialId") String materialId);


    /**
     * 查询工单下的对应物料的bom行信息
     *
     * @param tenantId       租户
     * @param workOrderId    工单
     * @param materialIdList 物料列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeBomComponentTrxVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 04:47:08
     */
    List<HmeBomComponentTrxVO> selectBomMaterialTrxList(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "workOrderId") String workOrderId,
                                                        @Param(value = "materialIdList") List<String> materialIdList);

    /**
     * 根据工单查询工序名称
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @return MtRouterStep
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/5 02:35:26
     */
    HmeRouterOperationVO selectRouterStepByWo(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "workOrderId") String workOrderId);

    /**
     *
     * 查询工单下snNum 是否存在
     *
     * @param tenantId
     * @param workOrderNum
     * @return
     */
    String selectHmeRepairWoSnRelByWorkOrderNum(@Param(value = "tenantId") Long tenantId, @Param(value = "workOrderNum") String workOrderNum);
}
