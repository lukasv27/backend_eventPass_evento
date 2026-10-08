package Event_pass.eventos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import Event_pass.eventos.model.Evento;
import Event_pass.eventos.repository.EventoRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
    public Optional<Evento> buscarEventoPorId(Long eventoId) {
        return eventoRepository.findById(eventoId);
    }

    // Crear evento
    public Evento crearEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    // Actualizar evento
    public Evento actualizarEvento(Long eventoId, Evento eventoActualizado) {

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));

        evento.setNombre(eventoActualizado.getNombre());
        evento.setFecha(eventoActualizado.getFecha());
        evento.setLugar(eventoActualizado.getLugar());
        evento.setCupoTotal(eventoActualizado.getCupoTotal());
        evento.setCupoDisponible(eventoActualizado.getCupoDisponible());

        return eventoRepository.save(evento);
    }

    // Eliminar evento
    public void eliminarEvento(Long eventoId) {
        eventoRepository.deleteById(eventoId);
    }

    // Reservar cupos
    @Transactional 
    public Evento reservarCupos(Long eventoId, Long cantidad) {

         Evento evento = eventoRepository.buscarParaReserva(eventoId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Evento no encontrado"
        ));

    if (evento.getCupoDisponible() == null ||
        evento.getCupoDisponible() < cantidad) {
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "No hay cupos suficientes"
        );
    }

    evento.setCupoDisponible(
        evento.getCupoDisponible() - cantidad
    );

    return eventoRepository.save(evento);
}
}