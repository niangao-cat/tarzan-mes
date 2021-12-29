package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/6 15:08
 */
@Data
public class HmeTagCheckVO implements Serializable {

    private static final long serialVersionUID = -162626539445144813L;

    @ApiModelProperty("进站SN")
    private String snNum;
    @ApiModelProperty("工序")
    private String processId;
    @ApiModelProperty("类型")
    private String ruleType;
    @ApiModelProperty("进站SN集合")
    private List<String> snNumList;

    public void initParam() {
        snNumList = StringUtils.isNotBlank(snNum) ? Arrays.asList(StringUtils.split(snNum, ",")) : null;
    }
}
