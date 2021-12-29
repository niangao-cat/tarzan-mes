package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slj on 2018-09-06.
 */
public class MtModOrganizationChildrenVO implements Serializable {
    private static final long serialVersionUID = 1320185479570625955L;
    private String id;
    private String code;
    private String name;
    private String type;
    private String topSiteId;
    private Long sequence;
    private String parentId;
    private String parentType;
    private List<MtModOrganizationChildrenVO> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MtModOrganizationChildrenVO> getChildren() {
        return children;
    }

    public void setChildren(List<MtModOrganizationChildrenVO> children) {
        this.children = children;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}
