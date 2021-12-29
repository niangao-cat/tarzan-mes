package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeCommonExportRepository;
import com.ruike.hme.domain.vo.HmeTagExportVO;
import com.ruike.hme.domain.vo.HmeTagGroupExportVO;
import com.ruike.hme.domain.vo.HmeTagGroupObjectExportVO;
import com.ruike.hme.infra.mapper.HmeCommonExportMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendSettingsVO2;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.api.dto.MtTagGroupDTO2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/31 10:23
 */
@Component
public class HmeCommonExportRepositoryImpl implements HmeCommonExportRepository {

    @Autowired
    private HmeCommonExportMapper hmeCommonExportMapper;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    private static final String TABLE_NAME_TAG = "mt_tag_attr";
    private static final String TABLE_NAME_TAG_GROUP = "mt_tag_group_attr";

    @Override
    @ProcessLovValue
    public List<HmeTagExportVO> tagExport(Long tenantId, MtTagDTO mtTagDTO) {
        List<HmeTagExportVO> hmeTagExportVOS = hmeCommonExportMapper.tagExport(tenantId, mtTagDTO);
        List<String> tagIdList = hmeTagExportVOS.stream().map(HmeTagExportVO::getTagId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 表的所有扩展字段
        Map<String, List<MtExtendSettingsVO2>> attrMap = new HashMap<>();
        // 数据项的扩展字段
        Map<String, List<MtExtendAttrVO1>> extendAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(tagIdList)) {
            // 获取表所有扩展字段扩展
            List<MtExtendSettingsVO2> attrList = mtExtendSettingsRepository.customAttrQuery(tenantId, TABLE_NAME_TAG, "Y");
            if (CollectionUtils.isNotEmpty(attrList)) {
                attrMap = attrList.stream().collect(Collectors.groupingBy(MtExtendSettingsVO2::getAttrName));
            }
            List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1() {{
                setKeyIdList(tagIdList);
                setTableName(TABLE_NAME_TAG);
                setAttrs(Collections.emptyList());
            }});
            // 过滤掉 值为空的数据
            List<MtExtendAttrVO1> filterAttrList = extendAttrVO1List.stream().filter(attr -> StringUtils.isNotBlank(attr.getAttrValue())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterAttrList)) {
                extendAttrMap = filterAttrList.stream().collect(Collectors.groupingBy(MtExtendAttrVO1::getKeyId));
            }
        }
        // 已扩展字段为最小维度 即多少扩展字段则有多少行数据 过程数据标识除外
        List<HmeTagExportVO> result = new ArrayList<>();
        for (HmeTagExportVO hmeTagExportVO : hmeTagExportVOS) {
            List<MtExtendAttrVO1> extendAttrVO1List = extendAttrMap.get(hmeTagExportVO.getTagId());
            if (CollectionUtils.isEmpty(extendAttrVO1List)) {
                result.add(hmeTagExportVO);
            } else {
                Optional<MtExtendAttrVO1> processFlagOpt = extendAttrVO1List.stream().filter(ex -> StringUtils.equals(ex.getAttrName(), "PROCESS_FLAG")).findFirst();
                // 过滤掉过程数据标识
                List<MtExtendAttrVO1> filterAttrList = extendAttrVO1List.stream().filter(attr -> !StringUtils.equals(attr.getAttrName(), "PROCESS_FLAG")).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(filterAttrList)) {
                    // 若只有过程数据标识 则返回当前数据
                    if (processFlagOpt.isPresent()) {
                        hmeTagExportVO.setProcessFlag(processFlagOpt.get().getAttrValue());
                    }
                    result.add(hmeTagExportVO);
                } else {
                    for (MtExtendAttrVO1 mtExtendAttrVO1 : filterAttrList) {
                        if (!StringUtils.equals(mtExtendAttrVO1.getAttrName(), "PROCESS_FLAG")) {
                            HmeTagExportVO tagExportVO = new HmeTagExportVO();
                            BeanUtils.copyProperties(hmeTagExportVO, tagExportVO);
                            if (processFlagOpt.isPresent()) {
                                tagExportVO.setProcessFlag(processFlagOpt.get().getAttrValue());
                            }
                            // 扩展属性含义
                            List<MtExtendSettingsVO2> vo2List = attrMap.get(mtExtendAttrVO1.getAttrName());
                            if (CollectionUtils.isNotEmpty(vo2List)) {
                                tagExportVO.setAttrName(vo2List.get(0).getAttrMeaning());
                            }

                            tagExportVO.setAttrValue(mtExtendAttrVO1.getAttrValue());
                            result.add(tagExportVO);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    @ProcessLovValue
    public List<HmeTagGroupExportVO> tagGroupExport(Long tenantId, MtTagGroupDTO2 mtTagGroupDTO2) {
        List<HmeTagGroupExportVO> hmeTagGroupExportVOS = hmeCommonExportMapper.tagGroupExport(tenantId, mtTagGroupDTO2);
        return hmeTagGroupExportVOS;
    }

    @Override
    @ProcessLovValue
    public List<HmeTagGroupObjectExportVO> tagGroupObjectExport(Long tenantId, MtTagGroupDTO2 mtTagGroupDTO2) {
        List<HmeTagGroupObjectExportVO> hmeTagGroupExportVOS = hmeCommonExportMapper.tagGroupObjectExport(tenantId, mtTagGroupDTO2);
        // 表的所有扩展字段
        Map<String, List<MtExtendSettingsVO2>> attrMap = new HashMap<>();
        // 数据收集组的扩展字段
        Map<String, List<MtExtendAttrVO1>> extendAttrMap = new HashMap<>();
        List<String> tagGroupIdList = hmeTagGroupExportVOS.stream().map(HmeTagGroupObjectExportVO::getTagGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(tagGroupIdList)) {
            // 获取表所有扩展字段扩展
            List<MtExtendSettingsVO2> attrList = mtExtendSettingsRepository.customAttrQuery(tenantId, TABLE_NAME_TAG_GROUP, "Y");
            if (CollectionUtils.isNotEmpty(attrList)) {
                attrMap = attrList.stream().collect(Collectors.groupingBy(MtExtendSettingsVO2::getAttrName));
            }
            List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1() {{
                setKeyIdList(tagGroupIdList);
                setTableName(TABLE_NAME_TAG_GROUP);
                setAttrs(Collections.emptyList());
            }});
            List<MtExtendAttrVO1> filterAttrList = extendAttrVO1List.stream().filter(attr -> StringUtils.isNotBlank(attr.getAttrValue())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterAttrList)) {
                extendAttrMap = filterAttrList.stream().collect(Collectors.groupingBy(MtExtendAttrVO1::getKeyId));
            }
        }
        // 已扩展字段为最小维度 即多少扩展字段则有多少行数据
        List<HmeTagGroupObjectExportVO> result = new ArrayList<>();
        // 数据组类型
        MtGenTypeVO2 groupType = new MtGenTypeVO2();
        groupType.setModule("GENERAL");
        groupType.setTypeGroup("TAG_GROUP_TYPE");
        List<MtGenType> groupTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, groupType);
        // 状态处理
        MtGenTypeVO2 statusType = new MtGenTypeVO2();
        statusType.setModule("GENERAL");
        statusType.setTypeGroup("TAG_GROUP_STATUS");
        List<MtGenType> mtGenStatusList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, statusType);
        // 数据收集时点
        MtGenTypeVO2 collectionType = new MtGenTypeVO2();
        collectionType.setModule("GENERAL");
        collectionType.setTypeGroup("TAG_GROUP_COLLECTION_TIME");
        List<MtGenType> collectionTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, collectionType);
        // 业务类型
        MtGenTypeVO2 businessType = new MtGenTypeVO2();
        businessType.setModule("GENERAL");
        businessType.setTypeGroup("TAG_GROUP_BUSINESS_TYPE");
        List<MtGenType> businessTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, businessType);
        for (HmeTagGroupObjectExportVO hmeTagGroupExportVO : hmeTagGroupExportVOS) {
            List<MtExtendAttrVO1> extendAttrVO1List = extendAttrMap.get(hmeTagGroupExportVO.getTagGroupId());
            // 数据组类型
            if (StringUtils.isNotBlank(hmeTagGroupExportVO.getTagGroupType())) {
                Optional<MtGenType> genTypeOpt = groupTypeList.stream().filter(genType -> StringUtils.equals(genType.getTypeCode(), hmeTagGroupExportVO.getTagGroupType())).findFirst();
                hmeTagGroupExportVO.setTagGroupTypeMeaning(genTypeOpt.isPresent() ? genTypeOpt.get().getDescription() : "");
            }
            // 状态
            if (StringUtils.isNotBlank(hmeTagGroupExportVO.getStatus())) {
                Optional<MtGenType> genTypeOpt = mtGenStatusList.stream().filter(genType -> StringUtils.equals(genType.getTypeCode(), hmeTagGroupExportVO.getStatus())).findFirst();
                hmeTagGroupExportVO.setStatusMeaning(genTypeOpt.isPresent() ? genTypeOpt.get().getDescription() : "");
            }
            // 数据收集时点
            if (StringUtils.isNotBlank(hmeTagGroupExportVO.getCollectionTimeControl())) {
                Optional<MtGenType> genTypeOpt = collectionTypeList.stream().filter(genType -> StringUtils.equals(genType.getTypeCode(), hmeTagGroupExportVO.getCollectionTimeControl())).findFirst();
                hmeTagGroupExportVO.setCollectionTimeControlMeaning(genTypeOpt.isPresent() ? genTypeOpt.get().getDescription() : "");
            }
            // 业务类型
            if (StringUtils.isNotBlank(hmeTagGroupExportVO.getBusinessType())) {
                Optional<MtGenType> genTypeOpt = businessTypeList.stream().filter(genType -> StringUtils.equals(genType.getTypeCode(), hmeTagGroupExportVO.getBusinessType())).findFirst();
                hmeTagGroupExportVO.setBusinessTypeMeaning(genTypeOpt.isPresent() ? genTypeOpt.get().getDescription() : "");
            }
            if (CollectionUtils.isEmpty(extendAttrVO1List)) {
                result.add(hmeTagGroupExportVO);
            } else {
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    HmeTagGroupObjectExportVO exportVO = new HmeTagGroupObjectExportVO();
                    BeanUtils.copyProperties(hmeTagGroupExportVO, exportVO);
                    // 扩展属性含义
                    List<MtExtendSettingsVO2> vo2List = attrMap.get(mtExtendAttrVO1.getAttrName());
                    if (CollectionUtils.isNotEmpty(vo2List)) {
                        exportVO.setAttrName(vo2List.get(0).getAttrMeaning());
                    }
                    exportVO.setAttrValue(mtExtendAttrVO1.getAttrValue());
                    result.add(exportVO);
                }
            }
        }
        return result;
    }
}
