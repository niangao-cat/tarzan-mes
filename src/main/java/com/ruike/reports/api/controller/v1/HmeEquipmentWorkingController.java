package com.ruike.reports.api.controller.v1;

import com.ruike.reports.api.dto.HmeEquipmentWorkingDTO;
import com.ruike.reports.app.service.HmeEquipmentWorkingService;
import com.ruike.reports.domain.repository.HmeEquipmentWorkingRepository;
import com.ruike.reports.domain.vo.HmeEquipmentWorkingVO;
import com.ruike.reports.domain.vo.HmeEquipmentWorkingVO4;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * 设备报表
 *
 * @author li.zhang 2021/01/14 11:04
 */
@RestController("HmeEquipmentWorkingController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-working")
@Api(tags = SwaggerApiConfig.HME_EQUIPMENT_WORKING)
public class HmeEquipmentWorkingController extends BaseController {

    @Autowired
    HmeEquipmentWorkingService hmeEquipmentWorkingService;
    @Autowired
    HmeEquipmentWorkingRepository hmeEquipmentWorkingRepository;

    @ApiOperation(value = "设备运行情况明细报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<HmeEquipmentWorkingVO4> list(HmeEquipmentWorkingDTO dto,
                                                       @ApiIgnore PageRequest pageRequest,
                                                       @PathVariable("organizationId") String tenantId) throws ParseException {
        return Results.success(hmeEquipmentWorkingRepository.selectHmeEquipmentWorking(pageRequest, tenantId, dto));
    }


    @ApiOperation(value = "设备运行情况明细报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    public  ResponseEntity<?> export(@PathVariable("organizationId") String tenantId,
                                                              HmeEquipmentWorkingDTO dto,
                                                              HttpServletResponse response
                                                             ){
        try {
            hmeEquipmentWorkingRepository.export(tenantId, dto, response);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        return Results.success();
    }
}
