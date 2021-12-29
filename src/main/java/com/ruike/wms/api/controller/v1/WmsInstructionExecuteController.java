package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsInstructionExecuteDTO;
import com.ruike.wms.app.service.WmsInstructionExecuteService;
import com.ruike.wms.domain.vo.WmsInstructionExecuteVO;
import groovy.util.logging.Slf4j;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 单据执行统计报表
 *
 * @author li.zhang 2021/09/09 17:50
 */
@RestController("wmsInstructionExecuteController.v1")
@RequestMapping("/v1/{organizationId}/wms-instruction-execute")
@Api(tags = SwaggerApiConfig.WMS_INSTRUCTION_EXECUTE)
public class WmsInstructionExecuteController extends BaseController {

    @Autowired
    private WmsInstructionExecuteService wmsInstructionExecuteService;

    @ApiOperation(value = "单据执行统计报表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<WmsInstructionExecuteVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                   WmsInstructionExecuteDTO dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(wmsInstructionExecuteService.queryList(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "单据执行统计报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/export", produces = "application/json;charset=UTF-8")
    @ExcelExport(WmsInstructionExecuteVO.class)
    public ResponseEntity<List<WmsInstructionExecuteVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                WmsInstructionExecuteDTO dto,
                                                                ExportParam exportParam,
                                                                HttpServletResponse response) {
        dto.initParam();
        return Results.success(wmsInstructionExecuteService.export(tenantId,dto,exportParam));
    }
}
