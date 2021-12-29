package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeEqManageTag;
import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import com.ruike.hme.domain.vo.HmeEquipmentTagVO;
import com.ruike.hme.infra.mapper.HmeEqManageTagGroupMapper;
import com.ruike.hme.infra.mapper.HmeEqManageTagMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
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
import java.util.List;
import java.util.Optional;

/**
 * 设备点检&保养项目维护导入
 *
 * @author sanfeng.zhang@hand-china.com 2021/5/8 15:18
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.EQUIPMENT_TAG")
})
public class HmeEquipmentTagImportValidator extends ValidatorHandler {

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
    private LovAdapter lovAdapter;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if (data != null && !"".equals(data)) {
            HmeEquipmentTagVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeEquipmentTagVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 设备类别
            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.EQUIPMENT_CATEGORY", tenantId);
            if (StringUtils.isNotBlank(importVO.getEquipmentCategoryDesc())) {
                String description = importVO.getEquipmentCategoryDesc();
                Optional<LovValueDTO> firstPot = valueDTOList.stream().filter(ec -> StringUtils.equals(ec.getMeaning(), description)).findFirst();
                if (!firstPot.isPresent()) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_011", "HME", importVO.getEquipmentCategoryDesc()));
                    return false;
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
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_001", "HME", importVO.getBusinessName()));
                    return false;
                }
                importVO.setBusinessId(mtModAreaList.get(0).getAreaId());
            }
            // 工艺
            if (StringUtils.isNotBlank(importVO.getOperationName())) {
                List<MtOperation> mtOperationList = mtOperationRepository.selectByCondition(Condition.builder(MtOperation.class).andWhere(Sqls.custom()
                        .andEqualTo(MtOperation.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtOperation.FIELD_OPERATION_NAME, importVO.getOperationName())).build());
                if (CollectionUtils.isEmpty(mtOperationList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_002", "HME", importVO.getOperationName()));
                    return false;
                }
                importVO.setOperationId(mtOperationList.get(0).getOperationId());
            }
            // 项目组编码
            if (StringUtils.isNotBlank(importVO.getTagGroupCode())) {
                List<MtTagGroup> mtTagGroups = mtTagGroupRepository.selectByCondition(Condition.builder(MtTagGroup.class).andWhere(Sqls.custom()
                        .andEqualTo(MtTagGroup.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtTagGroup.FIELD_TAG_GROUP_CODE, importVO.getTagGroupCode())).build());
                if (CollectionUtils.isEmpty(mtTagGroups)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_003", "HME", importVO.getTagGroupCode()));
                    return false;
                }
                importVO.setTagGroupId(mtTagGroups.get(0).getTagGroupId());
            }
            // 项目编码
            if (StringUtils.isNotBlank(importVO.getTagCode())) {
                List<MtTag> mtTagList = mtTagRepository.selectByCondition(Condition.builder(MtTag.class).andWhere(Sqls.custom()
                        .andEqualTo(MtTag.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtTag.FIELD_TAG_CODE, importVO.getTagCode())).build());
                if (CollectionUtils.isEmpty(mtTagList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_004", "HME", importVO.getTagCode()));
                    return false;
                }
                importVO.setTagId(mtTagList.get(0).getTagId());
            }
            // 数据类型
            if (StringUtils.isNotBlank(importVO.getValueType())) {
                MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
                mtGenTypeVO2.setModule("GENERAL");
                mtGenTypeVO2.setTypeGroup("TAG_VALUE_TYPE");
                List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                String description = importVO.getValueType();
                Optional<MtGenType> firstOpt = mtGenTypes.stream().filter(vt -> StringUtils.equals(vt.getDescription(), description)).findFirst();
                if (!firstOpt.isPresent()) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_005", "HME", importVO.getValueType()));
                    return false;
                }
            }
            // 收集方式
            if (StringUtils.isNotBlank(importVO.getCollectionMethod())) {
                MtGenTypeVO2 mtGenTypeVO21 = new MtGenTypeVO2();
                mtGenTypeVO21.setModule("GENERAL");
                mtGenTypeVO21.setTypeGroup("TAG_COLLECTION_METHOD");
                List<MtGenType> mtGenTypes2 = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO21);
                String description = importVO.getCollectionMethod();
                Optional<MtGenType> firstOpt = mtGenTypes2.stream().filter(vt -> StringUtils.equals(vt.getDescription(), description)).findFirst();
                if (!firstOpt.isPresent()) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_006", "HME", importVO.getCollectionMethod()));
                    return false;
                }
            }
            // 单位
            if (StringUtils.isNotBlank(importVO.getUomName())) {
                List<MtUom> mtUomList = mtUomRepository.selectByCondition(Condition.builder(MtUom.class).andWhere(Sqls.custom()
                        .andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtUom.FIELD_UOM_NAME, importVO.getUomName())).build());
                if (CollectionUtils.isEmpty(mtUomList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_007", "HME", importVO.getUomName()));
                    return false;
                }
            }
            importVO.setSiteId(defaultSiteId);
            List<HmeEqManageTagGroup> hmeEqManageTagGroups = hmeEqManageTagGroupMapper.queryEqManageHead(tenantId, importVO);
            if (StringUtils.equals(importVO.getImportType(), "INCREASE")) {
                // 新增
                if (CollectionUtils.isNotEmpty(hmeEqManageTagGroups)) {
                    List<HmeEqManageTag> hmeEqManageTags = hmeEqManageTagMapper.selectByCondition(Condition.builder(HmeEqManageTag.class).andWhere(Sqls.custom()
                            .andEqualTo(HmeEqManageTag.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEqManageTag.FIELD_MANAGE_TAG_GROUP_ID, hmeEqManageTagGroups.get(0).getManageTagGroupId())
                            .andEqualTo(HmeEqManageTag.FIELD_TAG_ID, importVO.getTagId())).build());
                    if (CollectionUtils.isNotEmpty(hmeEqManageTags)) {
                        getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_TAG_IMPORT_008", "HME",importVO.getBusinessName(),importVO.getEquipmentCategory(), importVO.getOperationName(), importVO.getTagGroupCode(), importVO.getManageType() , importVO.getTagCode()));
                        return false;
                    }
                }
            } else if (StringUtils.equals(importVO.getImportType(), "UPDATE")) {
                // 更新
                if (CollectionUtils.isEmpty(hmeEqManageTagGroups)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_009", "HME",importVO.getBusinessName(),importVO.getEquipmentCategory(), importVO.getOperationName(), importVO.getTagGroupCode(), importVO.getManageType() , importVO.getTagCode()));
                    return false;
                }
                List<HmeEqManageTag> hmeEqManageTags = hmeEqManageTagMapper.selectByCondition(Condition.builder(HmeEqManageTag.class).andWhere(Sqls.custom()
                        .andEqualTo(HmeEqManageTag.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEqManageTag.FIELD_MANAGE_TAG_GROUP_ID, hmeEqManageTagGroups.get(0).getManageTagGroupId())
                        .andEqualTo(HmeEqManageTag.FIELD_TAG_ID, importVO.getTagId())).build());
                if (CollectionUtils.isEmpty(hmeEqManageTags)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_TAG_IMPORT_010", "HME",importVO.getBusinessName(),importVO.getEquipmentCategory(), importVO.getOperationName(), importVO.getTagGroupCode(), importVO.getManageType() , importVO.getTagCode()));
                    return false;
                }
            }
        }
        return true;
    }
}
