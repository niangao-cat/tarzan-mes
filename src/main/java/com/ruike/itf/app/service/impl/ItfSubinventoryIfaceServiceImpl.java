package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfSubinventoryIfaceService;
import com.ruike.itf.domain.entity.ItfSubinventoryIface;
import com.ruike.itf.domain.repository.ItfSubinventoryIfaceRepository;
import com.ruike.itf.utils.Utils;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.entity.MtSubinventoryIface;
import tarzan.iface.domain.repository.MtSubinventoryIfaceRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ERP子库存接口表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-08-18 10:52:45
 */
@Service
@Slf4j
public class ItfSubinventoryIfaceServiceImpl implements ItfSubinventoryIfaceService {

    @Autowired
    private ItfSubinventoryIfaceRepository itfSubinventoryIfaceRepository;

    @Autowired
    private MtSubinventoryIfaceRepository mtSubinventoryIfaceRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    /**
     * 库存地点同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @Override
    public List<ItfSubinventoryIface> invoke(List<ItfSapIfaceDTO> dto) {
        Long cidAndBatchId = Long.valueOf(customDbRepository.getNextKey("mt_subinventory_iface_cid_s"));
        List<ItfSubinventoryIface> ifaces = new ArrayList<>(1000);
        List<ItfSubinventoryIface> errorList = new ArrayList<>(500);
        Date createDate = Utils.getNowDate();
        for (ItfSapIfaceDTO dto1 : dto) {
            ItfSubinventoryIface iface = new ItfSubinventoryIface();
            MtSubinventoryIface mtIface = new MtSubinventoryIface();
            iface.setTenantId(tenantId);
            iface.setBatchId(Double.valueOf(cidAndBatchId));
            iface.setStatus("N");
            iface.setEnableFlag("Y");
            iface.setSubinventoryCode(Strings.isEmpty(dto1.getLGORT()) ? "" : dto1.getLGORT());
            iface.setDescription(Strings.isEmpty(dto1.getLGOBE()) ? "" : dto1.getLGOBE());
            iface.setPlantCode(Strings.isEmpty(dto1.getWERKS()) ? "" : dto1.getWERKS());
            iface.setErpCreatedBy(-1L);
            iface.setErpCreationDate(createDate);
            iface.setErpLastUpdatedBy(-1L);
            iface.setErpLastUpdateDate(createDate);
            iface.setObjectVersionNumber(1L);
            iface.setCreatedBy(-1L);
            iface.setCreationDate(createDate);
            iface.setLastUpdatedBy(-1L);
            iface.setLastUpdateDate(createDate);
            iface.setStatus("N");
            iface.setCid(cidAndBatchId);
            String ifaceId = customDbRepository.getNextKey("mt_subinventory_iface_s");
            iface.setIfaceId(ifaceId);
            if ("".equals(iface.getPlantCode()) || "".equals(iface.getSubinventoryCode())) {
                iface.setMessage("工厂编码或者库存地点编码不允许为空，请检查!");
                errorList.add(iface);
                ifaces.add(iface);
                itfSubinventoryIfaceRepository.insertSelective(iface);
                continue;
            }
            BeanUtils.copyProperties(iface, mtIface);
            itfSubinventoryIfaceRepository.insertSelective(iface);
            mtSubinventoryIfaceRepository.insertSelective(mtIface);
            ifaces.add(iface);
        }
        mtSubinventoryIfaceRepository.subinventoryInterfaceImport(tenantId);
        return errorList;
    }

}
