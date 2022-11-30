package com.main10.global.file;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFile {
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;
}
