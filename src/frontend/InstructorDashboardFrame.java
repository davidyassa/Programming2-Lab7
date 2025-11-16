package frontend;

import backend.*;
import controller.CourseService;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;


public class InstructorDashboardFrame extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(InstructorDashboardFrame.class.getName());

    private Instructor currentInstructor;
    private CourseService courseService;
    

    public InstructorDashboardFrame(Instructor instructor) {
        this.currentInstructor = instructor;
        this.courseService = new CourseService();

        initComponents();

        customInit();
    }

        JLabel welcomeLabel = new JLabel("Welcome to the instructor Dashboard!");
        welcomeLabel.setBounds(200, 100, 400, 50);
        add(welcomeLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(300, 300, 150, 40);
        add(logoutButton);

         logoutButton.addActionListener(e -> frame.showMainMenu());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void initComponents() {

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 400, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 300, Short.MAX_VALUE)
    );

    pack();
}// </editor-fold>                        

    private void customInit() {

    loadCoursesData();
}

    private void loadCoursesData() {
    logger.info("Loading courses for instructor: " + currentInstructor.getUsername());

}

    private void handleCreateCourse() {

    String title = JOptionPane.showInputDialog(this, "Enter Course Title:");
    String description = JOptionPane.showInputDialog(this, "Enter Course Description:");

    if (title != null && description != null) {
        try {
            courseService.createCourse(currentInstructor, title, description);
            JOptionPane.showMessageDialog(this, "Course created successfully!");
            loadCoursesData(); // Refresh the display
        } catch (Exception ex) {
            logger.severe("Error creating course: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error creating course.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
