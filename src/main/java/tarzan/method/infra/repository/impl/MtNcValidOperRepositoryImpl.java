package tarzan.method.infra.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtNcValidOper;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtNcValidOperRepository;
import tarzan.method.domain.vo.MtNcValidOperVO1;
import tarzan.method.infra.mapper.MtNcValidOperMapper;

/**
 * 不良代码工艺分配 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@Component
public class MtNcValidOperRepositoryImpl extends BaseRepositoryImpl<MtNcValidOper> implements MtNcValidOperRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtNcValidOperMapper mtNcValidOperMapper;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepo;

    @Override
    public String ncCodeOperationValidate(Long tenantId, MtNcValidOperVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getNcCodeId())) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeOperationValidate】"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "operationId", "【API:ncCodeOperationValidate】"));
        }

        MtNcValidOper mtNcValidOper = new MtNcValidOper();
        mtNcValidOper.setTenantId(tenantId);
        mtNcValidOper.setOperationId(dto.getOperationId());
        mtNcValidOper.setNcObjectId(dto.getNcCodeId());
        mtNcValidOper.setNcObjectType("NC_CODE");
        mtNcValidOper = mtNcValidOperMapper.selectOne(mtNcValidOper);

        return mtNcValidOper != null && StringUtils.isNotEmpty(mtNcValidOper.getNcValidOperId()) ? "Y" : "N";

    }

    @Override
    public List<MtNcValidOper> ncValidOperationQuery(Long tenantId, String ncCodeId) {

        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncValidOperationQuery】"));
        }
        // 1.1获取不良代码可用清单[P1]
        MtNcValidOper ncValidOper = new MtNcValidOper();
        ncValidOper.setTenantId(tenantId);
        ncValidOper.setNcObjectId(ncCodeId);
        ncValidOper.setNcObjectType("NC_CODE");
        List<MtNcValidOper> rs = mtNcValidOperMapper.select(ncValidOper);
        if (rs == null || rs.size() == 0) {
            // 在表MT_NC_VALID_OPER进行查询：
            // 从mt_Nc_Code获取数据
            String ncObjectId = mtNcCodeRepo.ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (ncObjectId == null) {
                return Collections.emptyList();
            } else {
                ncValidOper.setNcObjectId(ncObjectId);
            }
            ncValidOper.setNcObjectType("NC_GROUP");
            rs = mtNcValidOperMapper.select(ncValidOper);
        }
        return rs;

    }

    @Override
    public List<MtNcValidOper> operationValidNcQuery(Long tenantId, String operationId) {
        if (StringUtils.isEmpty(operationId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "operationId", "【API:operationValidNcQuery】"));
        }

        MtNcValidOper mtNcValidOper = new MtNcValidOper();
        mtNcValidOper.setTenantId(tenantId);
        mtNcValidOper.setOperationId(operationId);
        return mtNcValidOperMapper.select(mtNcValidOper);
    }
}
