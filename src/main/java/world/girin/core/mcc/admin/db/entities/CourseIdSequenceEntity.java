package world.girin.core.mcc.admin.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mcc_course_id_sequence")
@Getter
@NoArgsConstructor
public class CourseIdSequenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}