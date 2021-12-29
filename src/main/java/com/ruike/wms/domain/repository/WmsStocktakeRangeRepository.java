package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsStocktakeRangeQueryDTO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 库存盘点范围 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:19
 */
public interface WmsStocktakeRangeRepository {

    /**
     * 根据盘点单据ID查询范围列表
     *
     * @param tenantId        租户
     * @param stocktakeId     盘点单据ID
     * @param rangeObjectType 范围对象类型
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:06:43
     */
    List<WmsStocktakeRangeVO> selectListByDocId(Long tenantId, String stocktakeId, String rangeObjectType);

    /**
     * 盘点范围分页查询
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:06:43
     */
    Page<WmsStocktakeRangeVO> pageStocktakeRange(Long tenantId, WmsStocktakeRangeQueryDTO dto, PageRequest pageRequest);

    /**
     * 批量插入
     *
     * @param data        数据
     * @param tenantId    租户
     * @param stocktakeId 盘点单ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 08:24:47
     */
    void batchInsertList(List<WmsStocktakeRangeVO> data, Long tenantId, String stocktakeId);

    /**
     * 批量更新
     *
     * @param data 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 08:24:47
     */
    void batchUpdateList(List<WmsStocktakeRangeVO> data);

    /**
     * 批量删除
     *
     * @param tenantId 租户
     * @param idList   删除ID列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 08:36:30
     */
    void batchDeleteList(Long tenantId, List<String> idList);

    /**
     * 根据盘点单据ID列表查询货位
     *
     * @param tenantId        租户
     * @param stocktakeIdList 盘点单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:06:43
     */
    List<WmsStocktakeRangeVO> selectLocatorsByDocIds(Long tenantId, String stocktakeIdList);

    /**
     * 查询有覆盖范围的其他单据的范围
     *
     * @param tenantId          租户
     * @param stocktakeId       盘点单据
     * @param rangeObjectType   范围对象类型
     * @param rangeObjectIdList 范围对象ID列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 10:02:01
     */
    List<String> selectCoveredRangeList(Long tenantId,
                                        String rangeObjectType,
                                        String stocktakeId,
                                        List<String> rangeObjectIdList);

    /**
     * 查询活跃的有效范围数据
     *
     * @param tenantId        租户
     * @param rangeObjectType 范围对象类型(货位，物料)
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 10:02:01
     */
    List<WmsStocktakeRangeVO> selectActiveRangeList(Long tenantId,
                                                    String rangeObjectType);

    /**
     * 查询盘点范围内的物料批
     *
     * @param tenantId    租户
     * @param stocktakeId 盘点单
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 10:43:29
     */
    List<WmsMaterialLotVO> selectMaterialLotInRange(Long tenantId,
                                                    String stocktakeId);

    /**
     * 批量按主键删除
     *
     * @param tenantId        租户
     * @param rangeObjectType 范围类型
     * @param stocktakeId     单据ID
     * @param list            待删除数据
     * @return List<WmsStocktakeRangeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 08:36:30
     */
    List<WmsStocktakeRangeVO> batchDeleteByPrimaryKey(Long tenantId, String stocktakeId, String rangeObjectType, List<WmsStocktakeRangeVO> list);
}
