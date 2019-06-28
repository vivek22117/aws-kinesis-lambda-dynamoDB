package com.ddsolutions.stream.domain;

/**
 * Created by Vivek Kumar Mishra on 20-03-2018.
 */
public class MyResponse {

    private String contentType;
    private String content;


    public MyResponse( String content, String contentType) {
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
