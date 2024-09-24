package exercise;

import exercise.model.Post;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity getPostById(@PathVariable String id) {
        Optional<Post> postReturn = posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
        if (postReturn.isPresent()) {
            return ResponseEntity.ok()
                    .body(postReturn.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/posts")
    public ResponseEntity createPosts(@RequestBody Post newPost) {
        posts.add(newPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity updatePost(@PathVariable String id, @RequestBody Post postUpdate) {
        Optional<Post> postPresent = posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
        if (postPresent.isPresent()) {
            postPresent.get().setBody(postUpdate.getBody());
            postPresent.get().setTitle(postUpdate.getTitle());
            return ResponseEntity.status(HttpStatus.OK).body(postPresent.get());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
