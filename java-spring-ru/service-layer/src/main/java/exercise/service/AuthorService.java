package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.dto.BookDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;


    public List<AuthorDTO> index() {
        var author = authorRepository.findAll();
        return author.stream()
                .map(authorMapper::map)
                .toList();
    }

    public AuthorDTO show(long id) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        var authorDTO = authorMapper.map(author);
        return authorDTO;
    }


    public AuthorDTO create(AuthorCreateDTO authorData) {
        var author = authorMapper.map(authorData);
        authorRepository.save(author);
        var authorDto = authorMapper.map(author);
        return authorDto;
    }

    public AuthorDTO update(AuthorUpdateDTO authorData, Long id) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not Found: " + id));

        authorMapper.update(authorData, author);
        authorRepository.save(author);
        var authorDto = authorMapper.map(author);
        return authorDto;
    }

    public void destroy(@PathVariable Long id) {
        authorRepository.deleteById(id);
    }
    // END
}
