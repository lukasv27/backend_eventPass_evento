package Event_pass.eventos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import Event_pass.eventos.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    // este método se utiliza para buscar un evento por su ID y bloquearlo para evitar conflictos de concurrencia al reservar cupos.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Evento e WHERE e.eventoId = :eventoId")
    Optional<Evento> buscarParaReserva(@Param("eventoId") Long eventoId);
}