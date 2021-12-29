package io.tarzan.common.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Transient;

/**
 * @author Leeloing
 * @date 2020/9/28 10:28
 */
public class MtExtendRpcVO implements Serializable {
	private static final long                      serialVersionUID = -5052494740654020012L;
	@ApiModelProperty("拓展表描述主键")
	private              String                    extendTableDescId;
	@ApiModelProperty(value = "拓展表", required = true)
	private              String                    attrTable;
	@ApiModelProperty(value = "拓展表描述", required = true)
	private              String                    attrTableDesc;
	@ApiModelProperty(value = "主表", required = true)
	private              String                    mainTable;
	@ApiModelProperty(value = "主表主键", required = true)
	private              String                    mainTableKey;
	@ApiModelProperty(value = "历史表")
	private              String                    hisTable;
	@ApiModelProperty(value = "历史表主键")
	private              String                    hisTableKey;
	@ApiModelProperty(value = "历史表扩展表")
	private              String                    hisAttrTable;
	@ApiModelProperty(value = "有效性", required = true)
	private              String                    enableFlag;
	@ApiModelProperty(value = "初始化标识")
	private              String                    initialFlag;
	@Transient
	@ApiModelProperty(hidden = true)
	private transient    String                    lang;
	@ApiModelProperty(value = "扩展属性行")
	private              List<MtExtendColumnRpcVO> extendColumnList;

	public String getExtendTableDescId() {
		return extendTableDescId;
	}

	public void setExtendTableDescId(String extendTableDescId) {
		this.extendTableDescId = extendTableDescId;
	}

	public String getAttrTable() {
		return attrTable;
	}

	public void setAttrTable(String attrTable) {
		this.attrTable = attrTable;
	}

	public String getAttrTableDesc() {
		return attrTableDesc;
	}

	public void setAttrTableDesc(String attrTableDesc) {
		this.attrTableDesc = attrTableDesc;
	}

	public String getMainTable() {
		return mainTable;
	}

	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
	}

	public String getMainTableKey() {
		return mainTableKey;
	}

	public void setMainTableKey(String mainTableKey) {
		this.mainTableKey = mainTableKey;
	}

	public String getHisTable() {
		return hisTable;
	}

	public void setHisTable(String hisTable) {
		this.hisTable = hisTable;
	}

	public String getHisTableKey() {
		return hisTableKey;
	}

	public void setHisTableKey(String hisTableKey) {
		this.hisTableKey = hisTableKey;
	}

	public String getHisAttrTable() {
		return hisAttrTable;
	}

	public void setHisAttrTable(String hisAttrTable) {
		this.hisAttrTable = hisAttrTable;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getInitialFlag() {
		return initialFlag;
	}

	public void setInitialFlag(String initialFlag) {
		this.initialFlag = initialFlag;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public List<MtExtendColumnRpcVO> getExtendColumnList() {
		return extendColumnList;
	}

	public void setExtendColumnList(List<MtExtendColumnRpcVO> extendColumnList) {
		this.extendColumnList = extendColumnList;
	}
}
