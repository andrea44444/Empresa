package view;
import java.util.List;
import java.util.UUID;

import dao.ManipularBaseDatos;
import io.IO;
import model.Empleado;
import model.Departamento;

import java.time.LocalDate;

public class Menu {
	//private static Departamento x = new Departamento();
	//private static Empleado y = new Empleado();
	
	public static void main(String[] args) {
		ManipularBaseDatos bd = new ManipularBaseDatos();

		List<String> opciones = List.of(
				"1. Insertar empleado\n"+
				"2. Insertar departamento\n"+
				"3. Modificar empleado\n"+
				"4. Modificar departamento\n"+
				"5. Eliminar empleado\n"+
				"6. Eliminar departamento\n"+
				"7. Buscar empleado\n"+
				"8. Buscar departamento\n"+
				"9. Mostrar empleados\n"+
				"10. Mostrar departamentos\n"+
				"11. Salir");
		while(true) {
			System.out.println(opciones);
			switch (IO.readInt()) {
				case 1:
					insertarEmpleado(bd);
					break;
				case 2:
					insertarDepartamento(bd);
					break;
				case 3:
					modificarEmpleado(bd);
					break;
				case 4:
					modificarDepartamento(bd);
					break;
				case 5:
					eliminarEmpleado(bd);
					break;
				case 6:
					eliminarDepartamento(bd);
					break;
				case 7:
					buscarEmpleado(bd);
					break;
				case 8:
					buscarDepartamento(bd);
					break;
				case 9:
					mostrarEmpleados(bd);
					break;
				case 10:
					mostrarDepartamentos(bd);
					break;
				case 11:
					bd.close();
					return;
					
				default:
			}
		}
	}
	
	private static void insertarEmpleado(ManipularBaseDatos bd) {
		IO.print("Nombre ? ");
		String nombre = IO.readString();
		IO.print("Salario ? ");
		String salario = IO.readString();
		IO.print("Fecha nacimiento ? (YYYY-MM-DD)");
		LocalDate fechaNacimiento = IO.readLocalDate();
		IO.print("Departamento ?");
		if (IO.readString() == "") {
			Empleado empleado = new Empleado(nombre, salario, fechaNacimiento);
			boolean anadido = bd.addEmpleado(empleado, null);
			IO.println(anadido ? "Añadido" : "No se ha podido añadir");
		} else {
			UUID uuidDepartamento = IO.readUUID();
			if (bd.findDepartamento(uuidDepartamento) == null) {
				IO.println("No existe en la base de datos");
			} else {
				Departamento departamento = bd.findDepartamento(uuidDepartamento);
				Empleado empleado = new Empleado(nombre, salario, fechaNacimiento, departamento);
				boolean anadido = bd.addEmpleado(empleado, uuidDepartamento);
				IO.println(anadido ? "Añadido" : "No se ha podido añadir");
			}
		}
	}
	
	private static void modificarDepartamento(ManipularBaseDatos bd) {
		IO.print("UUID ? ");
		UUID id= IO.readUUID();
		if(bd.findDepartamento(id)==null) {
			IO.println("No existe en la base de datos");
			return;
		}else{
			IO.print("Nombre ? ");
			String nombre = IO.readString();
			IO.print("Jefe ? ");
			UUID jefe= IO.readUUID();
			if(bd.findEmpleado(jefe)==null) {
				IO.println("No existe en la base de datos");
				return;
			}else {
				boolean modificado = bd.modifyDepartamento(new Departamento(id,nombre, bd.findEmpleado(jefe)));
				IO.println(modificado ? "Modificado" : "No se ha podido modificar");
			}	
		}
	}
	
	private static void modificarEmpleado(ManipularBaseDatos bd) {
		IO.print("UUID ? ");
		UUID id= IO.readUUID();
		if(bd.findEmpleado(id)==null) {
			IO.println("No existe en la base de datos");
			return;
		}else{
			IO.print("Nombre ? ");
			String nombre = IO.readString();
			IO.print("Salario ? ");
			String salario = IO.readString();
			IO.print("Fecha nacimiento ? (YYYY-MM-DD)");
			LocalDate fechaNacimiento= IO.readLocalDate();
			IO.print("Departamento ?");
			UUID departamento= IO.readUUID();
			if(bd.findDepartamento(departamento)==null) {
				IO.println("No existe en la base de datos");
				return;
			} else {
				boolean modificado = bd.modifyEmpleado(new Empleado(id,nombre, salario, fechaNacimiento, bd.findDepartamento(departamento)));
				IO.println(modificado ? "Modificado" : "No se ha podido modificar");
			}
		}
	}
	
	private static void insertarDepartamento(ManipularBaseDatos bd) {
		IO.print("Nombre ? ");
		String nombre = IO.readString();
		IO.print("Jefe ? ");
		
		if(IO.readString()=="") {
			Departamento departamento = new Departamento(nombre);
			boolean anadido = bd.addDepartamento(departamento,null);
			IO.println(anadido ? "Añadido" : "No se ha podido añadir");
		}else {
			UUID uuidEmpleado = IO.readUUID();
			Empleado empleado = bd.findEmpleado(uuidEmpleado);
			
			Departamento departamento = new Departamento(nombre, empleado);
			boolean anadido = bd.addDepartamento(departamento,uuidEmpleado);
			
			IO.println(anadido ? "Añadido" : "No se ha podido añadir");
		}
	}
	
	private static void eliminarEmpleado(ManipularBaseDatos bd) {
		IO.print("UUID ? ");
		UUID id= IO.readUUID();
		if(bd.findEmpleado(id)==null) {
			IO.println("No existe en la base de datos");
			return;
		}else{
			boolean eliminado =bd.deleteEmpleado(id.toString());
			IO.println(eliminado ? "Eliminado" : "No se ha podido eliminar");
		}
	}
	
	private static void eliminarDepartamento(ManipularBaseDatos bd) {
		IO.print("UUID ? ");
		UUID id= IO.readUUID();
		if(bd.findDepartamento(id)==null) {
			IO.println("No existe en la base de datos");
			return;
		}else{
			boolean eliminado =bd.deleteDepartamento(id.toString());
			IO.println(eliminado ? "Eliminado" : "No se ha podido eliminar");
		}
	}
	
	private static void mostrarEmpleados(ManipularBaseDatos bd) {
		if(bd.showEmpleados()=="") {
			System.out.println("No hay Empleados");
		}else {
			System.out.println(bd.showEmpleados());
		}
	}
	
	private static void mostrarDepartamentos(ManipularBaseDatos bd) {
		if(bd.showDepartamentos()=="") {
			System.out.println("No hay Departamentos");
		}else {
			System.out.println(bd.showDepartamentos());
		}
	}
	
	private static void buscarEmpleado(ManipularBaseDatos bd) {
		IO.print("UUID del empleado a buscar");
		String empleadoUUID = IO.readUUID().toString();
	
		Empleado empleado = bd.selectEmpleado(empleadoUUID);
		if (empleado != null) {
			IO.println(" UUID: " + empleado.getId());
			IO.println(" Nombre: " + empleado.getNombre());
			IO.println(" Salario: " + empleado.getSalario());
			IO.println(" Fecha de Nacimiento: " + empleado.getFNacimiento());
			if(empleado.getDepartamento()==null) {
				IO.println(" UUID del Departamento: null");
			}else {
				IO.println(" UUID del Departamento: " + empleado.getDepartamento().getId() + "\n");
			}
			
		} else {
			IO.println("No se ha encontrado el empleado con UUID " + empleadoUUID);
		}
	}

	private static void buscarDepartamento(ManipularBaseDatos bd) {
		IO.print("UUID del departamento a buscar");
		String departamentoUUID = IO.readUUID().toString();
		Departamento departamento = bd.selectDepartamento(departamentoUUID);

		if (departamento != null) {
			IO.println(" UUID: " + departamento.getId());
			IO.println(" Nombre: " + departamento.getNombre());
			IO.println(" UUID del Jefe: " + departamento.getEmpleado().getId() + "\n");
		} else {
			IO.println("No se ha encontrado el departamento con UUID " + departamentoUUID);
		}
	}
}
