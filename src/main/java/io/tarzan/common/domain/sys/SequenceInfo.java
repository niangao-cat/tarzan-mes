package io.tarzan.common.domain.sys;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class SequenceInfo implements Serializable {

    private static final long serialVersionUID = 2351074237353812987L;
    private boolean isCustomPrimary;
    private String primarySequence;
    private String cidSequence;

    public SequenceInfo() {

    }

    public SequenceInfo(boolean isCustomPrimary, String primarySequence, String cidSequence) {
        this.isCustomPrimary = isCustomPrimary;
        this.primarySequence = primarySequence;
        this.cidSequence = cidSequence;
    }

    public boolean isCustomPrimary() {
        return isCustomPrimary;
    }

    public void setCustomPrimary(boolean isCustomPrimary) {
        this.isCustomPrimary = isCustomPrimary;
    }

    public String getPrimarySequence() {
        return primarySequence;
    }

    public void setPrimarySequence(String primarySequence) {
        this.primarySequence = primarySequence;
    }

    public String getCidSequence() {
        return cidSequence;
    }

    public void setCidSequence(String cidSequence) {
        this.cidSequence = cidSequence;
    }

}
