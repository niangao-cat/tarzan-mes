package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.wms.app.service.WmsProductReceiptService;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsProductReceiptMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO10;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/10 10:33
 */
@Slf4j
@Service
public class WmsProductReceiptServiceImpl implements WmsProductReceiptService {

    @Autowired
    private WmsProductReceiptMapper wmsProductReceiptMapper;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtUserClient userClient;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    @ProcessLovValue
    public Page<WmsReceiptDocVO> receiptDocQuery(Long tenantId, WmsReceiptDocReqVO reqVO, PageRequest pageRequest) {
        if(StringUtils.isBlank(reqVO.getSiteId())){
            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
            reqVO.setSiteId(defaultSiteId);
        }
        return PageHelper.doPage(pageRequest, () ->wmsProductReceiptMapper.receiptDocQuery(tenantId, reqVO));
    }

    @Override
    @ProcessLovValue
    public Page<WmsReceiptLineVO> receiptDocLineQuery(Long tenantId, WmsReceiptDocVO docVO, PageRequest pageRequest) {
        //调用{ propertyLimitInstructionQuery }获取出货单行
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(docVO.getInstructionDocId());
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        if(CollectionUtils.isEmpty(instructionIdList)){
            return new Page<WmsReceiptLineVO>(Collections.EMPTY_LIST, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), 0);
        }
        return PageHelper.doPage(pageRequest, () ->wmsProductReceiptMapper.receiptDocLineQuery(tenantId, instructionIdList));
    }

    @Override
    @ProcessLovValue
    public Page<WmsReceiptDetailVO> receiptDocLineDetail(Long tenantId, WmsReceiptDetailReqVO docVO, PageRequest pageRequest) {
        if(CollectionUtils.isEmpty(docVO.getInstructionIdList())){
            return new Page<WmsReceiptDetailVO>(Collections.EMPTY_LIST, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), 0);
        }

        //手动分页处理
        List<WmsReceiptDetailVO> wmsReceiptDetailVOList = wmsProductReceiptMapper.receiptDocLineDetail(tenantId, docVO);

        Map<String, List<WmsReceiptDetailVO>> detailMap = wmsReceiptDetailVOList.stream().collect(Collectors.groupingBy(qmsInvoiceLineReturnDTO ->
                qmsInvoiceLineReturnDTO.getMaterialLotCode() + "_" + qmsInvoiceLineReturnDTO.getInstructionId()));

        List<WmsReceiptDetailVO> detailList = new ArrayList<>();

        for (String materialLotCode : detailMap.keySet()) {
            if (StringUtils.isNotBlank(materialLotCode)) {
                List<WmsReceiptDetailVO> groupedDetailList = detailMap.get(materialLotCode);
                if(CollectionUtils.isNotEmpty(groupedDetailList)){
                    WmsReceiptDetailVO detailVO = new WmsReceiptDetailVO();
                    if (groupedDetailList.size() > 1) {
                        //优先取实绩的
                        for (WmsReceiptDetailVO wmsReceiptDetailVO : groupedDetailList) {
                            if(StringUtils.equals(wmsReceiptDetailVO.getFlag(), "1")){
                                detailVO = wmsReceiptDetailVO;
                                break;
                            }
                        }
                    }else {
                        detailVO = groupedDetailList.get(0);
                    }

                    //执行人
                    if(detailVO.getExecutedBy() != null){
                        MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, Long.valueOf(detailVO.getExecutedBy()));
                        if(mtUserInfo != null){
                            detailVO.setExecutedByName(mtUserInfo.getRealName());
                        }
                    }
                    if(StringUtils.isBlank(detailVO.getLineNum())){
                        detailVO.setLineNum("");
                    }
                    detailList.add(detailVO);
                }
            }
        }
        //行号排序
        detailList = detailList.stream().sorted(Comparator.comparing(WmsReceiptDetailVO::getLineNum)).collect(Collectors.toList());
        return new Page<WmsReceiptDetailVO>(detailList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), detailList.size());
    }

    @Override
    public void multiplePrint(Long tenantId, List<String> instructionDocIds, HttpServletResponse response) {
        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if(!file.mkdir()){
                    throw new MtException("创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }

        String docNumber = "";
        String uuid = UUID.randomUUID().toString();
        String barcodePath = "";
        String qrcodePath = "";
        String content = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> barcodeImageFileList = new ArrayList<File>();
        //当前登录用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        String realName = curUser.getRealName();


        //定义每页可以打印的行上限
        Long lineCount = 10L;
        //循环需要打印的数据，单个打印
        for(String instructionDocId:instructionDocIds) {
            //获取头数据
            WmsReceiptDocVO headDto = wmsProductReceiptMapper.receiptDocPrintQuery(tenantId,instructionDocId);
            //headDto.setCreatedBy(realName);

            //生成二维码
            String codeUuid = UUID.randomUUID().toString();
            qrcodePath = basePath + "/" + codeUuid + "_" + docNumber + "_qrcode.png";
            File qrcodeImageFile = new File(qrcodePath);
            barcodeImageFileList.add(qrcodeImageFile);
            content = headDto.getReceiptDocNum();
            try {
                CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
                log.info("<====生成二维码完成！{}", qrcodePath);
            } catch (Exception e) {
                log.error("<==== WmsProductReceiptServiceImpl.multiplePrint.encode Error", e);
                throw new MtException(e.getMessage());
            }

            //组装参数
            Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            imgMap.put("barcodeImage", qrcodePath);
            Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

            //获取行数据
            Boolean printflag = false;
            Long currentLine = 0L;
            List<WmsReceiptLinePrintVO> lineList = wmsProductReceiptMapper.receiptLinePrintQuery(tenantId, instructionDocId);
            for(WmsReceiptLinePrintVO lineDto:lineList){
                formMap.put("LineNum"+currentLine.toString(), lineDto.getLineNum());
                formMap.put("ItemCode"+currentLine.toString(), lineDto.getMaterialCode());
                formMap.put("ItemDesc"+currentLine.toString(), lineDto.getMaterialName());
                formMap.put("Qty"+currentLine.toString(), lineDto.getReceiptQty());
                formMap.put("Version"+currentLine.toString(), lineDto.getMaterialVersion());
                formMap.put("Uom"+currentLine.toString(), lineDto.getUom());
                //formMap.put("Warehouse"+currentLine.toString(), lineDto.getTargetWarehouse());
                formMap.put("InWarehouse" + currentLine.toString(), lineDto.getTargetWarehouse());
                formMap.put("ContainerQty" + currentLine.toString(), lineDto.getContainerQty());
                //formMap.put("OldItemCode"+currentLine.toString(), lineDto.getOldItemCode());
                formMap.put("remark"+currentLine.toString(), lineDto.getRemark());
                currentLine +=1L;
                printflag = false;
                if(currentLine%lineCount == 0){
                    formMap.put("instructionDocNum", headDto.getReceiptDocNum());
                    formMap.put("siteName", headDto.getSiteName());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    formMap.put("date", sdf.format(new Date()));
                    formMap.put("remark", headDto.getRemark());
                    formMap.put("createdBy", realName);

                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    currentLine = 0L;
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    printflag = true;
                }
            }
            if(!printflag) {
                formMap.put("instructionDocNum", headDto.getReceiptDocNum());
                formMap.put("siteName", headDto.getSiteName());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                formMap.put("date", sdf.format(new Date()));
                formMap.put("remark", headDto.getRemark());
                formMap.put("createdBy", realName);

                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
            }
        }
        if(dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/wms_product_receipt_template.pdf", pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== WmsProductReceiptServiceImpl.multiplePrint.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //将文件转化成流进行输出
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try{
            //设置相应参数
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition","attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if(org.apache.commons.lang.StringUtils.isNotEmpty(encoding)){
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
            log.error("<==== WmsProductReceiptServiceImpl.multiplePrint.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null){
                    bis.close();
                }
                if (bos != null){
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== WmsProductReceiptServiceImpl.multiplePrint.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File file:barcodeImageFileList
                ) {
            if(!file.delete()){
                log.info("<==== WmsProductReceiptServiceImpl.multiplePrint.barcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if(!pdfFile.delete()){
            log.info("<==== WmsProductReceiptServiceImpl.multiplePrint.pdfFile Failed: {}", barcodePath);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retractReceiptDoc(Long tenantId, List<String> instructionDocIds) {
        if (CollectionUtils.isNotEmpty(instructionDocIds)) {
            // 创建请求事件
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCT_RECEIPT_CANCEL");
            // 创建事件
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventRequestId(eventRequestId);
            mtEventCreateVO.setEventTypeCode("PRODUCT_RECEIPT_CANCEL");
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

            for (String instructionDocId : instructionDocIds) {
                mtInstructionDocRepository.instructionDocCancel(tenantId, instructionDocId, eventRequestId);
            }
            // 根据单据找到所有条码
            List<String> materialLotIdList = wmsProductReceiptMapper.queryMaterialLotIdList(tenantId, instructionDocIds);
            // 执行【单据撤销】后，调用api批量更新条码状态扩展字段为【COMPLETED】
            if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
                for (String materialLotId : materialLotIdList) {
                    MtCommonExtendVO6 commonExtendVO6 = new MtCommonExtendVO6();
                    commonExtendVO6.setKeyId(materialLotId);
                    List<MtCommonExtendVO5> attrs = new ArrayList<>();
                    MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("STATUS");
                    mtCommonExtendVO5.setAttrValue("COMPLETED");
                    attrs.add(mtCommonExtendVO5);
                    commonExtendVO6.setAttrs(attrs);
                    attrPropertyList.add(commonExtendVO6);
                }
                mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
            }
        }
    }
}
