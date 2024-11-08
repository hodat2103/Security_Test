package com.vsii.coursemanagement.dtos.response;


public class ResponseError extends ResponseData {

    /**
     * Du lieu tra ve khi thuc thi api that bai cho http POST, GET, PUT, DELETE
     * @param status
     * @param message
     */

    public ResponseError(int status, String message) {
        super(status, message);
    }
}
