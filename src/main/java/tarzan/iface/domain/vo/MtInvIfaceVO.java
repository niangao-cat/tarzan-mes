package tarzan.iface.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-01 11:27
 */
public class MtInvIfaceVO implements Serializable {
    private static final long serialVersionUID = -7195585132836971028L;
    @ApiModelProperty("物料编码")
    private String itemCode;
    @ApiModelProperty("语言")
    private String lang;
    @ApiModelProperty("描述")
    private String description;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
