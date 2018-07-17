package finalyearproject.controller;

import finalyearproject.model.RefinedPlaylist;
import finalyearproject.repository.PlaylistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @RequestMapping(value = "playlists", method = RequestMethod.GET)
    public List<RefinedPlaylist> list() {
        return playlistRepository.findAll();
    }

    @RequestMapping(value = "playlists", method = RequestMethod.POST)
    public RefinedPlaylist create(@RequestBody RefinedPlaylist refinedPlaylist) {
        return playlistRepository.saveAndFlush(refinedPlaylist);
    }

    @RequestMapping(value = "playlists/{id}", method = RequestMethod.GET)
    public RefinedPlaylist get(@PathVariable String id) {
        return playlistRepository.getOne(id);
    }

    @RequestMapping(value = "playlists/{id}", method = RequestMethod.PUT)
    public RefinedPlaylist update(@PathVariable String id, @RequestBody RefinedPlaylist refinedPlaylist) {
        RefinedPlaylist existingRefinedPlaylist = playlistRepository.findOne(id);
        BeanUtils.copyProperties(refinedPlaylist, existingRefinedPlaylist);
        return playlistRepository.saveAndFlush(existingRefinedPlaylist);
    }

    @RequestMapping(value = "playlists/{id}", method = RequestMethod.DELETE)
    public RefinedPlaylist delete(@PathVariable String id) {
        RefinedPlaylist existingRefinedPlaylist = playlistRepository.findOne(id);
        playlistRepository.delete(existingRefinedPlaylist);
        return existingRefinedPlaylist;
    }

    @RequestMapping(value = "playlists/request", method = RequestMethod.POST)
    public RefinedPlaylist create(@RequestBody String playlistId, @RequestBody String playlistOwner) {
        RefinedPlaylist refinedPlaylist = new RefinedPlaylist();
        refinedPlaylist.setId(playlistId);
        refinedPlaylist.setName(playlistOwner);
        log.info("Playlist Id: " + playlistId + " will be downloaded in the next refresh");
        return playlistRepository.saveAndFlush(refinedPlaylist);
    }

}
