package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsStocktakeDocQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeDocSelectQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeMaterialDetailQueryDTO;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 库存盘点单据 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 09:38
 */
public interface WmsStocktakeDocRepository {

    /**
     * 分页查询
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsStocktakeDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:23:00
     */
    Page<WmsStocktakeDocVO> pageAndSort(Long tenantId, WmsStocktakeDocQueryDTO dto, PageRequest pageRequest);

    /**
     * 根据单据Id查询单条
     *
     * @param tenantId    租户
     * @param stocktakeId 单据ID
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsStocktakeDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 11:23:00
     */
    WmsStocktakeDocVO selectById(Long tenantId, String stocktakeId);

    /**
     * 查询状态不为NEW的盘点单号
     *
     * @param tenantId 租户
     * @param idList   盘点单Id列表
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 10:03:36
     */
    List<String> notNewStocktakeNums(Long tenantId, List<String> idList);

    /**
     * 查询状态不为NEW的盘点单号
     *
     * @param tenantId 租户
     * @param idList   盘点单Id列表
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 10:03:36
     */
    List<String> notNewStocktakeIds(Long tenantId, List<String> idList);

    /**
     * 盘点单选择LOV
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeDocSelectVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 04:39:28
     */
    List<WmsStocktakeDocSelectVO> stocktakeSelectLov(Long tenantId, WmsStocktakeDocSelectQueryDTO dto);

    /**
     * 通过单据号查询执行单据
     *
     * @param tenantId     租户
     * @param stocktakeNum 单据号
     * @return com.ruike.wms.domain.vo.WmsStocktakeDocImplVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 05:29:58
     */
    WmsStocktakeDocSelectVO selectImplDocByNum(Long tenantId, String stocktakeNum);

    /**
     * 通过id查询成本中心
     *
     * @param tenantId    租户
     * @param stocktakeId id列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeCostCenterVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 11:10:05
     */
    List<WmsStocktakeCostCenterVO> selectCostCenterByIds(Long tenantId, String stocktakeId);

    /**
     * 查询物料明细
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeMaterialDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 03:29:02
     */
    List<WmsStocktakeMaterialDetailVO> selectMaterialDetailByIds(Long tenantId, WmsStocktakeMaterialDetailQueryDTO dto);

    /**
     * 查询盘点货位
     *
     * @param tenantId    租户
     * @param warehouseId 仓库ID
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 08:01:13
     */
    List<MtModLocator> selectStocktakeLocator(Long tenantId,
                                              String warehouseId);

    /**
     * 查询盘点物料
     *
     * @param tenantId    租户
     * @param warehouseId 仓库ID
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 08:08:50
     */
    List<MtMaterial> selectStocktakeMaterial(Long tenantId,
                                             String warehouseId);

    /**
     * 查询是否有漏盘
     *
     * @param tenantId        租户
     * @param stocktakeIdList 盘点单
     * @return java.lang.Integer
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 01:50:30
     */
    List<String> leakDocGet(Long tenantId, List<String> stocktakeIdList);

}
