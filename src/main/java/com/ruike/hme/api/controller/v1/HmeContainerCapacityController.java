package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmeContainerCapacity;
import com.ruike.hme.domain.repository.HmeContainerCapacityRepository;
import com.ruike.hme.domain.vo.HmeContainerCapacityVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * 容器容量表 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-10 15:08:58
 */
@RestController("hmeContainerCapacityController.v1")
@RequestMapping("/v1/{organizationId}/hme-container-capacitys")
@Api(tags = SwaggerApiConfig.HME_CONTAINER_CAPACITY)
public class HmeContainerCapacityController extends BaseController {

    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;

    @ApiOperation(value = "容器容量表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-container-capacity")
    public ResponseEntity<Page<HmeContainerCapacityVO>> containerCapacityQuery(@PathVariable("organizationId") Long tenantId, String containerTypeId, @ApiIgnore PageRequest pageRequest) {
        Page<HmeContainerCapacityVO> list = hmeContainerCapacityRepository.containerCapacityQuery(tenantId, containerTypeId, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "创建&修改容器容量表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/create-container-capacity")
    public ResponseEntity<HmeContainerCapacity> createContainerCapacity(@PathVariable("organizationId") Long tenantId, @RequestBody HmeContainerCapacity hmeContainerCapacity) {
        return Results.success(hmeContainerCapacityRepository.createContainerCapacity(tenantId, hmeContainerCapacity));
    }


    @ApiOperation(value = "删除容器容量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/delete-container-capacity")
    public ResponseEntity<?> deleteContainerCapacity(@PathVariable("organizationId") Long tenantId, @RequestBody HmeContainerCapacity hmeContainerCapacity) {
        hmeContainerCapacityRepository.deleteContainerCapacity(tenantId, hmeContainerCapacity);
        return Results.success();
    }

}
