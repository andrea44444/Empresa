package model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
//import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor


public class Departamento {
	UUID id;
	String nombre;
	Empleado empleado;
	
	//Al crear un nuevo objeto empleado se enlaza con un departamento creado asi
	//El metodo findDepartamento devuelve con este constructor
	public Departamento(UUID id,String nombre) {
		setId(id);
		setNombre(nombre);
		setEmpleado(null);
	}
	
	//Al crear un nuevo departamento addDepartamento coge como parametro de entrada el objeto con este constructor
	public Departamento(String nombre, Empleado empleado) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setEmpleado(empleado);
	}
	
	//Se usa para crear un nuevo objeto departamento sin tener que especificar el jefe
	public Departamento(String nombre) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setEmpleado(null);
	}

}
