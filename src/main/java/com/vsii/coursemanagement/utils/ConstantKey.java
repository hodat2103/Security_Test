
package com.vsii.coursemanagement.utils;
/**
 * class Constant chua cac gia tri khong thay doi
 */

import java.util.Arrays;
import java.util.List;

public class ConstantKey {
    public static final List<String> VALID_CONTENT_TYPES = Arrays.asList("video/mp4", "video/mpeg", "video/avi", "video/quicktime");
    public static final long MAX_FILE_SIZE_MB = 100 * 1024 * 1024; // 100MB max size
}
