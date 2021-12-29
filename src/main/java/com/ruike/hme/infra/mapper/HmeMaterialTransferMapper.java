package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeMaterialTransferDTO2;
import com.ruike.hme.api.dto.HmeMaterialTransferDTO7;
import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料转移Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 10:03
 */
public interface HmeMaterialTransferMapper {

    /**
     * 物料批物料信息
     *
     * @param tenantId
     * @param materialLotCode
     * @return
     */
    HmeMaterialTransferDTO7 materialLotGet(@Param("tenantId") Long tenantId,
                                           @Param("materialLotCode") String materialLotCode);

    /**
     * 物料站点扩展属性修改
     *
     * @param tenantId
     * @param siteId
     * @param materialId
     * @author jiangling.zheng@hand-china.com 2020/10/10 14:33
     * @return java.lang.String
     */
    String materialSiteAttrGet(@Param("tenantId") Long tenantId,
                               @Param("siteId") String siteId,
                               @Param("materialId") String materialId);

    /**
     * 获取用户仓库的单据授权信息
     *
     * @param tenantId
     * @param userId
     * @param warehouseId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/4/6 9:53
     */
    List<String> queryDocPrivilegeByWarehouse(@Param("tenantId") Long tenantId,
                                              @Param("userId") Long userId,
                                              @Param("warehouseId") String warehouseId);

    /**
     * 查询条码绑定工位数据
     * @param tenantId
     * @param materialLotId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobLotMaterial>
     * @author sanfeng.zhang@hand-china.com 2021/6/28
     */
    List<HmeEoJobLotMaterial> queryEoJobLotMaterialList(@Param("tenantId") Long tenantId,
                                                  @Param("materialLotId") String materialLotId);
}
