package io.tarzan.common.domain.vo;

import java.io.Serializable;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/17 10:05
 */
public class MtGenStatusVO5 implements Serializable {

	private static final long serialVersionUID = 4608972923961866621L;
	private String relationTable;// 关联表
	private Double sequence;// 顺序
	private String statusGroup;// 类型组编码
	private String defaultFlag;// 初始数据标识
	private String genStatusId;// 通用状态ID
	private String module;// 服务包
	private String description;// 类型描述
	private String initialFlag;// 默认标识
	private String statusCode;// 类型编码

	public String getRelationTable() {
		return relationTable;
	}

	public void setRelationTable(String relationTable) {
		this.relationTable =relationTable;
	}

	public Double getSequence() {
		return sequence;
	}

	public void setSequence(Double sequence) {
		this.sequence =sequence;
	}

	public String getStatusGroup() {
		return statusGroup;
	}

	public void setStatusGroup(String statusGroup) {
		this.statusGroup =statusGroup;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag =defaultFlag;
	}

	public String getGenStatusId() {
		return genStatusId;
	}

	public void setGenStatusId(String genStatusId) {
		this.genStatusId =genStatusId;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module =module;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description =description;
	}

	public String getInitialFlag() {
		return initialFlag;
	}

	public void setInitialFlag(String initialFlag) {
		this.initialFlag =initialFlag;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode =statusCode;
	}

}