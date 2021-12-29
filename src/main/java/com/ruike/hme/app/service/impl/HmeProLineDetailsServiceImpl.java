package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.app.service.HmeProLineDetailsService;
import com.ruike.hme.domain.repository.HmeProLineDetailsRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产线日明细报表业务实现
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:58
 */

@Service
public class HmeProLineDetailsServiceImpl implements HmeProLineDetailsService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeProLineDetailsRepository hmeProLineDetailsRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public Page<MtModArea> queryModArea(Long tenantId, PageRequest pageRequest) {
        Page<MtModArea> resultList = PageHelper.doPageAndSort(pageRequest, () -> hmeProLineDetailsRepository.queryModArea());
        return resultList;
    }

    @Override
    public Page<HmeProductionLineDetailsDTO> queryProductionLineDetails(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) throws Exception {
        boolean startIsNull = true;
        boolean endIsNull = true;
        if (StringUtils.isNotEmpty(params.getStartTime())) {
            startIsNull = false;
        }
        if (StringUtils.isNotEmpty(params.getEndTime())) {
            endIsNull = false;
        }
        this.checkCalendarShiftDatePeriod(tenantId, params, startIsNull, endIsNull);
        return hmeProLineDetailsRepository.queryProductionLineDetails(tenantId, pageRequest, params);
    }

    @Override
    public Page<MtModWorkcell> selectWorkcells(Long tenantId, PageRequest pageRequest, HmeProductionQueryVO params) {
        //初始化工序ID列表
        List<String> workcellIds = new ArrayList<>();
        //判断比输参数站点是否为空值
        if (!StringUtils.isNotEmpty(params.getSiteId())) {
            return PageHelper.doPageAndSort(pageRequest, () -> hmeProLineDetailsRepository.selectWorkcells(tenantId, workcellIds));
        }
        //判断比输参数产线是否为空值
        if (!StringUtils.isNotEmpty(params.getProdLineId())) {
            return PageHelper.doPageAndSort(pageRequest, () -> hmeProLineDetailsRepository.selectWorkcells(tenantId, workcellIds));
        }

        MtModOrganizationVO2 rel = new MtModOrganizationVO2();
        rel.setTopSiteId(params.getSiteId());
        rel.setParentOrganizationType("PROD_LINE");
        rel.setParentOrganizationId(params.getProdLineId());
        rel.setOrganizationType("WORKCELL");
        rel.setQueryType("ALL");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, rel);
        for (MtModOrganizationItemVO vo : mtModOrganizationItemVOS) {
            workcellIds.add(vo.getOrganizationId());
        }

        return PageHelper.doPageAndSort(pageRequest, () -> hmeProLineDetailsRepository.selectWorkcells(tenantId, workcellIds));
    }

    @Override
    public Page<HmeProductionQueryDTO> queryProductDetails(Long tenantId, PageRequest pageRequest, HmeProductionQueryVO params) {
        // 判断比输参数站点是否为空值
        if (!StringUtils.isNotEmpty(params.getSiteId())) {
            throw new MtException("HME_CALENDAR_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CALENDAR_0002", "HME"));
        }
        // 判断比输参数产线是否为空值
        if (!StringUtils.isNotEmpty(params.getProdLineId())) {
            throw new MtException("HME_CALENDAR_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CALENDAR_0003", "HME"));
        }
        params.setTenantId(tenantId);
        Page<HmeProductionQueryDTO> resultList = PageHelper.doPageAndSort(pageRequest, () -> hmeProLineDetailsRepository.queryProductDetails(params));
        if (resultList.size() == 0) {
            return resultList;
        }

        // 查询产线下汇总工序的数量
        List<String> materialIdList = resultList.getContent().stream().map(HmeProductionQueryDTO::getMaterialId).filter(Objects::nonNull).collect(Collectors.toList());
        List<HmeProductDetailsVO> hmeProductDetailsVOList = hmeProLineDetailsRepository.batchQueryWorkingQTYAndCompletedQTY(tenantId, params.getSiteId(), params.getProdLineId(), materialIdList);
        Map<String, String> workcellMap = hmeProductDetailsVOList.stream().collect(Collectors.toMap(HmeProductDetailsVO::getWorkcellId, HmeProductDetailsVO::getDescription, (k1, k2) -> k1, LinkedMap::new));


        // 按物料和工段汇总数量
        Map<String, List<HmeProductDetailsVO>> qtyMap = hmeProductDetailsVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId() + "_" + dto.getWorkcellId()));

        Map<String, BigDecimal> queueNumMap = hmeProLineDetailsRepository.selectQueueNumByMaterialList(tenantId, params.getProdLineId(), params.getSiteId(), materialIdList).stream().collect(Collectors.toMap(HmeEoVO::getMaterialId, HmeEoVO::getQty, (k1, k2) -> k1));
        Map<String, BigDecimal> unCountMap = hmeProLineDetailsRepository.selectUnCountByMaterialList(tenantId, params.getProdLineId(), materialIdList).stream().collect(Collectors.toMap(HmeEoVO::getMaterialId, HmeEoVO::getQty, (k1, k2) -> k1));
        for (HmeProductionQueryDTO dto : resultList.getContent()) {
            List<HmeProcessInfoVO> resultMap = new ArrayList<>();
            if (!workcellMap.isEmpty()) {
                workcellMap.forEach((workcellId, description) -> {
                    HmeProcessInfoVO process = new HmeProcessInfoVO();
                    // 从map中取出数量，若没有则为0
                    String key = dto.getMaterialId() + "_" + workcellId;
                    BigDecimal runNum = BigDecimal.ZERO, finishNum = BigDecimal.ZERO;
                    if (qtyMap.containsKey(key)) {
                        List<HmeProductDetailsVO> qtyList = qtyMap.get(key);
                        runNum = qtyList.stream().map(HmeProductDetailsVO::getRunNum).reduce(BigDecimal.ZERO, BigDecimal::add);
                        finishNum = qtyList.stream().map(HmeProductDetailsVO::getFinishNum).reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    // 组装数据
                    process.setMaterialId(dto.getMaterialId());
                    process.setWorkcellId(workcellId);
                    process.setDescription(workcellMap.get(workcellId));
                    process.setRunNum(runNum);
                    process.setFinishNum(finishNum);
                    resultMap.add(process);
                });
            }
            dto.setWorkcells(resultMap);
            dto.setQueueNum(queueNumMap.containsKey(dto.getMaterialId()) ? queueNumMap.get(dto.getMaterialId()).longValue() : 0);
            dto.setUnCount(unCountMap.containsKey(dto.getMaterialId()) ? unCountMap.get(dto.getMaterialId()).longValue() : 0);
        }
        return resultList;
    }

    @Override
    public Page<HmeProductEoInfoVO> queryProductEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params) {
        return hmeProLineDetailsRepository.queryProductEoList(tenantId, pageRequest, params);
    }

    @Override
    public Page<HmeProductionLineDetailsDTO> queryProductShiftList(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) {
        return hmeProLineDetailsRepository.queryProductShiftList(tenantId, pageRequest, params);
    }

    @Override
    public Page<HmeProductEoInfoVO> queryProductProcessEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params) {
        return hmeProLineDetailsRepository.queryProductProcessEoList(tenantId, pageRequest, params);
    }

    @Override
    public void onlineReportExport(Long tenantId, HmeProductionQueryVO params, HttpServletResponse response) throws IOException {
        hmeProLineDetailsRepository.onlineReportExport(tenantId, params, response);
    }

    /**
     * 校验工作日历时间间隔
     * <p>
     * 时间间隔不能大于一个月
     *
     * @param tenantId    租户Id
     * @param startIsNull 开始日期是否为空
     * @param endIsNull   结束日期是否为空
     * @author benjamin
     * @date 2019/9/29 1:09 PM
     */
    private void checkCalendarShiftDatePeriod(Long tenantId, HmeProductionLineDetailsVO params, boolean startIsNull, boolean endIsNull) throws Exception {
        if (startIsNull || endIsNull) {
            return;
        }

        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        //绑定开始时间
        startTime.setTime(simpleDateFormat.parse(params.getStartTime()));
        //绑定结束时间
        endTime.setTime(simpleDateFormat.parse(params.getEndTime()));
        if (startIsNull) {
            //绑定开始时间
            startTime.setTime(simpleDateFormat.parse(params.getEndTime()));
            //开始时间向前移一个月
            startTime.add(Calendar.MONTH, -1);
            params.setStartTime(simpleDateFormat.format(startTime.getTime()));
            return;
        }
        if (endIsNull) {
            //绑定结束时间
            endTime.setTime(simpleDateFormat.parse(params.getStartTime()));
            //开始时间向后移一个月
            endTime.add(Calendar.MONTH, 1);
            params.setEndTime(simpleDateFormat.format(endTime.getTime()));
            return;
        }

        //开始时间向后移一个月
        startTime.add(Calendar.MONTH, 1);
        //比较开始时间后移一个月后是否在结束时间之前
        if (startTime.before(endTime)) {
            throw new MtException("HME_CALENDAR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CALENDAR_0001", "HME"));
        }
    }
}
