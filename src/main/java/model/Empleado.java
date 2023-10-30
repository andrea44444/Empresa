package model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
//import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class Empleado {
	UUID id;
	String nombre;
	String salario;
	LocalDate fNacimiento;
	Departamento departamento;
	
	public Empleado(String nombre,String salario, LocalDate fNacimiento,Departamento departamento) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setSalario(salario);
		setFNacimiento (fNacimiento);
		setDepartamento(departamento);
	}
	
	public Empleado(UUID id,String nombre,String salario, LocalDate fNacimiento) {
		setId(id);
		setNombre(nombre);
		setSalario(salario);
		setFNacimiento (fNacimiento);
		setDepartamento(null);
	}
	
	public Empleado(String nombre,String salario, LocalDate fNacimiento) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setSalario(salario);
		setFNacimiento (fNacimiento);
		setDepartamento(null);
	}
	
}
