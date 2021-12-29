package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContLoadDtlVO5 implements Serializable {
    private static final long serialVersionUID = 412972074040085564L;

    private String loadObjectType;// 装载对象类型
    private String loadObjectId;// 装载对象ID
    private String topLevelFlag;// 是否获取最上层容器

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    public String getTopLevelFlag() {
        return topLevelFlag;
    }

    public void setTopLevelFlag(String topLevelFlag) {
        this.topLevelFlag = topLevelFlag;
    }
}
