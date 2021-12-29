package com.ruike.wms.app.service.impl;

import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.wms.api.dto.WmsReplenishmentCreateDTO;
import com.ruike.wms.app.service.WmsDistributionListQueryService;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.domain.entity.WmsDistributionBasicDataProductionLine;
import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;
import com.ruike.wms.domain.repository.WmsDistributionDemandDetailRepository;
import com.ruike.wms.domain.repository.WmsInstructionDocRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsDistributionDemandDetailMapper;
import com.ruike.wms.infra.mapper.WmsDistributionDemandMapper;
import com.ruike.wms.infra.mapper.WmsDistributionListQueryMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO5;
import tarzan.inventory.infra.mapper.MtContainerMapper;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.wms.infra.constant.WmsConstant.BusinessType.DISTRIBUTION_EXECUTE;
import static com.ruike.wms.infra.constant.WmsConstant.DocType.DISTRIBUTION_DOC;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.INSTRUCTION_DOC_CREATE;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.*;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR;
import static com.ruike.wms.infra.constant.WmsConstant.MaterialLotStatus.PREPARED;
import static com.ruike.wms.infra.constant.WmsConstant.MaterialLotStatus.SCANNED;
import static com.ruike.wms.infra.constant.WmsConstant.Profile.WMS_DISTRIBUTION_SIGN_FLAG;

@Slf4j
@Service
public class WmsDistributionListQueryServiceImpl implements WmsDistributionListQueryService {

    @Autowired
    private WmsDistributionListQueryMapper wmsDistributionListQueryMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtContainerMapper mtContainerMapper;

    @Autowired
    private MtUserClient mtUserClient;

    @Autowired
    FileClient fileClient;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private WmsDistributionDemandDetailMapper wmsDistributionDemandDetailMapper;

    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private WmsDistributionDemandMapper wmsDistributionDemandMapper;
    @Autowired
    private WmsInstructionDocRepository wmsInstructionDocRepository;
    @Autowired
    private WmsEventService wmsEventService;
    @Autowired
    private MtInstructionDocRepository instructionDocRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private MtInstructionRepository instructionRepository;
    @Autowired
    private MtInstructionActualRepository instructionActualRepository;
    @Autowired
    private MtInstructionDocMapper instructionDocMapper;
    @Autowired
    private WmsDistributionDemandDetailRepository wmsDistributionDemandDetailRepository;

    @Override
    @ProcessLovValue
    public Page<WmsDistributionListQueryVO> propertyDistributionDocQuery(Long tenantId, WmsDistributionListQueryVO dto, PageRequest pageRequest) {
        dto.setTenantId(tenantId);
        if (StringUtils.isNotBlank(dto.getInstructionDocStatus())) {
            dto.setStatusList(Arrays.asList(dto.getInstructionDocStatus().split(",")));
        }
        //2020-11-02 add by sanfeng.zhang for yanshangwen 查询用户id
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        dto.setUserId(userId);
        Page<WmsDistributionListQueryVO> returnList = PageHelper.doPage(pageRequest, () -> wmsDistributionListQueryMapper.selectDistributionDoc(dto));
        List<Long> userIdList = returnList.getContent().stream().map(WmsDistributionListQueryVO::getCreatedBy).filter(Objects::nonNull).collect(Collectors.toList());
        userIdList.addAll(returnList.getContent().stream().map(WmsDistributionListQueryVO::getLastUpdatedBy).filter(Objects::nonNull).collect(Collectors.toList()));
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
        for (WmsDistributionListQueryVO resultDto : returnList) {
            // 回写用户名
            resultDto.setCreatedByName(userInfoMap.containsKey(resultDto.getCreatedBy()) ? userInfoMap.get(resultDto.getCreatedBy()).getRealName() : "");
            resultDto.setLastUpdatedByName(userInfoMap.containsKey(resultDto.getLastUpdatedBy()) ? userInfoMap.get(resultDto.getLastUpdatedBy()).getRealName() : "");
            // 检查备齐
            List<WmsDistributionListQueryVO1> lineList = wmsDistributionListQueryMapper.selectDistribution(tenantId, resultDto.getInstructionDocId());
            resultDto.setSuiteFlag(lineList.stream().allMatch(rec -> rec.getAcutalQty().compareTo(rec.getQuantity()) >= 0) ? YES : NO);
        }
        return returnList;
    }

    @Override
    public Page<WmsDistributionListQueryVO1> propertyDistributionQuery(Long tenantId, String instructionDocId, PageRequest pageRequest) {
        //获取workcellID
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, instructionDocId);
        String workCellId = null;
        MtExtendSettings mtExtendSettings = new MtExtendSettings();
        mtExtendSettings.setAttrName("WORKCELL");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                "mt_instruction_doc_attr", "INSTRUCTION_DOC_ID", instructionDocId, Collections.singletonList(mtExtendSettings));
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            MtModWorkcell modWorkcell = new MtModWorkcell();
            modWorkcell.setWorkcellCode(mtExtendAttrVOList.get(0).getAttrValue());
            modWorkcell = mtModWorkcellRepository.selectOne(modWorkcell);
            if (!Objects.isNull(modWorkcell)) {
                workCellId = modWorkcell.getWorkcellId();
            }
        }

        Page<WmsDistributionListQueryVO1> result = PageHelper.doPage(pageRequest, () -> wmsDistributionListQueryMapper.selectDistribution(tenantId, instructionDocId));
        //获取线边库存和仓库库存
        List<String> materialIdList = result.getContent().stream().map(WmsDistributionListQueryVO1::getMaterialId).collect(Collectors.toList());
        List<WmsBatchDistributionDemandVO> wmsBatchDistributionDemandVOList = new ArrayList<>();
        List<WmsBatchDistributionDemandVO> distributionDemandVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialIdList)) {
            wmsBatchDistributionDemandVOList = wmsDistributionListQueryMapper.selectBatchInStockByDemand(tenantId, mtInstructionDoc.getSiteId(), workCellId, materialIdList);
            distributionDemandVOList = wmsDistributionListQueryMapper.selectBatchInventoryQtyByDemand(tenantId, mtInstructionDoc.getSiteId(), materialIdList);
        }

        Map<String, List<WmsBatchDistributionDemandVO>> resultMap = wmsBatchDistributionDemandVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId() + dto.getMaterialVersion() + dto.getSiteId() + dto.getSoLineNum() + dto.getSoNum() + dto.getWorkCellId()));
        Map<String, List<WmsBatchDistributionDemandVO>> inventoryMap = distributionDemandVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId() + dto.getMaterialVersion() + dto.getSiteId() + dto.getSoLineNum() + dto.getSoNum()));
       // List<WmsDistributionDemandDetail> details = wmsDistributionDemandDetailRepository.selectAll();
        if(CollectionUtils.isEmpty(result))
        {
            return result;
        }
        List<String> instructionIds = result.stream().map(t -> t.getInstructionId()).collect(Collectors.toList());
        List<WmsDistributionListQueryVO3> details=wmsDistributionDemandDetailRepository.selectByInstructionId(tenantId,instructionDocId,instructionIds);
        Map<String, List<WmsDistributionListQueryVO3>> detailsMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(details)) {
            detailsMap = details.stream().collect(Collectors.groupingBy(t -> t.getInstructionId()));
        }
        for (WmsDistributionListQueryVO1 dto : result) {
            WmsDistributionDemandQueryVO vo = new WmsDistributionDemandQueryVO();
            vo.setMaterialVersion(dto.getMaterialVersion());
            vo.setSoNum(dto.getRelSoNum());
            vo.setSoLineNum(dto.getRelSoLineNum());
            vo.setSiteId(mtInstructionDoc.getSiteId());
            vo.setMaterialId(dto.getMaterialId());
            vo.setWorkcellId(workCellId);

            String inStockKey = concatParam(vo, YES);
            List<WmsBatchDistributionDemandVO> wmsBatchDistributionDemandVOS = resultMap.get(inStockKey);
            if (CollectionUtils.isNotEmpty(wmsBatchDistributionDemandVOS)) {
                dto.setInStockQty(wmsBatchDistributionDemandVOS.get(0).getInStockQty());
            } else {
                dto.setInStockQty(BigDecimal.ZERO);
            }

            String inventory = concatParam(vo, NO);
            List<WmsBatchDistributionDemandVO> inventoryList = inventoryMap.get(inventory);
            if (CollectionUtils.isNotEmpty(inventoryList)) {
                dto.setInventoryQty(inventoryList.get(0).getInStockQty());
            } else {
                dto.setInventoryQty(BigDecimal.ZERO);
            }

            //查询冲抵数量
            List<WmsDistributionListQueryVO3> detailList = detailsMap.get(dto.getInstructionId());
            if(CollectionUtils.isNotEmpty(detailList)){
                if(StringUtils.isNotBlank(detailList.get(0).getAttribute4())&&StringUtils.isNotBlank(detailList.get(0).getAttribute6())){
                    dto.setOffsetQty(new BigDecimal(detailList.get(0).getAttribute4()).add(new BigDecimal(detailList.get(0).getAttribute6()).abs()));
                }
            }

        }
        return result;
    }

    private String concatParam(WmsDistributionDemandQueryVO vo, String flag) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getMaterialId());
        if (StringUtils.isBlank(vo.getMaterialVersion())) {
            vo.setMaterialVersion("");
        }
        if (StringUtils.isBlank(vo.getSoLineNum())) {
            vo.setSoLineNum("");
        }
        if (StringUtils.isBlank(vo.getSoNum())) {
            vo.setSoNum("");
        }
        sb.append(vo.getMaterialVersion());
        sb.append(vo.getSiteId());
        sb.append(vo.getSoLineNum());
        sb.append(vo.getSoNum());
        if (YES.equals(flag)) {
            sb.append(vo.getWorkcellId());
        }
        return sb.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<WmsDistributionListQueryVO2> propertyDistributionDtlQuery(Long tenantId, String instructionId, PageRequest pageRequest) {
        Page<WmsDistributionListQueryVO2> returnList = PageHelper.doPage(pageRequest, () -> wmsDistributionListQueryMapper.selectDistributionDtl(tenantId, instructionId));
        for (WmsDistributionListQueryVO2 dto : returnList) {
            //调用API获取容器ID
            MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
            mtContLoadDtlVO5.setLoadObjectType("MATERIAL_LOT");
            mtContLoadDtlVO5.setLoadObjectId(dto.getMaterialLotId());
            mtContLoadDtlVO5.setTopLevelFlag("Y");
            List<String> containerIds = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
            if (CollectionUtils.isNotEmpty(containerIds)) {
                MtContainer mtContainer = new MtContainer();
                mtContainer.setContainerId(containerIds.get(0));
                mtContainer = mtContainerMapper.selectByPrimaryKey(mtContainer);
                dto.setContainerNum(mtContainer.getContainerCode());
            }
            MtUserInfo userInfo = mtUserClient.userInfoGet(tenantId, Long.parseLong(dto.getCreatedBy()));
            dto.setCreatedBy(userInfo.getRealName());

            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.MTLOT.STATUS", tenantId);
            List<LovValueDTO> collect = lovValueDTOS.stream().filter(e -> StringUtils.equals(e.getValue(), dto.getMaterialStatus())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                dto.setMaterialStatus(collect.get(0).getMeaning());
            }
        }
        return returnList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiplePrint(Long tenantId, List<String> instructionDocIds, HttpServletResponse response) {

        //单据状态变为下达
        for (String instructionDocId : instructionDocIds) {
            //根据配送单头id查询配送单头数据
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(instructionDocId);
            mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);

            if ("NEW".equals(mtInstructionDoc.getInstructionDocStatus())) {
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("DISTRIBUTION_RELEASED");
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                //头状态更改为下达
                MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
                mtInstructionDoc2.setInstructionDocId(instructionDocId);
                mtInstructionDoc2.setInstructionDocType("DISTRIBUTION_DOC");
                mtInstructionDoc2.setInstructionDocStatus("RELEASED");
                mtInstructionDoc2.setEventId(eventId);
                mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");

                //查询送货单头下的所有送货单行
                MtInstruction instruction = new MtInstruction();
                instruction.setSourceDocId(instructionDocId);
                List<MtInstruction> mtInstructions = mtInstructionRepository.select(instruction);
                for (MtInstruction lineDto : mtInstructions) {
                    MtEventCreateVO eventCreateVO1 = new MtEventCreateVO();
                    eventCreateVO1.setEventTypeCode("DISTRIBUTION_RELEASED");
                    String eventId1 = mtEventRepository.eventCreate(tenantId, eventCreateVO1);

                    MtInstructionVO mtInstructionVO = new MtInstructionVO();
                    mtInstructionVO.setInstructionId(lineDto.getInstructionId());
                    mtInstructionVO.setInstructionStatus("RELEASED");
                    mtInstructionVO.setEventId(eventId1);
                    mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, QmsConstants.ConstantValue.NO);
                }
            }
        }

        List<String> filePathList = new ArrayList();
        String outStream = "";
        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String dateTime = System.currentTimeMillis() + "";
        String docNumber = "";
        String uuid = UUID.randomUUID().toString();
        ;
        String barcodePath = "";
        String qrcodePath = "";
        String content = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //定义每页可以打印的行上限
        Long lineCount = 10L;
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        String realName = userDetails.getRealName();
        //循环需要打印的数据，单个打印
        for (String instructionDocId : instructionDocIds) {
            //获取头数据
            WmsDistributionListQueryVO headDto = new WmsDistributionListQueryVO();
            headDto.setInstructionDocId(instructionDocId);
            headDto.setTenantId(tenantId);
            headDto.setUserId(userId);
            headDto = wmsDistributionListQueryMapper.selectDistributionDoc(headDto).get(0);
            //当前登录用户

            headDto.setCreatedByName(realName);
            //获取工段名称
            MtModWorkcell modWorkcell = new MtModWorkcell();
            modWorkcell.setWorkcellCode(headDto.getWorkCell());
            modWorkcell = mtModWorkcellMapper.selectOne(modWorkcell);
            String workCellName = modWorkcell.getWorkcellName();

            //生成二维码
            String codeUuid = UUID.randomUUID().toString();
            ;
            qrcodePath = basePath + "/" + codeUuid + "_" + docNumber + "_qrcode.png";
            File qrcodeImageFile = new File(qrcodePath);
            qrcodeImageFileList.add(qrcodeImageFile);
            content = headDto.getInstructionDocNum();
            try {
                CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
                log.info("<====生成二维码完成！{}", qrcodePath);
            } catch (Exception e) {
                log.error("<==== WmsDeliveryDocServiceImpl.multiplePrint.encode Error", e);
                throw new MtException(e.getMessage());
            }

            //组装参数
            Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            imgMap.put("qrcodeImage", qrcodePath);
            Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

            //获取行数据
            Boolean printflag = false;
            Long currentLine = 0L;
            Double sumQty = 0d;
            List<WmsDistributionListQueryVO1> lineList = wmsDistributionListQueryMapper.selectDistribution(tenantId, instructionDocId);
            for (WmsDistributionListQueryVO1 lineDto : lineList) {
                formMap.put("lineNum" + currentLine.toString(), lineDto.getInstructionNum());
                formMap.put("itemCode" + currentLine.toString(), lineDto.getMaterialCode());
                formMap.put("itemDesc" + currentLine.toString(), lineDto.getMaterialName());
                formMap.put("qty" + currentLine.toString(), lineDto.getQuantity());
                formMap.put("version" + currentLine.toString(), lineDto.getMaterialVersion());
                formMap.put("uom" + currentLine.toString(), lineDto.getUomCode());
                formMap.put("remark" + currentLine.toString(), lineDto.getRemark());
                if (!Objects.isNull(lineDto.getQuantity())) {
                    sumQty += lineDto.getQuantity().doubleValue();
                }
                currentLine += 1L;
                printflag = false;
                if (currentLine % lineCount == 0) {

                    formMap.put("instructionDocNum", headDto.getInstructionDocNum());
                    formMap.put("siteName", headDto.getSiteId());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    formMap.put("date", sdf.format(new Date()));
                    formMap.put("remark", headDto.getProductionLine());
                    formMap.put("createdBy", headDto.getCreatedBy());
                    formMap.put("workCellName", workCellName);

                    formMap.put("sumQty", sumQty);
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    currentLine = 0L;
                    sumQty = 0D;
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    printflag = true;
                }
            }
            if (!printflag) {
                formMap.put("instructionDocNum", headDto.getInstructionDocNum());
                formMap.put("siteName", headDto.getSiteId());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                formMap.put("date", sdf.format(new Date()));
                formMap.put("remark", headDto.getProductionLine());
                formMap.put("createdBy", headDto.getCreatedBy());
                formMap.put("workCellName", workCellName);

                formMap.put("sumQty", sumQty);
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
            }
        }
        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/hme_distribution_print_template.pdf", pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeDistributionListQueryServiceImpl.multiplePrint.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //将文件转化成流进行输出
        /*File pdfFile = new File(pdfPath);
        //String outPutStream = WmsCommonUtils.getStringByFile(pdfPath);
        String d = DateUtil.date2String(new Date(), "yyyyMMdd");
        String filePath = fileClient.uploadFile(tenantId, bucketName, "pages" + "/" + d,
                pdfFileName, "application/pdf", FileUtil.File2byte(pdfFile));
        filePath = fileClient.getSignedUrl(tenantId,bucketName,null,filePath);
        filePathList.add(filePath);*/

        //将文件转化成流进行输出
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //设置相应参数
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //将文件转化成流进行输出
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== HmeDistributionListQueryServiceImpl.multiplePrint.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== HmeDistributionListQueryServiceImpl.multiplePrint.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File file : qrcodeImageFileList
        ) {
            if (!file.delete()) {
                log.info("<==== HmeDistributionListQueryServiceImpl.multiplePrint.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== HmeDistributionListQueryServiceImpl.multiplePrint.pdfFile Failed: {}", barcodePath);
        }

        //return filePathList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelDistribution(Long tenantId, List<String> instructionDocIds) {
        for (String instructionDocId : instructionDocIds) {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("DISTRIBUTION_CANCEL");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            //根据配送单头id查询配送单头数据
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(instructionDocId);
            mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);

            //校验配送单的状态表是否为NEW
            /*if (!"NEW".equals(mtInstructionDoc.getInstructionDocStatus())) {
                throw new MtException("WMS_DISTRIBUTION_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_004", "WMS"));
            }*/

            //查询送货单头下的所有送货单行
            List<WmsDistributionListQueryVO1> mtInstructions = wmsDistributionListQueryMapper.selectDistribution(tenantId, instructionDocId);
            for (WmsDistributionListQueryVO1 line : mtInstructions) {
                if (!Objects.isNull(line.getAcutalQty()) && BigDecimal.ZERO.compareTo(line.getAcutalQty()) < 0) {
                    throw new MtException("WMS_DISTRIBUTION_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_DISTRIBUTION_003", "WMS", mtInstructionDoc.getInstructionDocNum()));
                }
                //配送需求指令更新
                WmsDistributionDemandDetail wmsDistributionDemandDetail = new WmsDistributionDemandDetail();
                wmsDistributionDemandDetail.setInstructionDocId(instructionDocId);
                wmsDistributionDemandDetail.setInstructionId(line.getInstructionId());
                List<WmsDistributionDemandDetail> dtlList = wmsDistributionDemandDetailMapper.select(wmsDistributionDemandDetail);
                for (WmsDistributionDemandDetail dtlDto : dtlList) {
                    dtlDto.setInstructionId(null);
                    dtlDto.setInstructionDocId(null);
                    dtlDto.setAttribute1(null);
                    dtlDto.setAttribute2(null);
                    dtlDto.setAttribute3(null);
                    dtlDto.setAttribute4(null);
                    dtlDto.setAttribute5(null);
                    dtlDto.setAttribute6(null);
                    dtlDto.setAttribute7(null);
                    wmsDistributionDemandDetailMapper.updateByPrimaryKey(dtlDto);
                }
            }

            //头状态更改为取消
            MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
            mtInstructionDoc2.setInstructionDocId(instructionDocId);
            mtInstructionDoc2.setInstructionDocType("DISTRIBUTION_DOC");
            mtInstructionDoc2.setInstructionDocStatus("CANCEL");
            mtInstructionDoc2.setEventId(eventId);
            mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeDistribution(Long tenantId, List<String> instructionDocIds) {
        for (String instructionDocId : instructionDocIds) {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("DISTRIBUTION_CLOSED");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            //根据配送单头id查询配送单头数据
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(instructionDocId);
            mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);

            //校验配送单的状态表是否为CLOSED
            if (CLOSED.equals(mtInstructionDoc.getInstructionDocStatus())) {
                throw new MtException("WMS_DISTRIBUTION_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_007", "WMS"));
            }
            //查询送货单头下的所有送货单行
            MtInstruction instruction = new MtInstruction();
            instruction.setSourceDocId(instructionDocId);
            List<MtInstruction> mtInstructions = mtInstructionRepository.select(instruction);

            for (MtInstruction line : mtInstructions) {
                //调用API【propertyLimitInstructionActualQuery】获取指令实绩ID
                MtInstructionActual mtInstructionActual = new MtInstructionActual();
                mtInstructionActual.setInstructionId(line.getInstructionId());
                List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                        mtInstructionActual);

                //调用API获取物料批ID
                for (String actualId : actualIdList) {
                    MtInstructionActualDetail query = new MtInstructionActualDetail();
                    query.setActualId(actualId);
                    List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, query);

                    for (MtInstructionActualDetailVO mtMaterialLot : detailList) {
                        //条码状态
                        MtExtendSettings mtExtendSettings = new MtExtendSettings();
                        mtExtendSettings.setAttrName("STATUS");
                        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                                "mt_material_lot_attr", "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(), Collections.singletonList(mtExtendSettings));
                        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
                            if (SCANNED.equals(mtExtendAttrVO.getAttrValue())) {
                                throw new MtException("WMS_DISTRIBUTION_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WMS_DISTRIBUTION_005", "WMS"));
                            }
                            if (PREPARED.equals(mtExtendAttrVO.getAttrValue())) {
                                throw new MtException("WMS_DISTRIBUTION_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WMS_DISTRIBUTION_006", "WMS"));
                            }
                        }
                    }
                }
            }

            //头状态更改为关闭
            MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
            mtInstructionDoc2.setInstructionDocId(instructionDocId);
            mtInstructionDoc2.setInstructionDocType(DISTRIBUTION_DOC);
            mtInstructionDoc2.setInstructionDocStatus(CLOSED);
            mtInstructionDoc2.setEventId(eventId);
            mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, NO);
        }
    }

    @Override
    public List<WmsReplenishmentLineVO> replenishmentLineGet(Long tenantId, List<String> instructionDocIds, PageRequest pageRequest) {
        List<WmsInstructionDocAttrVO> docList = wmsInstructionDocRepository.selectListByDocIds(tenantId, instructionDocIds);
        Set<String> workcellSet = new HashSet<>();
        docList.forEach(doc -> {
            if (!StringCommonUtils.contains(doc.getInstructionDocStatus(), SIGN_EXECUTE, SIGN_COMPLETE, CLOSED)) {
                throw new CommonException("配送单还未进行签收，无需生成补料单");
            }
            if (StringUtils.isNotBlank(doc.getReplenishmentDocId())) {
                throw new CommonException("已经生成过补料单");
            }
            List<WmsDistributionListQueryVO1> lineList = wmsDistributionListQueryMapper.selectDistribution(tenantId, doc.getInstructionDocId());
            if (lineList.stream().allMatch(rec -> rec.getAcutalQty().compareTo(rec.getQuantity()) >= 0)) {
                throw new CommonException("配送单均已配送齐全，无需生成配送单");
            }
            workcellSet.add(doc.getWorkcellCode());
            if (workcellSet.size() > 1) {
                throw new CommonException("配送单需要相同工段才能生成补料单，请勾选相同工段的配送单");
            }
        });

        List<WmsReplenishmentLineVO> page = wmsDistributionListQueryMapper.selectReplenishLineByDocIds(tenantId, instructionDocIds);
        if (CollectionUtils.isNotEmpty(page)) {
            AtomicInteger atomKey = new AtomicInteger(1);
            page.forEach(rec -> {
                WmsDistributionDemandQueryVO vo = new WmsDistributionDemandQueryVO();
                vo.setMaterialId(rec.getMaterialId());
                vo.setMaterialVersion(rec.getMaterialVersion());
                vo.setSoNum(rec.getSoNum());
                vo.setSoLineNum(rec.getSoLineNum());
                vo.setSiteId(docList.get(0).getSiteId());
                vo.setWorkcellId(docList.get(0).getWorkcellId());
                BigDecimal inStockQty = wmsDistributionDemandMapper.selectInStockByDemand(tenantId, vo);
                BigDecimal inventoryQty = wmsDistributionDemandMapper.selectInventoryQtyByDemand(tenantId, vo);
                rec.setKeyId(atomKey.getAndIncrement());
                rec.setInStockQty(inStockQty);
                rec.setInventoryQty(inventoryQty);
            });
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void replenishmentDocCreate(Long tenantId, WmsReplenishmentCreateDTO dto) {
        // 验证数据合法性
        List<WmsInstructionDocAttrVO> docList = this.validatePreReplenishmentDocCreate(tenantId, dto);

        // 获取配置文件中的配送标志
        String signFlagProfile = Optional.ofNullable(profileClient.getProfileValueByOptions(WMS_DISTRIBUTION_SIGN_FLAG)).orElse(YES);

        // 事件与事件请求生成
        WmsEventVO event = wmsEventService.createEventWithRequest(tenantId, INSTRUCTION_DOC_CREATE);

        // 补料单指令单据创建
        MtInstructionDocVO3 doc = instructionDocCreate(tenantId, dto.getSiteId(), dto.getDemandTime(), dto.getRemark(), event.getEventRequestId());

        // 补料单指令拓展属性更新
        this.updateDocExtendAttr(tenantId, doc.getInstructionDocId(), event.getEventId(), dto.getProdLineCode(), dto.getWorkcellCode(), dto.getToLocatorId(), signFlagProfile);

        // 补料单指令行创建
        AtomicInteger lineNum = new AtomicInteger(1);
        dto.getLineList().stream().filter(line -> line.getReplenishQty().compareTo(BigDecimal.ZERO) > 0).forEach(line -> this.processCreateInstruction(tenantId, doc.getInstructionDocId(), lineNum.getAndIncrement() * 10, line, dto.getToLocatorId(), dto.getSiteId(), dto.getDemandTime(), event.getEventId()));

        // 将补料单ID回写原配送单上的拓展属性
        docList.forEach(origDoc -> {
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 signFlag = new MtExtendVO5();
            signFlag.setAttrName("REPLENISHMENT_LIST_NUM");
            signFlag.setAttrValue(doc.getInstructionDocId());
            attrs.add(signFlag);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", origDoc.getInstructionDocId(), event.getEventId(), attrs);
            // 触发乐观锁防止重复生成
            MtInstructionDoc oldDoc = new MtInstructionDoc();
            oldDoc.setInstructionDocId(origDoc.getInstructionDocId());
            instructionDocMapper.updateByPrimaryKeySelective(oldDoc);
        });
    }

    @Override
    @ProcessLovValue
    public List<WmsDistributionDocVO> instructionDocExport(Long tenantId, String instructionDocId) {
        List<String> instructionDocIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(instructionDocId)) {
            instructionDocIdList = Arrays.asList(StringUtils.split(instructionDocId, ","));
        } else {
            return Collections.EMPTY_LIST;
        }
        List<WmsDistributionDocVO> resultList = wmsDistributionListQueryMapper.instructionDocExport(tenantId, instructionDocIdList);
        for (WmsDistributionDocVO wmsDistributionDocVO : resultList) {
            // 检查备齐
            List<WmsDistributionListQueryVO1> lineList = wmsDistributionListQueryMapper.selectDistribution(tenantId, wmsDistributionDocVO.getInstructionDocId());
            wmsDistributionDocVO.setSuiteFlag(lineList.stream().allMatch(rec -> rec.getAcutalQty().compareTo(rec.getQuantity()) >= 0) ? YES : NO);
        }
        return resultList;
    }

    /**
     * 验证参数
     *
     * @param tenantId 租户
     * @param dto      参数
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/4 11:01:58
     */
    private List<WmsInstructionDocAttrVO> validatePreReplenishmentDocCreate(Long tenantId, WmsReplenishmentCreateDTO dto) {
        // 验证单据状态
        List<WmsInstructionDocAttrVO> docList = wmsInstructionDocRepository.selectListByDocIds(tenantId, dto.getInstructionDocIdList());
        docList.forEach(doc -> {
            if (StringUtils.isNotBlank(doc.getReplenishmentDocId())) {
                throw new CommonException("已经生成过补料单");
            }
        });
        if (dto.getLineList().stream().noneMatch(rec -> Optional.ofNullable(rec.getReplenishQty()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0)) {
            throw new CommonException("没有可以生成补料单的数据！");
        }
        return docList;
    }

    /**
     * 单据创建
     *
     * @param tenantId       租户
     * @param siteId         站点
     * @param demandTime     需求事件
     * @param remark         备注
     * @param eventRequestId 事件请求
     * @return tarzan.instruction.domain.vo.MtInstructionDocVO3
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/3 03:10:13
     */
    private MtInstructionDocVO3 instructionDocCreate(Long tenantId, String siteId, Date demandTime, String remark, String eventRequestId) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        // 指令单据创建
        MtInstructionDocDTO2 insDocCreate = new MtInstructionDocDTO2();
        insDocCreate.setSiteId(siteId);
        insDocCreate.setInstructionDocType(DISTRIBUTION_DOC);
        insDocCreate.setDemandTime(demandTime);
        insDocCreate.setPersonId(userDetails.getUserId());
        insDocCreate.setEventRequestId(eventRequestId);
        insDocCreate.setInstructionDocStatus(WmsConstant.InstructionStatus.NEW);
        insDocCreate.setRemark(remark);
        return instructionDocRepository.instructionDocUpdate(tenantId, insDocCreate, WmsConstant.CONSTANT_N);
    }

    /**
     * 更新单据拓展字段
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @param eventId          事件ID
     * @param prodLineCode     产线代码
     * @param locatorId        货位
     * @param workcellCode     工段代码
     * @param signFlagProfile  签收标志
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/7 07:55:26
     */
    private void updateDocExtendAttr(Long tenantId, String instructionDocId, String eventId, String prodLineCode, String workcellCode, String locatorId, String signFlagProfile) {
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 signFlag = new MtExtendVO5();
        signFlag.setAttrName("SIGN_FLAG");
        signFlag.setAttrValue(signFlagProfile);
        attrs.add(signFlag);
        MtExtendVO5 prodLine = new MtExtendVO5();
        prodLine.setAttrName("PROD_LINE_CODE");
        prodLine.setAttrValue(prodLineCode);
        attrs.add(prodLine);
        MtExtendVO5 workcell = new MtExtendVO5();
        workcell.setAttrName("WORKCELL");
        workcell.setAttrValue(workcellCode);
        attrs.add(workcell);
        MtExtendVO5 locator = new MtExtendVO5();
        locator.setAttrName("TO_LOCATOR_ID");
        locator.setAttrValue(locatorId);
        attrs.add(locator);
        MtExtendVO5 supplyFlag = new MtExtendVO5();
        supplyFlag.setAttrName("SUPPLY_FLAG");
        supplyFlag.setAttrValue(YES);
        attrs.add(supplyFlag);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", instructionDocId, eventId, attrs);
    }

    /**
     * 单据行生成
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @param instructionNum   单据行号
     * @param siteId           站点ID
     * @param eventId          事件ID
     * @param demandTime       需求事件
     * @param line             行数据
     * @param toLocatorId      目标货位
     * @return tarzan.instruction.domain.vo.MtInstructionVO6
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/7 07:54:27
     */
    private MtInstructionVO6 processCreateInstruction(Long tenantId, String instructionDocId, Integer instructionNum, WmsReplenishmentLineVO line, String toLocatorId, String siteId, Date demandTime, String eventId) {
        // 指令创建
        MtInstructionVO instructionCreate = new MtInstructionVO();
        instructionCreate.setSourceDocId(instructionDocId);
        instructionCreate.setSiteId(siteId);
        instructionCreate.setMaterialId(line.getMaterialId());
        instructionCreate.setUomId(line.getUomId());
        instructionCreate.setFromSiteId(siteId);
        instructionCreate.setToSiteId(siteId);
        instructionCreate.setFromLocatorId(toLocatorId);
        instructionCreate.setToLocatorId(toLocatorId);
        instructionCreate.setQuantity(line.getReplenishQty().doubleValue());
        instructionCreate.setDemandTime(demandTime);
        instructionCreate.setBusinessType(DISTRIBUTION_EXECUTE);
        instructionCreate.setInstructionType(TRANSFER_OVER_LOCATOR);
        instructionCreate.setRemark(line.getRemark());
        MtInstructionVO6 instruction = instructionRepository.instructionUpdate(tenantId, instructionCreate, WmsConstant.CONSTANT_N);

        // 指令拓展属性更新
        MtExtendVO10 extendAttr = new MtExtendVO10();
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 materialVersion = new MtExtendVO5();
        materialVersion.setAttrName("MATERIAL_VERSION");
        materialVersion.setAttrValue(line.getMaterialVersion());
        attrs.add(materialVersion);
        MtExtendVO5 lineNum = new MtExtendVO5();
        lineNum.setAttrName("INSTRUCTION_LINE_NUM");
        lineNum.setAttrValue(String.valueOf(instructionNum));
        attrs.add(lineNum);
        MtExtendVO5 so = new MtExtendVO5();
        so.setAttrName("SO_NUM");
        so.setAttrValue(line.getSoNum());
        attrs.add(so);
        MtExtendVO5 soLine = new MtExtendVO5();
        soLine.setAttrName("SO_LINE_NUM");
        soLine.setAttrValue(line.getSoLineNum());
        attrs.add(soLine);
        extendAttr.setKeyId(instruction.getInstructionId());
        extendAttr.setEventId(eventId);
        extendAttr.setAttrs(attrs);
        instructionRepository.instructionAttrPropertyUpdate(tenantId, extendAttr);

        // 指令实际创建
        MtInstructionActualVO actualCreate = new MtInstructionActualVO();
        actualCreate.setInstructionId(instruction.getInstructionId());
        actualCreate.setEventId(eventId);
        actualCreate.setActualQty(0.0);
        actualCreate.setMaterialId(line.getMaterialId());
        actualCreate.setTenantId(tenantId);
        actualCreate.setFromLocatorId(toLocatorId);
        actualCreate.setToLocatorId(toLocatorId);
        actualCreate.setBusinessType(DISTRIBUTION_EXECUTE);
        actualCreate.setInstructionType(TRANSFER_OVER_LOCATOR);
        actualCreate.setUomId(line.getUomId());
        actualCreate.setFromSiteId(siteId);
        actualCreate.setSourceOrderType(DISTRIBUTION_DOC);
        actualCreate.setSourceOrderId(instructionDocId);
        instructionActualRepository.instructionActualUpdate(tenantId, actualCreate);

        return instruction;
    }
}
