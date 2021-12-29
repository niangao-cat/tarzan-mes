package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class HmeProductionPrintVO2 implements Serializable {
    private static final long serialVersionUID = 3950382888381993159L;

    List<Map<String, Object>> dataList;

    List<File> fileList;

    String pdfPath;
}
