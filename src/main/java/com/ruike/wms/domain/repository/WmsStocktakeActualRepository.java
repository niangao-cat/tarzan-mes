package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsStocktakeActualQueryDTO;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.entity.MtStocktakeActualHis;

import java.util.List;

/**
 * 库存盘点实绩 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 19:09
 */
public interface WmsStocktakeActualRepository {

    /**
     * 根据条件查询列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 05:07:48
     */
    Page<WmsStocktakeActualVO> pageAndSort(Long tenantId, WmsStocktakeActualQueryDTO dto, PageRequest pageRequest);

    /**
     * 根据条件查询列表
     *
     * @param tenantId  租户
     * @param condition 盘点单
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 05:07:48
     */
    List<WmsStocktakeActualVO> listByDocId(Long tenantId, WmsStocktakeActualQueryDTO condition);

    /**
     * 拓展属性更新
     *
     * @param tenantId    租户
     * @param stocktakeId 盘点单据ID
     * @param eventId     事件ID
     * @param attrs       拓展属性
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 11:27:39
     */
    List<String> attrUpdate(Long tenantId, String stocktakeId, String eventId, WmsStocktakeActualAttrVO attrs);

    /**
     * 根据单据ID查询执行列表
     *
     * @param tenantId          租户
     * @param stocktakeId       单据ID
     * @param stocktakeTypeCode 盘点类型
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeDocImplVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 07:07:51
     */
    List<WmsStocktakeMaterialLotVO> selectMaterialLotByType(Long tenantId,
                                                            String stocktakeId,
                                                            String stocktakeTypeCode);

    /**
     * 根据ID列表批量查询列表
     *
     * @param tenantId    租户
     * @param stocktakeId 单据ID
     * @param idList      查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 05:07:48
     */
    List<WmsStocktakeActualVO> selectListByIds(Long tenantId, String stocktakeId, List<String> idList);

    /**
     * 盘点调整生成事务
     *
     * @param tenantId            租户
     * @param stocktakeActualList 盘点实绩列表
     * @param eventId             事件Id
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 11:44:03
     */
    List<WmsObjectTransactionResponseVO> stocktakeAdjustTransaction(Long tenantId, List<WmsStocktakeActualVO> stocktakeActualList, String eventId);

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
    List<WmsStocktakeMaterialDetailVO> stocktakeDetailGet(Long tenantId,
                                                          String stocktakeId,
                                                          String stocktakeTypeCode,
                                                          String materialCode);

    /**
     * 批量新增
     *
     * @param data 列表数据
     * @return java.lang.Integer
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/30 03:03:03
     */
    Integer insertBatch(List<MtStocktakeActual> data);

    /**
     * 批量新增历史表
     *
     * @param data 列表数据
     * @return java.lang.Integer
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/30 03:03:03
     */
    Integer insertHisBatch(List<MtStocktakeActualHis> data);
}
