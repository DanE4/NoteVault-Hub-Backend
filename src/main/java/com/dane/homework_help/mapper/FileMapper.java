package com.dane.homework_help.mapper;

import com.dane.homework_help.dto.FileDTO;
import com.dane.homework_help.entity.File;
import org.springframework.stereotype.Service;

@Service
public class FileMapper {

    public final PostMapper postMapper;
    public final UserMapper userMapper;

    public FileMapper(PostMapper postMapper, UserMapper userMapper) {
        this.postMapper = postMapper;
        this.userMapper = userMapper;
    }

    public FileDTO apply(File file) {
        return new FileDTO(file.getId(), file.getFileExtension(), file.getPath(),
                userMapper.apply(file.getUploader()), postMapper.apply(file.getPost()),
                ChatMapper.apply(file.getChat()));
    }
}



