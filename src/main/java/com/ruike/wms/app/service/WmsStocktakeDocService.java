package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import org.springframework.transaction.annotation.Transactional;
import tarzan.stocktake.domain.vo.MtStocktakeDocVO1;

import java.util.List;

/**
 * 库存盘点单据 服务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 12:24
 */
public interface WmsStocktakeDocService {
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
     * 创建库存盘点单
     *
     * @param tenantId 租户
     * @param vo       创建数据
     * @return com.ruike.wms.domain.vo.WmsStocktakeDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 02:07:28
     */
    WmsStocktakeDocVO create(Long tenantId, MtStocktakeDocVO1 vo);

    /**
     * 更新库存盘点单
     *
     * @param tenantId 租户
     * @param vo       更新数据
     * @return com.ruike.wms.domain.vo.WmsStocktakeDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 02:07:28
     */
    WmsStocktakeDocVO update(Long tenantId, WmsStocktakeDocUpdateDTO vo);

    /**
     * 下达库存盘点单
     *
     * @param tenantId        租户
     * @param stocktakeIdList 下达列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 09:21:32
     */
    void release(Long tenantId, List<String> stocktakeIdList);

    /**
     * 完成库存盘点单
     *
     * @param tenantId        租户
     * @param stocktakeIdList 完成列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 09:21:32
     */
    void complete(Long tenantId, List<String> stocktakeIdList);

    /**
     * 关闭库存盘点单
     *
     * @param tenantId        租户
     * @param stocktakeIdList 关闭列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 09:21:32
     */
    void close(Long tenantId, List<String> stocktakeIdList);

    /**
     * 通过单据号查询执行单据
     *
     * @param tenantId     租户
     * @param stocktakeNum 单据号
     * @return com.ruike.wms.domain.vo.WmsStocktakeDocImplVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 05:29:58
     */
    WmsStocktakeDocSelectVO docScan(Long tenantId, String stocktakeNum);

    /**
     * 分页查询物料明细
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeMaterialDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 03:29:02
     */
    Page<WmsStocktakeMaterialDetailVO> pageMaterialVersion(Long tenantId, WmsStocktakeMaterialDetailQueryDTO dto, PageRequest pageRequest);

    /**
     * 盘点明细导出
     *
     * @param tenantId    租户
     * @param exportParam 导出参数
     * @param condition   条件
     * @return java.util.List<com.ruike.wms.api.dto.WmsStocktakeDetailExportDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/19 08:17:02
     */
    List<WmsStocktakeDetailExportDTO> exportDetail(Long tenantId, ExportParam exportParam, WmsStocktakeMaterialDetailQueryDTO condition);


    /**
     * 盘点物料批锁定
     *
     * @param tenantId          租户
     * @param stocktakeId       盘点单ID
     * @param materialLotIdList 物料批ID列表
     * @param eventRequestId    事件请求ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 10:39:01
     */
    void stocktakeMaterialLotLock(Long tenantId, String stocktakeId, List<String> materialLotIdList, String eventRequestId);


    /**
     * 盘点物料批解锁
     *
     * @param tenantId          租户
     * @param stocktakeId       盘点单ID
     * @param materialLotIdList 物料批ID列表
     * @param eventRequestId    事件请求ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 10:39:01
     */
    void stocktakeMaterialLotUnlock(Long tenantId, String stocktakeId, List<String> materialLotIdList, String eventRequestId);
}
