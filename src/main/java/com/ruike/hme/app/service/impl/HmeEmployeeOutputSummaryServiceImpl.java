package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeEmployeeOutputSummaryService;
import com.ruike.hme.app.service.HmeEmployeeOutputSummaryTimeService;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import com.ruike.hme.domain.repository.HmeEmployeeOutputSummaryRepository;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5;
import com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO;
import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4;
import com.ruike.hme.domain.vo.HmeOrganizationVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEmployeeOutputSummaryMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.domain.entity.ItfBomComponentIface;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工产量汇总表应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-07-28 11:19:48
 */
@Service
@Slf4j
public class HmeEmployeeOutputSummaryServiceImpl implements HmeEmployeeOutputSummaryService {

    @Autowired
    private HmeEmployeeOutputSummaryRepository hmeEmployeeOutputSummaryRepository;

    @Autowired
    private HmeEmployeeOutputSummaryTimeService hmeEmployeeOutputSummaryTimeService;

    @Autowired
    private HmeEmployeeOutputSummaryMapper hmeEmployeeOutputSummaryMapper;

    @Autowired
    private WmsSiteRepository wmsSiteRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private CustomSequence customSequence;

    private static final String patter = "yyyy-MM-dd HH";

    /**
     * 根据工序查询工段、产线
     *
     * @param tenantId  租户id
     * @param processIdList  工序ID集合
     * @author penglin.sui@hand-china.com 2021/7/28 17:00
     * @return java.util.Map<java.lang.String, java.util.List< com.ruike.hme.domain.vo.HmeOrganizationVO>>
     */
    private Map<String, List<HmeOrganizationVO>> queryOrganizationByProcessIds(Long tenantId, String siteId , List<String> processIdList){

        if(CollectionUtils.isEmpty(processIdList)){
            return new HashMap<>();
        }

        List<HmeOrganizationVO> allHmeOrganizationVOList = new ArrayList<>();

        List<List<String>> splitProcessIdList = InterfaceUtils.splitSqlList(processIdList, 1000);
        for (List<String> splitProcessIds : splitProcessIdList
             ) {
            List<HmeOrganizationVO> hmeOrganizationVOList = hmeEmployeeOutputSummaryMapper.queryOrganizationByProcessIds(tenantId , siteId , splitProcessIds);
            if(CollectionUtils.isNotEmpty(hmeOrganizationVOList)){
                allHmeOrganizationVOList.addAll(hmeOrganizationVOList);
            }
        }

        if(CollectionUtils.isEmpty(allHmeOrganizationVOList)){
            return new HashMap<>();
        }

        return allHmeOrganizationVOList.stream().collect(Collectors.groupingBy(HmeOrganizationVO::getProcessId));
    }

    /**
     * 汇总数据查询
     *
     * @param tenantId  租户id
     * @param siteId    站点id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param dtoList  工序ID集合
     * @author penglin.sui@hand-china.com 2021/7/29 9:32
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO>
     */
    private Map<String , List<HmeEmployeeOutputSummaryVO>> querySummarys(Long tenantId,
                                                                         String siteId,
                                                                         String startTime,
                                                                         String endTime,
                                                                         List<HmeEmployeeOutputSummary> dtoList){
        List<HmeEmployeeOutputSummaryVO> employeeOutputSummaryVOList =
                hmeEmployeeOutputSummaryMapper.querySummarys(tenantId , siteId , startTime , endTime , dtoList);
        if(CollectionUtils.isEmpty(employeeOutputSummaryVOList)){
            //未查询到${1}!
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "汇总数据"));
        }

        //查询不良数
        List<HmeEmployeeOutputSummaryVO> ncQtyVOList =
                hmeEmployeeOutputSummaryMapper.queryNcQtys(tenantId , startTime , endTime , employeeOutputSummaryVOList);
        Map<String , List<HmeEmployeeOutputSummaryVO>> ngQtyMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(ncQtyVOList)){
            ngQtyMap = ncQtyVOList.stream().collect(Collectors.groupingBy(item -> item.getSiteInBy() + "#" + item.getProcessId()
                    + "#" + item.getProductionVersion() + "#" + item.getSnMaterialId() + "#" + CommonUtils.dateToString(item.getJobTime() , patter) + "#" + item.getEoId()));
        }

        //赋值不良数
        for (HmeEmployeeOutputSummaryVO item : employeeOutputSummaryVOList
        ) {
            List<HmeEmployeeOutputSummaryVO> subNcQtyVOList = ngQtyMap.getOrDefault(item.getSiteInBy() + "#" + item.getProcessId()
                    + "#" + item.getProductionVersion() + "#" + item.getSnMaterialId() + "#" + CommonUtils.dateToString(item.getJobTime() , patter) + "#" + item.getEoId(),
                    new ArrayList<>());

            if(CollectionUtils.isNotEmpty(subNcQtyVOList)){
                // 取进出站记录之前的不良记录
                List<HmeEmployeeOutputSummaryVO> ncList = subNcQtyVOList.stream().filter(e -> e.getJobTime().compareTo(item.getSiteInDate()) >= 0 && e.getJobTime().compareTo(item.getJobTime()) <= 0).collect(Collectors.toList());
                List<String> ncRecordList = ncList.stream().filter(e -> "Y".equals(e.getReworkRecordFlag())).map(HmeEmployeeOutputSummaryVO::getEoId).distinct().collect(Collectors.toList());
                item.setNgQty(BigDecimal.valueOf(ncList.size()));
                item.setReworkRecordQty(BigDecimal.valueOf(ncRecordList.size()));
            }else{
                item.setNgQty(BigDecimal.ZERO);
                item.setReworkRecordQty(BigDecimal.ZERO);
            }
        }

        //按照用户+工序+版本+物料分组
        return employeeOutputSummaryVOList.stream().collect(Collectors.groupingBy(item -> item.getSiteInBy() + "#" + item.getProcessId()
                + "#" + item.getProductionVersion() + "#" + item.getSnMaterialId() + "#" + CommonUtils.dateToString(item.getJobTime() , patter)));
    }

    /**
     * 删除数据
     *
     * @param tenantId  租户id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author penglin.sui@hand-china.com 2021/8/4 19:57
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEmployeeOutputSummaryVO>
     */
    private void deleteData(Long tenantId , String startTime, String endTime){

        List<String> employeeOutputSummaryList = hmeEmployeeOutputSummaryMapper.selectEmployeeOutSummary(tenantId , startTime , endTime);
        if(CollectionUtils.isNotEmpty(employeeOutputSummaryList)){
            hmeEmployeeOutputSummaryRepository.myBatchDelete(employeeOutputSummaryList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void employeeOutPutSummary(Long tenantId) {

        // 获取用户默认站点
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        if(StringUtils.isBlank(defaultSiteId)){
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
        }

        //获取当前时间
        Date now = CommonUtils.currentTimeGet();
        //结束时间 = 当前时间 - 1h
        Date endDateHour = CommonUtils.calculateDate(now , -1 , Calendar.HOUR_OF_DAY , patter);
        String endHour = CommonUtils.dateToString(endDateHour,patter);
        String endTime = endHour + ":59:59";

        //查询汇总成功的最大时间
        Date preMaxJobTime = hmeEmployeeOutputSummaryTimeService.selectMaxJobTime(tenantId);
        if(Objects.isNull(preMaxJobTime)){
            preMaxJobTime = CommonUtils.calculateDate(endDateHour , -1 , Calendar.HOUR_OF_DAY , patter);
        }
        //开始时间 = preMaxJobTime + 1h
        Date startDateHour = CommonUtils.calculateDate(preMaxJobTime , 1 , Calendar.HOUR_OF_DAY , patter);
        String startHour = CommonUtils.dateToString(startDateHour,patter);
        String startTime = startHour + ":00:00";

        //删除数据
        deleteData(tenantId , startTime , endTime);

        List<HmeEmployeeOutputSummary> resultList = new ArrayList<>();
        //进站信息查询
        List<HmeEmployeeOutputSummary> signInList = hmeEmployeeOutputSummaryRepository.selectDataOfSignIn(tenantId , startTime , endTime);
        if(CollectionUtils.isNotEmpty(signInList)){
            resultList.addAll(signInList);
        }

        //出站信息查询
        List<HmeEmployeeOutputSummary> signOutList = hmeEmployeeOutputSummaryRepository.selectDataOfSignOut(tenantId , startTime , endTime);
        if(CollectionUtils.isNotEmpty(signOutList)){
            resultList.addAll(signOutList);
        }

        if(CollectionUtils.isEmpty(resultList)){
            return;
        }

        //去重
        resultList = resultList.stream().filter(CommonUtils.distinctByKey(item -> item.getTenantId() + "#" + item.getMaterialId()
        + "#" + CommonUtils.dateToString(item.getJobTime() , patter) + item.getUserId() + item.getProcessId() + item.getProductionVersion()))
                .collect(Collectors.toList());

        // 根据工序查询工段、产线
        List<String> processIdList = resultList.stream()
                .map(HmeEmployeeOutputSummary::getProcessId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<String, List<HmeOrganizationVO>> hmeOrganizationVOMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(processIdList)){
            hmeOrganizationVOMap = queryOrganizationByProcessIds(tenantId , defaultSiteId , processIdList);
        }

        //汇总数据查询
        Map<String , List<HmeEmployeeOutputSummaryVO>> employeeOutputSummaryMap =
                querySummarys(tenantId , defaultSiteId , startTime , endTime , resultList);

        List<String> outputSummaryIdList = customSequence.getNextKeys("hme_employee_output_summary_s", resultList.size());
        List<String> outputSummaryCidList = customSequence.getNextKeys("hme_employee_output_summary_cid_s", 1);
        Long cid = Long.valueOf(outputSummaryCidList.get(0));

        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        BigDecimal qty = BigDecimal.ZERO;
        int index = 0;
        for (HmeEmployeeOutputSummary dto : resultList
        ) {
            //主键
            dto.setOutputSummaryId(outputSummaryIdList.get(index++));

            //CID
            dto.setCid(cid);

            //版本
            dto.setObjectVersionNumber(1L);

            //WHO字段
            dto.setCreatedBy(userId);
            dto.setCreationDate(now);
            dto.setLastUpdatedBy(userId);
            dto.setLastUpdateDate(now);

            //修改时间格式
            dto.setJobTime(CommonUtils.stringToDate(CommonUtils.dateToString(dto.getJobTime() , patter),patter));

            List<HmeOrganizationVO> hmeOrganizationVOS = hmeOrganizationVOMap.get(dto.getProcessId());
            // 工段
            dto.setLineId(hmeOrganizationVOS.get(0).getLineWorkcellId());
            dto.setLineCode(hmeOrganizationVOS.get(0).getLineWorkcellCode());
            dto.setLineName(hmeOrganizationVOS.get(0).getLineWorkcellName());
            //产线
            dto.setProdLineId(hmeOrganizationVOS.get(0).getProdLineId());
            dto.setProdLineCode(hmeOrganizationVOS.get(0).getProdLineCode());
            dto.setProdLineName(hmeOrganizationVOS.get(0).getProdLineName());

            List<HmeEmployeeOutputSummaryVO> subEmployeeOutputSummaryList = employeeOutputSummaryMap.getOrDefault(dto.getUserId()
                    + "#" + dto.getProcessId() + "#" + dto.getProductionVersion() + "#" + dto.getMaterialId() + "#"
                    + CommonUtils.dateToString(dto.getJobTime() , patter) , new ArrayList<>());
            if(CollectionUtils.isEmpty(subEmployeeOutputSummaryList)){
                dto.setOutputQty(BigDecimal.ZERO);
                dto.setReworkQty(BigDecimal.ZERO);
                dto.setTotalDuration(BigDecimal.ZERO);
                dto.setActualOutputQty(BigDecimal.ZERO);
                dto.setNcQty(BigDecimal.ZERO);
                dto.setAttribute1(BigDecimal.ZERO.toString());
            }else{
                //实际产出
                qty = subEmployeeOutputSummaryList.stream()
                        .filter(item -> HmeConstants.ConstantValue.NO.equals(item.getReworkFlag()) && item.getNgQty().compareTo(BigDecimal.ZERO) <= 0)
                        .map(HmeEmployeeOutputSummaryVO::getQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setActualOutputQty(qty);

                //产量
                BigDecimal reworkQty = subEmployeeOutputSummaryList.stream()
                        .filter(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag()) && item.getNgQty().compareTo(BigDecimal.ZERO) <= 0)
                        .map(HmeEmployeeOutputSummaryVO::getQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setOutputQty(qty.add(reworkQty));

                //返修数
                qty = subEmployeeOutputSummaryList.stream()
                        .filter(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag()))
                        .map(HmeEmployeeOutputSummaryVO::getQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setReworkQty(qty);

                //生产总时长
                qty = subEmployeeOutputSummaryList.stream()
                        .map(HmeEmployeeOutputSummaryVO::getTotalProductionTime)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setTotalDuration(qty);

                //不良
                qty = subEmployeeOutputSummaryList.stream()
                        .map(HmeEmployeeOutputSummaryVO::getNgQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setNcQty(qty);

                // 20211210 add by sanfeng.zhang for peng.zhao 存返修记录的不良数
                qty = subEmployeeOutputSummaryList.stream()
                        .map(HmeEmployeeOutputSummaryVO::getReworkRecordQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                dto.setAttribute1(qty.toString());
            }
        }

        //新增汇总数据
        hmeEmployeeOutputSummaryRepository.myBatchInsert(resultList);

        //新增时间数据
        hmeEmployeeOutputSummaryTimeService.batchInserData(tenantId , startDateHour , endDateHour);
    }
}
