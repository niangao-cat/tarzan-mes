package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmePumpPreSelectionService;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmePumpPreSelection;
import com.ruike.hme.domain.entity.HmePumpSelectionDetails;
import com.ruike.hme.domain.repository.HmePumpPreSelectionRepository;
import com.ruike.hme.domain.repository.HmePumpSelectionDetailsRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosPatchPdaMapper;
import com.ruike.hme.infra.mapper.HmePumpPreSelectionMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ???????????????????????????????????????????????????
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
@Service
public class HmePumpPreSelectionServiceImpl implements HmePumpPreSelectionService {

    @Autowired
    private HmePumpPreSelectionMapper hmePumpPreSelectionMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private HmePumpPreSelectionRepository hmePumpPreSelectionRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private HmePumpSelectionDetailsRepository hmePumpSelectionDetailsRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public List<HmePumpPreSelectionVO> queryTagInfoByMaterial(Long tenantId, String materialId) {
        return hmePumpPreSelectionMapper.queryTagInfoByMaterial(tenantId, materialId);
    }

    @Override
    public HmePumpPreSelectionVO3 scanContainerOrPumpMaterialLot(Long tenantId, HmePumpPreSelectionDTO dto) {
        //????????????
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????"));
        }
        if (StringUtils.isBlank(dto.getScanCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        if (Objects.isNull(dto.getContainerQty())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????"));
        }
        if (Objects.isNull(dto.getPumpQty())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        String defaultStorageLocatorId = null;
        //????????????????????????
        String lineWorkcellId = hmePumpPreSelectionMapper.getLineWorkcellIdByWorkcell(tenantId, dto.getWorkcellId());
        if (StringUtils.isNotBlank(lineWorkcellId)) {
            //????????????????????????????????????
            List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, lineWorkcellId);
            if (CollectionUtils.isNotEmpty(defaultStorageLocatorIdList)) {
                if (defaultStorageLocatorIdList.size() > 1) {
                    //????????????????????????????????????,?????????{??????????????????????????????????????????????????????}
                    throw new MtException("HME_EO_JOB_SN_209", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_209", "HME"));
                }
                defaultStorageLocatorId = defaultStorageLocatorIdList.get(0);
            }
        }
        if (StringUtils.isBlank(defaultStorageLocatorId)) {
            //?????????????????????????????????,?????????{????????????????????????????????????????????????????????????}
            throw new MtException("HME_EO_JOB_SN_092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_092", "HME"));
        }
        MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
            setTenantId(tenantId);
            setContainerCode(dto.getScanCode());
        }});
        List<String> materialLotIdList = new ArrayList<>();
        String containerCode = null;
        String containerId = null;
        if (Objects.nonNull(mtContainer)) {
            containerCode = mtContainer.getContainerCode();
            containerId = mtContainer.getContainerId();
            //??????????????????????????????????????????????????????
            materialLotIdList = hmePumpPreSelectionRepository.getMaterialLotByContainer(tenantId, mtContainer, defaultStorageLocatorId);
        } else {
            //????????????????????????
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto.getScanCode());
            }});
            if (Objects.nonNull(mtMaterialLot)) {
                materialLotIdList.add(mtMaterialLot.getMaterialLotId());
            }
        }
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            //?????????????????????????????????????????????????????????????????????${1}??????????????????????????????,?????????
            throw new MtException("HME_PUMP_SELECTION_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_006", "HME", dto.getScanCode()));
        }
        //???????????????????????????????????????????????????
        List<HmePumpPreSelectionVO4> materialLotInfoList = hmePumpPreSelectionMapper.materialLotInfoQuery(tenantId, materialLotIdList);
        for (HmePumpPreSelectionVO4 hmePumpPreSelectionVO4 : materialLotInfoList) {
            //?????????????????????
            if (!HmeConstants.ConstantValue.YES.equals(hmePumpPreSelectionVO4.getEnableFlag())) {
                throw new MtException(StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_003" : "MT_MATERIAL_TFR_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_003" : "MT_MATERIAL_TFR_0003", "HME", hmePumpPreSelectionVO4.getMaterialLotCode()));
            }
            //????????????????????????
            if (!HmeConstants.ConstantValue.OK.equals(hmePumpPreSelectionVO4.getQualityStatus())) {
                throw new MtException(StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_002" : "RK_INVENTORY_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_002" : "RK_INVENTORY_0028", StringUtils.isNotBlank(containerCode) ? "HME" : "INVENTORY", StringUtils.isNotBlank(containerCode) ? hmePumpPreSelectionVO4.getMaterialLotCode() : ""));
            }
            //??????????????????
            if (!hmePumpPreSelectionVO4.getLocatorId().equals(defaultStorageLocatorId)) {
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(hmePumpPreSelectionVO4.getLocatorId());
                throw new MtException("HME_PUMP_SELECTION_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_008", "HME", mtModLocator.getLocatorCode()));
            }
        }
        //???????????????????????????
        String errorMaterialLotCode = hmePumpPreSelectionMapper.materialLotLoadedQuery(tenantId, materialLotIdList);
        if (StringUtils.isNotBlank(errorMaterialLotCode)) {
            throw new MtException(StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_004" : "HME_PUMP_SELECTION_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_004" : "HME_PUMP_SELECTION_005", "HME", errorMaterialLotCode));
        }
        //??????????????????
        Long containerQty = dto.getContainerQty();
        if (StringUtils.isBlank(containerCode)) {
            containerCode = "/";
        } else {
            containerQty = containerQty + 1;
        }
        for (HmePumpPreSelectionVO4 hmePumpPreSelectionVO4 : materialLotInfoList) {
            hmePumpPreSelectionVO4.setContainerCode(containerCode);
            hmePumpPreSelectionVO4.setContainerId(containerId);
        }
        HmePumpPreSelectionVO3 hmePumpPreSelectionVO3 = new HmePumpPreSelectionVO3();
        hmePumpPreSelectionVO3.setDefaultStorageLocatorId(defaultStorageLocatorId);
        hmePumpPreSelectionVO3.setContainerQty(containerQty);
        hmePumpPreSelectionVO3.setPumpQty(dto.getPumpQty() + materialLotInfoList.size());
        hmePumpPreSelectionVO3.setPumpMaterialLotInfoList(materialLotInfoList);
        return hmePumpPreSelectionVO3;
    }

    @Override
    public void scanTargetContainer(Long tenantId, String containerCode) {
        MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
            setTenantId(tenantId);
            setContainerCode(containerCode);
        }});
        //????????????????????????
        if (Objects.isNull(mtContainer)) {
            throw new MtException("HME_PUMP_SELECTION_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_009", "HME", containerCode));
        }
        //?????????????????????
        mtContainerRepository.containerAvailableValidate(tenantId, mtContainer.getContainerId());
        //??????????????????
        MtContainerVO9 mtContainerVO9 = new MtContainerVO9();
        mtContainerVO9.setContainerId(mtContainer.getContainerId());
        mtContainerVO9.setLoadObjectType("MATERIAL_LOT");
        mtContainerRepository.containerLoadVerify(tenantId, mtContainerVO9);
        //????????????????????????
        mtContainerLoadDetailRepository.containerIsEmptyValidate(tenantId, mtContainer.getContainerId());
    }

    @Override
    @ProcessLovValue
    public Page<HmePumpPreSelectionVO2> pumpSelectionLotLovQuery(Long tenantId, HmePumpPreSelectionDTO2 dto, PageRequest pageRequest) {
        Page<HmePumpPreSelectionVO2> resultPage = PageHelper.doPage(pageRequest, () -> hmePumpPreSelectionMapper.pumpSelectionLotLovQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    @ProcessLovValue
    public Page<HmePumpPreSelectionVO5> pumpPreSelectionRecallQuery(Long tenantId, HmePumpPreSelectionDTO3 dto, PageRequest pageRequest) {
        Page<HmePumpPreSelectionVO5> resultPage = new Page<>();
        //????????????
        if (StringUtils.isNotBlank(dto.getOldContainerCode())) {
            MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
                setTenantId(tenantId);
                setContainerCode(dto.getOldContainerCode());
            }});
            if (Objects.isNull(mtContainer)) {
                return resultPage;
            }
            dto.setOldContainerId(mtContainer.getContainerId());
        }
        if (StringUtils.isNotBlank(dto.getNewContainerCode())) {
            MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
                setTenantId(tenantId);
                setContainerCode(dto.getNewContainerCode());
            }});
            if (Objects.isNull(mtContainer)) {
                return resultPage;
            }
            dto.setNewContainerId(mtContainer.getContainerId());
        }
        resultPage = PageHelper.doPage(pageRequest, () -> hmePumpPreSelectionMapper.pumpPreSelectionRecallQuery(tenantId, dto));
        if (CollectionUtils.isNotEmpty(resultPage.getContent())) {
            //????????????????????????
            List<HmePumpPreSelectionVO5> resultList = resultPage.getContent();
            List<String> containerIdList = new ArrayList<>();
            containerIdList.addAll(resultList.stream().filter(item -> StringUtils.isNotBlank(item.getOldContainerId()))
                    .map(HmePumpPreSelectionVO5::getOldContainerId)
                    .collect(Collectors.toList()));
            containerIdList.addAll(resultList.stream().filter(item -> StringUtils.isNotBlank(item.getNewContainerId()))
                    .map(HmePumpPreSelectionVO5::getNewContainerId)
                    .collect(Collectors.toList()));
            containerIdList = containerIdList.stream().distinct().collect(Collectors.toList());
            Map<String, List<String>> containerMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(containerIdList)) {
                List<MtContainer> mtContainerList = hmePumpPreSelectionMapper.containerInfoQuery(tenantId, containerIdList);
                containerMap = mtContainerList.stream().collect(Collectors.groupingBy(MtContainer::getContainerId,
                        Collectors.mapping(MtContainer::getContainerCode, Collectors.toList())));
            }
            Map<String, Long> groupMap = new HashMap<>();
            Long maxGroupNum = 0L;
            for (HmePumpPreSelectionVO5 hmePumpPreSelectionVO5 : resultList) {
                //????????????????????????
                if (Objects.nonNull(containerMap)) {
                    String oldContainerId = hmePumpPreSelectionVO5.getOldContainerId();
                    String newContainerId = hmePumpPreSelectionVO5.getNewContainerId();
                    if (StringUtils.isNotBlank(oldContainerId)) {
                        List<String> containerCodeList = containerMap.get(oldContainerId);
                        if (CollectionUtils.isNotEmpty(containerCodeList)) {
                            hmePumpPreSelectionVO5.setOldContainerCode(containerCodeList.get(0));
                        }
                    }
                    if (StringUtils.isNotBlank(newContainerId)) {
                        List<String> containerCodeList = containerMap.get(newContainerId);
                        if (CollectionUtils.isNotEmpty(containerCodeList)) {
                            hmePumpPreSelectionVO5.setNewContainerCode(containerCodeList.get(0));
                        }
                    }
                }
                //??????????????????
                StringBuilder groupkeyBuilder = new StringBuilder();
                groupkeyBuilder.append(hmePumpPreSelectionVO5.getPumpPreSelectionId());
                groupkeyBuilder.append("#");
                groupkeyBuilder.append(hmePumpPreSelectionVO5.getSelectionOrder());
                String groupkey = groupkeyBuilder.toString();
                Long groupNum = groupMap.get(groupkey);
                if (Objects.isNull(groupNum)) {
                    maxGroupNum++;
                    hmePumpPreSelectionVO5.setGroupNum(maxGroupNum);
                    groupMap.put(groupkey, maxGroupNum);
                } else {
                    hmePumpPreSelectionVO5.setGroupNum(groupNum);
                }
            }
        }
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pumpPreSelectionRecall(Long tenantId, List<HmePumpPreSelectionVO5> recallList) {
        if (CollectionUtils.isNotEmpty(recallList)) {
            //??????????????????????????????????????????????????????????????????
            List<String> pumpSelectionDetailsIdList = recallList.stream().map(HmePumpPreSelectionVO5::getPumpSelectionDetailsId).distinct().collect(Collectors.toList());
            Long existCount = hmePumpPreSelectionMapper.getCountBySelectionDetailsId(tenantId, pumpSelectionDetailsIdList);
            if(existCount != recallList.size()){
                //?????????????????????????????????,???????????????
                throw new MtException("HME_PUMP_SELECTION_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_033", "HME"));
            }
            // ??????????????????
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            //????????????????????????????????????????????????
            hmePumpPreSelectionMapper.batchDeletePumpSelectionDetailsByPrimary(tenantId, pumpSelectionDetailsIdList);
            //?????????????????????????????????SetsNum???status
            List<String> pumpPreSelectionIdList = recallList.stream().map(HmePumpPreSelectionVO5::getPumpPreSelectionId).distinct().collect(Collectors.toList());
            List<HmePumpPreSelection> hmePumpPreSelectionList = hmePumpPreSelectionRepository.selectByCondition(Condition.builder(HmePumpPreSelection.class)
                    .andWhere(Sqls.custom()
                            .andIn(HmePumpPreSelection.FIELD_PUMP_PRE_SELECTION_ID, pumpPreSelectionIdList)
                            .andEqualTo(HmePumpSelectionDetails.FIELD_TENANT_ID, tenantId))
                    .build());
            List<HmePumpSelectionDetails> hmePumpSelectionDetailList = hmePumpSelectionDetailsRepository.selectByCondition(Condition.builder(HmePumpSelectionDetails.class)
                    .select(HmePumpSelectionDetails.FIELD_PUMP_SELECTION_DETAILS_ID,
                            HmePumpSelectionDetails.FIELD_PUMP_PRE_SELECTION_ID,
                            HmePumpSelectionDetails.FIELD_NEW_CONTAINER_ID)
                    .andWhere(Sqls.custom()
                            .andIn(HmePumpSelectionDetails.FIELD_PUMP_PRE_SELECTION_ID, pumpPreSelectionIdList)
                            .andEqualTo(HmePumpSelectionDetails.FIELD_TENANT_ID, tenantId))
                    .build());
            Map<String, List<Long>> map = recallList.stream().collect(Collectors.groupingBy(HmePumpPreSelectionVO5::getPumpPreSelectionId,
                    Collectors.mapping(HmePumpPreSelectionVO5::getSelectionOrder, Collectors.toList())));
            Date nowDate = new Date();
            List<String> cidS = mtCustomDbRepository.getNextKeys("hme_pump_pre_selection_cid_s", hmePumpPreSelectionList.size());
            int count = 0;
            List<String> updateSqlList = new ArrayList<>();
            for (HmePumpPreSelection hmePumpPreSelection : hmePumpPreSelectionList) {
                //??????SetsNum ?????????SetsNum - ????????????????????????????????????selectionOrder?????????
                List<Long> selectionOrderList = map.get(hmePumpPreSelection.getPumpPreSelectionId());
                selectionOrderList = selectionOrderList.stream().distinct().collect(Collectors.toList());
                hmePumpPreSelection.setSetsNum(hmePumpPreSelection.getSetsNum() - selectionOrderList.size());
                //??????status
                if (CollectionUtils.isEmpty(hmePumpSelectionDetailList)) {
                    hmePumpPreSelection.setStatus("UNSELECTED");
                } else {
                    //????????????????????????????????????????????????????????????????????????UNSELECTED
                    List<HmePumpSelectionDetails> singleSelectionDetailList = hmePumpSelectionDetailList.stream().filter(item -> hmePumpPreSelection.getPumpPreSelectionId().equals(item.getPumpPreSelectionId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(singleSelectionDetailList)) {
                        hmePumpPreSelection.setStatus("UNSELECTED");
                    } else {
                        singleSelectionDetailList = singleSelectionDetailList.stream().filter(item -> StringUtils.isBlank(item.getNewContainerId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(singleSelectionDetailList)) {
                            //??????????????????????????????????????????????????????????????????????????????????????????????????????LOADING
                            hmePumpPreSelection.setStatus("LOADING");
                        } else {
                            //?????????????????????????????????????????????????????????????????????????????????????????????????????????LOADED
                            hmePumpPreSelection.setStatus("LOADED");
                        }
                    }
                }
                hmePumpPreSelection.setLastUpdatedBy(userId);
                hmePumpPreSelection.setLastUpdateDate(nowDate);
                hmePumpPreSelection.setCid(Long.valueOf(cidS.get(count)));
                updateSqlList.addAll(mtCustomDbRepository.getUpdateSql(hmePumpPreSelection));
                count++;
            }
            if (CollectionUtils.isNotEmpty(updateSqlList)) {
                jdbcTemplate.batchUpdate(updateSqlList.toArray(new String[updateSqlList.size()]));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pumpPreSelectionConfirm(Long tenantId, HmePumpPreSelectionDTO4 dto) {
        //????????????
        HmePumpPreSelectionVO6 hmePumpPreSelectionVO6 = hmePumpPreSelectionRepository.pumpPreSelectionConfirmBarcodeVerify(tenantId, dto);
        List<HmePumpPreSelectionVO4> containerUnloadList = hmePumpPreSelectionVO6.getContainerUnloadList();
        List<String> materialLotIdList = hmePumpPreSelectionVO6.getMaterialLotIdList();
        //????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("PUMP_SELECTION");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getDefaultStorageLocatorId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //???????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(containerUnloadList)) {
            Map<String, List<String>> materialLotContainerMap = containerUnloadList.stream().collect(
                    Collectors.groupingBy(HmePumpPreSelectionVO4::getContainerId,
                            Collectors.mapping(HmePumpPreSelectionVO4::getMaterialLotId, Collectors.toList())));
            for (Map.Entry<String, List<String>> entry : materialLotContainerMap.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                MtContLoadDtlVO30 mtContLoadDtlVO30 = new MtContLoadDtlVO30();
                mtContLoadDtlVO30.setContainerId(key);
                mtContLoadDtlVO30.setWorkcellId(dto.getWorkcellId());
                mtContLoadDtlVO30.setParentEventId(eventId);
                List<MtContLoadDtlVO29> unLoadObjectList = new ArrayList<>();
                for (String materialLotId : value) {
                    MtContLoadDtlVO29 mtContLoadDtlVO29 = new MtContLoadDtlVO29();
                    mtContLoadDtlVO29.setLoadObjectType("MATERIAL_LOT");
                    mtContLoadDtlVO29.setLoadObjectId(materialLotId);
                    unLoadObjectList.add(mtContLoadDtlVO29);
                }
                mtContLoadDtlVO30.setUnLoadObjectList(unLoadObjectList);
                mtContainerRepository.containerBatchUnload(tenantId, mtContLoadDtlVO30);
            }
        }
        //??????????????????
        MtContainer mtContainer = hmePumpPreSelectionRepository.pumpPreSelectionConfirmContainerVerify(tenantId, dto, materialLotIdList);
        //??????????????????????????????????????????
        List<HmePumpSelectionDetails> hmePumpSelectionDetailList = hmePumpSelectionDetailsRepository.selectByCondition(Condition.builder(HmePumpSelectionDetails.class)
                .andWhere(Sqls.custom()
                        .andIn(HmePumpSelectionDetails.FIELD_MATERIAL_LOT_ID, materialLotIdList)
                        .andEqualTo(HmePumpSelectionDetails.FIELD_TENANT_ID, tenantId))
                .build());
        if (CollectionUtils.isEmpty(hmePumpSelectionDetailList)) {
            return;
        }
        if (hmePumpSelectionDetailList.size() != materialLotIdList.size()) {
            throw new MtException("HME_PUMP_SELECTION_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_014", "HME"));
        }
        //??????????????????????????????
        MtContainerVO30 mtContainerVO30 = new MtContainerVO30();
        mtContainerVO30.setParentEventId(eventId);
        mtContainerVO30.setWorkcellId(dto.getWorkcellId());
        List<MtContainerVO31> containerLoadList = new ArrayList<>();
        for (String materialLotId : materialLotIdList) {
            MtContainerVO31 mtContainerVO31 = new MtContainerVO31();
            mtContainerVO31.setContainerId(mtContainer.getContainerId());
            mtContainerVO31.setLoadObjectType("MATERIAL_LOT");
            mtContainerVO31.setLoadObjectId(materialLotId);
            containerLoadList.add(mtContainerVO31);
        }
        mtContainerVO30.setContainerLoadList(containerLoadList);
        mtContainerRepository.containerBatchLoad(tenantId, mtContainerVO30);
        //??????????????????ID?????????????????????????????????????????????????????????ID
        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        Date nowDate = new Date();
        List<String> cidS = mtCustomDbRepository.getNextKeys("hme_pump_selection_details_cid_s", hmePumpSelectionDetailList.size());
        int count = 0;
        List<String> updateSqlList = new ArrayList<>();
        for (HmePumpSelectionDetails hmePumpSelectionDetails : hmePumpSelectionDetailList) {
            hmePumpSelectionDetails.setNewContainerId(mtContainer.getContainerId());
            hmePumpSelectionDetails.setPackedBy(userId);
            hmePumpSelectionDetails.setPackedDate(nowDate);
            hmePumpSelectionDetails.setStatus("LOADED");
            hmePumpSelectionDetails.setLastUpdatedBy(userId);
            hmePumpSelectionDetails.setLastUpdateDate(nowDate);
            hmePumpSelectionDetails.setCid(Long.valueOf(cidS.get(count)));
            updateSqlList.addAll(mtCustomDbRepository.getUpdateSql(hmePumpSelectionDetails));
            count++;
        }
        if (CollectionUtils.isNotEmpty(updateSqlList)) {
            jdbcTemplate.batchUpdate(updateSqlList.toArray(new String[updateSqlList.size()]));
        }
        //???????????????????????????????????????
        HmePumpPreSelection hmePumpPreSelection = hmePumpPreSelectionRepository.selectByPrimaryKey(hmePumpSelectionDetailList.get(0).getPumpPreSelectionId());
        List<HmePumpSelectionDetails> pumpSelectionDetailList = hmePumpSelectionDetailsRepository.selectByCondition(Condition.builder(HmePumpSelectionDetails.class)
                .select(HmePumpSelectionDetails.FIELD_NEW_CONTAINER_ID)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmePumpSelectionDetails.FIELD_PUMP_PRE_SELECTION_ID, hmePumpPreSelection.getPumpPreSelectionId())
                        .andEqualTo(HmePumpSelectionDetails.FIELD_TENANT_ID, tenantId))
                .build());
        pumpSelectionDetailList = pumpSelectionDetailList.stream().filter(item -> StringUtils.isBlank(item.getNewContainerId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(pumpSelectionDetailList)) {
            hmePumpPreSelection.setStatus("LOADING");
        } else {
            hmePumpPreSelection.setStatus("LOADED");
        }
        hmePumpPreSelectionMapper.updateByPrimaryKeySelective(hmePumpPreSelection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmePumpPreSelectionVO15 pumPreSelection(Long tenantId, HmePumpPreSelectionDTO5 dto) {
        //????????????
        pumPreSelectionVerify(tenantId, dto);
        //?????????????????????????????????????????????????????????
        HmePumpPreSelectionVO13 hmePumpPreSelectionVO13 = hmePumpPreSelectionRepository.pumpFilterRuleLineQuery(tenantId, dto.getMaterialId());
        List<HmePumpPreSelectionVO7> pumpFilterRuleLineList = hmePumpPreSelectionVO13.getPumpFilterRuleLineList();
        Map<String, String> letterTagIdMap = hmePumpPreSelectionVO13.getLetterTagIdMap();
        HmePumpPreSelectionVO7 wpeLine = hmePumpPreSelectionVO13.getWpeLine();
        BigDecimal qty = pumpFilterRuleLineList.get(0).getQty();
        long qtyLong = qty.longValue();
        //???????????????????????????
        List<String> materialLotIdList = dto.getPumpMaterialLotInfoList().stream().map(HmePumpPreSelectionVO4::getMaterialLotId).distinct().collect(Collectors.toList());
        if (materialLotIdList.size() < qtyLong) {
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????,????????????
            throw new MtException("HME_PUMP_SELECTION_029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_029", "HME"));
        }
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        HmePumpPreSelectionVO14 hmePumpPreSelectionVO14 = hmePumpPreSelectionRepository.getAllMaterialByMainMaterial(tenantId, dto);
        String bomId = hmePumpPreSelectionVO14.getBomId();
        List<String> materialIdList = hmePumpPreSelectionVO14.getMaterialId();
        //??????????????????????????????????????????????????????
        materialLotIdList = hmePumpPreSelectionMapper.getMaterialLotByMaterialLotAndMaterial(tenantId, materialLotIdList, materialIdList);
        if (CollectionUtils.isEmpty(materialLotIdList) || materialLotIdList.size() < qtyLong) {
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            throw new MtException("HME_PUMP_SELECTION_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_017", "HME"));
        }
        //???????????????????????????????????????????????????,?????????????????????????????????????????????????????????????????????
        HmePumpPreSelectionVO8 hmePumpPreSelectionVO8 = hmePumpPreSelectionRepository.eoJobDataRecordVerify(tenantId, materialLotIdList, pumpFilterRuleLineList, qtyLong);
        materialLotIdList = hmePumpPreSelectionVO8.getMaterialLotIdList();
        Map<String, List<HmePumpPreSelectionVO9>> materialLotJobDataRecordMap = hmePumpPreSelectionVO8.getMaterialLotJobDataRecordMap();
        List<HmePumpPreSelectionVO11> materialLotTagResultList = hmePumpPreSelectionVO8.getMaterialLotTagResultList();
        //??????????????????????????????SINGLE???MULTIPLE??????????????????????????????????????????
        HmePumpPreSelectionVO8 hmePumpPreSelectionVO82 = hmePumpPreSelectionRepository.multipleSinglePumpFilterRuleLineVerify(tenantId, materialLotIdList, pumpFilterRuleLineList, materialLotJobDataRecordMap, qtyLong);
        materialLotIdList = hmePumpPreSelectionVO82.getMaterialLotIdList();
        List<List<String>> materialLotGroupList = hmePumpPreSelectionVO82.getMaterialLotGroupList();

        //materialLotTagResultList??????????????????????????????????????????????????????
        List<String> nowMaterialLotIdList = materialLotIdList;
        materialLotTagResultList.removeIf(item -> !nowMaterialLotIdList.contains(item.getMaterialLotId()));

        List<HmePumpPreSelectionVO7> sumList = pumpFilterRuleLineList.stream().filter(item -> "SUM".equals(item.getCalculateType())).collect(Collectors.toList());
        //???SUM??????????????????????????????Sequence????????????????????????????????????
        sumList = sumList.stream().sorted(Comparator.comparing(HmePumpPreSelectionVO7::getSequence)).collect(Collectors.toList());
        HmePumpPreSelectionVO7 sequenceMinSumLine = sumList.get(0);
        //finalMaterialLotList ?????????????????????????????????????????????
        List<List<String>> finalMaterialLotList = new ArrayList<>();
        if ("ASCEND".equals(sequenceMinSumLine.getPriority())) {
            //Priority??????ASCEND??????????????????????????????
            finalMaterialLotList = hmePumpPreSelectionRepository.pumpFilterRuleSumLineAscend(tenantId, materialLotIdList, sumList,
                    qty, qtyLong, materialLotTagResultList, materialLotGroupList, letterTagIdMap, wpeLine);
        } else if ("DESCEND".equals(sequenceMinSumLine.getPriority())) {
            //Priority??????DESCEND??????????????????????????????
            finalMaterialLotList = hmePumpPreSelectionRepository.pumpFilterRuleSumLineDescend(tenantId, materialLotIdList, sumList,
                    qty, qtyLong, materialLotTagResultList, materialLotGroupList, letterTagIdMap, wpeLine);
        }
        if(CollectionUtils.isEmpty(finalMaterialLotList)){
            //???????????????????????????????????????,?????????????????????
            throw new MtException("HME_PUMP_SELECTION_031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_031", "HME"));
        }
        //????????????????????????????????????
        HmePumpPreSelectionVO15 result = hmePumpPreSelectionRepository.insertPumpPreSelectionData(tenantId, dto, finalMaterialLotList, bomId, pumpFilterRuleLineList.get(0).getRuleHeadId(), qtyLong);
        return result;
    }

    @Override
    public HmePumpPreSelectionVO3 scanSelectionLot(Long tenantId, HmePumpPreSelectionDTO6 dto) {
        //????????????
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????"));
        }
        if (StringUtils.isBlank(dto.getPumpPreSelectionId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        String defaultStorageLocatorId = null;
        //????????????????????????
        String lineWorkcellId = hmePumpPreSelectionMapper.getLineWorkcellIdByWorkcell(tenantId, dto.getWorkcellId());
        if (StringUtils.isNotBlank(lineWorkcellId)) {
            //????????????????????????????????????
            List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, lineWorkcellId);
            if (CollectionUtils.isNotEmpty(defaultStorageLocatorIdList)) {
                if (defaultStorageLocatorIdList.size() > 1) {
                    //????????????????????????????????????,?????????{??????????????????????????????????????????????????????}
                    throw new MtException("HME_EO_JOB_SN_209", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_209", "HME"));
                }
                defaultStorageLocatorId = defaultStorageLocatorIdList.get(0);
            }
        }
        if (StringUtils.isBlank(defaultStorageLocatorId)) {
            //?????????????????????????????????,?????????{????????????????????????????????????????????????????????????}
            throw new MtException("HME_EO_JOB_SN_092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_092", "HME"));
        }
        List<HmePumpPreSelectionVO4> pumpMaterialLotInfoList = hmePumpPreSelectionMapper.newPumpSelectionDetailsQueryBySelectionLot(tenantId, dto.getPumpPreSelectionId());
        HmePumpPreSelectionVO3 result = new HmePumpPreSelectionVO3();
        result.setDefaultStorageLocatorId(defaultStorageLocatorId);
        result.setPumpMaterialLotInfoList(pumpMaterialLotInfoList);
        return result;
    }

    @Override
    public HmePumpPreSelectionVO16 setsNumQueryBySelectionLot(Long tenantId, String selectionLot) {
        HmePumpPreSelectionVO16 hmePumpPreSelectionVO16 = new HmePumpPreSelectionVO16();
        //??????????????????????????????
        HmePumpPreSelection hmePumpPreSelection = hmePumpPreSelectionMapper.getSetsNumBySelectionLot(tenantId, selectionLot);
        hmePumpPreSelectionVO16.setSetsNum(hmePumpPreSelection.getSetsNum());
        //???????????????????????????????????????
        List<String> selectionOrderList = hmePumpPreSelectionMapper.getAlreadySetsNumBySelectionLot(tenantId, hmePumpPreSelection.getPumpPreSelectionId());
        if(CollectionUtils.isNotEmpty(selectionOrderList)){
            selectionOrderList = selectionOrderList.stream().distinct().collect(Collectors.toList());
            hmePumpPreSelectionVO16.setAlreadySetsNum(Long.valueOf(selectionOrderList.size()));
        }else {
            hmePumpPreSelectionVO16.setAlreadySetsNum(0L);
        }
        //?????????????????????????????????
        hmePumpPreSelectionVO16.setNoSetsNum(hmePumpPreSelectionVO16.getSetsNum() - hmePumpPreSelectionVO16.getAlreadySetsNum());
        return hmePumpPreSelectionVO16;
    }

    void pumPreSelectionVerify(Long tenantId, HmePumpPreSelectionDTO5 dto) {
        //????????????
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????ID"));
        }
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????ID"));
        }
        if (StringUtils.isBlank(dto.getMaterialId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????ID"));
        }
        if (StringUtils.isBlank(dto.getMaterialCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        if (StringUtils.isBlank(dto.getRevision())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????"));
        }
        if (CollectionUtils.isEmpty(dto.getPumpMaterialLotInfoList())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "???????????????"));
        }
    }
}
