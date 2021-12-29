package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeNcValidOperImportVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.MtNcValidOperRepository;

import java.io.IOException;
import java.util.List;

/**
 * 不良代码与工艺关系
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 19:32
 */
@ImportService(templateCode = "MT_NC_VALID_OPER")
public class HmeNcValidOperImportServiceImpl implements IBatchImportService {

    @Autowired
    private MtNcValidOperRepository mtNcValidOperRepository;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            Integer index = 0;
            for (String vo : data) {
                HmeNcValidOperImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeNcValidOperImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                if(importVO.getTenantId() != null){
                    tenantId = importVO.getTenantId();
                }
                //处理数据
                if(StringUtils.isBlank(importVO.getOperationId())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_hEXCEL_IMPORT_001", "HME",String.valueOf(index+1),"工艺编码"));
                }

                List<MtOperation> mtOperationList = hmeSnBindEoRepository.queryOperationIdByName(tenantId, importVO.getOperationId());
                if(CollectionUtils.isEmpty(mtOperationList)){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"工艺Id"));
                }
                importVO.setOperationId(mtOperationList.get(0).getOperationId());

                if(StringUtils.isBlank(importVO.getEnableFlag())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"有效性"));
                }

                if(StringUtils.isBlank(importVO.getNcObjectType())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"类型"));
                }

                if(StringUtils.equals(importVO.getNcObjectType(),"NC_CODE")){
                    List<MtNcCode> mtNcCodes = hmeSnBindEoRepository.queryNcCodeInfo(tenantId, importVO.getNcObjectId());
                    if(CollectionUtils.isNotEmpty(mtNcCodes)){
                        importVO.setNcObjectId(mtNcCodes.get(0).getNcCodeId());
                    }
                }else if(StringUtils.equals(importVO.getNcObjectType(),"NC_GROUP")){
                    List<MtNcGroup> mtNcGroupList = hmeSnBindEoRepository.queryNcGroupInfo(tenantId, importVO.getNcObjectId());
                    if(CollectionUtils.isNotEmpty(mtNcGroupList)){
                        importVO.setNcObjectId(mtNcGroupList.get(0).getNcGroupId());
                    }
                }

                if(StringUtils.isBlank(importVO.getNcObjectId())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"不良代码/不良代码组"));
                }
                MtNcValidOper mtNcValidOper = new MtNcValidOper();

                BeanUtils.copyProperties(importVO,mtNcValidOper);

                //处理组Id
                List<MtDispositionGroup> mtDispositionGroups = hmeSnBindEoRepository.queryDispositionGroupIdByDesc(tenantId, importVO.getDispositionGroupId());
                if(CollectionUtils.isNotEmpty(mtDispositionGroups)){
                    mtNcValidOper.setDispositionGroupId(mtDispositionGroups.get(0).getDispositionGroupId());
                }

                mtNcValidOperRepository.insertSelective(mtNcValidOper);
            }
        }
        return true;
    }

}
