package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeSsnInspectDetail;
import com.ruike.hme.domain.entity.HmeSsnInspectHeader;
import com.ruike.hme.domain.entity.HmeSsnInspectLine;
import com.ruike.hme.domain.repository.HmeSsnInspectDetailRepository;
import com.ruike.hme.domain.repository.HmeSsnInspectLineRepository;
import com.ruike.hme.domain.vo.HmeSsnInspectImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeSsnInspectHeaderMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/8 11:23
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.SSN_INSPECT")
})
public class HmeSsnInspectImportValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;
    @Autowired
    private HmeSsnInspectHeaderMapper hmeSsnInspectHeaderMapper;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeSsnInspectLineRepository hmeSsnInspectLineRepository;
    @Autowired
    private HmeSsnInspectDetailRepository hmeSsnInspectDetailRepository;


    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (data != null && !"".equals(data)) {

            HmeSsnInspectImportVO importDTO = null;
            try {
                importDTO = objectMapper.readValue(data, HmeSsnInspectImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 标准件编码
            if (StringUtils.isBlank(importDTO.getStandardSnCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_IMPORT_001", "HME", "标准件编码"));
                return false;
            }
            // 物料编码
            if (StringUtils.isBlank(importDTO.getMaterialCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_IMPORT_001", "HME", "物料编码"));
                return false;
            }
            List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importDTO.getMaterialCode()));
            if (CollectionUtils.isEmpty(mtMaterialList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_IMPORT_002", "HME", importDTO.getMaterialCode()));
                return false;
            }
            importDTO.setMaterialId(mtMaterialList.get(0).getMaterialId());
            // 序号
            if (importDTO.getSequence() == null) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_IMPORT_001", "HME", "序号"));
                return false;
            }
            // 检验项
            if (StringUtils.isBlank(importDTO.getTagCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_IMPORT_001", "HME", "检验项编码"));
                return false;
            }
            List<MtTag> tagList = mtTagRepository.selectByCodeList(tenantId, Collections.singletonList(importDTO.getTagCode()));
            if (CollectionUtils.isEmpty(tagList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_IMPORT_003", "HME", importDTO.getTagCode()));
                return false;
            }
            importDTO.setTagId(tagList.get(0).getTagId());
            // 耦合项目组编码
            if (StringUtils.isNotBlank(importDTO.getTagGroupCode())) {
                List<MtTagGroup> mtTagGroups = mtTagGroupRepository.selectTagGroupByTagGroupCodes(tenantId, Collections.singletonList(importDTO.getTagGroupCode()));
                if (CollectionUtils.isEmpty(mtTagGroups)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_004", "HME", importDTO.getTagGroupCode()));
                    return false;
                }
                importDTO.setTagGroupId(mtTagGroups.get(0).getTagGroupId());
            }
            // 影响耦合标识为Y时耦合允差不能为空
            if (HmeConstants.ConstantValue.YES.equals(importDTO.getCoupleFlag())) {
                if (importDTO.getAllowDiffer() == null) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_005", "HME", importDTO.getStandardSnCode()));
                    return false;
                }
            }
            // COS耦合标志为Y时耦合cos位置和耦合允差不能为空
            if (HmeConstants.ConstantValue.YES.equals(importDTO.getCosCoupleFlag())) {
                if (importDTO.getCosPos() == null || importDTO.getAllowDiffer() == null) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_006", "HME", importDTO.getStandardSnCode()));
                    return false;
                }
            }
            if (StringUtils.isNotBlank(importDTO.getWorkcellCode())) {
                List<MtModWorkcell> workcellList = mtModWorkcellRepository.workcellForWkcCodeQuery(tenantId, Collections.singletonList(importDTO.getWorkcellCode()));
                if (CollectionUtils.isEmpty(workcellList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_007", "HME", importDTO.getWorkcellCode()));
                    return false;
                }
                importDTO.setWorkcellId(workcellList.get(0).getWorkcellId());
            }
            // 标准件编码+物料编码+芯片类型+工作方式+工位编码
            List<HmeSsnInspectHeader> inspectHeaderList = hmeSsnInspectHeaderMapper.querySsnInspectHeader(tenantId, importDTO);
            // 新增重复校验
            if ("INCREASE".equals(importDTO.getImportType())) {
                if (CollectionUtils.isNotEmpty(inspectHeaderList)) {
                    // 头存在 行重复则报错
                    String tagId = importDTO.getTagId();
                    List<HmeSsnInspectLine> ssnInspectLineList = hmeSsnInspectLineRepository.select(new HmeSsnInspectLine() {{
                        setTagId(tagId);
                        setSsnInspectHeaderId(inspectHeaderList.get(0).getSsnInspectHeaderId());
                    }});
                    if (CollectionUtils.isNotEmpty(ssnInspectLineList)) {
                        // 行存在  明细为空时 报行重复  不为空  则校验 明细是否重复
                        if (StringUtils.isBlank(importDTO.getTagGroupCode())) {
                            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_SSN_INSPECT_IMPORT_010", "HME", importDTO.getStandardSnCode(), importDTO.getMaterialCode(), importDTO.getCosType(), importDTO.getWorkWay(), importDTO.getWorkcellCode(), importDTO.getTagCode()));
                            return false;
                        } else {
                            String tagGroupId = importDTO.getTagGroupId();
                            List<HmeSsnInspectDetail> inspectDetailList = hmeSsnInspectDetailRepository.select(new HmeSsnInspectDetail() {{
                                setSsnInspectLineId(ssnInspectLineList.get(0).getSsnInspectLineId());
                                setTagGroupId(tagGroupId);
                            }});
                            if (CollectionUtils.isNotEmpty(inspectDetailList)) {
                                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_SSN_INSPECT_IMPORT_011", "HME", importDTO.getStandardSnCode(), importDTO.getMaterialCode(), importDTO.getCosType(), importDTO.getWorkWay(), importDTO.getWorkcellCode(), importDTO.getTagCode(), importDTO.getTagGroupCode()));
                                return false;
                            }
                        }
                    }
                }
            }
            // 更新重复校验
            if ("UPDATE".equals(importDTO.getImportType())) {
                // 头不存在 则报错
                if (CollectionUtils.isEmpty(inspectHeaderList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_008", "HME", importDTO.getStandardSnCode(), importDTO.getMaterialCode(), importDTO.getCosType(), importDTO.getWorkWay(), importDTO.getWorkcellCode()));
                    return false;
                }
                // 行不存在也报错
                String tagId = importDTO.getTagId();
                List<HmeSsnInspectLine> ssnInspectLineList = hmeSsnInspectLineRepository.select(new HmeSsnInspectLine() {{
                    setTagId(tagId);
                    setSsnInspectHeaderId(inspectHeaderList.get(0).getSsnInspectHeaderId());
                }});
                if (CollectionUtils.isEmpty(ssnInspectLineList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_009", "HME", importDTO.getStandardSnCode(), importDTO.getTagCode()));
                    return false;
                }
            }
        }
        return true;
    }

}
