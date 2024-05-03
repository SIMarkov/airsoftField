package pu.project.app.repo;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import pu.project.app.models.Player;

public interface PlayerRepo extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    @Query("SELECT COUNT(p) FROM Player p WHERE p.team = :team")
    long countByTeam(@Param("team") String team);

    @Query("SELECT p FROM Player p WHERE LOWER(p.name) LIKE %:name% AND LOWER(p.team) LIKE %:team%")
    Page<Player> findByNameAndTeam(@Param("name") String name, @Param("team") String team, Pageable pageable);

}