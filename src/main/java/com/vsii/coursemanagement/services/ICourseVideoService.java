package com.vsii.coursemanagement.services;



public interface ICourseVideoService {
    /**
     * phuong thuc nay de lay ra video url theo courseId
     * @param courseId
     * @return tra ve url cua video khoa hoc theo courseId tuong ung
     */
    String getByCourseId(int courseId);
}
