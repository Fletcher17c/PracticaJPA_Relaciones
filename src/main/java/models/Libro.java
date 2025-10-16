package models;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "anio_publicacion")
    private Integer anioPublicacion;

    // Relación ManyToOne con Autor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Autor autor;

    // Relación ManyToMany con Categoria
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "libro_categoria",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Categoria> categorias = new ArrayList<>();

    // Métodos helper para manejar la relación con categorías
    public void agregarCategoria(Categoria categoria) {
        categorias.add(categoria);
        categoria.getLibros().add(this);
    }

    public void removerCategoria(Categoria categoria) {
        categorias.remove(categoria);
        categoria.getLibros().remove(this);
    }
}