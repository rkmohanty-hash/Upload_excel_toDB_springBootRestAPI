package com.capgemini.poc.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.capgemini.poc.model.Employee;
import com.capgemini.poc.service.EmployeeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/upload")                        
	public List<Employee> uploadData(@RequestParam("file") MultipartFile file) throws IOException {
		return employeeService.uploadFile(file);
	}

	@GetMapping("/all")
	public List<Employee> getEmployeeDetails() {
		return employeeService.getAllEmployee();
	}

	@PutMapping("/update")
	public void updateEmployee(@RequestBody Employee employee) throws Exception {
		employeeService.updateEmployee(employee);
	}

	@DeleteMapping("/delete")
	public void deleteEmployee(@RequestParam Long id) throws Exception {
		employeeService.deleteEmployee(id);
	}
}
