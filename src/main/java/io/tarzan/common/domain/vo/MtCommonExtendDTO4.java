package io.tarzan.common.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2020/9/28 11:30
 */
public class MtCommonExtendDTO4 implements Serializable {

	private static final long                    serialVersionUID = -273704997349257871L;
	@ApiModelProperty(value = "主表名")
	private              String                  tableName;
	@ApiModelProperty(value = "事件ID")
	private              String                  eventId;
	@ApiModelProperty("扩展属性列表")
	private              List<MtCommonExtendVO6> attrPropertyList;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public List<MtCommonExtendVO6> getAttrPropertyList() {
		return attrPropertyList;
	}

	public void setAttrPropertyList(List<MtCommonExtendVO6> attrPropertyList) {
		this.attrPropertyList = attrPropertyList;
	}
}
