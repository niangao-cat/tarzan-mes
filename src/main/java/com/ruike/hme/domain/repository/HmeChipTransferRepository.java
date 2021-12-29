package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.entity.HmeCosTransferHis;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.*;
import org.hzero.core.base.AopProxy;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;
import java.util.Map;


/**
 * 芯片转移
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/17 10:28
 */
public interface HmeChipTransferRepository extends AopProxy<HmeChipTransferRepository> {

    /**
     * 工位扫描-登入校验
     *
     * @param tenantId      租户ID
     * @param hmeEoJobSnDTO 扫描数据
     * @return HmeEoJobSnVO4
     */
    HmeEoJobSnVO4 workcellScan(Long tenantId, HmeEoJobSnDTO hmeEoJobSnDTO);

    /**
     * 待转移容器进站
     *
     * @param tenantId  租户id
     * @param vo        条件
     * @author sanfeng.zhang@hand-china.com 2020/8/17 11:34
     * @return com.ruike.hme.domain.vo.HmeChipTransferVO
     */
    HmeChipTransferVO inSiteTransfer(Long tenantId, HmeChipTransferVO vo);

    /**
     * 目标容器进站
     *
     * @param tenantId      租户id
     * @param vo            条件
     * @author sanfeng.zhang@hand-china.com 2020/8/17 16:30
     * @return com.ruike.hme.domain.vo.HmeChipTransferVO
     */
    HmeChipTransferVO inSiteTarget(Long tenantId, HmeChipTransferVO vo);

    /**
     * 手动单元格全量转移
     *
     * @param tenantId      租户id
     * @param vo2           条件
     * @author sanfeng.zhang@hand-china.com 2020/8/17 17:25
     * @return void
     */
    HmeChipTransferVO8 handleAllTransfer(Long tenantId, HmeChipTransferVO2 vo2);

    /**
     * 条码转移完成
     * 
     * @param tenantId     租户id
     * @param vo3          参数
     * @author sanfeng.zhang@hand-china.com 2020/8/24 10:33 
     * @return void
     */
    void handleChipTransferComplete(Long tenantId, HmeChipTransferVO3 vo3);

    /**
     * 转移完成
     *
     * @param tenantId     租户id
     * @param vo3          参数
     * @author sanfeng.zhang@hand-china.com 2020/8/24 10:33
     * @return void
     */
    void chipTransferComplete(Long tenantId, HmeChipTransferVO3 vo3);
    
    /**
     * 自动分配转移
     *
     * @param tenantId      租户id
     * @param vo4           条件
     * @author sanfeng.zhang@hand-china.com 2020/8/18 0:52
     * @return void
     */
    void autoAssignTransfer(Long tenantId, HmeChipTransferVO4 vo4);

    /**
     * 不良自动分配
     *
     * @param tenantId         租户id
     * @param vo4               条件
     * @author sanfeng.zhang@hand-china.com 2020/9/24 14:06
     * @return void
     */
    void autoNgAssignTransfer(Long tenantId, HmeChipTransferVO4 vo4);

    /**
     * 获取容器的行 列及芯片数
     *
     * @param tenantId          租户id
     * @param containerType     容器类型
     * @param operationId       工艺
     * @author sanfeng.zhang@hand-china.com 2020/8/18 10:58
     * @return com.ruike.hme.domain.vo.HmeChipTransferVO5
     */
    HmeChipTransferVO5 containerInfoQuery(Long tenantId, String containerType, String operationId, String cosType);

    /**
     * 获取工位未出站条码
     *
     * @param tenantId          租户id
     * @param workcellId        工位
     * @param  operationId       工艺
     * @author sanfeng.zhang@hand-china.com 2020/8/18 14:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeChipTransferVO6>
     */
    HmeChipTransferVO6 siteInMaterialCodeQuery(Long tenantId, String workcellId, String operationId);

    /**
     * 目标条码信息（未含装载信息）
     *
     * @param tenantId              租户id
     * @param materialLotCodeList   目标条码
     * @param workcellId            工位id
     * @author sanfeng.zhang@hand-china.com 2020/8/18 15:01
     * @return java.util.List<com.ruike.hme.domain.vo.HmeChipTransferVO>
     */
    List<HmeChipTransferVO> materialCodeTargetQuery(Long tenantId, String materialLotCodeList, String workcellId);

    /**
     * 物料批编码返回物料批列表
     *
     * @param tenantId              租户id
     * @param materialLotCodeList   物料批ids
     * @author sanfeng.zhang@hand-china.com 2020/8/20 16:14
     * @return java.util.List<tarzan.inventory.domain.entity.MtMaterialLot>
     */
    List<MtMaterialLot> materialLotPropertyGet(Long tenantId, List<String> materialLotCodeList);

    /**
     * 工单与工位对应产线是否一致
     *
     * @param tenantId
     * @param materialLotCode
     * @param prodLineId
     * @param workcellId
     * @author sanfeng.zhang@hand-china.com 2021/3/8 11:30
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    Map<String, Object> verifyProdLine(Long tenantId, String materialLotCode, String prodLineId, String workcellId);

    /**
     * 获取装载规则
     *
     * @param tenantId
     * @param materialLotId
     * @param operationId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/3/18 15:29
     */
    String queryLoadRule(Long tenantId, String materialLotId, String operationId);

    /**
     * 根据装载规则转移位置信息
     *
     * @param locationRow
     * @param locationColumn
     * @param stringListMap
     * @param recordMap
     * @param transferVO2List
     * @param transferVO
     * @param lotLoad
     * @param flag
     * @param vo4
     * @param tenantId
     * @return java.lang.Boolean
     * @author sanfeng.zhang@hand-china.com 2021/3/18 16:51
     */
    Boolean autoAssignTransferRules(Long locationRow, Long locationColumn, Map<String, List<HmeMaterialLotLoad>> stringListMap, Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap, List<HmeChipTransferVO2> transferVO2List, HmeChipTransferVO transferVO, HmeMaterialLotLoad lotLoad, Boolean flag, HmeChipTransferVO4 vo4, Long tenantId);

    /**
     * 批量保存转移历史
     *
     * @param tenantId
     * @param transferHisList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/18 16:55
     */
    void batchSaveCosTransferHis(Long tenantId, List<HmeCosTransferHis> transferHisList);
}
