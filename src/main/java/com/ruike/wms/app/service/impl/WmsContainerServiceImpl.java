package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsContainerService;
import com.ruike.wms.infra.barcode.CommonBarcodeUtil;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsContainerMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname ContainerServiceImpl
 * @Description 物流器具功能
 * @Date 2019/9/16 14:58
 * @Created by admin
 */
@Slf4j
@Service
public class WmsContainerServiceImpl implements WmsContainerService {

    @Autowired
    private MtContainerRepository repository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private WmsCommonServiceComponent commonServiceComponent;

    @Autowired
    private WmsContainerMapper containerMapper;

    @Autowired
    private MtModLocatorOrgRelRepository modLocatorOrgRelRepository;

    @Autowired
    private MtContainerLoadDetailRepository containerLoadDetailRepository;

    @Autowired
    private WmsCommonApiService commonApiService;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String containerCreate(Long tenantId, WmsContainerDTO dto) {
        //2020/7/29 sanfeng.zhang 批量创建物流器具
        if (dto.getBatchNum() == null) {
            dto.setBatchNum(1L);
        }


        String eventId = commonServiceComponent.generateEvent(tenantId, WmsConstant.EVENT_TYPE_CONTAINER);

        MtNumrangeVO2 numrangeVO2 = new MtNumrangeVO2();
        numrangeVO2.setObjectCode(WmsConstant.EVENT_CONTAINER_CODE);
        //by junhui.liu
        MtContainerType mtContainerType = mtContainerTypeRepository.containerTypePropertyGet(tenantId, dto.getContainerTypeId());
        Map<String, String> callObjectCodeList = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        callObjectCodeList.put("containerTypeCode", mtContainerType.getContainerTypeCode());
        numrangeVO2.setCallObjectCodeList(callObjectCodeList);
        // by liyuan.lv, 号码段生成指定站点ID
        numrangeVO2.setSiteId(dto.getSiteId());

        String endContainerId = "";
        for (int index = 0; index < dto.getBatchNum(); index++) {
            MtNumrangeVO5 numrangeVO5 = commonServiceComponent.generateNumber(tenantId, numrangeVO2);
            if (numrangeVO5 == null) {
                throw new MtException("MT_GENERAL_0020", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_GENERAL_0020", "GENERAL"));
            }
            Long cid = Long.valueOf(customSequence.getNextKey("mt_container_cid_s"));

            MtContainerVO12 dto1 = new MtContainerVO12();
            BeanUtils.copyProperties(dto, dto1);
            dto1.setContainerCode(numrangeVO5.getNumber());
            dto1.setStatus("CANRELEASE");
            dto1.setEventId(eventId);

            MtContainerVO26 mtContainerVO26 = repository.containerUpdate(tenantId, dto1, HmeConstants.ConstantValue.YES);
            String containerId = mtContainerVO26.getContainerId();
            endContainerId = containerId;
            if (!StringUtils.isEmpty(dto.getCreateReason())) {
                MtContainerAttrVO3 attrVO3 = new MtContainerAttrVO3();
                attrVO3.setContainerId(containerId);
                attrVO3.setEventId(eventId);

                List<MtExtendVO5> attr = new ArrayList<>();
                MtExtendVO5 extendVO5 = new MtExtendVO5();
                extendVO5.setAttrName("CREATE_REASON");
                extendVO5.setAttrValue(dto.getCreateReason());
                attr.add(extendVO5);
                attrVO3.setAttr(attr);

                repository.containerLimitAttrUpdate(tenantId, attrVO3);
            }

        }
        return endContainerId;
    }

    @Override
    @ProcessLovValue
    public Page<WmsContainerResultDTO> containerHeaderQuery(PageRequest pageRequest, WmsContainerQryDTO containerQryDTO) {
        return PageHelper
                .doPageAndSort(pageRequest, () -> containerMapper.containerHeaderQuery(containerQryDTO));
    }

    @Override
    @ProcessLovValue
    public Page<WmsContainerLineResultDTO> containerLineQuery(PageRequest pageRequest, WmsContainerQryDTO containerQryDTO) {
        Page<WmsContainerLineResultDTO> containerResultES = PageHelper
                .doPageAndSort(pageRequest, () -> containerMapper.containerLineQuery(containerQryDTO));
        List<WmsContainerLineResultDTO> resultList = containerResultES.getContent();
        for (WmsContainerLineResultDTO cl : resultList) {
            if (!StringUtils.isEmpty(cl.getLoadObjectCode())) {
                if (cl.getLoadObjectCode().indexOf("!") > 0) {
                    String[] code = cl.getLoadObjectCode().split("!");
                    cl.setLoadObjectCode(code[0]);
                    if (code.length > 1) {
                        cl.setLot(code[1]);
                    }

                }
            }

            if (!StringUtils.isEmpty(cl.getCodeName())) {
                if (cl.getCodeName().indexOf("!") > 0) {
                    String[] codes = cl.getCodeName().split("!");
                    cl.setMaterialCode(codes[0]);
                    cl.setMaterialName(codes[1]);
                    if (codes.length == 3) {
                        cl.setUom(codes[2]);
                    }
                }
            }
        }

        return containerResultES;
    }

    @Override
    @ProcessLovValue
    public Page<WmsContainerHisResultDTO> containerHeaderHis(PageRequest pageRequest, WmsContainerHisQryDTO containerHisQryDTO) {

        return PageHelper
                .doPageAndSort(pageRequest, () -> containerMapper.containerHeaderHis(containerHisQryDTO));
    }

    @Override
    public Page<WmsContainerLineHisResultDTO> containerLineHis(PageRequest pageRequest, WmsContainerHisQryDTO containerHisQryDTO) {
        Page<WmsContainerLineHisResultDTO> page = PageHelper
                .doPageAndSort(pageRequest, () -> containerMapper.containerLineHis(containerHisQryDTO));
        for (WmsContainerLineHisResultDTO clh : page.getContent()) {
            if (!StringUtils.isEmpty(clh.getCodeName())) {
                if (clh.getCodeName().indexOf("!") > 0) {
                    String[] codes = clh.getCodeName().split("!");
                    clh.setMaterialCode(codes[0]);
                    clh.setMaterialName(codes[1]);
                }
            }
        }

        return page;
    }

    /**
     * @param tenantId
     * @param containerCode
     * @return com.superlighting.hwms.api.controller.dto.ZContainerInfoResultDTO
     * @Description 物流器具条码查询
     * @Date 2019/10/23 9:40
     * @user
     */
    @Override
    @ProcessLovValue(targetField = {"", "details", "details.objectType"})
    public WmsContainerInfoResultDTO containerCodeQuery(Long tenantId, String containerCode) {

        MtContainerVO13 containerVO = new MtContainerVO13();
        containerVO.setContainerCode(containerCode);
        //调用【propertyLimitContainerQuery】，有返回值继续下一 步，无返回值，系统报错
        List<String> list = repository.propertyLimitContainerQuery(tenantId, containerVO);
        log.info("<=====api propertyLimitContainerQuery size {}", list.size());
        if (CollectionUtils.isEmpty(list)) {
            throw new MtException("WMS_CONTAINER_001", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "WMS_CONTAINER_001", "WMS", containerCode));
        }
        //容器id
        String containerId = list.get(0);
        //查询容器基本信息
        WmsContainerInfoResultDTO resultDTO = containerMapper.containnerCodeQuery(tenantId, containerId);
        log.info("<=====api propertyLimitContainerQuery containerId {}", containerId);
        try {
            //
            repository.containerAvailableValidate(tenantId, containerId);
            resultDTO.setEnableFlag(true);
        } catch (MtException e) {
            log.error(" ContainerServiceImpl-containnerCodeQuery error: {}:{}", e.getMessage(), e);
            resultDTO.setEnableFlag(false);
            return resultDTO;
        }

        List<WmsContainerDetailResultDTO> detailList = this.getContainerDetail(tenantId, containerId);

        resultDTO.setDetails(detailList);
        return resultDTO;
    }

    /**
     * @param tenantId
     * @param type
     * @param materialLotCodeList
     * @param response
     * @Description 打印
     * @Date 2020/09/22 14:36
     * @user yaoyapeng
     */
    @Override
    public void containerCodePrint(Long tenantId, String type, List<String> materialLotCodeList, HttpServletResponse response) {
        //获取数据
        if (CollectionUtils.isEmpty(materialLotCodeList)) {
            return;
        }

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
        String uuid = UUID.randomUUID().toString();
        String barcodePath = "";
        String qrcodePath = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        String pdfName = "";
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //组装参数
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

        for (int i = 0; i < materialLotCodeList.size(); i++) {
            String materialLotCode = materialLotCodeList.get(i);
            if (StringUtils.equals(type, "1")) {
                // 条形码
                barcodePath = getBarcodePath(basePath, uuid, materialLotCode, barcodeImageFileList);
                imgMap.put("barcodeImage", barcodePath);
                pdfName = "/wms_container_print_barcode_template.pdf";
                formMap.put("eoNum", materialLotCode);

                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            } else if (StringUtils.equals(type, "2")) {
                // 二维码
                qrcodePath = getQrcodePath(basePath, uuid, materialLotCode, barcodeImageFileList);
                imgMap.put("qrcodeImage", qrcodePath);
                pdfName = "/wms_container_print_qrcode_template.pdf";
                formMap.put("materialLotCode", materialLotCode);
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            }
        }
        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + pdfName, pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== WmsContainerServiceImpl.containerCodePrint.multiplePage Error", e);
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
        } catch (Exception e) {
            log.error("<==== WmsContainerServiceImpl.containerCodePrint.outputPDFFile Error", e);
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
                log.error("<==== WmsContainerServiceImpl.containerCodePrint.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File file : barcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== WmsContainerServiceImpl.containerCodePrint.deleteBarcodeImageFile Failed: {}", barcodePath);
            }
        }
        for (File file : qrcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== WmsContainerServiceImpl.containerCodePrint.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }

        if (!pdfFile.delete()) {
            log.info("<==== WmsContainerServiceImpl.containerCodePrint.pdfFile Failed: {}", barcodePath);
        }
    }

    /**
     * 生成二维码
     *
     * @param basePath
     * @param uuid
     * @param materialLotCode
     * @param barcodeImageFileList
     * @return
     */
    private String getQrcodePath(String basePath, String uuid, String materialLotCode, List<File> barcodeImageFileList) {
        String qrcodePath = basePath + "/" + uuid + "_" + materialLotCode + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        barcodeImageFileList.add(qrcodeImageFile);
        try {
            CommonQRCodeUtil.encode(materialLotCode, qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        return qrcodePath;
    }

    /**
     * 生成条形码
     *
     * @param basePath
     * @param uuid
     * @param materialLotCode
     * @param barcodeImageFileList
     * @return
     */
    private String getBarcodePath(String basePath, String uuid, String materialLotCode, List<File> barcodeImageFileList) {
        // 生成条形码
        String barcodePath = basePath + "/" + uuid + "_" + materialLotCode + "_barcode.png";
        File barcodeImageFile = new File(barcodePath);
        barcodeImageFileList.add(barcodeImageFile);
        try {
            CommonBarcodeUtil.generateCode128ToFile(materialLotCode, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====生成条形码完成！{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }

        return barcodePath;
    }

    /**
     * @param tenantId
     * @param containerId 容器id
     * @return java.util.List<com.ruike.wms.api.controller.dto.WmsContainerDetailResultDTO>
     * @Description 获取容器明细信息
     * @Date 2019/10/23 9:41
     * @user
     */
    @ProcessLovValue()
    private List<WmsContainerDetailResultDTO> getContainerDetail(Long tenantId, String containerId) {
        List<WmsContainerDetailResultDTO> detailList = new ArrayList<>();
        //调用api获取容器
        MtContLoadDtlVO contLoad = new MtContLoadDtlVO();
        contLoad.setContainerId(containerId);
        contLoad.setAllLevelFlag(WmsConstant.CONSTANT_N);
        List<MtContLoadDtlVO6> loadDtlVO6sList = containerLoadDetailRepository.containerLimitObjectQuery(tenantId, contLoad);
        //容器明细列表
        WmsContainerDetailResultDTO containerDetailResultDTO = null;
        for (MtContLoadDtlVO6 loadDtlVO6s : loadDtlVO6sList) {
            containerDetailResultDTO = new WmsContainerDetailResultDTO();
            log.info("<=====api propertyLimitContainerQuery loadDtlVO6s id {}", loadDtlVO6s.getLoadObjectId());
            log.info("<=====api propertyLimitContainerQuery loadDtlVO6s type {}", loadDtlVO6s.getLoadObjectType());
            //类型为material_lot时，查询mt_material_lot
            //类型为container 时，查询mt_container
            if ("MATERIAL_LOT".equals(loadDtlVO6s.getLoadObjectType())) {
                WmsContainerDetailResultDTO materialLotInfo = containerMapper.getMaterialLotInfoByLotId(tenantId, loadDtlVO6s.getLoadObjectId());
                if (materialLotInfo != null) {
                    BeanUtils.copyProperties(materialLotInfo, containerDetailResultDTO);
                }
                containerDetailResultDTO.setObjectType("material_lot");
            } else if ("EO".equals(loadDtlVO6s.getLoadObjectType())) {
                containerDetailResultDTO.setObjectType("EO");
            } else {
                String mtContainerCode = containerMapper.getContainerCodeById(tenantId, containerId);
                MtContLoadDtlVO contLoad2 = new MtContLoadDtlVO();
                contLoad2.setContainerId(loadDtlVO6s.getLoadObjectId());
                contLoad2.setAllLevelFlag(WmsConstant.CONSTANT_N);
                //如果是容器，查询容器下面的物料批信息，如果物料批id相同，则累加物料数量，显示到前台
                List<MtContLoadDtlVO6> list = containerLoadDetailRepository.containerLimitObjectQuery(tenantId, contLoad2);
                String materialId = "";
                Double qry = 0.0D;
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        materialId = list.get(0).getLoadObjectId();
                    }
                    if (!materialId.equals(list.get(i))) {
                        if ("MATERIAL_LOT".equals(list.get(i).getLoadObjectType())) {
                            WmsContainerDetailResultDTO materialLot = containerMapper.getMaterialLotInfoById(tenantId, list.get(i).getLoadObjectId());
                            qry += Double.valueOf(materialLot.getQuantity());
                            if (materialLot != null) {
                                BeanUtils.copyProperties(materialLot, containerDetailResultDTO);
                            }
                        }
                    } else {
                        break;
                    }
                }
                containerDetailResultDTO.setQuantity(qry.toString());
                containerDetailResultDTO.setObjectType("container");
                //containerDetailResultDTO.setObjectCode(mtContainerCode);
            }
            log.info("<=====containerDetailResultDTO {}", containerDetailResultDTO);
            detailList.add(containerDetailResultDTO);
        }

        return detailList;
    }
}
