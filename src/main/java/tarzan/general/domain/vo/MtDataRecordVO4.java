package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * distributionGroupLimitTagGroupQuery-根据收集组及收集参数获取应分发组清单 返回参数类
 * 
 * @author benjamin
 * @date 2019-07-02 17:43
 */
public class MtDataRecordVO4 implements Serializable {
    private static final long serialVersionUID = 6984233965175658961L;
    /**
     * 数据收集组Id集合
     */
    private List<String> tagGroupIdList;
    /**
     * 物料Id
     */
    private String materialId;
    /**
     * 工艺路线步骤ID
     */
    private String routerStepId;
    /**
     * 步骤实绩唯一标识
     */
    private String eoStepActualId;
    /**
     * 工艺Id
     */
    private String operationId;
    /**
     * 执行作业Id
     */
    private String eoId;
    /**
     * 不良代码Id
     */
    private String ncCodeId;
    /**
     * BOM组件Id
     */
    private String bomComponentId;
    /**
     * 装配实绩唯一标识
     */
    private String assembleConfirmId;

    public List<String> getTagGroupIdList() {
        return tagGroupIdList;
    }

    public void setTagGroupIdList(List<String> tagGroupIdList) {
        this.tagGroupIdList = tagGroupIdList;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getAssembleConfirmId() {
        return assembleConfirmId;
    }

    public void setAssembleConfirmId(String assembleConfirmId) {
        this.assembleConfirmId = assembleConfirmId;
    }
}
