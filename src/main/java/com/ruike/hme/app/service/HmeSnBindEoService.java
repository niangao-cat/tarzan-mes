package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeWoReleaseVO;
import tarzan.order.domain.vo.MtEoVO6;
import tarzan.order.domain.vo.MtWorkOrderVO50;

import java.util.List;

/**
 * 基于工单生成SN绑定E
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/06 20:12
 */
public interface HmeSnBindEoService {

    /**
     *  更改EO及BOM状态为下达
     *
     * @param tenantId      租户Id
     * @param eoIdList      EoId列表
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 15:18
     * @return void
     */
    void changeEoAndBomStatus(Long tenantId, List<String> eoIdList);

    /**
     * 查询工序组件数
     *
     * @param tenantId         租户id
     * @param workOrderId      工单
     * @author sanfeng.zhang@hand-china.com 2020/8/11 20:20
     * @return java.lang.Integer
     */
    Integer queryOperationComponentCount(Long tenantId, String workOrderId);

    /**
     * 查询bom组件数
     *
     * @param tenantId         租户id
     * @param workOrderId      工单
     * @author sanfeng.zhang@hand-china.com 2020/8/11 20:21
     * @return java.lang.Integer
     */
    Integer queryBomComponentCount(Long tenantId,String workOrderId);

    /**
     * EO及BOM状态为下达 和 生成SN绑定EO
     *
     * @param tenantId
     * @param releaseVOList
     * @author sanfeng.zhang@hand-china.com 2020/9/24 10:38
     * @return void
     */
    void handleEoRelated(Long tenantId, List<HmeWoReleaseVO> releaseVOList);

    /**
     * 工单下达
     *
     * @param tenantId
     * @param mtWorkOrder
     * @author sanfeng.zhang@hand-china.com 2020/9/29 15:48
     * @return void
     */
    void handleWoAndEoRelease(Long tenantId, List<MtWorkOrderVO50> mtWorkOrder);

    /**
     * EO创建
     *
     * @param tenantId
     * @param vo
     * @author sanfeng.zhang@hand-china.com 2020/9/29 15:58
     * @return java.lang.String
     */
    String woLimitEoCreate(Long tenantId, MtEoVO6 vo);

    /**
     * UI创建EO
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2020/9/29 17:32
     * @return void
     */
    void eoCreateForUi(Long tenantId, MtWorkOrderVO50 dto);
}
