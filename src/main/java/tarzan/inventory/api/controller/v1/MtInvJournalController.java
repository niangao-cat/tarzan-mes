package tarzan.inventory.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.inventory.api.dto.MtInvJournalDTO;
import tarzan.inventory.api.dto.MtInvJournalDTO2;
import tarzan.inventory.api.dto.MtInvJournalDTO4;
import tarzan.inventory.app.service.MtInvJournalService;
import tarzan.inventory.domain.entity.MtInvJournal;
import tarzan.inventory.domain.repository.MtInvJournalRepository;
import tarzan.inventory.domain.vo.MtInvJournalVO;

import java.util.List;

/**
 * 库存日记账 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@RestController("mtInvJournalController.v1")
@RequestMapping("/v1/{organizationId}/mt-inv-journal")
@Api(tags = "MtInvJournal")
public class MtInvJournalController extends BaseController {

    @Autowired
    private MtInvJournalService service;
    @Autowired
    private MtInvJournalRepository repository;

    @ApiOperation(value = "UI根据属性获取库存日记账")
    @PostMapping(value = "/property/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvJournalDTO2>> queryInvJournalForUi(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtInvJournalDTO dto,
                                                                     @ApiIgnore @SortDefault(value = MtInvJournal.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtInvJournalDTO2>> responseData = new ResponseData<List<MtInvJournalDTO2>>();
        try {
            responseData.setRows(this.service.queryInvJournalForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "库存日记账报表")
    @PostMapping(value = "/report/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvJournalDTO2>> queryInvJournalReport(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtInvJournalDTO4 dto,
                                                                      @ApiIgnore @SortDefault(value = MtInvJournal.FIELD_EVENT_TIME,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtInvJournalDTO2>> responseData = new ResponseData<List<MtInvJournalDTO2>>();
        try {
            responseData.setRows(this.service.queryInvJournalReport(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "propertyLimitInvJournalQuery")
    @PostMapping(value = "/property-id/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitInvJournalQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtInvJournalVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitInvJournalQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "invJournalGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInvJournal> invJournalGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String journalId) {
        ResponseData<MtInvJournal> result = new ResponseData<MtInvJournal>();
        try {
            result.setRows(this.repository.invJournalGet(tenantId, journalId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "invJournalBatchGet")
    @PostMapping(value = "/property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvJournal>> invJournalBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> journalIds) {
        ResponseData<List<MtInvJournal>> responseData = new ResponseData<List<MtInvJournal>>();
        try {
            responseData.setRows(this.repository.invJournalBatchGet(tenantId, journalIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
