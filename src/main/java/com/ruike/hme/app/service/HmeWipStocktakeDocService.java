package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeWipStocktakeDoc;
import com.ruike.hme.domain.vo.*;
import com.ruike.wms.api.dto.WmsStocktakeRangeQueryDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.util.List;

/**
 * 在制盘点单应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
public interface HmeWipStocktakeDocService {

    /**
     * 在制品盘点单分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 04:23:51
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO>
     */
    Page<HmeWipStocktakeDocVO> wipStocktakeDocPageQuery(Long tenantId, HmeWipStocktakeDocDTO dto, PageRequest pageRequest);

    /**
     * 盘点范围分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 09:47:45
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     */
    Page<HmeWipStocktakeDocVO4> stocktakeRangePageQuery(Long tenantId, WmsStocktakeRangeQueryDTO dto, PageRequest pageRequest);

    /**
     * 在制盘点明细分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 02:59:16
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO2>
     */
    Page<HmeWipStocktakeDocVO2> wipStocktakeDetailPageQuery(Long tenantId, HmeWipStocktakeDocDTO5 dto, PageRequest pageRequest);

    /**
     * 在制盘点汇总分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/4 07:09:19
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO3>
     */
    Page<HmeWipStocktakeDocVO3> wipStocktakeSumPageQuery(Long tenantId, HmeWipStocktakeDocDTO7 dto, PageRequest pageRequest);

    /**
     * 根据物料组查询组下所有物料
     *
     * @param tenantId 租户ID
     * @param itemGroupId 物料组ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 02:06:01
     * @return java.util.List<tarzan.material.domain.entity.MtMaterial>
     */
    List<MtMaterial> getMaterialByMaterialGroup(Long tenantId, String itemGroupId);

    /**
     * 根据车间ID查询其下所有产线
     *
     * @param tenantId 租户ID
     * @param areaId 车间ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 04:35:54
     * @return java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    List<MtModProductionLine> getProdLineByAreaId(Long tenantId, String areaId);

    /**
     * 根据产线查询其下所有工序
     *
     * @param tenantId 租户ID
     * @param prodLineId 产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 05:06:10
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> getWorkcellByProdLineId(Long tenantId, String prodLineId);

    /**
     * 创建盘点单以及盘点氛围
     *
     * @param tenantId 租户ID
     * @param dto 创建信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 05:19:46
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO6
     */
    HmeWipStocktakeDocDTO6 createStocktakeDoc(Long tenantId, HmeWipStocktakeDocDTO6 dto);

    /**
     * 更新盘点单头信息
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/7 05:40:01
     * @return com.ruike.hme.domain.entity.HmeWipStocktakeDoc
     */
    HmeWipStocktakeDoc updateStocktakeDoc(Long tenantId, HmeWipStocktakeDocDTO9 dto);

    /**
     * 删除盘点范围
     *
     * @param tenantId 租户ID
     * @param dto 盘点范围数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/7 06:10:03
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO10
     */
    HmeWipStocktakeDocDTO10 deleteStocktakeRange(Long tenantId, HmeWipStocktakeDocDTO10 dto);

    /**
     * 新增盘点范围
     *
     * @param tenantId 租户ID
     * @param dto      盘点范围数据
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO11
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 01:43:57
     */
    HmeWipStocktakeDocDTO11 addStocktakeRange(Long tenantId, HmeWipStocktakeDocDTO11 dto);

    /**
     * 新建盘点范围
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO11
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/23 04:54:06
     */
    HmeWipStocktakeDocDTO11 rangeCreate(Long tenantId, HmeWipStocktakeDocDTO11 dto);

    /**
     * 查询盘点单下产品范围中的产线ID集合
     *
     * @param tenantId    租户ID
     * @param stocktakeId 盘点单ID
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 02:48:29
     */
    List<String> plStocktakeRangeQuery(Long tenantId, String stocktakeId);

    /**
     * 下达盘点单
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 03:29:44
     * @return com.ruike.hme.domain.vo.HmeWipStocktakeDocVO
     */
    HmeWipStocktakeDocDTO12 releasedWipStocktake(Long tenantId, HmeWipStocktakeDocDTO12 dto);

    /**
     * 完成盘点单前的校验
     *
     * @param tenantId
     * @param dto
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 05:29:19
     * @return com.ruike.hme.domain.vo.HmeWipStocktakeDocVO6
     */
    HmeWipStocktakeDocVO6 completedWipStocktakeValidate(Long tenantId, HmeWipStocktakeDocDTO12 dto);

    /**
     * 完成盘点单
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 06:24:55
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO12
     */
    HmeWipStocktakeDocDTO12 completedWipStocktake(Long tenantId, HmeWipStocktakeDocDTO12 dto);

    /**
     * 关闭盘点单前的校验
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/9 09:31:40
     * @return com.ruike.hme.domain.vo.HmeWipStocktakeDocVO6
     */
    HmeWipStocktakeDocVO6 closedWipStocktakeValidate(Long tenantId, HmeWipStocktakeDocDTO12 dto);

    /**
     * 关闭盘点单
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/9 09:58:55
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO12
     */
    HmeWipStocktakeDocDTO12 closedWipStocktake(Long tenantId, HmeWipStocktakeDocDTO12 dto);

    /**
     * 根据多个产线ID集合查询其下工序
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/9 08:00:04
     * @return io.choerodon.core.domain.Page<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    Page<MtModWorkcell> processQueryByProdLineIdList(Long tenantId, HmeWipStocktakeDocDTO13 dto, PageRequest pageRequest);

    /**
     * 盘点投料明细汇总
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/10 09:41:26
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7>
     */
    Page<HmeWipStocktakeDocVO7> releaseDetailPageQuery(Long tenantId, HmeWipStocktakeDocDTO15 dto, PageRequest pageRequest);

    /**
     * 根据多个产线ID查询产线信息
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/10 03:43:48
     * @return io.choerodon.core.domain.Page<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    Page<MtModProductionLine> prodLinePageQuery(Long tenantId, HmeWipStocktakeDocDTO14 dto, PageRequest pageRequest);

    /**
     * 根据盘点单查询盘点导出数据
     *
     * @param tenantId
     * @param stocktakeNum
     * @author sanfeng.zhang@hand-china.com 2021/3/16 15:15
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO8>
     */
    List<HmeWipStocktakeDocVO8> cosInventoryExport(Long tenantId, String stocktakeNum);

    /**
     * 在制盘点明细导出
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/8 02:18:51 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO2>
     */
    List<HmeWipStocktakeDocVO9> wipStocktakeDetailExport(Long tenantId, HmeWipStocktakeDocDTO5 dto);

    /**
     * 在制盘点汇总导出
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/8 02:42:29 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO10>
     */
    List<HmeWipStocktakeDocVO10> wipStocktakeSumExport(Long tenantId, HmeWipStocktakeDocDTO7 dto);

    /**
     * 在制盘点投料汇总
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 11:06:10 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWipStocktakeDocVO11>
     */
    List<HmeWipStocktakeDocVO11> releaseDetailExport(Long tenantId, HmeWipStocktakeDocDTO15 dto);
}
