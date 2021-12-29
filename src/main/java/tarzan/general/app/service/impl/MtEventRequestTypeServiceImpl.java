package tarzan.general.app.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import tarzan.general.api.dto.MtEventRequestTypeDTO;
import tarzan.general.app.service.MtEventRequestTypeService;
import tarzan.general.domain.entity.MtEventRequestType;
import tarzan.general.domain.repository.MtEventRequestTypeRepository;
import tarzan.general.domain.vo.MtEventRequestTypeVO2;
import tarzan.general.infra.mapper.MtEventRequestTypeMapper;

/**
 * 事件组类型定义应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Service
public class MtEventRequestTypeServiceImpl implements MtEventRequestTypeService {

    @Autowired
    private MtEventRequestTypeMapper mtEventRequestTypeMapper;

    @Autowired
    private MtEventRequestTypeRepository mtEventRequestTypeRepository;

    @Override
    public Page<MtEventRequestTypeVO2> queryForUi(Long tenantId, MtEventRequestTypeDTO dto, PageRequest pageRequest) {
        MtEventRequestType mtEventRequestType = new MtEventRequestType();
        mtEventRequestType.setTenantId(tenantId);
        mtEventRequestType.setDescription(dto.getDescription());
        mtEventRequestType.setEnableFlag(dto.getEnableFlag());
        mtEventRequestType.setRequestTypeCode(dto.getRequestTypeCode());

        Criteria criteria = new Criteria(mtEventRequestType);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtEventRequestType.FIELD_TENANT_ID, Comparison.EQUAL));

        if (dto.getDescription() != null) {
            whereFields.add(new WhereField(MtEventRequestType.FIELD_DESCRIPTION, Comparison.LIKE));
        }
        if (dto.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtEventRequestType.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        if (dto.getRequestTypeCode() != null) {
            whereFields.add(new WhereField(MtEventRequestType.FIELD_REQUEST_TYPE_CODE, Comparison.LIKE));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        return PageHelper.doPageAndSort(pageRequest,
                        () -> this.mtEventRequestTypeMapper.selectOptional(mtEventRequestType, criteria));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveForUi(Long tenantId, MtEventRequestTypeVO2 dto) {
        if (null == dto) {
            return null;
        }

        MtEventRequestType mtEventRequestType = new MtEventRequestType();
        BeanUtils.copyProperties(dto, mtEventRequestType);
        return this.mtEventRequestTypeRepository.eventRequestTypeBasicPropertyUpdate(tenantId, mtEventRequestType);
    }

}
