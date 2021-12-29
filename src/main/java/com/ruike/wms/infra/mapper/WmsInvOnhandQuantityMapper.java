package com.ruike.wms.infra.mapper;

import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10;

import java.math.BigDecimal;
import java.util.List;

/**
 * 现有量查询 mapper
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/14 10:53:58
 */
public interface WmsInvOnhandQuantityMapper {

    /**
     * 现有量查询
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/14 10:54:17
     */
    List<MtInvOnhandQuantityVO10> onhandQuantityQuery(@Param(value = "tenantId") Long tenantId
            , @Param(value = "dto") MtInvOnhandQuantityVO10 dto);

    /**
     * 现有量导出
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/14 10:54:17
     */
    List<MtInvOnhandQuantityVO10> onhandQuantityExport(@Param(value = "tenantId") Long tenantId
            , @Param(value = "dto") MtInvOnhandQuantityVO10 dto);

    /**
     * 现有量计数
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.math.BigDecimal
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/14 10:55:18
     */
    BigDecimal countOnhandQuantity(@Param(value = "tenantId") Long tenantId
            , @Param(value = "dto") MtInvOnhandQuantityVO10 dto);

    /**
     * 库存快照
     *
     * @author penglin.sui@hand-china.com 2020/10/08 20:02
     */
    void batchInsertInvOnhandQuantityShot();

    /**
     * 删除七天前的库存快照
     *
     * @author penglin.sui@hand-china.com 2020/10/08 20:10
     */
    void deleteInvOnhandQuantityShot();
}
