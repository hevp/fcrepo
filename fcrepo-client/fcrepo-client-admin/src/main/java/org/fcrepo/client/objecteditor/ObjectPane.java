/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */
package org.fcrepo.client.objecteditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.fcrepo.client.Administrator;
import org.fcrepo.client.actions.ExportObject;
import org.fcrepo.client.actions.PurgeObject;
import org.fcrepo.client.actions.ViewObjectXML;


/**
 * Displays an object's attributes, allowing the editing of some. Also provides
 * buttons for performing object-wide operations, such as viewing and exporting
 * XML.
 *
 * @author Chris Wilper
 */
public class ObjectPane
        extends EditingPane {

    private static final long serialVersionUID = 1L;

    private final String m_pid;

    private String m_state;

    private String m_shareLevel;

    private String m_locked;

    private String m_label;

    private String m_ownerId;

    private final JComboBox<String> m_stateComboBox;

    private final JComboBox<String> m_shareLevelComboBox;

    private final JCheckBox m_lockedCheckBox;

    private final JTextField m_labelTextField;

    private final JTextField m_ownerIdTextField;

    private final Dimension m_labelDims;

    /**
     * Build the pane.
     */
    public ObjectPane(ObjectEditorFrame owner,
                      String pid,
                      String state,
                      String shareLevel,
                      String locked,
                      String label,
                      String cDate,
                      String mDate,
                      String ownerId)
            throws Exception {
        super(owner, null, null);
        m_pid = pid;
        m_state = state;
        m_shareLevel = shareLevel;
        m_locked = locked;
        m_label = label;
        m_ownerId = ownerId;
        if (ownerId == null) {
            m_ownerId = "";
        }
        m_labelDims = new JLabel("Content Model").getPreferredSize();

        // mainPane(valuePane, actionPane)

        // CENTER: valuePane(northValuePane)

        // NORTH: northValuePane(state, label, cModel, cDate, mDate, ownerId)

        // LEFT: Labels
        JLabel stateLabel = new JLabel("State");
        stateLabel.setPreferredSize(m_labelDims);
        JLabel shareLevelLabel = new JLabel("Share level");
        shareLevelLabel.setPreferredSize(m_labelDims);
        JLabel lockedLabel = new JLabel("Locked");
        lockedLabel.setPreferredSize(m_labelDims);
        JLabel labelLabel = new JLabel("Label");
        labelLabel.setPreferredSize(m_labelDims);
        JLabel cModelLabel = new JLabel("Content Model");
        cModelLabel.setPreferredSize(m_labelDims);
        JLabel cDateLabel = new JLabel("Created");
        cDateLabel.setPreferredSize(m_labelDims);
        JLabel mDateLabel = new JLabel("Modified");
        mDateLabel.setPreferredSize(m_labelDims);
        JLabel ownerIdLabel = new JLabel("Owner");
        ownerIdLabel.setPreferredSize(m_labelDims);
        JLabel[] labels =
                new JLabel[] {stateLabel, shareLevelLabel, lockedLabel, labelLabel, cDateLabel,
                        mDateLabel, ownerIdLabel};

        // RIGHT: Values
        // Object state
        String[] comboBoxStrings = {"Active", "Submitted", "Inactive", "Deleted"};
        m_stateComboBox = new JComboBox<String>(comboBoxStrings);
        Administrator.constrainHeight(m_stateComboBox);
        if (state.equals("A")) {
            m_stateComboBox.setSelectedIndex(0);
            m_stateComboBox.setBackground(Administrator.ACTIVE_COLOR);
        } else if (state.equals("S")) {
            m_stateComboBox.setSelectedIndex(1);
            m_stateComboBox.setBackground(Administrator.SUBMITTED_COLOR);
        } else if (state.equals("I")) {
            m_stateComboBox.setSelectedIndex(1);
            m_stateComboBox.setBackground(Administrator.INACTIVE_COLOR);
        } else {
            m_stateComboBox.setSelectedIndex(2);
            m_stateComboBox.setBackground(Administrator.DELETED_COLOR);
        }

        m_stateComboBox.addActionListener(dataChangeListener);
        m_stateComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (m_stateComboBox.getSelectedIndex() == 0) {
                    m_stateComboBox.setBackground(Administrator.ACTIVE_COLOR);
                } else if (m_stateComboBox.getSelectedIndex() == 1) {
                    m_stateComboBox.setBackground(Administrator.SUBMITTED_COLOR);
                } else if (m_stateComboBox.getSelectedIndex() == 2) {
                    m_stateComboBox.setBackground(Administrator.INACTIVE_COLOR);
                } else {
                    m_stateComboBox.setBackground(Administrator.DELETED_COLOR);
                }
            }
        });

        // Object share level
        String[] shareLevelComboBoxStrings = {"Open access", "Registered", "Private"};
        m_shareLevelComboBox = new JComboBox<String>(shareLevelComboBoxStrings);
        Administrator.constrainHeight(m_shareLevelComboBox);
        if (shareLevel.equals("O")) {
            m_shareLevelComboBox.setSelectedIndex(0);
            m_shareLevelComboBox.setBackground(Administrator.OPEN_COLOR);
        } else if (shareLevel.equals("R")) {
            m_shareLevelComboBox.setSelectedIndex(1);
            m_shareLevelComboBox.setBackground(Administrator.REGISTERED_COLOR);
        } else {
            m_shareLevelComboBox.setSelectedIndex(2);
            m_shareLevelComboBox.setBackground(Administrator.PRIVATE_COLOR);
        }

        m_shareLevelComboBox.addActionListener(dataChangeListener);
        m_shareLevelComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (m_shareLevelComboBox.getSelectedIndex() == 0) {
                    m_shareLevelComboBox.setBackground(Administrator.OPEN_COLOR);
                } else if (m_shareLevelComboBox.getSelectedIndex() == 1) {
                    m_shareLevelComboBox.setBackground(Administrator.REGISTERED_COLOR);
                } else {
                    m_shareLevelComboBox.setBackground(Administrator.PRIVATE_COLOR);
                }
            }
        });


        // Object locked
        m_lockedCheckBox = new JCheckBox("Locked");
        Administrator.constrainHeight(m_lockedCheckBox);

        // original
        m_labelTextField = new JTextField(label);
        m_labelTextField.getDocument().addDocumentListener(dataChangeListener);
        m_ownerIdTextField = new JTextField(ownerId);
        m_ownerIdTextField.getDocument()
                .addDocumentListener(dataChangeListener);
        // non-editables:
        JTextArea cDateValueLabel = new JTextArea(cDate);
        cDateValueLabel.setBackground(Administrator.BACKGROUND_COLOR);
        cDateValueLabel.setEditable(false);
        JTextArea mDateValueLabel = new JTextArea(mDate);
        mDateValueLabel.setBackground(Administrator.BACKGROUND_COLOR);
        mDateValueLabel.setEditable(false);

        JComponent[] values =
                new JComponent[] {m_stateComboBox, m_shareLevelComboBox, m_lockedCheckBox,
                    m_labelTextField, cDateValueLabel, mDateValueLabel, m_ownerIdTextField};

        JPanel northValuePane = new JPanel();
        GridBagLayout gridBag = new GridBagLayout();
        northValuePane.setLayout(gridBag);
        addLabelValueRows(labels, values, gridBag, northValuePane);

        // EAST: actionPane(northActionPane)

        // NORTH: northActionPane(viewButton, exportButton)
        JPanel viewPane = new JPanel();
        JButton viewButton = new JButton(new ViewObjectXML(pid, viewPane));
        viewButton.setText("View XML");
        Administrator.constrainHeight(viewButton);
        JButton exportButton = new JButton(new ExportObject(pid));
        exportButton.setText("Export...");
        Administrator.constrainHeight(exportButton);
        JButton purgeButton = new JButton(new PurgeObject(owner, pid));
        purgeButton.setText("Purge...");
        Administrator.constrainHeight(purgeButton);

        JPanel actionPane = new JPanel(new FlowLayout());
        actionPane.add(viewButton);
        actionPane.add(exportButton);
        actionPane.add(purgeButton);

        mainPane.setLayout(new BorderLayout());
        mainPane.add(northValuePane, BorderLayout.NORTH);
        mainPane.add(viewPane, BorderLayout.CENTER);
        mainPane.add(actionPane, BorderLayout.SOUTH);
    }

    public boolean isDirty() {
        if (!m_labelTextField.getText().equals(m_label)) {
            return true;
        }
        if (!m_ownerIdTextField.getText().equals(m_ownerId)) {
            return true;
        }
        int origIndex = 0;
        if (m_state.equals("I")) {
            origIndex = 1;
        } else if (m_state.equals("D")) {
            origIndex = 2;
        }
        if (m_stateComboBox.getSelectedIndex() != origIndex) {
            return true;
        }
        return false;
    }

    @Override
    public void saveChanges(String logMessage) throws Exception {
        String state = null;
        int i = m_stateComboBox.getSelectedIndex();
        if (i == 0) {
            state = "A";
        }
        if (i == 1) {
            state = "I";
        }
        if (i == 2) {
            state = "D";
        }
        Administrator.APIM.modifyObject(m_pid, state, m_shareLevel, m_locked, m_labelTextField
                .getText(), m_ownerIdTextField.getText(), logMessage);
    }

    @Override
    public void changesSaved() {
        int i = m_stateComboBox.getSelectedIndex();
        if (i == 0) {
            m_state = "A";
        }
        if (i == 1) {
            m_state = "I";
        }
        if (i == 2) {
            m_state = "D";
        }
        m_label = m_labelTextField.getText();
        m_ownerId = m_ownerIdTextField.getText();
    }

    @Override
    public void undoChanges() {
        if (m_state.equals("A")) {
            m_stateComboBox.setSelectedIndex(0);
            m_stateComboBox.setBackground(Administrator.ACTIVE_COLOR);
        } else if (m_state.equals("I")) {
            m_stateComboBox.setSelectedIndex(1);
            m_stateComboBox.setBackground(Administrator.INACTIVE_COLOR);
        } else if (m_state.equals("D")) {
            m_stateComboBox.setSelectedIndex(2);
            m_stateComboBox.setBackground(Administrator.DELETED_COLOR);
        }
        m_labelTextField.setText(m_label);
        m_ownerIdTextField.setText(m_ownerId);
    }

}
