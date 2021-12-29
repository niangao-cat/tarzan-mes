package com.ruike.itf.app.handler;

import com.ruike.itf.domain.service.QualityAnalyzeIfaceDomainService;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 质量分析文件 任务调用
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 17:50
 */
@JobHandler("QualityAnalyzeIfaceJob")
public class QualityAnalyzeIfaceHandler implements IJobHandler {

    private final QualityAnalyzeIfaceDomainService domainService;

    public QualityAnalyzeIfaceHandler(QualityAnalyzeIfaceDomainService domainService) {
        this.domainService = domainService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        try {
            domainService.invoke(tool.getBelongTenantId());
        } catch (Exception e) {
            tool.error("质量分析文件失败!!!" + e.getMessage());
            tool.error(Arrays.toString(e.getStackTrace()));
            return ReturnT.FAILURE;
        }

        return ReturnT.SUCCESS;
    }
}
