package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/17 14:21
 */
public class MtDispositionGroupVO implements Serializable {

	private static final long serialVersionUID = -5440794880893633992L;
	private String dispositionGroupId;//处置组ID
	private String siteId;//站点ID
	private String description;//描述
	private String dispositionGroup;//描述

	public String getDispositionGroupId() {
		return dispositionGroupId;
	}

	public void setDispositionGroupId(String dispositionGroupId) {
		this.dispositionGroupId =dispositionGroupId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId =siteId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description =description;
	}

	public String getDispositionGroup() {
		return dispositionGroup;
	}

	public void setDispositionGroup(String dispositionGroup) {
		this.dispositionGroup =dispositionGroup;
	}

}