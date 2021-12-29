package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeInspectorItemGroupRelDTO2;
import com.ruike.hme.domain.entity.HmeChipImportData;
import com.ruike.hme.domain.entity.HmeInspectorItemGroupRel;
import com.ruike.hme.domain.repository.HmeInspectorItemGroupRelRepository;
import com.ruike.hme.infra.mapper.HmeInspectorItemGroupRelMapper;
import com.ruike.wms.domain.entity.WmsItemGroup;
import com.ruike.wms.domain.repository.WmsItemGroupRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Objects;

/**
 * HmeInspectorItemGroupRelValidator
 * 检验员与物料组关系导入
 * @author: chaonan.hu@hand-china.com 2021-03-30 15:50:23
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.INSPECTOR_ITEM_GROUP_REL")
})
public class HmeInspectorItemGroupRelValidator extends ValidatorHandler {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HmeInspectorItemGroupRelMapper hmeInspectorItemGroupRelMapper;
    @Autowired
    private WmsItemGroupRepository wmsItemGroupRepository;
    @Autowired
    private HmeInspectorItemGroupRelRepository hmeInspectorItemGroupRelRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if(StringUtils.isNotBlank(data)){
            HmeInspectorItemGroupRelDTO2 importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeInspectorItemGroupRelDTO2.class);
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
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/30 04:00:07
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeInspectorItemGroupRelDTO2 importVO){
        boolean flag = true;
        //校验用户存在性
        Long userId = hmeInspectorItemGroupRelMapper.getUserIdByLoginName(importVO.getLoginName());
        if(Objects.isNull(userId)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_INSPECTOR_ITEM_GROUP_REL_0002", "HME", importVO.getLoginName()));
            flag = false;
        }
        //校验物料组存在性
        WmsItemGroup wmsItemGroup = wmsItemGroupRepository.selectOne(new WmsItemGroup() {{
            setTenantId(tenantId);
            setItemGroupCode(importVO.getItemGroupCode());
        }});
        if(Objects.isNull(wmsItemGroup)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_INSPECTOR_ITEM_GROUP_REL_0003", "HME", importVO.getItemGroupCode()));
            flag = false;
        }
        //唯一性校验
        if(Objects.nonNull(userId) && Objects.nonNull(wmsItemGroup)){
            HmeInspectorItemGroupRel hmeInspectorItemGroupRel = hmeInspectorItemGroupRelRepository.selectOne(new HmeInspectorItemGroupRel() {{
                setTenantId(tenantId);
                setUserId(userId);
                setItemGroupId(importVO.getItemGroupCode());
                setPrivilegeType(importVO.getInspectPowerType());
            }});
            if(Objects.nonNull(hmeInspectorItemGroupRel)){
                String inspectPowerTypeMeaning = lovAdapter.queryLovMeaning("QMS.INSPECT_POWER_TYPE", tenantId, importVO.getInspectPowerType());
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_INSPECTOR_ITEM_GROUP_REL_0001", "HME", importVO.getLoginName(), inspectPowerTypeMeaning, importVO.getItemGroupCode()));
                flag = false;
            }
        }
        return flag;
    }

}
