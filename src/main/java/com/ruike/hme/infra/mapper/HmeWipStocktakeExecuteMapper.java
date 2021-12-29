package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeWipStocktakeExecuteDocQueryDTO;
import com.ruike.hme.api.dto.HmeWipStocktakeExecuteDocRepresentationDTO;
import com.ruike.hme.api.dto.WipStocktakeExecSnRepresentationDTO;
import com.ruike.hme.api.dto.WipStocktakeSnDetailQueryDTO;
import com.ruike.hme.domain.vo.WipStocktakeMaterialLotWorkVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 10:18
 */
public interface HmeWipStocktakeExecuteMapper {

    /**
     * 根据条件查询
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.hme.api.dto.HmeWipStocktakeExecuteDocRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 10:27:58
     */
    List<HmeWipStocktakeExecuteDocRepresentationDTO> selectByCondition(@Param("tenantId") Long tenantId,
                                                                       @Param("dto") HmeWipStocktakeExecuteDocQueryDTO dto);

    /**
     * 查询非COS物料批
     *
     * @param tenantId       租户
     * @param siteId         站点
     * @param materialLotIds 物料批ID
     * @return java.util.List<com.ruike.hme.domain.vo.WipStocktakeMaterialLotWorkVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 04:57:43
     */
    List<WipStocktakeMaterialLotWorkVO> selectNonCosMaterialLots(@Param("tenantId") Long tenantId,
                                                                 @Param("siteId") String siteId,
                                                                 @Param("materialLotIds") Iterable<String> materialLotIds);

    /**
     * 非COS物料批针对返修情况二次查询
     *
     * @param tenantId 租户ID
     * @param materialLotIds 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/28 11:49:32
     * @return java.util.List<com.ruike.hme.domain.vo.WipStocktakeMaterialLotWorkVO>
     */
    List<WipStocktakeMaterialLotWorkVO> selectNonCosMaterialLots2(@Param("tenantId") Long tenantId,
                                                                  @Param("materialLotIds") Iterable<String> materialLotIds,
                                                                  @Param("stocktakeId") String stocktakeId);

    /**
     * 查询COS物料批
     *
     * @param tenantId       租户
     * @param siteId         站点
     * @param materialLotIds 物料批ID
     * @return java.util.List<com.ruike.hme.domain.vo.WipStocktakeMaterialLotWorkVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 04:57:43
     */
    List<WipStocktakeMaterialLotWorkVO> selectCosMaterialLots(@Param("tenantId") Long tenantId,
                                                              @Param("siteId") String siteId,
                                                              @Param("materialLotIds") Iterable<String> materialLotIds);

    /**
     * 在制盘点明细查询
     *
     * @param tenantId 租户ID
     * @param query    查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO2>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 03:02:35
     */
    List<WipStocktakeExecSnRepresentationDTO> selectSnDetails(@Param("tenantId") Long tenantId,
                                                              @Param("query") WipStocktakeSnDetailQueryDTO query);
}
