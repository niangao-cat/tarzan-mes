package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: penglin.sui
 * @Date: 2020/11/17 14:56
 * @Description: 投料信息
 */

@Data
public class HmeEoJobSnBatchVO4 implements Serializable {
    private static final long serialVersionUID = -5276317066920691367L;
    @ApiModelProperty(value = "BOM ID")
    private String bomId;
    @ApiModelProperty(value = "组件ID")
    private String bomComponentId;
    @ApiModelProperty(value = "组件物料ID,非替代料时：componentMaterialId = materialId")
    private String componentMaterialId;
    @ApiModelProperty(value = "组件物料CODE")
    private String componentMaterialCode;
    @ApiModelProperty(value = "序号")
    private String lineNumber;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "单位用量")
    private BigDecimal qty;
    @ApiModelProperty(value = "工单需求数")
    private BigDecimal woQty;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "虚拟件标识")
    private String virtualFlag;
    @ApiModelProperty(value = "虚拟件组件标识(值为'X'前台不展示)")
    private String virtualComponentFlag;
    @ApiModelProperty(value = "升级标识")
    private String upgradeFlag;
    @ApiModelProperty(value = "生产类型(序列、批次、时效)")
    private String productionType;
    @ApiModelProperty(value = "生产类型描述")
    private String productionTypeDesc;
    @ApiModelProperty(value = "组件类型(主料、BOM替代、全局替代)")
    private String componentType;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位描述")
    private String uomName;
    @ApiModelProperty(value = "单位类型")
    private String uomType;
    @ApiModelProperty(value = "需求数量")
    private BigDecimal requirementQty;
    @ApiModelProperty(value = "是否替代料")
    private String isSubstitute;
    @ApiModelProperty(value = "将投数量")
    private BigDecimal willReleaseQty;
    @ApiModelProperty(value = "已投数量")
    private BigDecimal releasedQty;
    @ApiModelProperty(value = "勾选条码条数")
    private BigDecimal selectedSnCount;
    @ApiModelProperty(value = "勾选条码数量")
    private BigDecimal selectedSnQty;
    @ApiModelProperty(value = "截止时间(倒计时)")
    private String deadLineDate;
    @ApiModelProperty(value = "是否投料")
    private Integer isReleased;
    @ApiModelProperty(value = "替代组ID")
    private String bomSubstituteGroupId;
    @ApiModelProperty(value = "替代组")
    private String substituteGroup;
    @ApiModelProperty(value = "预留项目号")
    private String bomReserveNum;
    @ApiModelProperty(value = "预留行项目号")
    private String bomReserveLineNum;
    @ApiModelProperty(value = "上层虚拟件物料编码")
    private String topVirtualMaterialCode;
    @ApiModelProperty(value = "反冲料标识")
    private String backflushFlag;
    @ApiModelProperty(value = "当前绑定的SN号")
    private String currBindSnNum;
    @ApiModelProperty(value = "是否为COS类型条码")
    private String cosMaterialLotFlag;
    @ApiModelProperty(value = "物料组")
    private String itemGroup;
    @ApiModelProperty("虚拟号ID列表")
    private List<String> virtualIdList;
    @ApiModelProperty(value = "组件条码")
    private List<HmeEoJobSnBatchVO6> materialLotList;
    @ApiModelProperty(value = "是否可以修改将投数量")
    private String qtyAlterFlag;
    @ApiModelProperty(value = "将投数量最大值")
    private String qtyAlterLimit;

    @ApiModelProperty(value = "泵浦源作业平台标识")
    private String isPumpProcess;
}


