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
 * 泵浦源预筛选基础表应用服务默认实现
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
        //必输校验
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if (StringUtils.isBlank(dto.getScanCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "扫描编码"));
        }
        if (Objects.isNull(dto.getContainerQty())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "容器数"));
        }
        if (Objects.isNull(dto.getPumpQty())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "泵浦源数"));
        }
        String defaultStorageLocatorId = null;
        //根据工位查询工段
        String lineWorkcellId = hmePumpPreSelectionMapper.getLineWorkcellIdByWorkcell(tenantId, dto.getWorkcellId());
        if (StringUtils.isNotBlank(lineWorkcellId)) {
            //根据工段查询默认存储库位
            List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, lineWorkcellId);
            if (CollectionUtils.isNotEmpty(defaultStorageLocatorIdList)) {
                if (defaultStorageLocatorIdList.size() > 1) {
                    //如果找到多个默认存储库位,则报错{当前工位的工段下存在多个默认存储库位}
                    throw new MtException("HME_EO_JOB_SN_209", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_209", "HME"));
                }
                defaultStorageLocatorId = defaultStorageLocatorIdList.get(0);
            }
        }
        if (StringUtils.isBlank(defaultStorageLocatorId)) {
            //如果未找到默认存储库位,则报错{当前工位的工段或产线下未维护默认存储库位}
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
            //如果扫描的是容器，根据容器获取物料批
            materialLotIdList = hmePumpPreSelectionRepository.getMaterialLotByContainer(tenantId, mtContainer, defaultStorageLocatorId);
        } else {
            //如果扫描的是条码
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto.getScanCode());
            }});
            if (Objects.nonNull(mtMaterialLot)) {
                materialLotIdList.add(mtMaterialLot.getMaterialLotId());
            }
        }
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            //如果扫描的既不是容器又不是条码，则报错扫描值【${1}】不为容器或者物料批,请检查
            throw new MtException("HME_PUMP_SELECTION_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_006", "HME", dto.getScanCode()));
        }
        //获取条码相关信息，并对条码进行校验
        List<HmePumpPreSelectionVO4> materialLotInfoList = hmePumpPreSelectionMapper.materialLotInfoQuery(tenantId, materialLotIdList);
        for (HmePumpPreSelectionVO4 hmePumpPreSelectionVO4 : materialLotInfoList) {
            //条码有效性校验
            if (!HmeConstants.ConstantValue.YES.equals(hmePumpPreSelectionVO4.getEnableFlag())) {
                throw new MtException(StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_003" : "MT_MATERIAL_TFR_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_003" : "MT_MATERIAL_TFR_0003", "HME", hmePumpPreSelectionVO4.getMaterialLotCode()));
            }
            //条码质量状态校验
            if (!HmeConstants.ConstantValue.OK.equals(hmePumpPreSelectionVO4.getQualityStatus())) {
                throw new MtException(StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_002" : "RK_INVENTORY_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_002" : "RK_INVENTORY_0028", StringUtils.isNotBlank(containerCode) ? "HME" : "INVENTORY", StringUtils.isNotBlank(containerCode) ? hmePumpPreSelectionVO4.getMaterialLotCode() : ""));
            }
            //条码货位校验
            if (!hmePumpPreSelectionVO4.getLocatorId().equals(defaultStorageLocatorId)) {
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(hmePumpPreSelectionVO4.getLocatorId());
                throw new MtException("HME_PUMP_SELECTION_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_008", "HME", mtModLocator.getLocatorCode()));
            }
        }
        //条码是否已筛选校验
        String errorMaterialLotCode = hmePumpPreSelectionMapper.materialLotLoadedQuery(tenantId, materialLotIdList);
        if (StringUtils.isNotBlank(errorMaterialLotCode)) {
            throw new MtException(StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_004" : "HME_PUMP_SELECTION_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    StringUtils.isNotBlank(containerCode) ? "HME_PUMP_SELECTION_004" : "HME_PUMP_SELECTION_005", "HME", errorMaterialLotCode));
        }
        //封装返回结果
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
        //目标容器存在校验
        if (Objects.isNull(mtContainer)) {
            throw new MtException("HME_PUMP_SELECTION_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_009", "HME", containerCode));
        }
        //容器可用性校验
        mtContainerRepository.containerAvailableValidate(tenantId, mtContainer.getContainerId());
        //容器装载验证
        MtContainerVO9 mtContainerVO9 = new MtContainerVO9();
        mtContainerVO9.setContainerId(mtContainer.getContainerId());
        mtContainerVO9.setLoadObjectType("MATERIAL_LOT");
        mtContainerRepository.containerLoadVerify(tenantId, mtContainerVO9);
        //容器是否装载校验
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
        //参数转换
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
            //新旧容器编码查询
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
                //新旧容器编码赋值
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
                //分组编号赋值
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
            //校验前台传入的撤回数据中是否存在已撤回的数据
            List<String> pumpSelectionDetailsIdList = recallList.stream().map(HmePumpPreSelectionVO5::getPumpSelectionDetailsId).distinct().collect(Collectors.toList());
            Long existCount = hmePumpPreSelectionMapper.getCountBySelectionDetailsId(tenantId, pumpSelectionDetailsIdList);
            if(existCount != recallList.size()){
                //存在勾选物料批已经撤回,请重新查询
                throw new MtException("HME_PUMP_SELECTION_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_033", "HME"));
            }
            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            //根据主键批量删除泵浦源预筛选明细
            hmePumpPreSelectionMapper.batchDeletePumpSelectionDetailsByPrimary(tenantId, pumpSelectionDetailsIdList);
            //更新泵浦源预筛选头表的SetsNum和status
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
                //更新SetsNum 当前的SetsNum - 筛选头所对应的要删除行的selectionOrder的个数
                List<Long> selectionOrderList = map.get(hmePumpPreSelection.getPumpPreSelectionId());
                selectionOrderList = selectionOrderList.stream().distinct().collect(Collectors.toList());
                hmePumpPreSelection.setSetsNum(hmePumpPreSelection.getSetsNum() - selectionOrderList.size());
                //更新status
                if (CollectionUtils.isEmpty(hmePumpSelectionDetailList)) {
                    hmePumpPreSelection.setStatus("UNSELECTED");
                } else {
                    //如果删除后，该筛选头下不存在行数据，则状态更新为UNSELECTED
                    List<HmePumpSelectionDetails> singleSelectionDetailList = hmePumpSelectionDetailList.stream().filter(item -> hmePumpPreSelection.getPumpPreSelectionId().equals(item.getPumpPreSelectionId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(singleSelectionDetailList)) {
                        hmePumpPreSelection.setStatus("UNSELECTED");
                    } else {
                        singleSelectionDetailList = singleSelectionDetailList.stream().filter(item -> StringUtils.isBlank(item.getNewContainerId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(singleSelectionDetailList)) {
                            //如果删除后，该筛选头下存在行数据，且存在新容器为空的行，则状态更新为LOADING
                            hmePumpPreSelection.setStatus("LOADING");
                        } else {
                            //如果删除后，该筛选头下存在行数据，且不存在新容器为空的行，则状态更新为LOADED
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
        //校验条码
        HmePumpPreSelectionVO6 hmePumpPreSelectionVO6 = hmePumpPreSelectionRepository.pumpPreSelectionConfirmBarcodeVerify(tenantId, dto);
        List<HmePumpPreSelectionVO4> containerUnloadList = hmePumpPreSelectionVO6.getContainerUnloadList();
        List<String> materialLotIdList = hmePumpPreSelectionVO6.getMaterialLotIdList();
        //生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("PUMP_SELECTION");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getDefaultStorageLocatorId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //如果有条码已装载容器，则先卸载容器
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
        //校验目标容器
        MtContainer mtContainer = hmePumpPreSelectionRepository.pumpPreSelectionConfirmContainerVerify(tenantId, dto, materialLotIdList);
        //勾选条码与筛选结果条码不匹配
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
        //目标容器批量装载条码
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
        //根据扫描条码ID查询泵浦源预筛选明细数据，去更新新容器ID
        // 获取当前用户
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
        //更新泵浦源预筛选头表的状态
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
        //必输校验
        pumPreSelectionVerify(tenantId, dto);
        //根据前端传入的物料查询有效的筛选规则行
        HmePumpPreSelectionVO13 hmePumpPreSelectionVO13 = hmePumpPreSelectionRepository.pumpFilterRuleLineQuery(tenantId, dto.getMaterialId());
        List<HmePumpPreSelectionVO7> pumpFilterRuleLineList = hmePumpPreSelectionVO13.getPumpFilterRuleLineList();
        Map<String, String> letterTagIdMap = hmePumpPreSelectionVO13.getLetterTagIdMap();
        HmePumpPreSelectionVO7 wpeLine = hmePumpPreSelectionVO13.getWpeLine();
        BigDecimal qty = pumpFilterRuleLineList.get(0).getQty();
        long qtyLong = qty.longValue();
        //筛选池中的条码信息
        List<String> materialLotIdList = dto.getPumpMaterialLotInfoList().stream().map(HmePumpPreSelectionVO4::getMaterialLotId).distinct().collect(Collectors.toList());
        if (materialLotIdList.size() < qtyLong) {
            //如果筛选池中的条码个数小于筛选规则头的泵浦源个数，则报错可筛选条码数不足,无法筛选
            throw new MtException("HME_PUMP_SELECTION_029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_029", "HME"));
        }
        //根据主物料查询组件物料及其全局替代料，得到二者合一之后去重的总物料集合
        HmePumpPreSelectionVO14 hmePumpPreSelectionVO14 = hmePumpPreSelectionRepository.getAllMaterialByMainMaterial(tenantId, dto);
        String bomId = hmePumpPreSelectionVO14.getBomId();
        List<String> materialIdList = hmePumpPreSelectionVO14.getMaterialId();
        //对筛选池中的条码在物料维度上进行筛选
        materialLotIdList = hmePumpPreSelectionMapper.getMaterialLotByMaterialLotAndMaterial(tenantId, materialLotIdList, materialIdList);
        if (CollectionUtils.isEmpty(materialLotIdList) || materialLotIdList.size() < qtyLong) {
            //如果根据物料筛选后，筛选池中无条码则报错符合装配清单组件物料的条码数不足，本次筛选无结果
            throw new MtException("HME_PUMP_SELECTION_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_017", "HME"));
        }
        //对筛选池中的条码进行数据采集项校验,找到符合筛选规则行要求的条码进而组成新的筛选池
        HmePumpPreSelectionVO8 hmePumpPreSelectionVO8 = hmePumpPreSelectionRepository.eoJobDataRecordVerify(tenantId, materialLotIdList, pumpFilterRuleLineList, qtyLong);
        materialLotIdList = hmePumpPreSelectionVO8.getMaterialLotIdList();
        Map<String, List<HmePumpPreSelectionVO9>> materialLotJobDataRecordMap = hmePumpPreSelectionVO8.getMaterialLotJobDataRecordMap();
        List<HmePumpPreSelectionVO11> materialLotTagResultList = hmePumpPreSelectionVO8.getMaterialLotTagResultList();
        //筛选池中去除掉不符合SINGLE、MULTIPLE的数据采集项上限、下限的条码
        HmePumpPreSelectionVO8 hmePumpPreSelectionVO82 = hmePumpPreSelectionRepository.multipleSinglePumpFilterRuleLineVerify(tenantId, materialLotIdList, pumpFilterRuleLineList, materialLotJobDataRecordMap, qtyLong);
        materialLotIdList = hmePumpPreSelectionVO82.getMaterialLotIdList();
        List<List<String>> materialLotGroupList = hmePumpPreSelectionVO82.getMaterialLotGroupList();

        //materialLotTagResultList集合中去除掉已经筛选掉的条码数据信息
        List<String> nowMaterialLotIdList = materialLotIdList;
        materialLotTagResultList.removeIf(item -> !nowMaterialLotIdList.contains(item.getMaterialLotId()));

        List<HmePumpPreSelectionVO7> sumList = pumpFilterRuleLineList.stream().filter(item -> "SUM".equals(item.getCalculateType())).collect(Collectors.toList());
        //将SUM类型的筛选规则行按照Sequence从小到大排序，取最小的行
        sumList = sumList.stream().sorted(Comparator.comparing(HmePumpPreSelectionVO7::getSequence)).collect(Collectors.toList());
        HmePumpPreSelectionVO7 sequenceMinSumLine = sumList.get(0);
        //finalMaterialLotList 存储的是最终挑选出来的条码组合
        List<List<String>> finalMaterialLotList = new ArrayList<>();
        if ("ASCEND".equals(sequenceMinSumLine.getPriority())) {
            //Priority等于ASCEND代表从小到大优先消耗
            finalMaterialLotList = hmePumpPreSelectionRepository.pumpFilterRuleSumLineAscend(tenantId, materialLotIdList, sumList,
                    qty, qtyLong, materialLotTagResultList, materialLotGroupList, letterTagIdMap, wpeLine);
        } else if ("DESCEND".equals(sequenceMinSumLine.getPriority())) {
            //Priority等于DESCEND代表从大到小优先消耗
            finalMaterialLotList = hmePumpPreSelectionRepository.pumpFilterRuleSumLineDescend(tenantId, materialLotIdList, sumList,
                    qty, qtyLong, materialLotTagResultList, materialLotGroupList, letterTagIdMap, wpeLine);
        }
        if(CollectionUtils.isEmpty(finalMaterialLotList)){
            //报错无符合要求的泵浦源组合,本次筛选无结果
            throw new MtException("HME_PUMP_SELECTION_031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_031", "HME"));
        }
        //插入头行数据，并返回结果
        HmePumpPreSelectionVO15 result = hmePumpPreSelectionRepository.insertPumpPreSelectionData(tenantId, dto, finalMaterialLotList, bomId, pumpFilterRuleLineList.get(0).getRuleHeadId(), qtyLong);
        return result;
    }

    @Override
    public HmePumpPreSelectionVO3 scanSelectionLot(Long tenantId, HmePumpPreSelectionDTO6 dto) {
        //必输校验
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if (StringUtils.isBlank(dto.getPumpPreSelectionId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "筛选批次"));
        }
        String defaultStorageLocatorId = null;
        //根据工位查询工段
        String lineWorkcellId = hmePumpPreSelectionMapper.getLineWorkcellIdByWorkcell(tenantId, dto.getWorkcellId());
        if (StringUtils.isNotBlank(lineWorkcellId)) {
            //根据工段查询默认存储库位
            List<String> defaultStorageLocatorIdList = hmeCosPatchPdaMapper.defaultStorageLocatorQuery(tenantId, lineWorkcellId);
            if (CollectionUtils.isNotEmpty(defaultStorageLocatorIdList)) {
                if (defaultStorageLocatorIdList.size() > 1) {
                    //如果找到多个默认存储库位,则报错{当前工位的工段下存在多个默认存储库位}
                    throw new MtException("HME_EO_JOB_SN_209", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_209", "HME"));
                }
                defaultStorageLocatorId = defaultStorageLocatorIdList.get(0);
            }
        }
        if (StringUtils.isBlank(defaultStorageLocatorId)) {
            //如果未找到默认存储库位,则报错{当前工位的工段或产线下未维护默认存储库位}
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
        //根据筛选批次查询套数
        HmePumpPreSelection hmePumpPreSelection = hmePumpPreSelectionMapper.getSetsNumBySelectionLot(tenantId, selectionLot);
        hmePumpPreSelectionVO16.setSetsNum(hmePumpPreSelection.getSetsNum());
        //根据筛选批次查询已挑选套数
        List<String> selectionOrderList = hmePumpPreSelectionMapper.getAlreadySetsNumBySelectionLot(tenantId, hmePumpPreSelection.getPumpPreSelectionId());
        if(CollectionUtils.isNotEmpty(selectionOrderList)){
            selectionOrderList = selectionOrderList.stream().distinct().collect(Collectors.toList());
            hmePumpPreSelectionVO16.setAlreadySetsNum(Long.valueOf(selectionOrderList.size()));
        }else {
            hmePumpPreSelectionVO16.setAlreadySetsNum(0L);
        }
        //两者相减得到未挑选套数
        hmePumpPreSelectionVO16.setNoSetsNum(hmePumpPreSelectionVO16.getSetsNum() - hmePumpPreSelectionVO16.getAlreadySetsNum());
        return hmePumpPreSelectionVO16;
    }

    void pumPreSelectionVerify(Long tenantId, HmePumpPreSelectionDTO5 dto) {
        //必输校验
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点ID"));
        }
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位ID"));
        }
        if (StringUtils.isBlank(dto.getMaterialId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "物料ID"));
        }
        if (StringUtils.isBlank(dto.getMaterialCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "物料编码"));
        }
        if (StringUtils.isBlank(dto.getRevision())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "版本号"));
        }
        if (CollectionUtils.isEmpty(dto.getPumpMaterialLotInfoList())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "筛选池条码"));
        }
    }
}
