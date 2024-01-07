package com.dane.notevault.mapper;

import com.dane.notevault.dto.FileDTO;
import com.dane.notevault.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileMapper {
    private PostMapper postMapper;
    private UserMapper userMapper;

    @Autowired
    public void setPostMapper(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public FileDTO apply(File file) {
        return new FileDTO(file.getId(), file.getFileExtension(), file.getPath(),
                userMapper.apply(file.getUploader()), postMapper.apply(file.getPost()),
                ChatMapper.apply(file.getChat()));
    }
}



