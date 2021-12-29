package com.ruike.hme.domain.vo;

import java.io.Serializable;

import com.ruike.hme.domain.entity.HmeExceptionGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * HmeExceptionGroupVO
 *
 * @author liyuan.lv@hand-china.com 2020/05/11 16:05
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeExceptionGroupVO implements Serializable {

    private static final long serialVersionUID = 3016590607495612598L;
    private String exceptionGroupId;
    private String exceptionGroupAssignId;
    private String exceptionId;
}
