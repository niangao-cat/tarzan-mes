package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.List;


public class MtNumrangeVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7902903454947950551L;
    private String objectId;
    private String objectCode;
    private String objectDescription;
    private String numrangeGroup;
    private String numDescription;
    private String inputBox1;
    private String inputBox2;
    private String inputBox3;
    private String inputBox4;
    private String inputBox5;
    private String numExample;
    private String outsideNumFlag;
    private String enableFlag;
    private List<MtNumrangeRuleVO> lineList;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(String objectDescription) {
        this.objectDescription = objectDescription;
    }

    public String getNumrangeGroup() {
        return numrangeGroup;
    }

    public void setNumrangeGroup(String numrangeGroup) {
        this.numrangeGroup = numrangeGroup;
    }

    public String getNumDescription() {
        return numDescription;
    }

    public void setNumDescription(String numDescription) {
        this.numDescription = numDescription;
    }

    public String getInputBox1() {
        return inputBox1;
    }

    public void setInputBox1(String inputBox1) {
        this.inputBox1 = inputBox1;
    }

    public String getInputBox2() {
        return inputBox2;
    }

    public void setInputBox2(String inputBox2) {
        this.inputBox2 = inputBox2;
    }

    public String getInputBox3() {
        return inputBox3;
    }

    public void setInputBox3(String inputBox3) {
        this.inputBox3 = inputBox3;
    }

    public String getInputBox4() {
        return inputBox4;
    }

    public void setInputBox4(String inputBox4) {
        this.inputBox4 = inputBox4;
    }

    public String getInputBox5() {
        return inputBox5;
    }

    public void setInputBox5(String inputBox5) {
        this.inputBox5 = inputBox5;
    }

    public String getNumExample() {
        return numExample;
    }

    public void setNumExample(String numExample) {
        this.numExample = numExample;
    }

    public String getOutsideNumFlag() {
        return outsideNumFlag;
    }

    public void setOutsideNumFlag(String outsideNumFlag) {
        this.outsideNumFlag = outsideNumFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public List<MtNumrangeRuleVO> getLineList() {
        return lineList;
    }

    public void setLineList(List<MtNumrangeRuleVO> lineList) {
        this.lineList = lineList;
    }

}
