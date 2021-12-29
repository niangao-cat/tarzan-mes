package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModWorkcellVO1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6479524095037505875L;

    private String workcellCode; // 工作单元编号
    private String workcellName; // 工作单元名称
    private String description; // 工作单元描述
    private String workcellType; // 工作单元类型
    private String workcellLocation; // 工作单元位置
    private String enableFlag; // 是否有效
    private String workcellCategory; // 工作单元分类

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkcellType() {
        return workcellType;
    }

    public void setWorkcellType(String workcellType) {
        this.workcellType = workcellType;
    }

    public String getWorkcellLocation() {
        return workcellLocation;
    }

    public void setWorkcellLocation(String workcellLocation) {
        this.workcellLocation = workcellLocation;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getWorkcellCategory() {
        return workcellCategory;
    }

    public void setWorkcellCategory(String workcellCategory) {
        this.workcellCategory = workcellCategory;
    }

}
