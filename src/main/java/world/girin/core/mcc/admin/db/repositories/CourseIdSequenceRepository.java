package world.girin.core.mcc.admin.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import world.girin.core.mcc.admin.db.entities.CourseIdSequenceEntity;

@Repository
public interface CourseIdSequenceRepository extends JpaRepository<CourseIdSequenceEntity, Long> {

    @Modifying
    @Query(value = "INSERT INTO course_id_sequence VALUES ()", nativeQuery = true)
    void generateNewSequence();

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastGeneratedSequence();

}
