
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Makli
 */
public class ChartFrame extends javax.swing.JFrame {

    DefaultListModel<String> QueryListModel = new DefaultListModel<>();
    DefaultListModel<String> DocListModel = new DefaultListModel<>();
    DefaultListModel<String> RevDocListModel = new DefaultListModel<>();
    private PorterStemAnalyzer Analyzer;
    private Directory Index;
    
    /**
     * Creates new form ChartFrame
     * @param Analyzer
     * @param Index
     * @throws java.io.IOException
     * @throws org.apache.lucene.queryparser.classic.ParseException
     */
    public ChartFrame(PorterStemAnalyzer Analyzer, Directory Index) throws IOException, ParseException {
        initComponents();
        initComponentsVol2(Analyzer, Index);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooseQueryComboBox = new javax.swing.JComboBox();
        graphBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        chooseQueryComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chooseQueryComboBoxItemStateChanged(evt);
            }
        });

        graphBtn.setText("Show Graph");
        graphBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chooseQueryComboBox, 0, 476, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graphBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseQueryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(graphBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(239, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsVol2(PorterStemAnalyzer Analyzer, Directory Index) throws FileNotFoundException, IOException {
        // Set icon
        SetIcon();

        // Initialize analyzer and index variables
        this.Analyzer = Analyzer;
        this.Index = Index;
        
        // Set the graph button disabled
        graphBtn.setEnabled(false);
        
        // Get queries from txt file, add them to list model
        BufferedReader buffR = new BufferedReader(new FileReader("npl\\query-text"));
        String currLine = buffR.readLine();
        StringBuilder queryBuffer = new StringBuilder();
        int iterator = 0;
            
        while (currLine != null) {
            // Check if current line holds the query ID
            if (currLine.matches(".*\\d+.*")) {
                queryBuffer.append(currLine);
                currLine = buffR.readLine();
                queryBuffer.append(".").append(" ").append(currLine);
                
                QueryListModel.add(iterator, queryBuffer.toString());
                queryBuffer.delete(0, queryBuffer.length());
                iterator++;
            }
            
            currLine = buffR.readLine();
        }
        
        // Populate the combobox
        for (int i = 0; i < QueryListModel.size(); i++) {
            chooseQueryComboBox.insertItemAt(QueryListModel.elementAt(i), i);
        }
    }
    
    private void graphBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphBtnActionPerformed
        // Get selected query's string value from combobox
        String query = chooseQueryComboBox.getSelectedItem().toString().subSequence(3, chooseQueryComboBox.getSelectedItem().toString().length()).toString();
        
        // Create new instance of GUIFrame class in order to call the SearchResults function and populate DocListModel
        try {
            GUIFrame GuiFunc = new GUIFrame();
            GuiFunc.SearchResults(this.Analyzer, this.Index, query, DocListModel);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(ChartFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Get the relevant docs from the "rlv-ass" file and populate RevDocListModel
        int queryID = chooseQueryComboBox.getSelectedIndex() + 1;       // Query ID
        
        // Get queries from txt file, add them to list model
        try {
            BufferedReader buffR = new BufferedReader(new FileReader("npl\\rlv-ass"));
            String strQueryID = Integer.toString(queryID);   
            String currLine = buffR.readLine();
            StringBuilder currDocBuffer = new StringBuilder();
            int iterator = 0;

            while (currLine != null) {
                // Check if current line holds the query ID
                if (currLine.equalsIgnoreCase(strQueryID)) {
                    currLine = buffR.readLine();                   
                    
                    while (!(currLine.contains("/"))) {
                        for (int i = 0; i < currLine.length(); i++) {   // Scan the whole line, put numbers into buffer until white space or end of line
                            String currChar = Character.toString(currLine.charAt(i));
                            if (currChar.matches(".*\\d+.*")) {
                                currDocBuffer.append(currChar);
                                
                                if (i == currLine.length() - 1) {
                                    if (!(currDocBuffer.toString().isEmpty())) {
                                        // If end of line and buffer has characters, then add the buffer value (toString) in the list
                                        RevDocListModel.add(iterator, currDocBuffer.toString());
                                        currDocBuffer.delete(0, currDocBuffer.length());
                                        iterator++;
                                    }
                                }
                            } else {
                                if (!(currDocBuffer.toString().isEmpty())) {
                                    // If white space and buffer has characters, then add the buffer value (toString) in the list
                                    RevDocListModel.add(iterator, currDocBuffer.toString());
                                    currDocBuffer.delete(0, currDocBuffer.length());
                                    iterator++;
                                }
                            }
                        }
                        currLine = buffR.readLine();
                    }
                    
                    break;
                }

                currLine = buffR.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChartFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Create and show the graph JFrame that holds the graphic representation
        Graph chartFrame = new Graph(chooseQueryComboBox.getSelectedItem().toString(), DocListModel, RevDocListModel);
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setResizable(false);
        chartFrame.pack();
        chartFrame.setLocationRelativeTo(this);
        chartFrame.setVisible(true);
        
        // Clear list models
        DocListModel.clear();
        RevDocListModel.clear();
    }//GEN-LAST:event_graphBtnActionPerformed

    private void chooseQueryComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chooseQueryComboBoxItemStateChanged
        int selectedIndex = chooseQueryComboBox.getSelectedIndex() + 1;
        if (selectedIndex != 0) {
            graphBtn.setEnabled(true);
        } else {
            graphBtn.setEnabled(false);
        }
    }//GEN-LAST:event_chooseQueryComboBoxItemStateChanged

    private void SetIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("IRicon.png")));
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                PorterStemAnalyzer Analyzer = null;
                Directory Index = null;
                
                try {
                    new ChartFrame(Analyzer, Index).setVisible(true);
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(ChartFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox chooseQueryComboBox;
    private javax.swing.JButton graphBtn;
    // End of variables declaration//GEN-END:variables
}
