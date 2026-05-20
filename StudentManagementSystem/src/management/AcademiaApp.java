package management;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Function;

abstract class Person {
    private final int id;
    private final String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}

class Student extends Person {
    private final int semester;
    private final String term;
    private final double gpa;
    private final String department;
    private final String enrollmentType;
    private final String phoneNumber;
    private final String contactEmail;
    private final ArrayList<Course> enrolledCourses = new ArrayList<>();

    public Student(int id, String name, int semester, String term, double gpa,
                   String department, String enrollmentType, String phoneNumber, String contactEmail) {
        super(id, name);
        this.semester = semester;
        this.term = term;
        this.gpa = gpa;
        this.department = department;
        this.enrollmentType = enrollmentType;
        this.phoneNumber = phoneNumber;
        this.contactEmail = contactEmail;
    }

    public int getSemester() { return semester; }
    public String getTerm() { return term; }
    public double getGpa() { return gpa; }
    public String getDepartment() { return department; }
    public String getEnrollmentType() { return enrollmentType; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getContactEmail() { return contactEmail; }
    public ArrayList<Course> getEnrolledCourses() { return enrolledCourses; }

    public void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            course.addStudent(this);
        }
    }
}

class Professor extends Person {
    private final String department;
    private final String academicRank;
    private final String officeRoom;
    private final ArrayList<Course> assignedCourses = new ArrayList<>();

    public Professor(int id, String name, String department, String academicRank, String officeRoom) {
        super(id, name);
        this.department = department;
        this.academicRank = academicRank;
        this.officeRoom = officeRoom;
    }

    public String getDepartment() { return department; }
    public String getAcademicRank() { return academicRank; }
    public String getOfficeRoom() { return officeRoom; }
    public ArrayList<Course> getAssignedCourses() { return assignedCourses; }

    public void assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
            course.setProfessor(this);
        }
    }
}

class TeachingAssistant extends Person {
    private final String department;
    private final String officeRoom;
    private final ArrayList<Course> assignedSections = new ArrayList<>();

    public TeachingAssistant(int id, String name, String department, String officeRoom) {
        super(id, name);
        this.department = department;
        this.officeRoom = officeRoom;
    }

    public String getDepartment() { return department; }
    public String getOfficeRoom() { return officeRoom; }
    public ArrayList<Course> getAssignedSections() { return assignedSections; }

    public void assignSection(Course course) {
        if (!assignedSections.contains(course)) {
            assignedSections.add(course);
            course.setTeachingAssistant(this);
        }
    }
}

class Course {
    private final String courseCode;
    private final String courseName;
    private final String day;
    private final String timeSlot;
    private final String roomNumber;
    private final int semester;
    private final int scheduleTrackId;
    private final boolean section;
    private Professor professor;
    private TeachingAssistant teachingAssistant;
    private final ArrayList<Student> enrolledStudents = new ArrayList<>();

    public Course(String courseCode, String courseName, String day, String timeSlot,
                  String roomNumber, int semester, int scheduleTrackId, boolean section) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.day = day;
        this.timeSlot = timeSlot;
        this.roomNumber = roomNumber;
        this.semester = semester;
        this.scheduleTrackId = scheduleTrackId;
        this.section = section;
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getDay() { return day; }
    public String getTimeSlot() { return timeSlot; }
    public String getRoomNumber() { return roomNumber; }
    public int getSemester() { return semester; }
    public int getScheduleTrackId() { return scheduleTrackId; }
    public boolean isSection() { return section; }
    public Professor getProfessor() { return professor; }
    public TeachingAssistant getTeachingAssistant() { return teachingAssistant; }

    public void setProfessor(Professor professor) { this.professor = professor; }
    public void setTeachingAssistant(TeachingAssistant teachingAssistant) { this.teachingAssistant = teachingAssistant; }

    public ArrayList<Student> getEnrolledStudents() { return enrolledStudents; }

    public void addStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }
}

class AcademiaEngine {
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Professor> professors = new ArrayList<>();
    private final ArrayList<TeachingAssistant> teachingAssistants = new ArrayList<>();
    private final ArrayList<Course> courses = new ArrayList<>();

    public void addStudent(Student student) { students.add(student); }
    public void addProfessor(Professor professor) { professors.add(professor); }
    public void addTA(TeachingAssistant ta) { teachingAssistants.add(ta); }
    public void addCourse(Course course) { courses.add(course); }

    public ArrayList<Student> getStudents() { return students; }
    public ArrayList<Professor> getProfessors() { return professors; }
    public ArrayList<TeachingAssistant> getTAs() { return teachingAssistants; }
    public ArrayList<Course> getCourses() { return courses; }

    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) return student;
        }
        return null;
    }
}

class StaffRow {
    private final int id;
    private final String name;
    private final String typeOrRank;
    private final String officeRoom;
    private final String department;

    public StaffRow(int id, String name, String typeOrRank, String officeRoom, String department) {
        this.id = id;
        this.name = name;
        this.typeOrRank = typeOrRank;
        this.officeRoom = officeRoom;
        this.department = department;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getTypeOrRank() { return typeOrRank; }
    public String getOfficeRoom() { return officeRoom; }
    public String getDepartment() { return department; }
}

public class AcademiaApp extends Application {
    private static final String PRIMARY = "#0F172A";
    private static final String ACCENT = "#0EA5E9";
    private static final String LIGHT = "#F1F5F9";
    private static final String CARD = "#FFFFFF";
    private static final String TEXT = "#1E293B";
    private static final String CYBER = "#0A0F1E";
    private static final String CYBER_CARD = "#141A30";

    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private final String[] times = {"8AM", "10AM", "12PM", "2PM", "4PM"};

    private AcademiaEngine engine;
    private StackPane contentStack;
    private final Map<String, Node> views = new LinkedHashMap<>();

    private TextField search;
    private ComboBox<String> deptFilter;
    private ComboBox<String> semFilter;
    private ComboBox<String> scheduleSem;
    private ComboBox<String> scheduleTrack;

    private TableView<Student> studentTable;
    private TableView<StaffRow> facultyTable;
    private TableView<Course> courseTable;

    private FilteredList<Student> filteredStudents;
    private FilteredList<StaffRow> filteredStaff;
    private FilteredList<Course> filteredCourses;

    private Label totalStudents;
    private Label totalStaff;
    private Label totalCourses;
    private Label avgGpa;
    private TextArea resourcePane;
    private TextArea performancePane;
    private TextArea capacityPane;
    private GridPane scheduleGrid;

    @Override
    public void start(Stage stage) {
        engine = new AcademiaEngine();
        DataSeedPipeline.populate(engine);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + LIGHT + ";");
        root.setLeft(sidebar());

        VBox centerAndTicker = new VBox();
        VBox.setVgrow(centerAndTicker, Priority.ALWAYS);

        Node mainArea = mainArea();
        VBox.setVgrow((Region) mainArea, Priority.ALWAYS);
        centerAndTicker.getChildren().addAll(mainArea, ticker());

        root.setCenter(centerAndTicker);

        refreshAllDataLayers();
        switchView("DASHBOARD");

        stage.setTitle("Academia OS - Performance Unified Architecture");
        stage.setScene(new Scene(root, 1450, 950));
        stage.setMinWidth(1300);
        stage.setMinHeight(850);
        stage.show();
    }

    private BorderPane mainArea() {
        BorderPane main = new BorderPane();
        main.setPadding(new Insets(20, 25, 10, 25));

        contentStack = new StackPane();
        // Make contentStack fill all available space
        BorderPane.setMargin(contentStack, Insets.EMPTY);

        views.put("DASHBOARD", dashboard());
        views.put("STUDENTS", studentsView());
        views.put("PROFESSORS", facultyView());
        views.put("COURSES", coursesView());
        views.put("SCHEDULES", schedulesView());
        views.put("OPERATIONS", onboardView());
        contentStack.getChildren().addAll(views.values());

        main.setTop(controlBar());
        main.setCenter(contentStack);

        // Ensure main area itself grows
        VBox.setVgrow(main, Priority.ALWAYS);
        return main;
    }

    private VBox sidebar() {
        VBox box = new VBox(8);
        box.setPrefWidth(260);
        // Fix: sidebar should fill the full height
        box.setMaxHeight(Double.MAX_VALUE);
        box.setPadding(new Insets(25, 15, 20, 15));
        box.setStyle("-fx-background-color:" + PRIMARY + ";");

        Label title = label("Academia Matrix", 22, true, "white");
        title.setPadding(new Insets(0, 0, 35, 5));

        Button dash = nav("Dashboard Stream", "DASHBOARD");
        Button stud = nav("Students Directory", "STUDENTS");
        Button prof = nav("Faculty & TA Rosters", "PROFESSORS");
        Button cour = nav("Course/Section Matrix", "COURSES");
        Button sched = nav("Academic Timetables", "SCHEDULES");
        Button ops = primary("Onboard Portal Center");
        ops.setMaxWidth(Double.MAX_VALUE);
        ops.setOnAction(e -> switchView("OPERATIONS"));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        box.getChildren().addAll(title, dash, stud, prof, cour, sched, spacer, ops);
        return box;
    }

    private HBox controlBar() {
        search = new TextField();
        search.setPromptText("Search...");
        search.setPrefWidth(220);

        deptFilter = combo(170, "All Departments", "Computer Science", "Information Systems", "Digital Media", "Artificial Intelligence");
        semFilter = combo(140, "All Semesters", "Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8");

        Button reset = primary("× Reset");
        reset.setStyle(reset.getStyle() + "-fx-background-color:#EF4444;");
        reset.setOnAction(e -> {
            search.clear();
            deptFilter.getSelectionModel().selectFirst();
            semFilter.getSelectionModel().selectFirst();
            filter();
        });

        search.textProperty().addListener((obs, oldText, newText) -> filter());
        deptFilter.valueProperty().addListener((obs, oldValue, newValue) -> filter());
        semFilter.valueProperty().addListener((obs, oldValue, newValue) -> filter());

        HBox bar = new HBox(12, new Label("Search Name/Code:"), search, new Label("Department:"), deptFilter,
                new Label("Semester:"), semFilter, reset);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 0, 15, 0));
        return bar;
    }

    private VBox dashboard() {
        VBox main = new VBox(18);
        main.setPadding(new Insets(20));
        main.setStyle("-fx-background-color:" + CYBER + ";-fx-background-radius:8;");
        // Fix: dashboard fills the stack
        main.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label title = label("ENTERPRISE METRIC SYSTEMS OVERVIEW - METRICS & PARALLEL TIMETABLE SIMULATIONS", 13, true, ACCENT);

        totalStudents = scoreCard("TOTAL ACTIVE", "0", "LAUNCH DIRECTORY →", ACCENT, "STUDENTS");
        totalStaff = scoreCard("FACULTY & TAs", "0", "ACCESS ROSTERS →", "#A855F7", "PROFESSORS");
        totalCourses = scoreCard("COURSES & SECTIONS", "0", "MAP CATALOGS →", "#10B981", "COURSES");
        avgGpa = scoreCard("INSTITUTIONAL GPA", "0.00", "SYSTEM NOMINAL", "#F59E0B", "DASHBOARD");

        HBox cards = new HBox(18, totalStudents, totalStaff, totalCourses, avgGpa);
        cards.setMaxWidth(Double.MAX_VALUE);
        cards.getChildren().forEach(card -> HBox.setHgrow(card, Priority.ALWAYS));
        // Fix: score cards stretch to equal width
        for (Node card : cards.getChildren()) {
            ((Label) card).setMaxWidth(Double.MAX_VALUE);
        }

        resourcePane = console();
        performancePane = console();
        capacityPane = console();

        // Fix: analytics cards grow vertically to fill remaining space
        VBox resCard = analyticsCard("/// INTERACTIVE FACULTY RESOURCE WORKLOAD METER", resourcePane);
        VBox perfCard = analyticsCard("/// ACADEMIC MONITOR NODE PERFORMANCES", performancePane);
        VBox capCard = analyticsCard("/// INTERACTIVE CAPACITY MATRIX ROOM BOTTLENECK MAP", capacityPane);

        HBox analytics = new HBox(15, resCard, perfCard, capCard);
        analytics.setMaxWidth(Double.MAX_VALUE);
        analytics.setMaxHeight(Double.MAX_VALUE);
        analytics.getChildren().forEach(card -> {
            HBox.setHgrow(card, Priority.ALWAYS);
            ((VBox) card).setMaxHeight(Double.MAX_VALUE);
        });
        VBox.setVgrow(analytics, Priority.ALWAYS);

        main.getChildren().addAll(title, cards, analytics);
        return main;
    }

    private BorderPane studentsView() {
        studentTable = table();
        studentTable.getColumns().addAll(
                column("ID", s -> String.valueOf(s.getId())),
                column("Student Full Name", Student::getName),
                column("Enrollment Term", Student::getTerm),
                column("Semester", s -> "Semester " + s.getSemester()),
                column("Cumulative GPA", s -> String.format("%.2f", s.getGpa())),
                column("Department Track", Student::getDepartment)
        );

        studentTable.setRowFactory(tableView -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showStudentDetails(row.getItem());
                }
            });
            return row;
        });

        BorderPane panel = panel();
        panel.setCenter(studentTable);
        // Fix: table fills the panel
        BorderPane.setMargin(studentTable, Insets.EMPTY);
        return panel;
    }

    private BorderPane facultyView() {
        facultyTable = table();
        facultyTable.getColumns().addAll(
                column("ID", s -> String.valueOf(s.getId())),
                column("Staff Member Name", StaffRow::getName),
                column("Type/Rank", StaffRow::getTypeOrRank),
                column("Office Room", StaffRow::getOfficeRoom),
                column("Department Specialization", StaffRow::getDepartment)
        );

        BorderPane panel = panel();
        panel.setCenter(facultyTable);
        return panel;
    }

    private BorderPane coursesView() {
        courseTable = table();
        courseTable.getColumns().addAll(
                column("Code", Course::getCourseCode),
                column("Course Name Designation", Course::getCourseName),
                column("Sem", c -> String.valueOf(c.getSemester())),
                column("Schedule Track", c -> "Track Option " + c.getScheduleTrackId()),
                column("Type", c -> c.isSection() ? "Section" : "Lecture"),
                column("Assigned Educator", this::instructor),
                column("Weekly Day", Course::getDay),
                column("Time Slot Grid", Course::getTimeSlot),
                column("Room Assignment", Course::getRoomNumber)
        );

        BorderPane panel = panel();
        panel.setCenter(courseTable);
        return panel;
    }

    private void showStudentDetails(Student student) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Student Academic Profile");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox content = new VBox(12);
        content.setPadding(new Insets(15));
        content.setPrefSize(560, 480);

        GridPane info = new GridPane();
        info.setHgap(12);
        info.setVgap(8);
        info.setPadding(new Insets(12));
        info.setStyle("-fx-background-color:#F8FAFC;-fx-background-radius:8;-fx-border-color:#E2E8F0;-fx-border-radius:8;");

        addInfo(info, 0, "Name:", student.getName());
        addInfo(info, 1, "ID:", String.valueOf(student.getId()));
        addInfo(info, 2, "Department:", student.getDepartment());
        addInfo(info, 3, "Semester:", "Semester " + student.getSemester());
        addInfo(info, 4, "GPA:", String.format("%.2f", student.getGpa()));
        addInfo(info, 5, "Phone:", student.getPhoneNumber());
        addInfo(info, 6, "Email:", student.getContactEmail());

        TableView<Course> scheduleTable = table();
        scheduleTable.getColumns().addAll(
                column("Course Code", Course::getCourseCode),
                column("Course Title", Course::getCourseName),
                column("Day", Course::getDay),
                column("Time", Course::getTimeSlot),
                column("Room", Course::getRoomNumber)
        );

        ObservableList<Course> semesterCourses = FXCollections.observableArrayList();
        for (Course course : engine.getCourses()) {
            if (course.getSemester() == student.getSemester()) {
                semesterCourses.add(course);
            }
        }
        scheduleTable.setItems(semesterCourses);
        VBox.setVgrow(scheduleTable, Priority.ALWAYS);

        content.getChildren().addAll(info, label("Courses for Student Semester", 14, true, TEXT), scheduleTable);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private BorderPane schedulesView() {
        scheduleSem = combo(140, "Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5");
        scheduleTrack = combo(180, "Schedule Variant 1", "Schedule Variant 2", "Schedule Variant 3");

        HBox controls = new HBox(15, label("Semester Group:", 13, true, TEXT), scheduleSem,
                label("Parallel Track Configuration:", 13, true, TEXT), scheduleTrack);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(0, 0, 15, 0));

        scheduleGrid = new GridPane();
        scheduleGrid.setHgap(1);
        scheduleGrid.setVgap(1);
        scheduleGrid.setStyle("-fx-background-color:#CBD5E1;");

        scheduleSem.valueProperty().addListener((obs, oldValue, newValue) -> updateSchedule());
        scheduleTrack.valueProperty().addListener((obs, oldValue, newValue) -> updateSchedule());

        // Fix: ScrollPane fills the panel properly
        ScrollPane scrollPane = new ScrollPane(scheduleGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        BorderPane panel = panel();
        panel.setPadding(new Insets(20));
        panel.setTop(controls);
        panel.setCenter(scrollPane);
        return panel;
    }

    private void updateSchedule() {
        if (scheduleGrid == null || scheduleSem == null || scheduleTrack == null) return;

        scheduleGrid.getChildren().clear();

        int selectedSemester = scheduleSem.getSelectionModel().getSelectedIndex() + 1;
        int selectedTrack = scheduleTrack.getSelectionModel().getSelectedIndex() + 1;

        Course[][] grid = new Course[days.length][times.length];

        for (Course course : engine.getCourses()) {
            if (course.getSemester() == selectedSemester && course.getScheduleTrackId() == selectedTrack) {
                int dayIndex = index(days, course.getDay());
                int timeIndex = index(times, course.getTimeSlot());
                if (dayIndex >= 0 && timeIndex >= 0) {
                    grid[dayIndex][timeIndex] = course;
                }
            }
        }

        scheduleGrid.add(cell("Day Axis", true), 0, 0);
        for (int col = 0; col < times.length; col++) {
            scheduleGrid.add(cell(times[col], true), col + 1, 0);
        }

        for (int row = 0; row < days.length; row++) {
            scheduleGrid.add(cell(days[row], true), 0, row + 1);
            for (int col = 0; col < times.length; col++) {
                scheduleGrid.add(cell(scheduleText(grid[row][col]), false), col + 1, row + 1);
            }
        }
    }

    private HBox onboardView() {
        VBox studentCard = formCard("Onboard Brand New Student");
        VBox staffCard = formCard("Onboard Faculty Staff / Teaching Assistant");

        TextField studentName = new TextField();
        TextField studentPhone = new TextField();
        TextField studentEmail = new TextField();
        ComboBox<String> studentSemester = combo(280, "1", "2", "3", "4");
        ComboBox<String> studentDepartment = deptCombo();
        Button saveStudent = primary("Register Profile Node");

        studentCard.getChildren().addAll(
                row("Full Name:", studentName),
                row("Semester Placed:", studentSemester),
                row("Department:", studentDepartment),
                row("Phone number:", studentPhone),
                row("Email address:", studentEmail),
                saveStudent
        );

        TextField staffName = new TextField();
        TextField staffOffice = new TextField();
        ComboBox<String> staffType = combo(280, "Professor Lecture Faculty", "Teaching Assistant (TA)");
        ComboBox<String> staffDepartment = deptCombo();
        Button saveStaff = primary("Induct Staff Member");

        staffCard.getChildren().addAll(
                row("Staff Full Name:", staffName),
                row("Role Classification:", staffType),
                row("Department:", staffDepartment),
                row("Office Room Base:", staffOffice),
                saveStaff
        );

        saveStudent.setOnAction(event -> {
            if (studentName.getText().trim().isEmpty()) {
                alert(Alert.AlertType.WARNING, "Validation Required", "Student name is required.");
                return;
            }

            engine.addStudent(new Student(
                    202600 + engine.getStudents().size() + 1,
                    studentName.getText().trim(),
                    Integer.parseInt(studentSemester.getValue()),
                    "Fall 2026",
                    3.40,
                    studentDepartment.getValue(),
                    "Regular",
                    studentPhone.getText().trim(),
                    studentEmail.getText().trim()
            ));

            studentName.clear();
            studentPhone.clear();
            studentEmail.clear();
            refreshAllDataLayers();
        });

        saveStaff.setOnAction(event -> {
            if (staffName.getText().trim().isEmpty()) {
                alert(Alert.AlertType.WARNING, "Validation Required", "Staff name is required.");
                return;
            }

            String office = staffOffice.getText().trim().isEmpty() ? "Lab-400" : staffOffice.getText().trim();

            if (staffType.getSelectionModel().getSelectedIndex() == 0) {
                engine.addProfessor(new Professor(
                        9000 + engine.getProfessors().size() + 1,
                        staffName.getText().trim(),
                        staffDepartment.getValue(),
                        "Professor",
                        office
                ));
            } else {
                engine.addTA(new TeachingAssistant(
                        5000 + engine.getTAs().size() + 1,
                        staffName.getText().trim(),
                        staffDepartment.getValue(),
                        office
                ));
            }

            staffName.clear();
            staffOffice.clear();
            refreshAllDataLayers();
        });

        HBox root = new HBox(25, studentCard, staffCard);
        root.setPadding(new Insets(5));
        // Fix: form cards grow to fill width
        root.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(studentCard, Priority.ALWAYS);
        HBox.setHgrow(staffCard, Priority.ALWAYS);
        return root;
    }

    private StackPane ticker() {
        StackPane pane = new StackPane();
        pane.setPrefHeight(30);
        pane.setMinHeight(30);
        pane.setMaxHeight(30);
        pane.setStyle("-fx-background-color:#060A17;-fx-border-color:#1E293B transparent transparent transparent;");

        String[] alerts = {
                "SYSTEM UPDATE: Expanded course datasets successfully mapped across parallel schedules.",
                "ALERT: Faculty tracking confirms unique non-overlapping course time slots updated.",
                "DATA SYNC: Multiple custom schedule options generated for Semester Tracks 1, 2, and 3."
        };

        Label text = label(alerts[0], 13, true, ACCENT);
        text.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        text.setManaged(false);
        text.setLayoutY(6);

        pane.getChildren().add(text);

        final int[] idx = {0};
        pane.widthProperty().addListener((obs, oldWidth, newWidth) -> text.setLayoutX(newWidth.doubleValue()));

        Timeline loop = new Timeline(new KeyFrame(Duration.millis(15), event -> {
            text.setLayoutX(text.getLayoutX() - 2);
            if (text.getLayoutX() < -text.getWidth() - 50) {
                idx[0] = (idx[0] + 1) % alerts.length;
                text.setText(alerts[idx[0]]);
                text.setLayoutX(pane.getWidth());
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
        return pane;
    }

    private void refreshAllDataLayers() {
        ObservableList<Student> students = FXCollections.observableArrayList(engine.getStudents());

        ObservableList<StaffRow> staff = FXCollections.observableArrayList();
        for (Professor professor : engine.getProfessors()) {
            staff.add(new StaffRow(professor.getId(), professor.getName(), professor.getAcademicRank(), professor.getOfficeRoom(), professor.getDepartment()));
        }
        for (TeachingAssistant ta : engine.getTAs()) {
            staff.add(new StaffRow(ta.getId(), ta.getName(), "Teaching Assistant", ta.getOfficeRoom(), ta.getDepartment()));
        }

        ObservableList<Course> courses = FXCollections.observableArrayList(engine.getCourses());

        filteredStudents = new FilteredList<>(students, s -> true);
        filteredStaff = new FilteredList<>(staff, s -> true);
        filteredCourses = new FilteredList<>(courses, c -> true);

        if (studentTable != null) studentTable.setItems(filteredStudents);
        if (facultyTable != null) facultyTable.setItems(filteredStaff);
        if (courseTable != null) courseTable.setItems(filteredCourses);

        updateDashboard();
        updateSchedule();
        filter();
    }

    private void updateDashboard() {
        double sum = 0.0;
        for (Student student : engine.getStudents()) sum += student.getGpa();

        double average = engine.getStudents().isEmpty() ? 0.0 : sum / engine.getStudents().size();

        if (totalStudents != null) totalStudents.setText("TOTAL ACTIVE\n" + engine.getStudents().size() + "\n\nLAUNCH DIRECTORY →");
        if (totalStaff != null) totalStaff.setText("FACULTY & TAs\n" + (engine.getProfessors().size() + engine.getTAs().size()) + "\n\nACCESS ROSTERS →");
        if (totalCourses != null) totalCourses.setText("COURSES & SECTIONS\n" + engine.getCourses().size() + "\n\nMAP CATALOGS →");
        if (avgGpa != null) avgGpa.setText("INSTITUTIONAL GPA\n" + String.format("%.2f", average) + "\n\nSYSTEM NOMINAL");

        if (resourcePane != null) {
            StringBuilder builder = new StringBuilder("\n  [FACULTY CAPACITY RESOURCE LOADING]\n\n");
            for (Professor professor : engine.getProfessors()) {
                builder.append(String.format("  » [Prof] %-20s Loads: %d%n", professor.getName(), professor.getAssignedCourses().size()));
            }
            resourcePane.setText(builder.toString());
        }

        if (performancePane != null) {
            performancePane.setText("\n  [ANALYTIC STATUS NODE]\n\n  » Simulation Tracks: Nominal\n  » Multi-Schedules active: True");
        }

        if (capacityPane != null) {
            Map<String, Integer> rooms = new HashMap<>();
            for (Course course : engine.getCourses()) {
                rooms.put(course.getRoomNumber(), rooms.getOrDefault(course.getRoomNumber(), 0) + 1);
            }

            StringBuilder builder = new StringBuilder("\n  [ROOM ASSIGNMENTS FREQUENCY]\n\n");
            for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
                builder.append(String.format("  • %-15s Allocated %d classes%n", entry.getKey(), entry.getValue()));
            }
            capacityPane.setText(builder.toString());
        }
    }

    private void filter() {
        if (filteredStudents == null || filteredStaff == null || filteredCourses == null) return;

        String query = search == null ? "" : search.getText().trim().toLowerCase();
        String department = comboValue(deptFilter, "All Departments");
        String semester = comboValue(semFilter, "All Semesters");

        filteredStudents.setPredicate(student ->
                (query.isEmpty() || student.getName().toLowerCase().contains(query) || String.valueOf(student.getId()).contains(query))
                        && (department.equals("All Departments") || student.getDepartment().equalsIgnoreCase(department))
                        && (semester.equals("All Semesters") || semester.equalsIgnoreCase("Semester " + student.getSemester()))
        );

        filteredStaff.setPredicate(staff ->
                (query.isEmpty() || staff.getName().toLowerCase().contains(query) || String.valueOf(staff.getId()).contains(query))
                        && (department.equals("All Departments") || staff.getDepartment().equalsIgnoreCase(department))
        );

        filteredCourses.setPredicate(course ->
                (query.isEmpty() || course.getCourseName().toLowerCase().contains(query) || course.getCourseCode().toLowerCase().contains(query))
                        && (semester.equals("All Semesters") || semester.replace("Semester ", "").equals(String.valueOf(course.getSemester())))
        );
    }

    private void switchView(String key) {
        for (Node node : contentStack.getChildren()) {
            node.setVisible(false);
            node.setManaged(false);
        }

        Node view = views.get(key);
        if (view != null) {
            view.setVisible(true);
            view.setManaged(true);
        }
    }

    private String instructor(Course course) {
        if (!course.isSection() && course.getProfessor() != null) return course.getProfessor().getName();
        if (course.isSection() && course.getTeachingAssistant() != null) return course.getTeachingAssistant().getName();
        return "Staff Unassigned";
    }

    private String scheduleText(Course course) {
        return course == null ? "" : course.getCourseName() + "\n" + course.getCourseCode() + "\n" + instructor(course) + "\n[" + course.getRoomNumber() + "]";
    }

    private int index(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(value)) return i;
        }
        return -1;
    }

    private String comboValue(ComboBox<String> comboBox, String defaultValue) {
        return comboBox == null || comboBox.getValue() == null ? defaultValue : comboBox.getValue();
    }

    private Button nav(String text, String viewKey) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setTextFill(Color.web("#94A3B8"));
        button.setFont(Font.font("Segoe UI", 14));

        String normal = "-fx-background-color:transparent;-fx-padding:12 15 12 15;-fx-cursor:hand;";
        String hover = "-fx-background-color:#1E293B;-fx-background-radius:8;-fx-padding:12 15 12 15;-fx-cursor:hand;";

        button.setStyle(normal);
        button.setOnMouseEntered(event -> button.setStyle(hover));
        button.setOnMouseExited(event -> button.setStyle(normal));
        button.setOnAction(event -> switchView(viewKey));

        return button;
    }

    private Button primary(String text) {
        Button button = new Button(text);
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        button.setStyle("-fx-background-color:" + ACCENT + ";-fx-background-radius:6;-fx-padding:9 18 9 18;-fx-cursor:hand;");
        return button;
    }

    private ComboBox<String> combo(double width, String... items) {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(items));
        comboBox.getSelectionModel().selectFirst();
        comboBox.setPrefWidth(width);
        return comboBox;
    }

    private ComboBox<String> deptCombo() {
        return combo(280, "Computer Science", "Information Systems", "Digital Media", "Artificial Intelligence");
    }

    private Label label(String text, int size, boolean bold, String color) {
        Label label = new Label(text);
        label.setTextFill(Color.web(color));
        label.setFont(Font.font("Segoe UI", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        return label;
    }

    private BorderPane panel() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(5));
        // Fix: panel fills its parent container
        panel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        panel.setStyle("-fx-background-color:" + CARD + ";-fx-background-radius:8;-fx-border-color:#E2E8F0;-fx-border-radius:8;");
        return panel;
    }

    private VBox formCard(String title) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(22));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color:" + CARD + ";-fx-background-radius:8;-fx-border-color:#E2E8F0;-fx-border-radius:8;");
        card.getChildren().add(label(title, 15, true, TEXT));
        return card;
    }

    private HBox row(String labelText, Node input) {
        Label label = label(labelText, 13, true, TEXT);
        label.setMinWidth(145);

        if (input instanceof Region) {
            ((Region) input).setPrefWidth(280);
        }

        HBox row = new HBox(12, label, input);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void addInfo(GridPane grid, int row, String labelText, String valueText) {
        grid.add(label(labelText, 13, true, TEXT), 0, row);
        grid.add(label(valueText, 13, false, TEXT), 1, row);
    }

    private <S> TableView<S> table() {
        TableView<S> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Fix: table fills its container
        tableView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        return tableView;
    }

    private <S> TableColumn<S, String> column(String title, Function<S, String> mapper) {
        TableColumn<S, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(mapper.apply(data.getValue())));
        return column;
    }

    private Label scoreCard(String title, String value, String footer, String color, String viewKey) {
        Label label = label(title + "\n" + value + "\n\n" + footer, 13, true, color);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMinHeight(150);
        label.setPadding(new Insets(12));
        label.setAlignment(Pos.CENTER_LEFT);
        label.setStyle("-fx-background-color:" + CYBER_CARD + ";-fx-background-radius:8;-fx-border-radius:8;-fx-border-width:1 1 1 5;-fx-border-color:#283453 #283453 #283453 " + color + ";");
        label.setOnMouseClicked(event -> switchView(viewKey));
        return label;
    }

    private VBox analyticsCard(String title, TextArea area) {
        VBox card = new VBox(10, label(title, 12, true, "white"), area);
        card.setPadding(new Insets(15));
        // Fix: analytics card fills vertical space
        card.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(card, Priority.ALWAYS);
        card.setStyle("-fx-background-color:" + CYBER_CARD + ";-fx-background-radius:8;-fx-border-color:#1E293B;-fx-border-radius:8;");
        VBox.setVgrow(area, Priority.ALWAYS);
        return card;
    }

    private TextArea console() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        // Fix: TextArea fills its card
        textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        textArea.setStyle("-fx-control-inner-background:#0D1224;-fx-text-fill:#38BDF8;");
        return textArea;
    }

    private StackPane cell(String text, boolean header) {
        Label label = label(text, header ? 13 : 12, header, TEXT);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        StackPane cell = new StackPane(label);
        cell.setMinSize(header ? 130 : 190, header ? 45 : 115);
        cell.setPrefSize(header ? 130 : 190, header ? 45 : 115);
        cell.setPadding(new Insets(8));
        cell.setStyle("-fx-background-color:" + (header ? "#E2E8F0" : "#FFFFFF") + ";");
        return cell;
    }

    private void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    static class DataSeedPipeline {
        static void populate(AcademiaEngine engine) {
            String[] departments = {"Computer Science", "Information Systems", "Digital Media", "Artificial Intelligence"};

            Professor[] professors = {
                    new Professor(101, "Dr. Adel Emam", departments[0], "Full Professor", "Hall-Room-301"),
                    new Professor(102, "Dr. Mohamed Salah", departments[3], "Research Chair", "Hall-Room-302"),
                    new Professor(103, "Dr. Mohamed Ramadan", departments[1], "Associate Professor", "Hall-Room-303"),
                    new Professor(104, "Dr. Yehia El-Fakharany", departments[0], "Senior Faculty", "Hall-Room-304"),
                    new Professor(105, "Dr. Ahmed Helmy", departments[2], "Department Head", "Hall-Room-305"),
                    new Professor(106, "Dr. Yousra Ibrahim", departments[2], "Professor Emeritus", "Hall-Room-401"),
                    new Professor(107, "Dr. Amr Diab", departments[3], "Distinguished Lecturer", "Hall-Room-402"),
                    new Professor(108, "Dr. Mervat Amin", departments[1], "Senior Academic", "Hall-Room-403")
            };

            TeachingAssistant[] tas = {
                    new TeachingAssistant(201, "Eng. Maged El-Kedwany", departments[0], "TA-Lab-101"),
                    new TeachingAssistant(202, "Eng. Akram Hosny", departments[1], "TA-Lab-102"),
                    new TeachingAssistant(203, "Eng. Karim Abdel Aziz", departments[2], "TA-Lab-103"),
                    new TeachingAssistant(204, "Eng. Tamer Hosny", departments[3], "TA-Lab-104"),
                    new TeachingAssistant(205, "Eng. Ahmed Mekky", departments[0], "TA-Lab-105")
            };

            for (Professor professor : professors) engine.addProfessor(professor);
            for (TeachingAssistant ta : tas) engine.addTA(ta);

            Object[][] studentData = {
                    {202601, "Ahmed Mansour", 1, 3.85, departments[0]},
                    {202602, "Mina Shenouda", 1, 3.91, departments[0]},
                    {202603, "Youssef Ibrahim", 2, 2.15, departments[1]},
                    {202604, "Mariam Hassan", 2, 3.72, departments[2]},
                    {202605, "Fatma Ali", 3, 1.88, departments[3]},
                    {202606, "Omar Sherif", 3, 3.55, departments[0]},
                    {202607, "Nour El-Din", 4, 2.95, departments[1]},
                    {202608, "Layla Khaled", 5, 3.40, departments[2]},
                    {202609, "Hassan Kamal", 6, 3.10, departments[3]},
                    {202610, "Salma Yasser", 7, 3.95, departments[0]}
            };

            for (Object[] row : studentData) {
                engine.addStudent(new Student(
                        (int) row[0],
                        (String) row[1],
                        (int) row[2],
                        "Fall 2026",
                        (double) row[3],
                        (String) row[4],
                        "Regular",
                        "01000",
                        "stud@edu.eg"
                ));
            }

            add(engine, professors[0], null, new Course("CS-101", "Programming I", "Monday", "8AM", "Hall-301", 1, 1, false));
            add(engine, professors[1], null, new Course("AI-101", "Neural Basics", "Wednesday", "10AM", "Hall-302", 1, 1, false));
            add(engine, professors[7], null, new Course("IS-404", "Enterprise Systems", "Tuesday", "12PM", "Hall-403", 4, 1, false));
            add(engine, professors[5], null, new Course("DM-303", "Digital Cinematography", "Friday", "4PM", "Studio-1", 3, 2, false));
            add(engine, professors[6], null, new Course("AI-505", "Advanced Robotics", "Thursday", "2PM", "Lab-205", 5, 1, false));
            add(engine, professors[0], null, new Course("CS-202", "Systems Architecture", "Monday", "10AM", "Hall-301", 2, 1, false));

            add(engine, null, tas[0], new Course("CS-101-S", "Programming I Section", "Tuesday", "8AM", "Lab-101", 1, 1, true));
            add(engine, null, tas[3], new Course("AI-101-S", "Neural Basics Section", "Thursday", "10AM", "Lab-104", 1, 1, true));
        }

        static void add(AcademiaEngine engine, Professor professor, TeachingAssistant ta, Course course) {
            if (professor != null) professor.assignCourse(course);
            if (ta != null) ta.assignSection(course);
            engine.addCourse(course);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}