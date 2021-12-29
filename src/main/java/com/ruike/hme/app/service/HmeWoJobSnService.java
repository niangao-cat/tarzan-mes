package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeMaterialLotNcLoad;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.domain.entity.MtMaterialLot;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * wo工艺作业记录表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-08-12 10:38:22
 */
public interface HmeWoJobSnService {

    /**
     *@description 获取工单数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/12 16:27
     *@return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO>
     *@param tenantId
     * @param dto  */
    Page<HmeWoJobSnReturnDTO> workList(Long tenantId, HmeWoJobSnDTO dto, PageRequest pageRequest);

    /**
     *@description 获取数量
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/13 9:53
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO2
     **/
    HmeWoJobSnReturnDTO2 cosNum(Long tenantId, HmeWoJobSnDTO2 dto);

    /**
     *@description 新增来料记录表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/13 9:53
     *@return com.ruike.hme.domain.entity.HmeIncomingRecord
     *
     * @param tenantId
     * @param dto*/
    HmeCosOperationRecord addIncoming(Long tenantId, HmeCosOperationRecordDTO dto);

    /**
     *@description 点击工单获取信息
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/13 10:41
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3
     **/
    HmeWoJobSnReturnDTO3 workDetails(Long tenantId, HmeWoJobSnDTO3 dto);

    /**
     *@description 扫描物料批
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/13 20:28
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3
     **/
    HmeWoJobSnReturnDTO3 scanMaterialLot(Long tenantId, HmeWoJobSnDTO3 dto);

    /**
     *@description 出站
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/13 20:27
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3
     **/
    HmeWoJobSnReturnDTO3 siteOut(Long tenantId, HmeWojobSnDTO4 dto);

    /**
     *@description 不良确认
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/24 9:49
     *@param tenantId
     *@param dto
     *@return void
     **/
    void ncLoad(Long tenantId, List<HmeMaterialLotNcLoad> dto);

    /**
     *@description 获取其剩余数量
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/24 9:48
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO2
     **/
    HmeWoJobSnReturnDTO2 remainingQty(Long tenantId, HmeWoJobSnDTO2 dto);

    /**
     *@description 获取工单组件
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/24 9:48
     *@param tenantId
     *@param workOrderId
     *@return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO6>
     **/
    List<HmeWoJobSnReturnDTO6> component(Long tenantId, String workOrderId);

    /**
     *@description 更新数量
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/26 10:24
     *@param tenantId
     *@param dto
     *@return void
     **/
    void updateQty(Long tenantId, HmeWoJobSnDTO5 dto);

    /**
     *@description 根据条码获取数量
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/26 10:24
     *@param tenantId
     *@param materialLotCode
     *@param workOrderId
     *@param wkcLinetId
     *@param siteId
     *@return java.lang.String
     **/
    MtMaterialLot materiallotQtyQuery(Long tenantId, String materialLotCode, String workOrderId, String wkcLinetId, String siteId);

    /**
     *@description 拆分
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/26 10:24
     *@param tenantId
     *@param dto
     *@return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnDTO7>
     **/
    HmeWoJobSnReturnDTO7 materialLotSplit(Long tenantId, HmeWoJobSnDTO6 dto);

    /**
     * 更新条码扩展及记录信息
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2020/11/23 19:56
     * @return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO7
     */
    HmeWoJobSnReturnDTO7 updateBarcodeRecord(Long tenantId, HmeWoJobSnReturnDTO7 dto);

    /**
     *@description 查询数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/27 14:56
     *@param tenantId
     *@param operationRecordId
     *@return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO7
     **/
    HmeWoJobSnReturnDTO7 materiallotQuery(Long tenantId, String operationRecordId);

    HmeWoJobSnReturnDTO7 waferSplit(Long tenantId, HmeWoJobSnDTO6 dto);

    /**
     * 来料录入导出
     *
     * @param tenantId
     * @param dto
     * @param response
     * @author sanfeng.zhang@hand-china.com 2021/4/21 19:13
     * @return void
     */
    void incomingExport(Long tenantId, HmeWoJobSnDTO dto, HttpServletResponse response);
}
