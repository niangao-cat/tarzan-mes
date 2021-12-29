package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeFreezeDocQueryDTO;
import com.ruike.hme.api.dto.HmeFreezeDocSaveCommandDTO;
import com.ruike.hme.app.export.HmeFreezeDocExportService;
import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.domain.repository.HmeFreezeDocRepository;
import com.ruike.hme.domain.service.HmeFreezeDocDomainService;
import com.ruike.hme.domain.vo.HmeFreezeDocCreateSnVO;
import com.ruike.hme.domain.vo.HmeFreezeDocVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 条码冻结单 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:42
 */
@RestController("hmeFreezeDocController.v1")
@RequestMapping("/v1/{organizationId}/hme-freeze-docs")
@Api(tags = SwaggerApiConfig.HME_FREEZE_DOC)
public class HmeFreezeDocController extends BaseController {

    private final HmeFreezeDocRepository hmeFreezeDocRepository;
    private final HmeFreezeDocDomainService hmeFreezeDocDomainService;
    private final HmeFreezeDocExportService exportService;

    public HmeFreezeDocController(HmeFreezeDocRepository hmeFreezeDocRepository, HmeFreezeDocDomainService hmeFreezeDocDomainService, HmeFreezeDocExportService exportService) {
        this.hmeFreezeDocRepository = hmeFreezeDocRepository;
        this.hmeFreezeDocDomainService = hmeFreezeDocDomainService;
        this.exportService = exportService;
    }

    @ApiOperation(value = "条码冻结单 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeFreezeDocVO>> list(@PathVariable("organizationId") Long tenantId,
                                                     HmeFreezeDocQueryDTO dto,
                                                     @ApiIgnore PageRequest pageRequest) {
        this.validObject(dto);
        Page<HmeFreezeDocVO> list = hmeFreezeDocRepository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "条码冻结单 SN查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/sn")
    public ResponseEntity<List<HmeFreezeDocCreateSnVO>> snList(@PathVariable("organizationId") Long tenantId,
                                                               HmeFreezeDocQueryDTO dto) {
        this.validObject(dto);
        dto.paramInit();
        List<HmeFreezeDocCreateSnVO> list = hmeFreezeDocRepository.snList(tenantId, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "条码冻结单 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    public ResponseEntity<Void> export(@PathVariable("organizationId") Long tenantId,
                                       @RequestParam("freezeDocId") String freezeDocId,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        exportService.export(tenantId, request, response, freezeDocId);
        return Results.success();
    }

    @ApiOperation(value = "条码冻结单 创建")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeFreezeDocVO> create(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody HmeFreezeDocSaveCommandDTO doc) {
        this.validObject(doc);
        this.validList(doc.getLineList());
        return Results.success(hmeFreezeDocDomainService.docCreation(tenantId, doc));
    }

    @ApiOperation(value = "条码冻结单 新版创建")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/create/new")
    public ResponseEntity<HmeFreezeDocQueryDTO> createNew(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody HmeFreezeDocQueryDTO dto) {
        this.validObject(dto);
        dto.paramInit();
        hmeFreezeDocDomainService.docCreationNew(tenantId, dto);
        return Results.success(dto);
    }

    @ApiOperation(value = "条码冻结单 新版冻结")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/freeze/new")
    public ResponseEntity<HmeFreezeDocQueryDTO> docFreeze(@PathVariable("organizationId") Long tenantId,
                                                          @RequestParam("freezeDocId") String freezeDocId) {
        HmeFreezeDocQueryDTO result = hmeFreezeDocDomainService.docFreeze(tenantId, freezeDocId);
        return Results.success(result);
    }

    @ApiOperation(value = "条码冻结单 提交审批")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/approval")
    public ResponseEntity<Void> apply(@PathVariable("organizationId") Long tenantId,
                                      @RequestParam("freezeDocId") String freezeDocId) {
        hmeFreezeDocDomainService.apply(tenantId, freezeDocId);
        return Results.success();
    }

    @ApiOperation(value = "条码冻结单 解冻")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/unfreeze")
    public ResponseEntity<Void> unfreeze(@PathVariable("organizationId") Long tenantId,
                                         @RequestParam("freezeDocId") String freezeDocId) {
        hmeFreezeDocDomainService.docUnfrozen(tenantId, freezeDocId);
        return Results.success();
    }
}
