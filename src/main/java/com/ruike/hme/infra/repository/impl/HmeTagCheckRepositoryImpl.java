package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.app.service.HmeWorkOrderManagementService;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.repository.HmeTagCheckRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeTagCheckMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:44
 */
@Component
@Slf4j
public class HmeTagCheckRepositoryImpl implements HmeTagCheckRepository {

    @Autowired
    private HmeWorkOrderManagementService hmeWorkOrderManagementService;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeTagCheckMapper hmeTagCheckMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;


    @Override
    public List<HmeTagCheckVO2> snListQuery(Long tenantId, HmeTagCheckVO vo) {
        // 获取用户默认事业部
        HmeUserOrganizationVO2 organizationVO2 = defaultDepartment(tenantId);
        if (organizationVO2 == null) {
            throw new MtException("HME_TAG_CHECK_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_019", "HME"));
        }
        if (StringUtils.isBlank(vo.getProcessId())) {
            throw new MtException("HME_TAG_CHECK_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_020", "HME"));
        }
        // 查询数据项维护数据
        List<HmeTagCheckVO2> resultList = hmeTagCheckMapper.queryTagCheckList(tenantId, vo.getProcessId(), organizationVO2.getAreaId());
        return handleSnData(tenantId, resultList, vo);
    }

    private List<HmeTagCheckVO2> handleSnData(Long tenantId, List<HmeTagCheckVO2> resultList, HmeTagCheckVO checkVO) {
        if (CollectionUtils.isEmpty(resultList)) {
            return Collections.EMPTY_LIST;
        }
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, Collections.singletonList(checkVO.getSnNum()));
        if (CollectionUtils.isEmpty(materialLotList)) {
            throw new MtException("HME_TAG_CHECK_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_021", "HME", checkVO.getSnNum()));
        }
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        // 用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        List<String> processList = resultList.stream().map(HmeTagCheckVO2::getSourceProcessId).distinct().collect(Collectors.toList());
        // 查出EO在该工序的进站记录
        List<HmeTagCheckVO3> eoJobList = hmeTagCheckMapper.queryEoJobList(tenantId, materialLotList.get(0).getMaterialLotId(), defaultSiteId, processList);
        // 取出在工序下作业最大的Job
        Map<String, HmeTagCheckVO3> groupProcessMap = eoJobList.stream().collect(Collectors.toMap(HmeTagCheckVO3::getProcessId, Function.identity(), (n1, n2) -> n1.getSiteOutDate().before(n2.getSiteOutDate()) ? n2 : n1));
        List<String> maxJobIdList = groupProcessMap.values().stream().map(HmeTagCheckVO3::getJobId).collect(Collectors.toList());
        List<String> tagIdList = resultList.stream().map(HmeTagCheckVO2::getTagId).distinct().collect(Collectors.toList());
        // 根据job和数据项查询记录
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(maxJobIdList)) {
            hmeEoJobDataRecordList = hmeTagCheckMapper.queryJobDataRecordList(tenantId, maxJobIdList, tagIdList);
        }
        Map<String, List<HmeEoJobDataRecord>> hmeEoJobDataRecordMap = hmeEoJobDataRecordList.stream().collect(Collectors.groupingBy(dto -> dto.getJobId() + "_" + dto.getTagId()));
        for (HmeTagCheckVO2 hmeTagCheckVO2 : resultList) {
            HmeTagCheckVO3 hmeTagCheckVO3 = groupProcessMap.get(hmeTagCheckVO2.getSourceProcessId());
            if (hmeTagCheckVO3 != null) {
                List<HmeEoJobDataRecord> recordList = hmeEoJobDataRecordMap.get(hmeTagCheckVO3.getJobId() + "_" + hmeTagCheckVO2.getTagId());
                if (CollectionUtils.isNotEmpty(recordList)) {
                    hmeTagCheckVO2.setMaximalValue(recordList.get(0).getMaximalValue());
                    hmeTagCheckVO2.setMinimumValue(recordList.get(0).getMinimumValue());
                    hmeTagCheckVO2.setResult(recordList.get(0).getResult());
                }
            }
        }
        return resultList;
    }

    private HmeUserOrganizationVO2 defaultDepartment(Long tenantId) {
        List<HmeUserOrganizationVO2> departmentList = hmeWorkOrderManagementService.departmentQuery(tenantId);
        Optional<HmeUserOrganizationVO2> firstOpt = departmentList.stream().filter(department -> HmeConstants.ConstantValue.YES.equals(department.getDefaultOrganizationFlag())).findFirst();
        return firstOpt.isPresent() ? firstOpt.get() : null;
    }

    @Override
    public List<HmeTagCheckVO5> componentListQuery(Long tenantId, HmeTagCheckVO vo) {
        // 获取用户默认事业部
        HmeUserOrganizationVO2 organizationVO2 = defaultDepartment(tenantId);
        if (organizationVO2 == null) {
            throw new MtException("HME_TAG_CHECK_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_019", "HME"));
        }
        List<HmeTagCheckVO5> resultList = this.queryTagCheckMaterialLotList(tenantId, vo, organizationVO2.getAreaId());
        this.handleComponentData(tenantId, resultList, vo, organizationVO2.getAreaId());
        return resultList;
    }

    @Override
    public String getShowTagModelFlag(Long tenantId, String workcellId) {
        // 获取用户默认事业部
        HmeUserOrganizationVO2 organizationVO2 = defaultDepartment(tenantId);
        if (organizationVO2 != null) {
            List<MtModOrganizationRel> organizationRelList = mtModOrganizationRelRepository.selectByCondition(Condition.builder(MtModOrganizationRel.class).select(MtModOrganizationRel.FIELD_PARENT_ORGANIZATION_ID).andWhere(Sqls.custom()
                    .andEqualTo(MtModOrganizationRel.FIELD_PARENT_ORGANIZATION_TYPE, "WORKCELL")
                    .andEqualTo(MtModOrganizationRel.FIELD_ORGANIZATION_TYPE, "WORKCELL")
                    .andEqualTo(MtModOrganizationRel.FIELD_ORGANIZATION_ID, workcellId)
                    .andEqualTo(MtModOrganizationRel.FIELD_TENANT_ID, tenantId)).build());
            if (CollectionUtils.isNotEmpty(organizationRelList)) {
                List<String> headerIdList = hmeTagCheckMapper.queryTagCheckHeaderList(tenantId, organizationRelList.get(0).getParentOrganizationId(), organizationVO2.getAreaId());
                if (CollectionUtils.isNotEmpty(headerIdList)) {
                    return HmeConstants.ConstantValue.YES;
                }
            }
        }
        return HmeConstants.ConstantValue.NO;
    }

    private List<HmeTagCheckVO5> queryTagCheckMaterialLotList(Long tenantId, HmeTagCheckVO vo, String areaId) {
        List<HmeTagCheckVO5> resultList = new ArrayList<>();

        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        // 用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        // 分为当前SN数据和组件数据
        if (StringUtils.equals(vo.getRuleType(), "SN_DATA")) {
            // 查询作业记录数据
            List<HmeTagCheckVO4> tagCheckVOList = hmeTagCheckMapper.querySnMaterialLotCodeJobList(tenantId, vo, defaultSiteId);
            // 根据序列号进行分组显示 再进行手动分页
            Map<String, List<HmeTagCheckVO4>> tagCheckVOMap = tagCheckVOList.stream().collect(Collectors.groupingBy(HmeTagCheckVO4::getMaterialLotId));

            // 根据序列号组装数据
            tagCheckVOMap.forEach((key, value) -> {
                HmeTagCheckVO4 hmeTagCheckVO4 = value.get(0);
                HmeTagCheckVO5 checkVO5 = new HmeTagCheckVO5();
                checkVO5.setMaterialLotCode(hmeTagCheckVO4.getMaterialLotCode());
                checkVO5.setMaterialCode(hmeTagCheckVO4.getMaterialCode());
                checkVO5.setMaterialName(hmeTagCheckVO4.getMaterialName());
                // 再根据工序分组 取出工序下最新的作业记录 如果限制了出站时间从至 则根据最大作业记录的出站时间限制
                List<HmeTagCheckVO4> maxJobList = this.queryMaxJobList(tenantId, value);
                checkVO5.setJobList(maxJobList);
                resultList.add(checkVO5);
            });
        } else {
            // 查询数据项维护的物料组下所有物料
            List<String> materialIdList = hmeTagCheckMapper.queryComponentMaterialTagCheckList(tenantId, vo.getProcessId(), areaId, defaultSiteId);
            if (CollectionUtils.isNotEmpty(materialIdList)) {
                List<HmeTagCheckVO4> componentMaterialLotList = hmeTagCheckMapper.queryCmbMaterialLotCodeList(tenantId, vo.getSnNumList(), materialIdList);
                // 同一SN多次返修时 投相同物料时 取最新投的物料
//                Map<String, List<HmeTagCheckVO4>> groupSnAndMaterialMap = componentMaterialLotList.stream().collect(Collectors.groupingBy(cm -> cm.getMaterialLotId() + "_" + cm.getComponentMaterialId()));
//                List<HmeTagCheckVO4> filterComponentMaterialLotList = new ArrayList<>();
//                groupSnAndMaterialMap.forEach((key, value) -> {
//                    List<HmeTagCheckVO4> sortValueList = value.stream().sorted(Comparator.comparing(HmeTagCheckVO4::getSiteOutDate).reversed()).collect(Collectors.toList());
//                    filterComponentMaterialLotList.add(sortValueList.get(0));
//                });

                // 查询组件条码 工序下最新进站job
                List<String> componentMaterialLotIdList = componentMaterialLotList.stream().map(HmeTagCheckVO4::getComponentMaterialLotId).collect(Collectors.toList());
                // 对组件SN进行分组
                Map<String, List<HmeTagCheckVO4>> componentMaterialLotMap = componentMaterialLotList.stream().collect(Collectors.groupingBy(HmeTagCheckVO4::getComponentMaterialLotId));

                if (CollectionUtils.isNotEmpty(componentMaterialLotIdList)) {
                    List<HmeTagCheckVO4> componentJobList = hmeTagCheckMapper.queryCmbMaterialLotCodeJobList(tenantId, componentMaterialLotIdList, defaultSiteId);
                    // 根据序列号进行分组显示 再进行手动分页
                    Map<String, List<HmeTagCheckVO4>> tagCheckVOMap = componentJobList.stream().collect(Collectors.groupingBy(HmeTagCheckVO4::getMaterialLotId));
                    // 根据序列号组装数据
                    tagCheckVOMap.forEach((key, value) -> {
                        List<HmeTagCheckVO4> hmeTagCheckVO4List = componentMaterialLotMap.get(key);
                        if (CollectionUtils.isNotEmpty(hmeTagCheckVO4List)) {
                            HmeTagCheckVO5 checkVO5 = new HmeTagCheckVO5();
                            checkVO5.setMaterialLotCode(hmeTagCheckVO4List.get(0).getMaterialLotCode());
                            checkVO5.setMaterialCode(hmeTagCheckVO4List.get(0).getMaterialCode());
                            checkVO5.setMaterialName(hmeTagCheckVO4List.get(0).getMaterialName());
                            checkVO5.setComponentMaterialLotCode(hmeTagCheckVO4List.get(0).getComponentMaterialLotCode());
                            checkVO5.setComponentMaterialCode(hmeTagCheckVO4List.get(0).getComponentMaterialCode());
                            checkVO5.setComponentMaterialName(hmeTagCheckVO4List.get(0).getComponentMaterialName());
                            // 再根据工序分组 取出工序下最新的作业记录 如果限制了出站时间从至 则根据最大作业记录的出站时间限制
                            List<HmeTagCheckVO4> maxJobList = this.queryMaxJobList(tenantId, value);
                            checkVO5.setJobList(maxJobList);
                            resultList.add(checkVO5);
                        }
                    });
                }
            }
        }
        return resultList;
    }

    /**
     * 动态列处理
     *
     * @param tenantId
     * @param hmeTagCheckVO5List
     * @author sanfeng.zhang@hand-china.com 2021/9/1 16:23
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO2>
     */
    private List<HmeTagCheckVO5> handleComponentData(Long tenantId, List<HmeTagCheckVO5> hmeTagCheckVO5List, HmeTagCheckVO vo, String businessId) {
        // 根据查询条件找出对应的数据项展示规则信息
        List<HmeTagCheckVO7> hmeTagCheckVO7s = hmeTagCheckMapper.queryComponentTagCheckList(tenantId, businessId, vo.getProcessId(), vo.getRuleType());
        if (CollectionUtils.isEmpty(hmeTagCheckVO5List) || CollectionUtils.isEmpty(hmeTagCheckVO7s)) {
            return hmeTagCheckVO5List;
        }
        // 去重 不同的数据项规则展示头的可能维护相同的来源工序和数据项
        List<HmeTagCheckVO7> distinctTagCheckList = hmeTagCheckVO7s.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(obj -> obj.getTagId() + "," + obj.getSourceWorkcellId()))), ArrayList::new));
        // 构建Job和tag的关系 根据工序去匹配 前面工序对应一个最大job
        List<HmeTagCheckVO4> tagCheckVOList = new ArrayList<>();
        List<HmeTagCheckVO4> processTagList = null;
        HmeTagCheckVO4 tagCheckVO = null;
        Map<String, List<HmeTagCheckVO7>> groupProcessTagMap = distinctTagCheckList.stream().collect(Collectors.groupingBy(HmeTagCheckVO7::getSourceWorkcellId));
        for (HmeTagCheckVO5 hmeTagCheckVO5 : hmeTagCheckVO5List) {
            if (CollectionUtils.isNotEmpty(hmeTagCheckVO5.getJobList())) {
                // 根据工位找到对应数据项
                processTagList = new ArrayList<>();
                for (HmeTagCheckVO4 hmeTagCheckVO4 : hmeTagCheckVO5.getJobList()) {
                    // 一个工序会维护多个数据项
                    List<HmeTagCheckVO7> tagCheckList = groupProcessTagMap.getOrDefault(hmeTagCheckVO4.getProcessId(), Collections.EMPTY_LIST);
                    for (HmeTagCheckVO7 hmeTagCheckVO7 : tagCheckList) {
                        tagCheckVO = new HmeTagCheckVO4();
                        tagCheckVO.setTagId(hmeTagCheckVO7.getTagId());
                        tagCheckVO.setTagCode(hmeTagCheckVO7.getTagCode());
                        tagCheckVO.setTagDescription(hmeTagCheckVO7.getTagDescription());
                        tagCheckVO.setProcessId(hmeTagCheckVO7.getSourceWorkcellId());
                        tagCheckVO.setProcessCode(hmeTagCheckVO7.getProcessCode());
                        tagCheckVO.setProcessName(hmeTagCheckVO7.getProcessName());
                        tagCheckVO.setJobId(hmeTagCheckVO4.getJobId());
                        tagCheckVOList.add(tagCheckVO);
                        processTagList.add(tagCheckVO);
                    }
                }
                hmeTagCheckVO5.setJobList(processTagList);
            }
        }
        // 根据jobId和数据项去找采集项记录
        List<HmeTagCheckVO8> recordResultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tagCheckVOList)) {
            recordResultList =  hmeTagCheckMapper.queryRecordResult(tenantId, tagCheckVOList);
        }

        // 根据数据项和工序进行去重 此处就为动态列要展示的工序及工序下的采集项 工序来排序
        tagCheckVOList = tagCheckVOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(obj -> obj.getTagId() + "," + obj.getProcessId()))), ArrayList::new))
                .stream().sorted(Comparator.comparing(HmeTagCheckVO4::getProcessId).thenComparing(HmeTagCheckVO4::getTagId)).collect(Collectors.toList());

        Map<String, List<HmeTagCheckVO8>> recordResultMap = recordResultList.stream().collect(Collectors.groupingBy(record -> record.getJobId() + "_" + record.getTagId()));
        //组装动态数据项 按总的数据项和工序来显示 没有不显示值
        List<HmeTagCheckVO6> processList = null;
        List<HmeTagCheckVO8> tagList = null;
        Map<String, List<HmeTagCheckVO4>> processMap = tagCheckVOList.stream().collect(Collectors.groupingBy(HmeTagCheckVO4::getProcessId, LinkedHashMap::new, Collectors.toList()));
        for (HmeTagCheckVO5 tagVo : hmeTagCheckVO5List) {
            // 对该序列号下的作业记录 根据工序和数据项进行分组 取到数据 则说明序列号在工序对该数据项有采集
            Map<String, HmeTagCheckVO4> processTagMap = tagVo.getJobList().stream().collect(Collectors.toMap(tag -> tag.getProcessId() + "_" + tag.getTagId(), Function.identity()));
            processList = new ArrayList<>();
            for (Map.Entry<String, List<HmeTagCheckVO4>> processEntry : processMap.entrySet()) {
                List<HmeTagCheckVO4> tagResultList = processEntry.getValue();
                HmeTagCheckVO6 checkVO6 = new HmeTagCheckVO6();
                checkVO6.setProcessCode(tagResultList.get(0).getProcessCode());
                checkVO6.setProcessName(tagResultList.get(0).getProcessName());
                tagList = new ArrayList<>();
                for (HmeTagCheckVO4 hmeTagCheckVO4 : tagResultList) {
                    HmeTagCheckVO8 checkVO8 = new HmeTagCheckVO8();
                    checkVO8.setTagId(hmeTagCheckVO4.getTagId());
                    checkVO8.setTagCode(hmeTagCheckVO4.getTagCode());
                    checkVO8.setTagDescription(hmeTagCheckVO4.getTagDescription());
                    HmeTagCheckVO4 jobVo = processTagMap.get(tagResultList.get(0).getProcessId() + "_" + hmeTagCheckVO4.getTagId());
                    checkVO8.setResult("");
                    if (jobVo != null) {
                        List<HmeTagCheckVO8> resultList = recordResultMap.get(jobVo.getJobId() + "_" + hmeTagCheckVO4.getTagId());
                        if (CollectionUtils.isNotEmpty(resultList)) {
                            checkVO8.setResult(resultList.get(0).getResult());
                        }
                    }
                    tagList.add(checkVO8);
                }
                checkVO6.setTagList(tagList);
                processList.add(checkVO6);
            }
            tagVo.setProcessList(processList);
        }
        return hmeTagCheckVO5List;
    }

    private List<HmeTagCheckVO4> queryMaxJobList (Long tenantId, List<HmeTagCheckVO4> tagCheckVOList) {
        Map<String, List<HmeTagCheckVO4>> groupProcessList = tagCheckVOList.stream().collect(Collectors.groupingBy(HmeTagCheckVO4::getProcessId));
        List<HmeTagCheckVO4> maxJobList = new ArrayList<>();
        for (Map.Entry<String, List<HmeTagCheckVO4>> groupProcessEntry : groupProcessList.entrySet()) {
            List<HmeTagCheckVO4> sortList = groupProcessEntry.getValue().stream().sorted(Comparator.comparing(HmeTagCheckVO4::getSiteOutDate).reversed()).collect(Collectors.toList());
            maxJobList.add(sortList.get(0));
        }
        return maxJobList;
    }
}
