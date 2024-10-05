package com.example.SpringAppCrud.dao;

import com.example.SpringAppCrud.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findAll(); // Actualizăm interogarea pentru a găsi toți angajații
}
