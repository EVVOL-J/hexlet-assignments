package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::convertToDto).toList();
    }

    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable String id) {
        var post = postRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        return convertToDto(post);
    }

    private PostDTO convertToDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setBody(post.getBody());
        postDTO.setComments(convertCommentsDto(commentRepository.findByPostId(post.getId())));
        return postDTO;
    }

    private List<CommentDTO> convertCommentsDto(List<Comment> byPostId) {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        if (!byPostId.isEmpty()) {
            byPostId.forEach(comment -> {
                CommentDTO commentDTO=new CommentDTO();
                commentDTO.setId(comment.getId());
                commentDTO.setBody(comment.getBody());
                commentDTOS.add(commentDTO);
            });
        }
        return commentDTOS;
    }
}
// END
