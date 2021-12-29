package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWipStocktakeDocMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.PageUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 在制盘点单 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@Slf4j
@Component
public class HmeWipStocktakeDocRepositoryImpl extends BaseRepositoryImpl<HmeWipStocktakeDoc> implements HmeWipStocktakeDocRepository {

    @Autowired
    private HmeWipStocktakeDocMapper hmeWipStocktakeDocMapper;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private HmeWipStocktakeRangeRepository hmeWipStocktakeRangeRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeWipStocktakeDocHisRepository hmeWipStocktakeDocHisRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeWipStocktakeActualRepository hmeWipStocktakeActualRepository;
    @Autowired
    private HmeWipStocktakeActualHisRepository hmeWipStocktakeActualHisRepository;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<HmeWipStocktakeDocDTO2> departmentLovQuery(Long tenantId, HmeWipStocktakeDocDTO2 mtModArea, PageRequest pageRequest) {
        Page<HmeWipStocktakeDocDTO2> resultPage = new Page<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        List<MtUserOrganization> areaList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, new MtUserOrganization() {{
            setUserId(userId);
            setOrganizationType("AREA");
        }});
        if(CollectionUtils.isNotEmpty(areaList)){
            List<String> areaIdList = areaList.stream().map(MtUserOrganization::getOrganizationId).distinct().collect(Collectors.toList());
            resultPage = PageHelper.doPage(pageRequest, ()-> hmeWipStocktakeDocMapper.departmentListQuery(tenantId, areaIdList, mtModArea));
            return resultPage;
        }
        return resultPage;
    }

    @Override
    public Page<HmeWipStocktakeDocDTO3> prodLineLovQuery(Long tenantId, HmeWipStocktakeDocDTO3 mtModProductionLine, PageRequest pageRequest) {
        Page<HmeWipStocktakeDocDTO3> resultPage = new Page<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户默认工厂
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if(StringUtils.isEmpty(siteId)){
            return resultPage;
        }
        String departmentId = null;
        if(StringUtils.isEmpty(mtModProductionLine.getDepartmentId())){
            //获取用户默认事业部
            departmentId = hmeWipStocktakeDocMapper.getDepartmentId(tenantId, userId);
            if(StringUtils.isEmpty(departmentId)){
                return resultPage;
            }
        }else{
            departmentId = mtModProductionLine.getDepartmentId();
        }
        //调用API{subOrganizationRelQuery}查询用户有权限的产线
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setParentOrganizationType("AREA");
        mtModOrganizationVO2.setParentOrganizationId(departmentId);
        mtModOrganizationVO2.setOrganizationType("PROD_LINE");
        mtModOrganizationVO2.setQueryType("ALL");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId,mtModOrganizationVO2);
        if(CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)){
            List<String> prodLineIdList = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).distinct().collect(Collectors.toList());
            List<HmeWipStocktakeDocDTO3> mtModProductionLineList = hmeWipStocktakeDocMapper.prodLineListQuery(tenantId, prodLineIdList, mtModProductionLine);
            List<HmeWipStocktakeDocDTO3> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), mtModProductionLineList);
            resultPage = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), mtModProductionLineList.size());
        }
        return resultPage;
    }

    @Override
    public Page<HmeWipStocktakeDocDTO4> processLovQuery(Long tenantId, HmeWipStocktakeDocDTO4 mtModWorkcell, PageRequest pageRequest) {
        Page<HmeWipStocktakeDocDTO4> resultPage = new Page<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户默认工厂
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if(StringUtils.isEmpty(siteId)){
            return resultPage;
        }
        //获取用户默认事业部
        String departmentId = hmeWipStocktakeDocMapper.getDepartmentId(tenantId, userId);
        if(StringUtils.isEmpty(departmentId)){
            return resultPage;
        }
        //调用API{subOrganizationRelQuery}查询用户有权限的工序
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
            setTopSiteId(siteId);
            setParentOrganizationType("AREA");
            setParentOrganizationId(departmentId);
            setOrganizationType("WORKCELL");
            setQueryType("ALL");
        }});
        if(CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)){
            List<String> wprkcellIdList = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).distinct().collect(Collectors.toList());
            List<HmeWipStocktakeDocDTO4> mtModWorkcellList = hmeWipStocktakeDocMapper.workcellListQuery(tenantId, wprkcellIdList, mtModWorkcell);
            List<HmeWipStocktakeDocDTO4> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), mtModWorkcellList);
            resultPage = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), mtModWorkcellList.size());
        }
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWipStocktakeDocDTO6 createStocktakeDoc(Long tenantId, HmeWipStocktakeDocDTO6 dto, String cosFlag) {
        //创建盘点单
        HmeWipStocktakeDoc hmeWipStocktakeDoc = new HmeWipStocktakeDoc();
        hmeWipStocktakeDoc.setTenantId(tenantId);
        //调用API{numrangeGenerate}生成盘点单号
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, new MtNumrangeVO2() {{
            setSiteId(dto.getSiteId());
            setObjectCode("WIP_STOCKTAKE_NUM");
        }});
        hmeWipStocktakeDoc.setStocktakeNum(mtNumrangeVO5.getNumber());
        hmeWipStocktakeDoc.setStocktakeStatus(HmeConstants.StatusCode.NEW);
        hmeWipStocktakeDoc.setSiteId(dto.getSiteId());
        hmeWipStocktakeDoc.setAreaId(dto.getAreaId());
        hmeWipStocktakeDoc.setOpenFlag(dto.getOpenFlag());
        hmeWipStocktakeDoc.setMaterialRangeFlag(HmeConstants.ConstantValue.YES);
        hmeWipStocktakeDoc.setAdjustTimelyFlag(HmeConstants.ConstantValue.NO);
        hmeWipStocktakeDoc.setMaterialLotLockFlag(HmeConstants.ConstantValue.NO);
        hmeWipStocktakeDoc.setIdentification(hmeWipStocktakeDoc.getStocktakeNum());
        hmeWipStocktakeDoc.setRemark(dto.getRemark());
        hmeWipStocktakeDoc.setAttribute1(cosFlag);
        this.insertSelective(hmeWipStocktakeDoc);

        List<String> idS =
                customDbRepository.getNextKeys("hme_wip_stocktake_range_s", dto.getProdLineIdList().size()
                        + dto.getWorkcellIdList().size() + dto.getMaterialIdList().size());
        List<String> cidS = customDbRepository.getNextKeys("hme_wip_stocktake_range_cid_s",dto.getProdLineIdList().size()
                        + dto.getWorkcellIdList().size() + dto.getMaterialIdList().size());
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = CommonUtils.currentTimeGet();
        int i = 0;
        List<String> sqlList = new ArrayList<>();
        //创建产线范围
        for (String prodLineId:dto.getProdLineIdList()) {
            HmeWipStocktakeRange hmeWipStocktakeRange = new HmeWipStocktakeRange();
            hmeWipStocktakeRange.setTenantId(tenantId);
            hmeWipStocktakeRange.setStocktakeId(hmeWipStocktakeDoc.getStocktakeId());
            hmeWipStocktakeRange.setRangeObjectType("PL");
            hmeWipStocktakeRange.setRangeObjectId(prodLineId);
            hmeWipStocktakeRange.setStocktakeRangeId(idS.get(i));
            hmeWipStocktakeRange.setCid(Long.valueOf(cidS.get(i)));
            hmeWipStocktakeRange.setCreatedBy(userId);
            hmeWipStocktakeRange.setCreationDate(now);
            hmeWipStocktakeRange.setLastUpdatedBy(userId);
            hmeWipStocktakeRange.setLastUpdateDate(now);
            sqlList.addAll(customDbRepository.getInsertSql(hmeWipStocktakeRange));
            i++;
        }
        //创建工序范围
        for (String workcellId:dto.getWorkcellIdList()) {
            HmeWipStocktakeRange hmeWipStocktakeRange = new HmeWipStocktakeRange();
            hmeWipStocktakeRange.setTenantId(tenantId);
            hmeWipStocktakeRange.setStocktakeId(hmeWipStocktakeDoc.getStocktakeId());
            hmeWipStocktakeRange.setRangeObjectType("WP");
            hmeWipStocktakeRange.setRangeObjectId(workcellId);
            hmeWipStocktakeRange.setStocktakeRangeId(idS.get(i));
            hmeWipStocktakeRange.setCid(Long.valueOf(cidS.get(i)));
            hmeWipStocktakeRange.setCreatedBy(userId);
            hmeWipStocktakeRange.setCreationDate(now);
            hmeWipStocktakeRange.setLastUpdatedBy(userId);
            hmeWipStocktakeRange.setLastUpdateDate(now);
            sqlList.addAll(customDbRepository.getInsertSql(hmeWipStocktakeRange));
            i++;
        }
        //创建物料范围
        for (String materialId : dto.getMaterialIdList()) {
            HmeWipStocktakeRange hmeWipStocktakeRange = new HmeWipStocktakeRange();
            hmeWipStocktakeRange.setTenantId(tenantId);
            hmeWipStocktakeRange.setStocktakeId(hmeWipStocktakeDoc.getStocktakeId());
            hmeWipStocktakeRange.setRangeObjectType("MATERIAL");
            hmeWipStocktakeRange.setRangeObjectId(materialId);
            hmeWipStocktakeRange.setStocktakeRangeId(idS.get(i));
            hmeWipStocktakeRange.setCid(Long.valueOf(cidS.get(i)));
            hmeWipStocktakeRange.setCreatedBy(userId);
            hmeWipStocktakeRange.setCreationDate(now);
            hmeWipStocktakeRange.setLastUpdatedBy(userId);
            hmeWipStocktakeRange.setLastUpdateDate(now);
            sqlList.addAll(customDbRepository.getInsertSql(hmeWipStocktakeRange));
            i++;
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        //记录盘点单历史
        HmeWipStocktakeDocHis hmeWipStocktakeDocHis = new HmeWipStocktakeDocHis();
        BeanUtils.copyProperties(hmeWipStocktakeDoc, hmeWipStocktakeDocHis);
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_STOCKTAKE_DOC_UPDATE");
        MtEventCreateVO receiptEvent = new MtEventCreateVO();
        receiptEvent.setEventTypeCode("WIP_STOCKTAKE_DOC_UPDATE");
        receiptEvent.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, receiptEvent);
        hmeWipStocktakeDocHis.setEventId(eventId);
        hmeWipStocktakeDocHisRepository.insertSelective(hmeWipStocktakeDocHis);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWipStocktakeDoc updateStocktakeDoc(Long tenantId, HmeWipStocktakeDocDTO9 dto) {
        //更新盘点单
        HmeWipStocktakeDoc hmeWipStocktakeDoc = this.selectByPrimaryKey(dto.getStocktakeId());
        hmeWipStocktakeDoc.setRemark(dto.getRemark());
        hmeWipStocktakeDocMapper.updateByPrimaryKey(hmeWipStocktakeDoc);
        //记录历史
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_STOCKTAKE_DOC_UPDATE");
        MtEventCreateVO receiptEvent = new MtEventCreateVO();
        receiptEvent.setEventTypeCode("WIP_STOCKTAKE_DOC_UPDATE");
        receiptEvent.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, receiptEvent);
        HmeWipStocktakeDocHis hmeWipStocktakeDocHis = new HmeWipStocktakeDocHis();
        BeanUtils.copyProperties(hmeWipStocktakeDoc, hmeWipStocktakeDocHis);
        hmeWipStocktakeDocHis.setEventId(eventId);
        hmeWipStocktakeDocHisRepository.insertSelective(hmeWipStocktakeDocHis);
        return hmeWipStocktakeDoc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStocktakeRange(Long tenantId, List<String> stocktakeRangeIdList) {
        List<List<String>> list = WmsCommonUtils.splitSqlList(stocktakeRangeIdList, 500);
        list.forEach(deleteList -> hmeWipStocktakeDocMapper.deleteStocktakeRange(tenantId, deleteList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWipStocktakeDocDTO11 addStocktakeRange(Long tenantId, HmeWipStocktakeDocDTO11 dto) {
        List<String> rangeObjectIdList = dto.getAddList().stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).distinct().collect(Collectors.toList());

        List<String> idS =
                customDbRepository.getNextKeys("hme_wip_stocktake_range_s", rangeObjectIdList.size());
        List<String> cidS = customDbRepository.getNextKeys("hme_wip_stocktake_range_cid_s",rangeObjectIdList.size());
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = CommonUtils.currentTimeGet();
        int i = 0;
        List<String> sqlList = new ArrayList<>();
        for (String rangeObjectId : rangeObjectIdList) {
            HmeWipStocktakeRange hmeWipStocktakeRange = new HmeWipStocktakeRange();
            hmeWipStocktakeRange.setTenantId(tenantId);
            hmeWipStocktakeRange.setStocktakeId(dto.getStocktakeId());
            hmeWipStocktakeRange.setRangeObjectType(dto.getRangeObjectType());
            hmeWipStocktakeRange.setRangeObjectId(rangeObjectId);
            hmeWipStocktakeRange.setStocktakeRangeId(idS.get(i));
            hmeWipStocktakeRange.setCid(Long.valueOf(cidS.get(i)));
            hmeWipStocktakeRange.setCreatedBy(userId);
            hmeWipStocktakeRange.setCreationDate(now);
            hmeWipStocktakeRange.setLastUpdatedBy(userId);
            hmeWipStocktakeRange.setLastUpdateDate(now);
            sqlList.addAll(customDbRepository.getInsertSql(hmeWipStocktakeRange));
            i++;
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releasedWipStocktake(Long tenantId, HmeWipStocktakeDocVO dto) {
        long startDate5 = System.currentTimeMillis();
        //更新盘点单状态
        HmeWipStocktakeDoc hmeWipStocktakeDoc = this.selectByPrimaryKey(dto.getStocktakeId());
        hmeWipStocktakeDoc.setStocktakeStatus("RELEASED");
        hmeWipStocktakeDoc.setStocktakeLastStatus("NEW");
        hmeWipStocktakeDocMapper.updateByPrimaryKeySelective(hmeWipStocktakeDoc);
        //记录历史
        HmeWipStocktakeDocHis hmeWipStocktakeDocHis = new HmeWipStocktakeDocHis();
        BeanUtils.copyProperties(hmeWipStocktakeDoc, hmeWipStocktakeDocHis);
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_STOCKTAKE_DOC_UPDATE");
        MtEventCreateVO receiptEvent = new MtEventCreateVO();
        receiptEvent.setEventTypeCode("WIP_STOCKTAKE_RELEASE");
        receiptEvent.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, receiptEvent);
        hmeWipStocktakeDocHis.setEventId(eventId);
        hmeWipStocktakeDocHisRepository.insertSelective(hmeWipStocktakeDocHis);
        //获取盘点范围内条码
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<String> cosItemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        //查询盘点单下是否有物料范围，看物料范围是否作为限制条件
        String materialFlag = null;
        List<String> materialIdList = hmeWipStocktakeDocMapper.rangeObjectIdByStocktake(tenantId, dto.getStocktakeId(), "MATERIAL");
        if(CollectionUtils.isNotEmpty(materialIdList)){
            materialFlag = "Y";
        }else{
            materialFlag = "N";
        }
        List<HmeWipStocktakeDocVO5> materialLotList = new ArrayList<>();
        long startDate3 = System.currentTimeMillis();
        Map<String, String> eoRepairMaterialLotMap = new HashMap<>();
        if("Y".equals(hmeWipStocktakeDoc.getAttribute1())){
            //COS盘点单
            materialLotList = hmeWipStocktakeDocMapper.getStocktakeMaterialLotCos(tenantId, hmeWipStocktakeDoc, cosItemGroupList, materialFlag);
        }else if("N".equals(hmeWipStocktakeDoc.getAttribute1())){
            //非COS盘点单
            // modify by sanfeng.zhang for peng.zhao wipc存在多条时 取最新的一条
            List<HmeWipStocktakeDocVO5> stocktakeMaterialLotNoCosList = hmeWipStocktakeDocMapper.getStocktakeMaterialLotNoCos(tenantId, hmeWipStocktakeDoc, cosItemGroupList, materialFlag);
            if (CollectionUtils.isNotEmpty(stocktakeMaterialLotNoCosList)) {
                List<String> eoIdList = new ArrayList<>();
                Map<String, List<HmeWipStocktakeDocVO5>> stocktakeMaterialLotNoCosMap = stocktakeMaterialLotNoCosList.stream().collect(Collectors.groupingBy(noCos -> this.spliceStr(noCos)));
                for (Map.Entry<String, List<HmeWipStocktakeDocVO5>> cosMap : stocktakeMaterialLotNoCosMap.entrySet()) {
                    List<HmeWipStocktakeDocVO5> valueList = cosMap.getValue().stream().sorted(Comparator.comparing(HmeWipStocktakeDocVO5::getLastUpdateDate).reversed()).collect(Collectors.toList());
                    HmeWipStocktakeDocVO5 value = valueList.get(0);
                    eoIdList.add(value.getEoId());
                    materialLotList.add(value);
                }
                if(CollectionUtils.isNotEmpty(eoIdList)){
                    //2021-09-23 10:38 edit by chaonan.hu for peng.zhao 根据eoId查询对应的返修条码,生成实绩时将返修条码记录到attribute1
                    eoRepairMaterialLotMap = getRepairMaterialLotByEo(tenantId, eoIdList);
                }
            }
        }
        long endDate3 = System.currentTimeMillis();
        log.info("=======>盘点单"+ hmeWipStocktakeDoc.getStocktakeNum()+"下达时找到条码"+ materialLotList.size() +"个，共耗时"+(endDate3 - startDate3) + "毫秒");
        if(CollectionUtils.isNotEmpty(materialLotList)){
            // 获取当前时间
            final Date currentDate = CommonUtils.currentTimeGet();

            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            //批量获取wipStocktakeActual Id/Cid
            List<String> actualIdList = customSequence.getNextKeys("hme_wip_stocktake_actual_s", materialLotList.size());
            List<String> actualCidList = customSequence.getNextKeys("hme_wip_stocktake_actual_cid_s", materialLotList.size());
            //批量获取wipStocktakeActualHis Id/Cid
            List<String> actualHisIdList = customSequence.getNextKeys("hme_wip_stocktake_actual_his_s", materialLotList.size());
            List<String> actualHisCidList = customSequence.getNextKeys("hme_wip_stocktake_actual_his_cid_s", materialLotList.size());
            int index = 0;

            List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
            long startDate = System.currentTimeMillis();
            List<HmeWipStocktakeActual> hmeWipStocktakeActualList = new ArrayList<>();
            List<HmeWipStocktakeActualHis> hmeWipStocktakeActualHisList = new ArrayList<>();
            for (HmeWipStocktakeDocVO5 hmeWipStocktakeDocVO5:materialLotList) {
                //调用{materialLotUpdate}对盘点范围内的条码进行停用
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(hmeWipStocktakeDocVO5.getMaterialLotId());
                mtMaterialLotVO20.setStocktakeFlag(HmeConstants.ConstantValue.YES);
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
                //创建盘点实绩
                HmeWipStocktakeActual hmeWipStocktakeActual = new HmeWipStocktakeActual();
                hmeWipStocktakeActual.setTenantId(tenantId);
                hmeWipStocktakeActual.setStocktakeActualId(actualIdList.get(index));
                hmeWipStocktakeActual.setStocktakeId(hmeWipStocktakeDoc.getStocktakeId());
                hmeWipStocktakeActual.setSiteId(hmeWipStocktakeDoc.getSiteId());
                hmeWipStocktakeActual.setMaterialLotId(hmeWipStocktakeDocVO5.getMaterialLotId());
                hmeWipStocktakeActual.setLotCode(hmeWipStocktakeDocVO5.getLot());
                hmeWipStocktakeActual.setMaterialId(hmeWipStocktakeDocVO5.getMaterialId());
                hmeWipStocktakeActual.setWorkOrderId(hmeWipStocktakeDocVO5.getWorkOrderId());
                hmeWipStocktakeActual.setProdLineId(hmeWipStocktakeDocVO5.getProdLineId());
                hmeWipStocktakeActual.setWorkcellId(hmeWipStocktakeDocVO5.getWorkcellId());
                hmeWipStocktakeActual.setContainerId(hmeWipStocktakeDocVO5.getCurrentContainerId());
                hmeWipStocktakeActual.setUomId(hmeWipStocktakeDocVO5.getPrimaryUomId());
                hmeWipStocktakeActual.setCurrentQuantity(new BigDecimal(hmeWipStocktakeDocVO5.getPrimaryUomQty()));
                hmeWipStocktakeActual.setCid(Long.parseLong(actualCidList.get(index)));
                hmeWipStocktakeActual.setObjectVersionNumber(1L);
                hmeWipStocktakeActual.setCreatedBy(userId);
                hmeWipStocktakeActual.setCreationDate(currentDate);
                hmeWipStocktakeActual.setLastUpdatedBy(userId);
                hmeWipStocktakeActual.setLastUpdateDate(currentDate);
                if(!eoRepairMaterialLotMap.isEmpty()){
                    String repairMaterialLotId = eoRepairMaterialLotMap.get(hmeWipStocktakeDocVO5.getEoId());
                    if(StringUtils.isNotBlank(repairMaterialLotId)){
                        hmeWipStocktakeActual.setAttribute1(repairMaterialLotId);
                    }
                }
                hmeWipStocktakeActualList.add(hmeWipStocktakeActual);
                //记录实绩历史
                HmeWipStocktakeActualHis hmeWipStocktakeActualHis = new HmeWipStocktakeActualHis();
                BeanUtils.copyProperties(hmeWipStocktakeActual, hmeWipStocktakeActualHis);
                hmeWipStocktakeActualHis.setEventId(eventId);
                hmeWipStocktakeActualHis.setStocktakeActualHisId(actualHisIdList.get(index));
                hmeWipStocktakeActualHis.setCid(Long.parseLong(actualHisCidList.get(index)));
                hmeWipStocktakeActualHisList.add(hmeWipStocktakeActualHis);
                index++;
            }
            long endDate = System.currentTimeMillis();
            log.info("===========>盘点单" + hmeWipStocktakeDoc.getStocktakeNum() + "组装盘点实绩数据耗时："+(endDate - startDate) + "毫秒");
            long startDate4 = System.currentTimeMillis();
            if (CollectionUtils.isNotEmpty(hmeWipStocktakeActualList)) {
                List<List<HmeWipStocktakeActual>> splitSqlList = CommonUtils.splitSqlList(hmeWipStocktakeActualList, 500);
                for (List<HmeWipStocktakeActual> domains : splitSqlList) {
                    hmeWipStocktakeDocMapper.batchInsertActual(domains);
                }
            }
            if (CollectionUtils.isNotEmpty(hmeWipStocktakeActualHisList)) {
                List<List<HmeWipStocktakeActualHis>> splitSqlList = CommonUtils.splitSqlList(hmeWipStocktakeActualHisList, 500);
                for (List<HmeWipStocktakeActualHis> domains : splitSqlList) {
                    hmeWipStocktakeDocMapper.batchInsertActualHis(domains);
                }
            }
            long endDate4 = System.currentTimeMillis();
            log.info("===========>盘点单" + hmeWipStocktakeDoc.getStocktakeNum() + "批量插入数据耗时："+(endDate4 - startDate4) + "毫秒");
            long startDate2 = System.currentTimeMillis();
            mtMaterialLotRepository.materialLotBatchUpdatePrecompile(tenantId, mtMaterialLotVO20List, eventId);
            long endDate2 = System.currentTimeMillis();
            log.info("===========>盘点单" + hmeWipStocktakeDoc.getStocktakeNum() +  "批量冻结条码："+(endDate2 - startDate2) + "毫秒");
            long endDate5 = System.currentTimeMillis();
            log.info("===========>盘点单" + hmeWipStocktakeDoc.getStocktakeNum() + "下达总共耗时："+(endDate5 - startDate5) + "毫秒");
        }else{
            throw new MtException("WMS_WIP_STOCKTAKE_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WIP_STOCKTAKE_015", "WMS", hmeWipStocktakeDoc.getStocktakeNum()));
        }
    }

    private String spliceStr(HmeWipStocktakeDocVO5 vo5) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo5.getMaterialLotId());
        sb.append(vo5.getItemGroup());
        sb.append(vo5.getProdLineId());
        sb.append(vo5.getWorkcellId());
        sb.append(vo5.getLot());
        sb.append(vo5.getMaterialId());
        sb.append(vo5.getCurrentContainerId());
        sb.append(vo5.getPrimaryUomId());
        sb.append(vo5.getPrimaryUomQty());
        sb.append(vo5.getWorkOrderId());
        return sb.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completedWipStocktake(Long tenantId, HmeWipStocktakeDocVO dto) {
        //更新盘点单状态
        HmeWipStocktakeDoc hmeWipStocktakeDoc = this.selectByPrimaryKey(dto.getStocktakeId());
        hmeWipStocktakeDoc.setStocktakeStatus("COMPLETED");
        hmeWipStocktakeDoc.setStocktakeLastStatus("RELEASED");
        hmeWipStocktakeDocMapper.updateByPrimaryKeySelective(hmeWipStocktakeDoc);
        //记录历史
        HmeWipStocktakeDocHis hmeWipStocktakeDocHis = new HmeWipStocktakeDocHis();
        BeanUtils.copyProperties(hmeWipStocktakeDoc, hmeWipStocktakeDocHis);
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_STOCKTAKE_DOC_UPDATE");
        MtEventCreateVO receiptEvent = new MtEventCreateVO();
        receiptEvent.setEventTypeCode("WIP_STOCKTAKE_COMPLETED");
        receiptEvent.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, receiptEvent);
        hmeWipStocktakeDocHis.setEventId(eventId);
        hmeWipStocktakeDocHisRepository.insertSelective(hmeWipStocktakeDocHis);
        //查询盘点范围内的条码
        List<String> actualMaterialLot = hmeWipStocktakeDocMapper.getActualMaterialLot(tenantId, dto.getStocktakeId());
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList();
        for (String materialLotId:actualMaterialLot) {
            //调用{materialLotUpdate}对盘点范围内的条码进行解冻
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(materialLotId);
            mtMaterialLotVO20.setStocktakeFlag(HmeConstants.ConstantValue.NO);
            mtMaterialLotVO20List.add(mtMaterialLotVO20);
        }
        mtMaterialLotRepository.materialLotBatchUpdatePrecompile(tenantId, mtMaterialLotVO20List, eventId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closedWipStocktake(Long tenantId, HmeWipStocktakeDocVO dto) {
        long startDate = System.currentTimeMillis();
        //更新盘点单状态
        HmeWipStocktakeDoc hmeWipStocktakeDoc = this.selectByPrimaryKey(dto.getStocktakeId());
        hmeWipStocktakeDoc.setStocktakeLastStatus(hmeWipStocktakeDoc.getStocktakeStatus());
        hmeWipStocktakeDoc.setStocktakeStatus("CLOSED");
        hmeWipStocktakeDocMapper.updateByPrimaryKeySelective(hmeWipStocktakeDoc);
        //记录历史
        HmeWipStocktakeDocHis hmeWipStocktakeDocHis = new HmeWipStocktakeDocHis();
        BeanUtils.copyProperties(hmeWipStocktakeDoc, hmeWipStocktakeDocHis);
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_STOCKTAKE_DOC_UPDATE");
        MtEventCreateVO receiptEvent = new MtEventCreateVO();
        receiptEvent.setEventTypeCode("WIP_STOCKTAKE_CLOSED");
        receiptEvent.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, receiptEvent);
        hmeWipStocktakeDocHis.setEventId(eventId);
        hmeWipStocktakeDocHisRepository.insertSelective(hmeWipStocktakeDocHis);
        long endDate2 = System.currentTimeMillis();
        log.info("<====盘点单{}关闭 更新状态及历史耗时：{}毫秒", hmeWipStocktakeDoc.getStocktakeNum(), (endDate2 - startDate));
        if("RELEASED".equals(hmeWipStocktakeDoc.getStocktakeLastStatus())){
            //如果盘点单状态为下达，则查询盘点范围内的条码
            List<String> actualMaterialLot = hmeWipStocktakeDocMapper.getActualMaterialLot(tenantId, dto.getStocktakeId());
            long endDate3 = System.currentTimeMillis();
            log.info("<====盘点单{}关闭 查询盘点范围内的条码{}条耗时：{}毫秒", hmeWipStocktakeDoc.getStocktakeNum(), actualMaterialLot.size(), (endDate3 - endDate2));
            List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList();
            for (String materialLotId:actualMaterialLot) {
                //调用{materialLotUpdate}对盘点范围内的条码进行解冻
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(materialLotId);
                mtMaterialLotVO20.setStocktakeFlag(HmeConstants.ConstantValue.NO);
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
            }
            long endDate4 = System.currentTimeMillis();
            log.info("<====盘点单{}关闭 组装盘点范围内的条码{}条耗时：{}毫秒", hmeWipStocktakeDoc.getStocktakeNum(), actualMaterialLot.size(), (endDate4 - endDate3));
            mtMaterialLotRepository.materialLotBatchUpdatePrecompile(tenantId, mtMaterialLotVO20List, eventId);
            long endDate5 = System.currentTimeMillis();
            log.info("<====盘点单{}关闭 批量更新盘点范围内的条码{}条耗时：{}毫秒", hmeWipStocktakeDoc.getStocktakeNum(), actualMaterialLot.size(), (endDate5 - endDate4));
        }
        long endDate = System.currentTimeMillis();
        log.info("<====盘点单{}关闭耗时：{}毫秒", hmeWipStocktakeDoc.getStocktakeNum(), (endDate - startDate));
    }

    @Override
    public Map<String, String> getRepairMaterialLotByEo(Long tenantId, List<String> eoIdList) {
        Map<String, String> resultMap = new HashMap<>();
        List<HmeEoRel> hmeEoRelList = new ArrayList<>();
        //eoId去重后批量根据eoId查询topEoId，取topEoId不等于eoId且最后更新时间最近的topEoId
        eoIdList = eoIdList.stream().distinct().collect(Collectors.toList());
        List<List<String>> splitEoIdList = CommonUtils.splitSqlList(eoIdList, 1000);
        for (List<String> splitEoId:splitEoIdList) {
            hmeEoRelList.addAll(hmeWipStocktakeDocMapper.eoRelQueryByEoId(tenantId, splitEoId));
        }
        List<String> topEoIdList = new ArrayList<>();
        Map<String, String> eoTopEoMap = new HashMap<>();
        Map<String, List<HmeEoRel>> eoRelMap = hmeEoRelList.stream().collect(Collectors.groupingBy(HmeEoRel::getEoId));
        for (Map.Entry<String, List<HmeEoRel>> entry:eoRelMap.entrySet()) {
            String eoId = entry.getKey();
            List<HmeEoRel> value = entry.getValue();
            value = value.stream().filter(item -> !item.getTopEoId().equals(eoId))
                    .sorted(Comparator.comparing(HmeEoRel::getLastUpdateDate).reversed()).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(value)){
                String topEoId = value.get(0).getTopEoId();
                topEoIdList.add(topEoId);
                eoTopEoMap.put(entry.getKey(), topEoId);
            }
        }
        if(CollectionUtils.isNotEmpty(topEoIdList)){
            //topEo去重后，根据topEo查询对应的物料批ID即为返修条码ID
            topEoIdList = topEoIdList.stream().distinct().collect(Collectors.toList());
            List<HmeWipStocktakeDocVO12> resultList = hmeWipStocktakeDocMapper.getMaterialLotIdByEoId(tenantId, topEoIdList);
            Map<String, List<String>> topEoMaterialLotMap = resultList.stream()
                    .collect(Collectors.groupingBy(HmeWipStocktakeDocVO12::getEoId,
                            Collectors.mapping(HmeWipStocktakeDocVO12::getMaterialLotId, Collectors.toList())));
            for (Map.Entry<String, String> entry:eoTopEoMap.entrySet()) {
                String topEoId = entry.getValue();
                List<String> materialLotIdList = topEoMaterialLotMap.get(topEoId);
                if(CollectionUtils.isNotEmpty(materialLotIdList)){
                    resultMap.put(entry.getKey(), materialLotIdList.get(0));
                }
            }
        }
        return resultMap;
    }
}
