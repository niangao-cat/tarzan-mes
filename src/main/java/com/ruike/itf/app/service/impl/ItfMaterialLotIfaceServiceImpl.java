package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfMaterialLotReturnDTO;
import com.ruike.itf.app.service.ItfMaterialLotIfaceService;
import com.ruike.itf.domain.entity.ItfMaterialLotAttr;
import com.ruike.itf.domain.entity.ItfMaterialLotIface;
import com.ruike.itf.domain.repository.ItfMaterialLotIfaceRepository;
import com.ruike.itf.infra.mapper.ItfMaterialLotAttrMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据应用服务默认实现
 *
 * @author yapeng.yao@hand-china.com 2020-09-01 09:32:35
 */
@Slf4j
@Service
public class ItfMaterialLotIfaceServiceImpl implements ItfMaterialLotIfaceService {

    @Autowired
    private ItfMaterialLotIfaceRepository itfMaterialLotIfaceRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private ItfMaterialLotAttrMapper itfMaterialLotAttrMapper;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Override
    public List<ItfMaterialLotReturnDTO> invoke(List<ItfMaterialLotIface> itfMaterialLotIfaceList) {
        List<ItfMaterialLotReturnDTO> returnList = new ArrayList<>();
        // 判断是否为空
        if (Objects.nonNull(itfMaterialLotIfaceList)) {
            // 设置ID
            setItfMaterialLotIfaceListInterfaceId(itfMaterialLotIfaceList);
            itfMaterialLotIfaceList.stream().forEach(iface -> {
                // 校验物料批输入数据
                checkItfMaterialLotIface(iface);
            });

            // 正确数据
            List<ItfMaterialLotIface> mtIfaceList = itfMaterialLotIfaceList.stream()
                    .filter(iface -> "N".equals(iface.getProcessStatus())).collect(Collectors.toList());
            // 错误数据
            List<ItfMaterialLotIface> errIfaceList = itfMaterialLotIfaceList.stream()
                    .filter(iface -> !"N".equals(iface.getProcessStatus())).collect(Collectors.toList());

            // 全量插入接口表
            if (CollectionUtils.isNotEmpty(itfMaterialLotIfaceList)) {
                // 插入数据
                itfMaterialLotIfaceRepository.batchInsert(itfMaterialLotIfaceList);
            }

            // 导入校验成功数据
            if (CollectionUtils.isNotEmpty(mtIfaceList)) {
                List<MtMaterialLot> mtMaterialLotList = new ArrayList<>(mtIfaceList.size());
                List<ItfMaterialLotAttr> attrPropertyList = new ArrayList<>(mtIfaceList.size());

                int mtIfaceListSize = mtIfaceList.size();
                List<String> mt_material_lot_s = mtCustomDbRepository.getNextKeys("mt_material_lot_s", mtIfaceListSize);
                List<String> attrKey = mtCustomDbRepository.getNextKeys("mt_material_lot_attr_s", mtIfaceListSize * 10);
                List<String> mt_material_lot_cid_s = mtCustomDbRepository.getNextKeys("mt_material_lot_cid_s", mtIfaceListSize);
                for (int i = 0; i < mtIfaceListSize; i++) {
                    // set物料批数据
                    MtMaterialLot mtMaterialLot = setMtMaterialLot(mtIfaceList.get(i), mt_material_lot_s.get(i), mt_material_lot_cid_s.get(i));
                    mtMaterialLotList.add(mtMaterialLot);
                    // set物料批扩展数据

                    List<ItfMaterialLotAttr> mtCommonExtendVO5List = setMtCommonExtendVO5List(tenantId, mtIfaceList.get(i),
                            mtMaterialLot.getMaterialLotId(), attrKey);
                    // set物料批扩展表mt_material_lot_attr
                    attrPropertyList.addAll(mtCommonExtendVO5List);
                }
                boolean errorFlag = false;
                String status = "S";
                String errorMessage = "";
                try {
                    if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                        for (MtMaterialLot domains : mtMaterialLotList) {
                            // 物料批数据
                            mtMaterialLotMapper.insertSelective(domains);
                        }
                    }
                    List<String> materialIds = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
                    // 物料批扩展数据
                    if (CollectionUtils.isNotEmpty(materialIds)) {
                        itfMaterialLotAttrMapper.deleteByMaterialLotId(materialIds);
                    }
                    for (ItfMaterialLotAttr attr : attrPropertyList) {
                        itfMaterialLotAttrMapper.insertSelective(attr);
                    }
                } catch (Exception e) {
                    errorFlag = true;
                    errorMessage = e.getMessage();
                }
                if (errorFlag) {
                    // 插入报错,修改接口表状态-->E
                    status = "E";
                }
                // 修改接口表状态
                mtIfaceList = changeItfStatus(mtIfaceList, status, errorMessage);

                itfMaterialLotIfaceRepository.batchUpdateByPrimaryKeySelective(mtIfaceList);
            }
            // set返回信息
            setReturnList(mtIfaceList, errIfaceList, returnList);
        }
        return returnList;
    }

    /**
     * 设置接口表ID --> setInterfaceId
     *
     * @param itfMaterialLotIfaceList
     */
    private void setItfMaterialLotIfaceListInterfaceId(List<ItfMaterialLotIface> itfMaterialLotIfaceList) {
        List<String> interfaceIdList = mtCustomDbRepository.getNextKeys("itf_material_lot_iface_s", itfMaterialLotIfaceList.size());

        for (int i = 0, len = itfMaterialLotIfaceList.size(); i < len; i++) {
            itfMaterialLotIfaceList.get(i).setInterfaceId(interfaceIdList.get(i));
        }
    }

    /**
     * 修改状态 -> ProcessStatus
     *
     * @param mtIfaceList
     * @param status
     * @param errorMessage
     * @return
     */
    private List<ItfMaterialLotIface> changeItfStatus(List<ItfMaterialLotIface> mtIfaceList, String status, String errorMessage) {
        mtIfaceList.stream().forEach(iface -> {
            iface.setProcessStatus(status);
            if (StringUtils.isNotBlank(errorMessage)) {
                String processMessage = iface.getProcessMessage();
                String message = StringUtils.isBlank(processMessage) ? errorMessage : processMessage + ";" + errorMessage;
                iface.setProcessMessage(message);
            }
        });
        return mtIfaceList;
    }

    /**
     * set返回信息
     *
     * @param mtIfaceList
     * @param errIfaceList
     * @param returnList
     */
    private void setReturnList(List<ItfMaterialLotIface> mtIfaceList, List<ItfMaterialLotIface> errIfaceList, List<ItfMaterialLotReturnDTO> returnList) {
        Date nowDate = new Date();
        List<ItfMaterialLotIface> voList = new ArrayList<>(mtIfaceList.size() + errIfaceList.size());
        voList.addAll(mtIfaceList);
        voList.addAll(errIfaceList);
        voList.forEach(itfMaterialLotIface -> {
            ItfMaterialLotReturnDTO dto = new ItfMaterialLotReturnDTO();
            dto.setMaterialLotCode(itfMaterialLotIface.getMaterialLotCode());
            dto.setProcessDate(nowDate);
            dto.setProcessStatus(itfMaterialLotIface.getProcessStatus());
            dto.setProcessMessage(itfMaterialLotIface.getProcessMessage());
            returnList.add(dto);
        });
    }

    /**
     * set物料批数据
     *
     * @param itfMaterialLotIface
     * @param materialLotId
     * @param mtMaterialLotCidId
     * @return
     */
    private MtMaterialLot setMtMaterialLot(ItfMaterialLotIface itfMaterialLotIface, String materialLotId, String mtMaterialLotCidId) {
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        BeanUtils.copyProperties(itfMaterialLotIface, mtMaterialLot);

        mtMaterialLot.setMaterialLotId(materialLotId);
        // 数量
        mtMaterialLot.setPrimaryUomQty(itfMaterialLotIface.getPrimaryUomQty().doubleValue());

        mtMaterialLot.setLocatorId("-1");
        mtMaterialLot.setIdentification(itfMaterialLotIface.getMaterialLotCode());
        mtMaterialLot.setCid(Long.valueOf(mtMaterialLotCidId));
        mtMaterialLot.setTenantId(tenantId);
        mtMaterialLot.setObjectVersionNumber(1L);
        mtMaterialLot.setCreatedBy(-1L);
        mtMaterialLot.setCreationDate(new Date());
        mtMaterialLot.setLastUpdatedBy(-1L);
        mtMaterialLot.setLastUpdateDate(new Date());

        return mtMaterialLot;
    }

    /**
     * set物料批扩展数据
     *
     * @param tenantId
     * @param itfMaterialLotIface
     * @param materialLotId
     * @param attrKey
     */
    private List<ItfMaterialLotAttr> setMtCommonExtendVO5List(Long tenantId, ItfMaterialLotIface itfMaterialLotIface, String materialLotId, List<String> attrKey) {
        List<ItfMaterialLotAttr> mtCommonExtendVO5List = new ArrayList<>();
        // 外箱条码
        ItfMaterialLotAttr mtCommonExtendVO5 = new ItfMaterialLotAttr();
        mtCommonExtendVO5.setTenantId(tenantId);
        mtCommonExtendVO5.setAttrName("OUTER_BOX");
        mtCommonExtendVO5.setAttrValue(itfMaterialLotIface.getOuterBox());
        mtCommonExtendVO5.setMaterialLotId(materialLotId);
        mtCommonExtendVO5.setAttrId(attrKey.remove(0));
        mtCommonExtendVO5.setCid(-1L);
        mtCommonExtendVO5List.add(mtCommonExtendVO5);

        // 供应商条码批次
        mtCommonExtendVO5 = new ItfMaterialLotAttr();
        mtCommonExtendVO5.setTenantId(tenantId);
        mtCommonExtendVO5.setAttrName("SUPPLIER_LOT");
        mtCommonExtendVO5.setAttrValue(itfMaterialLotIface.getSupplierLot());
        mtCommonExtendVO5.setMaterialLotId(materialLotId);
        mtCommonExtendVO5.setAttrId(attrKey.remove(0));
        mtCommonExtendVO5.setCid(-1L);
        mtCommonExtendVO5List.add(mtCommonExtendVO5);

        // 物料版本
        mtCommonExtendVO5 = new ItfMaterialLotAttr();
        mtCommonExtendVO5.setTenantId(tenantId);
        mtCommonExtendVO5.setAttrName("MATERIAL_VERSION");
        mtCommonExtendVO5.setAttrValue(itfMaterialLotIface.getMaterialVersion());
        mtCommonExtendVO5.setMaterialLotId(materialLotId);
        mtCommonExtendVO5.setAttrId(attrKey.remove(0));
        mtCommonExtendVO5.setCid(-1L);
        mtCommonExtendVO5List.add(mtCommonExtendVO5);

        // 生产日期
        mtCommonExtendVO5 = new ItfMaterialLotAttr();
        mtCommonExtendVO5.setTenantId(tenantId);
        mtCommonExtendVO5.setAttrName("PRODUCT_DATE");
        mtCommonExtendVO5.setAttrValue(itfMaterialLotIface.getProductDate());
        mtCommonExtendVO5.setMaterialLotId(materialLotId);
        mtCommonExtendVO5.setAttrId(attrKey.remove(0));
        mtCommonExtendVO5.setCid(-1L);
        mtCommonExtendVO5List.add(mtCommonExtendVO5);

        // 状态
        mtCommonExtendVO5 = new ItfMaterialLotAttr();
        mtCommonExtendVO5.setTenantId(tenantId);
        mtCommonExtendVO5.setAttrName("STATUS");
        mtCommonExtendVO5.setAttrValue("NEW");
        mtCommonExtendVO5.setMaterialLotId(materialLotId);
        mtCommonExtendVO5.setAttrId(attrKey.remove(0));
        mtCommonExtendVO5.setCid(-1L);
        mtCommonExtendVO5List.add(mtCommonExtendVO5);

        return mtCommonExtendVO5List;
    }

    /**
     * 校验输入参数
     *
     * @param itfMaterialLotIface
     * @return
     */
    private ItfMaterialLotIface checkItfMaterialLotIface(ItfMaterialLotIface itfMaterialLotIface) {
        // 条码号
        String materialLotCode = itfMaterialLotIface.getMaterialLotCode();
        // 站点CODE-->ID
        String siteCode = itfMaterialLotIface.getSiteCode();
        // 物料CODE--ID
        String materialCode = itfMaterialLotIface.getMaterialCode();
        // 物料主计量单位Code-->ID
        String primaryUomCode = itfMaterialLotIface.getPrimaryUomCode();
        // 物料主计量单位下的数量
        BigDecimal primaryUomQty = itfMaterialLotIface.getPrimaryUomQty();
        // 供应商CODE-->ID
        String supplierCode = itfMaterialLotIface.getSupplierCode();
        StringBuilder messageSb = new StringBuilder();
        String status = "N";
        String errMessage = "";
        // 校验物料批
//        errMessage = checkMaterialLotCode(tenantId, materialLotCode);
        //V20210908 modify by penglin.sui for peng.zhao 物料批已存在，不报错，直接返回 S
        if (StringUtils.isBlank(materialLotCode)) {
            errMessage = "条码号为空!";
        } else {
            MtMaterialLot mtMaterialLotInfo = queryMtMaterialLot(tenantId, materialLotCode);
            if (Objects.nonNull(mtMaterialLotInfo)) {
                status = "S";
            }
        }
        if (StringUtils.isNotBlank(errMessage)) {
            status = "E";
            messageSb.append(errMessage).append(";");
        }
        // 校验站点
        errMessage = checkSiteCode(tenantId, siteCode);
        if (StringUtils.isNotBlank(errMessage)) {
            status = "E";
            messageSb.append(errMessage).append(";");
        } else {
            MtModSite mtModSite = queryMtModSite(tenantId, siteCode);
            itfMaterialLotIface.setSiteId(mtModSite.getSiteId());
        }

        // 校验物料
        errMessage = checkMaterialCode(materialCode, itfMaterialLotIface);
        if (StringUtils.isNotBlank(errMessage)) {
            status = "E";
            messageSb.append(errMessage).append(";");
        }

        // 校验单位
        errMessage = checkPrimaryUomCode(primaryUomCode, itfMaterialLotIface);
        if (StringUtils.isNotBlank(errMessage)) {
            status = "E";
            messageSb.append(errMessage).append(";");
        }

        // 校验数量
        if (primaryUomQty == null) {
            status = "E";
            messageSb.append("数量为空").append(";");
        }

        // 校验供应商
        errMessage = checkSupplierCode(supplierCode, itfMaterialLotIface);
        if (StringUtils.isNotBlank(errMessage)) {
            status = "E";
            messageSb.append(errMessage).append(";");
        }

        itfMaterialLotIface.setEnableFlag("N");
        itfMaterialLotIface.setQualityStatus("PENDING");
        itfMaterialLotIface.setProcessDate(new Date());
        itfMaterialLotIface.setProcessStatus(status);
        itfMaterialLotIface.setProcessMessage(messageSb.toString());

        itfMaterialLotIface.setTenantId(tenantId);
        itfMaterialLotIface.setObjectVersionNumber(1L);
        itfMaterialLotIface.setCreatedBy(-1L);
        itfMaterialLotIface.setCreationDate(new Date());
        itfMaterialLotIface.setLastUpdatedBy(-1L);
        itfMaterialLotIface.setLastUpdateDate(new Date());
        return itfMaterialLotIface;
    }

    /**
     * 校验物料编码
     *
     * @param materialCode
     * @param itfMaterialLotIface
     * @return
     */
    private String checkMaterialCode(String materialCode, ItfMaterialLotIface itfMaterialLotIface) {
        String errMessage = "";
        if (StringUtils.isBlank(materialCode)) {
            errMessage = "物料编码为空!";
        } else {
            MtMaterial mtMaterial = queryMtMaterial(tenantId, materialCode);
            if (Objects.isNull(mtMaterial)) {
                errMessage = "物料不存在!";
            } else {
                itfMaterialLotIface.setMaterialId(mtMaterial.getMaterialId());
            }
        }
        return errMessage;
    }

    /**
     * 校验供应商
     *
     * @param supplierCode
     * @param itfMaterialLotIface
     * @return
     */
    private String checkSupplierCode(String supplierCode, ItfMaterialLotIface itfMaterialLotIface) {
        String errMessage = "";
        if (StringUtils.isBlank(supplierCode)) {
            errMessage = "供应商为空!";
        } else {
            MtSupplier mtSupplier = queryMtSupplier(tenantId, supplierCode);
            if (Objects.isNull(mtSupplier)) {
                errMessage = "供应商不存在!";
            } else {
                itfMaterialLotIface.setSupplierId(mtSupplier.getSupplierId());
            }
        }
        return errMessage;
    }

    /**
     * 校验单位
     *
     * @param primaryUomCode
     * @param itfMaterialLotIface
     * @return
     */
    private String checkPrimaryUomCode(String primaryUomCode, ItfMaterialLotIface itfMaterialLotIface) {
        String errMessage = "";
        if (StringUtils.isBlank(primaryUomCode)) {
            errMessage = "单位为空!";
        } else {
            MtUom mtUom = queryMtUom(tenantId, primaryUomCode);
            if (Objects.isNull(mtUom)) {
                errMessage = "单位不存在!";
            } else {
                itfMaterialLotIface.setPrimaryUomId(mtUom.getUomId());
            }
        }
        return errMessage;
    }

    /**
     * 校验站点
     *
     * @param tenantId
     * @param siteCode
     * @return
     */
    private String checkSiteCode(Long tenantId, String siteCode) {
        String errMessage = "";
        if (StringUtils.isBlank(siteCode)) {
            errMessage = "工厂为空!";
        }
        return errMessage;
    }

    /**
     * 校验物料批
     *
     * @param tenantId
     * @param materialLotCode
     * @return
     */
    private String checkMaterialLotCode(Long tenantId, String materialLotCode) {
        String errMessage = "";
        if (StringUtils.isBlank(materialLotCode)) {
            errMessage = "条码号为空!";
        } else {
            MtMaterialLot mtMaterialLotInfo = queryMtMaterialLot(tenantId, materialLotCode);
            if (Objects.nonNull(mtMaterialLotInfo)) {
                errMessage = "条码号已存在!";
            }
        }
        return errMessage;
    }

    /**
     * 查询供应商信息
     *
     * @param tenantId
     * @param supplierCode
     * @return
     */
    private MtSupplier queryMtSupplier(Long tenantId, String supplierCode) {
        MtSupplier mtSupplier = new MtSupplier();
        mtSupplier.setTenantId(tenantId);
        mtSupplier.setSupplierCode(supplierCode);
        return mtSupplierRepository.selectOne(mtSupplier);
    }

    /**
     * 查询站点信息
     *
     * @param tenantId
     * @param siteCode
     * @return
     */
    private MtModSite queryMtModSite(Long tenantId, String siteCode) {
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(siteCode);
        return mtModSiteRepository.selectOne(mtModSite);
    }

    /**
     * 查询单位信息
     *
     * @param tenantId
     * @param primaryUomCode
     * @return
     */
    private MtUom queryMtUom(Long tenantId, String primaryUomCode) {
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomCode(primaryUomCode);
        return mtUomRepository.selectOne(mtUom);
    }

    /**
     * 查询物料信息
     *
     * @param tenantId
     * @param materialCode
     * @return
     */
    private MtMaterial queryMtMaterial(Long tenantId, String materialCode) {
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialCode(materialCode);
        return mtMaterialRepository.selectOne(mtMaterial);
    }

    /**
     * 查询物料批信息
     *
     * @param tenantId
     * @param materialLotCode
     */
    private MtMaterialLot queryMtMaterialLot(Long tenantId, String materialLotCode) {
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLot.setTenantId(tenantId);
        mtMaterialLot.setMaterialLotCode(materialLotCode);
        return mtMaterialLotRepository.selectOne(mtMaterialLot);
    }

}