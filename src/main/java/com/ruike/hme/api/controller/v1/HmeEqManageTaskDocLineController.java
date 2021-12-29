package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLine;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * 设备管理任务单行表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:10
 */
@RestController("hmeEqManageTaskDocLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-eq-manage-task-doc-line")
@Api(tags = SwaggerApiConfig.HME_EQ_MANAGE_TASK_DOC_LINE)
public class HmeEqManageTaskDocLineController extends BaseController {

    @Autowired
    private HmeEqManageTaskDocLineRepository hmeEqManageTaskDocLineRepository;

    @ApiOperation(value = "设备管理任务单行表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeEqManageTaskDocLine>> list(HmeEqManageTaskDocLine hmeEqManageTaskDocLine, @ApiIgnore @SortDefault(value = HmeEqManageTaskDocLine.FIELD_TASK_DOC_LINE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeEqManageTaskDocLine> list = hmeEqManageTaskDocLineRepository.pageAndSort(pageRequest, hmeEqManageTaskDocLine);
        return Results.success(list);
    }

    @ApiOperation(value = "设备管理任务单行表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{taskDocLineId}")
    public ResponseEntity<HmeEqManageTaskDocLine> detail(@PathVariable Long taskDocLineId) {
        HmeEqManageTaskDocLine hmeEqManageTaskDocLine = hmeEqManageTaskDocLineRepository.selectByPrimaryKey(taskDocLineId);
        return Results.success(hmeEqManageTaskDocLine);
    }

    @ApiOperation(value = "创建设备管理任务单行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeEqManageTaskDocLine> create(@RequestBody HmeEqManageTaskDocLine hmeEqManageTaskDocLine) {
        validObject(hmeEqManageTaskDocLine);
        hmeEqManageTaskDocLineRepository.insertSelective(hmeEqManageTaskDocLine);
        return Results.success(hmeEqManageTaskDocLine);
    }

    @ApiOperation(value = "修改设备管理任务单行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeEqManageTaskDocLine> update(@RequestBody HmeEqManageTaskDocLine hmeEqManageTaskDocLine) {
        SecurityTokenHelper.validToken(hmeEqManageTaskDocLine);
        hmeEqManageTaskDocLineRepository.updateByPrimaryKeySelective(hmeEqManageTaskDocLine);
        return Results.success(hmeEqManageTaskDocLine);
    }

    @ApiOperation(value = "删除设备管理任务单行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeEqManageTaskDocLine hmeEqManageTaskDocLine) {
        SecurityTokenHelper.validToken(hmeEqManageTaskDocLine);
        hmeEqManageTaskDocLineRepository.deleteByPrimaryKey(hmeEqManageTaskDocLine);
        return Results.success();
    }

}
