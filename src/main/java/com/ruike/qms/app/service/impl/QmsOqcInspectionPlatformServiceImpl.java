package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsOqcInspectionSaveDTO;
import com.ruike.qms.api.dto.QmsOqcInspectionSaveDetailDTO;
import com.ruike.qms.api.dto.QmsOqcInspectionSaveLineDTO;
import com.ruike.qms.app.service.QmsOqcInspectionPlatformService;
import com.ruike.qms.domain.entity.*;
import com.ruike.qms.domain.repository.*;
import com.ruike.qms.domain.vo.QmsOqcHeaderVO;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsOqcDetailsMapper;
import com.ruike.qms.infra.mapper.QmsOqcHeaderMapper;
import com.ruike.qms.infra.mapper.QmsOqcLineMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialCategoryAssignVO;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderAttrVO2;

import java.math.*;
import java.util.*;
import java.util.stream.*;

/**
 * @Classname QmsOqcInspectionPlatformServiceImpl
 * @Description OQC????????????
 * @Date 2020/8/28 14:40
 * @Author yuchao.wang
 */
@Service
public class QmsOqcInspectionPlatformServiceImpl implements QmsOqcInspectionPlatformService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtMaterialCategoryAssignRepository mtMaterialCategoryAssignRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private QmsOqcHeaderRepository qmsOqcHeaderRepository;

    @Autowired
    private QmsOqcHeaderHisRepository qmsOqcHeaderHisRepository;

    @Autowired
    private QmsOqcLineRepository qmsOqcLineRepository;

    @Autowired
    private QmsOqcLineHisRepository qmsOqcLineHisRepository;

    @Autowired
    private QmsOqcDetailsRepository qmsOqcDetailsRepository;

    @Autowired
    private QmsOqcDetailsHisRepository qmsOqcDetailsHisRepository;

    @Autowired
    private QmsPqcInspectionSchemeRepository qmsPqcInspectionSchemeRepository;

    @Autowired
    private QmsPqcInspectionContentRepository qmsPqcInspectionContentRepository;

    @Autowired
    private QmsOqcHeaderMapper qmsOqcHeaderMapper;

    @Autowired
    private QmsOqcLineMapper qmsOqcLineMapper;

    @Autowired
    private QmsOqcDetailsMapper qmsOqcDetailsMapper;

    /**
     *
     * @Description ????????????
     *
     * @author yuchao.wang
     * @date 2020/8/28 15:10
     * @param tenantId ??????ID
     * @param scanBarcode ??????
     * @return java.lang.Object
     *
     */
    @Override
    @ProcessLovValue(targetField = {"", "lineList"})
    public QmsOqcHeaderVO scanBarcode(Long tenantId, String scanBarcode) {
        //????????????
        if(StringUtils.isBlank(scanBarcode)){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }

        //????????????????????????????????????
        QmsOqcHeader qmsOqcHeader = qmsOqcHeaderMapper.queryLastOqcData(tenantId, scanBarcode);
        if(Objects.isNull(qmsOqcHeader) || StringUtils.isBlank(qmsOqcHeader.getOqcHeaderId())){
            //????????????????????????????????????
            return getBaseOqcData(tenantId, scanBarcode);
        } else {
            //????????????????????????????????????
            QmsOqcHeaderVO qmsOqcHeaderVO = qmsOqcHeaderMapper.queryOqcDataByHeadId(qmsOqcHeader.getOqcHeaderId());
            if(Objects.isNull(qmsOqcHeaderVO)){
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "???????????????"));
            }

            return qmsOqcHeaderVO;
        }
    }

    /**
     *
     * @Description ??????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/8/29 14:57
     * @param tenantId ??????ID
     * @param scanBarcode ??????
     * @return com.ruike.qms.domain.vo.QmsOqcHeaderVO
     *
     */
    private QmsOqcHeaderVO getBaseOqcData(Long tenantId, String scanBarcode){
        QmsOqcHeaderVO qmsOqcHeaderVO = qmsOqcHeaderMapper.queryBaseDataForOqc(tenantId, scanBarcode);
        if(Objects.isNull(qmsOqcHeaderVO)){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }

        //????????????????????????
        if(StringUtils.isNotBlank(qmsOqcHeaderVO.getWoId())) {
            MtWorkOrderAttrVO2 mtWorkOrderAttrVO2 = new MtWorkOrderAttrVO2();
            mtWorkOrderAttrVO2.setWorkOrderId(qmsOqcHeaderVO.getWoId());
            List<MtExtendAttrVO> workOrderAttrVOList = mtWorkOrderRepository.woLimitAttrQuery(tenantId, mtWorkOrderAttrVO2);
            if (CollectionUtils.isNotEmpty(workOrderAttrVOList)) {
                Map<String, String> workOrderAttrMap = new HashMap<>();
                workOrderAttrVOList.forEach(item -> workOrderAttrMap.put(item.getAttrName().toLowerCase(), item.getAttrValue()));
                qmsOqcHeaderVO.setSoNumber(workOrderAttrMap.get("attribute1"));
                qmsOqcHeaderVO.setSoLineNumber(workOrderAttrMap.get("attribute7"));
            }
        }
        return qmsOqcHeaderVO;
    }

    /**
     *
     * @Description ???????????????
     *
     * @author yuchao.wang
     * @date 2020/8/29 11:08
     * @param tenantId ??????ID
     * @param scanBarcode ??????
     * @return com.ruike.qms.domain.vo.QmsOqcHeaderVO
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue(targetField = {"", "lineList"})
    public QmsOqcHeaderVO docCreate(Long tenantId, String scanBarcode) {
        //????????????
        if(StringUtils.isBlank(scanBarcode)){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }

        //?????????????????????????????????
        if(qmsOqcHeaderRepository.checkProcessing(tenantId, scanBarcode)){
            throw new MtException("QMS_OQC_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_OQC_001", "QMS"));
        }

        //?????????????????????
        MtMaterialLotVO3 vo3 = new MtMaterialLotVO3();
        vo3.setMaterialLotCode(scanBarcode);
        List<MtMaterialLot> materialLotList = mtMaterialLotMapper.selectByPropertyLimit(tenantId, vo3);
        if (CollectionUtils.isEmpty(materialLotList) || Objects.isNull(materialLotList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "???????????????"));
        }
        MtMaterialLot materialLot = materialLotList.get(0);
        QmsPqcInspectionScheme inspectionScheme = new QmsPqcInspectionScheme();
        inspectionScheme.setTenantId(tenantId);
        inspectionScheme.setSiteId(materialLot.getSiteId());
        inspectionScheme.setMaterialId(materialLot.getMaterialId());
        inspectionScheme.setInspectionType("OQC");

        //??????????????????????????? zhangjian????????????????????? 2020/12/21 09:56
//        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
//        mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
//        mtMaterialLotAttrVO2.setAttrName("MATERIAL_VERSION");
//        List<MtExtendAttrVO> materialLotAttrVOList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
//        if(CollectionUtils.isNotEmpty(materialLotAttrVOList) && StringUtils.isNotBlank(materialLotAttrVOList.get(0).getAttrValue())){
//            inspectionScheme.setMaterialVersion(materialLotAttrVOList.get(0).getAttrValue());
//        }

        //??????????????????--????????????+??????+????????????
        inspectionScheme = qmsPqcInspectionSchemeRepository.selectOne(inspectionScheme);
        if(Objects.isNull(inspectionScheme) || StringUtils.isBlank(inspectionScheme.getInspectionSchemeId())){
            //??????????????????  zhangjian????????????????????? 2020/11/17 14:52
//            MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
//            assignVO.setSiteId(materialLotList.get(0).getSiteId());
//            assignVO.setMaterialId(materialLotList.get(0).getMaterialId());
//            assignVO.setDefaultType("MANUFACTURING");
//            String materialCategoryId = mtMaterialCategoryAssignRepository.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);
//            if(StringUtils.isBlank(materialCategoryId)){
//                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_CHIP_TRANSFER_013", "HME", "????????????"));
//            }
//
//            //??????????????????--????????????+????????????
//            inspectionScheme = new QmsPqcInspectionScheme();
//            inspectionScheme.setTenantId(tenantId);
//            inspectionScheme.setSiteId(materialLotList.get(0).getSiteId());
//            inspectionScheme.setMaterialCategoryId(materialCategoryId);
//            inspectionScheme.setInspectionType("OQC");
//            inspectionScheme = qmsPqcInspectionSchemeRepository.selectOne(inspectionScheme);
//            if(Objects.isNull(inspectionScheme) || StringUtils.isBlank(inspectionScheme.getInspectionSchemeId())){
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
//            }
        }

        //??????????????????????????????
        QmsPqcInspectionContent inspectionContent = new QmsPqcInspectionContent();
        inspectionContent.setTenantId(tenantId);
        inspectionContent.setSchemeId(inspectionScheme.getInspectionSchemeId());
        List<QmsPqcInspectionContent> qmsPqcInspectionContents = qmsPqcInspectionContentRepository.select(inspectionContent);
        if(CollectionUtils.isEmpty(qmsPqcInspectionContents)){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }
        List<QmsPqcInspectionContent> qmsPqcInspectionContentList = qmsPqcInspectionContents.stream()
                .sorted(Comparator.comparing(QmsPqcInspectionContent::getOrderKey)).collect(Collectors.toList());

        //??????????????????
        QmsOqcHeaderVO qmsOqcHeaderVO = getBaseOqcData(tenantId, scanBarcode);

        //??????????????????
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, new MtNumrangeVO2() {{
            setObjectCode("OQC_NUMBER");
            setSiteId(qmsOqcHeaderVO.getSiteId());
        }});

        //????????????ID
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("OQC_DOC_CREATED");
        }});

        //?????????????????????
        QmsOqcHeader qmsOqcHeader = new QmsOqcHeader();
        BeanUtils.copyProperties(qmsOqcHeaderVO, qmsOqcHeader);
        qmsOqcHeader.setTenantId(tenantId);
        qmsOqcHeader.setInspectionStatus(QmsConstants.ConstantValue.NEW);
        qmsOqcHeader.setCreatedDate(new Date());
        qmsOqcHeader.setOqcNumber(mtNumrangeVO5.getNumber());
        qmsOqcHeaderRepository.insertSelective(qmsOqcHeader);

        //?????????????????????
        QmsOqcHeaderHis qmsOqcHeaderHis = new QmsOqcHeaderHis();
        BeanUtils.copyProperties(qmsOqcHeader, qmsOqcHeaderHis);
        qmsOqcHeaderHis.setEventId(eventId);
        qmsOqcHeaderHisRepository.insertSelective(qmsOqcHeaderHis);

        //????????????????????????
        List<String> uomCodeList = qmsPqcInspectionContentList.stream().map(QmsPqcInspectionContent::getStandardUom).collect(Collectors.toList());
        List<MtUom> mtUoms = mtUomRepository.uomPropertyBatchGetByCodes(tenantId, uomCodeList);
        Map<String, String> uomMap = new HashMap<>();
        mtUoms.forEach(item -> uomMap.put(item.getUomCode(), item.getUomId()));

        //????????????????????????
        List<QmsOqcLine> insertList = new ArrayList<>();
        List<QmsOqcLineHis> insertHisList = new ArrayList<>();
        int index = 1;
        for(QmsPqcInspectionContent inspectionCon : qmsPqcInspectionContentList){
            QmsOqcLine oqcLine = new QmsOqcLine();
            BeanUtils.copyProperties(inspectionCon, oqcLine);
            oqcLine.setOqcHeaderId(qmsOqcHeader.getOqcHeaderId());
            oqcLine.setNumber(new BigDecimal(index++ * 10));
            oqcLine.setInspectionTool(inspectionCon.getInspectiomTool());
            if(uomMap.containsKey(inspectionCon.getStandardUom())) {
                oqcLine.setStandardUom(uomMap.get(inspectionCon.getStandardUom()));
            }
            insertList.add(oqcLine);
        }
        qmsOqcLineRepository.batchInsertSelective(insertList);

        //????????????
        insertList.forEach(item -> {
            QmsOqcLineHis oqcLineHis = new QmsOqcLineHis();
            BeanUtils.copyProperties(item, oqcLineHis);
            oqcLineHis.setEventId(eventId);
            insertHisList.add(oqcLineHis);
        });
        qmsOqcLineHisRepository.batchInsertSelective(insertHisList);

        //??????????????????
        return qmsOqcHeaderMapper.queryOqcDataByHeadId(qmsOqcHeader.getOqcHeaderId());
    }

    /**
     *
     * @Description ???????????????
     *
     * @author yuchao.wang
     * @date 2020/8/29 17:32
     * @param tenantId ??????ID
     * @param submitFlag ?????????????????? true:?????? false:??????
     * @param oqcInspectionSaveDTO ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void docSave(Long tenantId, boolean submitFlag, QmsOqcInspectionSaveDTO oqcInspectionSaveDTO) {
        //????????????
        if(StringUtils.isBlank(oqcInspectionSaveDTO.getOqcHeaderId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "?????????ID"));
        }

        //????????????????????????
        if(submitFlag){
            if(StringUtils.isBlank(oqcInspectionSaveDTO.getInspectionResult())){
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "?????????????????????"));
            }

            //??????????????????????????????
            QmsOqcLine query = new QmsOqcLine();
            query.setOqcHeaderId(oqcInspectionSaveDTO.getOqcHeaderId());
            int count = qmsOqcLineRepository.selectCount(query);
            if(CollectionUtils.size(oqcInspectionSaveDTO.getLineList()) != count){
                throw new MtException("Exception", "??????????????????????????????");
            }
            if(CollectionUtils.isNotEmpty(oqcInspectionSaveDTO.getLineList())){
                oqcInspectionSaveDTO.getLineList().forEach(item -> {
                    if(StringUtils.isBlank(item.getOqcLineId())){
                        throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "QMS_MATERIAL_INSP_0020", "QMS", "?????????ID"));
                    }
                    if(StringUtils.isBlank(item.getInspectionResult())){
                        throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
                    }
                });
            }
        } else {
            if(CollectionUtils.isNotEmpty(oqcInspectionSaveDTO.getLineList())){
                oqcInspectionSaveDTO.getLineList().forEach(item -> {
                    if(StringUtils.isBlank(item.getOqcLineId())){
                        throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "QMS_MATERIAL_INSP_0020", "QMS", "?????????ID"));
                    }
                });
            }
        }

        //??????????????????
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        //????????????????????????
        qmsOqcDetailsMapper.deleteByHeaderId(tenantId, oqcInspectionSaveDTO.getOqcHeaderId());

        //???????????????
        QmsOqcHeader qmsOqcHeader = new QmsOqcHeader();
        qmsOqcHeader.setOqcHeaderId(oqcInspectionSaveDTO.getOqcHeaderId());
        qmsOqcHeader.setInspectionResult(oqcInspectionSaveDTO.getInspectionResult());
        qmsOqcHeader.setRemark(oqcInspectionSaveDTO.getRemark());

        //?????????????????????????????????,???????????????
        if(submitFlag){
            qmsOqcHeader.setInspectionStatus(QmsConstants.DocStatus.COMPLETED);
            qmsOqcHeader.setInspectionFinishDate(new Date());
            qmsOqcHeader.setQcBy(userId);
            long diff = qmsOqcHeader.getInspectionFinishDate().getTime() - oqcInspectionSaveDTO.getCreatedDate().getTime();
            qmsOqcHeader.setInspectionTime(new BigDecimal(diff).divide(new BigDecimal(1000 * 60 * 60), 2, BigDecimal.ROUND_HALF_UP));
        }
        qmsOqcHeaderMapper.updateByPrimaryKeySelective(qmsOqcHeader);

        //????????????????????????
        List<QmsOqcLine> updateLineList = new ArrayList<>();
        List<QmsOqcDetails> insertDetailList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(oqcInspectionSaveDTO.getLineList())){
            for (QmsOqcInspectionSaveLineDTO lineDTO : oqcInspectionSaveDTO.getLineList()){
                if(StringUtils.isNotBlank(lineDTO.getInspectionResult()) || StringUtils.isNotBlank(lineDTO.getAttachmentUuid())) {
                    QmsOqcLine oqcLine = new QmsOqcLine();
                    oqcLine.setOqcLineId(lineDTO.getOqcLineId());
                    oqcLine.setInspectionResult(lineDTO.getInspectionResult());
                    oqcLine.setAttachmentUuid(lineDTO.getAttachmentUuid());
                    updateLineList.add(oqcLine);
                }

                //??????????????????
                if(CollectionUtils.isNotEmpty(lineDTO.getDetailList())) {
                    for(QmsOqcInspectionSaveDetailDTO detailDTO : lineDTO.getDetailList()){
                        if(!Objects.isNull(detailDTO.getNumber())) {
                            QmsOqcDetails oqcDetails = new QmsOqcDetails();
                            BeanUtils.copyProperties(detailDTO, oqcDetails);
                            oqcDetails.setOqcHeaderId(oqcInspectionSaveDTO.getOqcHeaderId());
                            oqcDetails.setOqcLineId(lineDTO.getOqcLineId());
                            oqcDetails.setTenantId(tenantId);
                            insertDetailList.add(oqcDetails);
                        }
                    }
                }
            }

            if(CollectionUtils.isNotEmpty(updateLineList)) {
                qmsOqcLineMapper.batchUpdateResult(userId, updateLineList);
            }
            if(CollectionUtils.isNotEmpty(insertDetailList)) {
                qmsOqcDetailsRepository.batchInsertSelective(insertDetailList);
            }
        }

        //????????????
        if(submitFlag){
            saveHisForSubmit(tenantId, oqcInspectionSaveDTO.getOqcHeaderId(), insertDetailList);
        }
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/8/29 18:17
     * @param tenantId ??????ID
     * @param oqcHeaderId ???ID
     * @param detailList ????????????
     * @return void
     *
     */
    private void saveHisForSubmit(Long tenantId, String oqcHeaderId, List<QmsOqcDetails> detailList){
        //????????????ID
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("OQC_DOC_SUBMIT");
        }});

        //?????????????????????
        QmsOqcHeader header = qmsOqcHeaderRepository.selectByPrimaryKey(oqcHeaderId);
        if(Objects.isNull(header) || StringUtils.isBlank(header.getOqcHeaderId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "???????????????"));
        }

        QmsOqcHeaderHis qmsOqcHeaderHis = new QmsOqcHeaderHis();
        BeanUtils.copyProperties(header, qmsOqcHeaderHis);
        qmsOqcHeaderHis.setEventId(eventId);
        qmsOqcHeaderHisRepository.insertSelective(qmsOqcHeaderHis);

        //?????????????????????
        QmsOqcLine line = new QmsOqcLine();
        line.setOqcHeaderId(oqcHeaderId);
        List<QmsOqcLine> oqcLineList = qmsOqcLineRepository.select(line);
        if(CollectionUtils.isNotEmpty(oqcLineList)) {
            List<QmsOqcLineHis> insertHisList = new ArrayList<>();
            oqcLineList.forEach(item -> {
                QmsOqcLineHis oqcLineHis = new QmsOqcLineHis();
                BeanUtils.copyProperties(item, oqcLineHis);
                oqcLineHis.setEventId(eventId);
                insertHisList.add(oqcLineHis);
            });
            qmsOqcLineHisRepository.batchInsertSelective(insertHisList);
        }

        //????????????????????????
        if(CollectionUtils.isNotEmpty(detailList)) {
            List<QmsOqcDetailsHis> insertHisList = new ArrayList<>();
            detailList.forEach(item -> {
                QmsOqcDetailsHis detailsHis = new QmsOqcDetailsHis();
                BeanUtils.copyProperties(item, detailsHis);
                detailsHis.setEventId(eventId);
                insertHisList.add(detailsHis);
            });
            qmsOqcDetailsHisRepository.batchInsertSelective(insertHisList);
        }
    }
}