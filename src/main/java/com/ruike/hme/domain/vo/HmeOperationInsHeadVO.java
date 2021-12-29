package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * description:E-SOP查询结果
 *
 * @author penglin.sui@hand-china.com 2021-01-19 17:12
 */
@Data
public class HmeOperationInsHeadVO implements Serializable {
    private static final long serialVersionUID = 5928431465139279797L;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("物料版本ID")
    private String materialVersionId;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("物料类别ID")
    private String materialCategoryId;
    @ApiModelProperty("物料组")
    private String itemGroupCode;
    @ApiModelProperty("物料组描述")
    private String itemGroupDescription;
    @ApiModelProperty("工艺编码")
    private String operationName;
    @ApiModelProperty("附件编码")
    private String attachmentCode;
    @ApiModelProperty("附件名称")
    private String attachmentName;
    @ApiModelProperty("附件url")
    private String fileUrl;
}
