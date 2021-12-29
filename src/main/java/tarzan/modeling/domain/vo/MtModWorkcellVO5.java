package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2019/9/29 19:56
 * @Author: ${yiyang.xie}
 */
public class MtModWorkcellVO5 implements Serializable {
    private static final long serialVersionUID = 6558175916867081566L;
    /**
     * 工作单元ID
     */
    private String workcellId;
    /**
     * 工作单元编码
     */
    private String workcellCode;
    /**
     * 工作单元名称
     */
    private String workcellName;
    /**
     * 工作单元描述
     */
    private String description;
    /**
     * 有效标识
     */
    private String enableFlag;
    /**
     * 工作单元类型
     */
    private String workcellType;
    /**
     * 工作单元位置
     */
    private String workcellLocation;
    /**
     * 工作单元分类
     */
    private String workcellCategory;


    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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

    public String getWorkcellCategory() {
        return workcellCategory;
    }

    public void setWorkcellCategory(String workcellCategory) {
        this.workcellCategory = workcellCategory;
    }

}
