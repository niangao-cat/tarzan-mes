package tarzan.general.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.api.dto.MtTagDTO1;
import tarzan.general.api.dto.MtTagDTO2;
import tarzan.general.app.service.MtTagService;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtMqttMessageVO1;
import tarzan.general.domain.vo.MtMqttMessageVO3;
import tarzan.general.domain.vo.MtTagVO;
import tarzan.general.domain.vo.MtTagVO2;
import tarzan.general.domain.vo.MtTagVO3;
import tarzan.general.domain.vo.MtTagVO4;

/**
 * 数据收集项表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag")
@Api(tags = "MtTag")
public class MtTagController extends BaseController {
    @Autowired
    private MtTagRepository repository;

    @Autowired
    private MtTagService service;

    @ApiOperation(value = "tagGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtTag> tagGet(@PathVariable("organizationId") Long tenantId, @RequestBody String tagId) {
        ResponseData<MtTag> responseData = new ResponseData<MtTag>();
        try {
            responseData.setRows(this.repository.tagGet(tenantId, tagId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "tagCodeAndGroupCodeLimitTagGroupAssignGet")
    @PostMapping(value = {"/limit-code-group"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> tagCodeAndGroupCodeLimitTagGroupAssignGet(@PathVariable("organizationId") Long tenantId,
                                                                                @RequestBody MtTagVO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.tagCodeAndGroupCodeLimitTagGroupAssignGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitTagQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitTagQuery(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtTagDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtTag param = new MtTag();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.propertyLimitTagQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "edginkMessageAnalysis")
    @PostMapping(value = {"/edgink/message/analysis"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMqttMessageVO3> edginkMessageAnalysis(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtMqttMessageVO1 dto) {
        ResponseData<MtMqttMessageVO3> responseData = new ResponseData<MtMqttMessageVO3>();
        try {
            responseData.setRows(this.repository.edginkMessageAnalysis(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "edginkMessageAnalysisAndRecordProcess")
    @PostMapping(value = {"/edgink/message/analysis-process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> edginkMessageAnalysisAndRecordProcess(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMqttMessageVO1 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.edginkMessageAnalysisAndRecordProcess(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "tagBatchUpdate")
    @PostMapping(value = {"/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> tagBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                             @RequestBody List<MtTagVO2> voList,
                                             @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(this.repository.tagBatchUpdate(tenantId, voList, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询数据采集项信息")
    @GetMapping(value = {"/query/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtTagDTO1>> tagQueryForUi(@PathVariable("organizationId") Long tenantId,
                                                       @ApiIgnore @SortDefault(value = MtTag.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest,
                                                       MtTagDTO dto) {
        ResponseData<Page<MtTagDTO1>> responseData = new ResponseData<Page<MtTagDTO1>>();
        try {
            responseData.setRows(service.tagQueryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询数据采集项明细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtTagDTO1> queryTagDetailForUi(@PathVariable("organizationId") Long tenantId, String tagId) {
        ResponseData<MtTagDTO1> responseData = new ResponseData<MtTagDTO1>();
        try {
            responseData.setRows(service.queryTagDetailForUi(tenantId, tagId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存数据采集项信息")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> tagSaveForUi(@PathVariable("organizationId") Long tenantId, @RequestBody MtTagDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.tagSaveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI复制数据采集项信息")
    @PostMapping(value = {"/copy/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> tagCopyForUi(@PathVariable("organizationId") Long tenantId,
                                             @RequestBody MtTagDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.tagCopyForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitTagPropertyQuery")
    @PostMapping(value = {"/tag/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtTagVO4>> propertyLimitTagPropertyQuery(@PathVariable("organizationId") Long tenantId, @RequestBody MtTagVO3 dto) {
        ResponseData<List<MtTagVO4>> result = new ResponseData<List<MtTagVO4>>();
        try {
            result.setRows(repository.propertyLimitTagPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
