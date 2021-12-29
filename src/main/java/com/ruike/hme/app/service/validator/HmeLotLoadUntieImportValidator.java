package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.vo.HmeLoadUntieImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.io.IOException;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/1 15:10
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME_LOAD_UNTIE")
})
public class HmeLotLoadUntieImportValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }

        if (data != null && !"".equals(data)) {

            HmeLoadUntieImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeLoadUntieImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }

            //条码
            List<MtMaterialLot> materialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                    .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, importVO.getMaterialLotCode())).build());
            if(CollectionUtils.isEmpty(materialLotList)){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"MT_ASSEMBLE_0017","ASSEMBLE", importVO.getMaterialLotCode()));
                return false;
            }
            // 校验在制品
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setAttrName("MF_FLAG");
                setKeyId(materialLotList.get(0).getMaterialLotId());
                setTableName("mt_material_lot_attr");
            }});
            String mfFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            if (StringUtils.equals(mfFlag, HmeConstants.ConstantValue.YES)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_MATERIAL_LOT_006", "HME", importVO.getMaterialLotCode()));
                return false;
            }
            // 校验条码是否有效 不为有效 则报错
            if (!HmeConstants.ConstantValue.YES.equals(materialLotList.get(0).getEnableFlag())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_MATERIAL_LOT_0007", "HME", importVO.getMaterialLotCode()));
                return false;
            }

            //位置 C2 -> 3,2
            if (StringUtils.isBlank(importVO.getPosition())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_004", "HME", "条码"));
                return false;
            }
            String loadRow = importVO.getPosition().subSequence(0, 1).toString();
            String loadColumn = importVO.getPosition().subSequence(1, importVO.getPosition().length()).toString();
            importVO.setLoadRow(changeNum(loadRow));
            importVO.setLoadColumn(loadColumn);
            //装载信息
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotList.get(0).getMaterialLotId())
                            .andEqualTo(HmeMaterialLotLoad.FIELD_LOAD_ROW, importVO.getLoadRow())
                            .andEqualTo(HmeMaterialLotLoad.FIELD_LOAD_COLUMN, importVO.getLoadColumn())).build());
            if(CollectionUtils.isEmpty(hmeMaterialLotLoadList)){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_COS_034","HME"));
                return false;
            }

            //热沉
            if(StringUtils.isNotBlank(importVO.getHotSinkCode())){
                if(!StringUtils.equals(importVO.getHotSinkCode(), hmeMaterialLotLoadList.get(0).getHotSinkCode())){
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_COS_035","HME"));
                    return false;
                }
            }
        }
        return true;
    }

    //字母转化成对应数字(一位字母)
    private String changeNum(String str){
        char charStr = str.charAt(0);
        Integer charNum = Integer.valueOf(charStr);
        Integer result = charNum - 64;
        return result.toString();
    }
}
