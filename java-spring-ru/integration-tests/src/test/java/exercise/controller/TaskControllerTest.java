package exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.model.Task;
import exercise.repository.TaskRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testShow() throws Exception {
        Task task = createRandomTask();
        taskRepository.save(task);
        var request = get("/tasks/" + task.getId());
        var response = mockMvc.perform(request).andExpect(status().isOk()).andReturn().getResponse();
        Task taskSystem = om.readValue(response.getContentAsString(), Task.class);
        assertTask(taskSystem, task);

    }

    @Test
    public void testCreate() throws Exception {
        Task task = createRandomTask();

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(task));

        var resp = mockMvc.perform(request).andExpect(status().isCreated()).andReturn().getResponse();
        Task respTask = om.readValue(resp.getContentAsString(), Task.class);

        Optional<Task> taskFromRepo = taskRepository.findById(respTask.getId());
        assertThat(taskFromRepo.isPresent()).isTrue();
        assertTask(taskFromRepo.get(), task);

    }

    @Test
    public void testUpdate() throws Exception {
        Task task = createRandomTask();
        task = taskRepository.save(task);
        Task updateTask = createRandomTask();
        updateTask.setId(task.getId());

        var request = put("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updateTask));

        mockMvc.perform(request).andExpect(status().isOk());

        task = taskRepository.findById(task.getId()).get();
        assertTask(task, updateTask);
    }

    @Test
    public void testDelete() throws Exception {
        Task task = createRandomTask();
        task = taskRepository.save(task);

        var request = delete("/tasks/" + task.getId());

        mockMvc.perform(request).andExpect(status().isOk());

        assertThat(taskRepository.findById(task.getId()).isPresent()).isFalse();

    }

    public Task createRandomTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph(1))
                .create();
    }

    public void assertTask(Task task1, Task task2) {
        assertThat(task1.getTitle()).isEqualTo(task2.getTitle());
        assertThat(task1.getDescription()).isEqualTo(task2.getDescription());
    }
    // END
}
