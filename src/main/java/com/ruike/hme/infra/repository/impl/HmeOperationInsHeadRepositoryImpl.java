package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeOperationInsHeadDTO;
import com.ruike.hme.api.dto.HmeOperationInsHeadDTO2;
import com.ruike.hme.domain.entity.HmeOperationInstruction;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeOperationInsHeadMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeOperationInsHead;
import com.ruike.hme.domain.repository.HmeOperationInsHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 作业指导头表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-11-09 19:09:39
 */
@Component
public class HmeOperationInsHeadRepositoryImpl extends BaseRepositoryImpl<HmeOperationInsHead> implements HmeOperationInsHeadRepository {

    @Autowired
    private HmeOperationInsHeadMapper hmeOperationInsHeadMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public List<HmeOperationInsHeadDTO2> operationInsHeadQuery(Long tenantId, HmeOperationInsHeadDTO dto) {
        return hmeOperationInsHeadMapper.operationInsHeadQuery(tenantId, dto);
    }

    @Override
    public HmeOperationInsHeadDTO2 detailListForUi(Long tenantId, HmeOperationInsHeadDTO dto) {
        if (Objects.isNull(dto)) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", HmeConstants.ConstantValue.HME));
        }
        if (StringUtils.isBlank(dto.getInsHeadId())) {
            throw new MtException("HME_OPERATION_INS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0003", HmeConstants.ConstantValue.HME, "InsHeadId"));
        }
        HmeOperationInsHeadDTO2 detailDto = hmeOperationInsHeadMapper.operationInsHeadQuery(tenantId, dto).get(0);
        return detailDto;
    }

    @Override
    public HmeOperationInsHead operationInsHeadUpdate(Long tenantId, HmeOperationInsHead hmeOperationInsHead) {
        if (StringUtils.isEmpty(hmeOperationInsHead.getInsHeaderId())){
            self().insertSelective(hmeOperationInsHead);
        } else {
            hmeOperationInsHeadMapper.updateByPrimaryKeySelective(hmeOperationInsHead);
        }
        return hmeOperationInsHead;
    }
}
