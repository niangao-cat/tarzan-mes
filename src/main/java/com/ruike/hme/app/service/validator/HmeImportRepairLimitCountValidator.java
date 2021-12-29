package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeRepairLimitCountDTO;
import com.ruike.hme.domain.entity.HmeRepairLimitCount;
import com.ruike.hme.domain.repository.HmeRepairLimitCountRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/11 10:09
 */
@ImportValidators({@ImportValidator(templateCode = "HME.REPAIR_LIMIT_COUNT")})
public class HmeImportRepairLimitCountValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeRepairLimitCountRepository repairLimitRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        Boolean flag = true;
        if (data != null && !"".equals(data)) {
            HmeRepairLimitCountDTO importDTO = null;
            try {
                importDTO = objectMapper.readValue(data, HmeRepairLimitCountDTO.class);
            } catch (IOException e) {
                return false;
            }

            flag = checkHmeRepairLimitCount(tenantId,importDTO);

        }
        return flag;
    }

    public Boolean checkHmeRepairLimitCount(Long tenantId, HmeRepairLimitCountDTO importDTO){
        HmeRepairLimitCount repairLimitCount = new HmeRepairLimitCount();
        repairLimitCount.setTenantId(tenantId);
        // 检查 materialCode 是否存在
        if (StringUtils.isNotBlank(importDTO.getMaterialCode())) {
            List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importDTO.getMaterialCode()));
            if (CollectionUtils.isEmpty(mtMaterialList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_BILL_EXERCISE_IMPORT_003", "HME",importDTO.getMaterialCode()));
                return false;
            } else {
                repairLimitCount.setMaterialId(mtMaterialList.get(0).getMaterialId());
            }
        }
        if (StringUtils.isNotBlank(importDTO.getWorkcellCode())){
            List<MtModWorkcell> workcellList = mtModWorkcellRepository.workcellForWkcCodeQuery(tenantId, Collections.singletonList(importDTO.getWorkcellCode()));
            List<MtModWorkcell> processList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(workcellList)){
                processList = workcellList.stream().filter(item->"PROCESS".equals(item.getWorkcellType())).collect(Collectors.toList());
            }
            if (CollectionUtils.isEmpty(processList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_REPAIR_LIMIT_COUNT_002", "HME", importDTO.getWorkcellCode()));
                return false;
            } else {
                repairLimitCount.setWorkcellId(processList.get(0).getWorkcellId());
            }
        }
        if (StringUtils.isNotBlank(importDTO.getLimitCount())){
            Long limitCount = Long.parseLong(importDTO.getLimitCount());
            if (limitCount < 0L) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_REPAIR_LIMIT_COUNT_003", "HME"));
                return false;
            }
        }
        List<HmeRepairLimitCount> repairLimitCountList = repairLimitRepository.select(repairLimitCount);

        if (CollectionUtils.isNotEmpty(repairLimitCountList)) {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_LIMIT_COUNT_001", "HME", importDTO.getMaterialCode(), importDTO.getWorkcellCode()));
            return false;
        }
        return true;
    }
}
