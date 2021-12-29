package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 15:40:11
 **/
@Data
public class HmeNcDisposePlatformDTO6 implements Serializable {
    private static final long serialVersionUID = -2328385503891684137L;

    private String workcellId;

    private String workcellCode;

    private String workcellName;
}
