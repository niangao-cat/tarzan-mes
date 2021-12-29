package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeSnReplaceDTO;
import com.ruike.hme.domain.vo.HmeCosScreeningChipImportVo;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * HmeSnReplaceValidator
 * SN替换导入
 * @author: chaonan.hu@hand-china.com 2020-11-04 14:29:23
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.SN_REPLACE")
})
public class HmeSnReplaceValidator extends ValidatorHandler {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEoRepository mtEoRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if(StringUtils.isNotBlank(data)){
            HmeSnReplaceDTO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeSnReplaceDTO.class);
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
     * @param tenantId
     * @param importVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/4 14:33:01
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeSnReplaceDTO importVO) {
        boolean flag = true;
        //查询当前用户默认站点
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if(StringUtils.isEmpty(siteId)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
            flag = false;
        }
        //原SN校验 {条码不存在, 请检查}
        MtMaterialLot oldMtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setSiteId(siteId);
            setMaterialLotCode(importVO.getOldMaterialLotCode());
        }});
        if(Objects.isNull(oldMtMaterialLot)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0006", "WMS", importVO.getOldMaterialLotCode()));
            flag = false;
        }else{
            //条码已失效,请检查!
            if (!YES.equals(oldMtMaterialLot.getEnableFlag())) {
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0004", "HME", importVO.getOldMaterialLotCode()));
                flag = false;
            }
            //条码不为在制品,无法升级
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(oldMtMaterialLot.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isEmpty(mtExtendAttrVOS) || !YES.equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_SN_REPLACE_0001", "HME", importVO.getOldMaterialLotCode()));
                flag = false;
            }
        }
        //新SN校验  新SN已存在,请通过工序投料进行升级
        MtMaterialLot newMtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setSiteId(siteId);
            setMaterialLotCode(importVO.getNewMaterialLotCode());
        }});
        if(Objects.nonNull(newMtMaterialLot)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_SN_REPLACE_0002", "HME", importVO.getNewMaterialLotCode()));
            flag = false;
        }
        //新SN已作为eo标识无法更新
        int count = mtEoRepository.selectCount(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(importVO.getNewMaterialLotCode());
        }});
        if(count > 0){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_SN_REPLACE_0003", "HME", importVO.getNewMaterialLotCode()));
            flag = false;
        }
        return flag;
    }
}
