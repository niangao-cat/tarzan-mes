package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtNcValidOperDTO;
import tarzan.method.app.service.MtNcValidOperService;
import tarzan.method.domain.entity.MtNcValidOper;
import tarzan.method.domain.repository.MtNcValidOperRepository;
import tarzan.method.infra.mapper.MtNcValidOperMapper;

/**
 * 不良代码工艺分配应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@Service
public class MtNcValidOperServiceImpl implements MtNcValidOperService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtNcValidOperRepository mtNcValidOperRepo;

    @Autowired
    private MtNcValidOperMapper mtNcValidOperMapper;

    @Override
    public List<MtNcValidOperDTO> queryNcValidOperListForUi(Long tenantId, String ncObjectId, String ncObjectType) {
        return mtNcValidOperMapper.queryNcValidOperList(tenantId, ncObjectId, ncObjectType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNcValidOperListForUi(Long tenantId, String ncObjectId, String ncObjectType,
                    List<MtNcValidOperDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }

        List<String> operationIdList =
                        dtoList.stream().map(MtNcValidOperDTO::getOperationId).distinct().collect(Collectors.toList());
        if (operationIdList.size() != dtoList.size()) {
            throw new MtException("MT_NC_CODE_0004",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0004", "NC_GROUP"));
        }
        for (MtNcValidOperDTO ever : dtoList) {
            MtNcValidOper ncValidOper = new MtNcValidOper();
            BeanUtils.copyProperties(ever, ncValidOper);
            ncValidOper.setNcObjectId(ncObjectId);
            ncValidOper.setNcObjectType(ncObjectType);

            MtNcValidOper vaildNcValidOper = new MtNcValidOper();
            vaildNcValidOper.setTenantId(tenantId);
            vaildNcValidOper.setNcValidOperId(ncValidOper.getNcValidOperId());
            vaildNcValidOper.setNcObjectType(ncValidOper.getNcObjectType());
            vaildNcValidOper.setNcObjectId(ncValidOper.getNcObjectId());
            vaildNcValidOper.setOperationId(ncValidOper.getOperationId());

            Criteria criteria = new Criteria(vaildNcValidOper);
            List<WhereField> whereFields = new ArrayList<WhereField>();
            whereFields.add(new WhereField(MtNcValidOper.FIELD_TENANT_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtNcValidOper.FIELD_NC_OBJECT_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtNcValidOper.FIELD_NC_OBJECT_TYPE, Comparison.EQUAL));
            whereFields.add(new WhereField(MtNcValidOper.FIELD_OPERATION_ID, Comparison.EQUAL));
            if (StringUtils.isNotEmpty(vaildNcValidOper.getNcValidOperId())) {
                whereFields.add(new WhereField(MtNcValidOper.FIELD_NC_VALID_OPER_ID, Comparison.NOT_EQUAL));
            }
            criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

            if (mtNcValidOperMapper.selectOptional(vaildNcValidOper, criteria).size() > 0) {
                throw new MtException("MT_NC_CODE_0004",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0004", "NC_CODE"));
            }

            ncValidOper.setTenantId(tenantId);
            if (StringUtils.isEmpty(ncValidOper.getNcValidOperId())) {
                mtNcValidOperRepo.insertSelective(ncValidOper);
            } else {
                mtNcValidOperRepo.updateByPrimaryKeySelective(ncValidOper);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeNcValidOperForUi(Long tenantId, String ncValidOperId) {
        if (StringUtils.isEmpty(ncValidOperId)) {
            return;
        }

        MtNcValidOper mtNcValidOper = new MtNcValidOper();
        mtNcValidOper.setTenantId(tenantId);
        mtNcValidOper.setNcValidOperId(ncValidOperId);
        if (mtNcValidOperRepo.delete(mtNcValidOper) == 0) {
            throw new MtException("数据删除失败.");
        }
    }
}
