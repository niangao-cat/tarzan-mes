package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsIqcCalSumDTO;
import com.ruike.qms.api.dto.QmsIqcExamineBoardDTO;
import com.ruike.qms.app.service.QmsIqcExamineBoardService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * Iqc检验看板 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
@RestController("qmsIqcExamineBoardController.v1")
@RequestMapping("/v1/{organizationId}/qms-iqc-examine-boards")
@Api(tags = SwaggerApiConfig.QMS_IQC_EXAMINE_BOARDS)
public class QmsIqcExamineBoardController extends BaseController {

    @Autowired
    private QmsIqcExamineBoardService qmsIqcExamineBoardService;

    @ApiOperation(value = "Iqc检验看板查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/iqc/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsIqcExamineBoardDTO>> selectIqcExamineBoardForUi(@ApiIgnore PageRequest pageRequest,
                                                                                  @PathVariable(value = "organizationId") Long tenantId) {
        Page<QmsIqcExamineBoardDTO> list = qmsIqcExamineBoardService.selectIqcExamineBoardForUi(tenantId, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "30天检验量查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/day/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<QmsIqcCalSumDTO>> selectIqcDayForUi(@PathVariable(value = "organizationId") Long tenantId) {
        List<QmsIqcCalSumDTO> list = qmsIqcExamineBoardService.selectIqcDayForUi(tenantId);
        return Results.success(list);
    }

    @ApiOperation(value = "全年检验量查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/month/list/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<QmsIqcCalSumDTO>> selectIqcMonthForUi(@PathVariable(value = "organizationId") Long tenantId) {
        List<QmsIqcCalSumDTO> list = qmsIqcExamineBoardService.selectIqcMonthForUi(tenantId);
        return Results.success(list);
    }

}
