package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeActual;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备盘点实际Mapper
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
public interface HmeEquipmentStocktakeActualMapper extends BaseMapper<HmeEquipmentStocktakeActual> {

    /**
     * 查询列表
     *
     * @param stocktakeId 盘点单ID
     * @return java.util.List<com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 03:21:39
     */
    List<HmeEquipmentStocktakeActualRepresentation> selectList(@Param("stocktakeId") String stocktakeId);

    /**
     * 盘点单设备
     * @param tenantId
     * @param stocktakeId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/8/2
     */
    List<String> queryStocktakeEquipment(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId);
}
