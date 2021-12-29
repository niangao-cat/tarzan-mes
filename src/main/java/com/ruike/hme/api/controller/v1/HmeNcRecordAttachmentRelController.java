package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeNcRecordAttachmentRel;
import com.ruike.hme.domain.repository.HmeNcRecordAttachmentRelRepository;
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
 * 不良代码记录与附件关系表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-01 17:12:24
 */
@RestController("hmeNcRecordAttachmentRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-record-attachment-rels")
@Api(tags = SwaggerApiConfig.HME_NC_RECORD_ATTACHMENT_REL)
public class HmeNcRecordAttachmentRelController extends BaseController {

    @Autowired
    private HmeNcRecordAttachmentRelRepository hmeNcRecordAttachmentRelRepository;

    @ApiOperation(value = "不良代码记录与附件关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeNcRecordAttachmentRel>> list(HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel, @ApiIgnore @SortDefault(value = HmeNcRecordAttachmentRel.FIELD_NC_RE_ATTACH_REL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeNcRecordAttachmentRel> list = hmeNcRecordAttachmentRelRepository.pageAndSort(pageRequest, hmeNcRecordAttachmentRel);
        return Results.success(list);
    }

    @ApiOperation(value = "不良代码记录与附件关系表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ncReAttachRelId}")
    public ResponseEntity<HmeNcRecordAttachmentRel> detail(@PathVariable Long ncReAttachRelId) {
        HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel = hmeNcRecordAttachmentRelRepository.selectByPrimaryKey(ncReAttachRelId);
        return Results.success(hmeNcRecordAttachmentRel);
    }

    @ApiOperation(value = "创建不良代码记录与附件关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeNcRecordAttachmentRel> create(@RequestBody HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel) {
        validObject(hmeNcRecordAttachmentRel);
        hmeNcRecordAttachmentRelRepository.insertSelective(hmeNcRecordAttachmentRel);
        return Results.success(hmeNcRecordAttachmentRel);
    }

    @ApiOperation(value = "修改不良代码记录与附件关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeNcRecordAttachmentRel> update(@RequestBody HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel) {
        SecurityTokenHelper.validToken(hmeNcRecordAttachmentRel);
        hmeNcRecordAttachmentRelRepository.updateByPrimaryKeySelective(hmeNcRecordAttachmentRel);
        return Results.success(hmeNcRecordAttachmentRel);
    }

    @ApiOperation(value = "删除不良代码记录与附件关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel) {
        SecurityTokenHelper.validToken(hmeNcRecordAttachmentRel);
        hmeNcRecordAttachmentRelRepository.deleteByPrimaryKey(hmeNcRecordAttachmentRel);
        return Results.success();
    }

}
