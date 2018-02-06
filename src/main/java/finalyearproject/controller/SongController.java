package finalyearproject.controller;

import finalyearproject.model.RefinedTrack;
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

    @RequestMapping(value = "songs", method = RequestMethod.GET)
    public List<RefinedTrack> list() {
        return songRepository.findAll();
    }

    @RequestMapping(value = "songs", method = RequestMethod.POST)
    public RefinedTrack create(@RequestBody RefinedTrack refinedTrack) {
        return songRepository.saveAndFlush(refinedTrack);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.GET)
    public RefinedTrack get(@PathVariable String id) {
        return songRepository.getOne(id);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.PUT)
    public RefinedTrack update(@PathVariable String id, @RequestBody RefinedTrack refinedTrack) {
        RefinedTrack existingRefinedTrack = songRepository.findOne(id);
        BeanUtils.copyProperties(refinedTrack, existingRefinedTrack);
        return songRepository.saveAndFlush(existingRefinedTrack);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.DELETE)
    public RefinedTrack delete(@PathVariable String id) {
        RefinedTrack existingRefinedTrack = songRepository.findOne(id);
        songRepository.delete(existingRefinedTrack);
        return existingRefinedTrack;
    }

}
