package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsInstructionLineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 16:17
 */
@Data
public class WmsMaterialOnShelfDocDTO {
    private static final long serialVersionUID = -4512133452220027093L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据Num")
    private String instructionDocNum;
    @ApiModelProperty("单据状态")
//    @LovValue(value = "WMS.DELIVERY_DOC.STATUS",meaningField ="instructionDocStatusMeaning")
    private String instructionDocStatus;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("单据类型")
    private String instructionDocTypeDesc;
    @ApiModelProperty("检验完成时间")
    private Date inspectionDate;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("单据状态描述")
    private String instructionDocStatusMeaning;
    @ApiModelProperty("单据行")
    List<WmsMaterialOnShelfDocLineDTO> orderLineList;
}
