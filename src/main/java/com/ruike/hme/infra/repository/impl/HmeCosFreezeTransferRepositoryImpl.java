package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeWoJobSnReturnDTO5;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
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
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/17 14:48
 */
@Component
public class HmeCosFreezeTransferRepositoryImpl implements HmeCosFreezeTransferRepository {

    @Autowired
    private HmeCosFreezeTransferMapper hmeCosFreezeTransferMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeChipTransferMapper hmeChipTransferMapper;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEoJobEquipmentService hmeEoJobEquipmentService;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;
    @Autowired
    private HmeLoadJobRepository hmeLoadJobRepository;
    @Autowired
    private HmeLoadJobMapper hmeLoadJobMapper;
    @Autowired
    private HmeLoadJobObjectRepository hmeLoadJobObjectRepository;
    @Autowired
    private HmeCosTransferHisRepository hmeCosTransferHisRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private HmeChipTransferRepository hmeChipTransferRepository;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;
    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosFreezeTransferVO2 sourceCodeSiteIn(Long tenantId, HmeCosFreezeTransferVO transferVO) {
        HmeCosFreezeTransferVO2 transferVO2 = new HmeCosFreezeTransferVO2();
        if (StringUtils.isBlank(transferVO.getTransferMaterialLotCode())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        //??????materialLotPropertyGet-?????????????????????
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, transferVO.getTransferMaterialLotCode());

        if (materialLot == null || !StringUtils.equals(materialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_002", "HME", transferVO.getTransferMaterialLotCode()));
        }

        //??????????????????????????????????????????
        hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, materialLot.getMaterialLotId());

        if (!StringUtils.equals(HmeConstants.ConstantValue.OK, materialLot.getQualityStatus())) {
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", materialLot.getMaterialLotCode()));
        }

        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());

        transferVO2.setMaterialId(materialLot.getMaterialId());
        if (mtMaterial != null) {
            transferVO2.setMaterialName(mtMaterial.getMaterialName());
            transferVO2.setMaterialCode(mtMaterial.getMaterialCode());
            List<MtMaterialBasic> mtMaterialBasics = hmeChipTransferMapper.queryMtMaterialBasicInfo(tenantId, mtMaterial.getMaterialId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasics)) {
                transferVO2.setMaterialType(mtMaterialBasics.get(0).getItemType());
            }
        }
        transferVO2.setReleaseQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
        transferVO2.setMaterialLotId(materialLot.getMaterialLotId());
        transferVO2.setReleaseLot(materialLot.getLot());


        //attrPropertyQuery-???????????????????????????
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), Collections.EMPTY_LIST);
        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
            switch (mtExtendAttrVO.getAttrName()) {
                case "WAFER_NUM":
                    //Wafer??????
                    transferVO2.setTransWaferNum(mtExtendAttrVO.getAttrValue());
                    break;
                case "TYPE":
                    //TYPE
                    transferVO2.setIncomingType(mtExtendAttrVO.getAttrValue());
                    break;
                case "LOTNO":
                    //LOTNO
                    transferVO2.setLotno(mtExtendAttrVO.getAttrValue());
                    break;
                case "CONTAINER_TYPE":
                    //CONTAINER_TYPE
                    transferVO2.setTransContainerType(mtExtendAttrVO.getAttrValue());
                    break;
                case "COS_TYPE":
                    //COS_TYPE
                    transferVO2.setCosType(mtExtendAttrVO.getAttrValue());
                    break;
                case "LOCATION_ROW":
                    transferVO2.setLocationRow(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "LOCATION_COLUMN":
                    transferVO2.setLocationColumn(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "CHIP_NUM":
                    transferVO2.setChipNum(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "WORK_ORDER_ID":
                    transferVO2.setWorkOrderId(mtExtendAttrVO.getAttrValue());
                    break;
                case "COS_RECORD":
                    transferVO2.setCosRecordId(mtExtendAttrVO.getAttrValue());
                    break;
                case "AVG_WAVE_LENGTH":
                    if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                        transferVO2.setAverageWavelength(new BigDecimal(mtExtendAttrVO.getAttrValue()));
                    }
                    break;
                case "REMARK":
                    transferVO2.setRemark(mtExtendAttrVO.getAttrValue());
                    break;
                case "LAB_CODE":
                    transferVO2.setLabCode(mtExtendAttrVO.getAttrValue());
                    break;
                case "LAB_REMARK":
                    transferVO2.setLabRemark(mtExtendAttrVO.getAttrValue());
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isNotBlank(transferVO2.getWorkOrderId())) {
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(transferVO2.getWorkOrderId());
            transferVO2.setWorkOrderNum(mtWorkOrder != null ? mtWorkOrder.getWorkOrderNum() : "");
        }
        // ?????? ???Y?????????
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", materialLot.getMaterialLotCode()));
        }
        // ?????? ??????Y?????????
        if (!HmeConstants.ConstantValue.YES.equals(materialLot.getFreezeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_020", "HME", materialLot.getMaterialLotCode()));
        }
        //??? ??? ??????????????????
        if (transferVO2.getLocationRow() == null || transferVO2.getLocationColumn() == null || transferVO2.getChipNum() == null) {
            throw new MtException("HME_CHIP_TRANSFER_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_016", "HME", materialLot.getMaterialLotCode()));
        }
        //??????????????????id
        HmeCosOperationRecord hmeIncomingRecord = hmeChipTransferMapper.queryHmeIncomingRecord(tenantId, transferVO2.getCosRecordId());
        if (Objects.nonNull(hmeIncomingRecord)) {
            transferVO2.setTransCosRecordId(hmeIncomingRecord.getOperationRecordId());
        }
        //????????????
        if (StringUtils.isNotBlank(transferVO2.getTransContainerType()) && StringUtils.isNotBlank(transferVO2.getCosType())) {
            Condition condition = new Condition(MtContainerType.class);
            condition.and().andEqualTo("containerTypeCode", transferVO2.getTransContainerType())
                    .andEqualTo("enableFlag", "Y");
            List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(condition);
            if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
                HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
                hmeContainerCapacity.setContainerTypeId(mtContainerTypeList.get(0).getContainerTypeId());
                hmeContainerCapacity.setOperationId(transferVO.getOperationId());
                hmeContainerCapacity.setCosType(transferVO2.getCosType());
                List<HmeContainerCapacity> capacity = hmeContainerCapacityRepository.select(hmeContainerCapacity);
                if (CollectionUtils.isNotEmpty(capacity)) {
                    transferVO2.setLoadRule(capacity.get(0).getAttribute1());
                    //????????????
                    List<LovValueDTO> rulesLovList = lovAdapter.queryLovValue("HME.LOADING_RULES", tenantId);
                    List<LovValueDTO> rulesList = rulesLovList.stream().filter(f -> StringUtils.equals(f.getValue(), capacity.get(0).getAttribute1())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(rulesList)) {
                        transferVO2.setLoadRuleMeaning(rulesList.get(0).getMeaning());
                    }
                }
            }
        }
        transferVO2.setWkcShiftId(transferVO.getWkcShiftId());
        transferVO2.setWorkcellId(transferVO.getWorkcellId());
        transferVO2.setOperationId(transferVO.getOperationId());
        //??????????????????hme_eo_job_sn???
        HmeEoJobSn hmeEoJobSn = this.createEoJobSnRecord(tenantId, hmeIncomingRecord, transferVO2, "FREEZE_TRANSFER_IN");
        hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, Collections.singletonList(hmeEoJobSn.getJobId()), transferVO.getWorkcellId());

        transferVO2.setEoJobSnId(hmeEoJobSn.getJobId());
        //????????????????????????
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(materialLot.getMaterialLotId());
        List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        List<HmeMaterialLotLoad> collect = select.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());
        for (HmeMaterialLotLoad lotLoad : collect) {
            HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
            hmeWoJobSnReturnDTO5.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
            hmeWoJobSnReturnDTO5.setLoadSequence(lotLoad.getLoadSequence());
            hmeWoJobSnReturnDTO5.setHotSinkCode(lotLoad.getHotSinkCode());
            hmeWoJobSnReturnDTO5.setFreezeFlag(lotLoad.getAttribute14());
            hmeWoJobSnReturnDTO5.setFreezeFlagMeaning(StringUtils.equals(lotLoad.getAttribute14(), HmeConstants.ConstantValue.YES) ? "???" : "???");
            hmeWoJobSnReturnDTO5.setChipLabCode(lotLoad.getAttribute19());
            hmeWoJobSnReturnDTO5.setChipLabRemark(lotLoad.getAttribute20());
            HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
            hmeMaterialLotNcLoad.setLoadSequence(lotLoad.getLoadSequence());
            List<HmeMaterialLotNcLoad> select1 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
            if (CollectionUtils.isNotEmpty(select1)) {
                hmeWoJobSnReturnDTO5.setDocList(select1);
            }
            hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
        }
        transferVO2.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s);
        return transferVO2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosFreezeTransferVO2 targetCodeSiteIn(Long tenantId, HmeCosFreezeTransferVO transferVO) {
        HmeCosFreezeTransferVO2 transferVO2 = new HmeCosFreezeTransferVO2();
        // ?????????????????? ????????????????????????
        if (StringUtils.isBlank(transferVO.getTransferMaterialLotCode())) {
            throw new MtException("HME_COS_BARCODE_RETEST_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_021", "HME"));
        }
        //??????materialLotPropertyGet-????????????????????????
        MtMaterialLot transLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, transferVO.getTransferMaterialLotCode());
        MtMaterialLot targetMaterialLot = new MtMaterialLot();
        Boolean createFlag = false;
        if (StringUtils.isBlank(transferVO.getMaterialLotCode())) {
            // ????????????
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("MATERIAL_LOT_INIT");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // ???????????????
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            CommonUtils.copyPropertiesIgnoreNullAndTableFields(transLot, mtMaterialLotVO2);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setMaterialLotId(null);
            mtMaterialLotVO2.setMaterialLotCode(null);
            mtMaterialLotVO2.setEnableFlag(NO);
            mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
            mtMaterialLotVO2.setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
            mtMaterialLotVO2.setCurrentContainerId(null);
            mtMaterialLotVO2.setTopContainerId(null);
            mtMaterialLotVO2.setCreateReason("INITIALIZE");
            MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

            MtMaterialLot queryMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLotVO13.getMaterialLotId());
            targetMaterialLot = queryMaterialLot;
            createFlag = true;
        } else {
            //??????materialLotPropertyGet-?????????????????????
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, transferVO.getMaterialLotCode());
            //??????
            if (materialLot == null) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", transferVO.getMaterialLotCode()));
            }
            targetMaterialLot = materialLot;
            createFlag = false;
        }
        if (!createFlag) {
            //??????????????????????????????????????????????????????
            if (!StringUtils.equals(transferVO.getMaterialLotCode(), transferVO.getTransferMaterialLotCode())) {
                hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, targetMaterialLot.getMaterialLotId());
            }
            //?????????????????????
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "FREEZE_TRANSFER_OUT")
                            .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, transferVO.getWorkcellId())
                            .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, targetMaterialLot.getMaterialLotId())
                            .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, transferVO.getOperationId())
                            .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                    .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());

            if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                throw new MtException("HME_CHIP_TRANSFER_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_014", "HME", transferVO.getMaterialLotCode()));
            }
        }

        transferVO2.setMaterialId(targetMaterialLot.getMaterialId());
        transferVO2.setReleaseQty(BigDecimal.valueOf(targetMaterialLot.getPrimaryUomQty()));
        transferVO2.setMaterialLotId(targetMaterialLot.getMaterialLotId());

        HmeCosOperationRecord hmeIncomingRecord = null;

        //??????????????????????????????
        List<MtExtendAttrVO> attrVOList = this.querySiteOutMaterialLot(tenantId, transferVO);

        //??????????????????????????????
        if (StringUtils.equals(HmeConstants.ConstantValue.NO, targetMaterialLot.getEnableFlag())) {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("MATERIAL_LOT_UPDATE");
            String createEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            if (!createFlag) {
                //????????????????????????????????????
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(targetMaterialLot.getMaterialLotId());
                mtMaterialLotVO2.setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
                mtMaterialLotVO2.setEventId(createEventId);
                mtMaterialLotVO2.setLot(transLot.getLot());
                mtMaterialLotVO2.setLocatorId(transLot.getLocatorId());
                mtMaterialLotVO2.setMaterialId(transLot.getMaterialId());
                mtMaterialLotVO2.setPrimaryUomId(transLot.getPrimaryUomId());
                mtMaterialLotVO2.setFreezeFlag(transLot.getFreezeFlag());
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
            }

            //??????????????????
            MtMaterialLotAttrVO3 vo3 = new MtMaterialLotAttrVO3();
            vo3.setMaterialLotId(targetMaterialLot.getMaterialLotId());
            vo3.setEventId(createEventId);
            List<MtExtendVO5> extendVO5List = new ArrayList<>();

            //??????????????????????????????
            HmeContainerCapacity containerCapacity = new HmeContainerCapacity();
            MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
                setContainerTypeCode(transferVO.getTransContainerType());
            }});

            MtExtendVO5 rowObj = new MtExtendVO5();
            rowObj.setAttrName("LOCATION_ROW");

            MtExtendVO5 colObj = new MtExtendVO5();
            colObj.setAttrName("LOCATION_COLUMN");

            MtExtendVO5 vo4 = new MtExtendVO5();
            vo4.setAttrName("CONTAINER_TYPE");

            //??????????????????  ??????????????????????????????
            if (CollectionUtils.isEmpty(attrVOList)) {
                if (mtContainerType != null) {
                    containerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
                    containerCapacity.setOperationId(transferVO.getOperationId());
                    containerCapacity.setCosType(transferVO.getTransCosType());
                    HmeContainerCapacity capacity = hmeContainerCapacityRepository.selectOne(containerCapacity);

                    vo4.setAttrValue(transferVO.getTransContainerType());

                    if (capacity != null) {
                        if (capacity.getLineNum() != null) {
                            rowObj.setAttrValue(String.valueOf(capacity.getLineNum()));
                            transferVO2.setLocationRow(capacity.getLineNum());
                        }
                        if (capacity.getColumnNum() != null) {
                            colObj.setAttrValue(String.valueOf(capacity.getColumnNum()));
                            transferVO2.setLocationColumn(capacity.getColumnNum());
                        }
                    }
                }
            } else {
                for (MtExtendAttrVO mtExtendAttrVO : attrVOList) {
                    switch (mtExtendAttrVO.getAttrName()) {
                        case "LOCATION_ROW":
                            rowObj.setAttrValue(mtExtendAttrVO.getAttrValue());
                            transferVO2.setLocationRow(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                            break;
                        case "LOCATION_COLUMN":
                            colObj.setAttrValue(mtExtendAttrVO.getAttrValue());
                            transferVO2.setLocationColumn(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                            break;
                        case "CONTAINER_TYPE":
                            vo4.setAttrValue(mtExtendAttrVO.getAttrValue());
                            transferVO2.setContainerType(mtExtendAttrVO.getAttrValue());
                            break;
                        default:
                            break;
                    }
                }
            }

            //????????????????????????????????? ?????????????????????????????????
            if (StringUtils.isBlank(rowObj.getAttrValue()) || StringUtils.isBlank(colObj.getAttrValue())) {
                rowObj.setAttrValue(String.valueOf(transferVO2.getLocationRow()));
                colObj.setAttrValue(String.valueOf(transferVO2.getLocationColumn()));
            }

            extendVO5List.add(rowObj);
            extendVO5List.add(colObj);

            if (StringUtils.isNotBlank(vo4.getAttrValue())) {
                extendVO5List.add(vo4);
            }

            //???????????? ??????????????????????????????
            if (transLot != null) {
                //???????????????????????????
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                        "mt_material_lot_attr", "MATERIAL_LOT_ID", transLot.getMaterialLotId(), Collections.EMPTY_LIST);

                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
                        switch (mtExtendAttrVO.getAttrName()) {
                            case "CHIP_NUM":
                                MtExtendVO5 vo8 = new MtExtendVO5();
                                vo8.setAttrName("CHIP_NUM");
                                vo8.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo8);
                                if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                                    transferVO2.setChipNum(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                                }
                                break;
                            case "COS_RECORD":
                                MtExtendVO5 vo1 = new MtExtendVO5();
                                vo1.setAttrName("COS_RECORD");
                                vo1.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo1);
                                if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                                    transferVO2.setCosRecordId(mtExtendAttrVO.getAttrValue());
                                }
                                break;
                            case "CONTAINER_TYPE":
                                if (StringUtils.isBlank(vo4.getAttrValue())) {
                                    vo4.setAttrValue(mtExtendAttrVO.getAttrValue());
                                    extendVO5List.add(vo4);
                                }
                                transferVO2.setTransContainerType(mtExtendAttrVO.getAttrValue());
                                break;
                            case "WAFER_NUM":
                                MtExtendVO5 vo2 = new MtExtendVO5();
                                vo2.setAttrName("WAFER_NUM");
                                vo2.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo2);
                                break;
                            case "COS_TYPE":
                                MtExtendVO5 vo5 = new MtExtendVO5();
                                vo5.setAttrName("COS_TYPE");
                                vo5.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo5);
                                transferVO2.setTransCosType(mtExtendAttrVO.getAttrValue());
                                break;
                            case "PRODUCT_DATE":
                                MtExtendVO5 vo6 = new MtExtendVO5();
                                vo6.setAttrName("PRODUCT_DATE");
                                vo6.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo6);
                                break;
                            case "STATUS":
                                MtExtendVO5 vo7 = new MtExtendVO5();
                                vo7.setAttrName("STATUS");
                                vo7.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo7);
                                break;
                            case "AVG_WAVE_LENGTH":
                                MtExtendVO5 vo9 = new MtExtendVO5();
                                vo9.setAttrName("AVG_WAVE_LENGTH");
                                vo9.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo9);
                                if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                                    transferVO2.setAverageWavelength(new BigDecimal(mtExtendAttrVO.getAttrValue()));
                                }
                                break;
                            case "TYPE":
                                MtExtendVO5 vo10 = new MtExtendVO5();
                                vo10.setAttrName("TYPE");
                                vo10.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo10);
                                transferVO2.setIncomingType(mtExtendAttrVO.getAttrValue());
                                break;
                            case "LOTNO":
                                MtExtendVO5 vo11 = new MtExtendVO5();
                                vo11.setAttrName("LOTNO");
                                vo11.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo11);
                                break;
                            case "REMARK":
                                MtExtendVO5 vo12 = new MtExtendVO5();
                                vo12.setAttrName("REMARK");
                                vo12.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo12);
                                transferVO2.setRemark(mtExtendAttrVO.getAttrValue());
                                break;
                            case "WORK_ORDER_ID":
                                MtExtendVO5 vo13 = new MtExtendVO5();
                                vo13.setAttrName("WORK_ORDER_ID");
                                vo13.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo13);
                                break;
                            case "WORKING_LOT":
                                MtExtendVO5 vo14 = new MtExtendVO5();
                                vo14.setAttrName("WORKING_LOT");
                                vo14.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo14);
                                break;
                            case "MF_FLAG":
                                MtExtendVO5 vo15 = new MtExtendVO5();
                                vo15.setAttrName("MF_FLAG");
                                vo15.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo15);
                                break;
                            case "FREEZE_LOCATOR":
                                MtExtendVO5 vo16 = new MtExtendVO5();
                                vo16.setAttrName("FREEZE_LOCATOR");
                                vo16.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo16);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            vo3.setAttr(extendVO5List);
            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, vo3);

            //??????????????????id
            hmeIncomingRecord = hmeChipTransferMapper.queryHmeIncomingRecord(tenantId, transferVO2.getTransCosRecordId());
        } else {
            //??????materialLotLimitAttrQuery-???????????????????????????
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(targetMaterialLot.getMaterialLotId());

            //COS??????
            mtMaterialLotAttrVO2.setAttrName("COS_TYPE");
            List<MtExtendAttrVO> cosTypeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(cosTypeList)) {
                transferVO2.setCosType(cosTypeList.get(0).getAttrValue());
            }

            //WAFER_NUM
            mtMaterialLotAttrVO2.setAttrName("WAFER_NUM");
            List<MtExtendAttrVO> waferNumList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(waferNumList)) {
                transferVO2.setWaferNum(waferNumList.get(0).getAttrValue());
            }

            //????????????
            mtMaterialLotAttrVO2.setAttrName("CONTAINER_TYPE");
            List<MtExtendAttrVO> containerTypeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(containerTypeList)) {
                transferVO2.setContainerType(containerTypeList.get(0).getAttrValue());
            }

            //??????
            mtMaterialLotAttrVO2.setAttrName("LOCATION_ROW");
            List<MtExtendAttrVO> rowList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(rowList) && StringUtils.isNotBlank(rowList.get(0).getAttrValue())) {
                transferVO2.setLocationRow(Long.valueOf(rowList.get(0).getAttrValue()));
            }

            //??????
            mtMaterialLotAttrVO2.setAttrName("LOCATION_COLUMN");
            List<MtExtendAttrVO> columnList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(columnList) && StringUtils.isNotBlank(columnList.get(0).getAttrValue())) {
                transferVO2.setLocationColumn(Long.valueOf(columnList.get(0).getAttrValue()));
            }

            //?????????
            mtMaterialLotAttrVO2.setAttrName("CHIP_NUM");
            List<MtExtendAttrVO> chipNumList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(chipNumList) && StringUtils.isNotBlank(chipNumList.get(0).getAttrValue())) {
                transferVO2.setChipNum(Long.valueOf(chipNumList.get(0).getAttrValue()));
            }

            //????????????
            mtMaterialLotAttrVO2.setAttrName("AVG_WAVE_LENGTH");
            List<MtExtendAttrVO> avgWaveLengthList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(avgWaveLengthList) && StringUtils.isNotBlank(avgWaveLengthList.get(0).getAttrValue())) {
                transferVO2.setAverageWavelength(new BigDecimal(avgWaveLengthList.get(0).getAttrValue()));
            }

            //??????
            mtMaterialLotAttrVO2.setAttrName("REMARK");
            List<MtExtendAttrVO> remarkList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(remarkList) && StringUtils.isNotBlank(remarkList.get(0).getAttrValue())) {
                transferVO2.setRemark(remarkList.get(0).getAttrValue());
            }

            //??????
            mtMaterialLotAttrVO2.setAttrName("TYPE");
            List<MtExtendAttrVO> typeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(typeList) && StringUtils.isNotBlank(typeList.get(0).getAttrValue())) {
                transferVO2.setIncomingType(typeList.get(0).getAttrValue());
            }

            //????????????id
            mtMaterialLotAttrVO2.setAttrName("COS_RECORD");
            List<MtExtendAttrVO> materialLotAttrList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            String cosRecord = "";
            if (CollectionUtils.isNotEmpty(materialLotAttrList)) {
                cosRecord = materialLotAttrList.get(0).getAttrValue();
            }
            //??????????????????id
            hmeIncomingRecord = hmeChipTransferMapper.queryHmeIncomingRecord(tenantId, cosRecord);
        }

        if (Objects.nonNull(hmeIncomingRecord)) {
            transferVO2.setCosRecordId(hmeIncomingRecord.getOperationRecordId());
        }

        //????????????????????????
        if (StringUtils.equals(targetMaterialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            //??????????????????WAFER?????????
            List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
            this.verifyWaferAndWorkOder(tenantId, targetMaterialLot, transLot.getMaterialLotId(), itemGroupList);
            //????????????
            if (!StringUtils.equals(transLot.getMaterialId(), targetMaterialLot.getMaterialId())) {
                throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_016", "HME"));
            }
            //???????????????
            if (!StringUtils.equals(transLot.getLot(), targetMaterialLot.getLot())) {
                throw new MtException("HME_COS_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_005", "HME"));
            }
            // ?????????????????????????????????
            if (!StringUtils.equals(transferVO2.getContainerType(), transferVO.getTransContainerType())) {
                throw new MtException("HME_CHIP_TRANSFER_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_008", "HME"));
            }
            // ???????????????COS????????????
            if (!StringUtils.equals(transferVO2.getCosType(), transferVO.getTransCosType())) {
                throw new MtException("HME_CHIP_TRANSFER_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_006", "HME"));
            }
            // ????????????
            if (!StringUtils.equals(HmeConstants.ConstantValue.OK, targetMaterialLot.getQualityStatus())) {
                throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_003", "HME", targetMaterialLot.getMaterialLotCode()));
            }
            // ?????? ???Y?????????
            if (HmeConstants.ConstantValue.YES.equals(targetMaterialLot.getStocktakeFlag())) {
                throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_003", "HME", targetMaterialLot.getMaterialLotCode()));
            }
        }
        //????????????????????????????????? ??????????????????
        this.verifyTarget(tenantId, attrVOList, transferVO2);

        //??????????????????hme_eo_job_sn???
        // ??????????????????????????? ????????????????????????????????????
        String workOrderId = null;
        List<MtExtendAttrVO> workOrderIdAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(transferVO2.getMaterialLotId());
            setAttrName("WORK_ORDER_ID");
        }});
        if (CollectionUtils.isNotEmpty(workOrderIdAttr) && StringUtils.isNotBlank(workOrderIdAttr.get(0).getAttrValue())) {
            workOrderId = workOrderIdAttr.get(0).getAttrValue();
        }
        transferVO2.setWorkOrderId(workOrderId);
        transferVO2.setWkcShiftId(transferVO.getWkcShiftId());
        transferVO2.setWorkcellId(transferVO.getWorkcellId());
        transferVO2.setOperationId(transferVO.getOperationId());
        HmeEoJobSn hmeEoJobSn = this.createEoJobSnRecord(tenantId, hmeIncomingRecord, transferVO2, "FREEZE_TRANSFER_OUT");
        hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, Collections.singletonList(hmeEoJobSn.getJobId()), transferVO.getWorkcellId());

        transferVO2.setEoJobSnId(hmeEoJobSn.getJobId());
        //????????????????????????
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(transferVO2.getMaterialLotId());
        List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        List<HmeMaterialLotLoad> collect = select.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());
        for (HmeMaterialLotLoad lotLoad : collect) {
            HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
            hmeWoJobSnReturnDTO5.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
            hmeWoJobSnReturnDTO5.setLoadSequence(lotLoad.getLoadSequence());
            hmeWoJobSnReturnDTO5.setHotSinkCode(lotLoad.getHotSinkCode());
            hmeWoJobSnReturnDTO5.setFreezeFlag(lotLoad.getAttribute14());
            hmeWoJobSnReturnDTO5.setFreezeFlagMeaning(StringUtils.equals(lotLoad.getAttribute14(), HmeConstants.ConstantValue.YES) ? "???" : "???");
            HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
            hmeMaterialLotNcLoad.setLoadSequence(lotLoad.getLoadSequence());
            List<HmeMaterialLotNcLoad> select1 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
            if (CollectionUtils.isNotEmpty(select1)) {
                hmeWoJobSnReturnDTO5.setDocList(select1);
            }
            hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
        }
        transferVO2.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s);
        return transferVO2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleChipTransferComplete(Long tenantId, HmeChipTransferVO3 vo3) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        //????????????
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(vo3.getEoJobSnId());
        if (hmeEoJobSn == null) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "eoJobSnId"));
        }
        hmeEoJobSn.setSiteOutDate(new Date());
        hmeEoJobSn.setSiteOutBy(userId);
        hmeEoJobSnRepository.updateByPrimaryKeySelective(hmeEoJobSn);

        //???????????? ???????????? ????????????
        if (StringUtils.equals(hmeEoJobSn.getJobType(), "FREEZE_TRANSFER_IN")) {
            HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
            hmeMaterialLotLoad.setMaterialLotId(hmeEoJobSn.getMaterialLotId());
            List<HmeMaterialLotLoad> lotLoadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
            if (CollectionUtils.isEmpty(lotLoadList)) {
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());
                if (mtMaterialLot != null) {
                    // 2021-01-11 add by sanfeng.zhang for ban.zhenyong ??????????????????
                    String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                        setEventTypeCode("FREEZE_TRANSFER_FINISH");
                    }});
                    //??????API????????????
                    mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                        setEventId(eventId);
                        setEnableFlag(HmeConstants.ConstantValue.NO);
                        setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    }}, "N");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleAllTransfer(Long tenantId, HmeChipTransferVO2 vo2) {
        if (StringUtils.isBlank(vo2.getTransMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        Condition condition = new Condition(MtContainerType.class);
        condition.and().andEqualTo("containerTypeCode", vo2.getContainerType())
                .andEqualTo("enableFlag", "Y");
        List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(condition);
        if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
            HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
            hmeContainerCapacity.setContainerTypeId(mtContainerTypeList.get(0).getContainerTypeId());
            hmeContainerCapacity.setOperationId(vo2.getOperationId());
            hmeContainerCapacity.setCosType(vo2.getCosType());
            List<HmeContainerCapacity> capacity = hmeContainerCapacityRepository.select(hmeContainerCapacity);
            if (CollectionUtils.isNotEmpty(capacity)) {
                if (!vo2.getTotalChipNum().equals(capacity.get(0).getCapacity())) {
                    throw new MtException("HME_CHIP_TRANSFER_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_009", "HME"));
                }
            }
        }
        //??????????????????
        String requestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_TRANSFER_FREEZE");
        //????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("FREEZE_TRANSFER_OUT");
        eventCreateVO.setEventRequestId(requestId);
        String transEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //??????????????????
        MtEventCreateVO createVO = new MtEventCreateVO();
        createVO.setEventTypeCode("FREEZE_TRANSFER_IN");
        createVO.setEventRequestId(requestId);
        String eventId = mtEventRepository.eventCreate(tenantId, createVO);
        //??????????????????
        MtMaterialLot transMtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(vo2.getTransMaterialLotId());
        // ???????????????????????????????????????Y ?????? ?????????
        if (!YES.equals(transMtMaterialLot.getFreezeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_020", "HME", transMtMaterialLot.getMaterialLotCode()));
        }
        //??????api???materialLotUpdate??? ??????????????????
        MtMaterialLotVO2 materialLotVO2 = new MtMaterialLotVO2();
        materialLotVO2.setEventId(transEventId);
        materialLotVO2.setMaterialLotId(vo2.getTransMaterialLotId());
        materialLotVO2.setPrimaryUomQty(BigDecimal.valueOf(transMtMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(vo2.getTotalChipNum())).doubleValue());
        mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO2, "N");

        //??????????????????????????????????????????????????????
        List<String> transMaterialLotIdList = new ArrayList<>();
        transMaterialLotIdList.add(vo2.getTransMaterialLotId());
        List<HmeCosPatchPdaVO9> labCodeAndRemarkAttr = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, transMaterialLotIdList);
        String labCode = null;
        String labReamrk = null;
        if(CollectionUtils.isNotEmpty(labCodeAndRemarkAttr)){
            labCode = labCodeAndRemarkAttr.get(0).getLabCode();
            labReamrk = labCodeAndRemarkAttr.get(0).getRemark();
        }
        //2021-11-01 09:30 add by chaonan.hu for yiming.zhang ???????????????????????????0,???????????????????????????????????????????????????
        if(materialLotVO2.getPrimaryUomQty() == 0){
            MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
            mtMaterialLotAttrVO3.setMaterialLotId(vo2.getTransMaterialLotId());
            //????????????
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("EMPTY_LAB_CODE");
            String emptyLabCodeEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            mtMaterialLotAttrVO3.setEventId(emptyLabCodeEventId);
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 labCodeAttr = new MtExtendVO5();
            labCodeAttr.setAttrName("LAB_CODE");
            labCodeAttr.setAttrValue("");
            attrList.add(labCodeAttr);
            MtExtendVO5 labRemarkAttr = new MtExtendVO5();
            labRemarkAttr.setAttrName("LAB_REMARK");
            labRemarkAttr.setAttrValue("");
            attrList.add(labRemarkAttr);
            mtMaterialLotAttrVO3.setAttr(attrList);
            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
        }

        //????????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(vo2.getMaterialLotId());
        HmeMaterialLotLoad materialLotLoad = hmeMaterialLotLoadRepository.selectByPrimaryKey(vo2.getMaterialLotLoadId());
        //2021-11-08 15:23 add by chaonan.hu for yiming.zhang
        //????????????????????????0???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if(mtMaterialLot.getPrimaryUomQty() != 0){
            MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(vo2.getOperationId());
            List<LovValueDTO> cosLabCodeLovList = lovAdapter.queryLovValue("HME.COS_LAB_CODE", tenantId);
            if(CollectionUtils.isNotEmpty(cosLabCodeLovList)){
                List<String> cosLabCodeLovValueList = cosLabCodeLovList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if(cosLabCodeLovValueList.contains(mtOperation.getOperationName())){
                    if(StringUtils.isNotBlank(materialLotLoad.getAttribute19())){
                        if(!materialLotLoad.getAttribute19().equals(vo2.getChipLabCode())){
                            throw new MtException("HME_LAB_CODE_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_LAB_CODE_010", "HME"));
                        }
                    }else {
                        if(StringUtils.isNotBlank(vo2.getChipLabCode())){
                            throw new MtException("HME_LAB_CODE_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_LAB_CODE_010", "HME"));
                        }
                    }
                    if(StringUtils.isNotBlank(materialLotLoad.getAttribute20())){
                        if(!materialLotLoad.getAttribute20().equals(vo2.getChipLabRemark())){
                            throw new MtException("HME_LAB_CODE_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_LAB_CODE_010", "HME"));
                        }
                    }else {
                        if(StringUtils.isNotBlank(vo2.getChipLabRemark())){
                            throw new MtException("HME_LAB_CODE_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_LAB_CODE_010", "HME"));
                        }
                    }
                }
            }
        }

        //????????????
        if (!StringUtils.equals(mtMaterialLot.getLot(), transMtMaterialLot.getLot())) {
            throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_016", "HME"));
        }
        //????????????
        if (!StringUtils.equals(mtMaterialLot.getMaterialId(), transMtMaterialLot.getMaterialId())) {
            throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_016", "HME"));
        }
        //???????????? ???????????????????????????????????????????????????
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_material_lot_attr");
            setAttrName("FREEZE_LOCATOR");
            setKeyId(transMtMaterialLot.getMaterialLotId());
        }});
        String freezeLocator = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
        boolean locatorFlag = StringCommonUtils.contains(mtMaterialLot.getLocatorId(), transMtMaterialLot.getLocatorId(), freezeLocator);
        if (!locatorFlag) {
            throw new MtException("HME_COS_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_019", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //?????????????????????????????????????????????
        // ?????????????????? ?????????????????????????????????????????????????????????
        if (YES.equals(mtMaterialLot.getEnableFlag())) {
            String codeFreezeFlag = StringUtils.isNotBlank(mtMaterialLot.getFreezeFlag()) ? mtMaterialLot.getFreezeFlag() : NO;
            String loadFreezeFlag = StringUtils.isNotBlank(materialLotLoad.getAttribute14()) ? materialLotLoad.getAttribute14() : NO;
            if (!StringUtils.equals(codeFreezeFlag, loadFreezeFlag)) {
                throw new MtException("HME_COS_BARCODE_RETEST_025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_025", "HME", mtMaterialLot.getMaterialLotCode()));
            }
        }
        // add liukejin 2020???12???18???15:59:18
        String routerStepId = checkMaterialLotRouterStepId(tenantId, vo2.getTransMaterialLotId(), vo2.getMaterialLotId());
        if (StringUtils.equals(mtMaterialLot.getEnableFlag(), HmeConstants.ConstantValue.NO)) {
            //??????api???materialLotUpdate????????????????????????
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setSiteId(transMtMaterialLot.getSiteId());
            mtMaterialLotVO2.setEnableFlag("Y");
            mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotVO2.setQualityStatus(transMtMaterialLot.getQualityStatus());
            mtMaterialLotVO2.setMaterialId(transMtMaterialLot.getMaterialId());
            mtMaterialLotVO2.setPrimaryUomId(transMtMaterialLot.getPrimaryUomId());
            mtMaterialLotVO2.setTrxPrimaryUomQty(Double.valueOf(vo2.getTotalChipNum()));
            mtMaterialLotVO2.setLocatorId(transMtMaterialLot.getLocatorId());
            mtMaterialLotVO2.setLoadTime(CommonUtils.currentTimeGet());
            mtMaterialLotVO2.setCreateReason("INITIALIZE");
            mtMaterialLotVO2.setInSiteTime(new Date());
            mtMaterialLotVO2.setEoId(transMtMaterialLot.getEoId());
            //V20210101 modify by penglin.sui for zhenyong.ban ??????????????????????????????????????????
            mtMaterialLotVO2.setSupplierId(transMtMaterialLot.getSupplierId());
            mtMaterialLotVO2.setSupplierSiteId(transMtMaterialLot.getSupplierSiteId());
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

            //????????????????????????????????? ????????????
            List<MtExtendSettings> attrList = new ArrayList<>();
            //V20210101 modify by penglin.sui for zhenyong.ban ????????????????????????????????????????????????(SUPPLIER_LOT)
            String[] arrList = new String[]{"MF_FLAG", "REWORK_FLAG", "PERFORMANCE_LEVEL", "COS_RECORD", "WAFER_NUM", "COS_TYPE", "SUPPLIER_LOT"};
            for (String arr : arrList) {
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName(arr);
                attrList.add(mtExtendSettings);
            }
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", transMtMaterialLot.getMaterialLotId(), attrList);
            // ??????????????????
            MtExtendVO10 materialLotExtend = new MtExtendVO10();
            materialLotExtend.setKeyId(mtMaterialLot.getMaterialLotId());
            materialLotExtend.setEventId(eventId);
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            mtExtendAttrVOList.forEach(e -> {
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName(e.getAttrName());
                mtExtendVO5.setAttrValue(e.getAttrValue());
                mtExtendVO5List.add(mtExtendVO5);
            });
            //????????????
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("CONTAINER_TYPE");
            mtExtendVO5.setAttrValue(vo2.getContainerType());
            mtExtendVO5List.add(mtExtendVO5);
            //???????????????
            MtExtendVO5 rowObj = new MtExtendVO5();
            rowObj.setAttrName("LOCATION_ROW");
            rowObj.setAttrValue(String.valueOf(vo2.getLocationRow()));
            mtExtendVO5List.add(rowObj);
            //???????????????
            MtExtendVO5 columnObj = new MtExtendVO5();
            columnObj.setAttrName("LOCATION_COLUMN");
            columnObj.setAttrValue(String.valueOf(vo2.getLocationColumn()));
            mtExtendVO5List.add(columnObj);
            //?????????
            MtExtendVO5 chipObj = new MtExtendVO5();
            chipObj.setAttrName("CHIP_NUM");
            chipObj.setAttrValue(String.valueOf(vo2.getTotalChipNum()));
            mtExtendVO5List.add(chipObj);
            //????????????
            MtExtendVO5 originalAttr = new MtExtendVO5();
            originalAttr.setAttrName("ORIGINAL_ID");
            originalAttr.setAttrValue(transMtMaterialLot.getMaterialLotId());
            mtExtendVO5List.add(originalAttr);
            // add liukejin 2020???12???18???15:59:18
            //??????ID
            MtExtendVO5 routerStep = new MtExtendVO5();
            routerStep.setAttrName("CURRENT_ROUTER_STEP");
            routerStep.setAttrValue(routerStepId);
            mtExtendVO5List.add(routerStep);
            materialLotExtend.setAttrs(mtExtendVO5List);
            //????????????
            if(StringUtils.isNotBlank(labCode)){
                MtExtendVO5 labCodeAttr = new MtExtendVO5();
                labCodeAttr.setAttrName("LAB_CODE");
                labCodeAttr.setAttrValue(labCode);
                mtExtendVO5List.add(labCodeAttr);
            }

            //??????????????????
            if(StringUtils.isNotBlank(labReamrk)){
                MtExtendVO5 labReamrkAttr = new MtExtendVO5();
                labReamrkAttr.setAttrName("LAB_REMARK");
                labReamrkAttr.setAttrValue(labReamrk);
                mtExtendVO5List.add(labReamrkAttr);
            }
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);
        } else if (StringUtils.equals(mtMaterialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            //??????WAFER?????????
            List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
            this.verifyWaferAndWorkOder(tenantId, mtMaterialLot, transMtMaterialLot.getMaterialLotId(), itemGroupList);
            //?????????????????????
            MtExtendVO1 mtExtendVO = new MtExtendVO1();
            mtExtendVO.setTableName("mt_material_lot_attr");
            List<String> materialLotIdList = new ArrayList<>();
            materialLotIdList.add(mtMaterialLot.getMaterialLotId());
            materialLotIdList.add(transMtMaterialLot.getMaterialLotId());
            mtExtendVO.setKeyIdList(materialLotIdList);

            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("MF_FLAG");
            mtExtendVO5s.add(mtExtendVO5);
            mtExtendVO.setAttrs(mtExtendVO5s);
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(attrVO1List)) {
                Optional<MtExtendAttrVO1> first = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), mtMaterialLot.getMaterialLotId())).findFirst();
                String mfFlag = first.isPresent() ? (StringUtils.equals(first.get().getAttrValue(), HmeConstants.ConstantValue.YES) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO) : HmeConstants.ConstantValue.NO;

                Optional<MtExtendAttrVO1> two = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), transMtMaterialLot.getMaterialLotId())).findFirst();

                String transMfFlag = two.isPresent() ? (StringUtils.equals(two.get().getAttrValue(), HmeConstants.ConstantValue.YES) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO) : HmeConstants.ConstantValue.NO;

                if (!StringUtils.equals(mfFlag, transMfFlag)) {
                    throw new MtException("HME_COS_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_017", "HME"));
                }
            }
            // ?????????????????????
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                {
                    setEventId(eventId);
                    setMaterialLotId(vo2.getMaterialLotId());
                    setTrxPrimaryUomQty(BigDecimal.valueOf(vo2.getTotalChipNum()).doubleValue());
                }
            }, "N");

            //????????????????????????
            MtExtendVO10 materialLotExtend = new MtExtendVO10();
            materialLotExtend.setKeyId(mtMaterialLot.getMaterialLotId());
            materialLotExtend.setEventId(eventId);
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 originalAttr = new MtExtendVO5();
            originalAttr.setAttrName("ORIGINAL_ID");
            originalAttr.setAttrValue(transMtMaterialLot.getMaterialLotId());
            mtExtendVO5List.add(originalAttr);
            //????????????
            if(StringUtils.isNotBlank(labCode)){
                MtExtendVO5 labCodeAttr = new MtExtendVO5();
                labCodeAttr.setAttrName("LAB_CODE");
                labCodeAttr.setAttrValue(labCode);
                mtExtendVO5List.add(labCodeAttr);
            }

            //??????????????????
            if(StringUtils.isNotBlank(labReamrk)){
                MtExtendVO5 labReamrkAttr = new MtExtendVO5();
                labReamrkAttr.setAttrName("LAB_REMARK");
                labReamrkAttr.setAttrValue(labReamrk);
                mtExtendVO5List.add(labReamrkAttr);
            }
            materialLotExtend.setAttrs(mtExtendVO5List);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);
        }

        if (materialLotLoad != null) {
            materialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            materialLotLoad.setSourceLoadRow(materialLotLoad.getLoadRow());
            materialLotLoad.setSourceLoadColumn(materialLotLoad.getLoadColumn());
            materialLotLoad.setLoadRow(vo2.getLoadRow());
            materialLotLoad.setLoadColumn(vo2.getLoadColumn());
            materialLotLoad.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
            if(StringUtils.isNotBlank(labCode)){
                materialLotLoad.setAttribute19(labCode);
            }
            if(StringUtils.isNotBlank(labReamrk)){
                materialLotLoad.setAttribute20(labReamrk);
            }
            hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(materialLotLoad);

            //2021-02-04 add by chaonan.hu for zhenyong.ban ??????COS????????????
            HmeLoadJob hmeLoadJob = new HmeLoadJob();
            hmeLoadJob.setTenantId(tenantId);
            hmeLoadJob.setSiteId(vo2.getSiteId());
            hmeLoadJob.setLoadSequence(materialLotLoad.getLoadSequence());
            hmeLoadJob.setLoadJobType("FREEZE_TRANSFER");
            hmeLoadJob.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeLoadJob.setMaterialId(transMtMaterialLot.getMaterialId());
            hmeLoadJob.setLoadRow(materialLotLoad.getLoadRow());
            hmeLoadJob.setLoadColumn(materialLotLoad.getLoadColumn());
            hmeLoadJob.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
            hmeLoadJob.setSourceLoadRow(materialLotLoad.getSourceLoadRow());
            hmeLoadJob.setSourceLoadColumn(materialLotLoad.getSourceLoadColumn());
            hmeLoadJob.setCosNum(materialLotLoad.getCosNum());
            hmeLoadJob.setOperationId(vo2.getOperationId());
            hmeLoadJob.setWorkcellId(vo2.getWorkcellId());
            //?????????????????????????????????
            List<MtExtendSettings> attrList = new ArrayList<>();
            String[] arrList = new String[]{"WAFER_NUM", "COS_TYPE", "WORK_ORDER_ID"};
            for (String arr : arrList) {
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName(arr);
                attrList.add(mtExtendSettings);
            }
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", transMtMaterialLot.getMaterialLotId(), attrList);
            mtExtendAttrVOList.forEach(e -> {
                if ("WORK_ORDER_ID".equals(e.getAttrName())) {
                    hmeLoadJob.setWorkOrderId(e.getAttrValue());
                } else if ("WAFER_NUM".equals(e.getAttrName())) {
                    hmeLoadJob.setWaferNum(e.getAttrValue());
                } else if ("COS_TYPE".equals(e.getAttrName())) {
                    hmeLoadJob.setCosType(e.getAttrValue());
                }
            });
            hmeLoadJob.setStatus("0");
            hmeLoadJobRepository.insertSelective(hmeLoadJob);
            //??????
            List<String> ncCodeList = hmeLoadJobMapper.ncCodeQuery(tenantId, materialLotLoad.getLoadSequence());
            if (CollectionUtils.isNotEmpty(ncCodeList)) {
                for (String ncCode : ncCodeList) {
                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                    hmeLoadJobObject.setTenantId(tenantId);
                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                    hmeLoadJobObject.setObjectType("NC");
                    hmeLoadJobObject.setObjectId(ncCode);
                    hmeLoadJobObjectRepository.insertSelective(hmeLoadJobObject);
                }
            }
            //????????????
            HmeCosTransferHis transferHis = new HmeCosTransferHis();
            transferHis.setTenantId(tenantId);
            transferHis.setSiteId(mtMaterialLot.getSiteId());
            transferHis.setMaterialId(mtMaterialLot.getMaterialId());
            transferHis.setLocatorId(mtMaterialLot.getLocatorId());
            transferHis.setLoadSequence(materialLotLoad.getLoadSequence());
            transferHis.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            transferHis.setLoadRow(materialLotLoad.getLoadRow());
            transferHis.setLoadColumn(materialLotLoad.getLoadColumn());
            transferHis.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
            transferHis.setSourceLoadRow(materialLotLoad.getSourceLoadRow());
            transferHis.setSourceLoadColumn(materialLotLoad.getSourceLoadColumn());
            transferHis.setCosNum(vo2.getTotalChipNum());
            transferHis.setLot(mtMaterialLot.getLot());
            hmeCosTransferHisRepository.insertSelective(transferHis);
        }
        // ???????????????????????????Y ???????????????????????? ??????????????????????????????
        List<MtExtendAttrVO> mfFlagAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_material_lot_attr");
            setAttrName("MF_FLAG");
            setKeyId(transMtMaterialLot.getMaterialLotId());
        }});
        String mfFlagAttr = CollectionUtils.isNotEmpty(mfFlagAttrList) ? mfFlagAttrList.get(0).getAttrValue() : "";
        if (!YES.equals(materialLotLoad.getAttribute14())) {
            HmeChipTransferVO2 hmeChipTransferVO2 = new HmeChipTransferVO2();
            hmeChipTransferVO2.setTotalChipNum(vo2.getTotalChipNum());
            hmeChipTransferVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            this.unfreezeTargetCode(tenantId, Collections.singletonList(hmeChipTransferVO2), transMtMaterialLot, mfFlagAttr);
        }
        // ????????????????????????????????????ATTRIBUTE14?????????Y ??????????????????????????????
        Integer freezeNum = hmeCosFreezeTransferMapper.queryFreezeMaterialLotLoad(tenantId, transMtMaterialLot.getMaterialLotId());
        if (Integer.valueOf(ZERO).compareTo(freezeNum) == 0) {
            this.unfreezeSourceCode(tenantId, transMtMaterialLot, BigDecimal.valueOf(vo2.getTotalChipNum()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoOkAssignTransfer(Long tenantId, HmeChipTransferVO4 vo4) {
        if (StringUtils.isBlank(vo4.getTransMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(vo4.getTransMaterialLotId());
        List<HmeMaterialLotLoad> queryMaterialLotLoadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        // ???????????????????????????
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = queryMaterialLotLoadList.stream().filter(dto -> !StringUtils.equals(YES, dto.getAttribute14())).collect(Collectors.toList());
        //????????????
        List<HmeMaterialLotLoad> lotLoadList = this.autoTransferRules(hmeMaterialLotLoadList, vo4.getLoadingRules());

        List<HmeChipTransferVO2> transferVO2List = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lotLoadList)) {
            //?????????????????????
            if (CollectionUtils.isNotEmpty(vo4.getMaterialLotCodeList())) {
                List<MtMaterialLot> materialLotList = hmeChipTransferRepository.materialLotPropertyGet(tenantId, vo4.getMaterialLotCodeList());
                if (CollectionUtils.isNotEmpty(materialLotList)) {

                    if (CollectionUtils.isNotEmpty(lotLoadList)) {
                        //??????????????????
                        String loadRule = hmeChipTransferRepository.queryLoadRule(tenantId, materialLotList.get(0).getMaterialLotId(), vo4.getOperationId());
                        vo4.setLoadRule(loadRule);
                        //?????????????????????
                        List<HmeChipTransferVO> transferVOList = this.materialCodeTargetQuery(tenantId, StringUtils.join(vo4.getMaterialLotCodeList(), ","), vo4.getWorkcellId());
                        //????????????
                        List<String> materialLotIdList = transferVOList.stream().map(HmeChipTransferVO::getMaterialLotId).collect(Collectors.toList());
                        List<HmeMaterialLotLoad> loadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                                        .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
                        Map<String, List<HmeMaterialLotLoad>> loadListMap = loadList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));

                        Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap = new HashMap<>();

                        for (String mid : materialLotIdList) {
                            List<HmeMaterialLotLoad> lotLoads = loadListMap.get(mid);
                            Map<String, List<HmeMaterialLotLoad>> resultList = new HashMap<>();
                            //?????????????????????????????? ?????????????????????????????????
                            if (CollectionUtils.isNotEmpty(lotLoads) && !StringUtils.equals(vo4.getTransMaterialLotId(), mid)) {
                                resultList = lotLoads.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.groupingBy(dto ->
                                        dto.getLoadRow() + "_" + dto.getLoadColumn()));
                            }
                            recordMap.put(mid, resultList);
                        }

                        for (HmeMaterialLotLoad lotLoad : lotLoadList) {
                            Boolean flag = false;
                            for (HmeChipTransferVO transferVO : transferVOList) {
                                if (flag) {
                                    break;
                                }
                                Map<String, List<HmeMaterialLotLoad>> stringListMap = recordMap.get(transferVO.getMaterialLotId());
                                //????????????
                                flag = hmeChipTransferRepository.autoAssignTransferRules(transferVO.getLocationRow(), transferVO.getLocationColumn(), stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4, tenantId);
                            }
                        }
                    }
                }
            }
        }
        //??????????????????
        this.batchTransLoadInfo(tenantId, vo4.getSiteId(), vo4.getWorkcellId(), vo4.getOperationId(), transferVO2List);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoFreezeAssignTransfer(Long tenantId, HmeChipTransferVO4 vo4) {
        if (StringUtils.isBlank(vo4.getTransMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(vo4.getTransMaterialLotId());
        List<HmeMaterialLotLoad> queryMaterialLotLoadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        // ???????????????????????????
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = queryMaterialLotLoadList.stream().filter(dto -> StringUtils.equals(YES, dto.getAttribute14())).collect(Collectors.toList());
        //????????????
        List<HmeMaterialLotLoad> lotLoadList = this.autoTransferRules(hmeMaterialLotLoadList, vo4.getLoadingRules());

        List<HmeChipTransferVO2> transferVO2List = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lotLoadList)) {
            //?????????????????????
            if (CollectionUtils.isNotEmpty(vo4.getMaterialLotCodeList())) {
                List<MtMaterialLot> materialLotList = hmeChipTransferRepository.materialLotPropertyGet(tenantId, vo4.getMaterialLotCodeList());
                if (CollectionUtils.isNotEmpty(materialLotList)) {

                    if (CollectionUtils.isNotEmpty(lotLoadList)) {
                        //??????????????????
                        String loadRule = hmeChipTransferRepository.queryLoadRule(tenantId, materialLotList.get(0).getMaterialLotId(), vo4.getOperationId());
                        vo4.setLoadRule(loadRule);
                        //?????????????????????
                        List<HmeChipTransferVO> transferVOList = this.materialCodeTargetQuery(tenantId, StringUtils.join(vo4.getMaterialLotCodeList(), ","), vo4.getWorkcellId());
                        //????????????
                        List<String> materialLotIdList = transferVOList.stream().map(HmeChipTransferVO::getMaterialLotId).collect(Collectors.toList());
                        List<HmeMaterialLotLoad> loadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                                        .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
                        Map<String, List<HmeMaterialLotLoad>> loadListMap = loadList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));

                        Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap = new HashMap<>();

                        for (String mid : materialLotIdList) {
                            List<HmeMaterialLotLoad> lotLoads = loadListMap.get(mid);
                            Map<String, List<HmeMaterialLotLoad>> resultList = new HashMap<>();
                            //?????????????????????????????? ?????????????????????????????????
                            if (CollectionUtils.isNotEmpty(lotLoads) && !StringUtils.equals(vo4.getTransMaterialLotId(), mid)) {
                                resultList = lotLoads.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.groupingBy(dto ->
                                        dto.getLoadRow() + "_" + dto.getLoadColumn()));
                            }
                            recordMap.put(mid, resultList);
                        }

                        for (HmeMaterialLotLoad lotLoad : lotLoadList) {
                            Boolean flag = false;
                            for (HmeChipTransferVO transferVO : transferVOList) {
                                if (flag) {
                                    break;
                                }
                                Map<String, List<HmeMaterialLotLoad>> stringListMap = recordMap.get(transferVO.getMaterialLotId());
                                //????????????
                                flag = hmeChipTransferRepository.autoAssignTransferRules(transferVO.getLocationRow(), transferVO.getLocationColumn(), stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4, tenantId);
                            }
                        }
                    }
                }
            }
        }
        //??????????????????
        this.batchTransLoadInfo(tenantId, vo4.getSiteId(), vo4.getWorkcellId(), vo4.getOperationId(), transferVO2List);
    }

    @Override
    public HmeChipTransferVO6 siteInMaterialCodeQuery(Long tenantId, String workcellId, String operationId) {
        HmeChipTransferVO6 transferVO6 = new HmeChipTransferVO6();
        //???????????????????????????????????????
        Set<String> criteriaJobTypes = new HashSet<>();
        criteriaJobTypes.add("FREEZE_TRANSFER_IN");
        criteriaJobTypes.add("FREEZE_TRANSFER_OUT");

        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeEoJobSn.FIELD_JOB_TYPE, criteriaJobTypes)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, workcellId)
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, operationId)
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());
        List<HmeChipTransferVO> transferVOList = new ArrayList<>();
        for (HmeEoJobSn hmeEoJobSn : hmeEoJobSnList) {
            HmeChipTransferVO transferVO = new HmeChipTransferVO();
            transferVO.setOperationId(hmeEoJobSn.getOperationId());
            transferVO.setEoJobSnId(hmeEoJobSn.getJobId());
            if (StringUtils.isNotBlank(hmeEoJobSn.getAttribute1())) {
                transferVO.setSiteSort(Integer.valueOf(hmeEoJobSn.getAttribute1()));
            }
            transferVO.setTransCosRecordId(hmeEoJobSn.getSourceJobId());

            //??????materialLotPropertyGet-?????????????????????
            MtMaterialLot materialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());

            if (materialLot == null) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", materialLot.getMaterialLotCode()));
            }

            transferVO.setMaterialLotId(materialLot.getMaterialLotId());
            transferVO.setMaterialLotCode(materialLot.getMaterialLotCode());
            transferVO.setReleaseLot(materialLot.getLot());
            this.queryChipMaterialCodeInfo(tenantId, transferVO, materialLot, hmeEoJobSn);

            if (StringUtils.equals(hmeEoJobSn.getJobType(), "FREEZE_TRANSFER_IN")) {
                //????????????
                if (StringUtils.isNotBlank(transferVO.getTransContainerType()) && StringUtils.isNotBlank(transferVO.getCosType())) {
                    Condition condition = new Condition(MtContainerType.class);
                    condition.and().andEqualTo("containerTypeCode", transferVO.getTransContainerType())
                            .andEqualTo("enableFlag", "Y");
                    List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(condition);
                    if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
                        HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
                        hmeContainerCapacity.setContainerTypeId(mtContainerTypeList.get(0).getContainerTypeId());
                        hmeContainerCapacity.setOperationId(operationId);
                        hmeContainerCapacity.setCosType(transferVO.getCosType());
                        List<HmeContainerCapacity> capacity = hmeContainerCapacityRepository.select(hmeContainerCapacity);
                        if (CollectionUtils.isNotEmpty(capacity)) {
                            transferVO.setLoadRule(capacity.get(0).getAttribute1());
                            //????????????
                            List<LovValueDTO> rulesLovList = lovAdapter.queryLovValue("HME.LOADING_RULES", tenantId);
                            List<LovValueDTO> rulesList = rulesLovList.stream().filter(f -> StringUtils.equals(f.getValue(), capacity.get(0).getAttribute1())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(rulesList)) {
                                transferVO.setLoadRuleMeaning(rulesList.get(0).getMeaning());
                            }
                        }
                    }
                }
                //????????????
                transferVO6.setTransferVo(transferVO);
                continue;
            }
            //????????????
            transferVOList.add(transferVO);
        }
        transferVO6.setTargetList(transferVOList);
        return transferVO6;
    }

    private void batchTransLoadInfo(Long tenantId, String siteId, String workcellId, String operationId, List<HmeChipTransferVO2> transferVO2List) {
        if (CollectionUtils.isNotEmpty(transferVO2List)) {
            //????????????
            this.batchVerifyChipNum(tenantId, transferVO2List);
            //??????????????????
            List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>(transferVO2List.size() * 20);
            //?????????
            List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
            //??????????????????                                                                             FREEZE_TRANSFER_FINISH
            String requestId = mtEventRequestRepository.eventRequestCreate(tenantId, "FREEZE_TRANSFER_FINISH");

            //????????????????????????
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("FREEZE_TRANSFER_OUT");
            eventCreateVO.setEventRequestId(requestId);
            String transEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            //??????????????????
            MtEventCreateVO createVO = new MtEventCreateVO();
            createVO.setEventTypeCode("FREEZE_TRANSFER_IN");
            createVO.setEventRequestId(requestId);
            String eventId = mtEventRepository.eventCreate(tenantId, createVO);

            //?????????????????????
            Double chipNum = transferVO2List.stream().mapToDouble(HmeChipTransferVO2::getTotalChipNum).sum();

            //????????????????????????
            MtMaterialLot transMtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(transferVO2List.get(0).getTransMaterialLotId());
            if (!YES.equals(transMtMaterialLot.getFreezeFlag())) {
                throw new MtException("HME_COS_BARCODE_RETEST_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_020", "HME", transMtMaterialLot.getMaterialLotCode()));
            }
            //??????api???materialLotUpdate??? ??????????????????
            MtMaterialLotVO2 materialLotVO2 = new MtMaterialLotVO2();
            materialLotVO2.setEventId(transEventId);
            materialLotVO2.setMaterialLotId(transMtMaterialLot.getMaterialLotId());
            materialLotVO2.setPrimaryUomQty(BigDecimal.valueOf(transMtMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(chipNum)).doubleValue());
            mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO2, "N");

            Map<String, List<HmeChipTransferVO2>> transMaterialLotMap = transferVO2List.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));

            //????????????????????????????????????????????????
            List<String> transMaterialLotIdList = new ArrayList<>();
            transMaterialLotIdList.add(transMtMaterialLot.getMaterialLotId());
            List<HmeCosPatchPdaVO9> labCodeAndRemarkAttrList = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, transMaterialLotIdList);
            String labCode = null;
            String labRemark = null;
            if(CollectionUtils.isNotEmpty(labCodeAndRemarkAttrList)){
                labCode = labCodeAndRemarkAttrList.get(0).getLabCode();
                labRemark = labCodeAndRemarkAttrList.get(0).getRemark();
            }

            //????????????????????????
            List<HmeMaterialLotLoad> lotLoadList = new ArrayList<>();

            //????????????????????????????????? ????????????
            List<MtExtendSettings> attrList = new ArrayList<>();
            //V20210101 modify by penglin.sui for zhenyong.ban ????????????????????????????????????????????????(SUPPLIER_LOT)
            String[] arrList = new String[]{"MF_FLAG", "REWORK_FLAG", "PERFORMANCE_LEVEL", "COS_RECORD", "WAFER_NUM", "COS_TYPE", "SUPPLIER_LOT", "FREEZE_LOCATOR"};
            for (String arr : arrList) {
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName(arr);
                attrList.add(mtExtendSettings);
            }
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId, "mt_material_lot_attr", "MATERIAL_LOT_ID", transMtMaterialLot.getMaterialLotId(), attrList);

            List<String> materialLotIdList = transferVO2List.stream().map(HmeChipTransferVO2::getMaterialLotId).collect(Collectors.toList());

            //?????????
            List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
            Map<String, List<MtMaterialLot>> materialLotListMap = mtMaterialLotList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));

            //????????????
            List<String> materialLotLoadIdList = transferVO2List.stream().map(HmeChipTransferVO2::getMaterialLotLoadId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            List<HmeMaterialLotLoad> materialLotLoadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                    .andWhere(Sqls.custom().andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID, materialLotLoadIdList)).build());
            Map<String, List<HmeMaterialLotLoad>> materialLotLoadListMap = materialLotLoadList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotLoadId()));

            //?????????????????????
            MtExtendVO1 mtExtendVO = new MtExtendVO1();
            mtExtendVO.setTableName("mt_material_lot_attr");
            List<String> keyIdList = new ArrayList<>();
            keyIdList.add(transMtMaterialLot.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                keyIdList.addAll(mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList()));
            }
            mtExtendVO.setKeyIdList(keyIdList);

            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
            MtExtendVO5 mfFlagAttr = new MtExtendVO5();
            mfFlagAttr.setAttrName("MF_FLAG");
            mtExtendVO5s.add(mfFlagAttr);
            mtExtendVO.setAttrs(mtExtendVO5s);
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
            //?????????????????????
            List<String> transferHisIdList = customSequence.getNextKeys("hme_cos_transfer_his_s", transferVO2List.size());
            List<String> transferHisCidList = customSequence.getNextKeys("hme_cos_transfer_his_cid_s", transferVO2List.size());
            Date now = new Date();
            Long userId = -1L;
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            if (userDetails != null) {
                userId = userDetails.getUserId();
            }
            List<HmeCosTransferHis> transferHisList = new ArrayList<>();
            // ??????????????????????????????????????????
            List<HmeChipTransferVO2> chipTransferVO2List = new ArrayList<>();
            // ?????????????????????????????????
            Optional<MtExtendAttrVO1> mfFlagOpt = attrVO1List.stream().filter(attr -> StringUtils.equals(attr.getKeyId(), transMtMaterialLot.getMaterialLotId()) && StringUtils.equals(attr.getAttrName(), "MF_FLAG")).findFirst();
            String sourceMfFlag = mfFlagOpt.isPresent() ? mfFlagOpt.get().getAttrValue() : "";

            for (Map.Entry<String, List<HmeChipTransferVO2>> map : transMaterialLotMap.entrySet()) {
                //???????????????????????????
                List<HmeChipTransferVO2> vo2List = map.getValue();
                Double transChipNum = vo2List.stream().mapToDouble(HmeChipTransferVO2::getTotalChipNum).sum();
                // add liukejin 2020???12???18???16:11:14
                // ??????routerStepId
                String routerStepId = checkMaterialLotRouterStepId(tenantId, vo2List.get(0).getTransMaterialLotId(), vo2List.get(0).getMaterialLotId());
                List<MtMaterialLot> materialLotList = materialLotListMap.get(map.getKey());
                if (CollectionUtils.isNotEmpty(materialLotList)) {
                    //????????????
                    if (!StringUtils.equals(materialLotList.get(0).getLot(), transMtMaterialLot.getLot())) {
                        throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_016", "HME"));
                    }

                    //????????????
                    if (!StringUtils.equals(materialLotList.get(0).getMaterialId(), transMtMaterialLot.getMaterialId())) {
                        throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_016", "HME"));
                    }

                    //???????????? ???????????????????????????????????????????????????
                    Optional<MtExtendAttrVO> freezeLocatorOpt = mtExtendAttrVOList.stream().filter(dto -> StringUtils.equals(dto.getAttrName(), "FREEZE_LOCATOR")).findFirst();
                    String freezeLocator = freezeLocatorOpt.isPresent() ? freezeLocatorOpt.get().getAttrValue() : "";
                    boolean locatorFlag = StringCommonUtils.contains(materialLotList.get(0).getLocatorId(), transMtMaterialLot.getLocatorId(), freezeLocator);
                    if (!locatorFlag) {
                        throw new MtException("HME_COS_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_019", "HME", materialLotList.get(0).getMaterialLotCode()));
                    }
                    //??????WAFER?????????
                    this.verifyWaferAndWorkOder(tenantId, materialLotList.get(0), transMtMaterialLot.getMaterialLotId(), itemGroupList);

                    //???????????????
                    if (CollectionUtils.isNotEmpty(attrVO1List)) {
                        Optional<MtExtendAttrVO1> first = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLotList.get(0).getMaterialLotId())).findFirst();
                        String mfFlag = first.isPresent() ? (StringUtils.equals(first.get().getAttrValue(), HmeConstants.ConstantValue.YES) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO) : HmeConstants.ConstantValue.NO;

                        Optional<MtExtendAttrVO1> two = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), transMtMaterialLot.getMaterialLotId())).findFirst();

                        String transMfFlag = two.isPresent() ? (StringUtils.equals(two.get().getAttrValue(), HmeConstants.ConstantValue.YES) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO) : HmeConstants.ConstantValue.NO;

                        if (!StringUtils.equals(mfFlag, transMfFlag)) {
                            throw new MtException("HME_COS_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_COS_017", "HME"));
                        }
                    }
                    if (StringUtils.equals(materialLotList.get(0).getEnableFlag(), HmeConstants.ConstantValue.NO)) {
                        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                        mtMaterialLotVO2.setEventId(eventId);
                        mtMaterialLotVO2.setSiteId(transMtMaterialLot.getSiteId());
                        mtMaterialLotVO2.setEnableFlag(YES);
                        mtMaterialLotVO2.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                        mtMaterialLotVO2.setQualityStatus(transMtMaterialLot.getQualityStatus());
                        mtMaterialLotVO2.setMaterialId(transMtMaterialLot.getMaterialId());
                        mtMaterialLotVO2.setPrimaryUomId(transMtMaterialLot.getPrimaryUomId());
                        mtMaterialLotVO2.setTrxPrimaryUomQty(transChipNum);
                        mtMaterialLotVO2.setLocatorId(transMtMaterialLot.getLocatorId());
                        mtMaterialLotVO2.setLoadTime(new Date());
                        mtMaterialLotVO2.setCreateReason("INITIALIZE");
                        mtMaterialLotVO2.setInSiteTime(new Date());
                        mtMaterialLotVO2.setEoId(transMtMaterialLot.getEoId());
                        //V20210101 modify by penglin.sui for zhenyong.ban ??????????????????????????????????????????
                        mtMaterialLotVO2.setSupplierId(transMtMaterialLot.getSupplierId());
                        mtMaterialLotVO2.setSupplierSiteId(transMtMaterialLot.getSupplierSiteId());
                        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

                        //?????????????????????
                        MtCommonExtendVO6 extendVO6 = new MtCommonExtendVO6();
                        List<MtCommonExtendVO5> mtExtendVO5List = new ArrayList<>();

                        mtExtendAttrVOList.forEach(e -> {
                            MtCommonExtendVO5 mtExtendVO5 = new MtCommonExtendVO5();
                            mtExtendVO5.setAttrName(e.getAttrName());
                            mtExtendVO5.setAttrValue(e.getAttrValue());
                            mtExtendVO5List.add(mtExtendVO5);
                        });

                        //????????????
                        MtCommonExtendVO5 mtExtendVO5 = new MtCommonExtendVO5();
                        mtExtendVO5.setAttrName("CONTAINER_TYPE");
                        mtExtendVO5.setAttrValue(vo2List.get(0).getContainerType());
                        mtExtendVO5List.add(mtExtendVO5);

                        //???????????????
                        MtCommonExtendVO5 rowObj = new MtCommonExtendVO5();
                        rowObj.setAttrName("LOCATION_ROW");
                        rowObj.setAttrValue(String.valueOf(vo2List.get(0).getLocationRow()));
                        mtExtendVO5List.add(rowObj);

                        //???????????????
                        MtCommonExtendVO5 columnObj = new MtCommonExtendVO5();
                        columnObj.setAttrName("LOCATION_COLUMN");
                        columnObj.setAttrValue(String.valueOf(vo2List.get(0).getLocationColumn()));
                        mtExtendVO5List.add(columnObj);

                        //?????????
                        MtCommonExtendVO5 chipObj = new MtCommonExtendVO5();
                        chipObj.setAttrName("CHIP_NUM");
                        chipObj.setAttrValue(String.valueOf(vo2List.get(0).getTotalChipNum()));
                        mtExtendVO5List.add(chipObj);

                        //????????????
                        MtCommonExtendVO5 originalAttr = new MtCommonExtendVO5();
                        originalAttr.setAttrName("ORIGINAL_ID");
                        originalAttr.setAttrValue(transMtMaterialLot.getMaterialLotId());
                        mtExtendVO5List.add(originalAttr);

                        // lkj 2020???12???22???11:24:37
                        mtExtendVO5List.add(new MtCommonExtendVO5() {{
                            setAttrName("CURRENT_ROUTER_STEP");
                            setAttrValue(routerStepId);
                        }});

                        //????????????
                        MtCommonExtendVO5 labCodeAttr = new MtCommonExtendVO5();
                        labCodeAttr.setAttrName("LAB_CODE");
                        labCodeAttr.setAttrValue(labCode);
                        mtExtendVO5List.add(labCodeAttr);

                        //??????????????????
                        MtCommonExtendVO5 labRemarkAttr = new MtCommonExtendVO5();
                        labRemarkAttr.setAttrName("LAB_REMARK");
                        labRemarkAttr.setAttrValue(labRemark);
                        mtExtendVO5List.add(labRemarkAttr);

                        extendVO6.setAttrs(mtExtendVO5List);
                        extendVO6.setKeyId(map.getKey());
                        attrPropertyList.add(extendVO6);
                    } else if (StringUtils.equals(materialLotList.get(0).getEnableFlag(), HmeConstants.ConstantValue.YES)) {
                        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                            {
                                setEventId(eventId);
                                setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                                setTrxPrimaryUomQty(transChipNum);
                            }
                        }, "N");

                        //?????????????????????
                        MtCommonExtendVO6 extendVO6 = new MtCommonExtendVO6();
                        List<MtCommonExtendVO5> mtExtendVO5List = new ArrayList<>();
                        MtCommonExtendVO5 extendVO5 = new MtCommonExtendVO5();
                        extendVO5.setAttrName("ORIGINAL_ID");
                        extendVO5.setAttrValue(transMtMaterialLot.getMaterialLotId());
                        mtExtendVO5List.add(extendVO5);
                        //????????????
                        MtCommonExtendVO5 labCodeAttr = new MtCommonExtendVO5();
                        labCodeAttr.setAttrName("LAB_CODE");
                        labCodeAttr.setAttrValue(labCode);
                        mtExtendVO5List.add(labCodeAttr);

                        //??????????????????
                        MtCommonExtendVO5 labRemarkAttr = new MtCommonExtendVO5();
                        labRemarkAttr.setAttrName("LAB_REMARK");
                        labRemarkAttr.setAttrValue(labRemark);
                        mtExtendVO5List.add(labRemarkAttr);
                        extendVO6.setAttrs(mtExtendVO5List);
                        extendVO6.setKeyId(map.getKey());
                        attrPropertyList.add(extendVO6);
                    }

                    //?????????????????????????????????
                    List<MtExtendSettings> attrList2 = new ArrayList<>();
                    String[] arrList2 = new String[]{"WAFER_NUM", "COS_TYPE", "WORK_ORDER_ID"};
                    for (String arr : arrList2) {
                        MtExtendSettings mtExtendSettings = new MtExtendSettings();
                        mtExtendSettings.setAttrName(arr);
                        attrList2.add(mtExtendSettings);
                    }
                    List<MtExtendAttrVO> mtExtendAttrVOList2 = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", transMtMaterialLot.getMaterialLotId(), attrList2);
                    for (HmeChipTransferVO2 chipTransferVO2 : vo2List) {
                        //???????????????????????????????????????????????????
                        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = materialLotLoadListMap.get(chipTransferVO2.getMaterialLotLoadId());
                        // ????????????????????????  ??????????????????????????????????????????????????????
                        if (YES.equals(materialLotList.get(0).getEnableFlag())) {
                            String codeFreezeFlag = StringUtils.isNotBlank(materialLotList.get(0).getFreezeFlag()) ? materialLotList.get(0).getFreezeFlag() : NO;
                            String loadFreezeFlag = StringUtils.isNotBlank(hmeMaterialLotLoadList.get(0).getAttribute14()) ? hmeMaterialLotLoadList.get(0).getAttribute14() : NO;
                            if (!StringUtils.equals(codeFreezeFlag, loadFreezeFlag)) {
                                throw new MtException("HME_COS_BARCODE_RETEST_025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_COS_BARCODE_RETEST_025", "HME", materialLotList.get(0).getMaterialLotCode()));
                            }
                        }

                        // ???????????????????????????Y ???????????????????????? ??????????????????????????????
                        if (!YES.equals(hmeMaterialLotLoadList.get(0).getAttribute14())) {
                            HmeChipTransferVO2 hmeChipTransferVO2 = new HmeChipTransferVO2();
                            hmeChipTransferVO2.setTotalChipNum(transChipNum.longValue());
                            hmeChipTransferVO2.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                            chipTransferVO2List.add(hmeChipTransferVO2);
                        }

                        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                            HmeMaterialLotLoad materialLotLoad = hmeMaterialLotLoadList.get(0);
                            materialLotLoad.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                            materialLotLoad.setSourceLoadRow(materialLotLoad.getLoadRow());
                            materialLotLoad.setSourceLoadColumn(materialLotLoad.getLoadColumn());
                            materialLotLoad.setLoadRow(chipTransferVO2.getLoadRow());
                            materialLotLoad.setLoadColumn(chipTransferVO2.getLoadColumn());
                            materialLotLoad.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
                            materialLotLoad.setAttribute19(labCode);
                            materialLotLoad.setAttribute20(labRemark);
                            lotLoadList.add(materialLotLoad);

                            //2021-02-04 add by chaonan.hu for zhenyong.ban ??????COS????????????
                            HmeLoadJob hmeLoadJob = new HmeLoadJob();
                            hmeLoadJob.setTenantId(tenantId);
                            hmeLoadJob.setSiteId(siteId);
                            hmeLoadJob.setLoadSequence(materialLotLoad.getLoadSequence());
                            hmeLoadJob.setLoadJobType("FREEZE_TRANSFER");
                            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                                setTenantId(tenantId);
                                setMaterialLotCode(chipTransferVO2.getMaterialLotCode());
                            }});
                            hmeLoadJob.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                            hmeLoadJob.setMaterialId(transMtMaterialLot.getMaterialId());
                            hmeLoadJob.setLoadRow(materialLotLoad.getLoadRow());
                            hmeLoadJob.setLoadColumn(materialLotLoad.getLoadColumn());
                            hmeLoadJob.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
                            hmeLoadJob.setSourceLoadRow(materialLotLoad.getSourceLoadRow());
                            hmeLoadJob.setSourceLoadColumn(materialLotLoad.getSourceLoadColumn());
                            hmeLoadJob.setCosNum(materialLotLoad.getCosNum());
                            hmeLoadJob.setOperationId(operationId);
                            hmeLoadJob.setWorkcellId(workcellId);
                            mtExtendAttrVOList2.forEach(e -> {
                                if ("WORK_ORDER_ID".equals(e.getAttrName())) {
                                    hmeLoadJob.setWorkOrderId(e.getAttrValue());
                                } else if ("WAFER_NUM".equals(e.getAttrName())) {
                                    hmeLoadJob.setWaferNum(e.getAttrValue());
                                } else if ("COS_TYPE".equals(e.getAttrName())) {
                                    hmeLoadJob.setCosType(e.getAttrValue());
                                }
                            });
                            hmeLoadJob.setStatus("0");
                            hmeLoadJobRepository.insertSelective(hmeLoadJob);
                            //??????
                            List<String> ncCodeList = hmeLoadJobMapper.ncCodeQuery(tenantId, materialLotLoad.getLoadSequence());
                            if (CollectionUtils.isNotEmpty(ncCodeList)) {
                                for (String ncCode : ncCodeList) {
                                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                                    hmeLoadJobObject.setTenantId(tenantId);
                                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                                    hmeLoadJobObject.setObjectType("NC");
                                    hmeLoadJobObject.setObjectId(ncCode);
                                    hmeLoadJobObjectRepository.insertSelective(hmeLoadJobObject);
                                }
                            }

                            //????????????
                            HmeCosTransferHis transferHis = new HmeCosTransferHis();
                            transferHis.setTenantId(tenantId);
                            transferHis.setSiteId(materialLotList.get(0).getSiteId());
                            transferHis.setMaterialId(materialLotList.get(0).getMaterialId());
                            transferHis.setLocatorId(materialLotList.get(0).getLocatorId());
                            transferHis.setLoadSequence(materialLotLoad.getLoadSequence());
                            transferHis.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                            transferHis.setLoadRow(materialLotLoad.getLoadRow());
                            transferHis.setLoadColumn(materialLotLoad.getLoadColumn());
                            transferHis.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
                            transferHis.setSourceLoadRow(materialLotLoad.getSourceLoadRow());
                            transferHis.setSourceLoadColumn(materialLotLoad.getSourceLoadColumn());
                            transferHis.setCosNum(chipTransferVO2.getTotalChipNum());
                            transferHis.setLot(materialLotList.get(0).getLot());
                            transferHis.setTransferHisId(transferHisIdList.get(0));
                            transferHis.setCid(Long.valueOf(transferHisCidList.get(0)));
                            transferHis.setObjectVersionNumber(1L);
                            transferHis.setCreatedBy(userId);
                            transferHis.setCreationDate(now);
                            transferHis.setLastUpdatedBy(userId);
                            transferHis.setLastUpdateDate(now);
                            transferHisList.add(transferHis);
                        }
                    }
                }
            }

            //????????????????????????
            if (CollectionUtils.isNotEmpty(attrPropertyList)) {
                mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
            }

            //????????????????????????
            if (CollectionUtils.isNotEmpty(lotLoadList)) {
                List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(lotLoadList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                    hmeChipTransferMapper.batchUpdateHmeMaterialLotLoad(tenantId, userId, domains);
                }
            }

            if (CollectionUtils.isNotEmpty(transferHisList)) {
                hmeChipTransferRepository.batchSaveCosTransferHis(tenantId, transferHisList);
            }

            // ???????????????????????????
            if (CollectionUtils.isNotEmpty(chipTransferVO2List)) {
                this.unfreezeTargetCode(tenantId, chipTransferVO2List, transMtMaterialLot, sourceMfFlag);
            }

            // ????????????????????????????????????ATTRIBUTE14?????????Y ??????????????????????????????
            Integer freezeNum = hmeCosFreezeTransferMapper.queryFreezeMaterialLotLoad(tenantId, transMtMaterialLot.getMaterialLotId());
            if (Integer.valueOf(ZERO).compareTo(freezeNum) == 0) {
                // ??????????????? ???????????????????????????????????????????????????
                this.unfreezeSourceCode(tenantId, transMtMaterialLot, BigDecimal.valueOf(chipNum));
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param tenantId
     * @param transferVO2List
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/18 16:58
     */
    private void batchVerifyChipNum(Long tenantId, List<HmeChipTransferVO2> transferVO2List) {
        List<String> containerTypeIdList = transferVO2List.stream().map(HmeChipTransferVO2::getContainerTypeId).distinct().collect(Collectors.toList());

        List<String> operationIdList = transferVO2List.stream().map(HmeChipTransferVO2::getOperationId).distinct().collect(Collectors.toList());

        List<HmeContainerCapacity> capacityList = hmeContainerCapacityRepository.selectByCondition(Condition.builder(HmeContainerCapacity.class)
                .andWhere(Sqls.custom().andEqualTo(HmeContainerCapacity.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeContainerCapacity.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                        .andIn(HmeContainerCapacity.FIELD_CONTAINER_TYPE_ID, containerTypeIdList)
                        .andIn(HmeContainerCapacity.FIELD_OPERATION_ID, operationIdList)).build());
        Map<String, List<HmeContainerCapacity>> capacityResultMap = capacityList.stream().collect(Collectors.groupingBy(dto ->
                dto.getContainerTypeId() + "_" + dto.getOperationId() + "_" + dto.getCosType()));

        for (HmeChipTransferVO2 chipTransferVO2 : transferVO2List) {
            String mapKey = chipTransferVO2.getContainerTypeId() + "_" + chipTransferVO2.getOperationId() + "_" + chipTransferVO2.getCosType();
            if (capacityResultMap.containsKey(mapKey)) {
                List<HmeContainerCapacity> containerCapacityList = capacityResultMap.get(mapKey);
                if (CollectionUtils.isNotEmpty(containerCapacityList)) {
                    if (!chipTransferVO2.getTotalChipNum().equals(containerCapacityList.get(0).getCapacity())) {
                        throw new MtException("HME_CHIP_TRANSFER_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_009", "HME"));
                    }
                }
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param transferVO2List
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/18 15:43
     */
    private void unfreezeTargetCode(Long tenantId, List<HmeChipTransferVO2> transferVO2List, MtMaterialLot sourceMaterialLot, String mfFlag) {
        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_FREE");
        // ??????????????????
        String outEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("MATERIAL_FREE_OUT");
            setEventRequestId(eventRequestId);
        }});
        // ??????????????????
        String inEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("MATERIAL_FREE_IN");
            setEventRequestId(eventRequestId);
        }});
        // ????????????????????????
        Double transChipNum = transferVO2List.stream().mapToDouble(HmeChipTransferVO2::getTotalChipNum).sum();

        // ????????????????????????????????????
        List<MtExtendAttrVO> freezeLocatorList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_material_lot_attr");
            setAttrName("FREEZE_LOCATOR");
            setKeyId(sourceMaterialLot.getMaterialLotId());
        }});
        if (CollectionUtils.isEmpty(freezeLocatorList)) {
            throw new MtException("HME_COS_BARCODE_RETEST_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_023", "HME", sourceMaterialLot.getMaterialLotCode()));
        }
        MtModLocator freezeLocator = mtModLocatorRepository.selectByPrimaryKey(freezeLocatorList.get(0).getAttrValue());
        if (freezeLocator == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_023", "HME", sourceMaterialLot.getMaterialLotCode()));
        }
        MtModLocator freezeWarehouse = mtModLocatorRepository.selectByPrimaryKey(freezeLocator.getParentLocatorId());
        if (freezeWarehouse == null) {
            throw new MtException("HME_COS_BARCODE_RETEST_024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_024", "HME", sourceMaterialLot.getMaterialLotCode()));
        }
        if (!YES.equals(mfFlag)) {
            WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
            transactionRequestVO.setTransactionTypeCode("MATERIAL_FREE");
            transactionRequestVO.setEventId(inEventId);
            transactionRequestVO.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            transactionRequestVO.setMaterialId(sourceMaterialLot.getMaterialId());
            transactionRequestVO.setTransactionQty(BigDecimal.valueOf(transChipNum));
            transactionRequestVO.setLotNumber(sourceMaterialLot.getLot());
            MtUom mtUom = mtUomRepository.selectByPrimaryKey(sourceMaterialLot.getPrimaryUomId());
            transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
            transactionRequestVO.setTransactionTime(new Date());
            transactionRequestVO.setPlantId(sourceMaterialLot.getSiteId());
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(sourceMaterialLot.getLocatorId());
            MtModLocator Warehouse = mtModLocatorRepository.selectByPrimaryKey(mtModLocator.getParentLocatorId());
            transactionRequestVO.setWarehouseCode(Warehouse != null ? Warehouse.getLocatorCode() : "");
            transactionRequestVO.setLocatorCode(mtModLocator != null ? mtModLocator.getLocatorCode() : "");
            transactionRequestVO.setTransferWarehouseCode(freezeWarehouse.getLocatorCode());
            transactionRequestVO.setTransferLocatorCode(freezeLocator.getLocatorCode());
            MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(sourceMaterialLot.getSupplierId());
            transactionRequestVO.setSupplierCode(mtSupplier != null ? mtSupplier.getSupplierCode() : "");
            transactionRequestVO.setMergeFlag(NO);

            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(transactionRequestVO));
            // ???????????????????????????????????????
            MtInvOnhandQuantityVO9 vo = new MtInvOnhandQuantityVO9();
            vo.setEventId(outEventId);
            vo.setSiteId(sourceMaterialLot.getSiteId());
            vo.setLocatorId(sourceMaterialLot.getLocatorId());
            vo.setMaterialId(sourceMaterialLot.getMaterialId());
            vo.setLotCode(sourceMaterialLot.getLot());
            vo.setChangeQuantity(transChipNum.doubleValue());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, vo);
            // ???????????????????????????????????????????????????????????????
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, new MtInvOnhandQuantityVO9() {{
                setSiteId(sourceMaterialLot.getSiteId());
                setLocatorId(freezeLocator.getLocatorId());
                setMaterialId(sourceMaterialLot.getMaterialId());
                setChangeQuantity(transChipNum.doubleValue());
                setLotCode(sourceMaterialLot.getLot());
                setEventId(inEventId);
            }});

        }
        Map<String, List<HmeChipTransferVO2>> targetCodeMap = transferVO2List.stream().collect(Collectors.groupingBy(HmeChipTransferVO2::getMaterialLotId));
        for (Map.Entry<String, List<HmeChipTransferVO2>> targetCode : targetCodeMap.entrySet()) {
            // ?????????????????????????????????????????????Y ?????????Y????????????????????????????????????
            Integer targetFreezeNum = hmeCosFreezeTransferMapper.queryFreezeMaterialLotLoad(tenantId, targetCode.getKey());
            if (Integer.valueOf(0).compareTo(targetFreezeNum) == 0) {
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setMaterialLotId(targetCode.getKey());
                    setLocatorId(freezeLocator.getLocatorId());
                    setFreezeFlag(NO);
                    setEventId(inEventId);
                }}, NO);
            }
        }
    }


    /**
     * ??????????????????
     *
     * @param tenantId
     * @param sourceMaterialLot
     * @param changeQty
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/18 15:42
     */
    private void unfreezeSourceCode(Long tenantId, MtMaterialLot sourceMaterialLot, BigDecimal changeQty) {
        // ???????????????????????? ??????0 ??????????????????
        BigDecimal lastQty = BigDecimal.valueOf(sourceMaterialLot.getPrimaryUomQty()).subtract(changeQty);
        if (lastQty.compareTo(BigDecimal.ZERO) != 0) {
            // ???????????????????????? ??????????????????????????????????????????
            List<MtExtendAttrVO> mfFlagAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_material_lot_attr");
                setAttrName("MF_FLAG");
                setKeyId(sourceMaterialLot.getMaterialLotId());
            }});
            String mfFlag = CollectionUtils.isNotEmpty(mfFlagAttrList) ? mfFlagAttrList.get(0).getAttrValue() : "";
            // ??????????????????
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_FREE");
            // ??????????????????
            String outEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("MATERIAL_FREE_OUT");
                setEventRequestId(eventRequestId);
            }});
            // ??????????????????
            String inEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("MATERIAL_FREE_IN");
                setEventRequestId(eventRequestId);
            }});
            // ????????????????????????????????????
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_material_lot_attr");
                setAttrName("FREEZE_LOCATOR");
                setKeyId(sourceMaterialLot.getMaterialLotId());
            }});
            if (CollectionUtils.isEmpty(attrVOList)) {
                throw new MtException("HME_COS_BARCODE_RETEST_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_023", "HME", sourceMaterialLot.getMaterialLotCode()));
            }
            MtModLocator freezeLocator = mtModLocatorRepository.selectByPrimaryKey(attrVOList.get(0).getAttrValue());
            if (freezeLocator == null) {
                throw new MtException("HME_COS_BARCODE_RETEST_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_023", "HME", sourceMaterialLot.getMaterialLotCode()));
            }
            MtModLocator freezeWarehouse = mtModLocatorRepository.selectByPrimaryKey(freezeLocator.getParentLocatorId());
            if (freezeWarehouse == null) {
                throw new MtException("HME_COS_BARCODE_RETEST_024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_024", "HME", sourceMaterialLot.getMaterialLotCode()));
            }
            if (!YES.equals(mfFlag)) {
                // ?????????????????????????????????
                WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                transactionRequestVO.setTransactionTypeCode("MATERIAL_FREE");
                transactionRequestVO.setEventId(inEventId);
                transactionRequestVO.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
                transactionRequestVO.setMaterialId(sourceMaterialLot.getMaterialId());
                transactionRequestVO.setTransactionQty(lastQty);
                transactionRequestVO.setLotNumber(sourceMaterialLot.getLot());
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(sourceMaterialLot.getPrimaryUomId());
                transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
                transactionRequestVO.setTransactionTime(new Date());
                transactionRequestVO.setPlantId(sourceMaterialLot.getSiteId());
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(sourceMaterialLot.getLocatorId());
                MtModLocator Warehouse = mtModLocatorRepository.selectByPrimaryKey(mtModLocator.getParentLocatorId());
                transactionRequestVO.setWarehouseCode(Warehouse != null ? Warehouse.getLocatorCode() : "");
                transactionRequestVO.setLocatorCode(mtModLocator != null ? mtModLocator.getLocatorCode() : "");
                transactionRequestVO.setTransferWarehouseCode(freezeWarehouse.getLocatorCode());
                transactionRequestVO.setTransferLocatorCode(freezeLocator.getLocatorCode());
                MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(sourceMaterialLot.getSupplierId());
                transactionRequestVO.setSupplierCode(mtSupplier != null ? mtSupplier.getSupplierCode() : "");
                transactionRequestVO.setMergeFlag(NO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(transactionRequestVO));
                // ???????????????????????????????????????
                MtInvOnhandQuantityVO9 vo = new MtInvOnhandQuantityVO9();
                vo.setEventId(outEventId);
                vo.setSiteId(sourceMaterialLot.getSiteId());
                vo.setLocatorId(sourceMaterialLot.getLocatorId());
                vo.setMaterialId(sourceMaterialLot.getMaterialId());
                vo.setLotCode(sourceMaterialLot.getLot());
                vo.setChangeQuantity(lastQty.doubleValue());
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, vo);
                // ???????????????????????????????????????????????????????????????
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, new MtInvOnhandQuantityVO9() {{
                    setSiteId(sourceMaterialLot.getSiteId());
                    setLocatorId(freezeLocator.getLocatorId());
                    setMaterialId(sourceMaterialLot.getMaterialId());
                    setChangeQuantity(lastQty.doubleValue());
                    setLotCode(sourceMaterialLot.getLot());
                    setEventId(inEventId);
                }});
            }
            // ???????????????????????????????????????
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                setMaterialLotCode(sourceMaterialLot.getMaterialLotCode());
                setLocatorId(freezeLocator.getLocatorId());
                setFreezeFlag(NO);
                setEventId(inEventId);
            }}, NO);
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param tenantId
     * @param vo
     * @return java.util.List<io.tarzan.common.domain.vo.MtExtendAttrVO>
     * @author sanfeng.zhang@hand-china.com 2021/3/18 11:26
     */
    private List<MtExtendAttrVO> querySiteOutMaterialLot(Long tenantId, HmeCosFreezeTransferVO vo) {
        //?????????????????????
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "FREEZE")
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, vo.getOperationId())
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, vo.getWorkcellId())
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());
        List<MtExtendAttrVO> attrVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            //??????materialLotPropertyGet-?????????????????????
            MtMaterialLot materialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSnList.get(0).getMaterialLotId());
            //???????????????????????????
            List<MtExtendSettings> attrList = new ArrayList<>();
            String[] arrList = new String[]{"CHIP_NUM", "LOCATION_ROW", "LOCATION_COLUMN", "CONTAINER_TYPE"};

            for (String arr : arrList) {
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName(arr);
                attrList.add(mtExtendSettings);
            }
            attrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), attrList);
        }
        return attrVOList;
    }

    /**
     * ??????????????????WAFER?????????
     *
     * @param tenantId
     * @param materialLot
     * @param itemGroupList
     * @return void
     * @Param siteId
     * @author sanfeng.zhang@hand-china.com 2020/11/24 15:04
     */
    private void verifyWaferAndWorkOder(Long tenantId, MtMaterialLot materialLot, String transMaterialLotId, List<LovValueDTO> itemGroupList) {
        String itemGroup = hmeChipTransferMapper.queryItemGroupByMaterialIdSite(tenantId, materialLot.getMaterialId(), materialLot.getSiteId());
        Optional<LovValueDTO> firstOpt = itemGroupList.stream().filter(dto -> StringUtils.equals(dto.getValue(), itemGroup)).findFirst();
        //????????????????????? ?????????WAFER
        MtExtendVO1 mtExtendVO = new MtExtendVO1();
        mtExtendVO.setTableName("mt_material_lot_attr");
        List<String> keyIdList = new ArrayList<>();
        keyIdList.add(materialLot.getMaterialLotId());
        keyIdList.add(transMaterialLotId);
        mtExtendVO.setKeyIdList(keyIdList);

        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
        MtExtendVO5 mfFlagAttr = new MtExtendVO5();
        mfFlagAttr.setAttrName("MF_FLAG");
        mtExtendVO5s.add(mfFlagAttr);
        MtExtendVO5 workOderAttr = new MtExtendVO5();
        workOderAttr.setAttrName("WORK_ORDER_ID");
        mtExtendVO5s.add(workOderAttr);
        MtExtendVO5 waferAttr = new MtExtendVO5();
        waferAttr.setAttrName("WAFER_NUM");
        mtExtendVO5s.add(waferAttr);
        mtExtendVO.setAttrs(mtExtendVO5s);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
        Map<String, List<MtExtendAttrVO1>> resultMap = extendAttrVO1List.stream().collect(Collectors.groupingBy(dto -> dto.getKeyId() + "_" + dto.getAttrName()));
        // ???????????????????????????????????????
        List<MtExtendAttrVO1> mfFlagList = resultMap.get(materialLot.getMaterialLotId() + "_" + "MF_FLAG");
        String mfFlag = CollectionUtils.isNotEmpty(mfFlagList) ? mfFlagList.get(0).getAttrValue() : "";
        List<MtExtendAttrVO1> transMfFlagList = resultMap.get(transMaterialLotId + "_" + "MF_FLAG");
        String transMfFlag = CollectionUtils.isNotEmpty(transMfFlagList) ? transMfFlagList.get(0).getAttrValue() : "";
        if (!StringUtils.equals(mfFlag, transMfFlag)) {
            throw new MtException("HME_COS_BARCODE_RETEST_022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_022", "HME", materialLot.getMaterialLotCode()));
        }
        if (firstOpt.isPresent()) {
            //??????????????? ???????????????
            if (StringUtils.equals(mfFlag, HmeConstants.ConstantValue.YES)) {
                //????????? ???????????? ?????????WAFER ??????????????? ??????????????? ?????????WAFER
                List<MtExtendAttrVO1> workOrderList = resultMap.get(materialLot.getMaterialLotId() + "_" + "WORK_ORDER_ID");
                List<MtExtendAttrVO1> transWorkOrderList = resultMap.get(transMaterialLotId + "_" + "WORK_ORDER_ID");
                String workOrder = CollectionUtils.isNotEmpty(workOrderList) ? workOrderList.get(0).getAttrValue() : "";
                String transWorkOrder = CollectionUtils.isNotEmpty(transWorkOrderList) ? transWorkOrderList.get(0).getAttrValue() : "";
                if (!StringUtils.equals(workOrder, transWorkOrder)) {
                    throw new MtException("HME_CHIP_TRANSFER_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_007", "HME", materialLot.getMaterialLotCode()));
                }
            }
        } else {
            //????????? ?????????WAFER?????????
            List<MtExtendAttrVO1> workOrderList = resultMap.get(materialLot.getMaterialLotId() + "_" + "WORK_ORDER_ID");
            List<MtExtendAttrVO1> transWorkOrderList = resultMap.get(transMaterialLotId + "_" + "WORK_ORDER_ID");
            String workOrder = CollectionUtils.isNotEmpty(workOrderList) ? workOrderList.get(0).getAttrValue() : "";
            String transWorkOrder = CollectionUtils.isNotEmpty(transWorkOrderList) ? transWorkOrderList.get(0).getAttrValue() : "";
            if (!StringUtils.equals(workOrder, transWorkOrder)) {
                throw new MtException("HME_CHIP_TRANSFER_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_007", "HME", materialLot.getMaterialLotCode()));
            }

            List<MtExtendAttrVO1> waferList = resultMap.get(materialLot.getMaterialLotId() + "_" + "WAFER_NUM");
            List<MtExtendAttrVO1> transWaferList = resultMap.get(transMaterialLotId + "_" + "WAFER_NUM");
            String wafer = CollectionUtils.isNotEmpty(waferList) ? waferList.get(0).getAttrValue() : "";
            String transWafer = CollectionUtils.isNotEmpty(transWaferList) ? transWaferList.get(0).getAttrValue() : "";
            if (!StringUtils.equals(wafer, transWafer)) {
                throw new MtException("HME_CHIP_TRANSFER_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_007", "HME", materialLot.getMaterialLotCode()));
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param tenantId
     * @param mtExtendAttrList
     * @param vo
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/18 9:43
     */
    private void verifyTarget(Long tenantId, List<MtExtendAttrVO> mtExtendAttrList, HmeCosFreezeTransferVO2 vo) {
        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrList) {
            switch (mtExtendAttrVO.getAttrName()) {
                case "CHIP_NUM":
                    if (!StringUtils.equals(mtExtendAttrVO.getAttrValue(), String.valueOf(vo.getChipNum()))) {
                        throw new MtException("HME_CHIP_TRANSFER_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_019", "HME", vo.getMaterialLotCode()));
                    }
                    break;
                case "LOCATION_ROW":
                    if (!StringUtils.equals(mtExtendAttrVO.getAttrValue(), String.valueOf(vo.getLocationRow()))) {
                        throw new MtException("HME_CHIP_TRANSFER_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_020", "HME", vo.getMaterialLotCode()));
                    }
                    break;
                case "LOCATION_COLUMN":
                    if (!StringUtils.equals(mtExtendAttrVO.getAttrValue(), String.valueOf(vo.getLocationColumn()))) {
                        throw new MtException("HME_CHIP_TRANSFER_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_021", "HME", vo.getMaterialLotCode()));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * <strong>Title : checkMaterialLotRouterStepId</strong><br/>
     * <strong>Description : ????????????RouterStepId??????????????????????????????????????????????????????????????????????????????
     * ????????????????????????????????????????????????mt_material_lot_attr???CURRENT_ROUTER_STEP???????????????
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????<br/>
     * ????????????????????????????????????????????????????????????mt_material_lot_attr???CURRENT_ROUTER_STEP????????????????????????<br/>
     * ??????????????????????????????????????????????????????ROUTER_STEP_ID<br/>
     * ??????????????????????????????????????????????????????????????????mt_material_lot_attr???CURRENT_ROUTER_STEP??????????????????????????????<br/>
     * </strong><br/>
     * <strong>Create on : 2020/12/18 ??????3:31</strong><br/>
     *
     * @param tenantId
     * @param sourceMaterialLotId ????????????ID
     * @param targetMaterialLotId ????????????ID
     * @return java.lang.String
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>????????????:</strong><br/>
     * ????????? | ???????????? | ????????????<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    private String checkMaterialLotRouterStepId(Long tenantId, String sourceMaterialLotId, String targetMaterialLotId) {
        MtMaterialLot sourceMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(sourceMaterialLotId);
        MtMaterialLot targetMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(targetMaterialLotId);
        MtMaterialLotAttrVO2 attrVO2 = new MtMaterialLotAttrVO2() {{
            setMaterialLotId(sourceMaterialLotId);
        }};
        List<MtExtendAttrVO> sourceMaterialAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, attrVO2);
        Map<String, String> sourceMaterialAttrMap = sourceMaterialAttr.stream().collect(Collectors.toMap(MtExtendAttrVO::getAttrName, dto -> dto.getAttrValue()));
        attrVO2 = new MtMaterialLotAttrVO2() {{
            setMaterialLotId(targetMaterialLotId);
        }};
        List<MtExtendAttrVO> targetMaterialAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, attrVO2);
        Map<String, String> targetMaterialAttrMap = targetMaterialAttr.stream().collect(Collectors.toMap(MtExtendAttrVO::getAttrName, dto -> dto.getAttrValue()));
        String sourceCurrentRouterStepId = sourceMaterialAttrMap.get("CURRENT_ROUTER_STEP");
        String targetCurrentRouterStepId = targetMaterialAttrMap.get("CURRENT_ROUTER_STEP");
        if (Strings.isEmpty(sourceCurrentRouterStepId)) {
            sourceCurrentRouterStepId = "";
        }
        if (Strings.isEmpty(targetCurrentRouterStepId)) {
            targetCurrentRouterStepId = "";
        }
        if (sourceMaterialLot.getEnableFlag().equals(targetMaterialLot.getEnableFlag())) {
            if (!sourceCurrentRouterStepId.equals(targetCurrentRouterStepId)) {
                throw new CommonException("?????????????????????????????????????????????????????????????????????");
            } else {
                return sourceCurrentRouterStepId;
            }
        }
        return sourceCurrentRouterStepId;
    }

    /**
     * ????????????
     *
     * @param hmeMaterialLotLoadList
     * @param loadingRules
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     * @author sanfeng.zhang@hand-china.com 2020/9/29 23:49
     */
    private List<HmeMaterialLotLoad> autoTransferRules(List<HmeMaterialLotLoad> hmeMaterialLotLoadList, String loadingRules) {
        List<HmeMaterialLotLoad> lotLoadList = new ArrayList<>();
        if (StringUtils.isBlank(loadingRules)) {
            loadingRules = "A";
        }
        switch (loadingRules) {
            case "A":
                //????????????,????????????
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());
                break;
            case "B":
                //????????????,????????????
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadColumn).thenComparing(HmeMaterialLotLoad::getLoadRow)).collect(Collectors.toList());
                break;
            case "C":
                //????????????,????????????
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadColumn).reversed().thenComparing(HmeMaterialLotLoad::getLoadRow)).collect(Collectors.toList());
                break;
            case "D":
                //????????????,????????????
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadColumn).thenComparing(HmeMaterialLotLoad::getLoadRow).reversed()).collect(Collectors.toList());
                break;
            case "E":
                //????????????,????????????
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadColumn).reversed().thenComparing(HmeMaterialLotLoad::getLoadRow).reversed()).collect(Collectors.toList());
                break;
            default:
                break;
        }
        return lotLoadList;
    }

    /**
     * ??????eoJobSN
     *
     * @param tenantId
     * @param record
     * @param vo
     * @param jobType
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     * @author sanfeng.zhang@hand-china.com 2021/3/18 11:25
     */
    private HmeEoJobSn createEoJobSnRecord(Long tenantId, HmeCosOperationRecord record, HmeCosFreezeTransferVO2 vo, String jobType) {
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setSiteInDate(new Date());
        hmeEoJobSn.setShiftId(vo.getWkcShiftId());
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setWorkcellId(vo.getWorkcellId());
        hmeEoJobSn.setOperationId(vo.getOperationId());
        hmeEoJobSn.setSnMaterialId(vo.getMaterialId());
        hmeEoJobSn.setMaterialLotId(vo.getMaterialLotId());
        hmeEoJobSn.setReworkFlag(HmeConstants.ConstantValue.NO);
        hmeEoJobSn.setJobType(jobType);
        // 20210310 add by sanfeng.zhang for zhenyong.ban ?????????eo???????????????????????????job_type???????????????eo_step_num + 1
        Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, vo.getWorkcellId(), null, vo.getMaterialLotId(), NO, jobType, vo.getOperationId());
        hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
//        // ????????????????????????????????????hme_eo_job_sn??????attribute3
//        String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, vo.getMaterialLotId());
        hmeEoJobSn.setSnQty(vo.getReleaseQty());
        if (record != null) {
            hmeEoJobSn.setWorkOrderId(record.getWorkOrderId());
            hmeEoJobSn.setSourceJobId(record.getOperationRecordId());
            hmeEoJobSn.setAttribute3(record.getCosType());
            hmeEoJobSn.setAttribute5(record.getWafer());
        } else {
            hmeEoJobSn.setWorkOrderId(vo.getWorkOrderId());
            hmeEoJobSn.setAttribute3(vo.getCosType());
            hmeEoJobSn.setAttribute5(vo.getWaferNum());
        }
        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
        return hmeEoJobSn;
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param transferVO
     * @param materialLot
     * @param hmeEoJobSn
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/19 10:18
     */
    private void queryChipMaterialCodeInfo(Long tenantId, HmeChipTransferVO transferVO, MtMaterialLot materialLot, HmeEoJobSn hmeEoJobSn) {
        transferVO.setMaterialId(materialLot.getMaterialId());
        transferVO.setReleaseQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
        transferVO.setMaterialLotId(materialLot.getMaterialLotId());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());

        transferVO.setMaterialId(materialLot.getMaterialId());
        if (mtMaterial != null) {
            transferVO.setMaterialName(mtMaterial.getMaterialName());
            transferVO.setMaterialCode(mtMaterial.getMaterialCode());
            List<MtMaterialBasic> mtMaterialBasics = hmeChipTransferMapper.queryMtMaterialBasicInfo(tenantId, mtMaterial.getMaterialId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasics)) {
                transferVO.setMaterialType(mtMaterialBasics.get(0).getItemType());
            }
        }

        //attrPropertyQuery-???????????????????????????
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), Collections.EMPTY_LIST);
        String containerTypeCode = null;
        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {

            switch (mtExtendAttrVO.getAttrName()) {
                case "WAFER_NUM":
                    //Wafer??????
                    transferVO.setTransWaferNum(mtExtendAttrVO.getAttrValue());
                    break;
                case "TYPE":
                    //TYPE
                    transferVO.setIncomingType(mtExtendAttrVO.getAttrValue());
                    break;
                case "LOTNO":
                    //LOTNO
                    transferVO.setLotno(mtExtendAttrVO.getAttrValue());
                    break;
                case "CONTAINER_TYPE":
                    //CONTAINER_TYPE
                    if (StringUtils.equals(hmeEoJobSn.getJobType(), "FREEZE_TRANSFER_IN")) {
                        transferVO.setTransContainerType(mtExtendAttrVO.getAttrValue());
                    } else {
                        transferVO.setContainerType(mtExtendAttrVO.getAttrValue());
                    }
                    containerTypeCode = mtExtendAttrVO.getAttrValue();
                    break;
                case "COS_TYPE":
                    //COS_TYPE
                    transferVO.setCosType(mtExtendAttrVO.getAttrValue());
                    break;
                case "LOCATION_ROW":
                    transferVO.setLocationRow(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "LOCATION_COLUMN":
                    transferVO.setLocationColumn(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "CHIP_NUM":
                    transferVO.setChipNum(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "WORK_ORDER_ID":
                    transferVO.setWorkOrderId(mtExtendAttrVO.getAttrValue());
                    MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtExtendAttrVO.getAttrValue());
                    transferVO.setWorkOrderNum(mtWorkOrder != null ? mtWorkOrder.getWorkOrderNum() : "");
                    break;
                case "COS_RECORD":
                    transferVO.setCosRecordId(mtExtendAttrVO.getAttrValue());
                    break;
                case "AVG_WAVE_LENGTH":
                    if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                        transferVO.setAverageWavelength(new BigDecimal(mtExtendAttrVO.getAttrValue()));
                    }
                    break;
                case "REMARK":
                    transferVO.setRemark(mtExtendAttrVO.getAttrValue());
                    break;
                case "WORKING_LOT":
                    transferVO.setReleaseLot(mtExtendAttrVO.getAttrValue());
                    break;
                case "LAB_CODE":
                    transferVO.setLabCode(mtExtendAttrVO.getAttrValue());
                    break;
                case "LAB_REMARK":
                    transferVO.setLabRemark(mtExtendAttrVO.getAttrValue());
                    break;
                default:
                    break;
            }

        }

        //?????? ?????? ??????
        MtContainerType mtContainerTypeQuery = new MtContainerType();
        mtContainerTypeQuery.setTenantId(tenantId);
        mtContainerTypeQuery.setContainerTypeCode(containerTypeCode);
        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(mtContainerTypeQuery);
        if (mtContainerType != null) {
            HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
            hmeContainerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
            hmeContainerCapacity.setOperationId(transferVO.getOperationId());
            hmeContainerCapacity.setCosType(transferVO.getCosType());
            HmeContainerCapacity capacity = hmeContainerCapacityRepository.selectOne(hmeContainerCapacity);

            if (capacity != null) {
                if (transferVO.getChipNum() == null) {
                    transferVO.setChipNum(capacity.getCapacity());
                }

                //??????
                if (transferVO.getLocationRow() == null) {
                    transferVO.setLocationRow(capacity.getLineNum());
                }
                //??????
                if (transferVO.getLocationColumn() == null) {
                    transferVO.setLocationColumn(capacity.getColumnNum());
                }
            }
        }

        //????????????????????????
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(materialLot.getMaterialLotId());
        List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        List<HmeMaterialLotLoad> collect = select.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());

        for (int i = 0; i < transferVO.getLocationRow(); i++) {
            for (int j = 0; j < transferVO.getLocationColumn(); j++) {
                Boolean flag = false;
                for (HmeMaterialLotLoad lotLoad : collect) {
                    if (Long.valueOf(i + 1).equals(lotLoad.getLoadRow()) && Long.valueOf(j + 1).equals(lotLoad.getLoadColumn())) {
                        HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                        hmeWoJobSnReturnDTO5.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
                        hmeWoJobSnReturnDTO5.setLoadSequence(lotLoad.getLoadSequence().toString());
                        hmeWoJobSnReturnDTO5.setLoadRow(lotLoad.getLoadRow());
                        hmeWoJobSnReturnDTO5.setLoadColumn(lotLoad.getLoadColumn());
                        hmeWoJobSnReturnDTO5.setCosNum(lotLoad.getCosNum());
                        hmeWoJobSnReturnDTO5.setHotSinkCode(lotLoad.getHotSinkCode());
                        hmeWoJobSnReturnDTO5.setFreezeFlag(lotLoad.getAttribute14());
                        hmeWoJobSnReturnDTO5.setFreezeFlagMeaning(StringUtils.equals(lotLoad.getAttribute14(), HmeConstants.ConstantValue.YES) ? "???" : "???");
                        hmeWoJobSnReturnDTO5.setChipLabCode(lotLoad.getAttribute19());
                        hmeWoJobSnReturnDTO5.setChipLabRemark(lotLoad.getAttribute20());
                        HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
                        hmeMaterialLotNcLoad.setLoadSequence(lotLoad.getLoadSequence());
                        List<HmeMaterialLotNcLoad> select1 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
                        if (CollectionUtils.isNotEmpty(select1)) {
                            hmeWoJobSnReturnDTO5.setDocList(select1);
                        }
                        flag = true;
                        hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                        break;
                    }
                }
                if (!flag) {
                    hmeWoJobSnReturnDTO5s.add(new HmeWoJobSnReturnDTO5());
                }
            }
        }
        transferVO.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s);
    }


    private List<HmeChipTransferVO> materialCodeTargetQuery(Long tenantId, String materialLotCodeList, String workcellId) {
        if (StringUtils.isBlank(materialLotCodeList)) {
            return Collections.EMPTY_LIST;
        }
        List<String> materialLotCodes = Arrays.asList(materialLotCodeList.split(","));
        List<HmeChipTransferVO> transferVOList = new ArrayList<>();
        materialLotCodes.forEach(e -> {
            HmeChipTransferVO transferVO = new HmeChipTransferVO();

            //??????materialLotPropertyGet-?????????????????????
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, e);

            if (materialLot == null) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", e));
            }

            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "FREEZE_TRANSFER_OUT")
                            .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, workcellId)
                            .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                            .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                    .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());

            if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
                throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_010", "HME", e));
            }

            transferVO.setEoJobSnId(hmeEoJobSnList.get(0).getJobId());
            transferVO.setOperationId(hmeEoJobSnList.get(0).getOperationId());
            transferVO.setMaterialId(materialLot.getMaterialId());
            transferVO.setReleaseQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
            transferVO.setMaterialLotId(materialLot.getMaterialLotId());

            //??????materialLotLimitAttrQuery-???????????????????????????
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
            //COS??????
            mtMaterialLotAttrVO2.setAttrName("COS_TYPE");
            List<MtExtendAttrVO> cosTypeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(cosTypeList)) {
                transferVO.setCosType(cosTypeList.get(0).getAttrValue());
            }

            //????????????
            mtMaterialLotAttrVO2.setAttrName("CONTAINER_TYPE");
            List<MtExtendAttrVO> containerTypeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(containerTypeList)) {
                transferVO.setContainerType(containerTypeList.get(0).getAttrValue());

                //????????????id ??????????????????
                List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(Condition.builder(MtContainerType.class)
                        .andWhere(Sqls.custom().andEqualTo(MtContainerType.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtContainerType.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                                .andEqualTo(MtContainerType.FIELD_CONTAINER_TYPE_CODE, containerTypeList.get(0).getAttrValue())).build());

                transferVO.setContainerTypeId(CollectionUtils.isNotEmpty(mtContainerTypeList) ? mtContainerTypeList.get(0).getContainerTypeId() : "");
            }

            //??????
            mtMaterialLotAttrVO2.setAttrName("LOCATION_ROW");
            List<MtExtendAttrVO> rowList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(rowList) && StringUtils.isNotBlank(rowList.get(0).getAttrValue())) {
                transferVO.setLocationRow(Long.valueOf(rowList.get(0).getAttrValue()));
            }

            //??????
            mtMaterialLotAttrVO2.setAttrName("LOCATION_COLUMN");
            List<MtExtendAttrVO> columnList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(columnList) && StringUtils.isNotBlank(columnList.get(0).getAttrValue())) {
                transferVO.setLocationColumn(Long.valueOf(columnList.get(0).getAttrValue()));
            }

            //?????????
            mtMaterialLotAttrVO2.setAttrName("CHIP_NUM");
            List<MtExtendAttrVO> chipNumList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(chipNumList) && StringUtils.isNotBlank(chipNumList.get(0).getAttrValue())) {
                transferVO.setChipNum(Long.valueOf(chipNumList.get(0).getAttrValue()));
            }
            transferVOList.add(transferVO);
        });
        return transferVOList;
    }
}
