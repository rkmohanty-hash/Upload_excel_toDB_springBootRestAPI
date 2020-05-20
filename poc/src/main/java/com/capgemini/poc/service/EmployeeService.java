package com.capgemini.poc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capgemini.poc.exception.EmployeeIdNotFoundException;
import com.capgemini.poc.model.Employee;
import com.capgemini.poc.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> uploadFile(MultipartFile file) throws IOException {

		List<Employee> employeeList = new ArrayList<>();

		Integer empId = null;
		Integer empName = null;
		Integer empAge = null;
		Integer empDept = null;
		Integer empAdd = null;
	

			// Get the workbook instance for XLSX file
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			// Get first/desired worksheet from the workbook
			XSSFSheet worksheet = workbook.getSheetAt(0);
			// get the row
			XSSFRow header = worksheet.getRow(0);
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {

				if ("Employee Id".equals(header.getCell(i).getStringCellValue())) {
					empId = i;
				} else if ("Employee Name".equals(header.getCell(i).getStringCellValue())) {
					empName = i;
				} else if ("Employee Age".equals(header.getCell(i).getStringCellValue())) {
					empAge = i;
				} else if ("Employee Department".equals(header.getCell(i).getStringCellValue())) {
					empDept = i;
				} else if ("Employee Address".equals(header.getCell(i).getStringCellValue())) {
					empAdd = i;
				}

			}
			

			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				Employee employee = new Employee();
				XSSFRow row = worksheet.getRow(i);
				XSSFCell idCell = row.getCell(empId);
				XSSFCell ageCell = row.getCell(empAge);
				Object idCellValue = getCellValue(idCell);
				Object ageCellValue = getCellValue(ageCell);
				
				if (idCellValue != null && idCellValue.getClass().getName().equals("java.lang.Double")
						&& ageCellValue.getClass().getName().equals("java.lang.Double")) {
					employee.setEmployeeId(new Double(row.getCell(empId).getNumericCellValue()).longValue());
					employee.setEmployeeName(row.getCell(empName).getStringCellValue());
					employee.setEmployeeAge(new Double(row.getCell(empAge).getNumericCellValue()).intValue());
					employee.setEmployeeDepartment(row.getCell(empDept).getStringCellValue());
					employee.setEmployeeAddress(row.getCell(empAdd).getStringCellValue());
					employeeRepository.save(employee);
					employeeList.add(employee);
				}
				
			}

			 return employeeList;


	}

	private Object getCellValue(XSSFCell cell) {
		if (cell != null) {
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_BLANK:
				return null;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case XSSFCell.CELL_TYPE_NUMERIC:
				return cell.getNumericCellValue();
			case XSSFCell.CELL_TYPE_STRING:
				return cell.getRichStringCellValue().toString();
			}
		}
		return null;
	}

	public List<Employee> getAllEmployee() {
		return (List<Employee>) employeeRepository.findAll();
	}

	public void updateEmployee(Employee employee) {
		Optional<Employee> empl = employeeRepository.findById(employee.getEmployeeId());
		if (!empl.isPresent()) {
			throw new EmployeeIdNotFoundException("Employee id is not present : " + employee.getEmployeeId());

		}

		Employee emp = empl.get();
		emp.setEmployeeAddress(employee.getEmployeeAddress());
		emp.setEmployeeAge(employee.getEmployeeAge());
		emp.setEmployeeDepartment(employee.getEmployeeDepartment());
		emp.setEmployeeName(employee.getEmployeeName());
		employeeRepository.save(emp);

	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

}
