package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 员工信息
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 17:00
 */
@ApiModel("员工信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEmployeeInfoDTO {

    private String employeeId;

    private String employeeNum;

    private String name;

    private String nameEn;

    private String tenantId;

}
