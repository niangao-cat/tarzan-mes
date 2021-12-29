package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosScrap;
import com.ruike.hme.domain.repository.HmeCosScrapRepository;
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
 * 报废记录表 管理 API
 *
 * @author yuchao.wang@hand-china.com 2020-10-26 20:09:44
 */
@RestController("hmeCosScrapController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-scraps")
public class HmeCosScrapController extends BaseController {

    @Autowired
    private HmeCosScrapRepository hmeCosScrapRepository;

    @ApiOperation(value = "报废记录表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosScrap>> list(HmeCosScrap hmeCosScrap, @ApiIgnore @SortDefault(value = HmeCosScrap.FIELD_COS_SCRAP_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeCosScrap> list = hmeCosScrapRepository.pageAndSort(pageRequest, hmeCosScrap);
        return Results.success(list);
    }

    @ApiOperation(value = "报废记录表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{cosScrapId}")
    public ResponseEntity<HmeCosScrap> detail(@PathVariable Long cosScrapId) {
        HmeCosScrap hmeCosScrap = hmeCosScrapRepository.selectByPrimaryKey(cosScrapId);
        return Results.success(hmeCosScrap);
    }

    @ApiOperation(value = "创建报废记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeCosScrap> create(@RequestBody HmeCosScrap hmeCosScrap) {
        validObject(hmeCosScrap);
        hmeCosScrapRepository.insertSelective(hmeCosScrap);
        return Results.success(hmeCosScrap);
    }

    @ApiOperation(value = "修改报废记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeCosScrap> update(@RequestBody HmeCosScrap hmeCosScrap) {
        SecurityTokenHelper.validToken(hmeCosScrap);
        hmeCosScrapRepository.updateByPrimaryKeySelective(hmeCosScrap);
        return Results.success(hmeCosScrap);
    }

    @ApiOperation(value = "删除报废记录表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeCosScrap hmeCosScrap) {
        SecurityTokenHelper.validToken(hmeCosScrap);
        hmeCosScrapRepository.deleteByPrimaryKey(hmeCosScrap);
        return Results.success();
    }

}
