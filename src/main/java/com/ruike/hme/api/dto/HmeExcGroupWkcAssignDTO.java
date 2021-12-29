package com.ruike.hme.api.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * HmeExcGroupWkcAssignDTO
 *
 * @author liyuan.lv@hand-china.com 2020/05/09 11:17
 */
@Data
public class HmeExcGroupWkcAssignDTO implements Serializable {
    private static final long serialVersionUID = -4658086541650072866L;
    private String excGroupWkcAssignId;
    private String exceptionGroupId;
}
