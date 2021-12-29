package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeQualificationDTO2;
import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.repository.HmeQualificationRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeOperationAssignMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeOperationAssign;
import com.ruike.hme.domain.repository.HmeOperationAssignRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 资质与工艺关系表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 19:08:35
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HmeOperationAssignRepositoryImpl extends BaseRepositoryImpl<HmeOperationAssign> implements HmeOperationAssignRepository {

    @Autowired
    private HmeQualificationRepository hmeQualificationRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeOperationAssignMapper hmeOperationAssignMapper;

    @Override
    @ProcessLovValue()
    public Page<HmeQualificationDTO2> query(Long tenantId, String operationId, PageRequest pageRequest) {
        List<HmeQualificationDTO2> hmeQualificationDTO2s = new ArrayList<>();

        HmeOperationAssign hmeOperationAssign = new HmeOperationAssign();
        hmeOperationAssign.setTenantId(tenantId);
        hmeOperationAssign.setOperationId(operationId);
        hmeOperationAssign.setEnableFlag(HmeConstants.ConstantValue.YES);
        Page<HmeOperationAssign> list =PageHelper
                .doPageAndSort(pageRequest, () -> hmeOperationAssignMapper.queryData(tenantId, operationId));
        for (HmeOperationAssign hmeOperationAssignDb:list) {
            HmeQualificationDTO2 hmeQualificationDTO2 = new HmeQualificationDTO2();
            HmeQualification hmeQualification = hmeQualificationRepository.selectByPrimaryKey(hmeOperationAssignDb.getQualityId());
            BeanUtils.copyProperties(hmeQualification, hmeQualificationDTO2);
            BeanUtils.copyProperties(hmeOperationAssignDb, hmeQualificationDTO2);
            hmeQualificationDTO2s.add(hmeQualificationDTO2);
        }

        Page<HmeQualificationDTO2> resultList = new Page<>();
        resultList.setTotalPages(list.getTotalPages());
        resultList.setTotalElements(list.getTotalElements());
        resultList.setNumberOfElements(list.getNumberOfElements());
        resultList.setSize(list.getSize());
        resultList.setNumber(list.getNumber());
        resultList.setContent(hmeQualificationDTO2s);
        return resultList;
    }

    @Override
    public List<HmeOperationAssign> create(List<HmeOperationAssign> hmeOperationAssigns) {
        //唯一性校验
        for (HmeOperationAssign hmeOperationAssign:hmeOperationAssigns) {
            HmeOperationAssign hmeOperationAssign1 = new HmeOperationAssign();
            hmeOperationAssign1.setTenantId(hmeOperationAssign.getTenantId());
            hmeOperationAssign1.setOperationId(hmeOperationAssign.getOperationId());
            hmeOperationAssign1.setQualityId(hmeOperationAssign.getQualityId());
            List<HmeOperationAssign> hmeOperationAssignList = this.select(hmeOperationAssign1);
            if(hmeOperationAssignList != null && hmeOperationAssignList.size() > 0){
                HmeQualification hmeQualification = hmeQualificationRepository.selectByPrimaryKey(hmeOperationAssign1.getQualityId());
                throw new MtException("HME_QUALIFICATIONS_0004", mtErrorMessageRepository.getErrorMessageWithModule(hmeOperationAssign.getTenantId(),
                        "HME_QUALIFICATIONS_0004", "HME", hmeQualification.getQualityName()));
            }
        }
        this.batchInsertSelective(hmeOperationAssigns);
        return hmeOperationAssigns;
    }
}
