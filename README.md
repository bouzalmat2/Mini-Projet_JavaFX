# Student Grades Management System (JavaFX + MySQL)

This is a JavaFX desktop application that allows managing students, their notes, and academic records. The system was built using the MVC architecture with a MySQL database backend.

##  How to Run

1. Clone the repo:
```
git clone https://github.com/bouzalmat2/Mini-Projet_JavaFX.git
cd javafx-gestion-notes
```

## Features

 Gestion des étudiants :
- Add, edit, delete and search students.
- ComboBoxes for filière → niveau (dynamic loading).
- Uniqueness check for student code.
- Validation for required fields.
- Export deleted students to XML file (optional).
- Restore deleted students (optional).

 Gestion des notes :
- Display list of matières based on filière and niveau.
- Add, edit, delete student notes.
- Automatic annual average calculation.
- Trigger to update student level if average ≥ 10.
- Validation for note between 0 and 20.

 Consultation :
- View notes by student.
- View annual report.
- Generate PDF report for deleted/restored data.

##  Technologies Used

- JavaFX (GUI)
- Java 17
- MySQL 8+
- JDBC
- SceneBuilder
- Maven (with javafx-maven-plugin)
- FXML (JavaFX UI markup)
- JasperReports (for PDF reports – optional)

