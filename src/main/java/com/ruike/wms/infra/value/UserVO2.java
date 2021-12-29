package com.ruike.wms.infra.value;

import lombok.*;

import java.util.List;

/**
 * @Classname UserVO2
 * @Description 用户VO2
 * @Date 2019/12/23 9:14
 * @Author wei.zheng
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVO2 {
    private Long gender;
    private String companyName;
    private String email;
    private String phone;
    private String loginName;
    private List<RoleVO> memberRoleList;
    private Long organizationId;
    private String password;
    private String realName;
}
