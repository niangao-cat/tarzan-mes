package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import com.ruike.hme.domain.vo.HmeReportSetupVO;
import com.ruike.hme.domain.vo.HmeReportSetupVO2;
import com.ruike.hme.domain.vo.HmeReportSetupVO3;
import com.ruike.hme.domain.vo.HmeReportSetupVO4;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.mapper.HmeReportSetupMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.message.util.DateUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeReportSetup;
import com.ruike.hme.domain.repository.HmeReportSetupRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 看板配置基础数据表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-10-22 09:43:00
 */
@Component
public class HmeReportSetupRepositoryImpl extends BaseRepositoryImpl<HmeReportSetup> implements HmeReportSetupRepository {

    @Autowired
    private HmeReportSetupMapper hmeReportSetupMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;

    @Override
    public List<HmeReportSetupVO> reportTypeList(Long tenantId) {
        List<HmeReportSetupVO> setupVOList = new ArrayList<>();
        List<LovValueDTO> reportTypeList = lovAdapter.queryLovValue("HME.REPORT_TYPE", tenantId);
        reportTypeList.forEach(report ->{
            HmeReportSetupVO vo = new HmeReportSetupVO();
            vo.setReportName(report.getMeaning());
            vo.setReportType(report.getValue());
            vo.setSort(report.getOrderSeq());
            setupVOList.add(vo);
        });
        return setupVOList.stream().sorted(Comparator.comparing(HmeReportSetupVO::getSort)).collect(Collectors.toList());
    }

    @Override
    public Page<HmeReportSetupVO2> queryReportSetupsList(Long tenantId, String reportType, PageRequest pageRequest) {
        Page<HmeReportSetupVO2> pageResult = PageHelper.doPage(pageRequest, () -> hmeReportSetupMapper.queryReportSetupsList(tenantId, reportType));
        //看板类型
        List<LovValueDTO> reportTypeList = lovAdapter.queryLovValue("HME.REPORT_TYPE", tenantId);
        //有效性
        List<LovValueDTO> flagList = lovAdapter.queryLovValue("HPFM.ENABLED_FLAG", tenantId);
        pageResult.getContent().forEach(dto -> {
            //看板类型含义
            if(StringUtils.isNotBlank(dto.getReportType())){
                Optional<LovValueDTO> reportOpt = reportTypeList.stream().filter(report -> StringUtils.equals(report.getValue(), dto.getReportType())).findFirst();
                if(reportOpt.isPresent()){
                    dto.setReportTypeMeaning(reportOpt.get().getMeaning());
                }
            }

            //有效性含义
            if(StringUtils.isNotBlank(dto.getEnableFlag())){
                Optional<LovValueDTO> flagOpt = flagList.stream().filter(flag -> StringUtils.equals(flag.getValue(), dto.getEnableFlag())).findFirst();
                if(flagOpt.isPresent()){
                    dto.setEnableFlagMeaning(flagOpt.get().getMeaning());
                }
            }
        });
        return pageResult;
    }

    @Override
    public HmeReportSetupVO2 saveReportSetups(Long tenantId, HmeReportSetupVO2 setupVO2) {
        if(StringUtils.isBlank(setupVO2.getSiteId())){
            //获取用户默认站点
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            String defaultSite = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
            if(StringUtils.isBlank(defaultSite)){
                throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
            }
            setupVO2.setSiteId(defaultSite);
        }

        //校验数据
        if(StringUtils.isBlank(setupVO2.getProdLineId())){
            throw new MtException("HME_REPORT_SETUPS_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPORT_SETUPS_001", "HME"));
        }

        if(StringUtils.isBlank(setupVO2.getAttachmentUuid())){
            throw new MtException("HME_REPORT_SETUPS_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPORT_SETUPS_002", "HME"));
        }

        if(StringUtils.isBlank(setupVO2.getEnableFlag())){
            throw new MtException("HME_REPORT_SETUPS_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPORT_SETUPS_003", "HME"));
        }

        //类型为MONTHLY_OUTPUT_REPORT时 工段必填
        if(StringUtils.equals("MONTHLY_OUTPUT_REPORT", setupVO2.getReportType()) && StringUtils.isBlank(setupVO2.getWorkcellId())){
            throw new MtException("HME_REPORT_SETUPS_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPORT_SETUPS_004", "HME"));
        }

        //校验唯一性
        List<HmeReportSetup> hmeReportSetupList = hmeReportSetupMapper.selectByCondition(Condition.builder(HmeReportSetup.class)
                .andWhere(Sqls.custom().andEqualTo(HmeReportSetup.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeReportSetup.FIELD_SITE_ID, setupVO2.getSiteId())
                        .andEqualTo(HmeReportSetup.FIELD_REPORT_TYPE, setupVO2.getReportType())
                        .andEqualTo(HmeReportSetup.FIELD_PROD_LINE_ID, setupVO2.getProdLineId())).build());

        HmeReportSetup reportSetup = new HmeReportSetup();
        BeanUtils.copyProperties(setupVO2, reportSetup);
        if(StringUtils.isBlank(reportSetup.getReportSetupId())){
            if(CollectionUtils.isNotEmpty(hmeReportSetupList)){
                throw new MtException("HME_REPORT_SETUPS_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_REPORT_SETUPS_005", "HME"));
            }
            reportSetup.setTenantId(tenantId);
            //新增
            self().insertSelective(reportSetup);
        }else {
            if(CollectionUtils.isNotEmpty(hmeReportSetupList) && !StringUtils.equals(hmeReportSetupList.get(0).getReportSetupId(), reportSetup.getReportSetupId())){
                throw new MtException("HME_REPORT_SETUPS_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_REPORT_SETUPS_005", "HME"));
            }
            hmeReportSetupMapper.updateByPrimaryKeySelective(reportSetup);
        }
        return setupVO2;
    }

    @Override
    public HmeReportSetupVO2 deleteReportSetups(Long tenantId, HmeReportSetupVO2 setupVO2) {
        if(StringUtils.isBlank(setupVO2.getReportSetupId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "reportSetupId"));
        }
        hmeReportSetupMapper.deleteByPrimaryKey(setupVO2.getReportSetupId());
        return setupVO2;
    }

    @Override
    public HmeReportSetupVO3 querySiteName(Long tenantId, String siteId) {
        if(StringUtils.isBlank(siteId)){
            //获取用户默认站点
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
            if(StringUtils.isBlank(siteId)){
                throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
            }
        }
        HmeReportSetupVO3 setupVO3 = new HmeReportSetupVO3();
        //获取标题名称
        List<MtExtendVO5> mtExtendVO5List = hmeReportSetupMapper.querySiteName(tenantId, siteId);
        for (MtExtendVO5 mtExtendVO5 : mtExtendVO5List) {
            if(StringUtils.equals(mtExtendVO5.getLang(), "zh_CN")){
                setupVO3.setZhSiteName(mtExtendVO5.getAttrValue());
            }

            if(StringUtils.equals(mtExtendVO5.getLang(), "en_US")){
                setupVO3.setEnSiteName(mtExtendVO5.getAttrValue());
            }
        }
        return setupVO3;
    }

    @Override
    public Page<HmeReportSetupVO4> queryProdVisionMonitorSystem(Long tenantId, String siteId, PageRequest pageRequest) {
        if(StringUtils.isBlank(siteId)){
            //获取用户默认站点
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
            if(StringUtils.isBlank(siteId)){
                throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
            }
        }
        String finalSiteId = siteId;
        //获取当前看板可展示的所有产线
        Page<HmeReportSetupVO4> pageObj = PageHelper.doPage(pageRequest, ()-> hmeReportSetupMapper.queryReportSetupsListOfSite(tenantId, finalSiteId));
        if(CollectionUtils.isEmpty(pageObj.getContent())){
            return pageObj;
        }

        List<String> prodLineIdList = pageObj.getContent().stream().map(HmeReportSetupVO2::getProdLineId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

        //本月累计达成数量
        String monthStartTime = CommonUtils.monthStartDayTime(new Date(), "yyyy-MM-dd 00:00:00", 0);
        String monthEndTime = CommonUtils.monthStartDayTime(new Date(), "yyyy-MM-dd 00:00:00", 1);
        List<HmeReportSetupVO4> monthCompletedQtyList = hmeReportSetupMapper.queryBatchCompletedQty(tenantId, siteId, prodLineIdList, monthStartTime, monthEndTime);
        Map<String, List<HmeReportSetupVO4>> monthCompletedQtyMap = monthCompletedQtyList.stream().collect(Collectors.groupingBy(dto -> dto.getProdLineId()));
        //本月累计派工数量
        List<String> workcellIdList = pageObj.getContent().stream().map(HmeReportSetupVO2::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<HmeReportSetupVO4> monthDispatchQtyList = hmeReportSetupMapper.queryBatchDispatchQty(tenantId, prodLineIdList, workcellIdList, monthStartTime, monthEndTime);
        Map<String, List<HmeReportSetupVO4>> monthDispatchQtyMap = monthDispatchQtyList.stream().collect(Collectors.groupingBy(dto -> dto.getProdLineId() + "_" + dto.getWorkcellId()));

        String startTime = DateUtils.format(new Date(), "yyyy-MM-dd 00:00:00");
        String endTime = DateUtils.format(new Date(), "yyyy-MM-dd 23:59:59");
        //今日派工
        List<HmeReportSetupVO4> dispatchQtyList = hmeReportSetupMapper.queryBatchDispatchQty(tenantId, prodLineIdList, workcellIdList, startTime, endTime);
        Map<String, List<HmeReportSetupVO4>> dispatchQtyMap = dispatchQtyList.stream().collect(Collectors.groupingBy(dto -> dto.getProdLineId() + "_" + dto.getWorkcellId()));

        //今日已完成
        List<HmeReportSetupVO4> completedQtyList = hmeReportSetupMapper.queryBatchCompletedQty(tenantId, siteId, prodLineIdList, startTime, endTime);
        Map<String, List<HmeReportSetupVO4>> completedQtyMap = completedQtyList.stream().collect(Collectors.groupingBy(dto -> dto.getProdLineId()));
        //每日产量
        List<HmeReportSetupVO4> dailyReportList = hmeReportSetupMapper.queryBatchDailyCompletedQty(tenantId, siteId, prodLineIdList, monthStartTime, monthEndTime);
        Map<String, List<HmeReportSetupVO4>> dailyReportMap = dailyReportList.stream().collect(Collectors.groupingBy(dto -> dto.getProdLineId() + "_" + dto.getDayTime()));

        List<String> dayAlis = CommonUtils.queryMonthDailyList(new Date());

        for (HmeReportSetupVO4 setupVO4 : pageObj.getContent()) {
            //查询文件地址
            if(StringUtils.isNotBlank(setupVO4.getAttachmentUuid())){
                ResponseEntity<List<HmeHzeroFileDTO>> unitsInfo = hmeHzeroFileFeignClient.getUnitsInfo(tenantId, setupVO4.getAttachmentUuid());
                setupVO4.setAttachmentUrlList(Collections.EMPTY_LIST);
                if (CollectionUtils.isNotEmpty(unitsInfo.getBody())) {
                    setupVO4.setAttachmentUrlList(unitsInfo.getBody());
                }
            }

            String dispatchKey = setupVO4.getProdLineId() + "_" + setupVO4.getWorkcellId();
            //本月累计达成数量
            List<HmeReportSetupVO4> monthCompletedList = monthCompletedQtyMap.get(setupVO4.getProdLineId());
            setupVO4.setMonthCompletedQty(BigDecimal.ZERO);
            if(CollectionUtils.isNotEmpty(monthCompletedList)){
                setupVO4.setMonthCompletedQty(monthCompletedList.get(0).getMonthCompletedQty());
            }
            //本月累计派工数量
            List<HmeReportSetupVO4> monthDispatchList = monthDispatchQtyMap.get(dispatchKey);
            setupVO4.setMonthDispatchQty(BigDecimal.ZERO);
            if(CollectionUtils.isNotEmpty(monthDispatchList)){
                setupVO4.setMonthDispatchQty(monthDispatchList.get(0).getMonthDispatchQty());
            }
            //今日派工
            List<HmeReportSetupVO4> dispatchList = dispatchQtyMap.get(dispatchKey);
            setupVO4.setDayDispatchQty(BigDecimal.ZERO);
            if(CollectionUtils.isNotEmpty(dispatchList)){
                setupVO4.setDayDispatchQty(dispatchList.get(0).getMonthDispatchQty());
            }
            //今日已完成
            List<HmeReportSetupVO4> dayCompletedList = completedQtyMap.get(setupVO4.getProdLineId());
            setupVO4.setDayCompletedQty(BigDecimal.ZERO);
            if(CollectionUtils.isNotEmpty(dayCompletedList)){
                setupVO4.setDayCompletedQty(dayCompletedList.get(0).getMonthCompletedQty());
            }
            //每日产量
            List<BigDecimal> dailyCompletedQtyList = new ArrayList<>();
            dayAlis.forEach(days -> {
                String dailyKey = setupVO4.getProdLineId() + "_" + days;
                List<HmeReportSetupVO4> setupVO4s = dailyReportMap.get(dailyKey);
                BigDecimal totalQty = BigDecimal.ZERO;
                if(CollectionUtils.isNotEmpty(setupVO4s)){
                    totalQty = setupVO4s.get(0).getDailyCompletedQty();
                }
                dailyCompletedQtyList.add(totalQty);
            });
            setupVO4.setDayAlis(dayAlis);
            setupVO4.setDailyCompletedQtyList(dailyCompletedQtyList);
        }
        return pageObj;
    }
}
