package tarzan.calendar.api.controller.v1;


import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
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
import tarzan.calendar.api.dto.*;
import tarzan.calendar.app.service.MtCalendarOrgRelService;
import tarzan.calendar.domain.entity.MtCalendarOrgRel;
import tarzan.calendar.domain.repository.MtCalendarOrgRelRepository;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO1;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO2;
import tarzan.calendar.domain.vo.MtCalendarOrgRelVO3;

/**
 * 日历组织关系表 管理 API
 *
 * @author peng.yuan@hand-china.com 2019-09-19 10:50:16
 */
@RestController("mtCalendarOrgRelController.v1")
@RequestMapping("/v1/{organizationId}/mt-calendar-org-rel")
@Api(tags = "MtCalendarOrgRel")
public class MtCalendarOrgRelController extends BaseController {

    @Autowired
    private MtCalendarOrgRelService service;

    @ApiOperation(value = "UI查询工作日历分配组织")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtCalendarOrgRelDTO>> queryCalendarOrgRelForUi(
                    @PathVariable("organizationId") Long tenantId, MtCalendarOrgRelDTO1 dto,
                    @ApiIgnore @SortDefault(value = MtCalendarOrgRel.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtCalendarOrgRelDTO>> responseData = new ResponseData<Page<MtCalendarOrgRelDTO>>();
        try {
            responseData.setRows(service.queryCalendarOrgRelForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存工作日历分配组织(批量)")
    @PostMapping(value = {"/save/batch/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarOrgRelDTO>> saveCalendarOrgRelBatchForUi(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarOrgRelDTO2 vo) {
        ResponseData<List<MtCalendarOrgRelDTO>> responseData = new ResponseData<List<MtCalendarOrgRelDTO>>();
        try {
            responseData.setRows(service.saveCalendarOrgRelBatchForUi(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除工作日历分配组织(批量)")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeCalendarOrgRelBatchForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> calendarOrgRelIdList) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeCalendarOrgRelBatchForUi(tenantId, calendarOrgRelIdList);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询组织树(限制用户权限)")
    @GetMapping(value = "/tree/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarOrgRelDTO4>> getOrganizationTreeForUi(
                    @PathVariable(value = "organizationId") Long tenantId, MtCalendarOrgRelDTO5 dto) {
        ResponseData<List<MtCalendarOrgRelDTO4>> responseData = new ResponseData<List<MtCalendarOrgRelDTO4>>();
        try {
            responseData.setRows(this.service.userLimitOrganizationTreeSingleForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtCalendarOrgRelRepository relRepository;

    @ApiOperation("calendarLimitOrganizationRelationQuery")
    @PostMapping(value = {"/organization/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarOrgRelVO>> calendarLimitOrganizationRelationQuery(
                    @PathVariable("organizationId") Long tenanId, @RequestBody String calendarId) {
        ResponseData<List<MtCalendarOrgRelVO>> responseData = new ResponseData<List<MtCalendarOrgRelVO>>();
        try {
            responseData.setRows(relRepository.calendarLimitOrganizationRelationQuery(tenanId, calendarId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("calendarOrganizationRelationUpdate")
    @PostMapping(value = {"/organization/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> calendarOrganizationRelationUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtCalendarOrgRelVO1 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(relRepository.calendarOrganizationRelationUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitCalendarOrganizationRelationQuery")
    @PostMapping(value = {"/organization/limit/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarOrgRelVO2>> propertyLimitCalendarOrganizationRelationQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarOrgRelVO2 vo) {
        ResponseData<List<MtCalendarOrgRelVO2>> responseData = new ResponseData<List<MtCalendarOrgRelVO2>>();
        try {
            responseData.setRows(relRepository.propertyLimitCalendarOrganizationRelationQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitCalendarOrganizationRelationBatchQuery")
    @PostMapping(value = {"/organization/limit/batch-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtCalendarOrgRelVO2>> propertyLimitCalendarOrganizationRelationBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtCalendarOrgRelVO3 vo) {
        ResponseData<List<MtCalendarOrgRelVO2>> responseData = new ResponseData<List<MtCalendarOrgRelVO2>>();
        try {
            responseData.setRows(relRepository.propertyLimitCalendarOrganizationRelationBatchQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
