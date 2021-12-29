package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname InstructionDocRequestDTO
 * @Description 指令单据列表对象
 * @Date 2019/9/19 18:53
 * @Author by {HuangYuBin}
 */

@ApiModel("指令单据列表查询输入")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsInstructionDocRequestDTO implements Serializable {
	private static final long serialVersionUID = 7127811365905986959L;

	@ApiModelProperty(value = "送货单号")
	private String instructionDocNum;
	@ApiModelProperty(value = "站点ID")
	private String siteId;
	@ApiModelProperty(value = "单据类型,传入固定值\"delivery\"", hidden = true)
	private String instructionDocType;
	@ApiModelProperty(value = "送货单状态")
	private String instructionDocStatus;
	@ApiModelProperty(value = "物料ID")
	private String materialId;
	@ApiModelProperty(value = "物料描述")
	private String materialDes;
	@ApiModelProperty(value = "物料编码")
	private String materialCode;
	@ApiModelProperty(value = "发货时间从")
	private Date deliveryTimeStart;
	@ApiModelProperty(value = "发货时间至")
	private Date deliveryTimeEnd;
	@ApiModelProperty(value = "供应商ID")
	private String supplierId;
	@ApiModelProperty(value = "供应商描述")
	private String supplierDes;
	@ApiModelProperty(value = "预计送达时间从")
	private Date expectedArrivalTimeStart;
	@ApiModelProperty(value = "预计送达时间至")
	private Date expectedArrivalTimeEnd;
	@ApiModelProperty(value = "采购订单号")
	private String poNum;
	@ApiModelProperty(value = "采购订单行号")
	private String poLineNum;
	@ApiModelProperty(value = "实物条码")
	private String materialLotCode;
	@ApiModelProperty(value = "送货区域")
	private String shipTo;
	@ApiModelProperty(value = "ASN编号")
	private String asnNum;
	@ApiModelProperty(value = "采购订单号")
	private String poNumber;
	@ApiModelProperty(value = "全部属性是否为空")
	private String isAllNull;

	//重写时间类型的get,set方法

	public void setDeliveryTimeStart(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.deliveryTimeStart = sDateFormat.parse(date);
		} catch (Exception e) {
			this.deliveryTimeStart = null;
		}
	}

	public String getDeliveryTimeStart() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			return formatter.format(deliveryTimeStart);
		}catch (Exception e){
			return null;
		}
	}

	public void setDeliveryTimeEnd(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.deliveryTimeEnd = sDateFormat.parse(date);
		} catch (Exception e) {
			this.deliveryTimeEnd = null;
		}
	}

	public String getDeliveryTimeEnd() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			return formatter.format(deliveryTimeEnd);
		}catch (Exception e){
			return null;
		}
	}

	public void setExpectedArrivalTimeStart(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.expectedArrivalTimeStart = sDateFormat.parse(date);
		} catch (Exception e) {
			this.expectedArrivalTimeStart = null;
		}
	}

	public String getExpectedArrivalTimeStart() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			return formatter.format(expectedArrivalTimeStart);
		}catch (Exception e){
			return null;
		}
	}

	public void setExpectedArrivalTimeEnd(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.expectedArrivalTimeEnd = sDateFormat.parse(date);
		} catch (Exception e) {
			this.expectedArrivalTimeEnd = null;
		}
	}

	public String getExpectedArrivalTimeEnd() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			return formatter.format(expectedArrivalTimeEnd);
		}catch (Exception e){
			return null;
		}
	}

}