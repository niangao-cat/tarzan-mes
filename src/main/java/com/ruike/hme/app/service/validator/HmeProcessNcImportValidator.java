package com.ruike.hme.app.service.validator;

/**
 * 工序不良判定标准维护批量导入--校验
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/12 10:08
 */

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeProcessNcImportDTO;
import com.ruike.hme.domain.entity.HmeProcessNcDetail;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;
import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.domain.repository.HmeProcessNcHeaderRepository;
import com.ruike.hme.domain.repository.HmeProcessNcLineRepository;
import com.ruike.hme.domain.vo.HmeTagGroupImportVO;
import com.ruike.hme.infra.mapper.HmeProcessNcDetailMapper;
import com.ruike.hme.infra.mapper.HmeProcessNcHeaderMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ImportValidators({
        @ImportValidator(templateCode = "HME.PROCESS_NC_IMPORT")
})
public class HmeProcessNcImportValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private HmeProcessNcLineRepository hmeProcessNcLineRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;


    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (data != null && !"".equals(data)) {

            HmeProcessNcImportDTO importDTO = null;
            try {
                importDTO = objectMapper.readValue(data, HmeProcessNcImportDTO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            //物料
            List<MtMaterial> mtMaterialList = mtMaterialRepository.select(MtMaterial.FIELD_MATERIAL_CODE, importDTO.getMaterialCode());
            if (CollectionUtil.isEmpty(mtMaterialList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_MATERIAL_00001", "HME", importDTO.getMaterialCode()));
                return false;
            }

            //工艺
            if (StringUtils.isNotBlank(importDTO.getOperationName())) {
                List<MtOperation> operationList = mtOperationRepository.select(MtOperation.FIELD_OPERATION_NAME, importDTO.getOperationName());
                if (CollectionUtil.isEmpty(operationList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_OPERATION_00001", "HME", importDTO.getOperationName()));
                    return false;
                }
            }

            //数据项编码
            if (StringUtils.isBlank(importDTO.getTagCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TAGCODE_00001", "HME", importDTO.getTagCode()));
                return false;
            }

            List<MtTag> mtTagList = mtTagRepository.select(MtTag.FIELD_TAG_CODE, importDTO.getTagCode());
            if (CollectionUtil.isEmpty(mtTagList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TAGCODE_00001", "HME", importDTO.getTagCode()));
                return false;
            }
            //数据组编码
            if (StringUtils.isNotBlank(importDTO.getTagGroupCode())) {
                List<MtTagGroup> mtTagGroupList = mtTagGroupRepository.select(MtTagGroup.FIELD_TAG_GROUP_CODE, importDTO.getTagGroupCode());
                if (CollectionUtil.isEmpty(mtTagGroupList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TAGGROUP_00001", "HME", importDTO.getTagGroupCode()));
                    return false;
                }
            }

            //状态
            MtGenStatus mtGenStatus = new MtGenStatus();
            mtGenStatus.setModule("GENERAL");
            mtGenStatus.setStatusGroup("TAG_GROUP_STATUS");
            List<MtGenStatus> mtGenStatusList = mtGenStatusRepository.select(mtGenStatus);
            List<String> collect = mtGenStatusList.stream().map(MtGenStatus::getDescription).collect(Collectors.toList());
            //状态status不存在，报错：未定义的状态!
            if (!collect.contains(importDTO.getStatus())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_GENSTATUS_00001", "HME", importDTO.getStatus()));
                return false;
            }
            // 工序 有值 则需要存在
            if (StringUtils.isNotBlank(importDTO.getWorkcellCode())) {
                List<MtModWorkcell> workcellList = mtModWorkcellRepository.selectByCondition(Condition.builder(MtModWorkcell.class).andWhere(Sqls.custom()
                        .andEqualTo(MtModWorkcell.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtModWorkcell.FIELD_WORKCELL_CODE, importDTO.getWorkcellCode())).build());
                if (CollectionUtils.isEmpty(workcellList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_PROCESS_NC_IMPORT_001", "HME", importDTO.getWorkcellCode()));
                    return false;
                }
            }
        }
        return true;
    }
}
