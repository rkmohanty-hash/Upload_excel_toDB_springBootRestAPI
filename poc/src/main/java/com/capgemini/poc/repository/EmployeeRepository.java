package com.capgemini.poc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.poc.model.Employee;
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	

}
