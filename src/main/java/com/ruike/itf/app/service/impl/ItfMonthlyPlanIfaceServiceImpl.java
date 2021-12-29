package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.domain.entity.HmeMonthlyPlan;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.itf.api.dto.ItfMonthlyPlanIfaceDTO;
import com.ruike.itf.api.dto.ItfMonthlyPlanIfaceDTO2;
import com.ruike.itf.app.service.ItfMonthlyPlanIfaceService;
import com.ruike.itf.domain.entity.ItfMonthlyPlanIface;
import com.ruike.itf.domain.repository.ItfMonthlyPlanIfaceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.repository.MtModAreaRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 月度计划接口表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-06-01 14:21:59
 */
@Service
public class ItfMonthlyPlanIfaceServiceImpl implements ItfMonthlyPlanIfaceService {

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Autowired
    private ItfMonthlyPlanIfaceRepository itfMonthlyPlanIfaceRepository;
    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Override
    public List<ItfMonthlyPlanIfaceDTO2> invoke(Map<String, Object> monthlyPlanMap) {
        List<ItfMonthlyPlanIfaceDTO> monthlyPlanIfaceList = JSONArray.parseArray(monthlyPlanMap.get("ITEM").toString(), ItfMonthlyPlanIfaceDTO.class);

        // 判断是否为空
        if (CollectionUtils.isEmpty(monthlyPlanIfaceList)){
            return new ArrayList<>();
        }

        //将报文传入字段转换为MES字段
        List<ItfMonthlyPlanIface> itfMonthlyPlanIfaceList = new ArrayList<>();
        for (ItfMonthlyPlanIfaceDTO itfMonthlyPlanIfaceDTO:monthlyPlanIfaceList) {
            itfMonthlyPlanIfaceList.add(new ItfMonthlyPlanIface(itfMonthlyPlanIfaceDTO));
        }
        //将数据先存入接口表中，状态为N
        itfMonthlyPlanIfaceList = itfMonthlyPlanIfaceRepository.batchInsertIface(tenantId, itfMonthlyPlanIfaceList);

        Map<String, List<MtSitePlantReleation>> sitePlantReleationsMap = new HashMap<>();
        List<LovValueDTO> workshopMappingRelLov = lovAdapter.queryLovValue("wms.workshop_mapping_rel", tenantId);
        List<HmeMonthlyPlan> hmeMonthlyPlanList = new ArrayList<>();
        List<ItfMonthlyPlanIfaceDTO2> resultList = new ArrayList<>();
        Date nowDate = new Date();
        for (ItfMonthlyPlanIface itfMonthlyPlanIface:itfMonthlyPlanIfaceList) {
            String status = "S";
            String message = "";
            String siteId = null;
            String materialId = null;
            String areaId= null;
            if(StringUtils.isBlank(itfMonthlyPlanIface.getPlantCode())){
                status = "E";
                message += "[PLANT]不允许为空！";
            }else{
                //根据plantCode查询对应的siteId
                List<MtSitePlantReleation> mtSitePlantReleations = sitePlantReleationsMap.get(itfMonthlyPlanIface.getPlantCode());
                if(CollectionUtils.isEmpty(mtSitePlantReleations)){
                    mtSitePlantReleations = mtSitePlantReleationRepository.select(new MtSitePlantReleation() {{
                        setTenantId(tenantId);
                        setPlantCode(itfMonthlyPlanIface.getPlantCode());
                    }});
                    sitePlantReleationsMap.put(itfMonthlyPlanIface.getPlantCode(), mtSitePlantReleations);
                }
                if(CollectionUtils.isEmpty(mtSitePlantReleations)){
                    status = "E";
                    message += "工厂编码"+ itfMonthlyPlanIface.getPlantCode() +"不存在！";
                }else {
                    siteId = mtSitePlantReleations.get(0).getSiteId();
                }
            }
            if(StringUtils.isBlank(itfMonthlyPlanIface.getMaterialCode())){
                status = "E";
                message += "[MATERIAL]不允许为空！";
            }else {
                //校验物料编码
                MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {{
                    setTenantId(tenantId);
                    setMaterialCode(itfMonthlyPlanIface.getMaterialCode());
                }});
                if(Objects.isNull(mtMaterial)){
                    status = "E";
                    message += "物料编码"+ itfMonthlyPlanIface.getMaterialCode() +"不存在！";
                }else{
                    materialId = mtMaterial.getMaterialId();
                }
            }
            if(StringUtils.isBlank(itfMonthlyPlanIface.getProdSuper())){
                status = "E";
                message += "[PROD_SUPER]不允许为空！";
            }else {
                //校验生产车间
                List<LovValueDTO> lovValueDTOS = workshopMappingRelLov.stream().filter(item -> itfMonthlyPlanIface.getProdSuper().equals(item.getValue())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(lovValueDTOS) || StringUtils.isBlank(lovValueDTOS.get(0).getMeaning())){
                    status = "E";
                    message += "未维护车间/生产管理员映射关系！";
                }else {
                    String areaCode = lovValueDTOS.get(0).getMeaning();
                    MtModArea mtModArea = mtModAreaRepository.selectOne(new MtModArea() {{
                        setAreaCode(areaCode);
                        setTenantId(tenantId);
                    }});
                    if(Objects.isNull(mtModArea)){
                        status = "E";
                        message += "部门编码"+ areaCode +"不存在！";
                    }else {
                        areaId = mtModArea.getAreaId();
                    }
                }
            }
            if(StringUtils.isBlank(itfMonthlyPlanIface.getMonth())){
                status = "E";
                message += "[MONTH]不允许为空！";
            }
            if(Objects.isNull(itfMonthlyPlanIface.getQuantity())){
                status = "E";
                message += "[QUANTITY]不允许为空！";
            }
            itfMonthlyPlanIface.setStatus(status);
            itfMonthlyPlanIface.setMessage(message);
            if("S".equals(status)){
                HmeMonthlyPlan hmeMonthlyPlan = new HmeMonthlyPlan();
                hmeMonthlyPlan.setTenantId(tenantId);
                hmeMonthlyPlan.setSiteId(siteId);
                hmeMonthlyPlan.setBusinessId(areaId);
                hmeMonthlyPlan.setMaterialId(materialId);
                hmeMonthlyPlan.setMonth(itfMonthlyPlanIface.getMonth());
                hmeMonthlyPlan.setQuantity(itfMonthlyPlanIface.getQuantity());
                hmeMonthlyPlanList.add(hmeMonthlyPlan);
            }else if("E".equals(status)){
                ItfMonthlyPlanIfaceDTO2 itfMonthlyPlanIfaceDTO2 = new ItfMonthlyPlanIfaceDTO2();
                BeanCopierUtil.copy(itfMonthlyPlanIface, itfMonthlyPlanIfaceDTO2);
                itfMonthlyPlanIfaceDTO2.setProcessStatus(status);
                itfMonthlyPlanIfaceDTO2.setProcessMessage(message);
                itfMonthlyPlanIfaceDTO2.setProcessDate(nowDate);
                resultList.add(itfMonthlyPlanIfaceDTO2);
            }
        }
        //批量更新接口表数据状态
        itfMonthlyPlanIfaceRepository.batchUpdateIface(tenantId, itfMonthlyPlanIfaceList);
        //批量插入数据到业务表
        if(CollectionUtils.isNotEmpty(hmeMonthlyPlanList)){
            itfMonthlyPlanIfaceRepository.batchInsertMonthlyPlan(tenantId, hmeMonthlyPlanList);
        }
        return resultList;
    }
}
