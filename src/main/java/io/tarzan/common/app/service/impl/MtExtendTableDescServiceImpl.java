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

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtExtendAttrDTO4;
import io.tarzan.common.app.service.MtExtendTableDescService;
import io.tarzan.common.domain.entity.MtExtendTableDesc;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendTableDescRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendTableDescVO;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.infra.mapper.MtExtendTableDescMapper;

/**
 * 扩展说明表应用服务默认实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Service
public class MtExtendTableDescServiceImpl extends BaseServiceImpl<MtExtendTableDesc>
        implements MtExtendTableDescService {

    @Autowired
    private MtExtendTableDescMapper mtExtendTableDescMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendTableDescRepository mtExtendTableDescRepository;

    @Override
    public Page<MtExtendTableDescVO> extendListGet(Long tenantId, PageRequest pageRequest, MtExtendAttrDTO4 dto) {
        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("GENERAL");
        queryType.setTypeGroup("SERVICE_PACKAGE");
        List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
        MtExtendTableDesc tableDesc = new MtExtendTableDesc();
        tableDesc.setTenantId(tenantId);
        tableDesc.setAttrTable(dto.getAttrTable());
        tableDesc.setAttrTableDesc(dto.getAttrTableDesc());
        tableDesc.setInitialFlag(dto.getInitialFlag());
        Criteria criteria = new Criteria(tableDesc);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtExtendTableDesc.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtExtendTableDesc.FIELD_ATTR_TABLE, Comparison.LIKE));
        whereFields.add(new WhereField(MtExtendTableDesc.FIELD_ATTR_TABLE_DESC, Comparison.LIKE));
        whereFields.add(new WhereField(MtExtendTableDesc.FIELD_INITIAL_FLAG, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtExtendTableDesc> base = PageHelper.doPageAndSort(pageRequest, () -> {
            return mtExtendTableDescMapper.selectOptional(tableDesc, criteria);
        });
        Page<MtExtendTableDescVO> result = new Page<MtExtendTableDescVO>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());
        List<MtExtendTableDescVO> voList = new ArrayList<MtExtendTableDescVO>();
        for (MtExtendTableDesc ever : base) {
            MtExtendTableDescVO vo = new MtExtendTableDescVO();
            BeanUtils.copyProperties(ever, vo);
            Optional<MtGenType> typeOp =
                    types.stream().filter(t -> t.getTypeCode().equals(ever.getServicePackage())).findFirst();
            typeOp.ifPresent(mtGenType -> vo.setServicePackageDesc(mtGenType.getDescription()));
            voList.add(vo);
        }
        result.setContent(voList);

        return result;
    }

    @Override
    public MtExtendTableDescVO extendOneGet(Long tenantId, String extendTableDescId) {
        MtExtendTableDesc tableDesc = new MtExtendTableDesc();
        tableDesc.setTenantId(tenantId);
        tableDesc.setExtendTableDescId(extendTableDescId);
        tableDesc = mtExtendTableDescMapper.selectOne(tableDesc);
        MtGenType type = mtGenTypeRepository.getGenType(tenantId, "GENERAL","SERVICE_PACKAGE", tableDesc.getServicePackage());
        MtExtendTableDescVO result = new MtExtendTableDescVO();
        BeanUtils.copyProperties(tableDesc, result);
        if (type != null) {
            result.setServicePackageDesc(type.getDescription());
        }
        return result;
    }

    @Override
    public MtExtendTableDescVO extendSave(Long tenantId, MtExtendTableDescVO dto) {
        if (dto == null) {
            return null;
        }
        MtExtendTableDesc extendTableDesc = new MtExtendTableDesc();
        BeanUtils.copyProperties(dto, extendTableDesc);
        extendTableDesc.setTenantId(tenantId);
        extendTableDesc.set_tls(dto.get_tls());

        String extendTableDescId = mtExtendTableDescRepository.extTabBasicPropertyUpdate(tenantId, extendTableDesc);
        dto.setExtendTableDescId(extendTableDescId);
        return dto;
    }


}
