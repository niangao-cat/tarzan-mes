package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfInstructionITEMSyncDTO;
import com.ruike.itf.api.dto.ItfInstructionRESBSyncDTO;
import com.ruike.itf.api.dto.ItfInstructionReturnDTO;
import com.ruike.itf.api.dto.ItfInstructionSyncDTO;
import com.ruike.itf.app.service.ItfInstructionDocIfaceService;
import com.ruike.itf.domain.entity.ItfInstructionAttr;
import com.ruike.itf.domain.entity.ItfInstructionDocAttr;
import com.ruike.itf.domain.entity.ItfInstructionDocIface;
import com.ruike.itf.domain.entity.ItfInstructionIface;
import com.ruike.itf.infra.mapper.*;
import com.ruike.itf.utils.GetDeclaredFields;
import com.ruike.itf.utils.Utils;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import liquibase.structure.DatabaseObjectCollection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.infra.mapper.MtSitePlantReleationMapper;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.material.infra.mapper.MtUomMapper;
import tarzan.modeling.infra.mapper.MtModLocatorMapper;
import tarzan.modeling.infra.mapper.MtSupplierMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 指令单据头表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-08-11 11:24:54
 */
@Service
public class ItfInstructionDocIfaceServiceImpl implements ItfInstructionDocIfaceService {

    @Autowired
    private ItfInstructionDocIfaceMapper itfInstructionDocIfaceMapper;

    @Autowired
    private ItfInstructionIfaceMapper itfInstructionIfaceMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private ItfWorkOrderIfaceMapper itfWorkOrderIfaceMapper;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    @Autowired
    private ItfInstructionAttrMapper itfInstructionAttrMapper;

    @Autowired
    private ItfInstructionDocAttrMapper itfInstructionDocAttrMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtInstructionDocMapper mtInstructionDocMapper;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    /**
     * 采购订单同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/11 20:51
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ItfInstructionDocIface> invoke(ItfInstructionSyncDTO dto) {
        if (CollectionUtils.isEmpty(dto.getITEM())) {
            return new ArrayList<>();
        }
        // 返回信息
        // 由SAP数据转换为MT数据，更换DTO集合
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = Utils.getNowDate();
        String batchDate = format.format(nowDate);// 批次时间
        // 批次
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("itf_instruction_iface_cid_s"));
        List<ItfInstructionITEMSyncDTO> item = dto.getITEM();// 采购订单头行
        List<ItfInstructionDocIface> docIfaces = new ArrayList<>();
        // 循环插入头行数据，并且判断是否为空
        GetDeclaredFields<ItfInstructionITEMSyncDTO> head = new GetDeclaredFields();
        String[] headFields = {"PO_NUMBER", "DOC_TYPE", "VENDOR", "PO_ITEM", "PLANT", "MATERIAL",
                "LMEIN", "STORE_LOC", "LAGMG", "EINDT", "ITEM_CAT"};
        for (int i = 0; i < item.size(); i++) {
            // 判断是否有为空的字段
            List<String> declaredFields = head.getDeclaredFields(item.get(i), headFields);
            // 插入记录表
            String sdocItem = item.get(i).getSDOC_ITEM();
            if ("0".equals(sdocItem)) {
                item.get(i).setSDOC_ITEM(null);
            }
            ItfInstructionDocIface itfInstructionDocIface = new ItfInstructionDocIface(item.get(i), tenantId, batchId, batchDate, CollectionUtils.isEmpty(declaredFields) ? "" : declaredFields.toString());
            docIfaces.add(itfInstructionDocIface);

        }
        // 插入明细数据
        List<ItfInstructionIface> itfInstructionIfaces = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getRESB())) {
            // 删除批次
            List<ItfInstructionRESBSyncDTO> resb = dto.getRESB(); // 采购订单明细
            // 循环插入明细数据，并且判断是否有空值
            String[] lineFields = {"PO_NUMBER", "PO_ITEM", "RSPOS", "WERKS", "MATNR", "MEINS", "BDMNG", "RSNUM"};
            GetDeclaredFields<ItfInstructionRESBSyncDTO> line = new GetDeclaredFields();
            for (int i = 0; i < resb.size(); i++) {
                List<String> declaredFields = line.getDeclaredFields(resb.get(i), lineFields);


                ItfInstructionIface itfInstructionDocIface = new ItfInstructionIface(resb.get(i), tenantId, batchId, batchDate, CollectionUtils.isEmpty(declaredFields) ? "" : declaredFields.toString());

                itfInstructionIfaceMapper.insertSelective(itfInstructionDocIface);
                // 行信息在此位置不做判断，根据头信息判断是否存储到业务表
                if (Strings.isEmpty(itfInstructionDocIface.getErrorMsg())) {
                    itfInstructionIfaces.add(itfInstructionDocIface);
                }
            }
        }
        // 插入MT表数据
        List<ItfInstructionDocIface> headData = docIfaces.stream().filter(a -> Strings.isEmpty(a.getErrorMsg())).collect(Collectors.toList());
        List<ItfInstructionDocIface> errorHeadData = docIfaces.stream().filter(a -> Strings.isNotEmpty(a.getErrorMsg())).collect(Collectors.toList());
        List<ItfInstructionIface> errorLineData = itfInstructionIfaces.stream().filter(a -> Strings.isNotEmpty(a.getErrorMsg())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(errorHeadData) && CollectionUtils.isEmpty(errorLineData)) {
            List<ItfInstructionDocIface> insertErrorList = insertMtTableData(headData, itfInstructionIfaces);
            // 整理全部数据
            for (ItfInstructionDocIface errorList : insertErrorList) {
                for (int i = 0; i < docIfaces.size(); i++) {
                    if (errorList.getInstructionDocNum().equals(docIfaces.get(i).getInstructionDocNum())) {
                        docIfaces.get(i).setErrorMsg(errorList.getErrorMsg());
                    }
                }
            }
        }
        for (ItfInstructionDocIface iface : docIfaces) {
            itfInstructionDocIfaceMapper.insertSelective(iface);
        }

        List<ItfInstructionDocIface> errorList = docIfaces.stream().filter(a -> Strings.isNotEmpty(a.getErrorMsg())).collect(Collectors.toList());
        return errorList;
    }

    /**
     * 插入MT表数据
     *
     * @param docIfaces         头行数据
     * @param instructionIfaces 明细数据
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/12 17:36
     */
    public List<ItfInstructionDocIface> insertMtTableData(List<ItfInstructionDocIface> docIfaces, List<ItfInstructionIface> instructionIfaces) {
        List<ItfInstructionDocIface> returnDTOS = new ArrayList<>();
        String siteSql = "SELECT SITE_ID from mt_site_plant_releation where SITE_TYPE = 'MANUFACTURING' AND PLANT_CODE = 'value' AND TENANT_ID = '" + tenantId + "'";
        String materialSql = "SELECT MATERIAL_ID from mt_material where MATERIAL_CODE = 'value' AND TENANT_ID = '" + tenantId + "'";
        String uomSql = "SELECT UOM_ID from mt_uom where UOM_CODE = 'value' AND TENANT_ID = '" + tenantId + "'";
        String locatorSql = "SELECT LOCATOR_ID from mt_mod_locator where LOCATOR_CODE = 'value' AND TENANT_ID = '" + tenantId + "'";
        String supplierSql = "SELECT SUPPLIER_ID from mt_supplier where SUPPLIER_CODE = 'value' AND TENANT_ID = '" + tenantId + "'";
        String getDocIdSql = "SELECT INSTRUCTION_DOC_ID from MT_INSTRUCTION_DOC where INSTRUCTION_DOC_NUM = 'value' AND TENANT_ID = '" + tenantId + "'";

        for (int i = 0; i < docIfaces.size(); i++) {
            // 判断是否重复数据
            String docId = itfInstructionDocIfaceMapper.selectMySql(getDocIdSql.replace("value", docIfaces.get(i).getInstructionDocNum()));
            // 获取CID
            Long doc_cid = Long.valueOf(this.customDbRepository.getNextKey("mt_instruction_doc_cid_s"));
            // 查询工厂ID和供应商ID
            String siteId = itfInstructionDocIfaceMapper.selectMySql(siteSql.replace("value", docIfaces.get(i).getSiteCode()));
            String materialId = itfInstructionDocIfaceMapper.selectMySql(materialSql.replace("value", docIfaces.get(i).getMaterial()).replaceAll("^(0+)", ""));
            String uomId = itfInstructionDocIfaceMapper.selectMySql(uomSql.replace("value", docIfaces.get(i).getUomCode()));
            String locatorId = itfInstructionDocIfaceMapper.selectMySql(locatorSql.replace("value", docIfaces.get(i).getToLocatorCode()));
            String supplierId = itfInstructionDocIfaceMapper.selectMySql(supplierSql.replace("value", Long.valueOf(docIfaces.get(i).getSupplierCode()).toString()));

            String errorMsg = "";
            if (Strings.isEmpty(siteId)) {
                errorMsg += "根据工厂编码查询不到工厂，请核查！";
            }
            if (Strings.isEmpty(materialId)) {
                errorMsg += "根据物料编码查询不到物料，请核查！";
            }
            if (Strings.isEmpty(uomId)) {
                errorMsg += "根据单位编码查询不到单位，请核查！";
            }
            if (Strings.isEmpty(locatorId)) {
                errorMsg += "根据仓库编码查询不到仓库，请核查！";
            }
            if (Strings.isEmpty(supplierId)) {
                errorMsg += "根据供应商编码查询不到供应商，请核查！";
            }
            if (!Strings.isEmpty(errorMsg)) {
                docIfaces.get(i).setErrorMsg(errorMsg);
                returnDTOS.add(docIfaces.get(i));
                break;
            }
            // 插入头信息
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(Strings.isEmpty(docId) ? this.customDbRepository.getNextKey("mt_instruction_doc_s") : docId);
            mtInstructionDoc.setTenantId(tenantId);
            mtInstructionDoc.setInstructionDocNum(docIfaces.get(i).getInstructionDocNum());
            mtInstructionDoc.setInstructionDocType(docIfaces.get(i).getInstructionDocType());
            mtInstructionDoc.setInstructionDocStatus(docIfaces.get(i).getInstructionDocStatus());
            mtInstructionDoc.setSiteId(siteId);
            mtInstructionDoc.setSupplierId(supplierId);
            mtInstructionDoc.setRemark(docIfaces.get(i).getRemark());
            mtInstructionDoc.setCid(doc_cid);
            mtInstructionDoc.setIdentification(docIfaces.get(i).getInstructionDocNum());
            if (Strings.isEmpty(docId)) {
                mtInstructionDocRepository.insertSelective(mtInstructionDoc);
            } else {
                mtInstructionDoc.setInstructionDocId(docId);
                mtInstructionDocMapper.updateByPrimaryKeySelective(mtInstructionDoc);
            }


            // 插入头扩展表信息
            ItfInstructionDocAttr instructionDocAttr = new ItfInstructionDocAttr();
            instructionDocAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_doc_attr_s"));
            instructionDocAttr.setInstructionDocId(mtInstructionDoc.getInstructionDocId());
            instructionDocAttr.setTenantId(tenantId);
            instructionDocAttr.setAttrName("SAP_DELETE_HFLAG");
            instructionDocAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getDeleteIndH()) ? "" : docIfaces.get(i).getDeleteIndH());
            instructionDocAttr.setCid(doc_cid);
            if (Strings.isEmpty(docId)) {
                itfInstructionDocAttrMapper.insertSelective(instructionDocAttr);
            } else {
                ItfInstructionDocAttr getDocAttrId = new ItfInstructionDocAttr();
                getDocAttrId.setInstructionDocId(docId);
                getDocAttrId.setAttrName("SAP_DELETE_HFLAG");
                getDocAttrId = itfInstructionDocAttrMapper.selectOne(getDocAttrId);
                if (getDocAttrId == null) {
                    itfInstructionDocAttrMapper.insertSelective(instructionDocAttr);
                } else {
                    instructionDocAttr.setAttrId(getDocAttrId.getAttrId());
                    itfInstructionDocAttrMapper.updateByPrimaryKeySelective(instructionDocAttr);
                }
            }
            instructionDocAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_doc_attr_s"));
            instructionDocAttr.setAttrName("SAP_STATUS");
            instructionDocAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getSapStatus()) ? "" : docIfaces.get(i).getSapStatus());
            if (Strings.isEmpty(docId)) {
                itfInstructionDocAttrMapper.insertSelective(instructionDocAttr);
            } else {
                ItfInstructionDocAttr getDocAttrId = new ItfInstructionDocAttr();
                getDocAttrId.setInstructionDocId(docId);
                getDocAttrId.setAttrName("SAP_STATUS");
                getDocAttrId = itfInstructionDocAttrMapper.selectOne(getDocAttrId);
                if (getDocAttrId == null) {
                    itfInstructionDocAttrMapper.insertSelective(instructionDocAttr);
                } else {
                    instructionDocAttr.setAttrId(getDocAttrId.getAttrId());
                    itfInstructionDocAttrMapper.updateByPrimaryKeySelective(instructionDocAttr);
                }

            }

            // 插入行信息
            String instructionNum = mtInstructionDoc.getInstructionDocNum() + "-" + docIfaces.get(i).getPoItem();// 行NUM由头编码-行编码拼接
            String instructionId = this.customDbRepository.getNextKey("mt_instruction_s");
            MtInstruction mtInstruction = new MtInstruction();
            mtInstruction.setInstructionId(instructionId);
            mtInstruction.setTenantId(tenantId);
            mtInstruction.setInstructionNum(instructionNum);
            mtInstruction.setSourceDocId(mtInstructionDoc.getInstructionDocId());
            mtInstruction.setInstructionType(docIfaces.get(i).getInstructionType());
            mtInstruction.setInstructionStatus(docIfaces.get(i).getInstructionStatus());
            mtInstruction.setSiteId(siteId);
            mtInstruction.setMaterialId(materialId);
            mtInstruction.setUomId(uomId);
            mtInstruction.setToSiteId(siteId);
            mtInstruction.setToLocatorId(locatorId);
            mtInstruction.setQuantity(Double.valueOf(docIfaces.get(i).getQuantity().toString()));
            mtInstruction.setSupplierId(supplierId);
            mtInstruction.setDemandTime(docIfaces.get(i).getDemandTime());
            mtInstruction.setIdentification(instructionNum);
            mtInstruction.setCid(doc_cid);
            if (Strings.isEmpty(docId)) {
                mtInstructionRepository.insertSelective(mtInstruction);
            } else {
                String getDocLineIdSql = "SELECT INSTRUCTION_ID from MT_INSTRUCTION where INSTRUCTION_NUM = 'value' AND TENANT_ID = '" + tenantId + "'";
                instructionId = itfInstructionDocIfaceMapper.selectMySql(getDocLineIdSql.replace("value", instructionNum));
                if (instructionId == null) {
                    mtInstructionRepository.insertSelective(mtInstruction);
                } else {
                    mtInstruction.setInstructionId(instructionId);
                    mtInstruction.setCid(doc_cid);
                    mtInstructionRepository.updateByPrimaryKey(mtInstruction);
                }
            }

            // 插入行扩展表信息
            ItfInstructionAttr itfInstructionAttr = new ItfInstructionAttr();
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setTenantId(tenantId);
            itfInstructionAttr.setAttrName("INSTRUCTION_LINE_NUM");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getPoItem()) ? "" : docIfaces.get(i).getPoItem());
            itfInstructionAttr.setCid(doc_cid);
            itfInstructionAttr.setInstructionId(mtInstruction.getInstructionId());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("INSTRUCTION_LINE_NUM");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }

            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("PO_RETURN_FLAG");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getRetpo()) ? "" : docIfaces.get(i).getRetpo());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("PO_RETURN_FLAG");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }

            }

            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("DELETE_IND");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getDeleteIndI()) ? "" : docIfaces.get(i).getDeleteIndI());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("DELETE_IND");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("SO_NUM");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getSdDoc()) ? "" : docIfaces.get(i).getSdDoc());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("SO_NUM");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("SO_LINE_NUM");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getSdocItem()) ? "" : docIfaces.get(i).getSdocItem());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("SO_LINE_NUM");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("PO_TYPE");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getPoType()) ? "" : docIfaces.get(i).getPoType());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("PO_TYPE");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("PO_DISTR_TYPE");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getPoDistrType()) ? "" : docIfaces.get(i).getPoDistrType());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("PO_DISTR_TYPE");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            // 新增扩展字段，2020-10-09 start
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("PO_UOM");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getAttribute1()) ? "" : docIfaces.get(i).getAttribute1());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("PO_UOM");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("PO_UOM_QTY");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getAttribute2()) ? "" : docIfaces.get(i).getAttribute2());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("PO_UOM_QTY");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("UOM_RATE");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getAttribute3()) ? "" : docIfaces.get(i).getAttribute3());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("UOM_RATE");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("PO_UOM_RATE");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getAttribute4()) ? "" : docIfaces.get(i).getAttribute4());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("PO_UOM_RATE");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            itfInstructionAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
            itfInstructionAttr.setAttrName("SAMPLE_FLAG");
            itfInstructionAttr.setAttrValue(Strings.isEmpty(docIfaces.get(i).getAttribute5()) ? "" : docIfaces.get(i).getAttribute5());
            if (Strings.isEmpty(docId)) {
                itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
            } else {
                ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                getInstrAttrId.setInstructionId(mtInstruction.getInstructionId());
                getInstrAttrId.setAttrName("SAMPLE_FLAG");
                getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                if (getInstrAttrId == null) {
                    itfInstructionAttrMapper.insertSelective(itfInstructionAttr);
                } else {
                    getInstrAttrId.setAttrValue(itfInstructionAttr.getAttrValue());
                    itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                }
            }
            // end
            // 写入明细之前，先删除明细数据
            List<String>attrId= itfInstructionAttrMapper.selectByDocId(mtInstruction.getInstructionId());
            if(CollectionUtils.isNotEmpty(attrId))
            {
                itfInstructionAttrMapper.deleteByIds(attrId);
            }
            List<MtInstruction> mtInstructions= mtInstructionMapper.selectByDocId(mtInstruction.getInstructionId());
            if(CollectionUtils.isNotEmpty(mtInstructions))
            {
                mtInstructionRepository.batchDelete(mtInstructions);
            }
            //itfInstructionAttrMapper.deleteByDocId(mtInstructionDoc.getInstructionDocId());
            //mtInstructionMapper.deleteByDocId(mtInstructionDoc.getInstructionDocId());
            // 写入明细表
            for (ItfInstructionIface instructionIface : instructionIfaces) {
                String rspos = instructionIface.getPoNumber() + "-" + instructionIface.getPoItem();
                if (rspos.equals(instructionNum)) {
                    rspos += "-" + instructionIface.getRspos();// 行NUM由头编码-行编码-明细拼接
                    String instructionDlId = this.customDbRepository.getNextKey("mt_instruction_s");
                    String materialDlId = itfInstructionDocIfaceMapper.selectMySql(materialSql.replace("value", instructionIface.getMaterial()).replaceAll("^(0+)", ""));
                    String uomDlId = itfInstructionDocIfaceMapper.selectMySql(uomSql.replace("value", instructionIface.getUomCode()));
                    String siteDlId = itfInstructionDocIfaceMapper.selectMySql(siteSql.replace("value", instructionIface.getSiteCode()));
                    String locatorDlId = "";
                    if(Strings.isNotEmpty(instructionIface.getAttribute1())){
                        locatorDlId = itfInstructionDocIfaceMapper.selectMySql(locatorSql.replace("value", instructionIface.getAttribute1()));
                    }
                    MtInstruction mtInstructionDl = new MtInstruction();
                    mtInstructionDl.setInstructionId(instructionDlId);
                    mtInstructionDl.setTenantId(tenantId);
                    mtInstructionDl.setInstructionNum(rspos);
                    mtInstructionDl.setSourceDocId(mtInstructionDoc.getInstructionDocId());
                    mtInstructionDl.setSourceInstructionId(mtInstruction.getInstructionId());
                    mtInstructionDl.setInstructionType(instructionIface.getInstructionType());
                    mtInstructionDl.setInstructionStatus(instructionIface.getInstructionStatus());
                    mtInstructionDl.setSiteId(siteDlId);
                    mtInstructionDl.setMaterialId(materialDlId);
                    mtInstructionDl.setToLocatorId(Strings.isEmpty(locatorDlId) ? null : locatorDlId);
                    mtInstructionDl.setUomId(uomDlId);
                    mtInstructionDl.setQuantity(0D);
                    mtInstructionDl.setDemandTime(docIfaces.get(i).getDemandTime());
                    mtInstructionDl.setCid(doc_cid);
                    mtInstructionDl.setIdentification(rspos);
                    // 插入明细表数据
                    mtInstructionRepository.insertSelective(mtInstructionDl);
                    // 写入明细扩展表
                    ItfInstructionAttr itfInstructionDlAttr = new ItfInstructionAttr();
                    itfInstructionDlAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
                    itfInstructionDlAttr.setTenantId(tenantId);
                    itfInstructionDlAttr.setAttrName("RSNUM");
                    itfInstructionDlAttr.setAttrValue(Strings.isEmpty(instructionIface.getRsnum()) ? "" : instructionIface.getRsnum());
                    itfInstructionDlAttr.setCid(doc_cid);
                    itfInstructionDlAttr.setInstructionId(mtInstructionDl.getInstructionId());
                    if (Strings.isEmpty(docId)) {
                        itfInstructionAttrMapper.insertSelective(itfInstructionDlAttr);
                    } else {
                        ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                        getInstrAttrId.setInstructionId(mtInstructionDl.getInstructionId());
                        getInstrAttrId.setAttrName("RSNUM");
                        getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                        if (getInstrAttrId == null) {
                            itfInstructionAttrMapper.insertSelective(itfInstructionDlAttr);
                        } else {
                            getInstrAttrId.setAttrValue(itfInstructionDlAttr.getAttrValue());
                            itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                        }
                    }
                    itfInstructionDlAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
                    itfInstructionDlAttr.setAttrName("RSPOS");
                    itfInstructionDlAttr.setAttrValue(Strings.isEmpty(instructionIface.getRspos()) ? "" : instructionIface.getRspos());
                    if (Strings.isEmpty(docId)) {
                        itfInstructionAttrMapper.insertSelective(itfInstructionDlAttr);
                    } else {
                        ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                        getInstrAttrId.setInstructionId(mtInstructionDl.getInstructionId());
                        getInstrAttrId.setAttrName("RSPOS");
                        getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                        if (getInstrAttrId == null) {
                            itfInstructionAttrMapper.insertSelective(itfInstructionDlAttr);
                        } else {
                            getInstrAttrId.setAttrValue(itfInstructionDlAttr.getAttrValue());
                            itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                        }
                    }

                    itfInstructionDlAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
                    itfInstructionDlAttr.setAttrName("BOM_USUAGE");
                    itfInstructionDlAttr.setAttrValue(Objects.isNull(instructionIface.getQuantity()) ? "" : String.valueOf(instructionIface.getQuantity()));
                    if (Strings.isEmpty(docId)) {
                        itfInstructionAttrMapper.insertSelective(itfInstructionDlAttr);
                    } else {
                        ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                        getInstrAttrId.setInstructionId(mtInstructionDl.getInstructionId());
                        getInstrAttrId.setAttrName("BOM_USUAGE");
                        getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                        if (getInstrAttrId == null) {
                            itfInstructionAttrMapper.insertSelective(itfInstructionDlAttr);
                        } else {
                            getInstrAttrId.setAttrValue(itfInstructionDlAttr.getAttrValue());
                            itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                        }
                    }

                    itfInstructionDlAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_attr_s"));
                    itfInstructionDlAttr.setAttrName("INSTRUCTION_LINE_NUM");
                    itfInstructionDlAttr.setAttrValue(instructionIface.getRspos());
                    if (Strings.isEmpty(docId)) {
                        itfInstructionAttrMapper.insertSelective(itfInstructionDlAttr);
                    } else {
                        ItfInstructionAttr getInstrAttrId = new ItfInstructionAttr();
                        getInstrAttrId.setInstructionId(mtInstructionDl.getInstructionId());
                        getInstrAttrId.setAttrName("INSTRUCTION_LINE_NUM");
                        getInstrAttrId = itfInstructionAttrMapper.selectOne(getInstrAttrId);
                        if (getInstrAttrId == null) {
                            itfInstructionAttrMapper.insertSelective(itfInstructionDlAttr);
                        } else {
                            getInstrAttrId.setAttrValue(itfInstructionDlAttr.getAttrValue());
                            itfInstructionAttrMapper.updateByPrimaryKeySelective(getInstrAttrId);
                        }
                    }
                }
            }
        }
        // 提取行信息
        return returnDTOS;
    }
}
