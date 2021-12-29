package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeVisualInspectionDTO;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO3;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO9;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * 目检完工Mapper
 *
 * @author: chaonan.hu@hand-china.com 2021-01-20 14:55:12
 **/
public interface HmeVisualInspectionMapper {
    
    /**
     * 进站条码数据查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 15:40:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeVisualInspectionVO>
     */
    List<HmeVisualInspectionVO> materialLotQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeVisualInspectionDTO dto,
                                                 @Param("jobType") String jobType);

    /**
     * 进站条码数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 15:40:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeVisualInspectionVO>
     */
    List<HmeVisualInspectionVO> materialLotQuery2(@Param("tenantId") Long tenantId, @Param("dto") HmeVisualInspectionDTO dto,
                                                 @Param("jobType") String jobType);

    /**
     * 根据条码查询不良芯片个数
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 16:43:53
     * @return java.lang.Long
     */
    Long ncCountByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据条码查询cos_num不为1的数据量
     * 
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 16:58:23
     * @return java.lang.Long
     */
    Long countByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
    
    /**
     * 根据容器Id查询汇总条码总数
     * 
     * @param tenantId 租户ID
     * @param containerId 容器ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/21 09:51:22 
     * @return java.lang.Long
     */
    Long getCountByContainerId(@Param("tenantId") Long tenantId, @Param("containerId") String containerId);

    /**
     * 工单工位工艺在制记录查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/21 10:32:20 
     * @return com.ruike.hme.domain.entity.HmeCosOperationRecord
     */
    HmeCosOperationRecord cosOperationRecordQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                                  @Param("operationId") String operationId, @Param("dto") HmeVisualInspectionVO dto);

    /**
     * 条码未录入热沉编号位置查询
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/23 02:50:46
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> noHotSinkDataQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 条码未进行性能测试位置查询
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/23 03:05:46
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> noFunctionTestDataQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * loadSequence的不良查询
     *
     * @param tenantId 租户ID
     * @param loadSequenceList loadSequence集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/26 09:07:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmeVisualInspectionVO9>
     */
    List<HmeVisualInspectionVO9> ncCodeQuery(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<String> loadSequenceList);

    /**
     * 根据多个条码ID查询是否有条码存在不良芯片
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/27 03:23:32
     * @return java.util.List<java.lang.String>
     */
    List<String> ncCountByMaterialLotIdList(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据条码ID查询扩展属性COS类型和WAFER
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/12 10:31:08
     * @return com.ruike.hme.domain.vo.HmeVisualInspectionVO3
     */
    HmeVisualInspectionVO3 cosTypeWaferAttrQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据物料批ID+扩展名查询扩展值
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param attrName 扩展名
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/19 07:11:17
     * @return java.lang.String
     */
    String getMaterialLotAttrValueByAttrName(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                                             @Param("attrName") String attrName);

    /**
     * 查询在制品标识不为Y的条码
     * 
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/31 09:54:48 
     * @return java.lang.String
     */
    String getMfIsNMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 查询条码的盘点标识，冻结标识
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/05 16:31:23
     * @return java.lang.String
     */
    List<MtMaterialLot> materialLotFreezeStocktakeFlagQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);
}
