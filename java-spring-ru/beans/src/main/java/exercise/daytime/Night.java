package exercise.daytime;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class Night implements Daytime {
    private String name = "night";

    public String getName() {
        return name;
    }

    // BEGIN
    @PostConstruct
    public void printInitMessage(){
        System.out.println("Bean Night created");
    }

    @PreDestroy
    public void cleanup(){
        System.out.println("Bean Night destroy");
    }
    // END
}
