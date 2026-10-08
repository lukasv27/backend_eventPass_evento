package Event_pass.eventos.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity 
@Table(name = "eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventoId;
    private String nombre;
    private String fecha;
    private String lugar;
    private Long cupoTotal;
    private Long cupoDisponible;

    // Constructor
    public Evento() {
    }

    public Evento(Long eventoId, String nombre, String fecha, String lugar) {
        this.eventoId = eventoId;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.cupoTotal = 0L;
        this.cupoDisponible = 0L;
    }

    // Getters and Setters
    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Long getCupoTotal() {
        return cupoTotal;
    }

    public void setCupoTotal(Long cupoTotal) {
        this.cupoTotal = cupoTotal;
    }

    public Long getCupoDisponible() {
        return cupoDisponible;
    }

    public void setCupoDisponible(Long cupoDisponible) {
        this.cupoDisponible = cupoDisponible;
    }

}
