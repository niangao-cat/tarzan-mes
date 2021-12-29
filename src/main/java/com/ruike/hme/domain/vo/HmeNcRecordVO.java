package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeNcRecordVO
 * @Description HmeNcRecordVO
 * @Date 2020/11/12 18:47
 * @Author yuchao.wang
 */
@Data
public class HmeNcRecordVO implements Serializable {
    private static final long serialVersionUID = 4299048758210347811L;

    @ApiModelProperty("唯一标识，表ID，主键，供其他表做外键")
    private String ncRecordId;

    @ApiModelProperty(value = "工作单元")
    private String workcellId;

    @ApiModelProperty(value = "产生问题的源工作单元")
    private String rootCauseWorkcellId;

    @ApiModelProperty(value = "产生问题的源工序ID")
    private String rootCauseProcessId;

    @ApiModelProperty(value = "产生问题的源工序编码")
    private String rootCauseProcessCode;

    @ApiModelProperty(value = "产生问题的源工序名称")
    private String rootCauseProcessName;

    @ApiModelProperty(value = "当前工位对应的工序ID")
    private String currentProcessId;
}