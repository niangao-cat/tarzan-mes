package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeQualificationDTO;
import com.ruike.hme.api.dto.HmeQualificationQueryDTO;
import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.repository.HmeQualificationRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 资质基础信息表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-06-15 10:32:20
 */
@RestController("hmeQualificationController.v1")
@RequestMapping("/v1/{organizationId}/hme-qualifications")
@Api(tags = SwaggerApiConfig.HME_QUALIFICATION)
public class HmeQualificationController extends BaseController {

    @Autowired
    private HmeQualificationRepository hmeQualificationRepository;

    @ApiOperation(value = "资质基础信息表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeQualificationDTO>> list(HmeQualificationQueryDTO dto, @ApiIgnore @SortDefault(value = HmeQualification.FIELD_QUALITY_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest, @PathVariable("organizationId") Long tenantId) {
        return Results.success(hmeQualificationRepository.query(pageRequest, dto, tenantId));
    }

    @ApiOperation(value = "资质基础信息表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{qualityId}")
    public ResponseEntity<HmeQualification> detail(@PathVariable Long qualityId) {
        HmeQualification hmeQualification = hmeQualificationRepository.selectByPrimaryKey(qualityId);
        return Results.success(hmeQualification);
    }

    @ApiOperation(value = "删除资质基础信息表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeQualification hmeQualification) {
        SecurityTokenHelper.validToken(hmeQualification);
        hmeQualificationRepository.deleteByPrimaryKey(hmeQualification);
        return Results.success();
    }

    @ApiOperation(value = "资质基础信息表  保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/createOrUpdate"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeQualification>> createOrUpdate(@PathVariable("organizationId") Long tenantId, @RequestBody List<HmeQualification> hmeQualifications){
        return Results.success(hmeQualificationRepository.createOrUpdate(tenantId, hmeQualifications));
    }
}
