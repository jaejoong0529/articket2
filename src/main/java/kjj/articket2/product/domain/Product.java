package kjj.articket2.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor//기본생성자
@AllArgsConstructor//모든 필드 포함 생성자
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String productName;
    private Integer currentBid;
    private String description;
    private Integer price;
    private String image;
    private String category;



}
