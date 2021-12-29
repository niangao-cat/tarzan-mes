package com.ruike.mdm.infra.repository.impl;

import com.ruike.mdm.api.dto.query.ModSiteQuery;
import com.ruike.mdm.api.dto.representation.ModSiteRept;
import com.ruike.mdm.domain.repository.ModSiteRepository;
import com.ruike.mdm.infra.mapper.ModSiteMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 站点资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/12 15:20
 */
@Repository
public class ModSiteRepositoryImpl implements ModSiteRepository {
    private final ModSiteMapper mapper;

    public ModSiteRepositoryImpl(ModSiteMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<ModSiteRept> list(ModSiteQuery query) {
        return mapper.listByCondition(query);
    }
}
