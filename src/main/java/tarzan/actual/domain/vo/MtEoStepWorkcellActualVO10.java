package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoStepWorkcellActualVO10
 * @description
 * @date 2019年12月04日 14:04
 */
public class MtEoStepWorkcellActualVO10 implements Serializable {
    private static final long serialVersionUID = -172526087930792449L;

    @ApiModelProperty(value = "执行作业ID")
    private String eoId;
    @ApiModelProperty(value = "执行作业编码")
    private String eoNum;
    @ApiModelProperty(value = "来源执行作业ID")
    private List<String> sourceEoId = new ArrayList<>();
    @ApiModelProperty(value = "来源执行作业编码")
    private String sourceEoNum;
    @ApiModelProperty(value = "执行作业工艺路线实绩")
    private String eoRouterActualId;
    @ApiModelProperty(value = "顺序")
    private Long sequence;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "工艺路线")
    private String routerId;
    // 增加参数
    @ApiModelProperty(value = "工艺路线短描述")
    private String router;
    @ApiModelProperty(value = "工艺路线长描述")
    private String routerName;
    @ApiModelProperty(value = "加工数量")
    private Double qty;
    @ApiModelProperty(value = "分之工艺路线标识")
    private String subRouterFlag;
    @ApiModelProperty(value = "来源步骤实绩")
    private String sourceEoStepActualId;
    @ApiModelProperty(value = "完成数量")
    private Double completedQty;

    // 初始化集合，避免空指针异常
    private List<MtEoStepActualVO33> eoStepActualList = new ArrayList<>();

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public List<String> getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(List<String> sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getSubRouterFlag() {
        return subRouterFlag;
    }

    public void setSubRouterFlag(String subRouterFlag) {
        this.subRouterFlag = subRouterFlag;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public List<MtEoStepActualVO33> getEoStepActualList() {
        return eoStepActualList;
    }

    public void setEoStepActualList(List<MtEoStepActualVO33> eoStepActualList) {
        this.eoStepActualList = eoStepActualList;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getSourceEoNum() {
        return sourceEoNum;
    }

    public void setSourceEoNum(String sourceEoNum) {
        this.sourceEoNum = sourceEoNum;
    }
}
