package tarzan.instruction.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.instruction.app.service.MtBusinessInstructionTypeRService;
import tarzan.instruction.domain.repository.MtBusinessInstructionTypeRRepository;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO2;

/**
 * 业务类型与指令移动类型关系表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@RestController("mtBusinessInstructionTypeRController.v1")
@RequestMapping("/v1/{organizationId}/mt-business-instruction-type-r")
@Api(tags = "MtBusinessInstructionTypeR")
public class MtBusinessInstructionTypeRController extends BaseController {

    @Autowired
    private MtBusinessInstructionTypeRRepository repository;

    @Autowired
    private MtBusinessInstructionTypeRService service;

    @ApiOperation("businessInstructionTypeRelUpdate")
    @PostMapping("/type/rel/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> businessInstructionTypeRelUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtBusinessInstructionTypeRVO dto) {

        ResponseData<String> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.businessInstructionTypeRelUpdate(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("businessInstructionTypeRelPropertyBatchGet")
    @PostMapping("/property/batch/get")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBusinessInstructionTypeRVO>> businessInstructionTypeRelPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> relationIds) {

        ResponseData<List<MtBusinessInstructionTypeRVO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.businessInstructionTypeRelPropertyBatchGet(tenantId, relationIds));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitBusinessInstructionTypeRelQuery")
    @PostMapping("/type/rel/query")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBusinessInstructionTypeRVO2>> propertyLimitBusinessInstructionTypeRelQuery(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtBusinessInstructionTypeRVO dto) {

        ResponseData<List<MtBusinessInstructionTypeRVO2>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.propertyLimitBusinessInstructionTypeRelQuery(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("业务指令类型查询")
    @GetMapping("/list/ui")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtBusinessInstructionTypeRVO2>> businessInstructionTypeRelQuery(
                    @PathVariable(value = "organizationId") Long tenantId, MtBusinessInstructionTypeRVO dto,
                    @ApiIgnore PageRequest pageRequest) {

        ResponseData<Page<MtBusinessInstructionTypeRVO2>> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.propertyLimitBusinessInstructionTypeRelQueryForUi(tenantId, dto, pageRequest));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("保存或更新业务与指令类型")
    @PostMapping("/save/ui")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> businessInstructionTypeRelUpdateForUi(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtBusinessInstructionTypeRVO dto) {

        ResponseData<String> responseData = new ResponseData<>();
        try {
            responseData.setRows(service.businessInstructionTypeRelUpdateForUi(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


}
