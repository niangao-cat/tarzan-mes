package com.ruike.hme.api.controller.v1;


import com.ruike.hme.api.dto.HmeCosCheckBarcodesDTO;
import com.ruike.hme.app.service.HmeCosCheckBarcodesService;
import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import com.ruike.hme.domain.repository.HmeCosCheckBarcodesRepository;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO;
import com.ruike.hme.domain.vo.HmeEquipTagGroupReturnVO;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO;
import io.choerodon.core.base.BaseController;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;

/**
 * cos目检条码表
 *
 * @author li.zhang 2021/01/19 11:04
 */
@RestController("HmeCosCheckBarcodesController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-checkBarcodes")
@Api(tags = SwaggerApiConfig.HME_COS_CHECK_BARCODES)
public class HmeCosCheckBarcodesController extends BaseController {

    @Autowired
    HmeCosCheckBarcodesRepository hmeCosCheckBarcodesRepository;
    @Autowired
    HmeCosCheckBarcodesService hmeCosCheckBarcodesService;

    /**
     * @Description cos目检条码表查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return HmeCosCheckBarcodesVO
     * @author li.zhang
     */
    @ApiOperation(value = "cos目检条码表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosCheckBarcodesVO>> list(@PathVariable("organizationId") String tenantId, HmeCosCheckBarcodesDTO dto,
                                                            @ApiIgnore PageRequest pageRequest) {
        Page<HmeCosCheckBarcodesVO> list = hmeCosCheckBarcodesRepository.selectCheckBarcodes(tenantId,dto,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description cos目检条码表导出
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return HmeCosCheckBarcodesVO
     * @author li.zhang
     */
    @ApiOperation(value = "cos目检条码表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeCosCheckBarcodesVO.class)
    public ResponseEntity<Page<HmeCosCheckBarcodesVO>> export(@PathVariable("organizationId") String tenantId,
                                                              HmeCosCheckBarcodesDTO dto,
                                                              ExportParam exportParam,
                                                              HttpServletResponse response,
                                                            @ApiIgnore PageRequest pageRequest) {
        Page<HmeCosCheckBarcodesVO> list = hmeCosCheckBarcodesService.exportCheckBarcodes(tenantId,dto,pageRequest,exportParam);
        return Results.success(list);
    }
}
