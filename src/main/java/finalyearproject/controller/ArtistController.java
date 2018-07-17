package finalyearproject.controller;

import finalyearproject.model.RefinedArtist;
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
    public List<RefinedArtist> list() {
        return artistRepository.findAll();
    }

    @RequestMapping(value = "artists", method = RequestMethod.POST)
    public RefinedArtist create(@RequestBody RefinedArtist RefinedArtist) {
        return artistRepository.saveAndFlush(RefinedArtist);
    }

    @RequestMapping(value = "artists/{id}", method = RequestMethod.GET)
    public RefinedArtist get(@PathVariable String id) {
        return artistRepository.getOne(id);
    }

    @RequestMapping(value = "artists/{id}", method = RequestMethod.PUT)
    public RefinedArtist update(@PathVariable String id, @RequestBody RefinedArtist RefinedArtist) {
        RefinedArtist existingRefinedArtist = artistRepository.findOne(id);
        BeanUtils.copyProperties(RefinedArtist, existingRefinedArtist);
        return artistRepository.saveAndFlush(existingRefinedArtist);
    }

    @RequestMapping(value = "artists/{id}", method = RequestMethod.DELETE)
    public RefinedArtist delete(@PathVariable String id) {
        RefinedArtist existingRefinedArtist = artistRepository.findOne(id);
        artistRepository.delete(existingRefinedArtist);
        return existingRefinedArtist;
    }


}
