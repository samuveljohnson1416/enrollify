import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Student {
    int id;
    String stdName;
    String email;
    String phNum;
    String crsName;
    String status;

    Student(int id, String stdName, String email,
            String phNum, String crsName) {
        this.id = id;
        this.stdName = stdName;
        this.email = email;
        this.phNum = phNum;
        this.crsName = crsName;
        this.status = "Active";
    }

    public String toString() {
        return id + " | " + stdName + " | " + email +
               " | " + phNum + " | " + crsName +
               " | " + status;
    }
}

class Course {
    int id;
    String crsName;
    int duration;
    String instructor;
    double fee;
    int seats;

    Course(int id, String crsName, int duration, String instructor, double fee, int seats) {
        this.id = id;
        this.crsName = crsName;
        this.duration = duration;
        this.instructor = instructor;
        this.fee = fee;
        this.seats = seats;
    }

    public String toString() {
        return id + " | " + crsName + " | " + duration + " weeks | " +
               instructor + " | $" + fee + " | Seats: " + seats;
    }
}

public class Main {
    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Course> courses = new ArrayList<>();
    static HashMap<String, Integer> seatMap = new HashMap<>();
    static int stdIdCount = 1;
    static int crsIdCount = 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("====================================");
        System.out.println("  ENROLLIFY - Course Registration  ");
        System.out.println("====================================");

        do {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Update Course");
            System.out.println("4. Delete Course");
            System.out.println("5. Enroll Student");
            System.out.println("6. View All Students");
            System.out.println("7. Update Student");
            System.out.println("8. Delete Student");
            System.out.println("9. Search Student by Name");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addCourse(sc);
                case 2 -> viewCourses();
                case 3 -> updateCourse(sc);
                case 4 -> deleteCourse(sc);
                case 5 -> enrollStudent(sc);
                case 6 -> viewStudents();
                case 7 -> updateStudent(sc);
                case 8 -> deleteStudent(sc);
                case 9 -> searchStudent(sc);
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 0);

        sc.close();
    }



    static void addCourse(Scanner sc) {
        System.out.print("Course Name: ");
        String name = sc.nextLine();
        if (name.isBlank()) { System.out.println("Name cant be empty!"); return; }

        System.out.print("Duration (weeks): ");
        int dur = sc.nextInt(); sc.nextLine();

        System.out.print("Instructor: ");
        String inst = sc.nextLine();

        System.out.print("Fee: ");
        double fee = sc.nextDouble(); sc.nextLine();

        System.out.print("Available Seats: ");
        int seats = sc.nextInt(); sc.nextLine();

        Course c = new Course(crsIdCount++, name, dur, inst, fee, seats);
        courses.add(c);
        seatMap.put(name, seats);
        System.out.println("Course added!");
    }

    static void viewCourses() {
        if (courses.isEmpty()) { System.out.println("No courses found!"); return; }
        System.out.println("\n--- COURSES ---");
        for (Course c : courses) System.out.println(c);
    }

    static void updateCourse(Scanner sc) {
        viewCourses();
        System.out.print("Enter Course ID to update: ");
        int id = sc.nextInt(); sc.nextLine();
        for (Course c : courses) {
            if (c.id == id) {
                System.out.print("New Name (" + c.crsName + "): ");
                String n = sc.nextLine();
                if (!n.isBlank()) c.crsName = n;
                System.out.print("New Seats (" + c.seats + "): ");
                c.seats = sc.nextInt(); sc.nextLine();
                seatMap.put(c.crsName, c.seats);
                System.out.println("Course updated!");
                return;
            }
        }
        System.out.println("Course not found!");
    }

    static void deleteCourse(Scanner sc) {
        viewCourses();
        System.out.print("Enter Course ID to delete: ");
        int id = sc.nextInt(); sc.nextLine();
        courses.removeIf(c -> c.id == id);
        System.out.println("Course deleted!");
    }

    static void enrollStudent(Scanner sc) {
        if (courses.isEmpty()) { System.out.println("No courses available!"); return; }
        viewCourses();

        System.out.print("Course Name to enroll: ");
        String crsName = sc.nextLine();

        if (!seatMap.containsKey(crsName)) {
            System.out.println("Course not found!"); return;
        }
        if (seatMap.get(crsName) <= 0) {
            System.out.println("Sorry! No seats available in " + crsName);
            return;
        }

        System.out.print("Student Name: ");
        String name = sc.nextLine();
        if (name.isBlank()) { System.out.println("Name cant be empty!"); return; }

        System.out.print("Email: ");
        String email = sc.nextLine();
        if (!email.contains("@")) { System.out.println("Invalid email!"); return; }

        for (Student s : students) {
            if (s.email.equals(email)) {
                System.out.println("Email already registered!"); return;
            }
        }

        System.out.print("Phone: ");
        String ph = sc.nextLine();

        Student std = new Student(stdIdCount++, name, email, ph, crsName);
        students.add(std);

        seatMap.put(crsName, seatMap.get(crsName) - 1);
        for (Course c : courses) {
            if (c.crsName.equals(crsName)) { c.seats--; break; }
        }

        System.out.println("Student enrolled! Seats left: " + seatMap.get(crsName));
    }

    static void viewStudents() {
        if (students.isEmpty()) { System.out.println("No students found!"); return; }
        System.out.println("\n--- STUDENTS ---");
        for (Student s : students) System.out.println(s);
    }

    static void updateStudent(Scanner sc) {
        viewStudents();
        System.out.print("Enter Student ID to update: ");
        int id = sc.nextInt(); sc.nextLine();
        for (Student s : students) {
            if (s.id == id) {
                System.out.print("New Name (" + s.stdName + "): ");
                String n = sc.nextLine();
                if (!n.isBlank()) s.stdName = n;
                System.out.print("New Phone (" + s.phNum + "): ");
                String p = sc.nextLine();
                if (!p.isBlank()) s.phNum = p;
                System.out.print("Status Active/Inactive (" + s.status + "): ");
                String st = sc.nextLine();
                if (!st.isBlank()) s.status = st;
                System.out.println("Student updated!");
                return;
            }
        }
        System.out.println("Student not found!");
    }

    static void deleteStudent(Scanner sc) {
        viewStudents();
        System.out.print("Enter Student ID to delete: ");
        int id = sc.nextInt(); sc.nextLine();
        students.removeIf(s -> s.id == id);
        System.out.println("Student deleted!");
    }

    static void searchStudent(Scanner sc) {
        System.out.print("Enter name to search: ");
        String key = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Student s : students) {
            if (s.stdName.toLowerCase().contains(key)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No students found with that name!");
    }

}