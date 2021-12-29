package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeWoJobSnReturnDTO5;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/17 15:05
 */
@Data
public class HmeCosFreezeTransferVO2 implements Serializable {

    private static final long serialVersionUID = 7314803811160987404L;

    @ApiModelProperty("转移条码")
    private String transferMaterialLotCode;

    @ApiModelProperty("eoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("条码ID")
    private String materialLotId;

    @ApiModelProperty("条码")
    private String materialLotCode;

    @ApiModelProperty("物料id")
    private String materialId;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料类型")
    private String materialType;

    @ApiModelProperty("工单id")
    private String workOrderId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "来料数量")
    private BigDecimal releaseQty;

    @ApiModelProperty(value = "来料批次")
    private String releaseLot;

    @ApiModelProperty(value = "来料信息记录id")
    private String cosRecordId;

    @ApiModelProperty(value = "转移来料信息记录id")
    private String transCosRecordId;

    @ApiModelProperty(value = "Wafer编码")
    private String waferNum;

    @ApiModelProperty(value = "转移Wafer编码")
    private String transWaferNum;

    @ApiModelProperty(value = "容器类型")
    private String containerType;

    @ApiModelProperty(value = "容器类型Id")
    private String containerTypeId;

    @ApiModelProperty(value = "转移容器类型")
    private String transContainerType;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "转移cos类型")
    private String transCosType;

    @ApiModelProperty(value = "行数")
    private Long locationRow;

    @ApiModelProperty(value = "列数")
    private Long locationColumn;

    @ApiModelProperty(value = "芯片数")
    private Long chipNum;

    @ApiModelProperty(value = "来料备注")
    private String remark;

    @ApiModelProperty(value = "平均波长")
    private BigDecimal averageWavelength;

    @ApiModelProperty(value = "来料类型")
    private String incomingType;

    @ApiModelProperty(value = "LOTNO")
    private String lotno;

    @ApiModelProperty("班组ID")
    private String wkcShiftId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("进站顺序")
    private Integer siteSort;

    @ApiModelProperty("装载规则")
    private String loadRule;

    @ApiModelProperty("装载规则含义")
    private String loadRuleMeaning;

    @ApiModelProperty("条码实验代码")
    private String labCode;

    @ApiModelProperty("条码实验代码备注")
    private String labRemark;

    @ApiModelProperty(value = "图示信息")
    private List<HmeWoJobSnReturnDTO5> HmeWoJobSnReturnDTO5List;
}

