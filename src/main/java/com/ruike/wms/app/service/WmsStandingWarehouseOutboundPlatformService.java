package com.ruike.wms.app.service;

import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO;
import com.ruike.wms.api.dto.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface WmsStandingWarehouseOutboundPlatformService {
    /**
     * @description:查询头
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/6 15:36
     */
    WmsStandingWarehouseOutboundPlatformHeadDTO queryHead(Long tenantId, String instructionDocNum);

    /**
     * @description:根据单据号查询
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/6 13:53
     */
    List<WmsStandingWarehouseOutboundPlatformDTO> queryList(Long tenantId, String instructionDocId);

    /**
     * @description:SN指定
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/6 16:02
     */
    List<WmsStandingWarehouseOutboundPlatformLineDTO> snSpecified(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto);

    /**
     * @description:检验sn状态
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/6 16:30
     */
    String snEntry(Long tenantId, String instructionId,String sn);

    /**
     * @description:SN录入校验
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/6 16:43
     */
    Void snCheck(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto);

    /**
     * @description:SN批量录入
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/9 11:05
     */
    List<WmsStandingWarehouseOutboundPlatformLineDTO> snBatchEntry(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto);

    /**
     * @description:批量录入
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/9 13:02
     */
    Void snBatchSaveEntry(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto);

    /**
     * @description:SN删除
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/9 13:06
     */
    Void snDeleteEntry(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto);

    /**
     * @description:出库区域显示
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/9 14:04
     */
    List<ItfFinishDeliveryInstructionIfaceVO> snBatchOutBound(Long tenantId, List<WmsStandingWarehouseOutboundPlatformDTO> dtoList);

    /**
     * @description:出库取消
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/9 15:29
     */
    List<ItfFinishDeliveryInstructionIfaceVO> snCancel(Long tenantId, WmsStandingWarehouseOutboundPlatformReturnDTO2 dto);

    /**
     * @description:清单查询
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/19 14:52
     */
    Page<WmsStandingWarehouseOutboundPlatformReturnDTO2> figure(Long tenantId, PageRequest pageRequest);

    /**
     * @description:界面查询
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/19 19:32
     */
    WmsStandingWarehouseOutboundPlatformReturnDTO mainQuery(Long tenantId);
}
