package io.tarzan.common.app.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtGenTypeDTO;
import io.tarzan.common.app.service.MtGenTypeService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO3;
import io.tarzan.common.domain.vo.MtGenTypeVO4;
import io.tarzan.common.domain.vo.MtGenTypeVO5;
import io.tarzan.common.infra.mapper.MtGenTypeMapper;

/**
 * 类型应用服务默认实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Service
public class MtGenTypeServiceImpl extends BaseServiceImpl<MtGenType> implements MtGenTypeService {

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenTypeMapper mtGenTypeMapper;

    @Override
    public List<MtGenType> comboBoxUi(Long tenantId, MtGenTypeVO2 condition) {
        return mtGenTypeRepository.groupLimitTypeQuery(tenantId, condition);
    }

    @Override
    public Page<MtGenTypeVO4> listGenTypeForUi(Long tenantId, MtGenTypeVO3 condition, PageRequest pageRequest) {

        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("GENERAL");
        queryType.setTypeGroup("SERVICE_PACKAGE");
        List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);

        MtGenType genType = new MtGenType();
        BeanUtils.copyProperties(condition, genType);
        genType.setTenantId(tenantId);
        Criteria criteria = new Criteria(genType);

        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtGenType.FIELD_TENANT_ID, Comparison.EQUAL));

        if (StringUtils.isNotEmpty(genType.getTypeGroup())) {
            whereFields.add(new WhereField(MtGenType.FIELD_TYPE_GROUP, Comparison.LIKE));
        }

        if (StringUtils.isNotEmpty(genType.getTypeCode())) {
            whereFields.add(new WhereField(MtGenType.FIELD_TYPE_CODE, Comparison.LIKE));
        }

        if (StringUtils.isNotEmpty(genType.getModule())) {
            whereFields.add(new WhereField(MtGenType.FIELD_MODULE, Comparison.EQUAL));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtGenType> base =
                        PageHelper.doPageAndSort(pageRequest, () -> mtGenTypeMapper.selectOptional(genType, criteria));

        Page<MtGenTypeVO4> result = new Page<MtGenTypeVO4>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());

        List<MtGenTypeVO4> voList = new ArrayList<MtGenTypeVO4>();
        for (MtGenType mtGenType : base) {
            MtGenTypeVO4 vo = new MtGenTypeVO4();
            BeanUtils.copyProperties(mtGenType, vo);
            Optional<MtGenType> typeOp =
                            types.stream().filter(t -> t.getTypeCode().equals(mtGenType.getModule())).findFirst();
            typeOp.ifPresent(t -> vo.setModuleDesc(t.getDescription()));
            voList.add(vo);
        }
        result.setContent(voList);
        return result;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveGenTypeForUi(Long tenantId, MtGenTypeDTO dto) {

        MtGenType genType = new MtGenType();
        BeanUtils.copyProperties(dto, genType);
        genType.setTenantId(tenantId);

        return this.mtGenTypeRepository.genTypeBasicPropertyUpdate(tenantId, genType);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeGenTypeForUi(Long tenantId, List<MtGenType> list) {
        this.mtGenTypeRepository.removeGenType(tenantId, list);
    }

    @Override
    public List<MtGenTypeVO5> getModuleByTypeGroupForUi(Long tenantId, String typeGroup) {
        return mtGenTypeMapper.selectModuleByTypeGroup(tenantId, typeGroup);
    }

}
