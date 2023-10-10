package org.example.Search;

import org.example.Video.Thumbnail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {

   List<Thumbnail> findByTitleContainingIgnoreCase(String searchText);
}
