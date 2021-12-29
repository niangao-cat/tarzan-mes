package tarzan.modeling.api.controller.v1;

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
import tarzan.modeling.api.dto.*;
import tarzan.modeling.app.service.MtModOrganizationRelService;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.*;

/**
 * 组织结构关系 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModOrganizationRelController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-organization-rel")
@Api(tags = "MtModOrganizationRel")
public class MtModOrganizationRelController extends BaseController {

    @Autowired
    private MtModOrganizationRelService service;

    @Autowired
    private MtModOrganizationRelRepository repository;

    @ApiOperation("UI根据父节点对象获取子节点信息")
    @GetMapping(value = "/limit-parent/children/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModOrganizationChildrenVO> getChildrenByParentForUi(
            @PathVariable(value = "organizationId") Long tenantId, MtModOrganizationRelDTO7 dto) {
        ResponseData<MtModOrganizationChildrenVO> responseData = new ResponseData<MtModOrganizationChildrenVO>();
        try {
            responseData.setRows(this.service.getChildrenByParentForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI构建完整树")
    @GetMapping(value = "/tree/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModOrganizationChildrenVO> getOrganizationTreeForUi(
            @PathVariable(value = "organizationId") Long tenantId) {
        ResponseData<MtModOrganizationChildrenVO> responseData = new ResponseData<MtModOrganizationChildrenVO>();
        try {
            responseData.setRows(this.service.getOrganizationTreeForUi(tenantId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI构建单层树")
    @PostMapping(value = "/tree/single/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModOrganizationSingleChildrenVO>> getOrganizationTreeSingleForUi(
            @PathVariable(value = "organizationId") Long tenantId,
            @RequestBody MtModOrganizationSingleNodeVO dto) {
        ResponseData<List<MtModOrganizationSingleChildrenVO>> responseData =
                new ResponseData<List<MtModOrganizationSingleChildrenVO>>();
        try {
            responseData.setRows(this.service.getOrganizationTreeSingleForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("UI树状图复制粘贴功能")
    @PostMapping(value = "/copy/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> copyForUi(@PathVariable(value = "organizationId") Long tenantId,
                                        @RequestBody MtModOrganizationCopyVO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            validObject(dto);
            this.service.copyForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI树状图删除节点功能")
    @PostMapping(value = "/delete/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> deleteForUi(@PathVariable(value = "organizationId") Long tenantId,
                                          @RequestBody MtModOrganizationDTO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            validObject(dto);
            this.service.deleteForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI树状图新增节点功能")
    @PostMapping(value = "/assign/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assignForUi(@PathVariable(value = "organizationId") Long tenantId,
                                          @RequestBody MtModOrganizationDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            validObject(dto);
            this.service.assignForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI 树状图剪切节点功能")
    @PostMapping(value = "/cut/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> cutForUi(@PathVariable(value = "organizationId") Long tenantId,
                                       @RequestBody MtModOrganizationCopyVO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            validObject(dto);
            this.service.cutForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI获取当前节点同层级的所有的组织的顺序(包含库存)")
    @GetMapping(value = "/node/order/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtModOrganizationDTO6>> currentNodeOrderUi(
            @PathVariable(value = "organizationId") Long tenantId, MtModOrganizationDTO5 dto,
            @ApiIgnore @SortDefault(value = MtModOrganizationRel.FIELD_CREATION_DATE,
                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtModOrganizationDTO6>> responseData = new ResponseData<Page<MtModOrganizationDTO6>>();
        try {
            responseData.setRows(this.service.currentNodeOrderUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI节点顺序修改")
    @PostMapping(value = "/node/order/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> nodeOrderSaveUi(@PathVariable(value = "organizationId") Long tenantId,
                                                @RequestBody MtModOrganizationDTO7 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            validObject(dto);
            responseData.setRows(this.service.nodeOrderSaveUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI根据组织获取货位LOV")
    @GetMapping(value = "/locator/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtModLocator>> organizationLimitLocatorForLovQuery(
            @PathVariable(value = "organizationId") Long tenantId, MtModOrganizationRelDTO6 dto,
            @ApiIgnore @SortDefault(value = MtModOrganizationRel.FIELD_CREATION_DATE,
                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.service.organizationLimitLocatorForLovQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation("UI查找不存在于父节点的组织")
    @GetMapping(value = "/not-exist/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtModOrganizationDTO4>> notExistOrganizationForUi(
            @PathVariable(value = "organizationId") Long tenantId, MtModOrganizationDTO3 dto,
            @ApiIgnore @SortDefault(value = MtModOrganizationRel.FIELD_CREATION_DATE,
                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.service.notExistOrganizationForUi(tenantId, dto, pageRequest));
    }

    @ApiOperation("UI根据顶层站点，获取该站点下传入组织类型的数据")
    @GetMapping(value = "/top-site/org-type/limit/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtModOrganizationRelDTO8>> topSiteLimitOrganizationLovForUi(
            @PathVariable(value = "organizationId") Long tenantId, MtModOrganizationRelDTO9 dto,
            @ApiIgnore @SortDefault(value = MtModOrganizationRel.FIELD_CREATION_DATE,
                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(this.service.topSiteLimitOrganizationLovForUi(tenantId, dto, pageRequest));
    }


    @ApiOperation("siteOrganizationRelVerify")
    @PostMapping(value = "/site-type-rel-verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> siteOrganizationRelVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                          @RequestBody MtModOrganizationRelDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.siteOrganizationRelVerify(tenantId, dto.getSiteId(),
                    dto.getParentOrganizationType(), dto.getParentOrganizationId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setRows("N");
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("areaOrganizationRelVerify")
    @PostMapping(value = "/area-top-type-rel-verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> areaOrganizationRelVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                          @RequestBody MtModOrganizationRelDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.areaOrganizationRelVerify(tenantId, dto.getAreaId(),
                    dto.getTopSiteId(), dto.getParentOrganizationType(), dto.getParentOrganizationId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setRows("N");
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("workcellOrganizationRelVerify")
    @PostMapping(value = "/workcell-top-type-rel-verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> workcellOrganizationRelVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                              @RequestBody MtModOrganizationRelDTO3 dto) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.workcellOrganizationRelVerify(tenantId, dto.getWorkcellId(),
                    dto.getTopSiteId(), dto.getParentOrganizationType(), dto.getParentOrganizationId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setRows("N");
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("prodLineOrganizationRelVerify")
    @PostMapping(value = "/prodline-top-type-rel-verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> prodLineOrganizationRelVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                              @RequestBody MtModOrganizationRelDTO4 dto) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.prodLineOrganizationRelVerify(tenantId, dto.getProdLineId(),
                    dto.getTopSiteId(), dto.getParentOrganizationType(), dto.getParentOrganizationId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setRows("N");
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("organizationRelCopy")
    @PostMapping(value = "/tree-copy", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> organizationRelCopy(@PathVariable(value = "organizationId") Long tenantId,
                                                  @RequestBody MtModOrganizationCopyVO dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.organizationRelCopy(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("organizationRelDelete")
    @PostMapping(value = "/tree-remove", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> organizationRelDelete(@PathVariable(value = "organizationId") Long tenantId,
                                                    @RequestBody MtModOrganizationRelDTO5 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtModOrganizationRel param = new MtModOrganizationRel();
            BeanUtils.copyProperties(dto, param);
            this.repository.organizationRelDelete(tenantId, param);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("subOrganizationRelQuery")
    @PostMapping(value = "/tree-child", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModOrganizationItemVO>> subOrganizationRelQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModOrganizationVO2 dto) {

        ResponseData<List<MtModOrganizationItemVO>> responseData = new ResponseData<List<MtModOrganizationItemVO>>();
        try {
            responseData.setRows(this.repository.subOrganizationRelQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("parentOrganizationRelQuery")
    @PostMapping(value = "/tree/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModOrganizationItemVO>> parentOrganizationRelQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModOrganizationVO2 dto) {
        ResponseData<List<MtModOrganizationItemVO>> responseData = new ResponseData<List<MtModOrganizationItemVO>>();
        try {
            responseData.setRows(this.repository.parentOrganizationRelQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("parentOrganizationRelBatchQuery")
    @PostMapping(value = "/parent/batch/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModOrganizationVO5>> parentOrganizationRelBatchQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModOrganizationVO4 dto) {
        ResponseData<List<MtModOrganizationVO5>> responseData = new ResponseData<List<MtModOrganizationVO5>>();
        try {
            responseData.setRows(this.repository.parentOrganizationRelBatchQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("organizationLimitSiteQuery")
    @PostMapping(value = "/organization-limit-site", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> organizationLimitSiteQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                 @RequestBody MtModOrganizationRelVO dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.organizationLimitSiteQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("organizationLimitSiteBatchQuery")
    @PostMapping(value = "/organization-limit-batch-site", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModOrganizationRelVO3>> organizationLimitSiteBatchQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModOrganizationRelVO2 dto) {

        ResponseData<List<MtModOrganizationRelVO3>> responseData = new ResponseData<List<MtModOrganizationRelVO3>>();
        try {
            responseData.setRows(this.repository.organizationLimitSiteBatchQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


}