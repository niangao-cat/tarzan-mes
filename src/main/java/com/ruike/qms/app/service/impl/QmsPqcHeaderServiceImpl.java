package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsPqcHeaderService;
import com.ruike.qms.domain.entity.QmsPqcInspectionScheme;
import com.ruike.qms.domain.entity.QmsPqcLine;
import com.ruike.qms.domain.repository.QmsPqcHeaderRepository;
import com.ruike.qms.domain.repository.QmsPqcInspectionSchemeRepository;
import com.ruike.qms.domain.repository.QmsPqcLineRepository;
import com.ruike.qms.domain.vo.*;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsPqcHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialCategoryAssignVO;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 巡检单头表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-08-17 11:49:31
 */
@Service
public class QmsPqcHeaderServiceImpl implements QmsPqcHeaderService {

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private QmsPqcInspectionSchemeRepository inspectionSchemeRepository;
    @Autowired
    private MtMaterialCategoryAssignRepository materialCategoryAssignRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private QmsPqcHeaderRepository qmsPqcHeaderRepository;
    @Autowired
    private QmsPqcHeaderMapper qmsPqcHeaderMapper;
    @Autowired
    private QmsPqcLineRepository qmsPqcLineRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Override
    public void pgcCreate(Long tenantId, QmsPqcHeaderDTO dto) {
        //校验必输性
        if(StringUtils.isEmpty(dto.getMaterialLotCode())){
            throw new MtException("HME_OP_TIME_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OP_TIME_0001", "HME", "SN"));
        }
        if(StringUtils.isEmpty(dto.getProcessId())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "工序"));
        }
        if(StringUtils.isEmpty(dto.getProdLineId())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "产线"));
        }
        //根据SN查询W0
        String workOrderId = null;
        // 判断mt_material_lot中eo_id是否为空
        if (StringUtils.isNotBlank(qmsPqcHeaderMapper.queryEoIdByMaterialLotCode(tenantId, dto.getMaterialLotCode()))) {
            workOrderId = qmsPqcHeaderMapper.getWoIdBySn(tenantId, dto.getMaterialLotCode());
        } else {
            workOrderId = qmsPqcHeaderMapper.getWoIdBySnSupplement(tenantId, dto.getMaterialLotCode());
        }
        if(StringUtils.isBlank(workOrderId)){
            throw new MtException("QMS_PGC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_PGC_0003", "QMS", dto.getMaterialLotCode()));
        }
        //校验工单状态
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
        if(!"RELEASED".equals(mtWorkOrder.getStatus()) && !"EORELEASED".equals(mtWorkOrder.getStatus())){
            throw new MtException("QMS_PGC_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_PGC_0001", "QMS", mtWorkOrder.getWorkOrderNum()));
        }
        //2020-12-07 add by chaonan.hu for jian.zhang 增加前台选择产线与工单产线是否一致的校验
        if(!dto.getProdLineId().equals(mtWorkOrder.getProductionLineId())){
            MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(dto.getProdLineId());
            throw new MtException("QMS_PQC_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_PQC_0001", "QMS", mtModProductionLine.getProdLineCode()));
        }
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtWorkOrder.getMaterialId());
        //校验工单的检验计划能否找到
        QmsPqcInspectionScheme qmsPqcInspectionScheme = null;
        //2020-11-06 14:41 edit by chaonan.hu for lu.bai 改为根据工厂+物料查找数据，若找到多条，则报错
        //按照工厂+物料+物料版本维度在表QMS_PQC_INSPECTION_SCHEME查找数据
        List<QmsPqcInspectionScheme> qmsPqcInspectionSchemeList = inspectionSchemeRepository.select(new QmsPqcInspectionScheme() {{
            setTenantId(tenantId);
            setSiteId(mtWorkOrder.getSiteId());
            setMaterialId(mtWorkOrder.getMaterialId());
            //setMaterialVersion(StringUtils.isEmpty(mtWorkOrder.getProductionVersion())?null:mtWorkOrder.getProductionVersion());
            setStatus("PUBLISHED");
        }});
        //2020-12-07 edit by chaonan.hu for jian.zhang 只有INSPECTION_TYPE为"NORMAL"或"PQC"的才是有效的巡检检验计划
        if(CollectionUtils.isNotEmpty(qmsPqcInspectionSchemeList)){
            qmsPqcInspectionSchemeList = qmsPqcInspectionSchemeList.stream().filter(item ->
                    "NORMAL".equals(item.getInspectionType()) || "PQC".equals(item.getInspectionType())).collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(qmsPqcInspectionSchemeList)){
            if(qmsPqcInspectionSchemeList.size() > 1){
                throw new MtException("QMS_PGC_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_PGC_0005", "QMS", mtMaterial.getMaterialCode()));
            }else{
                qmsPqcInspectionScheme = qmsPqcInspectionSchemeList.get(0);
            }
        }
        if(Objects.isNull(qmsPqcInspectionScheme)){
            //如果没有找到数据，则按照工厂+物料类别在表QMS_PQC_INSPECTION_SCHEME查找数据
            String materialCategoryId = materialCategoryAssignRepository.defaultSetMaterialAssignCategoryGet(tenantId, new MtMaterialCategoryAssignVO() {{
                setSiteId(mtWorkOrder.getSiteId());
                setMaterialId(mtWorkOrder.getMaterialId());
                setDefaultType("MANUFACTURING");
            }});
            if(StringUtils.isNotEmpty(materialCategoryId)){
                qmsPqcInspectionScheme = inspectionSchemeRepository.selectOne(new QmsPqcInspectionScheme() {{
                    setTenantId(tenantId);
                    setSiteId(mtWorkOrder.getSiteId());
                    setMaterialCategoryId(materialCategoryId);
                    setStatus("PUBLISHED");
                }});
            }
        }
        if(Objects.isNull(qmsPqcInspectionScheme)){
            throw new MtException("QMS_PGC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_PGC_0002", "QMS", mtWorkOrder.getWorkOrderNum(),mtMaterial.getMaterialCode()));
        }
//        if(!StringUtils.equals(qmsPqcInspectionScheme.getInspectionType(), QmsConstants.InspectionType.PQC)){
//            throw new MtException("QMS_PGC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "QMS_PGC_0006", "QMS"));
//        }
        qmsPqcHeaderRepository.pgcCreate(tenantId, dto.getProcessId(),mtWorkOrder, dto.getMaterialLotCode(),qmsPqcInspectionScheme, mtMaterial.getMaterialCode());
    }

    @Override
    public List<QmsPqcHeaderVO> prodLineQuery(Long tenantId, QmsPqcHeaderDTO2 dto) {
        return qmsPqcHeaderRepository.prodLineQuery(tenantId, dto);
    }

    @Override
    public List<QmsPqcHeaderVO2> processQuery(Long tenantId, QmsPqcHeaderDTO3 dto) {
        if(StringUtils.isEmpty(dto.getProdLineId())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "产线"));
        }
        return qmsPqcHeaderRepository.processQuery(tenantId, dto);
    }

    @Override
    public Page<QmsPqcHeaderVO3> pqcListQuery(Long tenantId, QmsPqcHeaderDTO4 dto, PageRequest pageRequest) {
        if(StringUtils.isEmpty(dto.getProdLineId())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "产线"));
        }
        return qmsPqcHeaderRepository.pqcListQuery(tenantId, dto, pageRequest);
    }

    @Override
    public QmsPqcHeaderVO6 pqcInfoQuery(Long tenantId, String pqcHeaderId, PageRequest pageRequest) {
        if(StringUtils.isEmpty(pqcHeaderId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "一条巡检单"));
        }
        return qmsPqcHeaderRepository.pqcInfoQuery(tenantId, pqcHeaderId, pageRequest);
    }

    @Override
    public Page<QmsPqcHeaderVO7> pqcResultQuery(Long tenantId, String pqcLineId, PageRequest pageRequest) {
        if(StringUtils.isEmpty(pqcLineId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "一条检验项目"));
        }
        return qmsPqcHeaderRepository.pqcResultQuery(tenantId, pqcLineId, pageRequest);
    }

    @Override
    public void attachmentUpload(Long tenantId, QmsPqcHeaderDTO5 dto) {
        if(StringUtils.isEmpty(dto.getPqcLineId())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "一条检验项目"));
        }
        qmsPqcHeaderRepository.attachmentUpload(tenantId, dto);
    }

    @Override
    public QmsPqcHeaderDTO7 pqcSave(Long tenantId, QmsPqcHeaderDTO7 dto) {
        return qmsPqcHeaderRepository.pqcSave(tenantId, dto);
    }

    @Override
    public void pqcSubmit(Long tenantId, QmsPqcHeaderDTO6 dto) {
        List<QmsPqcLine> qmsPqcLines = qmsPqcLineRepository.select(new QmsPqcLine() {{
            setTenantId(tenantId);
            setPqcHeaderId(dto.getPqcHeaderId());
        }});
        String inspectionResult = "OK";
        for (QmsPqcLine lineData:qmsPqcLines) {
            if(StringUtils.isEmpty(lineData.getInspectionResult())){
                throw new MtException("QMS_PGC_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_PGC_0004", "QMS", lineData.getInspection()));
            }
            if("NG".equals(lineData.getInspectionResult())){
                inspectionResult = "NG";
            }
        }
        dto.setInspectionResult(inspectionResult);
        qmsPqcHeaderRepository.pqcSubmit(tenantId, dto);
    }

    @Override
    public List<QmsPqcHeaderVO10> areaLovQuery(Long tenantId, QmsPqcHeaderVO10 dto) {
        return qmsPqcHeaderRepository.areaLovQuery(tenantId, dto);
    }

    @Override
    public List<QmsPqcHeaderVO11> workshopLovQuery(Long tenantId, QmsPqcHeaderVO11 dto) {
        return qmsPqcHeaderRepository.workshopLovQuery(tenantId, dto);
    }

    @Override
    public List<QmsPqcHeaderVO12> prodLineLovQuery(Long tenantId, QmsPqcHeaderVO12 dto) {
        return qmsPqcHeaderRepository.prodLineLovQuery(tenantId, dto);
    }
}
