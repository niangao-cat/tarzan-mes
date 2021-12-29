package tarzan.modeling.app.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtException;
import tarzan.modeling.api.dto.MtModProdLineDispatchOperDTO;
import tarzan.modeling.app.service.MtModProdLineDispatchOperService;
import tarzan.modeling.domain.entity.MtModProdLineDispatchOper;
import tarzan.modeling.domain.repository.MtModProdLineDispatchOperRepository;
import tarzan.modeling.infra.mapper.MtModProdLineDispatchOperMapper;

/**
 * 生产线调度指定工艺应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Service
public class MtModProdLineDispatchOperServiceImpl implements MtModProdLineDispatchOperService {

    @Autowired
    private MtModProdLineDispatchOperRepository mtModProdLineDispatchOperRepository;

    @Autowired
    private MtModProdLineDispatchOperMapper mtModProdLineDispatchOperMapper;

    @Override
    public List<MtModProdLineDispatchOper> prodLineIdLimitDispatchOperationQueryForUi(Long tenantId, String prodLineId,
                    PageRequest pageRequest) {
        if (StringUtils.isEmpty(prodLineId)) {
            return Collections.emptyList();
        }

        return PageHelper.doPage(pageRequest, () -> mtModProdLineDispatchOperMapper
                        .prodLineIdLimitDispatchOperationQueryForUi(tenantId, prodLineId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtModProdLineDispatchOperDTO saveModProdLineDispatchOperForUi(Long tenantId,
                    MtModProdLineDispatchOperDTO dto) {
        MtModProdLineDispatchOper mtModProdLineDispatchOper = new MtModProdLineDispatchOper();
        mtModProdLineDispatchOper.setTenantId(tenantId);
        mtModProdLineDispatchOper.setProdLineId(dto.getProdLineId());
        mtModProdLineDispatchOper.setOperationId(dto.getOperationId());
        if (StringUtils.isEmpty(dto.getDispatchOperationId())) {
            mtModProdLineDispatchOperRepository.insertSelective(mtModProdLineDispatchOper);
        } else {
            mtModProdLineDispatchOper.setDispatchOperationId(dto.getDispatchOperationId());
            mtModProdLineDispatchOperRepository.updateByPrimaryKeySelective(mtModProdLineDispatchOper);
        }
        dto.setDispatchOperationId(mtModProdLineDispatchOper.getDispatchOperationId());

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteModProdLineDispatchOperForUi(Long tenantId, List<String> modProdLineDispatchOperIdList) {
        if (CollectionUtils.isEmpty(modProdLineDispatchOperIdList)) {
            return 0;
        }

        int delCount = 0;
        MtModProdLineDispatchOper mtModProdLineDispatchOper;
        for (String id : modProdLineDispatchOperIdList) {
            mtModProdLineDispatchOper = new MtModProdLineDispatchOper();
            mtModProdLineDispatchOper.setTenantId(tenantId);
            mtModProdLineDispatchOper.setDispatchOperationId(id);
            delCount += mtModProdLineDispatchOperRepository.delete(mtModProdLineDispatchOper);
        }

        if (delCount != modProdLineDispatchOperIdList.size()) {
            throw new MtException("数据删除失败.");
        }

        return delCount;
    }
}
