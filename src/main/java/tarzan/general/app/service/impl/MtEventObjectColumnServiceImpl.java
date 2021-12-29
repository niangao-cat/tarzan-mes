package tarzan.general.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.api.dto.MtEventObjectColumnDTO;
import tarzan.general.app.service.MtEventObjectColumnService;
import tarzan.general.domain.entity.MtEventObjectColumn;
import tarzan.general.infra.mapper.MtEventObjectColumnMapper;

/**
 * 对象列定义应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Service
public class MtEventObjectColumnServiceImpl extends BaseServiceImpl<MtEventObjectColumn>
                implements MtEventObjectColumnService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventObjectColumnMapper mtEventObjectColumnMapper;

    @Override
    public List<MtEventObjectColumn> queryEventObjectColumnByEventObjectTypeForUi(Long tenantId,
                    String eventObjectTypeId, PageRequest pageRequest) {
        MtEventObjectColumn queryEventObjectColumn = new MtEventObjectColumn();
        queryEventObjectColumn.setTenantId(tenantId);
        queryEventObjectColumn.setObjectTypeId(eventObjectTypeId);

        if (StringUtils.isEmpty(eventObjectTypeId)) {
            return null;
        }

        return PageHelper.doPageAndSort(pageRequest, () -> mtEventObjectColumnMapper.select(queryEventObjectColumn));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEventObjectColumnDTO saveEventObjectColumnForUi(Long tenantId, MtEventObjectColumnDTO dto) {
        MtEventObjectColumn queryEventObjectColumn = new MtEventObjectColumn();
        queryEventObjectColumn.setTenantId(tenantId);
        queryEventObjectColumn.setObjectTypeId(dto.getObjectTypeId());
        queryEventObjectColumn.setEnableFlag("Y");
        queryEventObjectColumn.setEventFlag("Y");
        queryEventObjectColumn = mtEventObjectColumnMapper.selectOne(queryEventObjectColumn);
        if ("Y".equals(dto.getEventFlag()) && queryEventObjectColumn != null
                        && !queryEventObjectColumn.getObjectColumnId().equals(dto.getObjectColumnId())) {
            throw new MtException("MT_EVENT_0016",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0016", "EVENT"));
        }

        MtEventObjectColumn saveEventObjectColumn = new MtEventObjectColumn();
        BeanUtils.copyProperties(dto, saveEventObjectColumn);
        saveEventObjectColumn.setTenantId(tenantId);

        Criteria criteria = new Criteria(saveEventObjectColumn);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        if (StringUtils.isNotEmpty(dto.getObjectColumnId())) {
            whereFields.add(new WhereField(MtEventObjectColumn.FIELD_OBJECT_COLUMN_ID, Comparison.NOT_EQUAL));
        }
        whereFields.add(new WhereField(MtEventObjectColumn.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtEventObjectColumn.FIELD_OBJECT_TYPE_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtEventObjectColumn.FIELD_COLUMN_FIELD, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        if (mtEventObjectColumnMapper.selectOptional(saveEventObjectColumn, criteria).size() > 0) {
            throw new MtException("MT_EVENT_0016",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0016", "EVENT"));
        }

        if (StringUtils.isEmpty(dto.getObjectColumnId())) {
            this.insertSelective(saveEventObjectColumn);
        } else {
            this.updateByPrimaryKeySelective(saveEventObjectColumn);
        }

        BeanUtils.copyProperties(saveEventObjectColumn, dto);

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteEventObjectColumnForUi(Long tenantId, List<String> eventObjectColumnIdList) {
        if (CollectionUtils.isEmpty(eventObjectColumnIdList)) {
            return 0;
        }

        int delCount = 0;
        MtEventObjectColumn mtEventObjectColumn;
        for (String id : eventObjectColumnIdList) {
            mtEventObjectColumn = new MtEventObjectColumn();
            mtEventObjectColumn.setTenantId(tenantId);
            mtEventObjectColumn.setObjectColumnId(id);
            delCount += this.delete(mtEventObjectColumn);
        }

        if (delCount != eventObjectColumnIdList.size()) {
            throw new MtException("数据删除失败.");
        }

        return delCount;
    }
}
