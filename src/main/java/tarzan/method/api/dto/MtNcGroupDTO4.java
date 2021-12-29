package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtNcGroupDTO4 implements Serializable {
    private static final long serialVersionUID = -1332459780139867617L;

    @ApiModelProperty(value = "不良代码组信息")
    private MtNcGroupDTO3 mtNcGroup;
    @ApiModelProperty(value = "次级代码列表")
    private List<MtNcSecondaryCodeDTO> mtNcSecondaryCodeList;
    @ApiModelProperty(value = "工艺列表")
    private List<MtNcValidOperDTO> mtNcValidOperList;

    public MtNcGroupDTO3 getMtNcGroup() {
        return mtNcGroup;
    }

    public void setMtNcGroup(MtNcGroupDTO3 mtNcGroup) {
        this.mtNcGroup = mtNcGroup;
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
