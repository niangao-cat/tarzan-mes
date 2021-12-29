package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.Date;

/**
 * @Classname MaterialGetReturnResponseDTO
 * @Description 领退料单查询返回参数
 * @Date 2019/10/12 14:41
 * @Author by {HuangYuBin}
 */
@ApiModel("领退料单查询返回参数")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialGetReturnResponseDTO {
	@ApiModelProperty(value = "单据ID")
	private String instructionDocId;
	@ApiModelProperty(value = "单据号")
	private String instructionDocNum;
	@ApiModelProperty(value = "工厂ID")
	private String siteId;
	@ApiModelProperty(value = "工厂编码")
	private String siteCode;
	@LovValue(lovCode = "Z_RP_DOC_TYPE",meaningField="type",defaultMeaning = "无")
	@ApiModelProperty(value = "单据类型")
	private String type;
	@LovValue(lovCode = "Z_INSTRUCTION_DOC_STATUS",meaningField="status",defaultMeaning = "无")
	@ApiModelProperty(value = "单据状态")
	private String status;
	@ApiModelProperty(value = "成本中心ID")
	private String costCenterId;
	@ApiModelProperty(value = "成本中心CODE")
	private String costCenterCode;
	@ApiModelProperty(value = "申请人")
	private String applier;
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date creationDate;
	@ApiModelProperty(value = "执行人")
	private String execution;
	@ApiModelProperty(value = "执行时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date executionDate;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "等级编码")
	private String gradeCode;
	@ApiModelProperty(value = "来源仓库")
	private String sourceLocator;
	@ApiModelProperty(value = "目标仓库")
	private String targetLocator;
}