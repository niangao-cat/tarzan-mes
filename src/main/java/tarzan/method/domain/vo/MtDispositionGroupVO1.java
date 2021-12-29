package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtDispositionGroupVO1 implements Serializable {

	private static final long serialVersionUID = 2091695376322947511L;
	private String dispositionGroupId;//处置组ID
	private String siteId;//站点ID
	private String description;//描述
	private String dispositionGroup;//描述
	private String siteCode;//站点编码
	private String siteName;//站点名称

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

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
}