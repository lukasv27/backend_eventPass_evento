
package Event_pass.eventos.controller;

import Event_pass.eventos.model.Evento;
import Event_pass.eventos.model.Reserva;
import Event_pass.eventos.services.EventoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interno/eventos")
public class ReservaController {

    private final EventoService eventoService;

    public ReservaController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping("/{eventoId}/reservas")
    public ResponseEntity<Reserva> reservarCupos(
            @PathVariable Long eventoId,
            @RequestBody Reserva reserva) {

        if (reserva.getOrdenId() == null ||
            reserva.getOrdenId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Evento evento = eventoService.reservarCupos(
            eventoId,
            reserva.getCantidad()
        );

        reserva.setEventoId(evento.getEventoId());
        reserva.setResultado("RESERVADA");

        return ResponseEntity.ok(reserva);
    }
}
