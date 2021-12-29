package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeWipStocktakeActual;
import com.ruike.hme.domain.vo.HmeWipStocktakeActualVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 在制盘点实际Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
public interface HmeWipStocktakeActualMapper extends BaseMapper<HmeWipStocktakeActual> {

    /**
     * 根据返修SN查询hme_eo_rel下的eoId
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 返修SN集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/27 05:33:05
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeActualVO>
     */
    List<HmeWipStocktakeActualVO> queryEoByRepairMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据eo、盘点单ID查询盘点实绩
     *
     * @param tenantId 租户ID
     * @param eoIdList eo集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/27 06:31:06
     * @return java.util.List<com.ruike.hme.domain.entity.HmeWipStocktakeActual>
     */
    List<HmeWipStocktakeActual> queryWipStocktakeActualByEo(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList,
                                                            @Param("stocktakeId") String stocktakeId);
}
