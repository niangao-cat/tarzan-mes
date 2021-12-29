package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname InstructionDocResponseDTO
 * @Description TODO
 * @Date 2019/9/21 14:37
 * @Author by {HuangYuBin}
 */

@ApiModel("指令单据列表查询输出")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsInstructionDocResponseDTO implements Serializable {
	private static final long serialVersionUID = 7127811365905986959L;

	public static final String FIELD_CREATION_DATE = "creationDate";

	@ApiModelProperty(value = "送货单ID")
	private String instructionDocId;
	@ApiModelProperty(value = "送货单号")
	private String instructionDocNum;
	@ApiModelProperty(value = "送货单状态")
	private String instructionDocStatus;
	@ApiModelProperty(value = "供应商ID")
	private String supplierId;
	@ApiModelProperty(value = "供应商编码")
	private String supplierCode;
	@ApiModelProperty(value = "预计送达时间")
	private Date expectedArrivalTime;
	@ApiModelProperty(value = "供应商描述")
	private String supplierName;
	@ApiModelProperty(value = "送货区域")
	private String shipTo;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "SRM送货单号")
	private String asnNum;
	@ApiModelProperty(value = "发货日期")
	private Date deliveryTime;
	@ApiModelProperty(value = "实际送达日期")
	private Date actualArriveTime;
	@ApiModelProperty(value = "站点id")
	private String siteId;
	@ApiModelProperty(value = "站点code")
	private String siteCode;
	@ApiModelProperty(value = "站点name")
	private String siteName;
	@ApiModelProperty(value = "创建时间")
	private String creationDate;
	@ApiModelProperty(value = "创建人id")
	private String createdBy;
	@ApiModelProperty(value = "接收工厂")
	private String customerName;
	@ApiModelProperty(value = "创建人")
	private String realName;

	public void setExpectedArrivalTime(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.expectedArrivalTime = sDateFormat.parse(date);
		} catch (Exception e) {
			this.expectedArrivalTime = null;
		}
	}
	public void setDeliveryTime(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.deliveryTime = sDateFormat.parse(date);
		} catch (Exception e) {
			this.deliveryTime = null;
		}
	}
	public void setActualArriveTime(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.actualArriveTime = sDateFormat.parse(date);
		} catch (Exception e) {
			this.actualArriveTime = null;
		}
	}
}