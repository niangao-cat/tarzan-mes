package tarzan.general.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.api.dto.MtEventObjectTypeColumnDTO;
import tarzan.general.api.dto.MtEventObjectTypeDTO;
import tarzan.general.app.service.MtEventObjectTypeService;
import tarzan.general.domain.entity.MtEventObjectColumn;
import tarzan.general.domain.entity.MtEventObjectType;
import tarzan.general.domain.repository.MtEventObjectColumnRepository;
import tarzan.general.domain.repository.MtEventObjectTypeRepository;
import tarzan.general.domain.vo.MtEventObjectColumnVO;
import tarzan.general.domain.vo.MtEventObjectTypeColumnVO;
import tarzan.general.infra.mapper.MtEventObjectTypeMapper;

/**
 * 对象类型定义应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Service
public class MtEventObjectTypeServiceImpl extends BaseServiceImpl<MtEventObjectType>
                implements MtEventObjectTypeService {

    @Autowired
    private MtEventObjectTypeMapper mtEventObjectTypeMapper;

    @Autowired
    private MtEventObjectTypeRepository mtEventObjectTypeRepository;
    @Autowired
    private MtEventObjectColumnRepository mtEventObjectColumnRepository;

    @Override
    public Page<MtEventObjectType> queryEventObjectTypeForUi(Long tenantId, MtEventObjectTypeDTO dto,
                    PageRequest pageRequest) {
        MtEventObjectType queryEventObjectType = new MtEventObjectType();
        queryEventObjectType.setTenantId(tenantId);
        queryEventObjectType.setObjectTypeCode(dto.getObjectTypeCode());
        queryEventObjectType.setDescription(dto.getDescription());

        Criteria criteria = new Criteria(queryEventObjectType);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtEventObjectType.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtEventObjectType.FIELD_OBJECT_TYPE_CODE, Comparison.LIKE));
        whereFields.add(new WhereField(MtEventObjectType.FIELD_DESCRIPTION, Comparison.LIKE));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        return PageHelper.doPageAndSort(pageRequest,
                        () -> mtEventObjectTypeMapper.selectOptional(queryEventObjectType, criteria));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEventObjectType saveEventObjectTypeForUi(Long tenantId, MtEventObjectTypeDTO dto) {
        MtEventObjectType saveEventObjectType = new MtEventObjectType();
        BeanUtils.copyProperties(dto, saveEventObjectType);
        saveEventObjectType.setTenantId(tenantId);

        mtEventObjectTypeRepository.eventObjectTypeBasicPropertyUpdate(tenantId, saveEventObjectType);
        return saveEventObjectType;
    }

    @Override
    public MtEventObjectTypeColumnDTO queryEventObjectTypeQuerySqlForUi(Long tenantId, String objectTypeId) {
        MtEventObjectTypeColumnDTO result = new MtEventObjectTypeColumnDTO();
        result.setObjectTypeId(objectTypeId);
        MtEventObjectTypeColumnVO eventObjectTypeColumn =
                        mtEventObjectTypeMapper.queryEventObjectColumnById(tenantId, objectTypeId);

        if (eventObjectTypeColumn == null) {
            return result;
        }

        StringBuilder columnSql = new StringBuilder("SELECT\r\n");
        for (MtEventObjectColumnVO vo : eventObjectTypeColumn.getEventObjectColumnList()) {
            if ("Y".equals(vo.getEnableFlag())) {
                if ("DATE".equals(vo.getColumnType())) {
                    columnSql.append(" DATE_FORMAT(").append(vo.getColumnField()).append(",'%Y-%m-%d %H:%i:%S')")
                                    .append(" ").append(vo.getColumnTitle()).append(",");
                } else {
                    columnSql.append(" ").append(vo.getColumnField()).append(" ").append(vo.getColumnTitle())
                                    .append(",");
                }
                columnSql.append("\r\n");
            }
        }
        if (CollectionUtils.isNotEmpty(eventObjectTypeColumn.getEventObjectColumnList())) {
            int a = columnSql.lastIndexOf(",");
            columnSql.deleteCharAt(a);
            columnSql.append("\r\n").append(" FROM ").append("\r\n").append(eventObjectTypeColumn.getTableName())
                            .append("WHERE ").append("\r\n").append(eventObjectTypeColumn.getWhereClause());
        }
        result.setEventTypeQuerySql(columnSql.toString());

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteEventObjectTypeForUi(Long tenantId, List<String> objectTypeIdList) {
        if (CollectionUtils.isEmpty(objectTypeIdList)) {
            return 0;
        }

        int delCount = 0;
        MtEventObjectColumn mtEventObjectColumn;
        for (String id : objectTypeIdList) {
            delCount += this.deleteByPrimaryKey(id);

            mtEventObjectColumn = new MtEventObjectColumn();
            mtEventObjectColumn.setTenantId(tenantId);
            mtEventObjectColumn.setObjectTypeId(id);
            mtEventObjectColumnRepository.delete(mtEventObjectColumn);
        }

        if (delCount != objectTypeIdList.size()) {
            throw new MtException("数据删除失败.");
        }

        return delCount;
    }
}
