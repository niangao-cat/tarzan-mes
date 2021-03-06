package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeCosPoorInspectionService;
import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * ?????????ruijie.wang01@hand-china.com
 * ?????????2020/8/19 10:08
 */
@Component
@Slf4j
public class HmeCosPoorInspectionServiceImpl implements HmeCosPoorInspectionService {

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private HmeCosCommonService hmeCosCommonService;

    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;

    @Autowired
    private HmeMaterialLotNcRecordRepository hmeMaterialLotNcRecordRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private HmeCosScrapRepository hmeCosScrapRepository;

    @Autowired
    private HmeMaterialLotNcRecordMapper hmeMaterialLotNcRecordMapper;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private HmeCosScrapMapper hmeCosScrapMapper;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobEquipmentService hmeEoJobEquipmentService;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private HmeCosNcRecordRepository hmeCosNcRecordRepository;

    @Autowired
    private HmeLoadJobRepository hmeLoadJobRepository;

    @Autowired
    private HmeLoadJobObjectRepository hmeLoadJobObjectRepository;

    @Autowired
    private HmeLoadJobMapper hmeLoadJobMapper;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeVisualInspectionMapper hmeVisualInspectionMapper;

    /**
     * ????????????????????????-??????
     *
     * @param tenantId
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosPoorInspectionScanBarcodeResponseDTO siteIn(Long tenantId, HmeCosGetChipScanBarcodeDTO dto) {
        //????????????
        if (StringUtils.isEmpty(dto.getBarcode())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getWkcShiftId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }

        //????????????????????????????????????ID
        MtMaterialLotVO3 param = new MtMaterialLotVO3();
        param.setMaterialLotCode(dto.getBarcode());
        List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, param);
        if (CollectionUtils.isEmpty(materialLotIds) || StringUtils.isEmpty(materialLotIds.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }
        //2021-05-31 09:56:06 add by chaonan.hu for zhenyong.ban ???????????????????????????????????????
        String mfIsNMaterialLot = hmeVisualInspectionMapper.getMfIsNMaterialLot(tenantId, materialLotIds);
        if(StringUtils.isNotBlank(mfIsNMaterialLot)){
            throw new MtException("HME_COS_MATERIAL_RETURN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_004", "HME", mfIsNMaterialLot));
        }
        String materialLotId = materialLotIds.get(0);

        //??????API???????????????????????????
        HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, materialLotId, true, dto.getBarcode());

        //??????????????????????????????????????????
        hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, materialLotId);

        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
        // ???????????? ??????/?????? ???Y?????????
        if (mtMaterialLot != null) {
            if (YES.equals(mtMaterialLot.getFreezeFlag()) || YES.equals(mtMaterialLot.getStocktakeFlag())) {
                throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLot.getMaterialLotCode()));
            }
        }
        //???????????????????????????
        HmeCosGetChipScanBarcodeResponseDTO baseResponseDTO = hmeCosCommonService.getBaseScanBarcodeResponseDTO(tenantId, hmeCosMaterialLotVO, null);

        //??????????????????
        List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.queryLoadDetailByMaterialLotId(tenantId, hmeCosMaterialLotVO.getMaterialLotId());
        List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList1 = new ArrayList<>();
        int size = 0;
        for (int i = 1; i <= Integer.parseInt(baseResponseDTO.getLocationRow()); i++) {
            for (int j = 1; j <= Integer.parseInt(baseResponseDTO.getLocationColumn()); j++) {
                Long loadRow = (long) i;
                Long loadColumn = (long) j;
                List<HmeMaterialLotLoadVO2> collect = hmeMaterialLotLoadList.stream()
                        .filter(t -> t.getLoadRow().equals(loadRow) && t.getLoadColumn().equals(loadColumn))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    hmeMaterialLotLoadList1.add(collect.get(0));
                    size += collect.get(0).getDocList().size();
                } else {
                    hmeMaterialLotLoadList1.add(new HmeMaterialLotLoadVO2());
                }
            }
        }
        baseResponseDTO.setMaterialLotLoadList(hmeMaterialLotLoadList1);
        baseResponseDTO.setOkQty(String.valueOf(hmeCosMaterialLotVO.getPrimaryUomQty() - size));
//        // ???????????????????????????
//        String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, materialLotId);

        //??????EOJobSn
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setShiftId(dto.getWkcShiftId());
        hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSn.setWorkOrderId(baseResponseDTO.getWorkOrderId());
        hmeEoJobSn.setOperationId(dto.getOperationId());
        hmeEoJobSn.setSnMaterialId(baseResponseDTO.getMaterialId());
        hmeEoJobSn.setMaterialLotId(baseResponseDTO.getMaterialLotId());
        hmeEoJobSn.setJobType("NC_RECORD");
        hmeEoJobSn.setSourceJobId(baseResponseDTO.getCosRecord());
        hmeEoJobSn.setSnQty(new BigDecimal(hmeCosMaterialLotVO.getPrimaryUomQty()));
        hmeEoJobSn.setAttribute3(hmeCosMaterialLotVO.getMaterialLotAttrMap().get("COS_TYPE"));
        hmeEoJobSn.setAttribute5(hmeCosMaterialLotVO.getMaterialLotAttrMap().get("WAFER_NUM"));
        hmeCosCommonService.eoJobSnSiteIn(tenantId, hmeEoJobSn);
        hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId,Collections.singletonList(hmeEoJobSn.getJobId()),dto.getWorkcellId());
        baseResponseDTO.setEoJobSnId(hmeEoJobSn.getJobId());

        //??????????????????
        return getCosPoorInspectionScanBarcodeResponse(baseResponseDTO);
    }

    /**
     * ??????????????????????????????
     *
     * @param tenantId
     * @param ncRecordDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeMaterialLotNcLoadVO> ncRecordConfirm(Long tenantId, HmeCosPoorInspectionNcRecordDTO ncRecordDTO) {
        List<HmeMaterialLotNcLoadVO> hmeMaterialLotNcLoadVOs = new ArrayList<>();
        if (Objects.isNull(ncRecordDTO)) {
            return hmeMaterialLotNcLoadVOs;
        }
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        //??????????????????????????????????????????????????????
        if(CollectionUtils.isNotEmpty(ncRecordDTO.getLoadNum())){
            //??????????????????
            if(CollectionUtils.isNotEmpty(ncRecordDTO.getLoadSequenceList()) && ncRecordDTO.getLoadSequenceList().size() == 1){
                //????????????????????????
                return this.batchSaveMaterialLotNcLoad(tenantId, ncRecordDTO.getLoadNum(), ncRecordDTO.getLoadSequenceList().get(0), ncRecordDTO, userId);
            }
        }else {
            if(CollectionUtils.isNotEmpty(ncRecordDTO.getLoadSequenceList())){
                if(ncRecordDTO.getLoadSequenceList().size() > 1){
                    //???????????????
                    List<HmeMaterialLotLoad> materialLotLoadList = hmeMaterialLotLoadMapper.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                            .andIn(HmeMaterialLotLoad.FIELD_LOAD_SEQUENCE, ncRecordDTO.getLoadSequenceList())).build());
                    for (HmeMaterialLotLoad materialLotLoad : materialLotLoadList) {
                        List<String> loadNumList = new ArrayList<>();
                        for(long i = 1; i<= materialLotLoad.getCosNum(); i++){
                            loadNumList.add(String.valueOf(i));
                        }
                        //????????????????????????
                        List<HmeMaterialLotNcLoadVO> loadVOList = this.batchSaveMaterialLotNcLoad(tenantId, loadNumList, materialLotLoad.getLoadSequence(), ncRecordDTO, userId);
                        if(CollectionUtils.isNotEmpty(loadVOList)){
                            hmeMaterialLotNcLoadVOs.addAll(loadVOList);
                        }
                    }
                }else if(ncRecordDTO.getLoadSequenceList().size() == 1){
                    //????????????
                    HmeMaterialLotLoad materialLotLoad = hmeMaterialLotLoadMapper.selectOne(new HmeMaterialLotLoad() {{
                        setTenantId(tenantId);
                        setLoadSequence(ncRecordDTO.getLoadSequenceList().get(0));
                    }});
                    List<String> loadNumList = new ArrayList<>();
                    for(long i = 1; i<= materialLotLoad.getCosNum(); i++){
                        loadNumList.add(String.valueOf(i));
                    }
                    //????????????????????????
                    return this.batchSaveMaterialLotNcLoad(tenantId, loadNumList, materialLotLoad.getLoadSequence(), ncRecordDTO, userId);
                }
            }
        }
        return hmeMaterialLotNcLoadVOs;
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param loadNumList
     * @param loadSequence
     * @param ncRecordDTO
     * @param userId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotNcLoadVO>
     * @author sanfeng.zhang@hand-china.com 2020/11/16 11:33
     */
    private List<HmeMaterialLotNcLoadVO> batchSaveMaterialLotNcLoad(Long tenantId, List<String> loadNumList, String loadSequence, HmeCosPoorInspectionNcRecordDTO ncRecordDTO, Long userId) {
        List<HmeMaterialLotNcLoadVO> loadVOList = new ArrayList<>();
        List<HmeCosNcRecord> hmeCosNcRecordList = new ArrayList<>();
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        // ??????????????????????????????
        HmeMaterialLotLoad lotLoad = hmeMaterialLotLoadMapper.selectOne(new HmeMaterialLotLoad() {{
            setTenantId(tenantId);
            setLoadSequence(loadSequence);
        }});
        // ????????????????????????NC_RECORD???????????????????????????
        List<String> jobTypeList = new ArrayList<>();
        jobTypeList.add("NC_RECORD");
        jobTypeList.add("COS_MJ_COMPLETED");
        jobTypeList.add("COS_TEST");
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).andWhere(Sqls.custom()
                .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, ncRecordDTO.getWorkcellId())
                .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, lotLoad.getMaterialLotId())
                .andIn(HmeEoJobSn.FIELD_JOB_TYPE, jobTypeList)
                .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());
        if(CollectionUtils.isEmpty(hmeEoJobSnList)){
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(lotLoad.getMaterialLotId());
            throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_010", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // ??????????????????
        List<MtNcCode> mtNcCodeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ncRecordDTO.getNcCodeList())) {
            mtNcCodeList = mtNcCodeRepository.selectByCondition(Condition.builder(MtNcCode.class).andWhere(Sqls.custom()
                    .andEqualTo(MtNcCode.FIELD_TENANT_ID, tenantId)
                    .andIn(MtNcCode.FIELD_NC_CODE, ncRecordDTO.getNcCodeList())).build());
        }
        //????????????COS?????????????????????
        int keyLength = loadNumList.size() * ncRecordDTO.getNcCodeList().size();
        List<String> cosNcRecordIdList = customSequence.getNextKeys("hme_cos_nc_record_s", keyLength);
        List<String> cosNcRecordCidList = customSequence.getNextKeys("hme_cos_nc_record_cid_s", keyLength);
        // ?????????
        Integer cosNcRecordNum = 0;
        for (String loadNum : loadNumList) {
            //????????????????????????
            HmeMaterialLotNcLoadVO materialLotNcLoadVO = new HmeMaterialLotNcLoadVO();
            HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
            hmeMaterialLotNcLoad.setTenantId(tenantId);
            hmeMaterialLotNcLoad.setLoadSequence(loadSequence);
            hmeMaterialLotNcLoad.setLoadNum(loadNum);
            //??????????????????
            HmeMaterialLotNcLoad ncLoad = hmeMaterialLotNcLoadRepository.selectOne(hmeMaterialLotNcLoad);
            if (Objects.isNull(ncLoad) || StringUtils.isEmpty(ncLoad.getNcLoadId())) {
                hmeMaterialLotNcLoadRepository.insertSelective(hmeMaterialLotNcLoad);
                BeanUtils.copyProperties(hmeMaterialLotNcLoad, materialLotNcLoadVO);
            } else {
                BeanUtils.copyProperties(ncLoad, materialLotNcLoadVO);
            }

            //??????????????????
            List<String> ncRecordIdList = customSequence.getNextKeys("hme_material_lot_nc_record_s", ncRecordDTO.getNcCodeList().size());
            List<String> ncRecordCidList = customSequence.getNextKeys("hme_material_lot_nc_record_cid_s", ncRecordDTO.getNcCodeList().size());

            //????????????????????????
            int index = 0;
            Date now = new Date();
            List<HmeMaterialLotNcRecord> insertList = new ArrayList<>();
            List<HmeMaterialLotNcRecordVO> ncRecordVOList = new ArrayList<>();
            for (String ncCode : ncRecordDTO.getNcCodeList()) {
                HmeMaterialLotNcRecordVO materialLotNcRecordVO = new HmeMaterialLotNcRecordVO();
                HmeMaterialLotNcRecord hmeMaterialLotNcRecord = new HmeMaterialLotNcRecord();
                hmeMaterialLotNcRecord.setTenantId(tenantId);
                hmeMaterialLotNcRecord.setNcLoadId(materialLotNcLoadVO.getNcLoadId());
                hmeMaterialLotNcRecord.setNcCode(ncCode);
                //??????????????????
                HmeMaterialLotNcRecord ncRecord = hmeMaterialLotNcRecordRepository.selectOne(hmeMaterialLotNcRecord);
                if (Objects.isNull(ncRecord) || StringUtils.isEmpty(ncRecord.getNcRecordId())) {
                    hmeMaterialLotNcRecord.setNcRecordId(ncRecordIdList.get(index));
                    hmeMaterialLotNcRecord.setCid(Long.valueOf(ncRecordCidList.get(index++)));
                    hmeMaterialLotNcRecord.setObjectVersionNumber(1L);
                    hmeMaterialLotNcRecord.setCreatedBy(userId);
                    hmeMaterialLotNcRecord.setCreationDate(now);
                    hmeMaterialLotNcRecord.setLastUpdatedBy(userId);
                    hmeMaterialLotNcRecord.setLastUpdateDate(now);
                    insertList.add(hmeMaterialLotNcRecord);
                    BeanUtils.copyProperties(hmeMaterialLotNcRecord, materialLotNcRecordVO);

                    // ??????????????????????????????
                    HmeCosNcRecord hmeCosNcRecord = new HmeCosNcRecord();
                    hmeCosNcRecord.setTenantId(tenantId);
                    hmeCosNcRecord.setSiteId(defaultSiteId);
                    hmeCosNcRecord.setUserId(userId);
                    hmeCosNcRecord.setDefectCount(BigDecimal.valueOf(1));
                    if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                        hmeCosNcRecord.setJobId(hmeEoJobSnList.get(0).getJobId());
                        hmeCosNcRecord.setMaterialLotId(hmeEoJobSnList.get(0).getMaterialLotId());
                        hmeCosNcRecord.setComponentMaterialId(hmeEoJobSnList.get(0).getSnMaterialId());
                    }
                    Optional<MtNcCode> firstNcCode = mtNcCodeList.stream().filter(nc -> StringUtils.equals(nc.getNcCode(), ncCode)).findFirst();
                    if (firstNcCode.isPresent()) {
                        hmeCosNcRecord.setNcCodeId(firstNcCode.get().getNcCodeId());
                        hmeCosNcRecord.setNcType(firstNcCode.get().getNcType());
                    }
                    hmeCosNcRecord.setLoadNum(loadNum);
                    hmeCosNcRecord.setOperationId(ncRecordDTO.getOperationId());
                    hmeCosNcRecord.setWorkcellId(ncRecordDTO.getWorkcellId());
                    hmeCosNcRecord.setLoadSequence(loadSequence);
                    hmeCosNcRecord.setHotSinkCode(lotLoad.getHotSinkCode());
                    hmeCosNcRecord.setWorkOrderId(lotLoad.getAttribute3());
                    hmeCosNcRecord.setWaferNum(lotLoad.getAttribute2());
                    hmeCosNcRecord.setCosType(lotLoad.getAttribute1());
                    hmeCosNcRecord.setNcLoadRow(lotLoad.getLoadRow());
                    hmeCosNcRecord.setNcLoadColumn(lotLoad.getLoadColumn());
                    hmeCosNcRecord.setStatus(YES);
                    hmeCosNcRecord.setCosNcRecordId(cosNcRecordIdList.get(cosNcRecordNum));
                    hmeCosNcRecord.setCid(Long.valueOf(cosNcRecordCidList.get(cosNcRecordNum)));
                    hmeCosNcRecord.setObjectVersionNumber(1L);
                    hmeCosNcRecord.setCreatedBy(userId);
                    hmeCosNcRecord.setCreationDate(now);
                    hmeCosNcRecord.setLastUpdatedBy(userId);
                    hmeCosNcRecord.setLastUpdateDate(now);
                    hmeCosNcRecordList.add(hmeCosNcRecord);
                    cosNcRecordNum++;
                } else {
                    BeanUtils.copyProperties(ncRecord, materialLotNcRecordVO);
                }
                ncRecordVOList.add(materialLotNcRecordVO);
            }
            if (CollectionUtils.isNotEmpty(insertList)) {
                hmeMaterialLotNcRecordRepository.myBatchInsert(insertList);
            }
            materialLotNcLoadVO.setNcRecordList(ncRecordVOList);
            loadVOList.add(materialLotNcLoadVO);
        }
        if (CollectionUtils.isNotEmpty(hmeCosNcRecordList)) {
            List<List<HmeCosNcRecord>> splitSqlList = InterfaceUtils.splitSqlList(hmeCosNcRecordList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeCosNcRecord> domains : splitSqlList) {
                hmeCosNcRecordRepository.batchInsert(domains);
            }
        }
        //2021-02-22 add by chaonan.hu for zhenyong.ban ??????COS??????
        HmeLoadJobDTO3 hmeLoadJobDTO3 = new HmeLoadJobDTO3();
        hmeLoadJobDTO3.setSiteId(defaultSiteId);
        hmeLoadJobDTO3.setOperationId(ncRecordDTO.getOperationId());
        hmeLoadJobDTO3.setWorkcellId(ncRecordDTO.getWorkcellId());
        createLoadJob(tenantId, lotLoad, hmeLoadJobDTO3, "NC_RECORD");
        return loadVOList;
    }

    /**
     * @param tenantId          ??????ID
     * @param materialLotNcList ????????????????????????
     * @return void
     * @Description ??????????????????????????????
     * @author yuchao.wang
     * @date 2020/8/20 21:50
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ncRecordDelete(Long tenantId, List<HmeMaterialLotNcRecordVO2> materialLotNcList) {
        if (CollectionUtils.isEmpty(materialLotNcList)) {
            return;
        }

        //????????????????????????
        List<List<HmeMaterialLotNcRecordVO2>> list = InterfaceUtils.splitSqlList(materialLotNcList, 500);
        list.forEach(item -> {
            List<String> ncRecordIdList = item.stream().map(HmeMaterialLotNcRecordVO2::getNcRecordId).collect(Collectors.toList());

            //????????????????????????
            hmeMaterialLotNcRecordMapper.deleteNcRecordByIdList(tenantId, ncRecordIdList);
        });

        //?????????????????????????????????????????????????????????????????????
        List<String> ncLoadIdList = materialLotNcList.stream().map(HmeMaterialLotNcRecordVO2::getNcLoadId).distinct().collect(Collectors.toList());
        List<HmeCosNcRecord> hmeCosNcRecordList = new ArrayList<>();
        List<HmeMaterialLotNcLoad> hmeMaterialLotNcLoadList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ncLoadIdList)) {
            hmeMaterialLotNcLoadList = hmeMaterialLotNcLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotNcLoad.class).andWhere(Sqls.custom()
                    .andIn(HmeMaterialLotNcLoad.FIELD_NC_LOAD_ID, ncLoadIdList)).build());
        }
        // ????????????
        Map<String, List<HmeMaterialLotNcLoad>> ncLoadMap = hmeMaterialLotNcLoadList.stream().collect(Collectors.groupingBy(nc -> nc.getNcLoadId()));
        // ????????????????????????id??????
        Map<String, List<HmeMaterialLotNcRecordVO2>> ncRecordMap = materialLotNcList.stream().collect(Collectors.groupingBy(mln -> mln.getNcLoadId()));
        // ????????????
        List<String> ncCodeList = materialLotNcList.stream().map(HmeMaterialLotNcRecordVO2::getNcCode).distinct().collect(Collectors.toList());
        List<MtNcCode> mtNcCodeList = mtNcCodeRepository.ncCodeByMcCodeQuery(tenantId, ncCodeList);
        ncLoadIdList.forEach(ncLoadId -> {
            // ??????COS????????????
            List<HmeMaterialLotNcLoad> ncLoadList = ncLoadMap.get(ncLoadId);
            if (CollectionUtils.isNotEmpty(ncLoadList)) {
                List<HmeMaterialLotNcRecordVO2> hmeMaterialLotNcRecordList = ncRecordMap.get(ncLoadId);
                List<String> codeList = hmeMaterialLotNcRecordList.stream().map(HmeMaterialLotNcRecordVO2::getNcCode).collect(Collectors.toList());
                List<MtNcCode> filterResult = mtNcCodeList.stream().filter(nr -> codeList.contains(nr.getNcCode())).collect(Collectors.toList());
                List<HmeCosNcRecord> ncRecordList = hmeCosNcRecordRepository.selectByCondition(Condition.builder(HmeCosNcRecord.class).andWhere(Sqls.custom()
                        .andEqualTo(HmeCosNcRecord.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeCosNcRecord.FIELD_LOAD_SEQUENCE, ncLoadList.get(0).getLoadSequence())
                        .andEqualTo(HmeCosNcRecord.FIELD_LOAD_NUM, ncLoadList.get(0).getLoadNum())
                        .andEqualTo(HmeCosNcRecord.FIELD_STATUS, YES)
                        .andIn(HmeCosNcRecord.FIELD_NC_CODE_ID, filterResult.stream().map(MtNcCode::getNcCodeId).collect(Collectors.toList()))).build());
                if (CollectionUtils.isNotEmpty(ncRecordList)) {
                    ncRecordList = ncRecordList.stream().map((ncRecord) -> {
                        ncRecord.setStatus(HmeConstants.ConstantValue.NO);
                        return ncRecord;
                    }).collect(Collectors.toList());
                    hmeCosNcRecordList.addAll(ncRecordList);
                }
            }
            if (!hmeMaterialLotNcLoadRepository.checkHasNcRecodeFlag(tenantId, ncLoadId)) {
                hmeMaterialLotNcLoadRepository.deleteByPrimaryKey(ncLoadId);
            }
        });

        // ????????????????????????
        if (CollectionUtils.isNotEmpty(hmeCosNcRecordList)) {
            hmeCosNcRecordRepository.batchUpdateNcRecordStatus(tenantId, hmeCosNcRecordList);
        }
        //2022-02-22 add by chaonan.hu for zhenyong.ban ??????COS??????
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if(CollectionUtils.isNotEmpty(hmeMaterialLotNcLoadList)){
            List<String> loadSequenceList = hmeMaterialLotNcLoadList.stream().map(HmeMaterialLotNcLoad::getLoadSequence).distinct().collect(Collectors.toList());
            for (String loadSequence : loadSequenceList) {
                HmeMaterialLotLoad lotLoad = hmeMaterialLotLoadMapper.selectOne(new HmeMaterialLotLoad() {{
                    setTenantId(tenantId);
                    setLoadSequence(loadSequence);
                }});
                HmeLoadJobDTO3 hmeLoadJobDTO3 = new HmeLoadJobDTO3();
                hmeLoadJobDTO3.setSiteId(defaultSiteId);
                hmeLoadJobDTO3.setOperationId(materialLotNcList.get(0).getOperationId());
                hmeLoadJobDTO3.setWorkcellId(materialLotNcList.get(0).getWorkcellId());
                createLoadJob(tenantId, lotLoad, hmeLoadJobDTO3, "NC_RECORD_CANCEL");
            }
        }

    }

    /**
     * @param tenantId               ??????ID
     * @param hmeCosEoJobSnSiteOutVO ??????
     * @return void
     * @Description ????????????????????????-??????
     * @author yuchao.wang
     * @date 2020/8/20 21:45
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void siteOut(Long tenantId, HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO) {
        if (StringUtils.isEmpty(hmeCosEoJobSnSiteOutVO.getEoJobSnId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "eoSN????????????ID"));
        }
        if (StringUtils.isEmpty(hmeCosEoJobSnSiteOutVO.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }

        //??????
        hmeCosCommonService.eoJobSnSiteOut(tenantId, hmeCosEoJobSnSiteOutVO);
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeResponseDTO
     * @Description ????????????????????????-?????????????????????
     * @author yuchao.wang
     * @date 2020/8/24 14:28
     */
    @Override
    public HmeCosPoorInspectionScanBarcodeResponseDTO queryProcessing(Long tenantId, HmeCosGetChipScanBarcodeDTO dto) {
        //2020-01-22 16:33:23 edit by chaonan.hu for zhenyong.ban ?????????????????????eoJobSn????????????????????????????????????????????????????????????
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        if(StringUtils.isEmpty(dto.getJobId())){
            //???????????????????????? ????????????
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
            }
            if (StringUtils.isEmpty(dto.getWorkcellId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
            }
            if (StringUtils.isEmpty(dto.getWkcShiftId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
            }

            //????????????????????????EO
            hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSn.setOperationId(dto.getOperationId());
            hmeEoJobSn.setJobType("NC_RECORD");
            hmeEoJobSn = hmeEoJobSnMapper.queryLastEoJobId(tenantId, hmeEoJobSn);
            if (Objects.isNull(hmeEoJobSn) || StringUtils.isEmpty(hmeEoJobSn.getJobId())) {
                return new HmeCosPoorInspectionScanBarcodeResponseDTO();
            }
        }else{
            //????????????
            hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
        }

        //??????API???????????????????????????
        HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, hmeEoJobSn.getMaterialLotId(), true, dto.getBarcode());

        //???????????????????????????
        HmeCosGetChipScanBarcodeResponseDTO baseResponseDTO = hmeCosCommonService.getBaseScanBarcodeResponseDTO(tenantId, hmeCosMaterialLotVO, null);

        //??????????????????
        List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.queryLoadDetailByMaterialLotId(tenantId, hmeCosMaterialLotVO.getMaterialLotId());
        int size = 0;
        List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList1 = new ArrayList<>();
        for (int i = 1; i <= Integer.parseInt(baseResponseDTO.getLocationRow()); i++) {
            for (int j = 1; j <= Integer.parseInt(baseResponseDTO.getLocationColumn()); j++) {
                Long loadRow = (long) i;
                Long loadColumn = (long) j;
                List<HmeMaterialLotLoadVO2> collect = hmeMaterialLotLoadList.stream()
                        .filter(t -> t.getLoadRow().equals(loadRow) && t.getLoadColumn().equals(loadColumn))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    hmeMaterialLotLoadList1.add(collect.get(0));
                    size += collect.get(0).getDocList().size();
                } else {
                    hmeMaterialLotLoadList1.add(new HmeMaterialLotLoadVO2());
                }
            }
        }
        baseResponseDTO.setMaterialLotLoadList(hmeMaterialLotLoadList1);
        baseResponseDTO.setEoJobSnId(hmeEoJobSn.getJobId());
        baseResponseDTO.setOkQty(String.valueOf(hmeCosMaterialLotVO.getPrimaryUomQty() - size));

        //??????????????????
        return getCosPoorInspectionScanBarcodeResponse(baseResponseDTO);
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return void
     * @Description ??????
     * @author yuchao.wang
     * @date 2020/10/19 20:53
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scrapped(Long tenantId, HmeCosPoorInspectionScrappedDTO dto) {
        //????????????
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }
        if (CollectionUtils.isEmpty(dto.getMaterialLotLoadIdList())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "?????????ID"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }
        if (StringUtils.isEmpty(dto.getWkcShiftId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????ID"));
        }
        if (StringUtils.isEmpty(dto.getJobId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "JobID"));
        }
        //????????????
        HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, dto.getMaterialLotId(), false);

        //2021-05-31 15:12:23 add by chaonan.hu for zhenyong.ban ??????jobId????????????????????????????????????
        HmeEoJobSn hmeEoJobSnDb = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
        if(Objects.isNull(hmeEoJobSnDb) || Objects.nonNull(hmeEoJobSnDb.getSiteOutDate())){
            throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_010", "HME", hmeCosMaterialLotVO.getMaterialLotCode()));
        }

        //??????????????????
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID, dto.getMaterialLotLoadIdList())).build());
        if (CollectionUtils.isEmpty(hmeMaterialLotLoadList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        Optional<HmeMaterialLotLoad> loadOpt = hmeMaterialLotLoadList.stream().filter(load -> !StringUtils.equals(load.getMaterialLotId(), dto.getMaterialLotId()) || StringUtils.isBlank(load.getMaterialLotId())).findFirst();
        if(loadOpt.isPresent()){
            throw new MtException("HME_COS_027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_027", "HME"));
        }
        try{
            //2021-02-23 add by chaonan.hu for zhenyong.ban ??????
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("????????????????????????");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            hmeObjectRecordLockDTO.setObjectRecordId(dto.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(hmeCosMaterialLotVO.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            //??????
            hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);

            //???????????????????????????
            Double trxPrimaryUomQty = hmeMaterialLotLoadList.stream().map(HmeMaterialLotLoad::getCosNum).filter(Objects::nonNull).mapToDouble(Long::doubleValue).summaryStatistics().getSum();

            //??????API??????????????????
            MtMaterialLotVO2 updateMaterialLot = new MtMaterialLotVO2();
            updateMaterialLot.setMaterialLotId(dto.getMaterialLotId());
            updateMaterialLot.setTrxPrimaryUomQty(-1 * trxPrimaryUomQty);
            updateMaterialLot.setInLocatorTime(new Date());

            //????????????????????????0??????????????????????????????N
            if (Double.doubleToLongBits(trxPrimaryUomQty) == Double.doubleToLongBits(hmeCosMaterialLotVO.getPrimaryUomQty())) {
                updateMaterialLot.setEnableFlag("N");
                //2020-11-09 21:30 add by chaonan.hu for zhenyong.ban ?????????????????????0?????????????????????
                List<String> noSiteOutJobIdList = hmeCosScrapMapper.noSiteOutQuery(tenantId, dto.getMaterialLotId());
                if(CollectionUtils.isNotEmpty(noSiteOutJobIdList)){
                    Date date = new Date();
                    Long userId = DetailsHelper.getUserDetails().getUserId();
                    for (String jobId:noSiteOutJobIdList) {
                        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(jobId);
                        hmeEoJobSn.setSiteOutDate(date);
                        hmeEoJobSn.setSiteOutBy(userId);
                        hmeEoJobSnRepository.updateByPrimaryKeySelective(hmeEoJobSn);
                    }
                }
            }else if(hmeCosMaterialLotVO.getPrimaryUomQty() < trxPrimaryUomQty){
                throw new MtException("HME_COS_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_028", "HME"));
            }

            String eventId = "";
            String eventRequestId = "";
            //????????????????????????
            if (YES.equals(hmeCosMaterialLotVO.getMaterialLotAttrMap().getOrDefault("MF_FLAG", ""))) {
                //??????????????????????????????????????????ID
                eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("COS_SCRAP_MF");
                }});
                updateMaterialLot.setEventId(eventId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, updateMaterialLot, "N");
            } else {
                //????????????????????????????????????????????????ID
                eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("COS_SCRAP");
                }});
                updateMaterialLot.setEventId(eventId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, updateMaterialLot, "N");
                //??????????????????????????????????????????????????????????????????API
                eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "COS_SCRAP");

                //??????API{onhandQtyUpdateProcess} ???????????????????????????????????????
                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO9.setSiteId(hmeCosMaterialLotVO.getSiteId());
                mtInvOnhandQuantityVO9.setMaterialId(hmeCosMaterialLotVO.getMaterialId());
                mtInvOnhandQuantityVO9.setLocatorId(hmeCosMaterialLotVO.getLocatorId());
                mtInvOnhandQuantityVO9.setLotCode(hmeCosMaterialLotVO.getLot());
                mtInvOnhandQuantityVO9.setChangeQuantity(trxPrimaryUomQty);
                mtInvOnhandQuantityVO9.setEventId(eventId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
            }
            //????????????
            List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();
            //??????????????????
            List<HmeCosScrap> cosScrapList = new ArrayList<>();
            //??????????????????
            List<String> cosScrapIdList = customSequence.getNextKeys("hme_cos_scrap_s", hmeMaterialLotLoadList.size());
            List<String> cosScrapCidList = customSequence.getNextKeys("hme_cos_scrap_cid_s", hmeMaterialLotLoadList.size());

            Date now = new Date();
            Integer index = 0;
            for (HmeMaterialLotLoad materialLotLoad : hmeMaterialLotLoadList) {
                if(!StringUtils.equals("Y", hmeCosMaterialLotVO.getMaterialLotAttrMap().getOrDefault("MF_FLAG", ""))){
                    //???????????????
                    // ??????????????????API
                    // ??????woId?????????Id??????BOM??????Id?????????????????????{??????????????????????????????????????????}
                    String bomComponentId = getBomComponent(tenantId, materialLotLoad.getAttribute3(), hmeCosMaterialLotVO.getMaterialId());
                    if(StringUtils.isEmpty(bomComponentId)){
                        throw new MtException("HME_WO_JOB_SN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_WO_JOB_SN_008", "HME"));
                    }
                    MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
                    mtWoComponentActualVO1.setAssembleExcessFlag("Y");
                    mtWoComponentActualVO1.setEventRequestId(eventRequestId);
                    mtWoComponentActualVO1.setParentEventId(eventId);
                    mtWoComponentActualVO1.setLocatorId(hmeCosMaterialLotVO.getLocatorId());
                    mtWoComponentActualVO1.setMaterialId(hmeCosMaterialLotVO.getMaterialId());
                    mtWoComponentActualVO1.setOperationId(dto.getOperationId());
                    mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
                    mtWoComponentActualVO1.setWorkOrderId(materialLotLoad.getAttribute3());
                    mtWoComponentActualVO1.setTrxAssembleQty(materialLotLoad.getCosNum().doubleValue());
                    mtWoComponentActualVO1.setBomComponentId(bomComponentId);

                    MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());
                    if (Objects.isNull(mtWkcShift) || StringUtils.isBlank(mtWkcShift.getWkcShiftId())) {
                        throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_013", "HME", "????????????"));
                    }
                    mtWoComponentActualVO1.setShiftCode(mtWkcShift.getShiftCode());
                    mtWoComponentActualVO1.setShiftDate(mtWkcShift.getShiftDate());
                    mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);

                    //??????????????????????????????????????????
                    WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                    objectTransactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE");
                    objectTransactionRequestVO.setEventId(eventId);
                    objectTransactionRequestVO.setMaterialLotId(hmeCosMaterialLotVO.getMaterialLotId());
                    objectTransactionRequestVO.setMaterialId(hmeCosMaterialLotVO.getMaterialId());
                    objectTransactionRequestVO.setTransactionQty(new BigDecimal(materialLotLoad.getCosNum()));
                    objectTransactionRequestVO.setLotNumber(hmeCosMaterialLotVO.getLot());
                    if (StringUtils.isNotEmpty(hmeCosMaterialLotVO.getPrimaryUomId())) {
                        MtUom mtUom = mtUomRepository.selectByPrimaryKey(hmeCosMaterialLotVO.getPrimaryUomId());
                        if (Objects.isNull(mtUom) || StringUtils.isBlank(mtUom.getUomId())) {
                            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
                        }
                        objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
                    }
                    objectTransactionRequestVO.setTransactionTime(new Date());
                    objectTransactionRequestVO.setPlantId(hmeCosMaterialLotVO.getSiteId());
                    MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, materialLotLoad.getAttribute3());
                    if (Objects.isNull(mtWorkOrder) || StringUtils.isBlank(mtWorkOrder.getWorkOrderId())) {
                        throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_013", "HME", "????????????"));
                    }
                    objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    objectTransactionRequestVO.setMergeFlag("N");
                    WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                        setTenantId(tenantId);
                        setTransactionTypeCode("HME_WO_ISSUE");
                    }});
                    if (Objects.isNull(wmsTransactionType) || StringUtils.isBlank(wmsTransactionType.getTransactionTypeId())) {
                        throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
                    }
                    objectTransactionRequestVO.setMoveType(wmsTransactionType.getMoveType());

                    //??????
                    List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, hmeCosMaterialLotVO.getLocatorId(), "TOP");
                    if (CollectionUtils.isNotEmpty(pLocatorIds)) {
                        MtModLocator warehouse = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                        if (Objects.isNull(warehouse) || StringUtils.isBlank(warehouse.getLocatorId())) {
                            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
                        }
                        objectTransactionRequestVO.setWarehouseId(warehouse.getLocatorId());
                        objectTransactionRequestVO.setWarehouseCode(warehouse.getLocatorCode());
                    }
                    //??????
                    MtModLocator locator = mtModLocatorRepository.selectByPrimaryKey(hmeCosMaterialLotVO.getLocatorId());
                    if (Objects.isNull(locator) || StringUtils.isBlank(locator.getLocatorId())) {
                        throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_013", "HME", "????????????"));
                    }
                    objectTransactionRequestVO.setLocatorId(locator.getLocatorId());
                    objectTransactionRequestVO.setLocatorCode(locator.getLocatorCode());
                    transactionRequestVOList.add(objectTransactionRequestVO);
                }

                //2021-02-22 add by chaonan.hu for zhenyong.ban ??????COS??????
                CustomUserDetails userDetails = DetailsHelper.getUserDetails();
                Long userId = Objects.isNull(userDetails) ? -1L : userDetails.getUserId();
                String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
                HmeLoadJobDTO3 hmeLoadJobDTO3 = new HmeLoadJobDTO3();
                hmeLoadJobDTO3.setSiteId(defaultSiteId);
                hmeLoadJobDTO3.setOperationId(dto.getOperationId());
                hmeLoadJobDTO3.setWorkcellId(dto.getWorkcellId());
                createLoadJob(tenantId, materialLotLoad, hmeLoadJobDTO3, "NC_SCRAP");

                //???????????????
                HmeMaterialLotLoad update = new HmeMaterialLotLoad();
                update.setMaterialLotLoadId(materialLotLoad.getMaterialLotLoadId());
                update.setStatus("N");
                update.setMaterialLotId("");
                update.setSourceMaterialLotId(materialLotLoad.getMaterialLotId());
                update.setSourceLoadRow(materialLotLoad.getLoadRow());
                update.setSourceLoadColumn(materialLotLoad.getLoadColumn());
                hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(update);

                //?????????????????????
                HmeCosScrap cosScrap = new HmeCosScrap();
                cosScrap.setSiteId(hmeCosMaterialLotVO.getSiteId());
                cosScrap.setTenantId(tenantId);
                cosScrap.setUserId(userId);
                cosScrap.setJobId(dto.getJobId());
                cosScrap.setDateTime(new Date());
                cosScrap.setMaterialLotId(hmeCosMaterialLotVO.getMaterialLotId());
                cosScrap.setDefectCount(new BigDecimal(materialLotLoad.getCosNum()));
                cosScrap.setComponentMaterialId(hmeCosMaterialLotVO.getMaterialId());
                cosScrap.setOperationId(dto.getOperationId());
                cosScrap.setWorkcellId(dto.getWorkcellId());
                cosScrap.setLoadSequence(materialLotLoad.getLoadSequence());
                cosScrap.setHotSinkCode(materialLotLoad.getHotSinkCode());
                cosScrap.setWorkOrderId(materialLotLoad.getAttribute3());
                cosScrap.setWaferNum(materialLotLoad.getAttribute2());
                cosScrap.setCosType(materialLotLoad.getAttribute1());
                cosScrap.setAttribute1(materialLotLoad.getLoadRow() != null ? String.valueOf(materialLotLoad.getLoadRow()) : "");
                cosScrap.setAttribute2(materialLotLoad.getLoadColumn() != null ? String.valueOf(materialLotLoad.getLoadColumn()) : "");
                cosScrap.setCosScrapId(cosScrapIdList.get(index));
                cosScrap.setCid(Long.valueOf(cosScrapCidList.get(index)));
                cosScrap.setObjectVersionNumber(1L);
                cosScrap.setCreatedBy(userId);
                cosScrap.setCreationDate(now);
                cosScrap.setLastUpdatedBy(userId);
                cosScrap.setLastUpdateDate(now);
                index++;
                //?????????????????????????????????
                HmeMaterialLotNcLoadVO3 ncLoadVO3 = hmeMaterialLotNcLoadRepository
                        .queryNcInfoByLoadSequence(tenantId, materialLotLoad.getLoadSequence());
                if (Objects.isNull(ncLoadVO3) || StringUtils.isBlank(ncLoadVO3.getNcCodeId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "????????????"));
                }
                cosScrap.setNcCodeId(ncLoadVO3.getNcCodeId());
                cosScrap.setNcType(ncLoadVO3.getNcType());
                cosScrapList.add(cosScrap);
            }

            //??????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(transactionRequestVOList)){
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
            }
            //????????????
            if(CollectionUtils.isNotEmpty(cosScrapList)){
                hmeCosScrapRepository.batchInsertSelective(cosScrapList);
            }
        }catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("????????????????????????");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            hmeObjectRecordLockDTO.setObjectRecordId(dto.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(hmeCosMaterialLotVO.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            //??????
            hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
        }
    }

    @Override
    public void createLoadJob(Long tenantId, HmeMaterialLotLoad hmeMaterialLotLoad, HmeLoadJobDTO3 dto, String loadJobType) {
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
        hmeLoadJob.setCosNum(hmeMaterialLotLoad.getCosNum());
        hmeLoadJob.setHotSinkCode(hmeMaterialLotLoad.getHotSinkCode());
        hmeLoadJob.setOperationId(dto.getOperationId());
        hmeLoadJob.setWorkcellId(dto.getWorkcellId());
        if("NC_SCRAP".equals(loadJobType)){
            hmeLoadJob.setWorkOrderId(hmeMaterialLotLoad.getAttribute3());
            hmeLoadJob.setWaferNum(hmeMaterialLotLoad.getAttribute2());
            hmeLoadJob.setCosType(hmeMaterialLotLoad.getAttribute1());
        }else{
            List<MtExtendAttrVO> workOrderAttrList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
                setAttrName("WORK_ORDER_ID");
            }});
            if(CollectionUtils.isNotEmpty(workOrderAttrList) && StringUtils.isNotBlank(workOrderAttrList.get(0).getAttrValue())){
                hmeLoadJob.setWorkOrderId(workOrderAttrList.get(0).getAttrValue());
            }
            List<MtExtendAttrVO> waferNumAttrList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
                setAttrName("WAFER_NUM");
            }});
            if(CollectionUtils.isNotEmpty(waferNumAttrList) && StringUtils.isNotBlank(waferNumAttrList.get(0).getAttrValue())){
                hmeLoadJob.setWaferNum(waferNumAttrList.get(0).getAttrValue());
            }
            List<MtExtendAttrVO> cosTypeAttrList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
                setAttrName("COS_TYPE");
            }});
            if(CollectionUtils.isNotEmpty(cosTypeAttrList) && StringUtils.isNotBlank(cosTypeAttrList.get(0).getAttrValue())){
                hmeLoadJob.setCosType(cosTypeAttrList.get(0).getAttrValue());
            }
        }
        hmeLoadJob.setStatus("0");
        hmeLoadJobRepository.insertSelective(hmeLoadJob);
        //??????
        List<String> ncCodeList = hmeLoadJobMapper.ncCodeQuery(tenantId, hmeMaterialLotLoad.getLoadSequence());
        if(CollectionUtils.isNotEmpty(ncCodeList)){
            for (String ncCode:ncCodeList) {
                HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                hmeLoadJobObject.setTenantId(tenantId);
                hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                hmeLoadJobObject.setObjectType("NC");
                hmeLoadJobObject.setObjectId(ncCode);
                hmeLoadJobObjectRepository.insertSelective(hmeLoadJobObject);
            }
        }
    }

    /**
     * @param baseResponseDTO ??????
     * @return com.ruike.hme.api.dto.HmeCosPoorInspectionScanBarcodeResponseDTO
     * @Description ??????????????????
     * @author yuchao.wang
     * @date 2020/8/24 14:28
     */
    private HmeCosPoorInspectionScanBarcodeResponseDTO getCosPoorInspectionScanBarcodeResponse(HmeCosGetChipScanBarcodeResponseDTO baseResponseDTO) {
        //????????????
        HmeCosPoorInspectionScanBarcodeResponseDTO responseDTO = new HmeCosPoorInspectionScanBarcodeResponseDTO();
        BeanUtils.copyProperties(baseResponseDTO, responseDTO);

        //??????????????????-????????????
        List<HmeMaterialLotLoadVO2> materialLotLoadList = (List<HmeMaterialLotLoadVO2>) responseDTO.getMaterialLotLoadList();
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            List<HmeMaterialLotNcRecordVO2> materialLotNcList = new ArrayList<>();
            materialLotLoadList.forEach(load -> {
                //???????????????????????????????????????
                if (CollectionUtils.isNotEmpty(load.getDocList())) {
                    load.getDocList().forEach(ncLoad -> {
                        //?????????????????????????????????
                        ncLoad.getNcRecordList().forEach(ncRecord -> {
                            String position = String.valueOf((char) (64 + load.getLoadRow())) +
                                    '-' + load.getLoadColumn() + '-' + ncLoad.getLoadNum();
                            HmeMaterialLotNcRecordVO2 ncRecordVO2 = new HmeMaterialLotNcRecordVO2(ncLoad.getNcLoadId(),
                                    ncRecord.getNcRecordId(), position, ncRecord.getNcCode(), ncRecord.getNcDesc(), null, null);
                            materialLotNcList.add(ncRecordVO2);
                        });
                    });
                }
            });

            responseDTO.setMaterialLotNcList(materialLotNcList);
        }

        return responseDTO;
    }

    /**
     * ??????woId?????????Id??????BOM??????Id
     *
     * @param tenantId ??????Id
     * @param workOrderId ??????Id
     * @param materialId ??????Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/29 15:38:43
     * @return java.lang.String
     */
    private String getBomComponent(Long tenantId, String workOrderId, String materialId){
        String bomComponentId = null;
        //??????wo??????bomId
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
        if(Objects.nonNull(mtWorkOrder) && StringUtils.isNotEmpty(mtWorkOrder.getBomId())){
            List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
                setTenantId(tenantId);
                setBomId(mtWorkOrder.getBomId());
                setMaterialId(materialId);
            }});
            if(CollectionUtils.isNotEmpty(mtBomComponentList)){
                bomComponentId = mtBomComponentList.get(0).getBomComponentId();
            }
        }
        return bomComponentId;
    }
}
