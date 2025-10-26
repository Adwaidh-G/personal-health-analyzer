package ui;

import model.User;
import model.NutritionRecommendation;
import service.CalorieCalculator;
import service.IntakeCalculator;
import dao.DataManager;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private JTextField heightField;
    private JTextField weightField;
    private JTextArea outputArea;
    private JTable foodTable;
    private DefaultTableModel tableModel;
    private JButton calculateBMIButton, addRowButton, removeRowButton, calculateIntakeButton;
    private CalorieCalculator calculator;
    private JLabel bmiValueLabel, bmiCategoryLabel;
    private JPanel mainContentArea;
    private CardLayout cardLayout;

    // Color scheme
    private final Color SIDEBAR_BG = new Color(30, 41, 59);
    private final Color SIDEBAR_HOVER = new Color(51, 65, 85);
    private final Color MAIN_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private final Color DANGER_COLOR = new Color(239, 68, 68);
    private final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);

    public MainFrame() {
        super("Health Analyzer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);

        calculator = new CalorieCalculator();

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Create sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Create main content area with CardLayout
        cardLayout = new CardLayout();
        mainContentArea = new JPanel(cardLayout);
        mainContentArea.setBackground(MAIN_BG);

        // Add different pages
        mainContentArea.add(createDashboardPage(), "Dashboard");
        mainContentArea.add(createNutritionTrackerPage(), "Nutrition Tracker");
        mainContentArea.add(createMealPlannerPage(), "Meal Planner");
        mainContentArea.add(createReportsPage(), "Reports");
        mainContentArea.add(createPlaceholderPage("Settings"), "Settings");

        add(mainContentArea, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Logo/Title with icon
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setMaximumSize(new Dimension(250, 80));

        JLabel iconLabel = new JLabel("❤️");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));

        JLabel titleLabel = new JLabel("Health Analyzer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        logoPanel.add(iconLabel);
        logoPanel.add(titleLabel);
        sidebar.add(logoPanel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Menu items
        String[] menuItems = {"Dashboard", "Nutrition Tracker", "Meal Planner", "Reports", "Settings"};
        String[] icons = {"📊", "🍽️", "📋", "📈", "⚙️"};

        for (int i = 0; i < menuItems.length; i++) {
            final String pageName = menuItems[i];
            JPanel menuItem = createMenuItem(icons[i] + "  " + menuItems[i], i == 0);
            menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cardLayout.show(mainContentArea, pageName);
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    menuItem.setBackground(SIDEBAR_HOVER);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    // Reset to normal background unless it's the selected page
                }
            });
            sidebar.add(menuItem);
        }

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JPanel createMenuItem(String text, boolean selected) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        item.setMaximumSize(new Dimension(250, 45));
        item.setBackground(selected ? SIDEBAR_HOVER : SIDEBAR_BG);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(12, 20, 12, 20));
        item.add(label);

        return item;
    }

    private JPanel createDashboardPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(MAIN_BG);
        page.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header
        JPanel header = createHeader();
        page.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(MAIN_BG);

        // Dashboard title
        JLabel dashTitle = new JLabel("Dashboard Overview");
        dashTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        dashTitle.setForeground(TEXT_PRIMARY);
        dashTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(dashTitle);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        // Health metrics and nutrition goals
        JPanel topRow = new JPanel(new GridLayout(1, 2, 20, 0));
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        topRow.setBackground(MAIN_BG);
        topRow.add(createHealthMetricsCard());
        topRow.add(createNutritionGoalsCard());
        content.add(topRow);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scrollPane, BorderLayout.CENTER);

        return page;
    }

    private JPanel createNutritionTrackerPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(MAIN_BG);
        page.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header
        JPanel header = createHeader();
        page.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(MAIN_BG);

        // Page title
        JLabel pageTitle = new JLabel("Nutrition Tracker");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        pageTitle.setForeground(TEXT_PRIMARY);
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(pageTitle);
        content.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitle = new JLabel("Track your meals and calculate your daily macro intake");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        // Food intake card
        JPanel foodCard = createFoodIntakeCard();
        content.add(foodCard);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scrollPane, BorderLayout.CENTER);

        return page;
    }

    private JPanel createMealPlannerPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(MAIN_BG);
        page.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header
        JPanel header = createHeader();
        page.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(MAIN_BG);

        JLabel plannerTitle = new JLabel("Meal Planner");
        plannerTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        plannerTitle.setForeground(TEXT_PRIMARY);
        plannerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(plannerTitle);
        content.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitle = new JLabel("Plan your meals for the week and organize your nutrition goals");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        // Meal planner card
        JPanel mealCard = createMealPlannerCard();
        content.add(mealCard);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scrollPane, BorderLayout.CENTER);

        return page;
    }

    private JPanel createMealPlannerCard() {
        JPanel card = new JPanel(new BorderLayout(0, 20));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);

        JLabel title = new JLabel("Weekly Meal Plan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        headerPanel.add(title, BorderLayout.WEST);

        card.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Day", "Meal Time", "Food Items", "Quantity"};
        DefaultTableModel mealTableModel = new DefaultTableModel(columns, 0);

        JTable mealTable = new JTable(mealTableModel);
        mealTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mealTable.setRowHeight(40);
        mealTable.setShowVerticalLines(false);
        mealTable.setGridColor(new Color(241, 245, 249));
        mealTable.setSelectionBackground(new Color(239, 246, 255));
        mealTable.setSelectionForeground(TEXT_PRIMARY);

        JTableHeader header = mealTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        JScrollPane scrollPane = new JScrollPane(mealTable);
        scrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1));
        scrollPane.setPreferredSize(new Dimension(0, 300));
        card.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(CARD_BG);

        JButton addMealBtn = new JButton("+ Add Meal");
        styleButton(addMealBtn, SUCCESS_COLOR);
        addMealBtn.setPreferredSize(new Dimension(130, 38));

        JButton removeMealBtn = new JButton("Remove");
        styleButton(removeMealBtn, DANGER_COLOR);
        removeMealBtn.setPreferredSize(new Dimension(120, 38));

        buttonPanel.add(addMealBtn);
        buttonPanel.add(removeMealBtn);

        card.add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        addMealBtn.addActionListener(e -> mealTableModel.addRow(new Object[]{"", "", "", ""}));
        removeMealBtn.addActionListener(e -> {
            int row = mealTable.getSelectedRow();
            if (row >= 0) mealTableModel.removeRow(row);
            else JOptionPane.showMessageDialog(this, "Please select a row to remove.");
        });

        return card;
    }

    private JPanel createReportsPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(MAIN_BG);
        page.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel header = createHeader();
        page.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(MAIN_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel reportTitle = new JLabel("Reports & Analytics");
        reportTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        reportTitle.setForeground(TEXT_PRIMARY);
        reportTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(reportTitle);
        content.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitle = new JLabel("View your nutrition trends and export reports");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        // Reports card
        JPanel reportsCard = createReportsCard();
        content.add(reportsCard);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scrollPane, BorderLayout.CENTER);

        return page;
    }

    private JPanel createReportsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JLabel title = new JLabel("Nutrition Summary");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel info = new JLabel("<html>Track your calorie and macronutrient trends over time.<br>" +
                "Export your food journal and BMI history as a detailed report.<br><br>" +
                "Coming soon: Charts, graphs, and downloadable PDF reports.</html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(TEXT_SECONDARY);
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(info);

        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton exportBtn = new JButton("Export Report");
        styleButton(exportBtn, PRIMARY_COLOR);
        exportBtn.setPreferredSize(new Dimension(150, 40));
        exportBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        exportBtn.setMaximumSize(new Dimension(150, 40));
        exportBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Export feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE)
        );
        card.add(exportBtn);

        return card;
    }

    private JPanel createPlaceholderPage(String pageName) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(MAIN_BG);
        page.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel header = createHeader();
        page.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(MAIN_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(pageName);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel placeholder = new JLabel("This section is coming soon...");
        placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        placeholder.setForeground(TEXT_SECONDARY);
        content.add(placeholder);

        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Search bar
        JTextField searchField = new JTextField("🔍 Search food items or health articles...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(TEXT_SECONDARY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        searchField.setPreferredSize(new Dimension(400, 45));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(MAIN_BG);
        searchPanel.add(searchField);

        header.add(searchPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel createHealthMetricsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel title = new JLabel("Your Health Metrics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);

        JLabel subtitle = new JLabel("Enter your current physical measurements");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        // Height input
        card.add(createInputField("Height (cm)", heightField = new JTextField()));
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Weight input
        card.add(createInputField("Weight (kg)", weightField = new JTextField()));
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Calculate button
        calculateBMIButton = new JButton("Calculate BMI");
        styleButton(calculateBMIButton, PRIMARY_COLOR);
        calculateBMIButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        calculateBMIButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        card.add(calculateBMIButton);

        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // BMI Display
        JPanel bmiDisplay = new JPanel();
        bmiDisplay.setLayout(new BoxLayout(bmiDisplay, BoxLayout.Y_AXIS));
        bmiDisplay.setBackground(new Color(239, 246, 255));
        bmiDisplay.setBorder(new EmptyBorder(15, 15, 15, 15));
        bmiDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);

        bmiValueLabel = new JLabel("BMI: --");
        bmiValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bmiValueLabel.setForeground(PRIMARY_COLOR);
        bmiValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        bmiCategoryLabel = new JLabel("Category: --");
        bmiCategoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bmiCategoryLabel.setForeground(TEXT_SECONDARY);
        bmiCategoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        bmiDisplay.add(bmiValueLabel);
        bmiDisplay.add(bmiCategoryLabel);
        bmiDisplay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.add(bmiDisplay);

        calculateBMIButton.addActionListener(e -> calculateAndDisplayBMI());

        return card;
    }

    private JPanel createInputField(String label, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComp.setForeground(TEXT_PRIMARY);
        labelComp.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(labelComp);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(field);

        return panel;
    }

    private JPanel createNutritionGoalsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel title = new JLabel("Daily Nutrition Goals");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);

        JLabel subtitle = new JLabel("Personalized insights and daily macro targets");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        outputArea = new JTextArea("Calculate your BMI to see personalized recommendations...");
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setForeground(TEXT_SECONDARY);
        outputArea.setBackground(CARD_BG);
        outputArea.setBorder(null);
        outputArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(outputArea);

        return card;
    }

    private JPanel createFoodIntakeCard() {
        JPanel card = new JPanel(new BorderLayout(0, 20));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);

        JLabel title = new JLabel("Today's Food Intake");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        headerPanel.add(title, BorderLayout.WEST);

        card.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Food Item Name", "Quantity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return (col == 1) ? Integer.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return true;
            }
        };

        foodTable = new JTable(tableModel);
        foodTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        foodTable.setRowHeight(40);
        foodTable.setShowVerticalLines(false);
        foodTable.setGridColor(new Color(241, 245, 249));
        foodTable.setSelectionBackground(new Color(239, 246, 255));
        foodTable.setSelectionForeground(TEXT_PRIMARY);

        JTableHeader header = foodTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        JScrollPane scrollPane = new JScrollPane(foodTable);
        scrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1));
        scrollPane.setPreferredSize(new Dimension(0, 300));
        card.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(CARD_BG);

        addRowButton = new JButton("+ Add Food Item");
        styleButton(addRowButton, SUCCESS_COLOR);
        addRowButton.setPreferredSize(new Dimension(150, 38));

        removeRowButton = new JButton("Remove");
        styleButton(removeRowButton, DANGER_COLOR);
        removeRowButton.setPreferredSize(new Dimension(120, 38));

        calculateIntakeButton = new JButton("Calculate Intake");
        styleButton(calculateIntakeButton, PRIMARY_COLOR);
        calculateIntakeButton.setPreferredSize(new Dimension(160, 38));

        buttonPanel.add(addRowButton);
        buttonPanel.add(removeRowButton);
        buttonPanel.add(calculateIntakeButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        addRowButton.addActionListener(e -> tableModel.addRow(new Object[]{"", 1}));
        removeRowButton.addActionListener(e -> {
            int row = foodTable.getSelectedRow();
            if (row >= 0) tableModel.removeRow(row);
            else JOptionPane.showMessageDialog(this, "Please select a row to remove.");
        });
        calculateIntakeButton.addActionListener(e -> calculateIntakeAndAlert());

        return card;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
    }

    private void calculateAndDisplayBMI() {
        try {
            String heightStr = heightField.getText().trim();
            String weightStr = weightField.getText().trim();

            if (heightStr.isEmpty() || weightStr.isEmpty()) {
                outputArea.setText("Please enter your height and weight.");
                return;
            }

            double heightCm = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);

            if (heightCm <= 0 || weight <= 0) {
                outputArea.setText("Height and weight must be positive numbers.");
                return;
            }

            double heightM = heightCm / 100.0;
            User user = new User(heightM, weight);
            double bmi = user.calculateBMI();
            NutritionRecommendation rec = calculator.getRecommendation(user);

            // Update BMI display
            bmiValueLabel.setText(String.format("BMI: %.2f", bmi));
            bmiCategoryLabel.setText("Category: " + calculator.getBMICategory(bmi));

            // Update nutrition recommendations
            StringBuilder sb = new StringBuilder();
            if (rec != null) {
                sb.append("📊 Nutrition Recommendations:\n\n");
                sb.append(String.format("Calories: %d to %d kcal/day\n", rec.getMinCalories(), rec.getMaxCalories()));
                sb.append(String.format("Protein: %.2f grams\n", rec.getProteinGrams()));
                sb.append(String.format("Fat: %.2f grams\n", rec.getFatGrams()));
                sb.append(String.format("Carbohydrates: %.2f grams\n\n", rec.getCarbGrams()));
                sb.append("💡 " + rec.getNotes());
            } else {
                sb.append("No recommendation data found.");
            }

            outputArea.setText(sb.toString());
        } catch (Exception ex) {
            outputArea.setText("Invalid input. Please enter valid numbers.");
        }
    }

    private void calculateIntakeAndAlert() {
        List<String> foodQuantityList = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object foodObj = tableModel.getValueAt(i, 0);
            Object qtyObj = tableModel.getValueAt(i, 1);

            String foodName = (foodObj != null) ? foodObj.toString().trim() : "";
            String qtyStr = "1";
            if (qtyObj != null) {
                qtyStr = qtyObj.toString().trim();
            }

            if (!foodName.isEmpty()) {
                foodQuantityList.add(foodName + "," + qtyStr);
            }
        }

        if (foodQuantityList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one food item.", "Input Needed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            IntakeCalculator.TotalMacroIntake total = IntakeCalculator.calculateTotalMacros(foodQuantityList);

            String heightStr = heightField.getText().trim();
            String weightStr = weightField.getText().trim();

            if (heightStr.isEmpty() || weightStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter height and weight first.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double heightCm = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);
            double heightM = heightCm / 100.0;
            User user = new User(heightM, weight);
            double bmi = user.calculateBMI();

            CalorieCalculator.BMICategory category = calculator.getBMICategory(bmi);
            NutritionRecommendation rec = DataManager.getNutritionRecommendation(category.name());

            if (rec == null) {
                JOptionPane.showMessageDialog(this, "No recommendation found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            showIntakeSummaryDialog(total, rec);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showIntakeSummaryDialog(IntakeCalculator.TotalMacroIntake total, NutritionRecommendation rec) {
        JDialog dialog = new JDialog(this, "Calorie & Macro Intake Summary", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(0, 20));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(30, 30, 20, 30));
        contentPanel.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Calorie & Macro Intake Summary");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(title);

        JLabel subtitle = new JLabel("Details of your consumed calories and macros for today.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(subtitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Your Intake
        contentPanel.add(createSummarySection("Your Intake:", new String[][]{
                {"Total Calories:", total.totalCalories + " kcal"},
                {"Protein:", String.format("%.1f g", total.totalProtein)},
                {"Fat:", String.format("%.1f g", total.totalFat)},
                {"Carbohydrates:", String.format("%.1f g", total.totalCarbs)}
        }));

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Recommended Daily
        contentPanel.add(createSummarySection("Recommended Daily:", new String[][]{
                {"Calories:", rec.getMinCalories() + " to " + rec.getMaxCalories() + " kcal/day"},
                {"Protein:", String.format("%.2f grams", rec.getProteinGrams())},
                {"Fat:", String.format("%.2f grams", rec.getFatGrams())},
                {"Carbohydrates:", String.format("%.2f grams", rec.getCarbGrams())}
        }));

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Warning message
        String warningMsg = "";
        Color warningColor = SUCCESS_COLOR;
        if (total.totalCalories < rec.getMinCalories()) {
            warningMsg = "Calorie intake below recommended. Consider eating more.";
            warningColor = new Color(234, 179, 8);
        } else if (total.totalCalories > rec.getMaxCalories()) {
            warningMsg = "Calorie intake above recommended. Consider exercising.";
            warningColor = DANGER_COLOR;
        } else {
            warningMsg = "Calorie intake within recommended range. Great job!";
        }

        JPanel warningPanel = new JPanel();
        warningPanel.setLayout(new BoxLayout(warningPanel, BoxLayout.Y_AXIS));
        warningPanel.setBackground(new Color(warningColor.getRed(), warningColor.getGreen(), warningColor.getBlue(), 20));
        warningPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        warningPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel warningLabel = new JLabel(warningMsg);
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        warningLabel.setForeground(warningColor.darker());
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        warningPanel.add(warningLabel);
        warningPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        contentPanel.add(warningPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // OK Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        JButton okButton = new JButton("OK");
        styleButton(okButton, PRIMARY_COLOR);
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createSummarySection(String title, String[][] data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        for (String[] row : data) {
            JPanel rowPanel = new JPanel(new BorderLayout());
            rowPanel.setBackground(Color.WHITE);
            rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel label = new JLabel(row[0]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(TEXT_PRIMARY);

            JLabel value = new JLabel(row[1]);
            value.setFont(new Font("Segoe UI", Font.BOLD, 14));
            value.setForeground(TEXT_PRIMARY);

            rowPanel.add(label, BorderLayout.WEST);
            rowPanel.add(value, BorderLayout.EAST);
            panel.add(rowPanel);
        }

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        return panel;
    }
}
