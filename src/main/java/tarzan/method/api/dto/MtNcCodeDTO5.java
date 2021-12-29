package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtNcCodeDTO5 implements Serializable {
    private static final long serialVersionUID = -4389214485277702880L;

    @ApiModelProperty(value = "不良代码信息")
    private MtNcCodeDTO4 mtNcCode;
    @ApiModelProperty(value = "次级代码列表")
    private List<MtNcSecondaryCodeDTO> mtNcSecondaryCodeList;
    @ApiModelProperty(value = "工艺列表")
    private List<MtNcValidOperDTO> mtNcValidOperList;

    public MtNcCodeDTO4 getMtNcCode() {
        return mtNcCode;
    }

    public void setMtNcCode(MtNcCodeDTO4 mtNcCode) {
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
