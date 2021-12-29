package tarzan.modeling.domain.vo;

import java.io.Serializable;

import tarzan.modeling.domain.entity.MtModLocator;

/**
 * Created by slj on 2018-12-03.
 */
public class MtModLocatorVO2 extends MtModLocator implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1091579324477250555L;
    private String typeDesc;
    private String locatorGroupCode;
    private String sizeUomCode;
    private String weightUomCode;
    private String ownerCode;
    private String ownerSiteCode;
    private String ownerDesc;
    private String ownerSiteDesc;
    private String sizeUomDesc;
    private String weightUomDesc;
    private String locatorGroupName;



    public String getLocatorGroupCode() {
        return locatorGroupCode;
    }

    public void setLocatorGroupCode(String locatorGroupCode) {
        this.locatorGroupCode = locatorGroupCode;
    }

    public String getSizeUomCode() {
        return sizeUomCode;
    }

    public void setSizeUomCode(String sizeUomCode) {
        this.sizeUomCode = sizeUomCode;
    }

    public String getWeightUomCode() {
        return weightUomCode;
    }

    public void setWeightUomCode(String weightUomCode) {
        this.weightUomCode = weightUomCode;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerSiteCode() {
        return ownerSiteCode;
    }

    public void setOwnerSiteCode(String ownerSiteCode) {
        this.ownerSiteCode = ownerSiteCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getOwnerDesc() {
        return ownerDesc;
    }

    public void setOwnerDesc(String ownerDesc) {
        this.ownerDesc = ownerDesc;
    }

    public String getOwnerSiteDesc() {
        return ownerSiteDesc;
    }

    public void setOwnerSiteDesc(String ownerSiteDesc) {
        this.ownerSiteDesc = ownerSiteDesc;
    }

    public String getSizeUomDesc() {
        return sizeUomDesc;
    }

    public void setSizeUomDesc(String sizeUomDesc) {
        this.sizeUomDesc = sizeUomDesc;
    }

    public String getWeightUomDesc() {
        return weightUomDesc;
    }

    public void setWeightUomDesc(String weightUomDesc) {
        this.weightUomDesc = weightUomDesc;
    }

    public String getLocatorGroupName() {
        return locatorGroupName;
    }

    public void setLocatorGroupName(String locatorGroupName) {
        this.locatorGroupName = locatorGroupName;
    }
}
