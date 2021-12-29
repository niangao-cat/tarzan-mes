package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ItfDeleteTableDataVO implements Serializable {

    private static final long serialVersionUID = 791726875943560615L;

    public ItfDeleteTableDataVO(String keyIdName , List<String> keyIdList){
        this.keyIdName = keyIdName;
        this.keyIdList = keyIdList;
    }

    @ApiModelProperty("主键字段名")
    private String keyIdName;

    @ApiModelProperty("主键ID值集合")
    private List<String> keyIdList;
}
