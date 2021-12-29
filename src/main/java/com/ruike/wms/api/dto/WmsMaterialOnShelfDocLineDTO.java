package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsInstructionLineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 16:17
 */
@Data
public class WmsMaterialOnShelfDocLineDTO {
    private static final long serialVersionUID = 4942253858816696526L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据编码")
    private String instructionDocNum;
    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("指令Num")
    private String instructionNum;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料code")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位code")
    private String uomCode;
    @ApiModelProperty("单位描述")
    private String uomName;
    @ApiModelProperty("单据行状态")
//    @LovValue(value = "WMS.DELIVERY_DOC_LINE.STATUS",meaningField ="instructionStatusMeaning" )
    private String instructionStatus;
    @ApiModelProperty("单据行状态Meaning")
    private String instructionStatusMeaning;
    @ApiModelProperty("单据行接收仓库")
    private String toLocatorId;
    @ApiModelProperty("指令实绩ID")
    private String actualId;
    @ApiModelProperty("执行数量")
    private BigDecimal executeQty;
    @ApiModelProperty("已入库条码总数量")
    private BigDecimal barCodeQty;
    @ApiModelProperty("待入库条码总数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty("条码总数汇总")
    private BigDecimal sumCount;
    @ApiModelProperty("待入库条码总数汇总")
    private BigDecimal sumBarCodeCount;
    @ApiModelProperty("单据行数量")
    private BigDecimal quantity;
    @ApiModelProperty("推荐货位")
    private String recommendLocatorCode;
    @ApiModelProperty(value = "任务号")
    private String taskNum;
    @ApiModelProperty(value = "接口返回标识")
    private String status;
    @ApiModelProperty(value = "亮灯关灯状态")
    private String taskStatus;
}
