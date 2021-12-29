package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-11-09 20:45
 */
@Data
public class HmeOperationInsHeadDTO2 implements Serializable {

    private static final long serialVersionUID = 7004362499882459636L;
    @ApiModelProperty("主键id")
    private String insHeaderId;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂编码")
    private String siteCode;
    @ApiModelProperty(value = "附件url")
    private String fileUrl;
    @ApiModelProperty(value = "文件名称")
    private String attachmentCode;
    @ApiModelProperty(value = "文件名称")
    private String attachmentName;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "生效时间")
    private Date startDate;
    @ApiModelProperty(value = "失效时间")
    private Date endDate;
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
    @ApiModelProperty(value = "创建人")
    private Long createdBy;
    @ApiModelProperty(value = "创建人名称")
    private String createdByName;
    @ApiModelProperty(value = "修改时间")
    private Date lastUpdateDate;
    @ApiModelProperty(value = "修改人")
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "修改人名称")
    private String lastUpdatedByName;
}
