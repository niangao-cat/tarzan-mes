package tarzan.general.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.general.api.dto.*;
import tarzan.general.app.service.MtUserOrganizationService;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtOrganizationVO;

/**
 * 用户组织关系表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:40
 */
@RestController("mtUserOrganizationController.v1")
@RequestMapping("/v1/{organizationId}/mt-user-organization")
@Api(tags = "MtUserOrganization")
public class MtUserOrganizationController extends BaseController {
    @Autowired
    private MtUserOrganizationRepository repository;

    @Autowired
    private MtUserOrganizationService service;

    @ApiOperation(value = "userOrganizationPermissionQuery")
    @PostMapping(value = {"/permission"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtUserOrganization>> userOrganizationPermissionQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtUserOrganizationDTO dto) {
        ResponseData<List<MtUserOrganization>> result = new ResponseData<List<MtUserOrganization>>();
        try {
            MtUserOrganization param = new MtUserOrganization();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.userOrganizationPermissionQuery(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "userDefaultOrganizationGet")
    @PostMapping(value = {"/default"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUserOrganization> userDefaultOrganizationGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtUserOrganizationDTO dto) {
        ResponseData<MtUserOrganization> result = new ResponseData<MtUserOrganization>();
        try {
            MtUserOrganization param = new MtUserOrganization();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.userDefaultOrganizationGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "userOrganizationPermissionValidate")
    @PostMapping(value = {"/permission/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> userOrganizationPermissionValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtUserOrganizationDTO2 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            MtUserOrganization param = new MtUserOrganization();
            BeanUtils.copyProperties(dto, param);
            repository.userOrganizationPermissionValidate(tenantId, param);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取用户权限信息UI")
    @GetMapping(value = "/property/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtUserOrganizationDTO4>> userOrganizationSearch(
                    @PathVariable("organizationId") Long tenantId, MtUserOrganizationDTO4 dto,
                    @ApiIgnore @SortDefault(value = MtUserOrganization.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtUserOrganizationDTO4>> result = new ResponseData<Page<MtUserOrganizationDTO4>>();
        try {
            result.setRows(service.userOrganizationQuery(pageRequest, tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户权限信息维护")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUserOrganization> userOrganizationSave(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtUserOrganizationDTO3 dto) {
        validObject(dto);
        ResponseData<MtUserOrganization> result = new ResponseData<MtUserOrganization>();
        try {
            result.setRows(service.userOrganizationSave(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "组织对象查询(LOV查询)")
    @GetMapping(value = "/organization/lov/ui")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtOrganizationVO>> organizationLovSearch(@PathVariable("organizationId") Long tenantId,
                    MtOrganizationDTO dto, @ApiIgnore @SortDefault(value = MtUserOrganization.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(service.mtOrganizationSearch(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "UI用户组织查询(LOV)")
    @GetMapping(value = "/user/organization/lov/ui")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtUserOrganizationDTO4>> userOrganizationLovForUi(
                    @PathVariable("organizationId") Long tenantId, MtUserOrganizationDTO5 dto,
                    @ApiIgnore @SortDefault(value = MtUserOrganization.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(service.userOrganizationLovForUi(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "UI用户组织对象关系查询(LOV)")
    @GetMapping(value = "/user/organization/rel/lov/ui")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtUserOrganizationDTO4>> userOrganizationRelLovForUi(
                    @PathVariable("organizationId") Long tenantId, MtUserOrganizationDTO5 dto,
                    @ApiIgnore @SortDefault(value = MtUserOrganization.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(service.userOrganizationRelLovForUi(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "获取用户默认站点UI")
    @GetMapping(value = "/user/default/site/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUserOrganizationDTO6> userDefaultSiteForUi(@PathVariable("organizationId") Long tenantId) {
        ResponseData<MtUserOrganizationDTO6> result = new ResponseData<MtUserOrganizationDTO6>();
        try {
            result.setRows(service.userDefaultSiteForUi(tenantId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取用户站点列表UI")
    @GetMapping(value = "/user/site/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtUserOrganizationDTO6>> userSiteListForUi(@PathVariable("organizationId") Long tenantId) {
        ResponseData<List<MtUserOrganizationDTO6>> result = new ResponseData<List<MtUserOrganizationDTO6>>();
        try {
            result.setRows(service.userSiteListForUi(tenantId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
