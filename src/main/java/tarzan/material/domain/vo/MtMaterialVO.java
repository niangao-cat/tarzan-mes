package tarzan.material.domain.vo;

import java.io.Serializable;

import tarzan.material.domain.entity.MtMaterial;

/**
 * Created by slj on 2018-11-19.
 */
public class MtMaterialVO extends MtMaterial implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7917339159647255887L;
    private String sizeUomCode;
    private String sizeUomName;
    private String weightUomCode;
    private String weightUomName;
    private String primaryUomCode;
    private String primaryUomName;
    private String shelfLifeUomCode;
    private String shelfLifeUomName;
    private String volumeUomCode;
    private String volumeUomName;
    private String secondaryUomCode;
    private String secondaryUomName;


    public String getSizeUomCode() {
        return sizeUomCode;
    }

    public void setSizeUomCode(String sizeUomCode) {
        this.sizeUomCode = sizeUomCode;
    }

    public String getSizeUomName() {
        return sizeUomName;
    }

    public void setSizeUomName(String sizeUomName) {
        this.sizeUomName = sizeUomName;
    }

    public String getWeightUomCode() {
        return weightUomCode;
    }

    public void setWeightUomCode(String weightUomCode) {
        this.weightUomCode = weightUomCode;
    }

    public String getWeightUomName() {
        return weightUomName;
    }

    public void setWeightUomName(String weightUomName) {
        this.weightUomName = weightUomName;
    }

    public String getPrimaryUomCode() {
        return primaryUomCode;
    }

    public void setPrimaryUomCode(String primaryUomCode) {
        this.primaryUomCode = primaryUomCode;
    }

    public String getShelfLifeUomCode() {
        return shelfLifeUomCode;
    }

    public void setShelfLifeUomCode(String shelfLifeUomCode) {
        this.shelfLifeUomCode = shelfLifeUomCode;
    }

    public String getShelfLifeUomName() {
        return shelfLifeUomName;
    }

    public void setShelfLifeUomName(String shelfLifeUomName) {
        this.shelfLifeUomName = shelfLifeUomName;
    }

    public String getVolumeUomCode() {
        return volumeUomCode;
    }

    public void setVolumeUomCode(String volumeUomCode) {
        this.volumeUomCode = volumeUomCode;
    }

    public String getVolumeUomName() {
        return volumeUomName;
    }

    public void setVolumeUomName(String volumeUomName) {
        this.volumeUomName = volumeUomName;
    }

    public String getSecondaryUomCode() {
        return secondaryUomCode;
    }

    public void setSecondaryUomCode(String secondaryUomCode) {
        this.secondaryUomCode = secondaryUomCode;
    }

    public String getSecondaryUomName() {
        return secondaryUomName;
    }

    public void setSecondaryUomName(String secondaryUomName) {
        this.secondaryUomName = secondaryUomName;
    }

    public String getPrimaryUomName() {
        return primaryUomName;
    }

    public void setPrimaryUomName(String primaryUomName) {
        this.primaryUomName = primaryUomName;
    }

}