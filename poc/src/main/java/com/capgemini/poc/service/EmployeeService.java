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
	
	public static final String NAME_REGEX = "^[a-zA-Z]{0,10}+$";
	public static final String ADDRESS_REGEX = "^[a-zA-Z0-9]{0,10}$";
	public static final String ID_REGEX = "^[0-9]{1,5}$";
	public static final String AGE_REGEX = "^[0-9]{1,3}$";

	public List<Employee> uploadFile(MultipartFile file) throws IOException {
		

		List<Employee> employeeList = new ArrayList<>();

		Integer empId = null;
		Integer empName = null;
		Integer empAge = null;
		Integer empDept = null;
		Integer empAdd = null;

		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
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

		
			
			if (idCellValue != null && idCellValue.getClass().getName().equals("java.lang.Double") && Long.toString(new Double(row.getCell(empId).getNumericCellValue()).longValue()).matches(ID_REGEX) ) {
				
					 employee.setEmployeeId(new Double(row.getCell(empId).getNumericCellValue()).longValue());

					
					if (ageCellValue!=null && ageCellValue.getClass().getName().equals("java.lang.Double") && Long.toString(new Double(row.getCell(empAge).getNumericCellValue()).longValue()).matches(AGE_REGEX) ) {
					employee.setEmployeeAge(new Double(row.getCell(empAge).getNumericCellValue()).intValue());
				} else {
					if (row.getCell(empAge).getCellType()==XSSFCell.CELL_TYPE_NUMERIC) {
						throw new EmployeeIdNotFoundException(
								"EmployeeAge only allowed Number and not more than Three digit but you are providing : " +new Double(row.getCell(empAge).getNumericCellValue()).longValue());
					} else {
						throw new EmployeeIdNotFoundException(
								"EmployeeAge only allowed Number and not more than Three digit but you are providing : " +row.getCell(empAge).getStringCellValue());
					}
				}
				
				objectContruct(row, employee, empName, empDept, empAdd);

				employeeRepository.save(employee);
				employeeList.add(employee);
			} else {
				if (row.getCell(empId).getCellType()==XSSFCell.CELL_TYPE_NUMERIC) {
					throw new EmployeeIdNotFoundException(
							"EmployeeId only allowed Number and not more than Five digit but you are providing : " +new Double(row.getCell(empId).getNumericCellValue()).longValue());
				} else {
					throw new EmployeeIdNotFoundException(
							"EmployeeId only allowed Number and not more than Five digit but you are providing : " +row.getCell(empId).getStringCellValue());
				}
			}

		}

		return employeeList;

	}
	
	private void objectContruct(XSSFRow row, Employee employee, Integer empName, Integer empDept, Integer empAdd) {
		
		if (row.getCell(empName).getStringCellValue().matches(NAME_REGEX)) {
			employee.setEmployeeName(row.getCell(empName).getStringCellValue());
		} else {
			throw new EmployeeIdNotFoundException(
					"EmployeeName only allowed character and not more than Ten character  : ");

		}


		if (row.getCell(empDept).getStringCellValue().matches(NAME_REGEX)) {

			employee.setEmployeeDepartment(row.getCell(empDept).getStringCellValue());

		} else {
			throw new EmployeeIdNotFoundException(
					"Department only allowed character and not more than Ten character : ");

		}
		if (row.getCell(empAdd).getStringCellValue().matches(ADDRESS_REGEX)) {

			employee.setEmployeeAddress(row.getCell(empAdd).getStringCellValue());

		} else {
			throw new EmployeeIdNotFoundException(
					"Employee Address  allowed only  not more than Ten character : ");

		}
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

	public void updateEmployee(Employee employee) throws Exception {
		try {
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
		} catch (Exception e) {
			 throw new Exception(e.getMessage());
		}
		

	}

	public void deleteEmployee(Long id) throws Exception {
		try {
			employeeRepository.deleteById(id);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

}
