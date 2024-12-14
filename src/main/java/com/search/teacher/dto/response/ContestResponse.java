package com.search.teacher.dto.response;

import com.search.teacher.dto.modules.ListeningDto;
import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.dto.modules.writing.WritingDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ContestResponse {
    private Long id;
    private boolean active;
    private String name;
    private String description;
    private Date startDate;
    private List<ReadingPassageDto> readingPassages = new ArrayList<>();
    private List<ListeningDto> listeningModules = new ArrayList<>();
    private List<WritingDto> writings = new ArrayList<>();
}
