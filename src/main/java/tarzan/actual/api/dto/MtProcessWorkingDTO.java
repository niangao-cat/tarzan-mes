package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description:
 * @Date: 2020-02-11
 * @Author: xiao tang
 */
public class MtProcessWorkingDTO implements Serializable {

	private static final long serialVersionUID = -8731605495384694100L;
	
	@ApiModelProperty(value = "工作单元Id" ,required = true)
	private String workcellId;
	
	@ApiModelProperty(value = "站点ID",required = true)
	private String siteId;

	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

}
