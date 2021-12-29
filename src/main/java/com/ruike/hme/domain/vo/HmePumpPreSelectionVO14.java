package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * HmePumpPreSelectionVO14
 *
 * @author: chaonan.hu@hand-china.com 2021/09/06 13:53:11
 **/
@Data
public class HmePumpPreSelectionVO14 implements Serializable {
    private static final long serialVersionUID = -3530739822373788160L;

    private String bomId;

    private List<String> materialId;
}
