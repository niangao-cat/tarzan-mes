package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtModOrganizationVO5
 *
 * @author: {xieyiyang}
 * @date: 2020/2/20 9:49
 * @description:
 */
public class MtModOrganizationVO5 implements Serializable {
    private static final long serialVersionUID = -5459106106447097878L;

    @ApiModelProperty("需获取目标组织对象ID")
    private String organizationId;
    @ApiModelProperty("目标组织对象列表")
    private List<MtModOrganizationVO6> parentOrganizationList;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public List<MtModOrganizationVO6> getParentOrganizationList() {
        return parentOrganizationList;
    }

    public void setParentOrganizationList(List<MtModOrganizationVO6> parentOrganizationList) {
        this.parentOrganizationList = parentOrganizationList;
    }
}
