package org.example;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Création de quelques étudiants
        Studentt student1 = findOrCreateStudent(entityManager, "AAAA");
        Studentt student2 = findOrCreateStudent(entityManager, "BBBB");

        // Création de quelques cours
        Course course1 = findOrCreateCourse(entityManager, "AR");
        Course course2 = findOrCreateCourse(entityManager, "FR");
        Course course3 = findOrCreateCourse(entityManager, "EN");

        // Associer les étudiants aux cours
        if (!student1.getCourses().contains(course1)) {
            student1.getCourses().add(course1);
        }
        if (!student1.getCourses().contains(course2)) {
            student1.getCourses().add(course2);
        }
        if (!student2.getCourses().contains(course2)) {
            student2.getCourses().add(course2);
        }
        if (!student2.getCourses().contains(course3)) {
            student2.getCourses().add(course3);
        }

        // Persister les étudiants et les cours
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(student1);
        entityManager.persist(student2);

        transaction.commit();

        // Récupérer les étudiants avec les cours auxquels ils sont inscrits en utilisant une jointure
        String jpql = "SELECT DISTINCT s FROM Studentt s JOIN FETCH s.courses";
        TypedQuery<Studentt> query = entityManager.createQuery(jpql, Studentt.class);
        List<Studentt> students = query.getResultList();

        // Utiliser un Set pour stocker temporairement les étudiants uniques
        Set<Studentt> uniqueStudents = new HashSet<>(students);

        // Afficher les étudiants avec les cours auxquels ils sont inscrits
        for (Studentt student : uniqueStudents) {
            System.out.println("Student: " + student.getName());
            System.out.println("Courses:");
            for (Course course : student.getCourses()) {
                System.out.println("    " + course.getName());
            }
            System.out.println();
        }

        entityManager.close();
        entityManagerFactory.close();
    }

    private static Studentt findOrCreateStudent(EntityManager entityManager, String name) {
        TypedQuery<Studentt> query = entityManager.createQuery("SELECT s FROM Studentt s WHERE s.name = :name", Studentt.class);
        query.setParameter("name", name);
        List<Studentt> students = query.getResultList();
        if (!students.isEmpty()) {
            return students.get(0);
        } else {
            Studentt student = new Studentt();
            student.setName(name);
            entityManager.persist(student);
            return student;
        }
    }

    private static Course findOrCreateCourse(EntityManager entityManager, String name) {
        TypedQuery<Course> query = entityManager.createQuery("SELECT c FROM Course c WHERE c.name = :name", Course.class);
        query.setParameter("name", name);
        List<Course> courses = query.getResultList();
        if (!courses.isEmpty()) {
            return courses.get(0);
        } else {
            Course course = new Course();
            course.setName(name);
            entityManager.persist(course);
            return course;
        }
    }
}
