package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeQualificationDTO;
import com.ruike.hme.api.dto.HmeQualificationDTO2;
import com.ruike.hme.app.service.HmeOperationAssignService;
import com.ruike.hme.infra.mapper.HmeEmployeeAssignMapper;
import com.ruike.hme.infra.mapper.HmeOperationAssignMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 资质与工艺关系表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 19:08:35
 */
@Service
public class HmeOperationAssignServiceImpl implements HmeOperationAssignService {

    @Autowired
    private HmeOperationAssignMapper hmeOperationAssignMapper;
    @Autowired
    private LovAdapter lovAdapter;


    @Override
    public Page<HmeQualificationDTO2> listForUi(Long tenantId, HmeQualificationDTO2 dto, PageRequest pageRequest) {
        //适用于资质类型模糊查询
        List<String> qualityTypeList = new ArrayList<>();
//        if(StringUtils.isNotEmpty(dto.getQualityTypeMeaning())){
//            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.QUALITY_TYPE", tenantId);
//            for (LovValueDTO lovValueDTO:lovValueDTOS) {
//                if(lovValueDTO.getMeaning().contains(dto.getQualityTypeMeaning())){
//                    qualityTypeList.add(lovValueDTO.getValue());
//                }
//            }
//        }
        Page<HmeQualificationDTO2> resultList = PageHelper.doPageAndSort(pageRequest, () -> hmeOperationAssignMapper.queryLov(tenantId, dto, qualityTypeList));
        for (HmeQualificationDTO2 dto2:resultList) {
            dto2.setQualityTypeMeaning(lovAdapter.queryLovMeaning("HME.QUALITY_TYPE",tenantId, dto2.getQualityType()));
        }
        return resultList;
    }
}
