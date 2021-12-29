package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 新建页面保存时传入DTO
 * @Author tong.li
 * @Date 2020/5/14 19:24
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformCreatePageSaveDTO2 implements Serializable {
    private static final long serialVersionUID = -6384427804884638936L;

    @ApiModelProperty(value = "需要保存的信息List")
    List<QmsIqcCheckPlatformCreatePageSaveDTO> saveData;
}
