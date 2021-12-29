package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeRasterQualityDocDTO;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeDoc;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeDocRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.io.IOException;
import java.util.Objects;

/**
 * HmeRasterQualityDocValidator
 * 光栅质量文件导入
 * @author: chaonan.hu@hand-china.com 2021-01-19 17:01:23
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.RASTER_QUALITY_DOC")
})
public class HmeRasterQualityDocValidator extends ValidatorHandler {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeQuantityAnalyzeDocRepository hmeQuantityAnalyzeDocRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if(StringUtils.isNotBlank(data)){
            HmeRasterQualityDocDTO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeRasterQualityDocDTO.class);
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
     * 业务逻辑校验
     *
     * @param tenantId 租户ID
     * @param importVO 导入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/01/19 17:20:01
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeRasterQualityDocDTO importVO) {
        boolean flag = true;
        //物料编码校验
        MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {{
            setTenantId(tenantId);
            setMaterialCode(importVO.getMaterialCode());
        }});
        if(Objects.isNull(mtMaterial)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_TAG_FORMULA_0001", "HME", "物料", importVO.getMaterialCode()));
            flag = false;
        }
        //SN是否存在校验
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(importVO.getMaterialLotCode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_TAG_FORMULA_0001", "HME", "SN号", importVO.getMaterialLotCode()));
            flag = false;
        }else{
            //SN在质量文件头表不能存在数据校验
            HmeQuantityAnalyzeDoc hmeQuantityAnalyzeDoc = hmeQuantityAnalyzeDocRepository.selectOne(new HmeQuantityAnalyzeDoc() {{
                setTenantId(tenantId);
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
            }});
            if(Objects.nonNull(hmeQuantityAnalyzeDoc)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_QUALITY_DOC_0001", "HME", importVO.getMaterialLotCode()));
                flag = false;
            }
        }
        return flag;
    }
}
