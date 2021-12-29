package com.ruike.wms.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsLocatorPutInStorageDTO;
import com.ruike.wms.api.dto.WmsPutInStorageDTO;
import com.ruike.wms.api.dto.WmsPutInStorageDTO2;
import com.ruike.wms.app.service.WmsBarCodeIdentifyService;
import com.ruike.wms.app.service.WmsPutInStorageService;
import com.ruike.wms.api.dto.WmsCodeIdentifyDTO;
import com.ruike.wms.domain.entity.WmsMaterialLotDocRel;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.repository.WmsMaterialLotDocRelRepository;
import com.ruike.wms.domain.repository.WmsPutInStorageTaskRepository;
import com.ruike.wms.domain.vo.WmsInstructionLineVO;
import com.ruike.wms.domain.vo.WmsInstructionVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 入库上架功能 服务实现
 *
 * @author liyuan.lv@hand-china.com 2020/04/03 18:15
 */
@Service
public class WmsPutInStorageServiceImpl extends BaseServiceImpl<WmsPutInStorageTask> implements WmsPutInStorageService {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private WmsMaterialLotDocRelRepository wmsMaterialLotDocRelRepository;
    @Autowired
    private WmsBarCodeIdentifyService wmsBarCodeIdentifyService;
    @Autowired
    private WmsPutInStorageTaskRepository wmsPutInStorageTaskRepository;


    @Override
    @ProcessLovValue(targetField = {"", "orderLineList"})
    public WmsInstructionVO queryInstructionDocByNum(Long tenantId, String instructionDocNum) {
        MtInstructionDoc doc = mtInstructionDocRepository.selectOne(new MtInstructionDoc() {
            {
                setInstructionDocNum(instructionDocNum);
            }
        });

        if (ObjectUtils.isEmpty(doc)) {
            throw new MtException("WMS_PUT_IN_STOCK_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PUT_IN_STOCK_002", "WMS"));
        }

        if (!WmsConstant.DocType.DELIVERY_DOC.equals(doc.getInstructionDocType())) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0002", "INSTRUCTION", instructionDocNum));
        }

        //8)	校验用户是否有入库单对应的工厂权限
        mtUserOrganizationRepository.userOrganizationPermissionValidate(tenantId, new MtUserOrganization() {{
            setUserId(DetailsHelper.getUserDetails().getUserId());
            setOrganizationType("SITE");
            setOrganizationId(doc.getSiteId());
        }});

        return new WmsInstructionVO() {
            {
                setInstructionDocId(doc.getInstructionDocId());
                setInstructionDocNum(doc.getInstructionDocNum());
                setSiteId(doc.getSiteId());
                MtModSite ms = mtModSiteRepository.selectByPrimaryKey(doc.getSiteId());
                setSiteName(ms.getSiteName());
                setSiteCode(ms.getSiteCode());
                setSupplierId(doc.getSupplierId());
                MtSupplier supplier = mtSupplierRepository.selectByPrimaryKey(doc.getSupplierId());
                setSupplierCode(supplier.getSupplierCode());
                setSupplierName(supplier.getSupplierName());
                setRemark(doc.getRemark());
                setOrderLineList(wmsPutInStorageTaskRepository.queryInstructionLine(tenantId, doc.getInstructionDocId()));
            }
        };
    }

    @Override
    @ProcessLovValue(targetField = {"", "orderLineList", "detailLineList"})
    public WmsInstructionVO queryBarcode(Long tenantId, WmsPutInStorageDTO dto) {
        List<WmsInstructionLineVO> lineList = new ArrayList<>();
        WmsInstructionVO instructionVO = new WmsInstructionVO();
        if (StringUtils.isBlank(dto.getBarCode())) {
            throw new MtException("WMS_PUT_IN_STOCK_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PUT_IN_STOCK_010", "WMS"));
        }
        instructionVO.setBarCode(dto.getBarCode());
        instructionVO.setLocatorCode(dto.getLocatorCode());
        // 获取扫描的条码是物料批、容器还是不存在
        WmsCodeIdentifyDTO wmsCodeIdentifyDTO = wmsBarCodeIdentifyService.codeIdentify(tenantId, dto.getBarCode());
        instructionVO.setWmsCodeIdentifyDTO(wmsCodeIdentifyDTO);

        if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equalsIgnoreCase(wmsCodeIdentifyDTO.getCodeType())) {

            lineList = wmsPutInStorageTaskRepository.queryInstructionLineByLotIds(tenantId, new ArrayList<String>() {
                {
                    add(wmsCodeIdentifyDTO.getCodeId());
                }
            });

        } else if (HmeConstants.LoadTypeCode.CONTAINER.equalsIgnoreCase(wmsCodeIdentifyDTO.getCodeType())) {

            List<MtContLoadDtlVO4> mtContLoadDtl4List = mtContainerLoadDetailRepository
                    .containerLimitMaterialLotQuery(tenantId, new MtContLoadDtlVO10() {
                        {
                            setContainerId(wmsCodeIdentifyDTO.getCodeId());
                        }
                    });

            List<String> materialLotIdList = new ArrayList<>();
            for (MtContLoadDtlVO4 vo : mtContLoadDtl4List) {
                //i.	调用{materialLotLimitAttrQuery}获取条码关联的入库指令
                //传入：“materialLotId”= codeId，“attrName”=instructionId
                //获取：指令ID=“attrValue”，获取不到报错“Z_MATERIAL_LOT_0025”，（扫描条码无送货单指令，请确认！）；获取到继续执行ii；
                WmsMaterialLotDocRel condition = new WmsMaterialLotDocRel();
                condition.setTenantId(tenantId);
                condition.setMaterialLotId(vo.getMaterialLotId());
                List<WmsMaterialLotDocRel> wmsMaterialLotDocRelList = wmsMaterialLotDocRelRepository.select(condition);
                if (CollectionUtils.isEmpty(wmsMaterialLotDocRelList)) {
                    throw new MtException("WMS_PUT_IN_STOCK_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PUT_IN_STOCK_011", "WMS"));
                }
                materialLotIdList.add(vo.getMaterialLotId());
            }

            lineList = wmsPutInStorageTaskRepository.queryInstructionLineByLotIds(tenantId, materialLotIdList);
        }

        return wmsPutInStorageTaskRepository.handleData(tenantId, dto.getInstructionDocNum(), instructionVO, lineList, dto.getEnableDocFlag(), "N");
    }

}
