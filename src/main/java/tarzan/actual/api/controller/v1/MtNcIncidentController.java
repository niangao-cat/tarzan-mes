package tarzan.actual.api.controller.v1;

import org.hzero.core.base.BaseController;
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
import tarzan.actual.api.dto.MtNcIncidentDTO;
import tarzan.actual.domain.entity.MtNcIncident;
import tarzan.actual.domain.repository.MtNcIncidentRepository;
import tarzan.actual.domain.vo.MtNcIncidentVO1;
import tarzan.actual.domain.vo.MtNcIncidentVO2;
import tarzan.actual.domain.vo.MtNcIncidentVO3;

/**
 * 不良事故 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:48
 */
@RestController("mtNcIncidentController.v1")
@RequestMapping("/v1/{organizationId}/mt-nc-incident")
@Api(tags = "MtNcIncident")
public class MtNcIncidentController extends BaseController {

    @Autowired
    private MtNcIncidentRepository repository;

    @ApiOperation(value = "ncIncidentNumLimitNcIncidentGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcIncident> ncIncidentNumLimitNcIncidentGet(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtNcIncidentDTO dto) {
        ResponseData<MtNcIncident> responseData = new ResponseData<MtNcIncident>();
        try {
            responseData.setRows(this.repository.ncIncidentNumLimitNcIncidentGet(tenantId, dto.getSiteId(),
                            dto.getIncidentNumber()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncIncidentAndHisCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcIncidentVO3> ncIncidentAndHisCreate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtNcIncidentVO1 dto) {
        ResponseData<MtNcIncidentVO3> responseData = new ResponseData<MtNcIncidentVO3>();
        try {
            responseData.setRows(this.repository.ncIncidentAndHisCreate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncIncidentAndHisUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcIncidentVO3> ncIncidentAndHisUpdate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtNcIncidentVO2 dto) {
        ResponseData<MtNcIncidentVO3> responseData = new ResponseData<MtNcIncidentVO3>();
        try {
            responseData.setRows(this.repository.ncIncidentAndHisUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
