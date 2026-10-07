package Event_pass.eventos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import Event_pass.eventos.model.Evento;
import Event_pass.eventos.repository.EventoRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    // Listar todos los eventos
    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    // Buscar evento por ID
    public Optional<Evento> buscarEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    // Crear evento
    public Evento crearEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    // Actualizar evento
    public Evento actualizarEvento(Long id, Evento eventoActualizado) {

        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        evento.setNombre(eventoActualizado.getNombre());
        evento.setFecha(eventoActualizado.getFecha());
        evento.setLugar(eventoActualizado.getLugar());
        evento.setCupoTotal(eventoActualizado.getCupoTotal());
        evento.setCupoDisponible(eventoActualizado.getCupoDisponible());

        return eventoRepository.save(evento);
    }

    // Eliminar evento
    public void eliminarEvento(Long id) {
        eventoRepository.deleteById(id);
    }

    // Reservar cupos
    public Evento reservarCupos(Long eventoId, Long cantidad) {

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        if (evento.getCupoDisponible() < cantidad) {
            throw new RuntimeException("No hay cupos suficientes");
        }

        evento.setCupoDisponible(
                evento.getCupoDisponible() - cantidad
        );

        return eventoRepository.save(evento);
    }
}