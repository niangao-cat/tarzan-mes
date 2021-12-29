package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsPqcInspectionSchemeService;
import com.ruike.qms.domain.entity.QmsMaterialInspScheme;
import com.ruike.qms.domain.entity.QmsPqcInspectionScheme;
import com.ruike.qms.domain.repository.QmsPqcInspectionSchemeRepository;
import com.ruike.qms.domain.vo.QmsPqcInspectionContentVO;
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
 * 巡检检验计划 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
@RestController("qmsPqcInspectionSchemeController.v1")
@RequestMapping("/v1/{organizationId}/qms-pqc-inspection-schemes")
@Api(tags = SwaggerApiConfig.QMS_PQC_INSPECTION_SCHEME)
@Slf4j
public class QmsPqcInspectionSchemeController extends BaseController {

    @Autowired
    private QmsPqcInspectionSchemeRepository qmsPqcInspectionSchemeRepository;

    @Autowired
    private QmsPqcInspectionSchemeService qmsPqcInspectionSchemeService;

    @ApiOperation(value = "巡检检验计划头列表")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @GetMapping(value = "/list/head/ui")
    public ResponseEntity<Page<QmsMisHeadReturnDTO>> list(@PathVariable("organizationId") Long tenantId, QmsMaterialInspectionSchemeHeadDTO inspectionSchemeHeadDTO,
                                                          @ApiIgnore PageRequest pageRequest) {
        Page<QmsMisHeadReturnDTO> list = qmsPqcInspectionSchemeRepository.selectHeadList(tenantId,inspectionSchemeHeadDTO,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description 巡检检验计划行列表查询
     * @param tenantId 租户
     * @param qmsMisLineQueryDTO 头id
     * @param pageRequest 页码请求
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.qms.domain.entity.QmsMaterialInspectionContent>>
     * @Date 2020-04-22 15:03
     * @Author han.zhang
     */
    @ApiOperation(value = "巡检检验计划行列表,二级页面检验项行列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list/line")
    public ResponseEntity<Page<QmsPqcInspectionContentVO>> detail(@PathVariable("organizationId") Long tenantId
            , QmsMisLineQueryDTO qmsMisLineQueryDTO, @ApiIgnore @SortDefault(value = com.ruike.qms.domain.entity.QmsMaterialInspContent.FIELD_ORDER_KEY,
            direction = Sort.Direction.ASC) PageRequest pageRequest) {
        Page<QmsPqcInspectionContentVO> list = qmsPqcInspectionSchemeRepository.selectLineList(tenantId,qmsMisLineQueryDTO,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description 巡检检验计划头新增或保存
     * @param tenantId 租户id
     * @param qmsPqcInspectionScheme 参数
     * @return org.springframework.http.ResponseEntity<com.ruike.qms.domain.entity.QmsMaterialInspectionScheme>
     * @Date 2020-04-22 16:30
     * @Author han.zhang
     */
    @ApiOperation(value = "巡检检验计划头新增或保存")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/save")
    public ResponseEntity<QmsPqcInspectionScheme> addAndUpdateHead(@PathVariable("organizationId") Long tenantId, @RequestBody QmsPqcInspectionScheme qmsPqcInspectionScheme) {
        log.info("<====QmsMaterialInspSchemeController-addAndUpdateHead:{},{}",tenantId, qmsPqcInspectionScheme );
        QmsPqcInspectionScheme inspectionScheme = qmsPqcInspectionSchemeRepository.addAndUpdateHead(tenantId,qmsPqcInspectionScheme);
        return Results.success(inspectionScheme);
    }

    /**
     * @Description 巡检检验计划发布
     * @param tenantId 租户id
     * @param inspectionSchemeIds 参数
     * @return org.springframework.http.ResponseEntity<com.ruike.qms.domain.entity.QmsMaterialInspectionScheme>
     * @Date 2020-04-22 16:31
     * @Author han.zhang
     */
    @ApiOperation(value = "巡检检验计划发布")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/publish")
    public ResponseEntity<List<String>> publish(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> inspectionSchemeIds) {
        log.info("<====QmsMaterialInspSchemeController-publish:{},{}",tenantId, inspectionSchemeIds );
        List<String> list = qmsPqcInspectionSchemeRepository.publish(tenantId,inspectionSchemeIds);
        return Results.success(list);
    }

    /**
     * @Description 巡检检验计划删除
     * @param tenantId 租户id
     * @param inspectionSchemes 删除的检验计划
     * @return org.springframework.http.ResponseEntity<com.ruike.qms.domain.entity.QmsMaterialInspectionScheme>
     * @Date 2020-04-22 16:31
     * @Author han.zhang
     */
    @ApiOperation(value = "巡检检验计划删除")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/delete")
    public ResponseEntity<List<QmsPqcInspectionScheme>> delete(@PathVariable("organizationId") Long tenantId, @RequestBody List<QmsPqcInspectionScheme> inspectionSchemes) {
        log.info("<====QmsMaterialInspSchemeController-delete:{},{}",tenantId, inspectionSchemes );
        List<QmsPqcInspectionScheme> delete = qmsPqcInspectionSchemeRepository.delete(tenantId, inspectionSchemes);
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
        Page<QmsTagGroupQueryReturnDTO> qmsTagGroupQueryDTOS = qmsPqcInspectionSchemeRepository.selectQuatityList(tenantId, qmsTagGroupQueryDTO, pageRequest);
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
        qmsPqcInspectionSchemeRepository.addTagGroup(tenantId, addTagGroupDTOS);
        return Results.success();
    }

    @ApiOperation(value = "删除检验组下质检组")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/delete/tag-group")
    public ResponseEntity<?> deleteTagGroup(@PathVariable("organizationId") Long tenantId,
                                            @RequestBody List<String> tagGroupRelIds) {
        log.info("<====QmsMaterialInspSchemeController-deleteTagGroup:{},{}",tenantId, tagGroupRelIds );
        qmsPqcInspectionSchemeRepository.deleteTagGroup(tenantId, tagGroupRelIds);
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
        qmsPqcInspectionSchemeRepository.editTag(tenantId, tagContentEditDTOS);
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
        qmsPqcInspectionSchemeRepository.allSynchronize(tenantId, tagContentEditDTOS);
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
        qmsPqcInspectionSchemeRepository.partSynchronize(tenantId, tagContentEditDTOS);
        return Results.success();
    }

    /**
     * 复制按钮
     *
     * @param tenantId
     * @param qmsPqcInspectionSchemeDTO
     * @return org.springframework.http.ResponseEntity<?>
     * @auther wenqiang.yin@hand-china.com 2021/2/5 15:31
    */
    @ApiOperation(value = "复制")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @PostMapping(value = "/copy")
    public ResponseEntity<?> copy(@PathVariable("organizationId") Long tenantId,
                                  @RequestBody QmsPqcInspectionSchemeDTO qmsPqcInspectionSchemeDTO) {
        log.info("<====QmsMaterialInspSchemeController-copy:{},{}",tenantId, qmsPqcInspectionSchemeDTO);
        qmsPqcInspectionSchemeService.copy(tenantId, qmsPqcInspectionSchemeDTO);
        return Results.success();
    }

}
