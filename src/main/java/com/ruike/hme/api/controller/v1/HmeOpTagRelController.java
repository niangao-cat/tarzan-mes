package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmeOpTagRel;
import com.ruike.hme.domain.repository.HmeOpTagRelRepository;
import com.ruike.hme.domain.vo.HmeOpTagRelVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 工艺数据项关系表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-12-24 15:43:25
 */
@RestController("hmeOpTagRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-op-tag-rels")
public class HmeOpTagRelController extends BaseController {

    @Autowired
    private HmeOpTagRelRepository hmeOpTagRelRepository;

    @ApiOperation(value = "工艺数据项关系列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{operationId}")
    public ResponseEntity<Page<HmeOpTagRelVO2>> hmeOpTagRelQuery(@PathVariable("organizationId") Long tenantId, @PathVariable("operationId") String operationId,
                                                                 @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeOpTagRelRepository.hmeOpTagRelQuery(tenantId, operationId, pageRequest));
    }

    @ApiOperation(value = "工艺数据项关系表 保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save-op-tag-rel"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeOpTagRel>> saveOpTagRel(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody List<HmeOpTagRel> hmeOpTagRelList) {
        return Results.success(hmeOpTagRelRepository.saveOpTagRel(tenantId, hmeOpTagRelList));
    }

    @ApiOperation(value = "删除工艺数据项关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping(value = "/remove-rel")
    public ResponseEntity<?> removeRel(@RequestBody List<HmeOpTagRel> hmeOpTagRelList) {
        hmeOpTagRelRepository.batchDeleteByPrimaryKey(hmeOpTagRelList);
        return Results.success();
    }
}
