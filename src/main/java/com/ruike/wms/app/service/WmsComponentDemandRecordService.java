package com.ruike.wms.app.service;

import com.ruike.hme.domain.entity.HmeWoDispatchRecode;
import com.ruike.wms.domain.vo.WmsComponentDemandSumVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 组件需求记录表应用服务
 *
 * @author yonghui.zhu@hand-china.com 2020-08-24 14:00:05
 */
public interface WmsComponentDemandRecordService {
    /**
     * 新增组件需求
     *
     * @param tenantId       租户
     * @param woDispatchId   派工ID
     * @param shiftCode      班次编码
     * @param newDispatchQty 新派工数量
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/28 02:37:12
     */
    void insertDemandRecord(Long tenantId, String shiftCode, String woDispatchId, BigDecimal newDispatchQty);

    /**
     * 更新组件需求
     *
     * @param tenantId       租户
     * @param shiftCode      班次编码
     * @param woDispatchId   派工ID
     * @param newDispatchQty 新派工数量
     * @param oldDispatchQty 原始派工数量
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/28 02:37:12
     */
    void updateDemandRecord(Long tenantId, String shiftCode, String woDispatchId, BigDecimal newDispatchQty, BigDecimal oldDispatchQty);

    /**
     * 创建配送需求
     *
     * @param tenantId 租户
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/2 03:20:26
     */
    void createDistributionDemand(Long tenantId);

    /**
     * 分页查询需求
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @param startDate   开始日期
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsComponentDemandSumVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/19 04:52:17
     */
    Page<WmsComponentDemandSumVO> pagedRequirement(Long tenantId, String workOrderId, Date startDate, PageRequest pageRequest);
}
