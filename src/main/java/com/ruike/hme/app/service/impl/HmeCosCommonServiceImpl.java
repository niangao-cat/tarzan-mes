package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeResponseDTO;
import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO5;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeCosOperationRecordRepository;
import com.ruike.hme.domain.repository.HmeEoJobEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.HmeCosEoJobSnSiteOutVO;
import com.ruike.hme.domain.vo.HmeCosMaterialLotVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.itf.app.service.ItfWorkOrderIfaceService;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderVO25;

import java.math.*;
import java.util.*;

/**
 * @Classname HmeCosCommomServiceImpl
 * @Description COS相关公共方法
 * @Date 2020/8/20 19:25
 * @Author yuchao.wang
 */
@Slf4j
@Service
public class HmeCosCommonServiceImpl implements HmeCosCommonService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private ItfWorkOrderIfaceService itfWorkOrderIfaceService;

    @Autowired
    private MtEventRepository mtEventRepository;

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
    @Override
    public HmeCosMaterialLotVO materialLotPropertyAndAttrsGet(Long tenantId, String materialLotId, boolean checkFlag, String... args) {
        //调用API获取物料批相关信息
        MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);
        if(Objects.isNull(materialLot) || StringUtils.isEmpty(materialLot.getMaterialLotId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "物料批信息"));
        }
        HmeCosMaterialLotVO materialLotVO = new HmeCosMaterialLotVO();
        BeanUtils.copyProperties(materialLot, materialLotVO);

        if(checkFlag) {
            if (!WmsConstant.CONSTANT_Y.equals(materialLotVO.getEnableFlag())) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", materialLot.getMaterialLotCode()));
            }
            if (!WmsConstant.ConstantValue.OK.equals(materialLotVO.getQualityStatus())) {
                throw new MtException("WMS_COST_CENTER_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0010", "WMS", materialLot.getMaterialLotCode()));
            }
        }

        //查询扩展属性
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLotId);
        List<MtExtendAttrVO> materialLotAttrVOList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        Map<String, String> materialLotAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotAttrVOList)) {
            materialLotAttrVOList.forEach(item -> materialLotAttrMap.put(item.getAttrName(), item.getAttrValue()));
        }
        materialLotVO.setMaterialLotAttrMap(materialLotAttrMap);

        return materialLotVO;
    }

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSn eoJobSnSiteIn(Long tenantId, HmeEoJobSn hmeEoJobSn) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = Objects.isNull(userDetails) ? -1L : userDetails.getUserId();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setSiteInDate(new Date());
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setReworkFlag("N");
        // 20210310 add by sanfeng.zhang for zhenyong.ban 工位、eo、条码、返修标识、job_type取出最大的eo_step_num + 1
        Integer maxEoStepNum = this.queryMaxEoStepNum(tenantId, hmeEoJobSn.getWorkcellId(), hmeEoJobSn.getEoId(), hmeEoJobSn.getMaterialLotId(), HmeConstants.ConstantValue.NO, hmeEoJobSn.getJobType(), hmeEoJobSn.getOperationId());
        hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
        return hmeEoJobSn;
    }

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSn eoJobSnSiteOut(Long tenantId, HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO) {
        //更新EoJobSn
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = Objects.isNull(userDetails) ? -1L : userDetails.getUserId();
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setJobId(hmeCosEoJobSnSiteOutVO.getEoJobSnId());
        hmeEoJobSn.setSiteOutBy(userId);
        hmeEoJobSn.setSiteOutDate(new Date());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);

        //新增eoJobEquipment
        //if(CollectionUtils.isNotEmpty(hmeCosEoJobSnSiteOutVO.getEquipmentList())) {
        //    HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        //    equSwitchParams.setJobId(hmeCosEoJobSnSiteOutVO.getEoJobSnId());
        //    equSwitchParams.setWorkcellId(hmeCosEoJobSnSiteOutVO.getWorkcellId());
        //    equSwitchParams.setHmeWkcEquSwitchDTO6List(hmeCosEoJobSnSiteOutVO.getEquipmentList());
        //    hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        //}
        return hmeEoJobSn;
    }

    @Override
    public HmeCosGetChipScanBarcodeResponseDTO getBaseScanBarcodeResponseDTO(Long tenantId,
                                                                             HmeCosMaterialLotVO hmeCosMaterialLotVO,
                                                                             HmeCosOperationRecord cosOperationRecord) {
        //查询物料数据
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, hmeCosMaterialLotVO.getMaterialId());
        if(Objects.isNull(mtMaterialVO) || StringUtils.isEmpty(mtMaterialVO.getMaterialId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "物料信息"));
        }

        Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();

        //查询工单工艺工位在制记录
        if(Objects.isNull(cosOperationRecord)) {
            cosOperationRecord = new HmeCosOperationRecord();
            cosOperationRecord.setOperationRecordId(materialLotAttrMap.get("COS_RECORD"));
            cosOperationRecord = hmeCosOperationRecordRepository.selectOne(cosOperationRecord);
            if (Objects.isNull(cosOperationRecord) || StringUtils.isEmpty(cosOperationRecord.getOperationRecordId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "工单工艺工位在制记录信息"));
            }
        }

        //构造返回数据
        HmeCosGetChipScanBarcodeResponseDTO responseDTO = new HmeCosGetChipScanBarcodeResponseDTO();
        responseDTO.setMaterialLotId(hmeCosMaterialLotVO.getMaterialLotId());
        responseDTO.setMaterialLotCode(hmeCosMaterialLotVO.getMaterialLotCode());
        responseDTO.setMaterialId(hmeCosMaterialLotVO.getMaterialId());
        responseDTO.setMaterialName(mtMaterialVO.getMaterialName());
        responseDTO.setMaterialCode(mtMaterialVO.getMaterialCode());
        responseDTO.setPrimaryUomQty(Objects.isNull(hmeCosMaterialLotVO.getPrimaryUomQty())?0L:hmeCosMaterialLotVO.getPrimaryUomQty().longValue());
        responseDTO.setLot(hmeCosMaterialLotVO.getLot());
        responseDTO.setWafer(materialLotAttrMap.get("WAFER_NUM"));
        responseDTO.setCosRecord(materialLotAttrMap.get("COS_RECORD"));
        responseDTO.setCosType(materialLotAttrMap.get("COS_TYPE"));
        responseDTO.setContainerType(materialLotAttrMap.get("CONTAINER_TYPE"));
        responseDTO.setLocationRow(materialLotAttrMap.get("LOCATION_ROW"));
        responseDTO.setLocationColumn(materialLotAttrMap.get("LOCATION_COLUMN"));
        responseDTO.setChipNum(materialLotAttrMap.get("CHIP_NUM"));
        responseDTO.setWorkingLot(materialLotAttrMap.get("WORKING_LOT"));
        responseDTO.setWorkOrderId(cosOperationRecord.getWorkOrderId());
        responseDTO.setType(cosOperationRecord.getType());
        responseDTO.setLotNo(cosOperationRecord.getLotNo());
        responseDTO.setRemark(cosOperationRecord.getRemark());
        responseDTO.setSurplusCosNum(cosOperationRecord.getSurplusCosNum());
        if (StringUtils.isNotBlank(materialLotAttrMap.get("AVG_WAVE_LENGTH"))) {
            try {
                responseDTO.setAverageWavelength(new BigDecimal(materialLotAttrMap.get("AVG_WAVE_LENGTH")));
            } catch (Exception e) {
                log.warn("<====HmeCosCommonServiceImpl.getBaseScanBarcodeResponseDTO 条码【{}】扩展属性[AVG_WAVE_LENGTH]值[{}]不为数字", hmeCosMaterialLotVO.getMaterialLotCode(), materialLotAttrMap.get("AVG_WAVE_LENGTH"));
            }
        }

        return responseDTO;
    }

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
    @Override
    public void verifyMaterialLotSiteOut(Long tenantId, String materialLotId) {
        HmeEoJobSn lastEoJobSn = hmeEoJobSnRepository.queryLastEoJobSnByMaterialLotId(tenantId, materialLotId);
        if(Objects.nonNull(lastEoJobSn) && StringUtils.isNotBlank(lastEoJobSn.getJobId())) {
            //查询报错参数
            String workcell = lastEoJobSn.getWorkcellId();
            String jobType = lastEoJobSn.getJobType();
            if(StringUtils.isNotBlank(workcell)){
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, workcell);
                workcell = mtModWorkcell.getWorkcellCode();
            }
            if(StringUtils.isNotBlank(jobType)) {
                jobType = lovAdapter.queryLovMeaning("HME.JOB_TYPE", tenantId, lastEoJobSn.getJobType());
            }
            throw new MtException("HME_CHIP_TRANSFER_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_017", "HME", workcell, jobType));
        }
    }

    @Override
    public String queryMaterialLotRecentHisId(Long tenantId, String materialLotId) {
        String recentHisId = null;
        if (StringUtils.isNotBlank(materialLotId)) {
            // 取最近的条码历史
            List<MtMaterialLotHis> mtMaterialLotHisList = mtMaterialLotHisRepository.selectByCondition(Condition.builder(MtMaterialLotHis.class).andWhere(Sqls.custom()
                    .andEqualTo(MtMaterialLotHis.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtMaterialLotHis.FIELD_MATERIAL_LOT_ID, materialLotId)).orderByDesc(MtMaterialLotHis.FIELD_CREATION_DATE).build());
            if (CollectionUtils.isNotEmpty(mtMaterialLotHisList)) {
                recentHisId = mtMaterialLotHisList.get(0).getMaterialLotHisId();
            }
        }
        return recentHisId;
    }

    @Override
    public Integer queryMaxEoStepNum(Long tenantId, String workcellId, String eoId, String materialLotId, String reworkFlag, String jobType, String operationId) {
        return hmeEoJobSnMapper.queryMaxEoStepNum(tenantId, workcellId, eoId, materialLotId, reworkFlag, jobType, operationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woEoReleasedForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
        if (mtWorkOrder != null) {
            woStatusUpdateVerifyForCos(tenantId, mtWorkOrder, "EORELEASED");
            woStatusUpdateToEoReleased(tenantId, mtWorkOrder);
        }
    }

    /**
     * woStatusUpdateToEoReleased 变更工单状态为下达作业
     * @param tenantId
     * @param mtWorkOrder
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/5/18
     */
    private void woStatusUpdateToEoReleased(Long tenantId, MtWorkOrder mtWorkOrder) {
        // 创建时间
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("WO_EORELEASED");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        // 更新状态及记录历史
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setEventId(eventId);
        statusUpdateVO.setStatus("EORELEASED");
        statusUpdateVO.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        mtWorkOrderRepository.woStatusUpdate(tenantId, statusUpdateVO);
        // 调用工单状态同步sap接口
        itfWorkOrderIfaceService.erpWorkOrderStatusReturnRestProxy(tenantId, Collections.singletonList(mtWorkOrder.getWorkOrderId()));
    }

    private void woStatusUpdateVerifyForCos(Long tenantId, MtWorkOrder mtWorkOrder, String targetStatus) {
        // 不为下达 则报错
        if (!HmeConstants.StatusCode.RELEASED.equals(mtWorkOrder.getStatus())) {
            throw new MtException("HME_COS_059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_059", "HME"));
        }
        MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COC_PRODUCTION_LINE", tenantId);
        if (mtModProductionLine != null) {
            Optional<LovValueDTO> firstOpt = lovValueDTOS.stream().filter(lov -> StringUtils.equals(lov.getValue(), mtModProductionLine.getProdLineCode())).findFirst();
            if (!firstOpt.isPresent()) {
                throw new MtException("HME_COS_060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_060", "HME"));
            }
        }
    }
}