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
import tarzan.method.api.dto.MtNcSecondaryCodeDTO;
import tarzan.method.app.service.MtNcSecondaryCodeService;
import tarzan.method.domain.entity.MtNcSecondaryCode;
import tarzan.method.domain.repository.MtNcSecondaryCodeRepository;
import tarzan.method.infra.mapper.MtNcSecondaryCodeMapper;

/**
 * 次级不良代码应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@Service
public class MtNcSecondaryCodeServiceImpl implements MtNcSecondaryCodeService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtNcSecondaryCodeRepository mtNcSecondaryCodeRepo;

    @Autowired
    private MtNcSecondaryCodeMapper mtNcSecondaryCodeMapper;

    @Override
    public List<MtNcSecondaryCodeDTO> querySecondaryCodeListForUi(Long tenantId, String ncObjectId,
                                                                  String ncObjectType) {
        return mtNcSecondaryCodeMapper.querySecondaryCodeListForUi(tenantId, ncObjectId, ncObjectType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSecondaryCodeListForUi(Long tenantId, String ncObjectId, String ncObjectType,
                    List<MtNcSecondaryCodeDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }

        List<String> ncCodeIdList =
                        dtoList.stream().map(MtNcSecondaryCodeDTO::getNcCodeId).distinct().collect(Collectors.toList());
        if (ncCodeIdList.size() != dtoList.size()) {
            throw new MtException("MT_NC_CODE_0003",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0003", "NC_GROUP"));
        }

        for (MtNcSecondaryCodeDTO ever : dtoList) {
            MtNcSecondaryCode secondaryCode = new MtNcSecondaryCode();
            BeanUtils.copyProperties(ever, secondaryCode);
            secondaryCode.setNcObjectId(ncObjectId);
            secondaryCode.setNcObjectType(ncObjectType);

            MtNcSecondaryCode vaildNcSecondary = new MtNcSecondaryCode();
            vaildNcSecondary.setTenantId(tenantId);
            vaildNcSecondary.setNcSecondaryCodeId(secondaryCode.getNcSecondaryCodeId());
            vaildNcSecondary.setNcObjectType(secondaryCode.getNcObjectType());
            vaildNcSecondary.setNcObjectId(secondaryCode.getNcObjectId());
            vaildNcSecondary.setNcCodeId(secondaryCode.getNcCodeId());

            Criteria criteria = new Criteria(vaildNcSecondary);
            List<WhereField> whereFields = new ArrayList<WhereField>();
            whereFields.add(new WhereField(MtNcSecondaryCode.FIELD_TENANT_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtNcSecondaryCode.FIELD_NC_OBJECT_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtNcSecondaryCode.FIELD_NC_OBJECT_TYPE, Comparison.EQUAL));
            whereFields.add(new WhereField(MtNcSecondaryCode.FIELD_NC_CODE_ID, Comparison.EQUAL));
            if (StringUtils.isNotEmpty(vaildNcSecondary.getNcSecondaryCodeId())) {
                whereFields.add(new WhereField(MtNcSecondaryCode.FIELD_NC_SECONDARY_CODE_ID, Comparison.NOT_EQUAL));
            }
            criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

            if (mtNcSecondaryCodeMapper.selectOptional(vaildNcSecondary, criteria).size() > 0) {
                throw new MtException("MT_NC_CODE_0003",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0003", "NC_CODE"));
            }

            secondaryCode.setTenantId(tenantId);
            if (StringUtils.isEmpty(secondaryCode.getNcSecondaryCodeId())) {
                mtNcSecondaryCodeRepo.insertSelective(secondaryCode);
            } else {
                mtNcSecondaryCodeRepo.updateByPrimaryKeySelective(secondaryCode);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSecondaryCodeForUi(Long tenantId, String ncSecondaryCodeId) {
        if (StringUtils.isEmpty(ncSecondaryCodeId)) {
            return;
        }

        MtNcSecondaryCode mtNcSecondaryCode = new MtNcSecondaryCode();
        mtNcSecondaryCode.setTenantId(tenantId);
        mtNcSecondaryCode.setNcSecondaryCodeId(ncSecondaryCodeId);
        if (mtNcSecondaryCodeRepo.delete(mtNcSecondaryCode) == 0) {
            throw new MtException("数据删除失败.");
        }
    }
}
