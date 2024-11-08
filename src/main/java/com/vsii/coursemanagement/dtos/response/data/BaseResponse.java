package com.vsii.coursemanagement.dtos.response.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * BaseResponse su dung de dung cho cac phan hoi bang base chung duoi day.
 * Gom 2 truong ngay gio khoi tao va cap nhat lan cuoi.
 * de ap dung cho tat ca cac truong ngay gio cho cac response khac khi can mo rong.
 */

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BaseResponse {

    /**
     * ngay va gio được tao lan dau va cap nhat lan cuoi.
     * thiet lap cho 2 truong 'created_at' va 'update_at' phan hoi theo dang JSON
     * ngay gio duoc  mac dinh theo 'yyyy-MM-dd HH:mm:ss'.
     */

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING) // covert to string
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;

}
