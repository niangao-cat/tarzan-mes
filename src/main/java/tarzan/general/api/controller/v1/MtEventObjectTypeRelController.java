package tarzan.general.api.controller.v1;

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
import tarzan.general.api.dto.MtEventObjectTypeRelDTO;
import tarzan.general.api.dto.MtEventObjectTypeRelDTO2;
import tarzan.general.app.service.MtEventObjectTypeRelService;
import tarzan.general.domain.entity.MtEventObjectTypeRel;
import tarzan.general.domain.repository.MtEventObjectTypeRelRepository;
import tarzan.general.domain.vo.*;

/**
 * 事件类型与对象类型关系定义 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@RestController("mtEventObjectTypeRelController.v1")
@RequestMapping("/v1/{organizationId}/mt-event-object-type-rel")
@Api(tags = "MtEventObjectTypeRel")
public class MtEventObjectTypeRelController extends BaseController {

    @Autowired
    private MtEventObjectTypeRelService service;

    @Autowired
    private MtEventObjectTypeRelRepository repository;

    @ApiOperation(value = "UI根据事件获取事件影响对象信息")
    @GetMapping(value = {"/limit-type/property/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventObjectTypeRelVO6>> eventLimitEventObjectInfoQueryForUi(
                    @PathVariable("organizationId") Long tenantId, MtEventObjectTypeRelVO5 vo) {
        ResponseData<List<MtEventObjectTypeRelVO6>> result = new ResponseData<List<MtEventObjectTypeRelVO6>>();
        try {
            result.setRows(service.eventLimitEventObjectInfoQueryForUi(tenantId, vo));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eventTypeLimitObjectTypeQuery")
    @PostMapping(value = {"/limit-property/object-type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventObjectTypeRelVO2>> eventTypeLimitObjectTypeQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEventObjectTypeRelVO1 dto) {
        ResponseData<List<MtEventObjectTypeRelVO2>> result = new ResponseData<>();
        try {
            result.setRows(repository.eventTypeLimitObjectTypeQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "objectTypeLimitEventTypeQuery")
    @PostMapping(value = {"/limit-object/event-type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventObjectTypeRelVO4>> objectTypeLimitEventTypeQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEventObjectTypeRelVO3 dto) {
        ResponseData<List<MtEventObjectTypeRelVO4>> result = new ResponseData<List<MtEventObjectTypeRelVO4>>();
        try {
            result.setRows(repository.objectTypeLimitEventTypeQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eventLimitEventObjectInfoQuery")
    @PostMapping(value = {"/limit-event/object"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventObjectTypeRelVO6>> eventLimitEventObjectInfoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEventObjectTypeRelVO5 dto) {
        ResponseData<List<MtEventObjectTypeRelVO6>> result = new ResponseData<List<MtEventObjectTypeRelVO6>>();
        try {
            result.setRows(repository.eventLimitEventObjectInfoQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "requestLimitEventObjectInfoQuery")
    @PostMapping(value = {"/limit-request/object"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventObjectTypeRelVO8>> requestLimitEventObjectInfoQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEventObjectTypeRelVO7 dto) {
        ResponseData<List<MtEventObjectTypeRelVO8>> result = new ResponseData<List<MtEventObjectTypeRelVO8>>();
        try {
            result.setRows(repository.requestLimitEventObjectInfoQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据事件类型获取关联的对象类型UI")
    @GetMapping(value = {"/limit-property/object-type/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventObjectTypeRelVO2>> eventTypeLimitObjectTypeQueryUi(
                    @PathVariable("organizationId") Long tenantId, MtEventObjectTypeRelVO1 dto) {
        ResponseData<List<MtEventObjectTypeRelVO2>> result = new ResponseData<>();
        try {
            result.setRows(service.eventTypeLimitObjectTypeQueryUi(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据事件类型ID获取关联的对象类型UI")
    @GetMapping(value = "/limit-id/property/list/ui")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEventObjectTypeRelDTO2>> queryRelByEventTypeId(
                    @PathVariable("organizationId") Long tenantId,
                    @ApiIgnore @SortDefault(value = MtEventObjectTypeRel.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest,
                    @RequestParam(value = "eventTypeId") String eventTypeId) {
        ResponseData<Page<MtEventObjectTypeRelDTO2>> result = new ResponseData<>();
        try {
            result.setRows(service.eventTypeIdLimitRel(tenantId, pageRequest, eventTypeId));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "保存事件类型对应的对象类型UI")
    @PostMapping(value = "/save/object-type/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventObjectTypeRelDTO2> saveMtEventTypeRelObjectType(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEventObjectTypeRelDTO dto) {
        ResponseData<MtEventObjectTypeRelDTO2> result = new ResponseData<MtEventObjectTypeRelDTO2>();
        try {
            result.setRows(service.saveEventObjectTypeRel(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }
}
