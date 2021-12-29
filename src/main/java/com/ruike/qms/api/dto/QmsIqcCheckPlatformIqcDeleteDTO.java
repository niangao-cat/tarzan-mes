package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author tong.li
 * @Date 2020/5/21 16:17
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformIqcDeleteDTO implements Serializable {
    private static final long serialVersionUID = -9161724541278982951L;

    @ApiModelProperty(value = "行ID的list")
    List<String> iqcLineIdList;
}
