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
        //??????API{subOrganizationRelQuery} ???????????????????????????
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
        //??????API{subOrganizationRelQuery} ????????????????????????
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

        //??????????????????????????????
        List<HmeEquipmentMonitorVO4> equipmentStatusList = new ArrayList<>();
        Date nowDate = new Date();
        //????????????????????????????????????????????????????????????????????????
        long totalRunTime = 0L;
        long errorEquipmentCount = 0L;
        long totalEquioment = 0L;
        //?????????????????????????????????????????????????????????Top10
        List<String> equipmentIdList = new ArrayList<>();
        //??????API{subOrganizationRelQuery} ??????????????????????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
        mtModOrganizationVO2.setParentOrganizationId(prodLineId);
        mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        List<HmeEquipmentMonitorVO4> hmeEquipmentMonitorVO4List = new ArrayList<>();
        for (MtModOrganizationItemVO vo : mtModOrganizationItemVOS) {
            //??????????????????????????????
            hmeEquipmentMonitorVO4List = hmeEquipmentMonitorMapper.equipmentStatusQuery(tenantId, vo.getOrganizationId());
            totalEquioment += hmeEquipmentWkcRelRepository.selectCount(new HmeEquipmentWkcRel() {{
                setTenantId(tenantId);
                setStationId(vo.getOrganizationId());
            }});
            for (HmeEquipmentMonitorVO4 hmeEquipmentMonitorVO4 : hmeEquipmentMonitorVO4List) {
                equipmentIdList.add(hmeEquipmentMonitorVO4.getEquipmentId());
                //??????API{subOrganizationRelQuery} ????????????????????????
                mtModOrganizationVO2 = new MtModOrganizationVO2();
                mtModOrganizationVO2.setTopSiteId(siteId);
                mtModOrganizationVO2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                mtModOrganizationVO2.setOrganizationId(hmeEquipmentMonitorVO4.getWorkcellId());
                mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                mtModOrganizationVO2.setQueryType("TOP");
                mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
                String lineWorkcellId = mtModOrganizationItemVOS.get(0).getOrganizationId();
                //??????????????????????????????
                Date shiftStartTime = hmeEquipmentMonitorMapper.shiftStartTimeQuery(tenantId, lineWorkcellId);
                if (shiftStartTime == null) {
                    //?????????????????????????????????????????????????????????
                    hmeEquipmentMonitorVO4.setPercentage("--");
                } else {
                    //????????????????????? = ????????????  -  SHIFT_START_TIME???
                    Long totalTime = nowDate.getTime() - shiftStartTime.getTime();
                    //????????????Id?????????Id????????????????????????
                    List<HmeExcWkcRecord> hmeExcWkcRecordList = hmeEquipmentMonitorMapper.equExcRecordQuery(tenantId, shiftStartTime,
                            hmeEquipmentMonitorVO4.getEquipmentId(), hmeEquipmentMonitorVO4.getWorkcellId());
                    if (CollectionUtils.isEmpty(hmeExcWkcRecordList)) {
                        //????????????????????????????????????????????????????????????100%
                        totalRunTime += 100;
                        hmeEquipmentMonitorVO4.setPercentage("100%");
                    } else {
                        //?????????????????????????????????????????????????????????
                        long equExcTime = 0L;
                        for (HmeExcWkcRecord hmeExcWkcRecord : hmeExcWkcRecordList) {
                            if (hmeExcWkcRecord.getCreationDate().compareTo(shiftStartTime) <= 0) {
                                //?????????????????????creationDate ???????????? shiftStartTime,??????????????? = closeTime - shiftStartTime
                                equExcTime += hmeExcWkcRecord.getCloseTime().getTime() - shiftStartTime.getTime();
                            } else {
                                //????????????????????? = closeTime - creationDate
                                equExcTime += hmeExcWkcRecord.getCloseTime().getTime() - hmeExcWkcRecord.getCreationDate().getTime();
                            }
                        }
                        //??????????????????????????? = ??? 1- ????????????/??????????????????????????? *100%
                        long percentageL = (1 - equExcTime / totalTime) * 100;
                        totalRunTime += percentageL;
                        hmeEquipmentMonitorVO4.setPercentage(Long.toString(percentageL) + "%");
                    }
                }
                equipmentStatusList.add(hmeEquipmentMonitorVO4);
                //????????????Id?????????Id?????????hme_exc_wkc_record??????????????? exception_status ??????CLOSE ??????????????????????????????????????????
                Long recordCount = hmeEquipmentMonitorMapper.errorEquipmentCount(tenantId, hmeEquipmentMonitorVO4.getEquipmentId(), hmeEquipmentMonitorVO4.getWorkcellId());
                if(recordCount > 0){
                    errorEquipmentCount = errorEquipmentCount + 1L;
                }
            }
        }
        result.setEquipmentStatusList(equipmentStatusList);
        //????????????????????????
        HmeEquipmentMonitorVO5 generalOverview = new HmeEquipmentMonitorVO5();
        //?????????????????? = ??????????????? - ??????????????????
        generalOverview.setRunEquipmentNumber(totalEquioment - errorEquipmentCount);
        generalOverview.setErrorEquipmentNumber(errorEquipmentCount);
        generalOverview.setAveragePercentage((totalRunTime / totalEquioment)+"%");
        result.setGeneralOverview(generalOverview);
        //??????????????????TOP10
        List<HmeEquipmentMonitorVO8> abnormalEquipmentList = downEquipmentQuery(tenantId, equipmentIdList);
        result.setAbnormalEquipmentList(abnormalEquipmentList);
        HmeEquipmentMonitorVO9 downEquipmentDetail = new HmeEquipmentMonitorVO9();
        List<HmeEquipmentMonitorVO10> exceptionHistoryList = new ArrayList<>();
        List<HmeEquipmentMonitorVO11> sameExceptionTypeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(abnormalEquipmentList)){
            //??????????????????
            downEquipmentDetail = downEquipmentDetailQuery(tenantId, abnormalEquipmentList.get(0));
            //30??????????????????
            exceptionHistoryList = exceptionHistoryQuery(tenantId, abnormalEquipmentList.get(0));
            //???????????????????????????
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
        //????????????????????????????????????
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
        //????????????ID??????????????????????????????
        for (String equipmentId : equipmentIdList) {
            hmeEquipmentMonitorVO7s.addAll(hmeEquipmentMonitorMapper.downEquipmentQuery(tenantId, equipmentId, minDate, maxDate));
        }
        //???????????????????????????????????????????????????????????????????????????TOP10
        if(CollectionUtils.isNotEmpty(hmeEquipmentMonitorVO7s)){
            hmeEquipmentMonitorVO7s = hmeEquipmentMonitorVO7s.stream().sorted(Comparator.comparing(HmeEquipmentMonitorVO7::getDownTime)).collect(Collectors.toList());
        }
        int total = hmeEquipmentMonitorVO7s.size() > 10 ? 10 : hmeEquipmentMonitorVO7s.size();
        HmeEquipmentMonitorVO8 hmeEquipmentMonitorVO8 = null;
        for (int i = 0; i < total; i++) {
            hmeEquipmentMonitorVO8 = new HmeEquipmentMonitorVO8();
            hmeEquipmentMonitorVO8.setNumber(i+1);
            BeanUtils.copyProperties(hmeEquipmentMonitorVO7s.get(i), hmeEquipmentMonitorVO8);
            //?????????????????? ????????????
            Long downTime = hmeEquipmentMonitorVO7s.get(i).getDownTime();
            BigDecimal downTimeB = BigDecimal.valueOf(downTime).divide(new BigDecimal(2400 * 60 * 60), 2, BigDecimal.ROUND_UP);
            hmeEquipmentMonitorVO8.setDownTime(downTimeB.toString());
            if(hmeEquipmentMonitorVO7s.get(i).getRespondTime() == null){
                //???????????????????????????????????????null??????????????????=????????????-???????????? ????????????
                long dispose = hmeEquipmentMonitorVO7s.get(i).getCloseTime().getTime() - hmeEquipmentMonitorVO7s.get(i).getRespondTime().getTime();
                BigDecimal disposeB = BigDecimal.valueOf(dispose).divide(new BigDecimal(2400 * 60 * 60), 2, BigDecimal.ROUND_UP);
                hmeEquipmentMonitorVO8.setDisposeTime(disposeB.toString());
            }else{
                //?????????????????????--
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
        //????????????
        HmeExcWkcRecord hmeExcWkcRecord = hmeExcWkcRecordRepository.selectByPrimaryKey(dto.getRecordId());
        HmeExceptionGroup hmeExceptionGroup = hmeExceptionGroupRepository.selectByPrimaryKey(hmeExcWkcRecord.getExceptionGroupId());
        result.setExceptionGroupId(hmeExcWkcRecord.getExceptionGroupId());
        result.setExceptionGroupName(hmeExceptionGroup.getExceptionGroupName());
        //????????????
        result.setCreationDate(hmeExcWkcRecord.getCreationDate());
        //????????????
        if(hmeExcWkcRecord.getRespondTime() != null){
            result.setRespondTime(hmeExcWkcRecord.getRespondTime());
        }
        //?????????
        if(hmeExcWkcRecord.getRespondedBy() != null){
            ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, hmeExcWkcRecord.getRespondedBy(), "P");
            if(Strings.isNotEmpty(userInfo.getBody().getId())){
                result.setRespondBy(userInfo.getBody().getId());
                MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId,  Long.valueOf(result.getRespondBy()));
                result.setRespondByName(mtUserInfo.getRealName());
            }else{
                result.setRespondBy(hmeExcWkcRecord.getRespondedBy());
                result.setRespondByName("?????????"+hmeExcWkcRecord.getRespondedBy()+"???????????????????????????????????????OA????????????");
            }

        }
        //????????????
        if(hmeExcWkcRecord.getCloseTime() != null){
            result.setCloseTime(hmeExcWkcRecord.getCloseTime());
        }
        //?????????
        if(hmeExcWkcRecord.getClosedBy() != null){
            result.setClosedBy(hmeExcWkcRecord.getClosedBy().toString());
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeExcWkcRecord.getClosedBy());
            result.setRespondByName(mtUserInfo.getRealName());
        }
        return result;
    }

    @Override
    public List<HmeEquipmentMonitorVO10> exceptionHistoryQuery(Long tenantId, HmeEquipmentMonitorVO8 dto) {
        //?????????????????????30????????????
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date minDate = calendar.getTime();
        List<HmeEquipmentMonitorVO10> hmeEquipmentMonitorVO10s = hmeEquipmentMonitorMapper.exceptionHistoryQuery(tenantId, dto.getEquipmentId(), minDate, nowDate);
        for (HmeEquipmentMonitorVO10 hmeEquipmentMonitorVO10:hmeEquipmentMonitorVO10s) {
            //????????????
            long disposeTimeL = hmeEquipmentMonitorVO10.getCloseTime().getTime() - hmeEquipmentMonitorVO10.getCreationDate().getTime();
            BigDecimal disposeB = BigDecimal.valueOf(disposeTimeL).divide(new BigDecimal(2400 * 60 * 60), 2, BigDecimal.ROUND_UP);
            hmeEquipmentMonitorVO10.setDownTime(disposeB.toString());
            //?????????
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
            //????????????
            long disposeTimeL = hmeEquipmentMonitorVO11.getCloseTime().getTime() - hmeEquipmentMonitorVO11.getCreationDate().getTime();
            BigDecimal disposeB = BigDecimal.valueOf(disposeTimeL).divide(new BigDecimal(2400 * 60 * 60), 2, BigDecimal.ROUND_UP);
            hmeEquipmentMonitorVO11.setDownTime(disposeB.toString());
            //?????????
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
