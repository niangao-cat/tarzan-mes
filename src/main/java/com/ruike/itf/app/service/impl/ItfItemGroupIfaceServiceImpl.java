package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfInvItemSyncDTO;
import com.ruike.itf.api.dto.ItfItemGroupReturnDTO;
import com.ruike.itf.api.dto.ItfItemGroupSyncDTO;
import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfItemGroupIfaceService;
import com.ruike.itf.domain.entity.ItfItemGroupIface;
import com.ruike.itf.infra.mapper.ItfItemGroupIfaceMapper;
import com.ruike.itf.infra.mapper.ItfWorkOrderIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.entity.WmsItemGroup;
import com.ruike.wms.domain.repository.WmsItemGroupRepository;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料组接口表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 10:12:42
 */
@Service
public class ItfItemGroupIfaceServiceImpl implements ItfItemGroupIfaceService {

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ItfItemGroupIfaceMapper itfItemGroupIfaceMapper;

    @Autowired
    private ItfWorkOrderIfaceMapper itfWorkOrderIfaceMapper;

    @Autowired
    private WmsItemGroupRepository wmsItemGroupRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Override
    public List<ItfItemGroupReturnDTO> invoke(List<ItfSapIfaceDTO> sapIfaceDTOS) {
        if (CollectionUtils.isEmpty(sapIfaceDTOS)) {
            return new ArrayList<>();
        }
        // 由SAP数据转换为MT数据，更换DTO集合
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String batchDate = format.format(new Date());// 批次时间

        List<ItfItemGroupSyncDTO> itemGroupList = new ArrayList<>();
        for (int i = 0; i < sapIfaceDTOS.size(); i++) {
            itemGroupList.add(new ItfItemGroupSyncDTO(sapIfaceDTOS.get(i)));
        }
        // 校验不通过数据
        List<ItfItemGroupReturnDTO> returnList = new ArrayList<>();
        try {
            // 批次
            Long batchId = Long.valueOf(this.customDbRepository.getNextKey("itf_item_group_iface_cid_s"));
            List<ItfItemGroupIface> ifaceList = new ArrayList<>();
            for (ItfItemGroupSyncDTO dto : itemGroupList) {
                String status = "N";
                String message = "";
                String itemGroupCode = dto.getItemGroupCode();
                // 校验接口表必输字段不能为空
                if (StringUtils.isEmpty(itemGroupCode)) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0001", "ITF", "itemGroupCode");
                }
                ItfItemGroupIface itfItemGroupIface = new ItfItemGroupIface();
                itfItemGroupIface.setTenantId(tenantId);
                itfItemGroupIface.setItemGroupCode(dto.getItemGroupCode());
                itfItemGroupIface.setItemGroupDescription(dto.getItemGroupDescription());
                itfItemGroupIface.setBatchId(batchId);
                itfItemGroupIface.setStatus(status);
                itfItemGroupIface.setMessage(message);
                String ifaceId = this.customDbRepository.getNextKey("itf_item_group_iface_s");
                itfItemGroupIface.setIfaceId(ifaceId);
                itfItemGroupIface.setObjectVersionNumber(1L);
                itfItemGroupIface.setCreatedBy(-1L);
                itfItemGroupIface.setCreationDate(new Date());
                itfItemGroupIface.setLastUpdatedBy(-1L);
                itfItemGroupIface.setLastUpdateDate(new Date());
                ifaceList.add(itfItemGroupIface);
            }
            // 分批全量插入接口表
            if (CollectionUtils.isNotEmpty(ifaceList)) {
                for (int i = 0; i < ifaceList.size(); i++) {
                    ifaceList.get(i).setBatchDate(batchDate);
                }
                List<List<ItfItemGroupIface>> splitSqlList = InterfaceUtils.splitSqlList(ifaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfItemGroupIface> domains : splitSqlList) {
                    itfItemGroupIfaceMapper.batchInsertItemGroup(domains);
                }
            }
            Date newDate = new Date();
            // 筛选数据
            List<ItfItemGroupIface> errIfaceList = ifaceList.stream().filter(item ->
                    !"N".equals(item.getStatus())).collect(Collectors.toList());
            errIfaceList.forEach(err -> {
                ItfItemGroupReturnDTO dto = new ItfItemGroupReturnDTO();
                dto.setItemGroupCode(err.getItemGroupCode());
                dto.setItemGroupDescription(err.getItemGroupDescription());
                dto.setProcessDate(newDate);
                dto.setProcessStatus(err.getStatus());
                dto.setProcessMessage(err.getMessage());
                returnList.add(dto);
            });
            for (ItfItemGroupIface iface : ifaceList) {
                WmsItemGroup wmsItemGroup = new WmsItemGroup();
                BeanUtils.copyProperties(iface,wmsItemGroup);
                if("N".equals(iface.getStatus())){

                    // 查询是否存在，已经存在的更新处理
                    WmsItemGroup isNull = new WmsItemGroup();
                    isNull.setTenantId(tenantId);
                    isNull.setItemGroupCode(iface.getItemGroupCode());
                    List<WmsItemGroup> wmsItemGroups = wmsItemGroupRepository.select(isNull);
                    if(wmsItemGroups.size() == 0){
                        wmsItemGroup.setItemGroupId(customDbRepository.getNextKey("wms_item_group_s"));
                        wmsItemGroupRepository.insertSelective(wmsItemGroup);
                    }else{
                        wmsItemGroup.setItemGroupId(wmsItemGroups.get(0).getItemGroupId());
                        wmsItemGroup.setObjectVersionNumber(wmsItemGroups.get(0).getObjectVersionNumber());
                        wmsItemGroupRepository.updateByPrimaryKeySelective(wmsItemGroup);
                    }

                }
            }
        } catch (Exception e) {
            throw new MtException("ITF_INV_ITEM_0005",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "ITF_INV_ITEM_0005",
                            "ITF", e.getMessage()));
        }
        return returnList;
    }
}
