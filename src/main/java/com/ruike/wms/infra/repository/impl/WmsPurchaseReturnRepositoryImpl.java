package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.repository.WmsPurchaseReturnRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsPurchaseOrderMapper;
import com.ruike.wms.infra.mapper.WmsPurchaseReturnMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 采购退货平台
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/9 13:43
 */
@Component
@Slf4j
public class WmsPurchaseReturnRepositoryImpl implements WmsPurchaseReturnRepository {

    @Autowired
    private WmsPurchaseReturnMapper wmsPurchaseReturnMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtUserClient mtUserClient;

    @Override
    @ProcessLovValue
    public Page<WmsPurchaseReturnHeadVO> purchaseReturnHeaderQuery(Long tenantId, PageRequest pageRequest, WmsPurchaseReturnVO wmsPurchaseReturnVO) {
        List<LovValueDTO> poTypeList = lovAdapter.queryLovValue("WMS.PO.TYPE", tenantId);
        List<String> docTypeList = poTypeList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        wmsPurchaseReturnVO.setDocTypeList(docTypeList);
        Page<WmsPurchaseReturnHeadVO> pageObj = PageHelper.doPage(pageRequest, () -> wmsPurchaseReturnMapper.purchaseReturnHeaderQuery(tenantId, wmsPurchaseReturnVO));
        List<Long> userIdList = pageObj.getContent().stream().map(WmsPurchaseReturnHeadVO::getLastUpdatedBy).filter(Objects::nonNull).distinct().map(dto -> {
            return Long.valueOf(dto);
        }).collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
        for (WmsPurchaseReturnHeadVO wmsPurchaseReturnHeadVO : pageObj.getContent()) {
            if (StringUtils.isNotBlank(wmsPurchaseReturnHeadVO.getLastUpdatedBy())) {
                MtUserInfo mtUserInfo = userInfoMap.get(Long.valueOf(wmsPurchaseReturnHeadVO.getLastUpdatedBy()));
                wmsPurchaseReturnHeadVO.setLastUpdatedByName(mtUserInfo != null ? mtUserInfo.getRealName() : "");
            }
        }
        return pageObj;
    }

    @Override
    @ProcessLovValue
    public Page<WmsPurchaseReturnLineVO> purchaseReturnLineQuery(Long tenantId, PageRequest pageRequest, String sourceDocId) {
        return PageHelper.doPage(pageRequest, () -> wmsPurchaseReturnMapper.purchaseReturnLineQuery(tenantId, sourceDocId));
    }

    @Override
    public Page<WmsPurchaseReturnDetailsVO> purchaseReturnDetailsQuery(Long tenantId, PageRequest pageRequest, String instructionId) {
        Page<WmsPurchaseReturnDetailsVO> pageObj = PageHelper.doPage(pageRequest, () -> wmsPurchaseReturnMapper.purchaseReturnDetailsQuery(tenantId, instructionId));
        List<Long> userIdList = pageObj.getContent().stream().map(WmsPurchaseReturnDetailsVO::getLastUpdatedBy).filter(Objects::nonNull).distinct().map(dto -> {
            return Long.valueOf(dto);
        }).collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
        for (WmsPurchaseReturnDetailsVO wmsPurchaseReturnDetailsVO : pageObj.getContent()) {
            if (StringUtils.isNotBlank(wmsPurchaseReturnDetailsVO.getLastUpdatedBy())) {
                MtUserInfo mtUserInfo = userInfoMap.get(Long.valueOf(wmsPurchaseReturnDetailsVO.getLastUpdatedBy()));
                wmsPurchaseReturnDetailsVO.setLastUpdatedByName(mtUserInfo != null ? mtUserInfo.getRealName() : "");
            }
        }
        return pageObj;
    }

    @Override
    public void multiplePrint(Long tenantId, List<String> instructionDocIdList, HttpServletResponse response) {
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
        String barcodePath = "";
        String qrcodePath = "";
        String content = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //定义每页可以打印的行上限
        Long lineCount = 10L;
        //循环需要打印的数据，单个打印
        for (String instructionDocId : instructionDocIdList) {
            //获取头数据
            List<LovValueDTO> poTypeList = lovAdapter.queryLovValue("WMS.PO.TYPE", tenantId);
            List<String> docTypeList = poTypeList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            WmsPurchaseReturnHeadVO headDto = wmsPurchaseReturnMapper.selectPurchaseReturnPrintHead(tenantId, instructionDocId, docTypeList);
            //单据类型
            List<LovValueDTO> list = lovAdapter.queryLovValue("WMS.PO.TYPE", tenantId);
            List<LovValueDTO> typeList = list.stream().filter(dto -> StringUtils.equals(dto.getValue(), headDto.getInstructionDocType())).collect(toList());
            if (CollectionUtils.isNotEmpty(typeList)) {
                headDto.setInstructionDocTypeMeaning(typeList.get(0).getMeaning());
            }

            //生成二维码
            String codeUuid = UUID.randomUUID().toString();
            qrcodePath = basePath + "/" + codeUuid + "_" + docNumber + "_qrcode.png";
            File qrcodeImageFile = new File(qrcodePath);
            barcodeImageFileList.add(qrcodeImageFile);
            content = headDto.getInstructionDocNum();
            try {
                CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
                log.info("<====生成二维码完成！{}", qrcodePath);
            } catch (Exception e) {
                log.error("<==== WmsPurchaseReturnRepositoryImpl.multiplePrint.encode Error", e);
                throw new MtException(e.getMessage());
            }

            //组装参数
            Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            imgMap.put("barcodeImage", qrcodePath);
            Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

            //获取行数据
            Boolean printflag = false;
            Long currentLine = 0L;

            List<WmsPurchaseReturnLineVO> lineList = wmsPurchaseReturnMapper.purchaseReturnLineQuery(tenantId, instructionDocId);

            if (CollectionUtils.isNotEmpty(lineList)) {
                for (WmsPurchaseReturnLineVO wmsPurchaseReturnLineVO : lineList) {
                    formMap.put("LineNum" + currentLine.toString(), wmsPurchaseReturnLineVO.getLineNumber());
                    formMap.put("ItemCode" + currentLine.toString(), wmsPurchaseReturnLineVO.getMaterialCode());
                    formMap.put("ItemDesc" + currentLine.toString(), wmsPurchaseReturnLineVO.getMaterialName());
                    formMap.put("Qty" + currentLine.toString(), wmsPurchaseReturnLineVO.getQuantity().stripTrailingZeros().toPlainString());
                    formMap.put("Uom" + currentLine.toString(), wmsPurchaseReturnLineVO.getUomCode());
                    formMap.put("warehouse" + currentLine.toString(), wmsPurchaseReturnLineVO.getLocatorCode());
                    formMap.put("remark" + currentLine.toString(), wmsPurchaseReturnLineVO.getRemark());
                    currentLine += 1L;
                    printflag = false;
                    if (currentLine % lineCount == 0) {
                        formMap.put("instructionDocNum", headDto.getInstructionDocNum());
                        formMap.put("instructionType", headDto.getInstructionDocTypeMeaning());
                        formMap.put("siteName", headDto.getSupplierName());
                        formMap.put("remark", headDto.getRemark());
                        formMap.put("createdBy", "");
                        Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                        param.put("formMap", formMap);
                        param.put("imgMap", imgMap);
                        dataList.add(param);
                        currentLine = 0L;
                        formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                        printflag = true;
                    }
                }
                if (!printflag) {
                    formMap.put("instructionDocNum", headDto.getInstructionDocNum());
                    formMap.put("instructionType", headDto.getInstructionDocTypeMeaning());
                    formMap.put("siteName", headDto.getSupplierName());
                    formMap.put("remark", headDto.getRemark());
                    formMap.put("createdBy", "");
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                }
            }
        }

        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/hme_purchase_return_template.pdf", pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== WmsDeliveryDocServiceImpl.multiplePrint.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

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
        } catch (
                Exception e) {
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
        for (
                File file : barcodeImageFileList
        ) {
            if (!file.delete()) {
                log.info("<==== WmsDeliveryDocServiceImpl.multiplePrint.barcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== WmsDeliveryDocServiceImpl.multiplePrint.pdfFile Failed: {}", barcodePath);
        }
    }
}
