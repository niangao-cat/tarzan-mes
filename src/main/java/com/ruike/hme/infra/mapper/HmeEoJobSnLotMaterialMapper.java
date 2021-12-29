package com.ruike.hme.infra.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnDTO2;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.domain.vo.HmeEoJobSnVO9;
import com.ruike.hme.domain.vo.HmeMaterialLotVO3;
import com.ruike.hme.domain.vo.HmeModLocatorVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.modeling.domain.entity.MtModLocator;

/**
 * 工序作业平台-产品SN批次投料Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
 */
public interface HmeEoJobSnLotMaterialMapper extends BaseMapper<HmeEoJobSnLotMaterial> {
    /**
     * 查询区域库位
     *
     * @param tenantId  租户id
     * @param locatorId 货位ID
     * @return MtModLocator
     */
    MtModLocator queryAreaLocator(@Param("tenantId") Long tenantId,
                                  @Param("locatorId") String locatorId);

    /**
     * 批量查询区域库位
     *
     * @param tenantId  租户id
     * @param locatorIdList 货位ID
     * @return MtModLocator
     */
    List<HmeModLocatorVO2> batchQueryAreaLocator(@Param("tenantId") Long tenantId,
                                                 @Param("locatorIdList") List<String> locatorIdList);

    /**
     * 查询预装库位
     *
     * @param tenantId  租户id
     * @param locatorId 货位ID
     * @param workcellId 工位ID
     * @return MtModLocator
     */
    List<MtModLocator> queryPreLoadLocator(@Param("tenantId") Long tenantId,
                                           @Param("locatorId") String locatorId,
                                           @Param("workcellId") String workcellId);

    /**
     * 序列号已投料物料
     *
     * @param tenantId 租户id
     * @param dto      工序作业参数
     * @return MtModLocator
     */
    List<HmeEoJobSnVO9> selectReleaseEoJobSnLotMaterial(@Param("tenantId") Long tenantId,
                                                        @Param("dto") HmeEoJobSnVO3 dto);

    /**
     * 序列号已投料物料-返修
     *
     * @param tenantId 租户id
     * @param dto      工序作业参数
     * @return MtModLocator
     */
    List<HmeEoJobSnVO9> selectReleaseEoJobSnLotMaterialOfRework(@Param("tenantId") Long tenantId,
                                                                @Param("dto") HmeEoJobSnVO3 dto);

    /**
     * 序列号物料已投料之和
     *
     * @param tenantId 租户id
     * @param dto      传入参数
     * @return BigDecimal
     */
    BigDecimal selectReleaseQtySum(@Param("tenantId") Long tenantId,
                                   @Param("dto") HmeEoJobSnVO9 dto);

    /**
     * 序列号物料已投料之和
     *
     * @param tenantId 租户id
     * @param dto      传入参数
     * @return BigDecimal
     */
    BigDecimal selectVirtualReleaseQtySum(@Param("tenantId") Long tenantId,
                                          @Param("dto") HmeEoJobSnVO9 dto);

    /**
     * 查询批次、时效物料序列号组件
     *
     * @param tenantId     租户id
     * @param jobIdList    工序作业ID
     * @return List<HmeEoJobSnLotMaterial>
     */
    List<HmeEoJobSnLotMaterial> selectVirtualComponent(@Param("tenantId") Long tenantId,
                                                       @Param("jobIdList") List<String> jobIdList);

    /**
     * 物料投料记录
     *
     * @param tenantId     租户id
     * @param jobId        工序作业ID
     * @param materialType 物料类型
     * @return List<HmeEoJobSnLotMaterial>
     */
    List<HmeEoJobSnLotMaterial> selectHaveReleaseSnLotMaterial(@Param("tenantId") Long tenantId,
                                                               @Param("workcellId") String workcellId,
                                                               @Param("jobId") String jobId,
                                                               @Param("materialId") String materialId,
                                                               @Param("materialType") String materialType,
                                                               @Param("materialLotId") String materialLotId);

    /**
     * 查询工艺步骤
     *
     * @param tenantId    租户id
     * @param routerId    工序路线ID
     * @param operationId 工艺ID
     * @return List<HmeEoJobSnLotMaterial>
     */
    List<MtRouterStep> selectOperationStep(@Param("tenantId") Long tenantId,
                                           @Param("routerId") String routerId,
                                           @Param("operationId") String operationId);

    /**
     * 扫描查询替代料
     *
     * @param tenantId           租户id
     * @param bomComponentIdList 组件ID
     * @return List<MtBomSubstitute>
     */
    List<MtBomSubstitute> selectBomSubstitute(@Param("tenantId") Long tenantId,
                                              @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     * 出站查询替代料
     *
     * @param tenantId           租户id
     * @param materialId         物料id
     * @param bomComponentIdList 组件ID
     * @return List<MtBomSubstitute>
     */
    List<MtBomSubstitute> selectBomSubstitute2(@Param("tenantId") Long tenantId,
                                               @Param("materialId") String materialId,
                                               @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     * 查询反冲料
     *
     * @param tenantId 租户id
     * @param siteId 站点ID
     * @param bomComponentIdList 组件ID
     * @return List<MtBomComponent>
     */
    List<MtBomComponent> selectBackFlash(@Param("tenantId") Long tenantId,
                                         @Param("siteId") String siteId,
                                         @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     *
     * @Description 预装作业平台反冲料查询
     *
     * @author yuchao.wang
     * @date 2020/12/16 20:08
     * @param tenantId 租户id
     * @param siteId 站点ID
     * @param bomComponentIdList 组件ID
     * @return java.util.List<tarzan.method.domain.entity.MtBomComponent>
     *
     */
    List<MtBomComponent> selectBackFlashForPrepare(@Param("tenantId") Long tenantId,
                                                   @Param("siteId") String siteId,
                                                   @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     * 判断当前组件是否反冲料
     *
     * @param tenantId 租户id
     * @param siteId 站点ID
     * @param bomComponentId 组件ID
     * @return List<MtBomComponent>
     */
    String selectIsBackFlashMaterial(@Param("tenantId") Long tenantId,
                                     @Param("siteId") String siteId,
                                     @Param("bomComponentId") String bomComponentId);

    /**
     * 判断当前组件是否反冲料
     *
     * @param tenantId 租户id
     * @param siteId 站点ID
     * @param bomComponentIdList 组件ID
     * @return List<MtBomComponent>
     */
    List<String> selectIsBackFlashMaterial2(@Param("tenantId") Long tenantId,
                                            @Param("siteId") String siteId,
                                            @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     * 查询虚拟件物料
     *
     * @param tenantId           租户id
     * @param bomComponentIdList 组件ID
     * @return List<MtMaterial>
     */
    List<MtMaterial> selectVirtualMaterial(@Param("tenantId") Long tenantId,
                                           @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     * 查询库位
     *
     * @param tenantId   租户id
     * @param locatorType 库位类型
     * @param workcellId 工位ID
     * @return MtModLocator
     */
    List<MtModLocator> selectTypeLocator(@Param("tenantId") Long tenantId,
                                   @Param("locatorType") String locatorType,
                                   @Param("workcellId") String workcellId);

    /**
     * 查询产线的库位
     *
     * @param tenantId   租户id
     * @param locatorType 库位类型
     * @param workcellId 工位ID
     * @return MtModLocator
     */
    List<MtModLocator> selectProdLineTypeLocator(@Param("tenantId") Long tenantId,
                                           @Param("locatorType") String locatorType,
                                           @Param("workcellId") String workcellId);

    /**
     * 查询产线仓库下的的库位
     *
     * @param tenantId   租户id
     * @param locatorType 库位类型
     * @param workcellId 工位ID
     * @return MtModLocator
     */
    List<MtModLocator> selectTypeLocators(@Param("tenantId") Long tenantId,
                                          @Param("locatorType") String locatorType,
                                          @Param("workcellId") String workcellId);

    /**
     * 校验替代料是否在相同替代组下
     *
     * @param tenantId   租户id
     * @param materialIdList 物料ID
     * @return String
     */
    String selectSameSubstituteGroup(@Param("tenantId") Long tenantId,
                                     @Param("materialIdList") List<String> materialIdList);

    /**
     * 校验替代料是否在相同BOM替代组下
     *
     * @param tenantId   租户id
     * @param materialIdList 物料ID
     * @return String
     */
    String selectSameBomSubstituteGroup(@Param("tenantId") Long tenantId,
                                        @Param("materialIdList") List<String> materialIdList);

    /**
     * 已投记录
     *
     * @param tenantId   租户id
     * @param jobId      工序作业ID
     * @param workcellId 工位ID
     * @return List<HmeEoJobSnLotMaterial>
     */
    List<HmeEoJobSnLotMaterial> selectHaveReleaseSnLotMaterial2(@Param("tenantId") Long tenantId,
                                                                @Param("workcellId") String workcellId,
                                                                @Param("jobId") String jobId);

    /**
     * 已投数量之和
     *
     * @param tenantId   租户id
     * @param jobId      工序作业ID
     * @param materialIdList 物料ID
     * @return BigDecimal
     */
    BigDecimal selectHaveReleaseSnLotSum(@Param("tenantId") Long tenantId,
                                         @Param("jobId") String jobId,
                                         @Param("materialIdList") List<String> materialIdList);

    /**
     * 查询反冲物料
     *
     * @param tenantId   租户id
     * @param materialIdList 物料ID
     * @param locatorId 库位ID
     * @return BigDecimal
     */
    List<MtMaterialLot> selectBackFlushMaterialLot(@Param("tenantId") Long tenantId,
                                                   @Param("materialIdList") List<String> materialIdList,
                                                   @Param("locatorId") String locatorId);
    /**
     * 批量查询反冲物料及相关信息
     *
     * @param tenantId   租户id
     * @param materialIdList 物料ID
     * @param locatorId 库位ID
     * @return BigDecimal
     */
    List<HmeMaterialLotVO3> batchQueryBackFlushMaterialLot(@Param("tenantId") Long tenantId,
                                                           @Param("materialIdList") List<String> materialIdList,
                                                           @Param("locatorId") String locatorId);

    /**
     * 添加行级别锁
     *
     * @param materialLotIdList 条码ID
     * @return BigDecimal
     */
    List<MtMaterialLot> selectForUpdateMaterialLot(@Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 已投数量之和
     *
     * @param tenantId   租户id
     * @param materialIdList 物料ID
     * @return List<MtMaterialVO>
     */
    List<MtMaterialVO> selectMaterialProperty(@Param("tenantId") Long tenantId,
                                              @Param("materialIdList") List<String> materialIdList);

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/12/25 15:33
     * @param insertList 新增数据列表
     * @return void
     *
     */
    void myBatchInsert(@Param("insertList") List<HmeEoJobSnLotMaterial> insertList);

    /**
     * 序列号已投料物料-返修
     *
     * @param tenantId  租户id
     * @param dto       工序作业参数
     * @param jobIdList 作业id
     * @author sanfeng.zhang@hand-china.com 2021/4/12 0:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnVO9>
     */
    List<HmeEoJobSnVO9> selectReleaseEoJobSnLotMaterialOfRework2(@Param("tenantId")Long tenantId,
                                                                 @Param("dto") HmeEoJobSnVO3 dto,
                                                                 @Param("jobIdList") List<String> jobIdList);

}
