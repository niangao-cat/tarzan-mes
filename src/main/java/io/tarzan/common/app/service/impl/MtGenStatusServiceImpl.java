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
import io.tarzan.common.api.dto.MtGenStatusDTO;
import io.tarzan.common.api.dto.MtGenStatusDTO2;
import io.tarzan.common.app.service.MtGenStatusService;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtGenStatusVO3;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.infra.mapper.MtGenStatusMapper;

/**
 * 状态应用服务默认实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Service
public class MtGenStatusServiceImpl extends BaseServiceImpl<MtGenStatus> implements MtGenStatusService {

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenStatusMapper mtGenStatusMapper;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Override
    public List<MtGenStatus> comboBoxUi(Long tenantId, MtGenStatusVO2 condition) {
        return mtGenStatusRepository.groupLimitStatusQuery(tenantId, condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveGenStatusForUi(Long tenantId, MtGenStatusDTO2 dto) {
        MtGenStatus mtGenStatus = new MtGenStatus();
        BeanUtils.copyProperties(dto, mtGenStatus);
        mtGenStatus.setTenantId(tenantId);

        return this.mtGenStatusRepository.genStatusBasicPropertyUpdate(tenantId, mtGenStatus);
    }

    @Override
    public Page<MtGenStatusVO3> listGenStatusForUi(Long tenantId, MtGenStatusDTO condition, PageRequest pageRequest) {

        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("GENERAL");
        queryType.setTypeGroup("SERVICE_PACKAGE");
        List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);

        MtGenStatus genStatus = new MtGenStatus();
        BeanUtils.copyProperties(condition, genStatus);
        genStatus.setTenantId(tenantId);

        Criteria criteria = new Criteria(genStatus);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtGenStatus.FIELD_TENANT_ID, Comparison.EQUAL));

        if (StringUtils.isNotEmpty(genStatus.getStatusGroup())) {
            whereFields.add(new WhereField(MtGenStatus.FIELD_STATUS_GROUP, Comparison.LIKE));
        }

        if (StringUtils.isNotEmpty(genStatus.getStatusCode())) {
            whereFields.add(new WhereField(MtGenStatus.FIELD_STATUS_CODE, Comparison.LIKE));
        }

        if (StringUtils.isNotEmpty(genStatus.getModule())) {
            whereFields.add(new WhereField(MtGenStatus.FIELD_MODULE, Comparison.EQUAL));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtGenStatus> base = PageHelper.doPageAndSort(pageRequest,
                        () -> mtGenStatusMapper.selectOptional(genStatus, criteria));

        Page<MtGenStatusVO3> result = new Page<MtGenStatusVO3>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());

        List<MtGenStatusVO3> voList = new ArrayList<MtGenStatusVO3>();
        for (MtGenStatus mtGenStatus : base) {
            MtGenStatusVO3 vo = new MtGenStatusVO3();
            BeanUtils.copyProperties(mtGenStatus, vo);
            Optional<MtGenType> statusOp =
                            types.stream().filter(t -> t.getTypeCode().equals(mtGenStatus.getModule())).findFirst();
            statusOp.ifPresent(t -> vo.setModuleDesc(t.getDescription()));
            voList.add(vo);
        }
        result.setContent(voList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeGenStatusForUi(Long tenantId, List<MtGenStatus> list) {
        mtGenStatusRepository.removeGenStatus(tenantId, list);
    }
}
