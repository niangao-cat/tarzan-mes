package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeOpEqRelDTO;
import com.ruike.hme.api.dto.HmeOpEqRelDTO2;
import com.ruike.hme.app.service.HmeOpEqRelService;
import com.ruike.hme.infra.constant.HmeConstants;
import io.swagger.annotations.Api;
import io.tarzan.common.domain.sys.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeOpEqRel;
import com.ruike.hme.domain.repository.HmeOpEqRelRepository;
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

import java.util.List;

/**
 * 工艺设备类关系表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-06-22 09:42:57
 */
@RestController("hmeOpEqRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-op-eq-rels")
@Api(tags = SwaggerApiConfig.HME_OP_EQ_REL)
public class HmeOpEqRelController extends BaseController {

    @Autowired
    private HmeOpEqRelRepository hmeOpEqRelRepository;
    @Autowired
    private HmeOpEqRelService hmeOpEqRelService;

    @ApiOperation(value = "工艺设备类关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{operationId}")
    public ResponseEntity<Page<HmeOpEqRelDTO>> list(@PathVariable("organizationId") Long tenantId,@PathVariable("operationId")String operationId,
                                               @ApiIgnore @SortDefault(value = HmeOpEqRel.FIELD_OP_EQ_REL_ID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(hmeOpEqRelService.query(tenantId, operationId, pageRequest));
    }

    @ApiOperation(value = "工艺设备类关系表 保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/createOrUpdate"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeOpEqRel>> createOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody List<HmeOpEqRel> hmeOpEqRels) {
        for (HmeOpEqRel hmeOpEqRel:hmeOpEqRels) {
            hmeOpEqRel.setTenantId(tenantId);
            if(StringUtils.isBlank(hmeOpEqRel.getEnableFlag())){
                hmeOpEqRel.setEnableFlag(HmeConstants.ConstantValue.YES);
            }
            validObject(hmeOpEqRel);
        }
        return Results.success(hmeOpEqRelService.createOrUpdate(hmeOpEqRels));
    }

    @ApiOperation(value = "删除工艺设备类关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<HmeOpEqRel> hmeOpEqRels) {
        hmeOpEqRelRepository.batchDeleteByPrimaryKey(hmeOpEqRels);
        return Results.success();
    }

    @ApiOperation(value = "设备类查询LOV")
    @GetMapping(value = "/list/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeOpEqRelDTO2>> listForLov(@PathVariable("organizationId") Long tenantId,
                                                         HmeOpEqRelDTO2 dto, PageRequest pageRequest){
        return Results.success(hmeOpEqRelService.listForLov(tenantId, dto, pageRequest));
    }
}
