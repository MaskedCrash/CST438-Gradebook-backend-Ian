package com.cst438.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository extends CrudRepository <Assignment, Integer> {

	@Query("select a from Assignment a where a.needsGrading=1 and a.dueDate < current_date and a.course.instructor= :email order by a.id")
	List<Assignment> findNeedGradingByEmail(@Param("email") String email);
	
	//Adding this here because I am TIRED and sometimes the simplest solution is also the most effective- I imagine this would be a useful tool.
	@Query("select a from Assignment a where a.name = :name and a.course.course_id = :id")
	Assignment findByAssignmentNameAndCourseID(@Param("name") String name, @Param("id") int id);
}
