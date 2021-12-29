package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeSnLabCodeDTO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.util.Objects;

/**
 * HmeSnLabCodeValidator
 * SN实验代码导入
 * @author: chaonan.hu@hand-china.com 2021/04/01 09:38:47
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.SN_LABCODE")
})
public class HmeSnLabCodeValidator extends ValidatorHandler {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if(StringUtils.isNotBlank(data)){
            HmeSnLabCodeDTO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeSnLabCodeDTO.class);
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
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/1 09:40:57
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeSnLabCodeDTO importVO){
        boolean flag = true;
        //站点是否存在，是否有效校验
        MtModSite mtModSite = mtModSiteRepository.selectOne(new MtModSite() {{
            setTenantId(tenantId);
            setSiteCode(importVO.getSiteCode());
        }});
        if(Objects.isNull(mtModSite) || !HmeConstants.ConstantValue.YES.equals(mtModSite.getEnableFlag())){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_EXCEL_IMPORT_043", "HME", importVO.getSiteCode()));
            flag = false;
        }else{
            //条码是否存在校验
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(importVO.getMaterialLotCode());
                setSiteId(mtModSite.getSiteId());
            }});
            if(Objects.isNull(mtMaterialLot)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_MATERIAL_LOT_0012", "HME", importVO.getMaterialLotCode(), importVO.getSiteCode()));
                flag = false;
            }
            //工艺版本是否存在校验
            MtOperation mtOperation = mtOperationRepository.selectOne(new MtOperation() {{
                setTenantId(tenantId);
                setSiteId(mtModSite.getSiteId());
                setOperationName(importVO.getOperationName());
                setRevision(importVO.getRevision());
            }});
            if(Objects.isNull(mtOperation)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_MATERIAL_LOT_0013", "HME", importVO.getSiteCode(), importVO.getOperationName(), importVO.getRevision()));
                flag = false;
            }
        }
        return flag;
    }
}
