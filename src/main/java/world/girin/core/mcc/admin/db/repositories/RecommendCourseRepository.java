package world.girin.core.mcc.admin.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import world.girin.core.mcc.admin.db.entities.RecommendCourseEntity;

@Repository
public interface RecommendCourseRepository extends JpaRepository<RecommendCourseEntity, Long> {

    @Query("SELECT MAX(rc.order) FROM RecommendCourseEntity rc WHERE rc.courseId = :courseId")
    Integer findMaxOrderByCourseId(@Param("courseId") Long courseId);


}
