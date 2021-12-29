package com.ruike.hme.domain.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeWipStocktakeActual;
import com.ruike.hme.domain.vo.HmeWipStocktakeExecuteVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * 在制品盘点执行 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 09:57
 */
public interface HmeWipStocktakeExecuteService {

    /**
     * 单据扫描
     *
     * @param tenantId     租户
     * @param stocktakeNum 单据
     * @return java.util.List<com.ruike.hme.api.dto.HmeWipStocktakeExecuteDocRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 10:27:58
     */
    HmeWipStocktakeExecuteDocRepresentationDTO docScan(Long tenantId,
                                                       String stocktakeNum);

    /**
     * 单据选择
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.hme.api.dto.HmeWipStocktakeExecuteDocRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 10:27:58
     */
    List<HmeWipStocktakeExecuteDocRepresentationDTO> docSelect(Long tenantId,
                                                               HmeWipStocktakeExecuteDocQueryDTO dto);

    /**
     * 范围扫描
     *
     * @param tenantId 租户
     * @param dto      扫描查询条件
     * @return WipStocktakeRangeScanResponseDTO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 10:27:58
     */
    WipStocktakeRangeScanResponseDTO rangeScan(Long tenantId,
                                               WipStocktakeRangeScanQueryDTO dto);

    /**
     * 条码扫描
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return com.ruike.hme.api.dto.WipStocktakeBarcodeScanResponseDTO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 02:35:32
     */
    WipStocktakeBarcodeScanResponseDTO barcodeScan(Long tenantId,
                                                   WipStocktakeBarcodeScanQueryDTO dto);

    /**
     * 存在在盘点实际验证
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return com.ruike.hme.api.dto.WipStocktakeBarcodeScanResponseDTO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 02:35:32
     */
    WipStocktakeBarcodeScanResponseDTO actualExistsValid(Long tenantId,
                                                         WipStocktakeMaterialLotValidQueryDTO dto);

    /**
     * 校验获取值在盘点实绩表中是否不存在初/复盘数据
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return com.ruike.hme.api.dto.WipStocktakeBarcodeScanResponseDTO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 02:35:32
     */
    WipStocktakeBarcodeScanResponseDTO countedValid(Long tenantId,
                                                    WipStocktakeMaterialLotValidQueryDTO dto);

    /**
     * 物料明细获取
     *
     * @param tenantId 租户
     * @param query    查询条件
     * @return java.util.List<com.ruike.hme.api.dto.WipStocktakeMaterialDetailRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 02:55:07
     */
    List<WipStocktakeMaterialDetailRepresentationDTO> materialDetailGet(Long tenantId, WipStocktakeMaterialDetailQueryDTO query);

    /**
     * 条码获取
     *
     * @param tenantId 租户
     * @param query    查询条件
     * @return java.util.List<com.ruike.hme.api.dto.WipStocktakeMaterialDetailRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 02:55:07
     */
    Page<WipStocktakeExecSnRepresentationDTO> snDetailGet(Long tenantId, WipStocktakeSnDetailQueryDTO query, PageRequest pageRequest);

    /**
     * 盘点范围补充
     *
     * @param tenantId 租户
     * @param command  保存命令
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:27:26
     */
    int stocktakeRangeAddition(Long tenantId, WipStocktakeRangeSaveCommandDTO command);

    /**
     * 盘点实际补充
     *
     * @param tenantId 租户
     * @param command  保存命令
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:27:26
     */
    int actualAddition(Long tenantId, WipStocktakeActualSaveCommandDTO command);

    /**
     * 执行
     *
     * @param tenantId 租户
     * @param command  执行命令
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:27:26
     */
    HmeWipStocktakeExecuteVO execute(Long tenantId, WipStocktakeExecuteCommandDTO command);

    /**
     * 盘点实际更新
     *
     * @param list    实际列表
     * @param eventId 事件
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:27:26
     */
    void stocktakeActualUpdate(List<HmeWipStocktakeActual> list, String eventId);
}
