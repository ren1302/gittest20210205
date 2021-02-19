package swingPractice;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CustomerGUI {
	private JFrame f;
	private JTable table;
	private JTextField tf1;
	private JTextField tf2;
	private JTextField tf3;
	private JTextField tf4;
	private JTextField tf5;
	private JButton btnSelect;
	private CustomerDAO cd = CustomerDAO.getInstance();
	
	
	public CustomerGUI() {	
		showFrame();
	}
	public void showFrame() {
		f = new JFrame("회원 관리 프로그램");
		f.setSize(800, 300);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel lbl1 = new JLabel("번 호");
		JLabel lbl2 = new JLabel("이 름");
		JLabel lbl3 = new JLabel("나 이");
		JLabel lbl4 = new JLabel("이 메 일");
		JLabel lbl5 = new JLabel("주민번호");
		
		tf1 = new JTextField(10);
		tf1.setEditable(false);
		tf2 = new JTextField(10);
		tf3 = new JTextField(10);
		tf4 = new JTextField(10);
		tf5 = new JTextField(10);
		
		JPanel pnl = new JPanel(new GridLayout(5,1));
		JPanel pnl1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel pnl3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel pnl4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel pnl5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		pnl1.add(lbl1);
		pnl1.add(tf1);
		pnl2.add(lbl2);
		pnl2.add(tf2);
		pnl3.add(lbl3);
		pnl3.add(tf3);
		pnl4.add(lbl4);
		pnl4.add(tf4);
		pnl5.add(lbl5);
		pnl5.add(tf5);
		
		pnl.add(pnl1);
		pnl.add(pnl2);
		pnl.add(pnl3);
		pnl.add(pnl4);
		pnl.add(pnl5);
		JPanel pnlSouth = new JPanel();
		JButton btnInsert = new JButton("회원추가");
		btnSelect = new JButton("회원목록");
		JButton btnDelete = new JButton("회원삭제");
		pnlSouth.add(btnInsert);
		pnlSouth.add(btnSelect);
		pnlSouth.add(btnDelete);
		
		JScrollPane sp = new JScrollPane();
		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		sp.setViewportView(table);
		Vector<String> columnNames = new Vector<String>(Arrays.asList("번호","이름","나이","이메일","주민번호"));
		DefaultTableModel dtm = new DefaultTableModel(columnNames,0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(dtm);
		
		f.add(sp,BorderLayout.CENTER);
		f.add(pnl,BorderLayout.WEST);
		f.add(pnlSouth,BorderLayout.SOUTH);
		
		ActionListener al = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn =  (JButton) e.getSource();
				if(btn == btnInsert) {
					String name = tf2.getText();
					String age = tf3.getText();
					String email = tf4.getText();
					String jumin = tf5.getText();
					
					if(name.length()==0) {
						JOptionPane.showMessageDialog(f, "이름을 입력해주세요");
						tf2.requestFocus();
						return;
					} else if(age.length()==0) {
						JOptionPane.showMessageDialog(f, "나이를 입력해주세요");
						tf3.requestFocus();
						return;
					} else if(email.length()==0) {
						JOptionPane.showMessageDialog(f, "메일을 입력해주세요");
						tf4.requestFocus();
						return;
					} else if(jumin.length()==0) {
						JOptionPane.showMessageDialog(f, "주민번호를 입력해주세요");
						tf5.requestFocus();
						return;
					} else {
						String agePattern = "^[0-9]{1,3}$";
						if(!Pattern.matches(agePattern, age)) {
							JOptionPane.showMessageDialog(f, "나이는 1~3자리 숫자만가능합니다");
							tf3.requestFocus();
						} else {
							insert();
						}
					}

				} 
				if(btn == btnSelect) {
					select();
				}
				if(btn == btnDelete) {
					delete();
				}
			}
		};
		
		
		btnInsert.addActionListener(al);
		btnSelect.addActionListener(al);
		btnDelete.addActionListener(al);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tf1.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				tf2.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
				tf3.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				tf4.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
				tf5.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
				
				
			}
		});
		
		
		f.setVisible(true);
	}
	
	
	
	public void select() {
		Vector<CustomerDTO> customers = cd.select();
		
		DefaultTableModel myModel = (DefaultTableModel)table.getModel();
		myModel.setRowCount(0);
		for(CustomerDTO customer : customers) {
			Vector<Object> rowData = new Vector<Object>();
			rowData.add(customer.getIdx());
			rowData.add(customer.getName());
			rowData.add(customer.getAge());
			rowData.add(customer.getEmail());
			rowData.add(customer.getJumin());
			myModel.addRow(rowData);
		}
	}
	public void insert() {
		
		String name = tf2.getText();
		int age = Integer.parseInt(tf3.getText());
		String email = tf4.getText();
		String jumin = tf5.getText();

		boolean isInserted = cd.insert(new CustomerDTO(0,name, age, email, jumin));
		if(isInserted) {
			JOptionPane.showMessageDialog(f, "회원가입성공");
			tf2.setText("");
			tf3.setText("");
			tf4.setText("");
			tf5.setText("");
			btnSelect.doClick();
			
			
		} else {
			JOptionPane.showMessageDialog(f, "회원가입실패");
		}
		
		
		
	}
	public void delete() {
		
		String idx = JOptionPane.showInputDialog(f,"삭제할 회원번호를 입력하세요");
		
		if(idx != null) {
			int result = JOptionPane.showConfirmDialog(f, "정말 삭제 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
			boolean isDeleted = false;
			
			if(result == JOptionPane.YES_OPTION) {
				isDeleted = cd.delete(Integer.parseInt(idx));
				if(isDeleted) {
					JOptionPane.showMessageDialog(f, "삭제완료");
					select();
				} else {
					JOptionPane.showMessageDialog(f, "삭제실패");
				}
			} else {
				return;
			}
			
		}
		
		
	}
	
		

	public static void main(String[] args) {
		new CustomerGUI();
	}
}
