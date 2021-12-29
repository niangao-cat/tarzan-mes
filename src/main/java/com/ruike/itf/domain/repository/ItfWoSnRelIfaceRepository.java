package com.ruike.itf.domain.repository;

import com.ruike.itf.domain.entity.ItfWoSnRelIface;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * Sn同步接口Repository
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/24 04:25:28
 */
public interface ItfWoSnRelIfaceRepository extends BaseRepository<ItfWoSnRelIface> {

    /**
     * 接口数据查询
     *
     * @param tenantId 租户
     * @return java.util.List<com.ruike.itf.domain.vo.ItfWoSnRelVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/24 04:27:09
     */
    List<ItfWoSnRelIface> selectInterfaceData(Long tenantId);

    /**
     *
     * @Description 回写接口表时查询原有数据
     *
     * @author yuchao.wang
     * @date 2020/7/30 17:19
     * @param woSnRelIface
     * @return com.ruike.itf.domain.entity.ItfWoSnRelIface
     *
     */
    ItfWoSnRelIface queryIfaceForReturnWrite(ItfWoSnRelIface woSnRelIface);
}
