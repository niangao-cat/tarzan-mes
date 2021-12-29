package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname InstructionDetailResponseDTO
 * @Description 指令详细列表返回参数
 * @Date 2019/9/21 10:42
 * @Author by {HuangYuBin}
 */
@ApiModel("指令详细列表")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsInstructionDetailResponseDTO implements Serializable {
	private static final long serialVersionUID = 7127811365905986959L;
	@ApiModelProperty(value = "条码ID")
	private String materialLotId;
	@ApiModelProperty(value = "条码号")
	private String materialLotCode;
	@ApiModelProperty(value = "条码状态")
	private String status;
	@ApiModelProperty(value = "条码数量")
	private Double primaryUomQty;
	@ApiModelProperty(value = "单位")
	private String uomCode;
	@ApiModelProperty(value = "批次")
	private String lot;
	@ApiModelProperty(value = "接收批号")
	private String deliveryBatch;
	@ApiModelProperty(value = "检验批号")
	private String qualityBatch;
	@ApiModelProperty(value = "接收时间")
	private Date creationDate;
	@ApiModelProperty(value = "接收人")
	private Long createdBy;
	@ApiModelProperty(value = "接收人名称")
	private String createdByName;
	@ApiModelProperty(value = "检验完成时间")
	private Date qcCompleteTime;
	@ApiModelProperty(value = "检验结果")
	private String qualityStatus;
	@ApiModelProperty(value = "入库人")
	private Long instockBy;
	@ApiModelProperty(value = "入库人名称")
	private String instockByName;
	@ApiModelProperty(value = "入库时间")
	private Date inLocatorTime;


	@ApiModelProperty(value = "送货单行号")
	private String instructionNum;
	@ApiModelProperty(value = "物料编码")
	private String materialCode;
	@ApiModelProperty(value = "物料描述")
	private String materialName;
	@ApiModelProperty(value = "采购订单号")
	private String poNum;
	@ApiModelProperty(value = "采购订单行号")
	private String poLineNum;
	@ApiModelProperty(value = "采购订单发运号")
	private String poLineLocationNum;


	public void setInstockBy(String data){
		try {
			this.instockBy = Long.parseLong(data);
		} catch (Exception e) {
			this.instockBy = null;
		}
	}


	public void setQcCompleteTime(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.qcCompleteTime = sDateFormat.parse(date);
		} catch (Exception e) {
			this.qcCompleteTime = null;
		}
	}
	public void setInLocatorTime(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.inLocatorTime = sDateFormat.parse(date);
		} catch (Exception e) {
			this.inLocatorTime = null;
		}
	}
	public String getCreationDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			return formatter.format(creationDate);
		}catch (Exception e){
			return "";
		}
	}

	public String getQcCompleteTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			return formatter.format(qcCompleteTime);
		}catch (Exception e){
			return null;
		}
	}
}