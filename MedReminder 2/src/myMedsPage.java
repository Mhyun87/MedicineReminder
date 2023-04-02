/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;


public class myMedsPage extends javax.swing.JFrame {
    
    String medName;
    String rxNumber;
    int dosageNumber;
    String dosageType;
    int frequencyNumber;
    String frequencyType;
    int dosesProv;
    boolean refills;
    int refillNumber;
    boolean remind;
    boolean again;
    String reminder;
    
    String newReminders;
    String newReminderTimes;
    String newRefillsBool;
    String rx;
    String refillText;
    String reminderText;
    String sqlString;
    String updateString;
    
    int selection;
    int choice;
     
    /*
    Declaring the connection, statements, model, and result set for
    the table to show
    */
    
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DefaultListCellRenderer centerRender = new DefaultListCellRenderer();
    DefaultListModel DLM = new DefaultListModel();
    DefaultListModel CL = new DefaultListModel();
    DefaultListModel MIL = new DefaultListModel();


    public myMedsPage() 
    {
        initComponents();
        /*
        manually adding a DocumentListener which is not supported by 
        the GUI builder. Don't edit this code!!
        */
        refillNumberIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(refillNumberIn.getText().isBlank() || !refillNumberIn.getText().matches("[0-9]+")){
                    refillNextButton.setEnabled(false);
                }else{
                    refillNextButton.setEnabled(true);
                }                  
            }
        });
        
        medNameIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(medNameIn.getText().isBlank()){
                    nameNextButton.setEnabled(false);
                }else{
                    nameNextButton.setEnabled(true);
                }   
            }
        });
        
        dosageNumberIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(dosageNumberIn.getText().isBlank() || !dosageNumberIn.getText().matches("[0-9]+")){                    
                    dosageNextButton.setEnabled(false);
                }else{
                    dosageNextButton.setEnabled(true);
                }   
            }
        });
        
        
        frequencyNumberIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(frequencyNumberIn.getText().isBlank() || !frequencyNumberIn.getText().matches("[0-9]+")){                    
                    frequencyNextButton.setEnabled(false);
                }else{
                    frequencyNextButton.setEnabled(true);
                }   
            }
        });
        
        dosesProvIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            
            public void changed(){
                if(dosesProvIn.getText().isBlank() || !dosesProvIn.getText().matches("[0-9]+")){                    
                    dosesProvNextButton.setEnabled(false);
                }else{
                    dosesProvNextButton.setEnabled(true);
                }   
            }
        });
        
        
        /*
        Initializing the column to appear on the "my meds" page as well as
        establishing the model for the table on my meds page. Initializing the 
        connection to the local database and calling the updateTable() method
        */
        initList();
        
        updateList();

    }
    
    private void initList()
    {

        conn = myMedsPage.ConnectDB();
        
        stylizeList();
        medList.setModel(DLM);
    
    }
    

    private void stylizeList()
    {
                
        
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        centerRender.setHorizontalTextPosition(JLabel.CENTER);
        centerRender.setVerticalAlignment(JLabel.CENTER);
        centerRender.setBorder(BorderFactory.createBevelBorder(1));
        
        
        medList.setCellRenderer(centerRender);
        confirmList.setCellRenderer(centerRender);
        medInfoList.setCellRenderer(centerRender);
    }
    
    public void getMedInfo(){
        conn = myMedsPage.ConnectDB();
        
        
        if (conn != null)
        {
            medName = medList.getSelectedValue();
            String sql = "Select * FROM Medications WHERE MedName = '" + medName + "';";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    MIL.addElement("Medication: " + rs.getString("MedName"));
                    
                    String rxNum = rs.getString("RXNumber");
                    
                    if(rxNum.equals("null") == false){
                    MIL.addElement("RX Number: " + rs.getString("RXNumber"));
                    }
                    
                    MIL.addElement("Dosage: " + rs.getString("Dosage") + " " + 
                            " " + rs.getString("FrequencyNumber") + " x " + rs.getString("Frequency") + ".");
                    
                    MIL.addElement(rs.getString("DosesProvided") + " Doses Provided.");
                    MIL.addElement(rs.getString("RefillsBool") + " Refills Are Available.");
                    
                    String check = rs.getString("RefillsBool");
                    
                    if(check.equalsIgnoreCase("yes")){
                    MIL.addElement(rs.getString("RefillNumber") + " Refills Available.");
                    }
                    
                    String rem = rs.getString("Reminders");
                    
                    if(rem.equalsIgnoreCase("yes")){
                        MIL.addElement(rs.getString("Reminders") + " Reminders Are Set.");
                        MIL.addElement("For: " + rs.getString("ReminderTimes"));
                    }else if(rem.equalsIgnoreCase("no")){
                        MIL.addElement(rs.getString("Reminders") + " Reminders Are Set.");
                    }
  
                }
                
                pst.close();
                rs.close();
                medInfoList.setModel(MIL);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public String getRemindersAgain(){
        if(yesButtonRemindEdit.isSelected()){
            newReminders = "Yes";
        }else if(noButtonRemindEdit.isSelected()){
            newReminders = "No";
        }
        
        return newReminders;
    }
    
    public String getYesOrNo(){
        if(yesButtonRefillsEdit.isSelected()){
            newRefillsBool = "Yes";
        }else if(noButtonRefillsEdit.isSelected()){
            newRefillsBool = "No";
        }
        
        return newRefillsBool;
    }
    
    public void editMedInfo(){
        conn = myMedsPage.ConnectDB();
        
        if (conn != null)
        {
            medName = medList.getSelectedValue();
            String sql = "Select * FROM Medications WHERE MedName = '" + medName + "';";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    medNameEdit.setText(rs.getString("MedName"));
                    
                    String rxNum = rs.getString("RXNumber");
                    
                    if(rxNum.equals("null") == true){
                    rxNumberEdit.setText("");
                    }
                    else{
                        rxNumberEdit.setText(rs.getString("RXNumber"));
                    }
                    
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    String doseNumber = rs.getString("Dosage");
                    char[] chars = doseNumber.toCharArray();
                    for(char c : chars){
                        if(Character.isDigit(c)){
                            sb.append(c);
                        }else if(!Character.isDigit(c)){
                            sb2.append(c);
                        }
                    }
                    
                    dosageNumberEdit.setText(sb.toString()); 
                    frequencyNumberEdit.setText(rs.getString("FrequencyNumber")); 
                    
                    String doseTypeEdits = sb2.toString().stripLeading();
                    
                    switch(doseTypeEdits){
                        
                        case "MG":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Tablet(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Capsule(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "ML":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Injection(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Puff(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Application(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                        case "Drop(s)":
                            dosageTypeEdit.setSelectedItem(doseTypeEdits);
                            break;
                    }
                    
                    String fc = rs.getString("Frequency");
                    
                    if(fc.equalsIgnoreCase("a day")){
                        frequencyTypeIn.setSelectedIndex(0);
                    }else if(fc.equalsIgnoreCase("a week")){
                        frequencyTypeIn.setSelectedIndex(1);
                    }else if(fc.equalsIgnoreCase("a month")){
                        frequencyTypeIn.setSelectedIndex(2);
                    }
                    
                    dosesProvEdit.setText(rs.getString("DosesProvided"));
                    
                    
                    String check = rs.getString("RefillsBool");
                    
                    if(check.equalsIgnoreCase("yes")){
                        yesButtonRefillsEdit.setSelected(true);
                        howManyLabel.setVisible(true);
                        refillNumberEdit.setVisible(true);
                        refillNumberEdit.setText(rs.getString("RefillNumber"));
                    }else if(check.equalsIgnoreCase("no")){
                        noButtonRefillsEdit.setSelected(true);
                        howManyLabel.setVisible(false);
                        refillNumberEdit.setVisible(false);
                    }
                    
                    String rem = rs.getString("Reminders");
                    
                    if(rem.equalsIgnoreCase("yes")){
                        yesButtonRemindEdit.setSelected(true);
                        
                        String time = rs.getString("ReminderTimes");
                        System.out.print(time);
                        String hour = time.substring(0, 2);
                        String minute = time.substring(3, 5);
                        String half = time.substring(6, 10);
                        hourInEdit.setVisible(true);
                        minuteInEdit.setVisible(true);
                        ampmInEdit.setVisible(true);
                        hourInEdit.setValue(hour);
                        minuteInEdit.setValue(minute);
                        if(half.equalsIgnoreCase("a.m.")){
                            ampmInEdit.setValue("A.M.");
                        }else if(half.equalsIgnoreCase("p.m.")){
                            ampmInEdit.setValue("P.M.");
                        }
                    }else if(rem.equalsIgnoreCase("no")){
                        noButtonRemindEdit.setSelected(true);
                        hourInEdit.setVisible(false);
                        minuteInEdit.setVisible(false);
                        ampmInEdit.setVisible(false);
                    }
  
                }
                
                pst.close();
                rs.close();
                medInfoList.setModel(MIL);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public String getMedName()
    {   
        medName = medNameIn.getText();
        
        return medName;
    }   
    
    public String getRXNumber()
    {
        if(rxNumberIn.getText().toString().isBlank()){
            rxNumber = null;
        }else{
            rxNumber = rxNumberIn.getText().toString();
        }
        return rxNumber;
    }
   
    public int getDosageNumber()
    {
        dosageNumber = Integer.parseInt(dosageNumberIn.getText().toString());
        
        return dosageNumber;
    }
    
    public String getDosageType()
    {
        dosageType = dosageTypeIn.getSelectedItem().toString();
        
        return dosageType;
    }
    
    public int getFrequencyNumber()
    {
        frequencyNumber = Integer.parseInt(frequencyNumberIn.getText().toString());
        
        return frequencyNumber;
    }
    
    public String getFrequencyType()
    {
        frequencyType = frequencyTypeIn.getSelectedItem().toString();
        
        return frequencyType;
    }
    
    public int getDosesProv()
    {
        dosesProv = Integer.parseInt(dosesProvIn.getText().toString());
        
        return dosesProv;
    }
    
    
    public boolean getRefills(Boolean refills)
    {
        if(refillNumberIn.getText().isBlank())
        {
            refills = false;
            
        }else if(!refillNumberIn.getText().isBlank())
        {
            refills = true;
            setRefillNumber();
        }
        return refills;
    }
    
    
    public int setRefillNumber()
    {
        refillNumber = Integer.parseInt(refillNumberIn.getText().toString());
        
        return refillNumber;
    }
    
    public boolean getReminder()
    {
        if(hourIn.isVisible()){
            remind = true;
            
        }else if(!hourIn.isVisible()){
            remind = false;
        }
        return remind;
    }
    
    public String setReminder(){
        DecimalFormat df = new DecimalFormat("##");
        
        String hour = hourIn.getValue().toString();
        String minute = minuteIn.getValue().toString();
        String half = ampmIn.getValue().toString();
        reminder = (hour + ":" + minute + " " + half);
        
        return reminder;
    }
    
    public boolean getAgain(Boolean again){
        if(yesButtonA.isSelected()){
            again = true;
        }else if(noButtonA.isSelected()){
            again = false;
        }
        
        return again;
    }
    
    private void setConList(String medName, String rxNumber, int dosageNumber, String dosageType,
        int frequencyNumber, String frequencyType, int dosesProv, boolean refills,
        int refillNumber, boolean remind, boolean again, String reminder){

        CL.clear();
        confirmList.setModel(CL);
        CL.addElement(medName);

        
        if(rxNumber != null){
                CL.addElement("RX Number:  " + rxNumber);
            }else if(rxNumber == null){
                CL.addElement("RX Number: Not Provided");
            }
        CL.addElement(dosageNumber + " " + dosageType + " " +  frequencyNumber + " Times " + frequencyType);

        CL.addElement(dosesProv + " doses provided.");


        if(refills == false){
            CL.addElement("No Refills Available");
        }else if(refills == true){            
            CL.addElement(String.valueOf(refillNumber) + " Refills Available");
        }


        if(remind = false){
            CL.addElement(" ");
        }else if(remind = true){
            CL.addElement(reminder);
        }


        if(yesButtonA.isSelected() == true){
            CL.addElement("You will be reminded");
        }else if(noButtonA.isSelected() == true){
            CL.addElement("You do not require");
            CL.addElement("this medication again");
        }
    }
        
        
    public String createUpdateString(String medName, String newRefillsBool, String newReminders,
            String newReminderTimes){
        
        String newMedName = medNameEdit.getText();
        String newRXNumber = rxNumberEdit.getText();
        String newDosageNumber = dosageNumberEdit.getText();
        String newDosageType = dosageTypeEdit.getSelectedItem().toString();
        String newDose = (newDosageNumber + " " + newDosageType);
        String newFrequencyNumber = frequencyNumberEdit.getText();
        String newFrequencyType = frequencyTypeEdit.getSelectedItem().toString();
        String newDosesProv = dosesProvEdit.getText();
        String newRefillNumber = refillNumberEdit.getText();
        String newHour = hourInEdit.getValue().toString();
        String newMinute = minuteInEdit.getValue().toString();
        String newAMPM = ampmInEdit.getValue().toString();
        
        

        if(yesButtonRefillsEdit.isSelected()){
            newRefillsBool = "Yes";
        }else if(noButtonRefillsEdit.isSelected()){
            newRefillsBool = "No";
        }
        
        if(yesButtonRemindEdit.isSelected()){
            newReminders = "Yes";
            newReminderTimes = newHour + ":" + newMinute + " " + newAMPM;
        }else if(noButtonRemindEdit.isSelected()){
            newReminders = "No";
            newReminderTimes = "null";
        }
        

        sqlString = ("UPDATE Medications SET "
            + "MedName = '"
            + newMedName + "',"
            + "RXNumber = '"
            + newRXNumber + "', "
            + "Dosage = '"
            + newDose + "', "
            + "FrequencyNumber = '"
            + newFrequencyNumber + "', "
            + "Frequency = '"
            + newFrequencyType + "', "
            + "DosesProvided = '"
            + newDosesProv + "', "
            + "RefillsBool = '"
            + newRefillsBool + "', "
            + "RefillNumber = '"
            + newRefillNumber + "', "
            + "Reminders = '"
            + newReminders + "', "
            + "ReminderTimes = '"
            + newReminderTimes + "' WHERE MedName = '" + medName + "';");
        
        
        System.out.print(sqlString);
    
        return sqlString;
    }

    /*
      createSQLString method creates the command to enter the information into
        the database based on the information the user has provided    
    */
    public String createSQLString(String medName, String rxNumber, int dosageNumber, String dosageType,
            int frequencyNumber, String frequencyType, int dosesProv, boolean refills, 
            int refillNumber, boolean remind, boolean again, String reminder){
        
        
        String dosage = (String.valueOf(dosageNumber) + " " + dosageType);

            if(rxNumber != null){
                rx = rxNumber;
            }else if(rxNumber == null){
                rx = "null";
            }
            
        String doses = String.valueOf(dosesProv);
        
            if(refills == false){
                refillText = "No";
            }else if(refills == true){            
                refillText = "Yes";
            }
            
        String numOfRefill = String.valueOf(refillNumber);
          
            if(remind == false){
                reminderText = "No";
                reminder = "null";
            }else if(remind == true){
                reminderText = "Yes";
            }

        sqlString = ("INSERT INTO Medications VALUES(" 
            + "'" + medName + "',"
            + "'" + rx + "', " 
            + "'" + dosage + "', "
            + "'" + frequencyNumber + "', "
            + "'" + frequencyType + "', " 
            + "'" + doses + "', " 
            + "'" + refillText + "', "
            + "'" + numOfRefill + "', "
            + "'" + reminderText + "', "
            + "'" + reminder + "'" + ");");
        
        
        System.out.print(sqlString);
    
        return sqlString;
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonBar = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        middlePanel01 = new javax.swing.JPanel();
        myMedsListPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        medList = new javax.swing.JList<>();
        addNewMedButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        medNamePanel = new javax.swing.JPanel();
        nameInstruct = new javax.swing.JLabel();
        medNameIn = new javax.swing.JTextField();
        nameNextButton = new javax.swing.JButton();
        nameBackButton = new javax.swing.JButton();
        nameCancelButton = new javax.swing.JButton();
        rxNumberPanel = new javax.swing.JPanel();
        rxNumInst = new javax.swing.JLabel();
        rxNumberIn = new javax.swing.JTextField();
        rxNumberNextButton = new javax.swing.JButton();
        rxNumberBackButton = new javax.swing.JButton();
        rxNumberCancelButton = new javax.swing.JButton();
        dosagePanel = new javax.swing.JPanel();
        dosageInstruct = new javax.swing.JLabel();
        dosageNumberIn = new javax.swing.JTextField();
        dosageNextButton = new javax.swing.JButton();
        dosageTypeIn = new javax.swing.JComboBox<>();
        dosageBackButton = new javax.swing.JButton();
        dosageCancelButton = new javax.swing.JButton();
        frequencyPanel = new javax.swing.JPanel();
        frequencyInstruct = new javax.swing.JLabel();
        frequencyNumberIn = new javax.swing.JTextField();
        frequencyTypeIn = new javax.swing.JComboBox<>();
        frequencyNextButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        frequencyBackButton = new javax.swing.JButton();
        frequencyCancelButton = new javax.swing.JButton();
        dosesPanel = new javax.swing.JPanel();
        dosesInstruct = new javax.swing.JLabel();
        dosesProvIn = new javax.swing.JTextField();
        dosesProvNextButton = new javax.swing.JButton();
        dosesProvBackButton = new javax.swing.JButton();
        dosesProvCancelButton = new javax.swing.JButton();
        refillsPanel = new javax.swing.JPanel();
        refillInstruct = new javax.swing.JLabel();
        howMany = new javax.swing.JLabel();
        refillNumberIn = new javax.swing.JTextField();
        refillNextButton = new javax.swing.JButton();
        yesButton = new javax.swing.JButton();
        noButton = new javax.swing.JButton();
        refillsBackButton = new javax.swing.JButton();
        refillsCancelButton = new javax.swing.JButton();
        reminderPanel = new javax.swing.JPanel();
        reminderInstruct = new javax.swing.JLabel();
        remindNextButton = new javax.swing.JButton();
        yesButtonR = new javax.swing.JButton();
        noButtonR = new javax.swing.JButton();
        hourIn = new javax.swing.JSpinner();
        minuteIn = new javax.swing.JSpinner();
        ampmIn = new javax.swing.JSpinner();
        reminderBackButton = new javax.swing.JButton();
        reminderCancelButton = new javax.swing.JButton();
        requireAgainPanel = new javax.swing.JPanel();
        requireAgainInstruct = new javax.swing.JLabel();
        requireAgainNextButton = new javax.swing.JButton();
        callMessage = new javax.swing.JLabel();
        requireAgainBackButton = new javax.swing.JButton();
        requireAgainCancelButton = new javax.swing.JButton();
        yesButtonA = new javax.swing.JToggleButton();
        noButtonA = new javax.swing.JToggleButton();
        confirmationPanel = new javax.swing.JPanel();
        confirmButton = new javax.swing.JButton();
        confirmCancelButton = new javax.swing.JButton();
        confirmBackButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        confirmList = new javax.swing.JList<>();
        medInfoPanel = new javax.swing.JPanel();
        medInfoBackButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        medInfoList = new javax.swing.JList<>();
        medInfoEditButton = new javax.swing.JButton();
        editMedPanel = new javax.swing.JPanel();
        editPanelConfirmButton = new javax.swing.JButton();
        editPanelBackButton = new javax.swing.JButton();
        medNameEdit = new javax.swing.JTextField();
        rxNumberEdit = new javax.swing.JTextField();
        dosageNumberEdit = new javax.swing.JTextField();
        dosageTypeEdit = new javax.swing.JComboBox<>();
        frequencyNumberEdit = new javax.swing.JTextField();
        frequencyTypeEdit = new javax.swing.JComboBox<>();
        dosesProvEdit = new javax.swing.JTextField();
        refillNumberEdit = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        howManyLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        hourInEdit = new javax.swing.JSpinner();
        minuteInEdit = new javax.swing.JSpinner();
        ampmInEdit = new javax.swing.JSpinner();
        yesButtonRemindEdit = new javax.swing.JToggleButton();
        noButtonRemindEdit = new javax.swing.JToggleButton();
        yesButtonRefillsEdit = new javax.swing.JToggleButton();
        noButtonRefillsEdit = new javax.swing.JToggleButton();
        yesButtonAgainEdit = new javax.swing.JToggleButton();
        noButtonAgainEdit = new javax.swing.JToggleButton();
        topPanel = new javax.swing.JPanel();
        titleBar = new javax.swing.JLabel();
        myMedsButton = new javax.swing.JButton();
        upcomingRemindersButton = new javax.swing.JButton();
        iceButton = new javax.swing.JButton();
        profileOptionsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 602, 841));
        setSize(new java.awt.Dimension(510, 800));

        middlePanel01.setBackground(new java.awt.Color(255, 232, 214));
        middlePanel01.setPreferredSize(new java.awt.Dimension(510, 600));
        middlePanel01.setLayout(new java.awt.CardLayout());

        myMedsListPanel.setOpaque(false);
        myMedsListPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setMaximumSize(new java.awt.Dimension(300, 350));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 350));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 350));
        jScrollPane2.setViewportView(medList);

        medList.setBackground(new java.awt.Color(107, 112, 92));
        medList.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        medList.setForeground(new java.awt.Color(255, 232, 214));
        medList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        medList.setSelectionBackground(new java.awt.Color(221, 190, 169));
        medList.setSelectionForeground(new java.awt.Color(107, 112, 92));
        medList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                medListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(medList);

        addNewMedButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add A Medication.png"))); // NOI18N
        addNewMedButton.setBorderPainted(false);
        addNewMedButton.setContentAreaFilled(false);
        addNewMedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewMedButtonActionPerformed(evt);
            }
        });

        editButton.setText("EDIT");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("DELETE");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout myMedsListPanelLayout = new javax.swing.GroupLayout(myMedsListPanel);
        myMedsListPanel.setLayout(myMedsListPanelLayout);
        myMedsListPanelLayout.setHorizontalGroup(
            myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myMedsListPanelLayout.createSequentialGroup()
                .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(myMedsListPanelLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(addNewMedButton))
                    .addGroup(myMedsListPanelLayout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(myMedsListPanelLayout.createSequentialGroup()
                                .addComponent(editButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(deleteButton))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(79, Short.MAX_VALUE))
            .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 510, Short.MAX_VALUE))
        );
        myMedsListPanelLayout.setVerticalGroup(
            myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myMedsListPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editButton)
                    .addComponent(deleteButton))
                .addGap(18, 18, 18)
                .addComponent(addNewMedButton)
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(myMedsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 600, Short.MAX_VALUE))
        );

        middlePanel01.add(myMedsListPanel, "card2");
        myMedsListPanel.setVisible(true);

        medNamePanel.setBackground(new java.awt.Color(255, 232, 214));
        medNamePanel.setForeground(new java.awt.Color(255, 232, 214));
        medNamePanel.setOpaque(false);
        medNamePanel.setPreferredSize(new java.awt.Dimension(510, 600));

        nameInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        nameInstruct.setForeground(new java.awt.Color(0, 0, 0));
        nameInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nameInstruct.setText("Enter the name of the medication:");

        medNameIn.setBackground(new java.awt.Color(107, 112, 92));
        medNameIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N

        nameNextButton.setBackground(new java.awt.Color(107, 112, 92));
        nameNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        nameNextButton.setForeground(new java.awt.Color(221, 190, 169));
        nameNextButton.setText("NEXT");
        nameNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nameNextButton.setEnabled(false);
        nameNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameNextButtonActionPerformed(evt);
            }
        });

        nameBackButton.setText("BACK");
        nameBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameBackButtonActionPerformed(evt);
            }
        });

        nameCancelButton.setText("CANCEL");
        nameCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout medNamePanelLayout = new javax.swing.GroupLayout(medNamePanel);
        medNamePanel.setLayout(medNamePanelLayout);
        medNamePanelLayout.setHorizontalGroup(
            medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(medNamePanelLayout.createSequentialGroup()
                .addGroup(medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(medNamePanelLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(medNamePanelLayout.createSequentialGroup()
                                .addComponent(nameBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(67, 67, 67)
                                .addComponent(nameNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(nameInstruct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(medNameIn))))
                    .addGroup(medNamePanelLayout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(nameCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(100, 100, 100))
        );
        medNamePanelLayout.setVerticalGroup(
            medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, medNamePanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(nameInstruct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(medNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115)
                .addGroup(medNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nameNextButton, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(nameBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(44, 44, 44)
                .addComponent(nameCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        middlePanel01.add(medNamePanel, "card2");
        medNamePanel.setVisible(false);

        rxNumberPanel.setBackground(new java.awt.Color(255, 232, 214));
        rxNumberPanel.setForeground(new java.awt.Color(255, 232, 214));
        rxNumberPanel.setOpaque(false);
        rxNumberPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        rxNumInst.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        rxNumInst.setForeground(new java.awt.Color(0, 0, 0));
        rxNumInst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rxNumInst.setText("Enter the Prescription Number:");

        rxNumberIn.setBackground(new java.awt.Color(107, 112, 92));
        rxNumberIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N

        rxNumberNextButton.setBackground(new java.awt.Color(107, 112, 92));
        rxNumberNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        rxNumberNextButton.setForeground(new java.awt.Color(221, 190, 169));
        rxNumberNextButton.setText("NEXT");
        rxNumberNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rxNumberNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxNumberNextButtonActionPerformed(evt);
            }
        });

        rxNumberBackButton.setText("BACK");
        rxNumberBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxNumberBackButtonActionPerformed(evt);
            }
        });

        rxNumberCancelButton.setText("CANCEL");
        rxNumberCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxNumberCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rxNumberPanelLayout = new javax.swing.GroupLayout(rxNumberPanel);
        rxNumberPanel.setLayout(rxNumberPanelLayout);
        rxNumberPanelLayout.setHorizontalGroup(
            rxNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rxNumberPanelLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(rxNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(rxNumberPanelLayout.createSequentialGroup()
                        .addComponent(rxNumberBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rxNumberNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rxNumInst, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(rxNumberIn))
                .addContainerGap(77, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rxNumberPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rxNumberCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157))
        );
        rxNumberPanelLayout.setVerticalGroup(
            rxNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rxNumberPanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(rxNumInst)
                .addGap(104, 104, 104)
                .addComponent(rxNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115)
                .addGroup(rxNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rxNumberNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rxNumberBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(rxNumberCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );

        middlePanel01.add(rxNumberPanel, "card2");
        rxNumberPanel.setVisible(false);

        dosagePanel.setBackground(new java.awt.Color(255, 232, 214));
        dosagePanel.setForeground(new java.awt.Color(255, 232, 214));
        dosagePanel.setOpaque(false);
        dosagePanel.setPreferredSize(new java.awt.Dimension(510, 600));

        dosageInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        dosageInstruct.setForeground(new java.awt.Color(0, 0, 0));
        dosageInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dosageInstruct.setText("Enter the Dosage Information:");

        dosageNumberIn.setBackground(new java.awt.Color(107, 112, 92));
        dosageNumberIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosageNumberIn.setToolTipText("Enter a number for the dosage.");

        dosageNextButton.setBackground(new java.awt.Color(107, 112, 92));
        dosageNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosageNextButton.setForeground(new java.awt.Color(255, 232, 214));
        dosageNextButton.setText("NEXT");
        dosageNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        dosageNextButton.setEnabled(false);
        dosageNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosageNextButtonActionPerformed(evt);
            }
        });

        dosageTypeIn.setBackground(new java.awt.Color(107, 112, 92));
        dosageTypeIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosageTypeIn.setForeground(new java.awt.Color(255, 232, 214));
        dosageTypeIn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MG", "Tablet(s)", "Capsule(s)", "ML", "Injection(s)", "Puff(s)", "Application(s)", "Drop(s)" }));
        dosageTypeIn.setToolTipText("Select the type of dosage");

        dosageBackButton.setText("BACK");
        dosageBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosageBackButtonActionPerformed(evt);
            }
        });

        dosageCancelButton.setText("CANCEL");
        dosageCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosageCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dosagePanelLayout = new javax.swing.GroupLayout(dosagePanel);
        dosagePanel.setLayout(dosagePanelLayout);
        dosagePanelLayout.setHorizontalGroup(
            dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dosagePanelLayout.createSequentialGroup()
                .addGroup(dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dosagePanelLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(dosagePanelLayout.createSequentialGroup()
                                .addComponent(dosageBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dosageNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dosageInstruct, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dosagePanelLayout.createSequentialGroup()
                                .addComponent(dosageNumberIn)
                                .addGap(18, 18, 18)
                                .addComponent(dosageTypeIn, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(dosagePanelLayout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(dosageCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        dosagePanelLayout.setVerticalGroup(
            dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dosagePanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(dosageInstruct)
                .addGap(104, 104, 104)
                .addGroup(dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosageNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosageTypeIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(115, 115, 115)
                .addGroup(dosagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosageNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosageBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(dosageCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        middlePanel01.add(dosagePanel, "card2");
        dosagePanel.setVisible(false);

        frequencyPanel.setBackground(new java.awt.Color(255, 232, 214));
        frequencyPanel.setForeground(new java.awt.Color(255, 232, 214));
        frequencyPanel.setOpaque(false);
        frequencyPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        frequencyInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        frequencyInstruct.setForeground(new java.awt.Color(0, 0, 0));
        frequencyInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        frequencyInstruct.setText("How often do you take this medication?");

        frequencyNumberIn.setBackground(new java.awt.Color(107, 112, 92));
        frequencyNumberIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        frequencyNumberIn.setToolTipText("");

        frequencyTypeIn.setBackground(new java.awt.Color(107, 112, 92));
        frequencyTypeIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        frequencyTypeIn.setForeground(new java.awt.Color(255, 232, 214));
        frequencyTypeIn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A Day", "A Week", "A Month", "" }));
        frequencyTypeIn.setToolTipText("");

        frequencyNextButton.setBackground(new java.awt.Color(107, 112, 92));
        frequencyNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        frequencyNextButton.setForeground(new java.awt.Color(255, 232, 214));
        frequencyNextButton.setText("NEXT");
        frequencyNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        frequencyNextButton.setEnabled(false);
        frequencyNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frequencyNextButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(107, 112, 92));
        jLabel1.setText("X");

        frequencyBackButton.setText("BACK");
        frequencyBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frequencyBackButtonActionPerformed(evt);
            }
        });

        frequencyCancelButton.setText("CANCEL");
        frequencyCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frequencyCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frequencyPanelLayout = new javax.swing.GroupLayout(frequencyPanel);
        frequencyPanel.setLayout(frequencyPanelLayout);
        frequencyPanelLayout.setHorizontalGroup(
            frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frequencyPanelLayout.createSequentialGroup()
                .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frequencyPanelLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(frequencyInstruct))
                    .addGroup(frequencyPanelLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(frequencyPanelLayout.createSequentialGroup()
                                    .addComponent(frequencyBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(frequencyNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(frequencyPanelLayout.createSequentialGroup()
                                    .addGap(61, 61, 61)
                                    .addComponent(frequencyCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(frequencyPanelLayout.createSequentialGroup()
                                .addComponent(frequencyNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(frequencyTypeIn, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        frequencyPanelLayout.setVerticalGroup(
            frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frequencyPanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(frequencyInstruct)
                .addGap(104, 104, 104)
                .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frequencyNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frequencyTypeIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(116, 116, 116)
                .addGroup(frequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frequencyBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frequencyNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(frequencyCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        middlePanel01.add(frequencyPanel, "card2");
        frequencyPanel.setVisible(false);

        dosesPanel.setBackground(new java.awt.Color(255, 232, 214));
        dosesPanel.setForeground(new java.awt.Color(255, 232, 214));
        dosesPanel.setOpaque(false);
        dosesPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        dosesInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        dosesInstruct.setForeground(new java.awt.Color(0, 0, 0));
        dosesInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dosesInstruct.setText("How many doses do you have?");

        dosesProvIn.setBackground(new java.awt.Color(107, 112, 92));
        dosesProvIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N

        dosesProvNextButton.setBackground(new java.awt.Color(107, 112, 92));
        dosesProvNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosesProvNextButton.setForeground(new java.awt.Color(221, 190, 169));
        dosesProvNextButton.setText("NEXT");
        dosesProvNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        dosesProvNextButton.setEnabled(false);
        dosesProvNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosesProvNextButtonActionPerformed(evt);
            }
        });

        dosesProvBackButton.setText("BACK");
        dosesProvBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosesProvBackButtonActionPerformed(evt);
            }
        });

        dosesProvCancelButton.setText("CANCEL");
        dosesProvCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosesProvCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dosesPanelLayout = new javax.swing.GroupLayout(dosesPanel);
        dosesPanel.setLayout(dosesPanelLayout);
        dosesPanelLayout.setHorizontalGroup(
            dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dosesPanelLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(dosesPanelLayout.createSequentialGroup()
                        .addComponent(dosesProvBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(dosesProvNextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(dosesInstruct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dosesProvIn)
                    .addGroup(dosesPanelLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(dosesProvCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        dosesPanelLayout.setVerticalGroup(
            dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dosesPanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(dosesInstruct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dosesProvIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115)
                .addGroup(dosesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosesProvBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosesProvNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(dosesProvCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(299, 299, 299))
        );

        middlePanel01.add(dosesPanel, "card2");
        dosesPanel.setVisible(false);

        refillsPanel.setBackground(new java.awt.Color(255, 232, 214));
        refillsPanel.setForeground(new java.awt.Color(255, 232, 214));
        refillsPanel.setOpaque(false);
        refillsPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        refillInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        refillInstruct.setForeground(new java.awt.Color(0, 0, 0));
        refillInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        refillInstruct.setText("Do you have refills available?");

        howMany.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        howMany.setForeground(new java.awt.Color(0, 0, 0));
        howMany.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        howMany.setText("How Many?");

        refillNumberIn.setBackground(new java.awt.Color(107, 112, 92));
        refillNumberIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N

        refillNextButton.setBackground(new java.awt.Color(107, 112, 92));
        refillNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        refillNextButton.setForeground(new java.awt.Color(221, 190, 169));
        refillNextButton.setText("NEXT");
        refillNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        refillNextButton.setEnabled(false);
        refillNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refillNextButtonActionPerformed(evt);
            }
        });

        yesButton.setBackground(new java.awt.Color(107, 112, 92));
        yesButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        yesButton.setForeground(new java.awt.Color(255, 232, 214));
        yesButton.setText("YES");
        yesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        yesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonActionPerformed(evt);
            }
        });

        noButton.setBackground(new java.awt.Color(107, 112, 92));
        noButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        noButton.setForeground(new java.awt.Color(255, 232, 214));
        noButton.setText("NO");
        noButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        noButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonActionPerformed(evt);
            }
        });

        refillsBackButton.setText("BACK");
        refillsBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refillsBackButtonActionPerformed(evt);
            }
        });

        refillsCancelButton.setText("CANCEL");
        refillsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refillsCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout refillsPanelLayout = new javax.swing.GroupLayout(refillsPanel);
        refillsPanel.setLayout(refillsPanelLayout);
        refillsPanelLayout.setHorizontalGroup(
            refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(refillsPanelLayout.createSequentialGroup()
                .addGroup(refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(refillsPanelLayout.createSequentialGroup()
                            .addGap(96, 96, 96)
                            .addGroup(refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(refillInstruct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refillNumberIn)
                                .addGroup(refillsPanelLayout.createSequentialGroup()
                                    .addComponent(yesButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(noButton))
                                .addComponent(howMany, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(refillsPanelLayout.createSequentialGroup()
                            .addGap(95, 95, 95)
                            .addComponent(refillsBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(refillNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(refillsPanelLayout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(refillsCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        refillsPanelLayout.setVerticalGroup(
            refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, refillsPanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(refillInstruct)
                .addGap(46, 46, 46)
                .addGroup(refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yesButton)
                    .addComponent(noButton))
                .addGap(18, 18, 18)
                .addComponent(howMany)
                .addGap(18, 18, 18)
                .addComponent(refillNumberIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(refillsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refillNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refillsBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(refillsCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        howMany.setVisible(false);
        refillNumberIn.setVisible(false);

        middlePanel01.add(refillsPanel, "card2");
        refillsPanel.setVisible(false);

        reminderPanel.setBackground(new java.awt.Color(255, 232, 214));
        reminderPanel.setForeground(new java.awt.Color(255, 232, 214));
        reminderPanel.setOpaque(false);
        reminderPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        reminderInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        reminderInstruct.setForeground(new java.awt.Color(0, 0, 0));
        reminderInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        reminderInstruct.setText("Do you want to set reminders?");

        remindNextButton.setBackground(new java.awt.Color(107, 112, 92));
        remindNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        remindNextButton.setForeground(new java.awt.Color(221, 190, 169));
        remindNextButton.setText("NEXT");
        remindNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        remindNextButton.setEnabled(false);
        remindNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remindNextButtonActionPerformed(evt);
            }
        });

        yesButtonR.setBackground(new java.awt.Color(107, 112, 92));
        yesButtonR.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        yesButtonR.setForeground(new java.awt.Color(255, 232, 214));
        yesButtonR.setText("YES");
        yesButtonR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        yesButtonR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonRActionPerformed(evt);
            }
        });

        noButtonR.setBackground(new java.awt.Color(107, 112, 92));
        noButtonR.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        noButtonR.setForeground(new java.awt.Color(255, 232, 214));
        noButtonR.setText("NO");
        noButtonR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        noButtonR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonRActionPerformed(evt);
            }
        });

        hourIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        hourIn.setModel(new javax.swing.SpinnerListModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
        hourIn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        minuteIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        minuteIn.setModel(new javax.swing.SpinnerListModel(new String[] {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"}));
        minuteIn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ampmIn.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        ampmIn.setModel(new javax.swing.SpinnerListModel(new String[] {"A.M.", "P.M."}));

        reminderBackButton.setText("BACK");
        reminderBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reminderBackButtonActionPerformed(evt);
            }
        });

        reminderCancelButton.setText("CANCEL");
        reminderCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reminderCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout reminderPanelLayout = new javax.swing.GroupLayout(reminderPanel);
        reminderPanel.setLayout(reminderPanelLayout);
        reminderPanelLayout.setHorizontalGroup(
            reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reminderPanelLayout.createSequentialGroup()
                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reminderPanelLayout.createSequentialGroup()
                        .addGap(205, 205, 205)
                        .addComponent(minuteIn, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(reminderPanelLayout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(reminderPanelLayout.createSequentialGroup()
                                .addComponent(reminderBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)
                                .addComponent(remindNextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(reminderInstruct)
                            .addGroup(reminderPanelLayout.createSequentialGroup()
                                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(hourIn)
                                    .addComponent(yesButtonR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noButtonR, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ampmIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(98, 98, 98))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reminderPanelLayout.createSequentialGroup()
                .addComponent(reminderCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(157, 157, 157))
        );

        reminderPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ampmIn, hourIn, minuteIn});

        reminderPanelLayout.setVerticalGroup(
            reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reminderPanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(reminderInstruct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yesButtonR)
                    .addComponent(noButtonR))
                .addGap(71, 71, 71)
                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hourIn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ampmIn, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(minuteIn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(104, 104, 104)
                .addGroup(reminderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reminderBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(remindNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(reminderCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );

        reminderPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ampmIn, hourIn, minuteIn});

        hourIn.setVisible(false);
        minuteIn.setVisible(false);
        ampmIn.setVisible(false);

        middlePanel01.add(reminderPanel, "card2");
        refillsPanel.setVisible(false);

        requireAgainPanel.setBackground(new java.awt.Color(255, 232, 214));
        requireAgainPanel.setForeground(new java.awt.Color(255, 232, 214));
        requireAgainPanel.setOpaque(false);
        requireAgainPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        requireAgainInstruct.setFont(new java.awt.Font("Mongolian Baiti", 1, 24)); // NOI18N
        requireAgainInstruct.setForeground(new java.awt.Color(0, 0, 0));
        requireAgainInstruct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        requireAgainInstruct.setText("Will you require this again?");

        requireAgainNextButton.setBackground(new java.awt.Color(107, 112, 92));
        requireAgainNextButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        requireAgainNextButton.setForeground(new java.awt.Color(221, 190, 169));
        requireAgainNextButton.setText("NEXT");
        requireAgainNextButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        requireAgainNextButton.setEnabled(false);
        requireAgainNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requireAgainNextButtonActionPerformed(evt);
            }
        });

        callMessage.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        callMessage.setText("You'll Be Reminded to Call Your Doctor");

        requireAgainBackButton.setText("BACK");
        requireAgainBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requireAgainBackButtonActionPerformed(evt);
            }
        });

        requireAgainCancelButton.setText("CANCEL");
        requireAgainCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requireAgainCancelButtonActionPerformed(evt);
            }
        });

        buttonGroup4.add(yesButtonA);
        yesButtonA.setText("YES");
        yesButtonA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonAActionPerformed(evt);
            }
        });

        buttonGroup4.add(noButtonA);
        noButtonA.setText("NO");
        noButtonA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout requireAgainPanelLayout = new javax.swing.GroupLayout(requireAgainPanel);
        requireAgainPanel.setLayout(requireAgainPanelLayout);
        requireAgainPanelLayout.setHorizontalGroup(
            requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requireAgainPanelLayout.createSequentialGroup()
                .addContainerGap(98, Short.MAX_VALUE)
                .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requireAgainPanelLayout.createSequentialGroup()
                        .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, requireAgainPanelLayout.createSequentialGroup()
                                .addComponent(requireAgainBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(requireAgainNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(callMessage))
                        .addGap(95, 95, 95))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requireAgainPanelLayout.createSequentialGroup()
                        .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(requireAgainPanelLayout.createSequentialGroup()
                                .addComponent(yesButtonA, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(noButtonA, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(requireAgainInstruct))
                        .addGap(111, 111, 111))))
            .addGroup(requireAgainPanelLayout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(requireAgainCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        requireAgainPanelLayout.setVerticalGroup(
            requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requireAgainPanelLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(requireAgainInstruct)
                .addGap(52, 52, 52)
                .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yesButtonA)
                    .addComponent(noButtonA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(callMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(requireAgainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(requireAgainNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(requireAgainBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(requireAgainCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );

        callMessage.setVisible(false);

        middlePanel01.add(requireAgainPanel, "card2");
        refillsPanel.setVisible(false);

        confirmationPanel.setBackground(new java.awt.Color(255, 232, 214));
        confirmationPanel.setForeground(new java.awt.Color(255, 232, 214));
        confirmationPanel.setOpaque(false);
        confirmationPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        confirmButton.setBackground(new java.awt.Color(107, 112, 92));
        confirmButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        confirmButton.setForeground(new java.awt.Color(221, 190, 169));
        confirmButton.setText("CONFIRM");
        confirmButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        confirmCancelButton.setText("CANCEL");
        confirmCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmCancelButtonActionPerformed(evt);
            }
        });

        confirmBackButton.setText("BACK");
        confirmBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBackButtonActionPerformed(evt);
            }
        });

        jScrollPane3.setMaximumSize(new java.awt.Dimension(300, 350));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(300, 350));

        confirmList.setBackground(new java.awt.Color(107, 112, 92));
        confirmList.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        confirmList.setForeground(new java.awt.Color(255, 232, 214));
        confirmList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        confirmList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        confirmList.setPreferredSize(new java.awt.Dimension(275, 300));
        confirmList.setSelectionBackground(new java.awt.Color(221, 190, 169));
        confirmList.setSelectionForeground(new java.awt.Color(107, 112, 92));
        confirmList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                confirmListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(confirmList);

        javax.swing.GroupLayout confirmationPanelLayout = new javax.swing.GroupLayout(confirmationPanel);
        confirmationPanel.setLayout(confirmationPanelLayout);
        confirmationPanelLayout.setHorizontalGroup(
            confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirmationPanelLayout.createSequentialGroup()
                .addGroup(confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(confirmationPanelLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(confirmationPanelLayout.createSequentialGroup()
                                .addComponent(confirmBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(confirmationPanelLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(confirmCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        confirmationPanelLayout.setVerticalGroup(
            confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, confirmationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(confirmationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(confirmCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        middlePanel01.add(confirmationPanel, "card2");
        medNamePanel.setVisible(false);

        medInfoPanel.setBackground(new java.awt.Color(255, 232, 214));
        medInfoPanel.setForeground(new java.awt.Color(255, 232, 214));
        medInfoPanel.setOpaque(false);
        medInfoPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        medInfoBackButton.setText("BACK");
        medInfoBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                medInfoBackButtonActionPerformed(evt);
            }
        });

        jScrollPane4.setMaximumSize(new java.awt.Dimension(300, 350));
        jScrollPane4.setMinimumSize(new java.awt.Dimension(300, 350));

        medInfoList.setBackground(new java.awt.Color(107, 112, 92));
        medInfoList.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        medInfoList.setForeground(new java.awt.Color(255, 232, 214));
        medInfoList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        medInfoList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        medInfoList.setPreferredSize(new java.awt.Dimension(275, 300));
        medInfoList.setSelectionBackground(new java.awt.Color(221, 190, 169));
        medInfoList.setSelectionForeground(new java.awt.Color(107, 112, 92));
        medInfoList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                medInfoListMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(medInfoList);

        medInfoEditButton.setText("EDIT");
        medInfoEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                medInfoEditButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout medInfoPanelLayout = new javax.swing.GroupLayout(medInfoPanel);
        medInfoPanel.setLayout(medInfoPanelLayout);
        medInfoPanelLayout.setHorizontalGroup(
            medInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(medInfoPanelLayout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addGroup(medInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(medInfoPanelLayout.createSequentialGroup()
                        .addComponent(medInfoBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(medInfoEditButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        medInfoPanelLayout.setVerticalGroup(
            medInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, medInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(medInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(medInfoBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(medInfoEditButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(122, Short.MAX_VALUE))
        );

        middlePanel01.add(medInfoPanel, "card2");
        medInfoPanel.setVisible(false);

        editMedPanel.setBackground(new java.awt.Color(255, 232, 214));
        editMedPanel.setForeground(new java.awt.Color(255, 232, 214));
        editMedPanel.setOpaque(false);
        editMedPanel.setPreferredSize(new java.awt.Dimension(510, 600));

        editPanelConfirmButton.setBackground(new java.awt.Color(107, 112, 92));
        editPanelConfirmButton.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        editPanelConfirmButton.setForeground(new java.awt.Color(221, 190, 169));
        editPanelConfirmButton.setText("CONFIRM");
        editPanelConfirmButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        editPanelConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPanelConfirmButtonActionPerformed(evt);
            }
        });

        editPanelBackButton.setText("BACK");
        editPanelBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPanelBackButtonActionPerformed(evt);
            }
        });

        medNameEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        rxNumberEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        dosageNumberEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        dosageTypeEdit.setBackground(new java.awt.Color(107, 112, 92));
        dosageTypeEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        dosageTypeEdit.setForeground(new java.awt.Color(255, 232, 214));
        dosageTypeEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MG", "Tablet(s)", "Capsule(s)", "ML", "Injection(s)", "Puff(s)", "Application(s)", "Drop(s)" }));
        dosageTypeEdit.setToolTipText("Select the type of dosage");

        frequencyNumberEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        frequencyTypeEdit.setBackground(new java.awt.Color(107, 112, 92));
        frequencyTypeEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        frequencyTypeEdit.setForeground(new java.awt.Color(255, 232, 214));
        frequencyTypeEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A Day", "A Week", "A Month", "" }));
        frequencyTypeEdit.setToolTipText("");

        dosesProvEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        refillNumberEdit.setPreferredSize(new java.awt.Dimension(150, 25));

        jLabel2.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(107, 112, 92));
        jLabel2.setText("X");

        jLabel3.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Medication Name");

        jLabel4.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Prescription Number");

        jLabel5.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Dosage");

        jLabel6.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Frequency");

        jLabel7.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Doses Provided");

        jLabel8.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Refills Available?");

        howManyLabel.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        howManyLabel.setForeground(new java.awt.Color(0, 0, 0));
        howManyLabel.setText("How Many?");

        jLabel10.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Reminders?");

        jLabel11.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Require Again?");

        hourInEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        hourInEdit.setModel(new javax.swing.SpinnerListModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
        hourInEdit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        minuteInEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        minuteInEdit.setModel(new javax.swing.SpinnerListModel(new String[] {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"}));
        minuteInEdit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ampmInEdit.setFont(new java.awt.Font("Mongolian Baiti", 1, 18)); // NOI18N
        ampmInEdit.setModel(new javax.swing.SpinnerListModel(new String[] {"A.M.", "P.M."}));

        buttonGroup2.add(yesButtonRemindEdit);
        yesButtonRemindEdit.setText("YES");
        yesButtonRemindEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonRemindEditActionPerformed(evt);
            }
        });

        buttonGroup2.add(noButtonRemindEdit);
        noButtonRemindEdit.setText("NO");
        noButtonRemindEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonRemindEditActionPerformed(evt);
            }
        });

        buttonGroup1.add(yesButtonRefillsEdit);
        yesButtonRefillsEdit.setText("YES");

        buttonGroup1.add(noButtonRefillsEdit);
        noButtonRefillsEdit.setText("NO");

        buttonGroup3.add(yesButtonAgainEdit);
        yesButtonAgainEdit.setText("YES");
        yesButtonAgainEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonAgainEditActionPerformed(evt);
            }
        });

        buttonGroup3.add(noButtonAgainEdit);
        noButtonAgainEdit.setText("NO");

        javax.swing.GroupLayout editMedPanelLayout = new javax.swing.GroupLayout(editMedPanel);
        editMedPanel.setLayout(editMedPanelLayout);
        editMedPanelLayout.setHorizontalGroup(
            editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editMedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(howManyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(editMedPanelLayout.createSequentialGroup()
                        .addComponent(editPanelBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editPanelConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(editMedPanelLayout.createSequentialGroup()
                            .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(frequencyNumberEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dosageNumberEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dosageTypeEdit, 0, 1, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(frequencyTypeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(rxNumberEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(medNameEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refillNumberEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dosesProvEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(editMedPanelLayout.createSequentialGroup()
                            .addComponent(hourInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(minuteInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ampmInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(editMedPanelLayout.createSequentialGroup()
                            .addComponent(yesButtonRemindEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(noButtonRemindEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                            .addComponent(yesButtonRefillsEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(noButtonRefillsEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(editMedPanelLayout.createSequentialGroup()
                            .addComponent(yesButtonAgainEdit)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(noButtonAgainEdit))))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        editMedPanelLayout.setVerticalGroup(
            editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(medNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rxNumberEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosageNumberEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosageTypeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frequencyNumberEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frequencyTypeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dosesProvEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(yesButtonRefillsEdit)
                    .addComponent(noButtonRefillsEdit))
                .addGap(21, 21, 21)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refillNumberEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(howManyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editMedPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editMedPanelLayout.createSequentialGroup()
                        .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(yesButtonRemindEdit)
                            .addComponent(noButtonRemindEdit))
                        .addGap(18, 18, 18)))
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hourInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ampmInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(minuteInEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(yesButtonAgainEdit))
                    .addComponent(noButtonAgainEdit))
                .addGap(18, 18, 18)
                .addGroup(editMedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editPanelBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editPanelConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        refillNumberEdit.setVisible(false);
        howManyLabel.setVisible(false);
        hourIn.setVisible(false);
        minuteIn.setVisible(false);
        ampmIn.setVisible(false);

        middlePanel01.add(editMedPanel, "card2");
        medInfoPanel.setVisible(false);

        topPanel.setBackground(new java.awt.Color(255, 232, 214));
        topPanel.setPreferredSize(new java.awt.Dimension(510, 200));

        titleBar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Title Bar.png"))); // NOI18N
        titleBar.setAlignmentY(0.0F);
        titleBar.setIconTextGap(0);

        myMedsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/My Meds Clicked.png"))); // NOI18N
        myMedsButton.setBorderPainted(false);
        buttonBar.add(myMedsButton);
        myMedsButton.setContentAreaFilled(false);

        upcomingRemindersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Upcoming Reminders Unclicked.png"))); // NOI18N
        upcomingRemindersButton.setBorderPainted(false);
        buttonBar.add(upcomingRemindersButton);
        upcomingRemindersButton.setContentAreaFilled(false);

        iceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICE Unclicked.png"))); // NOI18N
        iceButton.setBorderPainted(false);
        buttonBar.add(iceButton);
        iceButton.setContentAreaFilled(false);

        profileOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Profile Options Unclicked.png"))); // NOI18N
        profileOptionsButton.setBorderPainted(false);
        buttonBar.add(profileOptionsButton);
        profileOptionsButton.setContentAreaFilled(false);
        profileOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileOptionsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(myMedsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upcomingRemindersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profileOptionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(48, 48, 48))
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profileOptionsButton)
                    .addComponent(iceButton)
                    .addComponent(upcomingRemindersButton)
                    .addComponent(myMedsButton))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(middlePanel01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(middlePanel01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void profileOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileOptionsButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profileOptionsButtonActionPerformed

    private void addNewMedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewMedButtonActionPerformed
       
        myMedsListPanel.setVisible(false);
        medNamePanel.setVisible(true);
        medNameIn.requestFocus();
        
    }//GEN-LAST:event_addNewMedButtonActionPerformed

    private void nameNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameNextButtonActionPerformed
            if(DLM.contains(medNameIn.getText()) && medInfoEditButton.isSelected() == false){
                JOptionPane.showMessageDialog(this, "This medication name already exists.\n" + 
                "Please enter another medication name\nOR\n" +
                "Enter a number after the medication name", "", 
                JOptionPane.WARNING_MESSAGE);
            }else{
                getMedName();
                medNamePanel.setVisible(false);
                rxNumberPanel.setVisible(true);
                rxNumberIn.requestFocus();

            }
        
    }//GEN-LAST:event_nameNextButtonActionPerformed

    
    private void rxNumberNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rxNumberNextButtonActionPerformed
        if(rxNumberIn.getText().isBlank()){
           selection = JOptionPane.showConfirmDialog(this, "Are you sure you don't want to\n"
                    + "enter a prescription number?", "", JOptionPane.YES_NO_OPTION);
           if(selection == 0){
               rxNumberPanel.setVisible(false);
               dosagePanel.setVisible(true);
           }
        }else{
            getRXNumber();
            rxNumberPanel.setVisible(false);
            dosagePanel.setVisible(true);
            dosageNumberIn.requestFocus();
        }
    }//GEN-LAST:event_rxNumberNextButtonActionPerformed

    private void dosageNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosageNextButtonActionPerformed
        
        getDosageNumber();
        getDosageType();
        dosagePanel.setVisible(false);
        frequencyPanel.setVisible(true);
        frequencyNumberIn.requestFocus();
            
    }//GEN-LAST:event_dosageNextButtonActionPerformed

    private void frequencyNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequencyNextButtonActionPerformed
   
        getFrequencyNumber();
        getFrequencyType();

        frequencyPanel.setVisible(false);
        dosesPanel.setVisible(true);
        dosesProvIn.requestFocus();
               
    }//GEN-LAST:event_frequencyNextButtonActionPerformed

    private void dosesProvNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosesProvNextButtonActionPerformed
        
        getDosesProv();
        dosesPanel.setVisible(false);
        refillsPanel.setVisible(true);
        
    }//GEN-LAST:event_dosesProvNextButtonActionPerformed

    private void refillNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refillNextButtonActionPerformed
        if(yesButton.isSelected() == true){
            yesButton.setSelected(true);
        }else if(noButton.isSelected() == true){
            noButton.setSelected(true);
        }
        getRefills(refills);
        refillsPanel.setVisible(false);
        reminderPanel.setVisible(true);
    }//GEN-LAST:event_refillNextButtonActionPerformed

    private void yesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonActionPerformed
        refills = true;
        refillNextButton.setEnabled(false);
        howMany.setVisible(true);
        refillNumberIn.setVisible(true);
        refillNumberIn.requestFocus();

    }//GEN-LAST:event_yesButtonActionPerformed

    private void noButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonActionPerformed
        refillNextButton.setEnabled(true);
        howMany.setVisible(false);
        refillNumberIn.setVisible(false);
        refills = false;
    }//GEN-LAST:event_noButtonActionPerformed

    private void remindNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remindNextButtonActionPerformed
        getReminder();
        setReminder();
        reminderPanel.setVisible(false);
        requireAgainPanel.setVisible(true);
    }//GEN-LAST:event_remindNextButtonActionPerformed
    /*
    the changeColor method is only an attempt to change the look and feel 
    of the spinner items. It is hardly noticeable but it is there
    */
    private void changeColor(){
        hourIn.setBackground(new java.awt.Color(107, 112, 92));
        hourIn.setForeground(new java.awt.Color(107, 112, 92));
        minuteIn.setBackground(new java.awt.Color(107, 112, 92));
        minuteIn.setForeground(new java.awt.Color(107, 112, 92));
        ampmIn.setBackground(new java.awt.Color(107, 112, 92));
        ampmIn.setForeground(new java.awt.Color(107, 112, 92));
    }
    private void yesButtonRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonRActionPerformed
        changeColor();
        hourIn.setVisible(true);
        minuteIn.setVisible(true);
        ampmIn.setVisible(true);
        remindNextButton.setEnabled(true);
    }//GEN-LAST:event_yesButtonRActionPerformed

    private void noButtonRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonRActionPerformed
        remindNextButton.setEnabled(true);
        hourIn.setVisible(false);
        minuteIn.setVisible(false);
        ampmIn.setVisible(false);
    }//GEN-LAST:event_noButtonRActionPerformed

    private void requireAgainNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requireAgainNextButtonActionPerformed
        getAgain(again);
        if(confirmBackButton.isSelected() == false){
            setConList( medName,  rxNumber,  dosageNumber,  dosageType,
                  frequencyNumber,  frequencyType,  dosesProv,  refills, 
                  refillNumber,  remind,  again,  reminder);
            confirmList.setModel(CL);
        }
        requireAgainPanel.setVisible(false);
        confirmationPanel.setVisible(true);
    }//GEN-LAST:event_requireAgainNextButtonActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
    
        updateDatabase(createSQLString(medName,rxNumber, dosageNumber, dosageType,
            frequencyNumber, frequencyType, dosesProv, refills, 
            refillNumber, remind, again, reminder));
        
        confirmationPanel.setVisible(false);
        myMedsListPanel.setVisible(true);
        String med = medName;
        DLM.addElement(medName);
        medList.setModel(DLM);
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void confirmListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirmListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmListMouseClicked

    private void nameBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameBackButtonActionPerformed
        medNamePanel.setVisible(false);
        myMedsListPanel.setVisible(true);
        MIL.clear();
        medInfoList.setModel(MIL);
    }//GEN-LAST:event_nameBackButtonActionPerformed

    private void nameCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameCancelButtonActionPerformed

        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            medNamePanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_nameCancelButtonActionPerformed

    private void rxNumberBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rxNumberBackButtonActionPerformed
        rxNumberPanel.setVisible(false);
        medNamePanel.setVisible(true);
    }//GEN-LAST:event_rxNumberBackButtonActionPerformed

    private void rxNumberCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rxNumberCancelButtonActionPerformed
        
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            rxNumberPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_rxNumberCancelButtonActionPerformed

    private void dosageBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosageBackButtonActionPerformed
        dosagePanel.setVisible(false);
        rxNumberPanel.setVisible(true);
    }//GEN-LAST:event_dosageBackButtonActionPerformed

    private void dosageCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosageCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            dosagePanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_dosageCancelButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        
        
        
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to\n" + "delete this medication?\n" + "This cannot be undone.", "", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            
            JOptionPane.showMessageDialog(this, medList.getSelectedValue() + " has been deleted.", "", JOptionPane.WARNING_MESSAGE);
            
            deleteItem();
            
   
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void frequencyBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequencyBackButtonActionPerformed
        frequencyPanel.setVisible(false);
        dosagePanel.setVisible(true);
    }//GEN-LAST:event_frequencyBackButtonActionPerformed

    private void frequencyCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequencyCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            frequencyPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            dosageNumberIn.setText("");
            rxNumberIn.setText("");
            medNameIn.setText("");
            frequencyNumberIn.setText("");
            frequencyTypeIn.setSelectedIndex(0);
        }
    }//GEN-LAST:event_frequencyCancelButtonActionPerformed

    private void dosesProvBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosesProvBackButtonActionPerformed
        dosesPanel.setVisible(false);
        frequencyPanel.setVisible(true);
    }//GEN-LAST:event_dosesProvBackButtonActionPerformed

    private void dosesProvCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosesProvCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
        
            dosesPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_dosesProvCancelButtonActionPerformed

    private void refillsBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refillsBackButtonActionPerformed
        refillsPanel.setVisible(false);
        dosesPanel.setVisible(true);
    }//GEN-LAST:event_refillsBackButtonActionPerformed

    private void refillsCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refillsCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){
            refillsPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
    }//GEN-LAST:event_refillsCancelButtonActionPerformed

    private void reminderCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reminderCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){

            reminderPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
        }
        
    }//GEN-LAST:event_reminderCancelButtonActionPerformed

    private void reminderBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reminderBackButtonActionPerformed
        reminderPanel.setVisible(false);
        refillsPanel.setVisible(true);
    }//GEN-LAST:event_reminderBackButtonActionPerformed

    private void requireAgainCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requireAgainCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){

            requireAgainPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();
            
        }
    }//GEN-LAST:event_requireAgainCancelButtonActionPerformed

    private void requireAgainBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requireAgainBackButtonActionPerformed
        
        requireAgainPanel.setVisible(false);
        reminderPanel.setVisible(true);
    }//GEN-LAST:event_requireAgainBackButtonActionPerformed

    private void confirmCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmCancelButtonActionPerformed
        choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel\n" + "adding this medication?\n", "", 
        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(choice == 0){

            confirmationPanel.setVisible(false);
            myMedsListPanel.setVisible(true);
            cancelAction();

            
        }
    }//GEN-LAST:event_confirmCancelButtonActionPerformed

    private void confirmBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBackButtonActionPerformed
        confirmBackButton.setSelected(true);
        confirmationPanel.setVisible(false);
        requireAgainPanel.setVisible(true);
        
    }//GEN-LAST:event_confirmBackButtonActionPerformed

    private void medInfoBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_medInfoBackButtonActionPerformed
        medInfoPanel.setVisible(false);
        MIL.removeAllElements();
        myMedsListPanel.setVisible(true);
    }//GEN-LAST:event_medInfoBackButtonActionPerformed

    private void medInfoListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_medInfoListMouseClicked
        int selected = medInfoList.getSelectedIndex();
        if(evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && evt.getButton() != MouseEvent.BUTTON2){
            switch(selected){
                case 0: //set up editing function here
            }
        }
    }//GEN-LAST:event_medInfoListMouseClicked

    private void medListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_medListMouseClicked
        if(evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1 && evt.getButton() != MouseEvent.BUTTON2){
            myMedsListPanel.setVisible(false);
            medInfoPanel.setVisible(true);
            getMedInfo();
        }
    }//GEN-LAST:event_medListMouseClicked

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        myMedsListPanel.setVisible(false);
        medInfoPanel.setVisible(true);
        getMedInfo();
        
    }//GEN-LAST:event_editButtonActionPerformed

    private void medInfoEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_medInfoEditButtonActionPerformed
        medInfoPanel.setVisible(false);
        
        editMedPanel.setVisible(true);
        try{
        editMedInfo();
                }catch(Exception e){
                    
                }
    }//GEN-LAST:event_medInfoEditButtonActionPerformed

    private void editPanelConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPanelConfirmButtonActionPerformed
        createUpdateString(medName, newRefillsBool, newReminders, newReminderTimes);
        updateDatabase(sqlString);
        DLM.removeAllElements();
        medList.setModel(DLM);
        MIL.removeAllElements();
        medInfoList.setModel(MIL);
        updateList();
        editMedPanel.setVisible(false);
        myMedsListPanel.setVisible(true);
        
    }//GEN-LAST:event_editPanelConfirmButtonActionPerformed

    private void editPanelBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPanelBackButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editPanelBackButtonActionPerformed

    private void yesButtonRemindEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonRemindEditActionPerformed
        getRemindersAgain();
        noButtonRemindEdit.setSelected(false);
        if(yesButtonRemindEdit.isSelected() == true){
            newReminders = "Yes";
        }
        hourInEdit.setVisible(true);
        minuteInEdit.setVisible(true);
        ampmInEdit.setVisible(true);
    }//GEN-LAST:event_yesButtonRemindEditActionPerformed

    private void noButtonRemindEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonRemindEditActionPerformed
        getRemindersAgain();
        yesButtonRemindEdit.setSelected(false);
        if(noButtonRemindEdit.isSelected() == true){
            newReminders = "No";
        }
        hourInEdit.setVisible(false);
        minuteInEdit.setVisible(false);
        ampmInEdit.setVisible(false);
    }//GEN-LAST:event_noButtonRemindEditActionPerformed

    private void yesButtonAgainEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonAgainEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_yesButtonAgainEditActionPerformed

    private void yesButtonAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonAActionPerformed
                               
        requireAgainNextButton.setEnabled(true);
        callMessage.setVisible(true);
    
    }//GEN-LAST:event_yesButtonAActionPerformed

    private void noButtonAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonAActionPerformed
        requireAgainNextButton.setEnabled(true);
        callMessage.setVisible(false);
    }//GEN-LAST:event_noButtonAActionPerformed

        
    /*
    The method that actually creates the connection to the local database
    */
    public static Connection ConnectDB()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:medsList.db");
            return conn;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
    
    public void updateDatabase(String sqlString) throws NullPointerException
    {
        conn = myMedsPage.ConnectDB();
        
        if (conn != null)
        {
            String sql = sqlString;
            
            try
            {
                int rows = 0;
                pst = conn.prepareStatement(sql);
                rows = pst.executeUpdate();

                pst.close();
                rs.close();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
         
    public void cancelAction(){
            dosageNumberIn.setText("");
            rxNumberIn.setText("");
            medNameIn.setText("");
            frequencyNumberIn.setText("");
            frequencyTypeIn.setSelectedIndex(0);
            dosesProvIn.setText("");
            yesButton.setSelected(false);
            noButton.setSelected(false);
            refillNumberIn.setText("");
            yesButtonR.setSelected(false);
            noButtonR.setSelected(false);
            hourIn.setVisible(false);
            hourIn.setValue("01");
            minuteIn.setVisible(false);
            minuteIn.setValue("00");
            ampmIn.setVisible(false);
            ampmIn.setValue("A.M.");
            yesButtonA.setSelected(false);
            noButtonA.setSelected(false);
            callMessage.setVisible(false);
            CL.clear();
            confirmList.setModel(CL);
            MIL.clear();
            medInfoList.setModel(MIL);
    }
    public void deleteItem(){
        
        conn = myMedsPage.ConnectDB();
        
        if(conn != null)
        {
            String sql = "DELETE FROM Medications WHERE MedName='" + medList.getSelectedValue().toString() + "';";
            String selected = medList.getSelectedValue().toString();
            try
            {
            int rows = 0;
            pst = conn.prepareStatement(sql);
            rows = pst.executeUpdate();
            
            pst.close();
            
            DLM.removeElement(selected);
            medList.setModel(DLM);
            
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    /*
    This is the main method that will show the information currently in the 
    my meds list table. When there is a connection and there is data to be read, 
    this method will Select the MedName from the Medications table and insert
    it row by row into the table on the my meds page.
    */
     public void updateList()
    {
        conn = myMedsPage.ConnectDB();
        
        if (conn != null)
        {
            String sql = "Select MedName FROM Medications;";
            
            try
            {
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()){
                                       
                    DLM.addElement(rs.getString("MedName"));
  
                }
                
                pst.close();
                rs.close();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
     
     

    
    /**
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(myMedsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(myMedsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(myMedsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(myMedsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new myMedsPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewMedButton;
    private javax.swing.JSpinner ampmIn;
    private javax.swing.JSpinner ampmInEdit;
    private javax.swing.ButtonGroup buttonBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JLabel callMessage;
    private javax.swing.JButton confirmBackButton;
    private javax.swing.JButton confirmButton;
    private javax.swing.JButton confirmCancelButton;
    private javax.swing.JList<String> confirmList;
    private javax.swing.JPanel confirmationPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton dosageBackButton;
    private javax.swing.JButton dosageCancelButton;
    private javax.swing.JLabel dosageInstruct;
    private javax.swing.JButton dosageNextButton;
    private javax.swing.JTextField dosageNumberEdit;
    private javax.swing.JTextField dosageNumberIn;
    private javax.swing.JPanel dosagePanel;
    private javax.swing.JComboBox<String> dosageTypeEdit;
    private javax.swing.JComboBox<String> dosageTypeIn;
    private javax.swing.JLabel dosesInstruct;
    private javax.swing.JPanel dosesPanel;
    private javax.swing.JButton dosesProvBackButton;
    private javax.swing.JButton dosesProvCancelButton;
    private javax.swing.JTextField dosesProvEdit;
    private javax.swing.JTextField dosesProvIn;
    private javax.swing.JButton dosesProvNextButton;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel editMedPanel;
    private javax.swing.JButton editPanelBackButton;
    private javax.swing.JButton editPanelConfirmButton;
    private javax.swing.JButton frequencyBackButton;
    private javax.swing.JButton frequencyCancelButton;
    private javax.swing.JLabel frequencyInstruct;
    private javax.swing.JButton frequencyNextButton;
    private javax.swing.JTextField frequencyNumberEdit;
    private javax.swing.JTextField frequencyNumberIn;
    private javax.swing.JPanel frequencyPanel;
    private javax.swing.JComboBox<String> frequencyTypeEdit;
    private javax.swing.JComboBox<String> frequencyTypeIn;
    private javax.swing.JSpinner hourIn;
    private javax.swing.JSpinner hourInEdit;
    private javax.swing.JLabel howMany;
    private javax.swing.JLabel howManyLabel;
    private javax.swing.JButton iceButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton medInfoBackButton;
    private javax.swing.JButton medInfoEditButton;
    private javax.swing.JList<String> medInfoList;
    private javax.swing.JPanel medInfoPanel;
    private javax.swing.JList<String> medList;
    private javax.swing.JTextField medNameEdit;
    private javax.swing.JTextField medNameIn;
    private javax.swing.JPanel medNamePanel;
    private javax.swing.JPanel middlePanel01;
    private javax.swing.JSpinner minuteIn;
    private javax.swing.JSpinner minuteInEdit;
    private javax.swing.JButton myMedsButton;
    private javax.swing.JPanel myMedsListPanel;
    private javax.swing.JButton nameBackButton;
    private javax.swing.JButton nameCancelButton;
    private javax.swing.JLabel nameInstruct;
    private javax.swing.JButton nameNextButton;
    private javax.swing.JButton noButton;
    private javax.swing.JToggleButton noButtonA;
    private javax.swing.JToggleButton noButtonAgainEdit;
    private javax.swing.JButton noButtonR;
    private javax.swing.JToggleButton noButtonRefillsEdit;
    private javax.swing.JToggleButton noButtonRemindEdit;
    private javax.swing.JButton profileOptionsButton;
    private javax.swing.JLabel refillInstruct;
    private javax.swing.JButton refillNextButton;
    private javax.swing.JTextField refillNumberEdit;
    private javax.swing.JTextField refillNumberIn;
    private javax.swing.JButton refillsBackButton;
    private javax.swing.JButton refillsCancelButton;
    private javax.swing.JPanel refillsPanel;
    private javax.swing.JButton remindNextButton;
    private javax.swing.JButton reminderBackButton;
    private javax.swing.JButton reminderCancelButton;
    private javax.swing.JLabel reminderInstruct;
    private javax.swing.JPanel reminderPanel;
    private javax.swing.JButton requireAgainBackButton;
    private javax.swing.JButton requireAgainCancelButton;
    private javax.swing.JLabel requireAgainInstruct;
    private javax.swing.JButton requireAgainNextButton;
    private javax.swing.JPanel requireAgainPanel;
    private javax.swing.JLabel rxNumInst;
    private javax.swing.JButton rxNumberBackButton;
    private javax.swing.JButton rxNumberCancelButton;
    private javax.swing.JTextField rxNumberEdit;
    private javax.swing.JTextField rxNumberIn;
    private javax.swing.JButton rxNumberNextButton;
    private javax.swing.JPanel rxNumberPanel;
    private javax.swing.JLabel titleBar;
    private javax.swing.JPanel topPanel;
    private javax.swing.JButton upcomingRemindersButton;
    private javax.swing.JButton yesButton;
    private javax.swing.JToggleButton yesButtonA;
    private javax.swing.JToggleButton yesButtonAgainEdit;
    private javax.swing.JButton yesButtonR;
    private javax.swing.JToggleButton yesButtonRefillsEdit;
    private javax.swing.JToggleButton yesButtonRemindEdit;
    // End of variables declaration//GEN-END:variables
}
