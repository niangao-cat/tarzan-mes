package com.ruike.wms.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruike.wms.api.dto.WmsMaterialLotHisQryDTO;
import com.ruike.wms.api.dto.WmsMaterialLotHisResultDTO;
import com.ruike.wms.api.dto.WmsMaterialLotQryDTO;
import com.ruike.wms.api.dto.WmsMaterialLotQryResultDTO;
import com.ruike.wms.domain.vo.WmsMaterialLotPntVO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO2;
import com.ruike.wms.domain.vo.WmsMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO;

/**
 * WmsMaterialMapper
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 15:58
 */
public interface WmsMaterialMapper {

    /**
     * 根据条件获取物料
     *
     * @param tenantId 租户ID
     * @param dto      物料参数
     * @return 物料
     */
    List<WmsMaterialVO> selectBySiteCondition(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") WmsMaterialVO dto);

    /**
     * 获取用户可访问站点下的物料
     *
     * @param tenantId 租户ID
     * @param dto      物料参数
     * @return 物料
     */
    List<WmsMaterialVO> selectByUserPermission(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "dto") WmsMaterialVO dto);

    /**
     * 根据条件获取物料
     *
     * @param tenantId      租户ID
     * @param siteId        站点
     * @param itemGroupCode 物料类别编码
     * @return 物料
     */
    List<WmsMaterialVO> selectByItemGroup(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "siteId") String siteId,
                                          @Param(value = "itemGroupCode") String itemGroupCode);

    /**
     * 根据条件获取物料
     *
     * @param tenantId      租户ID
     * @param siteId        站点
     * @param itemGroupCode 物料类别编码
     * @param warehouseId   仓库ID
     * @return 物料
     */
    List<WmsMaterialVO> selectOnhandItemByGroup(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "siteId") String siteId,
                                                @Param(value = "itemGroupCode") String itemGroupCode,
                                                @Param(value = "warehouseId") String warehouseId);
}
