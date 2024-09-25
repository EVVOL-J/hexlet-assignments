package exercise.controller.users;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api/users")
public class PostsController {
    private List<Post> posts = Data.getPosts();

    @GetMapping("/{id}/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> readPostsByUserId(@PathVariable String id) {
        return posts.stream()
                .filter(p -> String.valueOf(p.getUserId()).equals(id))
                .toList();
    }

    @PostMapping("/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createNewPost(@PathVariable String id, @RequestBody Post data) {
        data.setUserId(Integer.parseInt(id));
        posts.add(data);
        return data;
    }
}
// END
