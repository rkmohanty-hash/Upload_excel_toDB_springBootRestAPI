package com.capgemini.poc.repository;

import org.springframework.data.repository.CrudRepository;

import com.capgemini.poc.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	

}
