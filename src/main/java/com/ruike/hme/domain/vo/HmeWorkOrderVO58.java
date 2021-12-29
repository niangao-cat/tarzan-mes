package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工单管理
 *
 * @author jiangling.zheng@hand-china.com 2020-03-30 11:09:08
 */

@Data
public class HmeWorkOrderVO58 implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("订单编号")
    private String makeOrderNum;
    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;
    @ApiModelProperty(value = "工单版本")
    private String productionVersion;
    @ApiModelProperty(value = "工单类型")
    private String workOrderType;
    @ApiModelProperty(value = "生产线ID")
    private String productionLineId;
    @ApiModelProperty(value = "生产线名称")
    private String productionLineName;
    @ApiModelProperty(value = "工单状态")
    private String status;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "计划开始时间")
    private Date planStartTime;
    @ApiModelProperty(value = "计划结束时间")
    private Date planEndTime;
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
    @ApiModelProperty(value = "修改人ID")
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "修改人姓名")
    private String lastUpdatedByName;
    @ApiModelProperty(value = "修改时间")
    private Date lastUpdateDate;
    @ApiModelProperty(value = "事业部")
    private String departmentName;
    @ApiModelProperty(value = "车间编码")
    private String workshopCode;
    @ApiModelProperty(value = "车间")
    private String workshop;
    @ApiModelProperty(value = "工单类型说明")
    private String workOrderTypeDesc;
    @ApiModelProperty(value = "工单状态说明")
    private String orderStatusDesc;
    @ApiModelProperty(value = "产品编码")
    private String materialCode;
    @ApiModelProperty(value = "产品名称")
    private String materialName;
    @ApiModelProperty(value = "产品类型")
    private String materialCategory;
    @ApiModelProperty(value = "是否有BOM")
    private String isBom;
    @ApiModelProperty(value = "是否有工艺路线")
    private String isRouter;
    @ApiModelProperty(value = "BOM")
    private String bomName;
    @ApiModelProperty(value = "工艺路线")
    private String routerName;
    @ApiModelProperty(value = "是否有BOM说明")
    private String isBomDesc;
    @ApiModelProperty(value = "是否有工艺路线说明")
    private String isRouterDesc;
    @ApiModelProperty(value = "父工单ID")
    private String parentWorkOrderId;
    @ApiModelProperty(value = "交付时间")
    private Date deliveryDate;
    @ApiModelProperty(value = "工单下达时间")
    private Date publishDate;
    @ApiModelProperty(value = "交付时间")
    private String deliveryDateStr;
    @ApiModelProperty(value = "工单下达时间")
    private String publishDateStr;
    @ApiModelProperty(value = "本级备注")
    private String woRemark;
    @ApiModelProperty(value = "计划开始时间从")
    private Date planStartTimeFrom;
    @ApiModelProperty(value = "计划开始时间至")
    private Date planStartTimeTo;
    @ApiModelProperty(value = "计划完成时间从")
    private Date planEndTimeFrom;
    @ApiModelProperty(value = "计划完成时间至")
    private Date planEndTimeTo;
    @ApiModelProperty(value = "创建时间从")
    private Date creationDateFrom;
    @ApiModelProperty(value = "创建时间至")
    private Date creationDateTo;
    @ApiModelProperty(value = "修改时间从")
    private Date lastUpdateDateFrom;
    @ApiModelProperty(value = "修改时间至")
    private Date lastUpdateDateTo;
    @ApiModelProperty(value = "交付时间从")
    private Date deliveryDateFrom;
    @ApiModelProperty(value = "交付时间至")
    private Date deliveryDateTo;
    @ApiModelProperty(value = "工单下达时间从")
    private Date publishDateFrom;
    @ApiModelProperty(value = "工单下达时间至")
    private Date publishDateTo;
    @ApiModelProperty(value = "工作令号")
    private String workCommandNum;
    @ApiModelProperty(value = "完成数量")
    private BigDecimal completedQty;
    @ApiModelProperty(value = "下达数量")
    private BigDecimal releasedQty;

    @ApiModelProperty(value = "组件物料Id")
    private String bomMaterialId;

}
