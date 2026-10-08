package Event_pass.eventos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Event_pass.eventos.model.Evento;
import Event_pass.eventos.services.EventoService;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    // GET - Listar todos los eventos
    @GetMapping
    public ResponseEntity<List<Evento>> listarEventos() {
        return ResponseEntity.ok(eventoService.listarEventos());
    }

    // GET - Buscar evento por ID
    @GetMapping("/{eventoId}")
    public ResponseEntity<Evento> buscarEventoPorId(@PathVariable Long eventoId) {

        return eventoService.buscarEventoPorId(eventoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Crear evento
    @PostMapping
    public ResponseEntity<Evento> crearEvento(@RequestBody Evento evento) {

        Evento nuevoEvento = eventoService.crearEvento(evento);

        return ResponseEntity.status(201).body(nuevoEvento);
    }

    // PUT - Actualizar evento
    @PutMapping("/{eventoId}")
    public ResponseEntity<Evento> actualizarEvento(
            @PathVariable Long eventoId,
            @RequestBody Evento evento) {

        Evento eventoActualizado =
                eventoService.actualizarEvento(eventoId, evento);

        return ResponseEntity.ok(eventoActualizado);
    }

    // DELETE - Eliminar evento
    @DeleteMapping("/{eventoId}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long eventoId) {

        eventoService.eliminarEvento(eventoId);

        return ResponseEntity.noContent().build();
    }
}