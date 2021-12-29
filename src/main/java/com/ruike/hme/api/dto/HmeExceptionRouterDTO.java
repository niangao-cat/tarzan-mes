package com.ruike.hme.api.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * HmeExceptionRouterDTO
 *
 * @author liyuan.lv@hand-china.com 2020/05/09 11:17
 */
@Data
public class HmeExceptionRouterDTO implements Serializable {
    private static final long serialVersionUID = 367501259761586240L;
    private String exceptionId;
    private String exceptionRouterId;
}
