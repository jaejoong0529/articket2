package kjj.articket2.admin.dto;
import lombok.*;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminTransactionResponseDto {
    private Long transactionId;
    private String productName;
    private String buyer;
    private String seller;
    private int price;
    private String date;
}
