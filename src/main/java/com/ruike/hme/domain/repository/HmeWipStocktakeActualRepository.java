package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeWipStocktakeActual;
import com.ruike.hme.domain.vo.HmeWipStocktakeActualVO2;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 在制盘点实际资源库
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
public interface HmeWipStocktakeActualRepository extends BaseRepository<HmeWipStocktakeActual> {
    /**
     * 保存
     *
     * @param record 记录
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:15:37
     */
    int save(HmeWipStocktakeActual record);

    /**
     * 根据返修SN查询对应的盘点实绩信息
     *
     * @param tenantId 租户ID
     * @param repairMaterialLotIdList 返修SN集合
     * @param stocktakeId 盘点单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/27 05:22:27
     * @return com.ruike.hme.domain.vo.HmeWipStocktakeActualVO2
     */
    HmeWipStocktakeActualVO2 queryWipStocktakeActualByRepairMaterialLotId(Long tenantId, List<String> repairMaterialLotIdList, String stocktakeId);
}
