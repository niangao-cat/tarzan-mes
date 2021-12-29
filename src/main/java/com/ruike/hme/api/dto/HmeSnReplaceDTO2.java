package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

import java.io.Serializable;

@Data
public class HmeSnReplaceDTO2 implements Serializable {

    private static final long serialVersionUID = 8375535239215165031L;

    private List<HmeSnReplaceDTO> dtoList;

    @ApiModelProperty(value = "用户", required = true)
    private String userName;


}
