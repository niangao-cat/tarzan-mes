package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeMaterialLotLabCodeDTO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.app.service.MtErrorMessageService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.io.IOException;
import java.util.Objects;

/**
 * HmeMaterialLotLabCodeValidator
 * 条码实验代码导入
 * @author: chaonan.hu@hand-china.com 2021/11/03 16:08:24
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.MATERIAL_LOT_LABCODE")
})
public class HmeMaterialLotLabCodeValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;
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
        if(StringUtils.isNotBlank(data)){
            HmeMaterialLotLabCodeDTO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeMaterialLotLabCodeDTO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 业务校验
            Boolean importDataFlag = importDataValidate(tenantId, importVO);
            if (!importDataFlag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 自定义校验
     *
     * @param tenantId 租户ID
     * @param importVO 导入单行数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/3 16:20:13
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeMaterialLotLabCodeDTO importVO){
        boolean flag = true;
        if(StringUtils.isBlank(importVO.getMaterialLotCode())){
            //实物条码必输
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LAB_CODE_006", "HME"));
            flag = false;
        }else {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setMaterialLotCode(importVO.getMaterialLotCode());
                setTenantId(tenantId);
            }});
            if(Objects.isNull(mtMaterialLot)){
                //实物条码不存在
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LAB_CODE_004", "HME", importVO.getMaterialLotCode()));
                flag = false;
            }else {
                //条码已失效
                if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_WO_INPUT_0004", "HME", importVO.getMaterialLotCode()));
                    flag = false;
                }
            }
        }
        if(StringUtils.isBlank(importVO.getLabCode())){
            //实验代码必输
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LAB_CODE_007", "HME"));
            flag = false;
        }
        return flag;
    }
}
