package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeNameplatePrintRelLineService;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelHeaderHis;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelLine;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelLineHis;
import com.ruike.hme.domain.repository.HmeNameplatePrintRelHeaderHisRepository;
import com.ruike.hme.domain.repository.HmeNameplatePrintRelLineHisRepository;
import com.ruike.hme.domain.repository.HmeNameplatePrintRelLineRepository;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelLineVO;
import com.ruike.hme.infra.mapper.HmeNameplatePrintRelLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang.StringUtils;
import org.hzero.core.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 铭牌打印内部识别码对应关系行表应用服务默认实现
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:13
 */
@Service
public class HmeNameplatePrintRelLineServiceImpl extends BaseAppService implements HmeNameplatePrintRelLineService {

    private final HmeNameplatePrintRelLineRepository hmeNameplatePrintRelLineRepository;
    private final HmeNameplatePrintRelLineMapper hmeNameplatePrintRelLineMapper;
    private final MtMaterialRepository mtMaterialRepository;
    private final HmeNameplatePrintRelHeaderHisRepository hmeNameplatePrintRelHeaderHisRepository;
    private final HmeNameplatePrintRelLineHisRepository hmeNameplatePrintRelLineHisRepository;

    @Autowired
    public HmeNameplatePrintRelLineServiceImpl(HmeNameplatePrintRelLineRepository hmeNameplatePrintRelLineRepository,
                                               HmeNameplatePrintRelLineMapper hmeNameplatePrintRelLineMapper, MtMaterialRepository mtMaterialRepository, HmeNameplatePrintRelHeaderHisRepository hmeNameplatePrintRelHeaderHisRepository, HmeNameplatePrintRelLineHisRepository hmeNameplatePrintRelLineHisRepository) {
        this.hmeNameplatePrintRelLineRepository = hmeNameplatePrintRelLineRepository;
        this.hmeNameplatePrintRelLineMapper = hmeNameplatePrintRelLineMapper;
        this.mtMaterialRepository = mtMaterialRepository;
        this.hmeNameplatePrintRelHeaderHisRepository = hmeNameplatePrintRelHeaderHisRepository;
        this.hmeNameplatePrintRelLineHisRepository = hmeNameplatePrintRelLineHisRepository;
    }

    @Override
    public Page<HmeNameplatePrintRelLineVO> queryPrintRelLine(Long tenantId, String nameplateHeaderId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeNameplatePrintRelLineMapper.queryPrintRelLine(tenantId, nameplateHeaderId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePrintRelLine(Long tenantId, HmeNameplatePrintRelLineVO hmeNameplatePrintRelLineVO) {
        HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis = new HmeNameplatePrintRelHeaderHis();
        hmeNameplatePrintRelHeaderHis.setNameplateHeaderId(hmeNameplatePrintRelLineVO.getNameplateHeaderId());
        List<HmeNameplatePrintRelHeaderHis> printRelHeaderHisList = hmeNameplatePrintRelHeaderHisRepository.select(hmeNameplatePrintRelHeaderHis);
        //取一条最新的记录获取 nameplateHeaderHisId
        HmeNameplatePrintRelHeaderHis printRelHeaderHis = printRelHeaderHisList.stream().max(Comparator.comparing(HmeNameplatePrintRelHeaderHis::getCreationDate)).get();
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setMaterialCode(hmeNameplatePrintRelLineVO.getMaterialCode());
        MtMaterial material = mtMaterialRepository.selectOne(mtMaterial);
        if (Objects.nonNull(material)) {
            if (StringUtils.isNotEmpty(hmeNameplatePrintRelLineVO.getNameplateLineId())) {
                //修改数据
                HmeNameplatePrintRelLine hmeNameplatePrintRelLine = new HmeNameplatePrintRelLine();
                hmeNameplatePrintRelLine.setNameplateLineId(hmeNameplatePrintRelLineVO.getNameplateLineId());
                hmeNameplatePrintRelLine.setMaterialId(material.getMaterialId());
                hmeNameplatePrintRelLine.setCode(hmeNameplatePrintRelLineVO.getCode());
                hmeNameplatePrintRelLine.setQty(hmeNameplatePrintRelLineVO.getQty());
                hmeNameplatePrintRelLine.setEnableFlag(hmeNameplatePrintRelLineVO.getEnableFlag());
                //更新行表
                hmeNameplatePrintRelLineMapper.updateByPrimaryKeySelective(hmeNameplatePrintRelLine);

                HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis = new HmeNameplatePrintRelLineHis();
                //历史表设置行表id
                hmeNameplatePrintRelLineHis.setNameplateLineId(hmeNameplatePrintRelLineVO.getNameplateLineId());
                //历史表设置头表id
                hmeNameplatePrintRelLineHis.setNameplateHeaderId(hmeNameplatePrintRelLineVO.getNameplateHeaderId());
                //设置头表历史id
                hmeNameplatePrintRelLineHis.setNameplateHeaderHisId(printRelHeaderHis.getNameplateHeaderHisId());
                hmeNameplatePrintRelLineHis.setMaterialId(material.getMaterialId());
                hmeNameplatePrintRelLineHis.setQty(hmeNameplatePrintRelLineVO.getQty());
                hmeNameplatePrintRelLineHis.setCode(hmeNameplatePrintRelLineVO.getCode());
                hmeNameplatePrintRelLineHis.setEnableFlag(hmeNameplatePrintRelLineVO.getEnableFlag());
                //历史行表新增数据
                hmeNameplatePrintRelLineHisRepository.insertSelective(hmeNameplatePrintRelLineHis);
            } else {
                //新增数据
                HmeNameplatePrintRelLine hmeNameplatePrintRelLine = new HmeNameplatePrintRelLine();
                hmeNameplatePrintRelLine.setMaterialId(material.getMaterialId());
                hmeNameplatePrintRelLine.setCode(hmeNameplatePrintRelLineVO.getCode());
                hmeNameplatePrintRelLine.setQty(hmeNameplatePrintRelLineVO.getQty());
                hmeNameplatePrintRelLine.setNameplateHeaderId(hmeNameplatePrintRelLineVO.getNameplateHeaderId());
                hmeNameplatePrintRelLine.setEnableFlag(hmeNameplatePrintRelLineVO.getEnableFlag());
                //行表新增数据
                hmeNameplatePrintRelLineRepository.insertSelective(hmeNameplatePrintRelLine);

                HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis = new HmeNameplatePrintRelLineHis();
                //历史表设置行表id
                hmeNameplatePrintRelLineHis.setNameplateLineId(hmeNameplatePrintRelLine.getNameplateLineId());
                //历史表设置头表id
                hmeNameplatePrintRelLineHis.setNameplateHeaderId(hmeNameplatePrintRelLineVO.getNameplateHeaderId());
                //设置头表历史id
                hmeNameplatePrintRelLineHis.setNameplateHeaderHisId(printRelHeaderHis.getNameplateHeaderHisId());
                hmeNameplatePrintRelLineHis.setMaterialId(material.getMaterialId());
                hmeNameplatePrintRelLineHis.setCode(hmeNameplatePrintRelLineVO.getCode());
                hmeNameplatePrintRelLineHis.setQty(hmeNameplatePrintRelLineVO.getQty());
                hmeNameplatePrintRelLineHis.setEnableFlag(hmeNameplatePrintRelLineVO.getEnableFlag());
                //历史行表新增数据
                hmeNameplatePrintRelLineHisRepository.insertSelective(hmeNameplatePrintRelLineHis);
            }
        }
    }
}
