package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slj on 2018-12-04.
 */
public class MtModLocatorVO implements Serializable {
    private static final long serialVersionUID = -6820841485980170625L;

    private List<MtModLocatorVO> locatorVO;
    private String myId;
    private String type;
    private String code;
    private String name;
    private MtModOrganizationChildrenVO children;
    private int pro;
    private String relId;
    private String topSiteId;

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public MtModOrganizationChildrenVO getChildren() {
        return children;
    }

    public void setChildren(MtModOrganizationChildrenVO children) {
        this.children = children;
    }

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public List<MtModLocatorVO> getLocatorVO() {
        return locatorVO;
    }

    public void setLocatorVO(List<MtModLocatorVO> locatorVO) {
        this.locatorVO = locatorVO;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

}
