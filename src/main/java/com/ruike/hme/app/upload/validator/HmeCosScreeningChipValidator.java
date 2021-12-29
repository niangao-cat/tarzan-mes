package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeCosChipNumImportVO;
import com.ruike.hme.domain.vo.HmeCosScreeningChipImportVo;
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
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * HmeScreeningChipValidator
 * COS筛选后芯片导入
 * @author: chaonan.hu@hand-china.com 2020-10-10 21:20:12
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.COS_FILTERED")
})
public class HmeCosScreeningChipValidator extends ValidatorHandler {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if(StringUtils.isNotBlank(data)){
            HmeCosScreeningChipImportVo importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeCosScreeningChipImportVo.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
//            // 非空校验
//            Boolean isNullFlag = isNullValidate(tenantId, importVO);
//            if (!isNullFlag) {
//                return false;
//            }
            // 业务校验
            Boolean importDataFlag = importDataValidate(tenantId, importVO);
            if (!importDataFlag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 非空校验
     *
     * @param tenantId
     * @param importVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/12 09:41:10
     * @return java.lang.Boolean
     */
    private Boolean isNullValidate(Long tenantId, HmeCosScreeningChipImportVo importVO) {
        boolean flag = true;
        if(StringUtils.isBlank(importVO.getSiteCode())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0001"));
            flag = false;
        }
        if(StringUtils.isBlank(importVO.getMaterialLotCode())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0002"));
            flag = false;
        }
        if(StringUtils.isBlank(importVO.getMaterialCode())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0003"));
            flag = false;
        }
        if(StringUtils.isBlank(importVO.getQtyStr())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0004"));
            flag = false;
        }else{
            try {
                BigDecimal qty = new BigDecimal(importVO.getQtyStr());
            }catch (Exception ex){
                getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                        "HME_COS_SCREENING_CHIP_0013"));
                flag = false;
            }
        }
        if(StringUtils.isBlank(importVO.getVirtualNum())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0006"));
            flag = false;
        }
        if(StringUtils.isBlank(importVO.getChipLocation())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0008"));
            flag = false;
        }
        if(StringUtils.isBlank(importVO.getWafer())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0009"));
            flag = false;
        }
        if(StringUtils.isBlank(importVO.getCosType())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0010"));
            flag = false;
        }
        if(StringUtils.isBlank(importVO.getChipSequence())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0011"));
            flag = false;
        }
        if(Objects.isNull(importVO.getCurrent())){
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                    "HME_COS_SCREENING_CHIP_0012"));
            flag = false;
        }
        return flag;
    }

    /**
     * 业务逻辑校验
     *
     * @param tenantId
     * @param importVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/12 10:08:01
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeCosScreeningChipImportVo importVO) {
        boolean flag = true;
        //公司编码
        MtModSite mtModSite = mtModSiteRepository.selectOne(new MtModSite() {{
            setTenantId(tenantId);
            setSiteCode(importVO.getSiteCode());
        }});
        if(Objects.isNull(mtModSite)){
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEL_IMPORT_021", "HME", importVO.getSiteCode()));
            flag = false;
        }
        //条码号
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(importVO.getMaterialLotCode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0017", "ASSEMBLE", importVO.getMaterialLotCode()));
            flag = false;
        }
        //物料
        MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {{
            setTenantId(tenantId);
            setMaterialCode(importVO.getMaterialCode());
        }});
        if(Objects.isNull(mtMaterial)){
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_0053", "MATERIAL", importVO.getMaterialCode()));
            flag = false;
        }
        //2020-10-27 add by chaonan.hu for zhenyong.ban 增加校验物料和条码物料是否一致
        if(Objects.nonNull(mtMaterialLot) && Objects.nonNull(mtMaterial)){
            if(!mtMaterialLot.getMaterialId().equals(mtMaterial.getMaterialId())){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_FUNCTION_001", "HME"));
                flag = false;
            }
        }
//        //货位
//        MtModLocator mtModLocator = mtModLocatorRepository.selectOne(new MtModLocator() {{
//            setTenantId(tenantId);
//            setLocatorCode(importVO.getLocatorCode());
//        }});
//        if(Objects.isNull(mtModLocator) || "N".equals(mtModLocator.getEnableFlag())){
//            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "WMS_COST_CENTER_0060", "WMS"));
//            flag = false;
//        }
        //芯片位置
        if(importVO.getChipLocation().length() != 2){
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_SCREENING_CHIP_0015", "HME", importVO.getMaterialLotCode(),importVO.getChipLocation()));
            flag = false;
        }else{
            if(!importVO.getChipLocation().substring(0, 1).matches("[A-Z]+") ||
                    !importVO.getChipLocation().substring(1, 2).matches("^[0-9]*$")){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_SCREENING_CHIP_0015", "HME", importVO.getMaterialLotCode(),importVO.getChipLocation()));
                flag = false;
            }
        }
        return flag;
    }
}
