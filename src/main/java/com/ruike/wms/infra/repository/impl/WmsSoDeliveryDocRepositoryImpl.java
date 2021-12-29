package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsProductPrepareDocQueryDTO;
import com.ruike.wms.api.dto.WmsSoDeliveryQueryDTO;
import com.ruike.wms.domain.repository.WmsSoDeliveryDocRepository;
import com.ruike.wms.domain.vo.WmsProdPrepareExecVO;
import com.ruike.wms.domain.vo.WmsProductPrepareDocVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryDocVO;
import com.ruike.wms.infra.mapper.WmsSoDeliveryDocMapper;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 出货单单据 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 15:16
 */
@Component
public class WmsSoDeliveryDocRepositoryImpl implements WmsSoDeliveryDocRepository {

    private final WmsSoDeliveryDocMapper wmsSoDeliveryDocMapper;

    public WmsSoDeliveryDocRepositoryImpl(WmsSoDeliveryDocMapper wmsSoDeliveryDocMapper) {
        this.wmsSoDeliveryDocMapper = wmsSoDeliveryDocMapper;
    }

    @Override
    public List<WmsSoDeliveryDocVO> selectListByQueryCondition(Long tenantId, WmsSoDeliveryQueryDTO dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        return wmsSoDeliveryDocMapper.selectListByQueryCondition(tenantId, userId, dto);
    }

    @Override
    public List<WmsProductPrepareDocVO> selectPrepareListByCondition(Long tenantId, WmsProductPrepareDocQueryDTO dto) {
        return wmsSoDeliveryDocMapper.selectPrepareListByCondition(tenantId, dto);
    }

    @Override
    @ProcessLovValue
    public WmsProductPrepareDocVO selectPrepareDocByNum(Long tenantId, String instructionDocNum) {
        return wmsSoDeliveryDocMapper.selectPrepareDocByNum(tenantId, instructionDocNum);
    }

    @Override
    public Set<String> selectMaterialLotIdByDocId(Long tenantId, String instructionDocId) {
        List<String> list = wmsSoDeliveryDocMapper.selectMaterialLotIdByDocId(tenantId, instructionDocId, null);
        if (CollectionUtils.isEmpty(list)) {
            return new HashSet<>();
        }
        return new HashSet<>(list);
    }

    @Override
    public Set<String> selectMaterialLotIdByDocId(Long tenantId, String instructionDocId, String materialLotStatus) {
        List<String> list = wmsSoDeliveryDocMapper.selectMaterialLotIdByDocId(tenantId, instructionDocId, materialLotStatus);
        if (CollectionUtils.isEmpty(list)) {
            return new HashSet<>();
        }
        return new HashSet<>(list);
    }

    @Override
    public Map<String, List<String>> selectPrepareTargetLocators(Long tenantId, List<String> warehouseIds) {
        List<WmsProdPrepareExecVO> list = wmsSoDeliveryDocMapper.selectPrepareTargetLocators(tenantId, warehouseIds);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>(0);
        }
        return list.stream().collect(Collectors.groupingBy(WmsProdPrepareExecVO::getTargetWarehouseId, Collectors.mapping(WmsProdPrepareExecVO::getTargetLocatorId, Collectors.toList())));
    }

    @Override
    public Map<String, List<String>> selectPrepareTargetSites(Long tenantId, List<String> warehouseIds) {
        List<WmsProdPrepareExecVO> list = wmsSoDeliveryDocMapper.selectPrepareTargetSites(tenantId, warehouseIds);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>(0);
        }
        return list.stream().collect(Collectors.groupingBy(WmsProdPrepareExecVO::getTargetWarehouseId, Collectors.mapping(WmsProdPrepareExecVO::getTargetSiteId, Collectors.toList())));
    }
}
