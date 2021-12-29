package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeCosGetChipPlatformService;
import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.entity.WmsMaterialSubstituteRel;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsMaterialSubstituteRelRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.core.internal.util.CollectionUtil;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.repository.MtEoStepWipRepository;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtEoRouterActualVO19;
import tarzan.actual.domain.vo.MtEoStepWipVO4;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.actual.domain.vo.MtWoComponentActualVO2;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtRouterOperationComponentRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO14;
import tarzan.order.domain.vo.MtEoVO18;
import tarzan.order.infra.mapper.MtEoMapper;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static com.ruike.hme.infra.constant.HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R;
import static com.ruike.hme.infra.constant.HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT;
import static com.ruike.mdm.infra.constant.MdmConstant.LocatorCategory.INVENTORY;

/**
 * HmeCosPatchPdaRepositoryImpl
 *
 * @author chaonan.hu@hand-china.com 2020-08-24 16:28:34
 **/
@Component
@Slf4j
public class HmeCosPatchPdaRepositoryImpl implements HmeCosPatchPdaRepository {

    @Autowired
    private HmeMaterialTransferRepository materialTransferRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private HmeCosOperationRecordRepository cosOperationRecordRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private HmeTimeProcessPdaMapper hmeTimeProcessPdaMapper;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;
    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;
    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtWorkOrderComponentActualRepository woComActualRepository;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;
    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;
    @Autowired
    private HmeCosGetChipPlatformService hmeCosGetChipPlatformService;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeSnBindEoMapper hmeSnBindEoMapper;
    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private HmeEoJobEquipmentService hmeEoJobEquipmentService;
    @Autowired
    private HmeTimeProcessPdaRepository hmeTimeProcessPdaRepository;
    @Autowired
    private HmeCosGetChipPlatformMapper hmeCosGetChipPlatformMapper;
    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;
    @Autowired
    private HmeEoJobEquipmentMapper hmeEoJobEquipmentMapper;
    @Autowired
    private WmsMaterialSubstituteRelRepository wmsMaterialSubstituteRelRepository;
    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;
    @Autowired
    private HmeLoadJobRepository hmeLoadJobRepository;
    @Autowired
    private HmeLoadJobObjectRepository hmeLoadJobObjectRepository;
    @Autowired
    private HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;
    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    @Override
    public HmeCosPatchPdaVO noSiteOutDataQuery(Long tenantId, HmeCosPatchPdaDTO7 dto) {
        HmeCosPatchPdaVO result = new HmeCosPatchPdaVO();
        String operationId = dto.getOperationIdList().get(0);
        String equipmentId = null;
        if (StringUtils.isNotEmpty(dto.getEquipmentCode())) {
            HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                setTenantId(tenantId);
                setAssetEncoding(dto.getEquipmentCode());
            }});
            equipmentId = hmeEquipment.getEquipmentId();
        }
        HmeCosOperationRecord hmeCosOperationRecord = hmeCosPatchPdaMapper.recordQuery(tenantId, dto.getWorkcellId(),
                operationId, equipmentId);
        if (hmeCosOperationRecord != null) {
            String workOrderId = hmeCosOperationRecord.getWorkOrderId();
            if(hmeCosOperationRecord.getSurplusCosNum() != 0){
                //查询返回结果（工位+工艺+工单+作业平台类型查询进出站所有数据）
                List<HmeCosPatchPdaVO5> hmeCosPatchPdaVO5s = hmeCosPatchPdaMapper.eoJobSnDataQuery(tenantId, dto.getWorkcellId(), workOrderId, operationId);
                if (CollectionUtils.isNotEmpty(hmeCosPatchPdaVO5s)) {
                    //2021-11-09 16:18 edit by chaonan.hu for yiming.zhang 取出查询列表中出站时间最近的那笔jobId
                    List<HmeCosPatchPdaVO5> siteOutDateNonNullList = hmeCosPatchPdaVO5s.stream()
                            .filter(item -> Objects.nonNull(item.getSiteOutDate()))
                            .sorted(Comparator.comparing(HmeCosPatchPdaVO5::getSiteOutDate).reversed())
                            .collect(Collectors.toList());
                    String labCode = "";
                    String labRemark = "";
                    if(CollectionUtils.isNotEmpty(siteOutDateNonNullList)){
                        //根据jobId查询实验代码以及备注,返回至前台，用于新增界面默认带出来
                        String jobId = siteOutDateNonNullList.get(0).getJobId();
                        HmeCosPatchPdaVO9 labCodeAndRemark = hmeCosPatchPdaMapper.labCodeAndRemarkQueryByJobId(tenantId, jobId);
                        if(Objects.nonNull(labCodeAndRemark)){
                            if(StringUtils.isNotBlank(labCodeAndRemark.getLabCode())){
                                labCode = labCodeAndRemark.getLabCode();
                            }
                            if(StringUtils.isNotBlank(labCodeAndRemark.getRemark())){
                                labRemark = labCodeAndRemark.getRemark();
                            }
                        }
                    }

                    long number = 1;
                    for (HmeCosPatchPdaVO5 hmeCosPatchPdaVO5 : hmeCosPatchPdaVO5s) {
                        hmeCosPatchPdaVO5.setLabCode(labCode);
                        hmeCosPatchPdaVO5.setLabRemark(labRemark);
                        hmeCosPatchPdaVO5.setNumber(number);
                        number++;
                        hmeCosPatchPdaVO5.setWafer(hmeCosOperationRecord.getWafer());
                        if (Objects.isNull(hmeCosPatchPdaVO5.getSiteOutDate())) {
                            hmeCosPatchPdaVO5.setStatus("未确认");
                            hmeCosPatchPdaVO5.setStatusFlag("N");
                        } else {
                            hmeCosPatchPdaVO5.setStatus("已确认");
                            hmeCosPatchPdaVO5.setStatusFlag("Y");
                        }
                    }
                    result.setMaterialLotList(hmeCosPatchPdaVO5s);
                }
            }
            //工单工艺在制记录ID
            HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
            hmeWoJobSn.setTenantId(tenantId);
            hmeWoJobSn.setSiteId(dto.getSiteId());
            hmeWoJobSn.setWorkOrderId(workOrderId);
            hmeWoJobSn.setOperationId(operationId);
            HmeWoJobSn hmeWoJobSnDb = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
            result.setWoJobSnId(hmeWoJobSnDb.getWoJobSnId());
            //来料信息记录ID
            HmeCosOperationRecord hmeCosOperationRecordDb = hmeCosPatchPdaMapper.cosOpRecordQuery(tenantId, dto.getSiteId(), workOrderId, operationId,
                    dto.getWorkcellId(), hmeCosOperationRecord.getWafer(), equipmentId);
            result.setOperationRecordId(hmeCosOperationRecordDb.getOperationRecordId());
            result.setWafer(hmeCosOperationRecord.getWafer());
            //2020-09-14 add by chaonan.hu for zhenyong.ban 增加剩余芯片数返回
            result.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum());
            //2020-09-24 add by chaonan.hu for zhenyong.ban 增加costype返回
            result.setCosType(hmeCosOperationRecord.getCosType());
            //2020-09-24 add by chaonan.hu for zhenyong.ban 增加可新增数量返回
            BigDecimal qty = hmeCosPatchPdaMapper.getAddQty(tenantId, dto.getWorkcellId(), operationId, hmeCosOperationRecord.getWafer(), "COS_PASTER_OUT");
            result.setAddQty(new BigDecimal(result.getSurplusCosNum()).subtract(qty));
            //工单信息
            MtWorkOrder mtWorkOrderDb = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
            result.setWorkOrderId(workOrderId);
            result.setWorkOrderNum(mtWorkOrderDb.getWorkOrderNum());
            result.setWorkOrderQty(new BigDecimal(mtWorkOrderDb.getQty()));
            if (StringUtils.isNotEmpty(mtWorkOrderDb.getMaterialId())) {
                result.setMaterialId(mtWorkOrderDb.getMaterialId());
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtWorkOrderDb.getMaterialId());
                result.setMaterialCode(mtMaterial.getMaterialCode());
            }
            //投料数量
            BigDecimal assembleQty = BigDecimal.ZERO;
            if (dto.getAssembleQty() != null) {
                assembleQty = dto.getAssembleQty();
            }
            List<MtWorkOrderComponentActual> workOrderComponentActuals = woComActualRepository.select(new MtWorkOrderComponentActual() {{
                setTenantId(tenantId);
                setWorkOrderId(workOrderId);
                setMaterialId(hmeCosOperationRecord.getMaterialId());
            }});
            if (CollectionUtils.isNotEmpty(workOrderComponentActuals)) {
                assembleQty = assembleQty.add(new BigDecimal(workOrderComponentActuals.get(0).getAssembleQty()));
            }
            result.setAssembleQty(assembleQty);
            //2020-10-01 add by chaonan.hu for zhenyong.ban 增加完工数量、工单贴片完成数返回
            HmeCosPatchPdaDTO5 hmeCosPatchPdaDTO5 = new HmeCosPatchPdaDTO5();
            hmeCosPatchPdaDTO5.setWorkcellId(dto.getWorkcellId());
            hmeCosPatchPdaDTO5.setWorkOrderId(workOrderId);
            hmeCosPatchPdaDTO5.setWafer(hmeCosOperationRecord.getWafer());
            HmeCosOperationRecord cosOperationRecord = hmeCosPatchPdaMapper.opRecordQuery(tenantId, operationId, hmeCosPatchPdaDTO5);
            result.setCompletedQty(new BigDecimal(cosOperationRecord.getCosNum() - cosOperationRecord.getSurplusCosNum()));
            //工单贴片完成数
            BigDecimal achieveQty = hmeCosPatchPdaMapper.getAchieveQty(tenantId, operationId, workOrderId);
            result.setAchieveQty(achieveQty);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosPatchPdaVO10 scanBarcode(Long tenantId, HmeCosPatchPdaDTO dto) {
        String operationId = dto.getOperationIdList().get(0);

        MtMaterialLot mtMaterialLot = materialTransferRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotCode());
        if (Objects.isNull(mtMaterialLot) || !YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        if (!OK.equals(mtMaterialLot.getQualityStatus())) {
            throw new MtException("HME_TIME_PROCESS_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0005", "HME", mtMaterialLot.getQualityStatus()));
        }
        if (YES.equals(mtMaterialLot.getFreezeFlag()) || YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //2020-09-25 add by chaonan.hu for zhenyong.ban
        //扫描投入条码时，校验条码库位的库位类别是否是INVENTORY
        if (StringUtils.isNotEmpty(mtMaterialLot.getLocatorId())) {
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
            if (!INVENTORY.equals(mtModLocator.getLocatorCategory())) {
                throw new MtException("HME_COS_PATCH_PDA_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0013", "HME", mtModLocator.getLocatorCode()));
            }
        } else {
            throw new MtException("HME_COS_PATCH_PDA_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0014", "HME"));
        }

        //工单工艺工位在制记录
        String cosRecord = null;
        List<MtExtendAttrVO> mtExtendAttrVOS = new ArrayList<>();
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("COS_RECORD");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isEmpty(mtExtendAttrVOS)) {
            throw new MtException("HME_TIME_PROCESS_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0009", "HME"));
        }
        cosRecord = mtExtendAttrVOS.get(0).getAttrValue();
        HmeCosOperationRecord cosOperationRecord = cosOperationRecordRepository.selectByPrimaryKey(cosRecord);
        String workOrderId = cosOperationRecord.getWorkOrderId();
        //Wafer编码
        String wafer = null;
        mtMaterialLotAttrVO2.setAttrName("WAFER_NUM");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isEmpty(mtExtendAttrVOS)) {
            throw new MtException("HME_COS_PATCH_PDA_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0001", "HME"));
        }
        wafer = mtExtendAttrVOS.get(0).getAttrValue();

        // 20211126 add by sanfeng.zhang for peng.zhao 对来料记录加锁
        String objectRecordCode = this.spliceObjectRecordCode(tenantId, dto.getSiteId(), workOrderId, operationId, dto.getWorkcellId(), wafer, dto.getEquipmentCode());
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("贴片平台");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.RECORD);
        hmeObjectRecordLockDTO.setObjectRecordId("");
        hmeObjectRecordLockDTO.setObjectRecordCode(objectRecordCode);
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, Collections.singletonList(hmeObjectRecordLock));

        HmeCosPatchPdaVO10 result = new HmeCosPatchPdaVO10();
        try {
            List<String> materialLotIdList = new ArrayList<>();
            materialLotIdList.add(mtMaterialLot.getMaterialLotId());
            List<HmeCosPatchPdaVO9> labCodeAndRemarkAttrList = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, materialLotIdList);
            String sourceMaterialLotLabCode = null;
            String sourceMaterialLotLabRemark = null;
            if(CollectionUtils.isNotEmpty(labCodeAndRemarkAttrList)){
                sourceMaterialLotLabCode = labCodeAndRemarkAttrList.get(0).getLabCode();
                sourceMaterialLotLabRemark = labCodeAndRemarkAttrList.get(0).getRemark();
            }
            //2021-11-08 13:46 add by chaonan.hu for yiming.zhang 增加对条码实验代码与芯片实验代码是否一致的校验
            List<String> chipLabCodeList = hmeCosGetChipPlatformMapper.chipLabCodeQuery(tenantId, mtMaterialLot.getMaterialLotId());
            if(CollectionUtils.isEmpty(chipLabCodeList)){
                //未查询到条码${1}上的芯片信息,请检查!
                throw new MtException("HME_LAB_CODE_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LAB_CODE_009", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            if(StringUtils.isBlank(sourceMaterialLotLabCode)){
                //当条码实验代码为空时，如果有芯片的实验代码不为空，则不一致
                long chipLabCodeNotNullCount = chipLabCodeList.stream().filter(item -> StringUtils.isNotBlank(item)).count();
                if(chipLabCodeNotNullCount != 0){
                    //扫描条码的实验代码与芯片上的实验代码不一致,请检查!
                    throw new MtException("HME_LAB_CODE_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_008", "HME"));
                }
            }else {
                //当条码实验代码不为空时，如果有芯片的实验代码不等于条码实验代码，则不一致
                String finalSourceMaterialLotLabCode = sourceMaterialLotLabCode;
                long chipLabCodeNotEqualsCount = chipLabCodeList.stream().filter(item -> !finalSourceMaterialLotLabCode.equals(item)).count();
                if(chipLabCodeNotEqualsCount != 0){
                    //扫描条码的实验代码与芯片上的实验代码不一致,请检查!
                    throw new MtException("HME_LAB_CODE_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_008", "HME"));
                }
            }

            //2021-10-27 add by chaonan.hu for yiming.zhang 增加对条码实验代码、备注扩展属性的校验
            if(Objects.nonNull(dto.getMaterialLotInfo()) && StringUtils.isNotBlank(dto.getMaterialLotInfo().getMaterialLotId())){
                if(!dto.getMaterialLotInfo().getLabCode().equals(sourceMaterialLotLabCode) || !dto.getMaterialLotInfo().getLabRemark().equals(sourceMaterialLotLabRemark)){
                    throw new MtException("HME_LAB_CODE_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_003", "HME"));
                }
            }
            //2020-12-22 add by chaonan.hu for zhenyong.ban 增加对上一步骤的校验
            hmeTimeProcessPdaRepository.operationVerify(tenantId, mtMaterialLot.getMaterialLotId(), operationId);

//        //容器类型
//        String containerType = null;
//        mtMaterialLotAttrVO2.setAttrName("CONTAINER_TYPE");
//        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
//        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS)){
//            containerType = mtExtendAttrVOS.get(0).getAttrValue();
//        }
            //COS类型
            String cosType = null;
            mtMaterialLotAttrVO2.setAttrName("COS_TYPE");
            mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                cosType = mtExtendAttrVOS.get(0).getAttrValue();
            }
            //校验最近行的工单ID和Wafer与投入芯片盒子的工单ID及Wafer需一致
            String equipmentId = null;
            if (StringUtils.isNotEmpty(dto.getEquipmentCode())) {
                HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                    setTenantId(tenantId);
                    setAssetEncoding(dto.getEquipmentCode());
                }});
                equipmentId = hmeEquipment.getEquipmentId();
            }
            HmeCosOperationRecord hmeCosOperationRecord = hmeCosPatchPdaMapper.recordQuery(tenantId, dto.getWorkcellId(), operationId, equipmentId);
            if (hmeCosOperationRecord != null) {
                if (!hmeCosOperationRecord.getWorkOrderId().equals(workOrderId) || !hmeCosOperationRecord.getWafer().equals(wafer)) {
                    MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeCosOperationRecord.getWorkOrderId());
                    throw new MtException("HME_COS_PATCH_PDA_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_PDA_0002", "HME", mtWorkOrder.getWorkOrderNum(), hmeCosOperationRecord.getWafer()));
                }
            }

            //根据material_lot_id+工艺ID+作业平台类型等于COS_FETCH_IN校验是否存在出站时间为空的记录
            //edit by chaonan.hu for zhenyong.ban COS贴片平台涉及到的平台类型由COS_FETCH_IN修改为COS_PASTER_IN

            //2020-09-27 edit by chaonan.hu for zhenyong.ban
            hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, mtMaterialLot.getMaterialLotId());
            //创建或者更新来料信息记录
            //根据工单ID+工艺+Wkc_ID+Wafer+设备ID（可为空）查询表hme_cos_operation_record数据，不存在则新增，如存在则更新
            HmeCosOperationRecord hmeCosOperationRecordDb = hmeCosPatchPdaMapper.cosOpRecordQuery(tenantId, dto.getSiteId(), workOrderId, operationId,
                    dto.getWorkcellId(), wafer, equipmentId);
            if (hmeCosOperationRecordDb == null) {
                // 新增记录时 如果存在未出站记录 则不允许新增
                if (!hmeEoJobSnRepository.checkNotSiteOutByWkcId(tenantId, dto.getWorkcellId(), "COS_PASTER_IN")) {
                    throw new MtException("HME_COS_PATCH_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_0007", "HME"));
                }
                hmeCosOperationRecordDb = new HmeCosOperationRecord();
                hmeCosOperationRecordDb.setTenantId(tenantId);
                hmeCosOperationRecordDb.setSiteId(dto.getSiteId());
                hmeCosOperationRecordDb.setWorkOrderId(workOrderId);
                hmeCosOperationRecordDb.setWafer(wafer);
                hmeCosOperationRecordDb.setCosNum(mtMaterialLot.getPrimaryUomQty().longValue());
                hmeCosOperationRecordDb.setOperationId(operationId);
                hmeCosOperationRecordDb.setWorkcellId(dto.getWorkcellId());
                hmeCosOperationRecordDb.setCosType(cosType);
                hmeCosOperationRecordDb.setEquipmentId(equipmentId);
                hmeCosOperationRecordDb.setSurplusCosNum(mtMaterialLot.getPrimaryUomQty().longValue());
                hmeCosOperationRecordDb.setMaterialId(mtMaterialLot.getMaterialId());
                hmeCosOperationRecordDb.setAttribute2(mtMaterialLot.getSupplierId());
                //2020-09-28 15:40 add by chaonan.hu for zhenyong.ban 增加字段插入到来料信息记录表中
                //container_type_id
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO22 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO22.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotAttrVO22.setAttrName("CONTAINER_TYPE");
                List<MtExtendAttrVO> mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2)) {
                    String containerTypeCode = mtExtendAttrVOS2.get(0).getAttrValue();
                    MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
                        setTenantId(tenantId);
                        setContainerTypeCode(containerTypeCode);
                    }});
                    if (mtContainerType != null) {
                        hmeCosOperationRecordDb.setContainerTypeId(mtContainerType.getContainerTypeId());
                    }
                }
                //average_wavelength
                mtMaterialLotAttrVO22.setAttrName("AVG_WAVE_LENGTH");
                mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                    hmeCosOperationRecordDb.setAverageWavelength(new BigDecimal(mtExtendAttrVOS2.get(0).getAttrValue()));
                }
                //type
                mtMaterialLotAttrVO22.setAttrName("TYPE");
                mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2)) {
                    hmeCosOperationRecordDb.setType(mtExtendAttrVOS2.get(0).getAttrValue());
                }
                //lot_no
                mtMaterialLotAttrVO22.setAttrName("LOTNO");
                mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2)) {
                    hmeCosOperationRecordDb.setLotNo(mtExtendAttrVOS2.get(0).getAttrValue());
                }
                //remark
                mtMaterialLotAttrVO22.setAttrName("REMARK");
                mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2)) {
                    hmeCosOperationRecordDb.setRemark(mtExtendAttrVOS2.get(0).getAttrValue());
                }
                cosOperationRecordRepository.insertSelective(hmeCosOperationRecordDb);

                // 保存历史记录
                HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
                BeanUtils.copyProperties(hmeCosOperationRecordDb, hmeCosOperationRecordHis);
                hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            } else {
                hmeCosOperationRecordDb.setCosNum(hmeCosOperationRecordDb.getCosNum() +
                        mtMaterialLot.getPrimaryUomQty().longValue());
                hmeCosOperationRecordDb.setSurplusCosNum(hmeCosOperationRecordDb.getSurplusCosNum() +
                        mtMaterialLot.getPrimaryUomQty().longValue());
                hmeCosOperationRecordDb.setMaterialId(mtMaterialLot.getMaterialId());
                hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecordDb);
                // 保存历史记录
                HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
                BeanUtils.copyProperties(hmeCosOperationRecordDb, hmeCosOperationRecordHis);
                hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            }
            //创建来源条码作业记录
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(new Date());
            hmeEoJobSn.setShiftId(dto.getWkcShiftId());
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? 1L : curUser.getUserId();
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(workOrderId);
            hmeEoJobSn.setOperationId(operationId);
            hmeEoJobSn.setSnMaterialId(mtMaterialLot.getMaterialId());
            hmeEoJobSn.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobSn.setReworkFlag("N");
            hmeEoJobSn.setJobType("COS_PASTER_IN");
            hmeEoJobSn.setSourceJobId(hmeCosOperationRecordDb.getOperationRecordId());
            hmeEoJobSn.setSnQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
            // 20210311 edit by chaonan.hu for zhenyong.ban 工位、eo、条码、返修标识、job_type取出最大的eo_step_num + 1
            Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeEoJobSn.getWorkcellId(), hmeEoJobSn.getEoId(), hmeEoJobSn.getMaterialLotId(), HmeConstants.ConstantValue.NO, hmeEoJobSn.getJobType(), hmeEoJobSn.getOperationId());
            hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
//        // 查询最近的条码历史记录到hme_eo_job_sn表中attribute3
//        String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, mtMaterialLot.getMaterialLotId());
            hmeEoJobSn.setAttribute3(cosType);
            hmeEoJobSn.setAttribute5(wafer);
            hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
            hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, Collections.singletonList(hmeEoJobSn.getJobId()), dto.getWorkcellId());

            //创建或者更新工单工艺在制记录
            //根据工单+工艺查询表hme_wo_job_sn中数据，不存在则新增，如存在则更新
            HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
            hmeWoJobSn.setTenantId(tenantId);
            hmeWoJobSn.setSiteId(dto.getSiteId());
            hmeWoJobSn.setWorkOrderId(workOrderId);
            hmeWoJobSn.setOperationId(operationId);
            HmeWoJobSn hmeWoJobSnDb = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
            if (hmeWoJobSnDb == null) {
                hmeWoJobSn.setSiteInNum(mtMaterialLot.getPrimaryUomQty().longValue());
                hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
            } else {
                hmeWoJobSnDb.setSiteInNum(hmeWoJobSnDb.getSiteInNum() + mtMaterialLot.getPrimaryUomQty().longValue());
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSnDb);
            }
            result.setLabCode(sourceMaterialLotLabCode);
            result.setLabRemark(sourceMaterialLotLabRemark);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , Collections.singletonList(hmeObjectRecordLock) , HmeConstants.ConstantValue.YES);
        }
        return result;
    }

    public String spliceObjectRecordCode(Long tenantId, String siteId, String workOrderId, String operationId, String workcellId, String wafer, String equipmentId) {
        StringBuffer sb = new StringBuffer();
        sb.append(tenantId);
        sb.append(siteId);
        sb.append(workOrderId);
        sb.append(operationId);
        sb.append(workcellId);
        sb.append(wafer);
        sb.append(equipmentId);
        return sb.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosPatchPdaDTO2> delete(Long tenantId, List<HmeCosPatchPdaDTO2> dtoList) {
        for (HmeCosPatchPdaDTO2 dto : dtoList) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
            long primaryUomQty = mtMaterialLot.getPrimaryUomQty().longValue();
            //删除来源条码作业记录 2021-06-03 09:33 add by chaonan.hu for zhenyong.ban 限制必须找到未出站的job_id
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
            if(Objects.isNull(hmeEoJobSn)){
                throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_010", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            hmeEoJobSnRepository.deleteByPrimaryKey(dto.getJobId());
            //更新工单工艺在制记录
            HmeWoJobSn hmeWoJobSn = hmeWoJobSnRepository.selectByPrimaryKey(dto.getWoJobSnId());
            hmeWoJobSn.setSiteInNum(hmeWoJobSn.getSiteInNum() - primaryUomQty);
            hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
            //更新来料信息记录
            //2020-09-18 10:21 add by chaonan.hu for zhenyong.ban 增加剩余芯片数是否小于条码数量的校验
            HmeCosOperationRecord hmeCosOperationRecord = hmeCosOperationRecordRepository.selectByPrimaryKey(dto.getOperationRecordId());
            if (hmeCosOperationRecord.getSurplusCosNum() < primaryUomQty) {
                throw new MtException("HME_COS_PATCH_PDA_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0009", "HME"));
            }
            hmeCosOperationRecord.setCosNum(hmeCosOperationRecord.getCosNum() - primaryUomQty);
            hmeCosOperationRecord.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum() - primaryUomQty);
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHis.setTenantId(tenantId);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosPatchPdaDTO3> siteIn(Long tenantId, List<HmeCosPatchPdaDTO3> dtoList) {
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_PASTER_IN");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtCommonExtendVO7> mtCommonExtendVO7List = new ArrayList<>();

        List<String> materialLotIdList = dtoList.stream().map(HmeCosPatchPdaDTO3::getMaterialLotId).collect(Collectors.toList());
        List<HmeCosPatchPdaVO9> materialLotAttrList = hmeCosPatchPdaMapper.labCodeRemarkAndWoAttrQuery(tenantId, materialLotIdList);
        List<HmeMaterialLotLoad> loadSequenceList = hmeCosPatchPdaMapper.loadSequenceQueryByMaterialLotId(tenantId, materialLotIdList);
        List<HmeMaterialLotLabCode> insertMaterialLotLabCodeList = new ArrayList<>();

        for (HmeCosPatchPdaDTO3 dto : dtoList) {
            String operationId = dto.getOperationIdList().get(0);
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
            //2021-04-22 14:20 edit by chaonan.hu for kang.wang 物料批更新改为批量
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLotVO20.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() * -1);
            mtMaterialLotVO20.setEnableFlag("N");
            mtMaterialLotVO20List.add(mtMaterialLotVO20);

//            //调用API{materialLotUpdate} 更新来源条码
//            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//            mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
//            mtMaterialLotVO2.setEventId(eventId);
//            mtMaterialLotVO2.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() * -1);
//            //2020-09-12 14:13 edit by chaonan.hu for zhenyong.ban 有效性改为N,质量状态改为NG
//            mtMaterialLotVO2.setEnableFlag("N");
//            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);

            //2021-04-22 14:27 edit by chaonan.hu for kang.wang 物料批扩展属性更新改为批量
            MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
            mtCommonExtendVO7.setKeyId(dto.getMaterialLotId());
            List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
            MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
            mtCommonExtendVO4.setAttrName("CURRENT_ROUTER_STEP");
            mtCommonExtendVO4.setAttrValue(null);
            mtCommonExtendVO4List.add(mtCommonExtendVO4);
            mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
            mtCommonExtendVO7List.add(mtCommonExtendVO7);

//            //2020-12-22 add by chaonan.hu for zhenyong.ban 删除条码的扩展属性CURRENT_ROUTER_STEP
//            MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
//            mtMaterialLotAttrVO3.setEventId(eventId);
//            mtMaterialLotAttrVO3.setMaterialLotId(dto.getMaterialLotId());
//            List<MtExtendVO5> attrList = new ArrayList<>();
//            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//            mtExtendVO5.setAttrName("CURRENT_ROUTER_STEP");
//            mtExtendVO5.setAttrValue(null);
//            attrList.add(mtExtendVO5);
//            mtMaterialLotAttrVO3.setAttr(attrList);
//            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
            //更新来源条码作业记录
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? 1L : curUser.getUserId();
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setSiteOutDate(new Date());
            hmeEoJobSnRepository.updateByPrimaryKeySelective(hmeEoJobSn);
            //2021-11-09 add by chaonan.hu for yiming.zhang 记录数据到实验代码表中
            List<HmeCosPatchPdaVO9> singleMaterialLotAttrList = materialLotAttrList.stream().filter(item -> dto.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
            HmeCosPatchPdaVO9 singleMaterialLotAttr = singleMaterialLotAttrList.get(0);
            if(StringUtils.isNotBlank(singleMaterialLotAttr.getLabCode())){
                List<HmeMaterialLotLoad> singleLoadSequenceList = loadSequenceList.stream().filter(item -> dto.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                for (HmeMaterialLotLoad hmeMaterialLotLoad:singleLoadSequenceList) {
                    HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    hmeMaterialLotLabCode.setTenantId(tenantId);
                    hmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                    hmeMaterialLotLabCode.setObject("COS");
                    hmeMaterialLotLabCode.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                    hmeMaterialLotLabCode.setLabCode(singleMaterialLotAttr.getLabCode());
                    hmeMaterialLotLabCode.setJobId(dto.getJobId());
                    hmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                    if(StringUtils.isNotBlank(singleMaterialLotAttr.getWorkOrderId())){
                        hmeMaterialLotLabCode.setWorkOrderId(singleMaterialLotAttr.getWorkOrderId());
                    }
                    hmeMaterialLotLabCode.setSourceObject("MA");
                    hmeMaterialLotLabCode.setEnableFlag("Y");
                    if(StringUtils.isNotBlank(singleMaterialLotAttr.getRemark())){
                        hmeMaterialLotLabCode.setAttribute1(singleMaterialLotAttr.getRemark());
                    }
                    insertMaterialLotLabCodeList.add(hmeMaterialLotLabCode);
                }
            }
            //2020-11-23 edit by chaonan.hu for zhenyong.ban 注释掉下面逻辑
//            //来源条码工单生产投料
//            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
//            String bomComponentId = null;
//            //2020-09-25 13:52 edit by chaonan.hu for zhenyong.ban
//            //获取Bom_component_id逻辑修改以及传入API参数修改 原先逻辑下方先暂留
//            if (StringUtils.isNotEmpty(mtWorkOrder.getBomId())) {
//                List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
//                    setTenantId(tenantId);
//                    setBomId(mtWorkOrder.getBomId());
//                    setMaterialId(mtMaterialLot.getMaterialId());
//                }});
//                if (CollectionUtils.isEmpty(mtBomComponentList)) {
//                    throw new MtException("HME_COS_PATCH_PDA_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "HME_COS_PATCH_PDA_0011", "HME"));
//                } else if (mtBomComponentList.size() > 1) {
//                    throw new MtException("HME_COS_PATCH_PDA_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "HME_COS_PATCH_PDA_0012", "HME"));
//                }
//                bomComponentId = mtBomComponentList.get(0).getBomComponentId();
//            }
//            MtEventCreateVO eventCreateVO2 = new MtEventCreateVO();
//            eventCreateVO2.setEventTypeCode("COS_PASTER_IN");
//            String eventId2 = mtEventRepository.eventCreate(tenantId, eventCreateVO);
//            //2020-09-24 add by chaonan.hu for zhenyong.ban 增加报错校验
//            if (StringUtils.isEmpty(bomComponentId)) {
//                throw new MtException("HME_COS_PATCH_PDA_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_COS_PATCH_PDA_0011", "HME"));
//            }
//            //调用API{woComponentAssemble}进行生产指令组件装配
//            MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
//            mtWoComponentActualVO1.setBomComponentId(bomComponentId);
//            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "COS_PASTER");
//            mtWoComponentActualVO1.setEventRequestId(eventRequestId);
//            mtWoComponentActualVO1.setLocatorId(mtMaterialLot.getLocatorId());
//            mtWoComponentActualVO1.setMaterialId(mtMaterialLot.getMaterialId());
//            mtWoComponentActualVO1.setOperationId(operationId);
//            mtWoComponentActualVO1.setParentEventId(eventId2);
//            //根据wkc_shift_id查询班次编码、班次日期
//            MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());
//            mtWoComponentActualVO1.setShiftCode(mtWkcShift.getShiftCode());
//            mtWoComponentActualVO1.setShiftDate(mtWkcShift.getShiftDate());
//            mtWoComponentActualVO1.setTrxAssembleQty(mtMaterialLot.getPrimaryUomQty());
//            mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
//            mtWoComponentActualVO1.setWorkOrderId(dto.getWorkOrderId());
//            woComActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);
//            //来源条码现有量扣减
//            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
//            mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
//            mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
//            mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
//            mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
//            mtInvOnhandQuantityVO9.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
//            mtInvOnhandQuantityVO9.setEventId(eventId);
//            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
//            //来源条码工单生产投料事务记录
//            WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
//            objectTransactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE");
//            objectTransactionRequestVO.setEventId(eventId);
//            objectTransactionRequestVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//            objectTransactionRequestVO.setMaterialId(mtMaterialLot.getMaterialId());
//            objectTransactionRequestVO.setTransactionQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
//            objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
//            if (StringUtils.isNotEmpty(mtMaterialLot.getPrimaryUomId())) {
//                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
//                objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
//            }
//            objectTransactionRequestVO.setTransactionTime(new Date());
//            objectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
//            objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
//            objectTransactionRequestVO.setMergeFlag("N");
//            WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
//                setTenantId(tenantId);
//                setTransactionTypeCode("HME_WO_ISSUE");
//            }});
//            objectTransactionRequestVO.setMoveType(wmsTransactionType.getMoveType());
//            //2020-10-03 10:51 add by chaonan.hu for zhenyong.ban 增加BomReserveNum传入
//            MtExtendVO mtExtendVO = new MtExtendVO();
//            mtExtendVO.setTableName("mt_bom_component_attr");
//            mtExtendVO.setKeyId(bomComponentId);
//            mtExtendVO.setAttrName("lineAttribute10");
//            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
//            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
//                objectTransactionRequestVO.setBomReserveNum(mtExtendAttrVOS.get(0).getAttrValue());
//            }
//            //工单行号
//            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
//            objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
//            //仓库
//            List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, mtMaterialLot.getLocatorId(), "TOP");
//            if (CollectionUtils.isNotEmpty(pLocatorIds)) {
//                MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
//                if (ploc != null) {
//                    objectTransactionRequestVO.setWarehouseCode(ploc.getLocatorCode());
//                    objectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
//                }
//            }
//            //货位
//            MtModLocator loc = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
//            if (loc != null) {
//                objectTransactionRequestVO.setLocatorId(mtMaterialLot.getLocatorId());
//                objectTransactionRequestVO.setLocatorCode(loc.getLocatorCode());
//            }
//            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
        }

        //批量更新物料批
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
        //批量更新物料批扩展属性
        if(CollectionUtils.isNotEmpty(mtCommonExtendVO7List)){
            mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, mtCommonExtendVO7List);
        }
        if(CollectionUtils.isNotEmpty(insertMaterialLotLabCodeList)){
            List<String> sqlList = new ArrayList<>();
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            Date nowDate = new Date();
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_material_lot_lab_code_s", insertMaterialLotLabCodeList.size());
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", insertMaterialLotLabCodeList.size());
            int i = 0;
            for (HmeMaterialLotLabCode hmeMaterialLotLabCode:insertMaterialLotLabCodeList) {
                hmeMaterialLotLabCode.setCreatedBy(userId);
                hmeMaterialLotLabCode.setCreationDate(nowDate);
                hmeMaterialLotLabCode.setLastUpdatedBy(userId);
                hmeMaterialLotLabCode.setLastUpdateDate(nowDate);
                hmeMaterialLotLabCode.setLabCodeId(ids.get(i));
                hmeMaterialLotLabCode.setCid(Long.valueOf(cids.get(i)));
                hmeMaterialLotLabCode.setObjectVersionNumber(1L);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeMaterialLotLabCode));
                i++;
            }
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosPatchPdaVO3 siteOut(Long tenantId, HmeCosPatchPdaDTO4 dto, String subLocatorId) {
        HmeCosPatchPdaVO3 result = new HmeCosPatchPdaVO3();
        List<HmeCosPatchPdaVO2> resultList = new ArrayList<>();
        HmeCosPatchPdaVO2 hmeCosPatchPdaVO2 = new HmeCosPatchPdaVO2();
        hmeCosPatchPdaVO2.setStatus("新建");
        String operationId = dto.getOperationIdList().get(0);
        Date nowDate = new Date();
        long number = 0;
        if (dto.getMaxNumber() != null) {
            number = dto.getMaxNumber();
        }
        //根据工单ID+工艺+Wkc_ID从hme_eo_job_sn表内获取job_type等于“COS_PASTER_IN”，进站时间最近行项目的material_lot_id
        String materialLotId = hmeCosPatchPdaMapper.getMaterialLotId(tenantId, dto.getWorkOrderId(), operationId, dto.getWorkcellId());
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("HME_COS_PATCH_PDA_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0010", "HME"));
        }
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
        //COS类型
        String cosType = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "COS_TYPE");
        //根据工艺+目标容器类型+当前工位在制COS类型，通过hme_container_capacity容器容量表获取目标容器：芯片
        // 数
        HmeContainerCapacity hmeContainerCapacity = hmeContainerCapacityRepository.selectOne(new HmeContainerCapacity() {{
            setOperationId(operationId);
            setCosType(cosType);
            setContainerTypeId(dto.getContainerTypeId());
        }});
        if (hmeContainerCapacity == null) {
            throw new MtException("HME_COS_PATCH_PDA_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0005", "HME"));
        }
        //MtContainerType mtContainerType = mtContainerTypeRepository.selectByPrimaryKey(hmeContainerCapacity.getContainerTypeId());
        //芯片数量校验
        //单盒待转移芯片数=目标容器行数*目标容器列数*目标容器芯片数 取值改为前台传入的条码芯片数
        long singleChipTotal = dto.getQty().longValue();
        hmeCosPatchPdaVO2.setQty(new BigDecimal(singleChipTotal));
        //总待转移芯片数
        long totalChipTotal = singleChipTotal * dto.getBoxTotal();
        //查询芯片剩余总数
        HmeCosOperationRecord hmeCosOperationRecordDb = hmeCosPatchPdaMapper.cosOpRecordQuery(tenantId, dto.getSiteId(), dto.getWorkOrderId(), operationId,
                dto.getWorkcellId(), dto.getWafer(), dto.getEquipmentId());
        if (hmeCosOperationRecordDb == null) {
            throw new MtException("HME_COS_PATCH_PDA_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0006", "HME"));
        }
        Long surplusCosNum = hmeCosOperationRecordDb.getSurplusCosNum();
        result.setCompletedQty(new BigDecimal(
                hmeCosOperationRecordDb.getCosNum() - hmeCosOperationRecordDb.getSurplusCosNum()));
        //2020-09-08 10:26 edit by chaonan.hu for zhenyong.ban
        // 数量校验改为总待转移芯片数需小于等于（芯片剩余总数 - 已生成状态为未打印（出站时间为空）的条码数量）
        //查询未打印状态的条码数量
        BigDecimal noSiteOutQty = hmeCosPatchPdaMapper.getNoSiteOutMaLotQty(tenantId, hmeCosOperationRecordDb.getOperationRecordId());
        if (totalChipTotal > (surplusCosNum.longValue() - noSiteOutQty.longValue())) {
            throw new MtException("HME_COS_PATCH_PDA_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0004", "HME"));
        }
        //获取来源条码的扩展属性
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
        List<MtExtendVO5> attrs = getMtExtendVO5List(tenantId, mtMaterialLot, hmeCosOperationRecordDb, hmeContainerCapacity, cosType);
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        //2020-12-22 add by chaonan.hu doe zhenyong.ban 根据工单查询到工步及工艺之间的关联关系
        List<HmeCosGetChipPlatformVO> hmeCosGetChipPlatformVOS = hmeCosGetChipPlatformMapper.routerStepAndOperationQuery(tenantId, mtWorkOrder.getWorkOrderId());
        if (CollectionUtils.isEmpty(hmeCosGetChipPlatformVOS)) {
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_028", "HME"));
        }
        //根据当前登录工位对应的工艺在关联关系中查找到对应工步,将该工步写入到目标条码扩展属性中
        List<HmeCosGetChipPlatformVO> afterFilterList = hmeCosGetChipPlatformVOS.stream().filter(item -> operationId.equals(item.getOperationId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(afterFilterList)) {
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_028", "HME"));
        }
        String currentRouterStep = afterFilterList.get(0).getRouterStepId();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("CURRENT_ROUTER_STEP");
        mtExtendVO5.setAttrValue(currentRouterStep);
        attrs.add(mtExtendVO5);
        //2021-02-25 add by chaonan.hu for zhenyong.ban 增加金锡比记录到扩展属性表中
        if (StringUtils.isNotBlank(dto.getAuSnRatio())) {
            MtExtendVO5 auSnRatioAttr = new MtExtendVO5();
            auSnRatioAttr.setAttrName("AUSN_RATIO");
            auSnRatioAttr.setAttrValue(dto.getAuSnRatio());
            attrs.add(auSnRatioAttr);
        }
        //2021-10-27 add by chaonan.hu for yiming.zhang 增加实验代码与备注记录到扩展属性表中
        if(StringUtils.isNotBlank(dto.getLabCode())){
            MtExtendVO5 labCodeAttr = new MtExtendVO5();
            labCodeAttr.setAttrName("LAB_CODE");
            labCodeAttr.setAttrValue(dto.getLabCode());
            attrs.add(labCodeAttr);
        }
        if(StringUtils.isNotBlank(dto.getLabRemark())){
            MtExtendVO5 remarkAttr = new MtExtendVO5();
            remarkAttr.setAttrName("LAB_REMARK");
            remarkAttr.setAttrValue(dto.getLabRemark());
            attrs.add(remarkAttr);
        }
        mtExtendVO10.setAttrs(attrs);
        //创建出站条码
        //调用API{materialLotUpdate}, 根据出站盒数创建出站条码
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_PASTER_OUT");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setSiteId(mtMaterialLot.getSiteId());
        mtMaterialLotVO2.setEnableFlag("Y");
        mtMaterialLotVO2.setQualityStatus(mtMaterialLot.getQualityStatus());
        //2020-09-12 edit by chaonan.hu for zhenyong.ban 物料ID改为取工单上的物料
        mtMaterialLotVO2.setMaterialId(mtWorkOrder.getMaterialId());
        //2020-09-14 edit by chaonan.hu for zhenyong.ban 主单位Id改为工单上物料的主单位Id,并增加更新eo上物料为工单上物料的逻辑
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtWorkOrder.getMaterialId());
        mtMaterialLotVO2.setPrimaryUomId(mtMaterial.getPrimaryUomId());
        mtMaterialLotVO2.setTrxPrimaryUomQty((double) singleChipTotal);
        //2020-09-20 10:27 add by chaonan.hu for zhenyong.ban 库位ID改为取工单上的默认完工库位
        //2020-09-22 15:52 edit by chaonan.hu for zhenyong.ban 库位ID改为取工单上的默认完工库位下的子库位
        //2020-10-03 10:40 edit by chaonan.hu for zhenyong.ban 库位ID改为取当前工位的工段或产线下维护的默认存储库位
        mtMaterialLotVO2.setLocatorId(subLocatorId);
        mtMaterialLotVO2.setLoadTime(nowDate);
        mtMaterialLotVO2.setCreateReason("INITIALIZE");
        mtMaterialLotVO2.setInSiteTime(nowDate);
        mtMaterialLotVO2.setEoId(mtMaterialLot.getEoId());
        //2020-09-22 16:29 add by chaonan.hu for zhenyong.ban 批次传入空字符串
        //系统参数HME_COS_MATERIAL_LOT_LOT中的“系统”层的值
        String lot = profileClient.getProfileValueByOptions("HME_COS_MATERIAL_LOT_LOT");
        if (StringUtils.isEmpty(lot)) {
            throw new MtException("HME_COS_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_006", "HME"));
        }
        mtMaterialLotVO2.setLot(lot);
        mtMaterialLotVO2.setSupplierId(hmeCosOperationRecordDb.getAttribute2());
//        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_LIMIT_EO_BATCH_CREATE");
//        eventCreateVO.setEventTypeCode("EO_BATCH_CREATEEO");
//        String parentEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //根据wkcShiftId查询当前班次编码，班次日期
//        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());
        //更新eo上的物料为工单上的物料
//        MtEo mtEo = mtEoRepository.selectByPrimaryKey(mtMaterialLot.getEoId());
//        if(!mtEo.getMaterialId().equals(mtWorkOrder.getMaterialId())){
//            mtEo.setMaterialId(mtWorkOrder.getMaterialId());
//            mtEoMapper.updateByPrimaryKeySelective(mtEo);
//        }
        MtNumrangeVO8 mtNumrangeVO8 = createIncomingValueList(tenantId, dto, Long.parseLong(String.valueOf(dto.getBoxTotal())), cosType);
        List<String> numberList = mtNumrangeVO8.getNumberList();
        List<String> hmeEoJobSnIdList = new ArrayList<>();
        // 查询工位的设备 将编码存入装载表
        List<HmeEquipment> hmeEquipmentList = hmeEoJobEquipmentMapper.queryEquipmentListByWorkCellId(tenantId, dto.getWorkcellId());
        if (CollectionUtils.isNotEmpty(hmeEquipmentList)) {
            List<String> assetEncodingList = hmeEquipmentList.stream().map(HmeEquipment::getAssetEncoding).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            dto.setAssetEncoding(StringUtils.join(assetEncodingList, "/"));
        }
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        Date currentDate = CommonUtils.currentTimeGet();
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        List<String> ids = mtCustomDbRepository.getNextKeys("hme_eo_job_sn_s", dto.getBoxTotal());
        List<String> cids = mtCustomDbRepository.getNextKeys("hme_eo_job_sn_cid_s", dto.getBoxTotal());
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = new ArrayList<>();
        List<HmeLoadJob> hmeLoadJobList = new ArrayList<>();
        List<HmeLoadJobObject> hmeLoadJobObjectList = new ArrayList<>();
        for (int i = 0; i < dto.getBoxTotal(); i++) {
            mtMaterialLotVO2.setMaterialLotCode(numberList.get(i));
            mtMaterialLotVO2.setOnlyInsert(true);
            MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
            MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLotVO13.getMaterialLotId());
            number++;
            hmeCosPatchPdaVO2.setNumber(number);
            hmeCosPatchPdaVO2.setMaterialLotCode(mtMaterialLot1.getMaterialLotCode());
            resultList.add(hmeCosPatchPdaVO2);
            //调用api【materialLotAttrPropertyUpdate】进行目标SN物料批扩展属性新增/更新
            mtExtendVO10.setKeyId(mtMaterialLotVO13.getMaterialLotId());
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
            //2020-12-03 edit by chaonan.hu for zhenyong.ban 不再生成eo,表hmeMaterialLotLoad的loadSequence也不再存eoNum
//            //调用API{woLimitEoBatchCreate}创建出站条码装载EO
//            //没有提到循环外面的原因，因为创建出站条码装载记录需要物料批ID和eoId，
//            //提到外面后就确定不了生成记录的物料批ID和eoId之间的关系，
//            MtEoVO14 mtEoVO14 = new MtEoVO14();
//            mtEoVO14.setEoCount(singleChipTotal + "");
//            mtEoVO14.setEventRequestId(eventRequestId);
//            mtEoVO14.setLocatorId(mtMaterialLot.getLocatorId());
//            mtEoVO14.setParentEventId(parentEventId);
//            mtEoVO14.setShiftCode(mtWkcShift.getShiftCode());
//            mtEoVO14.setShiftDate(mtWkcShift.getShiftDate());
//            mtEoVO14.setTotalQty((double) singleChipTotal);
//            mtEoVO14.setWorkcellId(dto.getWorkcellId());
//            mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
//            mtEoVO14.setCopyFlag("Y");
//            List<String> eoIds = mtEoRepository.woLimitEoBatchCreate(tenantId, mtEoVO14);
            //创建出站条码装载记录
            HmeCosPatchPdaVO8 hmeCosPatchPdaVO8 = containerLoadingEo(tenantId, hmeContainerCapacity,
                    mtMaterialLotVO13.getMaterialLotId(), singleChipTotal, cosType, dto, hmeCosOperationRecordDb);
            if (CollectionUtils.isNotEmpty(hmeCosPatchPdaVO8.getHmeMaterialLotLoadList())) {
                hmeMaterialLotLoadList.addAll(hmeCosPatchPdaVO8.getHmeMaterialLotLoadList());
            }
            if(CollectionUtils.isNotEmpty(hmeCosPatchPdaVO8.getHmeLoadJobList())) {
                hmeLoadJobList.addAll(hmeCosPatchPdaVO8.getHmeLoadJobList());
            }
            if (CollectionUtils.isNotEmpty(hmeCosPatchPdaVO8.getHmeLoadJobObjectList())) {
                hmeLoadJobObjectList.addAll(hmeCosPatchPdaVO8.getHmeLoadJobObjectList());
            }
            //创建出站条码作业记录
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(new Date());
            hmeEoJobSn.setShiftId(dto.getWkcShiftId());

            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobSn.setOperationId(operationId);
            //2020-09-28 edit by chaonan.hu for zhenyong.ban SN物料ID改为取工单上的物料
            hmeEoJobSn.setSnMaterialId(mtWorkOrder.getMaterialId());
            hmeEoJobSn.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
            hmeEoJobSn.setEoStepNum(1);
            hmeEoJobSn.setReworkFlag("N");
            hmeEoJobSn.setJobType("COS_PASTER_OUT");
            hmeEoJobSn.setSourceJobId(hmeCosOperationRecordDb.getOperationRecordId());
            hmeEoJobSn.setSnQty(new BigDecimal(mtMaterialLot1.getPrimaryUomQty()));
//            // 查询最近的条码历史记录到hme_eo_job_sn表中attribute3
//            String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, mtMaterialLotVO13.getMaterialLotId());
            hmeEoJobSn.setAttribute3(hmeCosOperationRecordDb.getCosType());
            hmeEoJobSn.setAttribute5(hmeCosOperationRecordDb.getWafer());
            hmeEoJobSn.setCreatedBy(userId);
            hmeEoJobSn.setCreationDate(currentDate);
            hmeEoJobSn.setLastUpdatedBy(userId);
            hmeEoJobSn.setLastUpdateDate(currentDate);
            hmeEoJobSn.setJobId(ids.get(i));
            hmeEoJobSn.setCid(Long.valueOf(cids.get(i)));
            hmeEoJobSn.setObjectVersionNumber(1L);
            hmeEoJobSnList.add(hmeEoJobSn);
            hmeEoJobSnIdList.add(hmeEoJobSn.getJobId());
        }
        if (CollectionUtils.isNotEmpty(hmeEoJobSnIdList)) {
            hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, hmeEoJobSnIdList, dto.getWorkcellId());
        }
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            List<String> sqlList = new ArrayList<>();
            for (HmeEoJobSn hmeEoJobSn : hmeEoJobSnList) {
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeEoJobSn));
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
            List<String> loadIds = mtCustomDbRepository.getNextKeys("hme_material_lot_load_s", hmeMaterialLotLoadList.size());
            List<String> loadCids = mtCustomDbRepository.getNextKeys("hme_material_lot_load_cid_s", hmeMaterialLotLoadList.size());
            List<String> sqlList = new ArrayList<>();
            Integer indexNum = 0;
            for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoadList) {
                hmeMaterialLotLoad.setMaterialLotLoadId(loadIds.get(indexNum));
                hmeMaterialLotLoad.setObjectVersionNumber(1L);
                hmeMaterialLotLoad.setCid(Long.valueOf(loadCids.get(indexNum++)));
                hmeMaterialLotLoad.setCreationDate(currentDate);
                hmeMaterialLotLoad.setCreatedBy(userId);
                hmeMaterialLotLoad.setLastUpdatedBy(userId);
                hmeMaterialLotLoad.setLastUpdateDate(currentDate);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeMaterialLotLoad));
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                List<List<String>> lists = CommonUtils.splitSqlList(sqlList, SQL_ITEM_COUNT_LIMIT);
                for (List<String> list : lists) {
                    jdbcTemplate.batchUpdate(list.toArray(new String[list.size()]));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(hmeLoadJobList)) {
            List<String> loadCids = mtCustomDbRepository.getNextKeys("hme_load_job_cid_s", hmeLoadJobList.size());
            List<String> sqlList = new ArrayList<>();
            Integer indexNum = 0;
            for (HmeLoadJob hmeLoadJob : hmeLoadJobList) {
                hmeLoadJob.setObjectVersionNumber(1L);
                hmeLoadJob.setCid(Long.valueOf(loadCids.get(indexNum++)));
                hmeLoadJob.setCreationDate(currentDate);
                hmeLoadJob.setCreatedBy(userId);
                hmeLoadJob.setLastUpdatedBy(userId);
                hmeLoadJob.setLastUpdateDate(currentDate);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeLoadJob));
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                List<List<String>> lists = CommonUtils.splitSqlList(sqlList, SQL_ITEM_COUNT_LIMIT);
                for (List<String> list : lists) {
                    jdbcTemplate.batchUpdate(list.toArray(new String[list.size()]));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(hmeLoadJobObjectList)) {
            List<String> loadIds = mtCustomDbRepository.getNextKeys("hme_load_job_object_s", hmeLoadJobObjectList.size());
            List<String> loadCids = mtCustomDbRepository.getNextKeys("hme_load_job_object_cid_s", hmeLoadJobObjectList.size());
            List<String> sqlList = new ArrayList<>();
            Integer indexNum = 0;
            for (HmeLoadJobObject hmeLoadJobObject : hmeLoadJobObjectList) {
                hmeLoadJobObject.setLoadObjectId(loadIds.get(indexNum));
                hmeLoadJobObject.setObjectVersionNumber(1L);
                hmeLoadJobObject.setCid(Long.valueOf(loadCids.get(indexNum++)));
                hmeLoadJobObject.setCreationDate(currentDate);
                hmeLoadJobObject.setCreatedBy(userId);
                hmeLoadJobObject.setLastUpdatedBy(userId);
                hmeLoadJobObject.setLastUpdateDate(currentDate);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeLoadJobObject));
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                List<List<String>> lists = CommonUtils.splitSqlList(sqlList, SQL_ITEM_COUNT_LIMIT);
                for (List<String> list : lists) {
                    jdbcTemplate.batchUpdate(list.toArray(new String[list.size()]));
                }
            }
        }
        result.setMaterialLotCodeList(resultList);
        result.setMaxNumber(number);
        return result;
    }

    List<MtExtendVO5> getMtExtendVO5List(Long tenantId, MtMaterialLot mtMaterialLot,
                                         HmeCosOperationRecord hmeCosOperationRecordDb, HmeContainerCapacity hmeContainerCapacity,
                                         String cosType) {
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("MF_FLAG");
        mtExtendVO5.setAttrValue("Y");
        attrs.add(mtExtendVO5);
        //REWORK_FLAG 取自来源条码
        String reworkFlag = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "REWORK_FLAG");
        if (StringUtils.isNotEmpty(reworkFlag)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("REWORK_FLAG");
            mtExtendVO5.setAttrValue(reworkFlag);
            attrs.add(mtExtendVO5);
        }
        //PERFORMANCE_LEVEL 取自来源条码
        String performanceLevel = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "PERFORMANCE_LEVEL");
        if (StringUtils.isNotEmpty(performanceLevel)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("PERFORMANCE_LEVEL");
            mtExtendVO5.setAttrValue(performanceLevel);
            attrs.add(mtExtendVO5);
        }
        //COS_RECORD 当前工单工艺工位在制记录
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("COS_RECORD");
        mtExtendVO5.setAttrValue(hmeCosOperationRecordDb.getOperationRecordId());
        attrs.add(mtExtendVO5);
        //WAFER_NUM 取自来源条码
        String waferNum = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "WAFER_NUM");
        if (StringUtils.isNotEmpty(waferNum)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("WAFER_NUM");
            mtExtendVO5.setAttrValue(waferNum);
            attrs.add(mtExtendVO5);
        }
        //CONTAINER_TYPE 目标容器类型
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("CONTAINER_TYPE");
        //2020-10-22 edit by chaonan.hu for zhenyong.ban 扩展值由容器类型Id改为容器类型编码
        MtContainerType mtContainerType = mtContainerTypeRepository.selectByPrimaryKey(hmeContainerCapacity.getContainerTypeId());
        mtExtendVO5.setAttrValue(mtContainerType.getContainerTypeCode());
        attrs.add(mtExtendVO5);
        //COS_TYPE 同来源条码
        if (StringUtils.isNotEmpty(cosType)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("COS_TYPE");
            mtExtendVO5.setAttrValue(cosType);
            attrs.add(mtExtendVO5);
        }
        //LOCATION_ROW 目标容器行
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("LOCATION_ROW");
        mtExtendVO5.setAttrValue(hmeContainerCapacity.getLineNum().toString());
        attrs.add(mtExtendVO5);
        //LOCATION_COLUMN 目标容器列
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("LOCATION_COLUMN");
        mtExtendVO5.setAttrValue(hmeContainerCapacity.getColumnNum().toString());
        attrs.add(mtExtendVO5);
        //CHIP_NUM 目标容器芯片数
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("CHIP_NUM");
        mtExtendVO5.setAttrValue(hmeContainerCapacity.getCapacity().toString());
        attrs.add(mtExtendVO5);
        //PRODUCT_DATE
        String productDate = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "PRODUCT_DATE");
        if (StringUtils.isNotEmpty(productDate)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("PRODUCT_DATE");
            mtExtendVO5.setAttrValue(productDate);
            attrs.add(mtExtendVO5);
        }
        //STATUS
        String status = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "STATUS");
        if (StringUtils.isNotEmpty(status)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("STATUS");
            mtExtendVO5.setAttrValue(status);
            attrs.add(mtExtendVO5);
        }
        //AVG_WAVE_LENGTH
        String avgWaveLength = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "AVG_WAVE_LENGTH");
        if (StringUtils.isNotEmpty(avgWaveLength)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("AVG_WAVE_LENGTH");
            mtExtendVO5.setAttrValue(avgWaveLength);
            attrs.add(mtExtendVO5);
        }
        //TYPE
        String type = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "TYPE");
        if (StringUtils.isNotEmpty(type)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("TYPE");
            mtExtendVO5.setAttrValue(type);
            attrs.add(mtExtendVO5);
        }
        //LOTNO
        String lotno = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "LOTNO");
        if (StringUtils.isNotEmpty(lotno)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("LOTNO");
            mtExtendVO5.setAttrValue(lotno);
            attrs.add(mtExtendVO5);
        }
        //REMARK
        String remark = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "REMARK");
        if (StringUtils.isNotEmpty(remark)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("REMARK");
            mtExtendVO5.setAttrValue(remark);
            attrs.add(mtExtendVO5);
        }
        //WORK_ORDER_ID
        String workOrderId = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "WORK_ORDER_ID");
        if (StringUtils.isNotEmpty(workOrderId)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("WORK_ORDER_ID");
            mtExtendVO5.setAttrValue(workOrderId);
            attrs.add(mtExtendVO5);
        }
        //WORKING_LOT
        String workingLot = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "WORKING_LOT");
        if (StringUtils.isNotEmpty(workingLot)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("WORKING_LOT");
            mtExtendVO5.setAttrValue(workingLot);
            attrs.add(mtExtendVO5);
        }
        return attrs;
    }

    @Override
    public HmeCosPatchPdaVO3 query(Long tenantId, HmeCosPatchPdaDTO5 dto) {
        HmeCosPatchPdaVO3 result = new HmeCosPatchPdaVO3();
        List<HmeCosPatchPdaVO2> resultList = new ArrayList<>();
        String operationId = dto.getOperationIdList().get(0);
        //根据工单ID+工艺+Wkc_ID+Wafer+设备ID（可为空），获取工单工艺工位在制记录表hme_cos_operation_record内，剩余芯片数>0的行数据
        HmeCosOperationRecord cosOperationRecord = hmeCosPatchPdaMapper.opRecordQuery(tenantId, operationId, dto);
        if (cosOperationRecord == null) {
            throw new MtException("HME_COS_PATCH_PDA_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0006", "HME"));
        }
        //2020-09-08 edit by chaonan.hu for zhenyong.ban 改为根据在制记录ID查询所有已出站和未出站数据
        List<HmeEoJobSn> hmeEoJobSnList = hmeCosPatchPdaMapper.getEoJobSnData(tenantId, cosOperationRecord.getOperationRecordId());
        Long number = 0L;
        if(CollectionUtils.isNotEmpty(hmeEoJobSnList)){
            //2021-10-28 edit by chaonan.hu for yiming.zhang 增加实验代码字段返回
            List<String> materialLotIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getMaterialLotId).collect(Collectors.toList());
            materialLotIdList = materialLotIdList.stream().filter(item -> StringUtils.isNotBlank(item)).collect(Collectors.toList());
            List<HmeCosPatchPdaVO9> labCodeList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(materialLotIdList)){
                labCodeList.addAll(hmeCosPatchPdaMapper.materialLotAttrQuery(tenantId, materialLotIdList, "LAB_CODE"));
            }
            for (HmeEoJobSn hmeEoJobSn : hmeEoJobSnList) {
                HmeCosPatchPdaVO2 hmeCosPatchPdaVO2 = new HmeCosPatchPdaVO2();
                hmeCosPatchPdaVO2.setJobId(hmeEoJobSn.getJobId());
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());
                number++;
                hmeCosPatchPdaVO2.setNumber(number);
                hmeCosPatchPdaVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeCosPatchPdaVO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                hmeCosPatchPdaVO2.setQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                if (hmeEoJobSn.getSiteOutDate() == null) {
                    hmeCosPatchPdaVO2.setStatus("新建");
                } else {
                    hmeCosPatchPdaVO2.setStatus("已打印");
                }
                List<HmeCosPatchPdaVO9> singleLabCodeList = labCodeList.stream().filter(item -> item.getMaterialLotId().equals(hmeEoJobSn.getMaterialLotId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleLabCodeList) && StringUtils.isNotBlank(singleLabCodeList.get(0).getAttrValue())){
                    hmeCosPatchPdaVO2.setLabCode(singleLabCodeList.get(0).getAttrValue());
                }
                resultList.add(hmeCosPatchPdaVO2);
            }
        }
        result.setMaxNumber(number);
        result.setMaterialLotCodeList(resultList);
        //2021-02-25 add by chaonan.hu for zhenyong.ban 增加金锡比返回给前台
        String auSnRatio = null;
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getWorkcellId());
        }});
        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterials)) {
            String materialLotId = hmeEoJobLotMaterials.get(0).getMaterialLotId();
            List<MtExtendAttrVO> supplierLotAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                setMaterialLotId(materialLotId);
                setAttrName("SUPPLIER_LOT");
            }});
            if (CollectionUtils.isNotEmpty(supplierLotAttr) && StringUtils.isNotBlank(supplierLotAttr.get(0).getAttrValue())) {
                auSnRatio = supplierLotAttr.get(0).getAttrValue();
                //2021-03-01 17:27 edit by chaonan.hu for zhenyong.ban 金锡比取值截取“-”后面的数值
                String[] split = auSnRatio.split("-");
                auSnRatio = split[split.length - 1];
            }
        }
        result.setAuSnRatio(auSnRatio);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> print(Long tenantId, HmeCosPatchPdaDTO5 dto) {
        Date nowDate = new Date();
        String operationId = dto.getOperationIdList().get(0);
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        //根据工单ID+工艺+Wkc_ID+Wafer+设备ID（可为空），获取工单工艺工位在制记录表hme_cos_operation_record内，剩余芯片数>0的行数据
        HmeCosOperationRecord cosOperationRecord = hmeCosPatchPdaMapper.opRecordQuery(tenantId, operationId, dto);
        if (cosOperationRecord == null) {
            throw new MtException("HME_COS_PATCH_PDA_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0006", "HME"));
        }
        //2021-07-12 11:03 add by chaonan.hu for zhenyong.ban 打印时,如果可新增数量(剩余芯片数-qty)小于0,则报错可新增数量小于0，请先撤回后再打印
        BigDecimal qty = hmeCosPatchPdaMapper.getAddQty(tenantId, dto.getWorkcellId(), operationId, dto.getWafer(), "COS_PASTER_OUT");
        BigDecimal addQty = BigDecimal.valueOf(cosOperationRecord.getSurplusCosNum()).subtract(qty);
        if(addQty.compareTo(BigDecimal.ZERO) == -1){
            throw new MtException("HME_COS_PATCH_PDA_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0016", "HME"));
        }

        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? 1L : curUser.getUserId();
        MtMaterialLot mtMaterialLot = null;
        long processedNum = 0;
        //要处理的EO集合
        List<String> eoIdList = new ArrayList<>();
        //要打印的物料批ID集合
        List<String> materialLotIdList = new ArrayList<>();
        //要更新的装载表信息
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = new ArrayList<>();
        //2020-10-19 edit by chaonan.hu for zhenyong.ban 增加校验条码是否在工段默认存储库位下
        List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, dto.getWkcLineId());
        if (CollectionUtils.isEmpty(defaultStorageLocatorIdList)) {
            //如果未找到默认存储库位，则报错{当前工位的工段或产线下未维护默认存储库位}
            throw new MtException("HME_EO_JOB_SN_092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_092", "HME"));
        }
        //2021-02-25 edit by chaonan.hu for zhenyong.ban 记录下装载表Id与jobId之间的对应关系,用于实验代码
        Map<String, String> loadJobMap = new HashMap<>();
        Map<String, HmeEoJobSn> hmeEoJobSnMap = new HashMap<>();
        Map<String, MtMaterialLot> mtMaterialLotMap = new HashMap<>();
        Map<String, List<HmeMaterialLotLoad>> hmeMaterialLotLoadMap = new HashMap<>();
        List<MtMaterialLot> mtMaterialLotList = new ArrayList();
        if (CollectionUtils.isNotEmpty(dto.getJobIdList())) {
            // 获取作业Eo
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).select(HmeEoJobSn.FIELD_JOB_ID, HmeEoJobSn.FIELD_EO_ID, HmeEoJobSn.FIELD_MATERIAL_LOT_ID).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                    .andIn(HmeEoJobSn.FIELD_JOB_ID, dto.getJobIdList())).build());
            hmeEoJobSnMap = hmeEoJobSnList.stream().collect(Collectors.toMap(HmeEoJobSn::getJobId, Function.identity()));

            eoIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getEoId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> materialLotIds = hmeEoJobSnList.stream().map(HmeEoJobSn::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            // 批量查询条码
            if (CollectionUtils.isNotEmpty(materialLotIds)) {
                materialLotIdList.addAll(materialLotIds);
                mtMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).select(MtMaterialLot.FIELD_MATERIAL_LOT_ID, MtMaterialLot.FIELD_MATERIAL_LOT_CODE, MtMaterialLot.FIELD_LOCATOR_ID, MtMaterialLot.FIELD_PRIMARY_UOM_QTY, MtMaterialLot.FIELD_MATERIAL_ID)
                        .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId).andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, materialLotIds)).build());
                mtMaterialLotMap = mtMaterialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId, Function.identity()));
            }
        }
        // 20211202 add by sanfeng.zhang for yiming.zhang 打印出站加锁
        List<HmeObjectRecordLock> recordLockList = new ArrayList<>();
        mtMaterialLotMap.forEach((codeKey, codeValue) -> {
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("贴片打印出站");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            hmeObjectRecordLockDTO.setObjectRecordId(codeKey);
            hmeObjectRecordLockDTO.setObjectRecordCode(codeValue.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            recordLockList.add(hmeObjectRecordLock);
        });
        if (CollectionUtils.isNotEmpty(recordLockList)) {
            hmeObjectRecordLockRepository.batchCommonLockObject(tenantId ,recordLockList);
        }
        try {
            if (CollectionUtils.isNotEmpty(dto.getJobIdList())) {
                // 批量出站
                hmeEoJobSnRepository.batchOutSite(tenantId, userId, dto.getJobIdList());
            }
            if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class).select(
                        HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID,
                        HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID,
                        HmeMaterialLotLoad.FIELD_LOAD_SEQUENCE,
                        HmeMaterialLotLoad.FIELD_LOAD_ROW,
                        HmeMaterialLotLoad.FIELD_LOAD_COLUMN).andWhere(Sqls.custom()
                        .andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
                // 批量查询条码装在信息
                hmeMaterialLotLoadMap = hmeMaterialLotLoads.stream().collect(Collectors.groupingBy(HmeMaterialLotLoad::getMaterialLotId));

                //2021-10-27 16:39 add by chaonan.hu for yiming.zhang 增加条码的实验代码记录
                insertMaterialLotLobCode(tenantId, mtMaterialLotList, hmeMaterialLotLoads, dto.getWorkcellId(), dto.getWorkOrderId());
            }

            //2020-09-08 10:31 edit by chaonan.hu 改为将前台传入的新建状态的jobId对应的记录打印
            for (String jobId : dto.getJobIdList()) {
                HmeEoJobSn hmeEoJobSn = hmeEoJobSnMap.get(jobId);
                if (hmeEoJobSn != null) {
                    mtMaterialLot = mtMaterialLotMap.getOrDefault(hmeEoJobSn.getMaterialLotId(), new MtMaterialLot());
                    if (!defaultStorageLocatorIdList.contains(mtMaterialLot.getLocatorId())) {
                        //{来料条码所在仓库不为该工段下线边仓}
                        throw new MtException("HME_COS_PATCH_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_PATCH_0006", "HME", mtMaterialLot.getMaterialLotCode()));
                    }
                    processedNum += mtMaterialLot.getPrimaryUomQty();

                    //2020-12-25 10:01 edit by chaonan.hu for can.wang 在生产投料为了记录装载表投料的投料热沉，这里需先查出装载表信息
                    List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeMaterialLotLoadMap.getOrDefault(mtMaterialLot.getMaterialLotId(), Collections.emptyList());
                    for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoads) {
                        loadJobMap.put(hmeMaterialLotLoad.getMaterialLotLoadId(), jobId);
                    }
                    hmeMaterialLotLoadList.addAll(hmeMaterialLotLoads);
                }
            }
            //2020-10-19 add by chaonan.hu for zhenyong.ban
            //当打印后剩余芯片数小于等于0，校验该工位是否在EO_JOB_Sn表中存在JOB_TYPE为COS_PASTER_IN的未出站数据
            if (cosOperationRecord.getSurplusCosNum().longValue() <= processedNum) {
                List<HmeCosPatchPdaVO5> hmeCosPatchPdaVO5List = hmeCosPatchPdaMapper.siteOutDateNullQuery2(tenantId, dto.getWorkcellId(), dto.getWorkOrderId(), operationId);
                if (CollectionUtils.isNotEmpty(hmeCosPatchPdaVO5List)) {
                    throw new MtException("HME_COS_PATCH_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_0007", "HME"));
                }
            }
            //更新WO工艺作业记录
            HmeWoJobSn hmeWoJobSn = hmeWoJobSnRepository.selectOne(new HmeWoJobSn() {{
                setSiteId(dto.getSiteId());
                setWorkOrderId(dto.getWorkOrderId());
                setOperationId(operationId);
            }});
            hmeWoJobSn.setProcessedNum(hmeWoJobSn.getProcessedNum() + processedNum);
            //更新工单工艺工位在制记录
            cosOperationRecord.setSurplusCosNum(cosOperationRecord.getSurplusCosNum() - processedNum);
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(cosOperationRecord);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(cosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            //EO处理
            //根据wkcShiftId获取班次编码、班次日期
            MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());
            //eo下达事件请求Id
            String eoReleaseEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_RELEASE");
            //eo下达时间Id
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("EO_VALIDATE_VERIFY_UPDATE");
            String eoReleaseEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            //eo更新事件请求Id
            String eoUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_UPDATE");

            Map<String, MtEo> mtEoMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(eoIdList)) {
                List<MtEo> mtEos = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).select(MtEo.FIELD_EO_ID, MtEo.FIELD_QTY).andWhere(Sqls.custom()
                        .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                        .andIn(MtEo.FIELD_EO_ID, eoIdList)).build());
                mtEoMap = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, Function.identity()));
            }

            for (String eoId : eoIdList) {
                MtEo mtEo = mtEoMap.get(eoId);
                if (mtEo != null) {
                    //调用API{eoReleaseAndStepQueue} 进行EO下达及首道工序排队
                    MtEoVO18 mtEoVO18 = new MtEoVO18();
                    mtEoVO18.setEoId(eoId);
                    mtEoVO18.setEventRequestId(eoReleaseEventRequestId);
                    mtEoVO18.setLocatorId(mtWorkOrder.getLocatorId());
                    mtEoVO18.setParentEventId(eoReleaseEventId);
                    mtEoVO18.setShiftCode(mtWkcShift.getShiftCode());
                    mtEoVO18.setShiftDate(mtWkcShift.getShiftDate());
                    mtEoVO18.setWorkcellId(dto.getWorkcellId());
                    mtEoRepository.eoReleaseAndStepQueue(tenantId, mtEoVO18);
                    //调用API{eoWkcQueue} 进行EO调度分配WKC
                    MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
                    //根据eoId、SUB_ROUTER_FLAG != Y 在mt_eo_router_actual表中获取EO_ROUTER_ACTUAL_ID，再从mt_eo_step_actual表获取LAST_UPDATE_DATE最近的行项目
                    String eoStepActualId = hmeCosPatchPdaMapper.getEoStepActualId(tenantId, eoId);
                    mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
                    mtEoStepWipVO4.setWorkcellId(dto.getWorkcellId());
                    mtEoStepWipVO4.setQueueQty(mtEo.getQty());
                    mtEoStepWipVO4.setEventRequestId(eoUpdateEventRequestId);
                    mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);
                    //调用API{eoWorkingProcess} 进行EO开工运行
                    MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
                    mtEoRouterActualVO19.setEoStepActualId(eoStepActualId);
                    mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
                    mtEoRouterActualVO19.setQty(mtEo.getQty());
                    mtEoRouterActualVO19.setSourceStatus("QUEQUE");
                    mtEoRouterActualRepository.eoWorkingProcess(tenantId, mtEoRouterActualVO19);
                    //调用API{completeProcess} 进行EO完工
                    mtEoRouterActualVO19.setSourceStatus("WORKING");
                    mtEoRouterActualRepository.completeProcess(tenantId, mtEoRouterActualVO19);
                }
            }
            //更新批次物料条码以及生产投料
            releaseMaterial(tenantId, dto, processedNum, hmeMaterialLotLoadList, cosOperationRecord, loadJobMap);
        } catch (Exception e){
            throw new CommonException(e.getMessage());
        } finally {
            //解锁
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , recordLockList , HmeConstants.ConstantValue.YES);
        }
        return materialLotIdList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bandingWorkcell(Long tenantId, HmeCosPatchPdaDTO6 dto, String materialId,
                                String materialLotId, Double qty) {
        //将条码绑定在工位上
        HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
        hmeEoJobLotMaterial.setTenantId(tenantId);
        hmeEoJobLotMaterial.setWorkcellId(dto.getWorkcellId());
        hmeEoJobLotMaterial.setMaterialId(materialId);
        hmeEoJobLotMaterial.setMaterialLotId(materialLotId);
        hmeEoJobLotMaterial.setReleaseQty(new BigDecimal(qty));
        hmeEoJobLotMaterial.setIsReleased(0);
        hmeEoJobLotMaterialRepository.insertSelective(hmeEoJobLotMaterial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosPatchPdaDTO6 unBindingWorkcell(Long tenantId, HmeCosPatchPdaDTO6 dto) {
        //删除绑定关系
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        HmeEoJobLotMaterial hmeEoJobLotMaterial = hmeEoJobLotMaterialRepository.selectOne(new HmeEoJobLotMaterial() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getWorkcellId());
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
        }});
        if (hmeEoJobLotMaterial != null) {
            hmeEoJobLotMaterialRepository.deleteByPrimaryKey(hmeEoJobLotMaterial);
        }
        return dto;
    }

    @Override
    public List<HmeCosPatchPdaVO4> bandingMaterialQuery(Long tenantId, String workcellId) {
        List<HmeCosPatchPdaVO4> resultList = new ArrayList<>();
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
            setTenantId(tenantId);
            setWorkcellId(workcellId);
        }});
        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterials)) {
            HmeCosPatchPdaVO4 hmeCosPatchPdaVO4 = null;
            for (HmeEoJobLotMaterial hmeEoJobLotMaterial : hmeEoJobLotMaterials) {
                if (StringUtils.isEmpty(hmeEoJobLotMaterial.getMaterialLotId())) {
                    continue;
                }
                hmeCosPatchPdaVO4 = new HmeCosPatchPdaVO4();
                hmeCosPatchPdaVO4.setMaterialId(hmeEoJobLotMaterial.getMaterialId());
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialId());
                hmeCosPatchPdaVO4.setMaterialCode(mtMaterial.getMaterialCode());
                hmeCosPatchPdaVO4.setMaterialName(mtMaterial.getMaterialName());
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialLotId());
                hmeCosPatchPdaVO4.setQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                hmeCosPatchPdaVO4.setUomCode(mtUom.getUomCode());
                hmeCosPatchPdaVO4.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeCosPatchPdaVO4.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                resultList.add(hmeCosPatchPdaVO4);
            }
        }
        return resultList;
    }

    @Override
    public void releaseMaterial(Long tenantId, HmeCosPatchPdaDTO5 dto, long processedNum, List<HmeMaterialLotLoad> hmeMaterialLotLoadList,
                                HmeCosOperationRecord cosOperationRecord, Map<String, String> loadJobMap) {
        String operationId = dto.getOperationIdList().get(0);
        //物料批更新事件ID  批次物料条码现有量扣减
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("MATERIAL_LOT_CONSUME");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //生产指令组件装配 事件组ID
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "COS_PASTER");
        //生产指令组件装配 父事件ID
        mtEventCreateVO.setEventTypeCode("COS_PASTER_IN");
        String parentEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        //根据wkc_shift_id查询班次编码、班次日期
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());
        //获取工单的装配组件
        List<MtBomComponent> mtBomComponentList = new ArrayList<>();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        String routerOperationId = null;
        String routerStepId = null;
        if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
            //根据ROUTER_ID和工艺ID通过工艺步骤表mt_router_step及
            //工艺步骤与工艺关系表mt_router_operation可获取routerStepId工艺路线步骤ID
            List<MtRouterOperation> mtRouterOperations = hmeCosPatchPdaMapper.routerOperationQuery(tenantId, mtWorkOrder.getRouterId());
            for (MtRouterOperation mtRouterOperation : mtRouterOperations) {
                if (operationId.equals(mtRouterOperation.getOperationId())) {
                    routerStepId = mtRouterOperation.getRouterStepId();
                    routerOperationId = mtRouterOperation.getRouterOperationId();
                    break;
                }
            }
            if (StringUtils.isNotEmpty(routerOperationId)) {
                //2020-09-25 edit by chaonan.hu for zhenyong.ban 用值集HME.CHIP_ITEM_GROUP作为限制条件
                List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
                if (CollectionUtils.isEmpty(lovValueDTOS)) {
                    throw new MtException("HME_COS_PATCH_PDA_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_PDA_0015", "HME"));
                }
                List<String> itemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                //获取BOM_COMPONENT_ID
                mtBomComponentList = hmeCosPatchPdaMapper.bomComponentQuery2(tenantId, routerOperationId, dto.getSiteId(), itemGroupList);
            }
        }
        //2020-10-15 edit by chaonan.hu for zhenyong.ban
        //用于记录组件ID与组件替代料之间的关系
        Map<String, List<String>> bomSubstituteMaterialMap = new HashMap<>();
        //用于记录组件的所有的工单替代料和全局替代料
        List<String> allBomSubstituteMaterialList = new ArrayList<>();
        //用于记录组件的所有的工单替代料
        List<String> woSubstituteMaterials = new ArrayList<>();
        //用于记录组件所有的全局替代料
        List<String> globalSubstituteMaterials = new ArrayList<>();
        //用于记录组件物料与组件Id之间的关系
        Map<String, String> materialBomComponentMap = new HashMap<>();
        for (MtBomComponent mtBomComponent : mtBomComponentList) {
            List<String> bomSubstituteMaterialList = new ArrayList<>();
            List<String> woSubstituteMaterialList = hmeCosPatchPdaMapper.getWorkOrderSubstituteMaterial(tenantId, mtBomComponent.getBomComponentId());
            bomSubstituteMaterialList.addAll(woSubstituteMaterialList);
            woSubstituteMaterials.addAll(woSubstituteMaterialList);
            //2020-12-03 edit by chaonan.hu for zhenyong.ban 查询全局替代料逻辑修改
            List<String> globalSubstituteMaterialList = hmeCosPatchPdaMapper.getGlobalSubstituteMaterial(tenantId, mtBomComponent.getMaterialId(), mtWorkOrder.getSiteId());
            if (globalSubstituteMaterialList.contains(mtBomComponent.getMaterialId()) && mtBomComponent.getQty() > 0) {
                //如果Bom组件物料在上面查出来的全局替代料中，但Bom组件数量大于0，则此物料不为全局替代料
                globalSubstituteMaterialList.remove(mtBomComponent.getMaterialId());
            }
            bomSubstituteMaterialList.addAll(globalSubstituteMaterialList);
            globalSubstituteMaterials.addAll(globalSubstituteMaterialList);
            allBomSubstituteMaterialList.addAll(bomSubstituteMaterialList);
            bomSubstituteMaterialMap.put(mtBomComponent.getBomComponentId(), bomSubstituteMaterialList);
            materialBomComponentMap.put(mtBomComponent.getMaterialId(), mtBomComponent.getBomComponentId());
        }
        if (CollectionUtils.isNotEmpty(allBomSubstituteMaterialList)) {
            allBomSubstituteMaterialList = allBomSubstituteMaterialList.stream().distinct().collect(Collectors.toList());
        }
        //2020-12-09 edit by chaonan.hu for zhenyong.ban API{woComponentAssemble}的强制装配标识assembleExcessFlag逻辑修改
        //2021-01-12 edit by chaonan.hu for zhenyong.ban 这里不在循环外面根据工单Bom去找，改为放在循环体里直接根据工单Bom+物料查询
//        List<String> bomComponentMaterialIdList = hmeCosPatchPdaMapper.getMaterialIdListByBomId(tenantId, mtWorkOrder.getBomId());
        //装载表要更新的起始索引和截止索引
        int start = 0;
        int end = 0;
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        //2020-10-27 add by chaonan.hu for zhenyong.ban {校验右侧投料条码是否在工段默认存储库位下}
        List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, dto.getWkcLineId());
        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = new ArrayList<>();
        List<HmeMaterialLotLoad> updateMaterialLotLoads = new ArrayList<>();
        List<HmeLoadJob> insetLoadJobList = new ArrayList<>();
        List<HmeLoadJobObject> insetHmeLoadJobObjectList = new ArrayList<>();
        List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        Date currentDate = CommonUtils.currentTimeGet();
        for (MtBomComponent mtBomComponent : mtBomComponentList) {
            //如果组件的物料ID存在替代料list集合中,且组件数量小于等于0，则跳过，循环下一组件
            if (allBomSubstituteMaterialList.contains(mtBomComponent.getMaterialId()) && mtBomComponent.getQty() <= 0) {
                continue;
            }
            //根据组件Id去map中查询到对应的替代料集合
            List<String> substituteMaterialList = bomSubstituteMaterialMap.get(mtBomComponent.getBomComponentId());
            substituteMaterialList.add(mtBomComponent.getMaterialId());

            //组件物料的总扣减数
            BigDecimal totalQty = new BigDecimal(mtBomComponent.getQty() * processedNum);
            //工位上绑定的组件物料条码数量
            BigDecimal currentQty = BigDecimal.ZERO;
            //用于记录组件物料和替代料物料在该工位绑定的数量
            List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeCosPatchPdaMapper.eoJobLotMaterialQuery(tenantId,
                    dto.getWorkcellId(), substituteMaterialList);
            if (CollectionUtils.isEmpty(hmeEoJobLotMaterials)) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtBomComponent.getMaterialId());
                throw new MtException("HME_COS_PATCH_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_0003", "HME", mtMaterial.getMaterialCode()));
            } else {
                if (CollectionUtils.isEmpty(defaultStorageLocatorIdList)) {
                    //如果未找到默认存储库位，则报错{当前工位的工段或产线下未维护默认存储库位}
                    throw new MtException("HME_EO_JOB_SN_092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_092", "HME"));
                }
                //判断现有数量与总数量之间的关系
                hmeEoJobLotMaterials = hmeEoJobLotMaterials.stream().sorted(Comparator.comparing(HmeEoJobLotMaterial::getCreationDate)).collect(Collectors.toList());
                for (HmeEoJobLotMaterial hmeEoJobLotMaterial : hmeEoJobLotMaterials) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialLotId());
                    if (!defaultStorageLocatorIdList.contains(mtMaterialLot.getLocatorId())) {
                        //{来料条码所在仓库不为该工段下线边仓}
                        throw new MtException("HME_WO_JOB_SN_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_WO_JOB_SN_010", "HME"));
                    }
                    currentQty = currentQty.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                }
                if (totalQty.compareTo(currentQty) == 1) {
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtBomComponent.getMaterialId());
                    //总数量>现有数量时，报错
                    throw new MtException("HME_COS_PATCH_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_0004", "HME", mtMaterial.getMaterialCode()));
                } else {
                    //循环扣减
                    Boolean breakFlag = false;
                    for (HmeEoJobLotMaterial hmeEoJobLotMaterial : hmeEoJobLotMaterials) {
                        Double useQty = null;
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialLotId());
                        if (totalQty.compareTo(BigDecimal.ZERO) == 1) {
                            //若扣减的总数量小于条码数量，则trxPrimaryUomQty = -扣减数
                            if (totalQty.compareTo(new BigDecimal(mtMaterialLot.getPrimaryUomQty())) == -1) {
                                //2021-04-22 14:54 edit by chaonan.hu for kang.wang 物料批更新改为批量
                                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                                mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                mtMaterialLotVO20.setTrxPrimaryUomQty(totalQty.multiply(new BigDecimal(-1)).doubleValue());
                                mtMaterialLotVO20List.add(mtMaterialLotVO20);

//                                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//                                mtMaterialLotVO2.setEventId(eventId);
//                                mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//                                mtMaterialLotVO2.setTrxPrimaryUomQty(totalQty.multiply(new BigDecimal(-1)).doubleValue());
//                                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                                useQty = totalQty.doubleValue();
                                breakFlag = true;
                            } else {
                                //2021-04-22 14:54 edit by chaonan.hu for kang.wang 物料批更新改为批量
                                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                                mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                mtMaterialLotVO20.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() * -1);
                                mtMaterialLotVO20.setEnableFlag("N");
                                mtMaterialLotVO20List.add(mtMaterialLotVO20);

                                //若扣减的总数量大于等于条码数量，则trxPrimaryUomQty = -条码数量,并将条码失效
//                                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//                                mtMaterialLotVO2.setEventId(eventId);
//                                mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//                                mtMaterialLotVO2.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() * -1);
//                                mtMaterialLotVO2.setEnableFlag("N");
//                                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                                totalQty = totalQty.subtract(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                                useQty = mtMaterialLot.getPrimaryUomQty();
                                //根据条码ID删除hme_eo_job_lot_material表，所有对应的行数据
                                List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
                                    setTenantId(tenantId);
                                    setMaterialLotId(hmeEoJobLotMaterial.getMaterialLotId());
                                }});
                                hmeEoJobLotMaterialRepository.batchDeleteByPrimaryKey(hmeEoJobLotMaterialList);
                            }
                            boolean labCodeFlag = false;
                            List<MtExtendAttrVO> labCodeAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                                setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                setAttrName("LAB_CODE");
                            }});
                            if (CollectionUtils.isNotEmpty(labCodeAttr) && StringUtils.isNotBlank(labCodeAttr.get(0).getAttrValue())) {
                                labCodeFlag = true;
                            }
                            //2020-12-25 add by chaonan.hu for zhenyong.ban 更新装载表数据
                            if (start < hmeMaterialLotLoadList.size()) {
                                end = start + useQty.intValue();
                                List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeMaterialLotLoadList.subList(start, Math.min(end, hmeMaterialLotLoadList.size()));
                                String supplierLot = null;
                                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                                mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                mtMaterialLotAttrVO2.setAttrName("SUPPLIER_LOT");
                                List<MtExtendAttrVO> mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotBlank(mtExtendAttrVOS2.get(0).getAttrValue())) {
                                    supplierLot = mtExtendAttrVOS2.get(0).getAttrValue();
                                }
                                List<String> ids = mtCustomDbRepository.getNextKeys("hme_load_job_s", hmeMaterialLotLoads.size());
                                List<String> cids = mtCustomDbRepository.getNextKeys("hme_load_job_cid_s", hmeMaterialLotLoads.size());
                                Integer indexNum = 0;
                                for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoads) {
                                    hmeMaterialLotLoad.setAttribute4(mtMaterialLot.getMaterialLotCode());
                                    hmeMaterialLotLoad.setAttribute5(mtMaterialLot.getSupplierId());
                                    hmeMaterialLotLoad.setAttribute6(supplierLot);
                                    hmeMaterialLotLoad.setAttribute11(mtMaterialLot.getMaterialId());
                                    updateMaterialLotLoads.add(hmeMaterialLotLoad);
                                    //2021-02-25 add by chaonan.hu for zhenyong.ban 记录实验代码
                                    if (labCodeFlag) {
                                        HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                                        hmeMaterialLotLabCode.setTenantId(tenantId);
                                        hmeMaterialLotLabCode.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
                                        hmeMaterialLotLabCode.setObject("COS");
                                        hmeMaterialLotLabCode.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                                        hmeMaterialLotLabCode.setLabCode(labCodeAttr.get(0).getAttrValue());
                                        String jobId = loadJobMap.get(hmeMaterialLotLoad.getMaterialLotLoadId());
                                        hmeMaterialLotLabCode.setJobId(jobId);
                                        hmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                                        List<MtExtendAttrVO> workOrderIdAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                                            setMaterialLotId(hmeMaterialLotLabCode.getMaterialLotId());
                                            setAttrName("WORK_ORDER_ID");
                                        }});
                                        if (CollectionUtils.isNotEmpty(workOrderIdAttr) && StringUtils.isNotBlank(workOrderIdAttr.get(0).getAttrValue())) {
                                            hmeMaterialLotLabCode.setWorkOrderId(workOrderIdAttr.get(0).getAttrValue());
                                        }
                                        hmeMaterialLotLabCode.setSourceObject("MA");
                                        hmeMaterialLotLabCode.setSourceMaterialLotId(mtMaterialLot.getMaterialLotId());
                                        hmeMaterialLotLabCode.setSourceMaterialId(mtMaterialLot.getMaterialId());
                                        hmeMaterialLotLabCode.setEnableFlag("Y");
                                        hmeMaterialLotLabCodeList.add(hmeMaterialLotLabCode);
                                    }
                                    //2021-02-25 add by chaonan.hu for zhenyong.ban 记录COS履历
                                    HmeLoadJob hmeLoadJob = new HmeLoadJob();
                                    hmeLoadJob.setTenantId(tenantId);
                                    hmeLoadJob.setSiteId(dto.getSiteId());
                                    hmeLoadJob.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                                    hmeLoadJob.setLoadJobType("COS_PASTER_OUT_PRINT");
                                    hmeLoadJob.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
                                    MtMaterialLot mtMaterialLot2 = mtMaterialLotRepository.selectByPrimaryKey(hmeMaterialLotLoad.getMaterialLotId());
                                    hmeLoadJob.setMaterialId(mtMaterialLot2.getMaterialId());
                                    hmeLoadJob.setLoadRow(hmeMaterialLotLoad.getLoadRow());
                                    hmeLoadJob.setLoadColumn(hmeMaterialLotLoad.getLoadColumn());
                                    hmeLoadJob.setCosNum(1L);
                                    hmeLoadJob.setOperationId(operationId);
                                    hmeLoadJob.setWorkcellId(dto.getWorkcellId());
                                    hmeLoadJob.setWorkOrderId(cosOperationRecord.getWorkOrderId());
                                    hmeLoadJob.setWaferNum(cosOperationRecord.getWafer());
                                    hmeLoadJob.setCosType(cosOperationRecord.getCosType());
                                    hmeLoadJob.setStatus("0");
                                    hmeLoadJob.setBomMaterialLotId(mtMaterialLot.getMaterialLotId());
                                    hmeLoadJob.setBomMaterialId(mtMaterialLot.getMaterialId());
                                    hmeLoadJob.setBomMaterialLotSupplier(mtMaterialLot.getSupplierId());
                                    hmeLoadJob.setObjectVersionNumber(1L);
                                    hmeLoadJob.setCreationDate(currentDate);
                                    hmeLoadJob.setCreatedBy(userId);
                                    hmeLoadJob.setLastUpdatedBy(userId);
                                    hmeLoadJob.setLastUpdateDate(currentDate);
                                    hmeLoadJob.setLoadJobId(ids.get(indexNum));
                                    hmeLoadJob.setCid(Long.valueOf(cids.get(indexNum++)));
                                    insetLoadJobList.add(hmeLoadJob);

                                    //设备
                                    List<HmeEquipment> hmeEquipmentList = hmeEoJobEquipmentMapper.queryEquipmentListByWorkCellId(tenantId, dto.getWorkcellId());
                                    for (HmeEquipment hmeEquipment : hmeEquipmentList) {
                                        HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                                        hmeLoadJobObject.setTenantId(tenantId);
                                        hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                                        hmeLoadJobObject.setObjectType("EQUIPMENT");
                                        hmeLoadJobObject.setObjectId(hmeEquipment.getEquipmentId());
                                        insetHmeLoadJobObjectList.add(hmeLoadJobObject);
                                    }
                                }
                                start += end;
                            }
                            //调用API{onhandQtyUpdateProcess} 进行批次物料条码现有量扣减
                            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                            mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                            mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                            mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
                            mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                            mtInvOnhandQuantityVO9.setChangeQuantity(useQty);
                            mtInvOnhandQuantityVO9.setEventId(eventId);
                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                            //调用API{objectTransactionSync} 进行批次物料条码工单生产投料事务记录
                            //2020-12-09 edit by chaonan.hu for zhenyong.ban 投料事务记录逻辑大改
                            //2021-01-08 edit by chaonan.hu for zhenyong.ban 投料事务记录逻辑大改
                            BigDecimal useQtyBig = BigDecimal.valueOf(useQty);
                            if (allBomSubstituteMaterialList.contains(mtMaterialLot.getMaterialId())) {
                                //如果是替代料，做一笔申请数量的计划外投料事务
                                WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                                objectTransactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                                objectTransactionRequestVO.setEventId(eventId);
                                objectTransactionRequestVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                objectTransactionRequestVO.setMaterialId(mtMaterialLot.getMaterialId());
                                objectTransactionRequestVO.setTransactionQty(useQtyBig);
                                objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                                objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
                                objectTransactionRequestVO.setTransactionTime(new Date());
                                objectTransactionRequestVO.setTransactionReasonCode("COS替代料投料");
                                objectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                                objectTransactionRequestVO.setLocatorId(mtMaterialLot.getLocatorId());
                                List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, mtMaterialLot.getLocatorId(), "TOP");
                                if (CollectionUtils.isNotEmpty(pLocatorIds)) {
                                    MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                                    if (ploc != null) {
                                        objectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
                                    }
                                }
                                objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                                objectTransactionRequestVO.setMergeFlag(NO);
                                objectTransactionRequestVO.setMoveType("261");
                                objectTransactionRequestVO.setMoveReason("COS替代料投料");
                                transactionRequestVOList.add(objectTransactionRequestVO);
                                // 先分全局替代料还是工单替代料找到对应的主键料,再对主键料做事务
                                MtBomComponent primaryBomComponent = new MtBomComponent();
                                if (globalSubstituteMaterials.contains(mtMaterialLot.getMaterialId())) {
                                    //全局替代料找主料
                                    primaryBomComponent = getPrimaryMaterialByGlobal(tenantId, mtMaterialLot.getSiteId(), mtMaterialLot.getMaterialId(), mtWorkOrder.getBomId(), routerOperationId);
                                } else {
                                    //工单替代料找主料
                                    // 20211109 modify by sanfeng.zhang for peng.zhao  去掉之前的逻辑此时bomComponentId不为替代料 实则主料的bom 找主料逻辑有问题
                                    primaryBomComponent = mtBomComponent;
                                }
                                //主键料的需求数量
                                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                                MtExtendVO mtExtendVO = new MtExtendVO();
                                mtExtendVO.setTableName("mt_bom_component_attr");
                                mtExtendVO.setKeyId(primaryBomComponent.getBomComponentId());
                                mtExtendVO.setAttrName("lineAttribute5");
                                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                                }
                                //根据主键料查询到所有的替代料集合
                                List<String> subMaterialList = bomSubstituteMaterialMap.get(primaryBomComponent.getBomComponentId());
                                subMaterialList.add(primaryBomComponent.getMaterialId());
                                BigDecimal assembleQtySum = hmeCosPatchPdaMapper.getAssembleQtySum(tenantId, subMaterialList, mtWorkOrder.getWorkOrderId());
                                if (lineAttribute5.compareTo(assembleQtySum) == 1) {
                                    //如果主料总需求数量>总已投数，则需进一步判断
                                    if (lineAttribute5.subtract(assembleQtySum).compareTo(useQtyBig) == 1) {
                                        //如果主料总需求数-总已投数>申请数量，则需记两笔事务
                                        //申请数量的HME_WO_ISSUE
                                        HmeCosPatchPdaDTO10 hmeCosPatchPdaDTO10 = new HmeCosPatchPdaDTO10();
                                        hmeCosPatchPdaDTO10.setEventId(eventId);
                                        hmeCosPatchPdaDTO10.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                        hmeCosPatchPdaDTO10.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                                        hmeCosPatchPdaDTO10.setTransactionQty(useQtyBig);
                                        WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, hmeCosPatchPdaDTO10, false);
                                        transactionRequestVOList.add(wmsObjectTransactionRequestVO);
                                        //申请数量的计划外退料HME_WO_ISSUE_R_EXT
                                        WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                                        BeanUtils.copyProperties(wmsObjectTransactionRequestVO, wmsObjectTransactionRequestVO2);
                                        wmsObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_R_EXT");
                                        wmsObjectTransactionRequestVO2.setTransactionReasonCode("COS主料退料");
                                        wmsObjectTransactionRequestVO2.setMoveType("262");
                                        wmsObjectTransactionRequestVO2.setMoveReason("COS主料退料");
                                        wmsObjectTransactionRequestVO2.setBomReserveLineNum(null);
                                        wmsObjectTransactionRequestVO2.setBomReserveNum(null);
                                        wmsObjectTransactionRequestVO2.setSoNum(null);
                                        wmsObjectTransactionRequestVO2.setSoLineNum(null);
                                        transactionRequestVOList.add(wmsObjectTransactionRequestVO2);
                                    } else {
                                        //如果主料总需求数-总已投数<申请数量，则需记两笔事务
                                        //总需求数-总已投数的计划内投料HME_WO_ISSUE
                                        HmeCosPatchPdaDTO10 hmeCosPatchPdaDTO10 = new HmeCosPatchPdaDTO10();
                                        hmeCosPatchPdaDTO10.setEventId(eventId);
                                        hmeCosPatchPdaDTO10.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                        hmeCosPatchPdaDTO10.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                                        hmeCosPatchPdaDTO10.setTransactionQty(lineAttribute5.subtract(assembleQtySum));
                                        WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, hmeCosPatchPdaDTO10, false);
                                        transactionRequestVOList.add(wmsObjectTransactionRequestVO);
                                        //总需求数-总已投数的计划外退料HME_WO_ISSUE_R_EXT
                                        WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                                        BeanUtils.copyProperties(wmsObjectTransactionRequestVO, wmsObjectTransactionRequestVO2);
                                        wmsObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_R_EXT");
                                        wmsObjectTransactionRequestVO2.setTransactionReasonCode("COS主料退料");
                                        wmsObjectTransactionRequestVO2.setMoveType("262");
                                        wmsObjectTransactionRequestVO2.setMoveReason("COS主料退料");
                                        wmsObjectTransactionRequestVO2.setBomReserveLineNum(null);
                                        wmsObjectTransactionRequestVO2.setBomReserveNum(null);
                                        wmsObjectTransactionRequestVO2.setSoNum(null);
                                        wmsObjectTransactionRequestVO2.setSoLineNum(null);
                                        transactionRequestVOList.add(wmsObjectTransactionRequestVO2);
                                    }
                                }
                            } else {
                                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                                MtExtendVO mtExtendVO = new MtExtendVO();
                                mtExtendVO.setTableName("mt_bom_component_attr");
                                mtExtendVO.setKeyId(mtBomComponent.getBomComponentId());
                                mtExtendVO.setAttrName("lineAttribute5");
                                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                                }
                                //根据主键料查询到所有的替代料集合
                                List<String> subMaterialList = bomSubstituteMaterialMap.get(mtBomComponent.getBomComponentId());
                                subMaterialList.add(mtBomComponent.getMaterialId());
                                BigDecimal assembleQtySum = hmeCosPatchPdaMapper.getAssembleQtySum(tenantId, subMaterialList, mtWorkOrder.getWorkOrderId());
                                if (assembleQtySum.add(useQtyBig).compareTo(lineAttribute5) <= 0) {
                                    //如果已投数量 + 申请数量 <= 工单需求数量 全部计划内 事务数量：申请数量
                                    //申请数量的HME_WO_ISSUE
                                    HmeCosPatchPdaDTO10 hmeCosPatchPdaDTO10 = new HmeCosPatchPdaDTO10();
                                    hmeCosPatchPdaDTO10.setEventId(eventId);
                                    hmeCosPatchPdaDTO10.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                    hmeCosPatchPdaDTO10.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                                    hmeCosPatchPdaDTO10.setTransactionQty(useQtyBig);
                                    WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", mtBomComponent, hmeCosPatchPdaDTO10, true);
                                    transactionRequestVOList.add(wmsObjectTransactionRequestVO);
                                } else {
                                    //如果已投数量 + 申请数量 > 工单需求数量, 则需进一步判断
                                    if (assembleQtySum.compareTo(lineAttribute5) >= 0) {
                                        //如果已投数量 >= 工单需求数量，则全部计划外 申请数量的HME_WO_ISSUE_EXT
                                        HmeCosPatchPdaDTO10 hmeCosPatchPdaDTO10 = new HmeCosPatchPdaDTO10();
                                        hmeCosPatchPdaDTO10.setEventId(eventId);
                                        hmeCosPatchPdaDTO10.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                        hmeCosPatchPdaDTO10.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                                        hmeCosPatchPdaDTO10.setTransactionQty(useQtyBig);
                                        WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE_EXT", mtBomComponent, hmeCosPatchPdaDTO10, true);
                                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(wmsObjectTransactionRequestVO));
                                    } else {
                                        //如果已投数量 < 工单需求数量, 则需记两笔事务
                                        //工单总需求数量-投料数量的HME_WO_ISSUE
                                        HmeCosPatchPdaDTO10 hmeCosPatchPdaDTO10 = new HmeCosPatchPdaDTO10();
                                        hmeCosPatchPdaDTO10.setEventId(eventId);
                                        hmeCosPatchPdaDTO10.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                        hmeCosPatchPdaDTO10.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                                        hmeCosPatchPdaDTO10.setTransactionQty(lineAttribute5.subtract(assembleQtySum));
                                        WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", mtBomComponent, hmeCosPatchPdaDTO10, true);
                                        transactionRequestVOList.add(wmsObjectTransactionRequestVO);
                                        //（投料数量 + 申请数量）- 工单总需求数量的HME_WO_ISSUE_EXT
                                        WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                                        BeanUtils.copyProperties(wmsObjectTransactionRequestVO, wmsObjectTransactionRequestVO2);
                                        wmsObjectTransactionRequestVO2.setBomReserveNum(null);
                                        wmsObjectTransactionRequestVO2.setBomReserveLineNum(null);
                                        wmsObjectTransactionRequestVO2.setSoNum(null);
                                        wmsObjectTransactionRequestVO2.setSoLineNum(null);
                                        wmsObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                                        wmsObjectTransactionRequestVO2.setTransactionQty(assembleQtySum.add(useQtyBig).subtract(lineAttribute5));
                                        transactionRequestVOList.add(wmsObjectTransactionRequestVO2);
                                    }
                                }
                            }
                            //调用API{woComponentAssemble} 进行生产指令组件装配
                            MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
                            mtWoComponentActualVO1.setEventRequestId(eventRequestId);
                            mtWoComponentActualVO1.setLocatorId(mtMaterialLot.getLocatorId());
                            mtWoComponentActualVO1.setMaterialId(mtMaterialLot.getMaterialId());
                            mtWoComponentActualVO1.setOperationId(operationId);
                            mtWoComponentActualVO1.setParentEventId(parentEventId);
                            mtWoComponentActualVO1.setRouterStepId(routerStepId);
                            mtWoComponentActualVO1.setShiftDate(mtWkcShift.getShiftDate());
                            mtWoComponentActualVO1.setShiftCode(mtWkcShift.getShiftCode());
                            mtWoComponentActualVO1.setTrxAssembleQty(useQty);
                            mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
                            mtWoComponentActualVO1.setWorkOrderId(dto.getWorkOrderId());
                            //根据工单Bom+条码物料查询BomComponentId,如果找不到则assembleExcessFlag写入Y
                            List<MtBomComponent> mtBomComponents = mtBomComponentRepository.select(new MtBomComponent() {{
                                setTenantId(tenantId);
                                setBomId(mtWorkOrder.getBomId());
                                setMaterialId(mtMaterialLot.getMaterialId());
                            }});
                            if (CollectionUtils.isEmpty(mtBomComponents)) {
                                mtWoComponentActualVO1.setAssembleExcessFlag("Y");
                            } else {
                                if (mtBomComponents.size() > 1) {
                                    MtMaterial mtMaterial2 = mtMaterialRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialId());
                                    throw new MtException("HME_COS_039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_COS_039", "HME", mtMaterial2.getMaterialCode()));
                                }
                                mtWoComponentActualVO1.setAssembleExcessFlag("N");
                                mtWoComponentActualVO1.setBomComponentId(mtBomComponents.get(0).getBomComponentId());
                            }
                            mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);
                            if (breakFlag) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList)) {
            List<List<HmeMaterialLotLabCode>> lists = CommonUtils.splitSqlList(hmeMaterialLotLabCodeList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotLabCode> list : lists) {
                hmeMaterialLotLabCodeRepository.batchInsertSelective(list);
            }
        }
        if (CollectionUtils.isNotEmpty(updateMaterialLotLoads)) {
            List<String> sqlList = new ArrayList<>();
            for (HmeMaterialLotLoad hmeMaterialLotLoad : updateMaterialLotLoads) {
                sqlList.addAll(mtCustomDbRepository.getUpdateSql(hmeMaterialLotLoad));
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
        if (CollectionUtils.isNotEmpty(insetLoadJobList)) {
            List<String> sqlList = new ArrayList<>();
            for (HmeLoadJob hmeLoadJob : insetLoadJobList) {
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeLoadJob));
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                List<List<String>> lists = CommonUtils.splitSqlList(sqlList, SQL_ITEM_COUNT_LIMIT);
                for (List<String> list : lists) {
                    jdbcTemplate.batchUpdate(list.toArray(new String[list.size()]));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(insetHmeLoadJobObjectList)) {
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_load_job_object_s", insetHmeLoadJobObjectList.size());
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_load_job_object_cid_s", insetHmeLoadJobObjectList.size());
            List<String> sqlList = new ArrayList<>();
            Integer indexNum = 0;
            for (HmeLoadJobObject hmeLoadJobObject : insetHmeLoadJobObjectList) {
                hmeLoadJobObject.setLoadObjectId(ids.get(indexNum));
                hmeLoadJobObject.setObjectVersionNumber(1L);
                hmeLoadJobObject.setCid(Long.valueOf(cids.get(indexNum++)));
                hmeLoadJobObject.setCreationDate(currentDate);
                hmeLoadJobObject.setCreatedBy(userId);
                hmeLoadJobObject.setLastUpdatedBy(userId);
                hmeLoadJobObject.setLastUpdateDate(currentDate);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeLoadJobObject));
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                List<List<String>> lists = CommonUtils.splitSqlList(sqlList, SQL_ITEM_COUNT_LIMIT);
                for (List<String> list : lists) {
                    jdbcTemplate.batchUpdate(list.toArray(new String[list.size()]));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
        }
        //批量更新物料批
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
    }

    @Override
    public HmeCosPatchPdaVO6 getChipQty(Long tenantId, HmeCosPatchPdaDTO9 dto) {
        String operationId = dto.getOperationIdList().get(0);
        //根据工单ID+工艺+Wkc_ID从hme_eo_job_sn表内获取job_type等于“COS_PASTER_IN”，进站时间最近行项目的material_lot_id
        String materialLotId = hmeCosPatchPdaMapper.getMaterialLotId(tenantId, dto.getWorkOrderId(), operationId, dto.getWorkcellId());
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("HME_COS_PATCH_PDA_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0010", "HME"));
        }
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
        //COS类型
        String cosType = getAttrValue(tenantId, mtMaterialLot.getMaterialLotId(), "COS_TYPE");

        HmeContainerCapacity hmeContainerCapacity = hmeContainerCapacityRepository.selectOne(new HmeContainerCapacity() {{
            setOperationId(operationId);
            setCosType(cosType);
            setContainerTypeId(dto.getContainerTypeId());
        }});
        if (hmeContainerCapacity == null) {
            throw new MtException("HME_COS_PATCH_PDA_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0005", "HME"));
        }
        if (hmeContainerCapacity.getCapacity() != 1) {
            throw new MtException("HME_COS_PATCH_PDA_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0008", "HME"));
        }
        long singleChipTotal = hmeContainerCapacity.getLineNum() * hmeContainerCapacity.getColumnNum() * hmeContainerCapacity.getCapacity();
        HmeCosPatchPdaVO6 hmeCosPatchPdaVO6 = new HmeCosPatchPdaVO6();
        hmeCosPatchPdaVO6.setQty(singleChipTotal);
        return hmeCosPatchPdaVO6;
    }

    String getAttrValue(Long tenantId, String materialLotId, String attrName) {
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLotId);
        mtMaterialLotAttrVO2.setAttrName(attrName);
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            return mtExtendAttrVOS.get(0).getAttrValue();
        }
        return null;
    }

    @Override
    public HmeCosPatchPdaVO8 containerLoadingEo(Long tenantId, HmeContainerCapacity hmeContainerCapacity, String materialLotId, long chipTotal, String cosType, HmeCosPatchPdaDTO4 dto, HmeCosOperationRecord hmeCosOperationRecord) {
        HmeCosPatchPdaVO8 hmeCosPatchPdaVO8 = new HmeCosPatchPdaVO8();
        HmeMaterialLotLoad hmeMaterialLotLoad = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
        Date now = new Date();
        String nowStr = sdf.format(now);
        int m = 0;
        boolean breakFlag = false;
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = new ArrayList<>();
        List<HmeLoadJob> hmeLoadJobList = new ArrayList<>();
        List<HmeLoadJobObject> hmeLoadJobObjectList = new ArrayList<>();
        if ("B".equals(hmeContainerCapacity.getAttribute1())) {
            //装载规则为从上到下，从左到右
            for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                for (int k = 1; k <= hmeContainerCapacity.getLineNum(); k++) {
                    //装载芯片数够了之后，跳出循环
                    if (m + 1 > chipTotal) {
                        breakFlag = true;
                        break;
                    }
                    hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setMaterialLotId(materialLotId);
                    StringBuffer loadSequence = new StringBuffer();
                    loadSequence.append(materialLotId.substring(0, materialLotId.length() - 2) + k + j + nowStr);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence.toString());
                    m++;
                    hmeMaterialLotLoad.setLoadRow(Long.valueOf(k));
                    hmeMaterialLotLoad.setLoadColumn(Long.valueOf(j));
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setAttribute1(cosType);
                    hmeMaterialLotLoad.setAttribute2(dto.getWafer());
                    hmeMaterialLotLoad.setAttribute3(dto.getWorkOrderId());
                    hmeMaterialLotLoad.setAttribute10(dto.getAssetEncoding());
                    if (StringUtils.isNotBlank(dto.getAuSnRatio())) {
                        hmeMaterialLotLoad.setAttribute13(dto.getAuSnRatio());
                    }
                    //2021-10-27 add by chaonan.hu for yiming.zhang 增加实验代码与备注记录到装载表
                    if (StringUtils.isNotBlank(dto.getLabCode())) {
                        hmeMaterialLotLoad.setAttribute19(dto.getLabCode());
                    }
                    if (StringUtils.isNotBlank(dto.getLabRemark())) {
                        hmeMaterialLotLoad.setAttribute20(dto.getLabRemark());
                    }
                    hmeMaterialLotLoadList.add(hmeMaterialLotLoad);
                    //2021-02-01 13:54 add by chaonan.hu for zhenyong.ban 把信息记录到装载信息作业记录表
                    HmeCosPatchPdaVO8 vo8 = createLoadJobNew(tenantId, hmeMaterialLotLoad, dto, "COS_PASTER_OUT", hmeCosOperationRecord);
                    if(CollectionUtils.isNotEmpty(vo8.getHmeLoadJobList())) {
                        hmeLoadJobList.addAll(vo8.getHmeLoadJobList());
                    }
                    if (CollectionUtils.isNotEmpty(vo8.getHmeLoadJobObjectList())) {
                        hmeLoadJobObjectList.addAll(vo8.getHmeLoadJobObjectList());
                    }
                }
                if (breakFlag) {
                    break;
                }
            }
        } else if ("C".equals(hmeContainerCapacity.getAttribute1())) {
            //装载规则为从上到下，从右到左
            for (int j = hmeContainerCapacity.getColumnNum().intValue(); j >= 1; j--) {
                for (int k = 1; k <= hmeContainerCapacity.getLineNum(); k++) {
                    //装载芯片数够了之后，跳出循环
                    if (m + 1 > chipTotal) {
                        breakFlag = true;
                        break;
                    }
                    hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setMaterialLotId(materialLotId);
                    StringBuffer loadSequence = new StringBuffer();
                    loadSequence.append(materialLotId.substring(0, materialLotId.length() - 2) + k + j + nowStr);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence.toString());
                    m++;
                    hmeMaterialLotLoad.setLoadRow(Long.valueOf(k));
                    hmeMaterialLotLoad.setLoadColumn(Long.valueOf(j));
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setAttribute1(cosType);
                    hmeMaterialLotLoad.setAttribute2(dto.getWafer());
                    hmeMaterialLotLoad.setAttribute3(dto.getWorkOrderId());
                    hmeMaterialLotLoad.setAttribute10(dto.getAssetEncoding());
                    if (StringUtils.isNotBlank(dto.getAuSnRatio())) {
                        hmeMaterialLotLoad.setAttribute13(dto.getAuSnRatio());
                    }
                    //2021-10-27 add by chaonan.hu for yiming.zhang 增加实验代码与备注记录到装载表
                    if (StringUtils.isNotBlank(dto.getLabCode())) {
                        hmeMaterialLotLoad.setAttribute19(dto.getLabCode());
                    }
                    if (StringUtils.isNotBlank(dto.getLabRemark())) {
                        hmeMaterialLotLoad.setAttribute20(dto.getLabRemark());
                    }
                    hmeMaterialLotLoadList.add(hmeMaterialLotLoad);
                    //2021-02-01 13:54 add by chaonan.hu for zhenyong.ban 把信息记录到装载信息作业记录表
                    HmeCosPatchPdaVO8 vo8 = createLoadJobNew(tenantId, hmeMaterialLotLoad, dto, "COS_PASTER_OUT", hmeCosOperationRecord);
                    if(CollectionUtils.isNotEmpty(vo8.getHmeLoadJobList())) {
                        hmeLoadJobList.addAll(vo8.getHmeLoadJobList());
                    }
                    if (CollectionUtils.isNotEmpty(vo8.getHmeLoadJobObjectList())) {
                        hmeLoadJobObjectList.addAll(vo8.getHmeLoadJobObjectList());
                    }
                }
                if (breakFlag) {
                    break;
                }
            }
        } else if ("D".equals(hmeContainerCapacity.getAttribute1())) {
            //装载规则为从下到上，从右到左
            for (int j = hmeContainerCapacity.getColumnNum().intValue(); j >= 1; j--) {
                for (int k = hmeContainerCapacity.getLineNum().intValue(); k >= 1; k--) {
                    if (m + 1 > chipTotal) {
                        breakFlag = true;
                        break;
                    }
                    hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setMaterialLotId(materialLotId);
                    StringBuffer loadSequence = new StringBuffer();
                    loadSequence.append(materialLotId.substring(0, materialLotId.length() - 2) + k + j + nowStr);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence.toString());
                    m++;
                    hmeMaterialLotLoad.setLoadRow(Long.valueOf(k));
                    hmeMaterialLotLoad.setLoadColumn(Long.valueOf(j));
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setAttribute1(cosType);
                    hmeMaterialLotLoad.setAttribute2(dto.getWafer());
                    hmeMaterialLotLoad.setAttribute3(dto.getWorkOrderId());
                    hmeMaterialLotLoad.setAttribute10(dto.getAssetEncoding());
                    if (StringUtils.isNotBlank(dto.getAuSnRatio())) {
                        hmeMaterialLotLoad.setAttribute13(dto.getAuSnRatio());
                    }
                    //2021-10-27 add by chaonan.hu for yiming.zhang 增加实验代码与备注记录到装载表
                    if (StringUtils.isNotBlank(dto.getLabCode())) {
                        hmeMaterialLotLoad.setAttribute19(dto.getLabCode());
                    }
                    if (StringUtils.isNotBlank(dto.getLabRemark())) {
                        hmeMaterialLotLoad.setAttribute20(dto.getLabRemark());
                    }
                    hmeMaterialLotLoadList.add(hmeMaterialLotLoad);
                    //2021-02-01 13:54 add by chaonan.hu for zhenyong.ban 把信息记录到装载信息作业记录表
                    HmeCosPatchPdaVO8 vo8 = createLoadJobNew(tenantId, hmeMaterialLotLoad, dto, "COS_PASTER_OUT", hmeCosOperationRecord);
                    if(CollectionUtils.isNotEmpty(vo8.getHmeLoadJobList())) {
                        hmeLoadJobList.addAll(vo8.getHmeLoadJobList());
                    }
                    if (CollectionUtils.isNotEmpty(vo8.getHmeLoadJobObjectList())) {
                        hmeLoadJobObjectList.addAll(vo8.getHmeLoadJobObjectList());
                    }
                }
                if (breakFlag) {
                    break;
                }
            }
        } else if ("E".equals(hmeContainerCapacity.getAttribute1())) {
            //装载规则为从下到上，从左到右
            for (int j = 1; j <= hmeContainerCapacity.getColumnNum().intValue(); j++) {
                for (int k = hmeContainerCapacity.getLineNum().intValue(); k >= 1; k--) {
                    if (m + 1 > chipTotal) {
                        breakFlag = true;
                        break;
                    }
                    hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setMaterialLotId(materialLotId);
                    StringBuffer loadSequence = new StringBuffer();
                    loadSequence.append(materialLotId.substring(0, materialLotId.length() - 2) + k + j + nowStr);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence.toString());
                    m++;
                    hmeMaterialLotLoad.setLoadRow(Long.valueOf(k));
                    hmeMaterialLotLoad.setLoadColumn(Long.valueOf(j));
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setAttribute1(cosType);
                    hmeMaterialLotLoad.setAttribute2(dto.getWafer());
                    hmeMaterialLotLoad.setAttribute3(dto.getWorkOrderId());
                    hmeMaterialLotLoad.setAttribute10(dto.getAssetEncoding());
                    if (StringUtils.isNotBlank(dto.getAuSnRatio())) {
                        hmeMaterialLotLoad.setAttribute13(dto.getAuSnRatio());
                    }
                    //2021-10-27 add by chaonan.hu for yiming.zhang 增加实验代码与备注记录到装载表
                    if (StringUtils.isNotBlank(dto.getLabCode())) {
                        hmeMaterialLotLoad.setAttribute19(dto.getLabCode());
                    }
                    if (StringUtils.isNotBlank(dto.getLabRemark())) {
                        hmeMaterialLotLoad.setAttribute20(dto.getLabRemark());
                    }
                    hmeMaterialLotLoadList.add(hmeMaterialLotLoad);
                    //2021-02-01 13:54 add by chaonan.hu for zhenyong.ban 把信息记录到装载信息作业记录表
                    HmeCosPatchPdaVO8 vo8 = createLoadJobNew(tenantId, hmeMaterialLotLoad, dto, "COS_PASTER_OUT", hmeCosOperationRecord);
                    if(CollectionUtils.isNotEmpty(vo8.getHmeLoadJobList())) {
                        hmeLoadJobList.addAll(vo8.getHmeLoadJobList());
                    }
                    if (CollectionUtils.isNotEmpty(vo8.getHmeLoadJobObjectList())) {
                        hmeLoadJobObjectList.addAll(vo8.getHmeLoadJobObjectList());
                    }
                }
                if (breakFlag) {
                    break;
                }
            }
        } else {
            //剩余情况一律按照A处理，装载规则：从左到右， 从上到下
            for (int j = 1; j <= hmeContainerCapacity.getLineNum(); j++) {
                for (int k = 1; k <= hmeContainerCapacity.getColumnNum(); k++) {
                    if (m + 1 > chipTotal) {
                        breakFlag = true;
                        break;
                    }
                    hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setMaterialLotId(materialLotId);
                    StringBuffer loadSequence = new StringBuffer();
                    loadSequence.append(materialLotId.substring(0, materialLotId.length() - 2) + k + j + nowStr);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence.toString());
                    m++;
                    hmeMaterialLotLoad.setLoadRow(Long.valueOf(j));
                    hmeMaterialLotLoad.setLoadColumn(Long.valueOf(k));
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setAttribute1(cosType);
                    hmeMaterialLotLoad.setAttribute2(dto.getWafer());
                    hmeMaterialLotLoad.setAttribute3(dto.getWorkOrderId());
                    hmeMaterialLotLoad.setAttribute10(dto.getAssetEncoding());
                    if (StringUtils.isNotBlank(dto.getAuSnRatio())) {
                        hmeMaterialLotLoad.setAttribute13(dto.getAuSnRatio());
                    }
                    //2021-10-27 add by chaonan.hu for yiming.zhang 增加实验代码与备注记录到装载表
                    if (StringUtils.isNotBlank(dto.getLabCode())) {
                        hmeMaterialLotLoad.setAttribute19(dto.getLabCode());
                    }
                    if (StringUtils.isNotBlank(dto.getLabRemark())) {
                        hmeMaterialLotLoad.setAttribute20(dto.getLabRemark());
                    }
                    hmeMaterialLotLoadList.add(hmeMaterialLotLoad);
                    //2021-02-01 13:54 add by chaonan.hu for zhenyong.ban 把信息记录到装载信息作业记录表
                    HmeCosPatchPdaVO8 vo8 = createLoadJobNew(tenantId, hmeMaterialLotLoad, dto, "COS_PASTER_OUT", hmeCosOperationRecord);
                    if(CollectionUtils.isNotEmpty(vo8.getHmeLoadJobList())) {
                        hmeLoadJobList.addAll(vo8.getHmeLoadJobList());
                    }
                    if (CollectionUtils.isNotEmpty(vo8.getHmeLoadJobObjectList())) {
                        hmeLoadJobObjectList.addAll(vo8.getHmeLoadJobObjectList());
                    }
                }
                if (breakFlag) {
                    break;
                }
            }
        }
        hmeCosPatchPdaVO8.setHmeMaterialLotLoadList(hmeMaterialLotLoadList);
        hmeCosPatchPdaVO8.setHmeLoadJobList(hmeLoadJobList);
        hmeCosPatchPdaVO8.setHmeLoadJobObjectList(hmeLoadJobObjectList);
        return hmeCosPatchPdaVO8;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtNumrangeVO8 createIncomingValueList(Long tenantId, HmeCosPatchPdaDTO4 dto, Long total, String cosType) {
        log.info("<====== HmeCosPatchPdaRepositoryImpl.createIncomingValueList dto={}, total={}, cosType={}", dto, total, cosType);
        //根据工厂ID查询工厂简码
        List<HmeSnBindEoVO2> hmeSnBindEoVO2s = hmeSnBindEoMapper.modSiteAttrValueGet(tenantId, Collections.singletonList(dto.getSiteId()));
        if (CollectionUtils.isEmpty(hmeSnBindEoVO2s)) {
            throw new MtException("HME_SN_BIND_EO_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SN_BIND_EO_001", "HME"));
        }
        String siteShortName = hmeSnBindEoVO2s.get(0).getSiteCode();
        //根据产线ID查询产线简码
        String prodLineShortName = hmeCosPatchPdaMapper.getProdLineShortName(tenantId, dto.getProdLineId());
        if (StringUtils.isEmpty(prodLineShortName)) {
            throw new MtException("HME_SN_BIND_EO_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SN_BIND_EO_002", "HME"));
        }
        //日期5位 如20201003-20A03
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
        //月：1-9，A-Z
        String month = hmeSnBindEoRepository.handleMonth(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DATE));
        if (day.length() == 1) {
            day = "0" + day;
        }
        //组装规则编码 站点简码产线简码日期批次号cos类型
        //2020-10-08 edit by chaonan.hu for zhenyong.ban 规则编码中增加cos类型
        StringBuffer codeStr = new StringBuffer();
        codeStr.append(siteShortName).append(prodLineShortName)
                .append(year).append(month).append(day).append(dto.getLot()).append(cosType);
        String ruleCode = codeStr.toString();
        MtNumrangeVO11 vo11 = new MtNumrangeVO11();
        vo11.setSequence(0L);
        List<String> valList = new ArrayList<>();
        valList.add(ruleCode);
        vo11.setIncomingValue(valList);
        //调用API{numrangeBatchGenerate}
        MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
        mtNumrangeVO9.setObjectCode("COS_PASTER_CODE");
        mtNumrangeVO9.setIncomingValueList(Collections.singletonList(vo11));
        mtNumrangeVO9.setObjectNumFlag("Y");
        mtNumrangeVO9.setNumQty(total);
        MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
        log.info("<====== HmeCosPatchPdaRepositoryImpl.createIncomingValueList {}", mtNumrangeVO8);
        return mtNumrangeVO8;
    }


    private HmeCosPatchPdaVO8 createLoadJobNew (Long tenantId, HmeMaterialLotLoad hmeMaterialLotLoad, HmeCosPatchPdaDTO4 dto, String loadJobType, HmeCosOperationRecord hmeCosOperationRecord) {
        String loadJobId = mtCustomDbRepository.getNextKey("hme_load_job_s");
        HmeCosPatchPdaVO8 hmeCosPatchPdaVO8 = new HmeCosPatchPdaVO8();
        HmeLoadJob hmeLoadJob = new HmeLoadJob();
        hmeLoadJob.setTenantId(tenantId);
        hmeLoadJob.setSiteId(dto.getSiteId());
        hmeLoadJob.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
        hmeLoadJob.setLoadJobType(loadJobType);
        hmeLoadJob.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeMaterialLotLoad.getMaterialLotId());
        hmeLoadJob.setMaterialId(mtMaterialLot.getMaterialId());
        hmeLoadJob.setLoadRow(hmeMaterialLotLoad.getLoadRow());
        hmeLoadJob.setLoadColumn(hmeMaterialLotLoad.getLoadColumn());
        hmeLoadJob.setCosNum(1L);
        hmeLoadJob.setOperationId(dto.getOperationIdList().get(0));
        hmeLoadJob.setWorkcellId(dto.getWorkcellId());
        hmeLoadJob.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeLoadJob.setWaferNum(hmeCosOperationRecord.getWafer());
        hmeLoadJob.setCosType(hmeCosOperationRecord.getCosType());
        hmeLoadJob.setStatus("0");
        hmeLoadJob.setLoadJobId(loadJobId);
        hmeCosPatchPdaVO8.setHmeLoadJobList(Collections.singletonList(hmeLoadJob));
        List<HmeLoadJobObject> hmeLoadJobObjectList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getAssetEncoding())) {
            List<String> assetEncodingList = Arrays.asList(StringUtils.split(dto.getAssetEncoding(), "/"));
            for (String assetEncoding : assetEncodingList) {
                HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                    setTenantId(tenantId);
                    setAssetEncoding(assetEncoding);
                }});
                if (Objects.nonNull(hmeEquipment)) {
                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                    hmeLoadJobObject.setTenantId(tenantId);
                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                    hmeLoadJobObject.setObjectType("EQUIPMENT");
                    hmeLoadJobObject.setObjectId(hmeEquipment.getEquipmentId());
                    hmeLoadJobObjectList.add(hmeLoadJobObject);
                }
            }
        }
        hmeCosPatchPdaVO8.setHmeLoadJobObjectList(hmeLoadJobObjectList);
        return hmeCosPatchPdaVO8;
    }

    @Override
    public void createLoadJob(Long tenantId, HmeMaterialLotLoad hmeMaterialLotLoad, HmeCosPatchPdaDTO4 dto, String loadJobType, HmeCosOperationRecord hmeCosOperationRecord) {
        HmeLoadJob hmeLoadJob = new HmeLoadJob();
        hmeLoadJob.setTenantId(tenantId);
        hmeLoadJob.setSiteId(dto.getSiteId());
        hmeLoadJob.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
        hmeLoadJob.setLoadJobType(loadJobType);
        hmeLoadJob.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeMaterialLotLoad.getMaterialLotId());
        hmeLoadJob.setMaterialId(mtMaterialLot.getMaterialId());
        hmeLoadJob.setLoadRow(hmeMaterialLotLoad.getLoadRow());
        hmeLoadJob.setLoadColumn(hmeMaterialLotLoad.getLoadColumn());
        hmeLoadJob.setCosNum(1L);
        hmeLoadJob.setOperationId(dto.getOperationIdList().get(0));
        hmeLoadJob.setWorkcellId(dto.getWorkcellId());
        hmeLoadJob.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeLoadJob.setWaferNum(hmeCosOperationRecord.getWafer());
        hmeLoadJob.setCosType(hmeCosOperationRecord.getCosType());
        hmeLoadJob.setStatus("0");
        hmeLoadJobRepository.insertSelective(hmeLoadJob);
        if (StringUtils.isNotBlank(dto.getAssetEncoding())) {
            List<String> assetEncodingList = Arrays.asList(StringUtils.split(dto.getAssetEncoding(), "/"));
            for (String assetEncoding : assetEncodingList) {
                HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                    setTenantId(tenantId);
                    setAssetEncoding(assetEncoding);
                }});
                if (Objects.nonNull(hmeEquipment)) {
                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                    hmeLoadJobObject.setTenantId(tenantId);
                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                    hmeLoadJobObject.setObjectType("EQUIPMENT");
                    hmeLoadJobObject.setObjectId(hmeEquipment.getEquipmentId());
                    hmeLoadJobObjectRepository.insertSelective(hmeLoadJobObject);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialLotRecall(Long tenantId, List<HmeCosPatchPdaVO2> dtoList) {
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO(){{
            setEventTypeCode("COS_PASTER_OUT_CANCEL");
        }});
        for (HmeCosPatchPdaVO2 hmeCosPatchPdaVO2:dtoList) {
            //删除eoJobSn表的作业数据
            hmeEoJobSnRepository.deleteByPrimaryKey(hmeCosPatchPdaVO2.getJobId());
            //2021-04-22 15:08 edit by chaonan.hu for kang.wang 物料批更新改为批量
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(hmeCosPatchPdaVO2.getMaterialLotId());
            mtMaterialLotVO20.setEnableFlag(NO);
            mtMaterialLotVO20List.add(mtMaterialLotVO20);

//            //将条码失效
//            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//            mtMaterialLotVO2.setMaterialLotId(hmeCosPatchPdaVO2.getMaterialLotId());
//            mtMaterialLotVO2.setEnableFlag(NO);
//            mtMaterialLotVO2.setEventId(eventId);
//            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
            //更新条码装载表数据
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.select(new HmeMaterialLotLoad() {{
                setTenantId(tenantId);
                setMaterialLotId(hmeCosPatchPdaVO2.getMaterialLotId());
            }});
            for (HmeMaterialLotLoad hmeMaterialLotLoad:hmeMaterialLotLoadList) {
                hmeMaterialLotLoad.setSourceMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
                hmeMaterialLotLoad.setMaterialLotId("");
                hmeMaterialLotLoad.setSourceLoadRow(hmeMaterialLotLoad.getLoadRow());
                hmeMaterialLotLoad.setSourceLoadColumn(hmeMaterialLotLoad.getLoadColumn());
                hmeMaterialLotLoadMapper.updateByPrimaryKey(hmeMaterialLotLoad);
            }
        }
        //批量更新物料批
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosPatchPdaDTO5 printRecall(Long tenantId, HmeCosPatchPdaDTO5 dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        String operationId = dto.getOperationIdList().get(0);
        //根据工单ID+工艺+Wkc_ID+Wafer+设备ID（可为空），获取工单工艺工位在制记录表hme_cos_operation_record内，剩余芯片数>0的行数据
        HmeCosOperationRecord cosOperationRecord = hmeCosPatchPdaMapper.opRecordQuery(tenantId, operationId, dto);
        if (cosOperationRecord == null) {
            throw new MtException("HME_COS_PATCH_PDA_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0006", "HME"));
        }
        //要处理的EO集合
        Map<String, HmeEoJobSn> hmeEoJobSnMap = new HashMap<>();
        Map<String, MtMaterialLot> mtMaterialLotMap = new HashMap<>();
        List<HmeMaterialLotLoad> hmeMaterialLotLoads = new ArrayList<>();
        Map<String, String> attrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dto.getJobIdList())) {
            // 获取作业Eo
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).select(HmeEoJobSn.FIELD_JOB_ID, HmeEoJobSn.FIELD_EO_ID, HmeEoJobSn.FIELD_MATERIAL_LOT_ID, HmeEoJobSn.FIELD_SITE_OUT_DATE).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                    .andIn(HmeEoJobSn.FIELD_JOB_ID, dto.getJobIdList())).build());
            // 批量更新出站记录
            hmeCosPatchPdaMapper.recallEoJobSn(tenantId, userId, dto.getJobIdList());

            hmeEoJobSnMap = hmeEoJobSnList.stream().collect(Collectors.toMap(HmeEoJobSn::getJobId, Function.identity()));

            List<String> materialLotIds = hmeEoJobSnList.stream().map(HmeEoJobSn::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            // 批量查询条码
            if (CollectionUtils.isNotEmpty(materialLotIds)) {
                List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).select(MtMaterialLot.FIELD_MATERIAL_LOT_ID, MtMaterialLot.FIELD_MATERIAL_LOT_CODE, MtMaterialLot.FIELD_LOCATOR_ID, MtMaterialLot.FIELD_PRIMARY_UOM_QTY)
                        .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId).andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, materialLotIds)).build());
                mtMaterialLotMap = mtMaterialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId, Function.identity()));

                Optional<HmeEoJobSn> firstOpt = hmeEoJobSnList.stream().filter(ejs -> Objects.isNull(ejs.getSiteOutDate())).findFirst();
                if (firstOpt.isPresent()) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotMap.getOrDefault(firstOpt.get().getMaterialLotId(), new MtMaterialLot());
                    throw new MtException("HME_COS_PRINT_CANCLE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PRINT_CANCLE_001", "HME", mtMaterialLot.getMaterialLotCode()));
                }
                List<MtExtendVO5> attrs = new ArrayList<>();
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("MF_FLAG");
                attrs.add(mtExtendVO5);
                List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1() {{
                    setTableName("mt_material_lot_attr");
                    setKeyIdList(materialLotIds);
                    setAttrs(attrs);
                }});
                attrMap = attrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }
            hmeMaterialLotLoads = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class).select(
                    HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID,
                    HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID,
                    HmeMaterialLotLoad.FIELD_LOAD_SEQUENCE,
                    HmeMaterialLotLoad.FIELD_LOAD_ROW,
                    HmeMaterialLotLoad.FIELD_LOAD_COLUMN).andWhere(Sqls.custom()
                    .andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                    .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIds)).build());
        }

        long processedNum = 0;
        MtMaterialLot mtMaterialLot = null;
        List<HmeLoadJob> deleteLoadJobList = new ArrayList();
        List<HmeLoadJobObject> deleteLoadJobObjectList = new ArrayList<>();
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList();
        List<MtInvOnhandQuantityVO13> onhandList = new ArrayList();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        // 创建事件
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("COS_PASTER_PRINT_CANCEL");
        }});

        for (String jobId : dto.getJobIdList()) {
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnMap.get(jobId);
            if (hmeEoJobSn != null) {
                mtMaterialLot = mtMaterialLotMap.getOrDefault(hmeEoJobSn.getMaterialLotId(), new MtMaterialLot());
                String mfFlag = attrMap.getOrDefault(hmeEoJobSn.getMaterialLotId(), "");
                // 在制品校验 不为在制品 则报错
                if (!YES.equals(mfFlag)) {
                    throw new MtException("HME_COS_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_065", "HME", mtMaterialLot.getMaterialLotCode()));
                }
                processedNum += mtMaterialLot.getPrimaryUomQty();

                // 查询COS履历信息
                List<HmeLoadJob> hmeLoadJobList = hmeCosPatchPdaMapper.queryLoadJob(tenantId, mtMaterialLot.getMaterialLotId(), dto.getWorkcellId(), dto.getWorkOrderId());
                if (CollectionUtils.isNotEmpty(hmeLoadJobList)) {
                    deleteLoadJobList.addAll(hmeLoadJobList);
                    List<String> loadJobIdList = hmeLoadJobList.stream().map(HmeLoadJob::getLoadJobId).collect(Collectors.toList());
                    // 装载信息作业对象表
                    List<HmeLoadJobObject> hmeLoadJobObjectList = hmeCosPatchPdaMapper.queryLoadJobObject(tenantId, loadJobIdList);
                    if (CollectionUtils.isNotEmpty(hmeLoadJobObjectList)) {
                        deleteLoadJobObjectList.addAll(hmeLoadJobObjectList);
                    }
                }
                // 取出热沉物料和条码
                MtMaterialLot hotSinkMaterialLot = hmeCosOperationRecordMapper.queryHotSinkMaterialLot(tenantId, mtMaterialLot.getMaterialLotId());

                // 装配信息
                List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                        .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, dto.getWorkOrderId())
                        .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, hotSinkMaterialLot.getMaterialId())).build());

                BigDecimal demandQty = BigDecimal.ZERO;
                BigDecimal assembleQty = BigDecimal.ZERO;
                String bomReserveNum = "";
                String bomReserveLineNum = "";
                //获取bomComponentId(bomId+物料)
                List<MtBomComponent> bomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
                    setTenantId(tenantId);
                    setBomId(mtWorkOrder.getBomId());
                    setMaterialId(hotSinkMaterialLot.getMaterialId());
                }});
                if (CollectionUtils.isNotEmpty(bomComponentList)) {
                    MtExtendVO mtExtendVO = new MtExtendVO();
                    mtExtendVO.setTableName("mt_bom_component_attr");
                    mtExtendVO.setAttrName("lineAttribute4");
                    mtExtendVO.setKeyId(bomComponentList.get(0).getBomComponentId());
                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                    demandQty = CollectionUtils.isNotEmpty(mtExtendAttrVOList) ? new BigDecimal(mtExtendAttrVOList.get(0).getAttrValue()) : BigDecimal.ZERO;

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

                if (CollectionUtils.isNotEmpty(mtWorkOrderComponentActualList)) {
                    assembleQty = mtWorkOrderComponentActualList.get(0).getAssembleQty() != null ? BigDecimal.valueOf(mtWorkOrderComponentActualList.get(0).getAssembleQty()) : BigDecimal.ZERO;
                }

                //分三种情况
                WmsTransactionTypeDTO externalTrxType = wmsTransactionTypeRepository.getTransactionType(tenantId, HME_WO_ISSUE_R_EXT);
                WmsTransactionTypeDTO internalTrxType = wmsTransactionTypeRepository.getTransactionType(tenantId, HME_WO_ISSUE_R);
                BigDecimal recallQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty());
                List<WmsObjectTransactionRequestVO> requestVOList = new ArrayList<>();
                if (recallQty.compareTo(assembleQty.subtract(demandQty)) <= 0) {
                    //退料数量<=工单组件装配数量-工单组件净需求数量 事务数量=退料数量，计划外退料
                    WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                    requestVO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                    requestVO.setTransactionReasonCode("COS计划外退料");
                    requestVO.setMoveReason("COS计划外退料");
                    requestVO.setTransactionQty(recallQty);
                    requestVO.setMoveType(externalTrxType != null ? externalTrxType.getMoveType() : "");
                    requestVOList.add(requestVO);
                } else if (assembleQty.subtract(demandQty).compareTo(BigDecimal.ZERO) > 0 && assembleQty.subtract(demandQty).compareTo(recallQty) < 0) {
                    // 0<工单组件装配数量-工单组件净需求数量退料数量<退料数量
                    //事务1： 事务数量=工单组件装配数量-工单组件净需求数量退料数量），计划外退料
                    WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                    requestOutVO.setTransactionTypeCode(HME_WO_ISSUE_R_EXT);
                    requestOutVO.setTransactionReasonCode("COS计划外退料");
                    requestOutVO.setMoveReason("COS计划外退料");
                    requestOutVO.setMoveType(externalTrxType != null ? externalTrxType.getMoveType() : "");
                    requestOutVO.setTransactionQty(assembleQty.subtract(demandQty));
                    requestVOList.add(requestOutVO);

                    //事务2：事务数量=退料数量-（工单组件装配数量-工单组件净需求数量退料数量），计划内退料
                    WmsObjectTransactionRequestVO requestInVO = new WmsObjectTransactionRequestVO();
                    requestInVO.setTransactionTypeCode(HME_WO_ISSUE_R);
                    requestOutVO.setTransactionReasonCode("COS计划内退料");
                    requestOutVO.setMoveReason("COS计划内退料");
                    requestInVO.setMoveType(internalTrxType != null ? internalTrxType.getMoveType() : "");
                    requestInVO.setTransactionQty(recallQty.subtract(assembleQty.subtract(demandQty)));

                    //计划内记录预留项目号及行号
                    requestInVO.setBomReserveNum(bomReserveNum);
                    requestInVO.setBomReserveLineNum(bomReserveLineNum);
                    requestVOList.add(requestInVO);
                } else if (assembleQty.subtract(demandQty).compareTo(BigDecimal.ZERO) <= 0) {
                    // 工单组件装配数量-工单组件净需求数量退料数量<=0 事务数量=退料数量，计划内退料
                    WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                    requestVO.setTransactionTypeCode(HME_WO_ISSUE_R);
                    requestVO.setTransactionReasonCode("COS计划内退料");
                    requestVO.setMoveReason("COS计划内退料");
                    requestVO.setMoveType(internalTrxType != null ? internalTrxType.getMoveType() : "");
                    requestVO.setTransactionQty(recallQty);
                    //计划内记录预留项目号及行号
                    requestVO.setBomReserveNum(bomReserveNum);
                    requestVO.setBomReserveLineNum(bomReserveLineNum);
                    requestVOList.add(requestVO);
                }

                // 记录工单退料事务
                for (WmsObjectTransactionRequestVO requestVO : requestVOList) {
                    WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                    transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                    transactionRequestVO.setTransactionReasonCode(requestVO.getTransactionReasonCode());
                    transactionRequestVO.setMoveReason(requestVO.getMoveReason());
                    transactionRequestVO.setEventId(eventId);
                    transactionRequestVO.setMaterialLotId(hotSinkMaterialLot.getMaterialLotId());
                    transactionRequestVO.setMaterialId(hotSinkMaterialLot.getMaterialId());
                    transactionRequestVO.setLotNumber(hotSinkMaterialLot.getLot());
                    transactionRequestVO.setTransactionQty(recallQty);
                    MtUom mtUom = mtUomRepository.selectByPrimaryKey(hotSinkMaterialLot.getPrimaryUomId());
                    transactionRequestVO.setTransactionUom(mtUom.getUomCode());
                    transactionRequestVO.setTransactionTime(new Date());
                    transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
                    MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(hotSinkMaterialLot.getLocatorId());
                    transactionRequestVO.setWarehouseId(mtModLocator != null ? mtModLocator.getParentLocatorId() : "");
                    transactionRequestVO.setLocatorId(hotSinkMaterialLot.getLocatorId());
                    transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    //查询产线
                    MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());
                    transactionRequestVO.setProdLineCode(productionLine != null ? productionLine.getProdLineCode() : "");
                    //移动类型
                    transactionRequestVO.setMoveType(requestVO.getMoveType());
                    transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                    transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                    transactionRequestVO.setRemark("COS贴片打印-热沉退料");
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(transactionRequestVO));
                }

                // 减组件装配实绩 只会找到一个
                MtWorkOrderComponentActual mtWorkOrderComponentActual = mtWorkOrderComponentActualList.get(0);
                MtWoComponentActualVO2 mtWoComponentActualVO2 = new MtWoComponentActualVO2();
                mtWoComponentActualVO2.setWorkOrderId(dto.getWorkOrderId());
                mtWoComponentActualVO2.setMaterialId(mtWorkOrderComponentActual.getMaterialId());
                mtWoComponentActualVO2.setBomComponentId(mtWorkOrderComponentActual.getBomComponentId());
                mtWoComponentActualVO2.setOperationId(mtWorkOrderComponentActual.getOperationId());
                mtWoComponentActualVO2.setRouterStepId(mtWorkOrderComponentActual.getRouterStepId());
                mtWoComponentActualVO2.setTrxAssembleQty(mtMaterialLot.getPrimaryUomQty() * -1);
                mtWoComponentActualVO2.setComponentType("ASSEMBLING");
                mtWoComponentActualVO2.setAssembleExcessFlag("N");
                mtWoComponentActualVO2.setAssembleRouterType(mtWorkOrderComponentActual.getAssembleRouterType());
                mtWoComponentActualVO2.setEventId(eventId);
                mtWorkOrderComponentActualRepository.woComponentActualUpdate(tenantId, mtWoComponentActualVO2);

                // 更新条码数量 加上扣减数量
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(hotSinkMaterialLot.getMaterialLotId());
                mtMaterialLotVO20.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty());
                mtMaterialLotVO20.setEnableFlag(YES);
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
                // 增加库存
                MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
                mtInvOnhandQuantityVO13.setSiteId(hotSinkMaterialLot.getSiteId());
                mtInvOnhandQuantityVO13.setMaterialId(hotSinkMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO13.setLocatorId(hotSinkMaterialLot.getLocatorId());
                mtInvOnhandQuantityVO13.setLotCode(hotSinkMaterialLot.getLot());
                mtInvOnhandQuantityVO13.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
                onhandList.add(mtInvOnhandQuantityVO13);
            }
        }
        // 增加剩余芯片数
        cosOperationRecord.setSurplusCosNum(cosOperationRecord.getSurplusCosNum()  + processedNum);
        hmeCosOperationRecordMapper.updateByPrimaryKeySelective(cosOperationRecord);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(cosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        // 删除打印履历
        if (CollectionUtils.isNotEmpty(deleteLoadJobList)) {
            hmeCosPatchPdaMapper.myBatchDeleteLoadJob(tenantId, userId, deleteLoadJobList);
        }
        if (CollectionUtils.isNotEmpty(deleteLoadJobObjectList)) {
            hmeCosPatchPdaMapper.myBatchDeleteLoadObject(tenantId, userId, deleteLoadJobObjectList);
        }
        // 更新装载热沉信息
        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoads)) {
            hmeCosPatchPdaMapper.myBatchCleanHotSink(tenantId, userId, hmeMaterialLotLoads);
        }
        if (CollectionUtils.isNotEmpty(mtMaterialLotVO20List)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
        if (CollectionUtils.isNotEmpty(onhandList)) {
            MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
            mtInvOnhandQuantityVO16.setEventId(eventId);
            mtInvOnhandQuantityVO16.setOnhandList(onhandList);
            mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
        }
        return dto;
    }

    @Override
    public void insertMaterialLotLobCode(Long tenantId, List<MtMaterialLot> mtMaterialLotList, List<HmeMaterialLotLoad> hmeMaterialLotLoads,
                                         String workcellId, String workOrderId) {
        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(mtMaterialLotList)){
            //根据条码ID查询其扩展属性实验代码
            List<String> materialLotIds = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
            List<HmeCosPatchPdaVO9> labCodeAttrList = hmeCosPatchPdaMapper.materialLotAttrQuery(tenantId, materialLotIds, "LAB_CODE");
            //实验代码不为空的条码才需要记录
            List<String> materialLotIdList = labCodeAttrList.stream().filter(item -> StringUtils.isNotBlank(item.getAttrValue())).map(HmeCosPatchPdaVO9::getMaterialLotId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(materialLotIdList)){
                List<HmeCosPatchPdaVO9> currentRouterStepList = hmeCosPatchPdaMapper.materialLotAttrQuery(tenantId, materialLotIdList, "CURRENT_ROUTER_STEP");
                for (String materialLotId:materialLotIdList) {
                    List<MtMaterialLot> singleMaterialLotInfo = mtMaterialLotList.stream().filter(item -> item.getMaterialLotId().equals(materialLotId)).collect(Collectors.toList());
                    List<HmeMaterialLotLoad> singleMaterialLotLoad = hmeMaterialLotLoads.stream().filter(item -> item.getMaterialLotId().equals(materialLotId)).collect(Collectors.toList());
                    List<HmeCosPatchPdaVO9> singleLabCode = labCodeAttrList.stream().filter(item -> item.getMaterialLotId().equals(materialLotId)).collect(Collectors.toList());
                    List<HmeCosPatchPdaVO9> singleCurrentRouterStepList = currentRouterStepList.stream().filter(item -> item.getMaterialLotId().equals(materialLotId)).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(singleMaterialLotLoad)){
                        for (HmeMaterialLotLoad hmeMaterialLotLoad:singleMaterialLotLoad) {
                            HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                            hmeMaterialLotLabCode.setTenantId(tenantId);
                            hmeMaterialLotLabCode.setMaterialLotId(materialLotId);
                            hmeMaterialLotLabCode.setObject("COS");
                            hmeMaterialLotLabCode.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                            hmeMaterialLotLabCode.setLabCode(singleLabCode.get(0).getAttrValue());
                            hmeMaterialLotLabCode.setWorkcellId(workcellId);
                            hmeMaterialLotLabCode.setWorkOrderId(workOrderId);
                            hmeMaterialLotLabCode.setSourceObject("MA");
                            if(CollectionUtils.isNotEmpty(singleCurrentRouterStepList)){
                                hmeMaterialLotLabCode.setRouterStepId(singleCurrentRouterStepList.get(0).getAttrValue());
                            }
                            hmeMaterialLotLabCode.setSourceMaterialLotId(materialLotId);
                            hmeMaterialLotLabCode.setSourceMaterialId(singleMaterialLotInfo.get(0).getMaterialId());
                            hmeMaterialLotLabCode.setEnableFlag(YES);
                            hmeMaterialLotLabCodeList.add(hmeMaterialLotLabCode);
                        }
                    }
                }
            }
        }
        //批量插入数据到表中
        if(CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList)){
            Date nowDate = new Date();
            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_material_lot_lab_code_s", hmeMaterialLotLabCodeList.size());
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", hmeMaterialLotLabCodeList.size());
            int i = 0;
            List<String> sqlList = new ArrayList<>();
            for (HmeMaterialLotLabCode hmeMaterialLotLabCode:hmeMaterialLotLabCodeList) {
                hmeMaterialLotLabCode.setLabCodeId(ids.get(i));
                hmeMaterialLotLabCode.setCid(Long.valueOf(cids.get(i)));
                hmeMaterialLotLabCode.setObjectVersionNumber(1L);
                hmeMaterialLotLabCode.setCreationDate(nowDate);
                hmeMaterialLotLabCode.setCreatedBy(userId);
                hmeMaterialLotLabCode.setLastUpdateDate(nowDate);
                hmeMaterialLotLabCode.setLastUpdatedBy(userId);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeMaterialLotLabCode));
                i++;
            }
            if (CollectionUtils.isNotEmpty(sqlList)) {
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
    }

    /**
     * 全局替代料找主料
     *
     * @param tenantId             租户ID
     * @param siteId               站点ID
     * @param substituteMaterialId 替代料ID
     * @param bomId                工单BOMID
     * @param routerOperationId
     * @return tarzan.method.domain.entity.MtBomComponent
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/16 09:49:33
     */
    MtBomComponent getPrimaryMaterialByGlobal(Long tenantId, String siteId, String substituteMaterialId, String bomId, String routerOperationId) {
        //根据站点Id、替代料Id关联表wms_material_substitute_rel查询SUBSTITUTE_GROUP
        List<WmsMaterialSubstituteRel> wmsMaterialSubstituteRelList = wmsMaterialSubstituteRelRepository.select(new WmsMaterialSubstituteRel() {{
            setTenantId(tenantId);
            setSiteId(siteId);
            setMaterialId(substituteMaterialId);
        }});
        if (CollectionUtils.isEmpty(wmsMaterialSubstituteRelList)) {
            return null;
        } else if (wmsMaterialSubstituteRelList.size() > 1) {
            //如果找到多个，则报错{当前全局替代料有多个替代组关系，请检查！}
            throw new MtException("HME_NC_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0043", "HME"));
        }
        //根据找的到替代组Id查询其下所有的替代料
        List<String> substituteMaterialList = hmeCosPatchPdaMapper.getSubstituteMaterialByGroup(tenantId, wmsMaterialSubstituteRelList.get(0).getSubstituteGroup());
        //根据所有的替代料Id、bomId在表mt_bom_component中查找数据
        List<MtBomComponent> mtBomComponentList = new ArrayList<>();
        for (String substituteMaterial : substituteMaterialList) {
            List<MtBomComponent> mtBomComponents = mtBomComponentRepository.select(new MtBomComponent() {{
                setTenantId(tenantId);
                setMaterialId(substituteMaterial);
                setBomId(bomId);
            }});
            if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                mtBomComponentList.addAll(mtBomComponents);
            }
        }
        if (CollectionUtils.isEmpty(mtBomComponentList)) {
            //如果找不到，则返回空
            return null;
        } else if (mtBomComponentList.size() == 1) {
            //如果找到一个，则直接返回
            return mtBomComponentList.get(0);
        } else {
            //根据多个bomComponentId、routerOperationId查询表mt_router_operation_component
            List<MtRouterOperationComponent> mtRouterOperationComponentList = new ArrayList<>();
            for (MtBomComponent mtBomComponent : mtBomComponentList) {
                MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
                mtRouterOperationComponent.setTenantId(tenantId);
                mtRouterOperationComponent.setBomComponentId(mtBomComponent.getBomComponentId());
                mtRouterOperationComponent.setRouterOperationId(routerOperationId);
                MtRouterOperationComponent mtRouterOperationComponentDb = mtRouterOperationComponentRepository.selectOne(mtRouterOperationComponent);
                if (Objects.nonNull(mtRouterOperationComponentDb)) {
                    mtRouterOperationComponentList.add(mtRouterOperationComponentDb);
                }
            }
            if (CollectionUtils.isEmpty(mtRouterOperationComponentList)) {
                //如果找不到，则报错{所扫描条码物料与装配清单不匹配,请检查}
                throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_073", "HME"));
            } else if (mtRouterOperationComponentList.size() > 1) {
                //如果找不到，则报错{当前统一工艺步骤存在多个相同组件，请检查}
                throw new MtException("HME_NC_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0048", "HME"));
            }
            String bomComponentId = mtRouterOperationComponentList.get(0).getBomComponentId();
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            return mtBomComponent;
        }
    }

    /**
     * Bom外主键料的投料事务 包含计划内、计划外
     *
     * @param tenantId            租户ID
     * @param transactionTypeCode 事务类型编码
     * @param mtBomComponent      Bom组件
     * @param dto                 参数信息
     * @return com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 11:11:04
     */
    WmsObjectTransactionRequestVO primaryAssembleTransaction(Long tenantId, String transactionTypeCode, MtBomComponent mtBomComponent, HmeCosPatchPdaDTO10 dto, Boolean materialLotFlag) {
        WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        objectTransactionRequestVO.setTransactionTypeCode(transactionTypeCode);
        objectTransactionRequestVO.setEventId(dto.getEventId());
        objectTransactionRequestVO.setMaterialId(mtBomComponent.getMaterialId());
        objectTransactionRequestVO.setTransactionQty(dto.getTransactionQty());
        if (materialLotFlag) {
            objectTransactionRequestVO.setMaterialLotId(dto.getMaterialLotId());
        }
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtBomComponent.getMaterialId());
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
        objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
        objectTransactionRequestVO.setTransactionTime(new Date());
        objectTransactionRequestVO.setTransactionReasonCode("COS主料投料");
        objectTransactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
        if (materialLotFlag) {
            objectTransactionRequestVO.setLocatorId(mtMaterialLot.getLocatorId());
            List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, mtMaterialLot.getLocatorId(), "TOP");
            if (CollectionUtils.isNotEmpty(pLocatorIds)) {
                MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                if (ploc != null) {
                    objectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
                }
            }
        } else {
            String warehouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
            if (StringUtils.isEmpty(warehouseCode)) {
                throw new MtException("HME_NC_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0056", "HME"));
            }
            MtModLocator warehouse = mtModLocatorRepository.selectOne(new MtModLocator() {{
                setTenantId(tenantId);
                setLocatorCode(warehouseCode);
            }});
            if (Objects.isNull(warehouse)) {
                throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0057", "HME"));
            }
            objectTransactionRequestVO.setWarehouseId(warehouse.getLocatorId());
            List<String> locatorIdList = hmeCosPatchPdaMapper.getSubLocatorByLocatorId(tenantId, warehouse.getLocatorId());
            if (CollectionUtils.isEmpty(locatorIdList) || locatorIdList.size() > 1) {
                throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0057", "HME"));
            }
            objectTransactionRequestVO.setLocatorId(locatorIdList.get(0));
        }
        objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        objectTransactionRequestVO.setMergeFlag(NO);
        objectTransactionRequestVO.setMoveType("261");
        objectTransactionRequestVO.setMoveReason("COS主料投料");
        if ("HME_WO_ISSUE".equals(transactionTypeCode)) {
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(mtBomComponent.getBomComponentId());
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                objectTransactionRequestVO.setBomReserveNum(mtExtendAttrVOS.get(0).getAttrValue());
            }
            objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            String lineAttribute11 = null;
            mtExtendVO.setAttrName("lineAttribute11");
            mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                lineAttribute11 = mtExtendAttrVOS.get(0).getAttrValue();
            }
            if (E.equals(lineAttribute11)) {
                MtExtendVO mtWorkOrderExtend = new MtExtendVO();
                mtWorkOrderExtend.setTableName("mt_work_order_attr");
                mtWorkOrderExtend.setKeyId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderExtend.setAttrName("attribute1");
                List<MtExtendAttrVO> mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
                mtWorkOrderExtend.setAttrName("attribute7");
                mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoLineNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
            }
        }
        return objectTransactionRequestVO;
    }
}


