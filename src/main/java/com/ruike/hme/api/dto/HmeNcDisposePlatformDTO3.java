package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 14:57:18
 **/
@Data
public class HmeNcDisposePlatformDTO3 implements Serializable {
    private static final long serialVersionUID = -3836425026003086144L;

    private String processId;

    private String processCode;

    private String processName;
}
