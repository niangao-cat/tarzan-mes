package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 10:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HmeFreezePrivilegeDetailObjectVO {
    @ApiModelProperty(value = "明细对象类型")
    private String detailObjectType;
    @ApiModelProperty(value = "明细对象id")
    private String detailObjectId;
    @ApiModelProperty(value = "明细对象编码")
    private String detailObjectCode;
    @ApiModelProperty(value = "明细对象名称")
    private String detailObjectName;
}
