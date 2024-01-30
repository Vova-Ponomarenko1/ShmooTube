package org.example.VideoControlers;

import org.example.Search.SearchService;
import org.example.Video.Thumbnail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;
import java.util.List;
@RestController
@RequestMapping("/ITube")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/search")
    public ModelAndView setTextSearch(@RequestParam("searchText") String searchText) {
        return new ModelAndView("redirect:/ITube/searchResults?searchText=" + searchText);
    }

    @GetMapping("/searchResults")
    public ModelAndView searchResults(@RequestParam("searchText") String searchText,
                                      Authentication authentication) {
        List<Thumbnail> searchResults =  searchService.findByTitleContainingIgnoreCase(searchText);
        ModelAndView modelAndView = new ModelAndView("SearchResults");
        searchResults.parallelStream().forEach(thumbnail -> {
            byte[] thumbnailBytes = thumbnail.getThumbnail();
            String dataUri = "data:image/png;base64," + Base64.getEncoder().encodeToString(thumbnailBytes);
            thumbnail.setDataUri(dataUri);
        });
        if (authentication != null && authentication.isAuthenticated()) {
            modelAndView.addObject("hideRegisterButton", true);
        } else {
            modelAndView.addObject("hideRegisterButton", false);
        }
        modelAndView.addObject("searchResults", searchResults);
        return modelAndView;
    }
}
