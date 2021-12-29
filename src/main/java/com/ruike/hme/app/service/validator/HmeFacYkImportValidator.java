package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeFacYkImportVO;
import com.ruike.hme.domain.vo.HmeImportCosFunctionVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/7 11:19
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.FAC_YK")
})
public class HmeFacYkImportValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (data != null && !"".equals(data)) {
            HmeFacYkImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeFacYkImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 物料编码
            if (StringUtils.isBlank(importVO.getMaterialCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_001", "HME", "物料编码"));
                return false;
            }
            List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importVO.getMaterialCode()));
            if (CollectionUtils.isEmpty(mtMaterialList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_002", "HME", importVO.getMaterialCode()));
                return false;
            }
            // 芯片类型
            if (StringUtils.isBlank(importVO.getCosType())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_001", "HME", "芯片类型"));
                return false;
            }
            // FAC物料编码
            if (StringUtils.isBlank(importVO.getFacMaterialCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_001", "HME", "FAC物料编码"));
                return false;
            }
            List<MtMaterial> facMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importVO.getFacMaterialCode()));
            if (CollectionUtils.isEmpty(facMaterialList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_003", "HME", importVO.getFacMaterialCode()));
                return false;
            }
            // 工位编码
            if (StringUtils.isBlank(importVO.getWorkcellCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_001", "HME", "工位编码"));
                return false;
            }
            List<MtModWorkcell> workcellList = mtModWorkcellRepository.workcellForWkcCodeQuery(tenantId, Collections.singletonList(importVO.getWorkcellCode()));
            if (CollectionUtils.isEmpty(workcellList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_004", "HME", importVO.getWorkcellCode()));
                return false;
            }
            // 标准值
            if (importVO.getStandardValue() == null) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_001", "HME", "标准值"));
                return false;
            }
            // 允差
            if (importVO.getAllowDiffer() == null) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_FACYK_IMPORT_001", "HME", "允差"));
                return false;
            }
        }
        return true;
    }
}
