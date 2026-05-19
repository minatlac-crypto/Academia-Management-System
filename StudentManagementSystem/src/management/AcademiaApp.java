package management;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ============================================================================
// CORE DATA MODELS
// ============================================================================

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
    private final ArrayList<Course> enrolledCourses;

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
        this.enrolledCourses = new ArrayList<>();
    }

    public int getSemester()          { return semester; }
    public String getTerm()           { return term; }
    public double getGpa()            { return gpa; }
    public String getDepartment()     { return department; }
    public String getEnrollmentType() { return enrollmentType; }
    public String getPhoneNumber()    { return phoneNumber; }
    public String getContactEmail()   { return contactEmail; }
    public ArrayList<Course> getEnrolledCourses() { return enrolledCourses; }

    public boolean enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            course.addStudent(this);
        }
        return true;
    }
}

class Professor extends Person {
    private final String department;
    private final String academicRank;
    private final String officeRoom;
    private final ArrayList<Course> assignedCourses;

    public Professor(int id, String name, String department, String academicRank, String officeRoom) {
        super(id, name);
        this.department = department;
        this.academicRank = academicRank;
        this.officeRoom = officeRoom;
        this.assignedCourses = new ArrayList<>();
    }

    public String getDepartment()           { return department; }
    public String getAcademicRank()         { return academicRank; }
    public String getOfficeRoom()           { return officeRoom; }
    public ArrayList<Course> getAssignedCourses() { return assignedCourses; }

    public boolean assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
            course.setProfessor(this);
        }
        return true;
    }
}

class TeachingAssistant extends Person {
    private final String department;
    private final String officeRoom;
    private final ArrayList<Course> assignedSections;

    public TeachingAssistant(int id, String name, String department, String officeRoom) {
        super(id, name);
        this.department = department;
        this.officeRoom = officeRoom;
        this.assignedSections = new ArrayList<>();
    }

    public String getDepartment()             { return department; }
    public String getOfficeRoom()             { return officeRoom; }
    public ArrayList<Course> getAssignedSections() { return assignedSections; }

    public boolean assignSection(Course section) {
        if (!assignedSections.contains(section)) {
            assignedSections.add(section);
            section.setTeachingAssistant(this);
        }
        return true;
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
    private final boolean isSection;
    private Professor professor;
    private TeachingAssistant teachingAssistant;
    private final ArrayList<Student> enrolledStudents;

    public Course(String courseCode, String courseName, String day, String timeSlot,
                  String roomNumber, int semester, int scheduleTrackId, boolean isSection) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.day = day;
        this.timeSlot = timeSlot;
        this.roomNumber = roomNumber;
        this.semester = semester;
        this.scheduleTrackId = scheduleTrackId;
        this.isSection = isSection;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCourseCode()      { return courseCode; }
    public String getCourseName()      { return courseName; }
    public String getDay()             { return day; }
    public String getTimeSlot()        { return timeSlot; }
    public String getRoomNumber()      { return roomNumber; }
    public int getSemester()           { return semester; }
    public int getScheduleTrackId()    { return scheduleTrackId; }
    public boolean isSection()         { return isSection; }

    public Professor getProfessor()    { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    public TeachingAssistant getTeachingAssistant() { return teachingAssistant; }
    public void setTeachingAssistant(TeachingAssistant ta) { this.teachingAssistant = ta; }

    public ArrayList<Student> getEnrolledStudents() { return enrolledStudents; }
    public void addStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }
}

// ============================================================================
// SYSTEM LOGIC ENGINE
// ============================================================================

class AcademiaEngine {
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Professor> professors = new ArrayList<>();
    private final ArrayList<TeachingAssistant> tas = new ArrayList<>();
    private final ArrayList<Course> courses = new ArrayList<>();
    private int uniqueScheduleCounter = 0;

    public void addStudent(Student s)           { students.add(s); }
    public void addProfessor(Professor p)        { professors.add(p); }
    public void addTA(TeachingAssistant ta)      { tas.add(ta); }
    public void addCourse(Course c)              { courses.add(c); }

    public ArrayList<Student> getStudents()             { return students; }
    public ArrayList<Professor> getProfessors()          { return professors; }
    public ArrayList<TeachingAssistant> getTAs()         { return tas; }
    public ArrayList<Course> getCourses()                { return courses; }

    public int getUniqueScheduleId() { return uniqueScheduleCounter++; }

    public Student findStudentById(int id) {
        for (Student s : students) if (s.getId() == id) return s;
        return null;
    }
}

// ============================================================================
// ANIMATED TICKER COMPONENT
// ============================================================================

class LiveNotificationMarquee extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JLabel lblTickerText;
    private final Timer loopTimer;
    private final List<String> alertPool;
    private int poolIndex = 0;
    private int currentXCoordinate;

    public LiveNotificationMarquee() {
        setLayout(new BorderLayout());
        setBackground(new Color(6, 10, 23));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(30, 41, 59)));
        setPreferredSize(new Dimension(1450, 30));

        alertPool = new ArrayList<>();
        alertPool.add("SYSTEM UPDATE: Expanded course datasets successfully mapped across parallel schedules.");
        alertPool.add("ALERT: Faculty tracking confirms unique non-overlapping course time slots updated.");
        alertPool.add("DATA SYNC: Multiple custom schedule options generated for Semester Tracks 1, 2, and 3.");

        lblTickerText = new JLabel(alertPool.get(0));
        lblTickerText.setFont(new Font("Consolas", Font.BOLD, 13));
        lblTickerText.setForeground(new Color(14, 165, 233));
        add(lblTickerText, BorderLayout.WEST);

        currentXCoordinate = 1450;
        loopTimer = new Timer(15, this);
        loopTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        currentXCoordinate -= 2;
        if (currentXCoordinate < -2000) {
            currentXCoordinate = getWidth() > 0 ? getWidth() : 1450;
            poolIndex = (poolIndex + 1) % alertPool.size();
            lblTickerText.setText(alertPool.get(poolIndex));
        }
        lblTickerText.setBorder(BorderFactory.createEmptyBorder(0, currentXCoordinate, 0, 0));
    }
}

// ============================================================================
// MAIN APPLICATION PORTAL INTERFACE
// ============================================================================

public class AcademiaApp extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Color COLOR_PRIMARY    = new Color(15, 23, 42);
    private static final Color COLOR_ACCENT     = new Color(14, 165, 233);
    private static final Color COLOR_SUCCESS    = new Color(16, 185, 129);
    private static final Color COLOR_BG_LIGHT   = new Color(241, 245, 249);
    private static final Color COLOR_CARD_BG    = Color.WHITE;
    private static final Color COLOR_TEXT_DARK  = new Color(30, 41, 59);

    private static final Color CYBER_DASH_BG  = new Color(10, 15, 30);
    private static final Color CYBER_CARD_BG  = new Color(20, 26, 48);
    private static final Color CYBER_TEXT     = new Color(248, 250, 252);

    private final AcademiaEngine engine;
    private CardLayout contentCardLayout;
    private JPanel centerContentViewport;

    private DefaultTableModel studentTableModel, professorTableModel, courseTableModel;
    private JTable studentTable, professorTable, courseTable;

    private JTextField searchInputField;
    private JComboBox<String> departmentFilterBox, semesterFilterBox;

    private JLabel lblTotalStudents, lblTotalProfessors, lblTotalCourses, lblAverageGpa;
    private JTextArea txtResourceMetricsPane, txtPerformanceAlertsPane, txtCapacityMapsPane;

    private JComboBox<String> comboScheduleTerms;
    private JComboBox<String> comboScheduleTracks;
    private DefaultTableModel termScheduleModel;
    private final String[] daysAxis  = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private final String[] timesAxis = {"8AM", "10AM", "12PM", "2PM", "4PM"};

    public AcademiaApp() {
        super("Academia OS - Performance Unified Architecture");
        engine = new AcademiaEngine();
        DataSeedPipeline.populate(engine);
        initializeUI();
        refreshAllDataLayers();
    }

    // -------------------------------------------------------------------------
    // SHOW STUDENT DETAILS DIALOG  (all fields accessed via getters)
    // -------------------------------------------------------------------------
    private void showStudentDetails(Student s) {
        JDialog detailDialog = new JDialog(this, "Student Academic Profile", true);
        detailDialog.setSize(500, 480);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setLayout(new BorderLayout(15, 15));

        // Profile header
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        infoPanel.add(new JLabel("  Name:        " + s.getName()));
        infoPanel.add(new JLabel("  ID:          " + s.getId()));
        infoPanel.add(new JLabel("  Department:  " + s.getDepartment()));
        infoPanel.add(new JLabel("  Semester:    " + s.getSemester()));
        infoPanel.add(new JLabel("  GPA:         " + String.format("%.2f", s.getGpa())));

        // Courses for this student's semester
        String[] columns = {"Course Code", "Course Title", "Day", "Time", "Room"};
        DefaultTableModel schedModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Course c : engine.getCourses()) {
            if (c.getSemester() == s.getSemester()) {
                schedModel.addRow(new Object[]{
                    c.getCourseCode(),
                    c.getCourseName(),
                    c.getDay(),
                    c.getTimeSlot(),
                    c.getRoomNumber()
                });
            }
        }

        JTable schedTable = new JTable(schedModel);
        schedTable.setFillsViewportHeight(true);
        schedTable.setEnabled(false);
        schedTable.setRowHeight(30);
        schedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        detailDialog.add(infoPanel, BorderLayout.NORTH);
        detailDialog.add(new JScrollPane(schedTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> detailDialog.dispose());
        btnPanel.add(btnClose);
        detailDialog.add(btnPanel, BorderLayout.SOUTH);

        detailDialog.setVisible(true);
    }

    // -------------------------------------------------------------------------
    // MAIN UI INITIALIZER  (single, complete method — initializeUI1 removed)
    // -------------------------------------------------------------------------
    private void initializeUI() {
        setSize(1450, 950);
        setMinimumSize(new Dimension(1300, 850));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout());

        // --- SIDEBAR NAVIGATION ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 950));
        sidebar.setBackground(COLOR_PRIMARY);

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 25));
        brandPanel.setOpaque(false);
        JLabel brandLabel = new JLabel("Academia Matrix");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandLabel.setForeground(Color.WHITE);
        brandPanel.add(brandLabel);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        JPanel navigationLayout = new JPanel();
        navigationLayout.setLayout(new BoxLayout(navigationLayout, BoxLayout.Y_AXIS));
        navigationLayout.setOpaque(false);
        navigationLayout.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton btnDash  = createNavigationButton("Dashboard Stream");
        JButton btnStud  = createNavigationButton("Students Directory");
        JButton btnProf  = createNavigationButton("Faculty & TA Rosters");
        JButton btnCour  = createNavigationButton("Course/Section Matrix");
        JButton btnSched = createNavigationButton("Academic Timetables");
        JButton btnOps   = createActionNavigationButton("Onboard Portal Center");

        navigationLayout.add(btnDash);  navigationLayout.add(Box.createVerticalStrut(8));
        navigationLayout.add(btnStud);  navigationLayout.add(Box.createVerticalStrut(8));
        navigationLayout.add(btnProf);  navigationLayout.add(Box.createVerticalStrut(8));
        navigationLayout.add(btnCour);  navigationLayout.add(Box.createVerticalStrut(8));
        navigationLayout.add(btnSched); navigationLayout.add(Box.createVerticalGlue());
        navigationLayout.add(btnOps);
        sidebar.add(navigationLayout, BorderLayout.CENTER);
        add(sidebar, BorderLayout.WEST);

        // --- TICKER ---
        add(new LiveNotificationMarquee(), BorderLayout.SOUTH);

        // --- MAIN VIEWPORT WRAPPER ---
        JPanel viewportWrapper = new JPanel(new BorderLayout());
        viewportWrapper.setOpaque(false);
        viewportWrapper.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        // --- SEARCH / FILTER CONTROL BAR ---
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        controlBar.setOpaque(false);
        controlBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        searchInputField = new JTextField();
        searchInputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchInputField.setPreferredSize(new Dimension(220, 35));
        searchInputField.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));

        JButton btnClearSearch = new JButton("× Reset");
        setupPrimaryButtonStyling(btnClearSearch);
        btnClearSearch.setBackground(new Color(239, 68, 68));
        btnClearSearch.setPreferredSize(new Dimension(85, 35));
        btnClearSearch.addActionListener(e -> {
            searchInputField.setText("");
            departmentFilterBox.setSelectedIndex(0);
            semesterFilterBox.setSelectedIndex(0);
            runGlobalDataFilter();
        });

        departmentFilterBox = new JComboBox<>(new String[]{
            "All Departments", "Computer Science", "Information Systems", "Digital Media", "Artificial Intelligence"});
        departmentFilterBox.setPreferredSize(new Dimension(170, 35));

        semesterFilterBox = new JComboBox<>(new String[]{
            "All Semesters", "Semester 1", "Semester 2", "Semester 3",
            "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"});
        semesterFilterBox.setPreferredSize(new Dimension(140, 35));

        controlBar.add(new JLabel("Search Name/Code:")); controlBar.add(searchInputField);
        controlBar.add(new JLabel("Department:"));        controlBar.add(departmentFilterBox);
        controlBar.add(new JLabel("Semester:"));          controlBar.add(semesterFilterBox);
        controlBar.add(btnClearSearch);
        viewportWrapper.add(controlBar, BorderLayout.NORTH);

        // --- CARD LAYOUT CONTAINER ---
        contentCardLayout = new CardLayout();
        centerContentViewport = new JPanel(contentCardLayout);
        centerContentViewport.setOpaque(false);

        // DASHBOARD VIEW
        JPanel homeDashboardView = createAdvancedAnalyticalDashboard();

        // STUDENTS VIEW
        JPanel studentsViewPanel = createTabularContainerPanel();
        studentTableModel = new DefaultTableModel(
            new String[]{"ID", "Student Full Name", "Enrollment Term", "Semester", "Cumulative GPA", "Department Track"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        studentTable = createStyledTable(studentTableModel);
        studentTable.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2 && studentTable.getSelectedRow() != -1) {
                    int row = studentTable.convertRowIndexToModel(studentTable.getSelectedRow());
                    int id = Integer.parseInt(studentTableModel.getValueAt(row, 0).toString());
                    Student s = engine.findStudentById(id);
                    if (s != null) showStudentDetails(s);
                }
            }
        });
        studentsViewPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // FACULTY VIEW
        JPanel facultyViewPanel = createTabularContainerPanel();
        professorTableModel = new DefaultTableModel(
            new String[]{"ID", "Staff Member Name", "Type/Rank", "Office Room", "Department Specialization"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        professorTable = createStyledTable(professorTableModel);
        facultyViewPanel.add(new JScrollPane(professorTable), BorderLayout.CENTER);

        // COURSES VIEW
        JPanel courseViewPanel = createTabularContainerPanel();
        courseTableModel = new DefaultTableModel(
            new String[]{"Code", "Course Name Designation", "Sem", "Schedule Track",
                         "Type", "Assigned Educator", "Weekly Day", "Time Slot Grid", "Room Assignment"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        courseTable = createStyledTable(courseTableModel);
        courseViewPanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        // SCHEDULE VIEW
        JPanel scheduleViewPanel = createAutomatedSchedulesPanel();

        // OPERATIONS VIEW
        JPanel entryConsolePanel = createNewRegistryOnboardCenter();

        // Register all cards
        centerContentViewport.add(homeDashboardView,  "DASHBOARD");
        centerContentViewport.add(studentsViewPanel,  "STUDENTS");
        centerContentViewport.add(facultyViewPanel,   "PROFESSORS");
        centerContentViewport.add(courseViewPanel,    "COURSES");
        centerContentViewport.add(scheduleViewPanel,  "SCHEDULES");
        centerContentViewport.add(entryConsolePanel,  "OPERATIONS");

        viewportWrapper.add(centerContentViewport, BorderLayout.CENTER);
        add(viewportWrapper, BorderLayout.CENTER);

        // Navigation action mappings
        btnDash.addActionListener(e  -> contentCardLayout.show(centerContentViewport, "DASHBOARD"));
        btnStud.addActionListener(e  -> contentCardLayout.show(centerContentViewport, "STUDENTS"));
        btnProf.addActionListener(e  -> contentCardLayout.show(centerContentViewport, "PROFESSORS"));
        btnCour.addActionListener(e  -> contentCardLayout.show(centerContentViewport, "COURSES"));
        btnSched.addActionListener(e -> contentCardLayout.show(centerContentViewport, "SCHEDULES"));
        btnOps.addActionListener(e   -> contentCardLayout.show(centerContentViewport, "OPERATIONS"));

        // Live filter wiring
        DocumentListener searchBoxWire = new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { runGlobalDataFilter(); }
            public void removeUpdate(DocumentEvent e)  { runGlobalDataFilter(); }
            public void changedUpdate(DocumentEvent e) { runGlobalDataFilter(); }
        };
        searchInputField.getDocument().addDocumentListener(searchBoxWire);
        departmentFilterBox.addActionListener(e -> runGlobalDataFilter());
        semesterFilterBox.addActionListener(e -> runGlobalDataFilter());
    }

    // -------------------------------------------------------------------------
    // GLOBAL FILTER
    // -------------------------------------------------------------------------
    private void runGlobalDataFilter() {
        String queryText   = searchInputField.getText().trim().toLowerCase();
        String selectedDept = departmentFilterBox.getSelectedItem().toString();
        String selectedSem  = semesterFilterBox.getSelectedItem().toString();

        if (studentTable.getRowSorter() instanceof TableRowSorter) {
            ((TableRowSorter<?>) studentTable.getRowSorter()).setRowFilter(new RowFilter<TableModel, Integer>() {
                @Override public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                    String name = entry.getStringValue(1).toLowerCase();
                    String sem  = entry.getStringValue(3);
                    String dept = entry.getStringValue(5);
                    return (queryText.isEmpty() || name.contains(queryText)) &&
                           (selectedDept.equals("All Departments") || dept.equalsIgnoreCase(selectedDept)) &&
                           (selectedSem.equals("All Semesters")    || sem.equalsIgnoreCase(selectedSem));
                }
            });
        }

        if (professorTable.getRowSorter() instanceof TableRowSorter) {
            ((TableRowSorter<?>) professorTable.getRowSorter()).setRowFilter(new RowFilter<TableModel, Integer>() {
                @Override public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                    String name = entry.getStringValue(1).toLowerCase();
                    String dept = entry.getStringValue(4);
                    return (queryText.isEmpty() || name.contains(queryText)) &&
                           (selectedDept.equals("All Departments") || dept.equalsIgnoreCase(selectedDept));
                }
            });
        }

        if (courseTable.getRowSorter() instanceof TableRowSorter) {
            ((TableRowSorter<?>) courseTable.getRowSorter()).setRowFilter(new RowFilter<TableModel, Integer>() {
                @Override public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                    String code = entry.getStringValue(0).toLowerCase();
                    String name = entry.getStringValue(1).toLowerCase();
                    String sem  = entry.getStringValue(2);
                    String semTarget = selectedSem.replace("Semester ", "");
                    return (queryText.isEmpty() || name.contains(queryText) || code.contains(queryText)) &&
                           (selectedSem.equals("All Semesters") || sem.equals(semTarget));
                }
            });
        }
    }

    // -------------------------------------------------------------------------
    // DASHBOARD
    // -------------------------------------------------------------------------
    private JPanel createAdvancedAnalyticalDashboard() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(CYBER_DASH_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblHeaderTitle = new JLabel(
            "ENTERPRISE METRIC SYSTEMS OVERVIEW - METRICS & PARALLEL TIMETABLE SIMULATIONS");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHeaderTitle.setForeground(COLOR_ACCENT);
        mainPanel.add(lblHeaderTitle, BorderLayout.NORTH);

        JPanel scoreboardPanel = new JPanel(new GridLayout(1, 4, 18, 0));
        scoreboardPanel.setOpaque(false);
        lblTotalStudents  = createCyberScoreCard(scoreboardPanel, COLOR_ACCENT, "STUDENTS");
        lblTotalProfessors = createCyberScoreCard(scoreboardPanel, new Color(168, 85, 247), "PROFESSORS");
        lblTotalCourses   = createCyberScoreCard(scoreboardPanel, COLOR_SUCCESS, "COURSES");
        lblAverageGpa     = createCyberScoreCard(scoreboardPanel, new Color(245, 158, 11), "DASHBOARD");

        JPanel matrixGridSplitPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        matrixGridSplitPanel.setOpaque(false);

        JPanel panelResource = createCyberAnalyticsCard("/// INTERACTIVE FACULTY RESOURCE WORKLOAD METER");
        txtResourceMetricsPane = createCyberConsoleTextArea();
        panelResource.add(new JScrollPane(txtResourceMetricsPane), BorderLayout.CENTER);

        JPanel panelPerformance = createCyberAnalyticsCard("/// ACADEMIC MONITOR NODE PERFORMANCES");
        txtPerformanceAlertsPane = createCyberConsoleTextArea();
        panelPerformance.add(new JScrollPane(txtPerformanceAlertsPane), BorderLayout.CENTER);

        JPanel panelCapacity = createCyberAnalyticsCard("/// INTERACTIVE CAPACITY MATRIX ROOM BOTTLENECK MAP");
        txtCapacityMapsPane = createCyberConsoleTextArea();
        panelCapacity.add(new JScrollPane(txtCapacityMapsPane), BorderLayout.CENTER);

        matrixGridSplitPanel.add(panelResource);
        matrixGridSplitPanel.add(panelPerformance);
        matrixGridSplitPanel.add(panelCapacity);

        JPanel contentStacker = new JPanel(new BorderLayout(0, 18));
        contentStacker.setOpaque(false);
        contentStacker.add(scoreboardPanel, BorderLayout.NORTH);
        contentStacker.add(matrixGridSplitPanel, BorderLayout.CENTER);
        mainPanel.add(contentStacker, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createCyberAnalyticsCard(String headerTitle) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CYBER_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(30, 41, 59), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        JLabel title = new JLabel(headerTitle);
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(CYBER_TEXT);
        card.add(title, BorderLayout.NORTH);
        return card;
    }

    private JTextArea createCyberConsoleTextArea() {
        JTextArea txt = new JTextArea();
        txt.setFont(new Font("Consolas", Font.BOLD, 12));
        txt.setEditable(false);
        txt.setBackground(new Color(13, 18, 36));
        txt.setForeground(new Color(56, 189, 248));
        txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return txt;
    }

    private JLabel createCyberScoreCard(JPanel container, Color highlightColor, String destinationKey) {
        JPanel cardOuterPanel = new JPanel(new BorderLayout());
        cardOuterPanel.setBackground(CYBER_CARD_BG);
        cardOuterPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cardOuterPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(40, 52, 83), 1, true),
            BorderFactory.createMatteBorder(0, 5, 0, 0, highlightColor)));
        cardOuterPanel.setBorder(BorderFactory.createCompoundBorder(
            cardOuterPanel.getBorder(),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel innerMetricsContentLabel = new JLabel();
        cardOuterPanel.add(innerMetricsContentLabel, BorderLayout.CENTER);

        cardOuterPanel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { contentCardLayout.show(centerContentViewport, destinationKey); }
            @Override public void mouseEntered(MouseEvent e) { cardOuterPanel.setBackground(new Color(28, 36, 65)); }
            @Override public void mouseExited(MouseEvent e)  { cardOuterPanel.setBackground(CYBER_CARD_BG); }
        });

        container.add(cardOuterPanel);
        return innerMetricsContentLabel;
    }

    // -------------------------------------------------------------------------
    // SCHEDULE PANEL
    // -------------------------------------------------------------------------
    private JPanel createAutomatedSchedulesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel configControlsLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        configControlsLine.setOpaque(false);

        JLabel lblTermSelect = new JLabel("Semester Group:");
        lblTermSelect.setFont(new Font("Segoe UI", Font.BOLD, 13));
        comboScheduleTerms = new JComboBox<>(
            new String[]{"Semester 1", "Semester 2", "Semester 3", "Semester 4"});
        comboScheduleTerms.setPreferredSize(new Dimension(140, 32));

        JLabel lblTrackSelect = new JLabel("Parallel Track Configuration:");
        lblTrackSelect.setFont(new Font("Segoe UI", Font.BOLD, 13));
        comboScheduleTracks = new JComboBox<>(
            new String[]{"Schedule Variant 1", "Schedule Variant 2", "Schedule Variant 3"});
        comboScheduleTracks.setPreferredSize(new Dimension(180, 32));

        configControlsLine.add(lblTermSelect);  configControlsLine.add(comboScheduleTerms);
        configControlsLine.add(Box.createHorizontalStrut(10));
        configControlsLine.add(lblTrackSelect); configControlsLine.add(comboScheduleTracks);
        panel.add(configControlsLine, BorderLayout.NORTH);

        String[] columns = {"Day Axis", "8:00 AM", "10:00 AM", "12:00 PM", "2:00 PM", "4:00 PM"};
        termScheduleModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tableMatrixGrid = new JTable(termScheduleModel);
        tableMatrixGrid.setRowHeight(115);
        tableMatrixGrid.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableMatrixGrid.setGridColor(new Color(226, 232, 240));
        tableMatrixGrid.getTableHeader().setReorderingAllowed(false);
        tableMatrixGrid.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableMatrixGrid.getTableHeader().setBackground(COLOR_BG_LIGHT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        tableMatrixGrid.setDefaultRenderer(Object.class, centerRenderer);

        panel.add(new JScrollPane(tableMatrixGrid), BorderLayout.CENTER);

        ActionListener matrixTrigger = e -> updateTermScheduleMatrixGrid();
        comboScheduleTerms.addActionListener(matrixTrigger);
        comboScheduleTracks.addActionListener(matrixTrigger);

        return panel;
    }

    private void updateTermScheduleMatrixGrid() {
        if (termScheduleModel == null) return;
        termScheduleModel.setRowCount(0);

        int selectedSemester = comboScheduleTerms.getSelectedIndex() + 1;
        int selectedTrack    = comboScheduleTracks.getSelectedIndex() + 1;

        Map<String, Integer> dayIndices  = new HashMap<>();
        Map<String, Integer> timeIndices = new HashMap<>();
        for (int i = 0; i < daysAxis.length;  i++) dayIndices.put(daysAxis[i].toLowerCase(), i);
        for (int j = 0; j < timesAxis.length; j++) timeIndices.put(timesAxis[j].toLowerCase(), j + 1);

        Course[][] grid = new Course[5][6];

        for (Course c : engine.getCourses()) {
            if (c.getSemester() == selectedSemester && c.getScheduleTrackId() == selectedTrack) {
                Integer dIdx = dayIndices.get(c.getDay().toLowerCase());
                Integer tIdx = timeIndices.get(c.getTimeSlot().toLowerCase());
                if (dIdx != null && tIdx != null) grid[dIdx][tIdx] = c;
            }
        }

        for (int r = 0; r < 5; r++) {
            Object[] rowData = new Object[6];
            rowData[0] = "<html><body style='text-align:center;'><b>" + daysAxis[r] + "</b></body></html>";
            for (int c = 1; c < 6; c++) {
                Course course = grid[r][c];
                if (course == null) {
                    rowData[c] = "";
                } else {
                    String staffName = "Staff Unassigned";
                    if (!course.isSection() && course.getProfessor() != null)
                        staffName = course.getProfessor().getName();
                    if (course.isSection() && course.getTeachingAssistant() != null)
                        staffName = course.getTeachingAssistant().getName();

                    rowData[c] = "<html><body style='text-align:center;vertical-align:middle;'>" +
                        "<span style='color:#0F172A;font-weight:bold;font-size:11px;'>" +
                        course.getCourseName() + " " + course.getCourseCode() + "</span><br>" +
                        "<span style='color:#475569;font-size:10px;'>" + staffName + "</span><br>" +
                        "<span style='color:#0EA5E9;font-weight:bold;font-size:10px;'>[" +
                        course.getRoomNumber() + "]</span></body></html>";
                }
            }
            termScheduleModel.addRow(rowData);
        }
    }

    // -------------------------------------------------------------------------
    // ONBOARD / OPERATIONS PANEL
    // -------------------------------------------------------------------------
    private JPanel createNewRegistryOnboardCenter() {
        JPanel dualPanelContainer = new JPanel(new GridLayout(1, 2, 25, 0));
        dualPanelContainer.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.weightx = 1.0;

        // ---- NEW STUDENT CARD ----
        JPanel newStudentCard = new JPanel(new GridBagLayout());
        newStudentCard.setBackground(COLOR_CARD_BG);
        newStudentCard.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            "Onboard Brand New Student",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), COLOR_TEXT_DARK));

        JTextField inputStudName  = new JTextField();
        JComboBox<String> comboStudSem  = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        JComboBox<String> comboStudDept = new JComboBox<>(new String[]{
            "Computer Science", "Information Systems", "Digital Media", "Artificial Intelligence"});
        JTextField inputStudPhone = new JTextField();
        JTextField inputStudEmail = new JTextField();
        JButton btnSaveNewStudent = new JButton("Register Profile Node");
        setupPrimaryButtonStyling(btnSaveNewStudent);

        gbc.gridx = 0; gbc.gridy = 0; newStudentCard.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; newStudentCard.add(inputStudName, gbc);
        gbc.gridx = 0; gbc.gridy = 1; newStudentCard.add(new JLabel("Semester Placed:"), gbc);
        gbc.gridx = 1; newStudentCard.add(comboStudSem, gbc);
        gbc.gridx = 0; gbc.gridy = 2; newStudentCard.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; newStudentCard.add(comboStudDept, gbc);
        gbc.gridx = 0; gbc.gridy = 3; newStudentCard.add(new JLabel("Phone number:"), gbc);
        gbc.gridx = 1; newStudentCard.add(inputStudPhone, gbc);
        gbc.gridx = 0; gbc.gridy = 4; newStudentCard.add(new JLabel("Email address:"), gbc);
        gbc.gridx = 1; newStudentCard.add(inputStudEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.insets = new Insets(15, 10, 10, 10);
        newStudentCard.add(btnSaveNewStudent, gbc);

        // ---- STAFF CARD ----
        JPanel staffCardPanel = new JPanel(new GridBagLayout());
        staffCardPanel.setBackground(COLOR_CARD_BG);
        staffCardPanel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            "Onboard Faculty Staff / Teaching Assistant",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), COLOR_TEXT_DARK));

        gbc.gridwidth = 1; gbc.insets = new Insets(6, 10, 6, 10);
        JTextField inputStaffName   = new JTextField();
        JComboBox<String> comboStaffType = new JComboBox<>(new String[]{
            "Professor Lecture Faculty", "Teaching Assistant (TA)"});
        JComboBox<String> comboStaffDept = new JComboBox<>(new String[]{
            "Computer Science", "Information Systems", "Digital Media", "Artificial Intelligence"});
        JTextField inputStaffOffice = new JTextField();
        JButton btnSaveStaff = new JButton("Induct Staff Member");
        setupPrimaryButtonStyling(btnSaveStaff);

        gbc.gridx = 0; gbc.gridy = 0; staffCardPanel.add(new JLabel("Staff Full Name:"), gbc);
        gbc.gridx = 1; staffCardPanel.add(inputStaffName, gbc);
        gbc.gridx = 0; gbc.gridy = 1; staffCardPanel.add(new JLabel("Role Classification:"), gbc);
        gbc.gridx = 1; staffCardPanel.add(comboStaffType, gbc);
        gbc.gridx = 0; gbc.gridy = 2; staffCardPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; staffCardPanel.add(comboStaffDept, gbc);
        gbc.gridx = 0; gbc.gridy = 3; staffCardPanel.add(new JLabel("Office Room Base:"), gbc);
        gbc.gridx = 1; staffCardPanel.add(inputStaffOffice, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(25, 10, 10, 10);
        staffCardPanel.add(btnSaveStaff, gbc);

        dualPanelContainer.add(newStudentCard);
        dualPanelContainer.add(staffCardPanel);

        btnSaveNewStudent.addActionListener(e -> {
            String name = inputStudName.getText().trim();
            if (name.isEmpty()) return;
            int generatedId = 202600 + (engine.getStudents().size() + 1);
            int targetSem   = Integer.parseInt(comboStudSem.getSelectedItem().toString());
            engine.addStudent(new Student(generatedId, name, targetSem, "Fall 2026", 3.40,
                comboStudDept.getSelectedItem().toString(), "Regular",
                inputStudPhone.getText(), inputStudEmail.getText()));
            inputStudName.setText(""); inputStudPhone.setText(""); inputStudEmail.setText("");
            refreshAllDataLayers();
        });

        btnSaveStaff.addActionListener(e -> {
            String name = inputStaffName.getText().trim();
            if (name.isEmpty()) return;
            String dept   = comboStaffDept.getSelectedItem().toString();
            String office = inputStaffOffice.getText().trim().isEmpty()
                ? "Lab-400" : inputStaffOffice.getText().trim();
            if (comboStaffType.getSelectedIndex() == 0) {
                engine.addProfessor(new Professor(
                    9000 + (engine.getProfessors().size() + 1), name, dept, "Professor", office));
            } else {
                engine.addTA(new TeachingAssistant(
                    5000 + (engine.getTAs().size() + 1), name, dept, office));
            }
            inputStaffName.setText(""); inputStaffOffice.setText("");
            refreshAllDataLayers();
        });

        return dualPanelContainer;
    }

    // -------------------------------------------------------------------------
    // DATA REFRESH
    // -------------------------------------------------------------------------
    private void refreshAllDataLayers() {
        // Students
        studentTableModel.setRowCount(0);
        double cumulativeGpaSum = 0;
        for (Student s : engine.getStudents()) {
            cumulativeGpaSum += s.getGpa();
            studentTableModel.addRow(new Object[]{
                s.getId(), s.getName(), s.getTerm(),
                "Semester " + s.getSemester(),
                String.format("%.2f", s.getGpa()),
                s.getDepartment()
            });
        }
        double finalAverageGpa = engine.getStudents().isEmpty()
            ? 0.0 : cumulativeGpaSum / engine.getStudents().size();

        // Faculty
        professorTableModel.setRowCount(0);
        for (Professor p : engine.getProfessors())
            professorTableModel.addRow(new Object[]{
                p.getId(), p.getName(), p.getAcademicRank(), p.getOfficeRoom(), p.getDepartment()});
        for (TeachingAssistant ta : engine.getTAs())
            professorTableModel.addRow(new Object[]{
                ta.getId(), ta.getName(), "Teaching Assistant", ta.getOfficeRoom(), ta.getDepartment()});

        // Courses
        courseTableModel.setRowCount(0);
        Map<String, Integer> roomBookingFrequencyMap = new HashMap<>();
        for (Course c : engine.getCourses()) {
            String instructor = (!c.isSection() && c.getProfessor() != null)
                ? c.getProfessor().getName()
                : (c.getTeachingAssistant() != null ? c.getTeachingAssistant().getName() : "Staff");
            roomBookingFrequencyMap.put(c.getRoomNumber(),
                roomBookingFrequencyMap.getOrDefault(c.getRoomNumber(), 0) + 1);
            courseTableModel.addRow(new Object[]{
                c.getCourseCode(), c.getCourseName(), c.getSemester(),
                "Track Option " + c.getScheduleTrackId(),
                c.isSection() ? "Section" : "Lecture",
                instructor, c.getDay(), c.getTimeSlot(), c.getRoomNumber()
            });
        }

        // Score cards
        lblTotalStudents.setText(
            "<html><body style='color:#F8FAFC;'>" +
            "<font size='3' color='#64748B'><b>TOTAL ACTIVE</b></font><br>" +
            "<font size='6' color='#0EA5E9'><b>" + engine.getStudents().size() + "</b></font><br><br>" +
            "<font size='2' color='#38BDF8'><b>LAUNCH DIRECTORY →</b></font></body></html>");

        lblTotalProfessors.setText(
            "<html><body style='color:#F8FAFC;'>" +
            "<font size='3' color='#64748B'><b>FACULTY & TAs</b></font><br>" +
            "<font size='6' color='#A855F7'><b>" +
            (engine.getProfessors().size() + engine.getTAs().size()) + "</b></font><br><br>" +
            "<font size='2' color='#C084FC'><b>ACCESS ROSTERS →</b></font></body></html>");

        lblTotalCourses.setText(
            "<html><body style='color:#F8FAFC;'>" +
            "<font size='3' color='#64748B'><b>COURSES & SECTIONS</b></font><br>" +
            "<font size='6' color='#10B981'><b>" + engine.getCourses().size() + "</b></font><br><br>" +
            "<font size='2' color='#34D399'><b>MAP CATALOGS →</b></font></body></html>");

        lblAverageGpa.setText(
            "<html><body style='color:#F8FAFC;'>" +
            "<font size='3' color='#64748B'><b>INSTITUTIONAL GPA</b></font><br>" +
            "<font size='6' color='#F59E0B'><b>" + String.format("%.2f", finalAverageGpa) + "</b></font><br><br>" +
            "<font size='2' color='#FBBF24'><b>SYSTEM NOMINAL</b></font></body></html>");

        // Analytics panes
        StringBuilder sbResource = new StringBuilder("\n  [FACULTY CAPACITY RESOURCE LOADING]\n\n");
        for (Professor p : engine.getProfessors())
            sbResource.append(String.format("  » [Prof] %-20s Loads: %d\n",
                p.getName(), p.getAssignedCourses().size()));
        txtResourceMetricsPane.setText(sbResource.toString());

        txtPerformanceAlertsPane.setText(
            "\n  [ANALYTIC STATUS NODE]\n\n" +
            "  » Simulation Tracks: Nominal\n" +
            "  » Multi-Schedules active: True");

        StringBuilder sbCap = new StringBuilder("\n  [ROOM ASSIGNMENTS FREQUENCY]\n\n");
        for (Map.Entry<String, Integer> entry : roomBookingFrequencyMap.entrySet())
            sbCap.append(String.format("  • %-15s Allocated %d classes\n",
                entry.getKey(), entry.getValue()));
        txtCapacityMapsPane.setText(sbCap.toString());

        updateTermScheduleMatrixGrid();
    }

    // -------------------------------------------------------------------------
    // UI HELPER FACTORIES
    // -------------------------------------------------------------------------
    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(148, 163, 184));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    private JButton createActionNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(COLOR_ACCENT);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        return button;
    }

    private JPanel createTabularContainerPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(COLOR_CARD_BG);
        outerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return outerPanel;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(241, 245, 249));
        table.setShowVerticalLines(false);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setBackground(COLOR_BG_LIGHT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        return table;
    }

    private void setupPrimaryButtonStyling(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_ACCENT);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
    }

    // ============================================================================
    // DATA SEED PIPELINE
    // ============================================================================
    static class DataSeedPipeline {
        public static void populate(AcademiaEngine eng) {
            String[] dp = {
                "Computer Science", "Information Systems", "Digital Media", "Artificial Intelligence"};

            Professor[] profs = {
                new Professor(101, "Dr. Adel Emam",          dp[0], "Full Professor",        "Hall-Room-301"),
                new Professor(102, "Dr. Mohamed Salah",      dp[3], "Research Chair",         "Hall-Room-302"),
                new Professor(103, "Dr. Mohamed Ramadan",    dp[1], "Associate Professor",    "Hall-Room-303"),
                new Professor(104, "Dr. Yehia El-Fakharany", dp[0], "Senior Faculty",         "Hall-Room-304"),
                new Professor(105, "Dr. Ahmed Helmy",        dp[2], "Department Head",        "Hall-Room-305"),
                new Professor(106, "Dr. Yousra Ibrahim",     dp[2], "Professor Emeritus",     "Hall-Room-401"),
                new Professor(107, "Dr. Amr Diab",           dp[3], "Distinguished Lecturer", "Hall-Room-402"),
                new Professor(108, "Dr. Mervat Amin",        dp[1], "Senior Academic",        "Hall-Room-403")
            };
            for (Professor p : profs) eng.addProfessor(p);

            TeachingAssistant[] tas = {
                new TeachingAssistant(201, "Eng. Maged El-Kedwany",  dp[0], "TA-Lab-101"),
                new TeachingAssistant(202, "Eng. Akram Hosny",        dp[1], "TA-Lab-102"),
                new TeachingAssistant(203, "Eng. Karim Abdel Aziz",  dp[2], "TA-Lab-103"),
                new TeachingAssistant(204, "Eng. Tamer Hosny",        dp[3], "TA-Lab-104"),
                new TeachingAssistant(205, "Eng. Ahmed Mekky",        dp[0], "TA-Lab-105")
            };
            for (TeachingAssistant ta : tas) eng.addTA(ta);

            String[][] studentData = {
                {"202601", "Ahmed Mansour",  "1", "3.85", dp[0]},
                {"202602", "Mina Shenouda",  "1", "3.91", dp[0]},
                {"202603", "Youssef Ibrahim","2", "2.15", dp[1]},
                {"202604", "Mariam Hassan",  "2", "3.72", dp[2]},
                {"202605", "Fatma Ali",       "3", "1.88", dp[3]},
                {"202606", "Omar Sherif",     "3", "3.55", dp[0]},
                {"202607", "Nour El-Din",     "4", "2.95", dp[1]},
                {"202608", "Layla Khaled",    "5", "3.40", dp[2]},
                {"202609", "Hassan Kamal",    "6", "3.10", dp[3]},
                {"202610", "Salma Yasser",    "7", "3.95", dp[0]}
            };
            for (String[] s : studentData) {
                eng.addStudent(new Student(
                    Integer.parseInt(s[0]), s[1], Integer.parseInt(s[2]),
                    "Fall 2026", Double.parseDouble(s[3]), s[4],
                    "Regular", "01000", "stud@edu.eg"));
            }

            Course c1 = new Course("CS-101", "Programming I",        "Monday",    "8AM",  "Hall-301", 1, 1, false);
            profs[0].assignCourse(c1); eng.addCourse(c1);

            Course c2 = new Course("AI-101", "Neural Basics",         "Wednesday", "10AM", "Hall-302", 1, 1, false);
            profs[1].assignCourse(c2); eng.addCourse(c2);

            Course c3 = new Course("IS-404", "Enterprise Systems",    "Tuesday",   "12PM", "Hall-403", 4, 1, false);
            profs[7].assignCourse(c3); eng.addCourse(c3);

            Course c4 = new Course("DM-303", "Digital Cinematography","Friday",    "4PM",  "Studio-1", 3, 2, false);
            profs[5].assignCourse(c4); eng.addCourse(c4);

            Course c5 = new Course("AI-505", "Advanced Robotics",     "Thursday",  "2PM",  "Lab-205",  5, 1, false);
            profs[6].assignCourse(c5); eng.addCourse(c5);

            Course c6 = new Course("CS-202", "Systems Architecture",  "Monday",    "10AM", "Hall-301", 2, 1, false);
            profs[0].assignCourse(c6); eng.addCourse(c6);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AcademiaApp().setVisible(true));
    }
}