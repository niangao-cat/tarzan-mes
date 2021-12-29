package com.ruike.itf.app.handler;


import com.ruike.itf.app.service.ItfSrmInstructionIfaceService;
import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时发送送货单状态
 *
 * @author kejin.liu01@hand-china.com 2020/9/8 14:56
 */
@JobHandler("ItfSrmInstructionHandler")
public class ItfSrmInstructionHandler implements IJobHandler {

    private final ItfSrmInstructionIfaceService itfSrmInstructionIfaceService;

    private final MtInstructionDocMapper mtInstructionDocMapper;

    private final LovAdapter lovAdapter;

    public ItfSrmInstructionHandler(ItfSrmInstructionIfaceService itfSrmInstructionIfaceService,
                                    MtInstructionDocMapper mtInstructionDocMapper, LovAdapter lovAdapter) {
        this.itfSrmInstructionIfaceService = itfSrmInstructionIfaceService;
        this.mtInstructionDocMapper = mtInstructionDocMapper;
        this.lovAdapter = lovAdapter;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        Long belongTenantId = tool.getBelongTenantId();
        List<LovValueDTO> instructionDocTypes = lovAdapter.queryLovValue("WMS.DELIVERY_TICKET_QUERY", belongTenantId);
        List<LovValueDTO> instructionDocStatus = lovAdapter.queryLovValue("WMS.SRM_DT_STATUS_LIMITED", belongTenantId);
        List<MtInstructionDoc> docs = new ArrayList<>();
        List<String> docType = instructionDocTypes.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        List<String> status = instructionDocStatus.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(docType) && CollectionUtils.isNotEmpty(status)) {
            String docTypes = "('" + StringUtils.join(docType, "','") + "')";
            String statuss = "('" + StringUtils.join(status, "','") + "')";
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocType(docTypes);
            mtInstructionDoc.setInstructionDocStatus(statuss);
            docs = mtInstructionDocMapper.selectDocByTypes(mtInstructionDoc);

        }
        List<ItfSrmInstructionIface> ifaces = new ArrayList<>();
        for (MtInstructionDoc doc : docs) {
            ItfSrmInstructionIface iface = new ItfSrmInstructionIface();
            iface.setInstructionDocNum(doc.getInstructionDocNum());
            iface.setInstructionDocType(doc.getInstructionDocType());
            iface.setSiteId(doc.getSiteId());
            iface.setSupplierId(doc.getSupplierId());
            iface.setInstructionDocStatus(doc.getInstructionDocStatus());
            iface.setInstructionDocId(doc.getInstructionDocId());
            ifaces.add(iface);
        }
        if (CollectionUtils.isNotEmpty(ifaces)) {
            itfSrmInstructionIfaceService.sendInstructionDocStatusSrm(ifaces, belongTenantId);
        }

        return ReturnT.SUCCESS;
    }
}
