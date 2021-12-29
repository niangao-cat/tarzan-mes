package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeFreezeCosLoadRepresentationDTO;
import com.ruike.hme.api.dto.HmeFreezeDocLineSnQueryDTO;
import com.ruike.hme.api.dto.HmeFreezeDocSnUnfreezeCommandDTO;
import com.ruike.hme.domain.repository.HmeFreezeDocLineRepository;
import com.ruike.hme.domain.service.HmeFreezeDocDomainService;
import com.ruike.hme.domain.vo.HmeFreezeDocLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 条码冻结单行 管理 API
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:41
 */
@RestController("hmeFreezeDocLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-freeze-doc-lines")
@Api(tags = SwaggerApiConfig.HME_FREEZE_DOC_LINE)
public class HmeFreezeDocLineController extends BaseController {

    private final HmeFreezeDocLineRepository hmeFreezeDocLineRepository;
    private final HmeFreezeDocDomainService hmeFreezeDocDomainService;

    public HmeFreezeDocLineController(HmeFreezeDocLineRepository hmeFreezeDocLineRepository, HmeFreezeDocDomainService hmeFreezeDocDomainService) {
        this.hmeFreezeDocLineRepository = hmeFreezeDocLineRepository;
        this.hmeFreezeDocDomainService = hmeFreezeDocDomainService;
    }

    @ApiOperation(value = "条码冻结单行 按条件查询展示列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeFreezeDocLineVO>> listByCondition(@PathVariable("organizationId") Long tenantId,
                                                                    HmeFreezeDocLineSnQueryDTO dto, PageRequest pageRequest) {
        validObject(dto);
        dto.paramInit();
        Page<HmeFreezeDocLineVO> list = hmeFreezeDocLineRepository.byCondition(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "条码冻结单行 条码装载分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/load/{materialLotId}")
    public ResponseEntity<Page<HmeFreezeCosLoadRepresentationDTO>> cosLoadGet(@PathVariable("organizationId") Long tenantId,
                                                                              @PathVariable("materialLotId") String materialLotId,
                                                                              @ApiIgnore PageRequest pageRequest) {
        Page<HmeFreezeCosLoadRepresentationDTO> list = hmeFreezeDocLineRepository.cosLoadGet(tenantId, materialLotId, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "条码冻结单行 选择解冻")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/unfreeze")
    public ResponseEntity<Void> unfreeze(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeFreezeDocSnUnfreezeCommandDTO command) {
        validObject(command);
        validList(command.getLineList());
        hmeFreezeDocDomainService.selectionUnfrozen(tenantId, command);
        return Results.success();
    }

}
