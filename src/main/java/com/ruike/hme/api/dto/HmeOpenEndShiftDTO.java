package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-07 10:43:21
 **/
@Data
public class HmeOpenEndShiftDTO implements Serializable {
    private static final long serialVersionUID = -3401924760174590222L;

    @ApiModelProperty(value = "默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工段Id", required = true)
    private String lineWorkcellId;

    @ApiModelProperty(value = "日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
