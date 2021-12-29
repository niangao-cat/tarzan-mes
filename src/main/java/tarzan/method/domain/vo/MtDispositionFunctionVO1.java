package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtDispositionFunctionVO1 implements Serializable {

    private static final long serialVersionUID = -5190989996027107453L;
    private String dispositionFunction;// 处置方法
    private String routerId;// 工艺路线ID
    private String siteId;// 站点ID
    private String description;// 描述
    private String functionType;// 处置方法类型
    private String dispositionFunctionId;// 处置方法ID
    private String siteCode;// siteCode
    private String siteName;// 站点名称

    public String getDispositionFunction() {
        return dispositionFunction;
    }

    public void setDispositionFunction(String dispositionFunction) {
        this.dispositionFunction = dispositionFunction;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getDispositionFunctionId() {
        return dispositionFunctionId;
    }

    public void setDispositionFunctionId(String dispositionFunctionId) {
        this.dispositionFunctionId = dispositionFunctionId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
