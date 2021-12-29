package com.ruike.wms.domain.service.impl;

import com.ruike.wms.api.dto.WmsDistDemandQueryDTO;
import com.ruike.wms.domain.repository.WmsDistributionDemandRepository;
import com.ruike.wms.domain.service.WmsDistributionDemandExportService;
import com.ruike.wms.domain.vo.WmsDistributionDemandExportVO;
import com.ruike.wms.domain.vo.WmsDistributionQtyVO;
import com.ruike.wms.infra.util.DatetimeUtils;
import com.ruike.wms.infra.util.FileUtils;
import io.tarzan.common.domain.util.CollectorsUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 配送需求 导出服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 15:04
 */
@Service
public class WmsDistributionDemandExportServiceImpl implements WmsDistributionDemandExportService {

    private final LovAdapter lovAdapter;
    private final WmsDistributionDemandRepository distributionDemandRepository;

    public WmsDistributionDemandExportServiceImpl(LovAdapter lovAdapter, WmsDistributionDemandRepository distributionDemandRepository) {
        this.lovAdapter = lovAdapter;
        this.distributionDemandRepository = distributionDemandRepository;
    }

    private static final String SHEET_NAME = "配送物料需求导出";
    private static final String FILE_NAME = "配送物料需求导出.xls";
    private static final List<String> TITLE = Stream.of("物料编码", "物料描述", "物料版本", "销售订单号", "销售订单行号").collect(Collectors.toList());
    private static final List<String> TITLE_END = Stream.of("汇总数量", "仓库库存").collect(Collectors.toList());
    private static final int MAX_DAYS = 14;

    @Override
    public void export(HttpServletRequest request, HttpServletResponse response, Long tenantId, WmsDistDemandQueryDTO dto) {
        // 获取查询时间天数
        List<LovValueDTO> distributionDateList = lovAdapter.queryLovValue("WMS.DISTRIBUTION_DATE", tenantId);
        int days = 0;
        LocalDate startDate = LocalDate.now();
        if (CollectionUtils.isNotEmpty(distributionDateList)) {
            days = Integer.parseInt(distributionDateList.get(0).getValue());
        }
        int maxDays = Math.max(MAX_DAYS, days);
        LocalDate demandDate = startDate.plusDays(days);
        LocalDate endDate = startDate.plusDays(maxDays);
        List<LocalDate> dateList = DatetimeUtils.getDateListInRange(startDate, demandDate.compareTo(endDate) > 0 ? demandDate : endDate);
        List<String> dateStrList = dateList.stream().map(date -> date.format(DateTimeFormatter.ISO_DATE)).collect(Collectors.toList());
        List<String> finalTitle = new ArrayList<>(TITLE);
        finalTitle.addAll(dateStrList);
        finalTitle.addAll(TITLE_END);

        // 取值数据
        List<WmsDistributionDemandExportVO> list = distributionDemandRepository.selectExportListByDateRange(tenantId, startDate, demandDate.plusDays(1), endDate, dto);

        // 新建文档
        HSSFWorkbook wk = new HSSFWorkbook();
        Sheet sh = wk.createSheet(SHEET_NAME);
        sh.setColumnWidth(0, 80 * 64);
        sh.setColumnWidth(1, 150 * 64);
        sh.setColumnWidth(3, 50 * 64);
        sh.setColumnWidth(4, 50 * 64);
        for (int i = 0; i <= maxDays; i++) {
            sh.setColumnWidth(5 + i, 60 * 64);
        }
        sh.setColumnWidth(6 + days, 50 * 64);
        sh.setColumnWidth(7 + days, 50 * 64);

        HSSFCellStyle borderStyle = getBorderStyle(wk);

        // 打印题头
        this.printHeader(sh, finalTitle, borderStyle);

        //打印行内容
        this.printLine(tenantId, sh, dateList, dto.getSiteId(), list, borderStyle);

        //下载文档
        FileUtils.downloadWorkbook(wk, request, response, FILE_NAME);
    }

    private HSSFCellStyle getBorderStyle(HSSFWorkbook wk) {
        HSSFCellStyle style = wk.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void printHeader(Sheet sheet, List<String> finalTitle, HSSFCellStyle borderStyle) {
        Row titleRow = sheet.createRow(0);
        this.fillRow(titleRow, finalTitle, borderStyle);
    }

    private void printLine(Long tenantId, Sheet sh, List<LocalDate> dateList, String siteId, List<WmsDistributionDemandExportVO> list, HSSFCellStyle borderStyle) {
        Map<WmsDistributionDemandExportVO, List<WmsDistributionDemandExportVO>> map = list.stream().collect(Collectors.groupingBy(WmsDistributionDemandExportVO::summaryRow));
        List<WmsDistributionQtyVO> onhandList = distributionDemandRepository.selectBatchInventoryQty(tenantId, siteId);
        Map<WmsDistributionQtyVO, BigDecimal> onhandMap = onhandList.stream().collect(Collectors.groupingBy(rec -> (new WmsDistributionQtyVO()).setMaterialId(rec.getMaterialId()).setMaterialVersion(rec.getMaterialVersion()).setSoNum(rec.getSoNum()).setSoLineNum(rec.getSoLineNum()), CollectorsUtil.summingBigDecimal(WmsDistributionQtyVO::getQuantity)));
        AtomicInteger col = new AtomicInteger(1);
        map.forEach((line, qtyList) -> {
            List<String> colList = new ArrayList<>();
            Map<LocalDate, BigDecimal> qtyMap = qtyList.stream().collect(Collectors.toMap(WmsDistributionDemandExportVO::getShiftDate, WmsDistributionDemandExportVO::getRequirementQty, (key1, key2) -> key1));
            Row row = sh.createRow(col.getAndAdd(1));
            colList.add(Optional.ofNullable(line.getMaterialCode()).orElse(""));
            colList.add(Optional.ofNullable(line.getMaterialName()).orElse(""));
            colList.add(Optional.ofNullable(line.getMaterialVersion()).orElse(""));
            colList.add(Optional.ofNullable(line.getSoNum()).orElse(""));
            colList.add(Optional.ofNullable(line.getSoLineNum()).orElse(""));
            for (LocalDate shiftDate : dateList) {
                BigDecimal requirementQty = Optional.ofNullable(qtyMap.get(shiftDate)).orElse(BigDecimal.ZERO);
                colList.add(requirementQty.toPlainString());
            }
            BigDecimal totalReqQty = qtyMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            colList.add(Optional.ofNullable(totalReqQty).orElse(BigDecimal.ZERO).toPlainString());
            WmsDistributionQtyVO onhandKey = new WmsDistributionQtyVO().setMaterialId(line.getMaterialId()).setMaterialVersion(line.getMaterialVersion()).setSoNum(line.getSoNum()).setSoLineNum(line.getSoLineNum());
            colList.add(Optional.ofNullable(onhandMap.get(onhandKey)).orElse(BigDecimal.ZERO).toPlainString());
            this.fillRow(row, colList, borderStyle);
        });
    }

    /**
     * 输入行内容
     *
     * @param row    行
     * @param fields 字段
     */
    private void fillRow(Row row, List<String> fields, HSSFCellStyle borderStyle) {
        AtomicInteger col = new AtomicInteger(0);
        fields.forEach(field -> {
            Cell cell = row.createCell(col.getAndAdd(1));
            cell.setCellValue(field);
            cell.setCellStyle(borderStyle);
        });
    }
}
