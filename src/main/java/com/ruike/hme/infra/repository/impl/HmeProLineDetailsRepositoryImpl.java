package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.domain.repository.HmeProLineDetailsRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeOpenEndShiftMapper;
import com.ruike.hme.infra.mapper.HmeProLineDetailsMapper;
import com.ruike.hme.infra.mapper.HmeWorkCellDetailsReportMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.ExcellUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.hzero.boot.message.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.entity.MtModWorkcellSchedule;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellScheduleRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * HmeProLineDetailsRepositoryImpl
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:40
 */

@Component
@Slf4j
public class HmeProLineDetailsRepositoryImpl implements HmeProLineDetailsRepository {

    @Autowired
    private HmeProLineDetailsMapper hmeProLineDetailsMapper;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private HmeWorkCellDetailsReportMapper hmeWorkCellDetailsReportMapper;

    @Autowired
    private MtModWorkcellScheduleRepository mtModWorkcellScheduleRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private HmeOpenEndShiftMapper hmeOpenEndShiftMapper;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public List<MtModArea> queryModArea() {
        return hmeProLineDetailsMapper.queryModAreaList();
    }

    @Override
    public List<HmeProductionLineDetailsDTO> selectDetails(Long tenantId, HmeProductionLineDetailsVO params) {
        return hmeProLineDetailsMapper.queryDetails(tenantId, params);
    }

    @Override
    public List<MtModWorkcell> selectWorkcells(Long tenantId, List<String> workcellIds) {
        return hmeProLineDetailsMapper.selectWorkcells(tenantId, workcellIds);
    }

    @Override
    public MtModWorkcell queryWorkcellsByTypeStation(Long tenantId, String workcellId) {
        return hmeProLineDetailsMapper.queryWorkcellsByTypeStation(tenantId, workcellId);
    }

    @Override
    public List<Map<String, Object>> queryWorkingQTYAndCompletedQTY(Long tenantId, List<String> workcellIds, String materialId) {
        return hmeProLineDetailsMapper.queryWorkingQTYAndCompletedQTY(tenantId, workcellIds, materialId);
    }

    @Override
    public List<HmeProductDetailsVO> batchQueryWorkingQTYAndCompletedQTY(Long tenantId, String siteId, String prodLineId, List<String> materialIdList) {
        return hmeProLineDetailsMapper.batchQueryWorkingQTYAndCompletedQTY(tenantId, siteId, prodLineId, materialIdList);
    }

    @Override
    public List<String> queryWorkcellIdList(Long tenantId, MtModOrganizationVO2 dto) {
        return hmeProLineDetailsMapper.queryWorkcellIdList(tenantId, dto);
    }

    @Override
    public List<Map<String, Object>> queryWorkingQTYAndCompletedQTYByProcess(Long tenantId, List<String> workcellIds, String materialId) {
        return hmeProLineDetailsMapper.queryWorkingQTYAndCompletedQTYByProcess(tenantId, workcellIds, materialId);
    }

    @Override
    public List<HmeProductionQueryDTO> queryProductDetails(HmeProductionQueryVO params) {
        return hmeProLineDetailsMapper.queryProductDetails(params.getTenantId(), params.getSiteId(), params.getProdLineId(),
                params.getProductType(), params.getProductClassification(), params.getProductCode(), params.getProductModel());
    }

    @Override
    public Page<HmeProductEoInfoVO> queryProductEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params) {
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        Page<HmeProductEoInfoVO> pageObj = null;

        switch (params.getFlag()) {
            // 运行
            case "Y":
                pageObj = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductEoListByRun(tenantId, params.getWorkcellId(), params.getMaterialId(), params.getEoIdentification(), defaultSiteId));
                MtModWorkcellSchedule mtModWorkcellSchedule =
                        mtModWorkcellScheduleRepository.workcellSchedulePropertyGet(tenantId, params.getWorkcellId());
                BigDecimal standardTimer = BigDecimal.ZERO;
                if (mtModWorkcellSchedule != null) {
                    if (StringUtils.isNotBlank(mtModWorkcellSchedule.getRateType())) {
                        if (StringUtils.equals("PERHOUR", mtModWorkcellSchedule.getRateType())) {
                            standardTimer = BigDecimal.valueOf(60).divide(BigDecimal.valueOf(mtModWorkcellSchedule.getRate()), 6, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(mtModWorkcellSchedule.getActivity())).divide(BigDecimal.valueOf(100), 6, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                        } else if (StringUtils.equals("SECOND", mtModWorkcellSchedule.getRateType())) {
                            standardTimer = BigDecimal.valueOf(mtModWorkcellSchedule.getRate()).divide(BigDecimal.valueOf(60), 6, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(mtModWorkcellSchedule.getActivity())).divide(BigDecimal.valueOf(100), 6, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);

                        }
                    }
                }
                for (HmeProductEoInfoVO infoVO : pageObj) {
                    infoVO.setStandardTimer(standardTimer);
                }
                break;
            // 库存
            case "N":
                pageObj = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductEoListByFinish(tenantId, params.getWorkcellId(), params.getMaterialId(), params.getEoIdentification(), defaultSiteId));
                break;
            // 待上线
            case "Q":
                pageObj = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductEoListByQueueQty(tenantId, params.getWorkcellId(), params.getMaterialId(), params.getEoIdentification(), defaultSiteId));
                break;
            // 未入库
            case "M":
                pageObj = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductEoListByNoCount(tenantId, params.getWorkcellId(), params.getMaterialId()));
                break;
            default:
                pageObj = new Page<HmeProductEoInfoVO>();
        }
        return pageObj;
    }

    @Override
    public List<MtModWorkcell> queryProcessInfoListByIds(Long tenantId, String processIds) {
        return hmeProLineDetailsMapper.queryProcessInfoListByIds(tenantId, processIds);
    }

    @Override
    public Page<HmeProductionLineDetailsDTO> queryProductionLineDetails(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) {
        //获取默认组织ID
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        if (StringUtils.isNotBlank(params.getStartTime())) {
            params.setStartTime(CommonUtils.dateStrFormat(params.getStartTime(), "yyyy-MM-dd 00:00:00"));
        }
        if (StringUtils.isNotBlank(params.getEndTime())) {
            params.setEndTime(CommonUtils.dateStrFormat(params.getEndTime(), "yyyy-MM-dd 23:59:59"));
        }

        Page<HmeProductionLineDetailsDTO> resultList = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryDetails(tenantId, params));
        resultList.forEach(e -> {
            //车间
            if (StringUtils.isNotBlank(e.getWorkshopId())) {
                e.setWorkshopName(hmeWorkCellDetailsReportMapper.queryAreaNameByWorkCellId(tenantId, e.getWorkshopId(), "CJ"));
            }
            //产线
            if (StringUtils.isNotBlank(e.getProductionLineId())) {
                e.setProduction(hmeWorkCellDetailsReportMapper.queryProductionLineName(tenantId, e.getProductionLineId()));
            }
            String shiftDate = DateUtils.format(e.getShiftDate(), "yyyy-MM-dd");
            e.setShiftDateStr(shiftDate);

            //工段
            if (StringUtils.isNotBlank(e.getLineWorkcellId())) {
                e.setLineWorkcellName(hmeWorkCellDetailsReportMapper.queryLineWorkcellName(tenantId, e.getLineWorkcellId(), "LINE"));

                //工段下所有工序
                MtModOrganizationVO2 orgVO = new MtModOrganizationVO2();
                orgVO.setTopSiteId(defaultSiteId);
                orgVO.setParentOrganizationType("WORKCELL");
                orgVO.setParentOrganizationId(e.getLineWorkcellId());
                orgVO.setOrganizationType("WORKCELL");
                orgVO.setQueryType("ALL");
                List<MtModOrganizationItemVO> orgItemList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, orgVO);
                if (CollectionUtils.isNotEmpty(orgItemList)) {
                    //开班时间
                    e.setShiftStartTime(hmeProLineDetailsMapper.queryMinShiftStart(tenantId, shiftDate, e.getLineWorkcellId()));
                    //结班时间
                    List<Date> dateList = hmeProLineDetailsMapper.queryMaxShiftEnd(tenantId, shiftDate, e.getLineWorkcellId());
                    if (CollectionUtils.isNotEmpty(dateList)) {
                        Boolean flag = false;
                        for (Date date : dateList) {
                            if (date == null) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            e.setShiftEndTime(dateList.get(0));
                        }
                    }

                    List<String> processIdList = orgItemList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());

                    //查询工段的首道和末道工序
                    List<MtModWorkcell> mtModWorkcells = hmeProLineDetailsMapper.queryFirstAndEndProcess(tenantId, processIdList);

                    //根据工单id找所有的工序
                    List<HmeProcessInfoVO> processList = hmeProLineDetailsMapper.queryProcessByWorkOderId(tenantId, e.getWorkOrderId());

                    List<MtModWorkcell> resultProcessList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(processList)) {
                        processList.forEach(pro -> {
                            mtModWorkcells.forEach(c -> {
                                if (pro != null) {
                                    if (StringUtils.equals(pro.getWorkcellId(), c.getWorkcellId())) {
                                        MtModWorkcell mtModWorkcell = new MtModWorkcell();
                                        mtModWorkcell.setWorkcellId(c.getWorkcellId());
                                        mtModWorkcell.setWorkcellName(c.getWorkcellName());
                                        resultProcessList.add(mtModWorkcell);
                                    }
                                }
                            });
                        });
                    } else {
                        resultProcessList.addAll(mtModWorkcells);
                    }

                    if (CollectionUtils.isNotEmpty(resultProcessList)) {
                        Date endDate = new Date();
                        if (e.getShiftEndTime() != null) {
                            endDate = e.getShiftEndTime();
                        }
                        //投产(首道)
                        MtModOrganizationVO2 firstVo = new MtModOrganizationVO2();
                        firstVo.setTopSiteId(defaultSiteId);
                        firstVo.setParentOrganizationType("WORKCELL");
                        firstVo.setParentOrganizationId(resultProcessList.get(0).getWorkcellId());
                        firstVo.setOrganizationType("WORKCELL");
                        firstVo.setQueryType("BOTTOM");
                        List<MtModOrganizationItemVO> processOrg = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, firstVo);
                        if (CollectionUtils.isNotEmpty(processOrg)) {
                            List<String> materialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), processOrg.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()), e.getShiftStartTime(), endDate);
                            //本班投产
                            BigDecimal shiftProduction = BigDecimal.ZERO;
                            for (String materialLotId : materialLotIdList) {
                                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                                shiftProduction = shiftProduction.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                            }
                            e.setPutData(shiftProduction.intValue());
                        }
                        e.setFirstProcessId(resultProcessList.get(0).getWorkcellId());

                        //完工(末道)
                        firstVo.setParentOrganizationId(resultProcessList.get(resultProcessList.size() - 1).getWorkcellId());
                        List<MtModOrganizationItemVO> endOrg = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, firstVo);
                        if (CollectionUtils.isNotEmpty(endOrg)) {
                            List<String> endMaterialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), endOrg.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()), e.getShiftStartTime(), endDate);
                            //本班投产
                            BigDecimal shiftProduction = BigDecimal.ZERO;
                            for (String materialLotId : endMaterialLotIdList) {
                                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                                shiftProduction = shiftProduction.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                            }
                            e.setFinishedData(shiftProduction.intValue());
                        }
                        e.setEndProcessId(resultProcessList.get(resultProcessList.size() - 1).getWorkcellId());
                    }

                }

                //不良数
                List<MtEo> mtEoList = mtEoRepository.select(new MtEo() {{
                    setTenantId(tenantId);
                    setWorkOrderId(e.getWorkOrderId());
                }});
                BigDecimal ncNumber = BigDecimal.ZERO;
                List<String> workcellIds = orgItemList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtEoList) && CollectionUtils.isNotEmpty(workcellIds)) {
                    List<String> eoIds = mtEoList.stream().map(MtEo::getEoId).collect(Collectors.toList());
                    List<String> materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId5(tenantId, e.getShiftStartTime(), e.getShiftEndTime(), workcellIds, eoIds);
                    materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                    for (String materialLotId : materialLotIdList) {
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                        ncNumber = ncNumber.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                    }
                }
                e.setNcNumber(ncNumber);
            }
        });
        return resultList;
    }


    @Override
    public Page<HmeProductionLineDetailsDTO> queryProductShiftList(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) {
        //获取默认组织ID
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        if (StringUtils.isNotBlank(params.getStartTime())) {
            params.setStartTime(CommonUtils.dateStrFormat(params.getStartTime(), "yyyy-MM-dd 00:00:00"));
        }
        if (StringUtils.isNotBlank(params.getEndTime())) {
            params.setEndTime(CommonUtils.dateStrFormat(params.getEndTime(), "yyyy-MM-dd 23:59:59"));
        }
        Page<HmeProductionLineDetailsDTO> resultList = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductShiftList(tenantId, params));
        resultList.forEach(e -> {
            //判断工段 为空 根据工段id 查询工段 生产线 车间id
            if (StringUtils.isBlank(e.getLineWorkcellId())) {
                HmeProductionLineDetailsDTO hmeProductionLineDetailsDTO = hmeProLineDetailsMapper.queryLineWorkcellUpIdInfo(tenantId, e.getShiftWorkcellId());
                if (hmeProductionLineDetailsDTO != null) {
                    e.setWorkshopId(hmeProductionLineDetailsDTO.getWorkshopId());
                    e.setProductionLineId(hmeProductionLineDetailsDTO.getProductionLineId());
                    e.setLineWorkcellId(hmeProductionLineDetailsDTO.getLineWorkcellId());
                }
            }

            //车间
            if (StringUtils.isNotBlank(e.getWorkshopId())) {
                e.setWorkshopName(hmeWorkCellDetailsReportMapper.queryAreaNameByWorkCellId(tenantId, e.getWorkshopId(), "CJ"));
            }
            //产线
            if (StringUtils.isNotBlank(e.getProductionLineId())) {
                e.setProduction(hmeWorkCellDetailsReportMapper.queryProductionLineName(tenantId, e.getProductionLineId()));
            }
            //工段
            if (StringUtils.isNotBlank(e.getLineWorkcellId())) {
                e.setLineWorkcellName(hmeWorkCellDetailsReportMapper.queryLineWorkcellName(tenantId, e.getLineWorkcellId(), "LINE"));

                //工段下所有工序
                MtModOrganizationVO2 orgVO = new MtModOrganizationVO2();
                orgVO.setTopSiteId(defaultSiteId);
                orgVO.setParentOrganizationType("WORKCELL");
                orgVO.setParentOrganizationId(e.getLineWorkcellId());
                orgVO.setOrganizationType("WORKCELL");
                orgVO.setQueryType("ALL");
                List<MtModOrganizationItemVO> orgItemList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, orgVO);
                if (CollectionUtils.isNotEmpty(orgItemList)) {
                    List<String> processIdList = orgItemList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());

                    //查询工段的首道和末道工序
                    List<MtModWorkcell> mtModWorkcells = hmeProLineDetailsMapper.queryFirstAndEndProcess(tenantId, processIdList);

                    //根据工单id找所有的工序
                    List<HmeProcessInfoVO> processList = hmeProLineDetailsMapper.queryProcessByWorkOderId(tenantId, e.getWorkOrderId());

                    List<MtModWorkcell> resultProcessList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(processList)) {
                        processList.forEach(pro -> {
                            if (pro != null) {
                                mtModWorkcells.forEach(c -> {
                                    if (StringUtils.equals(pro.getWorkcellId(), c.getWorkcellId())) {
                                        MtModWorkcell mtModWorkcell = new MtModWorkcell();
                                        mtModWorkcell.setWorkcellId(pro.getWorkcellId());
                                        resultProcessList.add(mtModWorkcell);
                                    }
                                });
                            }
                        });
                    } else {
                        resultProcessList.addAll(mtModWorkcells);
                    }

                    if (CollectionUtils.isNotEmpty(resultProcessList)) {
                        //投产(首道)
                        MtModOrganizationVO2 firstVo = new MtModOrganizationVO2();
                        firstVo.setTopSiteId(defaultSiteId);
                        firstVo.setParentOrganizationType("WORKCELL");
                        firstVo.setParentOrganizationId(resultProcessList.get(0).getWorkcellId());
                        firstVo.setOrganizationType("WORKCELL");
                        firstVo.setQueryType("BOTTOM");
                        List<MtModOrganizationItemVO> processOrg = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, firstVo);
                        if (CollectionUtils.isNotEmpty(processOrg)) {
                            List<String> materialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), processOrg.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()), e.getShiftStartTime(), e.getShiftEndTime());
                            //本班投产
                            BigDecimal shiftProduction = BigDecimal.ZERO;
                            for (String materialLotId : materialLotIdList) {
                                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                                shiftProduction = shiftProduction.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                            }
                            e.setPutData(shiftProduction.intValue());
                        }

                        //完工(末道)
                        firstVo.setParentOrganizationId(resultProcessList.get(resultProcessList.size() - 1).getWorkcellId());
                        List<MtModOrganizationItemVO> endOrg = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, firstVo);
                        if (CollectionUtils.isNotEmpty(endOrg)) {
                            List<String> endMaterialLotIdList = hmeProLineDetailsMapper.queryProcessQty(tenantId, e.getWorkOrderId(), e.getMaterialId(), endOrg.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()), e.getShiftStartTime(), e.getShiftEndTime());
                            //本班投产
                            BigDecimal shiftProduction = BigDecimal.ZERO;
                            for (String materialLotId : endMaterialLotIdList) {
                                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                                shiftProduction = shiftProduction.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                            }
                            e.setFinishedData(shiftProduction.intValue());
                        }
                    }
                }

                //不良数
                List<MtEo> mtEoList = mtEoRepository.select(new MtEo() {{
                    setTenantId(tenantId);
                    setWorkOrderId(e.getWorkOrderId());
                }});
                BigDecimal ncNumber = BigDecimal.ZERO;
                List<String> workcellIds = orgItemList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtEoList) && CollectionUtils.isNotEmpty(workcellIds)) {
                    List<String> eoIds = mtEoList.stream().map(MtEo::getEoId).collect(Collectors.toList());
                    List<String> materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId5(tenantId, e.getShiftStartTime(), e.getShiftEndTime(), workcellIds, eoIds);
                    materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                    for (String materialLotId : materialLotIdList) {
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                        ncNumber = ncNumber.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                    }
                }
                e.setNcNumber(ncNumber);
            }
        });
        return resultList;
    }

    @Override
    public Page<HmeProductEoInfoVO> queryProductProcessEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params) {
        //获取默认组织ID
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        //获取工序下 工位id
        MtModOrganizationVO2 firstVo = new MtModOrganizationVO2();
        firstVo.setTopSiteId(defaultSiteId);
        firstVo.setParentOrganizationType("WORKCELL");
        firstVo.setParentOrganizationId(params.getProcessId());
        firstVo.setOrganizationType("WORKCELL");
        firstVo.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> processOrg = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, firstVo);
        List<String> workcellIdList = processOrg.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(workcellIdList)) {
            return new Page<HmeProductEoInfoVO>();
        }
        if (StringUtils.isBlank(params.getShiftEndTime())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            params.setShiftEndTime(sdf.format(new Date()));
        }

        Page<HmeProductEoInfoVO> page = PageHelper.doPage(pageRequest, () -> hmeProLineDetailsMapper.queryProductProcessEoList(tenantId, params.getMaterialId(), params.getWorkOrderId(), workcellIdList, params.getShiftStartTime(), params.getShiftEndTime()));
        List<String> eoIdentificationList = page.getContent().stream().map(HmeProductEoInfoVO::getEoIdentification).filter(Objects::nonNull).collect(Collectors.toList());
        List<HmeProductEoInfoVO> eoInfoVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoIdentificationList)) {
            eoInfoVOList = hmeProLineDetailsMapper.batchReworkFlagQuery(tenantId, eoIdentificationList);
        }
        Map<String, List<HmeProductEoInfoVO>> reworkFlagMap = eoInfoVOList.stream().collect(Collectors.groupingBy(dto -> dto.getEoIdentification()));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            page.getContent().forEach(e -> {
                List<HmeProductEoInfoVO> infoVOList = reworkFlagMap.get(e.getEoIdentification());
                if (CollectionUtils.isNotEmpty(infoVOList)) {
                    //返修标识
                    e.setValidateFlag(infoVOList.get(0).getValidateFlag());
                    //操作时间
                    e.setLastUpdateDate(infoVOList.get(0).getLastUpdateDate());
                }
            });
        }
        return page;
    }

    @Override
    public List<MtModWorkcell> queryOrderProcessListByProLineId(Long tenantId, String proLineId) {
        return hmeProLineDetailsMapper.queryOrderProcessListByProLineId(tenantId, proLineId);
    }

    @Override
    public List<HmeEoVO> selectQueueNumByMaterialList(Long tenantId, String prodLineId, String siteId, List<String> materialIdList) {
        return hmeProLineDetailsMapper.selectQueueNumByMaterialList(tenantId, prodLineId, siteId, materialIdList);
    }

    @Override
    public List<HmeEoVO> selectUnCountByMaterialList(Long tenantId, String prodLineId, List<String> materialIdList) {
        return hmeProLineDetailsMapper.selectUnCountByMaterialList(tenantId, prodLineId, materialIdList);
    }

    @Override
    public void onlineReportExport(Long tenantId, HmeProductionQueryVO params, HttpServletResponse response) throws IOException {
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
        List<HmeProductionQueryDTO> queryDTOList = this.queryProductDetails(params);
        // 工序集合
        List<HmeProcessInfoVO> processInfoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(queryDTOList)) {
            // 查询产线下汇总工序的数量
            List<String> materialIdList = queryDTOList.stream().map(HmeProductionQueryDTO::getMaterialId).filter(Objects::nonNull).collect(Collectors.toList());
            List<HmeProductDetailsVO> hmeProductDetailsVOList = this.batchQueryWorkingQTYAndCompletedQTY(tenantId, params.getSiteId(), params.getProdLineId(), materialIdList);
            Map<String, String> workcellMap = hmeProductDetailsVOList.stream().collect(Collectors.toMap(HmeProductDetailsVO::getWorkcellId, HmeProductDetailsVO::getDescription, (k1, k2) -> k1, LinkedMap::new));

            // 按物料和工段汇总数量
            Map<String, List<HmeProductDetailsVO>> qtyMap = hmeProductDetailsVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId() + "_" + dto.getWorkcellId()));

            Map<String, BigDecimal> queueNumMap = this.selectQueueNumByMaterialList(tenantId, params.getProdLineId(), params.getSiteId(), materialIdList).stream().collect(Collectors.toMap(HmeEoVO::getMaterialId, HmeEoVO::getQty, (k1, k2) -> k1));
            Map<String, BigDecimal> unCountMap = this.selectUnCountByMaterialList(tenantId, params.getProdLineId(), materialIdList).stream().collect(Collectors.toMap(HmeEoVO::getMaterialId, HmeEoVO::getQty, (k1, k2) -> k1));

            for (HmeProductionQueryDTO dto : queryDTOList) {
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
                        // 统计工序不一致的数据
                        Optional<HmeProcessInfoVO> processOpt = processInfoList.stream().filter(pro -> StringUtils.equals(pro.getWorkcellId(), workcellId)).findFirst();
                        if (!processOpt.isPresent()) {
                            processInfoList.add(process);
                        }
                    });
                }
                dto.setWorkcells(resultMap);
                dto.setQueueNum(queueNumMap.containsKey(dto.getMaterialId()) ? queueNumMap.get(dto.getMaterialId()).longValue() : 0);
                dto.setUnCount(unCountMap.containsKey(dto.getMaterialId()) ? unCountMap.get(dto.getMaterialId()).longValue() : 0);
            }
        }

        log.info(">>>>>>>>>>>>>>>>>>>>>>开始导出在制报表数据");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("在制报表");
        String fileName = "在制报表" + DateUtil.format(new Date(), "yyyyMMdd") + ".xls";
        OutputStream fOut = null;
        try {
            //创建excel文件对象
            fOut = response.getOutputStream();
            //新增数据行，并且设置单元格数据
            //headers表示excel表中第一行的表头
            List<String> headerList = new ArrayList<>();
            String[] headers = {"生产线", "产品编码", "物料描述", "待上线"};
            headerList.addAll(Arrays.asList(headers));
            List<String> processNameList = processInfoList.stream().map(HmeProcessInfoVO::getDescription).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(processNameList)) {
                headerList.addAll(processNameList);
            }
            headerList.add("未入库库存");

            // 创建一个新的HSSFWorkbook对象
            HSSFRow row = sheet.createRow(0);
            //标题
            row.setHeightInPoints(30);
            HSSFCell headerCell1 = row.createCell(0);
            Map<String, CellStyle> styles = ExcellUtils.createStyles(workbook);
            headerCell1.setCellStyle(styles.get("title"));
            headerCell1.setCellValue("在 制 报 表");
            //存在合并 工序下有运行和库存 占两个单元格
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), 0, processNameList.size() * 2 + 5 - 1));
            //表头
            HSSFRow headerRow = sheet.createRow(1);
            Integer headerIndex = 0;
            for (String hd : headerList) {
                HSSFCell cell = headerRow.createCell(headerIndex);
                cell.setCellStyle(styles.get("subTitle"));
                cell.setCellValue(hd);
                if (CollectionUtils.isNotEmpty(processNameList)) {
                    if (headerIndex.compareTo(3) > 0 && headerIndex.compareTo(3 + processNameList.size() * 2) <= 0) {
                        // 工序占两列
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, headerIndex, headerIndex + 1));
                        headerIndex += 2;
                    } else {
                        headerIndex++;
                    }
                } else {
                    headerIndex++;
                }
            }
            // 工序存在 需上下合并行
            if (CollectionUtils.isNotEmpty(processInfoList)) {
                HSSFRow subHeaderRow = sheet.createRow(2);
                // 初始值
                Integer subIndex = 3;
                // 工序下分运行和库存
                for (HmeProcessInfoVO infoVO : processInfoList) {
                    HSSFCell runCell = subHeaderRow.createCell(subIndex + 1);
                    runCell.setCellValue("运行");
                    runCell.setCellStyle(styles.get("subTitle"));
                    HSSFCell finCell = subHeaderRow.createCell(subIndex + 2);
                    finCell.setCellValue("库存");
                    finCell.setCellStyle(styles.get("subTitle"));
                    subIndex += 2;
                }
                // 合并除工序外其他标题 占两行
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, subIndex + 1, subIndex + 1));
            }
            //内容的起始行 工序不存在 起始行为2 否则为3
            Integer rowIndex = CollectionUtils.isNotEmpty(processInfoList) ? 3 : 2;
            for (HmeProductionQueryDTO queryDTO : queryDTOList) {
                HSSFRow hssfRow = sheet.createRow(rowIndex++);
                HSSFCell cell0 = hssfRow.createCell(0);
                cell0.setCellStyle(styles.get("center"));
                cell0.setCellValue(queryDTO.getProdLineName());
                HSSFCell cell1 = hssfRow.createCell(1);
                cell1.setCellStyle(styles.get("center"));
                cell1.setCellValue(queryDTO.getMaterialCode());
                HSSFCell cell2 = hssfRow.createCell(2);
                cell2.setCellStyle(styles.get("center"));
                cell2.setCellValue(queryDTO.getMaterialName());
                HSSFCell cell3 = hssfRow.createCell(3);
                cell3.setCellStyle(styles.get("center"));
                cell3.setCellValue(queryDTO.getQueueNum());
                Integer columnIndex = 4;
                if (CollectionUtils.isNotEmpty(queryDTO.getWorkcells())) {
                    for (HmeProcessInfoVO workcell : queryDTO.getWorkcells()) {
                        HSSFCell runCell = hssfRow.createCell(columnIndex);
                        runCell.setCellStyle(styles.get("runCell"));
                        runCell.setCellValue(workcell.getRunNum().longValue());
                        HSSFCell finishCell = hssfRow.createCell(columnIndex + 1);
                        finishCell.setCellStyle(styles.get("finishCell"));
                        finishCell.setCellValue(workcell.getFinishNum().longValue());
                        columnIndex += 2;
                    }
                }
                HSSFCell cell4 = hssfRow.createCell(columnIndex);
                cell4.setCellStyle(styles.get("center"));
                cell4.setCellValue(queryDTO.getUnCount());
            }
            ExcellUtils.setResponseHeader(response, fileName);
            workbook.write(fOut);
        } catch (IOException e) {
            throw new CommonException("在制报表导出报错");
        } finally {
            //操作结束，关闭文件
            fOut.flush();
            fOut.close();
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>在制报表导出完成");
    }
}
