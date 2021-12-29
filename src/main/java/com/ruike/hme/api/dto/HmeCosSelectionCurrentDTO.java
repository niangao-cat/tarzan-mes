package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HmeCosSelectionCurrentDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/11/24 10:04
 **/
@Data
public class HmeCosSelectionCurrentDTO implements Serializable {
    private static final long serialVersionUID = -3408596955081459136L;

    @ApiModelProperty("COS类型，以逗号分隔的字符串")
    private String cosType;

    @ApiModelProperty(value = "COS类型集合，后端自用", hidden = true)
    private List<String> cosTypeList;

    public void initParam() {
        this.cosTypeList = StringUtils.isNotBlank(this.cosType) ? Arrays.asList(StringUtils.split(this.cosType, ",")) : new ArrayList<>();
    }
}
