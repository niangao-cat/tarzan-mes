package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.MtRouterSiteAssignDTO;
import tarzan.method.api.dto.MtRouterSiteAssignDTO2;
import tarzan.method.app.service.MtRouterSiteAssignService;
import tarzan.method.domain.entity.MtRouterSiteAssign;
import tarzan.method.domain.repository.MtRouterSiteAssignRepository;

/**
 * 工艺路线站点分配表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@RestController("mtRouterSiteAssignController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-site-assign")
@Api(tags = "MtRouterSiteAssign")
public class MtRouterSiteAssignController extends BaseController {
    @Autowired
    private MtRouterSiteAssignRepository repository;

    @Autowired
    private MtRouterSiteAssignService service;

    @ApiOperation(value = "UI查询工艺路线分配站点列表")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterSiteAssignDTO2>> queryRouterSiteAssignListForUi(
            @PathVariable("organizationId") Long tenantId, String routerId,
            @ApiIgnore @SortDefault(value = MtRouterSiteAssign.FIELD_CREATION_DATE,
                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtRouterSiteAssignDTO2>> responseData = new ResponseData<List<MtRouterSiteAssignDTO2>>();
        try {
            responseData.setRows(this.service.queryRouterSiteAssignListForUi(tenantId, routerId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存工艺路线分配站点")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveRouterSiteAssignForUi(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtRouterSiteAssignDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(this.service.saveRouterSiteAssignForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除工艺路线分配站点")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeRouterSiteAssignForUi(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody String routerSiteAssignId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.service.removeRouterSiteAssignForUi(tenantId, routerSiteAssignId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitRouterSiteAssignQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterSiteAssign>> propertyLimitRouterSiteAssignQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterSiteAssignDTO dto) {
        ResponseData<List<MtRouterSiteAssign>> responseData = new ResponseData<List<MtRouterSiteAssign>>();
        try {
            MtRouterSiteAssign param = new MtRouterSiteAssign();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.propertyLimitRouterSiteAssignQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerSiteAssignAttrPropertyUpdate")
    @PostMapping(value = {"/router/site/assign/attr/property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerSiteAssignAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtExtendVO10 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.routerSiteAssignAttrPropertyUpdate(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
