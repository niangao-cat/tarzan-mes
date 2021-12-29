package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeMaterialLotNcRecordVO2;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname HmeCosGetChipScanBarcodeResponseDTO
 * @Description COS取片平台-待取片容器进站条码扫描 返回参数
 * @Date 2020/8/18 14:42
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipScanBarcodeResponseDTO implements Serializable {
    private static final long serialVersionUID = 3923767918867146604L;

    @ApiModelProperty("物料批条码ID")
    private String materialLotId;

    @ApiModelProperty("物料批条码")
    private String materialLotCode;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("来料数量")
    private Long primaryUomQty;

    @ApiModelProperty("库存批次")
    private String lot;

    @ApiModelProperty("录入批次")
    private String workingLot;

    @ApiModelProperty("Wafer编码")
    private String wafer;

    @ApiModelProperty("工单工艺工位在制记录id")
    private String cosRecord;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("容器类型")
    private String containerType;

    @ApiModelProperty("行数")
    private String locationRow;

    @ApiModelProperty("列数")
    private String locationColumn;

    @ApiModelProperty("芯片数")
    private String chipNum;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("平均波长 Avg λ（nm）")
    private BigDecimal averageWavelength;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("LOTNO")
    private String lotNo;

    @ApiModelProperty("来料备注")
    private String remark;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("剩余芯片数")
    private Long surplusCosNum;

    @ApiModelProperty("可新增数量")
    private Double addNum;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("良品数")
    private String okQty;

    @ApiModelProperty("装载信息 [HmeMaterialLotLoadVO/HmeMaterialLotLoadVO2]")
    private List<?> materialLotLoadList;

    @ApiModelProperty("不良信息")
    private List<HmeMaterialLotNcRecordVO2> materialLotNcList;

    @ApiModelProperty("实验代码")
    private String labCode;

    @ApiModelProperty("实验备注")
    private String labRemark;

}