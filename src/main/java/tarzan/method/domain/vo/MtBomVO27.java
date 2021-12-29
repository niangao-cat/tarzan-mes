package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/9/18 11:44
 * @Description:
 */
public class MtBomVO27 extends MtBomVO23 implements Serializable {
    private static final long serialVersionUID = 4529451192000170121L;

    @ApiModelProperty("装配清单Name+Type")
    private String bomNameType;

    public String getBomNameType() {
        return bomNameType;
    }

    public void setBomNameType(String bomNameType) {
        this.bomNameType = bomNameType;
    }
}
