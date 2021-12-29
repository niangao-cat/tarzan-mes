package io.tarzan.common.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtNumrangeObjectColumnDTO2;
import io.tarzan.common.api.dto.MtNumrangeObjectColumnDTO3;
import io.tarzan.common.app.service.MtNumrangeObjectColumnService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtNumrangeObjectColumnVO;
import io.tarzan.common.infra.mapper.MtNumrangeObjectColumnMapper;

/**
 * 编码对象属性应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@Service
public class MtNumrangeObjectColumnServiceImpl extends BaseServiceImpl<MtNumrangeObjectColumn>
                implements MtNumrangeObjectColumnService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtNumrangeObjectColumnMapper mapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Override
    public Page<MtNumrangeObjectColumnVO> listNumrangeObjectColumnForUi(Long tenantId, MtNumrangeObjectColumnDTO2 dto,
                                                                        PageRequest pageRequest) {

        MtNumrangeObjectColumn mtNumrangeObjectColumn = new MtNumrangeObjectColumn();
        BeanUtils.copyProperties(dto, mtNumrangeObjectColumn);
        Page<MtNumrangeObjectColumnVO> mtNumrangeObjectColumnVOS = PageHelper.doPage(pageRequest,
                        () -> mapper.selectByConditionForUi(tenantId, mtNumrangeObjectColumn));

        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("GENERAL");
        queryType.setTypeGroup("SERVICE_PACKAGE");
        List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
        Map<String, MtGenType> typesMap = types.stream().collect(Collectors.toMap(t -> t.getTypeCode(), t -> t));

        for (MtNumrangeObjectColumnVO columnVO : mtNumrangeObjectColumnVOS) {
            columnVO.setModuleDesc(null == typesMap.get(columnVO.getModule()) ? null
                            : typesMap.get(columnVO.getModule()).getDescription());
        }
        return mtNumrangeObjectColumnVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveNumrangeObjectColumnForUi(Long tenantId, List<MtNumrangeObjectColumnDTO3> list) {

        List<String> sqlList = new ArrayList<String>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date date = new Date();
        List<MtNumrangeObjectColumnDTO3> columnDTO3s = list.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getModule()) && StringUtils.isNotEmpty(t.getTypeGroup()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(columnDTO3s) && columnDTO3s.size() > 1) {
            throw new MtException("MT_GENERAL_0062",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0062", "GENERAL"));
        }
        for (MtNumrangeObjectColumnDTO3 dto : list) {

            MtNumrangeObjectColumn mtNumrangeObjectColumn = new MtNumrangeObjectColumn();
            BeanUtils.copyProperties(dto, mtNumrangeObjectColumn);
            mtNumrangeObjectColumn.setTenantId(tenantId);

            if (StringUtils.isNotEmpty(mtNumrangeObjectColumn.getObjectColumnId())) {
                mtNumrangeObjectColumn.setLastUpdatedBy(userId);
                mtNumrangeObjectColumn.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getUpdateSql(mtNumrangeObjectColumn));
            } else {
                mtNumrangeObjectColumn.setObjectColumnId(this.customDbRepository.getNextKey("mt_numrange_object_column_s"));
                mtNumrangeObjectColumn.setCid(
                                Long.parseLong(this.customDbRepository.getNextKey("mt_numrange_object_column_cid_s")));
                mtNumrangeObjectColumn.setCreatedBy(userId);
                mtNumrangeObjectColumn.setLastUpdatedBy(userId);
                mtNumrangeObjectColumn.setCreationDate(date);
                mtNumrangeObjectColumn.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getInsertSql(mtNumrangeObjectColumn));
            }
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveNumrangeObjectColumnForUi(Long tenantId, MtNumrangeObjectColumnDTO3 dto) {

        // 唯一性校验
        MtNumrangeObjectColumn oldObjectColumn = new MtNumrangeObjectColumn();
        oldObjectColumn.setObjectId(dto.getObjectId());
        oldObjectColumn.setTenantId(tenantId);
        oldObjectColumn.setObjectColumnCode(dto.getObjectColumnCode());
        oldObjectColumn = mapper.selectOne(oldObjectColumn);

        if (oldObjectColumn != null && StringUtils.isEmpty(dto.getObjectColumnId())
                        && (StringUtils.isEmpty(dto.getObjectColumnId())
                                        || !dto.getObjectColumnId().equals(oldObjectColumn.getObjectColumnId()))) {
            throw new MtException("MT_GENERAL_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0004", "GENERAL", "objectColumnCode", ""));
        }

        // 获取类型组不为空的数据
        String objectColumnId = this.numrangeObjectColumnModuleForUi(tenantId, dto.getObjectId());
        Boolean oldFlag = StringUtils.isNotEmpty(objectColumnId);
        Boolean newFlag = StringUtils.isNotEmpty(dto.getModule()) && StringUtils.isNotEmpty(dto.getTypeGroup());


        // 新增或更新数据
        MtNumrangeObjectColumn mtNumrangeObjectColumn = new MtNumrangeObjectColumn();
        BeanUtils.copyProperties(dto, mtNumrangeObjectColumn);
        mtNumrangeObjectColumn.setTenantId(tenantId);

        if (StringUtils.isNotEmpty(mtNumrangeObjectColumn.getObjectColumnId())) {
            if (oldFlag && newFlag && !mtNumrangeObjectColumn.getObjectColumnId().equalsIgnoreCase(objectColumnId)) {
                throw new MtException("MT_GENERAL_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0062", "GENERAL"));
            }
            updateByPrimaryKeySelective(mtNumrangeObjectColumn);
        } else {
            if (oldFlag && newFlag) {
                throw new MtException("MT_GENERAL_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0062", "GENERAL"));
            }
            insertSelective(mtNumrangeObjectColumn);
        }
        return mtNumrangeObjectColumn.getObjectColumnId();
    }

    @Override
    public String numrangeObjectColumnModuleForUi(Long tenantId, String objectId) {
        MtNumrangeObjectColumn mtNumrangeObjectColumn = new MtNumrangeObjectColumn();
        mtNumrangeObjectColumn.setTenantId(tenantId);
        mtNumrangeObjectColumn.setObjectId(objectId);
        List<MtNumrangeObjectColumn> columns = mapper.select(mtNumrangeObjectColumn);
        String objectColumnId = null;
        if (CollectionUtils.isNotEmpty(columns)) {
            Optional<MtNumrangeObjectColumn> first = columns.stream().filter(
                            t -> StringUtils.isNotEmpty(t.getModule()) && StringUtils.isNotEmpty(t.getTypeGroup()))
                            .findFirst();
            if (first.isPresent()) {
                objectColumnId = first.get().getObjectColumnId();
            }
        }
        return objectColumnId;
    }
}
