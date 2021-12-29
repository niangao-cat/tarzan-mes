package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class HmeFifthAreaKanbanVO2 implements Serializable {
    private static final long serialVersionUID = 3313321324490846832L;

    @ApiModelProperty("EOID")
    private String eoId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "处理方式")
    private String processMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;};
        if (o == null || getClass() != o.getClass()) {return false;};
        HmeFifthAreaKanbanVO2 a = (HmeFifthAreaKanbanVO2) o;
        return Objects.equals(eoId, a.eoId) &&
                Objects.equals(workcellId, a.workcellId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eoId, workcellId);
    }
}
