package view;

import dao.BaiThiDAO;
import dao.CapHocDAO;
import dao.MonHocDAO;
import model.CapHoc;
import model.MonHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class pnlXemBangXepHang extends javax.swing.JPanel {

        private BaiThiDAO baiThiDAO = new BaiThiDAO();
        private MonHocDAO monHocDAO = new MonHocDAO();
        private CapHocDAO capHocDAO = new CapHocDAO();
        private DefaultTableModel tableModel;

        public pnlXemBangXepHang() {
                initComponents();
                setupTable();
                loadFilters();
                loadData();
        }

        private void setupTable() {
                tableModel = new DefaultTableModel(
                                new Object[][] {},
                                new String[] { "Hạng", "Họ tên", "Môn học", "Cấp học", "Điểm", "Số câu đúng",
                                                "Thời gian nộp" }) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tblXepHang.setModel(tableModel);
                tblXepHang.getColumnModel().getColumn(0).setPreferredWidth(50);
                tblXepHang.getColumnModel().getColumn(1).setPreferredWidth(200);
                tblXepHang.setRowHeight(30);
        }

        private void loadFilters() {
                cbMonHoc.removeAllItems();
                cbMonHoc.addItem("Tất cả");
                for (MonHoc m : monHocDAO.getAll()) {
                        cbMonHoc.addItem(m.getTenMonHoc());
                }

                cbCapHoc.removeAllItems();
                cbCapHoc.addItem("Tất cả");
                for (CapHoc c : capHocDAO.getAll()) {
                        cbCapHoc.addItem(c.getTenCapHoc());
                }
        }

        public void loadData() {
                String name = txtSearch.getText().trim();
                String subject = (String) cbMonHoc.getSelectedItem();
                String grade = (String) cbCapHoc.getSelectedItem();

                List<Object[]> data = baiThiDAO.getBangXepHang(name, subject, grade);
                tableModel.setRowCount(0);
                for (Object[] row : data) {
                        tableModel.addRow(row);
                }
        }

        @SuppressWarnings("unchecked")
        private void initComponents() {
                jLabel1 = new javax.swing.JLabel();
                jPanelFilters = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                txtSearch = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                cbMonHoc = new javax.swing.JComboBox<>();
                jLabel4 = new javax.swing.JLabel();
                cbCapHoc = new javax.swing.JComboBox<>();
                btnSearch = new javax.swing.JButton();
                jScrollPane1 = new javax.swing.JScrollPane();
                tblXepHang = new javax.swing.JTable();

                setBackground(new java.awt.Color(255, 255, 255));

                jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("BẢNG XẾP HẠNG ĐIỂM CAO");

                jPanelFilters.setBackground(new java.awt.Color(255, 255, 255));

                jLabel2.setText("Tìm theo tên:");
                jLabel3.setText("Môn học:");
                jLabel4.setText("Cấp học:");

                btnSearch.setText("Tìm kiếm / Lọc");
                btnSearch.addActionListener(e -> loadData());

                javax.swing.GroupLayout jPanelFiltersLayout = new javax.swing.GroupLayout(jPanelFilters);
                jPanelFilters.setLayout(jPanelFiltersLayout);
                jPanelFiltersLayout.setHorizontalGroup(
                                jPanelFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanelFiltersLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtSearch,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                150,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cbMonHoc,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                150,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel4)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cbCapHoc,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                150,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnSearch,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                120,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanelFiltersLayout.setVerticalGroup(
                                jPanelFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanelFiltersLayout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addGroup(jPanelFiltersLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel2)
                                                                                .addComponent(txtSearch,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel3)
                                                                                .addComponent(cbMonHoc,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel4)
                                                                                .addComponent(cbCapHoc,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnSearch,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap()));

                tblXepHang.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {},
                                new String[] {}));
                jScrollPane1.setViewportView(tblXepHang);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanelFilters,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jScrollPane1))
                                                                .addContainerGap()));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addComponent(jLabel1)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(jPanelFilters,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                450, Short.MAX_VALUE)
                                                                .addContainerGap()));
        }

        private javax.swing.JButton btnSearch;
        private javax.swing.JComboBox<String> cbCapHoc;
        private javax.swing.JComboBox<String> cbMonHoc;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JPanel jPanelFilters;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable tblXepHang;
        private javax.swing.JTextField txtSearch;
}
