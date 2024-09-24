package exercise;

import exercise.annotation.Inspect;
import exercise.model.Address;

import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        var address = new Address("London", 12345678);

        // BEGIN
        Arrays.stream(address.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Inspect.class))
                .forEach(method -> {
                    System.out.printf("Method %s returns a value of type %s. %s", method.getName(), method.getReturnType().getSimpleName(), System.lineSeparator());
                });

        // END
    }
}
