package tarzan.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Leeloing
 * @date 2020/10/31 14:12
 */
public class MtWorkOrderVO68 implements Serializable {
    private static final long serialVersionUID = 4312084775873873586L;

    @ApiModelProperty("生产指令列表")
    private MtWorkOrderVO27 woInfo;

    @ApiModelProperty("装配清单组件ID")
    private List<MtWorkOrderVO69> bomComponentList;

    private Date date;

    public MtWorkOrderVO27 getWoInfo() {
        return woInfo;
    }

    public void setWoInfo(MtWorkOrderVO27 woInfo) {
        this.woInfo = woInfo;
    }

    public List<MtWorkOrderVO69> getBomComponentList() {
        return bomComponentList;
    }

    public void setBomComponentList(List<MtWorkOrderVO69> bomComponentList) {
        this.bomComponentList = bomComponentList;
    }

}
