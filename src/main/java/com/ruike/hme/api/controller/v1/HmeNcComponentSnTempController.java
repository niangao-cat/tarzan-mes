package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeNcComponentSnTemp;
import com.ruike.hme.domain.repository.HmeNcComponentSnTempRepository;
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

/**
 * 不良材料清单条码临时表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-01 20:08:36
 */
@RestController("hmeNcComponentSnTempController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-component-sn-temps")
@Api(tags = SwaggerApiConfig.HME_NC_COMPONENT_SN_TEMP)
public class HmeNcComponentSnTempController extends BaseController {

    @Autowired
    private HmeNcComponentSnTempRepository hmeNcComponentSnTempRepository;

    @ApiOperation(value = "不良材料清单条码临时表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeNcComponentSnTemp>> list(HmeNcComponentSnTemp hmeNcComponentSnTemp, @ApiIgnore @SortDefault(value = HmeNcComponentSnTemp.FIELD_NC_COMPONENT_SN_TEMP_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeNcComponentSnTemp> list = hmeNcComponentSnTempRepository.pageAndSort(pageRequest, hmeNcComponentSnTemp);
        return Results.success(list);
    }

    @ApiOperation(value = "不良材料清单条码临时表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ncComponentSnTempId}")
    public ResponseEntity<HmeNcComponentSnTemp> detail(@PathVariable Long ncComponentSnTempId) {
        HmeNcComponentSnTemp hmeNcComponentSnTemp = hmeNcComponentSnTempRepository.selectByPrimaryKey(ncComponentSnTempId);
        return Results.success(hmeNcComponentSnTemp);
    }

    @ApiOperation(value = "创建不良材料清单条码临时表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeNcComponentSnTemp> create(@RequestBody HmeNcComponentSnTemp hmeNcComponentSnTemp) {
        validObject(hmeNcComponentSnTemp);
        hmeNcComponentSnTempRepository.insertSelective(hmeNcComponentSnTemp);
        return Results.success(hmeNcComponentSnTemp);
    }

    @ApiOperation(value = "修改不良材料清单条码临时表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeNcComponentSnTemp> update(@RequestBody HmeNcComponentSnTemp hmeNcComponentSnTemp) {
        SecurityTokenHelper.validToken(hmeNcComponentSnTemp);
        hmeNcComponentSnTempRepository.updateByPrimaryKeySelective(hmeNcComponentSnTemp);
        return Results.success(hmeNcComponentSnTemp);
    }

    @ApiOperation(value = "删除不良材料清单条码临时表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeNcComponentSnTemp hmeNcComponentSnTemp) {
        SecurityTokenHelper.validToken(hmeNcComponentSnTemp);
        hmeNcComponentSnTempRepository.deleteByPrimaryKey(hmeNcComponentSnTemp);
        return Results.success();
    }

}
