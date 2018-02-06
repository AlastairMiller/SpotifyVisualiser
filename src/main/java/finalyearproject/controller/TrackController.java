package finalyearproject.controller;

import finalyearproject.model.RefinedTrack;
import finalyearproject.repository.TrackRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class TrackController {

    @Autowired
    private TrackRepository trackRepository;

    @RequestMapping(value = "songs", method = RequestMethod.GET)
    public List<RefinedTrack> list() {
        return trackRepository.findAll();
    }

    @RequestMapping(value = "songs", method = RequestMethod.POST)
    public RefinedTrack create(@RequestBody RefinedTrack refinedTrack) {
        return trackRepository.saveAndFlush(refinedTrack);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.GET)
    public RefinedTrack get(@PathVariable String id) {
        return trackRepository.getOne(id);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.PUT)
    public RefinedTrack update(@PathVariable String id, @RequestBody RefinedTrack refinedTrack) {
        RefinedTrack existingRefinedTrack = trackRepository.findOne(id);
        BeanUtils.copyProperties(refinedTrack, existingRefinedTrack);
        return trackRepository.saveAndFlush(existingRefinedTrack);
    }

    @RequestMapping(value = "songs/{id}", method = RequestMethod.DELETE)
    public RefinedTrack delete(@PathVariable String id) {
        RefinedTrack existingRefinedTrack = trackRepository.findOne(id);
        trackRepository.delete(existingRefinedTrack);
        return existingRefinedTrack;
    }

}
