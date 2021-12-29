package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtNcCodeDTO3 implements Serializable {
    private static final long serialVersionUID = -9212749039917088882L;

    @ApiModelProperty(value = "不良代码信息")
    private MtNcCodeDTO2 mtNcCode;
    @ApiModelProperty(value = "次级代码列表")
    private List<MtNcSecondaryCodeDTO> mtNcSecondaryCodeList;
    @ApiModelProperty(value = "工艺列表")
    private List<MtNcValidOperDTO> mtNcValidOperList;

    public MtNcCodeDTO2 getMtNcCode() {
        return mtNcCode;
    }

    public void setMtNcCode(MtNcCodeDTO2 mtNcCode) {
        this.mtNcCode = mtNcCode;
    }

    public List<MtNcSecondaryCodeDTO> getMtNcSecondaryCodeList() {
        return mtNcSecondaryCodeList;
    }

    public void setMtNcSecondaryCodeList(List<MtNcSecondaryCodeDTO> mtNcSecondaryCodeList) {
        this.mtNcSecondaryCodeList = mtNcSecondaryCodeList;
    }

    public List<MtNcValidOperDTO> getMtNcValidOperList() {
        return mtNcValidOperList;
    }

    public void setMtNcValidOperList(List<MtNcValidOperDTO> mtNcValidOperList) {
        this.mtNcValidOperList = mtNcValidOperList;
    }
}
