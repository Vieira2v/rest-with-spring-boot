package com.example.rest_with_spring_boot.data_vo_v1;

import java.io.Serializable;

public class UploadFileResponseVO implements Serializable {
    
    private static final long SERIAL_VERSION_ID = 1L;

    private String filename;
    private String fileDowloadUri;
    private String fileType;
    private long size;
    
    public UploadFileResponseVO(){}

    public UploadFileResponseVO(String filename, String fileDowloadUri, String fileType, long size) {
        this.filename = filename;
        this.fileDowloadUri = fileDowloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public static long getSerialVersionId() {
        return SERIAL_VERSION_ID;
    }

    public String getfilename() {
        return filename;
    }

    public void setfilename(String filename) {
        this.filename = filename;
    }

    public String getFileDowloadUri() {
        return fileDowloadUri;
    }

    public void setFileDowloadUri(String fileDowloadUri) {
        this.fileDowloadUri = fileDowloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    
}
