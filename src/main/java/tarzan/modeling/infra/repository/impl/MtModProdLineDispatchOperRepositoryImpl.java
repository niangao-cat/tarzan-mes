package tarzan.modeling.infra.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.modeling.domain.entity.MtModProdLineDispatchOper;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.repository.MtModProdLineDispatchOperRepository;
import tarzan.modeling.domain.repository.MtModProdLineManufacturingRepository;
import tarzan.modeling.domain.vo.MtModProdLineDispatchOperVO1;
import tarzan.modeling.infra.mapper.MtModProdLineDispatchOperMapper;

/**
 * 生产线调度指定工艺 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Component
public class MtModProdLineDispatchOperRepositoryImpl extends BaseRepositoryImpl<MtModProdLineDispatchOper>
        implements MtModProdLineDispatchOperRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModProdLineDispatchOperMapper mtModProdLineDispatchOperMapper;

    @Autowired
    private MtModProdLineManufacturingRepository mtModProdLineManufacturingRepository;

    @Override
    public List<String> prodLineLimitDispatchOperationQuery(Long tenantId, String prodLineId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(prodLineId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineLimitDispatchOperationQuery】"));
        }

        MtModProdLineDispatchOper mtModProdLineDispatchOper = new MtModProdLineDispatchOper();
        mtModProdLineDispatchOper.setTenantId(tenantId);
        mtModProdLineDispatchOper.setProdLineId(prodLineId);
        List<MtModProdLineDispatchOper> mtModProdLineDispatchOperations =
                mtModProdLineDispatchOperMapper.select(mtModProdLineDispatchOper);

        if (CollectionUtils.isEmpty(mtModProdLineDispatchOperations)) {
            return Collections.emptyList();
        } else {
            return mtModProdLineDispatchOperations.stream().map(MtModProdLineDispatchOper::getOperationId)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void prodLineDispatchOperationValidate(Long tenantId, MtModProdLineDispatchOperVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineDispatchOperationValidate】"));
        }

        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "operationId", "【API:prodLineDispatchOperationValidate】"));
        }

        // 2. 获取该生产线的调度方式dispatchMethod
        MtModProdLineManufacturing mtModProdLineManufacturing = mtModProdLineManufacturingRepository
                .prodLineManufacturingPropertyGet(tenantId, dto.getProdLineId());
        if (mtModProdLineManufacturing == null
                || !"SPECIAL_OPERATION".equals(mtModProdLineManufacturing.getDispatchMethod())) {
            throw new MtException("MT_MODELING_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0031", "MODELING", "【API:prodLineDispatchOperationValidate】"));
        }

        MtModProdLineDispatchOper mtModProdLineDispatchOper = new MtModProdLineDispatchOper();
        mtModProdLineDispatchOper.setTenantId(tenantId);
        mtModProdLineDispatchOper.setProdLineId(dto.getProdLineId());
        mtModProdLineDispatchOper.setOperationId(dto.getOperationId());
        mtModProdLineDispatchOper = mtModProdLineDispatchOperMapper.selectOne(mtModProdLineDispatchOper);
        if (mtModProdLineDispatchOper == null
                || StringUtils.isEmpty(mtModProdLineDispatchOper.getDispatchOperationId())) {
            throw new MtException("MT_MODELING_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0032", "MODELING", "【API:prodLineDispatchOperationValidate】"));
        }
    }

    @Override
    public List<String> operationLimitProdLineQuery(Long tenantId, String operationId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(operationId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "operationId", "【API:operationLimitProdLineQuery】"));
        }

        MtModProdLineDispatchOper mtModProdLineDispatchOper = new MtModProdLineDispatchOper();
        mtModProdLineDispatchOper.setTenantId(tenantId);
        mtModProdLineDispatchOper.setOperationId(operationId);
        List<MtModProdLineDispatchOper> mtModProdLineDispatchOperations =
                mtModProdLineDispatchOperMapper.select(mtModProdLineDispatchOper);

        if (CollectionUtils.isEmpty(mtModProdLineDispatchOperations)) {
            return Collections.emptyList();
        } else {
            return mtModProdLineDispatchOperations.stream().map(MtModProdLineDispatchOper::getProdLineId)
                    .collect(Collectors.toList());
        }
    }

}
