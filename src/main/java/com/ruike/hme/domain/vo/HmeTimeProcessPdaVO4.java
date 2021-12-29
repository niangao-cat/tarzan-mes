package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeTimeProcessPdaVO4
 *
 * @author chaonan.hu@hand-china.com 2020-08-20 11:09:45
 **/
@Data
public class HmeTimeProcessPdaVO4 implements Serializable {
    private static final long serialVersionUID = -6802631713243630369L;

    private Boolean hide;

    private String equipmentCode;

    private String equipmentName;

}
