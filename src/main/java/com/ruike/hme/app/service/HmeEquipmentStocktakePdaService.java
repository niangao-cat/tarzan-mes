package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEquipmentStocktakePdaDTO;
import com.ruike.hme.api.dto.HmeEquipmentStocktakePdaDTO2;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakePdaVO;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakePdaVO2;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.util.List;

/**
 * HmeEquipmentStocktakePdaService
 * 设备盘点PDA应用服务
 * @author: chaonan.hu@hand-china.com 2021/04/01 15:33:21
 **/
public interface HmeEquipmentStocktakePdaService {

    /**
     * 值集查询-用于海马汇端
     *
     * @param tenantId 租户ID
     * @param lovCode 独立值集编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/1 03:47:05
     * @return java.util.List<org.hzero.boot.platform.lov.dto.LovValueDTO>
     */
    List<LovValueDTO> equipmentStatusLovQuery(Long tenantId, String lovCode);

    /**
     * 扫描单据
     *
     * @param tenantId 租户ID
     * @param stocktakeNum 单据号
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/1 04:04:03
     * @return com.ruike.hme.domain.vo.HmeEquipmentStocktakePdaVO
     */
    HmeEquipmentStocktakePdaVO scanStocktakeNum(Long tenantId, String stocktakeNum);

    /**
     * 扫描设备
     *
     * @param tenantId 租户ID
     * @param dto 传参信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/1 04:35:00
     * @return com.ruike.hme.domain.vo.HmeEquipmentStocktakePdaVO2
     */
    HmeEquipmentStocktakePdaVO2 scanEquipment(Long tenantId, HmeEquipmentStocktakePdaDTO dto);

    /**
     * 提交
     * 
     * @param tenantId 租户ID
     * @param dto 提交信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/1 04:59:07 
     * @return com.ruike.hme.api.dto.HmeEquipmentStocktakePdaDTO2
     */
    HmeEquipmentStocktakePdaDTO2 submit(Long tenantId, HmeEquipmentStocktakePdaDTO2 dto);
}
