package com.ruike.hme.app.export.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.app.export.HmeFreezeDocExportService;
import com.ruike.hme.domain.repository.HmeFreezeDocLineRepository;
import com.ruike.hme.domain.repository.HmeFreezeDocRepository;
import com.ruike.hme.domain.vo.HmeFreezeDocLineVO;
import com.ruike.hme.domain.vo.HmeFreezeDocVO;
import com.ruike.wms.infra.util.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 条码冻结单 导出服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/25 14:32
 */
@Service
public class HmeFreezeDocExportServiceImpl implements HmeFreezeDocExportService {
    private final static String SHEET_NAME = "条码解冻单";
    private static final String FILE_NAME = "条码解冻单导出.xlsx";
    private static final List<String> HEADER_TITLE = Arrays.asList("序号", "工厂", "冻结单据", "冻结类型", "冻结状态", "审核结果", "物料", "物料描述", "物料版本", "生产版本", "仓库", "货位", "供应商", "供应商描述", "库存批次", "供应商批次", "工单", "实验代码", "设备", "生产线", "工段", "工位", "COS类型", "Wafer", "虚拟号", "金锡比", "热沉编码","筛选规则", "操作人", "班次", "生产时间从", "生产时间至");
    private static final List<String> LINE_TITLE = Arrays.asList("序号", "条码", "单据冻结标识", "条码冻结标识", "冻结时间", "物料", "物料描述", "数量", "物料版本", "生产版本", "仓库", "货位", "供应商", "供应商描述", "库存批次", "供应商批次", "工单", "实验代码", "设备", "生产线", "工段", "工位", "操作人", "在制标识", "生产时间", "解冻时间");
    private static final List<Integer> COL_WIDTH = Arrays.asList(60, 50, 50, 50, 80, 60, 100, 100, 40, 40, 40, 40, 50, 80, 50, 50, 60, 50, 50, 40, 40, 40, 40, 40, 80, 80);
    private final HmeFreezeDocRepository docRepository;
    private final HmeFreezeDocLineRepository docLineRepository;

    public HmeFreezeDocExportServiceImpl(HmeFreezeDocRepository docRepository, HmeFreezeDocLineRepository docLineRepository) {
        this.docRepository = docRepository;
        this.docLineRepository = docLineRepository;
    }

    @Override
    public void export(Long tenantId, HttpServletRequest request, HttpServletResponse response, String freezeDocId) {
        // 获取基本数据
        HmeFreezeDocVO doc = docRepository.byId(tenantId, freezeDocId);
        List<HmeFreezeDocLineVO> lineList = docLineRepository.listGet(tenantId, freezeDocId);

        // 新建文档
        XSSFWorkbook wk = new XSSFWorkbook();
        XSSFSheet sh = sheetCreate(wk);
        XSSFCellStyle borderStyle = getBorderStyle(wk);
        AtomicInteger rowGen = new AtomicInteger(0);

        // 打印头标题
        XSSFRow headerTitleRow = sh.createRow(rowGen.getAndIncrement());
        fillRow(headerTitleRow, HEADER_TITLE, borderStyle);

        // 打印头数据
        printHeader(doc, rowGen, sh, borderStyle);

        // 打印行标题，答应之前先空一行
        rowGen.getAndIncrement();
        XSSFRow lineTitleRow = sh.createRow(rowGen.getAndIncrement());
        fillRow(lineTitleRow, LINE_TITLE, borderStyle);

        // 打印行数据
        printLine(lineList, rowGen, sh, borderStyle);

        // 下载文件
        FileUtils.downloadWorkbook(wk, request, response, FILE_NAME);
    }

    private void printLine(List<HmeFreezeDocLineVO> lineList, AtomicInteger rowGen, XSSFSheet sh, XSSFCellStyle borderStyle) {
        lineList.forEach(line -> {
            XSSFRow row = sh.createRow(rowGen.getAndIncrement());
            List<String> colList = new ArrayList<>();
            colList.add(String.valueOf(line.getSequenceNum()));
            colList.add(line.getMaterialLotCode());
            colList.add(line.getFreezeFlagMeaning());
            colList.add(line.getSnFreezeFlagMeaning());
            colList.add(line.getFreezeDate());
            colList.add(line.getMaterialCode());
            colList.add(line.getMaterialName());
            colList.add(line.getPrimaryUomQty().toPlainString());
            colList.add(line.getMaterialVersion());
            colList.add(line.getProductionVersion());
            colList.add(line.getWarehouseCode());
            colList.add(line.getLocatorCode());
            colList.add(line.getSupplierCode());
            colList.add(line.getSupplierName());
            colList.add(line.getInventoryLot());
            colList.add(line.getSupplierLot());
            colList.add(line.getWorkOrderNum());
            colList.add(line.getTestCode());
            colList.add(line.getEquipmentCode());
            colList.add(line.getProdLineCode());
            colList.add(line.getWorkcellCode());
            colList.add(line.getStationCode());
            colList.add(line.getOperatedByName());
            colList.add(line.getMfFlagMeaning());
            colList.add(DateUtil.formatDateTime(line.getProductionDate()));
            colList.add(DateUtil.formatDateTime(line.getUnfreezeDate()));
            fillRow(row, colList, borderStyle);
        });
    }

    private void printHeader(HmeFreezeDocVO doc, AtomicInteger rowGen, XSSFSheet sh, XSSFCellStyle borderStyle) {
        XSSFRow row = sh.createRow(rowGen.getAndIncrement());
        List<String> colList = new ArrayList<>();
        // 打印头数据
        colList.add(String.valueOf(doc.getSequenceNum()));
        colList.add(doc.getSiteCode());
        colList.add(doc.getFreezeDocNum());
        colList.add(doc.getFreezeTypeMeaning());
        colList.add(doc.getFreezeDocStatusMeaning());
        colList.add(doc.getApprovalStatusMeaning());
        colList.add(doc.getMaterialCode());
        colList.add(doc.getMaterialName());
        colList.add(doc.getMaterialVersion());
        colList.add(doc.getProductionVersion());
        colList.add(doc.getWarehouseCode());
        colList.add(doc.getLocatorCode());
        colList.add(doc.getSupplierCode());
        colList.add(doc.getSupplierName());
        colList.add(doc.getInventoryLot());
        colList.add(doc.getSupplierLot());
        colList.add(doc.getWorkOrderNum());
        colList.add(doc.getTestCode());
        colList.add(doc.getEquipmentCode());
        colList.add(doc.getProdLineCode());
        colList.add(doc.getWorkcellCode());
        colList.add(doc.getStationCode());
        colList.add(doc.getCosTypeMeaning());
        colList.add(doc.getWafer());
        colList.add(doc.getVirtualNum());
        colList.add(Objects.isNull(doc.getAusnRatio())?"":doc.getAusnRatio().toString());
        colList.add(doc.getHotSinkNum());
        colList.add(doc.getCosRuleCode());
        colList.add(doc.getOperatedByName());
        colList.add(doc.getShiftCode());
        colList.add(DateUtil.formatDateTime(doc.getProductionDateFrom()));
        colList.add(DateUtil.formatDateTime(doc.getProductionDateTo()));
        fillRow(row, colList, borderStyle);
    }

    private XSSFSheet sheetCreate(XSSFWorkbook wk) {
        XSSFSheet sh = wk.createSheet(SHEET_NAME);
        AtomicInteger colGen = new AtomicInteger(0);
        COL_WIDTH.forEach(width -> sh.setColumnWidth(colGen.getAndIncrement(), width * 64));
        return sh;
    }

    private XSSFCellStyle getBorderStyle(XSSFWorkbook wk) {
        XSSFCellStyle style = wk.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * 输入行内容
     *
     * @param row    行
     * @param fields 字段
     */
    private void fillRow(XSSFRow row, List<String> fields, XSSFCellStyle borderStyle) {
        AtomicInteger col = new AtomicInteger(0);
        fields.forEach(field -> {
            Cell cell = row.createCell(col.getAndAdd(1));
            cell.setCellValue(Optional.ofNullable(field).orElse(""));
            cell.setCellStyle(borderStyle);
        });
    }
}
