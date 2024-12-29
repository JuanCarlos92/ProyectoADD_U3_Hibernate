package org.example.repository;

import org.example.models.Departamento;
import org.example.models.Empleado;
import org.example.models.Empresa;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

//Repositorio que gestiona operaciones CRUD y consultas para la entidad {@link Departamento}.
public class DepartamentoRepository {
    private final SessionFactory FACTORY;

    //Constructor que inicializa la la configuración de Hibernate.
    public DepartamentoRepository() {
        FACTORY = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Empresa.class)
                .addAnnotatedClass(Departamento.class)
                .addAnnotatedClass(Empleado.class)
                .buildSessionFactory();
    }

    //Crea un Departamento
    public void createDepartamento(Departamento departamento) {
        try (Session session = FACTORY.getCurrentSession()) {
            session.beginTransaction();
            session.saveOrUpdate(departamento);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Obtiene un departamento por ID
    public Departamento readDepartamentoPorId(int id) {
        Departamento departamento = null;
        try (Session session = FACTORY.getCurrentSession()) {
            session.beginTransaction();
            departamento = session.get(Departamento.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return departamento;
    }

    //Obtiene todos los departamentos
    public List<Departamento> readTodosLosDepartamentos() {
        List<Departamento> departamentos = null;
        try (Session session = FACTORY.getCurrentSession()) {
            session.beginTransaction();
            departamentos = session.createQuery("FROM Departamento", Departamento.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return departamentos;
    }

    //Obtiene departamentos de una empresa.
    public List<Departamento> readDepartamentosPorEmpresa(int empresaId) {
        List<Departamento> departamentos = null;
        try (Session session = FACTORY.getCurrentSession()) {
            session.beginTransaction();
            departamentos = session.createQuery(
                            "FROM Departamento d WHERE d.empresa.id = :empId", Departamento.class)
                    .setParameter("empId", empresaId)
                    .getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return departamentos;
    }

    //Actualiza un departamento por ID
    public void updateDepartamentoPorId(int id, Departamento departamento) {
        try (Session session = FACTORY.getCurrentSession()) {
            session.beginTransaction();
            departamento.setId(id);
            session.update(departamento);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Elimina un departamento por ID
    public void deleteDepartamentoPorId(int id) {
        try (Session session = FACTORY.getCurrentSession()) {
            session.beginTransaction();
            Departamento departamento = session.get(Departamento.class, id);
            if (departamento != null) {
                session.delete(departamento);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Muestra todos los empleados asignados a un departamento.
    public void mostrarEmpleadosDeDepartamento(int departamentoId) {
        try (Session session = FACTORY.getCurrentSession()) {
            session.beginTransaction();

            // Obtenemos el departamento por ID
            Departamento departamento = session.get(Departamento.class, departamentoId);
            if (departamento == null) {
                System.out.println("No se encontró el departamento con ID: " + departamentoId);
                return;
            }

            // Obtenemos los empleados del departamento
            List<Empleado> empleados = departamento.getEmpleados();
            if (empleados == null || empleados.isEmpty()) {
                System.out.println("El departamento '" + departamento.getNombre()
                        + "' no tiene empleados registrados.");
            } else {
                System.out.println("Empleados en el departamento: " + departamento.getNombre());
                for (Empleado emp : empleados) {
                    System.out.println("- " + emp.getNombre() + " " + emp.getApellido()
                            + " [Puesto: " + emp.getPuesto() + "]");
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error al mostrar empleados del departamento: " + e.getMessage());
        }
    }

    public void cerrar() {
        FACTORY.close();
    }
}
