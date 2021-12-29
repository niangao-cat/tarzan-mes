package tarzan.actual.api.controller.v1;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.actual.api.dto.MtEoStepWipDTO;
import tarzan.actual.api.dto.MtEoStepWipDTO2;
import tarzan.actual.app.service.MtEoStepWipService;
import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.repository.MtEoStepWipRepository;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业在制品 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
@RestController("mtEoStepWipController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-step-wip")
@Api(tags = "MtEoStepWip")
public class MtEoStepWipController extends BaseController {

    @Autowired
    private MtEoStepWipRepository repository;

    @Autowired
    private MtEoStepWipService service;

    @ApiOperation("UI在制品报表查询")
    @GetMapping(value = "/wip/report/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoStepWipDTO2>> eoStepWipReportForUi(
                    @PathVariable(value = "organizationId") Long tenantId, MtEoStepWipDTO dto,
                    @ApiIgnore PageRequest pageRequest) {
        ResponseData<Page<MtEoStepWipDTO2>> responseData = new ResponseData<Page<MtEoStepWipDTO2>>();
        try {
            responseData.setRows(this.service.eoStepWipReportForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepWipUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepWipUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO3 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepWipUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepWipQuery")
    @PostMapping(value = {"/wkc/wip-qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWip>> eoWkcAndStepWipQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO1 vo) {
        ResponseData<List<MtEoStepWip>> responseData = new ResponseData<List<MtEoStepWip>>();
        try {
            responseData.setRows(this.repository.eoWkcAndStepWipQuery(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepWipBatchGet")
    @PostMapping(value = {"/wkc/batch/wip-qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWip>> eoWkcAndStepWipBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody Map<String, String> vo) {
        ResponseData<List<MtEoStepWip>> responseData = new ResponseData<List<MtEoStepWip>>();
        try {
            responseData.setRows(this.repository.eoWkcAndStepWipBatchGet(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcQueue")
    @PostMapping(value = {"/wkc/queue"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcQueue(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO4 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoWkcQueue(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcQueueCancel")
    @PostMapping(value = {"/wkc/queue-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcQueueCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO4 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoWkcQueueCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoScrappedConfirm")
    @PostMapping(value = {"/scrap/confirmation"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoScrappedConfirm(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO5 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoScrappedConfirm(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcLimitEoQuery")
    @PostMapping(value = {"/limit-wkc/eo/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWipVO6>> wkcLimitEoQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO7 vo) {
        ResponseData<List<MtEoStepWipVO6>> responseData = new ResponseData<List<MtEoStepWipVO6>>();
        try {
            responseData.setRows(this.repository.wkcLimitEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcWipLimitEoQuery")
    @PostMapping(value = {"/limit-wkc-wip/eo/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWipVO6>> wkcWipLimitEoQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO7 vo) {
        ResponseData<List<MtEoStepWipVO6>> responseData = new ResponseData<List<MtEoStepWipVO6>>();
        try {
            responseData.setRows(this.repository.wkcWipLimitEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepWipBatchQuery")
    @PostMapping(value = {"/wkc/wip-qty-batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWip>> eoWkcAndStepWipBatchQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtEoStepWipVO1> vo) {
        ResponseData<List<MtEoStepWip>> responseData = new ResponseData<List<MtEoStepWip>>();
        try {
            responseData.setRows(this.repository.eoWkcAndStepWipBatchQuery(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepWipUpdateQtyCalculate")
    @PostMapping(value = {"/wip/update-qty/calculate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> eoStepWipUpdateQtyCalculate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO8 vo) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.eoStepWipUpdateQtyCalculate(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcBatchQueue")
    @PostMapping(value = {"/wkc/batch/queue"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcBatchQueue(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO17 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoWkcBatchQueue(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepWipBatchUpdate")
    @PostMapping(value = {"/batch/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWipVO12>> eoStepWipBatchUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoStepWipVO13 vo) {
        ResponseData<List<MtEoStepWipVO12>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStepWipBatchUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepWipUpdateQtyBatchCalculate")
    @PostMapping(value = {"/wip/update-qty/batch/calculate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoStepWipVO20>> eoStepWipUpdateQtyBatchCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtEoStepWipVO18> vo) {
        ResponseData<List<MtEoStepWipVO20>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStepWipUpdateQtyBatchCalculate(tenantId, vo));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
