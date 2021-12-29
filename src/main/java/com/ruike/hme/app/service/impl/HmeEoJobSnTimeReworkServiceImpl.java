package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeEoJobSnTimeDTO;
import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO5;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.app.service.HmeEoJobSnReworkService;
import com.ruike.hme.app.service.HmeEoJobSnTimeReworkService;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeEoJobSnReWorkMapper;
import com.ruike.hme.infra.util.HmeEoJobSnUtils;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.*;
import java.util.*;
import java.util.stream.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * 时效返修作业平台-SN作业 Service
 *
 * @author yuchao.wang@hand-china.com 2021-01-26 15:04:39
 */
@Slf4j
@Service
public class HmeEoJobSnTimeReworkServiceImpl implements HmeEoJobSnTimeReworkService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private HmeOperationTimeObjectRepository hmeOperationTimeObjectRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;

    @Autowired
    private HmeEoJobSnReworkService hmeEoJobSnReworkService;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeEoJobSnReWorkMapper hmeEoJobSnReWorkMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @Autowired
    private HmeProcessNcHeaderRepository hmeProcessNcHeaderRepository;

    /**
     *
     * @Description 时效作业-入炉
     *
     * @author yuchao.wang
     * @date 2020/11/3 9:47
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.inSiteScan.Start tenantId={},dto={}", tenantId, dto);
        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getSnNum())) {
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        defaultInputVerification(tenantId, dto);

        //批量查询条码及步骤信息
        String materialLotId = dto.getSnLineList().get(0).getMaterialLotId();
        HmeEoJobSnVO16 hmeEoJobSnVO16 = hmeEoJobSnCommonService.batchQueryAndCheckMaterialLotByIdsForTimeRework(tenantId, materialLotId);
        HmeEoJobSnVO5 snVO = hmeEoJobSnVO16.getEoJobSnMap().get(materialLotId);

        //进站校验
        inSiteScanValidate(tenantId, dto, snVO.getEoId());

        log.info("HmeEoJobSnTimeReworkServiceImpl.inSiteScan snNum={},snType={},dto.getSnLineList().size()={}",
                dto.getSnNum(), dto.getSnType(), dto.getSnLineList().size());

        //如果有容器则先卸载
        if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(snVO.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(snVO.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }

        //进站逻辑
        HmeEoJobSn snJobEntity = inSiteScanForRework(tenantId, dto, hmeEoJobSnVO16, snVO.getEoId());

        //20210727 add by sanfeng.zhang for wenxin.zhang 创建设备数据
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(snJobEntity.getJobId());
        equSwitchParams.setWorkcellId(dto.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);

        //如果是第一次进站，将自身投料
        int inSiteCount = hmeEoJobSnMapper.queryEoJobSnCountByEoId(tenantId, snVO.getEoId());
        if (inSiteCount == 1) {
            // 20210527 add by sanfeng.zhang for fang.pan 构建eo返修关系
            hmeEoJobSnReworkService.createEoRel(tenantId, dto, snVO.getEoId());
            releaseSelfForFirstInSite(tenantId, dto, snVO, snJobEntity);
        }
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.inSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
    }

    /**
     * 
     * @Description 如果是第一次进站，将自身投料
     * 
     * @author yuchao.wang
     * @date 2021/1/28 15:46
     * @param tenantId 租户ID
     * @param dto 参数
     * @param snVO 参数
     * @param snJobEntity eoJobSn实体
     * @return void
     * 
     */
    private void releaseSelfForFirstInSite(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO5 snVO, HmeEoJobSn snJobEntity) {
        // 20210923 add by  sanfeng.zhang for wenxin.zhang 新条码 则找旧条码的物料
        // 20210923 add by  sanfeng.zhang for wenxin.zhang 新条码 则找旧条码 进行投料
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        String materialId = snVO.getMaterialId();
        String materialLotId = snVO.getMaterialLotId();
        if (newMaterialLotFlag) {
            // ture则为新条码
            String reworkMaterialLotCode = hmeEoJobSnReWorkMapper.queryReworkMaterialLotCode(tenantId, snVO.getMaterialLotId());
            List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, Collections.singletonList(reworkMaterialLotCode));
            if (CollectionUtils.isEmpty(materialLotList)) {
                throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_004", "HME", reworkMaterialLotCode));
            }
            materialLotId = materialLotList.get(0).getMaterialLotId();
            materialId = materialLotList.get(0).getMaterialId();
        }
        //查询EO组件中与当前条码物料对应的组件
        List<HmeBomComponentVO3> eoComponentList = hmeEoJobSnMapper.queryEoComponentByEoIdAndMaterialId(tenantId, snVO.getEoId(), materialId, snJobEntity.getEoStepId());
        if (CollectionUtils.isEmpty(eoComponentList) || eoComponentList.size() != 1) {
            throw new MtException("HME_EO_JOB_SN_162", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_162", "HME"));
        }
        HmeBomComponentVO3 eoComponent = eoComponentList.get(0);

        //查询条码相关信息
        HmeMaterialLotVO4 materialLotInfo = hmeEoJobSnMapper.queryMaterialLotInfoForInSiteRelease(tenantId, materialLotId);
        if (Objects.isNull(eoComponent.getBomComponentQty()) || Objects.isNull(materialLotInfo.getPrimaryUomQty())
                || !eoComponent.getBomComponentQty().equals(materialLotInfo.getPrimaryUomQty())) {
            throw new MtException("HME_EO_JOB_SN_162", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_162", "HME"));
        }

        //查询开班实绩编码
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());

        //创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        //创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        if (newMaterialLotFlag) {
            // 新条码找旧条码进行投料时  将旧条码失效 数量置为0
            String startEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventRequestId(eventRequestId);
                setEventTypeCode("HOME_MADE_PRODUCTION_REWORK_START");
            }});
            String finalMaterialLotId = materialLotId;
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2(){{
                setEventId(startEventId);
                setMaterialLotId(finalMaterialLotId);
                setEnableFlag(NO);
                setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
            }}, "N");
        }

        // 更新条码在制标识
        List<MtExtendVO5> extendAttrList = new ArrayList<>(1);
        MtExtendVO5 inworkFlagAttr = new MtExtendVO5();
        inworkFlagAttr.setAttrName("MF_FLAG");
        inworkFlagAttr.setAttrValue(YES);
        extendAttrList.add(inworkFlagAttr);
        //V20210324 modify by penglin.sui for fang.pan 返修第一次进站将进站sn返修标志改为Y
        MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
        reworkFlagAttr.setAttrName("REWORK_FLAG");
        reworkFlagAttr.setAttrValue(YES);
        extendAttrList.add(reworkFlagAttr);
        MtExtendVO5 statusFlagAttr = new MtExtendVO5();
        statusFlagAttr.setAttrName("STATUS");
        statusFlagAttr.setAttrValue("NEW");
        extendAttrList.add(statusFlagAttr);
        if (!newMaterialLotFlag) {
            MtExtendVO5 inFlagAttr = new MtExtendVO5();
            inFlagAttr.setAttrName("OLD_BARCODE_IN_FLAG");
            inFlagAttr.setAttrValue(YES);
            extendAttrList.add(inFlagAttr);
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", materialLotInfo.getMaterialLotId(), eventId, extendAttrList);

        //更新现有量
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(materialLotInfo.getLocatorId());
        mtInvOnhandQuantityVO9.setMaterialId(materialLotInfo.getMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(eoComponent.getBomComponentQty());
        mtInvOnhandQuantityVO9.setEventId(eventId);
        mtInvOnhandQuantityVO9.setLotCode(materialLotInfo.getLot());
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        // 客户机内部订单不存在投自身，因此单独获取事务用于mes客户机库存冲销，不传送sap
        if (StringUtils.isBlank(snJobEntity.getWorkOrderId())) {
            //未传入工单信息,请检查!
            throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0002", "HME"));
        }

        HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, snVO.getWorkOrderNum());
        // 事务类型
        String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
        // 判断工单是否存在于返修拆机记录中
        if (!Objects.isNull(splitRecord)) {
            //判断内部订单号是否为空
            if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())) {
                transactionTypeCode = HmeConstants.TransactionTypeCode.AF_ZSD005_ISSUE;
            }
        }

        WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
        wmsTransactionTypePara.setTenantId(tenantId);
        wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);

        //记录事务明细
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
        objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
        objectTransactionVO.setMaterialLotId(materialLotInfo.getMaterialLotId());
        objectTransactionVO.setLotNumber(materialLotInfo.getLot());
        objectTransactionVO.setEventId(eventId);
        objectTransactionVO.setMaterialId(materialLotInfo.getMaterialId());
        objectTransactionVO.setMaterialCode(materialLotInfo.getMaterialCode());
        objectTransactionVO.setTransactionQty(BigDecimal.valueOf(eoComponent.getBomComponentQty()));
        objectTransactionVO.setTransactionUom(materialLotInfo.getMaterialPrimaryUomCode());
        objectTransactionVO.setTransactionTime(new Date());
        objectTransactionVO.setPlantId(dto.getSiteId());
        objectTransactionVO.setLocatorId(materialLotInfo.getLocatorId());
        objectTransactionVO.setLocatorCode(materialLotInfo.getLocatorCode());
        objectTransactionVO.setWarehouseId(materialLotInfo.getWarehouseId());
        objectTransactionVO.setWarehouseCode(materialLotInfo.getWarehouseCode());
        objectTransactionVO.setWorkOrderNum(snVO.getWorkOrderNum());
        objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
        objectTransactionVO.setProdLineCode(snVO.getProdLineCode());
        String moveType = "";
        // 事务类型为AF_ZSD005_ISSUE 移动类型置为空
        if (StringUtils.equals(transactionTypeCode, HmeConstants.TransactionTypeCode.AF_ZSD005_ISSUE)) {
            moveType = "";
        } else {
            moveType = Objects.isNull(wmsTransactionType) ? "261" : wmsTransactionType.getMoveType();
        }
        objectTransactionVO.setMoveType(moveType);
        objectTransactionVO.setBomReserveNum(StringUtils.trimToEmpty(eoComponent.getReserveNum()));
        objectTransactionVO.setBomReserveLineNum(String.valueOf(eoComponent.getLineNumber()));
        objectTransactionVO.setSoNum(materialLotInfo.getSoNum());
        objectTransactionVO.setSoLineNum(materialLotInfo.getSoLineNum());
        objectTransactionRequestList.add(objectTransactionVO);
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

        //装配API
        MtAssembleProcessActualVO5 assembleProcessVo5 = new MtAssembleProcessActualVO5();
        assembleProcessVo5.setEoId(snVO.getEoId());
        assembleProcessVo5.setMaterialId(eoComponent.getBomComponentMaterialId());
        assembleProcessVo5.setBomComponentId(eoComponent.getBomComponentId());
        assembleProcessVo5.setRouterId(eoComponent.getRouterId());
        assembleProcessVo5.setOperationId(dto.getOperationId());
        assembleProcessVo5.setRouterStepId(snJobEntity.getEoStepId());
        assembleProcessVo5.setTrxAssembleQty(eoComponent.getBomComponentQty());
        assembleProcessVo5.setAssembleExcessFlag(NO);
        assembleProcessVo5.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
        assembleProcessVo5.setOperationBy(snJobEntity.getSiteInBy());
        assembleProcessVo5.setWorkcellId(dto.getWorkcellId());
        assembleProcessVo5.setEventRequestId(eventRequestId);
        assembleProcessVo5.setParentEventId(eventId);
        assembleProcessVo5.setMaterialLotId(materialLotInfo.getMaterialLotId());
        if (Objects.nonNull(mtWkcShift)) {
            assembleProcessVo5.setShiftCode(mtWkcShift.getShiftCode());
            if (Objects.nonNull(mtWkcShift.getShiftDate())) {
                assembleProcessVo5.setShiftDate(mtWkcShift.getShiftDate());
            }
        }
        assembleProcessVo5.setLocatorId(materialLotInfo.getLocatorId());
        mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVo5);

        // 改造工单更新料号 判断eo物料和条码物料是否相同 不一致则更新条码物料
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(snJobEntity.getEoId());
        if (!StringUtils.equals(mtEo.getMaterialId(), materialLotInfo.getMaterialId())) {
            String woEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("WO_MATERIAL_CHANGE");
            }});
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                setMaterialLotId(materialLotInfo.getMaterialLotId());
                setMaterialId(mtEo.getMaterialId());
                setPrimaryUomId(mtEo.getUomId());
                setEventId(woEventId);
            }}, NO);
        }
    }

    /**
     *
     * @Description 进站校验
     *
     * @author yuchao.wang
     * @date 2020/12/23 15:22
     * @return void
     *
     */
    private void inSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto, String eoId) {
        if (HmeConstants.LoadTypeCode.CONTAINER.equals(dto.getSnType()) || dto.getSnLineList().size() > 1) {
            //时效返修作业平台不支持容器扫描,请按照SN编码单独扫描!
            throw new MtException("HME_EO_JOB_SN_174", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_174", "HME", dto.getSnNum()));
        }

        //V20210727 modify by sanfeng.zhang for wenxin.zhang 进站校验工位绑定设备
        hmeEoJobSnCommonService.workcellBindEquipmentValidate(tenantId, dto.getOperationId(), dto.getWorkcellId());
        // 20210908 add by sanfeng.zhang for hui.gu 判断SN在该工序是否已拦截
        String materialLotId = dto.getSnLineList().get(0).getMaterialLotId();
        if (StringUtils.isNotBlank(materialLotId)) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            hmeEoJobSnCommonService.interceptValidate(tenantId, dto.getWorkcellId(), Collections.singletonList(mtMaterialLot));
        }

        //校验是否重复进站
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnCommonService.queryEoJobSnForTimeInSite(tenantId, true, dto.getOperationId(), Collections.singletonList(eoId));
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            //校验工位是否一致
            long count = hmeEoJobSnList.stream().filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).count();
            if (count > 0) {
                List<HmeEoJobSn> exitEoJobSn = hmeEoJobSnList.stream()
                        .filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, exitEoJobSn.get(0).getWorkcellId());
                //当前SN已在${1}进站,不允许在此工位重复进站
                throw new MtException("HME_EO_JOB_SN_144", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_144", "HME", mtModWorkcell.getWorkcellName()));
            }

            //校验平台是否一致
            count = hmeEoJobSnList.stream().filter(item -> !dto.getJobType().equals(item.getJobType())).count();
            if (count > 0) {
                String jobTypeDesc = "";
                List<HmeEoJobSn> exitEoJobSn = hmeEoJobSnList.stream()
                        .filter(item -> !dto.getJobType().equals(item.getJobType())).collect(Collectors.toList());
                switch(exitEoJobSn.get(0).getJobType()){
                    case HmeConstants.JobType.BATCH_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.BATCH_PROCESS;
                        break;
                    case HmeConstants.JobType.TIME_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.TIME_PROCESS;
                        break;
                    case HmeConstants.JobType.PREPARE_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.PREPARE_PROCESS;
                        break;
                    case HmeConstants.JobType.COS_COMPLETED:
                        jobTypeDesc = HmeConstants.JobTypeDesc.COS_COMPLETED;
                        break;
                    case HmeConstants.JobType.PACKAGE_PROCESS_PDA:
                        jobTypeDesc = HmeConstants.JobTypeDesc.PACKAGE_PROCESS_PDA;
                        break;
                    case HmeConstants.JobType.REPAIR_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.REPAIR_PROCESS;
                        break;
                    case HmeConstants.JobType.SINGLE_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.SINGLE_PROCESS;
                        break;
                    default:
                        break;
                }

                //当前SN已在${1}进站,不允许在此平台重复进站
                throw new MtException("HME_EO_JOB_SN_143", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_143", "HME", jobTypeDesc));
            }

            //判断报哪种错 目前只报错SN已入炉
            throw new MtException("HME_EO_JOB_TIME_SN_002", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_002", "HME"));
        }
    }

    /**
     *
     * @Description 时效进站-返修进站
     *
     * @author yuchao.wang
     * @date 2020/12/21 15:14
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @param eoId eoId
     * @return HmeEoJobSn
     *
     */
    private HmeEoJobSn inSiteScanForRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16, String eoId) {
        //取条码信息及上一步工艺
        Map<String, HmeEoJobSnVO5> snVoMap = hmeEoJobSnVO16.getEoJobSnMap();
        Map<String, HmeRouterStepVO2> nearStepMap = hmeEoJobSnVO16.getNearStepMap();
        Map<String, HmeRouterStepVO2> normalStepMap = hmeEoJobSnVO16.getNormalStepMap();

        //取当前步骤信息
        Map<String, HmeRouterStepVO3> currentStepMap = hmeEoJobSnCommonService
                .batchQueryCurrentRouterStepForTime(tenantId, Collections.singletonList(eoId), dto.getOperationId());

        //批量查询所有正常步骤的下一步骤
        List<String> normalStepIdList = normalStepMap.values().stream()
                .map(HmeRouterStepVO2::getRouterStepId).collect(Collectors.toList());
        Map<String, String> nextStepMap = hmeEoJobSnCommonService.batchQueryNextStep(tenantId, normalStepIdList);

        //获取EoJobSn Id/Cid
        String eoJobSnId = customSequence.getNextKey("hme_eo_job_sn_s");
        String eoJobSnCid = customSequence.getNextKey("hme_eo_job_sn_cid_s");

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        // 获取当前时间
        final Date currentDate = new Date();

        //取到当前条码的数据
        HmeEoJobSnVO3 jobSn = dto.getSnLineList().get(0);
        HmeEoJobSnVO5 snVO = snVoMap.get(jobSn.getMaterialLotId());
        HmeRouterStepVO2 normalRouterStep = normalStepMap.get(jobSn.getEoId());
        HmeRouterStepVO2 nearRouterStep = nearStepMap.get(jobSn.getEoId());
        HmeRouterStepVO3 currentRouterStep = currentStepMap.get(jobSn.getEoId());
        if (Objects.isNull(currentRouterStep) || StringUtils.isBlank(currentRouterStep.getRouterStepId())) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        //构造返回数据
        jobSn.setWorkcellId(dto.getWorkcellId());
        jobSn.setJobType(dto.getJobType());
        jobSn.setOperationId(dto.getOperationId());
        jobSn.setOperationIdList(dto.getOperationIdList());
        jobSn.setReworkFlag(snVO.getReworkFlag());
        jobSn.setEoStepId(currentRouterStep.getRouterStepId());
        jobSn.setSiteId(dto.getSiteId());
        jobSn.setWorkOrderId(snVO.getWorkOrderId());
        jobSn.setMaterialId(snVO.getMaterialId());

        log.info("当前jobSn对应eoId={},入口步骤标识:{}", jobSn.getEoId(), currentRouterStep.getEntryStepFlag());

        if (Objects.isNull(nearRouterStep) || StringUtils.isBlank(nearRouterStep.getRouterStepId())
                || Objects.isNull(normalRouterStep) || StringUtils.isBlank(normalRouterStep.getRouterStepId())) {
            // 当前为非入口步骤, 且根据EO没有找到最近加工步骤
            throw new MtException("HME_EO_JOB_SN_046",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_046", "HME"));
        }

        log.info("当前步骤:" + currentRouterStep.getRouterStepId() + ",最近一步骤:" + nearRouterStep.getRouterStepId()
                + ",最近一正常步骤:" + normalRouterStep.getRouterStepId() + ",最近一正常步骤的下一步骤:" +
                (nextStepMap.containsKey(normalRouterStep.getRouterStepId()) ? "" : nextStepMap.get(normalRouterStep.getRouterStepId())));

        //执行工艺步骤移入
        MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
        eoRouterActual.setRouterStepId(jobSn.getEoStepId());
        eoRouterActual.setEoId(jobSn.getEoId());
        eoRouterActual.setQty(snVO.getEoQty().doubleValue());
        eoRouterActual.setPreviousStepId(nearRouterStep.getEoStepActualId());
        eoRouterActual.setWorkcellId(nearRouterStep.getWipWorkcellId());
        eoRouterActual.setReworkStepFlag(HmeConstants.ConstantValue.YES);
        eoRouterActual.setSourceEoStepActualId(normalRouterStep.getEoStepActualId());
        HmeRouterStepVO nearStepVO = new HmeRouterStepVO();
        BeanUtils.copyProperties(nearRouterStep, nearStepVO);
        eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
        mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);

        //工序排队加工
        eoWorking(tenantId, snVO.getEoQty(), jobSn);

        // 插入工序作业平台表
        List<HmeEoJobSn> insertList = new ArrayList<>();
        HmeEoJobSn snJobEntity = getSnJobEntity(tenantId, userId, currentDate, eoJobSnId, eoJobSnCid, jobSn);
        insertList.add(snJobEntity);
        hmeEoJobSnRepository.myBatchInsert(insertList);

        //构造采集数据和自检数据插入参数，批量加载数据采集项
        List<HmeEoJobSnVO2> dataRecordInitParamList = new ArrayList<>();
        HmeEoJobSnVO2 dataRecordInitParam = new HmeEoJobSnVO2();
        BeanUtils.copyProperties(snJobEntity, dataRecordInitParam);
        dataRecordInitParam.setProductionVersion(snVO.getWoProductionVersion());
        dataRecordInitParam.setItemType(snVO.getItemType());
        dataRecordInitParamList.add(dataRecordInitParam);
        hmeEoJobDataRecordRepository.batchInitEoJobDataRecord(tenantId, dataRecordInitParamList);
        return snJobEntity;
    }

    /**
     *
     * @Description 工序排队加工
     *
     * @author yuchao.wang
     * @date 2020/12/17 10:53
     * @return void
     *
     */
    private void eoWorking(Long tenantId, BigDecimal eoQty, HmeEoJobSnVO3 snLine) {
        //创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_IN");

        // 取当前步骤的步骤实绩
        MtEoRouterActualVO2 routerActualParam = new MtEoRouterActualVO2();
        routerActualParam.setEoId(snLine.getEoId());
        List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                mtEoRouterActualMapper.eoOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList)) {
            throw new MtException("HME_EO_JOB_SN_039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
        }

        // 多条步骤实绩时，取最新的
        String eoStepActualId;
        if (eoOperationLimitCurrentRouterList.size() > 1) {
            eoStepActualId = eoOperationLimitCurrentRouterList.stream()
                    .max(Comparator.comparing(MtEoRouterActualVO5::getLastUpdateDate)).get()
                    .getEoStepActualId();
        } else {
            eoStepActualId = eoOperationLimitCurrentRouterList.get(0).getEoStepActualId();
        }
        log.debug("当前步骤的步骤实绩:" + eoStepActualId);

        // 工序排队
        MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
        mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO4.setQueueQty(new Double(String.valueOf(eoQty)));
        mtEoStepWipVO4.setWorkcellId(snLine.getWorkcellId());
        mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);

        // 工序在制
        MtEoRouterActualVO19 workingParam = new MtEoRouterActualVO19();
        workingParam.setEventRequestId(eventRequestId);
        workingParam.setEoStepActualId(eoStepActualId);
        workingParam.setWorkcellId(snLine.getWorkcellId());
        workingParam.setQty(new Double(String.valueOf(eoQty)));
        workingParam.setSourceStatus("QUEUE");
        workingParam.setLastWorkcellId(snLine.getWorkcellId());
        mtEoRouterActualRepository.eoWkcAndStepWorking(tenantId, workingParam);
    }

    /**
     *
     * @Description 默认输入校验
     *
     * @author yuchao.wang
     * @date 2020/11/5 14:11
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    private void defaultInputVerification(Long tenantId, HmeEoJobSnVO3 dto) {
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位ID"));
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        }
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "站点ID"));
        }
        if (StringUtils.isBlank(dto.getJobType())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "作业类型"));
        }
        if (StringUtils.isBlank(dto.getSnType())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "条码类型"));
        }
        if (CollectionUtils.isEmpty(dto.getSnLineList())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "条码列表"));
        }
    }

    /**
     *
     * @Description 时效作业-出炉
     *
     * @author yuchao.wang
     * @date 2020/11/5 13:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnTimeDTO outSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.outSiteScan.Start tenantId={},dto={}", tenantId, dto);
        // 非空校验
        defaultInputVerification(tenantId, dto);
        dto.setOutSiteAction(HmeConstants.OutSiteAction.COMPLETE);

        //获取eoId jobId
        String eoId = dto.getSnLineList().get(0).getEoId();
        String jobId = dto.getSnLineList().get(0).getJobId();

        HmeEoJobSnTimeDTO validateResult = outSiteScanValidate(tenantId, dto, eoId, jobId);
        if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
            return validateResult;
        }

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        // 获取当前时间
        final Date currentDate = new Date();

        //如果有容器则先卸载
        HmeMaterialLotVO3 hmeMaterialLot = validateResult.getMaterialLotList().get(0);
        if (StringUtils.isNotBlank(hmeMaterialLot.getCurrentContainerId())) {
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(hmeMaterialLot.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(hmeMaterialLot.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }

        //批量查询EO、WO相关信息
        HmeEoJobSnVO16 hmeEoJobSnVO16 = hmeEoJobSnCommonService.batchQueryEoAndWoInfoById(tenantId, Collections.singletonList(eoId));
        hmeEoJobSnVO16.setUserId(userId);
        hmeEoJobSnVO16.setCurrentDate(currentDate);
        hmeEoJobSnVO16.setHmeEoJobSnEntityList(validateResult.getHmeEoJobSnEntityList());
        hmeEoJobSnVO16.setTimeRework(true);

        // 完工会更新eo状态 故提前判断新旧条码
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        hmeEoJobSnVO16.setNewMaterialLotFlag(newMaterialLotFlag);
        //执行订单完成
        hmeEoJobSnCommonService.siteOutComplete(tenantId, dto.getWorkcellId(), hmeEoJobSnVO16.getEoMap().get(eoId));

        //获取条码返修标识
        Map<String, String> reworkFlagMap = new HashMap<>();
        reworkFlagMap.put(hmeMaterialLot.getMaterialLotId(), hmeMaterialLot.getReworkFlag());
        hmeEoJobSnVO16.setReworkFlagMap(reworkFlagMap);

        //非首道序调用出站通用主方法,首道序调用单独方法
        List<WmsObjectTransactionResponseVO> transactionResponseList = new ArrayList<WmsObjectTransactionResponseVO>();
        if (HmeConstants.ConstantValue.YES.equals(validateResult.getCurrentStep().getEntryStepFlag())
                && StringUtils.isBlank(validateResult.getCurrentStep().getRouterDoneStepId())) {
            hmeEoJobSnCommonService.batchMainOutSiteForTimeReworkEntryStep(tenantId, dto, hmeEoJobSnVO16);
        } else {
            transactionResponseList = hmeEoJobSnCommonService.batchMainOutSite(tenantId, dto, hmeEoJobSnVO16);
        }

        //批量更新出站
        hmeEoJobSnRepository.batchOutSite(tenantId, userId, Collections.singletonList(jobId));

        //20210727 modify by sanfeng.zhang for wenxin.zhang 保存设备数据
        hmeEoJobEquipmentRepository.batchSaveEquipmentRecordForOutSite(tenantId, dto);

        //完工数据统计
        hmeEoJobSnCommonService.batchWkcCompleteOutputRecord(tenantId, dto, hmeEoJobSnVO16);

        //发送实时接口
        if (CollectionUtils.isNotEmpty(transactionResponseList)) {
            hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
        }
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.outSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
        return new HmeEoJobSnTimeDTO();
    }

    /**
     *
     * @Description 出站(加工完成/继续返修)校验
     *
     * @author yuchao.wang
     * @date 2020/12/24 10:15
     * @return com.ruike.hme.api.dto.HmeEoJobSnTimeDTO
     *
     */
    private HmeEoJobSnTimeDTO outSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto, String eoId, String jobId) {
        if (HmeConstants.LoadTypeCode.CONTAINER.equals(dto.getSnType()) || dto.getSnLineList().size() > 1) {
            //时效返修作业平台不支持容器扫描,请按照SN编码单独扫描!
            throw new MtException("HME_EO_JOB_SN_174", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_174", "HME", dto.getSnNum()));
        }

        HmeEoJobSnTimeDTO result = new HmeEoJobSnTimeDTO();

        //获取所有条码Id
        String materialLotId = dto.getSnLineList().get(0).getMaterialLotId();

        //根据JobId查询EoJobSn数据
        List<HmeEoJobSn> hmeEoJobSnEntityList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_ID, jobId)).build());
        if (CollectionUtils.isEmpty(hmeEoJobSnEntityList) || Objects.isNull(hmeEoJobSnEntityList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "作业信息"));
        }

        //查询条码信息
        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnCommonService.batchQueryMaterialLotReworkFlagForTime(tenantId, Collections.singletonList(materialLotId));

        //V20210630 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可出站
        StringBuilder materialLots = new StringBuilder();
        for(HmeMaterialLotVO3 item : materialLotList){
            if(StringUtils.isNotBlank(item.getStocktakeFlag()) &&
                    HmeConstants.ConstantValue.YES.equals(item.getStocktakeFlag())){
                if(materialLots.length() == 0){
                    materialLots.append(item.getMaterialLotCode());
                }else{
                    materialLots.append("," + item.getMaterialLotCode());
                }
            }
        }
        if(materialLots.length() > 0){
            //此SN【${1}】正在盘点,不可出站
            throw new MtException("HME_EO_JOB_SN_204", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_204", "HME", materialLots.toString()));
        }

        //校验EoJobSn是否已经出站，有则报错
        if (Objects.nonNull(hmeEoJobSnEntityList.get(0).getSiteOutDate())) {
            //已完成SN号不允许重复完成
            throw new MtException("HME_EO_JOB_SN_056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_056", "HME"));
        }

        //如果是返修，并且是点的加工完成，则需要给用户确认框
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            //获取当前步骤
            HmeRouterStepVO5 currentStep = hmeEoJobSnCommonService.queryCurrentStepByEoAndOperation(tenantId, eoId, dto.getOperationId());
            result.setCurrentStep(currentStep);

            //如果非首尾步骤不允许点加工完成
            if (!HmeConstants.ConstantValue.YES.equals(currentStep.getEntryStepFlag()) && StringUtils.isBlank(currentStep.getRouterDoneStepId())) {
                //非首末序不允许出炉操作
                throw new MtException("HME_EO_JOB_SN_176", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_176", "HME"));
            }

            if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag())) {
                // 产品将返修完成，后续以正常加工的方式继续生产，是否确认
                result.setErrorCode("HME_EO_JOB_SN_038");
                result.setErrorMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_038", "HME"));
                return result;
            }
        } else {
            //如果是点继续返修，由于不需要用eo/wo信息，只需要校验是否存在，不需要查询
            SecurityTokenHelper.close();
            List<MtEo> eoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class)
                    .select(MtEo.FIELD_EO_ID, MtEo.FIELD_WORK_ORDER_ID, MtEo.FIELD_QTY)
                    .andWhere(Sqls.custom().andEqualTo(MtEo.FIELD_EO_ID, eoId)).build());
            if (CollectionUtils.isEmpty(eoList) || Objects.isNull(eoList.get(0)) || StringUtils.isBlank(eoList.get(0).getEoId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "EO信息"));
            }
            result.setReworkEo(eoList.get(0));

            String workOrderId = eoList.get(0).getWorkOrderId();
            SecurityTokenHelper.close();
            int recordCount = mtWorkOrderRepository.selectCountByCondition(Condition.builder(MtWorkOrder.class)
                    .andWhere(Sqls.custom().andEqualTo(MtWorkOrder.FIELD_WORK_ORDER_ID, workOrderId)).build());
            if (recordCount < 1) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "工单信息"));
            }
        }

        // 校验数据采集项不良判定
        if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag()) || !"HME_EO_JOB_SN_172".equals(dto.getErrorCode())) {
            //查询所有有结果的数据采集项
            List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobDataRecordRepository.queryForNcRecordValidate(tenantId, dto.getWorkcellId(), dto.getJobId());
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                HmeEoJobSn dataRecordValidateResult = null;

                HmeProcessNcHeaderVO2 processNcInfo = null;
                //根据工艺判断是器件不良还是老化不良
                if (hmeEoJobSnCommonService.isAgeingNc(tenantId, dto.getOperationId())) {
                    //老化不良
                    //查询工序信息
                    String stationId = hmeEoJobSnMapper.queryWkcStation(tenantId, dto.getSiteId(), dto.getWorkcellId());

                    String cosModel = mtMaterialLot.getMaterialLotCode().substring(4, 5);

                    processNcInfo = hmeProcessNcHeaderRepository
                            .queryProcessNcInfoForAgeingNcRecordValidate(tenantId, mtMaterialLot.getMaterialId(), stationId, cosModel , mtMaterialLot.getMaterialLotCode(), dto.getOperationId());
                    //对数据采集线校验
                    dataRecordValidateResult = hmeEoJobSnCommonService
                            .dataRecordAgeingProcessNcValidate(tenantId, mtMaterialLot.getMaterialLotCode(), hmeEoJobDataRecordList, processNcInfo);
                }
                if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {
                    //新增工序不良参数返回
                    result.setErrorMessage(dataRecordValidateResult.getErrorMessage());
                    result.setErrorCode(dataRecordValidateResult.getErrorCode());
                    result.setHmeNcDisposePlatformDTO(dataRecordValidateResult.getHmeNcDisposePlatformDTO());
                    result.setProcessNcDetailList(dataRecordValidateResult.getProcessNcDetailList());
                    return result;
                }
            }
        }

        result.setHmeEoJobSnEntityList(hmeEoJobSnEntityList);
        result.setMaterialLotList(materialLotList);
        result.setReworkFlag(materialLotList.get(0).getReworkFlag());
        return result;
    }

    /**
     *
     * @Description 时效作业平台-继续返修
     *
     * @author yuchao.wang
     * @date 2020/12/24 10:36
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    public void continueRework(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.continueRework.Start tenantId={},dto={}", tenantId, dto);
        // 非空校验
        defaultInputVerification(tenantId, dto);
        dto.setOutSiteAction(HmeConstants.OutSiteAction.REWORK);

        //出站校验
        String eoId = dto.getSnLineList().get(0).getEoId();
        String jobId = dto.getSnLineList().get(0).getJobId();
        HmeEoJobSnTimeDTO validateResult = outSiteScanValidate(tenantId, dto, eoId, jobId);

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        //调用继续返修通用主方法,继续返修只能单个条码
        HmeEoJobSnVO24 hmeEoJobSnVO24 = new HmeEoJobSnVO24();
        hmeEoJobSnVO24.setUserId(userId);
        hmeEoJobSnVO24.setReworkEo(validateResult.getReworkEo());
        hmeEoJobSnVO24.setReworkMaterialLot(validateResult.getMaterialLotList().get(0));
        hmeEoJobSnVO24.setHmeEoJobSnEntity(validateResult.getHmeEoJobSnEntityList().get(0));
        hmeEoJobSnCommonService.batchMainOutSiteForRework(tenantId, dto, hmeEoJobSnVO24);
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.continueRework.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
    }

    /**
     *
     * @Description 时效SN扫描
     *
     * @author yuchao.wang
     * @date 2021/1/26 15:01
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobTimeSnVO2
     *
     */
    @Override
    public HmeEoJobTimeSnVO2 timeSnScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.timeSnScan.Start tenantId={},dto={}", tenantId, dto);
        //非空校验
        if (StringUtils.isBlank(dto.getSnNum())) {
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        }
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "站点ID"));
        }
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位ID"));
        }

        // 获取当前用户ID
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = Objects.isNull(curUser) ? -1L : curUser.getUserId();

        //查询条码是容器还是物料批
        MtMaterialLotVO30 materialLotVo30 = new MtMaterialLotVO30();
        materialLotVo30.setCode(dto.getSnNum());
        materialLotVo30.setAllLevelFlag(HmeConstants.ConstantValue.YES);
        MtMaterialLotVO29 isContainerVO = mtMaterialLotRepository.codeOrIdentificationLimitObjectGet(tenantId, materialLotVo30);
        if (HmeConstants.LoadTypeCode.CONTAINER.equals(isContainerVO.getCodeType())) {
            //时效返修作业平台不支持容器扫描,请按照SN编码单独扫描!
            throw new MtException("HME_EO_JOB_SN_174", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_174", "HME", dto.getSnNum()));
        } else if (!HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(isContainerVO.getCodeType())) {
            //此SN条码无效则报错：当前扫描条码不存在在制信息
            throw new MtException("HME_EO_JOB_TIME_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_001", "HME"));
        }
        dto.setMaterialLotId(isContainerVO.getCodeId());

        //校验当前条码是否仅维护了一个返修EO
        int reworkEoCount = hmeEoJobSnMapper.queryReworkEoCount(tenantId, dto.getSnNum());
        if (reworkEoCount > 1) {
            //当前条码维护多条返修执行作业,请检查!
            throw new MtException("HME_EO_JOB_SN_179", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_179", "HME"));
        }

        //查询条码涉及到的作业
        SecurityTokenHelper.close();
        List<HmeEoJobSn> allEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, dto.getJobType())
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, dto.getMaterialLotId()))
                .build());

        Optional<HmeEoJobSn> notOutSiteOptional = allEoJobSnList.stream().filter(item -> Objects.isNull(item.getSiteOutDate())).findAny();
        if (HmeConstants.InOutType.IN.equals(dto.getInOutType())) {
            // 当前操作为入炉的情况：
            if (notOutSiteOptional.isPresent()) {
                // 报错：该SN已入炉
                throw new MtException("HME_EO_JOB_TIME_SN_002", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_002", "HME"));
            }
        } else {
            // 当前操作为出炉的情况：
            if (!notOutSiteOptional.isPresent()) {
                // 报错：该SN未入炉
                throw new MtException("HME_EO_JOB_TIME_SN_003", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_003", "HME"));
            }
        }

        //查询条码相关信息并校验
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        HmeEoJobSnVO5 snVO = null;
        if (newMaterialLotFlag) {
            snVO = hmeEoJobSnMapper.queryMaterialLotInfoForRework3(tenantId, dto.getMaterialLotId(), dto.getSiteId());
            // 20210914 add by 增加校验 新条码的话 如果旧条码进行返修 则不允许新条码进站
            List<MtExtendAttrVO> attrVOList = hmeEoJobSnCommonService.queryOldCodeAttrList(tenantId, snVO.getEoId());
            Optional<MtExtendAttrVO> oldBarcodeInflagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("OLD_BARCODE_IN_FLAG", attr.getAttrName())).findFirst();
            if (oldBarcodeInflagOpt.isPresent() && YES.equals(oldBarcodeInflagOpt.get().getAttrValue())) {
                // 已由旧条码进站,不允许用新条码进站
                throw new MtException("HME_EO_JOB_SN_241", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_241", "HME"));
            }
            // 20210914 modiy by sanfeng.zhang for wenxin.zhang 新条码 返修标识取旧条码上的
            Optional<MtExtendAttrVO> reworkFlagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("REWORK_FLAG", attr.getAttrName())).findFirst();
            String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getAttrValue() : "";
            snVO.setReworkFlag(reworkFlag);
        } else {
            snVO = hmeEoJobSnMapper.queryMaterialLotInfoForRework(tenantId, dto.getMaterialLotId(), dto.getSiteId());
        }

        materialLotInfoValidate(tenantId, snVO);

        HmeEoJobTimeSnVO3 snLine = new HmeEoJobTimeSnVO3();
        BeanUtils.copyProperties(snVO, snLine);
        snLine.setSnType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
        snLine.setSiteInBy(userId);
        snLine.setSiteInByName(userClient.userInfoGet(tenantId, userId).getRealName());
        if (HmeConstants.InOutType.OUT.equals(dto.getInOutType())) {
            //出炉的情况给定工序作业ID
            HmeEoJobSn existsSnLine = notOutSiteOptional.get();
            snLine.setJobId(existsSnLine.getJobId());
            snLine.setOperationId(existsSnLine.getOperationId());
            snLine.setEoStepId(existsSnLine.getEoStepId());
            snLine.setSiteInDate(existsSnLine.getSiteInDate());
        }

        //标准时长获取
        HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
        hmeEoJobSnVO3Para.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSnVO3Para.setEoId(snLine.getEoId());
        hmeEoJobSnVO3Para.setOperationId(dto.getOperationId());
        hmeEoJobSnVO3Para.setWorkOrderId(snVO.getWorkOrderId());
        hmeEoJobSnVO3Para.setMaterialId(snLine.getMaterialId());
        hmeEoJobSnVO3Para.setMaterialCode(snLine.getMaterialCode());
        hmeEoJobSnVO3Para.setSnNum(snLine.getMaterialLotCode());
        BigDecimal preStandardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
        snLine.setStandardReqdTimeInProcess(preStandardReqdTimeInProcess);

        //构造返回参数
        HmeEoJobTimeSnVO2 resultVO = new HmeEoJobTimeSnVO2();
        resultVO.setSnType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
        resultVO.setSiteInBy(userId);
        resultVO.setSiteInByName(userClient.userInfoGet(tenantId, userId).getRealName());
        resultVO.setSumEoCount(1);
        resultVO.setReworkFlag(HmeConstants.ConstantValue.YES);
        resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.YES);
        resultVO.setLineList(Collections.singletonList(snLine));

        //获取当前步骤
        HmeRouterStepVO5 currentStep = hmeEoJobSnCommonService.queryCurrentStepByEoAndOperation(tenantId, snVO.getEoId(), dto.getOperationId());

        //如果非首尾步骤不允许点加工完成
        // 20210529 modify by sanfeng.zhang for fang.pan 仅完工步骤允许加工完成
        if (StringUtils.isBlank(currentStep.getRouterDoneStepId())) {
            resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.NO);
        }
        // 20210529 modify by sanfeng.zhang for fang.pan 进站时新增校验，如果eo的当前步骤为完成步骤，同时当前eo的工单号在拆机表中存在 则报错
        if (StringUtils.isNotBlank(currentStep.getRouterDoneStepId())){
            HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, snVO.getWorkOrderNum());
            if (Objects.nonNull(splitRecord)) {
                throw new MtException("HME_SPLIT_RECORD_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SPLIT_RECORD_0029", "HME"));
            }
        }
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.timeSnScan.End resultVO={}", resultVO);
        return resultVO;
    }

    /**
     *
     * @Description 校验条码是否满足时效返修平台条件
     *
     * @author yuchao.wang
     * @date 2021/1/26 17:04
     * @param tenantId 租户ID
     * @param snVO 参数
     * @return void
     *
     */
    private void materialLotInfoValidate(Long tenantId, HmeEoJobSnVO5 snVO) {
        Set<String> repairTypes = lovAdapter.queryLovValue("HME.REPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> unrepairedTypes = lovAdapter.queryLovValue("HME.UNREPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        //非空校验
        if (Objects.isNull(snVO)) {
            //所扫描条码不存在或不为有效状态,请核实!
            throw new MtException("HME_NC_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0009", "HME"));
        }
        //校验工单类型
        if (StringUtils.isBlank(snVO.getEoId()) || !repairTypes.contains(snVO.getWorkOrderType())) {
            //当前条码未生成返修工单，无法进行返修
            throw new MtException("HME_EO_JOB_SN_148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_148", "HME"));
        }
        //验证WO状态
        if (!HmeConstants.WorkOrderStatus.RELEASED.equals(snVO.getWorkOrderStatus())
                && !HmeConstants.WorkOrderStatus.EORELEASED.equals(snVO.getWorkOrderStatus())) {
            //当前条码的返修工单不为EO下达状态
            throw new MtException("HME_EO_JOB_SN_175", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_175", "HME"));
        }
        //验证EO状态
        if (!HmeConstants.EoStatus.WORKING.equals(snVO.getStatus())
                || (!HmeConstants.EoStatus.RELEASED.equals(snVO.getLastEoStatus())
                && !HmeConstants.EoStatus.HOLD.equals(snVO.getLastEoStatus()))) {
            //EO状态必须为运行状态
            throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
        }

        //是否返修
        boolean isRework = HmeConstants.ConstantValue.YES.equals(snVO.getReworkFlag());
        if (!unrepairedTypes.contains(snVO.getWorkOrderType()) && !isRework) {
            //非返修状态条码不允许进行进站操作
            throw new MtException("HME_EO_JOB_SN_161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_161", "HME"));
        }
    }

    /**
     *
     * @Description 分页查询炉内条码
     *
     * @author yuchao.wang
     * @date 2020/11/17 10:10
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return com.ruike.hme.domain.vo.HmeEoJobTimeSnVO4
     *
     */
    @Override
    public HmeEoJobTimeSnVO4 queryPageTimeSnByWorkcell(Long tenantId, HmeEoJobSnDTO dto, PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnTimeReworkServiceImpl.queryPageTimeSnByWorkcell tenantId={},dto={}", tenantId, dto);
        //非空校验
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位ID"));
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        }
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "站点ID"));
        }

        List<HmeEoJobTimeSnVO3> lineList = new ArrayList<>();
        //拼接作业查询SQL
        Sqls eoJobSnSql = Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.REPAIR_TIME_PROCESS)
                .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE);
        if (StringUtils.isNotBlank(dto.getBarcode())) {
            //根据条码查询ID，拼接查询参数
            SecurityTokenHelper.close();
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                    .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getBarcode())).build());

            if (CollectionUtils.isEmpty(mtMaterialLots) || Objects.isNull(mtMaterialLots.get(0)) || StringUtils.isBlank(mtMaterialLots.get(0).getMaterialLotId())) {
                return new HmeEoJobTimeSnVO4(new ArrayList<HmeEoJobTimeSnVO3>(), pageRequest, 0, 0, 0);
            }

            eoJobSnSql = eoJobSnSql.andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, StringUtils.trimToEmpty(mtMaterialLots.get(0).getMaterialLotId()));
        }
        SecurityTokenHelper.close();
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(eoJobSnSql)
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());

        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            return new HmeEoJobTimeSnVO4(new ArrayList<HmeEoJobTimeSnVO3>(), pageRequest, 0, 0, 0);
        }

        //批量查询所有条码信息
        Map<String, HmeEoJobSnVO5> materialLotMap = new HashMap<String, HmeEoJobSnVO5>();
        List<String> materialLotIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getMaterialLotId).distinct().collect(Collectors.toList());

        List<HmeEoJobSnVO5> materialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoForRework(tenantId, dto.getSiteId(), materialLotIdList);
        materialLotList.forEach(item -> materialLotMap.put(item.getMaterialLotId(), item));

        //再筛选条码数据
        hmeEoJobSnList.forEach(jobSn -> {
            HmeEoJobSnVO5 materialLot = materialLotMap.getOrDefault(jobSn.getMaterialLotId(), new HmeEoJobSnVO5());

            HmeEoJobTimeSnVO3 jobSnVO = new HmeEoJobTimeSnVO3();
            jobSnVO.setSnType("MATERIAL_LOT");
            jobSnVO.setMaterialLotId(jobSn.getMaterialLotId());
            jobSnVO.setMaterialLotCode(materialLot.getMaterialLotCode());
            jobSnVO.setSiteInDate(jobSn.getSiteInDate());
            jobSnVO.setSiteInBy(jobSn.getSiteInBy());
            jobSnVO.setSiteInByName(userClient.userInfoGet(tenantId, jobSn.getSiteInBy()).getRealName());
            jobSnVO.setJobId(jobSn.getJobId());
            jobSnVO.setJobContainerId(jobSn.getJobContainerId());
            jobSnVO.setSumEoQty(materialLot.getEoQty());
            jobSnVO.setMaterialCode(materialLot.getMaterialCode());
            jobSnVO.setMaterialName(materialLot.getMaterialName());
            jobSnVO.setReworkFlag(materialLot.getReworkFlag());

            //获取标准工艺时长
            HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
            hmeEoJobSnVO3Para.setWorkcellId(jobSn.getWorkcellId());
            hmeEoJobSnVO3Para.setEoId(jobSn.getEoId());
            hmeEoJobSnVO3Para.setOperationId(jobSn.getOperationId());
            hmeEoJobSnVO3Para.setWorkOrderId(materialLot.getWorkOrderId());
            hmeEoJobSnVO3Para.setMaterialId(materialLot.getMaterialId());
            hmeEoJobSnVO3Para.setMaterialLotId(jobSn.getMaterialLotId());
            hmeEoJobSnVO3Para.setSnNum(materialLot.getMaterialLotCode());
            BigDecimal standardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
            if (Objects.nonNull(standardReqdTimeInProcess)) {
                jobSnVO.setStandardReqdTimeInProcess(standardReqdTimeInProcess);
            }
            lineList.add(jobSnVO);
        });

        //没有数据就返回
        if (CollectionUtils.isEmpty(lineList)) {
            return new HmeEoJobTimeSnVO4(new ArrayList<HmeEoJobTimeSnVO3>(), pageRequest, 0, 0, 0);
        }

        //按照进炉时间排序：容器在前，先入炉的在前
        List<HmeEoJobTimeSnVO3> pageList = lineList.stream().sorted(Comparator
                .comparing(HmeEoJobTimeSnVO3::getSiteInDate)).collect(Collectors.toList());

        //计算获取子串开始结束位置
        int fromIndex = pageRequest.getPage() * pageRequest.getSize();
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), lineList.size());
        return new HmeEoJobTimeSnVO4(pageList.subList(fromIndex, toIndex), pageRequest, lineList.size(), 0, lineList.size());
    }

    /**
     *
     * @Description 构造插入数据
     *
     * @author yuchao.wang
     * @date 2020/11/4 11:15
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param currentDate 当前时间
     * @param dto 参数
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn getSnJobEntity(Long tenantId, Long userId, Date currentDate, String Id, String Cid, HmeEoJobSnVO3 dto) {
        HmeEoJobSn snJob = new HmeEoJobSn();
        snJob.setJobId(Id);
        snJob.setCid(Long.parseLong(Cid));
        snJob.setTenantId(tenantId);
        snJob.setEoId(dto.getEoId());
        snJob.setSiteInDate(currentDate);
        snJob.setShiftId(dto.getWkcShiftId());
        snJob.setSiteInBy(userId);
        snJob.setWorkcellId(dto.getWorkcellId());
        snJob.setWorkOrderId(dto.getWorkOrderId());
        snJob.setOperationId(dto.getOperationId());
        snJob.setMaterialLotId(dto.getMaterialLotId());
        snJob.setSnMaterialId(dto.getMaterialId());
        snJob.setJobType(dto.getJobType());
        snJob.setEoStepId(dto.getEoStepId());
        snJob.setReworkFlag(dto.getReworkFlag());

        //V20210312 modify by penglin.sui for tianyang.xie 返修进站时，按tenant_id+eo_id+workcell_id+operation_id+rework_flag+job_type+material_lot_id取最大的eo_step_num+1没有为0
        if(HmeConstants.ConstantValue.YES.equals(dto.getReworkFlag())){
            int maxEoStepNum = hmeEoJobSnMapper.queryReworkMaxEoStepNum(tenantId,snJob);
            snJob.setEoStepNum(maxEoStepNum + 1);
        }else{
            snJob.setEoStepNum(dto.getEoStepNum());
        }

        snJob.setSourceContainerId(dto.getSourceContainerId());
        snJob.setJobContainerId(dto.getJobContainerId());
        snJob.setObjectVersionNumber(1L);
        snJob.setCreatedBy(userId);
        snJob.setCreationDate(currentDate);
        snJob.setLastUpdatedBy(userId);
        snJob.setLastUpdateDate(currentDate);
        return snJob;
    }

}
