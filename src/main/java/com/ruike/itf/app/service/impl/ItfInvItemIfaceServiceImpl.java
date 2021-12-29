package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.domain.entity.HmeMaterialVersion;
import com.ruike.hme.infra.mapper.HmeMaterialVersionMapper;
import com.ruike.itf.api.dto.ItfInvItemReturnDTO;
import com.ruike.itf.api.dto.ItfInvItemSyncDTO;
import com.ruike.itf.api.dto.ItfMaterialVersionSyncDTO;
import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfInvItemIfaceService;
import com.ruike.itf.domain.entity.ItfInvItemIface;
import com.ruike.itf.domain.entity.ItfMaterialVersionIface;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfInvItemIfaceMapper;
import com.ruike.itf.infra.mapper.ItfMaterialVersionIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.GetDeclaredFields;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtInvItemIfaceRepository;
import tarzan.iface.infra.mapper.MtInvItemIfaceMapper;
import tarzan.material.domain.entity.MtMaterial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 物料接口表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Service
public class ItfInvItemIfaceServiceImpl implements ItfInvItemIfaceService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private ItfInvItemIfaceMapper itfInvItemIfaceMapper;

    @Autowired
    private MtInvItemIfaceMapper mtInvItemIfaceMapper;

    @Autowired
    private MtInvItemIfaceRepository mtInvItemIfaceRepository;

    @Autowired
    private ItfMaterialVersionIfaceMapper itfMaterialVersionIfaceMapper;

    @Autowired
    private HmeMaterialVersionMapper hmeMaterialVersionMapper;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;


    @Override
    public List<ItfInvItemReturnDTO> invoke(Map<String, Object> itemMap) {
        List<ItfMaterialVersionSyncDTO> materialVersion = JSONArray.parseArray(itemMap.get("VERID").toString(), ItfMaterialVersionSyncDTO.class);
        List<ItfSapIfaceDTO> sapIfaceDTOS = JSONArray.parseArray(itemMap.get("MATERIAL").toString(), ItfSapIfaceDTO.class);

        // 判断是否为空
        if (CollectionUtils.isEmpty(sapIfaceDTOS)) {
            return new ArrayList<>();
        }
        // 由SAP数据转换为MT数据，更换DTO集合
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = new Date();
        String batchDate = format.format(newDate);// 批次时间
        List<ItfInvItemSyncDTO> invItemList = new ArrayList<>();
        for (int i = 0; i < sapIfaceDTOS.size(); i++) {
            invItemList.add(new ItfInvItemSyncDTO(sapIfaceDTOS.get(i)));
        }
        Double maxValue = Double.valueOf(lovAdapter.queryLovMeaning("WMS.POWER_LEVEL", 0L, "MAX"));
        Double minValue = Double.valueOf(lovAdapter.queryLovMeaning("WMS.POWER_LEVEL", 0L, "MIN"));
        // 校验不通过数据
        List<ItfInvItemReturnDTO> returnList = new ArrayList<>();
        Double batchId = Double.valueOf(this.customDbRepository.getNextKey("mt_inv_item_iface_batch_id_s"));
        List<String> ifaceId = this.customDbRepository.getNextKeys("mt_inv_item_iface_s", invItemList.size());
        try {
            // 批次
            BeanCopier copier = BeanCopier.create(ItfInvItemSyncDTO.class, ItfInvItemIface.class, false);
            //循环插入MES
            List<ItfInvItemIface> ifaceList = new ArrayList<>(invItemList.size());
            for (ItfInvItemSyncDTO invItem : invItemList) {
                String status = "N";
                String message = "";
                String plantCode = invItem.getPlantCode();
                String itemCode = invItem.getItemCode();
                String primaryUom = invItem.getPrimaryUom();
                String desc = invItem.getDescriptions();
                String erpCreationDateStr = invItem.getErpCreationDate();
                String erpLastUpdateDateStr = invItem.getErpLastUpdateDate();
                Date erpCreationDate = null;
                Date erpLastUpdateDate = null;
                // 校验接口表必输字段不能为空
                if (StringUtils.isEmpty(plantCode)) {
                    status = "E";
                    message += "[plantCode]不允许为空！";
                }
                if (StringUtils.isEmpty(itemCode)) {
                    status = "E";
                    message += "[itemCode]不允许为空";
                } else {
                    itemCode = itemCode.replaceAll("^(0+)", "");
                }
                if (StringUtils.isEmpty(primaryUom)) {
                    status = "E";
                    message += "[primaryUom]不允许为空！";
                }
                if (StringUtils.isEmpty(desc)) {
                    status = "E";
                    message += "[descriptions]不允许为空！";
                }
                if (StringUtils.isEmpty(erpCreationDateStr)) {
                    status = "E";
                    message += "[erpCreationDate]不允许为空！";
                } else {
                    try {
                        erpCreationDate = format.parse(erpCreationDateStr);
                    } catch (ParseException e) {
                        status = "E";
                        message += "[erpCreationDate]不允许为空！";
                    }
                }
                if (StringUtils.isEmpty(erpLastUpdateDateStr)) {
                    status = "E";
                    message += "[erpLastUpdateDate]不允许为空！";
                } else {
                    try {
                        erpLastUpdateDate = format.parse(erpLastUpdateDateStr);
                    } catch (ParseException e) {
                        status = "E";
                        message += "[erpLastUpdateDate]不允许为空！";
                    }
                }
                ItfInvItemIface itfInvItemIface = new ItfInvItemIface();
                copier.copy(invItem, itfInvItemIface, null);
                itfInvItemIface.setTenantId(tenantId);
                itfInvItemIface.setPlantCode(plantCode);
                itfInvItemIface.setItemCode(itemCode);
                itfInvItemIface.setOldItemCode(invItem.getOldItemCode());
                itfInvItemIface.setPrimaryUom(primaryUom);
                itfInvItemIface.setDescriptions(desc);
                itfInvItemIface.setEnableFlag("X".equals(invItem.getEnableFlag()) ? "N" : "Y");
                itfInvItemIface.setErpCreationDate(erpCreationDate);
                itfInvItemIface.setErpCreatedBy(-1L);
                itfInvItemIface.setErpLastUpdateDate(erpLastUpdateDate);
                itfInvItemIface.setErpLastUpdatedBy(-1L);
                itfInvItemIface.setStatus(status);
                itfInvItemIface.setMessage(message);
                itfInvItemIface.setBatchId(batchId);
                // 功率大小
                String powerSize = invItem.getAttribute4();
                // 功率对应数值
                String powerValueStr = null;
                if (StringUtils.isNotEmpty(powerSize)) {
                    String regEx = "[^0-9+(.[0-9]{0-9})?$]";
                    Pattern pat = Pattern.compile(regEx);
                    Matcher mat = pat.matcher(powerSize);
                    powerValueStr = mat.replaceAll("").trim();
                }
                Double powerValue = StringUtils.isNotEmpty(powerValueStr) ? Double.valueOf(powerValueStr) : 0D;
                // 功率
                String power;
                if (powerValue < minValue) {
                    power = ItfConstant.Power.LOW_POWER;
                } else if (powerValue > maxValue) {
                    power = ItfConstant.Power.HIGH_POWER;
                } else {
                    power = ItfConstant.Power.MED_POWER;
                }
                // 功率（01-高功率、02-中功率、03低功率）
                itfInvItemIface.setAttribute3(power);
                itfInvItemIface.setBatchDate(batchDate);
                itfInvItemIface.setIfaceId(ifaceId.remove(0));
                itfInvItemIface.setCid(-1L);
                itfInvItemIface.setObjectVersionNumber(1L);
                itfInvItemIface.setCreatedBy(-1L);
                itfInvItemIface.setCreationDate(new Date());
                itfInvItemIface.setLastUpdatedBy(-1L);
                itfInvItemIface.setLastUpdateDate(new Date());
                ifaceList.add(itfInvItemIface);
            }
            // 筛选数据
            List<ItfInvItemIface> mtIfaceList = ifaceList.stream().filter(item ->
                    "N".equals(item.getStatus())).collect(Collectors.toList());
            List<ItfInvItemIface> errIfaceList = ifaceList.stream().filter(item ->
                    !"N".equals(item.getStatus())).collect(Collectors.toList());
            // 分批全量插入接口表
            if (CollectionUtils.isNotEmpty(ifaceList)) {
                List<List<ItfInvItemIface>> splitSqlList = InterfaceUtils.splitSqlList(ifaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfInvItemIface> domains : splitSqlList) {
                    itfInvItemIfaceMapper.batchInsertItemIface("itf_inv_item_iface", domains);
                }
            }
            // 导入校验成功数据
            if (CollectionUtils.isNotEmpty(mtIfaceList)) {
                List<List<ItfInvItemIface>> splitSqlList = InterfaceUtils.splitSqlList(mtIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfInvItemIface> domains : splitSqlList) {
                    itfInvItemIfaceMapper.batchInsertItem("mt_inv_item_iface", domains);
                }
            }
            errIfaceList.forEach(err -> {
                ItfInvItemReturnDTO dto = new ItfInvItemReturnDTO();
                dto.setItemCode(err.getItemCode());
                dto.setPlantCode(err.getPlantCode());
                dto.setProcessDate(newDate);
                dto.setProcessStatus(err.getStatus());
                dto.setProcessMessage(err.getMessage());
                returnList.add(dto);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 异步线程跑API
        new Thread(() -> {
            mtInvItemIfaceRepository.myMaterialInterfaceImport(tenantId, batchId);
            if (CollectionUtils.isNotEmpty(materialVersion)) {
                saveMaterialVersion(materialVersion);
            }
        }).start();

        return returnList;
    }

    /**
     * 保存物料版本
     *
     * @param materialVersion
     */
    public void saveMaterialVersion(List<ItfMaterialVersionSyncDTO> materialVersion) {
        Date date = new Date();
        List<ItfMaterialVersionIface> versionIfaces = new ArrayList<>();
        List<String> ifaceIds = this.customDbRepository.getNextKeys("itf_material_version_iface_s", materialVersion.size());
        GetDeclaredFields<ItfMaterialVersionSyncDTO> fields = new GetDeclaredFields<>();
        String[] notNullFields = {"MATNR", "WERKS", "VERID", "BDATU", "ADATU"};
        for (ItfMaterialVersionSyncDTO syncDTO : materialVersion) {
            List<String> declaredFields = fields.getDeclaredFields(syncDTO, notNullFields);
            ItfMaterialVersionIface iface = new ItfMaterialVersionIface(tenantId,
                    ifaceIds.remove(0),
                    syncDTO, date);
            if (CollectionUtils.isNotEmpty(declaredFields)) {
                iface.setStatus("E");
                iface.setMessage(declaredFields.toString() + "不允许为空！");
            }
            iface.setStatus("S");
            versionIfaces.add(iface);
        }


        Map<String, Map<String, Object>> materialVsAttr = selectMaterialVsAttribute(versionIfaces);
        Map<String, Object> siteMap = materialVsAttr.get("site");
        Map<String, Object> materialMap = materialVsAttr.get("material");
        Map<String, Object> materialVersionMap = materialVsAttr.get("materialVersion");
        List<String> versionIds = this.customDbRepository.getNextKeys("hme_material_version_s", materialVersion.size());
        for (int i = 0; i < versionIfaces.size(); i++) {
            if ("E".equals(versionIfaces.get(i).getStatus())) {
                itfMaterialVersionIfaceMapper.insert(versionIfaces.get(i));
                continue;
            }
            // 校验工厂ID是否存在
            MtSitePlantReleation site = (MtSitePlantReleation) siteMap.get(versionIfaces.get(i).getSiteCode());
            MtMaterial mtMaterial = (MtMaterial) materialMap.get(versionIfaces.get(i).getMaterialCode());
            if (Objects.isNull(site)) {
                versionIfaces.get(i).setStatus("E");
                versionIfaces.get(i).setMessage(versionIfaces.get(i).getMessage() + "根据工厂编码找不到工厂！");
                itfMaterialVersionIfaceMapper.insert(versionIfaces.get(i));
                continue;
            }
            // 校验物料是否存在
            if (Objects.isNull(mtMaterial)) {
                versionIfaces.get(i).setStatus("E");
                versionIfaces.get(i).setMessage("根据物料编码找不到物料！");
                itfMaterialVersionIfaceMapper.insert(versionIfaces.get(i));
                continue;
            }
            // 校验通过
            versionIfaces.get(i).setStatus("S");
            versionIfaces.get(i).setMessage("成功！");
            // 赋值
            HmeMaterialVersion hmeMaterialVersion = new HmeMaterialVersion();
            BeanUtils.copyProperties(versionIfaces.get(i), hmeMaterialVersion);
            String siteId = site.getSiteId();
            String materialId = mtMaterial.getMaterialId();
            String mvCode = versionIfaces.get(i).getMaterialVersion();
            hmeMaterialVersion.setTenantId(tenantId);
            hmeMaterialVersion.setSiteId(siteId);
            hmeMaterialVersion.setMaterialId(materialId);
            hmeMaterialVersion.setMaterialVersion(mvCode);
            // 校验物料版本是否存在
            String mapKey = tenantId + "-" + siteId + "-" + materialId + "-" + mvCode;
            HmeMaterialVersion mvObject = (HmeMaterialVersion) materialVersionMap.get(mapKey);
            try {
                if (Objects.isNull(mvObject)) {
                    hmeMaterialVersion.setMaterialVersionId(versionIds.remove(0));
                    hmeMaterialVersionMapper.insert(hmeMaterialVersion);
                } else {
                    hmeMaterialVersion.setMaterialVersionId(mvObject.getMaterialVersionId());
                    hmeMaterialVersion.setObjectVersionNumber(mvObject.getObjectVersionNumber());
                    hmeMaterialVersionMapper.updateByPrimaryKey(hmeMaterialVersion);
                }
            } catch (Exception e) {
                e.printStackTrace();
                versionIfaces.get(i).setStatus("E");
                versionIfaces.get(i).setMessage(e.getMessage());
            }
            versionIfaces.get(i).setObjectVersionNumber(1L);
            itfMaterialVersionIfaceMapper.insert(versionIfaces.get(i));
        }


    }

    private Map<String, Map<String, Object>> selectMaterialVsAttribute(List<ItfMaterialVersionIface> versionIfaces) {

        // 查询工厂和物料
        Map<String, Map<String, Object>> map = new HashMap<>();
        List<String> plantCodes = versionIfaces.stream().filter(a -> Strings.isNotEmpty(a.getSiteCode())).map(ItfMaterialVersionIface::getSiteCode).distinct().collect(Collectors.toList());
        List<String> itemCodes = versionIfaces.stream().filter(a -> Strings.isNotEmpty(a.getMaterialCode())).map(ItfMaterialVersionIface::getMaterialCode).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(plantCodes)) {
            List<MtSitePlantReleation> siteList = mtInvItemIfaceMapper.selectSiteByPlantCode(tenantId, "('" + StringUtils.join(plantCodes, "','") + "')");
            Map<String, Object> site = siteList.stream().collect(Collectors.toMap(MtSitePlantReleation::getPlantCode, dto -> dto));
            map.put("site", site);
        }
        if (CollectionUtils.isNotEmpty(itemCodes)) {
            List<MtMaterial> materialList = mtInvItemIfaceMapper.selectMaterialByItemCodes(tenantId, "('" + StringUtils.join(itemCodes, "','") + "')");
            Map<String, Object> material = materialList.stream().collect(Collectors.toMap(MtMaterial::getMaterialCode, dto -> dto));
            map.put("material", material);
            // 查询物料版本
            List<String> materialIds = materialList.stream().map(MtMaterial::getMaterialId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(materialIds)) {
                List<HmeMaterialVersion> materialVersionList = hmeMaterialVersionMapper.selectMaterialVersion(tenantId, "('" + StringUtils.join(materialIds, "','") + "')");
                Map<String, Object> materialVersion = materialVersionList.stream().collect(Collectors.toMap(a -> a.getTenantId() + "-" + a.getSiteId() + "-" + a.getMaterialId() + "-" + a.getMaterialVersion(),
                        dto -> dto));
                map.put("materialVersion", materialVersion);
            }
        }
        return map;
    }
}
