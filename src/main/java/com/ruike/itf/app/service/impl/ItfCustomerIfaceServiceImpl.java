package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfCustomerDTO;
import com.ruike.itf.app.service.ItfCustomerIfaceService;
import com.ruike.itf.domain.entity.ItfCustomerIface;
import com.ruike.itf.domain.repository.ItfCustomerIfaceRepository;
import com.ruike.itf.infra.mapper.ItfCustomerIfaceMapper;
import com.ruike.itf.infra.mapper.ItfWorkOrderIfaceMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.repository.MtCustomerIfaceRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客户数据全量接口表应用服务默认实现
 *
 * @author yapeng.yao@hand-china.com 2020-08-21 11:19:59
 */
@Service
public class ItfCustomerIfaceServiceImpl implements ItfCustomerIfaceService {

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private ItfCustomerIfaceRepository itfCustomerIfaceRepository;

    @Autowired
    private ItfCustomerIfaceMapper itfCustomerIfaceMapper;

    @Autowired
    private MtCustomerIfaceRepository mtCustomerIfaceRepository;

    @Autowired
    private ItfWorkOrderIfaceMapper itfWorkOrderIfaceMapper;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    /**
     * 客户数据同步接口
     *
     * @param dtos
     * @return
     */
    @Override
    public List<ItfCommonReturnDTO> invoke(Map dtos) {
        if (dtos == null || dtos.size() == 0) {
            return new ArrayList<>();
        }
        List<ItfCustomerDTO> itfSapIfaceDTOList = JSONArray.parseArray(JSON.toJSONString(dtos.get("BASEINFO")), ItfCustomerDTO.class);
        if(CollectionUtils.isEmpty(itfSapIfaceDTOList)){
            return new ArrayList<>();
        }
        List<ItfCommonReturnDTO> returnDTOList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String batchDate = format.format(new Date());
        Double batchId = Double.valueOf(this.customDbRepository.getNextKey("mt_customer_iface_b_s"));
        Date nowDate = new Date();
        List<ItfCustomerIface> ifaceList = new ArrayList<>(itfSapIfaceDTOList.size());
        //循环插入MES
        for (ItfCustomerDTO itfCustomerDTO : itfSapIfaceDTOList) {
            ItfCustomerIface itfCustomerIface = getItfCustomerIface(itfCustomerDTO, format, batchDate, batchId, nowDate);
            ifaceList.add(itfCustomerIface);
        }

        // 筛选数据
        List<ItfCustomerIface> mtIfaceList = ifaceList.stream().filter(item ->
                "N".equals(item.getStatus())).collect(Collectors.toList());
        List<ItfCustomerIface> errIfaceList = ifaceList.stream().filter(item ->
                !"N".equals(item.getStatus())).collect(Collectors.toList());

        // 全量插入接口表
        if (CollectionUtils.isNotEmpty(ifaceList)) {
            // 插入数据
            itfCustomerIfaceRepository.batchInsert(ifaceList);
        }
        // 导入校验成功数据
        if (CollectionUtils.isNotEmpty(mtIfaceList)) {
            itfCustomerIfaceMapper.batchInsertCustomer("mt_customer_iface", mtIfaceList);
        }
        errIfaceList.forEach(err -> {
            ItfCommonReturnDTO dto = new ItfCommonReturnDTO();
            dto.setProcessDate(nowDate);
            dto.setProcessStatus(err.getStatus());
            dto.setProcessMessage(err.getMessage());
            returnDTOList.add(dto);
        });

        // 调用API
        mtCustomerIfaceRepository.customerInterfaceImport(tenantId);
        return returnDTOList;
    }

    /**
     * 将itfSapIfaceDTO转换成ItfCustomerIface
     *
     * @param itfCustomerDTO
     * @param format
     * @param batchDate
     * @param batchId
     * @param nowDate
     * @return
     */
    private ItfCustomerIface getItfCustomerIface(ItfCustomerDTO itfCustomerDTO, SimpleDateFormat format, String batchDate, Double batchId, Date nowDate) {
        ItfCustomerIface itfCustomerIface = new ItfCustomerIface();
        // 校验
        itfCustomerIface = checkInputInfo(itfCustomerDTO, itfCustomerIface, format, nowDate);
        itfCustomerIface.setTenantId(tenantId);
        itfCustomerIface.setBatchDate(batchDate);
        itfCustomerIface.setBatchId(batchId);
        Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_customer_iface_cid_s"));
        itfCustomerIface.setCid(cidId);
        itfCustomerIface.setIfaceId(this.customDbRepository.getNextKey("mt_customer_iface_s"));
        itfCustomerIface.setCustomerNameAlt(itfCustomerDTO.getSORT1());
        itfCustomerIface.setAddress(itfCustomerDTO.getSTREET());
        itfCustomerIface.setCountry(itfCustomerDTO.getLANDX());
        itfCustomerIface.setProvince(itfCustomerDTO.getREGION());
        itfCustomerIface.setCity(itfCustomerDTO.getBEZEI());
        itfCustomerIface.setContactPhoneNumber(itfCustomerDTO.getTEL_NUMBER());
        itfCustomerIface.setContactPerson(itfCustomerDTO.getBUILDING());

        //itfCustomerIface.setErpLastUpdateDate();
        itfCustomerIface.setErpCreatedBy(-1L);
        itfCustomerIface.setErpLastUpdatedBy(-1L);
        itfCustomerIface.setCreatedBy(-1L);
        itfCustomerIface.setCreationDate(nowDate);
        itfCustomerIface.setLastUpdatedBy(-1L);
        itfCustomerIface.setLastUpdateDate(nowDate);
        itfCustomerIface.setObjectVersionNumber(1L);
        return itfCustomerIface;
    }

    /**
     * 校验输入的数据
     *
     * @param itfCustomerDTO
     * @param itfCustomerIface
     * @param format
     * @param nowDate
     */
    private ItfCustomerIface checkInputInfo(ItfCustomerDTO itfCustomerDTO, ItfCustomerIface itfCustomerIface, SimpleDateFormat format, Date nowDate) {
        // 客户编码
        String kunnr = itfCustomerDTO.getKUNNR();
        // 客户描述
        String name1 = itfCustomerDTO.getNAME1();
        // 地点编码
        String adrnr = itfCustomerDTO.getADRNR();
        // 客户生效日期
        String erdat = itfCustomerDTO.getERDAT();
        // 客户失效日期
        String updat = itfCustomerDTO.getUPDAT();
        // 地点生效日期
        String dateFrom = itfCustomerDTO.getDATE_FROM();
        // 地点失效日期
        String dateTo = itfCustomerDTO.getDATE_TO();
        String status = "N";
        StringBuilder errMsg = new StringBuilder();
        // 客户编码
        if (StringUtils.isBlank(kunnr)) {
            status = "E";
            errMsg.append("KUNNR, 客户编码不可为空！").append(";");
        } else {
            itfCustomerIface.setCustomerCode(kunnr.replaceAll("^(0+)", ""));
        }
        // 客户描述
        if (StringUtils.isBlank(name1)) {
            name1 = "无";
        }
        // 地点编码
        if (StringUtils.isBlank(adrnr)) {
            itfCustomerIface.setCustomerSiteCode(itfCustomerIface.getCustomerCode());
        } else {
            itfCustomerIface.setCustomerSiteCode(adrnr.replaceAll("^(0+)", ""));

        }
        // 客户生效日期
        if (StringUtils.isNotBlank(erdat)) {
            Date erdatDate = null;
            try {
                erdatDate = format.parse(erdat);
            } catch (ParseException e) {
                status = "E";
                errMsg.append("客户生效日期格式不正确").append(";");
            }
            itfCustomerIface.setCustomerDateFrom(erdatDate);
            itfCustomerIface.setErpCreationDate(erdatDate);
            if (StringUtils.isBlank(itfCustomerDTO.getUPDAT())) {
                itfCustomerIface.setErpLastUpdateDate(erdatDate);
            }
        } else {
            itfCustomerIface.setCustomerDateFrom(nowDate);
            itfCustomerIface.setErpLastUpdateDate(nowDate);
            itfCustomerIface.setErpCreationDate(nowDate);
        }
        // 客户失效时间
        if (StringUtils.isNotBlank(updat)) {
            Date updatDate = null;
            try {
                updatDate = format.parse(updat);
            } catch (ParseException e) {
                status = "E";
                errMsg.append("客户失效日期格式不正确").append(";");
            }
            itfCustomerIface.setErpLastUpdateDate(updatDate);
        }
        // 地点生效日期
        if (StringUtils.isNotBlank(dateFrom)) {
            Date dateFromDate = null;
            try {
                dateFromDate = format.parse(dateFrom);
            } catch (ParseException e) {
                status = "E";
                errMsg.append("地点生效日期格式不正确").append(";");
            }
            itfCustomerIface.setSiteDateFrom(dateFromDate);
        } else {
            itfCustomerIface.setSiteDateFrom(nowDate);
        }
        // 地点失效日期
        if (StringUtils.isNotBlank(dateTo)) {
            Date dateToDate = null;
            try {
                dateToDate = format.parse(dateTo);
            } catch (ParseException e) {
                status = "E";
                errMsg.append("地点失效日期格式不正确").append(";");
            }
            itfCustomerIface.setSiteDateTo(dateToDate);
        }
        itfCustomerIface.setStatus(status);
        itfCustomerIface.setMessage(errMsg.toString());

//        itfCustomerIface.setCustomerCode(kunnr);//去掉开始的0 ADD BY WENZHANG.YU
        itfCustomerIface.setCustomerName(name1);
        //itfCustomerIface.setCustomerSiteCode(adrnr);去掉开始的0 ADD BY WENZHANG.YU
        return itfCustomerIface;
    }
}