package exercise.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

import lombok.*;

// BEGIN
@Entity
@Table(name="products")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"title", "price"})
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private Integer price;
}
// END
