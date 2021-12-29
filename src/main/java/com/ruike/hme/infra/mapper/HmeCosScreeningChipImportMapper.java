package com.ruike.hme.infra.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * HmeCosScreeningChipImportMapper
 *
 * @author: chaonan.hu@hand-china.com 2020-10-12 15:51:34
 **/
public interface HmeCosScreeningChipImportMapper {

    /**
     * 根据物料+站点获取产品类型
     *
     * @param tenantId   租户ID
     * @param materialId 物料ID
     * @param siteId     站点ID
     * @return java.lang.String
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/12 03:53:49
     */
    String getAttribute13(@Param("tenantId") Long tenantId, @Param("materialId") String materialId, @Param("siteId") String siteId);
}
