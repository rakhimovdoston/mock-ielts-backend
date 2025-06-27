package com.search.teacher.service.impl.module;

import com.search.teacher.dto.ImageDto;
import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.request.module.WritingRequest;
import com.search.teacher.dto.response.module.WritingResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.mapper.WritingMapper;
import com.search.teacher.model.entities.Image;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.Writing;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ImageRepository;
import com.search.teacher.repository.WritingRepository;
import com.search.teacher.service.FileService;
import com.search.teacher.service.module.WritingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class WritingServiceImpl implements WritingService {
    private final WritingRepository writingRepository;
    private final FileService fileService;
    private final ImageRepository imageRepository;

    @Override
    public JResponse saveWriting(User currentUser, WritingRequest request) {
        Writing writing = new Writing();
        writing.setActive(true);
        writing.setUser(currentUser);
        writing.setTask(request.task());
        writing.setImage(request.image());
        writing.setTitle(request.title());
        writingRepository.save(writing);
        return JResponse.success();
    }

    @Override
    public JResponse getAllWritings(User currentUser, ModuleFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Page<Writing> writings;

        if (filter.getType().equals("all")) {
            writings = writingRepository.findAllByUserAndActiveIsTrueAndDeletedIsFalse(currentUser, pageable);
        } else {
            writings = writingRepository.findAllByUserAndActiveIsTrueAndTaskAndDeletedIsFalse(currentUser, filter.getType().equals("true"), pageable);
        }

        if (writings.isEmpty()) {
            return JResponse.error(404, "No writings found.");
        }

        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(filter.getPage());
        response.setCurrentSize(filter.getSize());
        response.setTotalPages(writings.getTotalPages());
        response.setTotalSizes(writings.getTotalElements());
        response.setData(WritingMapper.INSTANCE.toList(writings.getContent()));
        return JResponse.success(response);
    }

    @Override
    public int countWritings(User currentUser) {
        return writingRepository.countAllByUser(currentUser);
    }

    @Override
    public JResponse deleteWriting(User currentUser, Long listeningId) {
        Writing writing = writingRepository.findByUserAndId(currentUser, listeningId).orElseThrow(() -> new NotfoundException("Writing not found"));
        writing.setDeleted(true);
        writingRepository.save(writing);
        return JResponse.success();
    }

    @Override
    public JResponse getWritingById(User currentUser, Long id) {
        Writing writing = getWritingByUserAndId(currentUser, id);

        WritingResponse response = WritingMapper.INSTANCE.toResponse(writing);
        return JResponse.success(response);
    }

    @Override
    public JResponse updateWriting(User currentUser, Long byId, WritingRequest request) {
        Writing writing = getWritingByUserAndId(currentUser, byId);
        writing.setTask(request.task());
        writing.setImage(request.image());
        writing.setTitle(request.title());
        writingRepository.save(writing);
        return JResponse.success();
    }

    @Override
    public JResponse deleteAudioByUrl(User currentUser, String url, Long writingId) {
        Writing writing = getWritingByUserAndId(currentUser, writingId);
        Image image = imageRepository.findByUrl(url);
        if (image != null) {
            if (image.getUserId().equals(currentUser.getId())) {
                fileService.deleteAudioFile(currentUser, image.getId());
                imageRepository.delete(image);
                writing.setImage(null);
                writingRepository.save(writing);
                return JResponse.success();
            } else {
                throw new NotfoundException("Image file not found");
            }
        }
        return JResponse.error(404, "Image file not found");
    }

    @Override
    public JResponse uploadPhoto(User currentUser, Long byId, MultipartFile file, String type) {
        JResponse response = fileService.uploadPhoto(currentUser, file, type);
        if (response.isSuccess()) {
            Writing writing = getWritingByUserAndId(currentUser, byId);
            ImageDto image = (ImageDto) response.getData();
            writing.setImage(image.getUrl());
            writingRepository.save(writing);
        }
        return response;
    }

    private Writing getWritingByUserAndId(User currentUser, Long id) {
        return writingRepository.findByUserAndId(currentUser, id)
                .orElseThrow(() -> new NotfoundException("Writing not found"));
    }
}
