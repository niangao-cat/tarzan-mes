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
import io.tarzan.common.api.dto.MtNumrangeObjectDTO2;
import io.tarzan.common.api.dto.MtNumrangeObjectDTO3;
import io.tarzan.common.app.service.MtNumrangeObjectService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectColumnRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO;
import io.tarzan.common.infra.mapper.MtNumrangeObjectMapper;

/**
 * 编码对象属性应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@Service
public class MtNumrangeObjectServiceImpl extends BaseServiceImpl<MtNumrangeObject> implements MtNumrangeObjectService {

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtNumrangeObjectMapper mtNumrangeObjectMapper;

    @Autowired
    private MtNumrangeObjectColumnRepository mtNumrangeObjectColumnRepository;

    @Override
    public Page<MtNumrangeObjectVO> listNumrangeObjectForUi(Long tenantId, MtNumrangeObjectDTO2 condition,
                                                            PageRequest pageRequest) {

        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("GENERAL");
        queryType.setTypeGroup("SERVICE_PACKAGE");
        List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);

        MtNumrangeObject mtNumrangeObject = new MtNumrangeObject();
        BeanUtils.copyProperties(condition, mtNumrangeObject);
        mtNumrangeObject.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtNumrangeObject);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtNumrangeObject.FIELD_TENANT_ID, Comparison.EQUAL));

        if (StringUtils.isNotEmpty(mtNumrangeObject.getObjectCode())) {
            whereFields.add(new WhereField(MtNumrangeObject.FIELD_OBJECT_CODE, Comparison.LIKE));
        }

        if (StringUtils.isNotEmpty(mtNumrangeObject.getObjectName())) {
            whereFields.add(new WhereField(MtNumrangeObject.FIELD_OBJECT_NAME, Comparison.LIKE));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtNumrangeObject> base = PageHelper.doPageAndSort(pageRequest,
                        () -> mtNumrangeObjectMapper.selectOptional(mtNumrangeObject, criteria));

        Page<MtNumrangeObjectVO> result = new Page<MtNumrangeObjectVO>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());

        List<MtNumrangeObjectVO> voList = new ArrayList<MtNumrangeObjectVO>();
        for (MtNumrangeObject numrangeObject : base) {
            MtNumrangeObjectVO vo = new MtNumrangeObjectVO();
            BeanUtils.copyProperties(numrangeObject, vo);
            MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
            objectColumn.setTenantId(tenantId);
            objectColumn.setObjectId(numrangeObject.getObjectId());
            MtNumrangeObjectColumn column = mtNumrangeObjectColumnRepository.selectOne(objectColumn);

            if(column != null){
                Optional<MtGenType> statusOp =
                        types.stream().filter(t -> t.getTypeCode().equals(column.getModule())).findFirst();
                statusOp.ifPresent(t -> vo.setModuleDesc(t.getDescription()));
            }

            voList.add(vo);
        }
        result.setContent(voList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveNumrangeObjectForUi(Long tenantId, MtNumrangeObjectDTO3 dto) {

        MtNumrangeObject oldObject = new MtNumrangeObject();
        oldObject.setTenantId(tenantId);
        oldObject.setObjectCode(dto.getObjectCode());
        oldObject = mtNumrangeObjectMapper.selectOne(oldObject);

        if (oldObject != null) {
            if (StringUtils.isEmpty(dto.getObjectId()) || !dto.getObjectId().equals(oldObject.getObjectId())) {
                throw new MtException("MT_GENERAL_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0046", "GENERAL"));
            }
        }

        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        BeanUtils.copyProperties(dto, numrangeObject);

        numrangeObject.setTenantId(tenantId);

        if (StringUtils.isNotEmpty(numrangeObject.getObjectId())) {
            updateByPrimaryKey(numrangeObject);
        } else {
            insertSelective(numrangeObject);
        }

        return numrangeObject.getObjectId();
    }

}
