package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEmployeeAssignDTO;
import com.ruike.hme.api.dto.HmeEmployeeAssignDTO3;
import com.ruike.hme.api.dto.HmeQualificationDTO2;
import com.ruike.hme.api.dto.HmeQualificationDTO3;
import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.repository.HmeQualificationRepository;
import com.ruike.hme.infra.mapper.HmeEmployeeAssignMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import com.ruike.hme.domain.repository.HmeEmployeeAssignRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

/**
 * 人员与资质关系表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 11:13:32
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HmeEmployeeAssignRepositoryImpl extends BaseRepositoryImpl<HmeEmployeeAssign> implements HmeEmployeeAssignRepository {

    @Autowired
    private HmeEmployeeAssignMapper hmeEmployeeAssignMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeQualificationRepository hmeQualificationRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Override
    public Page<HmeEmployeeAssignDTO> query(Long tenantId, String employeeId, PageRequest pageRequest) {
        Page<HmeEmployeeAssign> hmeEmployeeAssigns = PageHelper
                .doPageAndSort(pageRequest, () -> hmeEmployeeAssignMapper.queryData(tenantId, employeeId, null));
        List<HmeEmployeeAssignDTO> hmeEmployeeAssignDtoS = new ArrayList<>();
        for (HmeEmployeeAssign hmeEmployeeAssignDb:hmeEmployeeAssigns) {
            HmeEmployeeAssignDTO hmeEmployeeAssignDTO = new HmeEmployeeAssignDTO();
            HmeQualification hmeQualification = hmeQualificationRepository.selectByPrimaryKey(hmeEmployeeAssignDb.getQualityId());
            // update by sanfeng.zhang 解决版本号被覆盖问题
            BeanUtils.copyProperties(hmeQualification, hmeEmployeeAssignDTO);
            BeanUtils.copyProperties(hmeEmployeeAssignDb, hmeEmployeeAssignDTO);
            hmeEmployeeAssignDTO.setQualityTypeMeaning(lovAdapter.queryLovMeaning("HME.QUALITY_TYPE", tenantId, hmeEmployeeAssignDTO.getQualityType()));
            hmeEmployeeAssignDTO.setProficiencyMeaning(lovAdapter.queryLovMeaning("HME.PROFICIENCY", tenantId, hmeEmployeeAssignDTO.getProficiency()));

            //查询物料编号和名称
            if (StringUtils.isNotBlank(hmeEmployeeAssignDb.getMaterialId())){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeEmployeeAssignDb.getMaterialId());
                hmeEmployeeAssignDTO.setMaterialCode(mtMaterial.getMaterialCode()==null ? "" : mtMaterial.getMaterialCode());
                hmeEmployeeAssignDTO.setMaterialName(mtMaterial.getMaterialName()==null ? "" : mtMaterial.getMaterialName());
            }
            hmeEmployeeAssignDtoS.add(hmeEmployeeAssignDTO);
        }

        Page<HmeEmployeeAssignDTO> resultList = new Page<>();
        resultList.setTotalPages(hmeEmployeeAssigns.getTotalPages());
        resultList.setTotalElements(hmeEmployeeAssigns.getTotalElements());
        resultList.setNumberOfElements(hmeEmployeeAssigns.getNumberOfElements());
        resultList.setSize(hmeEmployeeAssigns.getSize());
        resultList.setNumber(hmeEmployeeAssigns.getNumber());
        resultList.setContent(hmeEmployeeAssignDtoS);
        return resultList;
    }

    @Override
    public List<HmeEmployeeAssign> createOrUpdate(List<HmeEmployeeAssign> hmeEmployeeAssigns) {
        List<HmeEmployeeAssign> resultList = new ArrayList<>();
        for (HmeEmployeeAssign hmeEmployeeAssign:hmeEmployeeAssigns) {
            //唯一性校验
            HmeEmployeeAssign hmeEmployeeAssign1 = new HmeEmployeeAssign();
            hmeEmployeeAssign1.setTenantId(hmeEmployeeAssign.getTenantId());
            hmeEmployeeAssign1.setEmployeeId(hmeEmployeeAssign.getEmployeeId());
            hmeEmployeeAssign1.setQualityId(hmeEmployeeAssign.getQualityId());
            List<HmeEmployeeAssign> hmeEmployeeAssignList = this.select(hmeEmployeeAssign1);
            //判断是新建还是更新
            if(StringUtils.isEmpty(hmeEmployeeAssign.getEmployeeAssignId())){
                //新建
                if(CollectionUtils.isNotEmpty(hmeEmployeeAssignList) ){
                    hmeEmployeeAssignList.forEach(item->{
                        //传入了物料，如果存在物料为空的记录或物料相同的记录，报错
                        if(StringUtils.isNotBlank(hmeEmployeeAssign.getMaterialId())) {
                            if (StringUtils.isBlank(item.getMaterialId()) || (item.getMaterialId().equals(hmeEmployeeAssign.getMaterialId()))) {
                                HmeQualification hmeQualification = hmeQualificationRepository.selectByPrimaryKey(hmeEmployeeAssign1.getQualityId());
                                throw new MtException("HME_QUALIFICATIONS_0006", mtErrorMessageRepository.getErrorMessageWithModule(hmeEmployeeAssign1.getTenantId(),
                                        "HME_QUALIFICATIONS_0006", "HME", hmeQualification.getQualityName()));
                            }
                        } else{
                            //未传入物料，报错
                            HmeQualification hmeQualification = hmeQualificationRepository.selectByPrimaryKey(hmeEmployeeAssign1.getQualityId());
                            throw new MtException("HME_QUALIFICATIONS_0007", mtErrorMessageRepository.getErrorMessageWithModule(hmeEmployeeAssign1.getTenantId(),
                                    "HME_QUALIFICATIONS_0007", "HME", hmeQualification.getQualityName()));
                        }
                    });
                }
                this.insertSelective(hmeEmployeeAssign);
            }else{
                //修改
                if(CollectionUtils.isNotEmpty(hmeEmployeeAssignList) ){
                    hmeEmployeeAssignList.forEach(item -> {
                        //传入了物料，如果存在物料为空的记录或物料相同的记录，报错
                        if(StringUtils.isNotBlank(hmeEmployeeAssign.getMaterialId())) {
                            if ((StringUtils.isBlank(item.getMaterialId()) || item.getMaterialId().equals(hmeEmployeeAssign.getMaterialId()))
                                    && !item.getEmployeeAssignId().equals(hmeEmployeeAssign.getEmployeeAssignId())) {
                                HmeQualification hmeQualification = hmeQualificationRepository.selectByPrimaryKey(hmeEmployeeAssign1.getQualityId());
                                throw new MtException("HME_QUALIFICATIONS_0006", mtErrorMessageRepository.getErrorMessageWithModule(hmeEmployeeAssign1.getTenantId(),
                                        "HME_QUALIFICATIONS_0006", "HME", hmeQualification.getQualityName()));
                            }
                        }else{
                            //未传入物料，报错
                            if (!item.getEmployeeAssignId().equals(hmeEmployeeAssign.getEmployeeAssignId())){
                                HmeQualification hmeQualification = hmeQualificationRepository.selectByPrimaryKey(hmeEmployeeAssign1.getQualityId());
                                throw new MtException("HME_QUALIFICATIONS_0007", mtErrorMessageRepository.getErrorMessageWithModule(hmeEmployeeAssign1.getTenantId(),
                                        "HME_QUALIFICATIONS_0007", "HME", hmeQualification.getQualityName()));
                            }
                        }
                    });
                }
                hmeEmployeeAssignMapper.updateByPrimaryKeySelective(hmeEmployeeAssign);
            }
            resultList.add(hmeEmployeeAssign);
        }
        return resultList;
    }

    @Override
    public Page<HmeQualificationDTO3> listForUi(Long tenantId, HmeQualificationDTO3 dto, PageRequest pageRequest) {
        //适用于资质类型模糊查询
        List<String> qualityTypeList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getQualityTypeMeaning())) {
            List<LovValueDTO> lovValueDtoS = lovAdapter.queryLovValue("HME.QUALITY_TYPE", tenantId);
            for (LovValueDTO lovValueDTO : lovValueDtoS) {
                if (lovValueDTO.getMeaning().contains(dto.getQualityTypeMeaning())) {
                    qualityTypeList.add(lovValueDTO.getValue());
                }
            }
        }
        Page<HmeQualificationDTO3> resultList = PageHelper.doPageAndSort(pageRequest, () -> hmeEmployeeAssignMapper.queryLov(tenantId, dto, qualityTypeList));
        for (HmeQualificationDTO3 dto3:resultList) {
            dto3.setQualityTypeMeaning(lovAdapter.queryLovMeaning("HME.QUALITY_TYPE",tenantId, dto3.getQualityType()));
        }
        return resultList;
    }
}
