package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.entity.HmeFacYkVO2;
import com.ruike.hme.domain.vo.HmeEquipmentVO5;
import com.ruike.hme.domain.vo.HmeFacYkHisVO;
import com.ruike.hme.domain.vo.HmeFacYkVO;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeFacYk;
import com.ruike.hme.domain.repository.HmeFacYkRepository;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * FAC-Y宽判定标准表 管理 API
 *
 * @author TAO.XU02@hand-china.com 2021-02-01 10:45:03
 */
@RestController("hmeFacYkController.v1")
@RequestMapping("/v1/{organizationId}/hme-fac-yks")
@Api(tags = SwaggerApiConfig.HME_FAC_YK)
public class HmeFacYkController extends BaseController {

    @Autowired
    private HmeFacYkRepository hmeFacYkRepository;

    @ApiOperation(value = "FAC-Y宽判定标准表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeFacYkVO>> list(HmeFacYkVO2 hmeFacYk, @ApiIgnore @SortDefault(value = HmeFacYk.FIELD_FAC_YK_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest, @PathVariable("organizationId") Long tenantId) {
        Page<HmeFacYkVO> list = hmeFacYkRepository.selectHmeFacYk(pageRequest, hmeFacYk, tenantId);
        return Results.success(list);
    }

    @ApiOperation(value = "FAC-Y宽判定标准表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{facYkId}")
    public ResponseEntity<HmeFacYk> detail(@PathVariable Long facYkId) {
        HmeFacYk hmeFacYk = hmeFacYkRepository.selectByPrimaryKey(facYkId);
        return Results.success(hmeFacYk);
    }

    @ApiOperation(value = "创建FAC-Y宽判定标准表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeFacYk> create(HmeFacYkVO2 hmeFacYk, @PathVariable("organizationId") Long tenantId) {
        hmeFacYkRepository.insertHmeFacYk(hmeFacYk, tenantId);
        return Results.success(hmeFacYk);
    }

    @ApiOperation(value = "修改FAC-Y宽判定标准表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeFacYk> update( HmeFacYk hmeFacYk,@PathVariable("organizationId") Long tenantId) {
        hmeFacYkRepository.updateByHmeFacYk(hmeFacYk,tenantId);
        return Results.success(hmeFacYk);
    }

    @ApiOperation(value = "删除FAC-Y宽判定标准表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(HmeFacYk hmeFacYk) {
        hmeFacYkRepository.deleteByPrimaryKey(hmeFacYk);
        return Results.success();
    }

    @ApiOperation(value = "FAC-Y宽判定标准历史列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/his-list-query")
    public ResponseEntity<Page<HmeFacYkHisVO>> hisListQuery(@PathVariable("organizationId") Long tenantId,
                                                            String facYkId,
                                                            @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeFacYkRepository.hisListQuery(tenantId, facYkId, pageRequest));
    }

    @ApiOperation(value = "FAC-Y宽判定标准导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/fac-yk-export")
    @ExcelExport(HmeFacYkVO.class)
    public ResponseEntity<List<HmeFacYkVO>> facYkExport(@PathVariable("organizationId") Long tenantId,
                                                        HmeFacYkVO2 hmeFacYk,
                                                        ExportParam exportParam,
                                                        HttpServletResponse response) {
        return Results.success(hmeFacYkRepository.facYkExport(tenantId, hmeFacYk));
    }

}
