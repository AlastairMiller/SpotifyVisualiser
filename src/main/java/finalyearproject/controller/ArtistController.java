package finalyearproject.controller;

import finalyearproject.model.Artist;
import finalyearproject.repository.ArtistRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @RequestMapping(value = "artists", method = RequestMethod.GET)
    public List<Artist> list() {
        return artistRepository.findAll();
    }

    @RequestMapping(value = "artists", method = RequestMethod.POST)
    public Artist create(@RequestBody Artist artist) {
        return artistRepository.saveAndFlush(artist);
    }

    @RequestMapping(value = "artists/{id}", method = RequestMethod.GET)
    public Artist get(@PathVariable String id) {
        return artistRepository.getOne(id);
    }

    @RequestMapping(value = "artists/{id}", method = RequestMethod.PUT)
    public Artist update(@PathVariable String id, @RequestBody Artist artist) {
        Artist existingArtist = artistRepository.findOne(id);
        BeanUtils.copyProperties(artist, existingArtist);
        return artistRepository.saveAndFlush(existingArtist);
    }

    @RequestMapping(value = "artists/{id}", method = RequestMethod.DELETE)
    public Artist delete(@PathVariable String id) {
        Artist existingArtist = artistRepository.findOne(id);
        artistRepository.delete(existingArtist);
        return existingArtist;
    }


}
