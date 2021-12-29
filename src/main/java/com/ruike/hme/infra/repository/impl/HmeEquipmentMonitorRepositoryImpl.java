package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import com.ruike.hme.domain.entity.HmeExcWkcRecord;
import com.ruike.hme.domain.entity.HmeExceptionGroup;
import com.ruike.hme.domain.repository.HmeEquipmentMonitorRepository;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelRepository;
import com.ruike.hme.domain.repository.HmeExcWkcRecordRepository;
import com.ruike.hme.domain.repository.HmeExceptionGroupRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import com.ruike.hme.infra.mapper.HmeEquipmentMonitorMapper;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * HmeEquipmentMonitorRepositoryImpl
 *
 * @author chaonan.hu@hand-china.com 2020-07-16 18:47:43
 **/
@Component
public class HmeEquipmentMonitorRepositoryImpl implements HmeEquipmentMonitorRepository {

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private HmeEquipmentMonitorMapper hmeEquipmentMonitorMapper;
    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;
    @Autowired
    private HmeExcWkcRecordRepository hmeExcWkcRecordRepository;
    @Autowired
    private HmeExceptionGroupRepository hmeExceptionGroupRepository;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private HmeHzeroIamFeignClient hmeHzeroIamFeignClient;
    @Override
    public List<HmeEquipmentMonitorVO> departmentDataQuery(Long tenantId, String siteId, String areaCategory) {
        return hmeEquipmentMonitorMapper.departmentDataQuery(tenantId, siteId, areaCategory);
    }

    @Override
    public List<HmeEquipmentMonitorVO2> workshopDataQuery(Long tenantId, String siteId, String departmentId) {
        List<HmeEquipmentMonitorVO2> resultList = new ArrayList<>();
        //调用API{subOrganizationRelQuery} 根据事业部查询车间
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
        mtModOrganizationVO2.setParentOrganizationId(departmentId);
        mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        HmeEquipmentMonitorVO2 hmeEquipmentMonitorVO2;
        for (MtModOrganizationItemVO vo : mtModOrganizationItemVOS) {
            hmeEquipmentMonitorVO2 = new HmeEquipmentMonitorVO2();
            hmeEquipmentMonitorVO2.setWorkshopId(vo.getOrganizationId());
            MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(vo.getOrganizationId());
            hmeEquipmentMonitorVO2.setWorkshopName(mtModArea.getAreaName());
            resultList.add(hmeEquipmentMonitorVO2);
        }
        return resultList;
    }

    @Override
    public List<HmeEquipmentMonitorVO3> prodLineDataQuery(Long tenantId, String siteId, String workshopId) {
        List<HmeEquipmentMonitorVO3> resultList = new ArrayList<>();
        //调用API{subOrganizationRelQuery} 根据车间查询产线
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
        mtModOrganizationVO2.setParentOrganizationId(workshopId);
        mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
        mtModOrganizationVO2.setQueryType("TOP");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        HmeEquipmentMonitorVO3 hmeEquipmentMonitorVO3 = null;
        for (MtModOrganizationItemVO vo : mtModOrganizationItemVOS) {
            hmeEquipmentMonitorVO3 = new HmeEquipmentMonitorVO3();
            hmeEquipmentMonitorVO3.setProdLineId(vo.getOrganizationId());
            MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(vo.getOrganizationId());
            hmeEquipmentMonitorVO3.setProdLineName(mtModProductionLine.getProdLineName());
            resultList.add(hmeEquipmentMonitorVO3);
        }
        return resultList;
    }

    @Override
    public HmeEquipmentMonitorVO6 equipmentStatusQuery(Long tenantId, String siteId, String prodLineId) {
        HmeEquipmentMonitorVO6 result = new HmeEquipmentMonitorVO6();

        //设备状态一览数据查询
        List<HmeEquipmentMonitorVO4> equipmentStatusList = new ArrayList<>();
        Date nowDate = new Date();
        //所有设备的总开机时长和故障设备数量，用于总体概况
        long totalRunTime = 0L;
        long errorEquipmentCount = 0L;
        long totalEquioment = 0L;
        //当前产线下的所有设备，用于当月异常停机Top10
        List<String> equipmentIdList = new ArrayList<>();
        //调用API{subOrganizationRelQuery} 根据产线查询所有工位
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
        mtModOrganizationVO2.setParentOrganizationId(prodLineId);
        mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        List<HmeEquipmentMonitorVO4> hmeEquipmentMonitorVO4List = new ArrayList<>();
        for (MtModOrganizationItemVO vo : mtModOrganizationItemVOS) {
            //根据工位查询设备信息
            hmeEquipmentMonitorVO4List = hmeEquipmentMonitorMapper.equipmentStatusQuery(tenantId, vo.getOrganizationId());
            totalEquioment += hmeEquipmentWkcRelRepository.selectCount(new HmeEquipmentWkcRel() {{
                setTenantId(tenantId);
                setStationId(vo.getOrganizationId());
            }});
            for (HmeEquipmentMonitorVO4 hmeEquipmentMonitorVO4 : hmeEquipmentMonitorVO4List) {
                equipmentIdList.add(hmeEquipmentMonitorVO4.getEquipmentId());
                //调用API{subOrganizationRelQuery} 根据工位查询工段
                mtModOrganizationVO2 = new MtModOrganizationVO2();
                mtModOrganizationVO2.setTopSiteId(siteId);
                mtModOrganizationVO2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                mtModOrganizationVO2.setOrganizationId(hmeEquipmentMonitorVO4.getWorkcellId());
                mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                mtModOrganizationVO2.setQueryType("TOP");
                mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
                String lineWorkcellId = mtModOrganizationItemVOS.get(0).getOrganizationId();
                //根据工段查询班次信息
                Date shiftStartTime = hmeEquipmentMonitorMapper.shiftStartTimeQuery(tenantId, lineWorkcellId);
                if (shiftStartTime == null) {
                    //找不到班次信息，则不计算开工时长百分比
                    hmeEquipmentMonitorVO4.setPercentage("--");
                } else {
                    //计算开班总时长 = 当前时间  -  SHIFT_START_TIME；
                    Long totalTime = nowDate.getTime() - shiftStartTime.getTime();
                    //根据设备Id和工位Id查询设备异常记录
                    List<HmeExcWkcRecord> hmeExcWkcRecordList = hmeEquipmentMonitorMapper.equExcRecordQuery(tenantId, shiftStartTime,
                            hmeEquipmentMonitorVO4.getEquipmentId(), hmeEquipmentMonitorVO4.getWorkcellId());
                    if (CollectionUtils.isEmpty(hmeExcWkcRecordList)) {
                        //如果查询不到异常记录，则开工时长百分比为100%
                        totalRunTime += 100;
                        hmeEquipmentMonitorVO4.setPercentage("100%");
                    } else {
                        //如果查询到异常记录，则计算总的异常时间
                        long equExcTime = 0L;
                        for (HmeExcWkcRecord hmeExcWkcRecord : hmeExcWkcRecordList) {
                            if (hmeExcWkcRecord.getCreationDate().compareTo(shiftStartTime) <= 0) {
                                //如果异常记录的creationDate 小于等于 shiftStartTime,则异常时间 = closeTime - shiftStartTime
                                equExcTime += hmeExcWkcRecord.getCloseTime().getTime() - shiftStartTime.getTime();
                            } else {
                                //否则，异常时间 = closeTime - creationDate
                                equExcTime += hmeExcWkcRecord.getCloseTime().getTime() - hmeExcWkcRecord.getCreationDate().getTime();
                            }
                        }
                        //计算开工时长百分比 = （ 1- 异常时间/班次的开班总时长） *100%
                        long percentageL = (1 - equExcTime / totalTime) * 100;
                        totalRunTime += percentageL;
                        hmeEquipmentMonitorVO4.setPercentage(Long.toString(percentageL) + "%");
                    }
                }
                equipmentStatusList.add(hmeEquipmentMonitorVO4);
                //根据设备Id、工位Id查询表hme_exc_wkc_record中异常状态 exception_status 不为CLOSE 的设备数量，用于总体概况查询
                Long recordCount = hmeEquipmentMonitorMapper.errorEquipmentCount(tenantId, hmeEquipmentMonitorVO4.getEquipmentId(), hmeEquipmentMonitorVO4.getWorkcellId());
                if(recordCount > 0){
                    errorEquipmentCount = errorEquipmentCount + 1L;
                }
            }
        }
        result.setEquipmentStatusList(equipmentStatusList);
        //总体概况数据查询
        HmeEquipmentMonitorVO5 generalOverview = new HmeEquipmentMonitorVO5();
        //运行设备数量 = 总设备数量 - 故障设备数量
        generalOverview.setRunEquipmentNumber(totalEquioment - errorEquipmentCount);
        generalOverview.setErrorEquipmentNumber(errorEquipmentCount);
        generalOverview.setAveragePercentage((totalRunTime / totalEquioment)+"%");
        result.setGeneralOverview(generalOverview);
        //当月异常停机TOP10
        List<HmeEquipmentMonitorVO8> abnormalEquipmentList = downEquipmentQuery(tenantId, equipmentIdList);
        result.setAbnormalEquipmentList(abnormalEquipmentList);
        HmeEquipmentMonitorVO9 downEquipmentDetail = new HmeEquipmentMonitorVO9();
        List<HmeEquipmentMonitorVO10> exceptionHistoryList = new ArrayList<>();
        List<HmeEquipmentMonitorVO11> sameExceptionTypeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(abnormalEquipmentList)){
            //停机设备详情
            downEquipmentDetail = downEquipmentDetailQuery(tenantId, abnormalEquipmentList.get(0));
            //30天内异常历史
            exceptionHistoryList = exceptionHistoryQuery(tenantId, abnormalEquipmentList.get(0));
            //同异常类型最近三次
            sameExceptionTypeList = sameExceptionTypeQuery(tenantId, abnormalEquipmentList.get(0));
        }
        result.setDownEquipmentDetail(downEquipmentDetail);
        result.setExceptionHistoryList(exceptionHistoryList);
        result.setSameExceptionTypeList(sameExceptionTypeList);
        return result;
    }

    @Override
    public List<HmeEquipmentMonitorVO8> downEquipmentQuery(Long tenantId, List<String> equipmentIdList) {
        List<HmeEquipmentMonitorVO8> resultList = new ArrayList<>();
        //获取当月第一天和最后一天
        Date nowDate = new Date();
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date minDate = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date maxDate = calendar.getTime();
        List<HmeEquipmentMonitorVO7> hmeEquipmentMonitorVO7s = new ArrayList<>();
        //根据设备ID查询本月异常停机记录
        for (String equipmentId : equipmentIdList) {
            hmeEquipmentMonitorVO7s.addAll(hmeEquipmentMonitorMapper.downEquipmentQuery(tenantId, equipmentId, minDate, maxDate));
        }
        //将所有设备的本月异常停机记录根据停机时长排序，找到TOP10
        if(CollectionUtils.isNotEmpty(hmeEquipmentMonitorVO7s)){
            hmeEquipmentMonitorVO7s = hmeEquipmentMonitorVO7s.stream().sorted(Comparator.comparing(HmeEquipmentMonitorVO7::getDownTime)).collect(Collectors.toList());
        }
        int total = hmeEquipmentMonitorVO7s.size() > 10 ? 10 : hmeEquipmentMonitorVO7s.size();
        HmeEquipmentMonitorVO8 hmeEquipmentMonitorVO8 = null;
        for (int i = 0; i < total; i++) {
            hmeEquipmentMonitorVO8 = new HmeEquipmentMonitorVO8();
            hmeEquipmentMonitorVO8.setNumber(i+1);
            BeanUtils.copyProperties(hmeEquipmentMonitorVO7s.get(i), hmeEquipmentMonitorVO8);
            //停机时长计算 单位小时
            Long downTime = hmeEquipmentMonitorVO7s.get(i).getDownTime();
            BigDecimal downTimeB = BigDecimal.valueOf(downTime).divide(new BigDecimal(2400 * 60 * 60), 2, BigDecimal.ROUND_UP);
            hmeEquipmentMonitorVO8.setDownTime(downTimeB.toString());
            if(hmeEquipmentMonitorVO7s.get(i).getRespondTime() == null){
                //如果当前记录的响应时间不为null，则处理时长=关闭时间-响应时间 单位小时
                long dispose = hmeEquipmentMonitorVO7s.get(i).getCloseTime().getTime() - hmeEquipmentMonitorVO7s.get(i).getRespondTime().getTime();
                BigDecimal disposeB = BigDecimal.valueOf(dispose).divide(new BigDecimal(2400 * 60 * 60), 2, BigDecimal.ROUND_UP);
                hmeEquipmentMonitorVO8.setDisposeTime(disposeB.toString());
            }else{
                //否则处理时长为--
                hmeEquipmentMonitorVO8.setDisposeTime("--");
            }
            resultList.add(hmeEquipmentMonitorVO8);
        }
        return resultList;
    }

    @Override
    public HmeEquipmentMonitorVO9 downEquipmentDetailQuery(Long tenantId, HmeEquipmentMonitorVO8 dto) {
        HmeEquipmentMonitorVO9 result = new HmeEquipmentMonitorVO9();
        BeanUtils.copyProperties(dto, result);
        //异常类型
        HmeExcWkcRecord hmeExcWkcRecord = hmeExcWkcRecordRepository.selectByPrimaryKey(dto.getRecordId());
        HmeExceptionGroup hmeExceptionGroup = hmeExceptionGroupRepository.selectByPrimaryKey(hmeExcWkcRecord.getExceptionGroupId());
        result.setExceptionGroupId(hmeExcWkcRecord.getExceptionGroupId());
        result.setExceptionGroupName(hmeExceptionGroup.getExceptionGroupName());
        //发现时间
        result.setCreationDate(hmeExcWkcRecord.getCreationDate());
        //响应时间
        if(hmeExcWkcRecord.getRespondTime() != null){
            result.setRespondTime(hmeExcWkcRecord.getRespondTime());
        }
        //响应人
        if(hmeExcWkcRecord.getRespondedBy() != null){
            ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, hmeExcWkcRecord.getRespondedBy(), "P");
            if(Strings.isNotEmpty(userInfo.getBody().getId())){
                result.setRespondBy(userInfo.getBody().getId());
                MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId,  Long.valueOf(result.getRespondBy()));
                result.setRespondByName(mtUserInfo.getRealName());
            }else{
                result.setRespondBy(hmeExcWkcRecord.getRespondedBy());
                result.setRespondByName("工号："+hmeExcWkcRecord.getRespondedBy()+"，根据工号找不到用户，请与OA系统核对");
            }

        }
        //关闭时间
        if(hmeExcWkcRecord.getCloseTime() != null){
            result.setCloseTime(hmeExcWkcRecord.getCloseTime());
        }
        //关闭人
        if(hmeExcWkcRecord.getClosedBy() != null){
            result.setClosedBy(hmeExcWkcRecord.getClosedBy().toString());
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeExcWkcRecord.getClosedBy());
            result.setRespondByName(mtUserInfo.getRealName());
        }
        return result;
    }

    @Override
    public List<HmeEquipmentMonitorVO10> exceptionHistoryQuery(Long tenantId, HmeEquipmentMonitorVO8 dto) {
        //获取当前时间及30天前时间
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date minDate = calendar.getTime();
        List<HmeEquipmentMonitorVO10> hmeEquipmentMonitorVO10s = hmeEquipmentMonitorMapper.exceptionHistoryQuery(tenantId, dto.getEquipmentId(), minDate, nowDate);
        for (HmeEquipmentMonitorVO10 hmeEquipmentMonitorVO10:hmeEquipmentMonitorVO10s) {
            //停机时长
            long disposeTimeL = hmeEquipmentMonitorVO10.getCloseTime().getTime() - hmeEquipmentMonitorVO10.getCreationDate().getTime();
            BigDecimal disposeB = BigDecimal.valueOf(disposeTimeL).divide(new BigDecimal(2400 * 60 * 60), 2, BigDecimal.ROUND_UP);
            hmeEquipmentMonitorVO10.setDownTime(disposeB.toString());
            //处理人
            if(hmeEquipmentMonitorVO10.getRespondedBy() != null){
                MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeEquipmentMonitorVO10.getRespondedBy()));
                hmeEquipmentMonitorVO10.setRespondedByName(mtUserInfo.getRealName());
            }
        }
        return hmeEquipmentMonitorVO10s;
    }

    @Override
    public List<HmeEquipmentMonitorVO11> sameExceptionTypeQuery(Long tenantId, HmeEquipmentMonitorVO8 dto) {
        HmeExcWkcRecord hmeExcWkcRecord = hmeExcWkcRecordRepository.selectByPrimaryKey(dto.getRecordId());
        List<HmeEquipmentMonitorVO11> hmeEquipmentMonitorVO11s = hmeEquipmentMonitorMapper.sameExceptionTypeQuery(tenantId, dto.getEquipmentId(), hmeExcWkcRecord.getExceptionGroupId());
        for (HmeEquipmentMonitorVO11 hmeEquipmentMonitorVO11:hmeEquipmentMonitorVO11s) {
            //停机时长
            long disposeTimeL = hmeEquipmentMonitorVO11.getCloseTime().getTime() - hmeEquipmentMonitorVO11.getCreationDate().getTime();
            BigDecimal disposeB = BigDecimal.valueOf(disposeTimeL).divide(new BigDecimal(2400 * 60 * 60), 2, BigDecimal.ROUND_UP);
            hmeEquipmentMonitorVO11.setDownTime(disposeB.toString());
            //处理人
            if(hmeEquipmentMonitorVO11.getRespondedBy() != null){
                MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeEquipmentMonitorVO11.getRespondedBy()));
                hmeEquipmentMonitorVO11.setRespondedByName(mtUserInfo.getRealName());
            }
        }
        return hmeEquipmentMonitorVO11s;
    }

    @Override
    public HmeEquipmentMonitorVO12 equipmentDetailQuery(Long tenantId, HmeEquipmentMonitorVO8 dto) {
        HmeEquipmentMonitorVO12 result = new HmeEquipmentMonitorVO12();
        HmeEquipmentMonitorVO9 hmeEquipmentMonitorVO9 = downEquipmentDetailQuery(tenantId, dto);
        List<HmeEquipmentMonitorVO10> hmeEquipmentMonitorVO10s = exceptionHistoryQuery(tenantId, dto);
        List<HmeEquipmentMonitorVO11> hmeEquipmentMonitorVO11s = sameExceptionTypeQuery(tenantId, dto);
        result.setDownEquipmentDetail(hmeEquipmentMonitorVO9);
        result.setExceptionHistoryList(hmeEquipmentMonitorVO10s);
        result.setSameExceptionTypeList(hmeEquipmentMonitorVO11s);
        return result;
    }
}
