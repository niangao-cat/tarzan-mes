package tarzan.inventory.infra.repository.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO2;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO1;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO2;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO3;
import tarzan.inventory.infra.mapper.MtMaterialLotHisMapper;

/**
 * 物料批历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
@Component
public class MtMaterialLotHisRepositoryImpl extends BaseRepositoryImpl<MtMaterialLotHis>
                implements MtMaterialLotHisRepository {
    @Autowired
    private MtMaterialLotHisMapper mtMaterialLotHisMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public List<MtMaterialLotHis> requestLimitMaterialLotHisQuery(Long tenantId, MtMaterialLotHisVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventRequestId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventRequestId", "【API:requestLimitMaterialLotHisQuery】"));
        }

        // 2. 获取满足条件的事件eventId列表
        MtEventVO2 eventVO2 = new MtEventVO2();
        eventVO2.setEventRequestId(dto.getEventRequestId());
        eventVO2.setEventTypeCode(dto.getEventTypeCode());
        List<String> eventIds = mtEventRepository.requestLimitEventQuery(tenantId, eventVO2);
        if (CollectionUtils.isEmpty(eventIds)) {
            return Collections.emptyList();
        } else {
            return mtMaterialLotHisMapper.selectByEventIds(tenantId, eventIds);
        }
    }

    @Override
    public List<MtMaterialLotHis> eventLimitMaterialLotHisQuery(Long tenantId, String eventId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API:eventLimitMaterialLotHisQuery】"));
        }

        return mtMaterialLotHisMapper.selectByEventIds(tenantId, Arrays.asList(eventId));
    }

    @Override
    public List<MtMaterialLotHis> eventLimitMaterialLotHisBatchQuery(Long tenantId, List<String> eventIds) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventId", "【API:eventLimitMaterialLotHisBatchQuery】"));
        }

        return mtMaterialLotHisMapper.selectByEventIds(tenantId, eventIds);
    }

    @Override
    public List<MtMaterialLotHis> materialLotHisQuery(Long tenantId, MtMaterialLotHisVO2 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "【API:materialLotHisQuery】"));
        }

        return mtMaterialLotHisMapper.selectMaterialLotEventLimit(tenantId, dto);
    }

    @Override
    public MtMaterialLotHisVO3 materialLotLatestHisGet(Long tenantId, String materialLotId) {
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "【API:materialLotLatestHisGet】"));
        }
        return mtMaterialLotHisMapper.materialLotLatestHisGet(tenantId, materialLotId);
    }

    @Override
    public List<MtMaterialLotHis> materialLotHisBatchGet(Long tenantId, List<String> hisIds) {
        if (CollectionUtils.isEmpty(hisIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotHisId", "【API:materialLotHisBatchGet】"));
        }
        return mtMaterialLotHisMapper.materialLotHisBatchGet(tenantId, hisIds);
    }
}
