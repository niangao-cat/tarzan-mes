package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 售后拆机退库检测
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/5 16:51
 */
@Data
public class HmeServiceSplitReturnCheckVO {
    @ApiModelProperty("接收批次")
    private String batchNumber;
    @ApiModelProperty("检验项目Id")
    private String tagGroupId;
    @ApiModelProperty("检验项目编码")
    private String tagGroupCode;
    @ApiModelProperty("检验项目描述")
    private String tagGroupDescription;
    @ApiModelProperty("数据项Id")
    private String tagId;
    @ApiModelProperty("数据项")
    private String tag;
    @ApiModelProperty("检验结果")
    private String result;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("附件UUID")
    private String attachmentUuid;
    @ApiModelProperty("最后更新人ID")
    private Long lastUpdatedBy;
    @ApiModelProperty("最后更新人")
    private String lastUpdatedByName;
    @ApiModelProperty("最后更新时间")
    private Date lastUpdateDate;
}
