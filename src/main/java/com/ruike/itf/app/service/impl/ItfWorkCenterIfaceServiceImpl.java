package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfWorkCenterIfaceService;
import com.ruike.itf.domain.entity.ItfWorkCenterIface;
import com.ruike.itf.infra.mapper.ItfWorkCenterIfaceMapper;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.domain.entity.WmsWorkCenter;
import com.ruike.wms.infra.mapper.WmsWorkCenterMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.infra.mapper.MtSitePlantReleationMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 工作中心接口记录表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-08-27 16:17:14
 */
@Service
@Slf4j
public class ItfWorkCenterIfaceServiceImpl implements ItfWorkCenterIfaceService {

    @Autowired
    private ItfWorkCenterIfaceMapper itfWorkCenterIfaceMapper;

    @Autowired
    private WmsWorkCenterMapper wmsWorkCenterMapper;

    @Autowired
    private MtSitePlantReleationMapper mtSitePlantReleationMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Override
    public List<ItfWorkCenterIface> invoke(List<ItfSapIfaceDTO> dto) {
        Date nowDate = Utils.getNowDate();
        List<ItfWorkCenterIface> workCenterIfaces = new ArrayList<>();
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("batch_cid_s"));
        for (ItfSapIfaceDTO dto1 : dto) {
            ItfWorkCenterIface itfWorkCenterIface = new ItfWorkCenterIface();
            itfWorkCenterIface.setTenantId(tenantId);
            itfWorkCenterIface.setSiteCode(dto1.getWERKS());
            itfWorkCenterIface.setWorkCenter(dto1.getARBPL());
            itfWorkCenterIface.setStartDate(dto1.getBEGDA());
            itfWorkCenterIface.setEndDate(dto1.getENDDA());
            itfWorkCenterIface.setBatchId(batchId);
            workCenterIfaces.add(itfWorkCenterIface);
        }
        // 判断必输项和查询工厂ID
        for (int i = 0; i < workCenterIfaces.size(); i++) {
            WmsWorkCenter wmsWorkCenter = new WmsWorkCenter();
            StringBuffer errorMsg = new StringBuffer();
            if (Strings.isEmpty(workCenterIfaces.get(i).getSiteCode())) {
                errorMsg.append("工厂编码不允许为空！");
            } else {
                // 查询工厂ID
                MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
                mtSitePlantReleation.setTenantId(tenantId);
                mtSitePlantReleation.setPlantCode(workCenterIfaces.get(i).getSiteCode());
                List<MtSitePlantReleation> select = mtSitePlantReleationMapper.select(mtSitePlantReleation);
                if(select.size() == 0){
                    errorMsg.append("根据工厂编码查询不到工厂ID，请核查工厂编码！");
                }else{
                    wmsWorkCenter.setSiteId(select.get(0).getSiteId());
                }
            }
            if (Strings.isEmpty(workCenterIfaces.get(i).getWorkCenter())) {
                errorMsg.append("工作中心不允许为空！");
            }
            if (Objects.isNull(workCenterIfaces.get(i).getStartDate())) {
                errorMsg.append("开始时间不允许为空！");
            }
            if (Objects.isNull(workCenterIfaces.get(i).getStartDate())) {
                errorMsg.append("结束时间不允许为空！");
            }
            workCenterIfaces.get(i).setMessage(errorMsg.toString());
            itfWorkCenterIfaceMapper.insertSelective(workCenterIfaces.get(i));
            if (StringUtils.isBlank(errorMsg)) {
                wmsWorkCenter.setTenantId(tenantId);
                wmsWorkCenter.setWorkCenter(workCenterIfaces.get(i).getWorkCenter());
                List<WmsWorkCenter> select = wmsWorkCenterMapper.select(wmsWorkCenter);
                wmsWorkCenter.setStartDate(workCenterIfaces.get(i).getStartDate());
                wmsWorkCenter.setEndDate(workCenterIfaces.get(i).getEndDate());
                wmsWorkCenter.setCreationDate(nowDate);
                wmsWorkCenter.setLastUpdateDate(nowDate);
                if (select.size() == 0) {
                    wmsWorkCenterMapper.insertSelective(wmsWorkCenter);
                } else {
                    wmsWorkCenter.setWorkCenterId(select.get(0).getWorkCenterId());
                    wmsWorkCenterMapper.updateByPrimaryKeySelective(wmsWorkCenter);
                }
            }

        }
        List<ItfWorkCenterIface> errorList = workCenterIfaces.stream().filter(a -> !Strings.isEmpty(a.getMessage())).collect(Collectors.toList());
        return errorList;
    }
}
