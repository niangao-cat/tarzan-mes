package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsMaterialInspSchemeService;
import com.ruike.qms.domain.entity.QmsMaterialInspScheme;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 *      物料检验计划 管理 API
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:43
 */
@RestController("qmsMaterialInspectionSchemeController.v1")
@RequestMapping("/v1/{organizationId}/qms-material-inspection-schemes")
@Api(tags = SwaggerApiConfig.QMS_MATERIAL_INSPECTION_SCHEME)
@Slf4j
public class QmsMaterialInspSchemeController extends BaseController {

    @Autowired
    private QmsMaterialInspSchemeService qmsMaterialInspectionSchemeService;

    /**
     * @Description 物料检验计划头列表查询
     * @param tenantId 租户
     * @param inspectionSchemeHeadDTO 查询条件
     * @param pageRequest 页码请求
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.qms.api.dto.QmsMisHeadReturnDTO>>
     * @Date 2020-04-22 15:03
     * @Author han.zhang
     */
    @ApiOperation(value = "物料检验计划头列表")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @GetMapping(value = "/list/head/ui")
    public ResponseEntity<Page<QmsMisHeadReturnDTO>> list(@PathVariable("organizationId") Long tenantId, QmsMaterialInspectionSchemeHeadDTO inspectionSchemeHeadDTO,
                                                          @ApiIgnore PageRequest pageRequest) {
        Page<QmsMisHeadReturnDTO> list = qmsMaterialInspectionSchemeService.selectHeadList(tenantId,inspectionSchemeHeadDTO,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description 物料检验计划行列表查询
     * @param tenantId 租户
     * @param qmsMisLineQueryDTO 头id
     * @param pageRequest 页码请求
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.qms.domain.entity.QmsMaterialInspectionContent>>
     * @Date 2020-04-22 15:03
     * @Author han.zhang
     */
    @ApiOperation(value = "物料检验计划行列表,二级页面检验项行列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list/line")
    public ResponseEntity<Page<QmsMaterialInspContentReturnDTO>> detail(@PathVariable("organizationId") Long tenantId
                                            , QmsMisLineQueryDTO qmsMisLineQueryDTO, @ApiIgnore @SortDefault(value = com.ruike.qms.domain.entity.QmsMaterialInspContent.FIELD_ORDER_KEY,
            direction = Sort.Direction.ASC) PageRequest pageRequest) {
        Page<QmsMaterialInspContentReturnDTO> list = qmsMaterialInspectionSchemeService.selectLineList(tenantId,qmsMisLineQueryDTO,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description 物料检验计划头新增或保存
     * @param tenantId 租户id
     * @param qmsMaterialInspectionScheme 参数
     * @return org.springframework.http.ResponseEntity<com.ruike.qms.domain.entity.QmsMaterialInspectionScheme>
     * @Date 2020-04-22 16:30
     * @Author han.zhang
     */
    @ApiOperation(value = "物料检验计划头新增或保存")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/save")
    public ResponseEntity<QmsMaterialInspScheme> addAndUpdateHead(@PathVariable("organizationId") Long tenantId, @RequestBody QmsMaterialInspScheme qmsMaterialInspectionScheme) {
        log.info("<====QmsMaterialInspSchemeController-addAndUpdateHead:{},{}",tenantId, qmsMaterialInspectionScheme );
        QmsMaterialInspScheme inspectionScheme = qmsMaterialInspectionSchemeService.addAndUpdateHead(tenantId,qmsMaterialInspectionScheme);
        return Results.success(inspectionScheme);
    }

    /**
     * @Description 物料检验计划发布
     * @param tenantId 租户id
     * @param inspectionSchemeIds 参数
     * @return org.springframework.http.ResponseEntity<com.ruike.qms.domain.entity.QmsMaterialInspectionScheme>
     * @Date 2020-04-22 16:31
     * @Author han.zhang
     */
    @ApiOperation(value = "物料检验计划发布")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/publish")
    public ResponseEntity<List<String>> publish(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> inspectionSchemeIds) {
        log.info("<====QmsMaterialInspSchemeController-publish:{},{}",tenantId, inspectionSchemeIds );
        List<String> list = qmsMaterialInspectionSchemeService.publish(tenantId,inspectionSchemeIds);
        return Results.success(list);
    }

    /**
     * @Description 物料检验计划删除
     * @param tenantId 租户id
     * @param inspectionSchemes 删除的检验计划
     * @return org.springframework.http.ResponseEntity<com.ruike.qms.domain.entity.QmsMaterialInspectionScheme>
     * @Date 2020-04-22 16:31
     * @Author han.zhang
     */
    @ApiOperation(value = "物料检验计划删除")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/delete")
    public ResponseEntity<List<QmsMaterialInspScheme>> delete(@PathVariable("organizationId") Long tenantId, @RequestBody List<QmsMaterialInspScheme> inspectionSchemes) {
        log.info("<====QmsMaterialInspSchemeController-delete:{},{}",tenantId, inspectionSchemes );
        List<QmsMaterialInspScheme> delete = qmsMaterialInspectionSchemeService.delete(tenantId, inspectionSchemes);
        return Results.success(delete);
    }

    /**
     * @Description 二级页面检验组查询列表
     * @param tenantId 租户id
     * @param qmsTagGroupQueryDTO 查询参数
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.qms.api.dto.QmsTagGroupQueryReturnDTO>>
     * @Date 2020-04-22 21:46
     * @Author han.zhang
     */
    @ApiOperation(value = "二级页面检验组查询列表")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @GetMapping(value = "/list/quatity/ui")
    public ResponseEntity<Page<QmsTagGroupQueryReturnDTO>> qualityList(@PathVariable("organizationId") Long tenantId, QmsTagGroupQueryDTO qmsTagGroupQueryDTO, @ApiIgnore PageRequest pageRequest) {
        Page<QmsTagGroupQueryReturnDTO> qmsTagGroupQueryDTOS = qmsMaterialInspectionSchemeService.selectQuatityList(tenantId, qmsTagGroupQueryDTO, pageRequest);
        return Results.success(qmsTagGroupQueryDTOS);
    }

    /**
     * @Description 新增检验组
     * @param tenantId 租户id
     * @param addTagGroupDTOS 参数
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-04-24 11:47
     * @Author han.zhang
     */
    @ApiOperation(value = "新增检验组")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/add/tag-group")
    public ResponseEntity<?> addTagGroup(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody List<QmsAddTagGroupDTO> addTagGroupDTOS) {
        log.info("<====QmsMaterialInspSchemeController-addTagGroup:{},{}",tenantId, addTagGroupDTOS );
        qmsMaterialInspectionSchemeService.addTagGroup(tenantId, addTagGroupDTOS);
        return Results.success();
    }

    @ApiOperation(value = "删除检验组下质检组")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/delete/tag-group")
    public ResponseEntity<?> deleteTagGroup(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody List<String> tagGroupRelIds) {
        log.info("<====QmsMaterialInspSchemeController-deleteTagGroup:{},{}",tenantId, tagGroupRelIds );
        qmsMaterialInspectionSchemeService.deleteTagGroup(tenantId, tagGroupRelIds);
        return Results.success();
    }

    /**
     * @Description 质检项编辑
     * @param tenantId
     * @param tagContentEditDTOS
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-04-24 15:26
     * @Author han.zhang
     */
    @ApiOperation(value = "质检项编辑")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/edit/tag")
    public ResponseEntity<?> editTag(@PathVariable("organizationId") Long tenantId,
                                          @RequestBody List<QmsTagContentEditDTO> tagContentEditDTOS) {
        log.info("<====QmsMaterialInspSchemeController-editTag:{},{}",tenantId, tagContentEditDTOS );
        qmsMaterialInspectionSchemeService.editTag(tenantId, tagContentEditDTOS);
        return Results.success();
    }

    /**
     * @Description 全量同步
     * @param tenantId
     * @param tagContentEditDTOS
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-04-24 17:37
     * @Author han.zhang
     */
    @ApiOperation(value = "全量同步")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/all-synchronize")
    public ResponseEntity<?> allSynchronize(@PathVariable("organizationId") Long tenantId,
                                   @RequestBody List<QmsAddTagGroupDTO> tagContentEditDTOS) {
        log.info("<====QmsMaterialInspSchemeController-allSynchronize:{},{}",tenantId, tagContentEditDTOS );
        qmsMaterialInspectionSchemeService.allSynchronize(tenantId, tagContentEditDTOS);
        return Results.success();
    }

    /**
     * @Description 增量同步
     * @param tenantId
     * @param tagContentEditDTOS
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-04-24 17:37
     * @Author han.zhang
     */
    @ApiOperation(value = "增量同步")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/part-synchronize")
    public ResponseEntity<?> partSynchronize(@PathVariable("organizationId") Long tenantId,
                                          @RequestBody List<QmsAddTagGroupDTO> tagContentEditDTOS) {
        log.info("<====QmsMaterialInspSchemeController-partSynchronize:{},{}",tenantId, tagContentEditDTOS );
        qmsMaterialInspectionSchemeService.partSynchronize(tenantId, tagContentEditDTOS);
        return Results.success();
    }

    @ApiOperation(value = "复制")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/copy")
    public ResponseEntity<?> copy(@PathVariable("organizationId") Long tenantId,
                                  @RequestBody QmsMaterialInspSchemeDTO qmsMaterialInspSchemeDTO) {
        log.info("<====QmsMaterialInspSchemeController-partSynchronize:{},{}\",tenantId, qmsMaterialInspSchemeDTO");
        qmsMaterialInspectionSchemeService.copy(tenantId,qmsMaterialInspSchemeDTO);
        return Results.success();
    }


}
