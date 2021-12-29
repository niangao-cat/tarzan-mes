package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeFreezeDocQueryDTO;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeFreezeDocRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeFreezeDocLineMapper;
import com.ruike.hme.infra.mapper.HmeFreezeDocMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Component;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeType.COS_INVENTORY;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeType.INVENTORY;

/**
 * 条码冻结单 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:42
 */
@Component
@Slf4j
public class HmeFreezeDocRepositoryImpl extends BaseRepositoryImpl<HmeFreezeDoc> implements HmeFreezeDocRepository {
    private final HmeFreezeDocMapper mapper;
    private final HmeFreezeDocLineMapper lineMapper;
    private final HmeEoJobSnRepository eoJobSnRepository;

    public HmeFreezeDocRepositoryImpl(HmeFreezeDocMapper mapper, HmeFreezeDocLineMapper lineMapper, HmeEoJobSnRepository eoJobSnRepository) {
        this.mapper = mapper;
        this.lineMapper = lineMapper;
        this.eoJobSnRepository = eoJobSnRepository;
    }

    @Override
    @ProcessLovValue
    public Page<HmeFreezeDocVO> pageList(Long tenantId, HmeFreezeDocQueryDTO dto, PageRequest pageRequest) {
        if (StringUtils.isNotBlank(dto.getFreezeDocStatus())) {
            dto.setFreezeDocStatusList(Arrays.asList(StringUtils.split(dto.getFreezeDocStatus(), ",")));
        }
        Page<HmeFreezeDocVO> page = PageHelper.doPage(pageRequest, () -> mapper.selectRepresentationList(tenantId, dto));
        Map<String, List<MtMaterial>> materialMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            List<String> materialIdList = new ArrayList<>();
            List<MtMaterial> materialList = new ArrayList<>();
            //需要物料ID多个的情况
            for (HmeFreezeDocVO hmeFreezeDocVO : page.getContent()) {
                if (hmeFreezeDocVO.getMaterialId().contains(",")) {
                    materialIdList.addAll(Arrays.asList(hmeFreezeDocVO.getMaterialId().split(",")));
                }
            }
            if(CollectionUtils.isNotEmpty(materialIdList)){
                materialIdList = materialIdList.stream().distinct().collect(Collectors.toList());
                List<List<String>> splitList = CommonUtils.splitSqlList(materialIdList, 1000);
                for (List<String> split : splitList) {
                    materialList.addAll(mapper.materialInfoBatchQuery(tenantId, split));
                }
                materialMap = materialList.stream().collect(Collectors.groupingBy(MtMaterial::getMaterialId));
            }
        }
        if (CollectionUtils.isNotEmpty(page)) {
            //需要考虑物料ID多个的情况
            for (HmeFreezeDocVO hmeFreezeDocVO:page.getContent()) {
                if(hmeFreezeDocVO.getMaterialId().contains(",")){
                    StringBuilder materialCode = new StringBuilder();
                    StringBuilder materialName = new StringBuilder();
                    List<String> singleMaterialList = Arrays.asList(hmeFreezeDocVO.getMaterialId().split(","));
                    for (String singleMaterialId : singleMaterialList) {
                        List<MtMaterial> mtMaterials = materialMap.get(singleMaterialId);
                        if(CollectionUtils.isNotEmpty(mtMaterials)){
                            materialCode = materialCode.append(mtMaterials.get(0).getMaterialCode()).append("/");
                            materialName = materialName.append(mtMaterials.get(0).getMaterialName()).append("/");
                        }
                    }
                    hmeFreezeDocVO.setMaterialCode(materialCode.toString());
                    hmeFreezeDocVO.setMaterialName(materialName.toString());
                }
            }
            AtomicInteger seqGen = new AtomicInteger(pageRequest.getPage() * pageRequest.getSize());
            page.getContent().forEach(rec -> rec.setSequenceNum(seqGen.incrementAndGet()));
        }
        return page;
    }

    @Override
    @ProcessLovValue
    public HmeFreezeDocVO byId(Long tenantId, String freezeDocId) {
        HmeFreezeDocQueryDTO dto = new HmeFreezeDocQueryDTO();
        dto.setFreezeDocId(freezeDocId);
        List<HmeFreezeDocVO> docList = mapper.selectRepresentationList(tenantId, dto);
        if (CollectionUtils.isEmpty(docList)) {
            return new HmeFreezeDocVO();
        }
        HmeFreezeDocVO doc = docList.get(0);
        if(doc.getMaterialId().contains(",")){
            List<String> materialLotIdList = Arrays.asList(doc.getMaterialId().split(","));
            List<MtMaterial> mtMaterials = mapper.materialInfoBatchQuery(tenantId, materialLotIdList);
            if(CollectionUtils.isNotEmpty(mtMaterials)){
                List<String> materialCodeList = mtMaterials.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
                List<String> materialNameList = mtMaterials.stream().map(MtMaterial::getMaterialName).collect(Collectors.toList());
                doc.setMaterialCode(StringUtils.join(materialCodeList.toArray(), "/"));
                doc.setMaterialName(StringUtils.join(materialNameList.toArray(), "/"));
            }
        }
        doc.setSequenceNum(1);
        return doc;
    }

    @Override
    @ProcessLovValue
    public List<HmeFreezeDocCreateSnVO> snList(Long tenantId, HmeFreezeDocQueryDTO dto) {
        log.info("<====冻结解冻平台 SN查询开始啦");
        long startDate = System.currentTimeMillis();
        List<HmeMaterialLotVO> snList = new ArrayList<>();
        snList.addAll(mapper.selectMaterialLotList(tenantId, dto));
        long endDate22 = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 第一步查询条码1耗时：{}毫秒", (endDate22 - startDate));
        boolean distinctFlag = false;
        if((("COS_INVENTORY").equals(dto.getFreezeTypeTag()) || ("COS_PROCESS").equals(dto.getFreezeTypeTag()))
            && (StringUtils.isNotBlank(dto.getWafer()) || Objects.nonNull(dto.getAusnRatio())
                || StringUtils.isNotBlank(dto.getHotSinkNum()) || StringUtils.isNotBlank(dto.getCosType()))
            && StringUtils.isEmpty(dto.getVirtualNum()) && StringUtils.isEmpty(dto.getCosRuleId()) ){
            //COS类型冻结时，如果输入了wafer或金锡比或热沉编号或COS类型，但没有输入虚拟号和筛选规则时，需调用此方法。主要是为了SQL拆分
            long startDate2 = System.currentTimeMillis();
            snList.addAll(mapper.selectMaterialLotList2(tenantId, dto));
            distinctFlag = true;
            long endDate2 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 第一步查询条码2耗时：{}毫秒", (endDate2 - startDate2));
        }
        if((("COS_INVENTORY").equals(dto.getFreezeTypeTag()) || ("COS_PROCESS").equals(dto.getFreezeTypeTag()))
                && (StringUtils.isNotBlank(dto.getWafer()) || Objects.nonNull(dto.getAusnRatio())
                || StringUtils.isNotBlank(dto.getHotSinkNum()) || StringUtils.isNotBlank(dto.getCosType()))
                && StringUtils.isEmpty(dto.getVirtualNum()) && StringUtils.isEmpty(dto.getCosRuleId()) ){
            //COS类型冻结时，如果输入了wafer或金锡比或热沉编号或COS类型，但没有输入虚拟号和筛选规则时，需调用此方法。主要是为了SQL拆分
            long startDate2 = System.currentTimeMillis();
            snList.addAll(mapper.selectMaterialLotList3(tenantId, dto));
            distinctFlag = true;
            long endDate2 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 第一步查询条码3耗时：{}毫秒", (endDate2 - startDate2));
        }
        if (CollectionUtils.isEmpty(snList)) {
            return new ArrayList<>();
        }
        if(distinctFlag){
            snList = snList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(HmeMaterialLotVO::getMaterialLotId))), ArrayList::new));
        }
        //为了解决COS贴片前和贴片后冻结时，选择了工单或设备或生产线或工段或工序或工位或操作人或班次或生产时间从至时，装盒后缺失的条码情况
        if(("COS_CHIP_INVENTORY".equals(dto.getFreezeType()) || "COS_M_INVENTORY".equals(dto.getFreezeType()))
                && (StringUtils.isNotBlank(dto.getWorkOrderId()) || StringUtils.isNotBlank(dto.getEquipmentId())
                    || StringUtils.isNotBlank(dto.getProdLineId()) || StringUtils.isNotBlank(dto.getWorkcellId())
                    || StringUtils.isNotBlank(dto.getProcessId()) || StringUtils.isNotBlank(dto.getStationId())
                    || Objects.nonNull(dto.getOperatedBy()) || StringUtils.isNotBlank(dto.getShiftId())
                    || Objects.nonNull(dto.getProductionDateFrom()) || Objects.nonNull(dto.getProductionDateTo()))){
            long startDate4 = System.currentTimeMillis();
            List<String> materialLotIdList = snList.stream().map(HmeMaterialLotVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 700);
            List<String> addMaterialLotIdList = new ArrayList<>();
            int i = 1;
            for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                long startDate2 = System.currentTimeMillis();
                List<HmeSelectionDetails> hmeSelectionDetails = mapper.selectBeforeReleaseMaterialLotList(tenantId, splitMaterialLotId);
                long endDate2 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 装盒后条码缺失情况第{}次selectBeforeReleaseMaterialLotList耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate2 - startDate2));
                List<String> oldMaterialLotIdList = hmeSelectionDetails.stream().map(HmeSelectionDetails::getOldMaterialLotId).collect(Collectors.toList());
                addMaterialLotIdList.addAll(oldMaterialLotIdList);
                List<String> newMaterialLotIdList = hmeSelectionDetails.stream().map(HmeSelectionDetails::getNewMaterialLotId).collect(Collectors.toList());
                addMaterialLotIdList.addAll(newMaterialLotIdList);
                List<String> afterReleaseNewMaterialLotIdList = mapper.selectAfterReleaseMaterialLotList(tenantId, splitMaterialLotId);
                long endDate3 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 装盒后条码缺失情况第{}次selectAfterReleaseMaterialLotList：{}毫秒", splitMaterialLotId.size(), i, (endDate3 - endDate2));
                addMaterialLotIdList.addAll(afterReleaseNewMaterialLotIdList);
                i++;
            }
            if(CollectionUtils.isNotEmpty(addMaterialLotIdList)){
                addMaterialLotIdList = addMaterialLotIdList.stream().distinct().collect(Collectors.toList());
                addMaterialLotIdList.removeAll(materialLotIdList);
                if(CollectionUtils.isNotEmpty(addMaterialLotIdList)){
                    List<List<String>> splitAddMaterialLotIdList = CommonUtils.splitSqlList(addMaterialLotIdList, 1000);
                    for (List<String> splitAddMaterialLotId:splitAddMaterialLotIdList) {
                        snList.addAll(mapper.mfFlagBatchQuery(tenantId, splitAddMaterialLotId));
                    }
                }
            }
            long endDate4 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 装盒后条码缺失情况总耗时：{}毫秒", (endDate4 - startDate4));
        }

        long endDate = System.currentTimeMillis();

        // 若为COS库存冻结类型，则需要找到关联的芯片，热忱和金线条码并补充进来，进入后续处理
        if (COS_INVENTORY.equals(dto.getFreezeTypeTag())) {
            List<String> materialLotIdList = snList.stream().map(HmeMaterialLotVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 300);
            int i = 1;
            for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                //追溯芯片投料后情况下的条码
                long startDate2 = System.currentTimeMillis();
                snList.addAll(mapper.selectChipAfterReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate2 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯芯片投料后情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate2 - startDate2));
                //追溯芯片投料前情况下的条码
                snList.addAll(mapper.selectChipBeforeReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate3 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯芯片投料前情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate3 - endDate2));
                //追溯热沉投料后情况下的条码
                snList.addAll(mapper.selectHotSinkAfterReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate4 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯热沉投料后情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate4 - endDate3));
                //追溯热沉投料前情况下的条码
                snList.addAll(mapper.selectHotSinkBeforeReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate5 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯热沉投料前情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate5 - endDate4));
                //追溯金线投料后情况下的条码
                snList.addAll(mapper.selectGoldWireAfterReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate6 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯金线投料后情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate6 - endDate5));
                //追溯金线投料前情况下的条码
                snList.addAll(mapper.selectGoldWireBeforeReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate7 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯金线投料前情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate7 - endDate6));
                i++;
            }
            snList = snList.stream().distinct().collect(Collectors.toList());
            long endDate2 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 追溯芯片、热沉、金线条码总耗时：{}毫秒",  (endDate2 - endDate));
        }

        long startDate3 = System.currentTimeMillis();
        Map<String, String> snMap = snList.stream().collect(Collectors.toMap(HmeMaterialLotVO::getMaterialLotId, HmeMaterialLotVO::getMfFlag, (a, b) -> a));
        // 筛选job相关参数
        if (HmeFreezeDocQueryDTO.jobValidNeeded(dto)) {
            List<String> passedSnList = new ArrayList<>();
            List<String> materialLotIdList = snList.stream().map(HmeMaterialLotVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<List<String>> splitList = CommonUtils.splitSqlList(materialLotIdList, 1000);
            for (List<String> split:splitList) {
                long startDate6 = System.currentTimeMillis();
                passedSnList.addAll(mapper.selectJobFilteredList(tenantId, dto, split));
                long endDate6 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 查询selectJobFilteredList数据耗时：{}毫秒", (endDate6 - startDate6));
            }
            Set<String> passedSns = passedSnList.stream().collect(Collectors.toSet());
            long endDate5 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 查询selectJobFilteredList数据总耗时：{}毫秒", (endDate5 - startDate3));
            snMap.keySet().removeIf(key -> !passedSns.contains(key));
        }
        Map<String, String> invMap = snMap.entrySet().stream().filter(rec -> NO.equals(rec.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        long endDate3 = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 确定snMap和invMap数据耗时：{}毫秒", (endDate3 - startDate3));

        // 库存/COS库存 迭代查询在制条码
        if (invMap.size() > 0 && StringCommonUtils.contains(dto.getFreezeTypeTag(), INVENTORY, COS_INVENTORY)) {
            int i = 1;
            snMap = this.snIteration(tenantId, snMap, invMap, i);
            long endDate4 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 迭代查询在制条码耗时：{}毫秒", (endDate4 - endDate3));
        }
        if(snMap.isEmpty()){
            return new ArrayList<>();
        }

        // 根据物料批ID查询数据
        long startDate4 = System.currentTimeMillis();
        List<HmeFreezeDocCreateSnVO> list = new ArrayList<>();
        List<String> materialLotIdList = new ArrayList<>(snMap.keySet());
        materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
        List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 1000);
        for (List<String> splitMaterialLotId : splitMaterialLotIdList) {
            long startDate5 = System.currentTimeMillis();
            list.addAll(mapper.selectMaterialLotMainList(tenantId, dto.getFreezeTypeTag(), splitMaterialLotId));
            long endDate5 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 根据物料批ID查询数据耗时：{}毫秒", (endDate5 - startDate5));
        }
        long endDate4 = System.currentTimeMillis();
        List<String> fianlMaterialLotIdList = list.stream().map(HmeFreezeDocCreateSnVO::getMaterialLotId).distinct().collect(Collectors.toList());
        log.info("<====冻结解冻平台 SN查询 根据物料批ID查询数据总耗时：{}毫秒,总数{}个", (endDate4 - startDate4), fianlMaterialLotIdList.size());
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        // 查询sn下所有的数据
        List<HmeEoJobSn> eoJobSns = new ArrayList<>();
        List<List<String>> splitFianlMaterialLotIdList = CommonUtils.splitSqlList(fianlMaterialLotIdList, 1000);
        for (List<String> splitFianlMaterialLotId:splitFianlMaterialLotIdList) {
            eoJobSns.addAll(eoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .select(HmeEoJobSn.FIELD_JOB_ID, HmeEoJobSn.FIELD_MATERIAL_LOT_ID)
                    .andWhere(Sqls.custom()
                            .andIn(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, splitFianlMaterialLotId))
                    .orderBy(HmeEoJobSn.FIELD_MATERIAL_LOT_ID)
                    .orderByDesc(HmeEoJobSn.FIELD_CREATION_DATE)
                    .build()));
        }
        long endDate5 = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 查询eoJobSn数据耗时：{}毫秒", (endDate5 - endDate4));
        if (CollectionUtils.isNotEmpty(eoJobSns)) {
            Map<String, String> snJobMap = eoJobSns.stream().collect(Collectors.groupingBy(HmeEoJobSn::getMaterialLotId, Collectors.collectingAndThen(Collectors.toList(), values -> values.get(0).getJobId())));
            long startDate6 = System.currentTimeMillis();
            List<HmeFreezeDocJobVO> jobList = new ArrayList<>();
            List<String> jobIdList = new ArrayList<>(snJobMap.values());
            jobIdList = jobIdList.stream().distinct().collect(Collectors.toList());
            List<List<String>> splitJobIdList = CommonUtils.splitSqlList(jobIdList, 1000);
            for (List<String> splitJobId:splitJobIdList) {
                jobList.addAll(lineMapper.selectJobInfoList2(tenantId, splitJobId));
            }
            long endDate6 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 selectJobInfoList耗时：{}毫秒", (endDate6 - startDate6));
            Map<String, HmeFreezeDocJobVO> jobMap = jobList.stream().collect(Collectors.toMap(HmeFreezeDocJobVO::getJobId, a -> a, (t, s) -> t));
            list.forEach(rec -> {
                if (snJobMap.containsKey(rec.getMaterialLotId())) {
                    String jobId = snJobMap.get(rec.getMaterialLotId());
                    if(StringUtils.isNotBlank(jobId)){
                        HmeFreezeDocJobVO hmeFreezeDocJobVO = jobMap.get(jobId);
                        if(Objects.nonNull(hmeFreezeDocJobVO)){
                            rec.propertiesCompletion(hmeFreezeDocJobVO);
                        }
                    }
                }
            });
        }
        long endDate6 = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 查询sn下所有的数据耗时：{}毫秒", (endDate6 - endDate4));
        AtomicInteger seqGen = new AtomicInteger(0);
        list.forEach(rec -> rec.setSequenceNum(seqGen.incrementAndGet()));
        long endDate7 = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 设置序号耗时：{}毫秒", (endDate7 - endDate6));
        log.info("<====冻结解冻平台 SN查询 总耗时：{}毫秒,返回总数{}", (endDate7 - startDate), list.size());
        return list;
    }

    @Override
    public List<String> selectFreezeMaterialLotIdList(Long tenantId, HmeFreezeDocQueryDTO dto) {
        log.info("<====冻结解冻平台 SN查询开始啦");
        long startDate = System.currentTimeMillis();
        List<HmeMaterialLotVO> snList = new ArrayList<>();
        //如果输入了热沉编码，则先查找物料批
        if(StringUtils.isNotBlank(dto.getHotSinkNum())){
            List<String> materialLotIdList = new ArrayList<>();
            long startDate2 = System.currentTimeMillis();
            materialLotIdList.addAll(mapper.getNewMaterialLotByHotSinkCode(tenantId, dto.getHotSinkNum()));
            long endDate2 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 getNewMaterialLotByHotSinkCode耗时：{}毫秒", (endDate2 - startDate2));
            materialLotIdList.addAll(mapper.getMaterialLotByHotSinkCode2(tenantId, dto.getHotSinkNum()));
            long endDate3 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 getMaterialLotByHotSinkCode2耗时：{}毫秒", (endDate3 - endDate2));
            if(CollectionUtils.isNotEmpty(materialLotIdList)){
                materialLotIdList = materialLotIdList.stream().filter(item -> StringUtils.isNotBlank(item)).distinct().collect(Collectors.toList());
                dto.setMaterialLotIdList(materialLotIdList);
            }
        }
        long startDate22 = System.currentTimeMillis();
        snList.addAll(mapper.selectMaterialLotList(tenantId, dto));
        long endDate22 = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 第一步查询条码1耗时：{}毫秒", (endDate22 - startDate22));
        boolean distinctFlag = false;
        if((("COS_INVENTORY").equals(dto.getFreezeTypeTag()) || ("COS_PROCESS").equals(dto.getFreezeTypeTag()))
                && (StringUtils.isNotBlank(dto.getWafer()) || Objects.nonNull(dto.getAusnRatio())
                || StringUtils.isNotBlank(dto.getCosType()))
                && StringUtils.isEmpty(dto.getVirtualNum()) && StringUtils.isEmpty(dto.getCosRuleId()) ){
            //COS类型冻结时，如果输入了wafer或金锡比或COS类型，但没有输入虚拟号和筛选规则时，需调用此方法。主要是为了SQL拆分
            long startDate2 = System.currentTimeMillis();
            snList.addAll(mapper.selectMaterialLotList2(tenantId, dto));
            distinctFlag = true;
            long endDate2 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 第一步查询条码2耗时：{}毫秒", (endDate2 - startDate2));
        }
        if((("COS_INVENTORY").equals(dto.getFreezeTypeTag()) || ("COS_PROCESS").equals(dto.getFreezeTypeTag()))
                && (StringUtils.isNotBlank(dto.getWafer()) || Objects.nonNull(dto.getAusnRatio())
                 || StringUtils.isNotBlank(dto.getCosType()))
                && StringUtils.isEmpty(dto.getVirtualNum()) && StringUtils.isEmpty(dto.getCosRuleId()) ){
            //COS类型冻结时，如果输入了wafer或金锡比或COS类型，但没有输入虚拟号和筛选规则时，需调用此方法。主要是为了SQL拆分
            long startDate2 = System.currentTimeMillis();
            snList.addAll(mapper.selectMaterialLotList3(tenantId, dto));
            distinctFlag = true;
            long endDate2 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 第一步查询条码3耗时：{}毫秒", (endDate2 - startDate2));
        }
        if (CollectionUtils.isEmpty(snList)) {
            return new ArrayList<>();
        }
        if(distinctFlag){
            snList = snList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(HmeMaterialLotVO::getMaterialLotId))), ArrayList::new));
        }
        //为了解决COS贴片前和贴片后冻结时，选择了工单或设备或生产线或工段或工序或工位或操作人或班次或生产时间从至时，装盒后缺失的条码情况
        if(("COS_CHIP_INVENTORY".equals(dto.getFreezeType()) || "COS_M_INVENTORY".equals(dto.getFreezeType()))
                && (StringUtils.isNotBlank(dto.getWorkOrderId()) || StringUtils.isNotBlank(dto.getEquipmentId())
                || StringUtils.isNotBlank(dto.getProdLineId()) || StringUtils.isNotBlank(dto.getWorkcellId())
                || StringUtils.isNotBlank(dto.getProcessId()) || StringUtils.isNotBlank(dto.getStationId())
                || Objects.nonNull(dto.getOperatedBy()) || StringUtils.isNotBlank(dto.getShiftId())
                || Objects.nonNull(dto.getProductionDateFrom()) || Objects.nonNull(dto.getProductionDateTo()))){
            long startDate4 = System.currentTimeMillis();
            List<String> materialLotIdList = snList.stream().map(HmeMaterialLotVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 700);
            List<String> addMaterialLotIdList = new ArrayList<>();
            int i = 1;
            for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                long startDate2 = System.currentTimeMillis();
                List<HmeSelectionDetails> hmeSelectionDetails = mapper.selectBeforeReleaseMaterialLotList(tenantId, splitMaterialLotId);
                long endDate2 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 装盒后条码缺失情况第{}次selectBeforeReleaseMaterialLotList耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate2 - startDate2));
                List<String> oldMaterialLotIdList = hmeSelectionDetails.stream().map(HmeSelectionDetails::getOldMaterialLotId).collect(Collectors.toList());
                addMaterialLotIdList.addAll(oldMaterialLotIdList);
                List<String> newMaterialLotIdList = hmeSelectionDetails.stream().map(HmeSelectionDetails::getNewMaterialLotId).collect(Collectors.toList());
                addMaterialLotIdList.addAll(newMaterialLotIdList);
                List<String> afterReleaseNewMaterialLotIdList = mapper.selectAfterReleaseMaterialLotList(tenantId, splitMaterialLotId);
                long endDate3 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 装盒后条码缺失情况第{}次selectAfterReleaseMaterialLotList：{}毫秒", splitMaterialLotId.size(), i, (endDate3 - endDate2));
                addMaterialLotIdList.addAll(afterReleaseNewMaterialLotIdList);
                i++;
            }
            if(CollectionUtils.isNotEmpty(addMaterialLotIdList)){
                addMaterialLotIdList = addMaterialLotIdList.stream().distinct().collect(Collectors.toList());
                addMaterialLotIdList.removeAll(materialLotIdList);
                if(CollectionUtils.isNotEmpty(addMaterialLotIdList)){
                    List<List<String>> splitAddMaterialLotIdList = CommonUtils.splitSqlList(addMaterialLotIdList, 1000);
                    for (List<String> splitAddMaterialLotId:splitAddMaterialLotIdList) {
                        snList.addAll(mapper.mfFlagBatchQuery(tenantId, splitAddMaterialLotId));
                    }
                }
            }
            long endDate4 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 装盒后条码缺失情况总耗时：{}毫秒", (endDate4 - startDate4));
        }

        long endDate = System.currentTimeMillis();

        // 若为COS库存冻结类型，则需要找到关联的芯片，热忱和金线条码并补充进来，进入后续处理
        if (COS_INVENTORY.equals(dto.getFreezeTypeTag())) {
            List<String> materialLotIdList = snList.stream().map(HmeMaterialLotVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 300);
            int i = 1;
            for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                //追溯芯片投料后情况下的条码
                long startDate2 = System.currentTimeMillis();
                snList.addAll(mapper.selectChipAfterReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate2 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯芯片投料后情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate2 - startDate2));
                //追溯芯片投料前情况下的条码
                snList.addAll(mapper.selectChipBeforeReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate3 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯芯片投料前情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate3 - endDate2));
                //追溯热沉投料后情况下的条码
                snList.addAll(mapper.selectHotSinkAfterReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate4 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯热沉投料后情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate4 - endDate3));
                //追溯热沉投料前情况下的条码
                snList.addAll(mapper.selectHotSinkBeforeReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate5 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯热沉投料前情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate5 - endDate4));
                //追溯金线投料后情况下的条码
                snList.addAll(mapper.selectGoldWireAfterReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate6 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯金线投料后情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate6 - endDate5));
                //追溯金线投料前情况下的条码
                snList.addAll(mapper.selectGoldWireBeforeReleaseMaterialLotList(tenantId, dto, splitMaterialLotId));
                long endDate7 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 根据{}个条码第{}次追溯金线投料前情况下的条码SQL耗时：{}毫秒", splitMaterialLotId.size(), i, (endDate7 - endDate6));
                i++;
            }
            snList = snList.stream().distinct().collect(Collectors.toList());
            long endDate2 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 追溯芯片、热沉、金线条码总耗时：{}毫秒",  (endDate2 - endDate));
        }

        long startDate3 = System.currentTimeMillis();
        Map<String, String> snMap = snList.stream().collect(Collectors.toMap(HmeMaterialLotVO::getMaterialLotId, HmeMaterialLotVO::getMfFlag, (a, b) -> a));
        // 筛选job相关参数
        if (HmeFreezeDocQueryDTO.jobValidNeeded(dto)) {
            List<String> passedSnList = new ArrayList<>();
            List<String> materialLotIdList = snList.stream().map(HmeMaterialLotVO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<List<String>> splitList = CommonUtils.splitSqlList(materialLotIdList, 1000);
            for (List<String> split:splitList) {
                long startDate6 = System.currentTimeMillis();
                passedSnList.addAll(mapper.selectJobFilteredList(tenantId, dto, split));
                long endDate6 = System.currentTimeMillis();
                log.info("<====冻结解冻平台 SN查询 查询selectJobFilteredList数据耗时：{}毫秒", (endDate6 - startDate6));
            }
            Set<String> passedSns = passedSnList.stream().collect(Collectors.toSet());
            long endDate5 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 查询selectJobFilteredList数据总耗时：{}毫秒", (endDate5 - startDate3));
            snMap.keySet().removeIf(key -> !passedSns.contains(key));
        }
        Map<String, String> invMap = snMap.entrySet().stream().filter(rec -> NO.equals(rec.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        long endDate3 = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 确定snMap和invMap数据耗时：{}毫秒", (endDate3 - startDate3));

        // 库存/COS库存 迭代查询在制条码
        if (invMap.size() > 0 && StringCommonUtils.contains(dto.getFreezeTypeTag(), INVENTORY, COS_INVENTORY)) {
            int i = 1;
            snMap = this.snIteration(tenantId, snMap, invMap, i);
            long endDate4 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 迭代查询在制条码耗时：{}毫秒", (endDate4 - endDate3));
        }
        if(snMap.isEmpty()){
            return new ArrayList<>();
        }

        // 根据物料批ID查询数据
        long startDate4 = System.currentTimeMillis();
        List<String> resultList = new ArrayList<>();
        List<String> materialLotIdList = new ArrayList<>(snMap.keySet());
        materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
        List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 1000);
        for (List<String> splitMaterialLotId : splitMaterialLotIdList) {
            long startDate5 = System.currentTimeMillis();
            resultList.addAll(mapper.selectFreezeMaterialLotIdList(tenantId, splitMaterialLotId));
            long endDate5 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 根据物料批ID查询有效物料批耗时：{}毫秒", (endDate5 - startDate5));
        }
        long endDate4 = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 根据物料批ID查询数据总耗时：{}毫秒,总数{}个", (endDate4 - startDate), resultList.size());
        return resultList;
    }

    @Override
    public List<HmeFreezeDocTrxVO> materialLotFreezeTrxListGet(Long tenantId, List<String> materialLotIds) {
        List<HmeFreezeDocTrxVO> list = mapper.selectMaterialLotTrxList(tenantId, materialLotIds);
        List<MtModLocator> locatorList = mapper.selectFreezeLocatorList(tenantId);
        Map<String, MtModLocator> freezeLocatorMap = locatorList.stream().collect(Collectors.toMap(MtModLocator::getParentLocatorId, Function.identity()));
        list.forEach(rec -> {
            if (freezeLocatorMap.containsKey(rec.getWarehouseId())) {
                MtModLocator freezeLocator = freezeLocatorMap.get(rec.getWarehouseId());
                rec.setLocatorId(rec.getReduceLocatorId());
                rec.setIncreaseLocatorId(freezeLocator.getLocatorId());
                rec.setTransferLocatorCode(freezeLocator.getLocatorCode());
            } else {
                throw new CommonException("仓库" +  rec.getWarehouseCode() + " 未找到冻结货位！");
            }
        });
        return list;
    }

    @Override
    public List<HmeFreezeDocTrxVO> materialLotUnfreezeTrxListGet(Long tenantId, List<String> materialLotIds) {
        return mapper.selectMaterialLotUnfreezeTrxList(tenantId, materialLotIds);
    }

    @Override
    public void save(HmeFreezeDoc doc) {
        if (StringUtils.isBlank(doc.getFreezeDocId())) {
            this.insertSelective(doc);
        } else {
            mapper.updateByPrimaryKeySelective(doc);
        }
    }

    @Override
    public List<HmeMaterialLotLoad> selectRelationLoadList(Long tenantId, HmeFreezeDocQueryDTO dto, Iterable<String> materialLotIds) {
        return mapper.selectRelationLoadList(tenantId, dto, materialLotIds);
    }

    private Map<String, String> snIteration(Long tenantId, Map<String, String> snMap, Map<String, String> invMap, int i) {
        Set<String> materialLotIds = invMap.keySet();
        List<String> materialLotIdList = new ArrayList<>(materialLotIds);
        long startDate = System.currentTimeMillis();

        List<HmeMaterialLotVO> snList = new ArrayList<>();
        List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 1000);
        for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
            long startDate2 = System.currentTimeMillis();
            snList.addAll(mapper.selectEoJobSnLotMaterialLotList(tenantId, splitMaterialLotId));
            long endDate2 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 第{}次迭代查询在制条码selectEoJobSnLotMaterialLotList耗时：{}毫秒", i, (endDate2 - startDate2));
            snList.addAll(mapper.selectEoJobMaterialLotList(tenantId, splitMaterialLotId));
            long endDate3 = System.currentTimeMillis();
            log.info("<====冻结解冻平台 SN查询 第{}次迭代查询在制条码selectEoJobSnLotMaterialLotList耗时：{}毫秒", i, (endDate3 - endDate2));
        }
        if(CollectionUtils.isNotEmpty(snList)){
            snList = snList.stream().distinct().collect(Collectors.toList());
        }
        long endDate = System.currentTimeMillis();
        log.info("<====冻结解冻平台 SN查询 第{}次迭代查询在制条码总耗时：{}毫秒", i, (endDate - startDate));
        Map<String, String> searchMap = snList.stream().collect(Collectors.toMap(HmeMaterialLotVO::getMaterialLotId, HmeMaterialLotVO::getMfFlag, (a, b) -> a));
        snMap.putAll(searchMap);
        Map<String, String> invSnMap = searchMap.entrySet().stream().filter(rec -> NO.equals(rec.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // 若仍然能找到库存物料批则继续迭代
        if (invSnMap.size() > 0) {
            i++;
            return snIteration(tenantId, snMap, invSnMap, i);
        } else {
            return snMap;
        }
    }
}
