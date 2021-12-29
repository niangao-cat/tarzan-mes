package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeTagImportVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据收集项-效验
 *
 * @author sanfeng.zhang@hand-china.com 2020/9/22 20:16
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.TAG_IMPORT")
})
public class HmeTagImportValidator extends ValidatorHandler {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }

        if (data != null && !"".equals(data)) {

            HmeTagImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeTagImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }

            //校验数据
            if (StringUtils.isBlank(importVO.getTagCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "数据项编码"));
                return false;
            }

            //数据类型
            if (StringUtils.isBlank(importVO.getValueType())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "数据类型"));
                return false;
            }
            // 是否启用
            if (StringUtils.isBlank(importVO.getEnableFlag())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "是否启用"));
                return false;
            }
            // 导入方式
            if (StringUtils.isBlank(importVO.getImportMethod())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "导入方式"));
                return false;
            }
        }
        return true;
    }

}
