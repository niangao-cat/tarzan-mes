package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeLovHeadersDTO;
import com.ruike.hme.domain.vo.HmeLovImportVO;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * 值集导入校验
 *
 * @author sanfeng.zhang@hand-china.com 2020/7/23 20:30
 */
@ImportValidators({
        @ImportValidator(templateCode = "HPFM_LOV_VALUE")
})
public class HmeLovValueImportValidator  extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }

        if (data != null && !"".equals(data)) {

            HmeLovImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeLovImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }

            //值集编码
            if(StringUtils.isBlank(importVO.getLovCode())){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EXCEL_IMPORT_004","HME", "lovCode"));
                return false;
            }
            //值集名称
            if(StringUtils.isBlank(importVO.getLovName())){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EXCEL_IMPORT_004","HME", "lovName"));
                return false;
            }
            //值集类型
            if(StringUtils.isBlank(importVO.getLovType())){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EXCEL_IMPORT_004","HME", "lovTypeCode"));
                return false;
            }
            //排序号
            if(importVO.getOrderSeq() == null){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EXCEL_IMPORT_004","HME", "orderSeq"));
                return false;
            }

            //值
            if(importVO.getValue() == null){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EXCEL_IMPORT_004","HME", "value"));
                return false;
            }

            //含义
            if(importVO.getMeaning() == null){
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EXCEL_IMPORT_004","HME", "meaning"));
                return false;
            }

        }
        return true;
    }
}
