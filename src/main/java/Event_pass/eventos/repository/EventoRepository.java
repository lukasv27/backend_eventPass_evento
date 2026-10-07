package Event_pass.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Event_pass.eventos.model.Evento;

@Repository 
public interface EventoRepository extends JpaRepository<Evento, Long> {

}