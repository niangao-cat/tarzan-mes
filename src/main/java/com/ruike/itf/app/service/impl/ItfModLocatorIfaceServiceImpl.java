package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfModLocatorIfaceSyncDTO;
import com.ruike.itf.app.service.ItfModLocatorIfaceService;
import com.ruike.itf.domain.entity.ItfModLocatorIface;
import com.ruike.itf.domain.entity.ItfSubinventoryIface;
import com.ruike.itf.domain.repository.ItfModLocatorIfaceRepository;
import com.ruike.itf.utils.GetDeclaredFields;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModLocatorOrgRel;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * 仓库接口记录表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-09-07 15:05:17
 */
@Service
public class ItfModLocatorIfaceServiceImpl implements ItfModLocatorIfaceService {
    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private ItfModLocatorIfaceRepository itfModLocatorIfaceRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    /**
     * 仓库同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ItfModLocatorIfaceSyncDTO> invoke(List<ItfModLocatorIfaceSyncDTO> dto) {
        String batchId = customDbRepository.getNextKey("mt_mod_locator_cid_s");
        List<ItfModLocatorIfaceSyncDTO> returnDTO = new ArrayList<>();
        GetDeclaredFields<ItfModLocatorIfaceSyncDTO> fields = new GetDeclaredFields<>();
        String[] strings = {"LGORT", "LGOBE", "WERKS"};
        for (ItfModLocatorIfaceSyncDTO ifaceSyncDTO : dto) {
            ItfModLocatorIface iface = new ItfModLocatorIface();
            String kidId = customDbRepository.getNextKey("mt_mod_locator_s");
            iface.setIfaceId(kidId);
            iface.setTenantId(tenantId);
            iface.setBatchId(Long.valueOf(batchId));
            iface.setCid(batchId);
            iface.setLgort(ifaceSyncDTO.getLGORT());
            iface.setLgobe(ifaceSyncDTO.getLGOBE());
            iface.setWerks(ifaceSyncDTO.getWERKS());
            // 判断是否为空，不为空则存储数据,跳过后续逻辑
            List<String> declaredFields = fields.getDeclaredFields(ifaceSyncDTO, strings);
            if (CollectionUtils.isNotEmpty(declaredFields)) {
                iface.setZflag(declaredFields.toString());
                iface.setZmessage("E");
                itfModLocatorIfaceRepository.insertSelective(iface);
                ifaceSyncDTO.setZFLAG("E");
                ifaceSyncDTO.setZMESSAGE(declaredFields.toString() + "不可以为空！");
                returnDTO.add(ifaceSyncDTO);
                continue;
            }
            // 查询工厂
            MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
            mtSitePlantReleation.setPlantCode(iface.getWerks());
            mtSitePlantReleation.setTenantId(tenantId);
            List<MtSitePlantReleation> mtSitePlantReleations = mtSitePlantReleationRepository.select(mtSitePlantReleation);
            if (CollectionUtils.isEmpty(mtSitePlantReleations)) {
                iface.setZflag("根据[WERKS]工厂编码查询不到工厂ID，请与MES核对！");
                iface.setZmessage("E");
                itfModLocatorIfaceRepository.insertSelective(iface);
                ifaceSyncDTO.setZFLAG("E");
                ifaceSyncDTO.setZMESSAGE("根据[WERKS]工厂编码查询不到工厂ID，请与MES核对！");
                returnDTO.add(ifaceSyncDTO);
                continue;
            }
            iface.setZflag("Y");
            iface.setZmessage("成功.");
            itfModLocatorIfaceRepository.insertSelective(iface);
            // 插入仓库
            MtModLocator mtModLocator = new MtModLocator();
            mtModLocator.setTenantId(tenantId);
            mtModLocator.setLocatorCode(iface.getLgort());
            // 查询仓库是否存在，存在则更新
            List<MtModLocator> mtModLocators = mtModLocatorRepository.select(mtModLocator);
            mtModLocator.setLocatorId(kidId);
            mtModLocator.setLocatorName(iface.getLgobe());
            mtModLocator.setLocatorType("7");
            mtModLocator.setEnableFlag("Y");
            mtModLocator.setNegativeFlag("N");
            mtModLocator.setLocatorCategory("AREA");
            mtModLocator.setCid(Long.valueOf(batchId));
            if (CollectionUtils.isEmpty(mtModLocators)) {
                mtModLocatorRepository.insertSelective(mtModLocator);
            } else {
                MtModLocator mtModLocator1 = mtModLocators.get(0);
                mtModLocator1.setLocatorName(mtModLocator.getLocatorName());
                mtModLocatorRepository.updateByPrimaryKeySelective(mtModLocator1);
            }
            // 插入仓库关系表
            MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
            mtModLocatorOrgRel.setTenantId(tenantId);
            mtModLocatorOrgRel.setOrganizationType("SITE");
            mtModLocatorOrgRel.setOrganizationId(mtSitePlantReleations.get(0).getSiteId());
            mtModLocatorOrgRel.setLocatorId(mtModLocator.getLocatorId());
            mtModLocatorOrgRel.setTenantId(tenantId);
            // 查询仓库关系表是否存在，存在则更新
            List<MtModLocatorOrgRel> mtModLocatorOrgRels = mtModLocatorOrgRelRepository.select(mtModLocatorOrgRel);
            mtModLocatorOrgRel.setLocatorOrganizationRelId(customDbRepository.getNextKey("mt_mod_locator_org_rel_s"));
            mtModLocatorOrgRel.setCid(Long.valueOf(customDbRepository.getNextKey("mt_mod_locator_org_rel_cid_s")));
            if (CollectionUtils.isEmpty(mtModLocatorOrgRels)) {
                mtModLocatorOrgRelRepository.insertSelective(mtModLocatorOrgRel);
            } else {
                mtModLocatorOrgRel.setLocatorOrganizationRelId(mtModLocatorOrgRels.get(0).getLocatorOrganizationRelId());
                mtModLocatorOrgRel.setObjectVersionNumber(mtModLocatorOrgRels.get(0).getObjectVersionNumber());
                mtModLocatorOrgRelRepository.updateByPrimaryKeySelective(mtModLocatorOrgRel);
            }

        }
        return returnDTO;
    }
}
