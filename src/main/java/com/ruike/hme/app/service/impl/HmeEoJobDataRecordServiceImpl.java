package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.ruike.hme.api.dto.HmeEoJobDataRecordDto;
import com.ruike.hme.api.dto.HmeEoJobDataRecordReturnDTO;
import com.ruike.hme.api.dto.HmeEoJobDataRecordReturnDTO2;
import com.ruike.hme.api.dto.HmeEoJobDataRecordReturnDTO3;
import com.ruike.hme.app.service.HmeEoJobDataRecordService;
import com.ruike.hme.domain.repository.HmeEoJobDataRecordRepository;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobDataRecordMapper;
import com.ruike.hme.infra.util.ExcellUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工序作业平台-数据采集应用服务默认实现
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:48:09
 */
@Service
public class HmeEoJobDataRecordServiceImpl implements HmeEoJobDataRecordService {

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private HmeEoJobDataRecordMapper mapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtUserRepository mtUserRepository;

    @Override
    public void deleteSupplementRecord(Long tenantId, List<HmeEoJobDataRecordVO> deleteVoList) {
        if (CollectionUtils.isEmpty(deleteVoList)) {
            throw new RuntimeException("请选择需要删除的行数据！");
        }
        for (HmeEoJobDataRecordVO deleteVO : deleteVoList
        ) {
            //判断行ID不为空时，从表中删除数据
            if (StringUtils.isNotEmpty(deleteVO.getJobRecordId())) {
                //根据ID删除数据采集信息
                this.hmeEoJobDataRecordRepository.deleteByPrimaryKey(deleteVO.getJobRecordId());
            }
        }
    }

    @Override
    public Page<HmeEoJobDataRecordVO> supplementRecordQuery(Long tenantId, PageRequest pageRequest, HmeEoJobDataRecordDto dto) {
        Page<HmeEoJobDataRecordVO> base = PageHelper.doPageAndSort(pageRequest,
                () -> this.mapper.supplementRecordQuery(tenantId, dto));
        return base;
    }

    @Override
    public Page<HmeEoJobDataRecordReturnDTO> summaryReport(Long tenantId, String materialId, String flag, String operationCode, PageRequest pageRequest) {
        //获取值集QMS.OQC_0001中存在的值operation_name（工艺编码）
        List<String> operationCodes = new ArrayList<>();
        if (StringUtils.isBlank(operationCode)) {
            List<LovValueDTO> operationLov = lovAdapter.queryLovValue("QMS.OQC_0001", tenantId);
            if (CollectionUtils.isEmpty(operationLov)) {

            }
            operationCodes = operationLov.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        } else {
            operationCodes = Collections.singletonList(operationCode);
        }
        List<String> finalOperationCodes = operationCodes;
        Page<HmeEoJobDataRecordReturnDTO> result = PageHelper.doPage(pageRequest, () -> mapper.summaryAll(tenantId, finalOperationCodes, materialId));
        if (CollectionUtils.isEmpty(result)) {
            return result;
        }
        List<String> materialIdList = result.stream().map(HmeEoJobDataRecordReturnDTO::getSnMaterialId).distinct().collect(Collectors.toList());
        List<String> operationIdList = result.stream().map(HmeEoJobDataRecordReturnDTO::getOperationId).distinct().collect(Collectors.toList());
        List<HmeEoJobDataRecordReturnDTO3> hmeEoJobDataRecordReturnDTO3s = mapper.summaryDetails(materialIdList, operationIdList);
        if (CollectionUtils.isEmpty(hmeEoJobDataRecordReturnDTO3s)) {
            List<HmeEoJobDataRecordReturnDTO2> hmeEoJobDataRecordReturnDTO2sTemp = new ArrayList<>();
            List<HmeEoJobDataRecordReturnDTO3> hmeEoJobDataRecordReturnDTO3Temp = new ArrayList<>();
            if (HmeConstants.ConstantValue.YES.equals(flag)) {
                hmeEoJobDataRecordReturnDTO3Temp = mapper.selsetCreate(materialIdList, operationIdList);
                List<Long> collect1 = hmeEoJobDataRecordReturnDTO3Temp.stream().map(HmeEoJobDataRecordReturnDTO3::getCreatedBy).distinct().collect(Collectors.toList());
                for (Long userId :
                        collect1) {
                    HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2 = new HmeEoJobDataRecordReturnDTO2();
                    hmeEoJobDataRecordReturnDTO2.setUserId(userId);
                    MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, userId);
                    hmeEoJobDataRecordReturnDTO2.setUserName(mtUserInfo.getRealName());
                    hmeEoJobDataRecordReturnDTO2sTemp.add(hmeEoJobDataRecordReturnDTO2);
                }
            }
            for (HmeEoJobDataRecordReturnDTO temp :
                    result) {
                temp.setNcNum(0l);
                temp.setNcRate("0");
                if (HmeConstants.ConstantValue.YES.equals(flag)) {
                    List<HmeEoJobDataRecordReturnDTO2> hmeEoJobDataRecordReturnDTO2sTemp1 = new ArrayList<>();
                    for (HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2Temp :
                            hmeEoJobDataRecordReturnDTO2sTemp) {
                        HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2 = new HmeEoJobDataRecordReturnDTO2();
                        BeanUtils.copyProperties(hmeEoJobDataRecordReturnDTO2Temp, hmeEoJobDataRecordReturnDTO2);
                        long count = hmeEoJobDataRecordReturnDTO3Temp.stream()
                                .filter(t -> temp.getSnMaterialId().equals(t.getSnMaterialId()) && temp.getOperationId().equals(t.getOperationId()))
                                .filter(t -> t.getCreatedBy().equals(hmeEoJobDataRecordReturnDTO2Temp.getUserId()))
                                .map(HmeEoJobDataRecordReturnDTO3::getJobId).distinct()
                                .count();
                        hmeEoJobDataRecordReturnDTO2.setInspectionNum(count);
                        hmeEoJobDataRecordReturnDTO2sTemp1.add(hmeEoJobDataRecordReturnDTO2);
                    }
                    temp.setHmeEoJobDataRecordReturnDTO2s(hmeEoJobDataRecordReturnDTO2sTemp1);
                }
            }

            return result;
        }
        List<HmeEoJobDataRecordReturnDTO2> hmeEoJobDataRecordReturnDTO2s = new ArrayList<>();
        if (HmeConstants.ConstantValue.YES.equals(flag)) {
            List<Long> collect1 = hmeEoJobDataRecordReturnDTO3s.stream().map(HmeEoJobDataRecordReturnDTO3::getCreatedBy).distinct().collect(Collectors.toList());
            for (Long userId :
                    collect1) {
                HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2 = new HmeEoJobDataRecordReturnDTO2();
                hmeEoJobDataRecordReturnDTO2.setUserId(userId);
                MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, userId);
                hmeEoJobDataRecordReturnDTO2.setUserName(mtUserInfo.getRealName());
                hmeEoJobDataRecordReturnDTO2s.add(hmeEoJobDataRecordReturnDTO2);
            }
        }
        for (HmeEoJobDataRecordReturnDTO temp :
                result) {
            List<String> jobIdlist = hmeEoJobDataRecordReturnDTO3s.stream()
                    .filter(t -> temp.getSnMaterialId().equals(t.getSnMaterialId()) && temp.getOperationId().equals(t.getOperationId()))
                    .map(HmeEoJobDataRecordReturnDTO3::getJobId).distinct()
                    .collect(Collectors.toList());
            long ncNum = 0l;
            for (String jobId :
                    jobIdlist) {
                List<HmeEoJobDataRecordReturnDTO3> collect = hmeEoJobDataRecordReturnDTO3s.stream()
                        .filter(t -> t.getJobId().equals(jobId))
                        .filter(t -> Strings.isNotBlank(t.getResult()) && !t.getResult().equals("OK") && Strings.isNotBlank(t.getMaximalValue()) && Strings.isNotBlank(t.getMinimumValue()) && NumberUtil.isNumber(t.getResult()))
                        .collect(Collectors.toList());
                long ng = collect.stream().filter(t -> t.getResult().equals("NG")
                        || new BigDecimal(t.getResult()).compareTo(new BigDecimal(t.getMinimumValue())) < 0
                        || new BigDecimal(t.getResult()).compareTo(new BigDecimal(t.getMaximalValue())) > 0).count();
                if (ng > 0) {
                    ncNum++;
                }
            }
            if (HmeConstants.ConstantValue.YES.equals(flag)) {
                List<HmeEoJobDataRecordReturnDTO2> hmeEoJobDataRecordReturnDTO2sTemp = new ArrayList<>();
                for (HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2Temp :
                        hmeEoJobDataRecordReturnDTO2s) {
                    HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2 = new HmeEoJobDataRecordReturnDTO2();
                    BeanUtils.copyProperties(hmeEoJobDataRecordReturnDTO2Temp, hmeEoJobDataRecordReturnDTO2);
                    long count = hmeEoJobDataRecordReturnDTO3s.stream()
                            .filter(t -> jobIdlist.contains(t.getJobId()))
                            .filter(t -> t.getCreatedBy().equals(hmeEoJobDataRecordReturnDTO2Temp.getUserId()))
                            .map(HmeEoJobDataRecordReturnDTO3::getJobId).distinct()
                            .count();
                    hmeEoJobDataRecordReturnDTO2.setInspectionNum(count);
                    hmeEoJobDataRecordReturnDTO2sTemp.add(hmeEoJobDataRecordReturnDTO2);
                }
                temp.setHmeEoJobDataRecordReturnDTO2s(hmeEoJobDataRecordReturnDTO2sTemp);
            }
            temp.setNcNum(ncNum);
            if (ncNum == 0l) {
                temp.setNcRate("0");
            } else {
                String format = String.format("%.2f", ((Double.valueOf(ncNum) / Double.valueOf(temp.getInspectionNum())) * 100)) + "%";
                temp.setNcRate(format);
            }
        }

        return result;
    }

    @Override
    public void summaryReportExport(Long tenantId, String materialId, String flag, String operationCode, HttpServletResponse response) {
        List<HmeEoJobDataRecordReturnDTO2> hmeEoJobDataRecordReturnDTO2s = new ArrayList<>();

        List<String> operationCodes = new ArrayList<>();
        if (StringUtils.isBlank(operationCode)) {
            List<LovValueDTO> operationLov = lovAdapter.queryLovValue("QMS.OQC_0001", tenantId);
            if (CollectionUtils.isEmpty(operationLov)) {

            }
            operationCodes = operationLov.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        } else {
            operationCodes = Collections.singletonList(operationCode);
        }
        List<String> finalOperationCodes = operationCodes;
        List<HmeEoJobDataRecordReturnDTO> result = mapper.summaryAll(tenantId, finalOperationCodes, materialId);
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        List<String> materialIdList = result.stream().map(HmeEoJobDataRecordReturnDTO::getSnMaterialId).distinct().collect(Collectors.toList());
        List<String> operationIdList = result.stream().map(HmeEoJobDataRecordReturnDTO::getOperationId).distinct().collect(Collectors.toList());
        List<HmeEoJobDataRecordReturnDTO3> hmeEoJobDataRecordReturnDTO3s = mapper.summaryDetails(materialIdList, operationIdList);
        if (CollectionUtils.isEmpty(hmeEoJobDataRecordReturnDTO3s)) {
            List<HmeEoJobDataRecordReturnDTO3> hmeEoJobDataRecordReturnDTO3Temp = new ArrayList<>();
            if (HmeConstants.ConstantValue.YES.equals(flag)) {
                hmeEoJobDataRecordReturnDTO3Temp = mapper.selsetCreate(materialIdList, operationIdList);
                List<Long> collect1 = hmeEoJobDataRecordReturnDTO3Temp.stream().map(HmeEoJobDataRecordReturnDTO3::getCreatedBy).distinct().collect(Collectors.toList());
                for (Long userId :
                        collect1) {
                    HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2 = new HmeEoJobDataRecordReturnDTO2();
                    hmeEoJobDataRecordReturnDTO2.setUserId(userId);
                    MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, userId);
                    hmeEoJobDataRecordReturnDTO2.setUserName(mtUserInfo.getRealName());
                    hmeEoJobDataRecordReturnDTO2s.add(hmeEoJobDataRecordReturnDTO2);
                }
            }
            for (HmeEoJobDataRecordReturnDTO temp :
                    result) {
                temp.setNcNum(0l);
                temp.setNcRate("0");
                if (HmeConstants.ConstantValue.YES.equals(flag)) {
                    List<HmeEoJobDataRecordReturnDTO2> hmeEoJobDataRecordReturnDTO2sTemp = new ArrayList<>();
                    for (HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2Temp :
                            hmeEoJobDataRecordReturnDTO2s) {
                        HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2 = new HmeEoJobDataRecordReturnDTO2();
                        BeanUtils.copyProperties(hmeEoJobDataRecordReturnDTO2Temp, hmeEoJobDataRecordReturnDTO2);
                        long count = hmeEoJobDataRecordReturnDTO3Temp.stream()
                                .filter(t -> temp.getSnMaterialId().equals(t.getSnMaterialId()) && temp.getOperationId().equals(t.getOperationId()))
                                .filter(t -> t.getCreatedBy().equals(hmeEoJobDataRecordReturnDTO2Temp.getUserId()))
                                .map(HmeEoJobDataRecordReturnDTO3::getJobId).distinct()
                                .count();
                        hmeEoJobDataRecordReturnDTO2.setInspectionNum(count);
                        hmeEoJobDataRecordReturnDTO2sTemp.add(hmeEoJobDataRecordReturnDTO2);
                    }
                    temp.setHmeEoJobDataRecordReturnDTO2s(hmeEoJobDataRecordReturnDTO2sTemp);
                }
            }
        } else {
            if (HmeConstants.ConstantValue.YES.equals(flag)) {
                List<Long> collect1 = hmeEoJobDataRecordReturnDTO3s.stream().map(HmeEoJobDataRecordReturnDTO3::getCreatedBy).distinct().collect(Collectors.toList());
                for (Long userId :
                        collect1) {
                    HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2 = new HmeEoJobDataRecordReturnDTO2();
                    hmeEoJobDataRecordReturnDTO2.setUserId(userId);
                    MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, userId);
                    hmeEoJobDataRecordReturnDTO2.setUserName(mtUserInfo.getRealName());
                    hmeEoJobDataRecordReturnDTO2s.add(hmeEoJobDataRecordReturnDTO2);
                }
            }
            for (HmeEoJobDataRecordReturnDTO temp :
                    result) {
                List<String> jobIdlist = hmeEoJobDataRecordReturnDTO3s.stream()
                        .filter(t -> temp.getSnMaterialId().equals(t.getSnMaterialId()) && temp.getOperationId().equals(t.getOperationId()))
                        .map(HmeEoJobDataRecordReturnDTO3::getJobId).distinct()
                        .collect(Collectors.toList());
                long ncNum = 0l;
                for (String jobId :
                        jobIdlist) {
                    List<HmeEoJobDataRecordReturnDTO3> collect = hmeEoJobDataRecordReturnDTO3s.stream()
                            .filter(t -> t.getJobId().equals(jobId))
                            .filter(t -> Strings.isNotBlank(t.getResult()) && !t.getResult().equals("OK") && Strings.isNotBlank(t.getMaximalValue()) && Strings.isNotBlank(t.getMinimumValue()) && NumberUtil.isNumber(t.getResult()))
                            .collect(Collectors.toList());
                    long ng = collect.stream().filter(t -> t.getResult().equals("NG")
                            || new BigDecimal(t.getResult()).compareTo(new BigDecimal(t.getMinimumValue())) < 0
                            || new BigDecimal(t.getResult()).compareTo(new BigDecimal(t.getMaximalValue())) > 0).count();
                    if (ng > 0) {
                        ncNum++;
                    }
                }
                if (HmeConstants.ConstantValue.YES.equals(flag)) {
                    List<HmeEoJobDataRecordReturnDTO2> hmeEoJobDataRecordReturnDTO2sTemp = new ArrayList<>();
                    for (HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2Temp :
                            hmeEoJobDataRecordReturnDTO2s) {
                        HmeEoJobDataRecordReturnDTO2 hmeEoJobDataRecordReturnDTO2 = new HmeEoJobDataRecordReturnDTO2();
                        BeanUtils.copyProperties(hmeEoJobDataRecordReturnDTO2Temp, hmeEoJobDataRecordReturnDTO2);
                        long count = hmeEoJobDataRecordReturnDTO3s.stream()
                                .filter(t -> jobIdlist.contains(t.getJobId()))
                                .filter(t -> t.getCreatedBy().equals(hmeEoJobDataRecordReturnDTO2Temp.getUserId()))
                                .map(HmeEoJobDataRecordReturnDTO3::getJobId).distinct()
                                .count();
                        hmeEoJobDataRecordReturnDTO2.setInspectionNum(count);
                        hmeEoJobDataRecordReturnDTO2sTemp.add(hmeEoJobDataRecordReturnDTO2);
                    }
                    temp.setHmeEoJobDataRecordReturnDTO2s(hmeEoJobDataRecordReturnDTO2sTemp);
                }
                temp.setNcNum(ncNum);
                String format = String.format("%.2f", ((Double.valueOf(ncNum) / Double.valueOf(temp.getInspectionNum())) * 100)) + "%";
                temp.setNcRate(format);
            }
        }

        //开始导出
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("产品应用检机");
        String fileName = "产品应用检机" + DateUtil.format(new Date(), "yyyyMMdd") + ".xlsx";

        List<String> tagList = new ArrayList<>();
        if (HmeConstants.ConstantValue.YES.equals(flag)) {
            tagList = hmeEoJobDataRecordReturnDTO2s.stream().map(HmeEoJobDataRecordReturnDTO2::getUserName).collect(Collectors.toList());
        }
        try {
            //创建excel文件对象
            OutputStream fOut = response.getOutputStream();
            //新增数据行，并且设置单元格数据
            //headers表示excel表中第一行的表头
            List<String> headerList = new ArrayList<>();
            String[] headers = {"机型", "机型描述", "工艺路线", "检验数", "不良数", "不良率"};
            headerList.addAll(Arrays.asList(headers));
            headerList.addAll(tagList);

            // 创建一个新的HSSFWorkbook对象
            HSSFRow row = sheet.createRow(0);
            //标题
            row.setHeightInPoints(30);
            HSSFCell cell1 = row.createCell(0);
            Map<String, CellStyle> styles = ExcellUtils.createStyles(workbook);
            cell1.setCellStyle(styles.get("title"));
            cell1.setCellValue("产品应用检机");
            //存在合并 长度先写死
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), 0, headerList.size()));
            //表头
            HSSFRow headerRow = sheet.createRow(1);
            Integer headerIndex = 0;
            for (String hd : headerList) {
                HSSFCell cell = headerRow.createCell(headerIndex);
                HSSFRichTextString text = new HSSFRichTextString(hd);
                cell.setCellStyle(styles.get("center"));
                cell.setCellValue(text);
                headerIndex++;
            }

            //行
            Integer rowIndex = 2;
            for (HmeEoJobDataRecordReturnDTO temp : result) {
                HSSFRow hssfRow = sheet.createRow(rowIndex);
                hssfRow.createCell(0).setCellValue(temp.getMaterialCode());
                hssfRow.createCell(1).setCellValue(temp.getMaterialName());
                hssfRow.createCell(2).setCellValue(temp.getOperationName());
                hssfRow.createCell(3).setCellValue(temp.getInspectionNum());
                hssfRow.createCell(4).setCellValue(temp.getNcNum());
                hssfRow.createCell(5).setCellValue(temp.getNcRate());
                //采集项处理
                List<HmeEoJobDataRecordReturnDTO2> tempList = temp.getHmeEoJobDataRecordReturnDTO2s();
                if (CollectionUtils.isNotEmpty(tempList)) {
                    tempList.forEach(tg -> {
                        int tagIndex = headerList.indexOf(tg.getUserName());
                        hssfRow.createCell(tagIndex).setCellValue(tg.getInspectionNum());
                    });
                }
                rowIndex++;
            }

            ExcellUtils.setResponseHeader(response, fileName);

            workbook.write(fOut);

            //操作结束，关闭文件
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
        }
    }
}
