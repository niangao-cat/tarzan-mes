package com.ruike.itf.app.service.impl;

import com.ruike.hme.domain.entity.HmeProductionVersion;
import com.ruike.hme.infra.mapper.HmeProductionVersionMapper;
import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfProductionVersionIfaceService;
import com.ruike.itf.domain.entity.ItfProductionVersionIface;
import com.ruike.itf.infra.mapper.ItfProductionVersionIfaceMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.infra.mapper.MtSitePlantReleationMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;
import tarzan.modeling.infra.mapper.MtModSiteMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 生产版本表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-08-20 12:21:46
 */
@Slf4j
@Service
public class ItfProductionVersionIfaceServiceImpl implements ItfProductionVersionIfaceService {

    @Autowired
    private ItfProductionVersionIfaceMapper itfProductionVersionIfaceMapper;

    @Autowired
    private HmeProductionVersionMapper hmeProductionVersionMapper;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtSitePlantReleationMapper mtSitePlantReleationMapper;

    @Autowired
    private MtMaterialMapper mtMaterialMapper;

    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ItfProductionVersionIface> invoke(List<ItfSapIfaceDTO> dto) {

        List<ItfProductionVersionIface> ifaces = isNull(getData(dto));
        // 插入记录表
        for (ItfProductionVersionIface iface : ifaces) {
            HmeProductionVersion hmeProductionVersion = new HmeProductionVersion();
            BeanUtils.copyProperties(iface, hmeProductionVersion);
            // 查询工厂和物料ID
            MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
            mtSitePlantReleation.setTenantId(tenantId);
            mtSitePlantReleation.setPlantCode(Strings.isEmpty(iface.getSiteCode()) ? "NULL" : iface.getSiteCode());
            List<MtSitePlantReleation> siteId = mtSitePlantReleationMapper.select(mtSitePlantReleation);
            // 查询物料ID
            MtMaterial mtMaterial = new MtMaterial();
            mtMaterial.setTenantId(tenantId);
            mtMaterial.setMaterialCode(Strings.isEmpty(iface.getMaterialCode()) ? "NULL" : iface.getMaterialCode());
            List<MtMaterial> materials = mtMaterialMapper.select(mtMaterial);

            if (siteId.size() == 0) {
                iface.setMessage(iface.getMessage() + "根据工厂编码找不到工厂ID，请检查工厂编码是否一致！");
            } else {
                hmeProductionVersion.setSiteId(siteId.get(0).getSiteId());
            }
            if (materials.size() == 0) {
                iface.setMessage(iface.getMessage() + "根据物料编码找不到物料ID，请检查物料编码是否一致！");
            }
            if (siteId.size() != 0 && materials.size() != 0) {
                MtMaterialSite mtMaterialSite = new MtMaterialSite();
                mtMaterialSite.setTenantId(tenantId);
                mtMaterialSite.setMaterialId(materials.get(0).getMaterialId());
                mtMaterialSite.setSiteId(siteId.get(0).getSiteId());
                List<MtMaterialSite> mtMaterialSites = mtMaterialSiteMapper.select(mtMaterialSite);
                if (mtMaterialSites.size() == 0) {
                    iface.setMessage(iface.getMessage() + "根据工厂ID和物料ID找不到MATERIAL_STE_ID，请检查物料和工厂是否一致！");
                } else {
                    hmeProductionVersion.setMaterialSiteId(mtMaterialSites.get(0).getMaterialSiteId());
                }
            } else {
                hmeProductionVersion.setMaterialSiteId(materials.get(0).getMaterialId());
            }
            itfProductionVersionIfaceMapper.insertSelective(iface);
            hmeProductionVersion.setProductionVersionId(iface.getIfaceId());
            if (hmeProductionVersion.getSiteId() != null && !"".equals(hmeProductionVersion.getSiteId())
                    && hmeProductionVersion.getMaterialSiteId() != null && !"".equals(hmeProductionVersion.getMaterialSiteId())
                    && Strings.isEmpty(iface.getMessage())) {
                // 查询是否存在，存在则更新
                HmeProductionVersion isNull = new HmeProductionVersion();
                isNull.setProductionVersion(hmeProductionVersion.getProductionVersion());
                isNull.setBomName(hmeProductionVersion.getBomName());
                isNull.setBomVersion(hmeProductionVersion.getBomVersion());
                isNull.setRouterName(hmeProductionVersion.getRouterName());
                isNull.setRouterVersion(hmeProductionVersion.getRouterVersion());
                isNull.setMaterialSiteId(hmeProductionVersion.getMaterialSiteId());
                List<HmeProductionVersion> select = hmeProductionVersionMapper.select(isNull);
                if (select.size() == 0) {
                    hmeProductionVersionMapper.insertSelective(hmeProductionVersion);
                } else {
                    hmeProductionVersion.setProductionVersionId(select.get(0).getProductionVersionId());
                    hmeProductionVersion.setObjectVersionNumber(select.get(0).getObjectVersionNumber());
                    hmeProductionVersionMapper.updateByPrimaryKeySelective(hmeProductionVersion);
                }
            }
        }
        List<ItfProductionVersionIface> errorList = ifaces.stream().filter(a -> !Strings.isEmpty(a.getMessage())).collect(Collectors.toList());
        return errorList;
    }

    /**
     * 判断数据是否为空
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/20 13:06
     */
    public List<ItfProductionVersionIface> isNull(List<ItfProductionVersionIface> dto) {
        // 判断是否为空
        List<ItfProductionVersionIface> data = new ArrayList<>(500);
        for (ItfProductionVersionIface iface : dto) {
            StringBuffer errorMsg = new StringBuffer();
            if ("".equals(iface.getMaterialCode())) {
                errorMsg.append("物料不允许为空！");
            }
            if ("".equals(iface.getSiteCode())) {
                errorMsg.append("工厂不允许为空！");
            }
            if ("".equals(iface.getProductionVersion())) {
                errorMsg.append("物料版本不允许为空！");
            }
            if ("".equals(iface.getBomName())) {
                errorMsg.append("物料单不允许为空！");
            }
            if ("".equals(iface.getBomVersion())) {
                errorMsg.append("物料单用途不允许为空！");
            }
            if ("".equals(iface.getRouterName())) {
                errorMsg.append("任务清单组件值不允许为空！");
            }
            if ("".equals(iface.getRouterVersion())) {
                errorMsg.append("组计数器不允许为空！");
            }
            if (iface.getDateFrom() == null) {
                errorMsg.append("有效开始时间不允许为空！");
            }
            iface.setMessage(errorMsg.toString());
            data.add(iface);
        }

        return data;
    }

    /**
     * 提取数据
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/20 13:04
     */
    public List<ItfProductionVersionIface> getData(List<ItfSapIfaceDTO> dto) {
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("hme_production_version_cid_s"));
        List<ItfProductionVersionIface> ifaces = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat();
        String batchDate = format.format(date);
        for (ItfSapIfaceDTO sapIfaceDTO : dto) {
            ItfProductionVersionIface iface = new ItfProductionVersionIface();
            iface.setMaterialCode(Strings.isEmpty(sapIfaceDTO.getMATNR()) ? "" : sapIfaceDTO.getMATNR().replaceAll("^(0+)",""));
            iface.setSiteCode(Strings.isEmpty(sapIfaceDTO.getWERKS()) ? "" : sapIfaceDTO.getWERKS());
            iface.setProductionVersion(Strings.isEmpty(sapIfaceDTO.getVERID()) ? "" : sapIfaceDTO.getVERID());
            iface.setDescription(Strings.isEmpty(sapIfaceDTO.getTEXT1()) ? "" : sapIfaceDTO.getTEXT1());
            iface.setBomName(Strings.isEmpty(sapIfaceDTO.getSTLNR()) ? "" : sapIfaceDTO.getSTLNR().replaceAll("^(0+)",""));
            iface.setBomVersion(Strings.isEmpty(sapIfaceDTO.getSTLAL()) ? "" : sapIfaceDTO.getSTLAL());
            iface.setRouterName(Strings.isEmpty(sapIfaceDTO.getPLNNR()) ? "" : sapIfaceDTO.getPLNNR());
            iface.setRouterVersion(Strings.isEmpty(sapIfaceDTO.getALNAL()) ? "" : sapIfaceDTO.getALNAL());
            iface.setLockFlag(Strings.isEmpty(sapIfaceDTO.getMKSP()) ? "" : sapIfaceDTO.getMKSP());
            iface.setDateFrom(Objects.isNull(sapIfaceDTO.getADATU()) ? null : sapIfaceDTO.getADATU());
            iface.setDateTo(Objects.isNull(sapIfaceDTO.getBDATU()) ? null : sapIfaceDTO.getBDATU());
            iface.setIfaceId(this.customDbRepository.getNextKey("hme_production_version_s"));
            iface.setTenantId(tenantId);
            iface.setCreatedBy(-1L);
            iface.setCreationDate(date);
            iface.setBatchDate(batchDate);
            iface.setLastUpdatedBy(-1L);
            iface.setLastUpdateDate(date);
            iface.setObjectVersionNumber(1L);
            iface.setCid(batchId);
            iface.setBatchId(batchId);
            ifaces.add(iface);

        }

        return ifaces;
    }
}
