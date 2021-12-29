package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeQualificationDTO;
import com.ruike.hme.api.dto.HmeQualificationQueryDTO;
import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.repository.HmeQualificationRepository;
import com.ruike.hme.infra.mapper.HmeQualificationMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 资质基础信息表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-06-15 10:32:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HmeQualificationRepositoryImpl extends BaseRepositoryImpl<HmeQualification> implements HmeQualificationRepository {

    @Autowired
    private HmeQualificationMapper hmeQualificationMapper;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public List<HmeQualification> createOrUpdate(Long tenantId, List<HmeQualification> dtos) {
        List<HmeQualification> resultList = new ArrayList<>();
        //判断是新增还是更新
        for (HmeQualification hmeQualification : dtos) {
            if (StringUtils.isEmpty(hmeQualification.getQualityId())) {
                //新增
                //校验租户和资质编码是否存在
                HmeQualification hmeQualification1 = new HmeQualification();
                hmeQualification1.setTenantId(tenantId);
                hmeQualification1.setQualityCode(hmeQualification.getQualityCode());
                List<HmeQualification> hmeQualifications = this.select(hmeQualification1);
                if (CollectionUtils.isNotEmpty(hmeQualifications)) {
                    throw new MtException("HME_QUALIFICATIONS_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_QUALIFICATIONS_0001", "HME", hmeQualification.getQualityCode()));
                }
                //校验租户和资质名称是否存在
                hmeQualification1.setQualityCode(null);
                hmeQualification1.setQualityName(hmeQualification.getQualityName());
                hmeQualifications = this.select(hmeQualification1);
                if (CollectionUtils.isNotEmpty(hmeQualifications)) {
                    throw new MtException("HME_QUALIFICATIONS_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_QUALIFICATIONS_0002", "HME", hmeQualification.getQualityName()));
                }
                this.insertSelective(hmeQualification);
                resultList.add(hmeQualification);
            } else {
                //更新
                //校验
                HmeQualification hmeQualification1 = new HmeQualification();
                hmeQualification1.setTenantId(tenantId);
                hmeQualification1.setQualityCode(hmeQualification.getQualityCode());
                List<HmeQualification> hmeQualifications = this.select(hmeQualification1);
                if (CollectionUtils.isNotEmpty(hmeQualifications) && !hmeQualifications.get(0).getQualityId().equals(hmeQualification.getQualityId())) {
                    throw new MtException("HME_QUALIFICATIONS_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_QUALIFICATIONS_0001", "HME", hmeQualification.getQualityCode()));
                }
                hmeQualification1.setQualityCode(null);
                hmeQualification1.setQualityName(hmeQualification.getQualityName());
                hmeQualifications = this.select(hmeQualification1);
                if (CollectionUtils.isNotEmpty(hmeQualifications)) {
                    if (CollectionUtils.isNotEmpty(hmeQualifications) && !hmeQualifications.get(0).getQualityId().equals(hmeQualification.getQualityId())) {
                        throw new MtException("HME_QUALIFICATIONS_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_QUALIFICATIONS_0002", "HME", hmeQualification.getQualityName()));
                    }
                }

                HmeQualification version = new HmeQualification();
                version.setTenantId(tenantId);
                version.setQualityId(hmeQualification.getQualityId());
                HmeQualification hmeQualificationDb = this.selectOne(version);
                hmeQualification.setObjectVersionNumber(hmeQualificationDb.getObjectVersionNumber());
                hmeQualificationMapper.updateByPrimaryKeySelective(hmeQualification);
                resultList.add(hmeQualification);
            }
        }
        return resultList;
    }

    @Override
    @ProcessLovValue()
    public Page<HmeQualificationDTO> query(PageRequest pageRequest, HmeQualificationQueryDTO hmeQualification, Long tenantId) {
        List<HmeQualificationDTO> resultS = new ArrayList<>();
        //查询条件
        HmeQualification queryDto = new HmeQualification();
        BeanUtils.copyProperties(hmeQualification, queryDto);
        queryDto.setTenantId(tenantId);

        Criteria criteria = new Criteria(queryDto);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(HmeQualification.FIELD_TENANT_ID, Comparison.EQUAL));

        if (StringUtils.isNotEmpty(queryDto.getQualityType())) {
            whereFields.add(new WhereField(HmeQualification.FIELD_QUALITY_TYPE, Comparison.EQUAL));
        }
        if (StringUtils.isNotEmpty(queryDto.getQualityName())) {
            whereFields.add(new WhereField(HmeQualification.FIELD_QUALITY_NAME, Comparison.LIKE));
        }
        if (StringUtils.isNotEmpty(queryDto.getEnableFlag())) {
            whereFields.add(new WhereField(HmeQualification.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<HmeQualification> list = PageHelper.doPageAndSort(pageRequest,
                () -> {
                    return hmeQualificationMapper.selectOptional(queryDto, criteria);
                });
        for (HmeQualification hmeQualificationDb : list) {
            HmeQualificationDTO hmeQualificationDTO = new HmeQualificationDTO();
            BeanUtils.copyProperties(hmeQualificationDb, hmeQualificationDTO);
            //创建人姓名
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeQualificationDb.getCreatedBy());
            hmeQualificationDTO.setCreateUserName(mtUserInfo.getRealName());
            //更新人姓名
            mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeQualificationDb.getLastUpdatedBy());
            hmeQualificationDTO.setUpdateUserName(mtUserInfo.getRealName());
            resultS.add(hmeQualificationDTO);
        }
        Page<HmeQualificationDTO> resultList = new Page<HmeQualificationDTO>();
        resultList.setTotalPages(list.getTotalPages());
        resultList.setTotalElements(list.getTotalElements());
        resultList.setNumberOfElements(list.getNumberOfElements());
        resultList.setSize(list.getSize());
        resultList.setNumber(list.getNumber());
        resultList.setContent(resultS);
        return resultList;
//        return new Page<>(resultS, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultS.size());
    }
}
