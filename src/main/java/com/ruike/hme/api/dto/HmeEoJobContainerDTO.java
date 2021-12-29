package com.ruike.hme.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/04/27 17:06
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobContainerDTO implements Serializable {
    private String workcellId;
    private String jobContainerId;
    private String containerId;
}
