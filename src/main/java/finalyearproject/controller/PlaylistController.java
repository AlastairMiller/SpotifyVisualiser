package finalyearproject.controller;

import finalyearproject.model.Playlist;
import finalyearproject.repository.PlaylistRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @RequestMapping(value = "playlists", method = RequestMethod.GET)
    public List<Playlist> list() {
        return playlistRepository.findAll();
    }

    @RequestMapping(value = "playlists", method = RequestMethod.POST)
    public Playlist create(@RequestBody Playlist playlist) {
        return playlistRepository.saveAndFlush(playlist);
    }

    @RequestMapping(value = "playlists/{id}", method = RequestMethod.GET)
    public Playlist get(@PathVariable String id) {
        return playlistRepository.getOne(id);
    }

    @RequestMapping(value = "playlists/{id}", method = RequestMethod.PUT)
    public Playlist update(@PathVariable String id, @RequestBody Playlist playlist) {
        Playlist existingPlaylist = playlistRepository.findOne(id);
        BeanUtils.copyProperties(playlist, existingPlaylist);
        return playlistRepository.saveAndFlush(existingPlaylist);
    }

    @RequestMapping(value = "playlists/{id}", method = RequestMethod.DELETE)
    public Playlist delete(@PathVariable String id) {
        Playlist existingPlaylist = playlistRepository.findOne(id);
        playlistRepository.delete(existingPlaylist);
        return existingPlaylist;
    }

}
