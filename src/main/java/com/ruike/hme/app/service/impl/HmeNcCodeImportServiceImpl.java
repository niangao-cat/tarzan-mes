package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeNcCodeImportVO;
import com.ruike.hme.infra.mapper.HmeSnBindEoMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.modeling.domain.entity.MtModSite;

import java.io.IOException;
import java.util.List;

/**
 * 不良代码
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 19:16
 */
@ImportService(templateCode = "MT_NC_CODE")
public class HmeNcCodeImportServiceImpl implements IBatchImportService {

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeSnBindEoMapper hmeSnBindEoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();


        if (CollectionUtils.isNotEmpty(data)) {
            Integer index = 0;
            for (String vo : data) {
                HmeNcCodeImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeNcCodeImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                if(importVO.getTenantId() != null){
                    tenantId = importVO.getTenantId();
                }
                //处理数据
                if(StringUtils.isBlank(importVO.getSiteId())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"站点编码"));
                }
                List<MtModSite> mtModSites = hmeSnBindEoMapper.queryModSiteByCode(tenantId, importVO.getSiteId());
                if(CollectionUtils.isNotEmpty(mtModSites)){
                    importVO.setSiteId(mtModSites.get(0).getSiteId());
                }else {
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"站点"));
                }

                if(StringUtils.isBlank(importVO.getNcCode())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"不良代码编码"));
                }

                if(StringUtils.isBlank(importVO.getNcType())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"不良类型"));
                }
                MtNcCode mtNcCode = new MtNcCode();
                BeanUtils.copyProperties(importVO,mtNcCode);

                //默认有效性Y
                mtNcCode.setEnableFlag("Y");

                mtNcCodeRepository.insertSelective(mtNcCode);
                //多语言处理
                if(StringUtils.isNotBlank(importVO.getNcCodeLang())){
                    String[] langStr = importVO.getNcCodeLang().split("$");
                    if(langStr.length == 3){
                        try {
                            hmeSnBindEoMapper.insetMtNcCodeTl(mtNcCode.getNcCodeId(),langStr[1],langStr[2]);
                        }catch (Exception e){
                            throw new MtException("HME_EXCEL_IMPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_002", "HME",String.valueOf(index+1)));
                        }
                    }
                }

            }
        }
        return true;
    }
}
