package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/16 10:02
 */
@Data
public class HmeSnBindEoVO2 implements Serializable {

    private static final long serialVersionUID = 1927462071730482721L;

    private String siteId;

    private String siteCode;

    private String proLineCode;

    private String proItemType;

    private String eoId;

    private String proLineId;
}
