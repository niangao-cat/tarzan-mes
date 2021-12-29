package io.tarzan.common.infra.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import io.tarzan.common.domain.vo.MtRoleVO;
import io.tarzan.common.infra.feign.failback.MtMemberRoleServiceImpl;

@FeignClient(value = "${hzero.service.iam:hzero-iam}", fallback = MtMemberRoleServiceImpl.class)
public interface MtMemberRoleService {
    @GetMapping({"/hzero/v1/member-roles/self-roles"})
    ResponseEntity<List<MtRoleVO>> listSelfRoles();
}
