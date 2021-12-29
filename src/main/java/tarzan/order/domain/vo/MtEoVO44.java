package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/26 15:01
 */
public class MtEoVO44 implements Serializable {
    private static final long serialVersionUID = -9108851882131776056L;
    @ApiModelProperty("事件")
    private String eventId;
    @ApiModelProperty("执行作业列表")
    private List<EoInfo> eoList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<EoInfo> getEoList() {
        return eoList;
    }

    public void setEoList(List<EoInfo> eoList) {
        this.eoList = eoList;
    }

    public static class EoInfo implements Serializable {
        private static final long serialVersionUID = 1110627200973015505L;
        @ApiModelProperty("执行作业")
        private String eoId;
        @ApiModelProperty("状态")
        private String status;

        public String getEoId() {
            return eoId;
        }

        public void setEoId(String eoId) {
            this.eoId = eoId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            EoInfo eoInfo = (EoInfo) o;
            return Objects.equals(getEoId(), eoInfo.getEoId()) && Objects.equals(getStatus(), eoInfo.getStatus());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getEoId(), getStatus());
        }
    }

}
