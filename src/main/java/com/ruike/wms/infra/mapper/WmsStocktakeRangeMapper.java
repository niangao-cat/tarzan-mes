package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsStocktakeRangeQueryDTO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 库存盘点范围 mapper
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:02
 */
public interface WmsStocktakeRangeMapper {

    /**
     * 根据盘点单据ID查询范围列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:06:43
     */
    List<WmsStocktakeRangeVO> selectListByDocId(@Param("tenantId") Long tenantId,
                                                @Param("dto") WmsStocktakeRangeQueryDTO dto);

    /**
     * 排除单据外所有的有效范围数据
     *
     * @param tenantId          租户
     * @param rangeObjectType   范围对象类型(货位，物料)
     * @param stocktakeId       盘点单据
     * @param rangeObjectIdList 范围ID列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 10:02:01
     */
    List<String> selectCoveredRangeList(@Param("tenantId") Long tenantId,
                                        @Param("rangeObjectType") String rangeObjectType,
                                        @Param("stocktakeId") String stocktakeId,
                                        @Param("idList") List<String> rangeObjectIdList);

    /**
     * 查询活跃的有效范围数据
     *
     * @param tenantId        租户
     * @param rangeObjectType 范围对象类型(货位，物料)
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 10:02:01
     */
    List<WmsStocktakeRangeVO> selectActiveRangeList(@Param("tenantId") Long tenantId,
                                                    @Param("rangeObjectType") String rangeObjectType);

    /**
     * 根据盘点单据ID列表查询货位
     *
     * @param tenantId        租户
     * @param stocktakeIdList 盘点单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:06:43
     */
    List<WmsStocktakeRangeVO> selectLocatorsByDocIds(@Param("tenantId") Long tenantId, @Param("stocktakeIdList") String stocktakeIdList);

    /**
     * 批量插入
     *
     * @param data 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 08:24:47
     */
    void batchInsertList(@Param(value = "data") List<WmsStocktakeRangeVO> data);

    /**
     * 批量更新
     *
     * @param data 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 08:24:47
     */
    void batchUpdateList(@Param(value = "data") List<WmsStocktakeRangeVO> data);

    /**
     * 批量删除
     *
     * @param tenantId 租户
     * @param idList   删除ID列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 08:36:30
     */
    void batchDeleteList(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);

    /**
     * 查询盘点范围内的物料批
     *
     * @param tenantId    租户
     * @param stocktakeId 盘点单
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 10:43:29
     */
    List<WmsMaterialLotVO> selectMaterialLotInRange(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId);

}
