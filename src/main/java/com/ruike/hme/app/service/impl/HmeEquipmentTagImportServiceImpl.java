package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeEqManageTag;
import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;
import com.ruike.hme.domain.repository.HmeEqManageTagGroupRepository;
import com.ruike.hme.domain.repository.HmeEqManageTagRepository;
import com.ruike.hme.domain.vo.HmeEquipmentTagVO;
import com.ruike.hme.infra.mapper.HmeEqManageTagGroupMapper;
import com.ruike.hme.infra.mapper.HmeEqManageTagMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.infra.mapper.MtModAreaMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 设备点检&保养项目维护导入
 *
 * @author sanfeng.zhang@hand-china.com 2021/5/8 15:19
 */
@ImportService(templateCode = "HME.EQUIPMENT_TAG")
public class HmeEquipmentTagImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModAreaMapper mtModAreaMapper;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    @Autowired
    private HmeEqManageTagGroupMapper hmeEqManageTagGroupMapper;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private HmeEqManageTagMapper hmeEqManageTagMapper;
    @Autowired
    private HmeEqManageTagGroupRepository hmeEqManageTagGroupRepository;
    @Autowired
    private HmeEqManageTagRepository hmeEqManageTagRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public Boolean doImport(List<String> data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        List<HmeEquipmentTagVO> importVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(data)) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("GENERAL");
            mtGenTypeVO2.setTypeGroup("TAG_VALUE_TYPE");
            List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            MtGenTypeVO2 mtGenTypeVO21 = new MtGenTypeVO2();
            mtGenTypeVO21.setModule("GENERAL");
            mtGenTypeVO21.setTypeGroup("TAG_COLLECTION_METHOD");
            List<MtGenType> mtGenTypes2 = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO21);

            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.EQUIPMENT_CATEGORY", tenantId);
            for (String vo : data) {
                HmeEquipmentTagVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeEquipmentTagVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 设备类别
                if (StringUtils.isNotBlank(importVO.getEquipmentCategoryDesc())) {
                    String description = importVO.getEquipmentCategoryDesc();
                    Optional<LovValueDTO> firstPot = valueDTOList.stream().filter(ec -> StringUtils.equals(ec.getMeaning(), description)).findFirst();
                    if (!firstPot.isPresent()) {
                        throw new MtException("HME_EQUIPMENT_TAG_IMPORT_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_011", "HME", importVO.getEquipmentCategoryDesc()));
                    }
                    importVO.setEquipmentCategory(firstPot.get().getValue());
                }
                // 部门
                if (StringUtils.isNotBlank(importVO.getBusinessName())) {
                    List<MtModArea> mtModAreaList = mtModAreaMapper.selectByCondition(Condition.builder(MtModArea.class).andWhere(Sqls.custom()
                            .andEqualTo(MtModArea.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtModArea.FIELD_DESCRIPTION, importVO.getBusinessName())
                            .andEqualTo(MtModArea.FIELD_AREA_CATEGORY, "SYB")).build());
                    if (CollectionUtils.isEmpty(mtModAreaList)) {
                        throw new MtException("HME_EQUIPMENT_TAG_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_001", "HME", importVO.getBusinessName()));
                    }
                    importVO.setBusinessId(mtModAreaList.get(0).getAreaId());
                }
                // 工艺
                if (StringUtils.isNotBlank(importVO.getOperationName())) {
                    List<MtOperation> mtOperationList = mtOperationRepository.selectByCondition(Condition.builder(MtOperation.class).andWhere(Sqls.custom()
                            .andEqualTo(MtOperation.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtOperation.FIELD_OPERATION_NAME, importVO.getOperationName())).build());
                    if (CollectionUtils.isEmpty(mtOperationList)) {
                        throw new MtException("HME_EQUIPMENT_TAG_IMPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_002", "HME", importVO.getOperationName()));
                    }
                    importVO.setOperationId(mtOperationList.get(0).getOperationId());
                }
                // 项目组编码
                if (StringUtils.isNotBlank(importVO.getTagGroupCode())) {
                    List<MtTagGroup> mtTagGroups = mtTagGroupRepository.selectByCondition(Condition.builder(MtTagGroup.class).andWhere(Sqls.custom()
                            .andEqualTo(MtTagGroup.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtTagGroup.FIELD_TAG_GROUP_CODE, importVO.getTagGroupCode())).build());
                    if (CollectionUtils.isEmpty(mtTagGroups)) {
                        throw new MtException("HME_EQUIPMENT_TAG_IMPORT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_003", "HME", importVO.getTagGroupCode()));
                    }
                    importVO.setTagGroupId(mtTagGroups.get(0).getTagGroupId());
                }
                // 项目编码
                if (StringUtils.isNotBlank(importVO.getTagCode())) {
                    List<MtTag> mtTagList = mtTagRepository.selectByCondition(Condition.builder(MtTag.class).andWhere(Sqls.custom()
                            .andEqualTo(MtTag.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtTag.FIELD_TAG_CODE, importVO.getTagCode())).build());
                    if (CollectionUtils.isEmpty(mtTagList)) {
                        throw new MtException("HME_EQUIPMENT_TAG_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_004", "HME", importVO.getTagCode()));
                    }
                    importVO.setTagId(mtTagList.get(0).getTagId());
                    importVO.setTagDescriptions(mtTagList.get(0).getTagDescription());
                }
                // 数据类型
                if (StringUtils.isNotBlank(importVO.getValueType())) {
                    String description = importVO.getValueType();
                    Optional<MtGenType> firstOpt = mtGenTypes.stream().filter(vt -> StringUtils.equals(vt.getDescription(), description)).findFirst();
                    if (!firstOpt.isPresent()) {
                        throw new MtException("HME_EQUIPMENT_TAG_IMPORT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_005", "HME", importVO.getTagCode()));
                    }
                    importVO.setValueType(firstOpt.get().getTypeCode());
                }
                // 收集方式
                if (StringUtils.isNotBlank(importVO.getCollectionMethod())) {
                    String description = importVO.getCollectionMethod();
                    Optional<MtGenType> firstOpt = mtGenTypes2.stream().filter(vt -> StringUtils.equals(vt.getDescription(), description)).findFirst();
                    if (!firstOpt.isPresent()) {
                        throw new MtException("HME_EQUIPMENT_TAG_IMPORT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_006", "HME", importVO.getTagCode()));
                    }
                    importVO.setCollectionMethod(firstOpt.get().getTypeCode());
                }
                // 单位
                if (StringUtils.isNotBlank(importVO.getUomName())) {
                    List<MtUom> mtUomList = mtUomRepository.selectByCondition(Condition.builder(MtUom.class).andWhere(Sqls.custom()
                            .andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtUom.FIELD_UOM_NAME, importVO.getUomName())).build());
                    if (CollectionUtils.isEmpty(mtUomList)) {
                        throw new MtException("HME_EQUIPMENT_TAG_IMPORT_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_007", "HME", importVO.getTagCode()));
                    }
                }
                importVO.setSiteId(defaultSiteId);
                importVOList.add(importVO);
            }
        }
        // 处理数据
        this.handleEqManageList(tenantId, importVOList);
        return true;
    }

    private void handleEqManageList(Long tenantId, List<HmeEquipmentTagVO> importVOList) {
        if (CollectionUtils.isNotEmpty(importVOList)) {
            List<HmeEqManageTagGroup> updateHeadList = new ArrayList<>();
            List<HmeEqManageTag> updateLineList = new ArrayList<>();

            // 组织+部门+设备类别+工艺编码+项目组编码+设备管理类型 分组
            Map<Object, List<HmeEquipmentTagVO>> importMap = importVOList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
            for (Map.Entry<Object, List<HmeEquipmentTagVO>> importEntry : importMap.entrySet()) {
                List<HmeEquipmentTagVO> value = importEntry.getValue();
                HmeEquipmentTagVO importVO = value.get(0);
                List<HmeEqManageTagGroup> hmeEqManageTagGroups = hmeEqManageTagGroupMapper.queryEqManageHead(tenantId, importVO);
                Map<String, List<HmeEquipmentTagVO>> resultMap = value.stream().collect(Collectors.groupingBy(HmeEquipmentTagVO::getImportType));
                for (Map.Entry<String, List<HmeEquipmentTagVO>> resultEntry : resultMap.entrySet()) {
                    if ("INCREASE".equals(resultEntry.getKey())) {
                        // 新增 都不存在 都新增 行不存在 新增行
                        String manageTagGroupId = "";
                        if (CollectionUtils.isEmpty(hmeEqManageTagGroups)) {
                            HmeEqManageTagGroup hmeEqManageTagGroup = new HmeEqManageTagGroup();
                            BeanUtils.copyProperties(importVO, hmeEqManageTagGroup);
                            hmeEqManageTagGroup.setTenantId(tenantId);
                            hmeEqManageTagGroupRepository.insertSelective(hmeEqManageTagGroup);
                            manageTagGroupId = hmeEqManageTagGroup.getManageTagGroupId();
                        } else {
                            manageTagGroupId = hmeEqManageTagGroups.get(0).getManageTagGroupId();
                        }
                        HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
                        List<HmeEquipmentTagVO> importVOs = resultEntry.getValue();
                        for (HmeEquipmentTagVO vo : importVOs) {
                            BeanUtils.copyProperties(vo, hmeEqManageTag);
                            hmeEqManageTag.setManageTagGroupId(manageTagGroupId);
                            hmeEqManageTag.setTenantId(tenantId);
                            hmeEqManageTagRepository.insertSelective(hmeEqManageTag);
                        }
                    } else if ("UPDATE".equals(resultEntry.getKey())) {
                        HmeEqManageTagGroup hmeEqManageTagGroup = new HmeEqManageTagGroup();
                        BeanUtils.copyProperties(importVO, hmeEqManageTagGroup);
                        hmeEqManageTagGroup.setTenantId(tenantId);
                        hmeEqManageTagGroup.setManageTagGroupId(hmeEqManageTagGroups.get(0).getManageTagGroupId());
                        updateHeadList.add(hmeEqManageTagGroup);
                        List<HmeEquipmentTagVO> importVOs = resultEntry.getValue();
                        for (HmeEquipmentTagVO vo : importVOs) {
                            List<HmeEqManageTag> hmeEqManageTags = hmeEqManageTagMapper.selectByCondition(Condition.builder(HmeEqManageTag.class).andWhere(Sqls.custom()
                                    .andEqualTo(HmeEqManageTag.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(HmeEqManageTag.FIELD_MANAGE_TAG_GROUP_ID, hmeEqManageTagGroups.get(0).getManageTagGroupId())
                                    .andEqualTo(HmeEqManageTag.FIELD_TAG_ID, vo.getTagId())).build());
                            HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
                            BeanUtils.copyProperties(vo, hmeEqManageTag);
                            hmeEqManageTag.setManageTagId(hmeEqManageTags.get(0).getManageTagId());
                            hmeEqManageTag.setManageTagGroupId(hmeEqManageTagGroups.get(0).getManageTagGroupId());
                            hmeEqManageTag.setTenantId(tenantId);
                            updateLineList.add(hmeEqManageTag);
                        }
                    }
                }
            }
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            if (CollectionUtils.isNotEmpty(updateHeadList)) {
                List<List<HmeEqManageTagGroup>> splitSqlList = InterfaceUtils.splitSqlList(updateHeadList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeEqManageTagGroup> domains : splitSqlList) {
                    hmeEqManageTagGroupMapper.batchHeaderUpdate(tenantId, userId, domains);
                }
            }
            if (CollectionUtils.isNotEmpty(updateLineList)) {
                List<List<HmeEqManageTag>> splitSqlList = InterfaceUtils.splitSqlList(updateLineList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeEqManageTag> domains : splitSqlList) {
                    hmeEqManageTagMapper.batchLineUpdate(tenantId, userId, domains);
                }
            }
        }
    }

    private String spliceStr (HmeEquipmentTagVO tagVO) {
        StringBuffer sb = new StringBuffer();
        sb.append(tagVO.getSiteId());
        sb.append(tagVO.getBusinessId());
        sb.append(tagVO.getEquipmentCategory());
        sb.append(tagVO.getOperationId());
        sb.append(tagVO.getTagGroupId());
        sb.append(tagVO.getManageType());
        return sb.toString();
    }

}
