package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.domain.entity.*;
import com.ruike.qms.domain.repository.*;
import com.ruike.qms.domain.vo.*;
import com.ruike.qms.infra.mapper.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.PageUtil;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtWorkOrder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡检单头表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-08-17 11:49:31
 */
@Component
public class QmsPqcHeaderRepositoryImpl extends BaseRepositoryImpl<QmsPqcHeader> implements QmsPqcHeaderRepository {

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private QmsPqcHeaderHisRepository qmsPqcHeaderHisRepository;
    @Autowired
    private QmsPqcInspectionContentRepository contentRepository;
    @Autowired
    private QmsPqcLineRepository qmsPqcLineRepository;
    @Autowired
    private QmsPqcLineHisRepository qmsPqcLineHisRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private QmsPqcHeaderMapper qmsPqcHeaderMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private QmsPqcDetailsRepository qmsPqcDetailsRepository;
    @Autowired
    private QmsPqcLineMapper qmsPqcLineMapper;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private QmsPqcDetailsHisRepository qmsPqcDetailsHisRepository;
    @Autowired
    private QmsPqcDetailsMapper qmsPqcDetailsMapper;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pgcCreate(Long tenantId, String processId, MtWorkOrder mtWorkOrder, String materialLotCode,
                          QmsPqcInspectionScheme qmsPqcInspectionScheme, String materialCode) {
        //根据schemeId查找表QMS_PQC_INSPECTION_CONTENT的数据，得到对应的检验项目
        List<QmsPqcInspectionContent> contents = contentRepository.select(new QmsPqcInspectionContent() {{
            setTenantId(tenantId);
            setSchemeId(qmsPqcInspectionScheme.getInspectionSchemeId());
            setProcessId(processId);
        }});
        //2020-12-08 add by chaonan.hu for jian.zhang 校验选择工序在检验计划下是否有检验项目
        if(CollectionUtils.isEmpty(contents)){
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(processId);
            throw new MtException("QMS_PQC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_PQC_0002", "QMS", materialCode, mtModWorkcell.getWorkcellCode()));
        }
        //巡检单头生成
        QmsPqcHeader qmsPqcHeader = new QmsPqcHeader();
        qmsPqcHeader.setTenantId(tenantId);
        qmsPqcHeader.setSiteId(mtWorkOrder.getSiteId());
        //质检单号  调用API numrangeGenerate 生成
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, new MtNumrangeVO2() {{
            setObjectCode("PQC_NUMBER");
            setSiteId(mtWorkOrder.getSiteId());
        }});
        qmsPqcHeader.setPqcNumber(mtNumrangeVO5.getNumber());
        //工单ID
        qmsPqcHeader.setWoId(mtWorkOrder.getWorkOrderId());
        //物料批ID
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(materialLotCode);
        }});
        qmsPqcHeader.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        //物料ID
        qmsPqcHeader.setMaterialId(mtWorkOrder.getMaterialId());
        //物料版本
        qmsPqcHeader.setMaterialVersion(mtWorkOrder.getProductionVersion());
        //检验状态
        qmsPqcHeader.setInspectionStatus("NEW");
        //产线ID
        qmsPqcHeader.setProdLineId(mtWorkOrder.getProductionLineId());
        //建单日期
        qmsPqcHeader.setCreatedDate(new Date());
        //2020-08-28 add by chaonan.hu fot lu.bai 增加班次取值
        //先根据工序找工段，再根据工段查询班次
        MtModOrganizationVO2 mtModOrganizationVO = new MtModOrganizationVO2();
        mtModOrganizationVO.setTopSiteId(mtWorkOrder.getSiteId());
        mtModOrganizationVO.setOrganizationType("WORKCELL");
        mtModOrganizationVO.setOrganizationId(processId);
        mtModOrganizationVO.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO.setQueryType("TOP");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS2 = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO);
        if(CollectionUtils.isNotEmpty(mtModOrganizationItemVOS2)){
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, mtModOrganizationItemVOS2.get(0).getOrganizationId());
            if(mtWkcShiftVO3 != null){
                MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(mtWkcShiftVO3.getWkcShiftId());
                qmsPqcHeader.setShiftCode(mtWkcShift.getShiftCode());
            }
        }
        this.insertSelective(qmsPqcHeader);
        //记录历史
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("PQC_DOC_CREATED");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        QmsPqcHeaderHis qmsPqcHeaderHis = new QmsPqcHeaderHis();
        qmsPqcHeaderHis.setEventId(eventId);
        BeanUtils.copyProperties(qmsPqcHeader, qmsPqcHeaderHis);
        qmsPqcHeaderHisRepository.insertSelective(qmsPqcHeaderHis);
        //巡检单行生成
        contents = contents.stream().sorted(Comparator.comparing(QmsPqcInspectionContent::getOrderKey)).collect(Collectors.toList());
        BigDecimal number = new BigDecimal(10);
        QmsPqcLine qmsPqcLine = null;
        for (QmsPqcInspectionContent content:contents) {
            qmsPqcLine = new QmsPqcLine();
            qmsPqcLine.setPqcHeaderId(qmsPqcHeader.getPqcHeaderId());
            //检验项序号
            qmsPqcLine.setNumber(number);
            number = number.add(new BigDecimal(10));
            //检验项类别
            qmsPqcLine.setInspectionType(content.getInspectionType());
            //检验项目
            qmsPqcLine.setInspection(content.getInspection());
            //检验项描述
            qmsPqcLine.setInspectionDesc(content.getInspectionDesc());
            //工序
            qmsPqcLine.setProcessId(processId);
            //文本规格值
            qmsPqcLine.setStandardText(content.getStandardText());
            //规格值从
            qmsPqcLine.setStandardFrom(content.getStandardFrom());
            //规格值至
            qmsPqcLine.setStandardTo(content.getStandardTo());
            //规格单位
            qmsPqcLine.setStandardUom(content.getStandardUom());
            //规格类型
            qmsPqcLine.setStandardType(content.getStandardType());
            //检验工具
            qmsPqcLine.setInspectionTool(content.getInspectiomTool());
            //有效性
            qmsPqcLine.setEnableFlag("Y");
            qmsPqcLineRepository.insertSelective(qmsPqcLine);
            //记录历史
            QmsPqcLineHis qmsPqcLineHis = new QmsPqcLineHis();
            qmsPqcLineHis.setEventId(eventId);
            BeanUtils.copyProperties(qmsPqcLine, qmsPqcLineHis);
            qmsPqcLineHisRepository.insertSelective(qmsPqcLineHis);
        }
    }

    @Override
    public List<QmsPqcHeaderVO> prodLineQuery(Long tenantId, QmsPqcHeaderDTO2 dto) {
        List<QmsPqcHeaderVO> resultList = new ArrayList<>();
        List<String> prodLineList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getProdLineId())){
            prodLineList.add(dto.getProdLineId());
        }
        //根据车间查询产线
        if(CollectionUtils.isEmpty(prodLineList) && StringUtils.isNotEmpty(dto.getWorkshopId())){
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
                setTopSiteId(dto.getSiteId());
                setParentOrganizationType("AREA");
                setParentOrganizationId(dto.getWorkshopId());
                setOrganizationType("PROD_LINE");
                setQueryType("BOTTOM");
            }});
            prodLineList.addAll(mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
        }
        //根据事业部查询产线
        if(CollectionUtils.isEmpty(prodLineList) && StringUtils.isNotEmpty(dto.getDepartmentId())){
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
                setTopSiteId(dto.getSiteId());
                setParentOrganizationType("AREA");
                setParentOrganizationId(dto.getDepartmentId());
                setOrganizationType("PROD_LINE");
                setQueryType("BOTTOM");
            }});
            prodLineList.addAll(mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
        }
        if(CollectionUtils.isEmpty(prodLineList) && StringUtils.isEmpty(dto.getDepartmentId())){
            //查询所有产线
            return qmsPqcHeaderMapper.prodLineQuery(tenantId, null);
        }
        for (String prodLineId:prodLineList) {
            resultList.addAll(qmsPqcHeaderMapper.prodLineQuery(tenantId, prodLineId));
        }
        return resultList;
    }

    @Override
    public List<QmsPqcHeaderVO2> processQuery(Long tenantId, QmsPqcHeaderDTO3 dto) {
        List<QmsPqcHeaderVO2> resultList = new ArrayList<>();
        //查询产线下的工序
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
            setTopSiteId(dto.getSiteId());
            setParentOrganizationType("PROD_LINE");
            setParentOrganizationId(dto.getProdLineId());
            setOrganizationType("WORKCELL");
            setQueryType("ALL");
        }});
        List<String> workcellIdList = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        for (String workcellId:workcellIdList) {
            QmsPqcHeaderVO2 qmsPqcHeaderVO2 = qmsPqcHeaderMapper.processQuery(tenantId, workcellId);
            if(qmsPqcHeaderVO2 != null){
                qmsPqcHeaderVO2.setProdLineId(dto.getProdLineId());
                resultList.add(qmsPqcHeaderVO2);
            }
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    public Page<QmsPqcHeaderVO3> pqcListQuery(Long tenantId, QmsPqcHeaderDTO4 dto, PageRequest pageRequest) {
        Page<QmsPqcHeaderVO3> resultPage = new Page<>();
        //2020-10-30 11:11 edit by chaonan.hu for lu.bai pda端发出的请求因为不做分页，所以要做时间限制
        if("1".equals(dto.getRequestFlag())){
            String day = lovAdapter.queryLovMeaning("HME.PQC_DAY", tenantId, "DAY");
            if(StringUtils.isEmpty(day)){
                day = "3";
            }
            Date dateTo = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTo);
            calendar.add(Calendar.DAY_OF_MONTH, -1*Integer.parseInt(day));
            Date dateFrom = calendar.getTime();
            resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcHeaderMapper.pqcListQueryPda(tenantId, dto, dateFrom, dateTo));
        }else{
            resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcHeaderMapper.pqcListQuery(tenantId, dto));
        }
        int i = 1;
        for (QmsPqcHeaderVO3 qmsPqcHeaderVO3:resultPage) {
            qmsPqcHeaderVO3.setNumber((pageRequest.getPage()*pageRequest.getSize()) + i);
            i++;
        }
        return resultPage;
    }

    @Override
    @ProcessLovValue
    public QmsPqcHeaderVO6 pqcInfoQuery(Long tenantId, String pqcHeaderId, PageRequest pageRequest) {
        QmsPqcHeaderVO6 result = new QmsPqcHeaderVO6();
        QmsPqcHeaderVO4 headData = qmsPqcHeaderMapper.headDataQuery(tenantId, pqcHeaderId);
        if(StringUtils.isNotEmpty(headData.getInspectionStatus())){
            String meaning = lovAdapter.queryLovMeaning("QMS.PQC_INSPECTION_STATUS", tenantId, headData.getInspectionStatus());
            headData.setInspectionStatusMeaning(meaning);
        }
        if(StringUtils.isNotEmpty(headData.getInspectionResult())){
            String inspectionResultMeaning = lovAdapter.queryLovMeaning("QMS.PQC_INSPECTION_RESULT", tenantId, headData.getInspectionResult());
            headData.setInspectionResultMeaning(inspectionResultMeaning);
        }
        if(headData != null && StringUtils.isNotEmpty(headData.getQcBy())){
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(headData.getQcBy()));
            headData.setQcByName(mtUserInfo.getRealName());
        }
        result.setHeadData(headData);
        Page<QmsPqcHeaderVO5> lineData = PageHelper.doPageAndSort(pageRequest, () -> qmsPqcHeaderMapper.lineDataQuery(tenantId, pqcHeaderId));
        for (QmsPqcHeaderVO5 qmsPqcHeaderVO5:lineData) {
            if(StringUtils.isNotEmpty(qmsPqcHeaderVO5.getInspectionTool())){
                String inspectionToolMeaning = lovAdapter.queryLovMeaning("QMS.PQC_INSPECTION_TOOL", tenantId, qmsPqcHeaderVO5.getInspectionTool());
                qmsPqcHeaderVO5.setInspectionToolMeaning(inspectionToolMeaning);
            }
            if(StringUtils.isNotEmpty(qmsPqcHeaderVO5.getInspectionResult())){
                String inspectionResultMeaning = lovAdapter.queryLovMeaning("QMS.INSPECTION_RESULT", tenantId, qmsPqcHeaderVO5.getInspectionResult());
                qmsPqcHeaderVO5.setInspectionResultMeaning(inspectionResultMeaning);
            }

            int count = qmsPqcDetailsRepository.selectCount(new QmsPqcDetails() {{
                setTenantId(tenantId);
                setPqcLineId(qmsPqcHeaderVO5.getPqcLineId());
            }});
            qmsPqcHeaderVO5.setInspectionNum(count);
        }
        result.setLineData(lineData);
        return result;
    }

    @Override
    public Page<QmsPqcHeaderVO7> pqcResultQuery(Long tenantId, String pqcLineId, PageRequest pageRequest) {
        Page<QmsPqcHeaderVO7> resultPage = PageHelper.doPageAndSort(pageRequest, () -> qmsPqcHeaderMapper.pqcResultQuery(tenantId, pqcLineId));
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void attachmentUpload(Long tenantId, QmsPqcHeaderDTO5 dto) {
        QmsPqcLine qmsPqcLine = qmsPqcLineRepository.selectByPrimaryKey(dto.getPqcLineId());
        qmsPqcLine.setAttachmentUuid(dto.getUuid());
        qmsPqcLineMapper.updateByPrimaryKeySelective(qmsPqcLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QmsPqcHeaderDTO7 pqcSave(Long tenantId, QmsPqcHeaderDTO7 dto) {
        for (QmsPqcHeaderVO9 lineAndDetail:dto.getLineAndDetailsDataList()) {
            QmsPqcHeaderVO5 lineData = lineAndDetail.getLineData();
            //巡检单行更新
            qmsPqcLineMapper.updateByPrimaryKeySelective(new QmsPqcLine() {{
                setPqcLineId(lineData.getPqcLineId());
                setInspectionResult(lineData.getInspectionResult());
            }});
            //巡检单明细更新
            int maxDetailsNum = qmsPqcHeaderMapper.maxDetailsNum(tenantId, dto.getPqcHeaderId(), lineData.getPqcLineId());
            List<QmsPqcHeaderVO8> detailsData = lineAndDetail.getDetailsData();
            if(CollectionUtils.isNotEmpty(detailsData)){
                for (QmsPqcHeaderVO8 qmsPqcHeaderVO8 : detailsData) {
                    if (StringUtils.isNotEmpty(qmsPqcHeaderVO8.getPqcDetailsId())) {
                        //更新
                        QmsPqcDetails qmsPqcDetailsDb = qmsPqcDetailsRepository.selectByPrimaryKey(qmsPqcHeaderVO8.getPqcDetailsId());
                        qmsPqcDetailsDb.setResult(qmsPqcHeaderVO8.getResult());
                        qmsPqcDetailsDb.setRemark(qmsPqcHeaderVO8.getRemark());
                        qmsPqcDetailsMapper.updateByPrimaryKeySelective(qmsPqcDetailsDb);
                    } else {
                        QmsPqcDetails qmsPqcDetails = new QmsPqcDetails();
                        qmsPqcDetails.setPqcHeaderId(dto.getPqcHeaderId());
                        qmsPqcDetails.setPqcLineId(lineData.getPqcLineId());
                        qmsPqcDetails.setResult(qmsPqcHeaderVO8.getResult());
                        qmsPqcDetails.setNumber(new BigDecimal(maxDetailsNum + 1));
                        maxDetailsNum++;
                        qmsPqcDetails.setRemark(qmsPqcHeaderVO8.getRemark());
                        qmsPqcDetailsRepository.insertSelective(qmsPqcDetails);
                    }
                }
            }
            //明细删除
            List<QmsPqcHeaderVO8> deleteDetailsData = lineAndDetail.getDeleteDetailsData();
            if(CollectionUtils.isNotEmpty(deleteDetailsData)){
                for (QmsPqcHeaderVO8 qmsPqcHeaderVO8:deleteDetailsData) {
                    if(StringUtils.isNotEmpty(qmsPqcHeaderVO8.getPqcDetailsId())){
                        qmsPqcDetailsRepository.deleteByPrimaryKey(qmsPqcHeaderVO8.getPqcDetailsId());
                    }
                }
            }
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pqcSubmit(Long tenantId, QmsPqcHeaderDTO6 dto) {
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("PQC_DOC_SUBMIT");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //头数据更新
        QmsPqcHeader qmsPqcHeader = this.selectByPrimaryKey(dto.getPqcHeaderId());
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? 1L : curUser.getUserId();
        qmsPqcHeader.setQcBy(userId);
        qmsPqcHeader.setInspectionStatus("COMPLETED");
        qmsPqcHeader.setInspectionFinishDate(new Date());
        qmsPqcHeader.setInspectionResult(dto.getInspectionResult());
        qmsPqcHeader.setRemark(dto.getRemark());
        qmsPqcHeaderMapper.updateByPrimaryKeySelective(qmsPqcHeader);
        //记录历史
        QmsPqcHeaderHis qmsPqcHeaderHis = new QmsPqcHeaderHis();
        BeanUtils.copyProperties(qmsPqcHeader, qmsPqcHeaderHis);
        qmsPqcHeaderHis.setEventId(eventId);
        qmsPqcHeaderHisRepository.insertSelective(qmsPqcHeaderHis);

        // 20210326 add by sanfeng.zhang for hui.ma 判定头为NG时 更新条码质量状态
        if (StringUtils.equals(dto.getInspectionResult(), "NG")) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(qmsPqcHeader.getMaterialLotId());
            if (mtMaterialLot != null) {
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2(){{
                    setTenantId(tenantId);
                    setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    setQualityStatus(dto.getInspectionResult());
                    setEventId(eventId);
                }}, "N");
            }
        }
    }

    @Override
    public List<QmsPqcHeaderVO10> areaLovQuery(Long tenantId,  QmsPqcHeaderVO10 dto) {
        return qmsPqcHeaderMapper.areaLovQuery(tenantId, dto.getQueryInfo());
    }

    @Override
    public List<QmsPqcHeaderVO11> workshopLovQuery(Long tenantId, QmsPqcHeaderVO11 dto) {
        return qmsPqcHeaderMapper.workshopLovQuery(tenantId, dto.getQueryInfo());
    }

    @Override
    public List<QmsPqcHeaderVO12> prodLineLovQuery(Long tenantId, QmsPqcHeaderVO12 dto) {
        return qmsPqcHeaderMapper.prodLineLovQuery(tenantId, dto.getQueryInfo());
    }
}
