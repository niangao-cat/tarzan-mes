package com.ruike.wms.infra.value;

import lombok.*;

/**
 * @Classname RoleVO
 * @Description 角色VO
 * @Date 2019/12/23 9:17
 * @Author wei.zheng
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleVO {
    private Long roleId;
    private Long tenantId;
    private String sourceType;
    private String memberType;
    private String assignLevel;
    private Long assignLevelValue;
    private Long sourceId;
}
