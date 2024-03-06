package app.portfoliojofregf.vercel.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;
    private Long quantity;
}
