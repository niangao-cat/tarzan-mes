package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeNameplatePrintRelHeaderDTO;
import com.ruike.hme.app.service.HmeNameplatePrintRelHeaderService;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelHeaderAndLineVO;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelHeaderVO;
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

/**
 * 铭牌打印内部识别码对应关系头表 管理 API
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:11
 */
@RestController("hmeNameplatePrintRelHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-nameplate-print-rel-headers")
public class HmeNameplatePrintRelHeaderController extends BaseController {

    private final HmeNameplatePrintRelHeaderService hmeNameplatePrintRelHeaderService;

    @Autowired
    public HmeNameplatePrintRelHeaderController(HmeNameplatePrintRelHeaderService hmeNameplatePrintRelHeaderService) {
        this.hmeNameplatePrintRelHeaderService = hmeNameplatePrintRelHeaderService;
    }

    @ApiOperation(value = "铭牌打印内部识别码对应关系头表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-print-header")
    public ResponseEntity<Page<HmeNameplatePrintRelHeaderVO>> queryPrintRelHeader(@PathVariable("organizationId") Long organizationId, HmeNameplatePrintRelHeaderDTO hmeNameplatePrintRelHeaderDTO,
                                                                                  PageRequest pageRequest) {
        return Results.success(hmeNameplatePrintRelHeaderService.queryPrintRelHeader(organizationId, hmeNameplatePrintRelHeaderDTO, pageRequest));
    }

    @ApiOperation(value = "铭牌打印内部识别码对应关系历史表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-print-header-line")
    public ResponseEntity<Page<HmeNameplatePrintRelHeaderAndLineVO>> queryPrintRelHeaderAndLine(@PathVariable("organizationId") Long organizationId, String nameplateHeaderId,
                                                                                                PageRequest pageRequest) {
        return Results.success(hmeNameplatePrintRelHeaderService.queryPrintRelHeaderAndLine(organizationId, nameplateHeaderId, pageRequest));
    }

    @ApiOperation(value = "创建铭牌打印内部识别码对应关系头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-print-re-header")
    public ResponseEntity<HmeNameplatePrintRelHeaderVO> savePrintRelHeader(@PathVariable("organizationId") Long organizationId, @RequestBody HmeNameplatePrintRelHeaderVO hmeNameplatePrintRelHeaderVO) {
        hmeNameplatePrintRelHeaderService.savePrintRelHeader(organizationId, hmeNameplatePrintRelHeaderVO);
        return Results.success(hmeNameplatePrintRelHeaderVO);
    }


}
