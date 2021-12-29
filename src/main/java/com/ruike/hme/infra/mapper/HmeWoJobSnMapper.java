package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeWoJobSn;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * wo工艺作业记录表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-12 10:38:22
 */
public interface HmeWoJobSnMapper extends BaseMapper<HmeWoJobSn> {


    /**
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO>
     * @description 获取工单数据
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/12 16:28
     **/
    List<HmeWoJobSnReturnDTO> workList(@Param("tenantId") Long tenantId, @Param("dto") HmeWoJobSnDTO dto);


    List<HmeWoJobSnReturnDTO4> workDetails(@Param("tenantId") Long tenantId, @Param("dto") HmeWoJobSnDTO3 dto);


    String queryMaterialType(@Param("materialId") String materialId, @Param("siteId") String siteId);


    List<String> getMaterials(@Param("operationRecordId") String operationRecordId);


    /**
     * @param tenantId
     * @param workOrderId
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO6>
     * @description 查询工单组件
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/24 9:51
     **/
    List<HmeWoJobSnReturnDTO6> component(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId);


    List<String> selectCosType(@Param("tenantId") Long tenantId,
                               @Param("siteId") String siteId,
                               @Param("itemGroup") String itemGroup,
                               @Param("bomId") String bomId);

    /**
     * 获取工单芯片需求数量
     *
     * @param tenantId
     * @param siteId
     * @param itemGroup
     * @param bomId
     * @author sanfeng.zhang@hand-china.com 2020/11/23 14:44
     * @return java.math.BigDecimal
     */
    BigDecimal queryWorkQty(@Param("tenantId") Long tenantId,
                            @Param("siteId") String siteId,
                            @Param("itemGroup") String itemGroup,
                            @Param("bomId") String bomId);


    List<String> getMaterialsByWorkOrder(@Param("workOrderId") String workOrderId);


    HmeWoJobSnReturnDTO7 selectByOperationRecordId(@Param("tenantId") Long tenantId, @Param("operationRecordId") String operationRecordId);

    /**
     * 查询工艺步骤
     *
     * @param tenantId    租户id
     * @param workOrderId 工单id
     * @param operationId 工艺id
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/12/9 19:36
     */
    List<String> queryRouterStepId(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId, @Param("operationId") String operationId);
}