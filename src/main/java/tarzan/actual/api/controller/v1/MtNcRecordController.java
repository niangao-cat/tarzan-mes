package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.api.dto.MtNcRecordDTO;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.actual.domain.repository.MtNcRecordRepository;
import tarzan.actual.domain.vo.MtNcRecordVO1;
import tarzan.actual.domain.vo.MtNcRecordVO2;
import tarzan.actual.domain.vo.MtNcRecordVO3;
import tarzan.actual.domain.vo.MtNcRecordVO4;
import tarzan.actual.domain.vo.MtNcRecordVO5;
import tarzan.actual.domain.vo.MtNcRecordVO6;
import tarzan.actual.domain.vo.MtNcRecordVO7;
import tarzan.actual.domain.vo.MtNcRecordVO8;

/**
 * 不良代码记录 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:55
 */
@RestController("mtNcRecordController.v1")
@RequestMapping("/v1/{organizationId}/mt-nc-record")
@Api(tags = "MtNcRecord")
public class MtNcRecordController extends BaseController {

    @Autowired
    private MtNcRecordRepository repository;

    @ApiOperation(value = "ncRecordPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcRecord> ncRecordPropertyGet(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody String ncRecordId) {
        ResponseData<MtNcRecord> responseData = new ResponseData<MtNcRecord>();
        try {
            responseData.setRows(this.repository.ncRecordPropertyGet(tenantId, ncRecordId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncEventCreate")
    @PostMapping(value = {"/event/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncEventCreate(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody MtNcRecordVO6 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.ncEventCreate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordSourceEoStepActualGet")
    @PostMapping(value = {"/step/actual"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncRecordSourceEoStepActualGet(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody String ncRecordId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.ncRecordSourceEoStepActualGet(tenantId, ncRecordId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncDispositionGroupGet")
    @PostMapping(value = {"/disposition/group"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcRecordVO5> ncDispositionGroupGet(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody String ncRecordId) {
        ResponseData<MtNcRecordVO5> responseData = new ResponseData<MtNcRecordVO5>();
        try {
            responseData.setRows(this.repository.ncDispositionGroupGet(tenantId, ncRecordId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoNcRecordAllClosedValidate")
    @PostMapping(value = {"/not-closed/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoNcRecordAllClosedValidate(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody String eoId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoNcRecordAllClosedValidate(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepNcRecordAllClosedValidate")
    @PostMapping(value = {"/step/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStepNcRecordAllClosedValidate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody String eoStepActualId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStepNcRecordAllClosedValidate(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "incidentLimitNcRecordQuery")
    @PostMapping(value = {"/incident/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcRecord>> incidentLimitNcRecordQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody String ncIncidentId) {
        ResponseData<List<MtNcRecord>> responseData = new ResponseData<List<MtNcRecord>>();
        try {
            responseData.setRows(this.repository.incidentLimitNcRecordQuery(tenantId, ncIncidentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitNcRecordQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcRecord>> propertyLimitNcRecordQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtNcRecord dto) {
        ResponseData<List<MtNcRecord>> responseData = new ResponseData<List<MtNcRecord>>();
        try {
            responseData.setRows(this.repository.propertyLimitNcRecordQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordSecondaryCodeClosedValidate")
    @PostMapping(value = {"/secondary/code/closed/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> ncRecordSecondaryCodeClosedValidate(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody String ncRecordId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.ncRecordSecondaryCodeClosedValidate(tenantId, ncRecordId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordAndHisUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcRecordVO8> ncRecordAndHisUpdate(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtNcRecordVO7 dto) {
        ResponseData<MtNcRecordVO8> responseData = new ResponseData<MtNcRecordVO8>();
        try {
            responseData.setRows(this.repository.ncRecordAndHisUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordCloseVerify")
    @PostMapping(value = {"/close/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncRecordCloseVerify(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody String ncRecordId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.ncRecordCloseVerify(tenantId, ncRecordId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordCloseProcess")
    @PostMapping(value = {"/close/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> ncRecordCloseProcess(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtNcRecordVO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.ncRecordCloseProcess(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncIncidentCloseVerify")
    @PostMapping(value = {"/incident/close/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncIncidentCloseVerify(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody String ncRecordId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            String isClose = this.repository.ncIncidentCloseVerify(tenantId, ncRecordId);
            responseData.setRows(isClose);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoIncidentAndCodeLimitNcRecordGet")
    @PostMapping(value = {"/limit-incident-nc-code/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcRecord> eoIncidentAndCodeLimitNcRecordGet(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtNcRecordDTO dto) {
        ResponseData<MtNcRecord> responseData = new ResponseData<MtNcRecord>();
        try {
            MtNcRecord param = new MtNcRecord();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.eoIncidentAndCodeLimitNcRecordGet(tenantId, param));
            responseData.setSuccess(true);

        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitNcRecordQuery")
    @PostMapping(value = {"/limit-eo/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcRecord>> eoLimitNcRecordQuery(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody String eoId) {
        ResponseData<List<MtNcRecord>> responseData = new ResponseData<List<MtNcRecord>>();
        try {
            responseData.setRows(this.repository.eoLimitNcRecordQuery(tenantId, eoId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoMaxNcLimitValidate")
    @PostMapping(value = {"/max/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoMaxNcLimitValidate(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody MtNcRecordVO1 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoMaxNcLimitValidate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordClose")
    @PostMapping(value = {"/close"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncRecordClose(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody MtNcRecordVO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.ncRecordClose(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordConfirm")
    @PostMapping(value = {"/confirmation"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> ncRecordConfirm(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody MtNcRecordVO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.ncRecordConfirm(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "ncRecordAndHisCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcRecordVO8> ncRecordAndHisCreate(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtNcRecordVO3 dto) {
        ResponseData<MtNcRecordVO8> responseData = new ResponseData<MtNcRecordVO8>();
        try {
            responseData.setRows(this.repository.ncRecordAndHisCreate(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordCancel")
    @PostMapping(value = {"/cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> ncRecordCancel(@PathVariable("organizationId") Long tenantId,
                                             @RequestBody MtNcRecordVO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.ncRecordCancel(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "parentNcCodeLimitNcRecordGet")
    @PostMapping(value = {"/limit-parent-code/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcRecord>> parentNcCodeLimitNcRecordGet(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody String parentNcRecordId) {
        ResponseData<List<MtNcRecord>> responseData = new ResponseData<List<MtNcRecord>>();
        try {
            responseData.setRows(this.repository.parentNcCodeLimitNcRecordGet(tenantId, parentNcRecordId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordCreateProcess")
    @PostMapping(value = {"/register"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> ncRecordCreateProcess(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtNcRecordVO4 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.ncRecordCreateProcess(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncRecordAndIncidentClose")
    @PostMapping(value = {"/incident/close"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncRecordAndIncidentClose(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody MtNcRecordVO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.ncRecordAndIncidentClose(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
