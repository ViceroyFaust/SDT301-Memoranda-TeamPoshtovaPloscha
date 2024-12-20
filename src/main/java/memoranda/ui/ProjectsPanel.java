package memoranda.ui;

import memoranda.*;
import memoranda.busSchedule.ApplicationContext;
import memoranda.busSchedule.models.*;
import memoranda.date.CalendarDate;
import memoranda.date.CurrentDate;
import memoranda.date.DateListener;
import memoranda.util.Context;
import memoranda.util.CurrentStorage;
import memoranda.util.Local;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

/*$Id: ProjectsPanel.java,v 1.14 2005/01/04 09:59:22 pbielen Exp $*/
public class ProjectsPanel extends JPanel implements ExpandablePanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JToolBar topBar = new JToolBar();
    JPanel toolbarPanel = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JPanel buttonsPanel = new JPanel();
    JButton toggleButton = new JButton();
    FlowLayout flowLayout1 = new FlowLayout();
    Vector expListeners = new Vector();
    boolean expanded = false;
    ImageIcon expIcon =
            new ImageIcon(
                    memoranda.ui.AppFrame.class.getResource(
                            "/ui/icons/exp_panel.png"));
    ImageIcon collIcon =
            new ImageIcon(
                    memoranda.ui.AppFrame.class.getResource(
                            "/ui/icons/coll_panel.png"));
    JLabel curProjectTitle = new JLabel();
    Component component1;
    JPopupMenu projectsPPMenu = new JPopupMenu();
    JMenuItem ppNewProject = new JMenuItem();
    JMenuItem ppProperties = new JMenuItem();
    JMenuItem ppDeleteProject = new JMenuItem();
    JMenuItem ppOpenProject = new JMenuItem();
    JCheckBoxMenuItem ppShowActiveOnlyChB = new JCheckBoxMenuItem();
    JButton ppOpenB = new JButton();
    ProjectsTablePanel prjTablePanel = new ProjectsTablePanel();

    public Action newProjectAction =
            new AbstractAction(
                    Local.getString("New project") + "...",
                    new ImageIcon(
                            memoranda.ui.AppFrame.class.getResource(
                                    "/ui/icons/newproject.png"))) {

                public void actionPerformed(ActionEvent e) {
                    ppNewProject_actionPerformed(e);
                }
            };

    // Save action. Save panes and chose file.
    public Action saveAction = new AbstractAction("Save models", getResizedIcon(
            "/ui/icons/saveIcon.png", 16, 16)) {

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(ProjectsPanel.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    ApplicationContext.getInstance().save(file.toString());
                } catch (IOException ex) {
                    System.out.println("IO ERROR: Could not save models to xml file");
                }
            }
        }
    };

    public Action loadAction = new AbstractAction("Load models") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = new JFileChooser();
            int dialogReturnStatus = fileChooser.showOpenDialog(ProjectsPanel.this);
            if (dialogReturnStatus == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ApplicationContext.getInstance().load(file.toString());
            }
        }
    };

    public Action showNodeAction = new AbstractAction("View Nodes") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StringBuilder sb = new StringBuilder();
            for (Node node : ApplicationContext.getInstance().nodes.getAll().values()) {
                sb.append(node).append("\n");
            }
            JOptionPane.showMessageDialog(ProjectsPanel.this, sb.toString(), "Nodes",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public Action showDriverAction = new AbstractAction("View Drivers") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StringBuilder sb = new StringBuilder();
            for (Driver driver : ApplicationContext.getInstance().drivers.getAll().values()) {
                sb.append(driver).append("\n");
            }
            JOptionPane.showMessageDialog(ProjectsPanel.this, sb.toString(), "Drivers",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public Action showBusesAction = new AbstractAction("View Buses") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StringBuilder sb = new StringBuilder();
            for (Bus bus : ApplicationContext.getInstance().buses.getAll().values()) {
                sb.append(bus).append("\n");
            }
            JOptionPane.showMessageDialog(ProjectsPanel.this, sb.toString(), "Buses",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public Action showRoutesAction = new AbstractAction("View Routes") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StringBuilder sb = new StringBuilder();
            for (Route route : ApplicationContext.getInstance().routes.getAll().values()) {
                sb.append(route).append("\n");
            }
            JOptionPane.showMessageDialog(ProjectsPanel.this, sb.toString(), "Routes",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public Action showToursAction = new AbstractAction("View Tours") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StringBuilder sb = new StringBuilder();
            for (Tour tour : ApplicationContext.getInstance().tours.getAll().values()) {
                sb.append(tour).append("\n");
            }
            JOptionPane.showMessageDialog(ProjectsPanel.this, sb.toString(), "Tours",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    public Action createDriverAction = new AbstractAction("Create Driver") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object[] options = {"Create Driver", "Cancel"};
            JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            dialogPanel.add(new JLabel("Driver Name:"));
            dialogPanel.add(nameField);
            dialogPanel.add(new JLabel("Phone Number:"));
            dialogPanel.add(phoneField);

            int n = JOptionPane.showOptionDialog(ProjectsPanel.this, dialogPanel, "Create Driver",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]);

            if (n == 0) {
                ApplicationContext.getInstance().drivers.add(new Driver(nameField.getText(), phoneField.getText()));
            }
        }
    };

    public Action createBusAction = new AbstractAction("Create Bus") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ApplicationContext context = ApplicationContext.getInstance();

            Object[] options = {"Create Bus", "Cancel"};
            JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
            SpinnerModel numberSpinnerModel = new SpinnerNumberModel(0, 0, 256, 1);
            JSpinner seatsSpinner = new JSpinner(numberSpinnerModel);

            String defaultComboChoice = "--- Make a Selection ---";
            JComboBox<Object> driversBox = new JComboBox<>();
            driversBox.addItem(defaultComboChoice);
            for (Driver driver : context.drivers.getAll().values()) {
                driversBox.addItem(driver);
            }

            dialogPanel.add(new JLabel("Number of Seats:"));
            dialogPanel.add(seatsSpinner);
            dialogPanel.add(new JLabel("Driver:"));
            dialogPanel.add(driversBox);

            int n = JOptionPane.showOptionDialog(ProjectsPanel.this, dialogPanel, "Create Bus",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (n == 0 && driversBox.getSelectedIndex() != 0) {
                context.buses.add(new Bus((int) seatsSpinner.getValue(), (Driver) driversBox.getSelectedItem()));
            }
        }
    };

    public Action createRouteAction = new AbstractAction("Create Route") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ApplicationContext context = ApplicationContext.getInstance();

            Object[] options = {"Create Route", "Cancel"};

            // Main Panel with BoxLayout
            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
            dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Selection Section
            JPanel selectionPanel = new JPanel(new BorderLayout());
            selectionPanel.setBorder(BorderFactory.createTitledBorder("Available Nodes"));

            String defaultListChoice = "--- Make Selection ---";
            DefaultListModel<Object> listModel = new DefaultListModel<>();
            listModel.addElement(defaultListChoice);
            for (Node node : context.nodes.getAll().values()) {
                listModel.addElement(node);
            }
            JList<Object> list = new JList<>(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPaneSelect = new JScrollPane(list);
            selectionPanel.add(scrollPaneSelect, BorderLayout.CENTER);

            // Selected Nodes Section
            JPanel selectedPanel = new JPanel(new BorderLayout());
            selectedPanel.setBorder(BorderFactory.createTitledBorder("Selected Nodes"));

            DefaultListModel<Object> selectionListModel = new DefaultListModel<>();
            JList<Object> selectionList = new JList<>(selectionListModel);
            JScrollPane scrollPaneSelected = new JScrollPane(selectionList);
            selectedPanel.add(scrollPaneSelected, BorderLayout.CENTER);

            // Button Panel for Selection
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            JButton selectButton = new JButton("Add Node");
            selectButton.addActionListener(e -> {
                Object selectedValue = list.getSelectedValue();
                if (selectedValue != null && !selectedValue.equals(defaultListChoice)) {
                    selectionListModel.addElement(selectedValue);
                    listModel.removeElement(selectedValue);
                }
            });
            JButton removeButton = new JButton("Remove Node");
            removeButton.addActionListener(e -> {
                Object selectedValue = selectionList.getSelectedValue();
                if (selectedValue != null) {
                    listModel.addElement(selectedValue);
                    selectionListModel.removeElement(selectedValue);
                }
            });
            buttonPanel.add(selectButton);
            buttonPanel.add(removeButton);

            // Add components to main dialog
            dialogPanel.add(selectionPanel);
            dialogPanel.add(Box.createVerticalStrut(10));
            dialogPanel.add(selectedPanel);
            dialogPanel.add(Box.createVerticalStrut(10));
            dialogPanel.add(buttonPanel);

            // Show Dialog
            int n = JOptionPane.showOptionDialog(ProjectsPanel.this, dialogPanel, "Create Route",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n == JOptionPane.YES_OPTION) {
                if (selectionListModel.contains(defaultListChoice)) {
                    return;
                }
                LinkedList<Node> nodes = new LinkedList<>();
                for (Object nodeObject : selectionListModel.toArray()) {
                    nodes.add((Node) nodeObject);
                }
                context.routes.add(new Route(nodes));
            }
        }
    };


    public Action createTourAction = new AbstractAction("Create Tour") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ApplicationContext context = ApplicationContext.getInstance();

            Object[] options = {"Create Tour", "Cancel"};
            JPanel dialogPanel = new JPanel(new GridLayout(0, 1));

            String defaultSelectionMessage = "--- Make Selection ---";
            JTextField nameField = new JTextField();

            JComboBox<Object> busCombo = new JComboBox<>();
            busCombo.addItem(defaultSelectionMessage);
            for (Bus bus : context.buses.getAll().values()) {
                busCombo.addItem(bus);
            }

            JComboBox<Object> routeCombo = new JComboBox<>();
            routeCombo.addItem(defaultSelectionMessage);
            for (Route route : context.routes.getAll().values()) {
                routeCombo.addItem(route);
            }

            dialogPanel.add(new JLabel("Tour Name:"));
            dialogPanel.add(nameField);
            dialogPanel.add(new JLabel("Tour Bus:"));
            dialogPanel.add(busCombo);
            dialogPanel.add(new JLabel("Tour Route:"));
            dialogPanel.add(routeCombo);

            int n = JOptionPane.showOptionDialog(ProjectsPanel.this, dialogPanel, "Create Tour",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n == 0) {
                if (nameField.getText().isEmpty() || busCombo.getSelectedIndex() == 0 ||
                        routeCombo.getSelectedIndex() == 0) {
                    return;
                }
                context.tours.add(new Tour(nameField.getText(), (Bus) busCombo.getSelectedItem(),
                        (Route) routeCombo.getSelectedItem()));
            }
        }
    };

    public Action createNodeAction = new AbstractAction("Create Node") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object[] options = {"Create Node", "Cancel"};
            int n = JOptionPane.showOptionDialog(ProjectsPanel.this, "Create Node?",
                    "Node Creation",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n == 0) {
                ApplicationContext.getInstance().nodes.add(new Node());
            }
        }
    };

    // Method to resize icon. Couldnt find previous resize method.
    // It is not on this branch.
    private ImageIcon getResizedIcon(String resourcePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(
                memoranda.ui.AppFrame.class.getResource(resourcePath)
        );
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }


    public ProjectsPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    void jbInit() throws Exception {
        component1 = Box.createHorizontalStrut(20);
        this.setLayout(borderLayout1);
        topBar.setBackground(new Color(215, 225, 250));
        topBar.setAlignmentX((float) 0.0);
        topBar.setFloatable(false);
        toolbarPanel.setLayout(borderLayout2);
        toggleButton.setMaximumSize(new Dimension(20, 20));
        toggleButton.setMinimumSize(new Dimension(20, 20));
        toggleButton.setOpaque(false);
        toggleButton.setPreferredSize(new Dimension(20, 20));
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setVerticalAlignment(SwingConstants.TOP);
        toggleButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleButton_actionPerformed(null);
            }
        });

        toggleButton.setIcon(expIcon);
        toggleButton.setMargin(new Insets(0, 0, 0, 0));
        buttonsPanel.setMinimumSize(new Dimension(70, 22));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setPreferredSize(new Dimension(80, 22));
        buttonsPanel.setRequestFocusEnabled(false);
        buttonsPanel.setLayout(flowLayout1);
        toolbarPanel.setBackground(SystemColor.textHighlight);
        toolbarPanel.setMinimumSize(new Dimension(91, 22));
        toolbarPanel.setOpaque(false);
        toolbarPanel.setPreferredSize(new Dimension(91, 22));
        flowLayout1.setAlignment(FlowLayout.RIGHT);
        flowLayout1.setHgap(0);
        flowLayout1.setVgap(0);

        curProjectTitle.setFont(new java.awt.Font("Dialog", 1, 11));
        curProjectTitle.setForeground(new Color(64, 70, 128));
        curProjectTitle.setMaximumSize(new Dimension(32767, 22));
        curProjectTitle.setPreferredSize(new Dimension(32767, 22));
        curProjectTitle.setText(CurrentProject.get().getTitle());
        curProjectTitle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                toggleButton_actionPerformed(null);
            }
        });

        /*
         * buttonsPanel.add(newProjectB, null); buttonsPanel.add(editProjectB,
         * null);
         */
        ppNewProject.setFont(new java.awt.Font("Dialog", 1, 11));
        ppNewProject.setAction(newProjectAction);

        ppProperties.setFont(new java.awt.Font("Dialog", 1, 11));
        ppProperties.setText(Local.getString("Project properties"));
        ppProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppProperties_actionPerformed(e);
            }
        });
        ppProperties.setIcon(
                new ImageIcon(
                        memoranda.ui.AppFrame.class.getResource(
                                "/ui/icons/editproject.png")));
        ppProperties.setEnabled(false);
        ppDeleteProject.setFont(new java.awt.Font("Dialog", 1, 11));
        ppDeleteProject.setText(Local.getString("Delete project"));
        ppDeleteProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppDeleteProject_actionPerformed(e);
            }
        });
        ppDeleteProject.setIcon(
                new ImageIcon(
                        memoranda.ui.AppFrame.class.getResource(
                                "/ui/icons/removeproject.png")));
        ppDeleteProject.setEnabled(false);

        ppOpenProject.setFont(new java.awt.Font("Dialog", 1, 11));

        ppOpenProject.setText(" " + Local.getString("Open project"));

        ppOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppOpenProject_actionPerformed(e);
            }
        });
        ppOpenProject.setEnabled(false);

        ppShowActiveOnlyChB.setFont(new java.awt.Font("Dialog", 1, 11));
        ppShowActiveOnlyChB.setText(
                Local.getString("Show active projects only"));
        ppShowActiveOnlyChB
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ppShowActiveOnlyChB_actionPerformed(e);
                    }
                });
        boolean isShao =
                (Context.get("SHOW_ACTIVE_PROJECTS_ONLY") != null)
                        && (Context.get("SHOW_ACTIVE_PROJECTS_ONLY").equals("true"));
        ppShowActiveOnlyChB.setSelected(isShao);
        ppShowActiveOnlyChB_actionPerformed(null);

        projectsPPMenu.setFont(new java.awt.Font("Dialog", 1, 10));
        ppOpenB.setMaximumSize(new Dimension(34, 20));
        ppOpenB.setMinimumSize(new Dimension(24, 10));
        ppOpenB.setOpaque(false);
        ppOpenB.setPreferredSize(new Dimension(24, 20));
        ppOpenB.setBorderPainted(false);
        ppOpenB.setFocusPainted(false);
        ppOpenB.setMargin(new Insets(0, 0, 0, 0));
        ppOpenB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppOpenB_actionPerformed(e);
            }
        });
        ppOpenB.setIcon(
                new ImageIcon(
                        memoranda.ui.AppFrame.class.getResource(
                                "/ui/icons/ppopen.png")));
        buttonsPanel.add(ppOpenB, null);
        buttonsPanel.add(component1, null);
        this.add(topBar, BorderLayout.NORTH);
        this.add(prjTablePanel, BorderLayout.CENTER);
        topBar.add(toolbarPanel, null);
        toolbarPanel.add(buttonsPanel, BorderLayout.EAST);
        buttonsPanel.add(toggleButton, null);
        toolbarPanel.add(curProjectTitle, BorderLayout.CENTER);
        projectsPPMenu.add(ppOpenProject);
        projectsPPMenu.addSeparator();
        projectsPPMenu.add(ppNewProject);
        projectsPPMenu.add(ppDeleteProject);
        projectsPPMenu.addSeparator();
        projectsPPMenu.add(ppProperties);
        projectsPPMenu.addSeparator();
        projectsPPMenu.add(ppShowActiveOnlyChB);
        CurrentProject.addProjectListener(new ProjectListener() {
            public void projectChange(
                    Project p,
                    NoteList nl,
                    TaskList tl,
                    ResourcesList rl) {
            }

            public void projectWasChanged() {
                curProjectTitle.setText(CurrentProject.get().getTitle());
                prjTablePanel.updateUI();
            }
        });
        CurrentDate.addDateListener(new DateListener() {
            public void dateChange(CalendarDate d) {
                prjTablePanel.updateUI();
            }
        });
        prjTablePanel.projectsTable.addMouseListener(new PopupListener());
        prjTablePanel
                .projectsTable
                .getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        boolean enabled =
                                !prjTablePanel
                                        .projectsTable
                                        .getModel()
                                        .getValueAt(
                                                prjTablePanel.projectsTable.getSelectedRow(),
                                                ProjectsTablePanel.PROJECT_ID)
                                        .toString()
                                        .equals(CurrentProject.get().getID());
                        ppDeleteProject.setEnabled(enabled);
                        ppOpenProject.setEnabled(enabled);
                        ppProperties.setEnabled(true);
                    }
                });
        prjTablePanel.projectsTable.setToolTipText(
                Local.getString("Double-click to set a current project"));

        // delete projects using the DEL kew
        prjTablePanel.projectsTable.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (prjTablePanel.projectsTable.getSelectedRows().length > 0
                        && e.getKeyCode() == KeyEvent.VK_DELETE)
                    ppDeleteProject_actionPerformed(null);
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
    }

    class PopupListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2)
                ppOpenProject_actionPerformed(null);
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                projectsPPMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    void toggleButton_actionPerformed(ActionEvent e) {
        for (int i = 0; i < expListeners.size(); i++)
            ((ActionListener) expListeners.get(i)).actionPerformed(
                    new ActionEvent(this, 0, "Panel expanded (collapsed)"));
        if (expanded) {
            expanded = false;
            toggleButton.setIcon(expIcon);
        } else {
            expanded = true;
            toggleButton.setIcon(collIcon);
        }
    }

    public void AddExpandListener(ActionListener al) {
        expListeners.add(al);
    }

    void ppOpenB_actionPerformed(ActionEvent e) {
        projectsPPMenu.show(
                buttonsPanel,
                (int) (ppOpenB.getLocation().getX() + 24)
                        - projectsPPMenu.getWidth(),
                (int) ppOpenB.getLocation().getY() + 24);
    }

    void ppOpenProject_actionPerformed(ActionEvent e) {
        CurrentProject.set(prjTablePanel.getSelectedProject());
        prjTablePanel.updateUI();
        ppDeleteProject.setEnabled(false);
        ppOpenProject.setEnabled(false);
    }

    void ppNewProject_actionPerformed(ActionEvent e) {
        ProjectDialog.newProject();
        prjTablePanel.updateUI();
    }

    void ppDeleteProject_actionPerformed(ActionEvent e) {
        String msg;
        Project prj;
        Vector toremove = new Vector();
        if (prjTablePanel.projectsTable.getSelectedRows().length > 1)
            msg =
                    Local.getString("Delete")
                            + " "
                            + prjTablePanel.projectsTable.getSelectedRows().length
                            + " "
                            + Local.getString("projects")
                            + "\n"
                            + Local.getString("Are you sure?");
        else {
            prj = prjTablePanel.getSelectedProject();
            msg =
                    Local.getString("Delete project")
                            + " '"
                            + prj.getTitle()
                            + "'.\n"
                            + Local.getString("Are you sure?");
        }

        int n =
                JOptionPane.showConfirmDialog(
                        App.getFrame(),
                        msg,
                        Local.getString("Delete project"),
                        JOptionPane.YES_NO_OPTION);
        if (n != JOptionPane.YES_OPTION)
            return;

        for (int i = 0;
             i < prjTablePanel.projectsTable.getSelectedRows().length;
             i++) {
            prj =
                    (memoranda.Project) prjTablePanel
                            .projectsTable
                            .getModel()
                            .getValueAt(
                                    prjTablePanel.projectsTable.getSelectedRows()[i],
                                    ProjectsTablePanel.PROJECT);
            toremove.add(prj.getID());
        }
        for (int i = 0; i < toremove.size(); i++) {
            ProjectManager.removeProject((String) toremove.get(i));
        }
        CurrentStorage.get().storeProjectManager();
        prjTablePanel.projectsTable.clearSelection();
        prjTablePanel.updateUI();
        setMenuEnabled(false);
    }

    void ppProperties_actionPerformed(ActionEvent e) {
        Project prj = prjTablePanel.getSelectedProject();
        ProjectDialog dlg =
                new ProjectDialog(null, Local.getString("Project properties"));
        Dimension dlgSize = dlg.getSize();
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dlg.setLocation(
                (frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.prTitleField.setText(prj.getTitle());
        dlg.startDate.getModel().setValue(
                prj.getStartDate().getCalendar().getTime());
        if (prj.getEndDate() != null) {
            dlg.edButton.setEnabled(true);
            dlg.endDateChB.setForeground(Color.BLACK);

            dlg.endDateChB.setSelected(true);
            dlg.endDate.setEnabled(true);
            dlg.endDate.getModel().setValue(
                    prj.getEndDate().getCalendar().getTime());
        }
		/*if (prj.getStatus() == Project.FROZEN)
			dlg.freezeChB.setSelected(true);*/
        dlg.setVisible(true);
        if (dlg.CANCELLED)
            return;
        prj.setTitle(dlg.prTitleField.getText());
        prj.setStartDate(
                new CalendarDate((Date) dlg.startDate.getModel().getValue()));

        if (dlg.endDateChB.isSelected())
            prj.setEndDate(
                    new CalendarDate((Date) dlg.endDate.getModel().getValue()));
        else
            prj.setEndDate(null);
        prjTablePanel.updateUI();
        /*
         * if (dlg.freezeChB.isSelected()) prj.freeze(); else
         */
    }

    void ppShowActiveOnlyChB_actionPerformed(ActionEvent e) {
        prjTablePanel.setShowActiveOnly(ppShowActiveOnlyChB.isSelected());
        Context.put(
                "SHOW_ACTIVE_PROJECTS_ONLY",
                Boolean.valueOf(ppShowActiveOnlyChB.isSelected()));
    }

    void setMenuEnabled(boolean enabled) {
        ppDeleteProject.setEnabled(enabled);
        ppOpenProject.setEnabled(enabled);
        ppProperties.setEnabled(enabled);
    }

}