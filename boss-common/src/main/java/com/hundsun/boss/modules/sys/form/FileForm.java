package com.hundsun.boss.modules.sys.form;

import org.springframework.web.multipart.MultipartFile;

public class FileForm {
    
    private String resourceName;
    
    private MultipartFile file;

    
    
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
