package com.ruike.wms.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.ruike.wms.api.dto.WmsLocatorDTO;
import com.ruike.wms.api.dto.WmsWarehouseDTO;
import com.ruike.wms.domain.repository.WmsWarehouseLocatorRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;
import tarzan.modeling.domain.vo.MtModLocatorVO9;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WmsWarehouseLocatorRepositoryImpl
 *
 * @author liyuan.lv@hand-china.com 2020/04/30 19:02
 */
@Slf4j
@Component
public class WmsWarehouseLocatorRepositoryImpl extends BaseRepositoryImpl<MtModLocator> implements WmsWarehouseLocatorRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Override
    public List<WmsWarehouseDTO> getWarehouse(Long tenantId, WmsWarehouseDTO dto) {
        if(StringUtils.isEmpty(dto.getSiteId())){
            throw new MtException("WMS_WAREHOUSE_LOCATOR_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WAREHOUSE_LOCATOR_001", "WMS"));
        }
        List<MtModLocatorOrgRelVO> warehouseList = mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId, "SITE", dto.getSiteId(), "TOP");
        if(CollectionUtils.isEmpty(warehouseList)){
            throw new MtException("WMS_WAREHOUSE_LOCATOR_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WAREHOUSE_LOCATOR_002", "WMS"));
        }
        List<String> warehouseIds = warehouseList.stream().map(MtModLocatorOrgRelVO::getLocatorId).collect(Collectors.toList());
        //获取库位详细信息
        List<WmsWarehouseDTO> resultList = new ArrayList<>();
        List<MtModLocator> mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, warehouseIds);

        for (MtModLocator mtModLocator : mtModLocators) {
            WmsWarehouseDTO warehouseDTO  = new WmsWarehouseDTO();
            warehouseDTO.setSiteId(dto.getSiteId());
            warehouseDTO.setWarehouseId(mtModLocator.getLocatorId());
            warehouseDTO.setWarehouseCode(mtModLocator.getLocatorCode());
            warehouseDTO.setWarehouseDesc(mtModLocator.getLocatorName());
            resultList.add(warehouseDTO);
        }
        return resultList;
    }

    @Override
    public List<WmsLocatorDTO> getLocator(Long tenantId, WmsLocatorDTO dto) {

        if (StringUtils.isEmpty(dto.getWarehouseId())) {
            throw new MtException("WMS_WAREHOUSE_LOCATOR_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WAREHOUSE_LOCATOR_003", "WMS"));
        }
        MtModLocatorVO9 mtModLocatorVo9 = new MtModLocatorVO9();
        mtModLocatorVo9.setLocatorId(dto.getWarehouseId());
        mtModLocatorVo9.setQueryType("ALL");
        List<String> strings = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVo9);
        if (CollectionUtils.isEmpty(strings)) {
            throw new MtException("WMS_WAREHOUSE_LOCATOR_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WAREHOUSE_LOCATOR_004", "WMS"));
        }
        //获取库位详细信息
        List<WmsLocatorDTO> resultList = new ArrayList<>();
        List<MtModLocator> mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, strings);
        for (MtModLocator mtModLocator : mtModLocators) {
            WmsLocatorDTO wmsLocatorDTO = new WmsLocatorDTO();
            wmsLocatorDTO.setWarehouseId(dto.getWarehouseId());
            wmsLocatorDTO.setLocatorId(mtModLocator.getLocatorId());
            wmsLocatorDTO.setLocatorCode(mtModLocator.getLocatorCode());
            wmsLocatorDTO.setLocatorName(mtModLocator.getLocatorName());
            resultList.add(wmsLocatorDTO);
        }
        return resultList;
    }
}
