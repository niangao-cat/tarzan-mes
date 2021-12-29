package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.vo.HmeAgingBasicVO;
import com.ruike.hme.domain.vo.HmeSnLabCodeVO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeSnLabCode;
import com.ruike.hme.domain.repository.HmeSnLabCodeRepository;
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

/**
 * SN工艺实验代码表 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-03-19 16:01:36
 */
@RestController("hmeSnLabCodeController.v1")
@RequestMapping("/v1/{organizationId}/hme-sn-lab-codes")
public class HmeSnLabCodeController extends BaseController {

    @Autowired
    private HmeSnLabCodeRepository hmeSnLabCodeRepository;

    @ApiOperation(value = "SN工艺实验代码表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeSnLabCode>> list(HmeSnLabCode hmeSnLabCode, @ApiIgnore @SortDefault(value = HmeSnLabCode.FIELD_KID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeSnLabCode> list = hmeSnLabCodeRepository.pageAndSort(pageRequest, hmeSnLabCode);
        return Results.success(list);
    }

    @ApiOperation(value = "SN工艺实验代码表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{kid}")
    public ResponseEntity<HmeSnLabCode> detail(@PathVariable Long kid) {
        HmeSnLabCode hmeSnLabCode = hmeSnLabCodeRepository.selectByPrimaryKey(kid);
        return Results.success(hmeSnLabCode);
    }

    @ApiOperation(value = "创建SN工艺实验代码表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeSnLabCode> create(@RequestBody HmeSnLabCode hmeSnLabCode) {
        validObject(hmeSnLabCode);
        hmeSnLabCodeRepository.insertSelective(hmeSnLabCode);
        return Results.success(hmeSnLabCode);
    }

    @ApiOperation(value = "修改SN工艺实验代码表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeSnLabCode> update(@RequestBody HmeSnLabCode hmeSnLabCode) {
        SecurityTokenHelper.validToken(hmeSnLabCode);
        hmeSnLabCodeRepository.updateByPrimaryKeySelective(hmeSnLabCode);
        return Results.success(hmeSnLabCode);
    }

    @ApiOperation(value = "删除SN工艺实验代码表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeSnLabCode hmeSnLabCode) {
        SecurityTokenHelper.validToken(hmeSnLabCode);
        hmeSnLabCodeRepository.deleteByPrimaryKey(hmeSnLabCode);
        return Results.success();
    }

    @ApiOperation(value = "SN工艺实验代码")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list/ui")
    public ResponseEntity<Page<HmeSnLabCodeVO>> listByMaterialLotId(@PathVariable("organizationId") Long tenantId,
                                                                  HmeSnLabCode hmeSnLabCode,
                                                                  @ApiIgnore @SortDefault(value = HmeSnLabCode.FIELD_KID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeSnLabCodeVO> list = hmeSnLabCodeRepository.listByMaterialLotId(pageRequest, hmeSnLabCode, tenantId);
        return Results.success(list);
    }

    @ApiOperation(value = "修改老化基础数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save/ui"},produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> save(@PathVariable (value = "organizationId") Long tenantId,
                                  @RequestBody HmeSnLabCode hmeSnLabCode) {
        hmeSnLabCodeRepository.save(hmeSnLabCode, tenantId);
        return Results.success(hmeSnLabCode);
    }
}
