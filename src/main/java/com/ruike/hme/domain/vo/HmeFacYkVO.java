package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ExcelSheet(zh = "FAC-Y宽判定标准")
public class HmeFacYkVO implements Serializable {
    private static final long serialVersionUID = -3398751682390521266L;
    @ApiModelProperty("主键")
    private String facYkId;
    @ApiModelProperty("物料id")
    private String materialId;
    @ApiModelProperty("fac物料id")
    private String facMaterialId;
    @ApiModelProperty("工位id")
    private String workcellId;
    @ApiModelProperty("物料编码")
    @ExcelColumn(zh = "物料编码", order = 0)
    private String materialCode;
    @ApiModelProperty("物料描述")
    @ExcelColumn(zh = "物料描述", order = 1)
    private String materialName;
    @ApiModelProperty("芯片类型")
    @ExcelColumn(zh = "芯片类型", order = 2)
    private String cosType;
    @ApiModelProperty("FAC物料编码")
    @ExcelColumn(zh = "FAC物料编码", order = 3)
    private String FacMaterialCode;
    @ApiModelProperty("FAC物料描述")
    @ExcelColumn(zh = "FAC物料描述", order = 4)
    private String FacMaterialName;
    @ApiModelProperty("工位编码")
    @ExcelColumn(zh = "工位编码", order = 5)
    private String workcellCode;
    @ApiModelProperty("工位描述")
    @ExcelColumn(zh = "工位描述", order = 6)
    private String description;
    @ApiModelProperty("标准值")
    @ExcelColumn(zh = "标准值", order = 7)
    private BigDecimal standardValue;
    @ApiModelProperty("允差")
    @ExcelColumn(zh = "允差", order = 8)
    private BigDecimal allowDiffer;
}
