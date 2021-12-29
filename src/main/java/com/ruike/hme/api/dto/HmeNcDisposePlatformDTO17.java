package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeNcRecordAttachmentRel;
import lombok.Data;
import tarzan.actual.domain.entity.MtNcRecord;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-02 10:49:46
 **/
@Data
public class HmeNcDisposePlatformDTO17 implements Serializable {
    private static final long serialVersionUID = 6866450656501872127L;

    private List<MtNcRecord> mtNcRecordList;

    private boolean success;
}
