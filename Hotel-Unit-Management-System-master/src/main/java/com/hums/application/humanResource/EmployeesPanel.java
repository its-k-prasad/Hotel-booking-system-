package com.hums.application.humanResource;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.hums.humanResourceManagementSystem.*;
import com.hums.tools.data.FileHandling;


public class EmployeesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static JTable employeesTable;
	private static DefaultTableModel employeesTableModel;
	private JButton buttonDeleteEmployee;
	private JButton buttonEditEmployee;

	/**
	 * Create the panel.
	 */
	public EmployeesPanel() {
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
		);
		
		buttonEditEmployee = new JButton("Edit Employee");
		buttonEditEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				int row= employeesTable.getSelectedRow();
				if( employeesTableModel.getRowCount()!=0 && row !=-1)
				{
					String lastname= (String)employeesTableModel.getValueAt(row, 0);
					String firstname=(String)employeesTableModel.getValueAt(row, 1);
					Employee employeeToEdit= HRMS_Registry.getInstance().getEmpList().searchEmployee(firstname, lastname);
					HR_Frame.showEditEmployeePanel(employeeToEdit);
				}
				
			}
		});
		panel.add(buttonEditEmployee);
		
		buttonDeleteEmployee = new JButton("Delete Employee");
		buttonDeleteEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = employeesTable.getSelectedRow();
				if(employeesTableModel.getRowCount()!=0 && row != -1) {
					String lastName =(String) employeesTableModel.getValueAt(row, 0);
					String firstName =(String) employeesTableModel.getValueAt(row, 1);
					Employee emp = HRMS_Registry.getInstance().getEmpList().searchEmployee(firstName, lastName);
					HRMS_Registry.getInstance().getEmpList().removeEmployee(emp);
					updateModel();
					FileHandling.exportToFile(HRMS_Registry.getInstance());
				}
			}
		});
		panel.add(buttonDeleteEmployee);
		
		employeesTable = new JTable();
		
		employeesTableModel = new DefaultTableModel(null, new String[] {
				"Last Name", "First Name", "Phone", "Email", "Address", "SSN", "Type"
			}) {
				private static final long serialVersionUID = 1L;

				@Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
			};
			
		employeesTable.setModel(employeesTableModel);
		updateModel();
			
		scrollPane.setViewportView(employeesTable);
		setLayout(groupLayout);
		
	}
	
	public static void updateModel() {
		HRMS_Registry reg= HRMS_Registry.getInstance();
		
		employeesTableModel.setRowCount(0);
		for(Employee employee: reg.getEmpList().getEmployees())
		{
			employeesTableModel.addRow(new Object[] {employee.getLastname(),employee.getFirstname(),
					employee.getPhone(),employee.getEmail(),
					employee.getAddress(),employee.getSsn(),employee.getType(),employee.getSalary()});
		}
		employeesTableModel.fireTableDataChanged();
		
	}

}
