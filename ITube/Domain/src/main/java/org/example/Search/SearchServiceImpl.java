package org.example.Search;

import org.example.Users.UserRepository;
import org.example.Users.UserService;
import org.example.Video.Thumbnail;
import org.example.Video.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Thumbnail> findByTitleContainingIgnoreCase(String searchText) {
         List<Thumbnail> searchResult = videoRepository.findByTitleContainingIgnoreCase(searchText);
         searchResult.parallelStream().forEach(thumbnail ->
             thumbnail.setUserAvatarBase64(userRepository.getUserAvatarById(thumbnail.getUserId())));
         return searchResult;
    }
}
