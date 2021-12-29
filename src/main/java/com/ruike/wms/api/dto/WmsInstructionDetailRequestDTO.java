package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname InstructionDetailRequestDTO
 * @Description 指令详细列表传入参数
 * @Date 2019/9/21 11:55
 * @Author by {HuangYuBin}
 */
@ApiModel("指令详细列表查询输入")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsInstructionDetailRequestDTO implements Serializable {
	private static final long serialVersionUID = 7127811365905986959L;
	@ApiModelProperty(value = "送货单行号ID")
	private String instructionId;
	@ApiModelProperty(value = "送货单号")
	private String deliveryNum;
	@ApiModelProperty(value = "送货单行号")
	private String deliveryLineNum;
	@ApiModelProperty(value = "条码号")
	private String materialLotCode;
	@ApiModelProperty(value = "检验结果")
	private String qualityStatus;
	@ApiModelProperty(value = "条码状态")
	private String status;
	@ApiModelProperty(value = "接收时间从")
	private Date creationDateStart;
	@ApiModelProperty(value = "接收时间至")
	private Date creationDateEnd;
	@ApiModelProperty(value = "入库时间从")
	private Date inLocatorTimeStart;
	@ApiModelProperty(value = "入库时间至")
	private Date inLocatorTimeEnd;

	//重写时间类型的get,set方法

	public void setCreationDateStart(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.creationDateStart = sDateFormat.parse(date);
		} catch (Exception e) {
			this.creationDateStart = null;
		}
	}


	public void setCreationDateEnd(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.creationDateEnd = sDateFormat.parse(date);
		} catch (Exception e) {
			this.creationDateEnd = null;
		}
	}

	public void setInLocatorTimeStart(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.inLocatorTimeStart = sDateFormat.parse(date);
		} catch (Exception e) {
			this.inLocatorTimeStart = null;
		}
	}

	public void setInLocatorTimeEnd(String date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.inLocatorTimeEnd = sDateFormat.parse(date);
		} catch (Exception e) {
			this.inLocatorTimeEnd = null;
		}
	}

	public String getCreationDateStart() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			return formatter.format(this.creationDateStart);
		}catch (Exception e){
			return null;
		}
	}


	public String getCreationDateEnd() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			return formatter.format(this.creationDateEnd);
		}catch (Exception e){
			return null;
		}
	}

	public String getInLocatorTimeStart() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			return formatter.format(this.inLocatorTimeStart);
		}catch (Exception e){
			return null;
		}
	}

	public String getInLocatorTimeEnd() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			return formatter.format(this.inLocatorTimeEnd);
		}catch (Exception e){
			return null;
		}
	}

}