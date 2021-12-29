package tarzan.modeling.app.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.core.domain.PageInfo;
import io.tarzan.common.domain.util.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.modeling.api.dto.*;
import tarzan.modeling.app.service.MtModOrganizationRelService;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.*;
import tarzan.modeling.infra.mapper.*;

/**
 * 组织结构关系应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Service
public class MtModOrganizationRelServiceImpl extends BaseServiceImpl<MtModOrganizationRel>
        implements MtModOrganizationRelService {

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;

    @Autowired
    private MtModLocatorMapper mtModLocatorMapper;

    @Autowired
    private MtModEnterpriseMapper mtModEnterpriseMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModOrganizationRelMapper mtModOrganizationRelMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModSiteMapper mtModSiteMapper;

    @Autowired
    private MtModProductionLineMapper mtModProductionLineMapper;

    @Autowired
    private MtModAreaMapper mtModAreaMapper;

    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;

    @Autowired
    private MtModLocatorOrgRelMapper mtModLocatorOrgRelMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public MtModOrganizationChildrenVO getChildrenByParentForUi(Long tenantId, MtModOrganizationRelDTO7 dto) {
        boolean emptyFlag = StringUtils.isEmpty(dto.getParentOrganizationId())
                && StringUtils.isEmpty(dto.getParentOrganizationType());
        boolean notEmptyFlag = StringUtils.isNotEmpty(dto.getParentOrganizationId())
                && StringUtils.isNotEmpty(dto.getParentOrganizationType());
        if (!(emptyFlag || notEmptyFlag)) {
            throw new MtException("MT_MODELING_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0043", "MODELING"));
        }
        MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
        queryVO.setTopSiteId(dto.getTopSiteId());
        queryVO.setParentOrganizationId(dto.getParentOrganizationId());
        queryVO.setParentOrganizationType(dto.getParentOrganizationType());
        return mtModOrganizationRelRepository.siteLimitOrganizationTree(tenantId, queryVO);
    }

    @Override
    public MtModOrganizationChildrenVO getOrganizationTreeForUi(Long tenantId) {
        MtModOrganizationChildrenVO result = new MtModOrganizationChildrenVO();
        MtModEnterprise param = new MtModEnterprise();
        param.setEnableFlag("Y");
        List<MtModEnterprise> list = mtModEnterpriseMapper.select(param);
        if (list.size() > 1) {
            throw new MtException("MT_MODELING_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0033", "MODELING"));
        } else if (list.size() == 0) {
            return result;
        }

        // 根据可用企业构建完整树
        return mtModOrganizationRelRepository.allOrganizationTree(tenantId, list.get(0).getEnterpriseId());
    }

    /**
     * UI构建单层树
     *
     * @author chuang.yang
     * @date 2019/11/11
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.modeling.domain.vo.MtModOrganizationSingleChildrenVO>
     */
    @Override
    public List<MtModOrganizationSingleChildrenVO> getOrganizationTreeSingleForUi(Long tenantId,
                                                                                  MtModOrganizationSingleNodeVO dto) {
        return mtModOrganizationRelRepository.singleOrganizationTree(tenantId, dto.getTopSiteId(),
                dto.getParentOrganizationType(), dto.getParentOrganizationId(), dto.getIsOnhand());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyForUi(Long tenantId, MtModOrganizationCopyVO dto) {
        if ("LOCATOR".equals(dto.getOrganizationType())) {
            // 查询顶层站点
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getTopSiteId());

            // 查询该库位跟顶层站点类型关系
            MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
            rel.setLocatorId(dto.getOrganizationId());
            rel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
            rel.setSiteType(mtModSite.getSiteType());
            List<MtModLocatorOrgRelVO3> existSiteList =
                    mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);

            // 关系存在则报错
            if (CollectionUtils.isNotEmpty(existSiteList)
                    && !existSiteList.get(0).getOrganizationId().equals(mtModSite.getSiteId())) {
                throw new MtException("MT_MODELING_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MODELING_0051", "MODELING", ""));
            }

            if ("LOCATOR".equals(dto.getParentOrganizationType())) {
                mtModLocatorRepository.locatorRelVerify(tenantId, dto.getOrganizationId(),
                        dto.getParentOrganizationId());
            } else {
                mtModLocatorOrgRelRepository.locatorOrganizationRelVerify(tenantId, dto.getOrganizationId(),
                        dto.getParentOrganizationType(), dto.getParentOrganizationId());
            }

            MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
            mtModLocatorOrgRel.setTenantId(tenantId);
            mtModLocatorOrgRel.setOrganizationType(dto.getTargetOrganizationType());
            mtModLocatorOrgRel.setOrganizationId(dto.getTargetOrganizationId());
            mtModLocatorOrgRel.setLocatorId(dto.getOrganizationId());
            mtModLocatorOrgRel = mtModLocatorOrgRelMapper.selectOne(mtModLocatorOrgRel);
            if (mtModLocatorOrgRel != null) {
                throw new MtException("MT_MODELING_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MODELING_0016", "MODELING"));
            }
            mtModLocatorOrgRel = new MtModLocatorOrgRel();
            mtModLocatorOrgRel.setTenantId(tenantId);
            mtModLocatorOrgRel.setOrganizationType(dto.getTargetOrganizationType());
            mtModLocatorOrgRel.setOrganizationId(dto.getTargetOrganizationId());
            mtModLocatorOrgRel.setLocatorId(dto.getOrganizationId());
            mtModLocatorOrgRelRepository.insertSelective(mtModLocatorOrgRel);
        } else {
            mtModOrganizationRelRepository.organizationRelCopy(tenantId, dto);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteForUi(Long tenantId, MtModOrganizationDTO dto) {
        if ("LOCATOR".equals(dto.getOrganizationType())) {
            if ("LOCATOR".equals(dto.getParentOrganizationType())) {
                MtModLocator tmp = new MtModLocator();
                tmp.setLocatorId(dto.getOrganizationId());
                tmp.setParentLocatorId(dto.getParentOrganizationId());
                mtModLocatorRepository.locatorRelDelete(tenantId, tmp);
            } else {
                MtModLocatorOrgRel tmp = new MtModLocatorOrgRel();
                tmp.setOrganizationId(dto.getParentOrganizationId());
                tmp.setOrganizationType(dto.getParentOrganizationType());
                tmp.setLocatorId(dto.getOrganizationId());
                mtModLocatorOrgRelRepository.locatorOrganizationRelDelete(tenantId, tmp);
            }
        } else {
            MtModOrganizationRel tmp = new MtModOrganizationRel();
            tmp.setOrganizationId(dto.getOrganizationId());
            tmp.setTopSiteId(dto.getTopSiteId());
            tmp.setParentOrganizationId(dto.getParentOrganizationId());
            tmp.setOrganizationType(dto.getOrganizationType());
            tmp.setParentOrganizationType(dto.getParentOrganizationType());
            mtModOrganizationRelRepository.organizationRelDelete(tenantId, tmp);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignForUi(Long tenantId, MtModOrganizationDTO2 dto) {
        if ("LOCATOR_GROUP".equals(dto.getOrganizationType())) {

            // 遍历所有的库位组
            List<String> ls = mtModLocatorRepository.locatorGroupLimitLocatorQuery(tenantId, dto.getOrganizationId(),
                    "TOP");
            for (String l : ls) {
                MtModOrganizationRel rtmp = new MtModOrganizationRel();
                rtmp.setOrganizationId(l);
                rtmp.setOrganizationType("LOCATOR");
                rtmp.setParentOrganizationType(dto.getParentOrganizationType());
                rtmp.setParentOrganizationId(dto.getParentOrganizationId());
                rtmp.setTopSiteId(dto.getTopSiteId());
                addOrganizationNode(tenantId, rtmp);
            }
        } else {
            MtModOrganizationRel tmp = new MtModOrganizationRel();
            tmp.setOrganizationId(dto.getOrganizationId());
            tmp.setOrganizationType(dto.getOrganizationType());
            tmp.setParentOrganizationType(dto.getParentOrganizationType());
            tmp.setParentOrganizationId(dto.getParentOrganizationId());
            if ("ENTERPRISE".equals(dto.getParentOrganizationType()) && "SITE".equals(dto.getOrganizationType())) {
                // 上层是企业时不能根据顶层站点来进行筛选
                dto.setTopSiteId(dto.getOrganizationId());
            }
            tmp.setTopSiteId(dto.getTopSiteId());
            addOrganizationNode(tenantId, tmp);
        }
    }

    /**
     * UI 树状图剪切节点功能
     *
     * @author chuang.yang
     * @date 2020/1/16
     * @param tenantId
     * @param dto
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cutForUi(Long tenantId, MtModOrganizationCopyVO dto) {
        // 功能实现思路：先查询保留原有关系数据，再删除原有的关系，再执行新增关系
        if (StringUtils.isEmpty(dto.getTopSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId"));
        }
        if (StringUtils.isEmpty(dto.getParentOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationType"));
        }
        if (StringUtils.isEmpty(dto.getParentOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationId"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationId"));
        }
        if (StringUtils.isEmpty(dto.getTargetSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "targetSiteId"));
        }
        if (StringUtils.isEmpty(dto.getTargetOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "targetOrganizationType"));
        }
        if (StringUtils.isEmpty(dto.getTargetOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "targetOrganizationId"));
        }

        // 获取组织类型数据
        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_TYPE");
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_MODELING_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0018", "MODELING"));
        }

        Optional<MtGenType> any = mtGenTypes.stream()
                .filter(t -> t.getTypeCode().equals(dto.getParentOrganizationType())).findAny();
        if (!any.isPresent()) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "parentOrganizationType"));
        }
        any = mtGenTypes.stream().filter(t -> t.getTypeCode().equals(dto.getOrganizationType())).findAny();
        if (!any.isPresent()) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "parentOrganizationType"));
        }
        any = mtGenTypes.stream().filter(t -> t.getTypeCode().equals(dto.getTargetOrganizationType())).findAny();
        if (!any.isPresent()) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "parentOrganizationType"));
        }

        MtModOrganizationRel source = new MtModOrganizationRel();
        source.setTenantId(tenantId);
        source.setParentOrganizationId(dto.getParentOrganizationId());
        source.setParentOrganizationType(dto.getParentOrganizationType());
        source.setTopSiteId(dto.getTopSiteId());
        source.setOrganizationId(dto.getOrganizationId());
        source.setOrganizationType(dto.getOrganizationType());
        source = mtModOrganizationRelMapper.selectOne(source);

        if (source == null) {
            throw new MtException("MT_MODELING_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0052", "MODELING"));
        }

        // 获取传入复制的组织的子结构
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getTopSiteId());
        mtModOrganizationVO2.setParentOrganizationType(dto.getOrganizationType());
        mtModOrganizationVO2.setParentOrganizationId(dto.getOrganizationId());
        // 子组织查询API有限制，必须输入查询目标组织类型，WKC为最底层，则输入WKC，表示获取所有组织
        mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
        mtModOrganizationVO2.setQueryType(MtBaseConstants.QUERY_TYPE.ALL);
        List<MtModOrganizationItemVO> modOrganizationItemList =
                mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);

        // 获取全部结构的组织关系ID集合
        List<String> relIdList = modOrganizationItemList.stream().map(MtModOrganizationItemVO::getRelId)
                .collect(Collectors.toList());

        // 根据需要处理的组织关系ID，获取对应的组织关系数据
        List<MtModOrganizationRel> organizationRels = null;
        if (CollectionUtils.isNotEmpty(relIdList)) {
            organizationRels = mtModOrganizationRelMapper.selectByRelIds(tenantId, relIdList);
        }

        // 添加自己
        if (CollectionUtils.isEmpty(organizationRels)) {
            organizationRels = new ArrayList<MtModOrganizationRel>(2);
            organizationRels.add(source);
        } else {
            organizationRels.add(source);
        }

        // 先执行删除原有关系数据
        MtModOrganizationDTO mtModOrganizationDTO = new MtModOrganizationDTO();
        mtModOrganizationDTO.setTopSiteId(dto.getTopSiteId());
        mtModOrganizationDTO.setParentOrganizationType(dto.getParentOrganizationType());
        mtModOrganizationDTO.setParentOrganizationId(dto.getParentOrganizationId());
        mtModOrganizationDTO.setOrganizationType(dto.getOrganizationType());
        mtModOrganizationDTO.setOrganizationId(dto.getOrganizationId());
        this.deleteForUi(tenantId, mtModOrganizationDTO);

        // 整理需要新增到目标组织的组织关系数据
        for (MtModOrganizationRel organizationRel : organizationRels) {
            organizationRel.setTenantId(tenantId);
            organizationRel.setOrganizationRelId(null);
            organizationRel.setTopSiteId(dto.getTargetSiteId());
            if (organizationRel.getParentOrganizationType().equals(source.getParentOrganizationType())
                    && organizationRel.getParentOrganizationId().equals(source.getParentOrganizationId())) {
                organizationRel.setParentOrganizationId(dto.getTargetOrganizationId());
                organizationRel.setParentOrganizationType(dto.getTargetOrganizationType());
            }

            // 数据校验
            switch (organizationRel.getOrganizationType()) {
                case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                    mtModOrganizationRelRepository.siteOrganizationRelVerify(tenantId,
                            organizationRel.getOrganizationId(), organizationRel.getParentOrganizationType(),
                            organizationRel.getParentOrganizationId());
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                    mtModOrganizationRelRepository.areaOrganizationRelVerify(tenantId,
                            organizationRel.getOrganizationId(), organizationRel.getTopSiteId(),
                            organizationRel.getParentOrganizationType(),
                            organizationRel.getParentOrganizationId());
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                    mtModOrganizationRelRepository.workcellOrganizationRelVerify(tenantId,
                            organizationRel.getOrganizationId(), organizationRel.getTopSiteId(),
                            organizationRel.getParentOrganizationType(),
                            organizationRel.getParentOrganizationId());
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                    mtModOrganizationRelRepository.prodLineOrganizationRelVerify(tenantId,
                            organizationRel.getOrganizationId(), organizationRel.getTopSiteId(),
                            organizationRel.getParentOrganizationType(),
                            organizationRel.getParentOrganizationId());
                    break;
                default:
                    break;
            }
        }

        List<String> sqlList = new ArrayList<>(organizationRels.size());

        // 批量获取主键ID和CID
        List<String> organizationRelIdS =
                customDbRepository.getNextKeys("mt_mod_organization_rel_s", organizationRels.size());
        List<String> organizationRelCidS =
                customDbRepository.getNextKeys("mt_mod_organization_rel_cid_s", organizationRels.size());
        for (MtModOrganizationRel newOrganizationRel : organizationRels) {
            newOrganizationRel.setOrganizationRelId(organizationRelIdS.remove(0));
            newOrganizationRel.setCid(Long.valueOf(organizationRelCidS.remove(0)));
            sqlList.addAll(customDbRepository.getInsertSql(newOrganizationRel));
        }

        // 插入数据
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrganizationNode(Long tenantId, MtModOrganizationRel dto) {
        String checkResult = "N";
        switch (dto.getOrganizationType()) {
            case "SITE":
                checkResult = mtModOrganizationRelRepository.siteOrganizationRelVerify(tenantId,
                        dto.getOrganizationId(), dto.getParentOrganizationType(),
                        dto.getParentOrganizationId());
                break;
            case "LOCATOR":
                if ("LOCATOR".equals(dto.getParentOrganizationType())) {
                    checkResult = mtModLocatorRepository.locatorRelVerify(tenantId, dto.getOrganizationId(),
                            dto.getParentOrganizationId());
                } else {
                    // 查询顶层站点
                    MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getTopSiteId());

                    // 查询该库位跟顶层站点类型关系
                    MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
                    rel.setLocatorId(dto.getOrganizationId());
                    rel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                    rel.setSiteType(mtModSite.getSiteType());
                    List<MtModLocatorOrgRelVO3> existSiteList =
                            mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);

                    // 关系存在则报错
                    if (CollectionUtils.isNotEmpty(existSiteList)
                            && !existSiteList.get(0).getOrganizationId().equals(mtModSite.getSiteId())) {
                        throw new MtException("MT_MODELING_0051", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "MT_MODELING_0051", "MODELING", ""));
                    }

                    checkResult = mtModLocatorOrgRelRepository.locatorOrganizationRelVerify(tenantId,
                            dto.getOrganizationId(), dto.getParentOrganizationType(),
                            dto.getParentOrganizationId());
                }
                break;
            case "AREA":
                checkResult = mtModOrganizationRelRepository.areaOrganizationRelVerify(tenantId,
                        dto.getOrganizationId(), dto.getTopSiteId(), dto.getParentOrganizationType(),
                        dto.getParentOrganizationId());

                break;
            case "WORKCELL":
                checkResult = mtModOrganizationRelRepository.workcellOrganizationRelVerify(tenantId,
                        dto.getOrganizationId(), dto.getTopSiteId(), dto.getParentOrganizationType(),
                        dto.getParentOrganizationId());
                break;
            case "PROD_LINE":
                checkResult = mtModOrganizationRelRepository.prodLineOrganizationRelVerify(tenantId,
                        dto.getOrganizationId(), dto.getTopSiteId(), dto.getParentOrganizationType(),
                        dto.getParentOrganizationId());
                break;
            default:
                break;
        }

        if ("LOCATOR".equals(dto.getOrganizationType())) {
            if ("LOCATOR".equals(dto.getParentOrganizationType())) {
                if ("Y".equals(checkResult)) {
                    MtModLocator tmp = new MtModLocator();
                    tmp.setTenantId(tenantId);
                    tmp.setLocatorId(dto.getOrganizationId());
                    tmp = mtModLocatorMapper.selectOne(tmp);

                    tmp.setParentLocatorId(dto.getParentOrganizationId());
                    tmp.setTenantId(tenantId);
                    mtModLocatorRepository.updateByPrimaryKey(tmp);
                }
            } else {
                if ("Y".equals(checkResult)) {
                    MtModLocatorOrgRel tmp = new MtModLocatorOrgRel();
                    tmp.setLocatorId(dto.getOrganizationId());
                    tmp.setOrganizationType(dto.getParentOrganizationType());
                    tmp.setOrganizationId(dto.getParentOrganizationId());
                    tmp.setTenantId(tenantId);
                    mtModLocatorOrgRelRepository.insertSelective(tmp);
                }
            }
        } else {
            dto.setTenantId(tenantId);
            mtModOrganizationRelRepository.insertSelective(dto);
        }
    }

    @Override
    public Page<MtModLocator> organizationLimitLocatorForLovQuery(Long tenantId, MtModOrganizationRelDTO6 dto,
                                                                  PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            return new Page<MtModLocator>();
        }

        List<MtModLocatorOrgRelVO> locatorOrgRelList = mtModLocatorOrgRelRepository
                .organizationLimitLocatorQuery(tenantId, "SITE", dto.getSiteId(), "ALL");

        MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
        queryVO.setTopSiteId(dto.getSiteId());
        queryVO.setParentOrganizationType("SITE");
        queryVO.setParentOrganizationId(dto.getSiteId());
        queryVO.setOrganizationType("AREA");
        queryVO.setQueryType("ALL");
        List<MtModOrganizationItemVO> areaList =
                mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);
        if (!CollectionUtils.isEmpty(areaList)) {
            for (MtModOrganizationItemVO ever : areaList) {
                locatorOrgRelList.addAll(mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId, "AREA",
                        ever.getOrganizationId(), "ALL"));
            }
        }
        queryVO.setOrganizationType("PROD_LINE");
        List<MtModOrganizationItemVO> prodlineList =
                mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);
        if (!CollectionUtils.isEmpty(prodlineList)) {
            for (MtModOrganizationItemVO ever : prodlineList) {
                locatorOrgRelList.addAll(mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId,
                        "PROD_LINE", ever.getOrganizationId(), "ALL"));
            }
        }
        queryVO.setOrganizationType("WORKCELL");
        List<MtModOrganizationItemVO> workcellList =
                mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);
        if (!CollectionUtils.isEmpty(workcellList)) {
            for (MtModOrganizationItemVO ever : workcellList) {
                locatorOrgRelList.addAll(mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId,
                        "WORKCELL", ever.getOrganizationId(), "ALL"));
            }
        }

        List<String> locatorIds =
                locatorOrgRelList.stream().map(MtModLocatorOrgRelVO::getLocatorId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(locatorIds)) {
            return new Page<MtModLocator>();
        }

        MtModLocatorDTO6 vo3 = new MtModLocatorDTO6();
        if (StringUtils.isNotEmpty(dto.getLocatorCode()) || StringUtils.isNotEmpty(dto.getLocatorName())) {
            vo3.setLocatorCode(dto.getLocatorCode());
            vo3.setLocatorName(dto.getLocatorName());
        }
        vo3.setLocatorIdList(locatorIds);
        return PageHelper.doPageAndSort(pageRequest,
                () -> mtModLocatorMapper.selectLovByConditionCustom(tenantId, vo3));
    }

    @Override
    public Page<MtModOrganizationDTO4> notExistOrganizationForUi(Long tenantId, MtModOrganizationDTO3 dto,
                                                                 PageRequest pageRequest) {
        // 必输条件
        if (StringUtils.isEmpty(dto.getOrganizationType()) || StringUtils.isEmpty(dto.getParentOrganizationId())
                || StringUtils.isEmpty(dto.getParentOrganizationType())) {
            return new Page<MtModOrganizationDTO4>();
        }

        Page<MtModOrganizationDTO4> result = null;
        if ("LOCATOR_GROUP".equals(dto.getOrganizationType()) && "LOCATOR".equals(dto.getParentOrganizationType())) {
            // 如果库位分配库位组,根据父库位找到所有库位，找出不包含这些库位的库位组
            result = PageHelper.doPage(pageRequest,
                    () -> mtModOrganizationRelMapper.notExistLocatorGroupByLocator(tenantId, dto));
        } else if ("LOCATOR_GROUP".equals(dto.getOrganizationType())) {
            // 如果组织分配库位组，找到该父组织相关的所有库位，找出不包含这些库位的库位组
            result = PageHelper.doPage(pageRequest,
                    () -> mtModOrganizationRelMapper.notExistLocatorGroupByOrganization(tenantId, dto));
        } else if ("LOCATOR".equals(dto.getOrganizationType()) && "LOCATOR".equals(dto.getParentOrganizationType())) {
            // 如果库位分配库位，找出不在该库位下的所有库位，包含在其他组织下的库位
            result = PageHelper.doPage(pageRequest,
                    () -> mtModOrganizationRelMapper.notExistLocatorByLocator(tenantId, dto));
        } else if ("LOCATOR".equals(dto.getOrganizationType())) {
            // 如果组织分配库位，找出不在该组织下层的所有库位，包含在其他组织下的库位
            result = PageHelper.doPage(pageRequest,
                    () -> mtModOrganizationRelMapper.notExistLocatorByOrganization(tenantId, dto));
        } else {
            // 如果组织分配组织，找出不在该组织下层的所有目标组织
            // 2020-08-05 edit by chaonan.hu 业务授意，为了避免一个工位在多个产线、车间下出现，导致功能报错
            result = PageHelper.doPage(pageRequest,
                    () -> mtModOrganizationRelMapper.notExistOrganizationByOrganization(tenantId, dto));
        }
        // 获取类型
        MtGenType allType = mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_ASSIGNABLE",
                dto.getOrganizationType());
        String type = allType != null ? allType.getDescription() : dto.getOrganizationType();
        for (MtModOrganizationDTO4 ever : result.getContent()) {
            ever.setOrganizationTypeDesc(type);
        }

        return result;
    }

    @Override
    public Page<MtModOrganizationDTO6> currentNodeOrderUi(Long tenantId, MtModOrganizationDTO5 dto,
                                                          PageRequest pageRequest) {
        if ("ENTERPRISE".equals(dto.getParentOrganizationType())) {
            // 上层是企业时不能根据顶层站点来进行筛选
            dto.setTopSiteId(null);
        }
        if ("LOCATOR".equals(dto.getParentOrganizationType()) && "LOCATOR".equals(dto.getOrganizationType())) {
            // 库位下库位不能进行顺序调整
            return new Page<MtModOrganizationDTO6>();
        }

        // 先获取所有的关系
        Page<MtModOrganizationDTO6> result = PageHelper.doPage(pageRequest,
                () -> mtModOrganizationRelMapper.currentNodeOrder(tenantId, dto));

        // 根据获取出的关系，获取所有的组织类型
        Map<String, List<String>> idMap = result.stream().collect(Collectors.groupingBy(
                MtModOrganizationDTO6::getOrganizationType,
                Collectors.mapping(MtModOrganizationDTO6::getOrganizationId, Collectors.toList())));

        // 根据组织类型和主键链表获取详细信息
        List<MtModSite> siteList = new ArrayList<MtModSite>();
        List<MtModArea> areaList = new ArrayList<MtModArea>();
        List<MtModProductionLine> prodLineList = new ArrayList<MtModProductionLine>();
        List<MtModWorkcell> workcellList = new ArrayList<MtModWorkcell>();
        List<MtModLocator> locatorList = new ArrayList<MtModLocator>();

        if (CollectionUtils.isNotEmpty(idMap.get("SITE"))) {
            siteList = mtModSiteMapper
                    .selectByCondition(Condition.builder(MtModSite.class)
                            .andWhere(Sqls.custom().andEqualTo(MtModSite.FIELD_TENANT_ID, tenantId)
                                    .andIn(MtModSite.FIELD_SITE_ID, idMap.get("SITE")))
                            .build());
        }
        if (CollectionUtils.isNotEmpty(idMap.get("AREA"))) {
            areaList = mtModAreaMapper
                    .selectByCondition(Condition.builder(MtModArea.class)
                            .andWhere(Sqls.custom().andEqualTo(MtModArea.FIELD_TENANT_ID, tenantId)
                                    .andIn(MtModArea.FIELD_AREA_ID, idMap.get("AREA")))
                            .build());
        }
        if (CollectionUtils.isNotEmpty(idMap.get("PROD_LINE"))) {
            prodLineList = mtModProductionLineMapper.selectByCondition(Condition.builder(MtModProductionLine.class)
                    .andWhere(Sqls.custom().andEqualTo(MtModProductionLine.FIELD_TENANT_ID, tenantId)
                            .andIn(MtModProductionLine.FIELD_PROD_LINE_ID, idMap.get("PROD_LINE")))
                    .build());
        }
        if (CollectionUtils.isNotEmpty(idMap.get("WORKCELL"))) {
            workcellList = mtModWorkcellMapper.selectByCondition(Condition.builder(MtModWorkcell.class)
                    .andWhere(Sqls.custom().andEqualTo(MtModWorkcell.FIELD_TENANT_ID, tenantId)
                            .andIn(MtModWorkcell.FIELD_WORKCELL_ID, idMap.get("WORKCELL")))
                    .build());
        }
        if (CollectionUtils.isNotEmpty(idMap.get("LOCATOR"))) {
            locatorList = mtModLocatorMapper
                    .selectByCondition(Condition.builder(MtModLocator.class)
                            .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                    .andIn(MtModLocator.FIELD_LOCATOR_ID, idMap.get("LOCATOR")))
                            .build());
        }

        // 将结果数据数据的编码和名称拼接回去
        for (MtModOrganizationDTO6 ever : result.getContent()) {
            ever.setUiId(ever.getRelType() + ever.getRelId());
            if ("SITE".equals(ever.getOrganizationType())) {
                Optional<MtModSite> optional = siteList.stream()
                        .filter(t -> t.getSiteId().equals(ever.getOrganizationId())).findFirst();
                if (optional.isPresent()) {
                    ever.setOrganizationCode(optional.get().getSiteCode());
                    ever.setOrganizationName(optional.get().getSiteName());
                }
            } else if ("AREA".equals(ever.getOrganizationType())) {
                Optional<MtModArea> optional = areaList.stream()
                        .filter(t -> t.getAreaId().equals(ever.getOrganizationId())).findFirst();
                if (optional.isPresent()) {
                    ever.setOrganizationCode(optional.get().getAreaCode());
                    ever.setOrganizationName(optional.get().getAreaName());
                }
            } else if ("PROD_LINE".equals(ever.getOrganizationType())) {
                Optional<MtModProductionLine> optional = prodLineList.stream()
                        .filter(t -> t.getProdLineId().equals(ever.getOrganizationId())).findFirst();
                if (optional.isPresent()) {
                    ever.setOrganizationCode(optional.get().getProdLineCode());
                    ever.setOrganizationName(optional.get().getProdLineName());
                }
            } else if ("WORKCELL".equals(ever.getOrganizationType())) {
                Optional<MtModWorkcell> optional = workcellList.stream()
                        .filter(t -> t.getWorkcellId().equals(ever.getOrganizationId())).findFirst();
                if (optional.isPresent()) {
                    ever.setOrganizationCode(optional.get().getWorkcellCode());
                    ever.setOrganizationName(optional.get().getWorkcellName());
                }
            } else if ("LOCATOR".equals(ever.getOrganizationType())) {
                Optional<MtModLocator> optional = locatorList.stream()
                        .filter(t -> t.getLocatorId().equals(ever.getOrganizationId())).findFirst();
                if (optional.isPresent()) {
                    ever.setOrganizationCode(optional.get().getLocatorCode());
                    ever.setOrganizationName(optional.get().getLocatorName());
                }
            }

        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String nodeOrderSaveUi(Long tenantId, MtModOrganizationDTO7 dto) {
        if ("ORG_ORG".equals(dto.getRelType())) {
            MtModOrganizationRel rel = new MtModOrganizationRel();
            rel.setTenantId(tenantId);
            rel.setOrganizationRelId(dto.getRelId());
            rel = mtModOrganizationRelMapper.selectOne(rel);
            rel.setSequence(dto.getSequence());
            mtModOrganizationRelRepository.updateByPrimaryKey(rel);
        } else if ("ORG_LOC".equals(dto.getRelType())) {
            MtModLocatorOrgRel rel = new MtModLocatorOrgRel();
            rel.setTenantId(tenantId);
            rel.setLocatorOrganizationRelId(dto.getRelId());
            rel = mtModLocatorOrgRelMapper.selectOne(rel);
            rel.setSequence(dto.getSequence());
            mtModLocatorOrgRelRepository.updateByPrimaryKey(rel);
        } else {
            // 不是这两种类型的数据抛出异常
            throw new RuntimeException();
        }

        return dto.getRelType() + dto.getRelId();
    }

    /**
     * UI根据顶层站点，获取该站点下传入组织类型的数据
     *
     * @author chuang.yang
     * @date 2019/12/31
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<tarzan.modeling.api.dto.MtModOrganizationRelDTO8>
     */
    @Override
    public Page<MtModOrganizationRelDTO8> topSiteLimitOrganizationLovForUi(Long tenantId, MtModOrganizationRelDTO9 dto,
                                                                           PageRequest pageRequest) {
        if (dto == null || StringUtils.isEmpty(dto.getOrganizationType())) {
            return null;
        }
        if("LOCATOR".equals(dto.getOrganizationType())){
            List<MtModOrganizationRelDTO8> resultList = new ArrayList<>();
            //找站点下的父层库位
            List<MtModOrganizationRelDTO8> parentLocatorList = mtModOrganizationRelMapper.topSiteLimitOrganizaitonQuery(tenantId, dto);
            for (MtModOrganizationRelDTO8 mtModOrganizationRelDTO8:parentLocatorList) {
                //找父层库位下的所有子库位
                List<MtModOrganizationRelDTO8> childList = new ArrayList<>();
                List<MtModOrganizationRelDTO8> forFlagList = null;
                childList.add(mtModOrganizationRelDTO8);
                resultList.addAll(childList);
                do{
                    forFlagList = new ArrayList<>();
                    for (MtModOrganizationRelDTO8 parentLocator :childList) {
                        String parentLocatorId = parentLocator.getOrganizationId();
                        childList = mtModOrganizationRelMapper.childLocatorQuery(tenantId, parentLocatorId);
                        forFlagList.addAll(childList);
                        resultList.addAll(childList);
                    }
                }while(CollectionUtils.isNotEmpty(forFlagList));
            }
            resultList = resultList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                    () -> new TreeSet<>(Comparator.comparing(o -> o.getOrganizationId()))),
                    ArrayList::new));
            List<MtModOrganizationRelDTO8> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
            return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
        }
        return PageHelper.doPage(pageRequest,
                () -> mtModOrganizationRelMapper.topSiteLimitOrganizaitonQuery(tenantId, dto));
    }

}
