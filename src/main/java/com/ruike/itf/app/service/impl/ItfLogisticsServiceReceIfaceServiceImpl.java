package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruike.itf.api.dto.ItfLogisticsServiceDTO;
import com.ruike.itf.app.service.ItfLogisticsServiceReceIfaceService;
import com.ruike.itf.domain.entity.ItfLogisticsServiceReceIface;
import com.ruike.itf.domain.repository.ItfLogisticsServiceReceIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.utils.SendESBConnect;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 售后信息回传ERP接口记录表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 10:49:32
 */
@Slf4j
@Service
public class ItfLogisticsServiceReceIfaceServiceImpl implements ItfLogisticsServiceReceIfaceService {

    @Autowired
    private ItfLogisticsServiceReceIfaceRepository receIfaceRepository;

    @Autowired
    private SendESBConnect sendESBConnect;

    /**
     * 售后信息回传ERP
     *
     * @param dto
     * @return 返回错误数据，可查看长度确认是否发送成功，存在message字段，有错误信息的存在，也可查看接口记录表itf_logistics_service_rece_iface
     * @author kejin.liu01@hand-china.com 2020/9/2 10:52
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<ItfLogisticsServiceReceIface> sendErpLogisticsMsg(List<ItfLogisticsServiceReceIface> dto, Long tenantId) {

        // 校验是否为空
        List<ItfLogisticsServiceReceIface> newData = new ArrayList<>();
        for (ItfLogisticsServiceReceIface iface : dto) {
            iface.setTenantId(tenantId);
            StringBuffer errorMsg = new StringBuffer();
            if (Strings.isEmpty(iface.getLogisticsNumber())) {
                errorMsg.append("LogisticsNumber：提单号不允许为空！");
            }
            if (Strings.isEmpty(iface.getLogisticsCompany())) {
                errorMsg.append("LogisticsCompany：物料公司不允许为空！");
            }
            if (Objects.isNull(iface.getCreationDate())) {
                errorMsg.append("CreationDate：创建时间不允许为空！");
            }
            if (Strings.isEmpty(iface.getCreatedByName())) {
                errorMsg.append("CreatedByName：收件人不允许为空！");
            }
            if (Strings.isEmpty(iface.getPlantCode())) {
                errorMsg.append("PlantCode：不允许为空！");
            }
            if (Strings.isEmpty(iface.getAreaCode())) {
                errorMsg.append("AreaCode：部门不允许为空！");
            }
            if (Strings.isEmpty(iface.getSnNum())) {
                errorMsg.append("SnNum：机器编号不允许为空！");
            }
            if (Strings.isEmpty(iface.getMaterialCode())) {
                errorMsg.append("MaterialCode：物料编码不允许为空！");
            }
            if (Strings.isNotEmpty(errorMsg)) {
                iface.setMessage(errorMsg.toString());
                iface.setIsFlag("N");
            } else {
                iface.setIsFlag("Y");
            }
            if ("N".equals(iface.getIsFlag())) {
                receIfaceRepository.insertSelective(iface);
            }

            newData.add(iface);
        }
        // 整理数据格式
        List<ItfLogisticsServiceReceIface> newList = newData.stream().filter(a -> Strings.isEmpty(a.getMessage())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(newList)) {
            return newData;
        }
        List<Map<String, Object>> sendList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        for (ItfLogisticsServiceReceIface iface : newList) {
            Map<String, Object> map = new HashMap<>(50);
            map.put("BOLNR", iface.getLogisticsNumber());
            map.put("ZWL", iface.getLogisticsCompany());
            map.put("ZSJR", iface.getCreatedByName());
            map.put("DATUM", format.format(iface.getCreationDate()));
            map.put("WERKS", iface.getPlantCode());
            map.put("PRCTR", iface.getAreaCode());
            map.put("SERNR1", iface.getSnNum());
            map.put("MATNR1", iface.getMaterialCode());
            sendList.add(map);
        }
        Map<String, Object> resultMap = sendESBConnect.sendEsb(sendList, "ZTSD001", "ItfLogisticsServiceReceIfaceServiceImpl.sendErpLogisticsMsg", ItfConstant.InterfaceCode.ESB_BIG_WAREHOUSE_REGISTER_SYNC);
        List<ItfLogisticsServiceDTO> lists = JSONArray.parseArray(JSONObject.toJSONString(resultMap.get("RETURN")), ItfLogisticsServiceDTO.class);
        for (ItfLogisticsServiceDTO serviceDTO : lists) {
            if (!"S".equals(serviceDTO.getZFLAG())) {
                throw new CommonException(serviceDTO.getZMESSAGE());
            }
            ItfLogisticsServiceReceIface iface = new ItfLogisticsServiceReceIface(serviceDTO, tenantId);
            receIfaceRepository.insertSelective(iface);
        }
        List<ItfLogisticsServiceReceIface> errorList = newData.stream().filter(a -> Strings.isEmpty(a.getMessage())).collect(Collectors.toList());
        return errorList;
    }
}
