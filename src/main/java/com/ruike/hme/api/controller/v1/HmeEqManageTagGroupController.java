package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEqManageTagDTO;
import com.ruike.hme.api.dto.HmeEqManageTagGroupDTO;
import com.ruike.hme.api.dto.HmeManageTagDTO;
import com.ruike.hme.domain.entity.HmeEqManageTag;
import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import com.ruike.hme.domain.repository.HmeEqManageTagGroupRepository;
import com.ruike.hme.domain.vo.HmeEquipManageTagReturnVO;
import com.ruike.hme.domain.vo.HmeEquipTagGroupReturnVO;
import com.ruike.hme.domain.vo.HmeEquipmentTagVO2;
import com.ruike.hme.domain.vo.HmeMtTagVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设备管理项目组表 管理 API
 *
 * @author han.zhang03@hand-china.com 2020-06-10 14:58:51
 */
@RestController("hmeEquipmentManageTaggroupController.v1")
@RequestMapping("/v1/{organizationId}/equipment-manage-tag-group")
@Api(tags = SwaggerApiConfig.HME_EQUIPMENT_MANAGE_TAGGROUP)
@Slf4j
public class HmeEqManageTagGroupController extends BaseController {

    @Autowired
    private HmeEqManageTagGroupRepository repository;

    /**
     * @Description 设备管理项目组查询
     * @param tenantId
     * @param hmeEqManageTaggroup
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeEquipmentWkcRel>>
     * @Date 2020-06-10 15:35
     * @Author han.zhang
     */
    @ApiOperation(value = "设备管理项目组查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeEquipTagGroupReturnVO>> list(@PathVariable("organizationId") Long tenantId, HmeEqManageTagGroup hmeEqManageTaggroup,
                                                       @ApiIgnore PageRequest pageRequest) {
        log.info("<====HmeEquipmentManageTaggroupController-list:{},{}",tenantId, hmeEqManageTaggroup);
        return Results.success(repository.selectEquipTagGroupData(tenantId,pageRequest, hmeEqManageTaggroup));
    }

    /**
     * @Description 新增or修改设备项目关系组维护
     * @param tenantId
     * @param hmeEqManageTaggroup
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.hme.domain.entity.HmeEquipmentWkcRel>>
     * @Date 2020-06-10 15:53
     * @Author han.zhang
     */
    @ApiOperation(value = "新增or修改设备项目关系组维护")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/update")
    public ResponseEntity<HmeEqManageTagGroup> update(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEqManageTagGroup hmeEqManageTaggroup) {
        log.info("<====HmeEquipmentManageTaggroupController-update:{},{}",tenantId, hmeEqManageTaggroup);
        return Results.success(repository.update(tenantId, hmeEqManageTaggroup));
    }

    /**
     * @Description 设备管理项查询
     * @param tenantId
     * @param manageTagGroupId
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEquipManageTagReturnVO>>
     * @Date 2020-06-11 15:37
     * @Author han.zhang
     */
    @ApiOperation(value = "设备管理项查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/equipment-manage-tag/list/ui/{manageTagGroupId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeEquipManageTagReturnVO>> manageTagList(@PathVariable("organizationId") Long tenantId, @PathVariable("manageTagGroupId") String manageTagGroupId,
                                                       @ApiIgnore PageRequest pageRequest) {
        log.info("<====HmeEquipmentManageTaggroupController-manageTagList:{},{}",tenantId, manageTagGroupId);
        return Results.success(repository.selectEquipTagData(tenantId,pageRequest, manageTagGroupId));
    }

    /**
     * @Description 设备管理项新增或保存
     * @param tenantId
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEquipManageTagReturnVO>>
     * @Date 2020-06-11 16:09
     * @Author han.zhang
     */
    @ApiOperation(value = "设备管理项新增或保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/equipment-manage-tag/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeEqManageTag>> manageTagUpdate(@PathVariable("organizationId") Long tenantId, @RequestBody List<HmeEqManageTag> hmeEqManageTags) {
        log.info("<====HmeEquipmentManageTaggroupController-manageTagUpdate:{},{}",tenantId, hmeEqManageTags);
        return Results.success(repository.manageTagUpdate(tenantId, hmeEqManageTags));
    }

    /**
     * @Description 全量同步
     * @param tenantId
     * @param manageTagGroupId
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.hme.domain.entity.HmeEquipmentManageTag>>
     * @Date 2020-06-11 16:30
     * @Author han.zhang
     */
    @ApiOperation(value = "全量同步")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/all-sync/{manageTagGroupId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> allSync(@PathVariable("organizationId") Long tenantId, @PathVariable("manageTagGroupId") String manageTagGroupId) {
        log.info("<====HmeEquipmentManageTaggroupController-allSync:{},{}",tenantId, manageTagGroupId);
        repository.allSync(tenantId, manageTagGroupId);
        return Results.success();
    }

    /**
     * @Description 增量同步
     * @param tenantId
     * @param manageTagGroupId
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.hme.domain.entity.HmeEquipmentManageTag>>
     * @Date 2020-06-11 16:30
     * @Author han.zhang
     */
    @ApiOperation(value = "增量同步")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/part-sync/{manageTagGroupId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> partSync(@PathVariable("organizationId") Long tenantId, @PathVariable("manageTagGroupId") String manageTagGroupId) {
        log.info("<====HmeEquipmentManageTaggroupController-partSync:{},{}",tenantId, manageTagGroupId);
        repository.partSync(tenantId, manageTagGroupId);
        return Results.success();
    }

    /**
     * 删除管理组和项目组
     * @param tenantId
     * @param dto
     * @return
     */
    @ApiModelProperty(value = "删除管理组和设备组")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/head/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteTag(@PathVariable("organizationId") Long tenantId,@RequestBody HmeEqManageTagDTO dto){
        log.info("<====HmeEquipmentManageTaggroupController-deleteTag:{},{}",tenantId, dto);
        repository.deleteTag(tenantId, dto);
        return Results.success();
    }

    /**
     * 删除设备管理组
     * @param tenantId
     * @param dto
     * @return
     */
    @ApiModelProperty(value = "删除设备管理组")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/line/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteManageTag(@PathVariable("organizationId") Long tenantId, @RequestBody HmeEqManageTagGroupDTO dto){
        log.info("<====HmeEquipmentManageTaggroupController-deleteManageTag:{},{}",tenantId, dto);
        repository.deleteManageTag(tenantId, dto);
        return Results.success();
    }

    /**
     * 设备管理组查询LOV
     * @param tenantId
     * @param hmeManageTagDTO
     * @return
     */
    @ApiModelProperty(value = "设备管理组查询LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/manage-tag-query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeMtTagVO>> queryManageTag(@PathVariable("organizationId") Long tenantId, HmeManageTagDTO hmeManageTagDTO,@ApiIgnore PageRequest pageRequest){
        log.info("<====HmeEquipmentManageTaggroupController-/manageTagQuery:{},{}",tenantId, hmeManageTagDTO);
        return Results.success(repository.queryManageTag(tenantId, hmeManageTagDTO, pageRequest));
    }

    @ApiOperation(value = "设备点检保养项目")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/export")
    @ExcelExport(HmeEquipmentTagVO2.class)
    public ResponseEntity<List<HmeEquipmentTagVO2>> export(@PathVariable("organizationId") Long tenantId,
                                                                 HmeEqManageTagGroup hmeEqManageTagGroup,
                                                                 ExportParam exportParam,
                                                                 HttpServletResponse response) {
        return Results.success(repository.export(tenantId, hmeEqManageTagGroup));
    }
}
