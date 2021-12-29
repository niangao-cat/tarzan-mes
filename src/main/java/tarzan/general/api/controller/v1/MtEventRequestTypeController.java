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
import tarzan.general.api.dto.MtEventRequestTypeDTO;
import tarzan.general.app.service.MtEventRequestTypeService;
import tarzan.general.domain.entity.MtEventRequestType;
import tarzan.general.domain.repository.MtEventRequestTypeRepository;
import tarzan.general.domain.vo.MtEventRequestTypeVO;
import tarzan.general.domain.vo.MtEventRequestTypeVO2;

/**
 * 事件组类型定义 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@RestController("mtEventRequestTypeController.v1")
@RequestMapping("/v1/{organizationId}/mt-event-request-type")
@Api(tags = "MtEventRequestType")
public class MtEventRequestTypeController extends BaseController {

    @Autowired
    private MtEventRequestTypeRepository repository;

    @Autowired
    private MtEventRequestTypeService mtEventRequestTypeService;

    @ApiOperation(value = "propertyLimitEventGroupTypeQuery")
    @PostMapping(value = {"/limit-property/type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEventGroupTypeQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventRequestTypeVO dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.propertyLimitEventGroupTypeQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eventGroupTypeGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventRequestType> eventGroupTypeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String requestTypeId) {
        ResponseData<MtEventRequestType> result = new ResponseData<MtEventRequestType>();
        try {
            result.setRows(repository.eventGroupTypeGet(tenantId, requestTypeId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("事件请求类型维护界面查询")
    @GetMapping(value = "/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEventRequestTypeVO2>> queryForUi(@PathVariable(value = "organizationId") Long tenantId,
                    MtEventRequestTypeDTO dto, @ApiIgnore @SortDefault(value = MtEventRequestType.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtEventRequestTypeVO2>> responseData = new ResponseData<Page<MtEventRequestTypeVO2>>();
        try {
            responseData.setRows(this.mtEventRequestTypeService.queryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("事件请求类型维护页面保存")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtEventRequestTypeVO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtEventRequestTypeService.saveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
