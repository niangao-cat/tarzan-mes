package com.ruike.qms.domain.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderVO10
 * @description: 巡检平台海马汇版事业部LOV返回对象
 * @author: chaonan.hu@hand-china.com 2020-10-23 09:57
 **/
@Data
public class QmsPqcHeaderVO10 implements Serializable {
    private static final long serialVersionUID = 1063161202032118127L;

    private String areaId;

    private String areaCode;

    private String description;

    private String areaCategory;

    private Boolean hide = false;

    private String queryInfo;
}
