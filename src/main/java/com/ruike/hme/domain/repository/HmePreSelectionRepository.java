package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmePreSelectionDTO;
import com.ruike.hme.api.dto.HmePreSelectionReturnDTO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmePreSelection;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * 预挑选基础表资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-18 15:00:33
 */
public interface HmePreSelectionRepository extends BaseRepository<HmePreSelection> {


    List<HmePreSelectionReturnDTO> workOrderQuery(Long tenantId, HmePreSelectionDTO dto);

    /**
     * 批量将条码与容器解绑
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/28 10:05:30
     * @return void
     */
    void materialLotBatchUnBindingContainer(Long tenantId, List<String> materialLotIdList);

}
