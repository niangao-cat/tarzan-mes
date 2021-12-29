package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfSupplierDTO;
import com.ruike.itf.api.dto.ItfSupplierSyncDTO;
import com.ruike.itf.app.service.ItfSupplierIfaceService;
import com.ruike.itf.domain.entity.ItfSupplierIface;
import com.ruike.itf.infra.mapper.ItfSupplierIfaceMapper;
import com.ruike.itf.infra.mapper.ItfWorkOrderIfaceMapper;
import com.ruike.itf.utils.Utils;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.repository.MtSupplierIfaceRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商数据接口表应用服务默认实现
 *
 * @author yapeng.yao@hand-china.com 2020-08-19 18:49:46
 */
@Service
public class ItfSupplierIfaceServiceImpl implements ItfSupplierIfaceService {

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private ItfSupplierIfaceMapper itfSupplierIfaceMapper;

    @Autowired
    private ItfWorkOrderIfaceMapper itfWorkOrderIfaceMapper;

    @Autowired
    private MtSupplierIfaceRepository mtSupplierIfaceRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    /**
     * 供应商数据同步
     *
     * @param itfSupplierDTOList
     * @return
     */
    @Override
    public List<ItfCommonReturnDTO> invoke(List<ItfSupplierDTO> itfSupplierDTOList) {
        List<ItfCommonReturnDTO> returnDTOList = new ArrayList<>();
        Date nowDate = Utils.getNowDate();
        if (CollectionUtils.isNotEmpty(itfSupplierDTOList)) {
            // 批次时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String batchDate = format.format(nowDate);
            // 由SAP数据转换为MT数据，更换DTO集合
            List<ItfSupplierSyncDTO> supplierSyncDTOList = new ArrayList<>();
            for (int i = 0; i < itfSupplierDTOList.size(); i++) {
                supplierSyncDTOList.add(new ItfSupplierSyncDTO(itfSupplierDTOList.get(i)));
            }

            // 批次
            Double batchId = Double.valueOf(this.customDbRepository.getNextKey("mt_supplier_iface_cid_s"));
            BeanCopier copier = BeanCopier.create(ItfSupplierSyncDTO.class, ItfSupplierIface.class, false);
            //循环插入MES
            List<ItfSupplierIface> ifaceList = new ArrayList<>(supplierSyncDTOList.size());
            for (ItfSupplierSyncDTO supplierSyncDTO : supplierSyncDTOList) {
                // 供应商代码
                String supplierCode = supplierSyncDTO.getSupplierCode();
                // 供应商名称
                String supplierName = supplierSyncDTO.getSupplierName();
                // 供应商地点编号
                String supplierSiteCode = supplierSyncDTO.getSupplierSiteCode();
                // 供应商地点描述
                String supplierSiteAddress = supplierSyncDTO.getSupplierSiteAddress();
                // 地点生效日期
                String siteDateFrom = supplierSyncDTO.getSiteDateFrom();
                // 地点失效日期
                String siteDateTo = supplierSyncDTO.getSiteDateTo();
                // 创建日期
                String erpCreationDateStr = supplierSyncDTO.getErpCreationDate();

                ItfSupplierIface itfSupplierIface = new ItfSupplierIface();
                copier.copy(supplierSyncDTO, itfSupplierIface, null);

                // 校验输入参数
                itfSupplierIface = checkInputInfo(supplierCode, supplierName, supplierSiteCode, supplierSiteAddress, siteDateFrom, siteDateTo, erpCreationDateStr, itfSupplierIface, format);
                // 取主键
                String ifaceId = this.customDbRepository.getNextKey("mt_supplier_iface_s");
                Long cidId = Long.valueOf(
                        this.customDbRepository.getNextKey("mt_supplier_iface_cid_s"));

                itfSupplierIface.setTenantId(tenantId);

                itfSupplierIface.setSupplierDateFrom(nowDate);
                itfSupplierIface.setErpCreatedBy(-1L);
                itfSupplierIface.setErpLastUpdatedBy(-1L);
                itfSupplierIface.setIfaceId(ifaceId);
                itfSupplierIface.setCid(cidId);
                itfSupplierIface.setObjectVersionNumber(1L);
                itfSupplierIface.setCreatedBy(-1L);
                itfSupplierIface.setCreationDate(nowDate);
                itfSupplierIface.setLastUpdatedBy(-1L);
                itfSupplierIface.setLastUpdateDate(nowDate);
                itfSupplierIface.setBatchId(batchId);

                ifaceList.add(itfSupplierIface);
            }

            // 筛选数据
            List<ItfSupplierIface> mtIfaceList = ifaceList.stream().filter(item ->
                    "N".equals(item.getStatus())).collect(Collectors.toList());
            List<ItfSupplierIface> errIfaceList = ifaceList.stream().filter(item ->
                    !"N".equals(item.getStatus())).collect(Collectors.toList());

            // 全量插入接口表
            if (CollectionUtils.isNotEmpty(ifaceList)) {
                for (int i = 0; i < ifaceList.size(); i++) {
                    ifaceList.get(i).setBatchDate(batchDate);
                }
                // 删除当日之前批次的数据
                // 插入数据
                itfSupplierIfaceMapper.batchInsertItemIface("itf_supplier_iface", ifaceList);
            }

            // 导入校验成功数据
            if (CollectionUtils.isNotEmpty(mtIfaceList)) {
                itfSupplierIfaceMapper.batchInsertItem("mt_supplier_iface", mtIfaceList);
                // 调用API
                mtSupplierIfaceRepository.supplierInterfaceImport(tenantId);
            }
            Date newDate = new Date();
            errIfaceList.forEach(err -> {
                ItfCommonReturnDTO dto = new ItfCommonReturnDTO();
                dto.setProcessDate(newDate);
                dto.setProcessStatus(err.getStatus());
                dto.setProcessMessage(err.getMessage());
                returnDTOList.add(dto);
            });

        }
        return returnDTOList;
    }

    /**
     * 校验输入参数
     *
     * @param supplierCode
     * @param supplierName
     * @param supplierSiteCode
     * @param siteDateFrom
     * @param erpCreationDateStr
     * @param itfSupplierIface
     * @param format
     * @return
     */
    private ItfSupplierIface checkInputInfo(String supplierCode, String supplierName, String supplierSiteCode, String supplierSiteAddress, String siteDateFrom, String siteDateTo, String erpCreationDateStr, ItfSupplierIface itfSupplierIface, SimpleDateFormat format) {
        StringBuilder messageSb = new StringBuilder();
        String status = "N";
        // 校验供应商代码

        if (StringUtils.isEmpty(supplierCode)) {
            status = "E";
            messageSb.append("供应商编码不可为空").append(";");
        } else {
            supplierCode = supplierCode.replaceAll("^(0+)", "");
        }
        // 供应商名称
        if (StringUtils.isEmpty(supplierName)) {
            status = "E";
            messageSb.append("供应商描述不可为空").append(";");
        }
        // 供应商地点编号
        if (StringUtils.isEmpty(supplierSiteCode)) {
            status = "E";
            messageSb.append("供应商地点编码不可为空").append(";");
        } else {
            supplierSiteCode = supplierSiteCode.replaceAll("^(0+)", "");
        }
        // 供应商地点描述编号
        if (StringUtils.isEmpty(supplierSiteAddress)) {
            supplierSiteAddress = "无";
        }
        // 地点生效日期
        if (StringUtils.isEmpty(siteDateFrom)) {
            status = "E";
            messageSb.append("地点生效日期不可为空").append(";");
        } else {
            Date siteDateFromDate = null;
            try {
                siteDateFromDate = format.parse(siteDateFrom);
            } catch (ParseException e) {
                status = "E";
                messageSb.append("地点生效日期格式不正确").append(";");
            }
            itfSupplierIface.setSiteDateFrom(siteDateFromDate);
        }
        if (StringUtils.isNotBlank(siteDateTo)) {
            Date siteDateToDate = null;
            try {
                siteDateToDate = format.parse(siteDateTo);
            } catch (ParseException e) {
                status = "E";
                messageSb.append("地点失效日期格式不正确").append(";");
            }
            itfSupplierIface.setSiteDateTo(siteDateToDate);
        }
        if (StringUtils.isNotBlank(erpCreationDateStr)) {
            Date erpCreationDate = null;
            try {
                erpCreationDate = format.parse(erpCreationDateStr);
            } catch (ParseException e) {
                status = "E";
                messageSb.append("ERP创建日期格式不正确").append(";");
            }
            itfSupplierIface.setErpCreationDate(erpCreationDate);
            itfSupplierIface.setErpLastUpdateDate(erpCreationDate);
        } else {
            itfSupplierIface.setErpCreationDate(new Date());
            itfSupplierIface.setErpLastUpdateDate(new Date());
        }
        itfSupplierIface.setSupplierCode(supplierCode);
        itfSupplierIface.setSupplierName(supplierName);
        itfSupplierIface.setSupplierSiteCode(supplierSiteCode);
        itfSupplierIface.setSupplierSiteAddress(supplierSiteAddress);
        itfSupplierIface.setStatus(status);
        itfSupplierIface.setMessage(messageSb.toString());
        return itfSupplierIface;
    }
}
