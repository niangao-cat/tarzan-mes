package com.ruike.hme.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.hme.api.dto.HmeEoWorkingDTO;
import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.domain.repository.HmeEoWorkingRepository;
import com.ruike.hme.domain.repository.HmeProLineDetailsRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeEoWorkingMapper;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HmeEoWorkingRepositoryImpl
 *
 * @author liyuan.lv@hand-china.com 2020/04/24 15:59
 */
@Component
public class HmeEoWorkingRepositoryImpl implements HmeEoWorkingRepository {

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeEoWorkingMapper hmeEoWorkingMapper;

    @Autowired
    private HmeProLineDetailsRepository hmeProLineDetailsRepository;

    @Override
    public List<HmeEoWorkingVO> queryForEoWorking(Long tenantId, HmeEoWorkingDTO dto) {
        List<HmeEoWorkingVO> resultHmeEoWorkingVOList = new ArrayList<>();


        MtModOrganizationVO2 vo2 = new MtModOrganizationVO2();
        vo2.setTopSiteId(dto.getSiteId());
        vo2.setOrganizationType("WORKCELL");
        vo2.setParentOrganizationId(dto.getProdLineId());
        vo2.setParentOrganizationType("PROD_LINE");
        vo2.setQueryType("ALL");
        List<String> workcellIdList = hmeProLineDetailsRepository.queryWorkcellIdList(tenantId,vo2);
        /*List<MtModOrganizationItemVO> organizationList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, vo2);

        if (CollectionUtils.isNotEmpty(organizationList)) {
            workcellIdList.addAll(organizationList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
        }*/

        MtModOrganizationRel workRangeCondition = new MtModOrganizationRel();
        workRangeCondition.setTenantId(tenantId);
        workRangeCondition.setTopSiteId(dto.getSiteId());
        workRangeCondition.setParentOrganizationType("PROD_LINE");
        workRangeCondition.setParentOrganizationId(dto.getProdLineId());
        workRangeCondition.setOrganizationType("WORKCELL");
        // 工段层
        List<MtModOrganizationRel> orgItemList = mtModOrganizationRelRepository.select(workRangeCondition);
        orgItemList = orgItemList.stream().sorted(Comparator.comparing(r -> r.getSequence())).collect(Collectors.toList());

        Integer index = 1;
        for (MtModOrganizationRel item : orgItemList) {
            MtModOrganizationRel workStepCondition = new MtModOrganizationRel();
            workStepCondition.setTenantId(tenantId);
            workStepCondition.setTopSiteId(dto.getSiteId());
            workStepCondition.setParentOrganizationType("WORKCELL");
            workStepCondition.setParentOrganizationId(item.getOrganizationId());
            workStepCondition.setOrganizationType("WORKCELL");
            // 工序层
            List<MtModOrganizationRel> wkcList = mtModOrganizationRelRepository.select(workStepCondition);
            wkcList = wkcList.stream().sorted(Comparator.comparing(r -> r.getSequence())).collect(Collectors.toList());

            List<String> workcellIds = wkcList.stream().map(MtModOrganizationRel::getOrganizationId).collect(Collectors.toList());

            List<MtModWorkcell> workcellList = mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);

            for (MtModOrganizationRel wkc : wkcList) {
                HmeEoWorkingVO hmeEoWorkingVO = new HmeEoWorkingVO();
                hmeEoWorkingVO.setSequenceNum(10L * index);
                Optional<MtModWorkcell> optional = workcellList.stream().filter(t -> t.getWorkcellId().equals(wkc.getOrganizationId())).findFirst();
                if (optional.isPresent()) {
                    MtModWorkcell mtModWorkcell = optional.get();
                    hmeEoWorkingVO.setWorkcellName(mtModWorkcell.getWorkcellName());
                }
                hmeEoWorkingVO.setWorkcellId(wkc.getOrganizationId());

                resultHmeEoWorkingVOList.add(hmeEoWorkingVO);
                index++;
            }
        }

        for (HmeEoWorkingVO wkc : resultHmeEoWorkingVOList) {
            List<HmeEoJobWipVO2> eoJobWipVO2List = hmeEoJobSnMapper.queryEoWorkingByWkc(tenantId, dto.getProdLineId(), wkc.getWorkcellId());

            if (CollectionUtils.isNotEmpty(eoJobWipVO2List)) {
                Map<String, List<HmeEoJobWipVO2>> groupedEoJobWipMap = eoJobWipVO2List.stream()
                        .collect(Collectors.groupingBy(HmeEoJobWipVO2::getMaterialCode));

                List<HmeEoJobWipVO3> groupedMaterialVOList = new ArrayList<>();
                for (String key : groupedEoJobWipMap.keySet()) {
                    List<HmeEoJobWipVO2> eoJobWipVO2s = groupedEoJobWipMap.get(key);
                    double workingQtySum =  0d;
                    double completedQtySum = 0d;

                    List<Map<String, Object>> qtyList = hmeProLineDetailsRepository.queryWorkingQTYAndCompletedQTYByProcess(tenantId, workcellIdList, eoJobWipVO2s.get(0).getMaterialId());
                    if (CollectionUtils.isNotEmpty(qtyList)) {
                        for (Map<String, Object> qtyMap : qtyList) {
                            String workcellIdOne = wkc.getWorkcellId();
                            String workcellIdTwo = qtyMap.get("workcellId").toString();
                            if (StringUtils.equals(workcellIdOne, workcellIdTwo)) {
                                workingQtySum = BigDecimal.valueOf(Double.valueOf(qtyMap.get("runNum").toString())).doubleValue();
                                completedQtySum = BigDecimal.valueOf(Double.valueOf(qtyMap.get("finishNum").toString())).doubleValue();
                            }
                        }
                    }
                    HmeEoJobWipVO3 groupedMaterialVO = new HmeEoJobWipVO3();
                    groupedMaterialVO.setMaterialCode(key);
                    groupedMaterialVO.setMaterialName(eoJobWipVO2s.get(0).getMaterialName());
                    groupedMaterialVO.setUomName(eoJobWipVO2s.get(0).getUomName());
                    groupedMaterialVO.setWorkingQtySum(workingQtySum);
                    groupedMaterialVO.setCompletedQtySum(completedQtySum);
                    groupedMaterialVOList.add(groupedMaterialVO);
                }
                if (StringUtils.isNotBlank(dto.getTypeName())) {
                    groupedMaterialVOList = groupedMaterialVOList.stream()
                            .filter(hmeEoJobWipVO3 -> hmeEoJobWipVO3.getMaterialName().equals(dto.getTypeName())).collect(Collectors.toList());
                }
                wkc.setHmeEoJobWipVO3List(groupedMaterialVOList);
            }

            if (wkc.getHmeEoJobWipVO3List() == null) {
                wkc.setHmeEoJobWipVO3List(new ArrayList<>());
            }
        }

        return resultHmeEoWorkingVOList;
    }

    @Override
    public List<HmeEoWorkingVO> queryForEoWorkingNew(Long tenantId, HmeEoWorkingDTO dto) {
        List<HmeEoWorkingVO2> hmeEoWorkingVO2s = hmeEoWorkingMapper.queryForEoWorkingNew(tenantId, dto);
        List<HmeEoWorkingVO> hmeEoWorkingVOS = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(hmeEoWorkingVO2s)){
            Map<String, List<HmeEoWorkingVO2>> eoWorkingMap = hmeEoWorkingVO2s.stream().collect(Collectors.groupingBy(HmeEoWorkingVO2::getWorkcellId));
            for (Map.Entry<String, List<HmeEoWorkingVO2>> entry:eoWorkingMap.entrySet()) {
                HmeEoWorkingVO hmeEoWorkingVO = new HmeEoWorkingVO();
                hmeEoWorkingVO.setWorkcellId(entry.getKey());
                hmeEoWorkingVO.setWorkcellName(entry.getValue().get(0).getWorkcellName());
                hmeEoWorkingVO.setSequence(entry.getValue().get(0).getSequence());
                hmeEoWorkingVO.setSequenceTwo(entry.getValue().get(0).getSequenceTwo());
                List<HmeEoJobWipVO3> hmeEoJobWipVO3List = new ArrayList<>();
                for (HmeEoWorkingVO2 hmeEoWorkingVO2:entry.getValue()) {
                    HmeEoJobWipVO3 hmeEoJobWipVO3 = new HmeEoJobWipVO3();
                    hmeEoJobWipVO3.setMaterialName(hmeEoWorkingVO2.getMaterialName());
                    hmeEoJobWipVO3.setMaterialCode(hmeEoWorkingVO2.getMaterialCode());
                    hmeEoJobWipVO3.setCompletedQtySum(hmeEoWorkingVO2.getCompletedQtySum());
                    hmeEoJobWipVO3.setWorkingQtySum(hmeEoWorkingVO2.getWorkingQtySum());
                    hmeEoJobWipVO3List.add(hmeEoJobWipVO3);
                }
                hmeEoWorkingVO.setHmeEoJobWipVO3List(hmeEoJobWipVO3List);
                hmeEoWorkingVOS.add(hmeEoWorkingVO);
            }
            hmeEoWorkingVOS.sort(Comparator.comparing(HmeEoWorkingVO::getSequence).thenComparing(HmeEoWorkingVO::getSequenceTwo));
            Long index = 1L;
            for (HmeEoWorkingVO hmeEoWorkingVO:hmeEoWorkingVOS) {
                hmeEoWorkingVO.setSequenceNum(10 * index);
                index++;
            }
        }
        return hmeEoWorkingVOS;
    }

    @Override
    public List<HmeModAreaVO2> queryForWorkshop(Long tenantId, HmeEoWorkingDTO dto) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        mtUserOrganization.setUserId(userId);
        mtUserOrganization.setOrganizationType("AREA");
        List<MtUserOrganization> mtUserOrganizationList =
                mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        List<String> areaIds = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
        List<MtModArea> mtModAreaList = mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);
        List<MtModArea> newMtModAreaList = mtModAreaList.stream().filter(mtModArea -> "CJ".equals(mtModArea.getAreaCategory())).collect(Collectors.toList());
        List<HmeModAreaVO2> hmeModAreaVO2List = new ArrayList<>();
        for (MtModArea mtModArea : newMtModAreaList) {
            HmeModAreaVO2 hmeModAreaVO2 = new HmeModAreaVO2();
            BeanUtils.copyProperties(mtModArea, hmeModAreaVO2);
            hmeModAreaVO2List.add(hmeModAreaVO2);
        }

        return hmeModAreaVO2List;
    }

    @Override
    public List<HmeProductionLineVO> queryForProductionLine(Long tenantId, HmeEoWorkingDTO dto) {
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        MtModOrganizationVO2 orgVO = new MtModOrganizationVO2();
        orgVO.setTopSiteId(defaultSiteId);
        orgVO.setParentOrganizationType("AREA");
        orgVO.setParentOrganizationId(dto.getAreaId());
        orgVO.setOrganizationType("PROD_LINE");
        orgVO.setQueryType("ALL");

        // 生产线
        List<MtModOrganizationItemVO> orgItemList =
                mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, orgVO);

        List<HmeProductionLineVO> hmeProductionLineVOList = new ArrayList<>();
        for (MtModOrganizationItemVO itemVO : orgItemList) {
            MtModProductionLine mtModProductionLine = mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId, itemVO.getOrganizationId());
            HmeProductionLineVO hmeProductionLineVO = new HmeProductionLineVO();
            BeanUtils.copyProperties(mtModProductionLine, hmeProductionLineVO);
            hmeProductionLineVOList.add(hmeProductionLineVO);
        }
        return hmeProductionLineVOList;
    }

}
