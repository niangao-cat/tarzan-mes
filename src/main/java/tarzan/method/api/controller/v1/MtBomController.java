package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
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
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.*;
import tarzan.method.app.service.MtBomService;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.vo.*;

/**
 * 装配清单头 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom")
@Api(tags = "MtBom")
public class MtBomController extends BaseController {

    @Autowired
    private MtBomService service;

    @Autowired
    private MtBomRepository repository;

    @ApiOperation(value = "bomBasicGet")
    @PostMapping(value = {"/basic"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomVO7> bomBasicGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomId) {
        ResponseData<MtBomVO7> responseData = new ResponseData<MtBomVO7>();
        try {
            responseData.setRows(this.repository.bomBasicGet(tenantId, bomId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomBasicBatchGet")
    @PostMapping(value = {"/basic/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomVO7>> bomBasicBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> bomIds) {
        ResponseData<List<MtBomVO7>> responseData = new ResponseData<List<MtBomVO7>>();
        try {
            responseData.setRows(this.repository.bomBasicBatchGet(tenantId, bomIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitBomQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitBomQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitBomQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomAvailableVerify")
    @PostMapping(value = {"/availability-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomAvailableVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomVO3 condition) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.bomAvailableVerify(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomUpdateVerify")
    @PostMapping(value = {"/editable-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomUpdateVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.bomUpdateVerify(tenantId, bomId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomStatusUpdate")
    @PostMapping(value = {"/status/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomStatusUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomDTO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.bomStatusUpdate(tenantId, dto.getBomId(), dto.getStatus());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomCurrentFlagUpdate")
    @PostMapping(value = {"/current/flag/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomCurrentFlagUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.bomCurrentFlagUpdate(tenantId, bomId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomReleasedFlagUpdate")
    @PostMapping(value = {"/released/flag/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomReleasedFlagUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.bomReleasedFlagUpdate(tenantId, dto.getBomId(), dto.getReleasedFlag());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomCopy")
    @PostMapping(value = {"/copy"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> bomCopy(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomVO2 condition) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.bomCopy(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomAutoRevisionGet")
    @PostMapping(value = {"/auto-revision"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> bomAutoRevisionGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.bomAutoRevisionGet(tenantId, bomId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomRevisionGenerate")
    @PostMapping(value = {"/revision/generate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> bomRevisionGenerate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomVO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.bomRevisionGenerate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomPropertyUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> bomPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomDTO4 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtBom dto1 = new MtBom();
            BeanUtils.copyProperties(dto, dto1);

            responseData.setRows(this.repository.bomPropertyUpdate(tenantId, dto1,fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "sourceBomLimitTargetBomUpdate")
    @PostMapping(value = {"/update/limit-source-bom"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> sourceBomLimitTargetBomUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomVO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.sourceBomLimitTargetBomUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomAllUpdate")
    @PostMapping(value = {"/all/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> bomAllUpdate(@PathVariable("organizationId") Long tenantId, @RequestBody MtBomVO6 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.bomAllUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "装配清单查询(前台)")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtBomDTO>> listUi(@PathVariable("organizationId") Long tenantId, MtBomDTO1 dto,
                    @ApiIgnore @SortDefault(value = MtBom.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtBomDTO>> result = new ResponseData<Page<MtBomDTO>>();
        try {
            result.setRows(service.listUi(tenantId, pageRequest, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "装配清单行明细(前台)")
    @GetMapping(value = {"/one/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomVO9> bomRecordForUi(@PathVariable("organizationId") Long tenantId,
                    @ApiParam(value="装配清单主键",required = true) @RequestParam(value = "bomId") String bomId) {
        ResponseData<MtBomVO9> result = new ResponseData<MtBomVO9>();
        try {
            result.setRows(service.bomRecordForUi(tenantId, bomId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "新增&更新装配清单(前台)")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveBomForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomDTO5 dto) {
        ResponseData<String> result = new ResponseData<String>();
        validObject(dto);
        try {
            result.setRows(service.saveBomForUi(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "复制装配清单行(前台)")
    @PostMapping(value = {"/copy/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> bomCopyForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomDTO6 dto) {
        ResponseData<String> result = new ResponseData<String>();
        validObject(dto);
        try {
            result.setRows(service.bomCopyForUi(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
