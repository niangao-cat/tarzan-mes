package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfWcsTaskIfaceDTO1;
import com.ruike.itf.api.dto.ItfWcsTaskIfaceDTO2;
import com.ruike.itf.domain.entity.ItfWcsTaskIface;
import org.hzero.mybatis.base.BaseRepository;


/**
 * 出库任务状态回传接口  API
 *
 * @author taowen.wang@hand-china.com 2021/7/2 16:29
 */
public interface ItfWcsTaskIfaceRepository extends BaseRepository<ItfWcsTaskIface> {

    ItfWcsTaskIfaceDTO2 ItfWcsTaskIfaceUpdate(ItfWcsTaskIfaceDTO1 itfWcsTaskIfaceDTO1);
}
