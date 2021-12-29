package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.domain.repository.QmsIqcCheckPlatformRepository;
import com.ruike.qms.domain.vo.QmsIqcMaterialLotVO;
import com.ruike.qms.infra.mapper.QmsIqcCheckPlatformMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/22 14:28
 */
@Component
public class QmsIqcCheckPlatformRepositoryImpl implements QmsIqcCheckPlatformRepository {
    private final QmsIqcCheckPlatformMapper iqcCheckPlatformMapper;

    public QmsIqcCheckPlatformRepositoryImpl(QmsIqcCheckPlatformMapper iqcCheckPlatformMapper) {
        this.iqcCheckPlatformMapper = iqcCheckPlatformMapper;
    }

    @Override
    public List<QmsIqcMaterialLotVO> selectMaterialLotListByIqcHeader(Long tenantId, String iqcHeaderId, String supplierLot) {
        return iqcCheckPlatformMapper.selectMaterialLotListByIqcHeader(tenantId, iqcHeaderId, supplierLot);
    }
}
