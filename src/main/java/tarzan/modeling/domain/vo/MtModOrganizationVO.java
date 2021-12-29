package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slj on 2018-09-05.
 */
public class MtModOrganizationVO implements Serializable {
    private static final long serialVersionUID = -391624729185505704L;
    private List<MtModSiteVO3> siteVO;
    private String myId;
    private String type;
    private String code;
    private String name;
    private MtModOrganizationChildrenVO children;
    private int pro;
    private String relId;
    private String topSiteId;
    private Long sequence;
    private List<MtModOrganizationVO> enterPriseVOList;
    private List<MtModSiteVO3> siteVOList;
    private List<MtModWorkcellVO> workcellVOList;
    private List<MtModProductionLineVO> prodlineVOList;
    private List<MtModAreaVO3> areaVOList;

    public MtModOrganizationVO() {

        enterPriseVOList = new ArrayList<MtModOrganizationVO>();
        siteVOList = new ArrayList<MtModSiteVO3>();
        workcellVOList = new ArrayList<MtModWorkcellVO>();
        prodlineVOList = new ArrayList<MtModProductionLineVO>();
        areaVOList = new ArrayList<MtModAreaVO3>();
    }


    public List<MtModSiteVO3> getSiteVO() {
        return siteVO;
    }

    public void setSiteVO(List<MtModSiteVO3> siteVO) {
        this.siteVO = siteVO;
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

    public List<MtModOrganizationVO> getEnterPriseVOList() {
        return enterPriseVOList;
    }

    public void setEnterPriseVOList(List<MtModOrganizationVO> enterPriseVOList) {
        this.enterPriseVOList = enterPriseVOList;
    }

    public List<MtModAreaVO3> getAreaVOList() {
        return areaVOList;
    }

    public void setAreaVOList(List<MtModAreaVO3> areaVOList) {
        this.areaVOList = areaVOList;
    }

    public List<MtModSiteVO3> getSiteVOList() {
        return siteVOList;
    }

    public void setSiteVOList(List<MtModSiteVO3> siteVOList) {
        this.siteVOList = siteVOList;
    }

    public List<MtModWorkcellVO> getWorkcellVOList() {
        return workcellVOList;
    }

    public void setWorkcellVOList(List<MtModWorkcellVO> workcellVOList) {
        this.workcellVOList = workcellVOList;
    }

    public List<MtModProductionLineVO> getProdlineVOList() {
        return prodlineVOList;
    }

    public void setProdlineVOList(List<MtModProductionLineVO> prodlineVOList) {
        this.prodlineVOList = prodlineVOList;
    }



}
