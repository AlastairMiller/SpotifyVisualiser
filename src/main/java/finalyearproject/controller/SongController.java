package finalyearproject.controller;

import finalyearproject.model.Song;
import finalyearproject.repository.SongRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @RequestMapping(value ="songs", method = RequestMethod.GET)
    public List<Song> list(){
        return songRepository.findAll();
    }

    @RequestMapping(value = "songs", method = RequestMethod.POST)
    public Song create(@RequestBody Song song){
        return songRepository.saveAndFlush(song);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.GET)
    public Song get(@PathVariable String id) {
        return songRepository.getOne(id);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.PUT)
    public Song update(@PathVariable String id, @RequestBody Song song) {
        Song existingSong = songRepository.findOne(id);
        BeanUtils.copyProperties(song, existingSong);
        return songRepository.saveAndFlush(existingSong);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.DELETE)
    public Song delete(@PathVariable String id) {
        Song existingSong = songRepository.findOne(id);
        songRepository.delete(existingSong);
        return existingSong;
    }

}
