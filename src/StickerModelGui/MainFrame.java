/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StickerModelGui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import javax.swing.SwingWorker;
import stickermodeljava.AlgorithmType;
import stickermodeljava.TestResults;

/**
 *
 * @author jared
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        
        //Setup the resultsArea as the standard out
        PrintStream out = new PrintStream(new TextAreaOutputStream(resultsArea));
        System.setOut(out);
        System.setErr(out);
        
        //Initialize button states
        resetButton.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        structureTextArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        percentDefectsLabel = new javax.swing.JLabel();
        totIterLabel = new javax.swing.JLabel();
        temperatureLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        mfeLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        badProbLabel = new javax.swing.JLabel();
        simButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        inputFileField = new javax.swing.JTextField();
        outputFileField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultsArea = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        timeLimitField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        strandLengthField = new javax.swing.JFormattedTextField();
        relaxedButton = new javax.swing.JRadioButton();
        vanillaButton = new javax.swing.JRadioButton();
        annealingButton = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DNA Compiler");

        structureTextArea.setColumns(20);
        structureTextArea.setRows(5);
        jScrollPane2.setViewportView(structureTextArea);

        jLabel2.setText("Minimum Free Energy (MFE) Structure");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Stats"));

        jLabel1.setText("Percent Defect");

        jLabel3.setText("Total Iterations");

        jLabel4.setText("Temperature");

        percentDefectsLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        percentDefectsLabel.setText("0.0");

        totIterLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        totIterLabel.setText("0");

        temperatureLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        temperatureLabel.setText("0.0");

        jLabel10.setText("Minimum Free Energy (MFE)");

        mfeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        mfeLabel.setText("0.0");

        jLabel11.setText("Bad Acceptance Probability");

        badProbLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        badProbLabel.setText("0.9");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(badProbLabel)
                            .addComponent(mfeLabel)
                            .addComponent(temperatureLabel)
                            .addComponent(totIterLabel)
                            .addComponent(percentDefectsLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(percentDefectsLabel)
                .addGap(22, 22, 22)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totIterLabel)
                .addGap(23, 23, 23)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(temperatureLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mfeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(badProbLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        simButton.setText("Run");
        simButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simButtonActionPerformed(evt);
            }
        });

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Input File");

        jLabel6.setText("Output File");

        inputFileField.setText("bigTest.txt");

        outputFileField.setText("testOutput.txt");

        jLabel7.setText("Console Output");

        resultsArea.setColumns(20);
        resultsArea.setRows(5);
        jScrollPane1.setViewportView(resultsArea);

        jLabel8.setText("Time Limit");

        timeLimitField.setText("15");

        jLabel9.setText("Strand Length");

        strandLengthField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        strandLengthField.setText("15");

        buttonGroup1.add(relaxedButton);
        relaxedButton.setSelected(true);
        relaxedButton.setText("Relaxed Greedy");

        buttonGroup1.add(vanillaButton);
        vanillaButton.setText("Vanilla Hill-Climber");

        buttonGroup1.add(annealingButton);
        annealingButton.setText("Simulated Annealing");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(simButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(resetButton)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(inputFileField)
                                    .addComponent(outputFileField, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(timeLimitField, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                    .addComponent(strandLengthField)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(555, 555, 555)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(vanillaButton)
                                    .addComponent(relaxedButton)
                                    .addComponent(annealingButton))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(inputFileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(timeLimitField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(outputFileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(strandLengthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(simButton)
                            .addComponent(resetButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(relaxedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vanillaButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(annealingButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simButtonActionPerformed
        //Clear console output
        resultsArea.setText(null);
        
        //Get selected algorithm type
        AlgorithmType algorithmType;
        if(relaxedButton.isSelected()) {
            algorithmType = AlgorithmType.Relaxed;
        } else if (vanillaButton.isSelected()) {
            algorithmType = AlgorithmType.Vanilla;
        } else if (annealingButton.isSelected()) {
            algorithmType = AlgorithmType.Annealing;
        } else { //Nothing selected
            resultsArea.append("No algorithm selected.  Please make a selection and restart.\n");
            return;
        }
        
        //Create new sim
        sim = new StickerModelExecutor(inputFileField.getText(), outputFileField.getText(),
                Integer.parseInt(timeLimitField.getText()), Integer.parseInt(strandLengthField.getText()), algorithmType);
        
        //Setup listener for state of sim
        sim.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                simPropertyChanged(evt);
            }
        });
        
        //Run sim
        sim.execute();
        simButton.setEnabled(false);
        resetButton.setEnabled(true);
    }//GEN-LAST:event_simButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        sim.cancel(true);
        simButton.setEnabled(true);
        resetButton.setEnabled(false);
    }//GEN-LAST:event_resetButtonActionPerformed

    private void simPropertyChanged(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("state") && evt.getNewValue().equals(SwingWorker.StateValue.DONE)) {
            simButton.setEnabled(true);
            resetButton.setEnabled(false);
        } else if (evt.getPropertyName().equals("lastRunTestResults")) {
            //Set new strand structure in text box
            structureTextArea.setText(null);
            TestResults results = (TestResults)evt.getNewValue();

            //Format strands for output by putting strands on seperate lines
            String[] strands = results.getSecondaryStructure().split("\\+");
            for(String strand : strands)
                structureTextArea.append(strand + "\n");
            
            //Update numeric fields
            //comboProbLabel.setText(Double.toString(results.getOverallNoncombiningProbability()));
            mfeLabel.setText(Double.toString(results.getMFE()));
        } else if (evt.getPropertyName().equals("lastNumIterations")) {
            totIterLabel.setText(Integer.toString((int)evt.getNewValue()));
        } else if (evt.getPropertyName().equals("temperature")) {
            temperatureLabel.setText(Double.toString((double)evt.getNewValue()));
        } else if (evt.getPropertyName().equals("nonDefectPercentage")) {
            percentDefectsLabel.setText(Double.toString((double)evt.getNewValue()));
        } else if (evt.getPropertyName().equals("acceptBadProb")) {
            badProbLabel.setText(Double.toString((double)evt.getNewValue()));
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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton annealingButton;
    private javax.swing.JLabel badProbLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField inputFileField;
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel mfeLabel;
    private javax.swing.JTextField outputFileField;
    private javax.swing.JLabel percentDefectsLabel;
    private javax.swing.JRadioButton relaxedButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JTextArea resultsArea;
    private javax.swing.JButton simButton;
    private javax.swing.JFormattedTextField strandLengthField;
    private javax.swing.JTextArea structureTextArea;
    private javax.swing.JLabel temperatureLabel;
    private javax.swing.JTextField timeLimitField;
    private javax.swing.JLabel totIterLabel;
    private javax.swing.JRadioButton vanillaButton;
    // End of variables declaration//GEN-END:variables
    
    private StickerModelExecutor sim;
}
