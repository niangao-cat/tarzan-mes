package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 检验组查询返回值
 * @author: han.zhang
 * @create: 2020/04/22 18:02
 */
@Getter
@Setter
@ToString
public class QmsTagGroupQueryReturnDTO implements Serializable {
    private static final long serialVersionUID = -2729348894963572545L;

    @ApiModelProperty(value = "主键id")
    private String tagGroupRelId;

    @ApiModelProperty(value = "数据收集组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "数据收集组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "数据收集组描述")
    private String tagGroupDescription;

    @ApiModelProperty(value = "备注")
    private String remark;

}