package tarzan.general.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
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
import tarzan.general.api.dto.MtEventObjectTypeRelDTO;
import tarzan.general.api.dto.MtEventObjectTypeRelDTO2;
import tarzan.general.app.service.MtEventObjectTypeRelService;
import tarzan.general.domain.entity.MtEventObjectType;
import tarzan.general.domain.entity.MtEventObjectTypeRel;
import tarzan.general.domain.repository.MtEventObjectTypeRelRepository;
import tarzan.general.domain.vo.MtEventObjectTypeRelVO1;
import tarzan.general.domain.vo.MtEventObjectTypeRelVO2;
import tarzan.general.domain.vo.MtEventObjectTypeRelVO5;
import tarzan.general.domain.vo.MtEventObjectTypeRelVO6;
import tarzan.general.infra.mapper.MtEventObjectTypeMapper;
import tarzan.general.infra.mapper.MtEventObjectTypeRelMapper;


/**
 * 事件类型与对象类型关系定义应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Service
public class MtEventObjectTypeRelServiceImpl extends BaseServiceImpl<MtEventObjectTypeRel>
                implements MtEventObjectTypeRelService {

    @Autowired
    private MtEventObjectTypeRelRepository repository;

    @Autowired
    private MtEventObjectTypeMapper mtEventObjectTypeMapper;

    @Autowired
    private MtEventObjectTypeRelMapper mtEventObjectTypeRelMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Override
    public List<MtEventObjectTypeRelVO6> eventLimitEventObjectInfoQueryForUi(Long tenantId,
                    MtEventObjectTypeRelVO5 vo) {
        return repository.eventLimitEventObjectInfoQuery(tenantId, vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEventObjectTypeRelDTO2 saveEventObjectTypeRel(Long tenantId, MtEventObjectTypeRelDTO dto) {
        MtEventObjectTypeRel param = new MtEventObjectTypeRel();
        BeanUtils.copyProperties(dto, param);
        param.setTenantId(tenantId);

        // 唯一性校验
        Criteria criteria = new Criteria(param);
        List<WhereField> whereFields2 = new ArrayList<WhereField>();
        if (StringUtils.isNotEmpty(dto.getRelId())) {
            whereFields2.add(new WhereField(MtEventObjectTypeRel.FIELD_REL_ID, Comparison.NOT_EQUAL));
        }
        whereFields2.add(new WhereField(MtEventObjectTypeRel.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtEventObjectTypeRel.FIELD_EVENT_TYPE_ID, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtEventObjectTypeRel.FIELD_OBJECT_TYPE_ID, Comparison.EQUAL));
        criteria.where(whereFields2.toArray(new WhereField[whereFields2.size()]));
        if (mtEventObjectTypeRelMapper.selectOptional(param, criteria).size() > 0) {
            MtEventObjectType objectType = new MtEventObjectType();
            objectType.setTenantId(tenantId);
            objectType.setObjectTypeId(param.getObjectTypeId());
            objectType = mtEventObjectTypeMapper.selectByPrimaryKey(objectType);
            // 事件对应对象类型{1}已存在,请确认
            throw new MtException("MT_EVENT_0020", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0020", "EVENT", objectType.getObjectTypeCode()));
        }
        if (StringUtils.isEmpty(dto.getRelId())) {
            this.insertSelective(param);
        } else {
            this.updateByPrimaryKeySelective(param);
        }

        MtEventObjectTypeRelDTO2 mtEventObjectTypeRelDTO2 = new MtEventObjectTypeRelDTO2();
        BeanUtils.copyProperties(param, mtEventObjectTypeRelDTO2);
        // 通过事件类型ID获取事件类型
        MtEventObjectType mtEventObjectType = new MtEventObjectType();
        mtEventObjectType.setTenantId(tenantId);
        mtEventObjectType.setObjectTypeId(dto.getObjectTypeId());
        mtEventObjectType = mtEventObjectTypeMapper.selectOne(mtEventObjectType);
        mtEventObjectTypeRelDTO2.setObjectTypeCode(mtEventObjectType.getObjectTypeCode());
        mtEventObjectTypeRelDTO2.setDescription(mtEventObjectType.getDescription());
        return mtEventObjectTypeRelDTO2;
    }

    @Override
    public Page<MtEventObjectTypeRelDTO2> eventTypeIdLimitRel(Long tenantId, PageRequest pageRequest,
                    String eventTypeId) {
        return PageHelper.doPage(pageRequest,
                        () -> mtEventObjectTypeRelMapper.selectEventTypeId(tenantId, eventTypeId));
    }

    @Override
    public List<MtEventObjectTypeRelVO2> eventTypeLimitObjectTypeQueryUi(Long tenantId, MtEventObjectTypeRelVO1 dto) {
        return repository.eventTypeLimitObjectTypeQuery(tenantId, dto);
    }
}
