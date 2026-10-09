package Event_pass.eventos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import Event_pass.eventos.model.Evento;
import Event_pass.eventos.model.Reserva;
import Event_pass.eventos.repository.EventoRepository;
import Event_pass.eventos.repository.ReservaRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final ReservaRepository reservaRepository;

    public EventoService(EventoRepository eventoRepository, ReservaRepository reservaRepository) {
        this.eventoRepository = eventoRepository;
        this.reservaRepository = reservaRepository;
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
    public Reserva reservarCupos(Long eventoId, Reserva solicitud) {
        if (eventoId == null || eventoId <= 0
                || solicitud == null
                || solicitud.getOrdenId() == null || solicitud.getOrdenId() <= 0
                || solicitud.getCantidad() == null || solicitud.getCantidad() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "eventoId, ordenId y cantidad deben ser valores positivos"
            );
        }

        Reserva existente = reservaRepository.findById(solicitud.getOrdenId()).orElse(null);
        if (existente != null) {
            return validarReservaRepetida(existente, eventoId, solicitud.getCantidad());
        }

        Evento evento = eventoRepository.buscarParaReserva(eventoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Evento no encontrado"
                ));

        // Releer bajo bloqueo por si otra solicitud con el mismo ordenId terminó
        // mientras esta esperaba el bloqueo del evento.
        existente = reservaRepository.buscarParaActualizar(solicitud.getOrdenId()).orElse(null);
        if (existente != null) {
            return validarReservaRepetida(existente, eventoId, solicitud.getCantidad());
        }

        if (evento.getCupoDisponible() == null || evento.getCupoDisponible() < solicitud.getCantidad()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No hay cupos suficientes");
        }

        evento.setCupoDisponible(evento.getCupoDisponible() - solicitud.getCantidad());
        eventoRepository.save(evento);

        Reserva reserva = new Reserva();
        reserva.setOrdenId(solicitud.getOrdenId());
        reserva.setEventoId(eventoId);
        reserva.setCantidad(solicitud.getCantidad());
        reserva.setResultado("RESERVADA");
        return reservaRepository.save(reserva);
    }

    private Reserva validarReservaRepetida(Reserva existente, Long eventoId, Long cantidad) {
        if (!existente.getEventoId().equals(eventoId) || !existente.getCantidad().equals(cantidad)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El orden ya tiene una reserva con datos diferentes"
            );
        }
        return existente;
    }
}
