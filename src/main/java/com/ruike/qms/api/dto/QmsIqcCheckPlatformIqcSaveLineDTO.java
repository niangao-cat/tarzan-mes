package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description IQC界面保存行数据
 * @Author tong.li
 * @Date 2020/5/13 9:28
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformIqcSaveLineDTO implements Serializable {
    private static final long serialVersionUID = -3322736922699173878L;


    @ApiModelProperty(value = "质检单行  主键")
    private String  iqcLineId;

    @ApiModelProperty(value = "质检单行 结论")
    private String inspectionResult;

    @ApiModelProperty(value = "质检单行 不合格数")
    private Long ngQty;

    @ApiModelProperty(value = "质检单行 合格数")
    private Long okQty;

    @ApiModelProperty(value = "质检单行 附件ID")
    private String attachmentUuid;

    @ApiModelProperty(value = "抽样数量")
    private BigDecimal sampleSize;


    @ApiModelProperty(value = "质检单明细")
    private List<QmsIqcCheckPlatformIqcSaveDetailDTO> detailList;

}
