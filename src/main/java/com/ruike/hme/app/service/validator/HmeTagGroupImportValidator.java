package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeTagGroupImportVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.repository.MtTagRepository;

import java.io.IOException;

/**
 * 数据收集组-效验
 *
 * @author sanfeng.zhang@hand-china.com 2020/9/22 21:29
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.TAG_GROUP_OBJECT")
})
public class HmeTagGroupImportValidator extends ValidatorHandler {


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

            HmeTagGroupImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeTagGroupImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }

            //校验数据
            if(StringUtils.isBlank(importVO.getTagGroupCode())){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EXCEL_IMPORT_001","HME","","数据组编码"));
                return false;
            }

            //数据组类型
            if(StringUtils.isBlank(importVO.getTagGroupType())){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EXCEL_IMPORT_001","HME","","数据组类型"));
                return false;
            }

            //状态
            if (StringUtils.isBlank(importVO.getStatus())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "状态"));
                return false;
            }

            //数据收集时点
            if (StringUtils.isBlank(importVO.getCollectionTimeControl())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "数据收集时点"));
                return false;
            }
            // 导入方式
            if (StringUtils.isBlank(importVO.getImportMethod())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "导入方式"));
                return false;
            }
            // 数据收集组描述
            if (StringUtils.isBlank(importVO.getTagGroupDescription())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "数据收集组描述"));
                return false;
            }
            // 业务类型
            if (StringUtils.isBlank(importVO.getBusinessType())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_001", "HME", "", "业务类型"));
                return false;
            }
        }
        return true;
    }

}
