package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.entity.MtDataRecord;
import tarzan.actual.domain.repository.MtDataRecordRepository;
import tarzan.general.domain.vo.MtDataRecordVO;
import tarzan.general.domain.vo.MtDataRecordVO2;
import tarzan.general.domain.vo.MtDataRecordVO3;
import tarzan.general.domain.vo.MtDataRecordVO4;
import tarzan.general.domain.vo.MtDataRecordVO5;
import tarzan.general.domain.vo.MtDataRecordVO6;

/**
 * 数据收集实绩 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
@RestController("mtDataRecordController.v1")
@RequestMapping("/v1/{organizationId}/mt-data-record")
@Api(tags = "MtDataRecord")
public class MtDataRecordController extends BaseController {
    @Autowired
    private MtDataRecordRepository repository;

    @ApiOperation(value = "dataRecordGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtDataRecord> dataRecordGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String dataRecordId) {

        ResponseData<MtDataRecord> responseData = new ResponseData<MtDataRecord>();
        try {
            responseData.setRows(repository.dataRecordGet(tenantId, dataRecordId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dataRecordBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtDataRecord>> dataRecordBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> dataRecordIdList) {
        ResponseData<List<MtDataRecord>> responseData = new ResponseData<List<MtDataRecord>>();
        try {
            responseData.setRows(repository.dataRecordBatchGet(tenantId, dataRecordIdList));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitDataRecordQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtDataRecord>> propertyLimitDataRecordQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtDataRecordVO5 dto) {
        ResponseData<List<MtDataRecord>> responseData = new ResponseData<List<MtDataRecord>>();
        try {
            responseData.setRows(repository.propertyLimitDataRecordQuery(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dataRecordAndHisCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtDataRecordVO6> dataRecordAndHisCreate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtDataRecordVO createVO) {
        ResponseData<MtDataRecordVO6> responseData = new ResponseData<MtDataRecordVO6>();
        try {
            responseData.setRows(repository.dataRecordAndHisCreate(tenantId, createVO));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "dataRecordAndHisUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtDataRecordVO6> dataRecordAndHisUpdate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtDataRecordVO2 updateVO,
                                                                @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtDataRecordVO6> responseData = new ResponseData<MtDataRecordVO6>();
        try {
            responseData.setRows(repository.dataRecordAndHisUpdate(tenantId, updateVO, fullUpdate));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "allTypeDataRecordCreate")
    @PostMapping(value = {"/all-type/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtDataRecordVO6> allTypeDataRecordCreate(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtDataRecordVO createVO) {

        ResponseData<MtDataRecordVO6> responseData = new ResponseData<MtDataRecordVO6>();
        try {
            responseData.setRows(repository.allTypeDataRecordCreate(tenantId, createVO));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "distributionGroupLimitTagGroupQuery")
    @PostMapping(value = {"/limit-distribution-group/tag-group"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtDataRecordVO4> distributionGroupLimitTagGroupQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtDataRecordVO3 queryVO) {
        ResponseData<MtDataRecordVO4> responseData = new ResponseData<MtDataRecordVO4>();
        try {
            responseData.setRows(repository.distributionGroupLimitTagGroupQuery(tenantId, queryVO));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
