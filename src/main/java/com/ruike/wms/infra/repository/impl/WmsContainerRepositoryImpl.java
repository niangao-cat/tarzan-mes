package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.repository.WmsContainerRepository;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.vo.WmsContainerVO;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsContainerMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 容器 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 16:30
 */
@Component
public class WmsContainerRepositoryImpl implements WmsContainerRepository {
    private final WmsContainerMapper wmsContainerMapper;
    private final MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    private final WmsMaterialLotRepository wmsMaterialLotRepository;

    public WmsContainerRepositoryImpl(WmsContainerMapper wmsContainerMapper, MtContainerLoadDetailRepository mtContainerLoadDetailRepository, WmsMaterialLotRepository wmsMaterialLotRepository) {
        this.wmsContainerMapper = wmsContainerMapper;
        this.mtContainerLoadDetailRepository = mtContainerLoadDetailRepository;
        this.wmsMaterialLotRepository = wmsMaterialLotRepository;
    }

    @Override
    public WmsContainerVO getInfoByCode(Long tenantId, String containerCode) {
        return wmsContainerMapper.selectByCode(tenantId, containerCode);
    }

    @Override
    public List<WmsMaterialLotAttrVO> getMaterialLotInContainer(Long tenantId, String containerId) {
        MtContLoadDtlVO10 lotSearch = new MtContLoadDtlVO10();
        lotSearch.setContainerId(containerId);
        lotSearch.setAllLevelFlag(WmsConstant.CONSTANT_Y);
        List<MtContLoadDtlVO4> containerMaterialLotList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, lotSearch);
        List<String> materialLotIdList = containerMaterialLotList.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            return new ArrayList<>();
        }
        return wmsMaterialLotRepository.selectListWithAttrByIds(tenantId, materialLotIdList);
    }
}
