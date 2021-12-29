package tarzan.material.app.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtUomDTO5;
import tarzan.material.api.dto.MtUomDTO6;
import tarzan.material.app.service.MtUomService;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.infra.mapper.MtUomMapper;

/**
 * 单位应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:10:08
 */
@Service
public class MtUomServiceImpl extends BaseServiceImpl<MtUom> implements MtUomService {
    private static final String YES="Y";
    @Autowired
    private MtUomMapper mtUomMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public Page<MtUom> queryUomForUi(Long tenantId, MtUomDTO5 dto, PageRequest pageRequest) {
        MtUom queryUom = new MtUom();
        queryUom.setTenantId(tenantId);
        queryUom.setUomCode(dto.getUomCode());
        queryUom.setUomName(dto.getUomName());
        queryUom.setUomType(dto.getUomType());
        queryUom.setEnableFlag(dto.getEnableFlag());

        Criteria criteria = new Criteria(queryUom);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtUom.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtUom.FIELD_UOM_CODE, Comparison.LIKE));
        whereFields.add(new WhereField(MtUom.FIELD_UOM_NAME, Comparison.LIKE));
        whereFields.add(new WhereField(MtUom.FIELD_UOM_TYPE, Comparison.EQUAL));
        whereFields.add(new WhereField(MtUom.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        return PageHelper.doPageAndSort(pageRequest,
                        () -> mtUomMapper.selectOptional(queryUom, criteria));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtUom saveUomForUi(Long tenantId, MtUomDTO6 dto) {
        MtUom mtUom = new MtUom();
        BeanUtils.copyProperties(dto, mtUom);
        mtUom.setTenantId(tenantId);

        //查询单位编码是否存在
        MtUom union=new MtUom();
        union.setTenantId(tenantId);
        union.setUomCode(dto.getUomCode());
        if(StringUtils.isNotEmpty(mtUom.getUomId())){
            union.setUomId(mtUom.getUomId());
        }
        Criteria criteria2 = new Criteria(union);
        List<WhereField> whereFields2 = new ArrayList<WhereField>();
        if(StringUtils.isNotEmpty(mtUom.getUomId())){
            whereFields2.add(new WhereField(MtUom.FIELD_UOM_ID, Comparison.NOT_EQUAL));
        }
        whereFields2.add(new WhereField(MtUom.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtUom.FIELD_UOM_CODE, Comparison.EQUAL));
        criteria2.where(whereFields2.toArray(new WhereField[whereFields2.size()]));
        if(mtUomMapper.selectOptional(union,criteria2).size()>0){
            throw new MtException("MT_MATERIAL_0080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_0080", "MATERIAL"));
        }

        //查询同单位类型中是否存在其他主单位
        if(YES.equals(mtUom.getPrimaryFlag())){
            MtUom uomQuery = new MtUom();
            uomQuery.setTenantId(mtUom.getTenantId());
            uomQuery.setUomType(mtUom.getUomType());
            uomQuery.setPrimaryFlag(YES);
            if(StringUtils.isNotEmpty(mtUom.getUomId())){
                uomQuery.setUomId(mtUom.getUomId());
            }
            Criteria criteria = new Criteria(uomQuery);
            List<WhereField> whereFields = new ArrayList<WhereField>();
            if(StringUtils.isNotEmpty(mtUom.getUomId())){
                whereFields.add(new WhereField(MtUom.FIELD_UOM_ID, Comparison.NOT_EQUAL));
            }
            whereFields.add(new WhereField(MtUom.FIELD_TENANT_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtUom.FIELD_UOM_TYPE, Comparison.EQUAL));
            whereFields.add(new WhereField(MtUom.FIELD_PRIMARY_FLAG, Comparison.EQUAL));
            criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
            if(mtUomMapper.selectOptional(uomQuery,criteria).size()>0){
                throw new MtException("MT_MATERIAL_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_0059", "MATERIAL"));
            }
        }

        if (StringUtils.isEmpty(dto.getUomId())) {
            this.insertSelective(mtUom);
        } else {
            this.updateByPrimaryKeySelective(mtUom);
        }

        return mtUom;
    }

}
