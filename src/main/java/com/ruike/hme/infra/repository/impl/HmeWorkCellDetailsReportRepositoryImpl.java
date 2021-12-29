package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.domain.repository.HmeWorkCellDetailsReportRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeWorkCellDetailsReportMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcellUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工位产量明细报表
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 14:46
 */
@Component
@Slf4j
public class HmeWorkCellDetailsReportRepositoryImpl implements HmeWorkCellDetailsReportRepository {

    @Autowired
    private HmeWorkCellDetailsReportMapper hmeWorkCellDetailsReportMapper;
    @Autowired
    private HmeHzeroIamFeignClient hmeHzeroIamFeignClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;

    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    private static final int DAY_7 = 7;
    private static final String WORKSHOP = "WORKSHOP"; //车间
    private static final String AREA = "AREA";  // 制造部
    private static final String PROD_LINE = "PROD_LINE"; // 产线

    @Override
    public Page<HmeWorkCellDetailsReportVO2> queryWorkCellReportList(Long tenantId, HmeWorkCellDetailsReportVO reportVO, PageRequest pageRequest) {
        //获取当前用户的站点信息
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        MtModOrganizationVO2 orgVO = new MtModOrganizationVO2();
        orgVO.setTopSiteId(defaultSiteId);

        //工位 必输
        if (StringUtils.isBlank(reportVO.getWorkcellId())) {
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        Page<HmeWorkCellDetailsReportVO2> reportVO2List = PageHelper.doPage(pageRequest, () -> hmeWorkCellDetailsReportMapper.queryWorkCellReportList(tenantId, reportVO));

        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.JOB_TYPE", tenantId);
        List<String> workcellIdList = reportVO2List.getContent().stream().map(HmeWorkCellDetailsReportVO2::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> processIdList = reportVO2List.getContent().stream().map(HmeWorkCellDetailsReportVO2::getProcessId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> lineWorkIdList = reportVO2List.getContent().stream().map(HmeWorkCellDetailsReportVO2::getLineWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> workIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workcellIdList)) {
            workIdList.addAll(workcellIdList);
        }
        if (CollectionUtils.isNotEmpty(processIdList)) {
            workIdList.addAll(processIdList);
        }
        if (CollectionUtils.isNotEmpty(lineWorkIdList)) {
            workIdList.addAll(lineWorkIdList);
        }
        //批量查询工位 工段 工序
        List<MtModWorkcell> mtModWorkcellList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workIdList)) {
            mtModWorkcellList = mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workIdList);
        }
        Map<String, List<MtModWorkcell>> mtModWorkcellMap = mtModWorkcellList.stream().collect(Collectors.groupingBy(MtModWorkcell::getWorkcellId));
        //批量查询产线
        List<String> proLineIdList = reportVO2List.getContent().stream().map(HmeWorkCellDetailsReportVO2::getProductionLineId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<MtModProductionLine> mtModProductionLineList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(proLineIdList)) {
            mtModProductionLineList = mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, proLineIdList);
        }
        Map<String, List<MtModProductionLine>> lineMap = mtModProductionLineList.stream().collect(Collectors.groupingBy(MtModProductionLine::getProdLineId));
        reportVO2List.forEach(e -> {
            //生产线处理
            if (StringUtils.isNotBlank(e.getProductionLineId())) {
                List<MtModProductionLine> lineList = lineMap.get(e.getProductionLineId());
                e.setProductionLineName(CollectionUtils.isNotEmpty(lineList) ? lineList.get(0).getProdLineName() : "");
            }
            //工段
            if (StringUtils.isNotBlank(e.getLineWorkcellId())) {
                List<MtModWorkcell> workcellList = mtModWorkcellMap.get(e.getLineWorkcellId());
                e.setLineWorkcellName(CollectionUtils.isNotEmpty(workcellList) ? workcellList.get(0).getWorkcellName() : "");
            }
            //工位
            if (StringUtils.isNotBlank(e.getWorkcellId())) {
                List<MtModWorkcell> workcellList = mtModWorkcellMap.get(e.getWorkcellId());
                e.setStationWorkcellName(CollectionUtils.isNotEmpty(workcellList) ? workcellList.get(0).getWorkcellName() : "");
            }
            //工序
            if (StringUtils.isNotBlank(e.getProcessId())) {
                List<MtModWorkcell> workcellList = mtModWorkcellMap.get(e.getProcessId());
                e.setProcessWorkcellName(CollectionUtils.isNotEmpty(workcellList) ? workcellList.get(0).getWorkcellName() : "");
            }

            //作业人处理
            if (StringUtils.equals(e.getFlag(), HmeConstants.ConstantValue.NO)) {
                e.setWorkerName(StringUtils.isNotBlank(e.getSiteInBy()) ? userClient.userInfoGet(tenantId, Long.valueOf(e.getSiteInBy())).getRealName() : "");
                e.setJobTypeName("进站");
            } else if (StringUtils.equals(e.getFlag(), HmeConstants.ConstantValue.YES)) {
                e.setWorkerName(StringUtils.isNotBlank(e.getSiteOutBy()) ? userClient.userInfoGet(tenantId, Long.valueOf(e.getSiteOutBy())).getRealName() : "");
                e.setJobTypeName("出站");
            }

            //作业平台
            if (StringUtils.isNotBlank(e.getJobPlatformCode())) {
                List<LovValueDTO> collect = list.stream().filter(f -> StringUtils.equals(f.getValue(), e.getJobPlatformCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    e.setJobPlatform(collect.get(0).getMeaning());
                }
            }
        });

        return reportVO2List;
    }

    @Override
    public String queryProductionLineName(Long tenantId, String organizationId) {
        return hmeWorkCellDetailsReportMapper.queryProductionLineName(tenantId, organizationId);
    }

    @Override
    public String queryLineWorkcellName(Long tenantId, String organizationId, String workcellType) {
        return hmeWorkCellDetailsReportMapper.queryLineWorkcellName(tenantId, organizationId, workcellType);
    }

    @Override
    public Page<HmeWorkCellVO> workCellUiQuery(Long tenantId, HmeWorkCellVO hmeWorkCellVO, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeWorkCellDetailsReportMapper.workCellUiQuery(tenantId, hmeWorkCellVO));
    }

    @Override
    @ProcessLovValue
    public Page<HmeProcessReportVo2> queryProcessReportList(Long tenantId, HmeProcessReportVo reportVO, PageRequest pageRequest) {
        Page<HmeProcessReportVo2> reportList = PageHelper.doPage(pageRequest, () -> hmeWorkCellDetailsReportMapper.queryProcessReportList(tenantId, reportVO));
        List<Long> userId = reportList.getContent().stream().map(HmeProcessReportVo2::getSiteInBy).filter(Objects::nonNull).distinct().map(Long::valueOf).collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, userId);
        //批量查询采集项
        List<String> jobIdList = reportList.getContent().stream().map(HmeProcessReportVo2::getJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<HmeProcessCollectVO> hmeProcessCollectVOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(jobIdList)) {
            hmeProcessCollectVOS = hmeWorkCellDetailsReportMapper.queryBatchProcessCollectList(tenantId, jobIdList);
        }
        Map<String, List<HmeProcessCollectVO>> processMap = hmeProcessCollectVOS.stream().collect(Collectors.groupingBy(HmeProcessCollectVO::getJobId));
        for(HmeProcessReportVo2 e : reportList){
            //是否冻结
            if(StringUtils.isBlank(e.getFreezeFlag())){
                e.setFreezeFlag("N");
            }
            //加工人
            if (StringUtils.isNotBlank(e.getSiteInBy())) {
                MtUserInfo mtUserInfo = userInfoMap.get(Long.valueOf(e.getSiteInBy()));
                e.setWorker(mtUserInfo != null ? mtUserInfo.getRealName() : "");
            }
            e.setWorkTime(e.getSiteOutDate());
            //采集项处理
            List<HmeProcessCollectVO> collectList = processMap.get(e.getJobId());
            //数据汇总 值为数值的累加
            List<HmeProcessCollectVO> collectHandleRepeatList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(collectList)) {
                for (HmeProcessCollectVO collectVO : collectList) {
                    List<HmeProcessCollectVO> filterList = collectHandleRepeatList.stream().filter(f -> StringUtils.equals(f.getProName(), collectVO.getProName())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(filterList)) {
                        //为数值 进行累加  替换原数组
                        if (StringUtils.isNotBlank(collectVO.getProResult()) && CommonUtils.isNumeric(collectVO.getProResult())) {
                            if (StringUtils.isNotBlank(filterList.get(0).getProResult())) {
                                if (StringUtils.isNotBlank(filterList.get(0).getProResult()) && CommonUtils.isNumeric(filterList.get(0).getProResult())) {
                                    collectVO.setProResult(BigDecimal.valueOf(Double.parseDouble(collectVO.getProResult())).add(BigDecimal.valueOf(Double.parseDouble(filterList.get(0).getProResult()))).toString());
                                }
                            } else {
                                collectVO.setProResult(collectVO.getProResult());
                            }
                            collectVO.setProCode(filterList.get(0).getProCode());
                            Collections.replaceAll(collectHandleRepeatList, filterList.get(0), collectVO);
                        }
                    } else {
                        collectVO.setProCode("code" + (collectHandleRepeatList.size() + 1));
                        collectHandleRepeatList.add(collectVO);
                    }
                }
            }
            e.setProcessCollectList(collectHandleRepeatList);
        };
        return reportList;
    }

    private void reportParamValid(Long tenantId, HmeProcessReportVo reportVO) {
        if (!(StringUtils.isNotBlank(reportVO.getSn()) || StringUtils.isNotBlank(reportVO.getMaterialCode()) || StringUtils.isNotBlank(reportVO.getProcessCode()) || StringUtils.isNotBlank(reportVO.getWorkOrderNum()))) {
            throw new MtException("HME_PRO_REPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_001", "HME"));
        }
    }

    @Override
    @ProcessLovValue
    public void queryProcessReportExport(Long tenantId, HmeProcessReportVo reportVO, HttpServletResponse response) {
        List<HmeProcessReportVo2> hmeProcessReportVo2List = hmeWorkCellDetailsReportMapper.queryProcessReportList(tenantId, reportVO);
        List<Long> userId = hmeProcessReportVo2List.stream().map(HmeProcessReportVo2::getSiteInBy).filter(Objects::nonNull).distinct().map(Long::valueOf).collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, userId);
        //批量查询采集项
        List<String> jobIdList = hmeProcessReportVo2List.stream().map(HmeProcessReportVo2::getJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<HmeProcessCollectVO> hmeProcessCollectVOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(jobIdList)) {
            hmeProcessCollectVOS = hmeWorkCellDetailsReportMapper.queryBatchProcessCollectList(tenantId, jobIdList);
        }
        Map<String, List<HmeProcessCollectVO>> processMap = hmeProcessCollectVOS.stream().collect(Collectors.groupingBy(HmeProcessCollectVO::getJobId));

        //采集项列表
        List<HmeProcessCollectVO> processCollectVOList = new ArrayList<>();
        hmeProcessReportVo2List.forEach(e -> reportFieldGet(userInfoMap, processMap, processCollectVOList, e));

        //导出数据
        log.info(">>>>>>>>>>>>>>>>>>>>>>开始导出数据采集项数据");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("工序采集项");
        String fileName = "工序采集项表" + DateUtil.format(new Date(), "yyyyMMdd") + ".xls";

        List<String> tagList = processCollectVOList.stream().map(HmeProcessCollectVO::getProName).distinct().collect(Collectors.toList());
        try {
            //创建excel文件对象
            OutputStream fOut = response.getOutputStream();
            //新增数据行，并且设置单元格数据
            //headers表示excel表中第一行的表头
            List<String> headerList = new ArrayList<>();
            String[] headers = {"产品料号", "产品描述", "工单号", "序列号", "加工时间", "工序", "工位", "加工人"};
            headerList.addAll(Arrays.asList(headers));
            headerList.addAll(tagList);

            // 创建一个新的HSSFWorkbook对象
            HSSFRow row = sheet.createRow(0);
            //标题
            row.setHeightInPoints(30);
            HSSFCell cell1 = row.createCell(0);
            Map<String, CellStyle> styles = ExcellUtils.createStyles(workbook);
            cell1.setCellStyle(styles.get("title"));
            cell1.setCellValue("工 序 采 集 项 报 表");
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
            for (HmeProcessReportVo2 hmeProcessReportVo2 : hmeProcessReportVo2List) {
                HSSFRow hssfRow = sheet.createRow(rowIndex);
                hssfRow.createCell(0).setCellValue(hmeProcessReportVo2.getMaterialCode());
                hssfRow.createCell(1).setCellValue(hmeProcessReportVo2.getMaterialName());
                hssfRow.createCell(2).setCellValue(hmeProcessReportVo2.getWorkOrderNum());
                hssfRow.createCell(3).setCellValue(hmeProcessReportVo2.getIdentification());
                hssfRow.createCell(4).setCellValue(hmeProcessReportVo2.getWorkTime() != null ? DateUtil.format(hmeProcessReportVo2.getWorkTime(), "yyyy-MM-dd HH:mm:ss") : "");
                hssfRow.createCell(5).setCellValue(hmeProcessReportVo2.getProcessWorkcellName());
                hssfRow.createCell(6).setCellValue(hmeProcessReportVo2.getWorkcellName());
                hssfRow.createCell(7).setCellValue(hmeProcessReportVo2.getWorker());
                //采集项处理
                List<HmeProcessCollectVO> processCollectList = hmeProcessReportVo2.getProcessCollectList();
                if (CollectionUtils.isNotEmpty(processCollectList)) {
                    processCollectList.forEach(tg -> {
                        int tagIndex = headerList.indexOf(tg.getProName());
                        hssfRow.createCell(tagIndex).setCellValue(tg.getProResult());
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
            log.error(">>>>>>>>>>>>>>>>>>>>>>>工序采集项导出出错: {}", e.getMessage());
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>工序采集项导出完成");
    }

    @Override
    public Page<HmeProcessJobDetailVO> pagedJobDetail(Long tenantId, String jobId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeWorkCellDetailsReportMapper.selectProcessJobDetailList(tenantId, jobId));
    }

    private void reportFieldGet(Map<Long, MtUserInfo> userInfoMap, Map<String, List<HmeProcessCollectVO>> processMap, List<HmeProcessCollectVO> processCollectVOList, HmeProcessReportVo2 e) {
        //加工人
        if (StringUtils.isNotBlank(e.getSiteInBy())) {
            MtUserInfo mtUserInfo = userInfoMap.get(Long.valueOf(e.getSiteInBy()));
            e.setWorker(mtUserInfo != null ? mtUserInfo.getRealName() : "");
        }
        e.setWorkTime(e.getSiteOutDate());
        //采集项处理
        List<HmeProcessCollectVO> collectList = processMap.get(e.getJobId());
        //数据汇总 值为数值的累加
        List<HmeProcessCollectVO> collectHandleRepeatList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(collectList)) {
            for (HmeProcessCollectVO collectVO : collectList) {
                List<HmeProcessCollectVO> filterList = collectHandleRepeatList.stream().filter(f -> StringUtils.equals(f.getProName(), collectVO.getProName())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(filterList)) {
                    //为数值 进行累加  替换原数组
                    if (StringUtils.isNotBlank(collectVO.getProResult()) && CommonUtils.isNumeric(collectVO.getProResult())) {
                        if (StringUtils.isNotBlank(filterList.get(0).getProResult())) {
                            if (StringUtils.isNotBlank(filterList.get(0).getProResult()) && CommonUtils.isNumeric(filterList.get(0).getProResult())) {
                                collectVO.setProResult(BigDecimal.valueOf(Double.parseDouble(collectVO.getProResult())).add(BigDecimal.valueOf(Double.parseDouble(filterList.get(0).getProResult()))).toString());
                            }
                        } else {
                            collectVO.setProResult(collectVO.getProResult());
                        }
                        collectVO.setProCode(filterList.get(0).getProCode());
                        Collections.replaceAll(collectHandleRepeatList, filterList.get(0), collectVO);
                    }
                } else {
                    collectVO.setProCode("code" + (collectHandleRepeatList.size() + 1));
                    collectHandleRepeatList.add(collectVO);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(collectHandleRepeatList)) {
            processCollectVOList.addAll(collectHandleRepeatList);
        }
        e.setProcessCollectList(collectHandleRepeatList);
    }

    @Override
    public Page<HmeExceptionReportVO2> queryExceptionReportList(Long tenantId, HmeExceptionReportVO reportVO, PageRequest pageRequest) {
        Page<HmeExceptionReportVO2> reportList = PageHelper.doPage(pageRequest, () -> hmeWorkCellDetailsReportMapper.queryExceptionReportList(tenantId, reportVO));
        // 批量查询发起人和关闭人信息
        List<Long> userIdList = new ArrayList<>();
        List<Long> createByIdList = reportList.stream().map(createBy -> StringUtils.isNotBlank(createBy.getCreatedBy()) ? Long.valueOf(createBy.getCreatedBy()) : null).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(createByIdList)) {
            userIdList.addAll(createByIdList);
        }
        List<Long> closeByIdList = reportList.stream().map(closeBy -> StringUtils.isNotBlank(closeBy.getClosedBy()) ? Long.valueOf(closeBy.getClosedBy()) : null).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(closeByIdList)) {
            userIdList.addAll(closeByIdList);
        }
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, userIdList);

        // 查询未绑定工位的异常信息 根据ATTRIBUTE1、ATTRIBUTE2取相应的制造部、车间、产线及工位
        List<HmeExceptionReportVO2> nonBindWorkCellList = reportList.getContent().stream().filter(report -> StringUtils.isBlank(report.getWorkcellId())).collect(Collectors.toList());
        Map<String, List<HmeExceptionReportVO2>> nonBindWorkCellMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(nonBindWorkCellList)) {
            nonBindWorkCellMap = nonBindWorkCellList.stream().collect(Collectors.groupingBy(dto -> dto.getOrganizationType() + "_" + dto.getOrganizationId()));
        }
        for (Map.Entry<String, List<HmeExceptionReportVO2>> nonBindWorkCellEntry : nonBindWorkCellMap.entrySet()) {
            // 根据类型区分制造部、车间及产线
            List<HmeExceptionReportVO2> reportVO2List = nonBindWorkCellEntry.getValue();
            HmeExceptionReportVO2 hmeExceptionReportVO2 = reportVO2List.get(0);
            if (AREA.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 制造部的话  只显示制造部
                MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(hmeExceptionReportVO2.getOrganizationId());
                reportVO2List.forEach(vo -> {
                    vo.setAreaId(mtModArea.getAreaId());
                    vo.setAreaName(mtModArea.getAreaName());
                });
            } else if (WORKSHOP.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 车间 根据组织关系表 找到制造部
                MtModArea workshop = mtModAreaRepository.selectByPrimaryKey(hmeExceptionReportVO2.getOrganizationId());
                // 查询制造部
                List<MtModOrganizationRel> areaList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                    setTenantId(tenantId);
                    setOrganizationId(workshop.getAreaId());
                    setOrganizationType("AREA");
                }});
                MtModArea mtModArea = null;
                if (CollectionUtils.isNotEmpty(areaList)) {
                    mtModArea = mtModAreaRepository.selectByPrimaryKey(areaList.get(0).getParentOrganizationId());
                }
                for (HmeExceptionReportVO2 exceptionReportVO2 : reportVO2List) {
                    if (mtModArea != null) {
                        exceptionReportVO2.setAreaId(mtModArea.getAreaId());
                        exceptionReportVO2.setAreaName(mtModArea.getAreaName());
                    }
                    exceptionReportVO2.setWorkshopId(workshop.getAreaId());
                    exceptionReportVO2.setWorkshopName(workshop.getAreaName());
                }
            } else if (PROD_LINE.equals(hmeExceptionReportVO2.getOrganizationType())) {
                // 产线 根据组织关系表 找车间、制造部
                MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(hmeExceptionReportVO2.getOrganizationId());
                // 车间
                MtModArea workshop = null;
                List<MtModOrganizationRel> workshopList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                    setTenantId(tenantId);
                    setOrganizationId(productionLine.getProdLineId());
                    setOrganizationType(PROD_LINE);
                    setParentOrganizationType(AREA);
                }});
                if (CollectionUtils.isNotEmpty(workshopList)) {
                    workshop = mtModAreaRepository.selectByPrimaryKey(workshopList.get(0).getParentOrganizationId());
                }
                // 制造部
                MtModArea mtModArea = null;
                if (workshop != null) {
                    String areaId = workshop.getAreaId();
                    List<MtModOrganizationRel> areaList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                        setTenantId(tenantId);
                        setOrganizationId(areaId);
                        setOrganizationType("AREA");
                    }});

                    if (CollectionUtils.isNotEmpty(areaList)) {
                        mtModArea = mtModAreaRepository.selectByPrimaryKey(areaList.get(0).getParentOrganizationId());
                    }
                }
                for (HmeExceptionReportVO2 exceptionReportVO2 : reportVO2List) {
                    if (mtModArea != null) {
                        exceptionReportVO2.setAreaId(mtModArea.getAreaId());
                        exceptionReportVO2.setAreaName(mtModArea.getAreaName());
                    }
                    if (workshop != null) {
                        exceptionReportVO2.setWorkshopId(workshop.getAreaId());
                        exceptionReportVO2.setWorkshopName(workshop.getAreaName());
                    }
                    exceptionReportVO2.setProdLineId(productionLine.getProdLineId());
                    exceptionReportVO2.setProdLineName(productionLine.getProdLineName());
                }
            }

        }
        // 批量查询制造部
        List<String> areaAllIdList = new ArrayList<>();
        List<String> areaIdList = reportList.getContent().stream().map(HmeExceptionReportVO2::getAreaId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(areaIdList)) {
            areaAllIdList.addAll(areaIdList);
        }
        // 批量查询车间
        List<String> workShopList = reportList.getContent().stream().map(HmeExceptionReportVO2::getWorkshopId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(workShopList)) {
            areaAllIdList.addAll(workShopList);
        }
        Map<String, List<MtModArea>> areaMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(areaAllIdList)) {
            List<MtModArea> areaList = mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaAllIdList);
            if (CollectionUtils.isNotEmpty(areaList)) {
                areaMap = areaList.stream().collect(Collectors.groupingBy(MtModArea::getAreaId));
            }
        }
        // 批量查询产线
        List<String> proLineIdList = reportList.getContent().stream().map(HmeExceptionReportVO2::getProdLineId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<MtModProductionLine>> proLineMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(proLineIdList)) {
            List<MtModProductionLine> proLineList = mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, proLineIdList);
            if (CollectionUtils.isNotEmpty(proLineList)) {
                proLineMap = proLineList.stream().collect(Collectors.groupingBy(MtModProductionLine::getProdLineId));
            }
        }

        Map<String, List<MtModArea>> areaFinalMap = areaMap;
        Map<String, List<MtModProductionLine>> proLineFinalMap = proLineMap;
        Map<String, List<HmeExceptionReportVO2>> nonBindWorkCellFinalMap = nonBindWorkCellMap;
        reportList.forEach(e -> {
            if (StringUtils.isNotBlank(e.getWorkcellId())) {
                // 绑定了工位 直接查询制造部、车间及产线
                // 制造部
                if (StringUtils.isNotBlank(e.getAreaId())) {
                    List<MtModArea> mtModAreas = areaFinalMap.get(e.getAreaId());
                    if (CollectionUtils.isNotEmpty(mtModAreas)) {
                        e.setAreaName(mtModAreas.get(0).getAreaName());
                    }
                }

                //车间
                if (StringUtils.isNotBlank(e.getWorkshopId())) {
                    List<MtModArea> workshopList = areaFinalMap.get(e.getWorkshopId());
                    if (CollectionUtils.isNotEmpty(workshopList)) {
                        e.setWorkshopName(workshopList.get(0).getAreaName());
                    }
                }
                //产线
                if (StringUtils.isNotBlank(e.getProdLineId())) {
                    List<MtModProductionLine> proLines = proLineFinalMap.get(e.getProdLineId());
                    if (CollectionUtils.isNotEmpty(proLines)) {
                        e.setProdLineName(proLines.get(0).getProdLineName());
                    }
                }
            } else {
                // 未绑定工位  则根据ATTRIBUTE1、ATTRIBUTE2取相应的制造部、车间、产线
                String mapKey = e.getOrganizationType() + "_" + e.getOrganizationId();
                List<HmeExceptionReportVO2> reportVO2List = nonBindWorkCellFinalMap.get(mapKey);
                if (CollectionUtils.isNotEmpty(reportVO2List)) {
                    e.setAreaName(reportVO2List.get(0).getAreaName());
                    e.setWorkshopName(reportVO2List.get(0).getWorkshopName());
                    e.setProdLineName(reportVO2List.get(0).getProdLineName());
                }
            }

            //班组
            if (StringUtils.isNotBlank(e.getShiftId())) {
                ResponseEntity<HmeHzeroPlatformUnitDTO> unitsInfo = hmeHzeroPlatformFeignClient.getUnitsInfo(tenantId, e.getShiftId());
                if (unitsInfo.getBody() != null) {
                    e.setShiftName(unitsInfo.getBody().getUnitName());
                }
            }

            //附件名称
            if (StringUtils.isNotBlank(e.getAttachmentUuid())) {
                ResponseEntity<List<HmeHzeroFileDTO>> unitsInfo = hmeHzeroFileFeignClient.getUnitsInfo(tenantId, e.getAttachmentUuid());
                if (CollectionUtils.isNotEmpty(unitsInfo.getBody())) {
                    String attachmentName = "";
                    for (HmeHzeroFileDTO hmeHzeroFileDTO : unitsInfo.getBody()) {
                        attachmentName += StringUtils.isNotEmpty(attachmentName) ? "_" : "" + hmeHzeroFileDTO.getFileName();
                    }
                    e.setAttachmentName(attachmentName);
                    e.setFileList(unitsInfo.getBody());
                }
            }

            //异常状态描述
            if (StringUtils.isNotBlank(e.getExceptionStatus())) {
                List<LovValueDTO> list = lovAdapter.queryLovValue("HME.EXCEPTION_STATUS", tenantId);
                List<LovValueDTO> collect = list.stream().filter(f -> StringUtils.equals(f.getValue(), e.getExceptionStatus())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    e.setExceptionStatusName(collect.get(0).getMeaning());
                }
            }

            //异常类型描述
            if (StringUtils.isNotBlank(e.getExceptionType())) {
                e.setExceptionTypeName(hmeWorkCellDetailsReportMapper.queryExceptionTypeName(tenantId, e.getExceptionType(), "zh_CN"));
            }

            //发起人
            if (StringUtils.isNotBlank(e.getCreatedBy())) {
                MtUserInfo mtUserInfo = userInfoMap.get(Long.valueOf(e.getCreatedBy()));
                e.setCreatedByName(mtUserInfo != null ? mtUserInfo.getRealName() : "");
            }

            //响应人
            if (Strings.isNotEmpty(e.getRespondedBy())) {
                ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, e.getRespondedBy(), "P");
                if (Strings.isEmpty(userInfo.getBody().getLoginName())) {
                    e.setRespondedByName("工号：" + e.getRespondedBy() + "，根据工号找不到用户，请与OA系统核对");
                } else {
                    String realName = userClient.userInfoGet(tenantId, Long.valueOf(userInfo.getBody().getId())).getRealName();
                    e.setRespondedByName(realName);
                }
            } else {
                e.setRespondedByName("");
            }

            //关闭人
            if (StringUtils.isNotBlank(e.getClosedBy())) {
                MtUserInfo mtUserInfo = userInfoMap.get(Long.valueOf(e.getClosedBy()));
                e.setClosedByName(mtUserInfo != null ? mtUserInfo.getRealName() : "");
            }
        });
        return reportList;
    }

    @Override
    public List<MtModLocator> queryParentLocatorId(Long tenantId, String locatorId) {
        return hmeWorkCellDetailsReportMapper.queryParentLocatorId(tenantId, locatorId);
    }
}
