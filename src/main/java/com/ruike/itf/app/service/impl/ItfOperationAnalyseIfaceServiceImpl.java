package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfOperationAnalyseDTO;
import com.ruike.itf.app.service.ItfOperationAnalyseIfaceService;
import com.ruike.itf.infra.mapper.ItfOperationAnalyseIfaceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.infra.mapper.MtTagMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.infra.mapper.MtMaterialMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItfOperationAnalyseIfaceServiceImpl implements ItfOperationAnalyseIfaceService {
    @Autowired
    private MtMaterialLotMapper materialLotMapper;

    @Autowired
    private ItfOperationAnalyseIfaceMapper itfOperationAnalyseIfaceMapper;

    @Autowired
    private MtMaterialMapper mtMaterialMapper;

    @Autowired
    private MtTagMapper mtTagMapper;

    @Override
    public ItfOperationAnalyseDTO invokeA(Long tenantId, ItfOperationAnalyseDTO.QueryDTO dto) {

        ItfOperationAnalyseDTO operationAnalyseDTO = new ItfOperationAnalyseDTO();

        //工序校验
        if (CollectionUtils.isNotEmpty(dto.getProcess())){
            List<ItfOperationAnalyseDTO.Process> processes = itfOperationAnalyseIfaceMapper.queryWorkcellByProcessName(tenantId,dto.getProcess());
            List<String> ops = new ArrayList<>(dto.getProcess());
            if (CollectionUtils.isNotEmpty(processes)) {
                ops.removeAll(processes.stream().map(ItfOperationAnalyseDTO.Process::getProcessName).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(ops)){
                operationAnalyseDTO.setMessage("工序："+StringUtils.join(ops,",")+"不存在");
                operationAnalyseDTO.setStatus("E");
                return operationAnalyseDTO;
            }
        }

        //批量获取数据
        List<MtMaterialLot> materialLotList = materialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getSn())).build());
        if(CollectionUtils.isEmpty(materialLotList)){
            operationAnalyseDTO.setStatus("E");
            operationAnalyseDTO.setMessage("传入SN全部不存在");
            return operationAnalyseDTO;
        }

        //查询不良信息
        Map<String,MtMaterialLot> materialLotMap = materialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode, c->c));
        List<ItfOperationAnalyseDTO.NcDTO> ncList = itfOperationAnalyseIfaceMapper.queryNc(tenantId, new ArrayList<>(materialLotMap.keySet()),dto.getProcess());
        Map<String,List<ItfOperationAnalyseDTO.NcDTO>> ncMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ncList)){
            ncMap = ncList.stream().filter(e->StringUtils.isNotEmpty(e.getNcGroup()) && StringUtils.isNotEmpty(e.getNcCode())).collect(Collectors.groupingBy(ItfOperationAnalyseDTO.NcDTO::getSn));
        }

        //产品信息
        List<ItfOperationAnalyseDTO.NcList> products = itfOperationAnalyseIfaceMapper.queryMaterialBySNs(tenantId, dto.getSn());
        Map<String,ItfOperationAnalyseDTO.NcList> productMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(products)){
            productMap = products.stream().collect(Collectors.toMap(ItfOperationAnalyseDTO.NcList::getSn,c->c));
        }

        //当前工序
        List<ItfOperationAnalyseDTO.Process> currProcesses = itfOperationAnalyseIfaceMapper.queryCurrentProcess(tenantId,dto.getSn());
        Map<String,List<ItfOperationAnalyseDTO.Process>> currOperationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(currProcesses)) {
            currOperationMap = currProcesses.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.Process::getSn));
        }

        //遍历传入SN
        List<ItfOperationAnalyseDTO.NcList> ncDTOList = new ArrayList<>();
        for (String mml:dto.getSn()){
            ItfOperationAnalyseDTO.NcList ncDTO = new ItfOperationAnalyseDTO.NcList();
            ncDTO.setSn(mml);
            MtMaterialLot lot = materialLotMap.get(mml);
            if (lot == null){
                ncDTO.setStatus("E");
                ncDTO.setMessage("SN不存在");
                ncDTOList.add(ncDTO);
                continue;
            }

            //当前工序
            List<ItfOperationAnalyseDTO.Process> currOp = currOperationMap.get(mml);
            if (CollectionUtils.isNotEmpty(currOp)) {
                ncDTO.setCurrProcessName(currOp.get(0).getProcessName());
            }
            //当前产品信息
            ItfOperationAnalyseDTO.NcList product = productMap.get(mml);
            if (product != null){
                ncDTO.setMaterialCode(product.getMaterialCode());
                ncDTO.setMaterialName(product.getMaterialName());
            }

            //无不良记录
            ncDTO.setStatus("S");
            List<ItfOperationAnalyseDTO.NcDTO> nc = ncMap.get(mml);
            if (CollectionUtils.isEmpty(nc)){
                ncDTO.setNcFlag("否");
                ncDTOList.add(ncDTO);
                continue;
            }

            //存在不良
            ncDTO.setNcFlag("是");
            ncDTO.setNcDTOS(nc);
            ncDTOList.add(ncDTO);
        }
        operationAnalyseDTO.setNcList(ncDTOList);
        return operationAnalyseDTO;
    }

    @Override
    public ItfOperationAnalyseDTO.ReturnDTO1 invokeB(Long tenantId, ItfOperationAnalyseDTO.QueryDTO dto) {

        ItfOperationAnalyseDTO.ReturnDTO1 returnDTO1 = new ItfOperationAnalyseDTO.ReturnDTO1();
        List<ItfOperationAnalyseDTO.ReturnDTO1.ReturnList> returnList = new ArrayList<>();
        List<MtMaterialLot> materialLotList = new ArrayList<>();
        Date begda = null;
        Date endda = null;
        String jobBeginDate = null;
        String jobEndDate = null;

        //SN+工序 / 工序+时间区间
        if (CollectionUtils.isEmpty(dto.getProcess())){
            returnDTO1.setStatus("E");
            returnDTO1.setMessage("请输入工序名称");
            return returnDTO1;
        }
        if ((StringUtils.isEmpty(dto.getEndda()) || StringUtils.isEmpty(dto.getBegda())) && CollectionUtils.isEmpty(dto.getSn())){
            returnDTO1.setStatus("E");
            returnDTO1.setMessage("请输入SN或时间段");
            return returnDTO1;
        }
        //投料查询需传入物料
        if (CollectionUtils.isEmpty(dto.getMaterialCodes())){
            returnDTO1.setStatus("E");
            returnDTO1.setMessage("请输入需要查询的投料组件编码");
            return returnDTO1;
        }

        //工序校验
        List<String> workcellIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getProcess())){
            List<ItfOperationAnalyseDTO.Process> processes = itfOperationAnalyseIfaceMapper.queryWorkcellByProcessName(tenantId,dto.getProcess());
            List<String> ops = new ArrayList<>(dto.getProcess());
            if (CollectionUtils.isNotEmpty(processes)) {
                ops.removeAll(processes.stream().map(ItfOperationAnalyseDTO.Process::getProcessName).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(ops)){
                returnDTO1.setMessage("工序："+StringUtils.join(ops,",")+"不存在");
                returnDTO1.setStatus("E");
                return returnDTO1;
            }
            workcellIds = processes.stream().map(ItfOperationAnalyseDTO.Process::getWorkcellId).collect(Collectors.toList());
        }

        //物料编码校验
        List<String> compMaterialIds = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dto.getMaterialCodes())){
            String materialCodeList = "b.material_code IN (\"" + StringUtils.join(dto.getMaterialCodes(),"\",\"") + "\")";
            List<MtMaterial> list = mtMaterialMapper.queryMaterialByCode(tenantId,materialCodeList);
            if (CollectionUtils.isNotEmpty(list)){
                List<String> mls = new ArrayList<>(dto.getMaterialCodes());
                mls.removeAll(list.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(mls)){
                    returnDTO1.setMessage("组件物料："+StringUtils.join(mls,",")+"不存在");
                    returnDTO1.setStatus("E");
                    return returnDTO1;
                }
                compMaterialIds = list.stream().map(MtMaterial::getMaterialId).collect(Collectors.toList());
            }
        }

        //校验传入时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(dto.getBegda())) {
            try {
                begda = sdf.parse(dto.getBegda());
            } catch (Exception e) {
                returnDTO1.setStatus("E");
                returnDTO1.setMessage("时间格式有误:"+e);
                return returnDTO1;
            }
        }
        if (StringUtils.isNotBlank(dto.getEndda())){
            try{
                endda = sdf.parse(dto.getEndda());
            } catch (Exception e) {
                returnDTO1.setStatus("E");
                returnDTO1.setMessage("时间格式有误:"+e);
                return returnDTO1;
            }
        }

        //时间不能超过一个月
        if (begda != null && endda != null) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            startTime.setTime(begda);
            endTime.setTime(endda);
            startTime.add(Calendar.MONTH, 1);
            if (begda.after(endda) || startTime.before(endTime)) {
                returnDTO1.setStatus("E");
                returnDTO1.setMessage("请勿查询超过一个月数据");
                return returnDTO1;
            }
            jobBeginDate = sdf.format(begda);
            jobEndDate = sdf.format(endda);
        }

        //校验SN
        Map<String,MtMaterialLot> lotMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dto.getSn())){
            materialLotList = materialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                    .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getSn())).build());
            if(CollectionUtils.isEmpty(materialLotList)){
                returnDTO1.setStatus("E");
                returnDTO1.setMessage("传入SN全部不存在");
                return returnDTO1;
            }
            lotMap = materialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode,c->c));
            for (String mml:dto.getSn()){
                MtMaterialLot lot = lotMap.get(mml);
                if (StringUtils.isEmpty(lot.getMaterialLotId())){
                    ItfOperationAnalyseDTO.ReturnDTO1.ReturnList returnDTO = new ItfOperationAnalyseDTO.ReturnDTO1.ReturnList();
                    returnDTO.setSn(mml);
                    returnDTO.setStatus("E");
                    returnDTO.setMessage("SN不存在");
                    returnList.add(returnDTO);
                }
            }
        }

        //查询投料

        //一级
        Map<String,List<ItfOperationAnalyseDTO.MaterialData>> dto2Map1 = new HashMap<>();
        List<String> materialLotIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialLotList)){
            materialLotIds = materialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
        }
        List<ItfOperationAnalyseDTO.MaterialData> materialData1 = new ArrayList<>();
        List<String> jobIds = itfOperationAnalyseIfaceMapper.queryEoJob(tenantId, jobBeginDate, jobEndDate,null,workcellIds,materialLotIds);
        if (CollectionUtils.isNotEmpty(jobIds)){
            materialData1 = itfOperationAnalyseIfaceMapper.eoMaterialQuery(tenantId, jobIds,compMaterialIds);
            if (CollectionUtils.isNotEmpty(materialData1)){
                dto2Map1 = materialData1.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.MaterialData::getSn));
            }
        }

        //二级
        Map<String,List<ItfOperationAnalyseDTO.MaterialData>> dto2Map2 = new HashMap<>();
        List<ItfOperationAnalyseDTO.MaterialData> materialData2 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialData1)) {
            List<String> materialLotIds2 = materialData1.stream().map(ItfOperationAnalyseDTO.MaterialData::getMaterialLotId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(materialLotIds2)) {
                List<String> jobIds2 = itfOperationAnalyseIfaceMapper.queryEoJob(tenantId, null, null, null,null, materialLotIds2);
                if (CollectionUtils.isNotEmpty(jobIds2)) {
                    materialData2 = itfOperationAnalyseIfaceMapper.eoMaterialQuery(tenantId, jobIds2, compMaterialIds);
                    if (CollectionUtils.isNotEmpty(materialData2)) {
                        dto2Map2 = materialData2.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.MaterialData::getSn));
                    }
                }
            }
        }

        //三级
        Map<String,List<ItfOperationAnalyseDTO.MaterialData>> dto2Map3 = new HashMap<>();
        List<ItfOperationAnalyseDTO.MaterialData> materialData3 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialData2)) {
            List<String> materialLotIds3 = materialData2.stream().map(ItfOperationAnalyseDTO.MaterialData::getMaterialLotId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(materialLotIds3)) {
                List<String> jobIds3 = itfOperationAnalyseIfaceMapper.queryEoJob(tenantId, null, null, null,null, materialLotIds3);
                if (CollectionUtils.isNotEmpty(jobIds3)) {
                    materialData3 = itfOperationAnalyseIfaceMapper.eoMaterialQuery(tenantId, jobIds3, compMaterialIds);
                    if (CollectionUtils.isNotEmpty(materialData3)) {
                        dto2Map3 = materialData3.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.MaterialData::getSn));
                    }
                }
            }
        }
        //四级
        Map<String,List<ItfOperationAnalyseDTO.MaterialData>> dto2Map4 = new HashMap<>();
        List<ItfOperationAnalyseDTO.MaterialData> materialData4 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialData3)) {
            List<String> materialLotIds4 = materialData3.stream().map(ItfOperationAnalyseDTO.MaterialData::getMaterialLotId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(materialLotIds4)) {
                List<String> jobIds4 = itfOperationAnalyseIfaceMapper.queryEoJob(tenantId, null, null, null,null, materialLotIds4);
                if (CollectionUtils.isNotEmpty(jobIds4)) {
                    materialData4 = itfOperationAnalyseIfaceMapper.eoMaterialQuery(tenantId, jobIds4, compMaterialIds);
                    if (CollectionUtils.isNotEmpty(materialData4)) {
                        dto2Map4 = materialData4.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.MaterialData::getSn));
                    }
                }
            }
        }
        Set<String> set = lotMap.keySet();
        if (CollectionUtils.isEmpty(set)){
            set = dto2Map1.keySet();
        }
        for (String mml:set){
            List<ItfOperationAnalyseDTO.MaterialData> materialDataList1 = dto2Map1.get(mml);
            ItfOperationAnalyseDTO.ReturnDTO1.ReturnList returnDTO = new ItfOperationAnalyseDTO.ReturnDTO1.ReturnList();
            returnDTO.setSn(mml);
            //未查询到投料的SN
            if (CollectionUtils.isEmpty(materialDataList1)){
                returnDTO.setStatus("E");
                returnDTO.setMessage("SN无投料记录");
                returnList.add(returnDTO);
            }else {
                //二级
                List<ItfOperationAnalyseDTO.MaterialData> issues = new ArrayList<>(materialDataList1);

                List<ItfOperationAnalyseDTO.MaterialData> materialDataList2 = new ArrayList<>();
                for (ItfOperationAnalyseDTO.MaterialData materialData : materialDataList1) {
                    materialDataList2 = dto2Map2.get(materialData.getMaterialLotCode());
                    if (CollectionUtils.isNotEmpty(materialDataList2)) {
                        issues.addAll(materialDataList2);
                    }
                }
                //三级
                List<ItfOperationAnalyseDTO.MaterialData> materialDataList3 = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(materialDataList2)) {
                    for (ItfOperationAnalyseDTO.MaterialData materialData : materialDataList2) {
                        materialDataList3 = dto2Map3.get(materialData.getMaterialLotCode());
                        if (CollectionUtils.isNotEmpty(materialDataList3)) {
                            issues.addAll(materialDataList3);
                        }
                    }
                }
                //四级
                List<ItfOperationAnalyseDTO.MaterialData> materialDataList4 = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(materialDataList3)) {
                    for (ItfOperationAnalyseDTO.MaterialData materialData : materialDataList3) {
                        materialDataList4 = dto2Map3.get(materialData.getMaterialLotCode());
                        if (CollectionUtils.isNotEmpty(materialDataList4)) {
                            issues.addAll(materialDataList4);
                        }
                    }
                }

                returnDTO.setIssue(issues);
                returnDTO.setStatus("S");
                returnList.add(returnDTO);
            }
        }
        returnDTO1.setReturnList(returnList);
        returnDTO1.setStatus("S");
        return returnDTO1;
    }

    @Override
    public ItfOperationAnalyseDTO.ReturnDTO2 invokeC(Long tenantId, ItfOperationAnalyseDTO.QueryDTO dto) {

        ItfOperationAnalyseDTO.ReturnDTO2 returnDTO2 = new ItfOperationAnalyseDTO.ReturnDTO2();
        List<ItfOperationAnalyseDTO.ReturnDTO2.ReturnList> returnList = new ArrayList<>();
        List<MtMaterialLot> materialLotList = new ArrayList<>();
        String siteId = null;
        Date begda = null;
        Date endda = null;
        String jobBeginDate = null;
        String jobEndDate = null;

        //SN+工序 / 工序+时间区间
        if (CollectionUtils.isEmpty(dto.getProcess())) {
            returnDTO2.setStatus("E");
            returnDTO2.setMessage("请输入工序名称");
            return returnDTO2;
        }
        if ((StringUtils.isEmpty(dto.getEndda()) || StringUtils.isEmpty(dto.getBegda())) && CollectionUtils.isEmpty(dto.getSn())) {
            returnDTO2.setStatus("E");
            returnDTO2.setMessage("请输入SN或时间段");
            return returnDTO2;
        }

        //工序校验
        List<String> workcellIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getProcess())){
            List<ItfOperationAnalyseDTO.Process> processes = itfOperationAnalyseIfaceMapper.queryWorkcellByProcessName(tenantId,dto.getProcess());
            List<String> ops = new ArrayList<>(dto.getProcess());
            if (CollectionUtils.isNotEmpty(processes)) {
                ops.removeAll(processes.stream().map(ItfOperationAnalyseDTO.Process::getProcessName).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(ops)){
                returnDTO2.setMessage("工序："+StringUtils.join(ops,",")+"不存在");
                returnDTO2.setStatus("E");
                return returnDTO2;
            }
            workcellIds = processes.stream().map(ItfOperationAnalyseDTO.Process::getWorkcellId).collect(Collectors.toList());
            siteId = processes.get(0).getSiteId();
        }

        //校验工艺质量项目
        if (CollectionUtils.isNotEmpty(dto.getEoJobDataItem())){
            List<MtTag> tags = mtTagMapper.selectByCodeList(tenantId, dto.getEoJobDataItem());
            List<String> ops = new ArrayList<>(dto.getEoJobDataItem());
            if (CollectionUtils.isNotEmpty(tags)) {
                ops.removeAll(tags.stream().map(MtTag::getTagCode).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(ops)){
                returnDTO2.setMessage("数据项："+StringUtils.join(ops,",")+"不存在");
                returnDTO2.setStatus("E");
                return returnDTO2;
            }
        }

        //校验传入时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(dto.getBegda())) {
            try {
                begda = sdf.parse(dto.getBegda());
            } catch (Exception e) {
                returnDTO2.setStatus("E");
                returnDTO2.setMessage("时间格式有误:" + e);
                return returnDTO2;
            }
        }
        if (StringUtils.isNotBlank(dto.getEndda())) {
            try {
                endda = sdf.parse(dto.getEndda());
            } catch (Exception e) {
                returnDTO2.setStatus("E");
                returnDTO2.setMessage("时间格式有误:" + e);
                return returnDTO2;
            }
        }

        //时间不能超过一个月
        if (begda != null && endda != null) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            startTime.setTime(begda);
            endTime.setTime(endda);
            startTime.add(Calendar.MONTH, 1);
            if (begda.after(endda) || startTime.before(endTime)) {
                returnDTO2.setStatus("E");
                returnDTO2.setMessage("请勿查询超过一个月数据");
                return returnDTO2;
            }
            jobBeginDate = sdf.format(begda);
            jobEndDate = sdf.format(endda);
        }

        //校验SN
        Map<String, MtMaterialLot> lotMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dto.getSn())) {
            materialLotList = materialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                    .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getSn())).build());
            if (CollectionUtils.isEmpty(materialLotList)) {
                returnDTO2.setStatus("E");
                returnDTO2.setMessage("传入SN全部不存在");
                return returnDTO2;
            }
            lotMap = materialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode, c -> c));
            for (String mml : dto.getSn()) {
                MtMaterialLot lot = lotMap.get(mml);
                if (StringUtils.isEmpty(lot.getMaterialLotId())) {
                    ItfOperationAnalyseDTO.ReturnDTO2.ReturnList returnDTO = new ItfOperationAnalyseDTO.ReturnDTO2.ReturnList();
                    returnDTO.setSn(mml);
                    returnDTO.setStatus("E");
                    returnDTO.setMessage("SN不存在");
                    returnList.add(returnDTO);
                }
            }
        }
        List<String> materialLotIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialLotList)) {
            materialLotIds = materialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
        }

        //查询工艺质量
        List<ItfOperationAnalyseDTO.EoJobData> eoJobData = new ArrayList<>();
        List<String> jobIds = itfOperationAnalyseIfaceMapper.queryEoJob(tenantId, jobBeginDate, jobEndDate, null,workcellIds, materialLotIds);
        if (CollectionUtils.isNotEmpty(jobIds)) {
            eoJobData = itfOperationAnalyseIfaceMapper.eoJobDataQuery(tenantId, siteId, jobIds,dto.getEoJobDataItem());
        }
        //包装返回值
        if (CollectionUtils.isEmpty(eoJobData)) {
            returnDTO2.setStatus("E");
            returnDTO2.setMessage("无工艺质量数据");
            return returnDTO2;
        }
        Map<String, List<ItfOperationAnalyseDTO.EoJobData>> eoJobMap = eoJobData.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.EoJobData::getMaterialLotCode));

        Set<String> set = lotMap.keySet();
        if (CollectionUtils.isEmpty(set)) {
            set = eoJobMap.keySet();
        }

        for (String mml : set) {
            List<ItfOperationAnalyseDTO.EoJobData> jobData = eoJobMap.get(mml);
            ItfOperationAnalyseDTO.ReturnDTO2.ReturnList returnDTO = new ItfOperationAnalyseDTO.ReturnDTO2.ReturnList();
            returnDTO.setSn(mml);
            //未查询到SN的工艺质量数据
            if (CollectionUtils.isEmpty(jobData)) {
                returnDTO.setStatus("E");
                returnDTO.setMessage("SN无工艺质量数据");
                returnList.add(returnDTO);
            } else {
                AtomicInteger i = new AtomicInteger();
                jobData.forEach(e->e.setLineNum(i.getAndIncrement()* 10L));
                returnDTO.setSn(mml);
                returnDTO.setStatus("S");
                returnDTO.setEoJobData(jobData);
                returnList.add(returnDTO);
            }
        }
        returnDTO2.setStatus("S");
        returnDTO2.setReturnList(returnList);
        return returnDTO2;
    }

    @Override
    public ItfOperationAnalyseDTO.AcceptedRate invokeD(Long tenantId, ItfOperationAnalyseDTO.QueryDTO dto) {

        ItfOperationAnalyseDTO.AcceptedRate acceptedRate = new ItfOperationAnalyseDTO.AcceptedRate();
        List<MtMaterialLot> materialLotList = new ArrayList<>();
        Date begda = null;
        Date endda = null;
        String jobBeginDate = null;
        String jobEndDate = null;

        // 工序+时间区间
        if (CollectionUtils.isEmpty(dto.getProcess())){
            acceptedRate.setStatus("E");
            acceptedRate.setMessage("请输入工序名称");
            return acceptedRate;
        }
        if (StringUtils.isEmpty(dto.getEndda()) || StringUtils.isEmpty(dto.getBegda())){
            acceptedRate.setStatus("E");
            acceptedRate.setMessage("请输入时间段");
            return acceptedRate;
        }

        //工序校验
        List<String> workcellIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getProcess())){
            List<ItfOperationAnalyseDTO.Process> processes = itfOperationAnalyseIfaceMapper.queryWorkcellByProcessName(tenantId,dto.getProcess());
            List<String> ops = new ArrayList<>(dto.getProcess());
            if (CollectionUtils.isNotEmpty(processes)) {
                ops.removeAll(processes.stream().map(ItfOperationAnalyseDTO.Process::getProcessName).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(ops)){
                acceptedRate.setMessage("工序："+StringUtils.join(ops,",")+"不存在");
                acceptedRate.setStatus("E");
                return acceptedRate;
            }
            workcellIds = processes.stream().map(ItfOperationAnalyseDTO.Process::getWorkcellId).collect(Collectors.toList());
        }

        //校验传入时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(dto.getBegda())) {
            try {
                begda = sdf.parse(dto.getBegda());
            } catch (Exception e) {
                acceptedRate.setStatus("E");
                acceptedRate.setMessage("时间格式有误:"+e);
                return acceptedRate;
            }
        }
        if (StringUtils.isNotBlank(dto.getEndda())){
            try{
                endda = sdf.parse(dto.getEndda());
            } catch (Exception e) {
                acceptedRate.setStatus("E");
                acceptedRate.setMessage("时间格式有误:"+e);
                return acceptedRate;
            }
        }

        //时间不能超过一个月
        if (begda != null && endda != null) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            startTime.setTime(begda);
            endTime.setTime(endda);
            startTime.add(Calendar.MONTH, 1);
            if (begda.after(endda) || startTime.before(endTime)) {
                acceptedRate.setStatus("E");
                acceptedRate.setMessage("请勿查询超过一个月数据");
                return acceptedRate;
            }
            jobBeginDate = sdf.format(begda);
            jobEndDate = sdf.format(endda);
        }
        //校验SN
        if (CollectionUtils.isNotEmpty(dto.getSn())) {
            materialLotList = materialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                    .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getSn())).build());
            if (CollectionUtils.isEmpty(materialLotList)) {
                acceptedRate.setStatus("E");
                acceptedRate.setMessage("传入SN全部不存在");
                return acceptedRate;
            }
            List<String> sns = new ArrayList<>(dto.getSn());
            sns.removeAll(materialLotList.stream().map(MtMaterialLot::getMaterialLotCode).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(sns)) {
                acceptedRate.setStatus("E");
                acceptedRate.setMessage("SN:" + StringUtils.join(sns,",")+"不存在");
                return acceptedRate;
            }
        }
        List<String> materialLotIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialLotList)) {
            materialLotIds = materialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
        }

        //查作业记录
        List<ItfOperationAnalyseDTO.EoJobNcRecord> hmeEoJobSns = itfOperationAnalyseIfaceMapper.queryEoJobNcRecord(tenantId, jobBeginDate, jobEndDate, null,workcellIds,materialLotIds);
        if (CollectionUtils.isEmpty(hmeEoJobSns)){
            acceptedRate.setStatus("E");
            acceptedRate.setMessage("无查询记录");
            return acceptedRate;
        }
        //已测试完成作业
        List<ItfOperationAnalyseDTO.EoJobNcRecord> testJobs = hmeEoJobSns.stream().filter(e->StringUtils.isNotEmpty(e.getLoginName())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(testJobs)){
            acceptedRate.setStatus("E");
            acceptedRate.setMessage("无测试完成记录");
            return acceptedRate;
        }
        //测试合格作业
        List<ItfOperationAnalyseDTO.EoJobNcRecord> accepteds = testJobs.stream().filter(e->StringUtils.isEmpty(e.getNcRecordId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(accepteds)){
            acceptedRate.setStatus("E");
            acceptedRate.setMessage("无合格产品");
            return acceptedRate;
        }
        //不合格作业
        List<ItfOperationAnalyseDTO.EoJobNcRecord> ncProducts = testJobs.stream().filter(e->StringUtils.isNotEmpty(e.getNcRecordId())).collect(Collectors.toList());

        //当前加工SN
        List<String> jobSns = hmeEoJobSns.stream().map(ItfOperationAnalyseDTO.EoJobNcRecord::getSn).distinct().collect(Collectors.toList());
        acceptedRate.setProducts(jobSns);
        acceptedRate.setProductQty(String.valueOf(jobSns.size()));
        //已测试完成SN
        List<String> testSns = testJobs.stream().map(ItfOperationAnalyseDTO.EoJobNcRecord::getSn).distinct().collect(Collectors.toList());
        acceptedRate.setTests(testSns);
        acceptedRate.setTestQty(String.valueOf(testSns.size()));
        //合格产品
        List<String> acceptedSns = accepteds.stream().map(ItfOperationAnalyseDTO.EoJobNcRecord::getSn).distinct().collect(Collectors.toList());
        acceptedRate.setAccepteds(acceptedSns);
        acceptedRate.setAcceptedQty(String.valueOf(acceptedSns.size()));
        //不合格产品
        if (CollectionUtils.isNotEmpty(ncProducts)) {
            List<String> ncProductSns = ncProducts.stream().map(ItfOperationAnalyseDTO.EoJobNcRecord::getSn).distinct().collect(Collectors.toList());
            acceptedRate.setNcProducts(ncProductSns);
            acceptedRate.setNcProductsQty(String.valueOf(ncProductSns.size()));
        }
        //工序良率
        BigDecimal processRate = BigDecimal.valueOf(accepteds.size()).setScale(4,BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(testJobs.size()).setScale(4,BigDecimal.ROUND_HALF_UP),BigDecimal.ROUND_HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
        acceptedRate.setProcessRate(processRate);
        //人员加工数-已测试
        Map<String,List<ItfOperationAnalyseDTO.EoJobNcRecord>> empSns = testJobs.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.EoJobNcRecord::getLoginName));
        //人员合格数
        Map<String,List<ItfOperationAnalyseDTO.EoJobNcRecord>> empAccSns = accepteds.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.EoJobNcRecord::getLoginName));
        //人员良率
        List<ItfOperationAnalyseDTO.AcceptedRate.EmpRate> empRates = new ArrayList<>();
        for (String emp:empSns.keySet()){
            ItfOperationAnalyseDTO.AcceptedRate.EmpRate empRate = new ItfOperationAnalyseDTO.AcceptedRate.EmpRate();
            empRate.setLoginName(emp);

            List<ItfOperationAnalyseDTO.EoJobNcRecord> empSnList = empSns.get(emp);
            List<ItfOperationAnalyseDTO.EoJobNcRecord> empAccSnList = empAccSns.get(emp);
            List<String> r1 = new ArrayList<>();
            List<String> r2 = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(empSnList)){
                r1 = empSnList.stream().map(ItfOperationAnalyseDTO.EoJobNcRecord::getSn).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(r1)) {
                    empRate.setTests(r1);
                    empRate.setTestQty(String.valueOf(r1.size()));
                }
            }
            if(CollectionUtils.isNotEmpty(empAccSnList)){
                r2 = empAccSnList.stream().map(ItfOperationAnalyseDTO.EoJobNcRecord::getSn).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(r2)){
                    empRate.setAccepteds(r2);
                    empRate.setAcceptedQty(String.valueOf(r2.size()));
                }
            }
            empRate.setRealName(empSnList.get(0).getRealName());
            if (CollectionUtils.isEmpty(r2)){
                empRate.setStatus("E");
                empRate.setMessage("当前员工无合格产品产出");
                empRates.add(empRate);
                continue;
            }
            empRate.setStatus("S");
            BigDecimal rate = BigDecimal.valueOf(r2.size()).setScale(4,BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(r1.size()).setScale(4,BigDecimal.ROUND_HALF_UP),BigDecimal.ROUND_HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
            empRate.setRate(rate);
            empRates.add(empRate);
        }
        acceptedRate.setEmpRates(empRates);
        //工位加工数-已测试
        Map<String,List<ItfOperationAnalyseDTO.EoJobNcRecord>> wkcSns = testJobs.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.EoJobNcRecord::getWorkcellCode));
        //工位合格数
        Map<String,List<ItfOperationAnalyseDTO.EoJobNcRecord>> wkcAccSns = accepteds.stream().collect(Collectors.groupingBy(ItfOperationAnalyseDTO.EoJobNcRecord::getWorkcellCode));
        //工位良率
        List<ItfOperationAnalyseDTO.AcceptedRate.WkcRate> wkcRates = new ArrayList<>();
        for (String wkc:wkcSns.keySet()){
            ItfOperationAnalyseDTO.AcceptedRate.WkcRate wkcRate = new ItfOperationAnalyseDTO.AcceptedRate.WkcRate();
            wkcRate.setWorkcellCode(wkc);

            List<ItfOperationAnalyseDTO.EoJobNcRecord> wkcSnList = wkcSns.get(wkc);
            List<ItfOperationAnalyseDTO.EoJobNcRecord> wkcAccSnList = wkcAccSns.get(wkc);

            List<String> r1 = new ArrayList<>();
            List<String> r2 = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(wkcSnList)){
                r1 = wkcSnList.stream().map(ItfOperationAnalyseDTO.EoJobNcRecord::getSn).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(r1)) {
                    wkcRate.setTests(r1);
                    wkcRate.setTestQty(String.valueOf(r1.size()));
                }
            }
            if (CollectionUtils.isNotEmpty(wkcAccSnList)){
                r2 = wkcAccSnList.stream().map(ItfOperationAnalyseDTO.EoJobNcRecord::getSn).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(r2)){
                    wkcRate.setAccepteds(r2);
                    wkcRate.setAcceptedQty(String.valueOf(r2.size()));
                }
            }
            if(CollectionUtils.isEmpty(r2)){
                wkcRate.setStatus("E");
                wkcRate.setMessage("当前工位无合格品产出");
                wkcRates.add(wkcRate);
                continue;
            }
            wkcRate.setStatus("S");
            wkcRate.setWorkcellName(wkcSnList.get(0).getWorkcellName());
            BigDecimal rate = BigDecimal.valueOf(r2.size()).setScale(4,BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(r1.size()).setScale(4,BigDecimal.ROUND_HALF_UP),BigDecimal.ROUND_HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
            wkcRate.setRate(rate);
            wkcRates.add(wkcRate);
        }
        acceptedRate.setWkcRates(wkcRates);
        acceptedRate.setStatus("S");

        return acceptedRate;
    }
}
