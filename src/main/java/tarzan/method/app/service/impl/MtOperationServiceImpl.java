package tarzan.method.app.service.impl;

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

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtOperationDTO3;
import tarzan.method.api.dto.MtOperationDTO4;
import tarzan.method.api.dto.MtOperationDTO5;
import tarzan.method.app.service.MtOperationService;
import tarzan.method.app.service.MtOperationSubstepService;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.infra.mapper.MtOperationMapper;

/**
 * 工序应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
@Service
public class MtOperationServiceImpl implements MtOperationService {

    private static final String MT_OPERATION_ATTR = "mt_operation_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtOperationSubstepService mtOperationSubstepService;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtOperationMapper mtOperationMapper;

    @Override
    public Page<MtOperationDTO4> queryOperationListForUi(Long tenantId, MtOperationDTO3 dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> mtOperationMapper.queryOperationForUi(tenantId, dto));
    }

    @Override
    public MtOperationDTO4 queryOperationDetailForUi(Long tenantId, String operationId) {
        MtOperationDTO3 dto = new MtOperationDTO3();
        dto.setOperationId(operationId);

        List<MtOperationDTO4> list = mtOperationMapper.queryOperationForUi(tenantId, dto);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        MtOperationDTO4 result = list.get(0);

        List<MtExtendAttrDTO> operationAttrList =
                mtExtendSettingsService.attrQuery(tenantId, operationId, MT_OPERATION_ATTR);
        result.setOperationAttrList(operationAttrList);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOperationForUi(Long tenantId, MtOperationDTO5 dto) {
        MtOperation mtOperation = new MtOperation();
        BeanUtils.copyProperties(dto, mtOperation);
        mtOperation.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtOperation);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        if (StringUtils.isNotEmpty(mtOperation.getOperationId())) {
            whereFields.add(new WhereField(MtOperation.FIELD_OPERATION_ID, Comparison.NOT_EQUAL));
        }
        whereFields.add(new WhereField(MtOperation.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtOperation.FIELD_SITE_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtOperation.FIELD_OPERATION_NAME, Comparison.EQUAL));
        whereFields.add(new WhereField(MtOperation.FIELD_REVISION, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        if (CollectionUtils.isNotEmpty(mtOperationRepository.selectOptional(mtOperation, criteria))) {
            throw new MtException("MT_ROUTER_0045",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0045", "ROUTER"));
        }

        if (StringUtils.isEmpty(dto.getOperationId())) {
            mtOperationRepository.insertSelective(mtOperation);
            mtOperationSubstepService.saveOperationSubstepForUi(tenantId, mtOperation.getOperationId(),
                    dto.getMtOperationSubstepList(), Boolean.TRUE);
        } else {
            MtOperation queryOperation = new MtOperation();
            queryOperation.setTenantId(tenantId);
            queryOperation.setOperationId(dto.getOperationId());
            queryOperation = mtOperationRepository.selectOne(queryOperation);
            if (null == queryOperation) {
                throw new MtException("MT_ROUTER_0006",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0006", "ROUTER", ""));
            }
            if ("FREEZE".equals(queryOperation.getOperationStatus())
                    || "ABANDON".equals(queryOperation.getOperationStatus())) {
                throw new MtException("MT_ROUTER_0023",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0023", "ROUTER", ""));
            }

            mtOperationRepository.updateByPrimaryKeySelective(mtOperation);
            mtOperationSubstepService.saveOperationSubstepForUi(tenantId, mtOperation.getOperationId(),
                    dto.getMtOperationSubstepList(), Boolean.FALSE);
        }

        if (CollectionUtils.isNotEmpty(dto.getOperationAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_OPERATION_ATTR, mtOperation.getOperationId(), null,
                    dto.getOperationAttrs());
        }

        return mtOperation.getOperationId();
    }
}
