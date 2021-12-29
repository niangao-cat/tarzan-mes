package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.api.dto.SitePlantReleationDTO;
import com.ruike.itf.domain.entity.SitePlantReleation;
import com.ruike.itf.domain.repository.SitePlantReleationRepository;
import com.ruike.itf.infra.mapper.SitePlantReleationMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.sys.MtException;
import liquibase.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.util.ArrayList;
import java.util.List;

/**
 * ERP工厂与站点映射关系 资源库实现
 *
 * @author taowen.wang@hand-china.com 2021-07-06 14:14:34
 */
@Component
public class SitePlantReleationRepositoryImpl extends BaseRepositoryImpl<SitePlantReleation> implements SitePlantReleationRepository {

    @Autowired
    private SitePlantReleationMapper sitePlantReleationMapper;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    /**
     * WCS与WMS成品SN核对接口 API
     *
     * @param plantCode
     * @return
     */
    @Override
    public SitePlantReleationDTO sitePlantReleationByPlantCode(Long tenantId, String plantCode) {
        SitePlantReleationDTO sitePlantReleationDTO = new SitePlantReleationDTO();
        try{
            // 校验参数
            if(StringUtils.isEmpty(plantCode)){
                throw new MtException("参数不能为空!");
            }
            // API逻辑
            String siteId = sitePlantReleationMapper.selectByPlantCode(plantCode);
            if (StringUtils.isEmpty(siteId)){
                throw new MtException("工厂编码为"+plantCode+"的工厂不存在!");
            }
            // 准备API:organizationLimitLocatorAllQuery参数
            MtModLocatorOrgRelVO3 mtModLocatorOrgRelVO3 = new MtModLocatorOrgRelVO3();
            mtModLocatorOrgRelVO3.setOrganizationId(siteId);
            mtModLocatorOrgRelVO3.setOrganizationType("SITE");
            // 调用API:organizationLimitLocatorAllQuery
            List<String> locatoridList1 = mtModLocatorOrgRelRepository.organizationLimitLocatorAllQuery(tenantId, mtModLocatorOrgRelVO3);
            if(CollectionUtils.isEmpty(locatoridList1)){
                throw new MtException("工厂编码为"+plantCode+"的库位为空！");
            }
            // 获取locatorid（根据库位类型为AUTO）
            List<String> locatoridList2 = sitePlantReleationMapper.selectByLoctorType();
            if(CollectionUtils.isEmpty(locatoridList2)){
                throw new MtException("立体货位维护错误!");
            }

            ArrayList<String> locatorids = new ArrayList<>();
            for (String item:locatoridList1) {
                if(locatoridList2.contains(item)){
                    locatorids.add(item);
                }
            }

            // 获取物料批编码 (条码号)
            List<String> materiaLotCodes = sitePlantReleationMapper.selectByLoctorId(locatorids);
            if(CollectionUtils.isEmpty(materiaLotCodes)){
                throw new MtException("条码号不存在!");
            }
            sitePlantReleationDTO.setMateriaLotCodes(materiaLotCodes);
            sitePlantReleationDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
            sitePlantReleationDTO.setProcessMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
            return sitePlantReleationDTO;
        }catch (Exception e){
            sitePlantReleationDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            sitePlantReleationDTO.setProcessMessage(e.getMessage());
            return sitePlantReleationDTO;
        }
    }
}
