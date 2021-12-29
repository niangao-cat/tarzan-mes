package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 泵浦源预筛选基础表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
public interface HmePumpPreSelectionService {

    /**
     * 根据物料查询泵浦源筛选规则行数据
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 01:59:06
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpPreSelectionVO>
     */
    List<HmePumpPreSelectionVO> queryTagInfoByMaterial(Long tenantId, String materialId);

    /**
     * 扫描容器编码或泵浦源条码
     *
     * @param tenantId 租户ID
     * @param dto 扫描信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 02:25:50
     * @return com.ruike.hme.domain.vo.HmePumpPreSelectionVO3
     */
    HmePumpPreSelectionVO3 scanContainerOrPumpMaterialLot(Long tenantId, HmePumpPreSelectionDTO dto);

    /**
     * 扫描目标容器
     *
     * @param tenantId 租户ID
     * @param containerCode 容器编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 07:25:37
     * @return void
     */
    void scanTargetContainer(Long tenantId, String containerCode);

    /**
     * 泵浦源筛选批次LOV
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 09:47:07 
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmePumpPreSelectionVO2>
     */
    Page<HmePumpPreSelectionVO2> pumpSelectionLotLovQuery(Long tenantId, HmePumpPreSelectionDTO2 dto, PageRequest pageRequest);

    /**
     * 泵浦源预挑选撤回界面查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 10:38:12 
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmePumpPreSelectionVO5>
     */
    Page<HmePumpPreSelectionVO5> pumpPreSelectionRecallQuery(Long tenantId, HmePumpPreSelectionDTO3 dto, PageRequest pageRequest);

    /**
     * 泵浦源预挑选撤回
     *
     * @param tenantId 租户ID
     * @param recallList 撤回数据集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 01:48:15
     * @return void
     */
    void pumpPreSelectionRecall(Long tenantId, List<HmePumpPreSelectionVO5> recallList);

    /**
     * 泵浦源预挑选确认
     *
     * @param tenantId 租户ID
     * @param dto 确认数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 03:47:30
     * @return void
     */
    void pumpPreSelectionConfirm(Long tenantId, HmePumpPreSelectionDTO4 dto);
    
    /**
     * 泵浦源预筛选
     * 
     * @param tenantId 租户ID
     * @param dto 筛选基础数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 10:56:59 
     * @return void
     */
    HmePumpPreSelectionVO15 pumPreSelection(Long tenantId, HmePumpPreSelectionDTO5 dto);

    /**
     * 根据筛选批次查询筛选池条码信息
     *
     * @param tenantId 租户ID
     * @param dto 筛选批次
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/9 09:34:27
     * @return com.ruike.hme.domain.vo.HmePumpPreSelectionVO3
     */
    HmePumpPreSelectionVO3 scanSelectionLot(Long tenantId, HmePumpPreSelectionDTO6 dto);

    /**
     * 根据挑选批次查询套数
     *
     * @param tenantId 租户ID
     * @param selectionLot 挑选批次
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/28 10:58:41
     * @return com.ruike.hme.domain.vo.HmePumpPreSelectionVO16
     */
    HmePumpPreSelectionVO16 setsNumQueryBySelectionLot(Long tenantId, String selectionLot);
}
