package tarzan.general.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtTagGroupDTO4 implements Serializable {
    private static final long serialVersionUID = -1381835457831072814L;

    @ApiModelProperty("数据收集组信息")
    private MtTagGroupDTO3 mtTagGroupDTO;
    @ApiModelProperty("数据收集组关联对象信息")
    private MtTagGroupObjectDTO2 mtTagGroupObjectDTO;

    public MtTagGroupDTO3 getMtTagGroupDTO() {
        return mtTagGroupDTO;
    }

    public void setMtTagGroupDTO(MtTagGroupDTO3 mtTagGroupDTO) {
        this.mtTagGroupDTO = mtTagGroupDTO;
    }

    public MtTagGroupObjectDTO2 getMtTagGroupObjectDTO() {
        return mtTagGroupObjectDTO;
    }

    public void setMtTagGroupObjectDTO(MtTagGroupObjectDTO2 mtTagGroupObjectDTO) {
        this.mtTagGroupObjectDTO = mtTagGroupObjectDTO;
    }
}
