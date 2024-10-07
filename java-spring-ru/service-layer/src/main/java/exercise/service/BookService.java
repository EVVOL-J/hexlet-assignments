package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    public List<BookDTO> index() {
        var categories = bookRepository.findAll();
        return categories.stream()
                .map(bookMapper::map)
                .toList();
    }
    public BookDTO show(Long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not Found: " + id));
        var bookDto = bookMapper.map(book);
        return bookDto;
    }

    public BookDTO create(BookCreateDTO bookData) {
        var book = bookMapper.map(bookData);
        bookRepository.save(book);
        var bookDto = bookMapper.map(book);
        return bookDto;
    }

    public BookDTO update(BookUpdateDTO bookData, Long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not Found: " + id));

        bookMapper.update(bookData, book);
        bookRepository.save(book);
        var bookDto = bookMapper.map(book);
        return bookDto;
    }

    public void destroy(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
    // END
}
