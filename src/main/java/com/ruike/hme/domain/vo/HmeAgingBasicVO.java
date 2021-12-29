package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeAgingBasic;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 老化基础数据 返回
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/02 15:57
 */

public class HmeAgingBasicVO extends HmeAgingBasic implements Serializable {


    private static final long serialVersionUID = -4783071581779451482L;

    @ApiModelProperty(value = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    private String materialName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

}
