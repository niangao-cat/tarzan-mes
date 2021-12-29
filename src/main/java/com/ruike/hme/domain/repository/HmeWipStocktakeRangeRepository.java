package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeWipStocktakeRange;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 在制盘点范围资源库
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
public interface HmeWipStocktakeRangeRepository extends BaseRepository<HmeWipStocktakeRange> {

    /**
     * 保存
     *
     * @param record 记录
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:15:37
     */
    int save(HmeWipStocktakeRange record);
}
