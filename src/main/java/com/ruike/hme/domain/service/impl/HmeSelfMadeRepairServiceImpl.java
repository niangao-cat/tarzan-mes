package com.ruike.hme.domain.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.domain.repository.HmeRepairWorkOrderCreateRepository;
import com.ruike.hme.domain.repository.HmeServiceSplitRecordRepository;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.service.HmeSelfMadeRepairService;
import com.ruike.hme.domain.vo.HmeSelfMadeRepairVO;
import com.ruike.hme.domain.vo.HmeSelfMadeRepairVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeSelfMadeRepairMapper;
import com.ruike.itf.app.service.ItfRepairWorkOrderCreateService;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.feign.LovFeignClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO5;
import tarzan.inventory.domain.vo.MtContainerVO25;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.MATERIAL_LOT;
import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.WMS;
import static io.tarzan.common.domain.util.MtBaseConstants.EO_STATUS.CLOSED;
import static io.tarzan.common.domain.util.MtBaseConstants.EO_STATUS.COMPLETED;

/**
 * <p>
 * ??????????????? ????????????
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/20 13:46
 */
@Service
public class HmeSelfMadeRepairServiceImpl implements HmeSelfMadeRepairService {

    private final ItfRepairWorkOrderCreateService repairWorkOrderCreateService;
    private final HmeRepairWorkOrderCreateRepository repairWorkOrderCreateRepository;
    private final WmsMaterialLotRepository wmsMaterialLotRepository;
    private final MtEoRepository mtEoRepository;
    private final MtWorkOrderRepository workOrderRepository;
    private final LovFeignClient lovFeignClient;
    private final MtExtendSettingsRepository extendSettingsRepository;
    private final HmeSelfMadeRepairMapper hmeSelfMadeRepairMapper;
    private final HmeMaterialTransferRepository hmeMaterialTransferRepository;
    private final HmeSnBindEoRepository hmeSnBindEoRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    private final MtContainerRepository mtContainerRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final MtNumrangeRepository mtNumrangeRepository;
    private final HmeEoJobSnMapper hmeEoJobSnMapper;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final MtMaterialLotHisRepository mtMaterialLotHisRepository;
    private final MtMaterialLotMapper mtMaterialLotMapper;
    private final HmeServiceSplitRecordRepository hmeServiceSplitRecordRepository;
    private final MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    private static final String[] IGNORE_TABLE_FIELDS = new String[]{MtMaterialLot.FIELD_IDENTIFICATION,
            AuditDomain.FIELD_OBJECT_VERSION_NUMBER, AuditDomain.FIELD_LAST_UPDATED_BY, AuditDomain.FIELD_CREATED_BY,
            AuditDomain.FIELD_CREATION_DATE, AuditDomain.FIELD_LAST_UPDATE_DATE};


    public HmeSelfMadeRepairServiceImpl(ItfRepairWorkOrderCreateService repairWorkOrderCreateService, HmeRepairWorkOrderCreateRepository repairWorkOrderCreateRepository, WmsMaterialLotRepository wmsMaterialLotRepository, MtEoRepository mtEoRepository, MtWorkOrderRepository workOrderRepository, LovFeignClient lovFeignClient, MtExtendSettingsRepository extendSettingsRepository, HmeSelfMadeRepairMapper hmeSelfMadeRepairMapper, HmeMaterialTransferRepository hmeMaterialTransferRepository, HmeSnBindEoRepository hmeSnBindEoRepository, MtErrorMessageRepository mtErrorMessageRepository, MtContainerLoadDetailRepository mtContainerLoadDetailRepository, MtContainerRepository mtContainerRepository, MtMaterialLotRepository mtMaterialLotRepository, MtNumrangeRepository mtNumrangeRepository, HmeEoJobSnMapper hmeEoJobSnMapper, MtExtendSettingsRepository mtExtendSettingsRepository, MtMaterialLotHisRepository mtMaterialLotHisRepository, MtMaterialLotMapper mtMaterialLotMapper, HmeServiceSplitRecordRepository hmeServiceSplitRecordRepository, MtModLocatorRepository mtModLocatorRepository) {
        this.repairWorkOrderCreateService = repairWorkOrderCreateService;
        this.repairWorkOrderCreateRepository = repairWorkOrderCreateRepository;
        this.wmsMaterialLotRepository = wmsMaterialLotRepository;
        this.mtEoRepository = mtEoRepository;
        this.workOrderRepository = workOrderRepository;
        this.lovFeignClient = lovFeignClient;
        this.extendSettingsRepository = extendSettingsRepository;
        this.hmeSelfMadeRepairMapper = hmeSelfMadeRepairMapper;
        this.hmeMaterialTransferRepository = hmeMaterialTransferRepository;
        this.hmeSnBindEoRepository = hmeSnBindEoRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtContainerLoadDetailRepository = mtContainerLoadDetailRepository;
        this.mtContainerRepository = mtContainerRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.mtNumrangeRepository = mtNumrangeRepository;
        this.hmeEoJobSnMapper = hmeEoJobSnMapper;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.mtMaterialLotHisRepository = mtMaterialLotHisRepository;
        this.mtMaterialLotMapper = mtMaterialLotMapper;
        this.hmeServiceSplitRecordRepository = hmeServiceSplitRecordRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
    }

    @Override
    public WmsMaterialLotAttrVO scan(Long tenantId, String materialLotCode) {
        WmsMaterialLotAttrVO materialLot = wmsMaterialLotRepository.selectWithAttrByCode(tenantId, materialLotCode);
        this.materialLotValidation(tenantId, materialLotCode, materialLot);
        return materialLot;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public HmeRepairWorkOrderCreate submit(Long tenantId, WmsMaterialLotAttrVO materialLot) {
        // ??????????????????
        String productionVersion = hmeServiceSplitRecordRepository.productionVersionGet(tenantId);
        Date startDate = DateUtil.date();
        Date endDate = DateUtils.addDays(startDate, 30);

        // 20210813 add by sanfeng.zhang for tianyang.xie ???????????????????????????????????????
        List<MtWorkOrder> mtWorkOrderList = hmeSelfMadeRepairMapper.querySelfReworkWorkOrder(tenantId, materialLot.getMaterialLotCode());
        if (CollectionUtils.isNotEmpty(mtWorkOrderList)) {
            Optional<MtWorkOrder> firstOpt = mtWorkOrderList.stream().filter(dto -> !StringCommonUtils.contains(dto.getStatus(), "COMPLETED", "ABANDON", "CLOSED")).findFirst();
            if (firstOpt.isPresent()) {
                throw new MtException("HME_REPAIR_SN_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_REPAIR_SN_0009", "HME", firstOpt.get().getWorkOrderNum()));
            }
        }

        //V20210224 modify by penglin.sui for lu.bai ??????????????????
        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "REWORK_WO_CREATE");

        // ????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("REWORK_WO_CREATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 20210812 add by sanfeng.zhang for tianyang.xie ???????????? ?????????????????????
        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2(){{
            setEventId(eventId);
            setMaterialLotId(materialLot.getMaterialLotId());
            setLastUpdateDate(new Date());
            setQualityStatus(NG);
        }}, NO);
        // ?????????????????????????????????
        List<MtExtendVO5> extendAttrList = new ArrayList<>(1);
        MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
        reworkFlagAttr.setAttrName("REWORK_FLAG");
        //V20210224 modify by penglin.sui for lu.bai ?????????????????????Y
        reworkFlagAttr.setAttrValue(YES);
        extendAttrList.add(reworkFlagAttr);
        extendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", materialLot.getMaterialLotId(), eventId, extendAttrList);

        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(materialLot.getWarehouseId());
        if (Objects.isNull(mtModLocator)) {
            throw new MtException("HME_SPLIT_RECORD_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0019", "HME", materialLot.getLocatorCode()));
        }

        // ??????????????????
        HmeRepairWorkOrderCreate repairWorkOrderCreate = new HmeRepairWorkOrderCreate();
        repairWorkOrderCreate.setMaterialLotCode(materialLot.getMaterialLotCode());
        repairWorkOrderCreate.setMaterialCode(materialLot.getMaterialCode());
        repairWorkOrderCreate.setQty(materialLot.getPrimaryUomQty().doubleValue());
        repairWorkOrderCreate.setPrimaryUomCode(materialLot.getPrimaryUomCode());
        repairWorkOrderCreate.setSiteCode(materialLot.getPlantCode());
        repairWorkOrderCreate.setLocatorCode(mtModLocator.getLocatorCode());
        repairWorkOrderCreate.setPlanStartTime(startDate);
        repairWorkOrderCreate.setPlanEndTime(endDate);
        repairWorkOrderCreate.setProductionVersion(productionVersion);
        return repairWorkOrderCreateService.hmeRepairWorkOrderCreateService(tenantId, repairWorkOrderCreate);
    }

    @Override
    @ProcessLovValue
    public HmeSelfMadeRepairVO scanOriginalCode(Long tenantId, String materialLotCode) {
        if (StringUtils.isBlank(materialLotCode)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "???SN"));
        }
        // ?????????????????????????????????
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);
        if (materialLot == null) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        // ????????? ????????????
        if (NO.equals(materialLot.getEnableFlag())) {
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        // ??????????????????????????????Y??????
        if (YES.equals(materialLot.getFreezeFlag()) || YES.equals(materialLot.getStocktakeFlag())) {
            throw new MtException("HME_CHIP_DATA_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0006", "HME", materialLotCode));
        }
        // ???????????????Y ?????????
        List<MtExtendAttrVO> mfFLagAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_material_lot_attr");
            setAttrName("MF_FLAG");
            setKeyId(materialLot.getMaterialLotId());
        }});
        String mfFlag = CollectionUtils.isNotEmpty(mfFLagAttrList) ? mfFLagAttrList.get(0).getAttrValue() : "";
        if (!YES.equals(mfFlag)) {
            throw new MtException("HME_COS_MATERIAL_RETURN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_004", "HME", materialLot.getMaterialLotCode()));
        }
        // ???????????????????????? ??????Y?????????
        List<MtExtendAttrVO> flagAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_material_lot_attr");
            setAttrName("RENOVATE_FLAG");
            setKeyId(materialLot.getMaterialLotId());
        }});
        String renovateFlag = CollectionUtils.isNotEmpty(flagAttrList) ? flagAttrList.get(0).getAttrValue() : "";
        if (!StringUtils.equals(renovateFlag, YES)) {
            throw new MtException("HME_REPAIR_SN_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0003", "HME"));
        }
        List<HmeSelfMadeRepairVO> hmeSelfMadeRepairVOList = hmeSelfMadeRepairMapper.querySelfMadeRepairByCode(tenantId, materialLotCode);
        if (CollectionUtils.isEmpty(hmeSelfMadeRepairVOList)) {
            throw new MtException("HME_REPAIR_SN_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0001", "HME"));
        }
        if (hmeSelfMadeRepairVOList.size() > 1) {
            throw new MtException("HME_EO_JOB_SN_179", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_179", "HME"));
        }
        HmeSelfMadeRepairVO hmeSelfMadeRepairVO = hmeSelfMadeRepairVOList.get(0);
        // ???????????????????????????
        if (StringUtils.isBlank(hmeSelfMadeRepairVO.getReworkMaterialLot())) {
            throw new MtException("HME_REPAIR_SN_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0002", "HME"));
        }
        // EO?????????????????????
        if (!HmeConstants.EoStatus.WORKING.equals(hmeSelfMadeRepairVO.getStatus())) {
            throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_003", "HME", materialLotCode));
        }
        hmeSelfMadeRepairVO.setSourceMaterialLotCode(materialLotCode);
        // ??????????????????
        hmeSelfMadeRepairVO.setSoNum("");
        // ??????????????????
        String siteName = "";
        if (StringUtils.isNotBlank(hmeSelfMadeRepairVO.getSiteId())) {
            List<MtExtendAttrVO> attrVOList = extendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(hmeSelfMadeRepairVO.getSiteId());
                setAttrName("SHORT_NAME");
                setTableName("mt_mod_site_attr");
            }});
            siteName = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
        }
        // ??????????????????
        String prodLineName = "";
        if (StringUtils.isNotBlank(hmeSelfMadeRepairVO.getProductionLineId())) {
            List<MtExtendAttrVO> attrVOList = extendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(hmeSelfMadeRepairVO.getProductionLineId());
                setAttrName("SHORT_NAME");
                setTableName("mt_mod_production_line_attr");
            }});
            prodLineName = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
        }
        hmeSelfMadeRepairVO.setSiteLine(siteName + prodLineName);
        // ????????????
        List<String> itemTypeList = hmeSelfMadeRepairMapper.queryProItemType(tenantId, hmeSelfMadeRepairVO.getSiteId(), hmeSelfMadeRepairVO.getWorkOrderId());
        hmeSelfMadeRepairVO.setMaterialType(CollectionUtils.isNotEmpty(itemTypeList) ? itemTypeList.get(0) : "");
        //????????? ?????????
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
        //??????1-9???A-Z ?????????????????????????????????
        String month = hmeSnBindEoRepository.handleMonth(calendar.get(Calendar.MONTH) + 1);
        hmeSelfMadeRepairVO.setYearMonth(year + month);

        // ???????????? Y-?????? ?????? ????????? ???????????????????????????????????????
        if (!YES.equals(hmeSelfMadeRepairVO.getAfFlag())) {
            // ?????????SN ??????????????????????????????????????????????????????
            String suggestSnCode = hmeSelfMadeRepairMapper.querySuggestSnCode(tenantId, hmeSelfMadeRepairVO.getEoId());
            // ????????????????????????????????? ??????????????????
            if (StringUtils.equals(suggestSnCode, materialLotCode)) {
                suggestSnCode = "";
            }

            hmeSelfMadeRepairVO.setSuggestSnCode(suggestSnCode);
            List<String> suggestSnCodeList = new ArrayList<>();
            if (StringUtils.isNotBlank(suggestSnCode)) {
                if (suggestSnCode.length() == 14) {
                    // ?????????????????? ???2???
                    String siteLine = suggestSnCode.substring(0, 2);
                    // ???????????? ???4???
                    String itemType = suggestSnCode.substring(2, 6);
                    // ?????? ???3???
                    String yearMonth = suggestSnCode.substring(6, 9);
                    // ???????????????
                    String snNum = suggestSnCode.substring(9, suggestSnCode.length());
                    suggestSnCodeList.add(siteLine);
                    suggestSnCodeList.add(itemType);
                    suggestSnCodeList.add(yearMonth);
                    suggestSnCodeList.add(snNum);
                }
            }
            hmeSelfMadeRepairVO.setSuggestSnCodeList(suggestSnCodeList);
        }
        return hmeSelfMadeRepairVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeSelfMadeRepairVO2 barcodeTransformSubmit(Long tenantId, HmeSelfMadeRepairVO hmeSelfMadeRepairVO) {
        // ????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("HME_REPAIR_PROCESS_SN_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ?????????????????????
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, hmeSelfMadeRepairVO.getSourceMaterialLotCode());
        if (materialLot == null) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        String newBarcode = "";
        String newBarcodeId = "";
        Boolean suggestFlag = false;
        // ?????????????????????????????? ???????????????????????? ???????????? ???????????????
        if (YES.equals(hmeSelfMadeRepairVO.getAfFlag())) {
            // ????????????????????? ?????????????????????????????? ??????API{ numrangeBatchGenerate}
            StringBuffer codeStr = new StringBuffer();
            codeStr.append(hmeSelfMadeRepairVO.getSiteLine())
                    .append(hmeSelfMadeRepairVO.getMaterialType())
                    .append(hmeSelfMadeRepairVO.getYearMonth());
            String ruleCode = codeStr.toString();
            List<String> mtMaterialLotCodeList = this.createNumRange(tenantId, ruleCode, 1L);
            newBarcode = mtMaterialLotCodeList.get(0);

            materialLot.setMaterialLotCode(newBarcode);
            mtMaterialLotMapper.updateByPrimaryKeySelective(materialLot);
            //????????????
            MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
            mtMaterialLotHis.setEventId(eventId);
            BeanUtils.copyProperties(materialLot, mtMaterialLotHis);
            mtMaterialLotHisRepository.insertSelective(mtMaterialLotHis);
        } else {
            // ????????????????????? 1-?????? 2-?????????
            if (StringUtils.equals(STRING_ONE, hmeSelfMadeRepairVO.getTransformMode())) {
                MtMaterialLot suggestCode = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, hmeSelfMadeRepairVO.getSuggestSnCode());
                if (suggestCode == null) {
                    throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0003", "HME"));
                }
                // ?????????????????? ?????? ??????
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setTenantId(tenantId);
                    setMaterialLotId(suggestCode.getMaterialLotId());
                    setLocatorId(materialLot.getLocatorId());
                    setLot(materialLot.getLot());
                    setEventId(eventId);
                }}, NO);

                // ???????????????????????????mf_flag???rework_flag???top_eo_id???RENOVATE_FLAG?????????????????????
                MtExtendVO1 dto = new MtExtendVO1();
                dto.setTableName("mt_material_lot_attr");
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 mfFlagAttr = new MtExtendVO5();
                mfFlagAttr.setAttrName("MF_FLAG");
                attrList.add(mfFlagAttr);
                MtExtendVO5 reworkAttr = new MtExtendVO5();
                reworkAttr.setAttrName("REWORK_FLAG");
                attrList.add(reworkAttr);
                MtExtendVO5 topEoAttr = new MtExtendVO5();
                topEoAttr.setAttrName("TOP_EO_ID");
                attrList.add(topEoAttr);
                MtExtendVO5 flagAttr = new MtExtendVO5();
                flagAttr.setAttrName("RENOVATE_FLAG");
                attrList.add(flagAttr);
                dto.setAttrs(attrList);
                dto.setKeyIdList(Collections.singletonList(materialLot.getMaterialLotId()));
                List<MtExtendAttrVO1> extendAttrVO1List = extendSettingsRepository.attrPropertyBatchQuery(tenantId, dto);
                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName(mtExtendAttrVO1.getAttrName());
                    mtExtendVO5.setAttrValue(mtExtendAttrVO1.getAttrValue());
                    mtExtendVO5List.add(mtExtendVO5);
                }
                if (CollectionUtils.isNotEmpty(mtExtendVO5List)) {
                    extendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", suggestCode.getMaterialLotId(), eventId, mtExtendVO5List);
                }
                suggestFlag = true;
                newBarcode = hmeSelfMadeRepairVO.getSuggestSnCode();
                newBarcodeId = suggestCode.getMaterialLotId();
            } else {
                // ????????? ?????????????????????????????? ??????API{ numrangeBatchGenerate}
                StringBuffer codeStr = new StringBuffer();
                codeStr.append(hmeSelfMadeRepairVO.getSiteLine())
                        .append(hmeSelfMadeRepairVO.getMaterialType())
                        .append(hmeSelfMadeRepairVO.getYearMonth());
                String ruleCode = codeStr.toString();
                List<String> materialLotCodeList = this.createNumRange(tenantId, ruleCode, 1L);

                // ?????????????????? ????????????????????????????????????
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                copyPropertiesIgnoreNullAndTableFields(materialLot, mtMaterialLotVO2);
                mtMaterialLotVO2.setMaterialLotCode(materialLotCodeList.get(0));
                mtMaterialLotVO2.setMaterialLotId(null);
                mtMaterialLotVO2.setCurrentContainerId(null);
                mtMaterialLotVO2.setTopContainerId(null);
                mtMaterialLotVO2.setIdentification(materialLotCodeList.get(0));
                mtMaterialLotVO2.setCreateReason("INITIALIZE");
                mtMaterialLotVO2.setEventId(eventId);
                MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

                // ????????????????????????????????????????????????
                MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                mtExtendVO1.setTableName("mt_material_lot_attr");
                mtExtendVO1.setKeyIdList(Collections.singletonList(materialLot.getMaterialLotId()));
                List<MtExtendAttrVO1> extendAttrVO1List = extendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName(mtExtendAttrVO1.getAttrName());
                    mtExtendVO5.setAttrValue(mtExtendAttrVO1.getAttrValue());
                    mtExtendVO5List.add(mtExtendVO5);
                }
                if (CollectionUtils.isNotEmpty(mtExtendVO5List)) {
                    extendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", mtMaterialLotVO13.getMaterialLotId(), eventId, mtExtendVO5List);
                }
                newBarcodeId = mtMaterialLotVO13.getMaterialLotId();
                newBarcode = materialLotCodeList.get(0);
            }
            // ???????????????
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                setTenantId(tenantId);
                setMaterialLotId(materialLot.getMaterialLotId());
                setEnableFlag(NO);
                setEventId(eventId);
            }}, "N");
            // ??????????????????????????????
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("OLD_BARCODE_IN_FLAG");
            mtExtendVO5.setAttrValue("");
            attrList.add(mtExtendVO5);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", materialLot.getMaterialLotId(), eventId, attrList);

            // ??????????????????????????????, ????????????
            MtContLoadDtlVO5 condition = new MtContLoadDtlVO5();
            condition.setLoadObjectType(MATERIAL_LOT);
            condition.setLoadObjectId(materialLot.getMaterialLotId());
            condition.setTopLevelFlag(NO);
            List<String> parentIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, condition);
            if (CollectionUtils.isNotEmpty(parentIdList)) {
                // ?????????????????? ???????????????
                MtContainerVO25 mtContainerVo25 = new MtContainerVO25();
                mtContainerVo25.setContainerId(parentIdList.get(0));
                mtContainerVo25.setLoadObjectId(materialLot.getMaterialLotId());
                mtContainerVo25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerRepository.containerUnload(tenantId, mtContainerVo25);
            }
            // ??????????????? ??????????????????
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.selectByCondition(Condition.builder(HmeEoJobSn.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(HmeEoJobSn.FIELD_EO_ID, hmeSelfMadeRepairVO.getEoId())
                    .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());
            if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                for (HmeEoJobSn hmeEoJobSn : hmeEoJobSnList) {
                    if (!StringUtils.equals(hmeEoJobSn.getMaterialLotId(), newBarcodeId)) {
                        hmeEoJobSn.setMaterialLotId(newBarcodeId);
                        hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);
                    }
                }
            }
        }
        // ??????????????????????????????mt_eo_attr??????REWORK_MATERIAL_LOT??? ??????????????? ???eo???????????????????????? ?????????????????????
        if (!suggestFlag) {
            List<MtExtendVO5> extendVO5List = new ArrayList<>();
            MtExtendVO5 extendVO5 = new MtExtendVO5();
            extendVO5.setAttrName("REWORK_MATERIAL_LOT");
            extendVO5.setAttrValue(newBarcode);
            extendVO5List.add(extendVO5);
            extendSettingsRepository.attrPropertyUpdate(tenantId, "mt_eo_attr", hmeSelfMadeRepairVO.getEoId(), eventId, extendVO5List);
        }
        HmeSelfMadeRepairVO2 vo2 = new HmeSelfMadeRepairVO2();
        vo2.setMaterialLotCode(newBarcode);
        return vo2;
    }

    /**
     * ????????????
     *
     * @param tenantId        ??????
     * @param materialLotCode ?????????
     * @param materialLot     ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/20 02:34:37
     */
    private void materialLotValidation(Long tenantId, String materialLotCode, WmsMaterialLotAttrVO materialLot) {
        WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(materialLot), "MT_MATERIAL_LOT_0011", MATERIAL_LOT, materialLotCode);
        WmsCommonUtils.processValidateMessage(tenantId, BigDecimal.ONE.compareTo(materialLot.getPrimaryUomQty()) != 0, "HME_EO_JOB_SN_125", HME);
        WmsCommonUtils.processValidateMessage(tenantId, YES.equals(materialLot.getMfFlag()), "WMS_DISTRIBUTION_0003", WMS, materialLotCode);
        if (StringUtils.isNotBlank(materialLot.getEoId())) {
            MtEo eo = mtEoRepository.selectByPrimaryKey(materialLot.getEoId());
            WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(eo), "HME_NC_0021", HME, materialLotCode);
            WmsCommonUtils.processValidateMessage(tenantId, !StringCommonUtils.contains(eo.getStatus(), COMPLETED, CLOSED), "HME_WORK_ORDER_SN_008", HME);
        }

        //V20210219 modify by penglin.sui for fang.pan ????????????????????????
//        List<HmeRepairWorkOrderCreate> repairList = repairWorkOrderCreateRepository.select(new HmeRepairWorkOrderCreate() {{
//            setMaterialLotCode(materialLotCode);
//        }});
//        if (CollectionUtils.isNotEmpty(repairList)) {
//            List<String> workOrderNumList = repairList.stream().map(HmeRepairWorkOrderCreate::getWorkOrderNum).collect(Collectors.toList());
//            List<MtWorkOrder> workOrderList = workOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class).andWhere(Sqls.custom().andIn(MtWorkOrder.FIELD_WORK_ORDER_NUM, workOrderNumList)).build());
//            workOrderList.forEach(rec -> {
//                if (StringCommonUtils.contains(rec.getStatus(), RELEASED, EORELEASED, NEW, HOLD)) {
//                    WmsCommonUtils.processValidateMessage(tenantId, "HME_WORK_ORDER_SN_009", HME, rec.getWorkOrderNum());
//                }
//            });
//        }
    }

    /**
     * ??????????????????????????????
     *
     * @param tenantId
     * @param ruleCode
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/3/12 17:41
     */
    private List<String> createNumRange(Long tenantId, String ruleCode, Long numSize) {
        List<MtNumrangeVO11> mtNumrangeVO11List = new ArrayList();
        MtNumrangeVO11 vo11 = new MtNumrangeVO11();
        vo11.setSequence(1L);
        List<String> valList = new ArrayList<>();
        valList.add(ruleCode);
        vo11.setIncomingValue(valList);
        mtNumrangeVO11List.add(vo11);
        MtNumrangeVO9 dto = new MtNumrangeVO9();
        dto.setObjectCode("SN_NUM");
        dto.setIncomingValueList(mtNumrangeVO11List);
        dto.setObjectNumFlag(HmeConstants.ConstantValue.YES);
        dto.setNumQty(numSize);
        MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, dto);
        return mtNumrangeVO8.getNumberList();
    }

    public static <E, T> void copyPropertiesIgnoreNullAndTableFields(E src, T target) {
        // ????????????????????????????????????ID???????????????????????????
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(Arrays.asList(WmsCommonUtils.getNullPropertyNames(src)));
        hashSet.addAll(Arrays.asList(IGNORE_TABLE_FIELDS));
        String[] result = new String[hashSet.size()];
        BeanUtils.copyProperties(src, target, hashSet.toArray(result));
    }

}
