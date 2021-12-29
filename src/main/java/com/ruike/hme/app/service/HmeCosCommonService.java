package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeResponseDTO;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmeCosEoJobSnSiteOutVO;
import com.ruike.hme.domain.vo.HmeCosMaterialLotVO;

/**
 * @Classname HmeCosCommonService
 * @Description COS相关公共方法
 * @Date 2020/8/20 19:25
 * @Author yuchao.wang
 */
public interface HmeCosCommonService {

    /**
     *
     * @Description 获取物料批及扩展属性
     *
     * @author yuchao.wang
     * @date 2020/8/20 19:50
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param checkFlag 是否校验物料批质量状态
     * @param args 报错消息参数 checkFlag=false不需要传
     * @return com.ruike.hme.domain.vo.HmeCosMaterialLotVO
     *
     */
    HmeCosMaterialLotVO materialLotPropertyAndAttrsGet(Long tenantId, String materialLotId, boolean checkFlag, String... args);

    /**
     *
     * @Description EoJobSn进站
     *
     * @author yuchao.wang
     * @date 2020/8/20 20:45
     * @param tenantId 租户ID
     * @param hmeEoJobSn EoJobSn
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    HmeEoJobSn eoJobSnSiteIn(Long tenantId, HmeEoJobSn hmeEoJobSn);

    /**
     *
     * @Description EoJobSn出站
     *
     * @author yuchao.wang
     * @date 2020/8/20 19:51
     * @param tenantId 租户ID
     * @param hmeCosEoJobSnSiteOutVO 参数
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    HmeEoJobSn eoJobSnSiteOut(Long tenantId, HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO);

    /**
     *
     * @Description 查询扫码操作返回的基础信息--取片平台、芯片不良记录平台
     *
     * @author yuchao.wang
     * @date 2020/8/20 20:32
     * @param tenantId 租户ID
     * @param hmeCosMaterialLotVO 物料批信息
     * @return com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeResponseDTO
     *
     */
    HmeCosGetChipScanBarcodeResponseDTO getBaseScanBarcodeResponseDTO(Long tenantId, HmeCosMaterialLotVO hmeCosMaterialLotVO, HmeCosOperationRecord cosOperationRecord);

    /**
     *
     * @Description 校验条码是否存在未出站的数据
     *
     * @author yuchao.wang
     * @date 2020/9/27 14:28
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return void
     *
     */
    void verifyMaterialLotSiteOut(Long tenantId, String materialLotId);

    /**
     * 查询最近的条码历史
     *
     * @param tenantId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2021/3/8 10:31
     * @return java.lang.String
     */
    String queryMaterialLotRecentHisId(Long tenantId, String materialLotId);


    /**
     * 根据工位、eo、条码、返修标识、job_type取最大的eoStepNum
     *
     * @param tenantId
     * @param workcellId
     * @param eoId
     * @param materialLotId
     * @param reworkFlag
     * @param jobType
     * @param operationId
     * @author sanfeng.zhang@hand-china.com 2021/3/10 16:21
     * @return java.lang.Integer
     */
    Integer queryMaxEoStepNum(Long tenantId, String workcellId, String eoId, String materialLotId, String reworkFlag, String jobType, String operationId);

    /**
     * 变更工单状态为下达作业
     * @param tenantId
     * @param workOrderId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/5/18
     */
    void woEoReleasedForUi(Long tenantId, String workOrderId);
}
