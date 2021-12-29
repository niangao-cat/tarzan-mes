package com.ruike.reports.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.vo.HmeProcessCollectVO;
import com.ruike.hme.domain.vo.HmeProcessReportVo2;
import com.ruike.hme.infra.util.ExcellUtils;
import com.ruike.reports.api.dto.ReportDispatchDetailsDTO;
import com.ruike.reports.app.service.ReportDispatchDetailsService;
import com.ruike.reports.domain.vo.ReportDispatchDetailsVO;
import com.ruike.reports.infra.mapper.ReportDispatchDetailsMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ReportDispatchDetailsServiceImpl
 * @Description 派工明细报表
 * @Author lkj
 * @Date 2020/12/15
 */
@Service
@Slf4j
public class ReportDispatchDetailsServiceImpl implements ReportDispatchDetailsService {

    private final ReportDispatchDetailsMapper reportDispatchDetailsMapper;

    public ReportDispatchDetailsServiceImpl(ReportDispatchDetailsMapper reportDispatchDetailsMapper) {
        this.reportDispatchDetailsMapper = reportDispatchDetailsMapper;
    }

    @Override
    public Page<ReportDispatchDetailsVO> selectDispatchDetails(PageRequest pageRequest, String tenantId, ReportDispatchDetailsDTO dto) {
        return PageHelper.doPage(pageRequest, () -> reportDispatchDetailsMapper.selectDispatchDetails(tenantId, dto));
    }

    @Override
    public void reportDispatchExport(String tenantId, ReportDispatchDetailsDTO dto, HttpServletResponse response) throws IOException {
        // 查询派工明细数据
        List<ReportDispatchDetailsVO> reportDispatchDetailsList = reportDispatchDetailsMapper.selectDispatchDetails(tenantId, dto);
        //导出数据
        log.info(">>>>>>>>>>>>>>>>>>>>>>开始导出派工明细数据");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("派工明细");
        String fileName = "派工明细" + DateUtil.format(new Date(), "yyyyMMdd") + ".xls";

        OutputStream fOut = null;
        try {
            //创建excel文件对象
            fOut = response.getOutputStream();
            //新增数据行，并且设置单元格数据
            //headers表示excel表中第一行的表头
            List<String> headerList = new ArrayList<>();
            String[] headers = {"生产线", "生产线描述", "工单编码", "长文本", "工单备注", "产品编码", "产品描述", "版本号",  "工单数量", "工单累计派工总数", "待派工数量", "工段编码", "工段名称", "日期", "班次","创建人", "创建时间", "派工人", "派工时间", "派工数量", "合计"};
            headerList.addAll(Arrays.asList(headers));

            // 创建一个新的HSSFWorkbook对象
            HSSFRow row = sheet.createRow(0);
            //标题
            row.setHeightInPoints(30);
            HSSFCell cell = row.createCell(0);
            Map<String, CellStyle> styles = ExcellUtils.createStyles(workbook);
            cell.setCellStyle(styles.get("title"));
            cell.setCellValue("派 工 明 细 报 表");
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), 0, headerList.size() - 1));
            //表头
            HSSFRow headerRow = sheet.createRow(1);
            Integer headerIndex = 0;
            for (String hd : headerList) {
                HSSFCell headCell = headerRow.createCell(headerIndex);
                HSSFRichTextString text = new HSSFRichTextString(hd);
                headCell.setCellStyle(styles.get("center"));
                headCell.setCellValue(text);
                headerIndex++;
            }

            Integer rowIndex = 2;
            // 根据生产线、生产线描述、工单编码、长文本、工单备注、产品编码、产品描述、版本号、工段编码、工段名称、日期分组汇总
            Map<String, List<ReportDispatchDetailsVO>> detailsMap = reportDispatchDetailsList.stream().collect(Collectors.groupingBy(details -> this.spliceParameter(details),LinkedHashMap::new, Collectors.toList()));
            for (Map.Entry<String, List<ReportDispatchDetailsVO>> stringListEntry : detailsMap.entrySet()) {
                List<ReportDispatchDetailsVO> dispatchDetailsVOList = stringListEntry.getValue();
                double dispatchQty = dispatchDetailsVOList.stream().map(ReportDispatchDetailsVO::getDispatchQty).filter(Objects::nonNull).mapToDouble(qty -> Double.valueOf(qty)).summaryStatistics().getSum();
                Integer index = 0;
                // 记录起始的位置
                Integer startIndex = rowIndex;
                for (ReportDispatchDetailsVO detailsVO : dispatchDetailsVOList) {
                    HSSFRow hssfRow = sheet.createRow(rowIndex++);
                    if (index.compareTo(0) == 0) {
                        HSSFCell cell0 = hssfRow.createCell(0);
                        cell0.setCellStyle(styles.get("center"));
                        cell0.setCellValue(detailsVO.getProdLineCode());
                        HSSFCell cell1 = hssfRow.createCell(1);
                        cell1.setCellStyle(styles.get("center"));
                        cell1.setCellValue(detailsVO.getProdLineName());
                        HSSFCell cell2 = hssfRow.createCell(2);
                        cell2.setCellStyle(styles.get("center"));
                        cell2.setCellValue(detailsVO.getWorkOrderNum());
                        HSSFCell cell3 = hssfRow.createCell(3);
                        cell3.setCellStyle(styles.get("center"));
                        cell3.setCellValue(detailsVO.getAttribute8());
                        HSSFCell cell4 = hssfRow.createCell(4);
                        cell4.setCellStyle(styles.get("center"));
                        cell4.setCellValue(detailsVO.getWoRemark());
                        HSSFCell cell5 = hssfRow.createCell(5);
                        cell5.setCellStyle(styles.get("center"));
                        cell5.setCellValue(detailsVO.getMaterialCode());
                        HSSFCell cell6 = hssfRow.createCell(6);
                        cell6.setCellStyle(styles.get("center"));
                        cell6.setCellValue(detailsVO.getMaterialName());
                        HSSFCell cell7 = hssfRow.createCell(7);
                        cell7.setCellStyle(styles.get("center"));
                        cell7.setCellValue(detailsVO.getProductionVersion());
                        HSSFCell cell8 = hssfRow.createCell(11);
                        cell8.setCellStyle(styles.get("center"));
                        cell8.setCellValue(detailsVO.getWorkcellCode());
                        HSSFCell cell9 = hssfRow.createCell(12);
                        cell9.setCellStyle(styles.get("center"));
                        cell9.setCellValue(detailsVO.getWorkcellName());
                        HSSFCell cell10 = hssfRow.createCell(13);
                        cell10.setCellStyle(styles.get("center"));
                        cell10.setCellValue(detailsVO.getShiftDate());
                        HSSFCell cell15 = hssfRow.createCell(20);
                        cell15.setCellStyle(styles.get("center"));
                        cell15.setCellValue(BigDecimal.valueOf(dispatchQty).intValue());
                    }
                    HSSFCell cell08 = hssfRow.createCell(8);
                    cell08.setCellStyle(styles.get("center"));
                    cell08.setCellValue(detailsVO.getQty());

                    HSSFCell cell09 = hssfRow.createCell(9);
                    cell09.setCellStyle(styles.get("center"));
                    cell09.setCellValue(detailsVO.getDispatchCodeQty());

                    HSSFCell cell10 = hssfRow.createCell(10);
                    cell10.setCellStyle(styles.get("center"));
                    cell10.setCellValue(detailsVO.getRestQty());
                    HSSFCell cell11 = hssfRow.createCell(14);
                    cell11.setCellStyle(styles.get("center"));
                    cell11.setCellValue(detailsVO.getShiftCode());
                    HSSFCell cell12 = hssfRow.createCell(15);
                    cell12.setCellStyle(styles.get("center"));
                    cell12.setCellValue(detailsVO.getCreatedByName());
                    HSSFCell cell13 = hssfRow.createCell(16);
                    cell13.setCellStyle(styles.get("center"));
                    cell13.setCellValue(detailsVO.getCreationDate());
                    HSSFCell cell14 = hssfRow.createCell(17);
                    cell14.setCellStyle(styles.get("center"));
                    cell14.setCellValue(detailsVO.getRealName());
                    HSSFCell cell15 = hssfRow.createCell(18);
                    cell15.setCellStyle(styles.get("center"));
                    cell15.setCellValue(detailsVO.getLastUpdateDate());
                    HSSFCell cell16 = hssfRow.createCell(19);
                    cell16.setCellStyle(styles.get("center"));
                    cell16.setCellValue(Double.valueOf(detailsVO.getDispatchQty()).intValue());
                    index++;
                }
                // 根据汇总维度合并上下行 包含末行 需减1
                if (dispatchDetailsVOList.size() > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 2, 2));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 3, 3));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 4, 4));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 5, 5));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 6, 6));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 7, 7));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 11, 11));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 12, 12));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 13, 13));
                    sheet.addMergedRegion(new CellRangeAddress(startIndex, startIndex + index - 1, 20, 20));
                }
            }
            ExcellUtils.setResponseHeader(response, fileName);

            workbook.write(fOut);
        } catch (IOException e) {
            throw new CommonException("派工明细导出失败");
        } finally {
            //操作结束，关闭文件
            fOut.flush();
            fOut.close();
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>派工明细导出完成");
    }

    private String spliceParameter(ReportDispatchDetailsVO detailsVO) {
        StringBuffer sb = new StringBuffer();
        sb.append(detailsVO.getProdLineCode());
        sb.append(detailsVO.getProdLineName());
        sb.append(detailsVO.getWorkOrderNum());
        sb.append(detailsVO.getAttribute8());
        sb.append(detailsVO.getWoRemark());
        sb.append(detailsVO.getMaterialCode());
        sb.append(detailsVO.getMaterialName());
        sb.append(detailsVO.getProductionVersion());
        sb.append(detailsVO.getWorkcellCode());
        sb.append(detailsVO.getWorkcellName());
        sb.append(detailsVO.getShiftDate());
        return sb.toString();
    }
}
