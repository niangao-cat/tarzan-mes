package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsMaterialTurnoverRateDTO;
import com.ruike.wms.domain.vo.WmsMaterialTurnoverRateVO;
import com.ruike.wms.domain.vo.WmsMaterialTurnoverRateVO2;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtInvJournal;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/27 17:19
 */
public interface WmsMaterialTurnoverRateMapper {

    List<WmsMaterialTurnoverRateVO> queryList(@Param("tenantId") Long tenantId,
                                              @Param("dto")WmsMaterialTurnoverRateDTO dto);

    List<WmsMaterialTurnoverRateVO2> selectQtyByStart(@Param("tenantId")Long tenantId,
                                                      @Param("materialId")String materialId,
                                                      @Param("locatorId")String locatorId,
                                                      @Param("startDate")String startDate);

    List<WmsMaterialTurnoverRateVO2> selectQtyByEnd(@Param("tenantId")Long tenantId,
                                                    @Param("materialId")String materialId,
                                                    @Param("locatorId")String locatorId,
                                                    @Param("endDate")String endDate);

    BigDecimal selectStartQty(@Param("tenantId")String tenantId,
                              @Param("siteId")String siteId,
                              @Param("materialId")String materialId,
                              @Param("locatorId")String locatorId,
                              @Param("lotCode")String lotCode,
                              @Param("ownerId")String ownerId,
                              @Param("ownerType")String ownerType,
                              @Param("startDate")String startDate,
                              @Param("flag")String flag);

    BigDecimal selectEndQty(@Param("tenantId")String tenantId,
                            @Param("siteId")String siteId,
                            @Param("materialId")String materialId,
                            @Param("locatorId")String locatorId,
                            @Param("lotCode")String lotCode,
                            @Param("ownerId")String ownerId,
                            @Param("ownerType")String ownerType,
                            @Param("endDate")String endDate,
                            @Param("flag")String flag);

    MtInvJournal selectStartInvJournal(@Param("tenantId")String tenantId,
                                       @Param("siteId")String siteId,
                                       @Param("materialId")String materialId,
                                       @Param("locatorId")String locatorId,
                                       @Param("lotCode")String lotCode,
                                       @Param("ownerId")String ownerId,
                                       @Param("ownerType")String ownerType,
                                       @Param("startDate")String startDate);

    MtInvJournal selectEndInvJournal(@Param("tenantId")String tenantId,
                                     @Param("siteId")String siteId,
                                     @Param("materialId")String materialId,
                                     @Param("locatorId")String locatorId,
                                     @Param("lotCode")String lotCode,
                                     @Param("ownerId")String ownerId,
                                     @Param("ownerType")String ownerType,
                                     @Param("endDate")String endDate);
}
