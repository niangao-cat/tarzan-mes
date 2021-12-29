package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmePreSelectionVO
 *
 * @author: chaonan.hu@hand-china.com 2021/11/02 14:11
 **/
@Data
public class HmePreSelectionVO implements Serializable {
    private static final long serialVersionUID = -7169495625453938087L;

    @ApiModelProperty(value = "主键ID")
    private String delRecordId;

    @ApiModelProperty(value = "数据库名")
    private String schemaName;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "删除的表数据主键ID")
    private String tableKeyId;

    @ApiModelProperty(value = "CID")
    private Long cid;

    @ApiModelProperty(value = "版本号")
    private Long objectVersionNumber;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "最后更新人")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateDate;
}
