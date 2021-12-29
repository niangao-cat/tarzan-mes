package com.ruike.itf.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmeTagFormulaLine;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.infra.mapper.HmeMaterialLotLoadMapper;
import com.ruike.itf.api.dto.BneCollectItfDTO;
import com.ruike.itf.api.dto.BneCollectItfDTO1;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.app.service.ItfBneCollectIfaceService;
import com.ruike.itf.domain.entity.ItfBneCollectIface;
import com.ruike.itf.domain.repository.ItfBneCollectIfaceRepository;
import com.ruike.itf.infra.mapper.ItfBneCollectIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.MapKeyComparator;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.choerodon.mybatis.util.StringUtil;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.velocity.runtime.directive.Foreach;
import org.hzero.boot.message.MessageClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BNE数据采集接口表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2020-09-12 13:59:43
 */
@Service
public class ItfBneCollectIfaceServiceImpl extends BaseServiceImpl<ItfBneCollectIface>  implements ItfBneCollectIfaceService {

    private final ItfBneCollectIfaceRepository itfBneCollectIfaceRepository;

    @Autowired
    private MessageClient messageClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private ItfBneCollectIfaceMapper itfBneCollectIfaceMapper;

    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    private Map<String, String> map;


    @Autowired
    public ItfBneCollectIfaceServiceImpl(ItfBneCollectIfaceRepository itfBneCollectIfaceRepository) {
        this.itfBneCollectIfaceRepository = itfBneCollectIfaceRepository;
    }

    private boolean validate(Long tenantId, List<ItfBneCollectIface> itfList) {
        boolean validFlag = true;
        // 反射得到字段map
        Map<String, Field> fieldMap = Arrays.stream(ItfBneCollectIface.class.getDeclaredFields()).collect(Collectors.toMap(Field::getName, rec -> rec, (key1, keys2) -> key1));
        for (ItfBneCollectIface itf : itfList) {
            String processMessage = "";
            // 验证字段
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getAssetEncoding()), processMessage, "ITF_DATA_COLLECT_0001");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getSn()), processMessage, "ITF_DATA_COLLECT_0003");

            //校验设备
            HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                setTenantId(tenantId);
                setAssetEncoding(itf.getAssetEncoding());
            }});
            if (ObjectUtil.isEmpty(hmeEquipmentFirst)) {
                processMessage = InterfaceUtils.processErrorMessage(tenantId, ObjectUtil.isEmpty(hmeEquipmentFirst), processMessage, "ITF_DATA_COLLECT_0011");
            } else {
                processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(hmeEquipmentFirst.getEquipmentCategory()), processMessage, "ITF_DATA_COLLECT_0010");
                itf.setEquipmentCategory(hmeEquipmentFirst.getEquipmentCategory());
            }
            if (StringUtils.isNotBlank(processMessage)) {
                itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itf.setProcessMessage(processMessage);
                itf.setProcessDate(new Date());
                itfBneCollectIfaceRepository.updateByPrimaryKeySelective(itf);
                validFlag = false;
            }
        }
        return validFlag;
    }



    @Override
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<BneCollectItfDTO> collectList) {
        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }
        // 插入接口表
        List<ItfBneCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(BneCollectItfDTO.class, ItfBneCollectIface.class, false);
        Date nowDate = new Date();
        for (BneCollectItfDTO data : collectList) {
            ItfBneCollectIface itf = new ItfBneCollectIface();
            copier.copy(data, itf, null);
            itf.setProcessDate(nowDate);
            itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_NEW);
            list.add(itf);
        }
        list.forEach(this::insertSelective);

        // 验证接口表
        boolean validFlag = this.validate(tenantId, list);

        if (validFlag) {
            // 执行插入业务表逻辑，如果有必要
            for (ItfBneCollectIface data : list) {
                //获取物料批Id
                MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
                materialLotVo3.setMaterialLotCode(data.getSn());
                List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
                if (CollectionUtils.isNotEmpty(materialLotIds)) {
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(materialLotIds.get(0));
                    List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
                    BneCollectItfDTO1 bneCollectItfDTO1=itfBneCollectIfaceMapper.selectWorkType(materialLotIds.get(0));
                    List<HmeMaterialLotLoad> newList=new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(select)) {
                        for (HmeMaterialLotLoad temp :
                                select) {
                            String load = null;
                            switch (temp.getLoadRow().intValue()) {
                                case 1:
                                    load = "a" + temp.getLoadColumn();
                                    break;
                                case 2:
                                    load = "b" + temp.getLoadColumn();
                                    break;
                                case 3:
                                    load = "c" + temp.getLoadColumn();
                                    break;
                                case 4:
                                    load = "d" + temp.getLoadColumn();
                                    break;
                                case 5:
                                    load = "e" + temp.getLoadColumn();
                                    break;
                                case 6:
                                    load = "f" + temp.getLoadColumn();
                                    break;
                                case 7:
                                    load = "g" + temp.getLoadColumn();
                                    break;
                                default:
                                    break;
                            }
                            String result = getFieldValueByFieldName(load, data);
                            if (StringUtil.isNotEmpty(result)) {
                                temp.setHotSinkCode(result);
                                newList.add(temp);
                                //hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(temp);
                            }
                        }
                        if(CollectionUtils.isNotEmpty(newList))
                        {
                            Long collect = newList.stream().collect(Collectors.summingLong(HmeMaterialLotLoad::getCosNum));
                            if(!bneCollectItfDTO1.getPrimaryUomQty().equals(collect))
                            {
                                data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                                data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "ITF_DATA_COLLECT_0020", "ITF"));
                                itfBneCollectIfaceMapper.updateByPrimaryKeySelective(data);
                                continue;
                            }
                            if(Strings.isNotBlank(bneCollectItfDTO1.getWorkOrderType())&&!bneCollectItfDTO1.getWorkOrderType().equals("RK04")
                                    &&!bneCollectItfDTO1.getWorkOrderType().equals("RK05")&&!bneCollectItfDTO1.getWorkOrderType().equals("RK06")) {
                                Map<String, String>  map = objectToMap(data);
                                if (map.size() != 0) {
                                    List<String> values = map.values().stream().filter(t->Strings.isNotBlank(t)).distinct().collect(Collectors.toList());
                                    List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                                            .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                                                    .andIn(HmeMaterialLotLoad.FIELD_HOT_SINK_CODE, values)).build());
                                    List<String> hotSinkCodes = hmeMaterialLotLoads.stream().map(HmeMaterialLotLoad::getHotSinkCode).distinct().collect(Collectors.toList());
                                    String rowCol = "";
                                    String hotSinkCode="" ;
                                    Map<String, String> resultMap = sortMapByKey(map);    //按Key进行排序
                                    Set set = resultMap.keySet();
                                    for (Object o : set) {
                                        if(!Objects.isNull(0)) {
                                            if (hotSinkCodes.contains(resultMap.get(o))) {
                                                rowCol += o + ",";
                                                hotSinkCode += resultMap.get(o) + ",";
                                            }
                                        }
                                    }
                                    if(Strings.isNotBlank(rowCol))
                                    {
                                        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                                        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "ITF_DATA_COLLECT_0021", "ITF",
                                                rowCol.substring(0,rowCol.length()-1).toUpperCase(),hotSinkCode.substring(0,hotSinkCode.length()-1).toUpperCase()));
                                        itfBneCollectIfaceMapper.updateByPrimaryKeySelective(data);
                                        continue;
                                    }
                                }
                            }
                            for (HmeMaterialLotLoad temp:
                            newList) {
                                hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(temp);
                            }
                        }
                        data.setProcessStatus(WmsConstant.CONSTANT_Y);
                        itfBneCollectIfaceMapper.updateByPrimaryKeySelective(data);
                    } else {
                        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "ITF_DATA_COLLECT_0009", "ITF"));
                        itfBneCollectIfaceMapper.updateByPrimaryKeySelective(data);
                    }
                } else {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0008", "ITF"));
                    itfBneCollectIfaceMapper.updateByPrimaryKeySelective(data);
                }

            }
        }

        // 返回处理结果
        return InterfaceUtils.getReturnList(list);
    }


    private static Map objectToMap(Object obj) {

        Map map = new HashMap();
        try {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if((field.getName().startsWith("a")||field.getName().startsWith("b")||field.getName().startsWith("c")||field.getName().startsWith("d")||
                        field.getName().startsWith("e")||field.getName().startsWith("f")||field.getName().startsWith("g"))&&!Objects.isNull(field.get(obj))) {
                    // 20210427 modify by sanfeng.zhang for tianyang.xie  过滤掉值为空的数据
                    if (StringUtils.isNotBlank((String.valueOf(field.get(obj))))) {
                        map.put(field.getName(), (String) field.get(obj));
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return map;
    }

    private static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return (String) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
}
