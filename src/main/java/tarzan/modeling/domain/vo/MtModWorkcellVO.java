package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slj on 2018-09-05.
 */
public class MtModWorkcellVO implements Serializable {
    private static final long serialVersionUID = -1184306257150768552L;
    private List<MtModWorkcellVO> workcellVO;
    private String type;
    private String myId;
    private String code;
    private String name;
    private MtModOrganizationChildrenVO children;
    private int pro;
    private String relId;
    private List<MtModLocatorVO> LocatorVO;
    private String topSiteId;
    private Long sequence;
    private List<MtModWorkcellVO> workcellVOList;

    public MtModWorkcellVO() {

        workcellVOList = new ArrayList<MtModWorkcellVO>();

    }

    public List<MtModWorkcellVO> getWorkcellVO() {
        return workcellVO;
    }

    public void setWorkcellVO(List<MtModWorkcellVO> workcellVO) {
        this.workcellVO = workcellVO;
    }

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
        return LocatorVO;
    }

    public void setLocatorVO(List<MtModLocatorVO> locatorVO) {
        LocatorVO = locatorVO;
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

    public List<MtModWorkcellVO> getWorkcellVOList() {
        return workcellVOList;
    }

    public void setWorkcellVOList(List<MtModWorkcellVO> workcellVOList) {
        this.workcellVOList = workcellVOList;
    }



}
