package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.api.dto.HmeEqManageTagDTO;
import com.ruike.hme.api.dto.HmeEqManageTagGroupDTO;
import com.ruike.hme.api.dto.HmeManageTagDTO;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEqManageTag;
import com.ruike.hme.domain.repository.HmeEqManageTagRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEqManageTagMapper;
import com.ruike.hme.infra.mapper.HmeEqManageTagGroupMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.NumberHelper;
import io.tarzan.common.domain.util.PageUtil;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import com.ruike.hme.domain.repository.HmeEqManageTagGroupRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtTagVO;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备管理项目组表 资源库实现
 *
 * @author han.zhang03@hand-china.com 2020-06-10 14:58:51
 */
@Component
public class HmeEqManageTagGroupRepositoryImpl extends BaseRepositoryImpl<HmeEqManageTagGroup> implements HmeEqManageTagGroupRepository {
    @Autowired
    private HmeEqManageTagGroupMapper hmeEqManageTaggroupMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEqManageTagGroupRepository hmeEqManageTagGroupRepository;
    @Autowired
    private HmeEquipmentRepository equipmentRepository;
    @Autowired
    private HmeEqManageTagRepository hmeEqManageTagRepository;
    @Autowired
    private HmeEqManageTagMapper hmeEqManageTagMapper;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;


    @Override
    @ProcessLovValue
    public Page<HmeEquipTagGroupReturnVO> selectEquipTagGroupData(Long tenantId, PageRequest pageRequest, HmeEqManageTagGroup hmeEqManageTaggroup) {
        Page<HmeEquipTagGroupReturnVO> results =
                PageHelper.doPage(pageRequest, () -> hmeEqManageTaggroupMapper.selectEquipTagData(tenantId, hmeEqManageTaggroup));
        return results;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEqManageTagGroup update(Long tenantId, HmeEqManageTagGroup hmeEqManageTaggroups) {
        checkUnique(tenantId, hmeEqManageTaggroups);

        if (StringUtils.isEmpty(hmeEqManageTaggroups.getManageTagGroupId())) {
            hmeEqManageTaggroups.setStatus(MtBaseConstants.NO);
            hmeEqManageTaggroups.setTenantId(tenantId);
            this.insertSelective(hmeEqManageTaggroups);

            /**
             * 将项目组下的设备管理项保存至行表中
             */
            saveEquipmentTag(tenantId, hmeEqManageTaggroups, true);

        } else {
            int i = hmeEqManageTaggroupMapper.updateByPrimaryKeySelective(hmeEqManageTaggroups);
            if (i == 0) {
                throw new MtException("GENERAL_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "GENERAL_0001",
                                "GENERAL"));
            }
        }
        return hmeEqManageTaggroups;
    }

    @Override
    @ProcessLovValue
    public Page<HmeEquipManageTagReturnVO> selectEquipTagData(Long tenantId, PageRequest pageRequest, String manageTagGroupId) {
        //根据头id查询
        HmeEqManageTag equipmentManageTag = new HmeEqManageTag();
        equipmentManageTag.setManageTagGroupId(manageTagGroupId);
        Page<HmeEqManageTag> hmeEqManageTags =
                PageHelper.doPage(pageRequest, () -> hmeEqManageTagRepository.select(equipmentManageTag));

        Page<HmeEquipManageTagReturnVO> pages = new Page<>();
        BeanUtils.copyProperties(hmeEqManageTags, pages);

        List<HmeEquipManageTagReturnVO> hmeEquipManageTagReturnVOS = new ArrayList<>();
        hmeEqManageTags.forEach(item -> {
            HmeEquipManageTagReturnVO equipTagReturnVO = new HmeEquipManageTagReturnVO();
            BeanUtils.copyProperties(item, equipTagReturnVO);

            //查询数据类型
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("GENERAL");
            mtGenTypeVO2.setTypeGroup("TAG_VALUE_TYPE");
            mtGenTypeVO2.setTypeCode(item.getValueType());
            List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            if (CollectionUtils.isNotEmpty(mtGenTypes)) {
                equipTagReturnVO.setValueTypeMeaning(mtGenTypes.get(0).getDescription());
            }
            //查询收集方式
            MtGenTypeVO2 mtGenTypeVO21 = new MtGenTypeVO2();
            mtGenTypeVO21.setModule("GENERAL");
            mtGenTypeVO21.setTypeGroup("TAG_COLLECTION_METHOD");
            mtGenTypeVO21.setTypeCode(item.getCollectionMethod());
            List<MtGenType> mtGenTypes2 = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO21);
            if (CollectionUtils.isNotEmpty(mtGenTypes2)) {
                equipTagReturnVO.setCollectionMethodMeaning(mtGenTypes2.get(0).getDescription());
            }
            //查询单位编码
            if (StringUtils.isNotEmpty(item.getUomId())) {
                MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, item.getUomId());
                if (ObjectUtil.isNotNull(mtUomVO)) {
                    equipTagReturnVO.setUomCode(mtUomVO.getUomCode());
                }
            }
            hmeEquipManageTagReturnVOS.add(equipTagReturnVO);
//            pages.getContent().add(equipTagReturnVO);
        });
        //靠serialNumber排序
        List<HmeEquipManageTagReturnVO> hmeEquipManageTagReturnVOList = hmeEquipManageTagReturnVOS.stream().sorted(Comparator.comparing(HmeEquipManageTagReturnVO::getSerialNumber))
                .collect(Collectors.toList());
        pages.setContent(hmeEquipManageTagReturnVOList);
        return pages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEqManageTag> manageTagUpdate(Long tenantId, List<HmeEqManageTag> hmeEqManageTags) {
        hmeEqManageTags.forEach(vo -> {
            if (StringUtils.isEmpty(vo.getManageTagId())) {
                HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
                hmeEqManageTag.setManageTagGroupId(vo.getManageTagGroupId());
                hmeEqManageTag.setTenantId(tenantId);
                hmeEqManageTag.setTagId(vo.getTagId());
                List<HmeEqManageTag> hmeEqManageTagList = hmeEqManageTagRepository.select(hmeEqManageTag);
                if (CollectionUtils.isNotEmpty(hmeEqManageTagList)){
                    throw new MtException("HME_EQUIP_TAG_GROUP_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EQUIP_TAG_GROUP_0003",
                                    "HME", vo.getTagCode()));
                }
                //新增
                hmeEqManageTagRepository.insert(vo);
            } else {
                //保存
                    HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
                hmeEqManageTag.setTenantId(tenantId);
                hmeEqManageTag.setManageTagGroupId(vo.getManageTagGroupId());
                hmeEqManageTag.setTagId(vo.getTagId());
                List<HmeEqManageTag> hmeEqManageTagList = hmeEqManageTagRepository.select(hmeEqManageTag);
                    if(CollectionUtils.isNotEmpty(hmeEqManageTagList) && !StringUtils.equals(hmeEqManageTagList.get(0).getManageTagId(), vo.getManageTagId())){
                        throw new MtException("HME_EQUIP_TAG_GROUP_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EQUIP_TAG_GROUP_0003",
                                        "HME", vo.getTagCode()));
                    }
                int i = hmeEqManageTagMapper.updateByPrimaryKey(vo);
                //更改头状态
//                String tagGroupId = vo.getTagGroupId();
                HmeEqManageTagGroup equipmentManageTaggroup = new HmeEqManageTagGroup();
                equipmentManageTaggroup.setManageTagGroupId(vo.getManageTagGroupId());
                equipmentManageTaggroup = hmeEqManageTaggroupMapper.selectByPrimaryKey(equipmentManageTaggroup);

                equipmentManageTaggroup.setStatus("P");
                equipmentManageTaggroup.setObjectVersionNumber(equipmentManageTaggroup.getObjectVersionNumber());
                hmeEqManageTaggroupMapper.updateByPrimaryKeySelective(equipmentManageTaggroup);
            }
        });
        return hmeEqManageTags;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allSync(Long tenantId, String equipmentTagGroupId) {
        //将选中设备管理项目组的status置为N
        HmeEqManageTagGroup equipmentManageTaggroup = new HmeEqManageTagGroup();
        equipmentManageTaggroup.setManageTagGroupId(equipmentTagGroupId);
        HmeEqManageTagGroup equipmentManageTaggroup1 = this.selectByPrimaryKey(equipmentManageTaggroup);
        equipmentManageTaggroup1.setManageTagGroupId(equipmentTagGroupId);
        equipmentManageTaggroup1.setObjectVersionNumber(equipmentManageTaggroup.getObjectVersionNumber());
        equipmentManageTaggroup.setStatus(MtBaseConstants.NO);
        hmeEqManageTaggroupMapper.updateByPrimaryKeySelective(equipmentManageTaggroup);
        //删除项目组下的项目
//        HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
//        hmeEqManageTag.setManageTagGroupId(equipmentTagGroupId);
//        hmeEqManageTagRepository.delete(hmeEqManageTag);
        //保存当前管理项目组所关联的所有项目
        equipmentManageTaggroup.setManageTagGroupId(equipmentTagGroupId);
        equipmentManageTaggroup = this.selectByPrimaryKey(equipmentManageTaggroup);
        saveEquipmentTag2(tenantId, equipmentManageTaggroup, equipmentTagGroupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void partSync(Long tenantId, String manageTaggroupId) {
        //将选中设备管理项目组的status置为N
        HmeEqManageTagGroup equipmentManageTaggroup = new HmeEqManageTagGroup();
//        equipmentManageTaggroup.setManageTagGroupId(manageTaggroupId);
//        HmeEqManageTagGroup equipmentManageTaggroup1 = this.selectByPrimaryKey(equipmentManageTaggroup);
//        equipmentManageTaggroup1.setManageTagGroupId(manageTaggroupId);
//        equipmentManageTaggroup1.setObjectVersionNumber(equipmentManageTaggroup.getObjectVersionNumber());
//        equipmentManageTaggroup.setStatus(MtBaseConstants.NO);;
//        hmeEqManageTaggroupMapper.updateByPrimaryKeySelective(equipmentManageTaggroup);
        //保存当前管理项目组所关联的所有项目
        equipmentManageTaggroup.setManageTagGroupId(manageTaggroupId);
        equipmentManageTaggroup = this.selectByPrimaryKey(equipmentManageTaggroup);
        saveEquipmentTag(tenantId, equipmentManageTaggroup, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long tenantId, HmeEqManageTagDTO dto) {
        //删除项目组下的项目
        HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
        hmeEqManageTag.setTenantId(tenantId);
        hmeEqManageTag.setManageTagGroupId(dto.getManageTagGroupId());
        hmeEqManageTagRepository.delete(hmeEqManageTag);
        //删除项目组中的项目
        HmeEqManageTagGroup hmeEqManageTagGroup = new HmeEqManageTagGroup();
        hmeEqManageTagGroup.setManageTagGroupId(dto.getManageTagGroupId());
        hmeEqManageTagGroupRepository.delete(hmeEqManageTagGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteManageTag(Long tenantId, HmeEqManageTagGroupDTO dto) {
        //删除项目组下的项目
        HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
        hmeEqManageTag.setTenantId(tenantId);
        hmeEqManageTag.setTagId(dto.getTagId());
        hmeEqManageTagRepository.delete(hmeEqManageTag);
    }

    @Override
    public Page<HmeMtTagVO> queryManageTag(Long tenantId, HmeManageTagDTO dto, PageRequest pageRequest) {
        //HmeMtTagVO hmeMtTagVO =
//        for (int i = 0; i < hmeMtTagVOS.size(); i++){
//            MtExtendVO1 mtExtendVO = new MtExtendVO1();
//            mtExtendVO.setTableName("mt_tag_attr");
//            mtExtendVO.setKeyIdList(Collections.singletonList(hmeMtTagVOS.get(i).getTagId()));
//
//            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
//            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//            mtExtendVO5.setAttrName("STANDARD");
//            mtExtendVO5s.add(mtExtendVO5);
//
//            MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
//            mtExtendVO51.setAttrName("ACCURACY");
//            mtExtendVO5s.add(mtExtendVO51);
//
//            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
//            mtExtendVO52.setAttrName("INSPECTION_TOOL");
//            mtExtendVO5s.add(mtExtendVO52);
//
//            mtExtendVO.setAttrs(mtExtendVO5s);
//            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
//            for (int j= 0; j < mtExtendAttrVO1s.size(); j++){
//
//                if (HmeConstants.ExtendAttr.ACCURACY.equals(mtExtendAttrVO1s.get(j).getAttrName())) {
//                    if (NumberHelper.isDouble(mtExtendAttrVO1s.get(j).getAttrValue()) && ObjectUtil.isNotEmpty(mtExtendAttrVO1s.get(j).getAttrValue())) {
//                        hmeMtTagVOS.get(i).setAccuracy(Double.parseDouble(mtExtendAttrVO1s.get(j).getAttrValue()));
//                    }
//                }else if (HmeConstants.ExtendAttr.STANDARD.equals(mtExtendAttrVO1s.get(j).getAttrName()) && ObjectUtil.isNotEmpty(mtExtendAttrVO1s.get(j).getAttrValue())){
//                    hmeMtTagVOS.get(i).setStandardValue(Double.parseDouble(mtExtendAttrVO1s.get(j).getAttrValue()));
//                }
//            }
//        }

        Page<HmeMtTagVO> result = PageHelper.doPage(pageRequest, () -> hmeEqManageTaggroupMapper.queryManageTag(tenantId, dto));
        return result;
    }

    @Override
    @ProcessLovValue
    public List<HmeEquipmentTagVO2> export(Long tenantId, HmeEqManageTagGroup hmeEqManageTagGroup) {
        List<HmeEquipmentTagVO2> resultList = hmeEqManageTaggroupMapper.eqManageExport(tenantId, hmeEqManageTagGroup);
        // 数值类型
        MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("GENERAL");
        mtGenTypeVO2.setTypeGroup("TAG_VALUE_TYPE");
        List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        // 收集方式
        MtGenTypeVO2 mtGenTypeVO21 = new MtGenTypeVO2();
        mtGenTypeVO21.setModule("GENERAL");
        mtGenTypeVO21.setTypeGroup("TAG_COLLECTION_METHOD");
        List<MtGenType> mtGenTypes2 = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO21);
        for (HmeEquipmentTagVO2 hmeEquipmentTagVO2 : resultList) {
            if (StringUtils.isNotBlank(hmeEquipmentTagVO2.getValueType())) {
                Optional<MtGenType> firstOpt = mtGenTypes.stream().filter(dto -> StringUtils.equals(dto.getTypeCode(), hmeEquipmentTagVO2.getValueType())).findFirst();
                if (firstOpt.isPresent()) {
                    hmeEquipmentTagVO2.setValueTypeMeaning(firstOpt.get().getDescription());
                }
            }
            if (StringUtils.isNotBlank(hmeEquipmentTagVO2.getCollectionMethod())) {
                Optional<MtGenType> firstOpt = mtGenTypes2.stream().filter(dto -> StringUtils.equals(dto.getTypeCode(), hmeEquipmentTagVO2.getCollectionMethod())).findFirst();
                if (firstOpt.isPresent()) {
                    hmeEquipmentTagVO2.setCollectionMethodMeaning(firstOpt.get().getDescription());
                }
            }
        }
        return resultList;
    }


    public void checkUnique(Long tenantId, HmeEqManageTagGroup item) {

        HmeEqManageTagGroup hmeEqManageTaggroup = new HmeEqManageTagGroup();
        //新增时校验唯一性
        if (StringUtils.isEmpty(item.getManageTagGroupId())) {
            hmeEqManageTaggroup.setEquipmentCategory(item.getEquipmentCategory());
            hmeEqManageTaggroup.setBusinessId(item.getBusinessId());
            hmeEqManageTaggroup.setOperationId(item.getOperationId());
            hmeEqManageTaggroup.setManageType(item.getManageType());
            //TODO
            List<HmeEqManageTagGroup> equipmentManageTaggroups = hmeEqManageTaggroupMapper.selectTagGroup(tenantId, hmeEqManageTaggroup);
//            List<HmeEqManageTagGroup> equipmentManageTaggroups = this.select(hmeEqManageTaggroup);
            if (CollectionUtils.isNotEmpty(equipmentManageTaggroups)) {
                throw new MtException("HME_EQUIP_TAG_GROUP_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EQUIP_TAG_GROUP_0001",
                                "HME", item.getEquipmentCategory()));
            }

//                hmeEqManageTaggroup.setEquipmentCategory(null);
//                hmeEqManageTaggroup.setEquipmentId(item.getEquipmentId());
//                equipmentManageTaggroups = this.select(hmeEqManageTaggroup);
//                if(CollectionUtils.isNotEmpty(equipmentManageTaggroups)){
//                    HmeEquipment equipment = new HmeEquipment();
//                    equipment.setEquipmentId(item.getEquipmentId());
//                    equipment = equipmentRepository.selectByPrimaryKey(equipment);
//                    throw new MtException("HME_EQUIP_TAG_GROUP_0002",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EQUIP_TAG_GROUP_0002",
//                                    "HME", equipment.getDescriptions()));
//                }
        }

        //保存
        if (StringUtils.isNotEmpty(item.getManageTagGroupId())) {
            hmeEqManageTaggroup.setEquipmentCategory(item.getEquipmentCategory());
            hmeEqManageTaggroup.setTagGroupId(item.getTagGroupId());
            hmeEqManageTaggroup.setEnableFlag(item.getEnableFlag());
            List<HmeEqManageTagGroup> equipmentManageTaggroups = this.select(hmeEqManageTaggroup);
            if (CollectionUtils.isNotEmpty(equipmentManageTaggroups) && !equipmentManageTaggroups.get(0).getManageTagGroupId().equals(item.getManageTagGroupId())) {
                throw new MtException("HME_EQUIP_TAG_GROUP_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EQUIP_TAG_GROUP_0001",
                                "HME", item.getEquipmentCategory()));
            }

//                hmeEqManageTaggroup.setEquipmentCategory(null);
//                hmeEqManageTaggroup.setEquipmentId(item.getEquipmentId());
//                equipmentManageTaggroups = this.select(hmeEqManageTaggroup);
//                if(CollectionUtils.isNotEmpty(equipmentManageTaggroups) && !equipmentManageTaggroups.get(0).getManageTagGroupId().equals(item.getManageTagGroupId())){
//                    HmeEquipment equipment = new HmeEquipment();
//                    equipment.setEquipmentId(item.getEquipmentId());
//                    equipment = equipmentRepository.selectByPrimaryKey(equipment);
//                    throw new MtException("HME_EQUIP_TAG_GROUP_0002",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EQUIP_TAG_GROUP_0002",
//                                    "HME", equipment.getDescriptions()));
//                }
        }

    }

    /**
     * @param isAll 是否是全量同步即是否是全部保存
     * @return void
     * @Description 保存当前管理项目组所关联的所有项目
     * @Date 2020-06-11 16:25
     * @Author han.zhang
     */
    public void saveEquipmentTag(Long tenantId, HmeEqManageTagGroup item, boolean isAll) {
        //不是全量同步。则需要判断是不是已存在，先查询所有的tagId
        List<String> tagIdList = new ArrayList<>();
        if (!isAll) {
            HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
            hmeEqManageTag.setManageTagGroupId(item.getManageTagGroupId());
            List<HmeEqManageTag> equipmentManageTags = hmeEqManageTagRepository.select(hmeEqManageTag);
            tagIdList = equipmentManageTags.stream().map(HmeEqManageTag::getTagId).collect(Collectors.toList());
        }

        MtTagGroup mtTagGroup = new MtTagGroup();
        mtTagGroup.setTagGroupId(item.getTagGroupId());
        mtTagGroup = mtTagGroupRepository.selectByPrimaryKey(mtTagGroup);

        MtTagVO mtTagVO = new MtTagVO();
        mtTagVO.setTagGroupCode(mtTagGroup.getTagGroupCode());
        List<String> tagGroupAssignIds = mtTagRepository.tagCodeAndGroupCodeLimitTagGroupAssignGet(tenantId, mtTagVO);


        List<String> finalTagIdList = tagIdList;
        tagGroupAssignIds.forEach(assignId -> {
            MtTagGroupAssign mtTagGroupAssign = mtTagGroupAssignRepository.tagGroupAssignGet(tenantId, assignId);
            if(ObjectUtil.isNull(mtTagGroupAssign)){
                return;
            }
            MtTag mtTag = null;
            mtTag = mtTagRepository.tagGet(tenantId, mtTagGroupAssign.getTagId());
            if (ObjectUtil.isNull(mtTag)) {
                return;
            }
            //如果是增量同步判断是不是已经有该数据了
            if (!isAll && CollectionUtils.isNotEmpty(finalTagIdList)) {
                if (finalTagIdList.contains(mtTag.getTagId())) {
                    return;
                }
            }

            MtExtendVO1 mtExtendVO = new MtExtendVO1();
            mtExtendVO.setTableName("mt_tag_attr");
            mtExtendVO.setKeyIdList(Collections.singletonList(mtTag.getTagId()));

            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("STANDARD");
            mtExtendVO5s.add(mtExtendVO5);

            MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
            mtExtendVO51.setAttrName("ACCURACY");
            mtExtendVO5s.add(mtExtendVO51);

            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("INSPECTION_TOOL");
            mtExtendVO5s.add(mtExtendVO52);

            mtExtendVO.setAttrs(mtExtendVO5s);
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);

            HmeEqManageTag equipmentManageTag = new HmeEqManageTag();
            equipmentManageTag.setTenantId(item.getTenantId());
            equipmentManageTag.setSiteId(item.getSiteId());
            equipmentManageTag.setManageTagGroupId(item.getManageTagGroupId());
            if (ObjectUtil.isNotNull(mtTagGroupAssign.getSerialNumber())) {
                equipmentManageTag.setSerialNumber(mtTagGroupAssign.getSerialNumber().longValue());
            }
//            equipmentManageTag.setTagGroupId(mtTagGroupAssign.getTagGroupId());
            equipmentManageTag.setTagId(mtTag.getTagId());
            equipmentManageTag.setTagCode(mtTag.getTagCode());
            equipmentManageTag.setTagDescriptions(mtTag.getTagDescription());
            equipmentManageTag.setValueType(mtTag.getValueType());
            if (StringUtils.isNotEmpty(mtTag.getCollectionMethod())) {
                equipmentManageTag.setCollectionMethod(mtTag.getCollectionMethod());
            }
            if (ObjectUtil.isNotNull(mtTag.getMinimumValue())) {
                equipmentManageTag.setMinimumValue(BigDecimal.valueOf(mtTag.getMinimumValue()));
            }
            if (ObjectUtil.isNotNull(mtTag.getMaximalValue())) {
                equipmentManageTag.setMaximalValue(BigDecimal.valueOf(mtTag.getMaximalValue()));
            }
            if (StringUtils.isNotEmpty(mtTag.getUnit())) {
                equipmentManageTag.setUomId(mtTag.getUnit());
            }
            if (StringUtils.isNotEmpty(mtTag.getRemark())) {
                equipmentManageTag.setRemark(mtTag.getRemark());
            }
            if (StringUtils.isNotEmpty(mtTag.getTrueValue())) {
                equipmentManageTag.setTrueValue(mtTag.getTrueValue());
            }
            if (StringUtils.isNotEmpty(mtTag.getFalseValue())) {
                equipmentManageTag.setFalseValue(mtTag.getFalseValue());
            }
            equipmentManageTag.setEnableFlag(mtTag.getEnableFlag());
            mtExtendAttrVO1s.forEach(vo -> {
                if (HmeConstants.ExtendAttr.ACCURACY.equals(vo.getAttrName())) {
                    if (NumberHelper.isDouble(vo.getAttrValue())) {
                        equipmentManageTag.setAccuracy(new BigDecimal(vo.getAttrValue()));
                    }
                } else if (HmeConstants.ExtendAttr.STANDARD.equals(vo.getAttrName())) {
                    if (NumberHelper.isDouble(vo.getAttrValue())) {
                        equipmentManageTag.setStandardValue(new BigDecimal(vo.getAttrValue()));
                    }
                } else if (HmeConstants.ExtendAttr.INSPECTION_TOOL.equals(vo.getAttrName())) {
                    equipmentManageTag.setTool(vo.getAttrValue());
                }

            });
            hmeEqManageTagRepository.insert(equipmentManageTag);
        });
    }

    /**
     * 全量保存
     *
     * @param tenantId
     * @param item
     */
    public void saveEquipmentTag2(Long tenantId, HmeEqManageTagGroup item, String equipmentTagGroupId) {
        List<String> tagIdList = new ArrayList<>();
        HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
        hmeEqManageTag.setManageTagGroupId(item.getManageTagGroupId());
        List<HmeEqManageTag> equipmentManageTags = hmeEqManageTagRepository.select(hmeEqManageTag);
        tagIdList = equipmentManageTags.stream().map(HmeEqManageTag::getTagId).collect(Collectors.toList());

        MtTagGroup mtTagGroup = new MtTagGroup();
        mtTagGroup.setTagGroupId(item.getTagGroupId());
        mtTagGroup = mtTagGroupRepository.selectByPrimaryKey(mtTagGroup);

        MtTagVO mtTagVO = new MtTagVO();
        mtTagVO.setTagGroupCode(mtTagGroup.getTagGroupCode());
        List<String> tagGroupAssignIds = mtTagRepository.tagCodeAndGroupCodeLimitTagGroupAssignGet(tenantId, mtTagVO);

        List<String> finalTagIdList = tagIdList;
        tagGroupAssignIds.forEach(assignId -> {
            MtTagGroupAssign mtTagGroupAssign = mtTagGroupAssignRepository.tagGroupAssignGet(tenantId, assignId);
            if(ObjectUtil.isNull(mtTagGroupAssign)){
                return;
            }
            MtTag mtTag = null;
            mtTag = mtTagRepository.tagGet(tenantId, mtTagGroupAssign.getTagId());
            if (ObjectUtil.isNull(mtTag)) {
                return;
            }
            //已经有该数据了则删除
            if (CollectionUtils.isNotEmpty(finalTagIdList)) {
                if (finalTagIdList.contains(mtTag.getTagId())) {
                    // 删除存在的TagId数据
//                    HmeEqManageTag hmeEqManageTag = new HmeEqManageTag();
//                    hmeEqManageTag.setManageTagGroupId(equipmentTagGroupId);
//                    hmeEqManageTagRepository.delete(hmeEqManageTag);
                    HmeEqManageTag hmeEqManageTag1 = new HmeEqManageTag();
                    hmeEqManageTag1.setTenantId(tenantId);
                    hmeEqManageTag1.setTagId(mtTag.getTagId());
                    hmeEqManageTag1.setManageTagGroupId(equipmentTagGroupId);
                    hmeEqManageTagRepository.delete(hmeEqManageTag1);

                }
            }
            MtExtendVO1 mtExtendVO = new MtExtendVO1();
            mtExtendVO.setTableName("mt_tag_attr");
            mtExtendVO.setKeyIdList(Collections.singletonList(mtTag.getTagId()));

            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("STANDARD");
            mtExtendVO5s.add(mtExtendVO5);

            MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
            mtExtendVO51.setAttrName("ACCURACY");
            mtExtendVO5s.add(mtExtendVO51);

            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("INSPECTION_TOOL");
            mtExtendVO5s.add(mtExtendVO52);

            mtExtendVO.setAttrs(mtExtendVO5s);
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);

            HmeEqManageTag equipmentManageTag = new HmeEqManageTag();
            equipmentManageTag.setTenantId(item.getTenantId());
            equipmentManageTag.setSiteId(item.getSiteId());
            equipmentManageTag.setManageTagGroupId(item.getManageTagGroupId());
            if (ObjectUtil.isNotNull(mtTagGroupAssign.getSerialNumber())) {
                equipmentManageTag.setSerialNumber(mtTagGroupAssign.getSerialNumber().longValue());
            }
//            equipmentManageTag.setTagGroupId(mtTagGroupAssign.getTagGroupId());
            equipmentManageTag.setTagId(mtTag.getTagId());
            equipmentManageTag.setTagCode(mtTag.getTagCode());
            equipmentManageTag.setTagDescriptions(mtTag.getTagDescription());
            equipmentManageTag.setValueType(mtTag.getValueType());
            if (StringUtils.isNotEmpty(mtTag.getCollectionMethod())) {
                equipmentManageTag.setCollectionMethod(mtTag.getCollectionMethod());
            }
            if (ObjectUtil.isNotNull(mtTag.getMinimumValue())) {
                equipmentManageTag.setMinimumValue(BigDecimal.valueOf(mtTag.getMinimumValue()));
            }
            if (ObjectUtil.isNotNull(mtTag.getMaximalValue())) {
                equipmentManageTag.setMaximalValue(BigDecimal.valueOf(mtTag.getMaximalValue()));
            }
            if (StringUtils.isNotEmpty(mtTag.getUnit())) {
                equipmentManageTag.setUomId(mtTag.getUnit());
            }
            if (StringUtils.isNotEmpty(mtTag.getRemark())) {
                equipmentManageTag.setRemark(mtTag.getRemark());
            }
            if (StringUtils.isNotEmpty(mtTag.getTrueValue())) {
                equipmentManageTag.setTrueValue(mtTag.getTrueValue());
            }
            if (StringUtils.isNotEmpty(mtTag.getFalseValue())) {
                equipmentManageTag.setFalseValue(mtTag.getFalseValue());
            }
            equipmentManageTag.setEnableFlag(mtTag.getEnableFlag());
            mtExtendAttrVO1s.forEach(vo -> {
                if (HmeConstants.ExtendAttr.ACCURACY.equals(vo.getAttrName())) {
                    if (NumberHelper.isDouble(vo.getAttrValue())) {
                        equipmentManageTag.setAccuracy(new BigDecimal(vo.getAttrValue()));
                    }
                } else if (HmeConstants.ExtendAttr.STANDARD.equals(vo.getAttrName())) {
                    if (NumberHelper.isDouble(vo.getAttrValue())) {
                        equipmentManageTag.setStandardValue(new BigDecimal(vo.getAttrValue()));
                    }
                } else if (HmeConstants.ExtendAttr.INSPECTION_TOOL.equals(vo.getAttrName())) {
                    equipmentManageTag.setTool(vo.getAttrValue());
                }

            });
            hmeEqManageTagRepository.insert(equipmentManageTag);
        });
    }
}
