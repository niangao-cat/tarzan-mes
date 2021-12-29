package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsStocktakeActualQueryDTO;
import com.ruike.wms.domain.vo.WmsStocktakeActualVO;
import com.ruike.wms.domain.vo.WmsStocktakeMaterialDetailVO;
import com.ruike.wms.domain.vo.WmsStocktakeMaterialLotVO;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.entity.MtStocktakeActualHis;

import java.util.List;

/**
 * description
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 16:12
 */
public interface WmsStocktakeActualMapper {

    /**
     * 根据条件查询列表
     *
     * @param dto 查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 05:07:48
     */
    List<WmsStocktakeActualVO> selectListByCondition(WmsStocktakeActualQueryDTO dto);


    /**
     * 根据单据ID查询执行列表
     *
     * @param tenantId          租户
     * @param stocktakeId       单据ID
     * @param stocktakeTypeCode 盘点类型
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeDocImplVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 07:07:51
     */
    List<WmsStocktakeMaterialLotVO> selectMaterialLotByType(@Param("tenantId") Long tenantId,
                                                            @Param("stocktakeId") String stocktakeId,
                                                            @Param("stocktakeTypeCode") String stocktakeTypeCode);

    /**
     * 根据ID列表批量查询列表
     *
     * @param tenantId    租户
     * @param stocktakeId 单据ID
     * @param idList      查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 05:07:48
     */
    List<WmsStocktakeActualVO> selectListByIds(@Param("tenantId") Long tenantId,
                                               @Param("stocktakeId") String stocktakeId,
                                               @Param("idList") List<String> idList);

    /**
     * 根据ID查询盘点明细
     *
     * @param tenantId          租户
     * @param stocktakeId       盘点单号
     * @param stocktakeTypeCode 盘点类型
     * @param materialCode      物料编码
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeMaterialDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/25 02:21:14
     */
    List<WmsStocktakeMaterialDetailVO> selectStocktakeDetailById(@Param("tenantId") Long tenantId,
                                                                 @Param("stocktakeId") String stocktakeId,
                                                                 @Param("stocktakeTypeCode") String stocktakeTypeCode,
                                                                 @Param("materialCode") String materialCode);

    /**
     * 批量新增
     *
     * @param data 列表数据
     * @return java.lang.Integer
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/30 03:03:03
     */
    Integer insertBatch(@Param("data") List<MtStocktakeActual> data);

    /**
     * 批量新增历史表
     *
     * @param data 列表数据
     * @return java.lang.Integer
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/30 03:03:03
     */
    Integer insertHisBatch(@Param("data") List<MtStocktakeActualHis> data);
}
