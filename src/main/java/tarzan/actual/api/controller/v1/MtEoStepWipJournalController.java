package tarzan.actual.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 执行作业在制品日记账 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
@RestController("mtEoStepWipJournalController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-step-wip-journal")
@Api(tags = "MtEoStepWipJournal")
public class MtEoStepWipJournalController extends BaseController {

}
