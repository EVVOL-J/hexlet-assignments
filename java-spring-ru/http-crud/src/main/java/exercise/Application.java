package exercise;

import exercise.model.Post;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    public List<Post> getPosts(@RequestParam(defaultValue = "10") Integer limit, @RequestParam(defaultValue ="1" ) Integer page) {
        return posts.stream().skip((long) (limit-1) * page).limit(limit).toList();
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> getPostById(@PathVariable Long id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    @PostMapping("/posts")
    public Post createPosts(@RequestBody Post newPost) {
        posts.add(newPost);
        return newPost;
    }

    @PutMapping("/posts/{id}")
    public void updatePost(@PathVariable Long id, @RequestBody Post postUpdate) {
        posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .ifPresent(p ->
                {
                    p.setBody(postUpdate.getBody());
                    p.setTitle(postUpdate.getTitle());
                });
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
    // END
}
