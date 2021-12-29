package com.ruike.hme.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruike.hme.domain.vo.HmeTagVO;
import com.ruike.wms.domain.vo.WmsMaterialVO;
import tarzan.general.domain.vo.MtTagGroupObjectVO3;

/**
 * WmsMaterialMapper
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 15:58
 */
public interface HmeTagMapper {

    /**
     * 根据工艺路线获取互检数据
     * @param tenantId    租户ID
     * @param operationId 工艺路线ID
     * @return 数据采集
     */
    List<HmeTagVO> operationLimitForCompleteBoxing(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "operationId") String operationId);

    /**
     * 根据工艺路线获取互检数据和采集数据
     *
     * @param tenantId    租户ID
     * @param operationId 工艺路线ID
     * @return 数据采集
     */
    List<HmeTagVO> operationLimitForEoJobSn(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "operationId") String operationId,
                                            @Param(value = "businessTypeList") List<String> businessTypeList);


    /**
     * 根据工艺路线和物料获取互检数据和采集数据
     *
     * @param tenantId    租户ID
     * @param operationId 工艺路线ID
     * @param materialId  物料ID
     * @return 数据采集
     */
    List<HmeTagVO> operationMaterialLimitForEoJobSn(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "operationId") String operationId,
                                                    @Param(value = "materialId") String materialId);

    /**
     * 根据工艺路线和物料获取互检数据和采集数据
     *
     * @param tenantId    租户ID
     * @param operationId 工艺路线ID
     * @param materialId  物料ID
     * @param productionVersion  版本
     * @param businessTypeList  类型
     * @return 数据采集
     */
    List<HmeTagVO> operationMaterialVersionLimitForEoJobSn(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "operationId") String operationId,
                                                           @Param(value = "materialId") String materialId,
                                                           @Param(value = "productionVersion") String productionVersion,
                                                           @Param(value = "businessTypeList") List<String> businessTypeList);

    /**
     * 根据工艺路线和物料获取互检数据和采集数据
     *
     * @param tenantId    租户ID
     * @param operationId 工艺路线ID
     * @param materialId  物料ID
     * @param businessTypeList  类型
     * @param labCode  实验代码
     * @return 数据采集
     */
    List<HmeTagVO> operationMaterialVersionLimitForEoJobSn2(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "operationId") String operationId,
                                                            @Param(value = "materialId") String materialId,
                                                            @Param(value = "businessTypeList") List<String> businessTypeList,
                                                            @Param(value = "labCode") String labCode);

    /**
     * 根据工艺路线和物料获取互检数据和采集数据
     *
     * @param tenantId    租户ID
     * @param operationId 工艺路线ID
     * @param itemType  物料类
     * @param businessTypeList  类型
     * @return 数据采集
     */
    List<HmeTagVO> operationItemTypeLimitForEoJobSn(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "operationId") String operationId,
                                                    @Param(value = "itemType") String itemType,
                                                    @Param(value = "businessTypeList") List<String> businessTypeList);
}
