package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeCosReturnScanDTO;
import com.ruike.hme.app.service.HmeCosMaterialReturnService;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeCosOperationRecordHis;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmeMaterialLotReturn;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosMaterialReturnMapper;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsMaterialLotMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.message.util.DateUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO7;
import tarzan.actual.domain.vo.MtWoComponentActualVO2;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.infra.mapper.MtWorkOrderMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static com.ruike.hme.infra.constant.HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R;
import static com.ruike.hme.infra.constant.HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT;

/**
 * @ClassName HmeCOSMaterialReturnServiceImpl
 * @Description COS退料
 * @Author lkj
 * @Date 2020/12/11
 */
@Service
public class HmeCosMaterialReturnServiceImpl implements HmeCosMaterialReturnService {

    private final HmeCosMaterialReturnMapper hmeCosMaterialReturnMapper;
    private final MtWorkOrderMapper mtWorkOrderMapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final HmeMaterialTransferRepository hmeMaterialTransferRepository;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final LovAdapter lovAdapter;
    private final MtMaterialRepository mtMaterialRepository;
    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtEventRepository mtEventRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final MtModWorkcellRepository mtModWorkcellRepository;
    private final MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    private final HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    private final MtNumrangeRepository mtNumrangeRepository;
    private final HmeMaterialLotReturnRepository hmeMaterialLotReturnRepository;
    private final MtModLocatorRepository mtModLocatorRepository;
    private final MtModProductionLineRepository mtModProductionLineRepository;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final MtBomComponentRepository mtBomComponentRepository;
    private final WmsMaterialLotMapper wmsMaterialLotMapper;
    private final HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    private final MtCustomDbRepository mtCustomDbRepository;
    private final JdbcTemplate jdbcTemplate;
    private final MtOperationRepository mtOperationRepository;
    private final MtNcCodeRepository mtNcCodeRepository;
    private final HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;


    public HmeCosMaterialReturnServiceImpl(HmeCosMaterialReturnMapper hmeCosMaterialReturnMapper, MtWorkOrderMapper mtWorkOrderMapper,
                                           MtErrorMessageRepository mtErrorMessageRepository, HmeMaterialTransferRepository hmeMaterialTransferRepository,
                                           MtExtendSettingsRepository mtExtendSettingsRepository, LovAdapter lovAdapter,
                                           MtMaterialRepository mtMaterialRepository, MtEventRequestRepository mtEventRequestRepository,
                                           MtEventRepository mtEventRepository, MtMaterialLotRepository mtMaterialLotRepository,
                                           MtModWorkcellRepository mtModWorkcellRepository, MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository,
                                           WmsObjectTransactionRepository wmsObjectTransactionRepository, HmeCosOperationRecordRepository hmeCosOperationRecordRepository,
                                           MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository, MtNumrangeRepository mtNumrangeRepository, HmeMaterialLotReturnRepository hmeMaterialLotReturnRepository,
                                           MtModLocatorRepository mtModLocatorRepository, MtModProductionLineRepository mtModProductionLineRepository, WmsTransactionTypeRepository wmsTransactionTypeRepository,
                                           MtBomComponentRepository mtBomComponentRepository, WmsMaterialLotMapper wmsMaterialLotMapper, HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository, MtCustomDbRepository mtCustomDbRepository, JdbcTemplate jdbcTemplate, MtOperationRepository mtOperationRepository, MtNcCodeRepository mtNcCodeRepository, HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository) {
        this.hmeCosMaterialReturnMapper = hmeCosMaterialReturnMapper;
        this.mtWorkOrderMapper = mtWorkOrderMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.hmeMaterialTransferRepository = hmeMaterialTransferRepository;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.lovAdapter = lovAdapter;
        this.mtMaterialRepository = mtMaterialRepository;
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.mtEventRepository = mtEventRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.mtModWorkcellRepository = mtModWorkcellRepository;
        this.mtWorkOrderComponentActualRepository = mtWorkOrderComponentActualRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.mtInvOnhandQuantityRepository = mtInvOnhandQuantityRepository;
        this.mtNumrangeRepository = mtNumrangeRepository;
        this.hmeMaterialLotReturnRepository = hmeMaterialLotReturnRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.mtModProductionLineRepository = mtModProductionLineRepository;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.mtBomComponentRepository = mtBomComponentRepository;
        this.hmeCosOperationRecordRepository = hmeCosOperationRecordRepository;
        this.wmsMaterialLotMapper = wmsMaterialLotMapper;
        this.hmeMaterialLotLoadRepository = hmeMaterialLotLoadRepository;
        this.mtCustomDbRepository = mtCustomDbRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.mtOperationRepository = mtOperationRepository;
        this.mtNcCodeRepository = mtNcCodeRepository;
        this.hmeCosOperationRecordHisRepository = hmeCosOperationRecordHisRepository;
    }

    private static final String HOT_SINK = "HOT_SINK";
    private static final String WIRE_BOND = "WIRE_BOND";
    private static final String COS_RETURN = "COS_RETURN";

    /**
     * <strong>Title : scanWorkOrderNum</strong><br/>
     * <strong>Description :  COS退料-工单扫描</strong><br/>
     * <strong>Create on : 2020/12/11 上午10:28</strong><br/>
     *
     * @param tenantId
     * @param workOrderId
     * @return com.ruike.hme.domain.vo.HmeCOSMaterialReturnVO
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     *
     * </p>
     */
    @Override
    @ProcessLovValue
    public HmeCosMaterialReturnVO scanWorkOrderNum(Long tenantId, String workOrderId) {
        //校验参数
        if (StringUtils.isBlank(workOrderId)) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", "HME", "工单ID"));
        }
        MtWorkOrder mtWorkOrder = new MtWorkOrder();
        mtWorkOrder.setWorkOrderId(workOrderId);
        List<MtWorkOrder> status = mtWorkOrderMapper.select(mtWorkOrder);
        if (CollectionUtils.isEmpty(status)) {
            throw new MtException("HME_COS_MATERIAL_RETURN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_002", "HME"));
        }
        if (!"RELEASED".equals(status.get(0).getStatus()) && !"EORELEASED".equals(status.get(0).getStatus())) {
            throw new MtException("HME_COS_MATERIAL_RETURN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_001", "HME", status.get(0).getWorkOrderNum()));
        }
        // 查询COS工单信息
        HmeCosMaterialReturnVO vo = hmeCosMaterialReturnMapper.scanWorkOrderNum(workOrderId);

        //查询组件可退料数量
        List<HmeCosMaterialReturnLineVO> lineVO = hmeCosMaterialReturnMapper.selectCosBomLine(workOrderId);
        vo.setBomComment(lineVO);
        return vo;
    }

    @Override
    public HmeCosScanBarcodeVO scanMaterialLot(Long tenantId, HmeCosReturnScanDTO dto) {
        //校验参数
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", "HME", "退料条码"));
        }

        if (StringUtils.isBlank(dto.getWorkOrderId())) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", "HME", "工单ID"));
        }
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotCode());
        if (materialLot == null) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }

        HmeCosScanBarcodeVO barcodeVO = new HmeCosScanBarcodeVO();
        String materialId = dto.getMaterialId();
        if (StringCommonUtils.contains(dto.getReturnType(), HOT_SINK, WIRE_BOND)) {
            materialId = mtWorkOrderMapper.selectByPrimaryKey(dto.getWorkOrderId()).getMaterialId();
        }

        //校验条码
        verifyMaterialLot(tenantId, materialLot, dto.getWorkOrderId(), barcodeVO, materialId, dto.getReturnType());

        barcodeVO.setMaterialLotId(materialLot.getMaterialLotId());
        barcodeVO.setMaterialLotCode(materialLot.getMaterialLotCode());
        barcodeVO.setQty(materialLot.getPrimaryUomQty());
        barcodeVO.setEnableFlag(materialLot.getEnableFlag());
        barcodeVO.setSupplierId(materialLot.getSupplierId());

        // 当退料类型为热沉/金线时，附加额外的查询结果
        if (StringCommonUtils.contains(dto.getReturnType(), HOT_SINK, WIRE_BOND)) {
            // 判断是否替代料
            List<String> materialSubstituteList = hmeCosMaterialReturnMapper.queryMaterialSubstitute(tenantId, materialLot.getMaterialLotId());
            List<HmeCosReturnScanLineVO> hotSinkList = new ArrayList<>();
            MtBomComponent mtBomComponent = null;
            if (CollectionUtils.isNotEmpty(materialSubstituteList)) {
                mtBomComponent = hmeCosMaterialReturnMapper.queryBomComponentByMaterialAndBomId(tenantId, dto.getWorkOrderId(), materialSubstituteList);
                hotSinkList = hmeCosMaterialReturnMapper.selectHotSinkListByMaterialLot(tenantId, dto.getWorkOrderId(), materialLot.getMaterialLotId());
            } else {
                mtBomComponent = hmeCosMaterialReturnMapper.queryBomComponentByMaterialLotIdAndBomId(tenantId, dto.getWorkOrderId(), materialLot.getMaterialLotId());
                hotSinkList = hmeCosMaterialReturnMapper.selectHotSinkListByMaterialLot2(tenantId, dto.getWorkOrderId(), materialLot.getMaterialLotId());
            }
            if (mtBomComponent != null) {
                BigDecimal usageQty = mtBomComponent.getQty() != null ? BigDecimal.valueOf(mtBomComponent.getQty()) : BigDecimal.ONE;
                for (HmeCosReturnScanLineVO hmeCosReturnScanLineVO : hotSinkList) {
                    hmeCosReturnScanLineVO.setUsageQty(usageQty);
                    hmeCosReturnScanLineVO.setReturnQty(hmeCosReturnScanLineVO.getTotalCosNum().multiply(usageQty));
                }
                barcodeVO.setHotSinkList(hotSinkList);
            }
        }

        //芯片退料 也显示芯片信息 查询组件可退料数量
        List<HmeCosReturnScanLineVO> cosReturnList = hmeCosMaterialReturnMapper.selectCosReturnByMaterialLot(tenantId, dto.getWorkOrderId(), dto.getMaterialId());
        // 退料数量 根据退料条码装载数量*单位用量
        Long countNum = hmeCosMaterialReturnMapper.queryCosNumByMaterialLotId(tenantId, materialLot.getMaterialLotId());
        cosReturnList.forEach(cos -> {
            cos.setReturnQty(BigDecimal.valueOf(countNum).multiply(cos.getUsageQty()));
        });
        barcodeVO.setCosReturnList(cosReturnList);

        if (StringCommonUtils.contains(dto.getReturnType(), WIRE_BOND)) {
            List<HmeCosReturnScanLineVO> wireBondList = hmeCosMaterialReturnMapper.selectWireBondListByMaterialLot(tenantId, dto.getWorkOrderId(), materialLot.getMaterialLotId());
            barcodeVO.setWireBondList(wireBondList);
        }

        barcodeVO.setHotSinkList(CollectionUtils.isEmpty(barcodeVO.getHotSinkList()) ? new ArrayList<>() : barcodeVO.getHotSinkList());
        barcodeVO.setWireBondList(CollectionUtils.isEmpty(barcodeVO.getWireBondList()) ? new ArrayList<>() : barcodeVO.getWireBondList());
        barcodeVO.setReturnType(dto.getReturnType());
        return barcodeVO;
    }

    @Override
    public MtMaterialLot scanTargetMaterialLot(Long tenantId, String materialLotCode) {
        if (StringUtils.isBlank(materialLotCode)) {
            return new MtMaterialLot();
        }
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);
        if (materialLot == null) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }

        if (StringUtils.equals(materialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            throw new MtException("HME_COS_MATERIAL_RETURN_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_009", "HME", materialLotCode));
        }
        return materialLot;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosMaterialReturnVO2 cosMaterialReturn(Long tenantId, HmeCosMaterialReturnVO3 returnVO3) {
        //校验目标条码
        if (StringUtils.isNotBlank(returnVO3.getTargetMaterialLotCode())) {
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, returnVO3.getTargetMaterialLotCode());
            if (materialLot == null) {
                throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0003", "HME"));
            }

            if (StringUtils.equals(materialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
                throw new MtException("HME_COS_MATERIAL_RETURN_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_MATERIAL_RETURN_009", "HME", returnVO3.getTargetMaterialLotCode()));
            }
        }

        //生成工单退料事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WORK_ORDER_RETURN");
        //调用API【eventCreate】 生成工单退料事件
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode("WORK_ORDER_RETURN");
        event.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, event);

        //工单
        MtWorkOrder mtWorkOrder = mtWorkOrderMapper.selectByPrimaryKey(returnVO3.getWorkOrderId());

        List<String> locatorIdList = hmeCosMaterialReturnMapper.queryLocatorIdByWorkcellId(tenantId, returnVO3.getWorkcellId());
        if (CollectionUtils.isEmpty(locatorIdList) || locatorIdList.size() > 1) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(returnVO3.getWorkcellId());
            throw new MtException("HME_COS_MATERIAL_RETURN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_008", "HME", mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : ""));
        }
        String locatorId = locatorIdList.get(0);
        //根据号码段生成批次
        MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
        mtNumrange.setObjectCode("RECEIPT_BATCH");
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange);
        String lot = mtNumrangeVO5 != null ? mtNumrangeVO5.getNumber() : null;
        String wafer = returnVO3.getBarcodeVOList().get(0).getWaferNum();
        String returnType = returnVO3.getBarcodeVOList().get(0).getReturnType();
        List<HmeCosReturnScanLineVO> cosReturnList = new ArrayList<>();
        returnVO3.getBarcodeVOList().forEach(barcode -> {
            if (CollectionUtils.isEmpty(returnVO3.getHotSinkList())) {
                //校验条码物料与行物料一致
                if (!StringUtils.equals(barcode.getMaterialId(), returnVO3.getMaterialId())) {
                    throw new MtException("HME_COS_MATERIAL_RETURN_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_MATERIAL_RETURN_010", "HME", barcode.getMaterialLotCode()));
                }
            }
            //条码清单内的条码进行失效
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                setEventId(eventId);
                setMaterialLotId(barcode.getMaterialLotId());
                setEnableFlag(NO);
                setPrimaryUomQty(0D);
            }}, "N");

            if (CollectionUtils.isNotEmpty(barcode.getCosReturnList())) {
                cosReturnList.addAll(barcode.getCosReturnList());
            }
        });
        if ("CHIP".equals(returnType)) {
            if (CollectionUtils.isNotEmpty(cosReturnList)) {
                // 如果是芯片退料 则组装数据 默认退料 芯片物料相同的 则累加数量 芯片退料 没有热沉和金线 故忽略
                Map<String, List<HmeCosReturnScanLineVO>> cosReturnMap = cosReturnList.stream().collect(Collectors.groupingBy(HmeCosReturnScanLineVO::getMaterialId));
                List<HmeCosReturnScanLineVO> groupCosReturnList = new ArrayList<>();
                cosReturnMap.forEach((cosKey, cosValue) -> {
                    HmeCosReturnScanLineVO cosReturnScanLineVO = cosValue.get(0);
                    cosReturnScanLineVO.setCosReturnFlag(YES);
                    // 芯片退料 直接取退料数量
                    cosReturnScanLineVO.setReturnQty(returnVO3.getBackQty());
                    groupCosReturnList.add(cosReturnScanLineVO);
                });
                returnVO3.setCosReturnList(groupCosReturnList);
            }
        }
        // 查询退料条码的金线物料 热沉物料
        String hotSinkMaterial = "";
        String wireMaterialId = "";
        //2021-06-28 14:21 add by chaonan.hu for peng.zhao 增加修改物料批装载表信息的逻辑
        if(CollectionUtils.isNotEmpty(returnVO3.getBarcodeVOList())){
            List<String> materialLotIdList = returnVO3.getBarcodeVOList().stream().map(HmeCosScanBarcodeVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                            .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
            if(CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)){
                Map<String, List<HmeMaterialLotLoad>> groupMaterialLoadMap = hmeMaterialLotLoadList.stream().collect(Collectors.groupingBy(HmeMaterialLotLoad::getMaterialLotId));
                List<String> hotSinkMaterialIdList = new ArrayList<>();
                List<String> wireMaterialIdList = new ArrayList<>();
                groupMaterialLoadMap.forEach((loadKey, loadVal) -> {
                    Optional<HmeMaterialLotLoad> hotSinkOpt = loadVal.stream().filter(vo -> StringUtils.isNotBlank(vo.getAttribute4())).findFirst();
                    if (hotSinkOpt.isPresent()) {
                        if (StringUtils.isNotBlank(hotSinkOpt.get().getAttribute11())) {
                            if (!hotSinkMaterialIdList.contains(hotSinkOpt.get().getAttribute11())) {
                                hotSinkMaterialIdList.add(hotSinkOpt.get().getAttribute11());
                            }
                        }
                    }
                    Optional<HmeMaterialLotLoad> wireBondOpt = loadVal.stream().filter(vo -> StringUtils.isNotBlank(vo.getAttribute7())).findFirst();
                    if (wireBondOpt.isPresent()) {
                        if (StringUtils.isNotBlank(hotSinkOpt.get().getAttribute12())) {
                            if (!wireMaterialIdList.contains(hotSinkOpt.get().getAttribute12())) {
                                wireMaterialIdList.add(hotSinkOpt.get().getAttribute12());
                            }
                        }
                    }
                });
                if (CollectionUtils.isNotEmpty(returnVO3.getHotSinkList())) {
                    if (CollectionUtils.isEmpty(hotSinkMaterialIdList) || hotSinkMaterialIdList.size() > 1) {
                        throw new MtException("HME_COS_MATERIAL_RETURN_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_MATERIAL_RETURN_017", "HME", "热沉"));
                    }
                    hotSinkMaterial = hotSinkMaterialIdList.get(0);
                }
                if (CollectionUtils.isNotEmpty(returnVO3.getWireBondList())) {
                    if (CollectionUtils.isEmpty(wireMaterialIdList) || wireMaterialIdList.size() > 1) {
                        throw new MtException("HME_COS_MATERIAL_RETURN_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_MATERIAL_RETURN_017", "HME", "金线"));
                    }
                    wireMaterialId = wireMaterialIdList.get(0);
                }
                CustomUserDetails curUser = DetailsHelper.getUserDetails();
                Date now = new Date();
                Long userId = curUser == null ? -1L : curUser.getUserId();
                List<String> sqlList = new ArrayList<>();
                for (HmeMaterialLotLoad hmeMaterialLotLoad:hmeMaterialLotLoadList) {
                    hmeMaterialLotLoad.setMaterialLotId("");
                    hmeMaterialLotLoad.setSourceLoadRow(hmeMaterialLotLoad.getLoadRow());
                    hmeMaterialLotLoad.setSourceLoadColumn(hmeMaterialLotLoad.getLoadColumn());
                    hmeMaterialLotLoad.setLastUpdatedBy(userId);
                    hmeMaterialLotLoad.setLastUpdateDate(now);
                    hmeMaterialLotLoad.setObjectVersionNumber(hmeMaterialLotLoad.getObjectVersionNumber() + 1);
                    sqlList.addAll(mtCustomDbRepository.getUpdateSql(hmeMaterialLotLoad));
                }
                if(CollectionUtils.isNotEmpty(sqlList)){
                    jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                }
            }
        }

        //记录退料信息
        HmeCosOperationRecord record = new HmeCosOperationRecord();
        record.setTenantId(tenantId);
        record.setSiteId(mtWorkOrder.getSiteId());
        record.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        //取最早的芯片类型
        List<HmeCosOperationRecord> recordList = hmeCosOperationRecordRepository.selectByCondition(Condition.builder(HmeCosOperationRecord.class)
                .andWhere(Sqls.custom().andEqualTo(HmeCosOperationRecord.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeCosOperationRecord.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                        .andEqualTo(HmeCosOperationRecord.FIELD_MATERIAL_ID, returnVO3.getMaterialId())).orderByAsc(HmeCosOperationRecord.FIELD_CREATION_DATE).build());
        String cosType = CollectionUtils.isNotEmpty(recordList) ? recordList.get(0).getCosType() : "";
        String operationId = CollectionUtils.isNotEmpty(recordList) ? recordList.get(0).getOperationId() : "";
        String workcellId = CollectionUtils.isNotEmpty(recordList) ? recordList.get(0).getWorkcellId() : "";
        record.setCosType(cosType);
        record.setBarNum(BigDecimal.ZERO.longValue());
        record.setCosNum(BigDecimal.ZERO.subtract(returnVO3.getBackQty()).longValue());
        record.setOperationId(operationId);
        record.setWorkcellId(workcellId);
        record.setSurplusCosNum(0L);
        record.setMaterialId(returnVO3.getMaterialId());
        hmeCosOperationRecordRepository.insertSelective(record);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(record, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        //净需求数量
        BigDecimal demandQty = BigDecimal.ZERO;
        //获取bomComponentId(bomId+物料+行号)
        List<MtBomComponent> bomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
            setTenantId(tenantId);
            setBomId(mtWorkOrder.getBomId());
            setMaterialId(returnVO3.getMaterialId());
            setLineNumber(Long.valueOf(returnVO3.getLineNumber()));
        }});
        if (CollectionUtils.isNotEmpty(bomComponentList)) {
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute4");
            mtExtendVO.setKeyId(bomComponentList.get(0).getBomComponentId());
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            demandQty = CollectionUtils.isNotEmpty(mtExtendAttrVOList) ? new BigDecimal(mtExtendAttrVOList.get(0).getAttrValue()) : BigDecimal.ZERO;
        }
        // 执行处理装载物料退料
        List<String> materialLotIdList = returnVO3.getBarcodeVOList().stream().map(HmeCosScanBarcodeVO::getMaterialLotId).collect(Collectors.toList());
        // 勾选逻辑处理  如果存在部分勾选 则报错
        String cosReturnFlag = NO;
        String hotSinkFlag = NO;
        String wireBondFlag = NO;
        if (CollectionUtils.isNotEmpty(returnVO3.getCosReturnList())) {
            List<String> filterCosReturnList = returnVO3.getCosReturnList().stream().map(HmeCosReturnScanLineVO::getCosReturnFlag).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filterCosReturnList) || filterCosReturnList.size() > 1) {
                throw new MtException("HME_COS_MATERIAL_RETURN_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_MATERIAL_RETURN_018", "HME"));
            }
            cosReturnFlag = filterCosReturnList.get(0);
        }
        if (CollectionUtils.isNotEmpty(returnVO3.getHotSinkList())) {
            List<String> filterHotSinkList = returnVO3.getHotSinkList().stream().map(HmeCosReturnScanLineVO::getHotSinkFlag).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filterHotSinkList) || filterHotSinkList.size() > 1) {
                throw new MtException("HME_COS_MATERIAL_RETURN_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_MATERIAL_RETURN_018", "HME"));
            }
            hotSinkFlag = filterHotSinkList.get(0);
        }
        if (CollectionUtils.isNotEmpty(returnVO3.getWireBondList())) {
            List<String> filterWireBondList = returnVO3.getWireBondList().stream().map(HmeCosReturnScanLineVO::getWireBondFlag).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filterWireBondList) || filterWireBondList.size() > 1) {
                throw new MtException("HME_COS_MATERIAL_RETURN_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_MATERIAL_RETURN_018", "HME"));
            }
            wireBondFlag = filterWireBondList.get(0);
        }
        // 查询不良代码
        MtNcCode mtNcCode = mtNcCodeRepository.selectOne(new MtNcCode() {{
            setTenantId(tenantId);
            setNcCode(returnVO3.getNcCode());
        }});
        returnVO3.setNcCodeId(mtNcCode.getNcCodeId());
        HmeCosMaterialReturnVO4 hmeCosMaterialReturnVO4 = null;
        if (YES.equals(cosReturnFlag)) {
            hmeCosMaterialReturnVO4 = this.processLoadMaterialReturn2(tenantId, eventId, mtWorkOrder, locatorId, lot, wafer, demandQty, materialLotIdList, returnVO3, mtNcCode.getNcCodeId());
        } else {
            this.processLoadMaterialCosScrapped(tenantId, eventId, mtWorkOrder, wafer, returnVO3);
        }
        if (YES.equals(hotSinkFlag)) {
            this.processLoadMaterialReturn(tenantId, eventId, mtWorkOrder, locatorId, lot, wafer, demandQty, materialLotIdList, returnVO3.getHotSinkList(), hotSinkMaterial, mtNcCode.getNcCodeId(), returnVO3.getWorkcellId());
        } else {
            this.processLoadMaterialHotSinkScrapped(tenantId, eventId, returnVO3);
        }
        if (YES.equals(wireBondFlag)) {
            this.processLoadMaterialReturn(tenantId, eventId, mtWorkOrder, locatorId, lot, wafer, demandQty, materialLotIdList, returnVO3.getWireBondList(), wireMaterialId, mtNcCode.getNcCodeId(), returnVO3.getWorkcellId());
        } else {
            this.processLoadMaterialWireBondReturn(tenantId, eventId, returnVO3);
        }
        HmeCosMaterialReturnVO2 materialReturn = new HmeCosMaterialReturnVO2();
        if (hmeCosMaterialReturnVO4 != null) {
            materialReturn.setTargetMaterialLotId(hmeCosMaterialReturnVO4.getMaterialLotId());
            materialReturn.setTargetMaterialLotCode(hmeCosMaterialReturnVO4.getMaterialLotCode());
        }
        materialReturn.setHotSinkList(returnVO3.getHotSinkList());
        materialReturn.setWireBondList(returnVO3.getWireBondList());
        materialReturn.setCosReturnList(returnVO3.getCosReturnList());
        return materialReturn;
    }

    /**
     * 处理芯片物料报废
     *
     * @param tenantId
     * @param eventId
     * @param mtWorkOrder
     * @param wafer
     * @param returnVO3
     * @author sanfeng.zhang@hand-china.com 2021/9/25 20:58
     * @return void
     */
    private void processLoadMaterialCosScrapped(Long tenantId, String eventId, MtWorkOrder mtWorkOrder, String wafer, HmeCosMaterialReturnVO3 returnVO3) {
        // COS芯片报废时 根据工单 cosType wafer 物料 找到对应的条码
        if (CollectionUtils.isNotEmpty(returnVO3.getCosReturnList())) {
            // 值集
            List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.COS_RETURN_OPERATION", tenantId);
            String operationId = "";
            if (CollectionUtils.isNotEmpty(lovValue) && lovValue.size() > 1) {
                List<MtOperation> mtOperationList = mtOperationRepository.selectByOperationName(tenantId, Collections.singletonList(lovValue.get(0).getValue()));
                if (CollectionUtils.isEmpty(mtOperationList)) {
                    throw new MtException("HME_COS_MATERIAL_RETURN_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_MATERIAL_RETURN_016", "HME", "芯片"));
                }
                operationId = mtOperationList.get(0).getOperationId();
            }
            String routeStepId = hmeCosMaterialReturnMapper.queryRouterStepId(tenantId, mtWorkOrder.getRouterId(), operationId);

            // 根据工单 芯片物料 步骤找到工单装配实绩
            List<MtWorkOrderComponentActual> mtWorkOrderComponentActuals = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, returnVO3.getCosReturnList().get(0).getMaterialId())
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routeStepId)
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_ASSEMBLE_EXCESS_FLAG, NO)).build());
            if (CollectionUtils.isNotEmpty(mtWorkOrderComponentActuals)) {
                MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
                cv2.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                cv2.setMaterialId(returnVO3.getCosReturnList().get(0).getMaterialId());
                cv2.setBomComponentId(mtWorkOrderComponentActuals.get(0).getBomComponentId());
                cv2.setOperationId(mtWorkOrderComponentActuals.get(0).getOperationId());
                cv2.setRouterStepId(mtWorkOrderComponentActuals.get(0).getRouterStepId());
                BigDecimal subtractQty = BigDecimal.valueOf(mtWorkOrderComponentActuals.get(0).getAssembleQty()).subtract(returnVO3.getBackQty());
                cv2.setAssembleQty(subtractQty.doubleValue());
                cv2.setScrappedQty(returnVO3.getBackQty().doubleValue());
                cv2.setComponentType("ASSEMBLING");
                cv2.setAssembleExcessFlag("N");
                cv2.setEventId(eventId);
                mtWorkOrderComponentActualRepository.woComponentActualUpdate(tenantId, cv2);
            }

            // 建立退料条码和目标条码的关系
            List<HmeMaterialLotReturn> relationList = new ArrayList<>();
            returnVO3.getBarcodeVOList().forEach(line -> {
                HmeMaterialLotReturn hmeMaterialLotReturn = new HmeMaterialLotReturn();
                hmeMaterialLotReturn.setTenantId(tenantId);
                hmeMaterialLotReturn.setReturnMaterialLotId(line.getMaterialLotId());
                hmeMaterialLotReturn.setTargetMaterialLotId("-1");
                hmeMaterialLotReturn.setReturnQty(BigDecimal.ZERO);
                hmeMaterialLotReturn.setWaferNum(wafer);
                hmeMaterialLotReturn.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                hmeMaterialLotReturn.setAttribute1(returnVO3.getWorkcellId());
                hmeMaterialLotReturn.setAttribute2("SCARP");
                hmeMaterialLotReturn.setAttribute3(returnVO3.getNcCodeId());
                hmeMaterialLotReturn.setAttribute4(returnVO3.getCosReturnList().get(0).getMaterialId());
                hmeMaterialLotReturn.setAttribute5(returnVO3.getCosReturnList().get(0).getUsageQty() != null ? returnVO3.getCosReturnList().get(0).getUsageQty().toPlainString() : null);
                relationList.add(hmeMaterialLotReturn);
            });
            hmeMaterialLotReturnRepository.batchInsertSelective(relationList);
        }
    }

    /**
     * 热沉报废
     *
     * @param tenantId
     * @param eventId
     * @param returnVO3
     * @author sanfeng.zhang@hand-china.com 2021/9/25 21:00
     * @return void
     */
    private void processLoadMaterialHotSinkScrapped(Long tenantId, String eventId, HmeCosMaterialReturnVO3 returnVO3) {
        // 找到对应的热沉条码
        if (CollectionUtils.isNotEmpty(returnVO3.getHotSinkList()) && CollectionUtils.isNotEmpty(returnVO3.getBarcodeVOList())) {
            List<HmeCosScanBarcodeVO> filterBarcodeList = returnVO3.getBarcodeVOList().stream().filter(vo -> CollectionUtils.isNotEmpty(vo.getHotSinkList())).collect(Collectors.toList());
            //工单
            MtWorkOrder mtWorkOrder = mtWorkOrderMapper.selectByPrimaryKey(returnVO3.getWorkOrderId());
            // 值集
            List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.COS_RETURN_OPERATION", tenantId);
            String operationId = "";
            if (CollectionUtils.isNotEmpty(lovValue) && lovValue.size() > 2) {
                List<MtOperation> mtOperationList = mtOperationRepository.selectByOperationName(tenantId, Collections.singletonList(lovValue.get(1).getValue()));
                if (CollectionUtils.isEmpty(mtOperationList)) {
                    throw new MtException("HME_COS_MATERIAL_RETURN_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_MATERIAL_RETURN_016", "HME", "热沉"));
                }
                operationId = mtOperationList.get(0).getOperationId();
            }
            String routeStepId = hmeCosMaterialReturnMapper.queryRouterStepId(tenantId, mtWorkOrder.getRouterId(), operationId);
            List<HmeMaterialLotReturn> relationList = new ArrayList<>();
            for (HmeCosReturnScanLineVO cosReturnScanLineVO : returnVO3.getHotSinkList()) {
                // 找到对应的退料条码
                List<HmeCosScanBarcodeVO> returnBarcodeList = filterBarcodeList.stream().filter(fb -> StringUtils.equals(fb.getHotSinkList().get(0).getMaterialId(), cosReturnScanLineVO.getMaterialId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(returnBarcodeList)) {
                    Double scrappedQty = returnBarcodeList.stream().map(HmeCosScanBarcodeVO::getQty).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                    // 根据来源条码和热沉物料
//                    MtMaterialLot mtMaterialLot = hmeCosMaterialReturnMapper.queryHotSinkScrappedBarcode(tenantId, firstOpt.get().getMaterialLotId(), cosReturnScanLineVO.getMaterialId());
//                    this.verifyScrappedMaterialLot(tenantId, mtMaterialLot, firstOpt.get().getQty());

                    // 根据工单 芯片物料 步骤找到工单装配实绩
                    List<MtWorkOrderComponentActual> mtWorkOrderComponentActuals = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, returnVO3.getWorkOrderId())
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, cosReturnScanLineVO.getMaterialId())
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routeStepId)
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_ASSEMBLE_EXCESS_FLAG, NO)).build());
                    if (CollectionUtils.isNotEmpty(mtWorkOrderComponentActuals)) {
                        MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
                        cv2.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                        cv2.setMaterialId(cosReturnScanLineVO.getMaterialId());
                        cv2.setBomComponentId(mtWorkOrderComponentActuals.get(0).getBomComponentId());
                        cv2.setOperationId(mtWorkOrderComponentActuals.get(0).getOperationId());
                        cv2.setRouterStepId(mtWorkOrderComponentActuals.get(0).getRouterStepId());
                        BigDecimal subtractQty = BigDecimal.valueOf(mtWorkOrderComponentActuals.get(0).getAssembleQty()).subtract(BigDecimal.valueOf(scrappedQty));
                        cv2.setAssembleQty(subtractQty.doubleValue());
                        cv2.setScrappedQty(scrappedQty);
                        cv2.setComponentType("ASSEMBLING");
                        cv2.setAssembleExcessFlag("N");
                        cv2.setEventId(eventId);
                        mtWorkOrderComponentActualRepository.woComponentActualUpdate(tenantId, cv2);
                    }
                    for (HmeCosScanBarcodeVO hmeCosScanBarcodeVO : returnBarcodeList) {
                        // 建立退料条码和目标条码的关系
                        HmeMaterialLotReturn hmeMaterialLotReturn = new HmeMaterialLotReturn();
                        hmeMaterialLotReturn.setTenantId(tenantId);
                        hmeMaterialLotReturn.setReturnMaterialLotId(hmeCosScanBarcodeVO.getMaterialLotId());
                        hmeMaterialLotReturn.setTargetMaterialLotId("-1");
                        hmeMaterialLotReturn.setReturnQty(BigDecimal.ZERO);
                        hmeMaterialLotReturn.setWaferNum(returnVO3.getBarcodeVOList().get(0).getWaferNum());
                        hmeMaterialLotReturn.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                        hmeMaterialLotReturn.setAttribute1(returnVO3.getWorkcellId());
                        hmeMaterialLotReturn.setAttribute2("SCARP");
                        hmeMaterialLotReturn.setAttribute3(returnVO3.getNcCodeId());
                        hmeMaterialLotReturn.setAttribute4(cosReturnScanLineVO.getMaterialId());
                        hmeMaterialLotReturn.setAttribute5(cosReturnScanLineVO.getUsageQty() != null ? cosReturnScanLineVO.getUsageQty().toPlainString() : null);
                        relationList.add(hmeMaterialLotReturn);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(relationList)) {
                hmeMaterialLotReturnRepository.batchInsertSelective(relationList);
            }
        }
    }

    /**
     * 金线物料报废
     *
     * @param tenantId
     * @param eventId
     * @param returnVO3
     * @author sanfeng.zhang@hand-china.com 2021/9/25 21:00
     * @return void
     */
    private void processLoadMaterialWireBondReturn(Long tenantId, String eventId, HmeCosMaterialReturnVO3 returnVO3) {
        // 找到对应的金线条码
        if (CollectionUtils.isNotEmpty(returnVO3.getWireBondList()) && CollectionUtils.isNotEmpty(returnVO3.getBarcodeVOList())) {
            List<HmeCosScanBarcodeVO> filterBarcodeList = returnVO3.getBarcodeVOList().stream().filter(vo -> CollectionUtils.isNotEmpty(vo.getWireBondList())).collect(Collectors.toList());
            //工单
            MtWorkOrder mtWorkOrder = mtWorkOrderMapper.selectByPrimaryKey(returnVO3.getWorkOrderId());
            // 值集
            List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.COS_RETURN_OPERATION", tenantId);
            String operationId = "";
            if (CollectionUtils.isNotEmpty(lovValue) && lovValue.size() > 3) {
                List<MtOperation> mtOperationList = mtOperationRepository.selectByOperationName(tenantId, Collections.singletonList(lovValue.get(2).getValue()));
                if (CollectionUtils.isEmpty(mtOperationList)) {
                    throw new MtException("HME_COS_MATERIAL_RETURN_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_MATERIAL_RETURN_016", "HME", "金线"));
                }
                operationId = mtOperationList.get(0).getOperationId();
            }
            String routeStepId = hmeCosMaterialReturnMapper.queryRouterStepId(tenantId, mtWorkOrder.getRouterId(), operationId);
            List<HmeMaterialLotReturn> relationList = new ArrayList<>();
            for (HmeCosReturnScanLineVO cosReturnScanLineVO : returnVO3.getWireBondList()) {
                // 找到对应的退料条码
                List<HmeCosScanBarcodeVO> returnBarcodeList = filterBarcodeList.stream().filter(fb -> StringUtils.equals(fb.getWireBondList().get(0).getMaterialId(), cosReturnScanLineVO.getMaterialId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(returnBarcodeList)) {
//                    // 根据来源条码和金线 物料
//                    MtMaterialLot mtMaterialLot = hmeCosMaterialReturnMapper.queryWireBondScrappedBarcode(tenantId, firstOpt.get().getMaterialLotId(), cosReturnScanLineVO.getMaterialId());
//                    this.verifyScrappedMaterialLot(tenantId, mtMaterialLot, firstOpt.get().getQty());
                    Double scrappedQty = returnBarcodeList.stream().map(HmeCosScanBarcodeVO::getQty).mapToDouble(Double::doubleValue).summaryStatistics().getSum();

                    // 根据工单 芯片物料 步骤找到工单装配实绩
                    List<MtWorkOrderComponentActual> mtWorkOrderComponentActuals = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, returnVO3.getWorkOrderId())
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, cosReturnScanLineVO.getMaterialId())
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routeStepId)
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_ASSEMBLE_EXCESS_FLAG, NO)).build());
                    if (CollectionUtils.isNotEmpty(mtWorkOrderComponentActuals)) {
                        MtWoComponentActualVO2 cv2 = new MtWoComponentActualVO2();
                        cv2.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                        cv2.setMaterialId(cosReturnScanLineVO.getMaterialId());
                        cv2.setBomComponentId(mtWorkOrderComponentActuals.get(0).getBomComponentId());
                        cv2.setOperationId(mtWorkOrderComponentActuals.get(0).getOperationId());
                        cv2.setRouterStepId(mtWorkOrderComponentActuals.get(0).getRouterStepId());
                        BigDecimal subtractQty = BigDecimal.valueOf(mtWorkOrderComponentActuals.get(0).getAssembleQty()).subtract(BigDecimal.valueOf(scrappedQty));
                        cv2.setAssembleQty(subtractQty.doubleValue());
                        cv2.setScrappedQty(scrappedQty);
                        cv2.setComponentType("ASSEMBLING");
                        cv2.setAssembleExcessFlag("N");
                        cv2.setEventId(eventId);
                        mtWorkOrderComponentActualRepository.woComponentActualUpdate(tenantId, cv2);
                    }
                    for (HmeCosScanBarcodeVO hmeCosScanBarcodeVO : returnBarcodeList) {
                        // 建立退料条码和目标条码的关系
                        HmeMaterialLotReturn hmeMaterialLotReturn = new HmeMaterialLotReturn();
                        hmeMaterialLotReturn.setTenantId(tenantId);
                        hmeMaterialLotReturn.setReturnMaterialLotId(hmeCosScanBarcodeVO.getMaterialLotId());
                        hmeMaterialLotReturn.setTargetMaterialLotId("-1");
                        hmeMaterialLotReturn.setReturnQty(BigDecimal.ZERO);
                        hmeMaterialLotReturn.setWaferNum(returnVO3.getBarcodeVOList().get(0).getWaferNum());
                        hmeMaterialLotReturn.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                        hmeMaterialLotReturn.setAttribute1(returnVO3.getWorkcellId());
                        hmeMaterialLotReturn.setAttribute2("SCARP");
                        hmeMaterialLotReturn.setAttribute3(returnVO3.getNcCodeId());
                        hmeMaterialLotReturn.setAttribute4(cosReturnScanLineVO.getMaterialId());
                        hmeMaterialLotReturn.setAttribute5(cosReturnScanLineVO.getUsageQty() != null ? cosReturnScanLineVO.getUsageQty().toPlainString() : null);
                        relationList.add(hmeMaterialLotReturn);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(relationList)) {
                hmeMaterialLotReturnRepository.batchInsertSelective(relationList);
            }
        }
    }

    private void verifyScrappedMaterialLot(Long tenantId, MtMaterialLot mtMaterialLot, Double qty) {
        // 冻结条码 进行报错
        if (YES.equals(mtMaterialLot.getFreezeFlag())) {
            throw new MtException("HME_COS_MATERIAL_RETURN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_013", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 盘点条码 进行报错
        if (YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_MATERIAL_RETURN_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_014", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 报废数量不足
        if (BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).compareTo(BigDecimal.valueOf(qty)) <0) {
            throw new MtException("HME_COS_MATERIAL_RETURN_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_015", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 非在制品 进行报错
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_material_lot_attr");
            setAttrName("MF_FLAG");
            setKeyId(mtMaterialLot.getMaterialLotId());
        }});
        String mfFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
        if (!YES.equals(mfFlag)) {
            throw new MtException("HME_COS_MATERIAL_RETURN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_004", "HME", mtMaterialLot.getMaterialLotCode()));
        }
    }

    /**
     * 芯片退料
     *
     * @param tenantId
     * @param eventId
     * @param mtWorkOrder
     * @param locatorId
     * @param lot
     * @param wafer
     * @param demandQty
     * @param materialLotIdList
     * @param returnVO3
     * @author sanfeng.zhang@hand-china.com 2021/10/22 9:32
     * @return
     */
    private HmeCosMaterialReturnVO4 processLoadMaterialReturn2(Long tenantId, String eventId, MtWorkOrder mtWorkOrder, String locatorId, String lot, String wafer, BigDecimal demandQty, List<String> materialLotIdList, HmeCosMaterialReturnVO3 returnVO3, String ncCodeId) {
        HmeCosMaterialReturnVO4 returnVO4 = new HmeCosMaterialReturnVO4();
        Date nowDate = DateUtil.date();
        WmsTransactionTypeDTO externalTrxType = wmsTransactionTypeRepository.getTransactionType(tenantId, HME_WO_ISSUE_R_EXT);
        WmsTransactionTypeDTO internalTrxType = wmsTransactionTypeRepository.getTransactionType(tenantId, HME_WO_ISSUE_R);

        returnVO3.getCosReturnList().forEach(rec -> {
            //更新退料条码或者生成新条码
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setEventId(eventId);
            if (StringUtils.isNotBlank(returnVO3.getTargetMaterialLotCode())) {
                MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, returnVO3.getTargetMaterialLotCode());
                mtMaterialLotVO2.setMaterialLotCode(returnVO3.getTargetMaterialLotCode());
                mtMaterialLotVO2.setMaterialLotId(materialLot.getMaterialLotId());
            }
            mtMaterialLotVO2.setCreateReason("INITIALIZE");
            mtMaterialLotVO2.setSiteId(mtWorkOrder.getSiteId());
            mtMaterialLotVO2.setEnableFlag(HmeConstants.ConstantValue.YES);
            mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
            mtMaterialLotVO2.setMaterialId(rec.getMaterialId());
            mtMaterialLotVO2.setPrimaryUomId(returnVO3.getUomId());
            mtMaterialLotVO2.setPrimaryUomQty(returnVO3.getBackQty().doubleValue());
            mtMaterialLotVO2.setLocatorId(locatorId);
            mtMaterialLotVO2.setSupplierId(returnVO3.getBarcodeVOList().get(0).getSupplierId());
            mtMaterialLotVO2.setInLocatorTime(nowDate);
            mtMaterialLotVO2.setLot(lot);
            MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            //更新扩展字段
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 waferNumAttr = new MtExtendVO5();
            waferNumAttr.setAttrName("WAFER_NUM");
            waferNumAttr.setAttrValue(wafer);
            mtExtendVO5List.add(waferNumAttr);
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName("STATUS");
            statusAttr.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
            mtExtendVO5List.add(statusAttr);
            MtExtendVO5 dateAttr = new MtExtendVO5();
            dateAttr.setAttrName("PRODUCT_DATE");
            dateAttr.setAttrValue(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            mtExtendVO5List.add(dateAttr);
            MtExtendVO5 ncAttr = new MtExtendVO5();
            ncAttr.setAttrName("NC_CODE");
            ncAttr.setAttrValue(returnVO3.getNcCode());
            mtExtendVO5List.add(ncAttr);
            MtExtendVO5 supplierLotAttr = new MtExtendVO5();
            supplierLotAttr.setAttrName("SUPPLIER_LOT");
            supplierLotAttr.setAttrValue(rec.getSupplierLot());
            mtExtendVO5List.add(supplierLotAttr);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", mtMaterialLotVO13.getMaterialLotId(), eventId, mtExtendVO5List);

            // 建立退料条码和目标条码的关系
            List<HmeMaterialLotReturn> relationList = new ArrayList<>();
            materialLotIdList.forEach(line -> {
                HmeMaterialLotReturn hmeMaterialLotReturn = new HmeMaterialLotReturn();
                hmeMaterialLotReturn.setTenantId(tenantId);
                hmeMaterialLotReturn.setReturnMaterialLotId(line);
                hmeMaterialLotReturn.setTargetMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
                hmeMaterialLotReturn.setReturnQty(rec.getReturnQty());
                hmeMaterialLotReturn.setWaferNum(wafer);
                hmeMaterialLotReturn.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                hmeMaterialLotReturn.setLot(lot);
                hmeMaterialLotReturn.setAttribute1(returnVO3.getWorkcellId());
                hmeMaterialLotReturn.setAttribute2("RETURN");
                hmeMaterialLotReturn.setAttribute3(ncCodeId);
                hmeMaterialLotReturn.setAttribute4(rec.getMaterialId());
                hmeMaterialLotReturn.setAttribute5(rec.getUsageQty() != null ? rec.getUsageQty().toPlainString() : null);
                relationList.add(hmeMaterialLotReturn);
            });
            hmeMaterialLotReturnRepository.batchInsertSelective(relationList);

            //装配数量
            List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.select(new MtWorkOrderComponentActual() {{
                setTenantId(tenantId);
                setWorkOrderId(mtWorkOrder.getWorkOrderId());
                setMaterialId(rec.getMaterialId());
            }});

            //装配数量
            BigDecimal assembleQty = BigDecimal.ZERO;
            // 20210827 modify by sanfeng.zhang for peng.zhao 预留头行号根据物料工单实时查询
            //获取bomComponentId(bomId+物料)
            List<MtBomComponent> bomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
                setTenantId(tenantId);
                setBomId(mtWorkOrder.getBomId());
                setMaterialId(rec.getMaterialId());
            }});
            String bomReserveNum = "";
            String bomReserveLineNum = "";
            if (CollectionUtils.isNotEmpty(bomComponentList)) {
                MtExtendVO bomComponentExtend = new MtExtendVO();
                bomComponentExtend.setTableName("mt_bom_component_attr");
                bomComponentExtend.setKeyId(bomComponentList.get(0).getBomComponentId());
                bomComponentExtend.setAttrName("lineAttribute10");
                List<MtExtendAttrVO> bomComponentExtendAttr = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomComponentExtend);
                if (CollectionUtils.isNotEmpty(bomComponentExtendAttr)) {
                    bomReserveNum = bomComponentExtendAttr.get(0).getAttrValue();
                }
                MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentList.get(0).getBomComponentId());
                bomReserveLineNum = String.valueOf(mtBomComponent.getLineNumber());
            }

            if (CollectionUtils.isNotEmpty(actualList)) {
                assembleQty = actualList.get(0).getAssembleQty() != null ? BigDecimal.valueOf(actualList.get(0).getAssembleQty()) : BigDecimal.ZERO;
            }
            //分三种情况
            List<WmsObjectTransactionRequestVO> requestVOList = new ArrayList<>();
            if (rec.getReturnQty().compareTo(assembleQty.subtract(demandQty)) <= 0) {
                //退料数量<=工单组件装配数量-工单组件净需求数量 事务数量=退料数量，计划外退料
                WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                requestVO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                requestVO.setTransactionQty(rec.getReturnQty());
                requestVO.setMoveType(externalTrxType != null ? externalTrxType.getMoveType() : "");
                requestVOList.add(requestVO);
            } else if (assembleQty.subtract(demandQty).compareTo(BigDecimal.ZERO) > 0 && assembleQty.subtract(demandQty).compareTo(rec.getReturnQty()) < 0) {
                // 0<工单组件装配数量-工单组件净需求数量退料数量<退料数量
                //事务1： 事务数量=工单组件装配数量-工单组件净需求数量退料数量），计划外退料
                WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                requestOutVO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                requestOutVO.setMoveType(externalTrxType != null ? externalTrxType.getMoveType() : "");
                requestOutVO.setTransactionQty(assembleQty.subtract(demandQty));
                requestVOList.add(requestOutVO);

                //事务2：事务数量=退料数量-（工单组件装配数量-工单组件净需求数量退料数量），计划内退料
                WmsObjectTransactionRequestVO requestInVO = new WmsObjectTransactionRequestVO();
                requestInVO.setTransactionTypeCode(HME_WO_ISSUE_R);
                requestInVO.setMoveType(internalTrxType != null ? internalTrxType.getMoveType() : "");
                requestInVO.setTransactionQty(rec.getReturnQty().subtract(assembleQty.subtract(demandQty)));

                //计划内记录预留项目号及行号
                requestInVO.setBomReserveNum(bomReserveNum);
                requestInVO.setBomReserveLineNum(bomReserveLineNum);
                requestVOList.add(requestInVO);
            } else if (assembleQty.subtract(demandQty).compareTo(BigDecimal.ZERO) <= 0) {
                // 工单组件装配数量-工单组件净需求数量退料数量<=0 事务数量=退料数量，计划内退料
                WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                requestVO.setTransactionTypeCode(HME_WO_ISSUE_R);
                requestVO.setMoveType(internalTrxType != null ? internalTrxType.getMoveType() : "");
                requestVO.setTransactionQty(rec.getReturnQty());
                //计划内记录预留项目号及行号
                requestVO.setBomReserveNum(bomReserveNum);
                requestVO.setBomReserveLineNum(bomReserveLineNum);
                requestVOList.add(requestVO);
            }

            // 记录工单退料事务
            for (WmsObjectTransactionRequestVO requestVO : requestVOList) {
                WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                transactionRequestVO.setEventId(eventId);
                transactionRequestVO.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
                transactionRequestVO.setMaterialId(rec.getMaterialId());
                transactionRequestVO.setLotNumber(lot);
                transactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
                transactionRequestVO.setTransactionUom(rec.getUomCode());
                transactionRequestVO.setTransactionTime(new Date());
                transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorId);
                transactionRequestVO.setWarehouseId(mtModLocator != null ? mtModLocator.getParentLocatorId() : "");
                transactionRequestVO.setLocatorId(locatorId);
                transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                //查询产线
                MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());
                transactionRequestVO.setProdLineCode(productionLine != null ? productionLine.getProdLineCode() : "");
                //移动类型
                transactionRequestVO.setMoveType(requestVO.getMoveType());
                transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                transactionRequestVO.setRemark("COS芯片退料");
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(transactionRequestVO));

            }

            //扣减工单对应物料装配数量
            if (CollectionUtils.isNotEmpty(actualList)) {
                // 可能存在工单，物料找到多个装配实际行，故而需要分摊退料数量到装配实际行上，直到数量扣光
                BigDecimal remainQty = rec.getReturnQty();
                for (MtWorkOrderComponentActual componentActual : actualList) {
                    // 计算装配数量，扣减数量为
                    BigDecimal assemblyQty = BigDecimal.valueOf(componentActual.getAssembleQty());
                    BigDecimal reduceQty = remainQty.min(assemblyQty);
                    BigDecimal newAssemblyQty = assemblyQty.subtract(reduceQty);
                    componentActual.setAssembleQty(newAssemblyQty.doubleValue());
                    mtWorkOrderComponentActualRepository.updateByPrimaryKeySelective(componentActual);
                    remainQty = remainQty.subtract(reduceQty);
                    if (remainQty.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }
                }
            }
            //增加库存现有量
            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            mtInvOnhandQuantityVO9.setSiteId(mtWorkOrder.getSiteId());
            mtInvOnhandQuantityVO9.setMaterialId(rec.getMaterialId());
            mtInvOnhandQuantityVO9.setLocatorId(locatorId);
            mtInvOnhandQuantityVO9.setLotCode(lot);
            mtInvOnhandQuantityVO9.setChangeQuantity(rec.getReturnQty().doubleValue());
            mtInvOnhandQuantityVO9.setEventId(eventId);
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

            // 返回目标条码
            rec.setTargetMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
            rec.setTargetMaterialLot(mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLotVO13.getMaterialLotId()).getMaterialLotCode());
        });
        if (CollectionUtils.isNotEmpty(returnVO3.getCosReturnList())) {
            returnVO4.setMaterialLotId(returnVO3.getCosReturnList().get(0).getTargetMaterialLotId());
            returnVO4.setMaterialLotCode(returnVO3.getCosReturnList().get(0).getTargetMaterialLot());
        }
        return returnVO4;
    }

    /**
     * 处理装载物料退料
     *
     * @param tenantId          租户
     * @param eventId           事件ID
     * @param mtWorkOrder       工单
     * @param locatorId         货位
     * @param lot               批次
     * @param wafer             wafer
     * @param demandQty         净需求数量
     * @param materialLotIdList 退料物料批ID列表
     * @param list              退料列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/12 05:35:29
     */
    private void processLoadMaterialReturn(Long tenantId, String eventId, MtWorkOrder mtWorkOrder, String locatorId, String lot, String wafer, BigDecimal demandQty, List<String> materialLotIdList, List<HmeCosReturnScanLineVO> list, String materialId, String ncCodeId, String workcellId) {
        Date nowDate = DateUtil.date();
        WmsTransactionTypeDTO externalTrxType = wmsTransactionTypeRepository.getTransactionType(tenantId, HME_WO_ISSUE_R_EXT);
        WmsTransactionTypeDTO internalTrxType = wmsTransactionTypeRepository.getTransactionType(tenantId, HME_WO_ISSUE_R);
        list.forEach(rec -> {
            String returnMaterialId = StringUtils.isNotBlank(materialId) ? materialId : rec.getMaterialId();
            // 新增条码
            MtMaterialLotVO2 materialLotUpdate = new MtMaterialLotVO2();
            materialLotUpdate.setEventId(eventId);
            materialLotUpdate.setSiteId(mtWorkOrder.getSiteId());
            materialLotUpdate.setEnableFlag(YES);
            materialLotUpdate.setQualityStatus(OK);
            materialLotUpdate.setMaterialId(returnMaterialId);
            materialLotUpdate.setPrimaryUomId(rec.getUomId());
            materialLotUpdate.setPrimaryUomQty(rec.getReturnQty().doubleValue());
            materialLotUpdate.setLocatorId(locatorId);
            materialLotUpdate.setLot(lot);
            materialLotUpdate.setSupplierId(rec.getSupplierId());
            materialLotUpdate.setInLocatorTime(nowDate);
            materialLotUpdate.setCreateReason("INITIALIZE");
            MtMaterialLotVO13 newMaterialLot = mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotUpdate, NO);

            //更新扩展字段
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 waferNumAttr = new MtExtendVO5();
            waferNumAttr.setAttrName("WAFER_NUM");
            waferNumAttr.setAttrValue(wafer);
            attrList.add(waferNumAttr);
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName("STATUS");
            statusAttr.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
            attrList.add(statusAttr);
            MtExtendVO5 dateAttr = new MtExtendVO5();
            dateAttr.setAttrName("PRODUCT_DATE");
            dateAttr.setAttrValue(DateUtils.format(nowDate, "yyyy-MM-dd HH:mm:ss"));
            attrList.add(dateAttr);
            MtExtendVO5 supplierLotAttr = new MtExtendVO5();
            supplierLotAttr.setAttrName("SUPPLIER_LOT");
            supplierLotAttr.setAttrValue(rec.getSupplierLot());
            attrList.add(supplierLotAttr);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", newMaterialLot.getMaterialLotId(), eventId, attrList);

            // 建立退料条码和目标条码的关系
            List<HmeMaterialLotReturn> relationList = new ArrayList<>();
            materialLotIdList.forEach(line -> {
                HmeMaterialLotReturn hmeMaterialLotReturn = new HmeMaterialLotReturn();
                hmeMaterialLotReturn.setTenantId(tenantId);
                hmeMaterialLotReturn.setReturnMaterialLotId(line);
                hmeMaterialLotReturn.setTargetMaterialLotId(newMaterialLot.getMaterialLotId());
                hmeMaterialLotReturn.setReturnQty(rec.getReturnQty());
                hmeMaterialLotReturn.setWaferNum(wafer);
                hmeMaterialLotReturn.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                hmeMaterialLotReturn.setLot(lot);
                hmeMaterialLotReturn.setAttribute1(workcellId);
                hmeMaterialLotReturn.setAttribute2("RETURN");
                hmeMaterialLotReturn.setAttribute3(ncCodeId);
                hmeMaterialLotReturn.setAttribute4(returnMaterialId);
                hmeMaterialLotReturn.setAttribute5(rec.getUsageQty() != null ? rec.getUsageQty().toPlainString() : null);
                relationList.add(hmeMaterialLotReturn);
            });
            hmeMaterialLotReturnRepository.batchInsertSelective(relationList);

            //装配数量
            List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.select(new MtWorkOrderComponentActual() {{
                setTenantId(tenantId);
                setWorkOrderId(mtWorkOrder.getWorkOrderId());
                setMaterialId(returnMaterialId);
            }});

            //装配数量
            BigDecimal assembleQty = BigDecimal.ZERO;
            // 20210827 modify by sanfeng.zhang for peng.zhao 预留头行号根据物料工单实时查询
            //获取bomComponentId(bomId+物料)
            List<MtBomComponent> bomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
                setTenantId(tenantId);
                setBomId(mtWorkOrder.getBomId());
                setMaterialId(rec.getMaterialId());
            }});
            String bomReserveNum = "";
            String bomReserveLineNum = "";
            if (CollectionUtils.isNotEmpty(bomComponentList)) {
                MtExtendVO bomComponentExtend = new MtExtendVO();
                bomComponentExtend.setTableName("mt_bom_component_attr");
                bomComponentExtend.setKeyId(bomComponentList.get(0).getBomComponentId());
                bomComponentExtend.setAttrName("lineAttribute10");
                List<MtExtendAttrVO> bomComponentExtendAttr = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomComponentExtend);
                if (CollectionUtils.isNotEmpty(bomComponentExtendAttr)) {
                    bomReserveNum = bomComponentExtendAttr.get(0).getAttrValue();
                }
                MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentList.get(0).getBomComponentId());
                bomReserveLineNum = String.valueOf(mtBomComponent.getLineNumber());
            }

            if (CollectionUtils.isNotEmpty(actualList)) {
                assembleQty = actualList.get(0).getAssembleQty() != null ? BigDecimal.valueOf(actualList.get(0).getAssembleQty()) : BigDecimal.ZERO;
            }
            //分三种情况
            List<WmsObjectTransactionRequestVO> requestVOList = new ArrayList<>();
            if (rec.getReturnQty().compareTo(assembleQty.subtract(demandQty)) <= 0) {
                //退料数量<=工单组件装配数量-工单组件净需求数量 事务数量=退料数量，计划外退料
                WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                requestVO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                requestVO.setTransactionQty(rec.getReturnQty());
                requestVO.setMoveType(externalTrxType != null ? externalTrxType.getMoveType() : "");
                requestVOList.add(requestVO);
            } else if (assembleQty.subtract(demandQty).compareTo(BigDecimal.ZERO) > 0 && assembleQty.subtract(demandQty).compareTo(rec.getReturnQty()) < 0) {
                // 0<工单组件装配数量-工单组件净需求数量退料数量<退料数量
                //事务1： 事务数量=工单组件装配数量-工单组件净需求数量退料数量），计划外退料
                WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                requestOutVO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                requestOutVO.setMoveType(externalTrxType != null ? externalTrxType.getMoveType() : "");
                requestOutVO.setTransactionQty(assembleQty.subtract(demandQty));
                requestVOList.add(requestOutVO);

                //事务2：事务数量=退料数量-（工单组件装配数量-工单组件净需求数量退料数量），计划内退料
                WmsObjectTransactionRequestVO requestInVO = new WmsObjectTransactionRequestVO();
                requestInVO.setTransactionTypeCode(HME_WO_ISSUE_R);
                requestInVO.setMoveType(internalTrxType != null ? internalTrxType.getMoveType() : "");
                requestInVO.setTransactionQty(rec.getReturnQty().subtract(assembleQty.subtract(demandQty)));

                //计划内记录预留项目号及行号
                requestInVO.setBomReserveNum(bomReserveNum);
                requestInVO.setBomReserveLineNum(bomReserveLineNum);
                requestVOList.add(requestInVO);
            } else if (assembleQty.subtract(demandQty).compareTo(BigDecimal.ZERO) <= 0) {
                // 工单组件装配数量-工单组件净需求数量退料数量<=0 事务数量=退料数量，计划内退料
                WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                requestVO.setTransactionTypeCode(HME_WO_ISSUE_R);
                requestVO.setMoveType(internalTrxType != null ? internalTrxType.getMoveType() : "");
                requestVO.setTransactionQty(rec.getReturnQty());
                //计划内记录预留项目号及行号
                requestVO.setBomReserveNum(bomReserveNum);
                requestVO.setBomReserveLineNum(bomReserveLineNum);
                requestVOList.add(requestVO);
            }

            // 记录工单退料事务
            for (WmsObjectTransactionRequestVO requestVO : requestVOList) {
                WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                transactionRequestVO.setEventId(eventId);
                transactionRequestVO.setMaterialLotId(newMaterialLot.getMaterialLotId());
                transactionRequestVO.setMaterialId(returnMaterialId);
                transactionRequestVO.setLotNumber(lot);
                transactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
                transactionRequestVO.setTransactionUom(rec.getUomCode());
                transactionRequestVO.setTransactionTime(new Date());
                transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorId);
                transactionRequestVO.setWarehouseId(mtModLocator != null ? mtModLocator.getParentLocatorId() : "");
                transactionRequestVO.setLocatorId(locatorId);
                transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                //查询产线
                MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());
                transactionRequestVO.setProdLineCode(productionLine != null ? productionLine.getProdLineCode() : "");
                //移动类型
                transactionRequestVO.setMoveType(requestVO.getMoveType());
                transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                transactionRequestVO.setRemark("COS芯片退料");
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(transactionRequestVO));

            }
            //扣减工单对应物料装配数量
            if (CollectionUtils.isNotEmpty(actualList)) {
                // 可能存在工单，物料找到多个装配实际行，故而需要分摊退料数量到装配实际行上，直到数量扣光
                BigDecimal remainQty = rec.getReturnQty();
                for (MtWorkOrderComponentActual componentActual : actualList) {
                    // 计算装配数量，扣减数量为
                    BigDecimal assemblyQty = BigDecimal.valueOf(componentActual.getAssembleQty());
                    BigDecimal reduceQty = remainQty.min(assemblyQty);
                    BigDecimal newAssemblyQty = assemblyQty.subtract(reduceQty);
                    componentActual.setAssembleQty(newAssemblyQty.doubleValue());
                    mtWorkOrderComponentActualRepository.updateByPrimaryKeySelective(componentActual);
                    remainQty = remainQty.subtract(reduceQty);
                    if (remainQty.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }
                }
            }
            //增加库存现有量
            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            mtInvOnhandQuantityVO9.setSiteId(mtWorkOrder.getSiteId());
            mtInvOnhandQuantityVO9.setMaterialId(returnMaterialId);
            mtInvOnhandQuantityVO9.setLocatorId(locatorId);
            mtInvOnhandQuantityVO9.setLotCode(lot);
            mtInvOnhandQuantityVO9.setChangeQuantity(rec.getReturnQty().doubleValue());
            mtInvOnhandQuantityVO9.setEventId(eventId);
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

            // 返回目标条码
            rec.setTargetMaterialLotId(newMaterialLot.getMaterialLotId());
            rec.setTargetMaterialLot(mtMaterialLotRepository.selectByPrimaryKey(newMaterialLot.getMaterialLotId()).getMaterialLotCode());
        });
    }


    private void verifyMaterialLot(Long tenantId, MtMaterialLot materialLot, String workOrderId, HmeCosScanBarcodeVO barcodeVO, String materialId, String returnType) {
        //校验有效性
        if (!HmeConstants.ConstantValue.YES.equals(materialLot.getEnableFlag())) {
            throw new MtException("HME_COS_MATERIAL_RETURN_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_003", "HME", materialLot.getMaterialLotCode()));
        }
        //校验物料一致
        if (!StringUtils.equals(materialId, materialLot.getMaterialId())) {
            if (StringCommonUtils.contains(returnType, HOT_SINK, WIRE_BOND)) {
                throw new MtException("HME_COS_MATERIAL_RETURN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_MATERIAL_RETURN_011", "HME", materialLot.getMaterialLotCode()));
            } else {
                throw new MtException("HME_COS_MATERIAL_RETURN_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_MATERIAL_RETURN_010", "HME", materialLot.getMaterialLotCode()));
            }
        }
        //查询条码的扩展字段
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_material_lot_attr");
        mtExtendVO1.setKeyIdList(Collections.singletonList(materialLot.getMaterialLotId()));
        List<MtExtendVO5> attrList = new ArrayList<>();
        MtExtendVO5 mfFlagAttr = new MtExtendVO5();
        mfFlagAttr.setAttrName("MF_FLAG");
        attrList.add(mfFlagAttr);
        MtExtendVO5 workOrderAttr = new MtExtendVO5();
        workOrderAttr.setAttrName("WORK_ORDER_ID");
        attrList.add(workOrderAttr);
        MtExtendVO5 waferNumAttr = new MtExtendVO5();
        waferNumAttr.setAttrName("WAFER_NUM");
        attrList.add(waferNumAttr);
        MtExtendVO5 cosTypeAttr = new MtExtendVO5();
        cosTypeAttr.setAttrName("COS_TYPE");
        attrList.add(cosTypeAttr);
        mtExtendVO1.setAttrs(attrList);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        //校验在制品
        Optional<MtExtendAttrVO1> mfFlagOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLot.getMaterialLotId()) && StringUtils.equals("MF_FLAG", dto.getAttrName())).findFirst();
        String mfFlag = mfFlagOpt.isPresent() ? mfFlagOpt.get().getAttrValue() : "";
        if (!StringUtils.equals(HmeConstants.ConstantValue.YES, mfFlag)) {
            throw new MtException("HME_COS_MATERIAL_RETURN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_004", "HME", materialLot.getMaterialLotCode()));
        }
        barcodeVO.setMfFlag(mfFlag);
        //校验工单一致
        Optional<MtExtendAttrVO1> workOrderOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLot.getMaterialLotId()) && StringUtils.equals("WORK_ORDER_ID", dto.getAttrName())).findFirst();
        String workOrder = workOrderOpt.isPresent() ? workOrderOpt.get().getAttrValue() : "";
        if (!StringUtils.equals(workOrder, workOrderId)) {
            throw new MtException("HME_COS_MATERIAL_RETURN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_005", "HME", materialLot.getMaterialLotCode()));
        }
        barcodeVO.setWorkOrderId(workOrder);
        Optional<MtExtendAttrVO1> cosTypeOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLot.getMaterialLotId()) && StringUtils.equals("COS_TYPE", dto.getAttrName())).findFirst();
        barcodeVO.setCosType(cosTypeOpt.isPresent() ? cosTypeOpt.get().getAttrValue() : "");
        //WAFER
        Optional<MtExtendAttrVO1> waferOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLot.getMaterialLotId()) && StringUtils.equals("WAFER_NUM", dto.getAttrName())).findFirst();
        barcodeVO.setWaferNum(waferOpt.isPresent() ? waferOpt.get().getAttrValue() : "");
        //校验供应商
        if (StringUtils.isBlank(materialLot.getSupplierId())) {
            throw new MtException("HME_COS_MATERIAL_RETURN_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_006", "HME", materialLot.getMaterialLotCode()));
        }
        //校验条码物料是否等于工单物料或芯片物料
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
        List<LovValueDTO> chipItemGroup = list.stream().filter(t -> "芯片".equals(t.getMeaning())).collect(Collectors.toList());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());
        if (!StringCommonUtils.contains(returnType, HOT_SINK, WIRE_BOND)) {
            //查询芯片物料
            List<String> materialIdList = hmeCosMaterialReturnMapper.queryMaterialIdByWorkOrderId(tenantId, workOrderId, chipItemGroup.get(0).getValue());
            String cosMaterialId = CollectionUtils.isNotEmpty(materialIdList) ? materialIdList.get(0) : "";
            if (!StringUtils.equals(cosMaterialId, materialLot.getMaterialId())) {
                throw new MtException("HME_COS_MATERIAL_RETURN_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_MATERIAL_RETURN_007", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : ""));
            }
        }
        if (mtMaterial != null) {
            barcodeVO.setMaterialCode(mtMaterial.getMaterialCode());
            barcodeVO.setMaterialId(mtMaterial.getMaterialId());
            barcodeVO.setMaterialName(mtMaterial.getMaterialName());
        }
    }
}
