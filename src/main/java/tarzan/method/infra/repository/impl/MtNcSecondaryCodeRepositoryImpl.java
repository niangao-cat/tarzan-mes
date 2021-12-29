package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtNcSecondaryCode;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtNcSecondaryCodeRepository;
import tarzan.method.infra.mapper.MtNcSecondaryCodeMapper;

/**
 * 次级不良代码 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@Component
public class MtNcSecondaryCodeRepositoryImpl extends BaseRepositoryImpl<MtNcSecondaryCode>
                implements MtNcSecondaryCodeRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepo;

    @Autowired
    private MtNcSecondaryCodeMapper mtNcSecondaryCodeMapper;

    @Override
    public List<MtNcSecondaryCode> ncCodeLimitRequiredSecondaryCodeQuery(Long tenantId, String ncCodeId) {
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeLimitRequiredSecondaryCodeQuery】"));
        }

        MtNcSecondaryCode queryCode = new MtNcSecondaryCode();
        queryCode.setTenantId(tenantId);
        queryCode.setNcObjectId(ncCodeId);
        queryCode.setNcObjectType("NC_CODE");
        queryCode.setRequiredFlag("Y");
        List<MtNcSecondaryCode> result = mtNcSecondaryCodeMapper.select(queryCode);

        if (CollectionUtils.isNotEmpty(result)) {
            return result;
        } else {
            String ncGroupId = mtNcCodeRepo.ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            MtNcSecondaryCode queryGroup = new MtNcSecondaryCode();
            queryGroup.setTenantId(tenantId);
            queryGroup.setNcObjectId(ncGroupId);
            queryGroup.setNcObjectType("NC_GROUP");
            queryGroup.setRequiredFlag("Y");
            result = mtNcSecondaryCodeMapper.select(queryGroup);
        }
        return result;
    }

    @Override
    public List<MtNcSecondaryCode> ncSecondaryCodeQuery(Long tenantId, String ncCodeId) {
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncSecondaryCodeQuery】"));
        }
        MtNcSecondaryCode queryCode = new MtNcSecondaryCode();
        queryCode.setTenantId(tenantId);
        queryCode.setNcObjectId(ncCodeId);
        queryCode.setNcObjectType("NC_CODE");
        List<MtNcSecondaryCode> result = mtNcSecondaryCodeMapper.select(queryCode);

        if (CollectionUtils.isNotEmpty(result)) {
            return result;
        } else {
            String ncGroupId = mtNcCodeRepo.ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            MtNcSecondaryCode queryGroup = new MtNcSecondaryCode();
            queryGroup.setTenantId(tenantId);
            queryGroup.setNcObjectId(ncGroupId);
            queryGroup.setNcObjectType("NC_GROUP");
            result = mtNcSecondaryCodeMapper.select(queryGroup);
        }

        return result;
    }
}
