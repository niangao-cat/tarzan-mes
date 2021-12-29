package com.ruike.itf.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/08/09 15:01
 */
@Data
public class ItfLightTaskIfaceVO2 implements Serializable {

    private static final long serialVersionUID = 9061170872926001481L;

    @JSONField(name = "TASK_NUM")
    private String TASK_NUM;

    @JSONField(name = "LOCATOR_CODE")
    private String LOCATOR_CODE;

    @JSONField(name = "TASK_TYPE")
    private String TASK_TYPE;

    @JSONField(name = "TASK_STATUS")
    private String TASK_STATUS;
}
