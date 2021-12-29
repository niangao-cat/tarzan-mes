package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.wms.api.dto.WmsStocktakeRangeQueryDTO;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import feign.Param;
import io.choerodon.mybatis.common.BaseMapper;
import org.springframework.web.bind.annotation.PostMapping;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.method.domain.entity.MtBom;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.math.BigDecimal;
import java.util.List;

/**
 * 在制盘点单Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
public interface HmeWipStocktakeDocMapper extends BaseMapper<HmeWipStocktakeDoc> {

    /**
     * 查询用户默认事业部
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 15:02:15
     * @return java.lang.String
     */
    String getDepartmentId(@Param("tenantId") Long tenantId, @Param("userId") Long userId);

    /**
     * 部门查询
     * 
     * @param tenantId 租户ID
     * @param areaIdList areaId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 15:33:25
     * @return java.util.List<tarzan.modeling.domain.entity.MtModArea>
     */
    List<HmeWipStocktakeDocDTO2> departmentListQuery(@Param("tenantId") Long tenantId, @Param("areaIdList") List<String> areaIdList,
                                                     @Param("dto") HmeWipStocktakeDocDTO2 dto);

    /**
     * 产线查询
     *
     * @param tenantId 租户ID
     * @param prodLineIdList 产线ID集合
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 03:53:32
     * @return java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    List<HmeWipStocktakeDocDTO3> prodLineListQuery(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList,
                                                   @Param("dto") HmeWipStocktakeDocDTO3 dto);

    /**
     * 工序查询
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工位ID集合
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 04:01:37
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<HmeWipStocktakeDocDTO4> workcellListQuery(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList,
                                                   @Param("dto") HmeWipStocktakeDocDTO4 dto);

    /**
     * 在制品盘点单分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 05:21:45
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO>
     */
    List<HmeWipStocktakeDocVO> wipStocktakeDocPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDocDTO dto);

    /**
     * 在制品盘点单盘点范围分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 09:56:48
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     */
    List<HmeWipStocktakeDocVO4> stocktakeRangePageQuery(@Param("tenantId") Long tenantId, @Param("dto") WmsStocktakeRangeQueryDTO dto);

    /**
     * 在制盘点明细分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 03:02:35
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO2>
     */
    List<HmeWipStocktakeDocVO2> wipStocktakeDetailPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDocDTO5 dto);

    /**
     * 不是COS物料组时，查询BOM数据
     *
     * @param tenantId 租户ID
     * @param identification 流水号
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 03:31:16
     * @return tarzan.method.domain.entity.MtBom
     */
    MtBom noCosItemGroupQuery(@Param("tenantId") Long tenantId, @Param("identification") String identification);

    /**
     * 是COS物料组时，查询BOM数据
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 03:31:16
     * @return tarzan.method.domain.entity.MtBom
     */
    MtBom cosItemGroupQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 在制盘点汇总的物料、产线、工序确定
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 07:11:32
     * @return com.ruike.hme.domain.vo.HmeWipStocktakeDocVO3
     */
    List<HmeWipStocktakeDocVO3> wipStocktakeSumPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDocDTO7 dto);

    /**
     * 账面数量查询
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param prodLineId 账面产线ID
     * @param workcellId 账面工序ID
     * @param stocktakeIdList 盘点单ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 07:28:09
     * @return java.math.BigDecimal
     */
    BigDecimal currentQuantityQuery(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                    @Param("prodLineId") String prodLineId, @Param("workcellId") String workcellId,
                                    @Param("stocktakeIdList") List<String> stocktakeIdList);

    /**
     * 初盘数量查询
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param prodLineId 初盘产线ID
     * @param workcellId 初盘工序ID
     * @param stocktakeIdList 盘点单ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 07:28:09
     * @return java.math.BigDecimal
     */
    BigDecimal firstcountQuantityQuery(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                    @Param("prodLineId") String prodLineId, @Param("workcellId") String workcellId,
                                    @Param("stocktakeIdList") List<String> stocktakeIdList);

    /**
     * 复盘数量查询
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param prodLineId 复盘产线ID
     * @param workcellId 复盘工序ID
     * @param stocktakeIdList 盘点单ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 07:28:09
     * @return java.math.BigDecimal
     */
    BigDecimal recountQuantityQuery(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                       @Param("prodLineId") String prodLineId, @Param("workcellId") String workcellId,
                                       @Param("stocktakeIdList") List<String> stocktakeIdList);

    /**
     * 根据物料组查询组下物料
     *
     * @param tenantId 租户ID
     * @param itemGroupId 物料组ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 02:14:44
     * @return tarzan.material.domain.entity.MtMaterial
     */
    List<MtMaterial> getMaterialByMaterialGroup(@Param("tenantId") Long tenantId, @Param("itemGroupId")  String itemGroupId);

    /**
     * 根据车间ID查询其下所有产线
     *
     * @param tenantId 租户ID
     * @param areaId 车间ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 04:38:24
     * @return java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    List<MtModProductionLine> getProdLineByAreaId(@Param("tenantId") Long tenantId, @Param("areaId")  String areaId);

    /**
     * 根据产线查询其下所有工序
     *
     * @param tenantId 租户ID
     * @param prodLineId 产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 05:06:10
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> getWorkcellByProdLineId(@Param("tenantId") Long tenantId, @Param("prodLineId") String prodLineId);

    /**
     * 根据事业部查询其下所有产线
     *
     * @param tenantId 租户ID
     * @param departmentId 事业部ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 05:43:16
     * @return java.util.List<java.lang.String>
     */
    List<String> getProdLineByDepartment(@Param("tenantId") Long tenantId, @Param("departmentId") String departmentId);

    /**
     * 根据产线集合查询其下所有的工序
     *
     * @param tenantId 租户ID
     * @param prodLineList 产线ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 05:56:08
     * @return java.util.List<java.lang.String>
     */
    List<String> getWorkcellByProdLineList(@Param("tenantId") Long tenantId, @Param("prodLineList") List<String> prodLineList);

    /**
     * 查询工序是否存在状态为NEW/RELEASED的盘点单
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工序ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 06:15:34
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO8
     */
    HmeWipStocktakeDocDTO8 errorMeageQuery(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 查询工序存在状态为NEW/RELEASED的盘点单ID集合
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工序ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/11 04:07:57
     * @return java.util.List<java.lang.String>
     */
    List<String> stocktakeIdWorkcellQuery(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 查询物料存在状态为NEW/RELEASED的盘点单ID集合
     *
     * @param tenantId 租户ID
     * @param materialIdList 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/11 04:07:57
     * @return java.util.List<java.lang.String>
     */
    List<String> stocktakeIdMaterialQuery(@Param("tenantId") Long tenantId, @Param("materialIdList") List<String> materialIdList);

    /**
     * 查询事业部下所有的物料
     *
     * @param tenantId 租户ID
     * @param siteId 工厂ID
     * @param departmentId 事业部ID
     * @param itemGroupList COS物料组集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 06:28:14
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialIdByDepartment(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                           @Param("departmentId") String departmentId, @Param("itemGroupList") List<String> itemGroupList);

    /**
     * 删除盘点范围数据
     *
     * @param tenantId 租户ID
     * @param stocktakeRangeIdList 盘点范围ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/7 06:16:14
     * @return void
     */
    void deleteStocktakeRange(@Param("tenantId") Long tenantId, @Param("stocktakeRangeIdList") List<String> stocktakeRangeIdList);

    /**
     * 根据盘点单ID、产线ID查询工序盘点范围
     *
     * @param tenantId 租户ID
     * @param stocktakeId 盘点单ID
     * @param prodLineId 产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 02:01:14
     * @return java.util.List<java.lang.String>
     */
    List<String> getStocktakeRangeByProdLine(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId,
                                             @Param("prodLineId") String prodLineId);
    /**
     * 查询盘点单下产品范围中的产线ID集合
     *
     * @param tenantId 租户ID
     * @param stocktakeId 盘点单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 02:41:42
     * @return java.util.List<java.lang.String>
     */
    List<String> plStocktakeRangeQuery(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId);

    /**
     * 获取盘点范围内的条码
     * 
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @param itemGroupList 物料组集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 04:13:14 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO5>
     */
    List<HmeWipStocktakeDocVO5> getStocktakeMaterialLot(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDoc dto,
                                                        @Param("itemGroupList") List<String> itemGroupList);

    /**
     * COS盘点单获取盘点范围内的条码
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @param itemGroupList 物料组集合
     * @param materialFlag 物料标识
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/8 09:56:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO5>
     */
    List<HmeWipStocktakeDocVO5> getStocktakeMaterialLotCos(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDoc dto,
                                                        @Param("itemGroupList") List<String> itemGroupList, @Param("materialFlag") String materialFlag);

    /**
     * 非COS盘点单获取盘点范围内的条码
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @param itemGroupList 物料组集合
     * @param materialFlag 物料标识
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/8 09:56:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO5>
     */
    List<HmeWipStocktakeDocVO5> getStocktakeMaterialLotNoCos(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDoc dto,
                                                           @Param("itemGroupList") List<String> itemGroupList, @Param("materialFlag") String materialFlag);

    /**
     * 查询盘点单是否有初盘漏盘项
     *
     * @param tenantId 租户ID
     * @param stocktakeId 盘点单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 05:54:23
     * @return java.util.List<java.lang.String>
     */
    List<String> getFirstcountQuantityNullData(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId);

    /**
     * 查询盘点实绩内的条码
     *
     * @param tenantId 租户ID
     * @param stocktakeId 盘点单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 06:12:43
     * @return java.util.List<java.lang.String>
     */
    List<String> getActualMaterialLot(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId);

    /**
     * 批量新增盘点实绩
     *
     * @param domains
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/9 04:39:29
     * @return void
     */
    void batchInsertActual(@org.apache.ibatis.annotations.Param("domains") List<HmeWipStocktakeActual> domains);

    /**
     * 批量新增盘点实绩历史
     *
     * @param domains
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/9 04:39:29
     * @return void
     */
    void batchInsertActualHis(@org.apache.ibatis.annotations.Param("domains") List<HmeWipStocktakeActualHis> domains);

    /**
     * 根据多个产线ID集合查询其下工序
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/9 07:51:27
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> processQueryByProdLineIdList(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeWipStocktakeDocDTO13 dto);

    /**
     * 盘点投料明细汇总
     *
     * @param tenantId 租户ID
     * @param stocktakeIdList 盘点单ID集合
     * @param itemGroupList COS物料组集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/10 09:36:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7>
     */
    List<HmeWipStocktakeDocVO7> releaseDetailPageQuery(@Param("tenantId") Long tenantId,
                                                       @Param("stocktakeIdList") List<String> stocktakeIdList,
                                                       @Param("itemGroupList") List<String> itemGroupList,
                                                       @Param("dto") HmeWipStocktakeDocDTO15 dto);

    /**
     * 非COS盘点单投料明细汇总
     *
     * @param tenantId 租户ID
     * @param stocktakeIdList 盘点单ID集合
     * @param itemGroupList COS物料组集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/10 09:36:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7>
     */
    List<HmeWipStocktakeDocVO7> releaseDetailPageQueryNoCos(@Param("tenantId") Long tenantId,
                                                       @Param("stocktakeIdList") List<String> stocktakeIdList,
                                                       @Param("itemGroupList") List<String> itemGroupList,
                                                       @Param("dto") HmeWipStocktakeDocDTO15 dto);

    /**
     * 投料明细汇总-在制数量查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/10 10:37:44
     * @return java.math.BigDecimal
     */
    BigDecimal currentQtyQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDocVO7 dto);

    /**
     * 根据多个产线ID查询产线信息
     *
     * @param tenantId
     * @param dto
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/10 03:39:04
     * @return java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    List<MtModProductionLine> prodLinePageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDocDTO14 dto);

    /**
     * 根据盘点单查询盘点导出数据
     *
     * @param tenantId          租户
     * @param stocktakeNum      盘点单
     * @author sanfeng.zhang@hand-china.com 2021/3/16 15:22
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO8>
     */
    List<HmeWipStocktakeDocVO8> cosInventoryExport(@Param("tenantId") Long tenantId, @Param("stocktakeNum") String stocktakeNum);

    /**
     * 查询产线是否存在状态为NEW/RELEASED的盘点单ID集合
     *
     * @param tenantId 租户ID
     * @param prodLineIdList 产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/7 03:34:14
     * @return java.lang.Long
     */
    Long stocktakeCountByProdLineQuery(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList);

    /**
     * 查询工序是否在其他全盘的状态为NEW/RELEASED盘点单中存在
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工序ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/7 03:53:20
     * @return java.lang.String
     */
    String stocktakeNumByWorkcellQuery(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 根据盘点单ID查询盘点范围中的范围对象ID
     *
     * @param tenantId
     * @param stocktakeId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/7 06:12:49
     * @return java.util.List<java.lang.String>
     */
    List<String> rangeObjectIdByStocktake(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId,
                                          @Param("rangeObjectType") String rangeObjectType);

    /**
     * 在制盘点明细导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/8 02:21:33
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO9>
     */
    List<HmeWipStocktakeDocVO9> wipStocktakeDetailExport(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDocDTO5 dto);

    /**
     * 在制盘点汇总导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/8 02:45:43
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO3>
     */
    List<HmeWipStocktakeDocVO10> wipStocktakeSumExport(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDocDTO7 dto);

    /**
     * 在制盘汇总点投料
     *
     * @param tenantId 租户ID
     * @param stocktakeIdList 盘点单ID集合
     * @param itemGroupList COS物料组集合
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 11:08:17
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO11>
     */
    List<HmeWipStocktakeDocVO11> releaseDetailExport(@Param("tenantId") Long tenantId,
                                                            @Param("stocktakeIdList") List<String> stocktakeIdList,
                                                            @Param("itemGroupList") List<String> itemGroupList,
                                                            @Param("dto") HmeWipStocktakeDocDTO15 dto);

    /**
     * 投料明细汇总导出-在制数量查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 11:13:21
     * @return java.math.BigDecimal
     */
    BigDecimal currentQtyExportQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeWipStocktakeDocVO11 dto);

    /**
     * 根据工单查询BOM版本号
     *
     * @param tenantId 租户ID
     * @param workOrderId WOID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/13 11:31:55
     * @return com.ruike.hme.domain.entity.HmeProductionVersion
     */
    HmeProductionVersion getProductionVersionByWo(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId );

    /**
     * 批量根据当前eo查询顶层eo
     *
     * @param tenantId 租户ID
     * @param eoIdList eoId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/23 09:40:47
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoRel>
     */
    List<HmeEoRel> eoRelQueryByEoId(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     * 根据EoId查询对应的物料批Id
     *
     * @param tenantId 租户ID
     * @param eoIdList eoId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/23 10:13:08
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO12>
     */
    List<HmeWipStocktakeDocVO12> getMaterialLotIdByEoId(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);
}
