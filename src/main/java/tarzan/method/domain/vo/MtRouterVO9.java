package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * routerCut-工艺路线裁剪 传入参数VO
 * 
 * @author benjamin
 * @date 2019-07-03 09:43
 */
public class MtRouterVO9 implements Serializable {
    private static final long serialVersionUID = 5358586555121057823L;

    /**
     * 原工艺了路线
     */
    private String sourceRouterId;
    /**
     * 新工艺路线名称
     */
    private String routerName;
    /**
     * 新工艺路线站点
     */
    private List<String> siteIds;
    /**
     * 新工艺路线类型
     */
    private String routerType;
    /**
     * 新工艺路线版本
     */
    private String revision;
    /**
     * 裁剪起始步骤
     */
    private String cutFromRouterStepId;
    /**
     * 裁剪终止步骤
     */
    private String cutToRouterStepId;
    /**
     * 返回步骤类型
     */
    private String returnType;
    /**
     * 返回步骤指定工艺
     */
    private String returnOperationId;
    /**
     * 返回步骤指定步骤识别码
     */
    private String returnStepName;

    public String getSourceRouterId() {
        return sourceRouterId;
    }

    public void setSourceRouterId(String sourceRouterId) {
        this.sourceRouterId = sourceRouterId;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public List<String> getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(List<String> siteIds) {
        this.siteIds = siteIds;
    }

    public String getRouterType() {
        return routerType;
    }

    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getCutFromRouterStepId() {
        return cutFromRouterStepId;
    }

    public void setCutFromRouterStepId(String cutFromRouterStepId) {
        this.cutFromRouterStepId = cutFromRouterStepId;
    }

    public String getCutToRouterStepId() {
        return cutToRouterStepId;
    }

    public void setCutToRouterStepId(String cutToRouterStepId) {
        this.cutToRouterStepId = cutToRouterStepId;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnOperationId() {
        return returnOperationId;
    }

    public void setReturnOperationId(String returnOperationId) {
        this.returnOperationId = returnOperationId;
    }

    public String getReturnStepName() {
        return returnStepName;
    }

    public void setReturnStepName(String returnStepName) {
        this.returnStepName = returnStepName;
    }
}
