package Calendar;
import javax.swing.*;
import javax.swing.table.*;

import Calendar.CalendarAndDIgitalClockmy.ClockLabel;
import Calendar.CalendarAndDIgitalClockmy.Event;
import Calendar.CalendarAndDIgitalClockmy.EventDialog;
import Calendar.CalendarAndDIgitalClockmy.EventViewer;
import Calendar.CalendarAndDIgitalClockmy.btnAddEvent_Action;
import Calendar.CalendarAndDIgitalClockmy.btnNext_Action;
import Calendar.CalendarAndDIgitalClockmy.btnPrev_Action;
import Calendar.CalendarAndDIgitalClockmy.btnViewEvents_Action;
import Calendar.CalendarAndDIgitalClockmy.cmbYear_Action;
import Calendar.CalendarAndDIgitalClockmy.tblCalendarRenderer;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class CalendarWithClock {
	static JLabel lblMonth, lblYear;
    static JButton btnPrev, btnNext, btnAddEvent, btnViewEvents;
    static JTable tblCalendar;
    static JComboBox<String> cmbYear;
    static JFrame frmMain;
    static Container pane;
    static DefaultTableModel mtblCalendar;
    static JScrollPane stblCalendar;
    static JPanel pnlCalendar;
    static int realYear, realMonth, realDay, currentYear, currentMonth;

    static ArrayList<Event> eventsList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error occurred.");
        }

        frmMain = new JFrame("Digital Calendar by Kamrul & Mubta");
        frmMain.setSize(400, 500);
        frmMain.setLocationRelativeTo(null);
        pane = frmMain.getContentPane();
        pane.setLayout(null);
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblMonth = new JLabel("January");
        lblYear = new JLabel("Change year:");
        cmbYear = new JComboBox<>();
        btnPrev = new JButton("<");
        btnNext = new JButton(">");
        btnAddEvent = new JButton("Add Event");
        btnViewEvents = new JButton("View Events");
        mtblCalendar = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        tblCalendar = new JTable(mtblCalendar);
        stblCalendar = new JScrollPane(tblCalendar);
        pnlCalendar = new JPanel(null);
        pnlCalendar.setBorder(BorderFactory.createTitledBorder("Calendar"));

        btnPrev.addActionListener(new btnPrev_Action());
        btnNext.addActionListener(new btnNext_Action());
        cmbYear.addActionListener(new cmbYear_Action());
        btnAddEvent.addActionListener(new btnAddEvent_Action());
        btnViewEvents.addActionListener(new btnViewEvents_Action());

        pane.add(pnlCalendar);
        pnlCalendar.add(lblMonth);
        pnlCalendar.add(lblYear);
        pnlCalendar.add(cmbYear);
        pnlCalendar.add(btnPrev);
        pnlCalendar.add(btnNext);
        pnlCalendar.add(stblCalendar);
        pnlCalendar.add(btnAddEvent);
        pnlCalendar.add(btnViewEvents);

        pnlCalendar.setBounds(0, 0, 320, 400);
        lblMonth.setBounds(160 - lblMonth.getPreferredSize().width / 2, 25, 100, 25);
        lblYear.setBounds(10, 305, 80, 20);
        cmbYear.setBounds(230, 305, 80, 20);
        btnPrev.setBounds(10, 25, 50, 25);
        btnNext.setBounds(260, 25, 50, 25);
        stblCalendar.setBounds(10, 50, 300, 250);
        btnAddEvent.setBounds(10, 350, 120, 30);
        btnViewEvents.setBounds(170, 350, 140, 30);

        frmMain.setResizable(false);

        GregorianCalendar cal = new GregorianCalendar();
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
        realMonth = cal.get(GregorianCalendar.MONTH);
        realYear = cal.get(GregorianCalendar.YEAR);
        currentMonth = realMonth;
        currentYear = realYear;

        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri","Sat"};
        for (int i = 0; i < 7; i++) {
            mtblCalendar.addColumn(headers[i]);
        }

        tblCalendar.getParent().setBackground(tblCalendar.getBackground());
        tblCalendar.getTableHeader().setResizingAllowed(false);
        tblCalendar.getTableHeader().setReorderingAllowed(false);
        tblCalendar.setColumnSelectionAllowed(true);
        tblCalendar.setRowSelectionAllowed(true);
        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCalendar.setRowHeight(38);
        mtblCalendar.setColumnCount(7);
        mtblCalendar.setRowCount(6);

        for (int i = realYear - 100; i <= realYear + 100; i++) {
            cmbYear.addItem(String.valueOf(i));
        }

        refreshCalendar(realMonth, realYear);

        ClockLabel timeLabel = new ClockLabel("time");
        timeLabel.setBounds(0, 410, 360, 50);
        frmMain.add(timeLabel);

        frmMain.setVisible(true);
    }

    public static void refreshCalendar(int month, int year) {
    	
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int nod, som;

        btnPrev.setEnabled(true);
        btnNext.setEnabled(true);
        if (month == 0 && year <= realYear - 10) {
            btnPrev.setEnabled(false);
        }
        if (month == 11 && year >= realYear + 100) {
            btnNext.setEnabled(false);
        }
        lblMonth.setText(months[month]);
        lblMonth.setBounds(160 - lblMonth.getPreferredSize().width / 2, 25, 180, 25);
        cmbYear.setSelectedItem(String.valueOf(year));

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                mtblCalendar.setValueAt(null, i, j);
            }
        }

        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        som = cal.get(GregorianCalendar.DAY_OF_WEEK);

        for (int i = 1; i <= nod; i++) {
            int row = (i + som - 2) / 7;
            int column = (i + som - 2) % 7;
            mtblCalendar.setValueAt(i, row, column);
        }

        tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
    }

    static class tblCalendarRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (column == 6) {
                setBackground(new Color(255, 0, 0));
            } else if (column == 0) {
                setBackground(new Color(255, 0, 0));
            } else {
                setBackground(new Color(255, 255, 255));
            }

            if (value != null) {
                if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear) {
                    setBackground(new Color(83, 230, 29));
                }
            }
         // Check if the date has an event and set the color to gray
            String day = value != null ? value.toString() : "";
            for (Event event : eventsList) {
                if (event.date.equals(day) && event.month.equals(lblMonth.getText()) && event.year.equals(cmbYear.getSelectedItem().toString())) {
                    setForeground(Color.cyan);
                }
            }
            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }

    static class btnPrev_Action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 0) {
                currentMonth = 11;
                currentYear -= 1;
            } else {
                currentMonth -= 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }

    static class btnNext_Action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentMonth == 11) {
                currentMonth = 0;
                currentYear += 1;
            } else {
                currentMonth += 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }

    static class cmbYear_Action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cmbYear.getSelectedItem() != null) {
                String b = cmbYear.getSelectedItem().toString();
                currentYear = Integer.parseInt(b);
                refreshCalendar(currentMonth, currentYear);
            }
        }
    }

    static class btnAddEvent_Action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	 int selectedRow = tblCalendar.getSelectedRow();
             int selectedColumn = tblCalendar.getSelectedColumn();
             if (selectedRow != -1 && selectedColumn != -1) {
            String date = tblCalendar.getValueAt(tblCalendar.getSelectedRow(), tblCalendar.getSelectedColumn()).toString();
            String month = lblMonth.getText();
            String year = cmbYear.getSelectedItem().toString();

            EventDialog eventDialog = new EventDialog(frmMain, date, month, year);
            eventDialog.setVisible(true);
        }else {
        	JOptionPane.showMessageDialog(null, "Please select a date from the calendar.");
        }
        	
        }
    }

    static class btnViewEvents_Action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventViewer eventViewer = new EventViewer(eventsList);
            eventViewer.setVisible(true);
        }
    }

    static class ClockLabel extends JLabel implements ActionListener {
        String type;
        SimpleDateFormat sdf;

        public ClockLabel(String type) {
            this.type = type;
            setForeground(Color.gray);

            switch (type) {
                case "date" -> {
                    sdf = new SimpleDateFormat("  MMMM dd yyyy");
                    setFont(new Font("sans-serif", Font.PLAIN, 12));
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                case "time" -> {
                    sdf = new SimpleDateFormat("hh:mm:ss a");
                    setFont(new Font("sans-serif", Font.PLAIN, 40));
                    setHorizontalAlignment(SwingConstants.CENTER);
                }
                default -> sdf = new SimpleDateFormat();

            }

            javax.swing.Timer t = new javax.swing.Timer(1000, this);
            t.start();
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Date d = new Date();
            setText(sdf.format(d));
        }
    }

    static class Event {
        String date;
        String month;
        String year;
        String eventName;

        public Event(String date, String month, String year, String eventName) {
            this.date = date;
            this.month = month;
            this.year = year;
            this.eventName = eventName;
        }

        @Override
        public String toString() {
            return eventName + " on " + date + " " + month + " " + year;
        }
    }

    public static class EventDialog extends JDialog {
        private JLabel lblDate, lblEventName;
        private JTextField txtEventName;
        private JButton btnAdd;
        private String date, month, year;

        public EventDialog(Frame parent, String date, String month, String year) {
            super(parent, true);
            this.date = date;
            this.month = month;
            this.year = year;

            setTitle("Add Event");
            setSize(300, 150);
            setLocationRelativeTo(parent);
            setLayout(new GridLayout(3, 2));

            lblDate = new JLabel("Date:");
            lblEventName = new JLabel("Event Name:");
            txtEventName = new JTextField();
            btnAdd = new JButton("Add");

            lblDate.setFont(new Font("Arial", Font.BOLD, 12));
            lblEventName.setFont(new Font("Arial", Font.BOLD, 12));

            add(lblDate);
            add(new JLabel(date + " " + month + " " + year));
            add(lblEventName);
            add(txtEventName);

            btnAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String eventName = txtEventName.getText();
                    Event event = new Event(date, month, year, eventName);
                    eventsList.add(event);
                    dispose();
                }
            });

            add(btnAdd);
        }
    }

    public static class EventViewer extends JDialog {
        public EventViewer(ArrayList<Event> events) {
            setTitle("View Events");
            setSize(300, 150);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            for (Event event : events) {
                textArea.append(event.toString() + "\n");
            }

            add(new JScrollPane(textArea), BorderLayout.CENTER);
        }
    }

}
