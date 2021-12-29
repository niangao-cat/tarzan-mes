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
import tarzan.general.api.dto.MtEventTypeDTO;
import tarzan.general.api.dto.MtEventTypeDTO2;
import tarzan.general.app.service.MtEventTypeService;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.general.domain.vo.MtEventTypeVO;

/**
 * 事件类型定义 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@RestController("mtEventTypeController.v1")
@RequestMapping("/v1/{organizationId}/mt-event-type")
@Api(tags = "MtEventType")
public class MtEventTypeController extends BaseController {

    @Autowired
    private MtEventTypeRepository repository;

    @Autowired
    private MtEventTypeService service;

    @ApiOperation(value = "propertyLimitEventTypeQuery")
    @PostMapping(value = {"/limit-property/type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEventTypeQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventTypeVO dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.propertyLimitEventTypeQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "eventTypeGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventType> eventTypeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eventTypeId) {
        ResponseData<MtEventType> result = new ResponseData<MtEventType>();
        try {
            result.setRows(repository.eventTypeGet(tenantId, eventTypeId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "保存事件类型UI")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventType> saveMtEvenType(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventTypeDTO dto) {
        ResponseData<MtEventType> result = new ResponseData<MtEventType>();
        try {
            result.setRows(service.saveMtEventType(tenantId, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取事件类型UI")
    @GetMapping(value = "/property/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEventType>> eventTypeQuery(@PathVariable("organizationId") Long tenantId,
                    MtEventTypeDTO2 dto, @ApiIgnore @SortDefault(value = MtEventType.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtEventType>> result = new ResponseData<Page<MtEventType>>();
        try {
            result.setRows(service.eventTypeQuery(tenantId, pageRequest, dto));
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据属性获取事件UI")
    @GetMapping(value = {"/limit-property/type/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEventTypeQueryUi(@PathVariable("organizationId") Long tenantId,
                    MtEventTypeVO dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(service.propertyLimitEventTypeQueryUi(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
