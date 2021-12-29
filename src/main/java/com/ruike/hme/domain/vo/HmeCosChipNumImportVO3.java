package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 芯片号-导入VO
 *
 * @author jiangling.zheng@hand-china.com 2020/9/16 18:32
 */
@Data
public class HmeCosChipNumImportVO3 implements Serializable {

    private static final long serialVersionUID = 3178024012883930009L;

    @ApiModelProperty(value = "总列数")
    private Long totalQty;
    @ApiModelProperty(value = "不良数")
    private Long badQty;


}
