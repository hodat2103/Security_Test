package com.vsii.coursemanagement.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

/**
 * ResponseData su dung de dung chung cho cac phan hoi.
 * Gom 3 truong status (trang thai response), message (thong diep), data(du lieu dang object).
 * de ap dung cho kieu phan hoi cho cac response khac khi can mo rong.
 */

@Getter
public class ResponseData<T> implements Serializable {
    private int status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * Du lieu tra ve cho Api khi lay du lieu thanh cong cho http GET, POST
     * @param status
     * @param message
     * @param data
     */
    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Du lieu tra ve khi thuc thi api thanh cong hay gap loi cho http PUT, DELETE
     * @param status
     * @param message
     */
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

