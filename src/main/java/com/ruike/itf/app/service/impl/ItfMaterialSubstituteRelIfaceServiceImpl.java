package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfMaterialSubstituteRelDTO;
import com.ruike.itf.api.dto.ItfMaterialSubstituteRelSyncDTO;
import com.ruike.itf.app.service.ItfMaterialSubstituteRelIfaceService;
import com.ruike.itf.domain.entity.ItfMaterialSubstituteRelIface;
import com.ruike.itf.infra.mapper.ItfMaterialSubstituteRelIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.infra.mapper.WmsMaterialSubstituteRelMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.core.base.AopProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 物料全局替代关系表应用服务默认实现
 *
 * @author yapeng.yao@hand-china.com 2020-08-18 14:40:53
 */
@Service
@Slf4j
public class ItfMaterialSubstituteRelIfaceServiceImpl implements ItfMaterialSubstituteRelIfaceService, AopProxy<ItfMaterialSubstituteRelIfaceServiceImpl> {

    private final MtCustomDbRepository customDbRepository;

    private final MtSitePlantReleationRepository sitePlantReleationRepository;

    private final MtMaterialRepository materialRepository;

    private final ItfMaterialSubstituteRelIfaceMapper itfMaterialSubstituteRelIfaceMapper;

    private final WmsMaterialSubstituteRelMapper wmsMaterialSubstituteRelMapper;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    public ItfMaterialSubstituteRelIfaceServiceImpl(MtCustomDbRepository customDbRepository, MtSitePlantReleationRepository sitePlantReleationRepository, MtMaterialRepository materialRepository, ItfMaterialSubstituteRelIfaceMapper itfMaterialSubstituteRelIfaceMapper, WmsMaterialSubstituteRelMapper wmsMaterialSubstituteRelMapper) {
        this.customDbRepository = customDbRepository;
        this.sitePlantReleationRepository = sitePlantReleationRepository;
        this.materialRepository = materialRepository;
        this.itfMaterialSubstituteRelIfaceMapper = itfMaterialSubstituteRelIfaceMapper;
        this.wmsMaterialSubstituteRelMapper = wmsMaterialSubstituteRelMapper;
    }

    /**
     * 物料全局替代关系同步
     *
     * @param msRelDtoList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<ItfCommonReturnDTO> invoke(List<ItfMaterialSubstituteRelDTO> msRelDtoList) {
        if (CollectionUtils.isEmpty(msRelDtoList)) {
            return new ArrayList<>();
        }
        List<ItfCommonReturnDTO> returnList = new ArrayList<>();
        // 判断是否为空
        // 由SAP数据转换为MT数据，更换DTO集合
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 批次时间
        String batchDate = format.format(Utils.getNowDate());
        // String --> substituteGroup
        List<ItfMaterialSubstituteRelSyncDTO> msRelSyncList = new ArrayList<>(msRelDtoList.size());
        for (ItfMaterialSubstituteRelDTO itfMaterialSubstituteRelDTO : msRelDtoList) {
            msRelSyncList.add(new ItfMaterialSubstituteRelSyncDTO(itfMaterialSubstituteRelDTO));
        }
        Map<String, List<ItfMaterialSubstituteRelSyncDTO>> map = msRelSyncList.stream().collect(Collectors.groupingBy(ItfMaterialSubstituteRelSyncDTO::getSubstituteGroup));

        // 批次
        Double batchId = Double.valueOf(this.customDbRepository.getNextKey("wms_material_substitute_rel_cid_s"));
        BeanCopier copier = BeanCopier.create(ItfMaterialSubstituteRelSyncDTO.class,
                ItfMaterialSubstituteRelIface.class, false);
        // 循环插入MES
        List<ItfMaterialSubstituteRelIface> ifaceList = new ArrayList<>(msRelDtoList.size());
        map.forEach((key, materialSubstituteRelList) -> {
            List<ItfMaterialSubstituteRelIface> ifaceSubstituteGroupList =
                    new ArrayList<>(materialSubstituteRelList.size());
            boolean errorFlag = false;
            for (int i = 0; i < materialSubstituteRelList.size(); i++) {
                ItfMaterialSubstituteRelSyncDTO materialSubstituteRel = materialSubstituteRelList.get(i);
                // 项目编号
                String sequence = materialSubstituteRel.getSequence();
                // 工厂
                String plant = materialSubstituteRel.getPlant();
                // 替代组
                String substituteGroup = materialSubstituteRel.getSubstituteGroup();
                // 物料编码
                String materialCode = materialSubstituteRel.getMaterialCode();
                // 替代组主料
                String mainMaterialCode = materialSubstituteRel.getMainMaterialCode();
                // 可替换零件的有效开始日期
                String startDateStr = materialSubstituteRel.getStartDate();
                // 冻结标识-X
                String attribute1 = materialSubstituteRel.getAttribute1();

                ItfMaterialSubstituteRelIface itfMaterialSubstituteRelIface = new ItfMaterialSubstituteRelIface();
                copier.copy(materialSubstituteRel, itfMaterialSubstituteRelIface, null);
                // 校验输入数据
                itfMaterialSubstituteRelIface = checkInputInfo(sequence, plant, substituteGroup, materialCode,
                        mainMaterialCode, startDateStr, format, itfMaterialSubstituteRelIface);
                itfMaterialSubstituteRelIface.setTenantId(tenantId);
                // 取主键
                String substituteId = this.customDbRepository.getNextKey("wms_material_substitute_rel_s");
                Long cidId = Long.valueOf(
                        this.customDbRepository.getNextKey("wms_material_substitute_rel_cid_s"));
                itfMaterialSubstituteRelIface.setSequence(sequence);
                itfMaterialSubstituteRelIface.setPlant(plant);
                itfMaterialSubstituteRelIface.setSubstituteGroup(substituteGroup);
                itfMaterialSubstituteRelIface.setMaterialCode(Strings.isEmpty(materialCode) ? "" : materialCode.replaceAll("^(0+)", ""));
                itfMaterialSubstituteRelIface.setMainMaterialCode(Strings.isEmpty(mainMaterialCode) ? "" : mainMaterialCode.replaceAll("^(0+)", ""));
                itfMaterialSubstituteRelIface.setAttribute1(attribute1);

                itfMaterialSubstituteRelIface.setSubstituteId(substituteId);
                itfMaterialSubstituteRelIface.setCid(cidId);
                itfMaterialSubstituteRelIface.setObjectVersionNumber(1L);
                itfMaterialSubstituteRelIface.setCreatedBy(-1L);
                itfMaterialSubstituteRelIface.setCreationDate(new Date());
                itfMaterialSubstituteRelIface.setLastUpdatedBy(-1L);
                itfMaterialSubstituteRelIface.setLastUpdateDate(new Date());
                itfMaterialSubstituteRelIface.setBatchId(batchId);

                ifaceSubstituteGroupList.add(itfMaterialSubstituteRelIface);
                if (!"N".equals(itfMaterialSubstituteRelIface.getStatus())) {
                    errorFlag = true;
                }
            }
            // 有报错信息,修改整组的状态
            if (errorFlag) {
                ifaceSubstituteGroupList.forEach(t -> {
                    t.setStatus("E");
                    t.setMessage("替代组：" + t.getSubstituteGroup() + ";" + t.getMessage());
                });
            }
            ifaceList.addAll(ifaceSubstituteGroupList);
        });

        // 正确数据
        List<ItfMaterialSubstituteRelIface> mtIfaceList = ifaceList.stream()
                .filter(iface -> "N".equals(iface.getStatus())).collect(Collectors.toList());
        // 错误数据
        List<ItfMaterialSubstituteRelIface> errIfaceList = ifaceList.stream()
                .filter(item -> !"N".equals(item.getStatus())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(errIfaceList)) {
            // 清空数据
            try {
                this.clearSub();
            } catch (Exception e) {
                log.error(Arrays.toString(e.getStackTrace()));
            }

            // 分批全量插入接口表
            if (CollectionUtils.isNotEmpty(ifaceList)) {
                for (int i = 0; i < ifaceList.size(); i++) {
                    ifaceList.get(i).setBatchDate(batchDate);
                }

                List<List<ItfMaterialSubstituteRelIface>> splitSqlList =
                        InterfaceUtils.splitSqlList(ifaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfMaterialSubstituteRelIface> domains : splitSqlList) {
                    // 插入表数据
                    itfMaterialSubstituteRelIfaceMapper.batchInsertMaterialSubstituteRelIface(
                            "itf_material_substitute_rel_iface", domains);
                }
            }

            // 导入校验成功数据
            if (CollectionUtils.isNotEmpty(mtIfaceList)) {
                List<List<ItfMaterialSubstituteRelIface>> splitSqlList =
                        InterfaceUtils.splitSqlList(mtIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfMaterialSubstituteRelIface> domains : splitSqlList) {
                    // 插入表数据
                    itfMaterialSubstituteRelIfaceMapper.batchInsertMaterialSubstituteRel(domains);
                }
            }
        }

        Date newDate = new Date();
        errIfaceList.forEach(err -> {
            ItfCommonReturnDTO dto = new ItfCommonReturnDTO();
            dto.setProcessDate(newDate);
            dto.setProcessStatus(err.getStatus());
            dto.setProcessMessage(err.getMessage());
            returnList.add(dto);
        });
        return returnList;
    }

    public void clearSub() {
        Future<Boolean> booleanFuture = executor.submit(() -> {
            itfMaterialSubstituteRelIfaceMapper.truncateAll();
            return true;
        });
        while (!booleanFuture.isDone()) {
        }
    }

    /**
     * 校验输入的参数
     *
     * @param sequence
     * @param plant
     * @param materialCode
     * @param mainMaterialCode
     * @param startDateStr
     * @param format
     * @param itfMaterialSubstituteRelIface
     * @return
     */
    private ItfMaterialSubstituteRelIface checkInputInfo(String sequence, String plant, String substituteGroup,
                                                         String materialCode, String mainMaterialCode, String startDateStr, SimpleDateFormat format,
                                                         ItfMaterialSubstituteRelIface itfMaterialSubstituteRelIface) {
        StringBuilder messageSb = new StringBuilder();
        String status = "N";
        // 校验替代组
        if (StringUtils.isBlank(substituteGroup)) {
            status = "E";
            messageSb.append("未输入替代组!").append(";");
        }
        // 校验项目编号
        if (StringUtils.isBlank(sequence)) {
            status = "E";
            messageSb.append("未输入项目编号!").append(";");
        }
        // 校验工厂
        if (StringUtils.isBlank(plant)) {
            status = "E";
            messageSb.append("未维护ERP工厂与MES站点关系！").append(";");
        } else {
            MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
            mtSitePlantReleation.setTenantId(tenantId);
            mtSitePlantReleation.setPlantCode(plant);
            MtSitePlantReleation mtSitePlantReleationByPlantCode =
                    sitePlantReleationRepository.selectOne(mtSitePlantReleation);
            if (Objects.isNull(mtSitePlantReleationByPlantCode)) {
                status = "E";
                messageSb.append("工厂:").append(plant).append(" 不存在！").append(";");
            } else {
                itfMaterialSubstituteRelIface.setSiteId(mtSitePlantReleationByPlantCode.getSiteId());
            }
        }
        // 校验物料编号
        if (StringUtils.isNotBlank(materialCode)) {
            materialCode = materialCode.replaceAll("^(0+)", "");
            MtMaterial mtMaterial = new MtMaterial();
            mtMaterial.setTenantId(tenantId);
            mtMaterial.setMaterialCode(materialCode);
            MtMaterial mtMaterialByMaterialCode = materialRepository.selectOne(mtMaterial);
            if (Objects.isNull(mtMaterialByMaterialCode)) {
                status = "E";
                messageSb.append("物料:").append(materialCode).append(" 不存在！").append(";");
            } else {
                itfMaterialSubstituteRelIface.setMaterialId(mtMaterialByMaterialCode.getMaterialId());
            }
        }
        // 校验替代组主料
        if (StringUtils.isNotBlank(mainMaterialCode)) {
            mainMaterialCode = mainMaterialCode.replaceAll("^(0+)", "");
            MtMaterial mtMaterial = new MtMaterial();
            mtMaterial.setTenantId(tenantId);
            mtMaterial.setMaterialCode(mainMaterialCode);
            MtMaterial mtMaterialByMainMaterialCode = materialRepository.selectOne(mtMaterial);
            if (Objects.isNull(mtMaterialByMainMaterialCode)) {
                status = "E";
                messageSb.append("替代组主料不存在！").append(";");
            } else {
                itfMaterialSubstituteRelIface.setMainMaterialId(mtMaterialByMainMaterialCode.getMaterialId());
            }
        }
        Date startDate = null;
        // 非空时，校验可替换零件的有效开始日期
        if (!StringUtils.isBlank(startDateStr)) {
            try {
                startDate = format.parse(startDateStr);
            } catch (ParseException e) {
                status = "E";
                messageSb.append("可替换零件的有效开始日期,格式不正确").append(";");
            }
        }
        itfMaterialSubstituteRelIface.setStatus(status);
        itfMaterialSubstituteRelIface.setMessage(messageSb.toString());
        itfMaterialSubstituteRelIface.setStartDate(startDate);
        return itfMaterialSubstituteRelIface;
    }
}
