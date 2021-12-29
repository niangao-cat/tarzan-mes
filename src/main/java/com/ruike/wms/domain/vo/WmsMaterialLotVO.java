package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * WmsMaterialLotVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/28 15:09
 */
@Data
public class WmsMaterialLotVO implements Serializable {
    private static final long serialVersionUID = 1986346833781838208L;
    @ApiModelProperty("作为物料批唯一标识，用于其他数据结构引用该物料批")
    private String materialLotId;
    @ApiModelProperty(value = "描述该物料批的唯一编码，用于方便识别", required = true)
    private List<String> materialLotCode;
    @ApiModelProperty(value = "该物料批所在生产站点的标识ID", required = true)
    private String siteId;
    @ApiModelProperty(value = "描述该物料批的有效状态：描述该物料批的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效", required = true)
    private String enableFlag;
    @ApiModelProperty(value = "物料批所标识实物的质量状态：", required = true)
    private List<String> qualityStatus;
    @ApiModelProperty(value = "该物料批所表示的实物的物料标识ID", required = true)
    private String materialId;
    @ApiModelProperty(value = "该物料批表示实物的主计量单位", required = true)
    private String primaryUomId;
    @ApiModelProperty(value = "实物在主计量单位下的数量", required = true)
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "物料批当前所在货位标识ID，表示物料批仓库下存储位置")
    private List<String> locatorId;
    @ApiModelProperty(value = "指示物料批所表示实物的来源批次编号")
    private String lot;
    @ApiModelProperty(value = "物料批对应实物通过采购获取时，指示物料批采购来源供应商标识ID")
    private String supplierId;
    @ApiModelProperty(value = "物料批对应实物通过采购获取时，指示物料批采购来源供应商地点标识ID")
    private String supplierSiteId;
    @ApiModelProperty(value = "物料批冻结标识，用来盘点时冻结物料批的移动。为Y时不允许移动物料批。")
    private String freezeFlag;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "仓库Id")
    private List<String> warehouseId;
    @ApiModelProperty(value = "冻结时间从")
    private String freezeDateFrom;
    @ApiModelProperty(value = "冻结时间至")
    private String freezeDateTo;

    /**
     * 新增查询条件：
     * 供应商批次 ：supplierLotValue
     * 销售订单 ：soNumValue
     * 状态 ： lotStatus
     * 质量状态
     */
    @ApiModelProperty(value = "供应商批次")
    private String supplierLotValue;
    @ApiModelProperty(value = "销售订单")
    private String soNumValue;
    @ApiModelProperty(value = "状态")
    private List<String> lotStatus;

}
