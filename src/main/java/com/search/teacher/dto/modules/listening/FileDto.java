package com.search.teacher.dto.modules.listening;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileDto {
    private MultipartFile file;
    private String listeningPart;
    private Integer listeningId;
}
