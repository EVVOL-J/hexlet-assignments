package exercise.controller;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

    @Autowired
    private AuthorService authorService;

    // BEGIN
    @GetMapping(path = "")
    public List<AuthorDTO> index() {
        return authorService.index();
    }

    @GetMapping(path = "/{id}")
    public AuthorDTO show(@PathVariable long id) {
        return authorService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO create(@Valid @RequestBody AuthorCreateDTO authorData) {
        return authorService.create(authorData);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO update(@RequestBody @Valid AuthorUpdateDTO authorData, @PathVariable Long id) {
        return authorService.update(authorData, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        authorService.destroy(id);
    }
    // END
}
