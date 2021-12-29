package com.ruike.wms.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * WmsMaterialLotFrozenDTO
 *
 * @author liyuan.lv@hand-china.com 2020/04/28 11:21
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialLotFrozenDTO {
    private String siteId;
    private String materialId;
    private String materialVersion;
    private String lotCode;
    private List<String> materialLotCode;
    private String freezeFlag;
    private String supplierId;
    private List<String> warehouseId;
    private List<String> locatorId;
    private String freezeDateFrom;
    private String freezeDateTo;
    private String pdaFlag;

    /**
     * 新增查询条件：
     * 供应商批次 ：supplierLotValue
     * 销售订单 ：soNumValue
     * 状态 ： lotStatus
     * 质量状态 : qualityStatus
     */
    private String supplierLotValue;
    private String soNumValue;
    private List<String> lotStatus;
    private List<String> qualityStatus;
}
