package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.repository.HmeQualificationRepository;
import com.ruike.hme.domain.vo.HmeQualificationImportVO;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 人员资质 自定义导入逻辑
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 14:47
 */
@ImportService(templateCode = "HME.QUALIFICATION")
public class HmeImportQualificationExcelServiceImpl implements IBatchImportService {


    @Autowired
    private HmeQualificationRepository hmeQualificationRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        List<HmeQualification> qualificationList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data)){
            for(String vo : data){
                HmeQualificationImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeQualificationImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                //处理数据
                HmeQualification qualification = new HmeQualification();
                BeanUtils.copyProperties(importVO,qualification);
                qualificationList.add(qualification);
            }
            hmeQualificationRepository.createOrUpdate(tenantId,qualificationList);
        }
        return true;
    }

    @Override
    public int getSize(){
        return 500;
    }

}
