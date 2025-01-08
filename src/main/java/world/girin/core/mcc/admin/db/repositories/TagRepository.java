package world.girin.core.mcc.admin.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.girin.core.mcc.admin.db.entities.TagEntity;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

}
