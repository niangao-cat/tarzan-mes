package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeOperationAssign;
import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.repository.HmeOperationAssignRepository;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeOperationAssignImportVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.method.domain.entity.MtOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 工艺与资质关系
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 16:17
 */
@ImportService(templateCode = "HME_OPERATION_ASSIGN")
public class HmeImportOperationAssignServiceImpl implements IBatchImportService {


    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private HmeOperationAssignRepository hmeOperationAssignRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public Boolean doImport(List<String> data) {
        //获取租戶Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        List<HmeOperationAssign> operationAssignList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data)){
            Integer index = 0;
            for(String vo : data){
                HmeOperationAssignImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeOperationAssignImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                //处理数据
                HmeOperationAssign operationAssign = new HmeOperationAssign();
                operationAssign.setTenantId(tenantId);
                operationAssign.setOperationAssignId(importVO.getOperationAssignId());
                operationAssign.setEnableFlag(importVO.getEnableFlag());

                //工艺Id
                if(StringUtils.isNotBlank(importVO.getOperationId())){
                    List<MtOperation> mtOperations = hmeSnBindEoRepository.queryOperationIdByName(tenantId, importVO.getOperationId());
                    if(CollectionUtils.isEmpty(mtOperations)){
                        throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"工艺主键"));
                    }
                    operationAssign.setOperationId(mtOperations.get(0).getOperationId());
                }
                //资质Id
                if(StringUtils.isNotBlank(importVO.getQualityId())){
                    List<HmeQualification> qualificationList = hmeSnBindEoRepository.queryQualificationByName(tenantId, importVO.getQualityId());
                    if(CollectionUtils.isEmpty(qualificationList)){
                        throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"资质主键"));
                    }
                    operationAssign.setQualityId(qualificationList.get(0).getQualityId());
                }
                operationAssignList.add(operationAssign);
            }
            hmeOperationAssignRepository.create(operationAssignList);
        }
        return true;
    }

    @Override
    public int getSize(){
        return 500;
    }



}
