package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnReworkVO implements Serializable {
    private static final long serialVersionUID = 8358216231707432479L;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
    @ApiModelProperty(value = "升级标识")
    private String upgradeFlag;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位描述")
    private String uomName;
    @ApiModelProperty(value = "已投数量")
    private BigDecimal releasedQty;
    @ApiModelProperty(value = "将投数量")
    private BigDecimal willReleaseQty;
    @ApiModelProperty(value = "勾选条码条数")
    private BigDecimal selectedSnCount;
    @ApiModelProperty(value = "勾选条码数量")
    private BigDecimal selectedSnQty;
    @ApiModelProperty(value = "截止时间(倒计时)")
    private String deadLineDate;
    @ApiModelProperty(value = "是否投料")
    private Integer isReleased;
    @ApiModelProperty(value = "辅助单位ID")
    private String secondaryUomId;
    @ApiModelProperty(value = "物料类型 SN 时效 批次")
    private String productionType;
    @ApiModelProperty(value = "序列物料作业ID")
    private String jobMaterialId;
    @ApiModelProperty(value = "组件条码")
    private List<HmeEoJobSnReworkVO3> materialLotList;
    @ApiModelProperty(value = "打印标识")
    private String printFlag;
    @ApiModelProperty(value = "子条码")
    private String subCode;

    @ApiModelProperty(value = "删除标识，为Y时代表要删除物料，需弹窗确认，为N则不需弹窗")
    private String deleteFlag;
}
