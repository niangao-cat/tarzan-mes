package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeMaterialLotLoadImportDTO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.io.IOException;

/**
 * description 物料批容器装载导入校验
 *
 * @author quan.luo@hand-china.com 2020/11/23 19:33
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.MATERIAL_LOT_LOAD")
})
public class HmeMaterialLotLoadDataValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (data != null && !"".equals(data)) {
            HmeMaterialLotLoadImportDTO importDTO = new HmeMaterialLotLoadImportDTO();
            try {
                importDTO = objectMapper.readValue(data, HmeMaterialLotLoadImportDTO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            MtContainer mtContainer = new MtContainer();
            mtContainer.setTenantId(tenantId);
            mtContainer.setContainerCode(importDTO.getContainerCode());
            mtContainer = mtContainerRepository.selectOne(mtContainer);
            // 容器存在性校验
            if (ObjectUtils.isEmpty(mtContainer)) {
                // 当前容器条码不存在
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_001", "HME"));
                return false;
            }
            // 状态校验
            if (!"CANRELEASE".equals(mtContainer.getStatus())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_002", "HME", mtContainer.getContainerCode()));
                return false;
            }
            MtMaterialLot mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotCode(importDTO.getLoadObjectCode());
            mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLot);
            if (ObjectUtils.isEmpty(mtMaterialLot)) {
                // 物料批不存在,请检查输入.${1}
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", importDTO.getLoadObjectCode()));
                return false;
            }
            if (!"Y".equals(mtMaterialLot.getEnableFlag())) {
                // 该条码不为有效,请检查!
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_QC_DOC_MATERIAL_LOT_002", "HME"));
                return false;
            }
        }
        return true;
    }
}
