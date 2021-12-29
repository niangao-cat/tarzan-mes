package com.ruike.itf.app.service.impl;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.ItfPreselectedCosService;
import com.ruike.itf.infra.mapper.ItfPreselectedCosMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * @description COS 接口1
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/1/19
 * @time 11:10
 * @version 0.0.1
 * @return
 */
@Service
@Slf4j
public class ItfPreselectedCosServiceImpl implements ItfPreselectedCosService {

    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;

    @Autowired
    private MtMaterialLotMapper materialLotMapper;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private ItfPreselectedCosMapper mapper;


    /***
     * @description COS 接口1
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2021/1/19
     * @time 11:20
     * @version 0.0.1
     * @return java.util.List<com.ruike.itf.api.dto.DataCollectReturnDTO>
     */
    @Override
    public List<ItfPreselectedCosOneReturnDTO> invoke(Long tenantId, List<String> dto) {

        List<ItfPreselectedCosOneReturnDTO> returnDTOList = new ArrayList<>();
        for(int i=0; i<dto.size(); i++){
            ItfPreselectedCosOneReturnDTO returnDTO = new ItfPreselectedCosOneReturnDTO();
            returnDTO.setMaterialLotCode(dto.get(i));

            // 判断输入的盒子号是否为空， 是则报错
            if (StringUtils.isEmpty(dto.get(i))) {
                returnDTO.setMessage("盒子号必输");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
                continue;
            }
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.get(i));

            // 查看对应的有效性是否为Y 是则通过 不是则报错
            if (materialLot == null || !"Y".equals(materialLot.getEnableFlag())) {
                returnDTO.setMessage("盒子号不存在，请重新输入！");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
                continue;
            }

            // 查询盒子的装载信息, 如果不存在 则报错
            HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
            hmeMaterialLotLoad.setMaterialLotId(materialLot.getMaterialLotId());
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);

            if (hmeMaterialLotLoadList.size() == 0) {
                returnDTO.setMessage("当前盒子无装载信息，请检查！");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
                continue;
            }

            // 查询盒子的虚拟号, 如果不存在 则报错
            List<String> virtualNumList = mapper.getVirtualNumList(hmeMaterialLotLoadList.stream().map(HmeMaterialLotLoad::getLoadSequence).collect(Collectors.toList()));

            if (virtualNumList.size() == 0) {
                returnDTO.setMessage("当前盒子未查到对应虚拟号，请检查！");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
            } else {
                returnDTO.setVirtualNum(virtualNumList);
                returnDTO.setStatus("S");
                returnDTOList.add(returnDTO);
            }
        }
        return  returnDTOList;
    }

    /***
     * @description COS 接口2
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2021/1/19
     * @time 15:08
     * @version 0.0.1
     * @return com.ruike.itf.api.dto.ItfPreselectedCosTwoReturnDTO
     */
    @Override
    public List<ItfPreselectedCosTwoReturnDTO> invokeSecond(Long tenantId, List<String> dto) {

        List<ItfPreselectedCosTwoReturnDTO> returnDTOList = new ArrayList<>();

        for(int x=0; x<dto.size(); x++){
            // 判断虚拟号是否为空 是则报错
            ItfPreselectedCosTwoReturnDTO returnDTO = new ItfPreselectedCosTwoReturnDTO();
            returnDTO.setVirtualNum(dto.get(x));
            if (StringUtils.isEmpty(dto.get(x))) {
                returnDTO.setMessage("虚拟号必输");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
                continue;
            }

            // 查询数据, 判断数据是否有值 没有则报错
            List<ItfPreselectedCosTwoShowDTO> itfPreselectedCosTwoShowDTOList = mapper.getMaterialLotList(dto.get(x));
            if (itfPreselectedCosTwoShowDTOList.size() == 0) {
                returnDTO.setMessage("当前虚拟号不存在，请重新输入！");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
                continue;
            }

            // 筛选有盒子号的数据, 没有则报错
            List<ItfPreselectedCosTwoShowDTO> materialLotCodeList = itfPreselectedCosTwoShowDTOList.stream().filter(item -> StringUtils.isNotEmpty(item.getMaterialLotCode())).collect(Collectors.toList());
            if (materialLotCodeList.size() == 0) {
                returnDTO.setMessage("未查询到盒子号信息！");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
                continue;
            }

            // 筛选有热沉信息的数据, 没有则报错
            List<ItfPreselectedCosTwoShowDTO> hotSinkList = materialLotCodeList.stream().filter(item -> StringUtils.isNotEmpty(item.getHotSink())).collect(Collectors.toList());
            if (hotSinkList.size() == 0) {
                returnDTO.setMessage("未查询到热沉信息！");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
                continue;
            }

            // 筛选有挑选规则信息的数据, 没有则报错
            List<ItfPreselectedCosTwoShowDTO> totalNumList = hotSinkList.stream().filter(item -> StringUtils.isNotEmpty(item.getTotalNum())).collect(Collectors.toList());
            if (totalNumList.size() == 0) {
                returnDTO.setMessage("未查询到挑选规则信息！");
                returnDTO.setStatus("E");
                returnDTOList.add(returnDTO);
                continue;
            }

            // 根据筛选完后的数据进行 行列排序
            //totalNumList = totalNumList.stream().sorted(Comparator.comparing(ItfPreselectedCosTwoShowDTO::getLoadRow).thenComparing(ItfPreselectedCosTwoShowDTO::getLoadColumn)).collect(Collectors.toList());

            // 根据排完序后的数据 进行数据整合
            for (int i = 0; i < totalNumList.size(); i++) {
                // 设置位置
                totalNumList.get(i).setLoad(setLoad(totalNumList.get(i).getOldLoad()));
                // 设置标志
                //if(i>=9){
                //    totalNumList.get(i).setCosPos("COS"+(i+1));
                //}else {
                //    totalNumList.get(i).setCosPos("COS0"+(i+1));
                //}

            }

            returnDTO.setList(totalNumList);
            returnDTO.setStatus("S");
            returnDTOList.add(returnDTO);
        }

        return returnDTOList;
    }

    @Override
    public ItfPreselectedCosSelectReturnDTO invokeSelect(Long tenantId, List<String> dto) {

        //返回列表1 - 芯片信息列表
        List<ItfPreselectedCosSelectReturnDTO1> returnSnList = new ArrayList<>();
        //返回列表2 - 盒子剩余芯片数
        List<ItfPreselectedCosSelectReturnDTO2> surplusNumList = new ArrayList<>();

        Map<String,MtMaterialLot> materialLotMap = new HashMap<>();
        Map<String,List<HmeMaterialLotLoad>> hmeMaterialLotLoadMap = new HashMap<>();

        if (CollectionUtils.isEmpty(dto)){
            returnSnList.add(new ItfPreselectedCosSelectReturnDTO1(){{
                setMessage("未传入盒子号");
                setStatus("E");
            }});
        }else {
            //批量获取数据
            List<MtMaterialLot> materialLotList = materialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                    .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto)).build());
            List<String> materialLotIdList = materialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
            List<HmeMaterialLotLoad> hmeMaterialLotLoadListAll = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                    .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
            if (CollectionUtils.isNotEmpty(materialLotList)){
                materialLotMap = materialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode,c->c));
            }
            if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadListAll)){
                hmeMaterialLotLoadMap = hmeMaterialLotLoadListAll.stream().collect(Collectors.groupingBy(HmeMaterialLotLoad::getMaterialLotId));
            }

            //剩余芯片数
            materialLotList.forEach(e -> surplusNumList.add(new ItfPreselectedCosSelectReturnDTO2(){{
                        setMaterialLotCode(e.getMaterialLotCode());
                        setSurplusNum(e.getPrimaryUomQty());
                    }})
            );
        }

        for (String s : dto) {
            ItfPreselectedCosSelectReturnDTO1 ret = new ItfPreselectedCosSelectReturnDTO1();
            ret.setMaterialLotCode(s);

            // 判断输入的盒子号是否为空， 是则报错
            if (StringUtils.isEmpty(s)) {
                ret.setMessage("盒子号必输");
                ret.setStatus("E");
                returnSnList.add(ret);
                continue;
            }
            MtMaterialLot materialLot =  materialLotMap.get(s);

            // 查看对应的有效性是否为Y 是则通过 不是则报错
            if (materialLot == null || !"Y".equals(materialLot.getEnableFlag())) {
                ret.setMessage("盒子号不存在，请重新输入！");
                ret.setStatus("E");
                returnSnList.add(ret);
                continue;
            }

            // 查询盒子的装载信息, 如果不存在 则报错
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadMap.get(materialLot.getMaterialLotId());

            if (hmeMaterialLotLoadList.size() == 0) {
                ret.setMessage("当前盒子无装载信息，请检查！");
                ret.setStatus("E");
                returnSnList.add(ret);
                continue;
            }

            //查询芯片记录
            List<ItfPreselectedCosSelectShowDTO> returnSn = mapper.getVirtualNumListNew(hmeMaterialLotLoadList.stream().map(HmeMaterialLotLoad::getLoadSequence).collect(Collectors.toList()));

            // 获取盒子的虚拟号, 如果不存在 则报错
            List<ItfPreselectedCosSelectShowDTO> virtualNumList = returnSn.stream().filter(item -> StringUtils.isNotEmpty(item.getVirtualNum())).collect(Collectors.toList());
            if (virtualNumList.size() == 0) {
                ret.setMessage("当前盒子未查到对应虚拟号，请检查！");
                ret.setStatus("E");
                returnSnList.add(ret);
                continue;
            }
            ret.setVirtualNum(virtualNumList.stream().map(ItfPreselectedCosSelectShowDTO::getVirtualNum).collect(Collectors.toList()));

            // 筛选有热沉信息的数据, 没有则报错
            List<ItfPreselectedCosSelectShowDTO> hotSinkList = virtualNumList.stream().filter(item -> StringUtils.isNotEmpty(item.getHotSink())).collect(Collectors.toList());
            if (hotSinkList.size() == 0) {
                ret.setMessage("未查询到热沉信息！");
                ret.setStatus("E");
                returnSnList.add(ret);
                continue;
            }

            // 筛选有挑选规则信息的数据, 没有则报错
            List<ItfPreselectedCosSelectShowDTO> totalNumList = hotSinkList.stream().filter(item -> StringUtils.isNotEmpty(item.getTotalNum())).collect(Collectors.toList());
            if (totalNumList.size() == 0) {
                ret.setMessage("未查询到挑选规则信息！");
                ret.setStatus("E");
                returnSnList.add(ret);
                continue;
            }

            // 根据排完序后的数据 进行数据整合
            for (int x = 0; x < totalNumList.size(); x++) {
                totalNumList.get(x).setLoad(setLoad(totalNumList.get(x).getOldLoad()));
            }

            ret.setList(totalNumList);
            ret.setStatus("S");
            returnSnList.add(ret);
        }

        //包装返回值
        ItfPreselectedCosSelectReturnDTO returnDTO = new ItfPreselectedCosSelectReturnDTO();
        returnDTO.setReturnSn(returnSnList);
        returnDTO.setReturnSurplusNum(surplusNumList);
        return returnDTO;
    }

    @Override
    public List<ItfPreselectedCosArrangeReturnDTO> invokeArrange(Long tenantId, List<String> dto) {

        List<ItfPreselectedCosArrangeReturnDTO> returnDTOS = new ArrayList<>();

        Map<String,MtMaterialLot> materialLotMap = new HashMap<>();
        Map<String,MtMaterialLot> sourceMaterialLotMap = new HashMap<>();
        Map<String,List<HmeMaterialLotLoad>> hmeMaterialLotLoadMap = new HashMap<>();

        if (CollectionUtils.isEmpty(dto)){
            returnDTOS.add(new ItfPreselectedCosArrangeReturnDTO(){{
                setMessage("未传入盒子号");
                setStatus("E");
            }});
        }else {
            //批量获取数据
            List<MtMaterialLot> materialLotList = materialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                    .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto)).build());
            List<String> materialLotIdList = materialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
            List<HmeMaterialLotLoad> hmeMaterialLotLoadListAll = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                    .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
            List<MtMaterialLot> sourceMaterialLotList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(hmeMaterialLotLoadListAll)){
                List<String> sourceMaterialLotIdList = hmeMaterialLotLoadListAll.stream().map(HmeMaterialLotLoad::getSourceMaterialLotId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(sourceMaterialLotIdList)){
                    sourceMaterialLotList = materialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                            .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                            .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, sourceMaterialLotIdList)).build());
                }

            }
            if (CollectionUtils.isNotEmpty(materialLotList)){
                materialLotMap = materialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode,c->c));
            }
            if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadListAll)){
                hmeMaterialLotLoadMap = hmeMaterialLotLoadListAll.stream().collect(Collectors.groupingBy(HmeMaterialLotLoad::getMaterialLotId));
            }
            if(CollectionUtils.isNotEmpty(sourceMaterialLotList)){
                sourceMaterialLotMap = sourceMaterialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId,c->c));
            }
        }

        for (String s : dto) {
            ItfPreselectedCosArrangeReturnDTO ret = new ItfPreselectedCosArrangeReturnDTO();
            ret.setMaterialLotCode(s);

            // 判断输入的盒子号是否为空， 是则报错
            if (StringUtils.isEmpty(s)) {
                ret.setMessage("盒子号必输");
                ret.setStatus("E");
                returnDTOS.add(ret);
                continue;
            }
            MtMaterialLot materialLot =  materialLotMap.get(s);

            // 查看对应的有效性是否为Y 是则通过 不是则报错
            if (materialLot == null || !"Y".equals(materialLot.getEnableFlag())) {
                ret.setMessage("盒子号不存在，请重新输入！");
                ret.setStatus("E");
                returnDTOS.add(ret);
                continue;
            }

            // 查询盒子的装载信息, 如果不存在 则报错
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadMap.get(materialLot.getMaterialLotId());
            if (hmeMaterialLotLoadList.size() == 0) {
                ret.setMessage("当前盒子无装载信息，请检查！");
                ret.setStatus("E");
                returnDTOS.add(ret);
                continue;
            }

            // 筛选有热沉信息的数据, 没有则报错
            List<HmeMaterialLotLoad> hotSinkList = hmeMaterialLotLoadList.stream().filter(item->StringUtils.isNotEmpty(item.getHotSinkCode())).collect(Collectors.toList());
            if (hotSinkList.size() == 0) {
                ret.setMessage("未查询到热沉信息！");
                ret.setStatus("E");
                returnDTOS.add(ret);
                continue;
            }

            //返回芯片列表
            List<ItfPreselectedCosArrangeShowDTO> cos = new ArrayList<>();
            for(HmeMaterialLotLoad e : hotSinkList){
                ItfPreselectedCosArrangeShowDTO dto1 = new ItfPreselectedCosArrangeShowDTO();
                BeanUtils.copyProperties(e,dto1);
                dto1.setMaterialLotCode(s);

                MtMaterialLot mtMaterialLot = sourceMaterialLotMap.get(e.getSourceMaterialLotId());
                dto1.setSourceMaterialLotCode(mtMaterialLot == null ? null : mtMaterialLot.getMaterialLotCode());

                String oldLoad = e.getSourceLoadRow() + "," + e.getSourceLoadColumn();
                String load = e.getLoadRow() + "," + e.getLoadColumn();
                dto1.setOldLoad(setLoad(oldLoad));
                dto1.setLoad(setLoad(load));

                cos.add(dto1);
            }

            ret.setList(cos);
            ret.setStatus("S");
            returnDTOS.add(ret);
        }

        return returnDTOS;
    }

    /***
     * @description 设置位置信息
     * @param load
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2021/1/19
     * @time 15:40
     * @version 0.0.1
     * @return java.lang.String
     */
    private String setLoad(String load) {
        if(StringUtils.isEmpty(load)){
            return  "";
        }else {

            // 进行逗号拆分
            String [] splitBttr = load.split(",");

            // 只处理 两位的数据
            if(splitBttr.length==2){

                // 判断是否为数字
                if(splitBttr[0].matches("^[0-9]*$")){
                    // 第一位根据 数字转为大写字母
                    return  String.valueOf((char)(Integer.parseInt(splitBttr[0]) + 64))+splitBttr[1];
                }else {
                    return  load;
                }
            }else {
                return  load;
            }
        }
    }
}
