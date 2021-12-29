package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeNcSecondaryCodeImportVO;
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
import tarzan.method.domain.entity.MtNcGroup;
import tarzan.method.domain.entity.MtNcSecondaryCode;
import tarzan.method.domain.repository.MtNcSecondaryCodeRepository;

import java.io.IOException;
import java.util.List;

/**
 * 次级不良
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 19:48
 */
@ImportService(templateCode = "MT_NC_SECONDARY_CODE")
public class HmeNcSecondaryCodeImportServiceImpl implements IBatchImportService {

    @Autowired
    private MtNcSecondaryCodeRepository mtNcSecondaryCodeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            Integer index = 0;
            for (String vo : data) {
                HmeNcSecondaryCodeImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeNcSecondaryCodeImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                if(importVO.getTenantId() != null){
                    tenantId = importVO.getTenantId();
                }
                //处理数据
                if(StringUtils.isBlank(importVO.getNcObjectType())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"类型"));
                }

                if(StringUtils.isBlank(importVO.getRequiredFlag())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"关闭是否需要"));
                }

                if(importVO.getSequence() == null){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"顺序"));
                }

                if(StringUtils.isBlank(importVO.getNcCodeId())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"不良代码"));
                }
                List<MtNcCode> mtNcCodes = hmeSnBindEoRepository.queryNcCodeInfo(tenantId, importVO.getNcCodeId());
                if(CollectionUtils.isNotEmpty(mtNcCodes)){
                    importVO.setNcCodeId(mtNcCodes.get(0).getNcCodeId());
                }

                if(StringUtils.isBlank(importVO.getNcObjectType())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"类型"));
                }

                if(StringUtils.equals(importVO.getNcObjectType(),"NC_GROUP")){
                    List<MtNcGroup> mtNcGroupList = hmeSnBindEoRepository.queryNcGroupInfo(tenantId, importVO.getNcObjectId());
                    if(CollectionUtils.isNotEmpty(mtNcGroupList)){
                        importVO.setNcObjectId(mtNcGroupList.get(0).getNcGroupId());
                    }
                }else if(StringUtils.equals(importVO.getNcObjectType(),"NC_CODE")){
                    List<MtNcCode> mtNcCodeList = hmeSnBindEoRepository.queryNcCodeInfo(tenantId, importVO.getNcObjectId());
                    if(CollectionUtils.isNotEmpty(mtNcCodeList)){
                        importVO.setNcObjectId(mtNcCodeList.get(0).getNcCodeId());
                    }
                }

                if(StringUtils.isBlank(importVO.getNcObjectId())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"不良代码/不良代码组"));
                }

                if(StringUtils.isBlank(importVO.getNcCodeId())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"不良代码"));
                }

                MtNcSecondaryCode secondaryCode = new MtNcSecondaryCode();
                BeanUtils.copyProperties(importVO,secondaryCode);
                mtNcSecondaryCodeRepository.insertSelective(secondaryCode);
            }
        }
        return true;
    }

}
