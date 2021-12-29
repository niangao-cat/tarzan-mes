package com.ruike.qms.api.controller.v1;

import com.ruike.qms.app.service.QmsIqcHeaderService;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.repository.QmsIqcHeaderRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * 质检单头表 管理 API
 *
 * @author tong.li05@hand-china.com 2020-04-28 19:39:00
 */
@RestController("qmsIqcHeaderController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-headers")
@Api(tags = SwaggerApiConfig.QMS_IQC_HEADERS)
@Slf4j
public class QmsIqcHeaderController extends BaseController {

    @Autowired
    private QmsIqcHeaderRepository iqcHeaderRepository;

    @Autowired
    private QmsIqcHeaderService iqcHeaderService;


    @ApiOperation(value = "质检单头表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<QmsIqcHeader>> list(QmsIqcHeader iqcHeader, @ApiIgnore @SortDefault(value = QmsIqcHeader.FIELD_IQC_HEADER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<QmsIqcHeader> list = iqcHeaderRepository.pageAndSort(pageRequest, iqcHeader);
        return Results.success(list);
    }

    @ApiOperation(value = "质检单头表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{iqcHeaderId}")
    public ResponseEntity<QmsIqcHeader> detail(@PathVariable Long iqcHeaderId) {
        QmsIqcHeader iqcHeader = iqcHeaderRepository.selectByPrimaryKey(iqcHeaderId);
        return Results.success(iqcHeader);
    }

    @ApiOperation(value = "质检单头表创建")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<QmsIqcHeader> create(@RequestBody QmsIqcHeader iqcHeader) {
        validObject(iqcHeader);
        iqcHeaderRepository.insertSelective(iqcHeader);
        return Results.success(iqcHeader);
    }

    @ApiOperation(value = "质检单头表修改")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<QmsIqcHeader> update(@RequestBody QmsIqcHeader iqcHeader) {
        SecurityTokenHelper.validToken(iqcHeader);
        iqcHeaderRepository.updateByPrimaryKeySelective(iqcHeader);
        return Results.success(iqcHeader);
    }

    @ApiOperation(value = "质检单头表删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody QmsIqcHeader iqcHeader) {
        SecurityTokenHelper.validToken(iqcHeader);
        iqcHeaderRepository.deleteByPrimaryKey(iqcHeader);
        return Results.success();
    }


    @ApiOperation(value = "质检单生成")
    @PostMapping(value = {"/create/iqc"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<Void> execute(@PathVariable("organizationId") Long tenantId, @RequestBody QmsIqcHeader iqcHeader) {
        log.info("<====QmsIqcHeaderController-execute:{},{} ", tenantId , iqcHeader);
        iqcHeaderRepository.createIqcBill(tenantId, iqcHeader);
        return Results.success();
    }

}
