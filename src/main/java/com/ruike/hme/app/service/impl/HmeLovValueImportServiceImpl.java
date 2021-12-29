package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeLovHeadersDTO;
import com.ruike.hme.api.dto.HmeLovValuesDTO;
import com.ruike.hme.domain.vo.HmeLangVO;
import com.ruike.hme.domain.vo.HmeLovImportVO;
import com.ruike.hme.domain.vo.HmeLovTlsVO;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 值集导入
 *
 * @author sanfeng.zhang@hand-china.com
 */
@ImportService(templateCode = "HPFM_LOV_VALUE")
public class HmeLovValueImportServiceImpl implements IBatchImportService {


    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            for (String vo : data) {
                HmeLovImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeLovImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                //保存头 有取lovId
                HmeLovHeadersDTO headersDTO = new HmeLovHeadersDTO();
                headersDTO.setLovCode(importVO.getLovCode());
                headersDTO.setLovName(importVO.getLovName());
                headersDTO.setLovTypeCode(importVO.getLovType());
                ResponseEntity<String> headers = hmeHzeroPlatformFeignClient.queryLovHeaders(importVO.getLovCode(),importVO.getLovName(),importVO.getLovType());

                Page<Map<String,Object>> page = new Page<Map<String,Object>>();
                Long lovId = null;
                try {
                    page = objectMapper.readValue(headers.getBody(), page.getClass());

                } catch (IOException e) {
                    // 失败
                    return false;
                }

                if(CollectionUtils.isNotEmpty(page.getContent())){
                    lovId = Long.valueOf(String.valueOf(page.getContent().get(0).get("lovId")));
                }else {
                    headersDTO.setEnabledFlag(1L);
                    headersDTO.setMustPageFlag(1L);
                    headersDTO.setTenantId(tenantId);
                    ResponseEntity<String> headerObj = hmeHzeroPlatformFeignClient.saveLovHeaders(headersDTO);
                    if (StringUtils.isNotBlank(headerObj.getBody())) {
                        headerObj.getBody();
                        try {
                            HmeLovHeadersDTO lovHeadersDTO = objectMapper.readValue(headerObj.getBody(), HmeLovHeadersDTO.class);
                            if(lovHeadersDTO != null){
                                lovId = lovHeadersDTO.getLovId();
                            }
                        } catch (IOException e) {
                            // 失败
                            return false;
                        }
                    }

                    if(lovId == null) {
                        throw new MtException("HME_EXCEL_IMPORT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_003", "HME"));
                    }
                }



                //保存行
                HmeLovValuesDTO valuesDTO = new HmeLovValuesDTO();
                valuesDTO.setLovId(lovId);
                valuesDTO.setValue(importVO.getValue());
                valuesDTO.setMeaning(importVO.getMeaning());
                ResponseEntity<String> queryLovValues = hmeHzeroPlatformFeignClient.queryLovValues(lovId, importVO.getMeaning(),importVO.getValue());

                if(StringUtils.isNotBlank(queryLovValues.getBody())){
                    Page<Map<String,Object>> valuesDTOPage = new Page<Map<String,Object>>();
                    try {
                        valuesDTOPage= objectMapper.readValue(queryLovValues.getBody(), valuesDTOPage.getClass());
                        if(CollectionUtils.isNotEmpty(valuesDTOPage.getContent())){
                            throw new MtException("HME_EXCEL_IMPORT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_005", "HME"));
                        }
                    } catch (IOException e) {
                        // 失败
                        return false;
                    }
                }

                valuesDTO.setLovCode(importVO.getLovCode());
                valuesDTO.setOrderSeq(importVO.getOrderSeq());
                valuesDTO.setTag(importVO.getTag());
                valuesDTO.setDescription(importVO.getDescription());
                valuesDTO.setStartDateActive(importVO.getDateFrom());
                valuesDTO.setEndDateActive(importVO.getDateTo());
                valuesDTO.setEnabledFlag(StringUtils.equals(importVO.getEnabledFlag(), "Y") ? 1L : 0L);
                valuesDTO.setTenantId(tenantId);
                hmeHzeroPlatformFeignClient.saveLovValues(valuesDTO);

            }
        }
        return true;
    }

}
