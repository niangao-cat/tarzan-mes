package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO;
import com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO2;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description 已检待上架看板
 * @Author tong.li
 * @Date 2020/4/25 18:49
 * @Version 1.0
 */
public interface WmsCheckedWaitGroudingMapper {

    /**
    * @param tenantId 1 租户ID
    * @return : java.util.List<com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO>
    * @Description: 查询看板任务区域数据
    * @author: tong.li
    * @date 2020/4/25 19:52
    * @version 1.0
    */
    List<WmsCheckedWaitGroudingDTO> queryPutInStorage(@Param("tenantId")Long tenantId);

    /**
    * @param tenantId 1
    * @param dailyTimeFrom 2
    * @param dailyTimeTo 3 
    * @return : java.math.BigDecimal
    * @Description: 30天物料上架量
    * @author: tong.li
    * @date 2020/4/26 16:20
    * @version 1.0
    */
    BigDecimal materialStoragedNumQuery(@Param("tenantId")Long tenantId, @Param("dailyTimeFrom")Date dailyTimeFrom, @Param("dailyTimeTo")Date dailyTimeTo);

    /**
    * @param tenantId 1
    * @param monthTimeFrom 2
    * @param monthTimeTo 3
    * @return : com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO2
    * @Description: 趋势图数据查询
    * @author: tong.li
    * @date 2020/4/26 16:36
    * @version 1.0
    */
    WmsCheckedWaitGroudingDTO2 trendDataQuery(@Param("tenantId")Long tenantId, @Param("monthTimeFrom")Date monthTimeFrom, @Param("monthTimeTo")Date monthTimeTo);
}
