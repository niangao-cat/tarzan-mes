package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * HmePumpPreSelectionVO9
 *
 * @author: chaonan.hu@hand-china.com 2021/09/01 17:19:12
 **/
@Data
public class HmePumpPreSelectionVO9 implements Serializable {
    private static final long serialVersionUID = 2226315333796370099L;

    private String jobId;

    private String tagId;

    private BigDecimal result;
}
