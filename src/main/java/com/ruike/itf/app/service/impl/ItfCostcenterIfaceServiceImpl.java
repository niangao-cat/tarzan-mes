package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfCostcenterIfaceService;
import com.ruike.itf.domain.entity.ItfCostcenterIface;
import com.ruike.itf.infra.mapper.ItfCostcenterIfaceMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtCostcenterIface;
import tarzan.iface.domain.repository.MtCostcenterIfaceRepository;
import tarzan.iface.infra.mapper.MtCostcenterIfaceMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 成本中心数据接口记录表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-08-24 09:19:52
 */
@Slf4j
@Service
public class ItfCostcenterIfaceServiceImpl implements ItfCostcenterIfaceService {

    @Autowired
    private ItfCostcenterIfaceMapper itfCostcenterIfaceMapper;

    @Autowired
    private MtCostcenterIfaceMapper mtCostcenterIfaceMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtCostcenterIfaceRepository mtCostcenterIfaceRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    /**
     * 成本中心同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @Override
    public List<ItfCostcenterIface> invoke(List<ItfSapIfaceDTO> dto) {
        if(CollectionUtils.isEmpty(dto)){
            return new ArrayList<>();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String batchDate = format.format(date);
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("mt_costcenter_iface_cid_s"));
        List<String> ifaceIdS = this.customDbRepository.getNextKeys("mt_costcenter_iface_s", dto.size());
        List<ItfCostcenterIface> ifaces = new ArrayList<>();
        List<ItfCostcenterIface> errorList = new ArrayList<>();
        // 获取数据
        for (ItfSapIfaceDTO dto1 : dto) {
            ItfCostcenterIface itfCostcenterIface = new ItfCostcenterIface();
            itfCostcenterIface.setIfaceId(ifaceIdS.remove(0));
            itfCostcenterIface.setBatchId(Double.valueOf(batchId));
            itfCostcenterIface.setBatchDate(batchDate);
            itfCostcenterIface.setTenantId(tenantId);
            itfCostcenterIface.setCid(batchId);
            itfCostcenterIface.setStatus("N");
            itfCostcenterIface.setErpCreatedBy(-1L);
            itfCostcenterIface.setErpCreationDate(date);
            itfCostcenterIface.setErpLastUpdatedBy(-1L);
            itfCostcenterIface.setErpLastUpdateDate(date);
            itfCostcenterIface.setObjectVersionNumber(1L);
            itfCostcenterIface.setCreatedBy(-1L);
            itfCostcenterIface.setCreationDate(date);
            itfCostcenterIface.setLastUpdatedBy(-1L);
            itfCostcenterIface.setLastUpdateDate(date);
            // 接口字段
            itfCostcenterIface.setPlantCode(Strings.isEmpty(dto1.getBUKRS()) ? "" : dto1.getBUKRS());
            itfCostcenterIface.setCostcenterCode(Strings.isEmpty(dto1.getKOSTL()) ? "" : dto1.getKOSTL());
            itfCostcenterIface.setDescription(Strings.isEmpty(dto1.getLTEXT()) ? "" : dto1.getLTEXT());
            itfCostcenterIface.setDateFromTo(Objects.isNull(dto1.getDATAB()) ? null : dto1.getDATAB());
            itfCostcenterIface.setDateEndTo(Objects.isNull(dto1.getDATBI()) ? null : dto1.getDATBI());
            // add 刘克金，王康 2020年11月03日16:01:51
            itfCostcenterIface.setCostcenterType(Strings.isEmpty(dto1.getKOSAR()) ? "" : dto1.getKOSAR());
            // 判断是否为空
            StringBuffer errorMdg = new StringBuffer();
            if ("".equals(itfCostcenterIface.getPlantCode())) {
                errorMdg.append("工厂不允许为空！");
            }
            if ("".equals(itfCostcenterIface.getCostcenterCode())) {
                errorMdg.append("成本中心编码不允许为空！");
            }
            if ("".equals(itfCostcenterIface.getDescription())) {
                errorMdg.append("成本中心描述不允许为空！");
            }
            if (itfCostcenterIface.getDateFromTo() == null) {
                errorMdg.append("地点生效时间不允许为空！");
            }

            if (itfCostcenterIface.getDateEndTo() == null) {
                errorMdg.append("地点失效时间不允许为空！");
            }
            if ("".equals(itfCostcenterIface.getCostcenterType())) {
                errorMdg.append("成本中心类型不允许为空！");
            }
            itfCostcenterIface.setMessage(errorMdg.toString());
            if (Strings.isEmpty(itfCostcenterIface.getMessage())) {
                MtCostcenterIface mtCostcenterIface = new MtCostcenterIface();
                BeanUtils.copyProperties(itfCostcenterIface, mtCostcenterIface);
                itfCostcenterIfaceMapper.insertSelective(itfCostcenterIface);
                mtCostcenterIfaceMapper.insertSelective(mtCostcenterIface);
                ifaces.add(itfCostcenterIface);
            } else {
                itfCostcenterIfaceMapper.insertSelective(itfCostcenterIface);
                errorList.add(itfCostcenterIface);
            }
        }
        mtCostcenterIfaceRepository.costCenterInterfaceImport(tenantId,batchId);
        log.info("<==== ItfCostcenterIface requestData Payload: {}", ifaces.size());
        log.info("<==== ItfCostcenterIface requestData Error Payload: {}", errorList.size());

        return errorList;
    }
}
