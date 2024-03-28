package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Création de quelques départements et employés
        Department department1 = new Department();
        department1.setName("IT Department");

        Department department2 = new Department();
        department2.setName("HR Department");

        Employee employee1 = new Employee();
        employee1.setName("IBTISSAM");
        employee1.setDepartment(department1); // Associer l'employé au département 1

        Employee employee2 = new Employee();
        employee2.setName("KOUTAR");
        employee2.setDepartment(department1); // Associer l'employé au département 1

        Employee employee3 = new Employee();
        employee3.setName("XXXXXXX");
        employee3.setDepartment(department2); // Associer l'employé au département 2

// Associer les employés aux départements
        List<Employee> employees1 = new ArrayList<>();
        employees1.add(employee1);
        employees1.add(employee2);

        List<Employee> employees2 = new ArrayList<>();
        employees2.add(employee3);

        department1.setEmployees(employees1);
        department2.setEmployees(employees2);

// Persister les départements et les employés
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(department1);
        entityManager.persist(department2);

        transaction.commit();

        // Récupérer les départements avec leurs employés
        TypedQuery<Department> query = entityManager.createQuery("SELECT d FROM Department d JOIN FETCH d.employees", Department.class);
        List<Department> departments = query.getResultList();

// Afficher les départements avec leurs employés
        for (Department department : departments) {
            System.out.println("Department: " + department.getName());
            System.out.println("Employees:");
            for (Employee employee : department.getEmployees()) {
                System.out.println("    " + employee.getName());
            }
            System.out.println(); // Ajouter une ligne vide entre chaque département
        }

        entityManager.close();
        entityManagerFactory.close();
    }
}
