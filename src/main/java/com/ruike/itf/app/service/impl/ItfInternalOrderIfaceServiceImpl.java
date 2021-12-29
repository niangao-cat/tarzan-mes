package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfInternalOrderIfaceService;
import com.ruike.itf.domain.entity.ItfInternalOrderIface;
import com.ruike.itf.infra.mapper.ItfInternalOrderIfaceMapper;
import com.ruike.wms.domain.entity.WmsInternalOrder;
import com.ruike.wms.infra.mapper.WmsInternalOrderMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.infra.mapper.MtSitePlantReleationMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内部订单接口表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-08-21 09:29:25
 */
@Slf4j
@Service
public class ItfInternalOrderIfaceServiceImpl implements ItfInternalOrderIfaceService {

    @Autowired
    private ItfInternalOrderIfaceMapper itfInternalOrderIfaceMapper;


    @Autowired
    private WmsInternalOrderMapper wmsInternalOrderMapper;

    @Autowired
    private MtSitePlantReleationMapper mtSitePlantReleationMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;


    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ItfInternalOrderIface> invoke(List<ItfSapIfaceDTO> dto) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String batchDate = format.format(date);
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("wms_internal_order_cid_s"));
        List<ItfInternalOrderIface> ifaces = new ArrayList<>();
        for (ItfSapIfaceDTO dto1 : dto) {
            ItfInternalOrderIface itfInternalOrderIface = new ItfInternalOrderIface();
            itfInternalOrderIface.setTenantId(tenantId);
            itfInternalOrderIface.setBatchId(String.valueOf(batchId));
            itfInternalOrderIface.setCid(batchId);
            itfInternalOrderIface.setInternalOrderId(this.customDbRepository.getNextKey("wms_internal_order_s"));
            itfInternalOrderIface.setBatchDate(batchDate);
            itfInternalOrderIface.setCreatedBy(-1L);
            itfInternalOrderIface.setCreationDate(date);
            itfInternalOrderIface.setLastUpdatedBy(-1L);
            itfInternalOrderIface.setLastUpdateDate(date);
            itfInternalOrderIface.setObjectVersionNumber(1L);
            itfInternalOrderIface.setDateFromTo(date);
            itfInternalOrderIface.setEnableFlag("Y");
            // 接口字段
            itfInternalOrderIface.setInternalOrder(Strings.isEmpty(dto1.getAUFNR()) ? "" : dto1.getAUFNR().replaceAll("^(0+)",""));
            itfInternalOrderIface.setInternalOrderType(Strings.isEmpty(dto1.getAUART()) ? "" : dto1.getAUART());
            itfInternalOrderIface.setDescription(Strings.isEmpty(dto1.getKTEXT()) ? "" : dto1.getKTEXT());
            itfInternalOrderIface.setRemark(Strings.isEmpty(dto1.getLTEXT()) ? "" : dto1.getLTEXT());
            itfInternalOrderIface.setSiteCode(Strings.isEmpty(dto1.getWERKS()) ? "" : dto1.getWERKS());
            itfInternalOrderIface.setInternalOrderStatus("NEW");
            ifaces.add(itfInternalOrderIface);
        }
        // 存数据
        List<ItfInternalOrderIface> errorList = new ArrayList<>();
        for (ItfInternalOrderIface iface : ifaces) {

            // 判断是否为空
            StringBuffer errorMsg = new StringBuffer();
            if (Strings.isEmpty(iface.getInternalOrder())) {
                errorMsg.append("订单号不允许为空！");
            }
            if (Strings.isEmpty(iface.getInternalOrderType())) {
                errorMsg.append("订单类型不允许为空！");
            }
            if (Strings.isEmpty(iface.getSiteCode())) {
                errorMsg.append("工厂不允许为空！");
            }
            if (Strings.isEmpty(iface.getInternalOrderStatus())) {
                errorMsg.append("状态不允许为空！");
            }
            // 查询工厂
            MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
            mtSitePlantReleation.setTenantId(tenantId);
            mtSitePlantReleation.setPlantCode(Strings.isEmpty(iface.getSiteCode()) ? "NULL" : iface.getSiteCode());
            List<MtSitePlantReleation> siteId = mtSitePlantReleationMapper.select(mtSitePlantReleation);
            // 存数据
            WmsInternalOrder internalOrder = new WmsInternalOrder();
            BeanUtils.copyProperties(iface, internalOrder);
            if (siteId.size() == 0) {
                errorMsg.append("根据工厂编码找不到工厂ID，请核查工厂编码！");
                iface.setMessage(errorMsg.toString());
            } else {
                internalOrder.setSiteId(siteId.get(0).getSiteId());
            }
            itfInternalOrderIfaceMapper.insertSelective(iface);
            if (Strings.isEmpty(iface.getMessage())) {
                // 查询是否存在，存在则更新
                WmsInternalOrder isNull = new WmsInternalOrder();
                isNull.setSiteId(internalOrder.getSiteId());
                isNull.setTenantId(tenantId);
                isNull.setInternalOrder(internalOrder.getInternalOrder());
                List<WmsInternalOrder> select = wmsInternalOrderMapper.select(isNull);
                if (select.size() == 0) {
                    wmsInternalOrderMapper.insertSelective(internalOrder);
                } else {
                    internalOrder.setInternalOrderId(select.get(0).getInternalOrderId());
                    internalOrder.setObjectVersionNumber(select.get(0).getObjectVersionNumber());
                    wmsInternalOrderMapper.updateByPrimaryKeySelective(internalOrder);
                }
            } else {
                errorList.add(iface);
            }
            iface.setMessage(errorMsg.toString());
        }
        return errorList;
    }
}
