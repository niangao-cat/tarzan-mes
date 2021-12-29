package tarzan.general.infra.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.entity.MtEventObjectColumn;
import tarzan.general.domain.entity.MtEventObjectType;
import tarzan.general.domain.entity.MtEventObjectTypeRel;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventObjectTypeRelRepository;
import tarzan.general.domain.repository.MtEventObjectTypeRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.general.domain.vo.*;
import tarzan.general.infra.mapper.MtEventObjectColumnMapper;
import tarzan.general.infra.mapper.MtEventObjectTypeMapper;
import tarzan.general.infra.mapper.MtEventObjectTypeRelMapper;
import tarzan.general.infra.mapper.MtEventTypeMapper;

/**
 * 事件类型与对象类型关系定义 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Component
public class MtEventObjectTypeRelRepositoryImpl extends BaseRepositoryImpl<MtEventObjectTypeRel>
                implements MtEventObjectTypeRelRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtEventRepository iMtEventService;
    @Autowired
    private MtEventTypeRepository iMtEventTypeService;
    @Autowired
    private MtEventObjectTypeRepository iMtEventObjectTypeService;

    @Autowired
    private MtEventObjectColumnMapper mtEventObjectColumnMapper;
    @Autowired
    private MtEventTypeMapper mtEventTypeMapper;
    @Autowired
    private MtEventObjectTypeMapper mtEventObjectTypeMapper;
    @Autowired
    private MtEventObjectTypeRelMapper mtEventObjectTypeRelMapper;

    @Override
    public List<MtEventObjectTypeRelVO2> eventTypeLimitObjectTypeQuery(Long tenantId,MtEventObjectTypeRelVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventTypeCode", "【API：eventTypeLimitObjectTypeQuery】"));
        }

        List<MtEventObjectTypeRelVO2> resultList = new ArrayList<>();

        // 2. 通过EventTypeCode 获取 eventType
        MtEventType mtEventType = new MtEventType();
        mtEventType.setTenantId(tenantId);
        mtEventType.setEventTypeCode(dto.getEventTypeCode());
        mtEventType = mtEventTypeMapper.selectOne(mtEventType);
        if (mtEventType == null || StringUtils.isEmpty(mtEventType.getEventTypeId())) {
            return resultList;
        }

        // 3. 通过EventTypeId 获取 EventObjectTypeRel
        MtEventObjectTypeRel mtEventObjectTypeRel = new MtEventObjectTypeRel();
        mtEventObjectTypeRel.setTenantId(tenantId);
        mtEventObjectTypeRel.setEnableFlag(dto.getEnableFlag());
        mtEventObjectTypeRel.setEventTypeId(mtEventType.getEventTypeId());
        List<MtEventObjectTypeRel> mtEventObjectTypeRelList = mtEventObjectTypeRelMapper.select(mtEventObjectTypeRel);
        if (CollectionUtils.isEmpty(mtEventObjectTypeRelList)) {
            return resultList;
        }

        for (MtEventObjectTypeRel tempRel : mtEventObjectTypeRelList) {
            // 4. 获取 EventObjectType
            MtEventObjectType mtEventObjectType =
                            iMtEventObjectTypeService.objectTypeGet(tenantId, tempRel.getObjectTypeId());
            if (mtEventObjectType != null) {
                MtEventObjectTypeRelVO2 result = new MtEventObjectTypeRelVO2();
                result.setEnableFlag(tempRel.getEnableFlag());
                result.setObjectTypeCode(mtEventObjectType.getObjectTypeCode());
                result.setObjectTypeId(mtEventObjectType.getObjectTypeId());
                result.setDescription(mtEventObjectType.getDescription());
                result.setRelId(tempRel.getRelId());
                resultList.add(result);
            }
        }

        return resultList;
    }

    @Override
    public List<MtEventObjectTypeRelVO4> objectTypeLimitEventTypeQuery(Long tenantId, MtEventObjectTypeRelVO3 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getObjectTypeCode())) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "objectTypeCode", "【API：objectTypeLimitEventTypeQuery】"));
        }

        List<MtEventObjectTypeRelVO4> resultList = new ArrayList<>();

        // 2. 获取 EventObjectType
        MtEventObjectType mtEventObjectType = new MtEventObjectType();
        mtEventObjectType.setTenantId(tenantId);
        mtEventObjectType.setObjectTypeCode(dto.getObjectTypeCode());
        mtEventObjectType = mtEventObjectTypeMapper.selectOne(mtEventObjectType);
        if (mtEventObjectType == null || StringUtils.isEmpty(mtEventObjectType.getObjectTypeId())) {
            return resultList;
        }

        // 3. 获取 EventObjectTypeRel
        MtEventObjectTypeRel mtEventObjectTypeRel = new MtEventObjectTypeRel();
        mtEventObjectTypeRel.setTenantId(tenantId);
        mtEventObjectTypeRel.setEnableFlag(dto.getEnableFlag());
        mtEventObjectTypeRel.setObjectTypeId(mtEventObjectType.getObjectTypeId());
        List<MtEventObjectTypeRel> mtEventObjectTypeRelList = mtEventObjectTypeRelMapper.select(mtEventObjectTypeRel);
        if (CollectionUtils.isEmpty(mtEventObjectTypeRelList)) {
            return resultList;
        }

        for (MtEventObjectTypeRel tempRel : mtEventObjectTypeRelList) {
            // 4. 获取 EventObjectType
            MtEventType mtEventType = iMtEventTypeService.eventTypeGet(tenantId, tempRel.getEventTypeId());
            if (mtEventType != null) {
                MtEventObjectTypeRelVO4 result = new MtEventObjectTypeRelVO4();
                result.setEnableFlag(tempRel.getEnableFlag());
                result.setEventTypeCode(mtEventType.getEventTypeCode());
                result.setEventTypeId(mtEventType.getEventTypeId());
                resultList.add(result);
            }
        }

        return resultList;
    }

    @Override
    public List<MtEventObjectTypeRelVO6> eventLimitEventObjectInfoQuery(Long tenantId, MtEventObjectTypeRelVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventId", "【API：eventLimitEventObjectInfoQuery】"));
        }

        // 2. 获取 objectTypeIds
        List<String> objectTypeIds = mtEventObjectTypeRelMapper.selectObjectTypeId(tenantId, dto.getEventId());
        if (CollectionUtils.isEmpty(objectTypeIds)) {
            return Collections.emptyList();
        }

        // 3. 判断是否输入 objectTypeCode
        if (StringUtils.isNotEmpty(dto.getObjectTypeCode())) {
            MtEventObjectType mtEventObjectType = new MtEventObjectType();
            mtEventObjectType.setTenantId(tenantId);
            mtEventObjectType.setObjectTypeCode(dto.getObjectTypeCode());
            mtEventObjectType = mtEventObjectTypeMapper.selectOne(mtEventObjectType);
            if (mtEventObjectType == null || StringUtils.isEmpty(mtEventObjectType.getObjectTypeId())) {
                throw new MtException("MT_EVENT_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0004", "EVENT", "objectTypeCode", "【API：eventLimitEventObjectInfoQuery】"));
            }

            if (!objectTypeIds.contains(mtEventObjectType.getObjectTypeId())) {
                throw new MtException("MT_EVENT_0003", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_EVENT_0003", "EVENT", "【API：eventLimitEventObjectInfoQuery】"));
            } else {
                objectTypeIds.clear();
                objectTypeIds.add(mtEventObjectType.getObjectTypeId());
            }
        }

        // 批量获取 eventObjectType 数据
        List<MtEventObjectType> mtEventObjectTypeList =
                        mtEventObjectTypeMapper.selectByIdsCustom(tenantId, objectTypeIds);

        List<MtEventObjectTypeRelVO6> resultList = new ArrayList<>();

        // 4. 循环处理 objectTypeIds 处理数据
        for (MtEventObjectType objectType : mtEventObjectTypeList) {
            // 5. 获取 column 数据
            StringBuilder Sql = new StringBuilder("SELECT ");

            MtEventObjectColumn objectColumn = new MtEventObjectColumn();
            objectColumn.setTenantId(tenantId);
            objectColumn.setEnableFlag("Y");
            objectColumn.setObjectTypeId(objectType.getObjectTypeId());
            List<MtEventObjectColumn> objectColumnList = mtEventObjectColumnMapper.select(objectColumn);
            if (CollectionUtils.isNotEmpty(objectColumnList)) {

                // 筛选展示列
                List<MtEventObjectColumn> displayColumnList =
                                objectColumnList.stream().filter(t -> "Y".equals(t.getDisplayFlag()))
                                                .sorted(Comparator.comparing(MtEventObjectColumn::getLineNumber))
                                                .collect(Collectors.toList());

                StringBuilder columnSql = new StringBuilder();
                for (int i = 1; i <= displayColumnList.size(); i++) {
                    MtEventObjectColumn column = displayColumnList.get(i - 1);
                    // 拼接 sql
                    if ("DATE".equals(column.getColumnType())) {
                        columnSql.append("  DATE_FORMAT(" + column.getColumnField() + ",'%Y-%m-%d %H:%i:%S'),");
                    } else {
                        columnSql.append("  " + column.getColumnField() + ",");
                    }
                }

                // 去掉最后一位逗号
                columnSql.deleteCharAt(columnSql.length() - 1);

                // 拼接sql
                Sql.append(columnSql);
                Sql.append("  FROM " + objectType.getTableName() + "  WHERE ");

                StringBuilder whereSql = new StringBuilder("  " + objectType.getWhereClause());

                // 查找 Event 限制列
                List<MtEventObjectColumn> eventColumnList = objectColumnList.stream()
                                .filter(t -> "Y".equals(t.getEventFlag())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(eventColumnList)) {
                    throw new MtException("MT_EVENT_0006",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0006", "EVENT",
                                                    objectType.getObjectTypeCode(),
                                                    "【API:eventLimitEventObjectInfoQuery】"));
                }

                whereSql.append("  AND " + eventColumnList.get(0).getColumnField())
                                .append(" = " + dto.getEventId() + " ");

                // 拼接sql
                Sql.append(whereSql);

                // 执行SQL语句
                this.jdbcTemplate.query(Sql.toString(), new ResultSetExtractor<List<MtEventObjectColumnValueVO>>() {
                    @Override
                    public List<MtEventObjectColumnValueVO> extractData(ResultSet rs)
                                    throws SQLException, DataAccessException {
                        while (rs.next()) {
                            // 列名、列值 结果集
                            List<MtEventObjectColumnValueVO> columnValueList = new ArrayList<>();

                            // 根据拼接的列个数，获取每个列对应的数据
                            for (int i = 0; i < displayColumnList.size(); i++) {
                                MtEventObjectColumn column = displayColumnList.get(i);

                                MtEventObjectColumnValueVO columnValue = new MtEventObjectColumnValueVO();
                                columnValue.setIndex(i + 1);
                                columnValue.setTitle(column.getColumnTitle());
                                columnValue.setValue(rs.getString(columnValue.getIndex()));
                                columnValueList.add(columnValue);
                            }

                            MtEventObjectTypeRelVO6 result = new MtEventObjectTypeRelVO6();
                            result.setObjectTypeId(objectType.getObjectTypeId());
                            result.setObjectTypeCode(objectType.getObjectTypeCode());
                            result.setObjectDescription(objectType.getDescription());
                            result.setColumnValueList(columnValueList);
                            resultList.add(result);
                        }
                        return null;
                    }
                });
            }
        }

        return resultList;
    }

    @Override
    public List<MtEventObjectTypeRelVO8> requestLimitEventObjectInfoQuery(Long tenantId, MtEventObjectTypeRelVO7 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventRequestId())) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_EVENT_0001", "EVENT", "eventRequestId", "【API：requestLimitEventObjectInfoQuery】"));
        }

        List<String> eventIds = new ArrayList<>();

        // 2. 判断是否传入 eventId
        if (StringUtils.isEmpty(dto.getEventId())) {
            MtEventVO2 eventVO2 = new MtEventVO2();
            eventVO2.setEventRequestId(dto.getEventRequestId());
            eventIds = iMtEventService.requestLimitEventQuery(tenantId, eventVO2);
        } else {
            eventIds.add(dto.getEventId());
        }

        if (CollectionUtils.isEmpty(eventIds)) {
            return Collections.emptyList();
        }

        List<MtEventObjectTypeRelVO8> resultList = new ArrayList<>();

        for (String eventId : eventIds) {
            // 3. 获取EVENT_TYPE_CODE
            MtEventType mtEventType = mtEventTypeMapper.selectByEventId(tenantId, eventId);

            // 4. 获取事件影响对象的信息内容
            MtEventObjectTypeRelVO5 mtEventObjectTypeRelVO5 = new MtEventObjectTypeRelVO5();
            mtEventObjectTypeRelVO5.setEventId(eventId);

            if (StringUtils.isNotEmpty(dto.getObjectTypeCode())) {
                mtEventObjectTypeRelVO5.setObjectTypeCode(dto.getObjectTypeCode());
            }

            List<MtEventObjectTypeRelVO6> mtEventObjectTypeRelVO6List =
                            eventLimitEventObjectInfoQuery(tenantId, mtEventObjectTypeRelVO5);

            if (CollectionUtils.isNotEmpty(mtEventObjectTypeRelVO6List)) {
                mtEventObjectTypeRelVO6List.forEach(t -> {
                    MtEventObjectTypeRelVO8 result = new MtEventObjectTypeRelVO8();
                    result.setEventId(eventId);
                    if (mtEventType != null) {
                        result.setEventTypeCode(mtEventType.getEventTypeCode());
                    }
                    result.setObjectTypeCode(t.getObjectTypeCode());
                    result.setObjectDescription(t.getObjectDescription());
                    result.setColumnValueList(t.getColumnValueList());
                    resultList.add(result);
                });
            }
        }

        return resultList;
    }

}
